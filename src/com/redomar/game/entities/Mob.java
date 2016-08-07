package com.redomar.game.entities;

import com.redomar.game.entities.projectiles.Medium;
import com.redomar.game.entities.projectiles.Projectile;
import com.redomar.game.entities.projectiles.Small;
import com.redomar.game.level.LevelHandler;
import com.redomar.game.level.Node;
import com.redomar.game.level.tiles.Tile;
import com.redomar.game.lib.utils.Vector2i;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Mob extends Entity {

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
	protected int ticker;
	/**
	 * [0] Contains the <strong>xMin</strong><br>
	 * [1] Contains the <strong>xMax</strong><br>
	 * [2] Contains the <strong>yMin</strong><br>
	 * [3] Contains the <strong>yMax</strong>
	 */
	protected int[] collisionBoders = new int[4];

	protected List<Projectile> projectiles = new ArrayList<Projectile>();

	public Mob(LevelHandler level, String name, int x, int y, double speed, int[] collisionBoders) {
		super(level);
		this.name = name;
		this.setX(x);
		this.setY(y);
		this.speed = speed;
		this.collisionBoders = collisionBoders;
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

		while (xa != 0){
			if (Math.abs(xa) > 1){
				if (!hasCollided(abs(xa), ya)) {
					this.x += abs(xa);
				}
				xa -= abs(xa);
			} else {
				if (!hasCollided(abs(xa), ya)) {
					this.x += xa;
				}
				xa = 0;
			}
		}

		while (ya != 0){
			if (Math.abs(ya) > 1){
				if (!hasCollided(xa, abs(ya))) {
					this.y += abs(ya);
				}
				ya -= abs(ya);
			} else {
				if (!hasCollided(xa, abs(ya))) {
					this.y += ya;
				}
				ya = 0;
			}
		}

	}

	public boolean hasCollided(double xa, double ya){
		int xMin = collisionBoders[0];
		int xMax = collisionBoders[1];
		int yMin = collisionBoders[2];
		int yMax = collisionBoders[3];

		for (int x = xMin; x < xMax; x++) {
			if (isSolid((int) xa, (int) ya, x, yMin)) {
				return true;
			}
		}

		for (int x = xMin; x < xMax; x++) {
			if (isSolid((int) xa, (int) ya, x, yMax)) {
				return true;
			}
		}

		for (int y = yMin; y < yMax; y++) {
			if (isSolid((int) xa, (int) ya, xMin, y)) {
				return true;
			}
		}

		for (int y = yMin; y < yMax; y++) {
			if (isSolid((int) xa, (int) ya, xMax, y)) {
				return true;
			}
		}

		return false;
	}

	public boolean hasCollidedAlt(int xa, int ya){
		boolean solid = false;
		for (int c = 0; c < 4; c++) {
			double xt = ((x + xa) - c % 2 * 8) / 8;
			double yt = ((y + ya) - c / 2 * 8) / 8;
			int ix = (int) Math.ceil(xt);
			int iy = (int) Math.ceil(yt);
			if (c % 2 == 0) ix = (int) Math.floor(xt);
			if (c / 2 == 0) iy = (int) Math.floor(yt);
			if(level.getTile(ix, iy).isSolid()) solid = true;
		}
		return solid;
	}

	private int abs(double i){
		if (i < 0) return -1;
		return 1;
	}

	protected boolean isSolid(int xa, int ya, int x, int y) {

		if (level == null) {
			return false;
		}

		Tile lastTile = level.getTile(((int) this.getX() + x) >> 3,
				((int) this.getY() + y) >> 3);
		Tile newtTile = level.getTile(((int) this.getX() + x + xa) >> 3, ((int) this.getY()
				+ y + ya) >> 3);

		return !lastTile.equals(newtTile) && newtTile.isSolid();

	}

	protected void aStarMovementAI(int x, int y, int px, int py, double xa,
								   double ya, double speed, Mob mob, List<Node> path, int time) {
		xa = 0;
		ya = 0;
		Vector2i start = new Vector2i(x >> 3, y >> 3);
		Vector2i goal = new Vector2i(px >> 3, py >> 3);
		path = level.findPath(start, goal);
		if(path != null) {
			if(path.size() > 0){
				Vector2i vector = path.get(path.size() - 1).tile;
				if(x < vector.getX() << 3) xa =+ speed;
				if(x > vector.getX() << 3) xa =- speed;
				if(y < vector.getY() << 3) ya =+ speed;
				if(y > vector.getY() << 3) ya =- speed;
				moveMob(xa, ya, mob);
			}
		}
	}

	protected void followMovementAI(int x, int y, int px, int py, double xa,
									double ya, double speed, Mob mob) {
		ya = 0;
		xa = 0;
		if (px > x)
			xa+=speed;
		if (px < x)
			xa-=speed;
		if (py > y)
			ya+=speed;
		if (py < y)
			ya-=speed;
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
		if(x <= 180){
			xa = 1;
			ya = -1;
		}
		double move[] = new double[2];
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

	protected void shoot(double x, double y, double dir, double buttonId, boolean secondry){
//		dir = dir * (180 /Math.PI); 
//		Printing print = new Printing();
//		print.print("Angle: "+ dir, PrintTypes.GAME);
		if(buttonId == 1){
			Projectile p = new Small(level, (int) x,(int) y, dir);
			projectiles.add(p);
			level.addProjectileEntities(p);
		} else if(buttonId == 3 && secondry == true){
			Projectile p = new Medium(level, (int) x,(int) y, dir);
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

	public boolean isMoving() {
		return this.isMoving;
	}

	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}

	public void setMovingDir(int movingDir) {
		this.movingDir = movingDir;
	}

}
