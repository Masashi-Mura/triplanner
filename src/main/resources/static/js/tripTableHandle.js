
let enteredTimes;
let arrivalTimes;
let departTimes;
let durationSecondsTimes;  //googleMapAPIの移動時間情報格納
let durationTimes;
let beforeSortableWaypoints;
let afterSortableWaypoints;

// 時間の単位は(分)を基準で計算する
function changeTimes() {
	console.log('changeTimes()実行');

	// googleMapAPiの移動時間が適用できない場合は時間を非表示にする
	if (durationSecondsTimes.length === 0) {
		$('.days').each(function() {
			$(this).text("");
		});
		$('.arrival-times').each(function() {
			$(this).text("");
		});
		$('.depart-times').each(function() {
			$(this).text("");
		});
		return;
	}

	// 配列を初期化
	enteredTimes = [];
	durationTimes = [];

	// googleMapAPIの移動時間(秒)を(分)に変換
	durationSecondsTimes.forEach((seconds) => {
		let apiDurationTime = Math.floor(seconds / 60);
		durationTimes.push(apiDurationTime);
	});

	// 表に入力された全時間を分で保持
	$('.inputed-time').each(function() {
		let minutesValue = this.valueAsNumber / 1000 / 60;
		enteredTimes.push(minutesValue);
	});

	// arrivalTimeArrayとdepartTimeArrayの全値の結果を保持
	departTimes = [enteredTimes[0]]; //出発地の時間のみ入力時間を使用
	arrivalTimes = [];
	let arrivalTimesLength = $('.arrival-times').length;

	for (let i = 0; i < arrivalTimesLength; i++) {
		if (durationTimes.length - 1 < i) {
			durationTimes.push(0);
		}
		arrivalTimes.push(departTimes[i] + durationTimes[i]);
		if (i < arrivalTimesLength - 1) {
			departTimes.push(arrivalTimes[i] + enteredTimes[i + 1]);
		}
	}

	// 結果を表に出力
	let outputedDay = 1;
	$('.days').each(function(index) {
		let currentDay = Math.floor(arrivalTimes[index] / 60 / 24) + 1;
		if (outputedDay !== currentDay) {
			$(this).text(currentDay + "日目");
			outputedDay = currentDay;
		} else {
			$(this).text("");
		}

	});
	$('.arrival-times').each(function(index) {
		$(this).text(minutesToHHMM(arrivalTimes[index]));
	});
	$('.depart-times').each(function(index) {
		$(this).text(minutesToHHMM(departTimes[index + 1]));
	});
};

// mm→hh:mmにフォーマットする関数
function minutesToHHMM(time) {
	let hour = Math.floor(time / 60);
	let minutes = time % 60;
	if (hour >= 24) {
		hour = hour % 24;
		hour = hour.toString().padStart(2, '0');
		minutes = minutes.toString().padStart(2, '0');
		return `${hour}:${minutes}`;
	}
	hour = hour.toString().padStart(2, '0');
	minutes = minutes.toString().padStart(2, '0');
	return `${hour}:${minutes}`;
}


// 経由地欄を１行追加、出発到着時間の表示をリセットする関数
function addPlaceNameField() {
	let tableHtml = '<tr>';
	tableHtml += '<td class="days"></td>';
	tableHtml += '<td class="arrival-times"></td>';
	tableHtml += '<td class="depart-times"></td>';
	tableHtml += '<td><input type="time" value="01:00" class="inputed-time changeTime"></td>';
	tableHtml += '<td class="pinName"></td>';
	tableHtml += '<td><input type="text" class="waypoint" name="placeName" placeholder="経由地"></td>';
	tableHtml += '<td><button class="handle btn btn-sm" type="button"><i class="bi bi-list"></i></button></td>';
	tableHtml += '<td>&nbsp;&nbsp;<button class="deletePlaceNameField btn btn-sm" type="button"><i class="bi bi-x-square"></i></button></td>';
	tableHtml += '</tr>';

	$('#sortableRow').append(tableHtml);
	$('.deletePlaceNameField').on('click', deletePlaceNameField);
	$('.changeTime').on('change', changeTimes);

	//表の出発、到着時間をリセット
	durationSecondsTimes = [];
	changeTimes();
};

// 経由地欄を１行削除、出発到着時間の表示をリセットする関数
function deletePlaceNameField() {
	$(this).closest("tr").find("*").remove();
	durationSecondsTimes = [];
	changeTimes();
};

// 全てのchangeTimeクラスにイベントリスナーを設定
$('.changeTime').on('change', changeTimes);

// 経由地追加ボタンにイベントリスナーを設定
$('#addPlaceNameField').on('click', addPlaceNameField);

// 削除ボタンにイベントリスナーを設定
$('.deletePlaceNameField').on('click', deletePlaceNameField);

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
		//		let isEqual = beforeSortableWaypoints.every((value, index) => value === afterSortableWaypoints[index]);
		if (!isEqual) {
			durationSecondsTimes = [];
			changeTimes();
		}
	},
});


// 開発用出力 読み込み完了
console.log('tripTableHandler.js読み込み完了');