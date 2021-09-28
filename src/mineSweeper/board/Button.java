package mineSweeper.board;

import javax.swing.JButton;

public class Button extends JButton {

	private boolean hasBomb;
	private State state;

	// Constructor
	Button() {
		hasBomb = false;
		state = State.Closed;
	}

	public boolean isHasBomb() {
		return hasBomb;
	}

	public void setHasBomb(boolean hasBomb) {
		this.hasBomb = hasBomb;
	}

	public State getState() {
		return state;
	}

	public void setState(State stat) {
		this.state = stat;
	}

}
