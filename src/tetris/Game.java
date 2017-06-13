package tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JPanel {
	private static final long serialVersionUID = -1156941986803691069L;
	private static final int ROWS = 20;
	private static final int COLS = 10;
	private static final int[] SCORE_TABLE = { 0, 10, 30, 100, 200 };
	private static final int RUNNING = 0;
	private static final int PAUSE = 1;
	private static final int GAME_OVER = 2;
	private static final int ROTATE_LEFT = -1;
	private static final int ROTATE_RIGHT = 1;

	public static BufferedImage backGround;
	public static BufferedImage gameOver;
	public static BufferedImage pause;
	public static BufferedImage I;
	public static BufferedImage T;
	public static BufferedImage S;
	public static BufferedImage J;
	public static BufferedImage L;
	public static BufferedImage Z;
	public static BufferedImage O;

	private Cell[][] wall = new Cell[ROWS][COLS];
	private Tetromino tetromino;
	private Tetromino nextOne;
	private int level = 0;
	private int index = 0;
	private int lines = 0;
	private int score = 0;
	private int state = RUNNING;
	
	static {
		try {
			backGround = ImageIO.read(Game.class.getResource("/img/tetris.png"));
			gameOver = ImageIO.read(Game.class.getResource("/img/game-over.png"));
			pause = ImageIO.read(Game.class.getResource("/img/pause.png"));
			T = ImageIO.read(Game.class.getResource("/img/T.png"));
			S = ImageIO.read(Game.class.getResource("/img/S.png"));
			J = ImageIO.read(Game.class.getResource("/img/J.png"));
			L = ImageIO.read(Game.class.getResource("/img/L.png"));
			Z = ImageIO.read(Game.class.getResource("/img/Z.png"));
			I = ImageIO.read(Game.class.getResource("/img/I.png"));
			O = ImageIO.read(Game.class.getResource("/img/O.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 游戏开始的方法
	public void action() {
		tetromino = Tetromino.randomOne();
		nextOne = Tetromino.randomOne();
		KeyListener l = new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				switch (state) {
				case RUNNING:
					handleRunningKeys(key);
					break;
				case PAUSE:
					handlePauseKeys(key);
					break;
				case GAME_OVER:
					handleGameOverKeys(key);
					break;
				}
				repaint();
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		};
		this.addKeyListener(l);
		this.setFocusable(true);
		this.requestFocus();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				index++;
				level = score / 1000 + 1;
				level = level > 20 ? 20 : level;
				int speed = 21 - level;
				if (index % speed == 0) {
					if (state == RUNNING) {
						softDropAction();
					}
				}
				repaint();
			}
		}, 20, 20);
	}

	private void handleRunningKeys(int key) {
		switch (key) {
		case KeyEvent.VK_LEFT:
			moveLeftAction();
			break;
		case KeyEvent.VK_RIGHT:
			moveRightAction();
			break;
		case KeyEvent.VK_UP:
			rotateRightAction();
			break;
		case KeyEvent.VK_DOWN:
			softDropAction();
			break;
		case KeyEvent.VK_SPACE:
			hardDropAction();
			break;
		case KeyEvent.VK_Z:
			rotateLeftAction();
			break;
		case KeyEvent.VK_P:
			state = PAUSE;
			break;
		case KeyEvent.VK_Q:
			System.exit(0);
		}
	}

	private void handlePauseKeys(int key) {
		switch (key) {
		case KeyEvent.VK_C:
			state = RUNNING;
			break;
		case KeyEvent.VK_Q:
			System.exit(0);
		}
	}

	private void handleGameOverKeys(int key) {
		switch (key) {
		case KeyEvent.VK_S:
			score = 0;
			lines = 0;
			index = 0;
			wall = new Cell[ROWS][COLS];
			tetromino = Tetromino.randomOne();
			nextOne = Tetromino.randomOne();
			state = RUNNING;
			break;
		case KeyEvent.VK_Q:
			System.exit(0);
		}
	}

	// 重写paint方法
	public void paint(Graphics g) {
		g.drawImage(backGround, 0, 0, null);
		g.translate(15, 15);
		paintWall(g);
		paintTetromino(g);
		paintNextOne(g);
		paintScore(g);
		paintState(g);
	}

	// 画墙
	public void paintWall(Graphics g) {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				int x = col * 26;
				int y = row * 26;
				// g.drawRect(x, y, 26, 26);
				if (wall[row][col] != null) {
					Cell c = wall[row][col];
					g.drawImage(c.getImage(), x, y, null);
				}
			}
		}
	}

	// 画正在下落的方块
	public void paintTetromino(Graphics g) {
		if (tetromino == null) {
			return;
		}
		Cell[] cells = tetromino.cells;
		for (Cell c : cells) {
			int x = c.getCol() * 26;
			int y = c.getRow() * 26;
			g.drawImage(c.getImage(), x, y, null);
		}
	}

	// 画下一个方块
	public void paintNextOne(Graphics g) {
		if (nextOne == null) {
			return;
		}
		Cell[] cells = nextOne.cells;
		for (Cell c : cells) {
			int x = (c.getCol() + 10) * 26;
			int y = (c.getRow() + 1) * 26;
			g.drawImage(c.getImage(), x, y, null);
		}
	}

	// 画分数、行数、等级
	public void paintScore(Graphics g) {
		Font font = new Font(Font.SANS_SERIF, Font.BOLD, 28);
		g.setFont(font);
		g.setColor(new Color(0x667799));
		g.drawString("SCORE:" + score, 295, 165);
		g.drawString("LINES:" + lines, 295, 220);
		g.drawString("LEVEL:" + level, 295, 275);
	}

	// 画状态
	public void paintState(Graphics g) {
		switch (state) {
		case PAUSE:
			g.drawImage(pause, -15, -15, null);
			break;
		case GAME_OVER:
			g.drawImage(gameOver, -15, -15, null);
			break;
		}
	}

	// 下落一格
	public void softDropAction() {
		if (canDrop()) {
			tetromino.softDrop();
		} else {
			landToWall();
			int line = destroyLines();
			score += SCORE_TABLE[line];
			lines += line;
			if (gameOver()) {
				state = GAME_OVER;
			} else {
				tetromino = nextOne;
				nextOne = Tetromino.randomOne();
			}
		}
	}

	// 下落到底
	public void hardDropAction() {
		while (canDrop()) {
			tetromino.softDrop();
		}
		landToWall();
		int line = destroyLines();
		score += SCORE_TABLE[line];
		lines += line;
		if (gameOver()) {
			state = GAME_OVER;
		} else {
			tetromino = nextOne;
			nextOne = Tetromino.randomOne();
		}
	}

	// 左移一格
	public void moveLeftAction() {
		tetromino.moveLeft();
		if (outOfBounds() || coincide()) {
			tetromino.moveRight();
		}
	}

	// 右移一格
	public void moveRightAction() {
		tetromino.moveRight();
		if (outOfBounds() || coincide()) {
			tetromino.moveLeft();
		}
	}

	// 检查能否下落
	private boolean canDrop() {
		Cell[] cells = tetromino.cells;
		for (Cell c : cells) {
			int row = c.getRow();
			int col = c.getCol();
			if (row == ROWS - 1 || wall[row + 1][col] != null) {
				return false;
			}
		}
		return true;
	}

	// 检查是否出界
	private boolean outOfBounds() {
		Cell[] cells = tetromino.cells;
		for (Cell c : cells) {
			if (c.getCol() < 0 || c.getCol() >= COLS || c.getRow() < 0 || c.getRow() >= ROWS) {
				return true;
			}
		}
		return false;
	}

	// 检查是否重合
	private boolean coincide() {
		Cell[] cells = tetromino.cells;
		for (Cell c : cells) {
			int row = c.getRow();
			int col = c.getCol();
			if (wall[row][col] != null) {
				return true;
			}
		}
		return false;
	}

	// 着墙
	private void landToWall() {
		Cell[] cells = tetromino.cells;
		for (Cell c : cells) {
			int row = c.getRow();
			int col = c.getCol();
//			if(row>9){
//				c.setImage(null);
//			}
			wall[row][col] = c;
		}
	}

	// 右旋转
	private void rotateRightAction() {
		String name=tetromino.getClass().getSimpleName();
		if("T".equals(name)||"J".equals(name)||"L".equals(name)){
			tetromino.rotate(ROTATE_RIGHT);
			if (outOfBounds() || coincide()) {
				tetromino.rotate(ROTATE_LEFT);
			}
		}else if("Z".equals(name)||"S".equals(name)||"I".equals(name)){
			Cell[] cells = tetromino.cells;
			int row;
			if("Z".equals(name)){
				row=cells[3].getRow()-cells[0].getRow();
			}else{
				row=cells[1].getRow()-cells[0].getRow();
			}
			if(row==0){
				tetromino.rotate(ROTATE_RIGHT);
				if (outOfBounds() || coincide()) {
					tetromino.rotate(ROTATE_LEFT);
				}
			}else{
				tetromino.rotate(ROTATE_LEFT);
				if (outOfBounds() || coincide()) {
					tetromino.rotate(ROTATE_RIGHT);
				}
			}
		}
	}

	// 左旋转
	private void rotateLeftAction() {
		String name=tetromino.getClass().getSimpleName();
		if("T".equals(name)||"J".equals(name)||"L".equals(name)){
			tetromino.rotate(ROTATE_LEFT);
			if (outOfBounds() || coincide()) {
				tetromino.rotate(ROTATE_RIGHT);
			}
		}else if("Z".equals(name)||"S".equals(name)||"I".equals(name)){
			Cell[] cells = tetromino.cells;
			int row;
			if("Z".equals(name)){
				row=cells[3].getRow()-cells[0].getRow();
			}else{
				row=cells[1].getRow()-cells[0].getRow();
			}
			if(row==0){
				tetromino.rotate(ROTATE_LEFT);
				if (outOfBounds() || coincide()) {
					tetromino.rotate(ROTATE_RIGHT);
				}
			}else{
				tetromino.rotate(ROTATE_RIGHT);
				if (outOfBounds() || coincide()) {
					tetromino.rotate(ROTATE_LEFT);
				}
			}
		}
	}

	// 消行
	private int destroyLines() {
		int line = 0;
		for (int row = 0; row < ROWS; row++) {
			if (fullLine(row)) {
				for (int i = row; i > 0; i--) {
					wall[i] = Arrays.copyOf(wall[i - 1], COLS);
				}
				wall[0] = new Cell[COLS];
				line++;
			}
		}
		return line;
	}

	// 检查是否满行
	private boolean fullLine(int row) {
		for (int col = 0; col < COLS; col++) {
			if (wall[row][col] == null) {
				return false;
			}
		}
		return true;
	}

	// 检查游戏是否结束
	private boolean gameOver() {
		Cell[] cells = nextOne.cells;
		for (Cell c : cells) {
			int row = c.getRow();
			int col = c.getCol();
			if (wall[row][col] != null) {
				return true;
			}
		}
		return false;
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Game game = new Game();
		frame.add(game);
		frame.setSize(530, 575);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		game.action();
	}
}
