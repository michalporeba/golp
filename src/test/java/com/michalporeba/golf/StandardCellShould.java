package com.michalporeba.golf;

import org.junit.Test;

class StandardCellShould {
	@Test
	void change_state() {
		RulesEngine rulesEngine = new ConwayRulesEngine();
		Cell sut = new StandardCell(rulesEngine);
		
		sut.goToNextState();
	}
}
