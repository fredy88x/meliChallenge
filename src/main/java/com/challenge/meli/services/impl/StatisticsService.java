package com.challenge.meli.services.impl;

import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.challenge.meli.dto.ResponseStatisticsDTO;
import com.challenge.meli.entitys.Dna;
import com.challenge.meli.repositories.DnaRepositoryI;
import com.challenge.meli.services.StatisticsServiceI;

@Service
public class StatisticsService implements StatisticsServiceI {

	@Autowired
	private DnaRepositoryI  dnaRepositoryI;
	
	@Override
	public ResponseStatisticsDTO getStatisticsDna() {
		Supplier<Stream<Dna>> dnaList = () -> StreamSupport.stream(dnaRepositoryI.findAll().spliterator(), false);
		long quantityMutants = dnaList.get().filter(dna -> dna.isMutant()).count();
		long quantityHumans = dnaList.get().filter(dna -> !dna.isMutant()).count();
		Double ratio = calculateRatio(quantityMutants, quantityHumans);
		return new ResponseStatisticsDTO(quantityMutants, quantityHumans, ratio);
	}
	
	private double calculateRatio(long quantityMutants, long quantityHumans) {
		Double ratio = (quantityHumans == 0) ? 1 : (double) quantityMutants / (double) quantityHumans;
		return Math.round(ratio * 100d )/100d;
	}

}
