package com.redomar.game.entities;

import com.redomar.game.Game;
import com.redomar.game.entities.efx.Swim;
import com.redomar.game.entities.efx.WaterType;
import com.redomar.game.entities.projectiles.Projectile;
import com.redomar.game.entities.projectiles.Small;
import com.redomar.game.gfx.Colours;
import com.redomar.game.gfx.Screen;
import com.redomar.game.level.LevelHandler;
import com.redomar.game.level.Node;
import com.redomar.game.level.tiles.Tile;
import com.redomar.game.lib.Font;
import com.redomar.game.lib.utils.Vector2i;
import com.redomar.game.objects.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Mob extends Entity {

	protected final int shirtColour;
	protected final int faceColour;
	protected Random random = new Random();
	protected double speed;
	protected int numSteps = 0;
	protected boolean isMoving;
	protected int movingDir = 1;
	protected int scale = 1;
	protected boolean isSwimming = false;
	protected boolean isMagma = false;
	protected boolean isMuddy = false;
	protected boolean changeLevels = false;
	protected boolean showName = false;
	/**
	 * [0] Contains the <strong>xMin</strong><br>
	 * [1] Contains the <strong>xMax</strong><br>
	 * [2] Contains the <strong>yMin</strong><br>
	 * [3] Contains the <strong>yMax</strong>
	 */
	protected int[] collisionBorders;

	protected List<Projectile> projectiles = new ArrayList<>();
	protected int colour;
	protected int tickCount = 0;
	protected Swim swim;
	protected boolean[] swimType;


	public Mob(LevelHandler level, String name, int x, int y, int[] tile, double speed, int[] collisionBorders, int shirtColour, int faceColour) {
		super(level);
		this.name = name;
		this.setX(x);
		this.setY(y);
		this.setXTile(tile[0]);
		this.setYTile(tile[1]);
		this.speed = speed;
		this.collisionBorders = collisionBorders;
		this.shirtColour = shirtColour;
		this.faceColour = faceColour;
		this.colour = Colours.get(-1, 111, shirtColour, faceColour);
	}

	public void move(double xa, double ya) {
		if (xa != 0 && ya != 0) {
			move(xa, 0);
			move(0, ya);
			numSteps--;
			return;
		}
		numSteps++;

		//Moving Directions
		//0 = Facing UP
		//1 = Facing Down
		//2 = Facing Left
		//3 = Facing Right

		if (ya < 0) {
			movingDir = 0;
		}
		if (ya > 0) {
			movingDir = 1;
		}
		if (xa < 0) {
			movingDir = 2;
		}
		if (xa > 0) {
			movingDir = 3;
		}

		while (xa != 0) {
			if (Math.abs(xa) > 1) {
				if (hasCollided(abs(xa), ya)) {
					this.x += abs(xa);
				}
				xa -= abs(xa);
			} else {
				if (hasCollided(abs(xa), ya)) {
					this.x += xa;
				}
				xa = 0;
			}
		}

		while (ya != 0) {
			if (Math.abs(ya) > 1) {
				if (hasCollided(xa, abs(ya))) {
					this.y += abs(ya);
				}
				ya -= abs(ya);
			} else {
				if (hasCollided(xa, abs(ya))) {
					this.y += ya;
				}
				ya = 0;
			}
		}

	}

	public boolean hasCollided(double xa, double ya) {
		int xMin = collisionBorders[0];
		int xMax = collisionBorders[1];
		int yMin = collisionBorders[2];
		int yMax = collisionBorders[3];

		for (int x = xMin; x < xMax; x++) {
			if (isSolid((int) xa, (int) ya, x, yMin)) {
				return false;
			}
			if (isSolid((int) xa, (int) ya, x, yMax)) {
				return false;
			}
		}

		for (int y = yMin; y < yMax; y++) {
			if (isSolid((int) xa, (int) ya, xMin, y)) {
				return false;
			}
			if (isSolid((int) xa, (int) ya, xMax, y)) {
				return false;
			}
		}

		return true;
	}

	@Deprecated
	private int abs(double i) {
		if (i < 0) return -1;
		return 1;
	}

	protected boolean isSolid(int xa, int ya, int x, int y) {

		if (level == null) {
			return false;
		}

		Tile lastTile = level.getTile(((int) this.getX() + x) >> 3, ((int) this.getY() + y) >> 3);
		Tile newtTile = level.getTile(((int) this.getX() + x + xa) >> 3, ((int) this.getY() + y + ya) >> 3);

		return !lastTile.equals(newtTile) && newtTile.isSolid();

	}

	protected void aStarMovementAI(int x, int y, int px, int py, double speed, Mob mob) {
		double xa = 0;
		double ya = 0;
		Vector2i start = new Vector2i(x >> 3, y >> 3);
		Vector2i goal = new Vector2i(px >> 3, py >> 3);
		List<Node> path = level.findPath(start, goal);
		if (path != null) {
			if (path.size() > 0) {
				Vector2i vector = path.get(path.size() - 1).tile;
				if (x < vector.getX() << 3) xa = speed;
				if (x > vector.getX() << 3) xa = -speed;
				if (y < vector.getY() << 3) ya = speed;
				if (y > vector.getY() << 3) ya = -speed;
				moveMob(xa, ya, mob);
			}
		}
	}

	@SuppressWarnings("unused")
	protected void followMovementAI(int x, int y, int px, int py, double speed, Mob mob) {
		double ya = 0;
		double xa = 0;
		if (px > x) xa += speed;
		if (px < x) xa -= speed;
		if (py > y) ya += speed;
		if (py < y) ya -= speed;
		moveMob(xa, ya, mob);
	}

	protected double[] randomMovementAI(double x, double y, double xa, double ya, int tick) {
		if (tick % (random.nextInt(50) + 30) == 0) {
			xa = random.nextInt(3) - 1;
			ya = random.nextInt(3) - 1;
			if (random.nextInt(4) == 0) {
				xa = 0;
				ya = 0;
			}
		}
		if (x <= 180) {
			xa = 1;
		}
		if (y <= 180) {
			ya = -1;
		}
		double[] move = new double[2];
		move[0] = xa;
		move[1] = ya;
		return move;
	}

	protected void moveMob(double xa, double ya, Mob mob) {
		if (xa != 0 || ya != 0) {
			mob.move(xa, ya);
			mob.isMoving = true;
		} else {
			mob.isMoving = false;
		}
	}

	protected void shoot(double x, double y, double dir, double buttonId) {
		if (buttonId == 1) {
			Projectile p = new Small(level, (int) x, (int) y, dir);
			projectiles.add(p);
			level.addProjectileEntities(p);
		}
	}

	public String getName() {
		return name;
	}

	public int getNumSteps() {
		return numSteps;
	}

	public void setNumSteps(int numSteps) {
		this.numSteps = numSteps;
	}

	public void setSwim(Swim swim) {
		this.swim = swim;
	}

	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	public void setMovingDir(int movingDir) {
		this.movingDir = movingDir;
	}

	@Override
	public void render(Screen screen) {
		int xTile = getXTile();
		int yTile = getYTile();
		int modifier = 8 * scale;
		int xOffset = (int) getX() - modifier / 2;
		int yOffset = (int) getY() - modifier / 2 - 4;
		int walkingSpeed = 4;
		int flipTop = (numSteps >> walkingSpeed) & 1;
		int flipBottom = (numSteps >> walkingSpeed) & 1;

		Inventory.activate();

		if (movingDir == 1) {
			xTile += 2;
			if (!isMoving || swim.isActive(swimType)) {
				yTile -= 2;
			}
		} else if (movingDir == 0 && !isMoving || movingDir == 0 && swim.isActive(swimType)) {
			yTile -= 2;
		} else if (movingDir > 1) {
			xTile += 4 + ((numSteps >> walkingSpeed) & 1) * 2;
			flipTop = (movingDir - 1) % 2;
			if (!isMoving) {
				xTile = 4;
			}
		}

		if (changeLevels) {
			Game.setChangeLevel(true);
		}

		WaterType type = isSwimming ? WaterType.SWIMMING : isMagma ? WaterType.MAGMA : isMuddy ? WaterType.SWAMP : null;
		if (type != null) {
			int[] swimColour = swim.waveCols(type);

			int waterColour;
			yOffset += 4;

			colour = Colours.get(-1, 111, -1, faceColour);

			if (tickCount % 60 < 15) {
				waterColour = Colours.get(-1, -1, swimColour[0], -1);
			} else if (tickCount % 60 < 30) {
				yOffset--;
				waterColour = Colours.get(-1, swimColour[1], swimColour[2], -1);
			} else if (tickCount % 60 < 45) {
				waterColour = Colours.get(-1, swimColour[2], -1, swimColour[1]);
			} else {
				yOffset--;
				waterColour = Colours.get(-1, -1, swimColour[1], swimColour[2]);
			}

			screen.render(xOffset, yOffset + 3, 31 + 31 * 32, waterColour, 0x00, 1);
			screen.render(xOffset + 8, yOffset + 3, 31 + 31 * 32, waterColour, 0x01, 1);
		}

		screen.render((xOffset + (modifier * flipTop)), yOffset, (xTile + yTile * 32), colour, flipTop, scale);
		screen.render((xOffset + modifier - (modifier * flipTop)), yOffset, ((xTile + 1) + yTile * 32), colour, flipTop, scale);
		if (!isSwimming && !isMagma && !isMuddy) {
			screen.render((xOffset + (modifier * flipBottom)), (yOffset + modifier), (xTile + (yTile + 1) * 32), colour, flipBottom, scale);
			screen.render((xOffset + modifier - (modifier * flipBottom)), (yOffset + modifier), ((xTile + 1) + (yTile + 1) * 32), colour, flipBottom, scale);
			colour = Colours.get(-1, 111, shirtColour, faceColour);
		}

		if (name != null && showName) {
			/*
			 * Improved name centering above player's sprite.
			 * Using player's own x value cast to int with an adjusted formula
			 * -posmicanomaly
			 */

			int fontCharSize = 8;
			int offsetUnit = ((name.length() & 1) == 0 ? fontCharSize / 2 : 0);
			int nameOffset = (name.length() / 2) * fontCharSize - offsetUnit;
			Font.render(name, screen, (int) x - nameOffset, yOffset - 10, Colours.get(-1, -1, -1, 111), 1);

		}
	}

	public void render(Screen screen, int xTile, int yTile) {
		setXTile(xTile);
		setYTile(yTile);
		render(screen);
	}
}
