package kiloboltgame;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import kiloboltgame.framework.Animation;

public class StartingClass extends Applet implements Runnable, KeyListener {

	static final long BLAST_TIME = 1500;
	private long ctrlPressed = 0, ctrlReleased = 0, spPressed = 0,
			spReleased = 0, currentTime = 0;
	private Robot robot;
	private Image image, currentSprite, character, character2, character3,
			characterDown, characterJumped, background, heliboy, heliboy2,
			heliboy3, heliboy4, heliboy5;
	private Graphics second;
	private Heliboy hb1, hb2;
	private URL base;
	private static Background bg1, bg2;
	private boolean isCharging = false, isWalking = false, rocketSpace = false,
			rocketTimer = false, jumpDisabled = false;
	private Animation anim, hanim;
	public static Image tilegrassTop, tilegrassBot, tilegrassLeft,
			tilegrassRight, tiledirt;
	private ArrayList<Tile> tilearray = new ArrayList<Tile>();

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
		character = getImage(base, "data/characterStill.png");
		character2 = getImage(base, "data/characterWalking2.png");
		// character3 = getImage(base, "data/characterWalking.png");

		characterDown = getImage(base, "data/down.png");
		characterJumped = getImage(base, "data/jumped.png");

		heliboy = getImage(base, "data/heliboy.png");
		heliboy2 = getImage(base, "data/heliboy2.png");
		heliboy3 = getImage(base, "data/heliboy3.png");
		heliboy4 = getImage(base, "data/heliboy4.png");
		heliboy5 = getImage(base, "data/heliboy5.png");

		background = getImage(base, "data/background.png");

		tiledirt = getImage(base, "data/tiledirt.png");
		tilegrassTop = getImage(base, "data/tilegrasstop.png");
		tilegrassBot = getImage(base, "data/tilegrassbot.png");
		tilegrassLeft = getImage(base, "data/tilegrassleft.png");
		tilegrassRight = getImage(base, "data/tilegrassright.png");

		anim = new Animation();
		anim.addFrame(character, 1250);
		anim.addFrame(character2, 1250);
		// anim.addFrame(character3, 1250);
		// anim.addFrame(character2, 1250);
		// anim.addFrame(character, 1250);

		hanim = new Animation();
		hanim.addFrame(heliboy, 100);
		hanim.addFrame(heliboy2, 100);
		hanim.addFrame(heliboy3, 100);
		hanim.addFrame(heliboy4, 100);
		hanim.addFrame(heliboy5, 100);
		hanim.addFrame(heliboy4, 100);
		hanim.addFrame(heliboy3, 100);
		hanim.addFrame(heliboy2, 100);

		currentSprite = anim.getImage();

	}

	@Override
	public void start() {
		bg1 = new Background(0, 0);
		bg2 = new Background(2160, 0);

		// Paints the tiles over the background

		try {
			loadMap("data/map1.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		robot = new Robot();
		hb1 = new Heliboy(340, 360);
		hb2 = new Heliboy(700, 360);

		Thread thread = new Thread(this);
		thread.start();
	}

	private void loadMap(String filename) throws IOException {
		ArrayList lines = new ArrayList();
		int width = 0;
		int height = 0;

		BufferedReader reader = new BufferedReader(new FileReader(filename));
		String line = null;
		while ((line = reader.readLine()) != null) {
			if (!line.startsWith("!")) {
				lines.add(line);
				width = Math.max(width, line.length());
			}
		}
		reader.close();

		/*
		 * while (true) { String line = reader.readLine(); // no more lines to
		 * read if (line == null) { reader.close(); break; }
		 * 
		 * if (!line.startsWith("!")) { lines.add(line); width = Math.max(width,
		 * line.length());
		 * 
		 * } }
		 */
		height = lines.size();

		for (int j = 0; j < 12; j++) {
			line = (String) lines.get(j);
			for (int i = 0; i < width; i++) {
				System.out.println(i + "is i ");

				if (i < line.length()) {
					char ch = line.charAt(i);
					Tile t = new Tile(i, j, Character.getNumericValue(ch));
					tilearray.add(t);
				}

			}
		}

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void animate() {
		if (isWalking && robot.isJumped() != true && robot.isJetPack() != true)
			anim.update(100);

		hanim.update(50);
	}

	@Override
	public void run() {
		while (true) {
			robot.update();
			System.out.println(robot.getCenterY());
			if (robot.isJumped()) {
				currentSprite = characterJumped;
			} else if (robot.isJumped() == false && robot.isDucked() == false) {
				currentSprite = anim.getImage();
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
			updateTiles();
			hb1.update();
			hb2.update();
			bg1.update();
			bg2.update();

			animate();
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
		double chargeMeter = (double) (System.currentTimeMillis() - ctrlPressed)
				/ (double) BLAST_TIME;
		// System.out.println(" charge " + chargeMeter);
		g.drawImage(background, bg1.getBgX(), bg1.getBgY(), this);
		g.drawImage(background, bg2.getBgX(), bg2.getBgY(), this);
		paintTiles(g);

		projectiles = robot.getProjectiles();
		for (int i = 0; i < projectiles.size(); i++) {
			p = projectiles.get(i);
			if (p.isBlast()) {
				g.setColor(Color.YELLOW);

				g.fillOval(p.getX(), p.getY(), 50, 50);
			} else {
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

			g.fillOval(robot.getCenterX() + 85, robot.getCenterY() + yMod, x, y);
		}
		g.drawImage(currentSprite, robot.getCenterX() - 61,
				robot.getCenterY() - 63, this);
		g.drawImage(hanim.getImage(), hb1.getCenterX() - 48,
				hb1.getCenterY() - 48, this);
		g.drawImage(hanim.getImage(), hb2.getCenterX() - 48,
				hb2.getCenterY() - 48, this);

	}

	private void updateTiles() {

		for (int i = 0; i < tilearray.size(); i++) {
			Tile t = (Tile) tilearray.get(i);
			t.update();
		}

	}

	private void paintTiles(Graphics g) {
		for (int i = 0; i < tilearray.size(); i++) {
			Tile t = (Tile) tilearray.get(i);
			g.drawImage(t.getTileImage(), t.getTileX(), t.getTileY(), this);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {

		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			rocketSpace = true;
			if (robot.isJetPack())
				robot.jumpPack();
			break;

		/*
		 * case KeyEvent.VK_DOWN: currentSprite = characterDown; if
		 * (robot.isJumped() == false) { robot.setDucked(true);
		 * robot.setSpeedX(0); } break;
		 */

		case KeyEvent.VK_LEFT:
			robot.moveLeft();
			robot.setMovingLeft(true);
			break;

		case KeyEvent.VK_RIGHT:
			isWalking = true;
			robot.moveRight();
			robot.setMovingRight(true);
			break;

		case KeyEvent.VK_SHIFT:

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
			robot.setJetPack(true);
			rocketSpace = false;
			//robot.setSpeedY(1);
			//Robot.setJumpMeter(0);
			break;

		/*
		 * case KeyEvent.VK_DOWN: //currentSprite = anim.getImage();
		 * robot.setDucked(false); break;
		 */

		case KeyEvent.VK_SHIFT:

			break;

		case KeyEvent.VK_LEFT:
			robot.stopLeft();
			break;

		case KeyEvent.VK_RIGHT:
			isWalking = false;
			robot.stopRight();
			break;

		case KeyEvent.VK_SPACE:
			jumpDisabled = false;
			spReleased = System.currentTimeMillis();
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