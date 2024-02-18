package com.example.triplanner.component;

import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.example.triplanner.entity.Prefecture;
import com.example.triplanner.repository.PrefectureRepository;

@Component
public class ReverceGeocoder {

	@Autowired
	private PrefectureRepository prefectureRepository;

	public Integer getPrefectureId(double lat, double lng) {

		final String externalApiUrl = "https://geoapi.heartrails.com/api/json?method=searchByGeoLocation&";

		// APIクエリ作成
		String apiUrl = String.format("%sx=%s&y=%s", externalApiUrl, lng, lat);

		// RestTemplateを使用してGETリクエストを送信
		RestTemplate restTemplate = new RestTemplate();
		String jsonString = restTemplate.getForObject(apiUrl, String.class);
		JSONObject jsonObj = new JSONObject(jsonString);
		// APIから都道府県名が返ってきたか確認
		if (jsonObj.getJSONObject("response").has("error")) {
			return 0;
		}
		String prefectureName = jsonObj.getJSONObject("response").getJSONArray("location").getJSONObject(0).getString("prefecture");

		//都道府県名からidを取得
		List<Prefecture> prefectures = prefectureRepository.findAllByOrderById();
		Integer prefectureId[] = new Integer[1];//forEach内で値を変えるため配列で宣言
		prefectures.forEach(prefecture -> {
			if (prefecture.getName().equals(prefectureName)) {
				prefectureId[0] = prefecture.getId();
			}
		});
		//idが取得できなかった場合
		if (prefectureId[0] == null) {
			prefectureId[0] = 0;
		}

		//idを返す
		return prefectureId[0];
	}
}
