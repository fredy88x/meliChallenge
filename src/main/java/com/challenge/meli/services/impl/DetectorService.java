package com.challenge.meli.services.impl;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.challenge.meli.entitys.Dna;
import com.challenge.meli.exceptions.InvalidDnaException;
import com.challenge.meli.repositories.DnaRepositoryI;
import com.challenge.meli.services.DetectorServiceI;
import com.challenge.meli.utils.MatrizUtils;

/**
 * It allows to detect mutants
 * @author fredylaverde
 *
 */
@Service
public class DetectorService implements DetectorServiceI {

	@Autowired
	private DnaRepositoryI dnaRepositoryI;
	
	private String[] dna;
	private long foundSequences;
	private final int MIN_SIZE_ROWS = 3;
	private final long MIN_SIZE_SEQUENCE_MUTANT = 2;
	private final String NITROGEN_BASE_REGEX = "^([ATGC]*)$";
	private final String MUTANT_REGEX = "([ATGC])\\1{3}";
	Pattern MUTANT_REGEX_PATTERN = Pattern.compile(MUTANT_REGEX);

	@Override
	public boolean isMutant(String[] dna) throws InvalidDnaException {
		this.dna = dna;
		validateMatrizDna();
		String dnaSequence = Arrays.toString(this.dna);
		Dna dnaFound = dnaRepositoryI.findById(dnaSequence).orElse(null);
		if( dnaFound != null) {
			return dnaFound.isMutant();
		};
		readDnaSequence(dna);
		boolean resultado =  foundSequences >= MIN_SIZE_SEQUENCE_MUTANT;
		saveDna(dnaSequence,resultado);
		this.dna = null;
		this.foundSequences = 0;
		return resultado;
	}

	/**
	 * Read a dna sequence to search the mutant gen
	 * @param dna
	 * @throws InvalidDnaException
	 */
	private void readDnaSequence(String[] dna) throws InvalidDnaException {
		char[][] dnaMatrix = MatrizUtils.getMatriz(dna);
		readRows();
		readColumns(dnaMatrix);
		readUpDiagonal(dnaMatrix);
		readDownDiagonal(dnaMatrix);
	}
	
	
	/**
	 * Save a dna sequence in redis database
	 * @param dnaSequence
	 * @param isMutant
	 */
	private void saveDna(String dnaSequence, boolean isMutant) {
		Dna dna = new Dna();
		dna.setDna(dnaSequence);
		dna.setMutant(isMutant);
		dnaRepositoryI.save(dna);
	}

	/**
	 * Read Up diagonal the dna matriz
	 * @param dnaMatriz
	 */
	private void readUpDiagonal(char[][] dnaMatriz) {
		if(foundSequences >= MIN_SIZE_SEQUENCE_MUTANT) {return;}
		for (int i=0;i<dnaMatriz.length;i++) {
			StringBuffer dnaDiagonal = new StringBuffer();
			for (int j=0;j<=i;j++) {

				dnaDiagonal.append(dnaMatriz[i-j][j]);

			}
			foundSequences += getMatchesSequences(dnaDiagonal.toString());
			}
		
	}

	/**
	 * Read Down diagonal the dna matriz
	 * @param dnaMatriz
	 */
	private void readDownDiagonal(char[][] dnaMatriz) {
		if(foundSequences >= MIN_SIZE_SEQUENCE_MUTANT) {return;}
		for (int i=0;i<dnaMatriz.length;i++) {
			StringBuffer dnaDiagonal = new StringBuffer();
			for (int j=0;j<dnaMatriz.length-i-1;j++) {

				dnaDiagonal.append(dnaMatriz[dnaMatriz.length-j-1][j+i+1]);

			}
			foundSequences += getMatchesSequences(dnaDiagonal.toString());

			}
	}

	/**
	 * Read columns the dna matriz
	 * @param dnaMatriz
	 */
	private void readColumns(char[][] dnaMatriz) {
		if (foundSequences >= MIN_SIZE_SEQUENCE_MUTANT) {
			return;
		}
		char[][] dnaTranspuesta = MatrizUtils.getMatrizTranspuesta(dnaMatriz, dna.length, dna.length);
		for (int i = 0; i < dnaTranspuesta.length && foundSequences < MIN_SIZE_SEQUENCE_MUTANT; i++) {
			StringBuffer dnaColumn = new StringBuffer();
			for (int j = 0; j < dnaTranspuesta[i].length; j++) {
				dnaColumn.append(dnaTranspuesta[i][j]);
			}
			foundSequences += getMatchesSequences(dnaColumn.toString());
		}

	}

	/**
	 * Read rows the DNA Matriz
	 * @throws InvalidDnaException
	 */
	private void readRows() throws InvalidDnaException {
		for (String string : dna) {
			if (!string.matches(NITROGEN_BASE_REGEX)) {
				throw new InvalidDnaException("The String DNA contain invalid characters");
			}
			if (string.length() != dna.length) {
				throw new InvalidDnaException(
						"Dna Matriz should be at least " + (MIN_SIZE_ROWS + 1) + " X " + (MIN_SIZE_ROWS + 1) + ".");
			}
			foundSequences += getMatchesSequences(string);
		}

	}
	
	private int getMatchesSequences(String dnaSequence) {
		Matcher founds = MUTANT_REGEX_PATTERN.matcher(dnaSequence);
		int count = 0;
		while(founds.find()) {
			count++;
		}
		return count;
	}

	/**
	 * Validate the DNA matriz
	 * @throws InvalidDnaException
	 */
	private void validateMatrizDna() throws InvalidDnaException {
		if (this.dna.length < MIN_SIZE_ROWS) {
			throw new InvalidDnaException(
					"Dna Matriz should be at least " + (MIN_SIZE_ROWS + 1) + " X " + (MIN_SIZE_ROWS + 1) + ".");
		}
	}
}
