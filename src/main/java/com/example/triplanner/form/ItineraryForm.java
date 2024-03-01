package com.example.triplanner.form;

import java.time.LocalDateTime;
import java.util.List;

import com.example.triplanner.validation.constraints.NotEmptyAtEven;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItineraryForm {
	//以下、旅程作成Formデータ（itinerariesテーブル保存分）
	//旅作成Formデータから作成したデータ。旅作成戻り時にデータの保持不要
	private List<Integer> rowSequences;

	@Valid
	private List<@NotNull(message="行動のプルダウンを全て選択してください") Integer> purposeIds;//itineraryでユーザ入力

	private List<LocalDateTime> startTimes;

	private List<LocalDateTime> endTimes;

	private List<String> departureNames;

	private List<Integer> departurePrefectureIds;//APIで取得

	private List<String> arrivalNames;

	@NotEmptyAtEven //0行始まり
	private List<String> titles;//itineraryでユーザ入力

	private List<String> descriptions;//itineraryでユーザ入力

	//以下、旅程作成Formデータ（tripsテーブル保存分）
	//旅程作成のユーザ入力データ。旅作成戻り時にデータの保持必要
	@NotEmpty(message = "旅のタイトルを入力してください")
	private String tripTitle;//itineraryでユーザ入力

	@NotNull
	private Integer publicId;//itineraryでユーザ入力

	@NotNull(message = "タグを１つ以上選択してください")
	private List<Integer> tagIds;//itineraryでユーザ入力

	
	//以下、確認画面出力用（旅程作成→旅程確認のコントローラでデータ作成）
	private List<String> purposeStrings;

	private String publicString;

	private List<String> tagStrings;

	private List<String> usedBrDescriptions;//改行を<br>に変換したデータ

}
