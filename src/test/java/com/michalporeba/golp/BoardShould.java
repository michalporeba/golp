package com.michalporeba.golp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class BoardShould {
	@ParameterizedTest
	@ValueSource(ints = {0,-1, Integer.MIN_VALUE})
	void throw_if_size_less_or_equal_zero(int size) {
		assertThrows(IllegalArgumentException.class, () -> {
			new Board(size);
		});
	}
	
	@Test
	void be_empty_to_start_with() {
		Board sut = new Board(4);
		assertEquals(true, sut.isEmpty());
	}
	
	@Test 
	void set_state_from_source() {
		Board sut = new Board(4);
		int[][] data = new int[][] {
				{ 0, 1, 0, 0 },
				{ 0, 0, 0, 1 },
				{ 0, 0, 1, 0 },
				{ 1, 0, 0, 0 },
		};
		                 
		BoardReader reader = new ArrayBoardReader(data);
		sut.loadFrom(reader);
		assertEquals(false, sut.isEmpty());
	}
	
	@Test
	void write_state_out() {
		Board sut = new Board(4);
		int[][] data = new int[][] {
				{ 0, 1, 0, 0 },
				{ 0, 0, 0, 1 },
				{ 0, 0, 1, 0 },
				{ 1, 0, 0, 0 },
		};
		BoardReader reader = new ArrayBoardReader(data);
		BoardWriter writer = new ArrayBoardWriter();

		sut.loadFrom(reader);
		
		assertEquals(false, sut.isEmpty());
	}
}
