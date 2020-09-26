package com.michalporeba.golp;

public class StandardCell implements Cell {
	private RulesEngine rulesEngine;
	private CellState currentState;
	private CellState previousState;
	
	public StandardCell(RulesEngine rulesEngine) {
		this.rulesEngine = rulesEngine;
	}
	
	public void goToNextState() {
		previousState = currentState;
		currentState = rulesEngine.nextState(previousState, new Cell[] {});
	}
}
