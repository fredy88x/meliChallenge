package com.challenge.meli.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.challenge.meli.dto.DataDnaDTO;
import com.challenge.meli.services.DetectorServiceI;
/**
 * It allows to detect if a person is mutant
 * @author fredylaverde
 *
 */
@RestController
public class MutantController {
	
	@Autowired
	private DetectorServiceI detectorServiceI;
	
	/**
	 * Resource to validate DNA sequence
	 * @param dna
	 * @return
	 */
	@PostMapping(path = "/mutant", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> isMutant (@RequestBody DataDnaDTO dna){
		try {
			if(detectorServiceI.isMutant(dna.getDna())) {
				return ResponseEntity.ok().build();
			}
			else {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
		}
		
	}

}
