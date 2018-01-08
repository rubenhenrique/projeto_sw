package com.example.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Planeta;

public interface PlanetaRepository extends MongoRepository<Planeta, String>{

	@Query
	Planeta findByNome(@Param(value = "nome") String nome);

}
