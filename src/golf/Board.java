package golf;

public class Board {
	public Board(int size) {
		if (size <= 0) {
			throw new IllegalArgumentException("size has to be positive");
		}
	}
	
	public boolean isEmpty() {
		return true;
	}
}
