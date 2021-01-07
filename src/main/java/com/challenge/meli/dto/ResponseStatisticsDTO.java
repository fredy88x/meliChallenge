package com.challenge.meli.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseStatisticsDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("count_mutant_dna")
	private long countMutantDna;
	@JsonProperty("count_human_dna")
	private long countHumanDna;
	@JsonProperty("ratio")
	private Double ratio;
	
	public ResponseStatisticsDTO(long countMutantDna, long countHumanDna, Double ratio) {
		super();
		this.countMutantDna = countMutantDna;
		this.countHumanDna = countHumanDna;
		this.ratio = ratio;
	}
	
	

}
