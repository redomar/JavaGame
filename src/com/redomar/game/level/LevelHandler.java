package com.redomar.game.level;

import com.redomar.game.entities.Entity;
import com.redomar.game.entities.Player;
import com.redomar.game.gfx.Screen;
import com.redomar.game.level.tiles.Tile;
import com.redomar.game.lib.utils.Vector2i;
import com.redomar.game.log.PrintTypes;
import com.redomar.game.log.Printer;
import com.redomar.game.scenes.Scene;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class LevelHandler {

	private final List<Entity> entities;
	private final List<Entity> entities_p;
	private final Printer print;
	private final Comparator<Node> nodeSorter;
	private byte[] tiles;
	private int width;
	private int height;
	private String imagePath;
	private BufferedImage image;

	public LevelHandler(String imagePath) {

		if (imagePath != null) {
			this.imagePath = imagePath;
			this.loadLevelFromFile();
		} else {
			tiles = new byte[getWidth() * getHeight()];
			this.setWidth(64);
			this.setHeight(64);
			this.generateLevel();
		}

		print = new Printer();
		entities = new ArrayList<>();
		entities_p = new ArrayList<>();
		nodeSorter = Comparator.comparingDouble(n0 -> n0.fCost);
	}

	private void loadLevelFromFile() {
		try {
			this.image = ImageIO.read(Objects.requireNonNull(LevelHandler.class.getResource(this.imagePath)));
			this.setWidth(image.getWidth());
			this.setHeight(image.getHeight());
			tiles = new byte[getWidth() * getHeight()];
			this.loadTiles();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void loadTiles() {
		int[] tileColours = this.image.getRGB(0, 0, getWidth(), getHeight(), null, 0, getWidth());
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				for (Tile t : Tile.getTiles())
					if (t != null && t.getLevelColour() == tileColours[x + y * getWidth()]) {
						this.tiles[x + y * getWidth()] = t.getId();
						break;
					}
			}
		}
	}

	@SuppressWarnings("unused")
	private void saveLevelToFile() {
		try {
			ImageIO.write(image, "png", new File(Objects.requireNonNull(Level.class.getResource(this.imagePath)).getFile()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private void alterTile(int x, int y, Tile newTile) {
		this.tiles[x + y * getWidth()] = newTile.getId();
		image.setRGB(x, y, newTile.getLevelColour());
	}

	private void generateLevel() {
		for (int y = 0; y < getHeight(); y++) {
			for (int x = 0; x < getWidth(); x++) {
				if (x * y % 10 < 7) {
					tiles[x + y * getWidth()] = Tile.getGrass().getId();
				} else {
					tiles[x + y * getWidth()] = Tile.getStone().getId();
				}
			}
		}
	}

	public synchronized List<Entity> getEntities() {
		return this.entities;
	}

	public synchronized List<Entity> getProjectileEntities() {
		return this.entities_p;
	}

	public void tick() {
		for (Entity e : entities) {
			e.tick();
		}

		for (Entity e : getProjectileEntities()) {
			e.tick();
		}

		for (Tile t : Tile.getTiles()) {
			if (t == null) {
				break;
			}
			t.tick();
		}

	}

	public void renderTiles(Screen screen, int xOffset, int yOffset) {
		if (xOffset < 0) {
			xOffset = 0;
		}
		if (xOffset > ((getWidth() << 3) - screen.getWidth())) {
			xOffset = ((getWidth() << 3) - screen.getWidth());
		}
		if (yOffset < 0) {
			yOffset = 0;
		}
		if (yOffset > ((getHeight() << 3) - screen.getHeight())) {
			yOffset = ((getHeight() << 3) - screen.getHeight());
		}

		screen.setOffset(xOffset, yOffset);

		Scene scene = new Scene(xOffset, yOffset, screen, this);
		scene.playerScene();
	}

	public void renderEntities(Screen screen) {
		for (Entity e : entities) {
			e.render(screen, e.getXTile(), e.getYTile());
		}
	}

	public void renderProjectileEntities(Screen screen) {
		for (Entity e : getProjectileEntities()) {
			e.render(screen);
		}
	}

	public Tile getTile(int x, int y) {
		if (0 > x || x >= getWidth() || 0 > y || y >= getHeight()) {
			return Tile.getVoid();
		}
		return Tile.getTiles()[tiles[x + y * getWidth()]];
	}

	public void addEntity(Entity entity) {
		this.entities.add(entity);
		print.print("Added " + entity.getName() + " Entity", PrintTypes.LEVEL);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void addProjectileEntities(Entity entity) {
		this.getProjectileEntities().add(entity);
	}


	/**
	 * Removes the entity that is passed through
	 *
	 * @param entity Specifies which entity needs to be removed
	 */
	public void removeEntity(Entity entity) {
		if (entity == null) return;
		print.print("Removed " + entity.getName() + " Entity", PrintTypes.LEVEL);
		this.entities.remove(entity);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			print.print("Interruption error: " + e, PrintTypes.ERROR);
			e.printStackTrace();
		}
	}

	public void removeProjectileEntities(Entity entity) {
		this.getProjectileEntities().remove(entity);
	}

	public void removeEntity(String username) {
		int index = 0;
		for (Entity e : entities) {
			if (e instanceof Player && e.getName().equalsIgnoreCase(username)) {
				break;
			}
			index++;
		}
		this.entities.remove(index);
	}


	public List<Node> findPath(Vector2i start, Vector2i goal) {
		List<Node> openList = new ArrayList<>();
		List<Node> closedList = new ArrayList<>();
		Node current = new Node(start, null, 0, getDistance(start, goal));
		openList.add(current);
		while (openList.size() > 0) {
			openList.sort(nodeSorter);
			current = openList.get(0);
			if (current.tile.equals(goal)) {
				List<Node> path = new ArrayList<>();
				while (current.parent != null) {
					path.add(current);
					current = current.parent;
				}
				openList.clear();
				closedList.clear();
				return path;
			}
			openList.remove(current);
			closedList.add(current);
			for (int i = 0; i < 9; i++) {
				if (i == 4) continue;
				int x = current.tile.getX();
				int y = current.tile.getY();
				int xi = (i % 3) - 1;
				int yi = (i / 3) - 1;
				Tile at = getTile(x + xi, y + yi);
				if (at == null) continue;
				if (at.isSolid()) continue;
				Vector2i a = new Vector2i(x + xi, y + yi);
				double gCost = current.gCost + (getDistance(current.tile, a) == 1 ? 1 : 0.95);
				double hCost = getDistance(a, goal);
				Node node = new Node(a, current, gCost, hCost);
				if (isVectorInList(closedList, a) && gCost >= node.gCost) continue;
				if (!isVectorInList(openList, a) || gCost < node.gCost) openList.add(node);
			}
		}
		closedList.clear();
		return null;
	}

	private boolean isVectorInList(List<Node> list, Vector2i vector) {
		for (Node n : list) {
			if (n.tile.equals(vector)) return true;
		}
		return false;
	}

	private double getDistance(Vector2i tile, Vector2i goal) {
		double dx = tile.getX() - goal.getX();
		double dy = tile.getY() - goal.getY();
		return Math.sqrt(dx * dx + dy * dy);
	}

	@Deprecated
	public List<Entity> getEntities(Entity e, int radius) {
		List<Entity> result = new ArrayList<>();
		int ex = (int) e.getX();
		int ey = (int) e.getY();
		for (Entity entity : entities) {
			int x = (int) entity.getX();
			int y = (int) entity.getY();

			int dx = Math.abs(x - ex);
			int dy = Math.abs(y - ey);

			double distance = Math.sqrt((dx * 2) + (dy * 2));
			if (distance <= radius) {
				result.add(entity);
			}
		}
		return result;
	}


	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
