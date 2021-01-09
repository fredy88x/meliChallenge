package com.challenge.meli;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.challenge.meli.controllers.MutantController;
import com.challenge.meli.controllers.StatController;
import com.challenge.meli.dto.DataDnaDTO;
import com.challenge.meli.dto.ResponseStatisticsDTO;
import com.challenge.meli.services.impl.DetectorService;
import com.challenge.meli.services.impl.StatisticsService;

@ExtendWith(MockitoExtension.class)
class MutantControllerTest {
	
	@Mock
	private DetectorService detectorService;
	@InjectMocks 
	private MutantController mutantController;
	@Mock
	private StatisticsService statisticsService;
	@InjectMocks 
	private StatController statController;
	
	
	
	@Test
	void test_is_not_mutant() {
		DataDnaDTO dna = new DataDnaDTO();
		dna.setDna(new String[] {"AGGCGC","CAGGAC","TTAGGT","ATTTCG","CGTCTA","TTATTG"});
		ResponseEntity<Void> response = mutantController.isMutant(dna);
		assertEquals(new ResponseEntity<>(HttpStatus.FORBIDDEN), response);
	}
	
	
	@Test
	void test_stats() {
		when(statisticsService.getStatisticsDna()).thenReturn(new ResponseStatisticsDTO((long)40, (long)100, 0.4));
		assertEquals(0.4, statController.getStatistics().getBody().getRatio().doubleValue());
	}
	
	
	

}
