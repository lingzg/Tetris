package tetris;

public class Tetromino {
	protected Cell[] cells = new Cell[4];

	private Tetromino() {
	}

	public static Tetromino randomOne() {
		int type = (int) (Math.random() * 7);
		switch (type) {
		case 0:
			return new T();
		case 1:
			return new S();
		case 2:
			return new J();
		case 3:
			return new L();
		case 4:
			return new Z();
		case 5:
			return new I();
		default:
			return new O();
		}
	}

	private static class T extends Tetromino {
		public T() {
			cells[0] = new Cell(0, 4, Game.T);
			cells[1] = new Cell(0, 3, Game.T);
			cells[2] = new Cell(0, 5, Game.T);
			cells[3] = new Cell(1, 4, Game.T);
		}
	}

	private static class S extends Tetromino {
		public S() {
			cells[0] = new Cell(0, 4, Game.S);
			cells[1] = new Cell(0, 5, Game.S);
			cells[2] = new Cell(1, 3, Game.S);
			cells[3] = new Cell(1, 4, Game.S);
		}
	}

	private static class J extends Tetromino {
		public J() {
			cells[0] = new Cell(0, 4, Game.J);
			cells[1] = new Cell(0, 3, Game.J);
			cells[2] = new Cell(0, 5, Game.J);
			cells[3] = new Cell(1, 5, Game.J);
		}
	}

	private static class L extends Tetromino {
		public L() {
			cells[0] = new Cell(0, 4, Game.L);
			cells[1] = new Cell(0, 3, Game.L);
			cells[2] = new Cell(0, 5, Game.L);
			cells[3] = new Cell(1, 3, Game.L);
		}
	}

	private static class Z extends Tetromino {
		public Z() {
			cells[0] = new Cell(1, 4, Game.Z);
			cells[1] = new Cell(0, 3, Game.Z);
			cells[2] = new Cell(0, 4, Game.Z);
			cells[3] = new Cell(1, 5, Game.Z);
		}
	}

	private static class I extends Tetromino {
		public I() {
			cells[0] = new Cell(0, 4, Game.I);
			cells[1] = new Cell(0, 3, Game.I);
			cells[2] = new Cell(0, 5, Game.I);
			cells[3] = new Cell(0, 6, Game.I);
		}
	}

	private static class O extends Tetromino {
		public O() {
			cells[0] = new Cell(0, 4, Game.O);
			cells[1] = new Cell(0, 5, Game.O);
			cells[2] = new Cell(1, 4, Game.O);
			cells[3] = new Cell(1, 5, Game.O);
		}
	}

	public void softDrop() {
		for (Cell c : cells) {
			c.drop();
		}
	}

	public void moveLeft() {
		for (Cell c : cells) {
			c.moveLeft();
		}
	}

	public void moveRight() {
		for (Cell c : cells) {
			c.moveRight();
		}
	}

	public void rotate(int angle) {
		if (angle == 1 || angle == -1) {
			int row = cells[0].getRow();
			int col = cells[0].getCol();
			for (Cell c : cells) {
				int x = c.getRow() - row;
				int y = c.getCol() - col;
				c.setRow(y * angle + row);
				c.setCol(-x * angle + col);
			}
		}
	}
}
