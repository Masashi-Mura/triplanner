package com.example.triplanner.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ApiController {

	private String externalApiUrl = "http://webservice.recruit.co.jp/hotpepper/gourmet/v1/";

	@Value("${hotpepper.key}")
	private String apiKey;
	
	@GetMapping("/getHottpepperGourmet")
	public String fetchDataFromExternalAPI(
			@RequestParam double lat,
			@RequestParam double lng
	) {
		
		int range = 5;
		int order = 4;

		// APIクエリ作成
		String apiUrl = String.format("%s?key=%s&lat=%s&lng=%s&range=%s&order=%s&format=json",
				externalApiUrl, apiKey, lat, lng, range, order);

		// RestTemplateを使用してGETリクエストを送信
		RestTemplate restTemplate = new RestTemplate();
		return restTemplate.getForObject(apiUrl, String.class);
	}

}
