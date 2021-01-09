package com.challenge.meli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.challenge.meli.dto.ResponseStatisticsDTO;
import com.challenge.meli.entitys.Dna;
import com.challenge.meli.repositories.DnaRepositoryI;
import com.challenge.meli.services.impl.StatisticsService;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

	@Mock
	private DnaRepositoryI dnaRepository;

	@InjectMocks
	private StatisticsService statisticsService;

	private List<Dna> dnaSequencesMock;

	@BeforeEach
	public void setup() {

		dnaSequencesMock = new ArrayList<>();
		Dna dnaOne = new Dna();
		dnaOne.setMutant(false);
		Dna dnaTwo = new Dna();
		dnaTwo.setMutant(true);
		dnaSequencesMock.add(dnaOne);
		dnaSequencesMock.add(dnaTwo);
	}
		
	
	/**
     * It gets stats
     */
	@Test
	void getStats() {
			when(dnaRepository.findAll()).thenReturn(dnaSequencesMock);
			ResponseStatisticsDTO response = statisticsService.getStatisticsDna();
			assertEquals(1, response.getCountMutantDna());
			assertEquals(1, response.getCountHumanDna());
			assertEquals(1, response.getRatio().intValue());
	}
	
	
}
