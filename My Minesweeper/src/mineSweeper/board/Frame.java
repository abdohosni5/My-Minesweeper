package mineSweeper.board;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.NumberFormatter;

public class Frame extends JFrame implements MouseListener {

	private int row = 30;
	private int col = 16;
	private int flagCount = 0;
	private boolean firstClick = true;
	private boolean end = false;
	private int mines = 50;
	private int firstx = 0;
	private int firsty = 0;

	private Button[][] buttons = new Button[row][col];
	private int[][] values = new int[row][col];

	public Frame(String s) {

		// Setting GUI top to bottom
		this.setTitle(s);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(300, 0);
		this.setSize(col * 42, row * 28);

		JMenuBar bar = new JMenuBar();
		JMenu tools = new JMenu("Tools");
		JMenuItem resetBoard = new JMenuItem("New Game");
		JMenuItem minesCount = new JMenuItem("Mines");

		bar.add(tools);
		tools.add(resetBoard);
		tools.add(minesCount);
		this.add(bar, BorderLayout.NORTH);

		JPanel p = new JPanel();
		this.add(p);
		p.setLayout(new GridLayout(row, col));

		// initiate buttons and add to frame
		for (int x = 0; x < row; x++) {
			for (int y = 0; y < col; y++) {
				buttons[x][y] = new Button();
				p.add(buttons[x][y]);
				buttons[x][y].addMouseListener(this);
//				buttons[x][y].setIcon(new ImageIcon(
//						"file:///D:/minesweepererererererer/minesweeper/src/minesweeper/board/src/gfx/bomb.png"));
			}
		}

		this.setVisible(true);

		// reset functionality
		resetBoard.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				reset();
			}
		});

		// Changing number of mine
		minesCount.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				NumberFormat format = NumberFormat.getInstance();
				NumberFormatter formatter = new NumberFormatter(format);
				formatter.setValueClass(Integer.class);
				formatter.setMinimum(0);
				formatter.setMaximum(((row * col) - 1) < 100 ? ((row * col) - 1) : 100);
				formatter.setAllowsInvalid(false);
				// If you want the value to be committed on each keystroke instead of focus lost
				formatter.setCommitsOnValidEdit(true);
				JFormattedTextField field = new JFormattedTextField(formatter);

				int numberOfMines = Integer.parseInt(JOptionPane.showInputDialog(
						"Enter Mines count, 1 to " + (((row * col) - 1) < 100 ? ((row * col) - 1) : 100) + "", field));

				if (mines != numberOfMines) {
					changeMines(numberOfMines);
				}

			}

		});

	}

	// Methods
	public void generateMines() {
		// reset
		for (int x = 0; x < mines;) {
			// generate random x and y
			int rndx = (int) (Math.random() * row);
			int rndy = (int) (Math.random() * col);

			// in random cell(x,y) if there is no bomb and that cell is not the cell the
			// user first clicked set bomb
			// and increment x
			if (!buttons[rndx][rndy].isHasBomb() && buttons[rndx][rndy] != buttons[firstx][firsty]) {
				x++;
				values[rndx][rndy] = 10;
				buttons[rndx][rndy].setHasBomb(true);
			}
		}

		// Neighbors check
		for (int x = 0; x < row; x++) {
			for (int y = 0; y < col; y++) {

				if (values[x][y] != 10) {
					int neighbourcount = 0;
					if (x > 0 && y > 0 && values[x - 1][y - 1] == 10) {// up left
						neighbourcount++;
					}
					if (y > 0 && values[x][y - 1] == 10) {// up
						neighbourcount++;
					}
					if (x < values.length - 1 && y > 0 && values[x + 1][y - 1] == 10) {// up right
						neighbourcount++;
					}
					if (x > 0 && y < values[0].length - 1 && values[x - 1][y + 1] == 10) {// down left
						neighbourcount++;
					}
					if (y < values[0].length - 1 && values[x][y + 1] == 10) {// down
						neighbourcount++;
					}
					if (x < values.length - 1 && y < values[0].length - 1 && values[x + 1][y + 1] == 10) {// down right
						neighbourcount++;
					}
					if (x > 0 && values[x - 1][y] == 10) {// left
						neighbourcount++;
					}
					if (x < values.length - 1 && values[x + 1][y] == 10) {// right
						neighbourcount++;
					}

					values[x][y] = neighbourcount;
				}
			}
		}

	}

	// Opens a cell
	private void open(int x, int y) {
		// check the button is not already open or has bomb flag on it then open it
		if (buttons[x][y].getState() == State.Closed) {

			// end game if it has bomb
			if (!buttons[x][y].isHasBomb()) {
				buttons[x][y].setText(values[x][y] + "");
//				buttons[x][y].setFont((new Font("Calibri", Font.PLAIN, 10)));
				// if blank cell call zero()
				if (values[x][y] == 0) {
					Zero(x, y);
					buttons[x][y].setText("");
				}
				buttons[x][y].setEnabled(false);
				buttons[x][y].setState(State.Opened);
			} else {
				gameEnd();
			}
		}
	}

	// Blank cells
	public void Zero(int x, int y) {

		if (buttons[x][y].isEnabled() == true) {
			buttons[x][y].setEnabled(false);
			if (x > 0 && y > 0) {// up left
				open(x - 1, y - 1);
			}
			if (y > 0) {// up
				open(x, y - 1);
			}
			if (x < values.length - 1 && y > 0) {// up right
				open(x + 1, y - 1);
			}
			if (x > 0 && y < values[0].length - 1) {// down left
				open(x - 1, y + 1);
			}
			if (y < values[0].length - 1) {// down
				open(x, y + 1);
			}
			if (x < values.length - 1 && y < values[0].length - 1) {// down right
				open(x + 1, y + 1);
			}
			if (x > 0) {// left
				open(x - 1, y);
			}
			if (x < values.length - 1) {// right
				open(x + 1, y);
			}
		}

	}

	// Opens all cells around (x,y)
	private void openaround(int x, int y) {

		int count = 0;
		if (x > 0 && y > 0) {// up left
			if (buttons[x - 1][y - 1].getState() == State.Flagged) {
				count++;
			}
		}
		if (y > 0) {// up
			if (buttons[x][y - 1].getState() == State.Flagged) {
				count++;
			}
		}
		if (x < values.length - 1 && y > 0) {// up right
			if (buttons[x + 1][y - 1].getState() == State.Flagged) {
				count++;
			}
		}
		if (x > 0 && y < values[0].length - 1) {// down left
			if (buttons[x - 1][y + 1].getState() == State.Flagged) {
				count++;
			}
		}
		if (y < values[0].length - 1) {// down
			if (buttons[x][y + 1].getState() == State.Flagged) {
				count++;
			}
		}
		if (x < values.length - 1 && y < values[0].length - 1) {// down right
			if (buttons[x + 1][y + 1].getState() == State.Flagged) {
				count++;
			}
		}
		if (x > 0) {// left
			if (buttons[x - 1][y].getState() == State.Flagged) {
				count++;
			}
		}
		if (x < values.length - 1) {// right
			if (buttons[x + 1][y].getState() == State.Flagged) {
				count++;
			}
		}

		if (count == values[x][y]) {
			if (x > 0 && y > 0) {// up left
				open(x - 1, y - 1);
			}
			if (y > 0) {// up
				open(x, y - 1);
			}
			if (x < values.length - 1 && y > 0) {// up right
				open(x + 1, y - 1);
			}
			if (x > 0 && y < values[0].length - 1) {// down left
				open(x - 1, y + 1);
			}
			if (y < values[0].length - 1) {// down
				open(x, y + 1);
			}
			if (x < values.length - 1 && y < values[0].length - 1) {// down right
				open(x + 1, y + 1);
			}
			if (x > 0) {// left
				open(x - 1, y);
			}
			if (x < values.length - 1) {// right
				open(x + 1, y);
			}

		}

	}

	// Monitors failing end of game
	public void gameEnd() {
		for (int x = 0; x < row; x++) {
			for (int y = 0; y < col; y++) {
				if (buttons[x][y].isHasBomb()) {
					if (buttons[x][y].getState() != State.Flagged) {
						buttons[x][y].setBackground(Color.RED);
						buttons[x][y].setEnabled(false);
					} else {
						buttons[x][y].setBackground(Color.GREEN);
					}
				}

			}
		}
		end = true;
	}

	// Monitors winning end of game
	public void checkWin() {
		for (int x = 0; x < row; x++) {
			for (int y = 0; y < col; y++) {
				if (buttons[x][y].getState() != State.Opened && !buttons[x][y].isHasBomb()) {
					System.out.println("lesa");
					return;
				}
			}
		}
		System.out.println("tmm");
//		JTextPane x= new JTextPane();
//		x.setVisible(true);
//		f.add(x);
//		f.repaint();
//		x.setText("AYWA B2AAAAAAA GOOD JOB 2OOM SHOOF 2LY WARAK YANOOB!");
//		JLabel x= new JLabel("allahhhhhhhhhhhhhhhhhhhhhhhh 3lek ya fa5r el 3rab!");
//		x.setVisible(true);
//		f.add(x,BorderLayout.CENTER);
//		f.repaint();
		JFrame l = new JFrame();
		l.setBounds(20, 20, 800, 800);
//		JButton buto = new JButton(
//				new ImageIcon("file:///D:/minesweepererererererer/minesweeper/src/minesweeper/board/src/gfx/bomb.png"));
//		buto.validate();
//		buto.setVisible(true);
//		l.add(buto);
//		JTextArea x = new JTextArea("YABN EL LA3EBA! BRAFO! :)");
//		l.add(x);
		l.setVisible(true);
		gameEnd();

//		for (int x1 = 0; x1 < row; x1++) {
//			for (int y = 0; y < col; y++) {
//				buttons[x1][y].setEnabled(false);
//			}
//		}

	}

	// Resets board
	public void reset() {
		flagCount = 0;
		firstClick = true;
		for (int x = 0; x < row; x++) {
			for (int y = 0; y < col; y++) {
				values[x][y] = 0;
				// enable all buttons
				buttons[x][y].setEnabled(true);
				// remove all bombs
				buttons[x][y].setHasBomb(false);
				// set status to closed for all buttons
				buttons[x][y].setState(State.Closed);
				// remove formating for all buttons
				buttons[x][y].setBackground(null);
				buttons[x][y].setText("");
				// new game's flag
				end = false;
			}
		}
	}

	// Changing number of mines on board
	private void changeMines(int input) {

		mines = input;
		reset();

	}

	// Mouse listener
	@Override
	public void mouseClicked(MouseEvent e) {

		int x = 0;
		int y = 0;
		// find x,y of clicked button
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {

				if (e.getSource().equals(buttons[i][j])) {
					x = i;
					y = j;
					// break;
				}
			}
		}

		if (!end) {
			if (!firstClick) {

				// if flagged and right clicked remove flag
				if (buttons[x][y].getState() == State.Flagged && e.getButton() == MouseEvent.BUTTON3) {
					buttons[x][y].setBackground(null);
					buttons[x][y].setEnabled(true);
					buttons[x][y].setState(State.Closed);
					flagCount--;
				} else {
					if (buttons[x][y].getState() == State.Closed && e.getButton() == MouseEvent.BUTTON3) {
						if (flagCount != mines) {
							buttons[x][y].setBackground(Color.BLUE);
							buttons[x][y].setEnabled(false);
							buttons[x][y].setState(State.Flagged);
							flagCount++;
						}
					} else {
						if (buttons[x][y].getState() == State.Closed && e.getButton() == MouseEvent.BUTTON1) {
							open(x, y);
						} else {
							if (buttons[x][y].getState() == State.Opened && e.getButton() == MouseEvent.BUTTON1) {
								if (e.getClickCount() == 2) {
									openaround(x, y);
									System.out.println("doubled");
								}
							}
						}
					}
				}
			} else {
				firstx = x;
				firsty = y;
				firstClick = false;
				generateMines();
				open(firstx, firsty);
				validate();
				System.out.println("first click Safe!");
			}
			checkWin();
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	// Getters for private modifier
	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public int getMines() {
		return mines;
	}

	public int getFirstx() {
		return firstx;
	}

	public int getFirsty() {
		return firsty;
	}

	public Button[][] getButtons() {
		return buttons;
	}

	public int[][] getValues() {
		return values;
	}

	public int getFlagCount() {
		return flagCount;
	}

	public boolean isFirstClick() {
		return firstClick;
	}

	public boolean isEnded() {
		return end;
	}

}