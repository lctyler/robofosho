package kiloboltgame;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.URL;
import java.util.ArrayList;

public class StartingClass extends Applet implements Runnable, KeyListener {

	static final long BLAST_TIME = 1500;
	private long ctrlPressed = 0, ctrlReleased = 0;
	private Robot robot;
	private Image image, currentSprite, character, characterDown,
			characterJumped, background, heliboy, blastImage;
	private Graphics second;
	private Heliboy hb1, hb2;
	private URL base;
	private static Background bg1, bg2;
	private boolean isCharging = false;

	@Override
	public void init() {

		setSize(800, 480);
		setBackground(Color.BLACK);
		setFocusable(true);
		addKeyListener(this);
		Frame frame = (Frame) this.getParent().getParent();
		frame.setTitle("Robotica Foh Shizzle");
		try {
			base = getDocumentBase();
		} catch (Exception e) {
			// TODO: handle exception
		}

		// Image Setups
		character = getImage(base, "data/character.png");
		characterDown = getImage(base, "data/down.png");
		characterJumped = getImage(base, "data/jumped.png");
		currentSprite = character;
		background = getImage(base, "data/background.png");
		heliboy = getImage(base, "data/heliboy.png");
		blastImage = getImage(base, "data/blast.png");
	}

	@Override
	public void start() {
		bg1 = new Background(0, 0);
		bg2 = new Background(2160, 0);
		robot = new Robot();
		hb1 = new Heliboy(340, 360);
		hb2 = new Heliboy(700, 360);

		Thread thread = new Thread(this);
		thread.start();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void run() {
		while (true) {
			robot.update();
			if (robot.isJumped()) {
				currentSprite = characterJumped;
			} else if (robot.isJumped() == false && robot.isDucked() == false) {
				currentSprite = character;
			}

			ArrayList<Projectile> projectiles = robot.getProjectiles();
			for (int i = 0; i < projectiles.size(); i++) {
				Projectile p = projectiles.get(i);
				if (p.isVisible() == true) {
					p.update();
				} else {
					projectiles.remove(i);
				}
			}

			hb1.update();
			hb2.update();
			bg1.update();
			bg2.update();
			repaint();
			try {
				Thread.sleep(17);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void update(Graphics g) {
		if (image == null) {
			image = createImage(this.getWidth(), this.getHeight());
			second = image.getGraphics();
		}

		second.setColor(getBackground());
		second.fillRect(0, 0, getWidth(), getHeight());
		second.setColor(getForeground());
		paint(second);

		g.drawImage(image, 0, 0, this);

	}

	@Override
	public void paint(Graphics g) {
		ArrayList<Projectile> projectiles;
		Projectile p;
		int x = 0, y = 0, yMod = 0;
		double chargeMeter = (double) (System.currentTimeMillis() - ctrlPressed) / (double) BLAST_TIME;
		System.out.println( " charge " + chargeMeter);
		g.drawImage(background, bg1.getBgX(), bg1.getBgY(), this);
		g.drawImage(background, bg2.getBgX(), bg2.getBgY(), this);

		projectiles = robot.getProjectiles();
		for (int i = 0; i < projectiles.size(); i++) {
			p = projectiles.get(i);
			if (p.isBlast()) {
				g.setColor(Color.YELLOW);

				g.fillOval(p.getX(), p.getY(), 50, 50);
			} else  {
				g.setColor(Color.YELLOW);
				g.fillRect(p.getX(), p.getY(), 10, 5);
			}

		}
		
		if (isCharging) {

			if (chargeMeter < .25) {
				x = 10;
				y = 10;
				yMod = 15;
			} else if (chargeMeter > .25 && chargeMeter < .75) {
				x = 25;
				y = 25;
				yMod = 5;
			} else if (chargeMeter >= 1.0) {
				x = 50;
				y = 50;
				yMod = 0;
			}
			g.setColor(Color.YELLOW);

			g.fillOval(robot.getCenterX() + 65, robot.getCenterY() + yMod - 50, x, y);
		}
		g.drawImage(currentSprite, robot.getCenterX() - 61,
				robot.getCenterY() - 63, this);
		g.drawImage(heliboy, hb1.getCenterX() - 48, hb1.getCenterY() - 48, this);
		g.drawImage(heliboy, hb2.getCenterX() - 48, hb2.getCenterY() - 48, this);

	}

	@Override
	public void keyPressed(KeyEvent e) {

		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			System.out.println("Move up");
			break;

		case KeyEvent.VK_DOWN:
			currentSprite = characterDown;
			if (robot.isJumped() == false) {
				robot.setDucked(true);
				robot.setSpeedX(0);
			}
			break;

		case KeyEvent.VK_LEFT:
			robot.moveLeft();
			robot.setMovingLeft(true);
			break;

		case KeyEvent.VK_RIGHT:
			robot.moveRight();
			robot.setMovingRight(true);
			break;

		case KeyEvent.VK_SPACE:
			robot.jump();
			break;

		case KeyEvent.VK_CONTROL:
			if (robot.isDucked() == false && robot.isJumped() == false) {
				if (!isCharging) {
					ctrlPressed = System.currentTimeMillis();
					isCharging = true;
				}
			}
			break;

		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			System.out.println("Stop moving up");
			break;

		case KeyEvent.VK_DOWN:
			currentSprite = character;
			robot.setDucked(false);
			break;

		case KeyEvent.VK_LEFT:
			robot.stopLeft();
			break;

		case KeyEvent.VK_RIGHT:
			robot.stopRight();
			break;

		case KeyEvent.VK_SPACE:
			break;

		case KeyEvent.VK_CONTROL:
			if (robot.isDucked() == false && robot.isJumped() == false) {

				isCharging = false;
				ctrlReleased = System.currentTimeMillis();
				if ((ctrlReleased - ctrlPressed) > BLAST_TIME) {
					System.out.println("BOOM");
					robot.blast();
				} else {

					robot.shoot();
				}

				ctrlReleased = ctrlPressed = 0;

			}
			break;

		}

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public static Background getBg1() {
		return bg1;
	}

	public static Background getBg2() {
		return bg2;
	}

}