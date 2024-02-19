// Value,Valuesで終わる変数：日付情報が無い時間を格納する変数
// Time,Timesで終わる変数：dayjsオブジェクトを格納する変数
let stayTimeValues;
let arrivalTimes;
let departTimes;
let durationSecondsValues;  //googleMapAPIの移動時間情報格納
let durationTimeValues;
let timeValue; //日付無時刻を格納（計算用）
let beforeSortableWaypoints;
let afterSortableWaypoints;
let waypointPlaceNames;

// 次行はdayjsのdurationを使う場合に使用するコード
// dayjs.extend(dayjs_plugin_duration);

// 時間の単位は(分)を基準で計算する
function changeTimes() {
	console.log('changeTimes()実行');

	// googleMapAPiの移動時間が適用できない場合は時間を非表示にする
	if (durationSecondsValues.length === 0) {
		$('.days').each(function() {
			$(this).text("");
		});
		$('.arrival-times').each(function() {
			$(this).text("");
		});
		$('.depart-times').each(function() {
			$(this).text("");
		});
		//次ページ遷移時のvalidation用データをリセット
		$('input[name="arrivalTimes"]').each(function() {
			$(this).val(null);
		});
		return;
	}

	// dayjsに時刻を加算した結果を保持するには、変数に代入が必要。
	// 最初の出発時刻をdayjsで保存
	const baseTime = dayjs("2020/1/1 00:00:00");
	let startTimeValue = $('.entered-start-time').val();//test
	departTimes = [];
	departTimes[0] = baseTime.add(startTimeValue.split(':')[0], 'hour').add(startTimeValue.split(':')[1], 'minute');
	console.log('departTimes[0]', departTimes[0]);//デバッグ用

	// 滞在時間HH:MMを取得
	stayTimeValues = [];
	$('.entered-stay-times').each(function() {
		stayTimeValues.push($(this).val());
	});

	// 移動時間をHH:MMで格納
	durationTimeValues = [];
	for (let i = 0; i < durationSecondsValues.length; i++) {
		durationTimeValues.push(secondsToHHMM(durationSecondsValues[i]));
	}
	console.log('durationTimeValues', durationTimeValues);//デバッグ用)

	// 到着時間、出発時間の全値を計算し格納
	// 到着時間１＝出発時間１＋移動時間１
	// 出発時間２＝到着時間１＋滞在時間１
	// 到着時間２＝出発時間２＋移動時間２
	arrivalTimes = [];
	for (let i = 0; i < $('.arrival-times').length; i++) {
		//到着時間＝出発時間＋移動時間
		arrivalTimes.push(departTimes[i].add(durationTimeValues[i].split(':')[0], 'h').add(durationTimeValues[i].split(':')[1], 'm'));
		console.log('arrivalTimes[' + i + ']', arrivalTimes[i]);//デバッグ用
		if (i === $('.arrival-times').length - 1) {
			break;
		}
		//出発時間＝到着時間＋滞在時間
		departTimes.push(arrivalTimes[i].add(stayTimeValues[i].split(':')[0], 'h').add(stayTimeValues[i].split(':')[1], 'm'));
		console.log('departTimes[' + (i + 1) + ']', departTimes[i + 1]);//デバッグ用
	}
	console.log('到着時間、出発時間の計算完了');//デバッグ用

	// 結果を表に出力
	let outputedDay = "1";
	$('.days').each(function(index) {
		let currentDay = arrivalTimes[index].format('D');
		if (outputedDay === currentDay) {
			$(this).text("");
		} else {
			$(this).text(currentDay + "日目");
			outputedDay = currentDay;
		}
	});
	$('.arrival-times').each(function(index) {
		$(this).text(arrivalTimes[index].format('HH:mm'));
	});
	$('.depart-times').each(function(index) {
		$(this).text(departTimes[index + 1].format('HH:mm'));
	});

	// 結果を（type="hidden"）に出力
	$('input[name="arrivalTimes"]').each(function(index) {
		$(this).val(arrivalTimes[index].format('YYYY-MM-DDTHH:mm'));
	});
	$('input[name="departTimes"]').each(function(index) {
		$(this).val(departTimes[index].format('YYYY-MM-DDTHH:mm'));
	});
};


// ss→hh:mm(繰り上げ)にフォーマットする関数
function secondsToHHMM(time) {
	let minutes = Math.ceil(time / 60);
	let hour = Math.floor(minutes / 60);
	minutes = minutes % 60;
	return `${hour}:${minutes}`;
}


// 経由地欄を１行追加、出発到着時間の表示をリセットする関数
function addPlaceNameField(placeName) {
	let tableHtml = '<tr>';
	tableHtml += '<td class="days"></td>';
	tableHtml += '<input type="hidden" name="days"><!-- hidden -->';
	tableHtml += '<td class="arrival-times"></td>';
	tableHtml += '<input type="hidden" name="arrivalTimes"><!-- hidden -->';
	tableHtml += '<td class="depart-times"></td>';
	tableHtml += '<input type="hidden" name="departTimes"><!-- hidden -->';
	tableHtml += '<td><input type="time" value="01:00" class="entered-stay-times changeTime"></td>';
	tableHtml += '<td class="pinName"></td>';
	if (typeof placeName === 'string') {
		tableHtml += '<td><input type="text" class="waypoint placeName" name="placeNames" placeholder="経由地" value="' + placeName + '"></td>';
	} else {
		tableHtml += '<td><input type="text" class="waypoint placeName" name="placeNames" placeholder="経由地" ></td>';
	}
	tableHtml += '<input type="hidden" name="latitudes"><!-- hidden -->';
	tableHtml += '<input type="hidden" name="longitudes"><!-- hidden -->';
	tableHtml += '<td><button class="handle btn btn-sm" type="button"><i class="bi bi-list"></i></button></td>';
	tableHtml += '<td><button class="deletePlaceNameField btn btn-sm" type="button"><i class="bi bi-x-square"></i></button></td>';
	tableHtml += '</tr>';

	$('#sortableRow').append(tableHtml);
	$('.deletePlaceNameField').on('click', deletePlaceNameField);
	$('.changeTime').on('change', changeTimes);

	//表の出発、到着時間をリセット
	durationSecondsValues = [];
	changeTimes();
};

// 経由地欄を１行削除、出発到着時間の表示をリセットする関数
function deletePlaceNameField() {
	$(this).closest("tr").find("*").remove();
	durationSecondsValues = [];
	changeTimes();
};

// 引数を経由地欄に追加する関数
function addPlaceName(keywordPlaceName) {
	//経由地欄情報を取得
	waypointPlaceNames = [];
	$(".waypoint").each(function() {
		waypointPlaceNames.push($(this).val());
	});
	//経由地に引数と同じ名称があるか確認
	if (waypointPlaceNames.includes(keywordPlaceName)) {
		let result = window.confirm("既に経由地に指定されています。\n追加しますか？");
		if (!result) {
			return;
		}
	}
	//空きの経由地欄に引数を追加
	let wayptsElements = document.querySelectorAll('.waypoint');
	for (let i = 0; i < waypointPlaceNames.length; i++) {
		if (wayptsElements[i].value === "") {
			wayptsElements[i].value = keywordPlaceName;
			return;
		}
	}
	//経由地欄を１つ追加し、引数を追加。
	addPlaceNameField(keywordPlaceName);

	//	window.alert("経由地がいっぱいです。\n経由地欄を追加してください。");
}

//SortableJSの導入 https://github.com/SortableJS/Sortable/blob/master/README.md
Sortable.create(sortableRow, {
	handle: '.handle',
	animation: 200,
	onStart: function() {
		//ドラッグ前の順序を格納
		beforeSortableWaypoints = [];
		$('.waypoint').each(function() {
			beforeSortableWaypoints.push(this.value);
		});
	},
	onEnd: function() {
		//ドラッグ後の順序を格納
		afterSortableWaypoints = [];
		$('.waypoint').each(function() {
			afterSortableWaypoints.push(this.value);
		});
		//ドラッグ前後での順序を比較
		let isEqual = JSON.stringify(beforeSortableWaypoints) === JSON.stringify(afterSortableWaypoints);
		if (!isEqual) {
			durationSecondsValues = [];
			changeTimes();
		}
	},
});


// 全てのchangeTimeクラスにイベントリスナーを設定
$('.changeTime').on('change', changeTimes);

// 経由地追加ボタンにイベントリスナーを設定
$('#addPlaceNameField').on('click', addPlaceNameField);

// 削除ボタンにイベントリスナーを設定
$('.deletePlaceNameField').on('click', deletePlaceNameField);

// 開発用出力 読み込み完了
console.log('tripTableHandler.js読み込み完了');