package kiloboltgame;

import java.util.ArrayList;

public class Robot {

	// Constants are Here
	final int JUMPSPEED = -15;
	final int MOVESPEED = 5;
	final int GROUND = 334;

	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	private int centerX = 100;
	private int centerY = GROUND;
	private boolean jumped = false, jetPack = false, isFalling = false;
	private boolean movingLeft = false;
	private boolean movingRight = false;
	private boolean ducked = false;
	private static double jumpMeter = 0;
	private int speedX = 0;
	private int speedY = 1;

	public void setIsFalling(boolean input) {
		isFalling = input;
	}

	public static double getJumpMeter() {
		return jumpMeter;
	}

	public static void setJumpMeter(double jumpMeter) {
		Robot.jumpMeter = jumpMeter;
	}

	private Background bg1 = StartingClass.getBg1();
	private Background bg2 = StartingClass.getBg2();

	public void update() {
		// Moves Character or Scrolls Background accordingly.

		if (speedX < 0) {
			centerX += speedX;
		}
		if (speedX == 0 || speedX < 0) {
			bg1.setSpeedX(0);
			bg2.setSpeedX(0);

		}
		if (centerX <= 200 && speedX > 0) {
			centerX += speedX;
		}
		if (speedX > 0 && centerX > 200) {
			bg1.setSpeedX(-MOVESPEED / 5);
			bg2.setSpeedX(-MOVESPEED / 5);
		}

		// Updates Y Position
		centerY += speedY;
		if (centerY + speedY >= GROUND) {
			centerY = GROUND;
		}

		// Handles Jumping
		if (jumped == true || isFalling == true) {
			speedY += 1;

			if (centerY + speedY >= GROUND) {
				centerY = GROUND;
				speedY = 0;
				jumped = false;
				isFalling = false;
			}

		}

		// Prevents going beyond X coordinate of 0
		if (centerX + speedX <= 60) {
			centerX = 61;
		}
	}

	public boolean isJetPack() {
		return jetPack;
	}

	public void setJetPack(boolean jetPack) {
		this.jetPack = jetPack;
	}

	public void shoot() {
		Projectile p = new Projectile(centerX + 75, centerY + 25, 7, false);
		this.projectiles.add(p);
	}

	public void blast() {
		Projectile p = new Projectile(centerX + 90, centerY + 10, 3, true);
		this.projectiles.add(p);
	}

	public void moveRight() {
		if (ducked == false) {
			speedX = MOVESPEED;
		}
	}

	public void moveLeft() {
		if (ducked == false) {
			speedX = -MOVESPEED;
		}
	}

	public void stopRight() {
		setMovingRight(false);
		stop();
	}

	public void stopLeft() {
		setMovingLeft(false);
		stop();
	}

	private void stop() {
		if (isMovingRight() == false && isMovingLeft() == false) {
			speedX = 0;
		}

		if (isMovingRight() == false && isMovingLeft() == true) {
			moveLeft();
		}

		if (isMovingRight() == true && isMovingLeft() == false) {
			moveRight();
		}

	}

	public void jump() {
		if (jumped == false) {
			speedY = JUMPSPEED;
			jumped = true;
		}

	}

	public boolean isFalling() {
		return isFalling;
	}

	public void setFalling(boolean isFalling) {
		this.isFalling = isFalling;
	}

	// FIX ME
	public void jumpPack() {
		if (!isFalling) {
			jumpMeter += 0.01;
			if (jumpMeter >= 0 && jumpMeter <= 0.35) {
				//System.out.println("wat and speed is "  + speedY);
				if (speedY > -5)
					speedY += -1;
			jetPack = true;
			} else {
				this.jetPack = false;
				jumpMeter = 0;
				isFalling = true;

			}
		}

	}

	public int getCenterX() {
		return centerX;
	}

	public int getCenterY() {
		return centerY;
	}

	public boolean isJumped() {
		return jumped;
	}

	public int getSpeedX() {
		return speedX;
	}

	public int getSpeedY() {
		return speedY;
	}

	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}

	public void setJumped(boolean jumped) {
		this.jumped = jumped;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}

	public boolean isDucked() {
		return ducked;
	}

	public void setDucked(boolean ducked) {
		this.ducked = ducked;
	}

	public boolean isMovingRight() {
		return movingRight;
	}

	public void setMovingRight(boolean movingRight) {
		this.movingRight = movingRight;
	}

	public boolean isMovingLeft() {
		return movingLeft;
	}

	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	public void setProjectiles(ArrayList<Projectile> projectiles) {
		this.projectiles = projectiles;
	}

	public void setMovingLeft(boolean movingLeft) {
		this.movingLeft = movingLeft;
	}

}
