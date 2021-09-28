package mineSweeper.board;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class ResetTests {

	static Frame f = new Frame("Minesweeper Tester");
	static int x;
	static int y;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		f.dispatchEvent(new WindowEvent(f, WindowEvent.WINDOW_CLOSING));
	}

	@BeforeEach
	void setUp() throws Exception {
		// gen random x y and click button[x][y]
		f.reset();
		x = (int) (Math.random() * (f.getRow()));
		y = (int) (Math.random() * (f.getCol()));

		MouseEvent e = new MouseEvent(f.getButtons()[x][y], MouseEvent.MOUSE_CLICKED, 1000, 0,
				f.getButtons()[x][y].getX(), f.getButtons()[x][y].getY(), 1, false, MouseEvent.BUTTON1);
		f.mouseClicked(e);
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void gameResetTest() throws InterruptedException, AWTException {
		assertFalse(f.getButtons()[x][y].isEnabled(), "Unexpected button behaviour");
//		Thread.sleep(2000);
		f.reset();
//		Thread.sleep(2000);
		assertTrue(f.getButtons()[x][y].isEnabled(), "Unexpected reset behaviour");
	}

	@Test
	void InitialClickTest() throws InterruptedException {

		f.reset();

		for (int x = 0; x < f.getRow(); x++) {
			for (int y = 0; y < f.getCol(); y++) {
				MouseEvent e = new MouseEvent(f.getButtons()[x][y], MouseEvent.MOUSE_CLICKED, 1000, 0,
						f.getButtons()[x][y].getX(), f.getButtons()[x][y].getY(), 1, false, MouseEvent.BUTTON1);
				f.mouseClicked(e);
				assertFalse(f.isEnded(), "First click is not safe");
				f.reset();
			}
		}

	}

	//Upon losing
	@Test
	void gameoverTest() throws InterruptedException {
		State tempButtonState;

		// find a bomb and click it
		boolean flag =false;
		for (int x = 0; x < f.getRow(); x++) {
			for (int y = 0; y < f.getCol(); y++) {
				if (f.getButtons()[x][y].isHasBomb() && !flag) {
					MouseEvent e = new MouseEvent(f.getButtons()[x][y], MouseEvent.MOUSE_CLICKED, 1000, 0,
							f.getButtons()[x][y].getX(), f.getButtons()[x][y].getY(), 1, false, MouseEvent.BUTTON1);
					f.mouseClicked(e);
					flag = true;
				}
			}
		}
		assertTrue(f.isEnded(), "Bomb triggered and game is not over");

		// try clicking all buttons and checking that nothing happens
		for (int x = 0; x < f.getRow(); x++) {
			for (int y = 0; y < f.getCol(); y++) {

				tempButtonState = f.getButtons()[x][y].getState();
				MouseEvent e = new MouseEvent(f.getButtons()[x][y], MouseEvent.MOUSE_CLICKED, 1000, 0,
						f.getButtons()[x][y].getX(), f.getButtons()[x][y].getY(), 1, false, MouseEvent.BUTTON1);
				f.mouseClicked(e);

				assertTrue(tempButtonState == f.getButtons()[x][y].getState(), "Game ended but was able to click");

			}
		}
		assertTrue(f.isEnded(), "Bomb triggered and game is not over");

	}

	// Upon winning
	@Test
	void gameoverTest2() throws InterruptedException {
		// win the game
		for (int x = 0; x < f.getRow(); x++) {
			for (int y = 0; y < f.getCol(); y++) {
				if (!f.getButtons()[x][y].isHasBomb()) {
					MouseEvent e = new MouseEvent(f.getButtons()[x][y], MouseEvent.MOUSE_CLICKED, 1000, 0,
							f.getButtons()[x][y].getX(), f.getButtons()[x][y].getY(), 1, false, MouseEvent.BUTTON1);
					f.mouseClicked(e);
				}
			}
		}
		assertTrue(f.isEnded(), "Won but game didnt end");
	}

}
