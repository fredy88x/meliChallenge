package com.challenge.meli.services;

import com.challenge.meli.exceptions.InvalidDnaException;

public interface DetectorServiceI {
	
	boolean isMutant(String[] dna) throws InvalidDnaException;

}
