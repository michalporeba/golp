package golf;

public class ArrayBoardReader implements BoardReader {
	
	private int[][] data;
	
	public ArrayBoardReader(int[][] data) {
		this.data = data;
	}
	
	public boolean hasNext() {
		return true;
	}
	
	public Point getNext() {
		return new Point(1,2);
	}
	
	private void findNext() {

	}
}
