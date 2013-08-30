package kiloboltgame;

public class Projectile {
	static final int BULLET_SPEED = 7;
	private int x, y, speedX;
	private boolean visible, isBlast;
	
	public Projectile(int startX, int startY, int speedX, boolean isBlast) {
		x = startX; 
		y = startY; 
		this.speedX = speedX; 
		visible = true; 
		this.isBlast = isBlast;
	}
	
	/**
	 * Sets the bullet to move across the screen, makes the bullet dissapear if it goes off screen 
	 */
	public void update() {
		x += speedX;
		if (x > 800) {
		   visible = false;
		}
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getSpeedX() {
		return speedX;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public static int getBulletSpeed() {
		return BULLET_SPEED;
	}

	public boolean isBlast() {
		return isBlast;
	}

	public void setBlast(boolean isBlast) {
		this.isBlast = isBlast;
	}
	
	

}
