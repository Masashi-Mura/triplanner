package com.example.triplanner.form;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ItineraryForm {

	private List<Integer> rowSequences;

//	@Size(min=1, message = "行動のプルダウンを全て選択してください")
	private List<Integer> purposeIds;//itineraryでユーザ入力

	private List<LocalDateTime> startTimes;

	private List<LocalDateTime> endTimes;

	private List<String> departureNames;

	private List<Integer> departurePrefectureIds;//APIで取得

	private List<String> arrivalNames;

	private List<String> titles;//itineraryでユーザ入力

	private List<String> descriptions;//itineraryでユーザ入力
	
	//以下、tripsテーブルで使用
//	@NotEmpty(message = "旅程のタイトルを入力してください")
	private String tripTitle;//itineraryでユーザ入力
	
//	@NotNull
	private Integer publicId;//itineraryでユーザ入力
	
//	@NotNull(message = "タグを１つ以上選択してください")
	private List<Integer> tagIds;//itineraryでユーザ入力
	
	//以下、確認画面出力用
	private List<String> purposeStrings;
	
	private String publicString;
	
	private List<String> tagStrings;
	
	//以下、遷移判定用
	private Boolean fromConfirm = false;
}
