package golf;

public interface RulesEngine {
	CellState nextState(CellState currentState, Cell[] neighbours);
}
