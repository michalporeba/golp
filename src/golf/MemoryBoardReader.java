package golf;

public class MemoryBoardReader implements BoardReader {
	
	private String data = "";
	private int pointer = 0;
	
	public MemoryBoardReader(String data) {
		this.data = data;
	}
	
	public boolean hasNext() {
		return true;
	}
	
	public Point getNext() {
		
	}
}
