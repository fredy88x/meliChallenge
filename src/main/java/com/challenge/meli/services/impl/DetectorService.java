package com.challenge.meli.services.impl;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.challenge.meli.entitys.Dna;
import com.challenge.meli.exceptions.InvalidDnaException;
import com.challenge.meli.repositories.DnaRepositoryI;
import com.challenge.meli.services.DetectorServiceI;
import com.challenge.meli.utils.MatrizUtils;

/**
 * It allows to detect mutants
 * 
 * @author fredylaverde
 *
 */
@Service
public class DetectorService implements DetectorServiceI {

	@Autowired
	private DnaRepositoryI dnaRepositoryI;

	private String[] dna;
	private long foundSequences;
	
	@Value("${app.config.min.size.rows}")
	private int minSizeRows;
	@Value("${app.config.min.size.sequence.mutant}")
	private int minSizeSequenceMutant;
	@Value("${app.config.nitrogen.base.redex}")
	private String nitrogenBaseRegex;
	@Value("${app.config.mutant.redex}")
	private String mutantRegex;
	

	@Override
	public boolean isMutant(String[] dna) throws InvalidDnaException {
		this.dna = dna;
		validateMatrizDna();
		String dnaSequence = Arrays.toString(this.dna);
		Dna dnaFound = dnaRepositoryI.findById(dnaSequence).orElse(null);
		if (dnaFound != null) {
			return dnaFound.isMutant();
		}
		readDnaSequence(dna);
		boolean resultado = foundSequences >= minSizeSequenceMutant;
		saveDna(dnaSequence, resultado);
		this.dna = null;
		this.foundSequences = 0;
		return resultado;
	}

	/**
	 * Read a dna sequence to search the mutant gen
	 * 
	 * @param dna
	 * @throws InvalidDnaException
	 */
	private void readDnaSequence(String[] dna) throws InvalidDnaException {
		readRows();
		char[][] dnaMatrix = MatrizUtils.getMatriz(dna);
		readColumns(dnaMatrix);
		readUpDiagonal(dnaMatrix);
		readDownDiagonal(dnaMatrix);
	}

	/**
	 * Save a dna sequence in redis database
	 * 
	 * @param dnaSequence
	 * @param isMutant
	 */
	private void saveDna(String dnaSequence, boolean isMutant) {
		Dna dnaData = new Dna();
		dnaData.setDnaSequence(dnaSequence);
		dnaData.setMutant(isMutant);
		dnaRepositoryI.save(dnaData);
	}

	/**
	 * Read Up diagonal the dna matriz
	 * 
	 * @param dnaMatriz
	 */
	private void readUpDiagonal(char[][] dnaMatriz) {
		if (foundSequences >= minSizeSequenceMutant) {
			return;
		}
		int matrizSize = dnaMatriz.length;
		for (int i = 0; i < matrizSize; i++) {
			StringBuilder dnaDiagonal = new StringBuilder();
			for (int j = 0; j <= i; j++) {
				dnaDiagonal.append(dnaMatriz[i - j][j]);
			}
			foundSequences += getMatchesSequences(dnaDiagonal.toString());
			if (foundSequences >= minSizeSequenceMutant) {
				return;
			}
		}
		readUpRightDiagonal(dnaMatriz);
		
	}
	
	private void readUpRightDiagonal(char[][] dnaMatriz) {
		int matrizSize = dnaMatriz.length;
		for (int i = 0; i < matrizSize; i++) {
			StringBuilder dnaDiagonal = new StringBuilder();
			for (int j = matrizSize - 1; j >= matrizSize - 1 - i; j--) {
				dnaDiagonal.append(dnaMatriz[i + j - (matrizSize - 1)][j]);
			}
			foundSequences += getMatchesSequences(dnaDiagonal.toString());
			if (foundSequences >= minSizeSequenceMutant) {
				return;
			}
		}
	}

	/**
	 * Read Down diagonal the dna matriz
	 * 
	 * @param dnaMatriz
	 */
	private void readDownDiagonal(char[][] dnaMatriz) {
		if (foundSequences >= minSizeSequenceMutant) {
			return;
		}
		int matrizSize = dnaMatriz.length;
		for (int i = 0; i < matrizSize; i++) {
			StringBuilder dnaDiagonal = new StringBuilder();
			for (int j = 0; j < matrizSize - i - 1; j++) {

				dnaDiagonal.append(dnaMatriz[matrizSize - j - 1][j + i + 1]);

			}
			foundSequences += getMatchesSequences(dnaDiagonal.toString());
			if (foundSequences >= minSizeSequenceMutant) {
				return;
			}
		}
		readDownLeftDiagonal(dnaMatriz);
		
	}
	
	private void readDownLeftDiagonal(char[][] dnaMatriz) {
		int matrizSize = dnaMatriz.length;
		for (int i = 0; i < matrizSize; i++) {
			StringBuilder dnaDiagonal = new StringBuilder();
			for (int j = 0; j <= matrizSize - i - 1; j++) {

				dnaDiagonal.append(dnaMatriz[matrizSize - j - 1][matrizSize - j - i - 1]);

			}
			foundSequences += getMatchesSequences(dnaDiagonal.toString());
			if (foundSequences >= minSizeSequenceMutant) {
				return;
			}
		}
	}

	/**
	 * Read columns the dna matriz
	 * 
	 * @param dnaMatriz
	 */
	private void readColumns(char[][] dnaMatriz) {
		if (foundSequences >= minSizeSequenceMutant) {
			return;
		}
		char[][] dnaTranspuesta = MatrizUtils.getMatrizTranspuesta(dnaMatriz, dna.length, dna.length);
		for (int i = 0; i < dnaTranspuesta.length && foundSequences < minSizeSequenceMutant; i++) {
			StringBuilder dnaColumn = new StringBuilder();
			for (int j = 0; j < dnaTranspuesta[i].length; j++) {
				dnaColumn.append(dnaTranspuesta[i][j]);
			}
			foundSequences += getMatchesSequences(dnaColumn.toString());
		}

	}

	/**
	 * Read rows the DNA Matriz
	 * 
	 * @throws InvalidDnaException
	 */
	private void readRows() throws InvalidDnaException {
		for (String string : dna) {
			if (!string.matches(nitrogenBaseRegex)) {
				throw new InvalidDnaException("The String DNA contain invalid characters");
			}
			if (string.length() != dna.length) {
				throw new InvalidDnaException(
						"Dna Matriz should be at least " + (minSizeRows + 1) + " X " + (minSizeRows + 1) + ".");
			}
			foundSequences += getMatchesSequences(string);
		}

	}

	private int getMatchesSequences(String dnaSequence) {
		Pattern mutantRegexPatern = Pattern.compile(mutantRegex);
		Matcher founds = mutantRegexPatern.matcher(dnaSequence);
		int count = 0;
		while (founds.find()) {
			count++;
		}
		return count;
	}

	/**
	 * Validate the DNA matriz
	 * 
	 * @throws InvalidDnaException
	 */
	private void validateMatrizDna() throws InvalidDnaException {
		if (this.dna.length < minSizeRows) {
			throw new InvalidDnaException(
					"Dna Matriz should be at least " + (minSizeRows + 1) + " X " + (minSizeRows + 1) + ".");
		}
	}
}
