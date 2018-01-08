package com.example.demo.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.Planeta;
import com.example.demo.repository.PlanetaRepository;


@RestController

@RequestMapping(value = "/planeta", method = RequestMethod.POST, produces="application/json")

public class PlanetaController {

	  @Autowired
	  private PlanetaRepository planetaRepository;
	  private RestTemplate restTemplate;


	  //Insere um planeta
	  @RequestMapping(method = RequestMethod.POST)
	  public Map<String, Object> createPlaneta(@RequestBody Map<String, Object> planetaMap){
		  
	    Planeta planeta = new Planeta(planetaMap.get("nome").toString(),
	    		planetaMap.get("clima").toString(),
	    		planetaMap.get("terreno").toString(),
	    		contarAparicoes(planetaMap.get("nome").toString()));

	    planetaRepository.save(planeta);
	    
	    Map<String, Object> response = new LinkedHashMap<String, Object>();
	    response.put("message", "Planeta inserido com sucesso");
	    response.put("planeta", planeta);
	    return response;
	  }

	  
	  //Busca um planeta pelo ID
	  @RequestMapping(method = RequestMethod.GET, value="/{planetaId}")
	  public Planeta getPlanetakDetails(@PathVariable("planetaId") String planetaId){
	    return planetaRepository.findOne(planetaId);
	  }
	  
	  //Busca por nome
	  @RequestMapping(method = RequestMethod.GET, value="/buscar")
	  public Planeta getPlanetaName(@RequestParam("nome") String nome){
	    return planetaRepository.findByNome(nome);
	  }	  
	  
	  
	  //Atualiza planeta
	  @RequestMapping(method = RequestMethod.PUT, value="/{planetaId}")
	  public Map<String, Object> editPlaneta(@PathVariable("planetaId") String planetaId,
	      @RequestBody Map<String, Object> planetaMap){
		  	Planeta planeta = new Planeta(planetaMap.get("nome").toString(),
		  			planetaMap.get("clima").toString(),
		  			planetaMap.get("terreno").toString(),
		  			contarAparicoes(planetaMap.get("nome").toString()));
	    planeta.setId(planetaId);

	    Map<String, Object> response = new LinkedHashMap<String, Object>();
	    response.put("message", "Planeta atualizado com sucesso");
	    response.put("planeta", planetaRepository.save(planeta));
	    return response;
	  }
	  
	  //Lista todos os planetas
	  @RequestMapping(method = RequestMethod.GET,  value="/listar")
	  public Map<String, Object> getAllPlanetas(){
	    List<Planeta> planetas = planetaRepository.findAll();
	    Map<String, Object> response = new LinkedHashMap<String, Object>();
	    response.put("totalPlanetas", planetas.size());
	    response.put("planetas", planetas);
	    return response;
	  }
	  
	  //Deleta planeta
	  @RequestMapping(method = RequestMethod.DELETE, value="/{planetaId}")
	  public Map<String, String> deletePlaneta(@PathVariable("planetaId") String planetaId){
	    planetaRepository.delete(planetaId);
	    Map<String, String> response = new HashMap<String, String>();
	    response.put("message", "Planeta deletado com sucesso");

	    return response;
	  }
	  
	  
	  
	  //Conecta a api de starw wars e insere no banco de dados em quantos filmes apareceu.
	  public int contarAparicoes(String nomePlaneta) {
		  
		  int aparicoes=0;
		  
		  
		  
		    RestTemplate restTemplate = new RestTemplate();
		    HttpHeaders headers = new HttpHeaders();
		    headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    headers.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:55.0) Gecko/20100101 Firefox/55.0");
		    HttpEntity<?> entity = new HttpEntity<>(headers);


		    HttpEntity<String> response = restTemplate.exchange("https://swapi.co/api/planets/?search="+nomePlaneta, HttpMethod.GET, entity, String.class);

		    String textoJson= response.getBody();
		    
		    
		    
		    try {
		    	
		        JSONObject obj = new JSONObject(textoJson);
		        JSONArray resultado = obj.getJSONArray("results");
		        
		        JSONObject filmes = resultado.getJSONObject(0);

		        
		        JSONArray filmesArray = filmes.getJSONArray("films");
		        aparicoes = filmesArray.length();
				
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    
		    return aparicoes;
		     
		     
		    
	  }
	  
	  	
	}
