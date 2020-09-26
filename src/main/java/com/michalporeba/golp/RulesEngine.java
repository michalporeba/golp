package com.michalporeba.golp;

public interface RulesEngine {
	CellState nextState(CellState currentState, Cell[] neighbours);
}
