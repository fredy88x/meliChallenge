package com.challenge.meli;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.challenge.meli.entitys.Dna;
import com.challenge.meli.exceptions.InvalidDnaException;
import com.challenge.meli.repositories.DnaRepositoryI;
import com.challenge.meli.services.impl.DetectorService;

@ExtendWith(MockitoExtension.class)
class DetectorServiceTest {
	
	@Mock
	private DnaRepositoryI dnaRepository;
	
	@InjectMocks
	private DetectorService detectorService;
	
	
	private Dna dnaSequenceStored;
	
	@BeforeEach
	public void setup() {
	
		ReflectionTestUtils.setField(detectorService, "minSizeRows", 3);
		ReflectionTestUtils.setField(detectorService, "minSizeSequenceMutant", 2);
		ReflectionTestUtils.setField(detectorService, "nitrogenBaseRegex", "^([ATGC]*)$");
		ReflectionTestUtils.setField(detectorService, "mutantRegex", "([ATGC])\\1{3}");
		dnaSequenceStored = new Dna();
		dnaSequenceStored.setMutant(true);
	}

    /**
     * It validates mutant DNA sequence validates by rows and columns
     */
	@Test
	void validateDnaMutantTrueRowsColumns() {
		try {
			String[] dnaSequence = {"ATGCGC","CAGTGC","TTATGT","TTTTGG","CGCCTA","TTACTG"};
			when(dnaRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
			when(dnaRepository.save(Mockito.any())).thenReturn(new Dna());
			boolean result = detectorService.isMutant(dnaSequence);
			assertTrue(result);
		} catch (InvalidDnaException e) {
			fail();
		}
		
	}
	
	/**
     * It validates mutant DNA sequence validates by rows and columns that has been previously stored
     */
	@Test
	void validateDnaMutantTrueRowsColumnsStored() {
		try {
			String[] dnaSequence = {"ATGCGC","CAGTGC","TTATGT","TTTTGG","CGCCTA","TTACTG"};
			when(dnaRepository.findById(Mockito.anyString())).thenReturn(Optional.of(dnaSequenceStored));
			boolean result = detectorService.isMutant(dnaSequence);
			assertTrue(result);
		} catch (InvalidDnaException e) {
			fail();
		}
		
	}
	
	@Test
	void validateDnaMutantTrueDiagonals() {
		try {
			String[] dnaSequence = {"AGGCGC","CAGGAC","TTAGGT","ATTTGG","CGTCTA","TTATTG"};
			when(dnaRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
			when(dnaRepository.save(Mockito.any())).thenReturn(new Dna());
			boolean result = detectorService.isMutant(dnaSequence);
			assertTrue(result);
		} catch (InvalidDnaException e) {
			fail();
		}
		
	}
	
	@Test
	void validateDnaMutantTrueDownDiagonals() {
		try {
			String[] dnaSequence = {"AGGCGC","CAGGAC","TTAGGT","ATTTCG","CGTCTA","TTATTG"};
			when(dnaRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());
			when(dnaRepository.save(Mockito.any())).thenReturn(new Dna());
			boolean result = detectorService.isMutant(dnaSequence);
			assertTrue(result);
		} catch (InvalidDnaException e) {
			fail();
		}	
	}
	
	@Test
	void matrixInvalid() {
		String[] dnaSequence = {"AGGCGC","CAGGAC"};
		assertThrows(InvalidDnaException.class, ()->detectorService.isMutant(dnaSequence));	
	}
	
	@Test
	void sequenceInvalid() {
		String[] dnaSequence = {"AGGCGC","CAGGAC","TTAGGT","ATHTCG","CGTCTA","TTATTG"};
		assertThrows(InvalidDnaException.class, ()->detectorService.isMutant(dnaSequence));	
	}
	
	@Test
	void lengthSequenceInvalid() {
		String[] dnaSequence = {"AGGCGCC","CAGGAC","TTAGGT","ATGTCG","CGTCTA","TTATTG"};
		assertThrows(InvalidDnaException.class, ()->detectorService.isMutant(dnaSequence));	
	}

}
