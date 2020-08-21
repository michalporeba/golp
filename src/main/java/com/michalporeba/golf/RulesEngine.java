package com.michalporeba.golf;

public interface RulesEngine {
	CellState nextState(CellState currentState, Cell[] neighbours);
}
