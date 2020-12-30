package mines;

import java.util.Random;

public class Mines {

	private Board[][] board;
	private int height;
	private int width;
	private int numMines;
	private boolean showAll;

	// Class that symbolized one cube in board game
	public class Board {
		private boolean board;
		private boolean flags;
		private boolean mines;

		public boolean isOpen() {
			return board;
		}

		public void setOpen(boolean open) {
			this.board = open;
		}

		public boolean isFlags() {
			return flags;
		}

		public void setFlags(boolean flags) {
			this.flags = flags;
		}

		public boolean isMines() {
			return mines;
		}

		public void setMines(boolean mines) {
			this.mines = mines;
		}

	}

	public Mines(int height, int width, int numMines) {
		board = new Board[height][width];
		this.height = height;
		this.width = width;
		this.numMines = numMines;

		// Creating all the board instances
		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				board[i][j] = new Board();

		Random r = new Random();
		// Place mines on the board randomly
		for (int i = 0; i < numMines; i++)
			board[r.nextInt(height)][r.nextInt(width)].setMines(true);
	}

	public boolean isShowAll() {
		return showAll;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	// add mine to the board in place i,j
	public boolean addMine(int i, int j) {

		if (board[i][j].isMines())
			return false;

		if (board[i][j].isOpen())
			return false;

		board[i][j].setMines(true);
		numMines++;
		return true;
	}

	public boolean open(int i, int j) {
		boolean[][] visited = new boolean[height][width]; // boolean matrix that simulated the nodes that were visted

		if (board[i][j].isMines())
			return false;

		return checkNeighbors(i, j, visited);
	}

	// Function that check if all of (i,j) node neighbors are not mines,and if they
	// are not its call recursively to this function again.
	private boolean checkNeighbors(int i, int j, boolean[][] visited) {

		// Check for height border of the board
		if (i >= height || i < 0)
			return false;

		// Check for width border of the board
		if (j >= width || j < 0)
			return false;

		// Mark (i,j) if it's visited already
		if (visited[i][j] == true)
			return false;
		else
			visited[i][j] = true;

		board[i][j].setOpen(true); // Set (i,j) to be open

		if (board[i][j].isMines()) // Check if (i,j) is mine
			return false;

		// If all of the neighbors are not mine visit all of them recursively
		if (numOfMines(i, j) == 0) {
			checkNeighbors(i + 1, j, visited);
			checkNeighbors(i - 1, j, visited);
			checkNeighbors(i, j + 1, visited);
			checkNeighbors(i, j - 1, visited);
			checkNeighbors(i + 1, j + 1, visited);
			checkNeighbors(i + 1, j - 1, visited);
			checkNeighbors(i - 1, j - 1, visited);
			checkNeighbors(i - 1, j + 1, visited);
		}

		return true;
	}

	// Toggle flag on/of
	public void toggleFlag(int x, int y) {
		if (!board[x][y].isFlags())
			board[x][y].setFlags(true);
		else if (board[x][y].isFlags())
			board[x][y].setFlags(false);
	}

	// Check if all of the board is open except the mines nodes
	public boolean isDone() {
		int winNum = height * width - numMines;

		for (int i = 0; i < height; i++)
			for (int j = 0; j < width; j++)
				if (board[i][j].isOpen())
					winNum--;

		if (winNum == 0)
			return true;

		return false;

	}

	public void setShowAll(boolean showAll) {
		this.showAll = showAll;
	}

	//get return string of the board in (i,j) F - flag , X - mine , " " Open , "." - Empty. numberVlaue - number of mines
	public String get(int i, int j) {

		if (board[i][j].isOpen() || showAll) {
			if (board[i][j].isMines())
				return "X";
			else {
				int val = numOfMines(i, j);
				if (val == 0)
					return " ";
				else
					return "" + val; // return the number of mines nearby
			}
		} else {
			if (board[i][j].isFlags())
				return "F";
			else
				return ".";
		}

	}

	// Function that count all of the neighbors's that are mines
	private int numOfMines(int i, int j) {
		int res = 0;
		if (i + 1 < height)
			if (board[i + 1][j].isMines())
				res++;
		if (i - 1 >= 0)
			if (board[i - 1][j].isMines())
				res++;
		if (j + 1 < width)
			if (board[i][j + 1].isMines())
				res++;
		if (j - 1 >= 0)
			if (board[i][j - 1].isMines())
				res++;
		if (i + 1 < height && j + 1 < width)
			if (board[i + 1][j + 1].isMines())
				res++;
		if (i + 1 < height && j - 1 >= 0)
			if (board[i + 1][j - 1].isMines())
				res++;
		if (i - 1 >= 0 && j - 1 >= 0)
			if (board[i - 1][j - 1].isMines())
				res++;
		if (i - 1 >= 0 && j + 1 < width)
			if (board[i - 1][j + 1].isMines())
				res++;

		return res;
	}

	public String toString() {
		String boardStr = "";

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++)
				boardStr += get(i, j);
			boardStr += "\n";
		}
		return boardStr;

	}

	public Board getBoard(int i, int j) {
		return board[i][j];
	}

}
