package com.prospecta.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.prospecta.modules.ResultList;
import com.prospecta.exceptions.EntryException;
import com.prospecta.modules.Entry;
import com.prospecta.modules.ResultDTO;
import com.prospecta.repository.EntryDAO;

@RestController
public class EntryController {

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	EntryDAO entryDAO;
	
	
	@GetMapping("/entries/{category}")
	public ResponseEntity<List<ResultDTO>> getEntriesHandler(@PathVariable("category") String category){
		
		
		ResultList resultList = restTemplate.getForObject("https://api.publicapis.org/entries", ResultList.class);
		
		List<Entry> entries = resultList.getEntries();
	
		List<ResultDTO> list = new ArrayList<>();
		
		for(Entry en:entries) {
			
			String[] categoryArray = en.getCategory().split(" ");
			
			if(categoryArray[0].equalsIgnoreCase(category))
				list.add(new ResultDTO(en.getApi(), en.getDescription()));
		}
		
		if(list.size() > 0) 
			return new ResponseEntity<List<ResultDTO>>(list, HttpStatus.OK);
		else
			throw new EntryException("No Entry found for the category: "+category);
				 
		
		
	}

	
	
	@PostMapping("/entries")
	public ResponseEntity<String> addEntriesHandler(@RequestBody Entry entry) {

		ResultList resultList = restTemplate.getForObject("https://api.publicapis.org/entries", ResultList.class);
		
		List<Entry> entries = resultList.getEntries();
		entries.add(entry);
		
		
		for(Entry en : entries) {
			entryDAO.saveAndFlush(en);
		}
		
		return new ResponseEntity<String>("Entry saved", HttpStatus.ACCEPTED);
	}
	
	
}
