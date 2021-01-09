package com.challenge.meli.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.meli.dto.ResponseStatisticsDTO;
import com.challenge.meli.services.StatisticsServiceI;

/**
 * It allows to obtain statistics of the mutant population
 * @author fredylaverde
 *
 */
@RestController
public class StatController {

	@Autowired
	private StatisticsServiceI statisticsServiceI;
	
	/**
	 * 
	 * @return
	 */
	@GetMapping(path = "/stats", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResponseStatisticsDTO> getStatistics(){
		try {
			return ResponseEntity.ok(statisticsServiceI.getStatisticsDna());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
		}
	}
	
}
