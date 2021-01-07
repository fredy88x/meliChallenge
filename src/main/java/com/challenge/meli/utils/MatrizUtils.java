package com.challenge.meli.utils;

import org.springframework.stereotype.Component;

@Component
public class MatrizUtils {
	
	public static char[][] getMatrizTranspuesta(char[][] matrix, int sizeRows, int sizeColumns) {
		char[][] dnaTranspuesta = new char[sizeRows][sizeColumns];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				dnaTranspuesta[j][i] = matrix[i][j];
			}
		}
		return dnaTranspuesta;
	}
	
	public static char[][] getMatriz(String[] lista) {
		char[][] dnaMatrix = new char[lista.length][lista.length];
		for (int i = 0; i < lista.length; i++) {
			for (int j = 0; j < lista[i].length(); j++) {
				dnaMatrix[i][j] = lista[i].charAt(j);
			}
		}
		return dnaMatrix;
	}

}
