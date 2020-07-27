package golf;

public class Board {
	private boolean isEmpty = true;
	
	public Board(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("size has to be positive");
		}
	}
	
	public boolean isEmpty() {
		return isEmpty;
	}
	
	public void loadFrom(BoardReader reader) {
		isEmpty = false;
	}
}
