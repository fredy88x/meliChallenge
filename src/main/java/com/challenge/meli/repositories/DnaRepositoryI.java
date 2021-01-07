package com.challenge.meli.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.challenge.meli.entitys.Dna;

@Repository
public interface DnaRepositoryI extends CrudRepository<Dna, String> {
	
}
