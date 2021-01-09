package com.challenge.meli.entitys;




import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@RedisHash("Dna")
public class Dna implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id 
	private String dnaSequence;
	private boolean mutant;

}
