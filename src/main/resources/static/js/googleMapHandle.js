let marker;
let directionsRenderer;
let infoWindow;
let map;
let waypts;
let enteredPlaceNames;
let shopMarkers = [];
let currentInfoWindow = null;
let startOrDestination;

function initMap() {
	//マップ初期表示設定
	const directionsService = new google.maps.DirectionsService();
	directionsRenderer = new google.maps.DirectionsRenderer();
	map = new google.maps.Map(document.getElementById("map"), {
		zoom: 8,
		center: { lat: 35.717, lng: 139.731 },
		scaleControl: true,
	});

	marker = new google.maps.Marker();
	infoWindow = new google.maps.InfoWindow();	//情報ウィンドウクラス

	//出発地検索ボタンでマップにマーカー表示しスタート地点に設定
	document.getElementById("submitStartPlaceName").addEventListener("click", () => {
		searchPlace(directionsService, "start");
	});

	//目的地検索ボタンでマップにマーカー表示し目的地に追加
	document.getElementById("submitPlaceName").addEventListener("click", () => {
		searchPlace(directionsService, "destination");
	});

	//ルート検索ボタンでgoogleMap経路検索api実行
	document.getElementById("routeSearch").addEventListener("click", () => {
		calculateAndDisplayRoute(directionsService);
	});

	//マップ中心付近のショップをapi検索
	document.getElementById("searchShop").addEventListener("click", () => {
		let latlng = map.getCenter();
		searchShop(latlng);
	});
}

//マップ中心付近のショップを検索、結果をリストで表示
function searchShop(latlng) {
	// 表示中のショップ情報をクリア
	document.getElementById('result').innerHTML = '';
	// マップ中心の緯度経度をホットペッパーAPIに送りショップのjsonデータ取得
	let lat = latlng.lat();
	let lng = latlng.lng();
	var apiUrl = `/getHottpepperGourmet?lat=${lat}&lng=${lng}`;
	fetch(apiUrl)  //controllerを経由してホットペッパーのAPI使用
		.then(response => response.json())
		.then(data => {

			// マーカー初期化
			for (let i = 0; i < shopMarkers.length; i++) {
				shopMarkers[i].setMap(null);
			}
			shopMarkers = [];

			// マーカー,infoWindow作成
			for (let i = 0; i < data.results.shop.length; i++) {
				//マーカー作成
				const tmpMarker = new google.maps.Marker({
					position: { lat: data.results.shop[i].lat, lng: data.results.shop[i].lng },
					label: (i + 1) + "",
					map: map,
				});
				shopMarkers.push(tmpMarker);
				shopMarkers[i].setMap(map);

				// マーカーに対応するボタンの作成
				// ボタン要素を作成
				const shopButton = document.createElement('button');

				// ボタン内に表示するテキストを設定
				shopButton.innerHTML = (i + 1) + '<br>' +
					data.results.shop[i].name + '<br>';

				// ボタンにクリックイベントを追加
				shopButton.addEventListener('click', function() {
					// ボタンがクリックされた時の処理
					if (currentInfoWindow) {
						currentInfoWindow.close();
					}
					showInfoWindow();
				});

				// 指定したIDの要素にボタンを追加
				const targetElementId = 'result';
				document.getElementById(targetElementId).appendChild(shopButton);

				function showInfoWindow() {
					// infoWindow作成
					infoWindow = new google.maps.InfoWindow();
					//目的地追加ボタンを作成
					const content = document.createElement("div");
					const addButton = document.createElement("button");
					addButton.id = "addPlaceNameButton";
					addButton.textContent = "ここを経由地に追加";
					content.appendChild(addButton);

					//addButtonにクリックイベントリスナーを追加
					addButton.addEventListener("click", function() {
						addPlaceName(data.results.shop[i].name);
					});


					//店名を新しい div に追加
					const keywordDiv = document.createElement("div");
					keywordDiv.textContent = data.results.shop[i].name;
					content.appendChild(keywordDiv);

					// 店のurlを新しい div に追加
					const urlDiv = document.createElement("div");
					urlDiv.textContent = data.results.shop[i].urls.pc;
					content.appendChild(urlDiv);

					//infoWindowにcontentを格納
					infoWindow.setContent(content);
					infoWindow.open(map, shopMarkers[i]);
					currentInfoWindow = infoWindow;


					//markerクリックでinfoWindowを表示
					shopMarkers[i].addListener("click", () => {
						infoWindow.open(map, shopMarkers[i]);
					})
				}
			}
		})
		.catch(error => console.error('Error fetching data:', error));

}

//出発地、目的地検索時のマーカー作成
function createMarker(response, searchPlaceName) {
	marker.setMap(map);
	marker.setPosition(response.routes[0].legs[0].start_location);

	map.setZoom(12);
	map.setCenter(response.routes[0].legs[0].start_location);

	const content = document.createElement("div");
	// keywordPlaceName の値を取得して新しい div に追加
	const keywordDiv = document.createElement("div");
	keywordDiv.classList.add("poi-info-window", "full-width");
	keywordDiv.textContent = response.request.origin.query;
	content.appendChild(keywordDiv);

	// response.routes[0].legs[0].start_address の値を取得して新しい div に追加
	const startAddressDiv = document.createElement("div");
	startAddressDiv.classList.add("poi-info-window", "full-width");
	startAddressDiv.textContent = response.routes[0].legs[0].start_address;
	content.appendChild(startAddressDiv);

	//検索後、infoWindowを自動表示
	infoWindow.setContent(content);
	infoWindow.open(map, marker);

	//infoWindowを閉じてもmarkerクリックでinfoWindowを表示
	marker.addListener("click", () => {
		infoWindow.open(map, marker);
	})

}

//単体の場所検索関数（ルート検索と同じ地点にするためdirectionsを使用)
function searchPlace(directionsService, startOrDestination) {
	clear();
	let keywordPlaceName;
	if (startOrDestination == "start") {
		keywordPlaceName = document.getElementById("searchStartPlaceName").value;
	} else {
		keywordPlaceName = document.getElementById("searchPlaceName").value;
	}
	let request = {
		origin: keywordPlaceName,
		destination: keywordPlaceName,
		travelMode: google.maps.TravelMode.DRIVING,
		avoidHighways: true,
		avoidTolls: true,
	};

	directionsService.route(request, function(response, status) {
		if (status !== "OK") {
			alertErrorMessage(status);
		} else {

			switch (startOrDestination) {
				case "start":
					createMarker(response);
					setStartPlaceName(document.getElementById("searchStartPlaceName").value);
					break;
				case "destination":
					createMarker(response);
					addPlaceName(document.getElementById("searchPlaceName").value);
					break;
				default:
					window.alert("出発地、目的地の検索でエラーが発生しました。エラーの発生状況を開発者に問い合わせて下さい。")
					break;
			}
		}
	})
}

//directionsService.routeエラー時のエラーメッセージ表示
function alertErrorMessage(status) {
	switch (status) {
		case "INVALID_REQUEST":
			window.alert("リクエストが無効です。エラー名：" + status + "。エラーの発生状況を開発者に問い合わせて下さい。")
			break;
		case "MAX_WAYPOINTS_EXCEEDED":
			window.alert("経由地点が多すぎます。経由地点を減らして再度実行してください。")
			break;
		case "NOT_FOUND":
			window.alert("名称が見つかりません。名称を変更するか、名称の後ろに都道府県や地名を追加して再度実行してください。");
			break;
		case "OVER_QUERY_LIMIT":
			window.alert("短期間にリクエストの制限回数を超えました。少し時間をおいて追加ボタンを押して下さい。")
			break;
		case "REQUEST_DENIED":
			window.alert("リクエストが拒否されました。エラー名：" + status + "。エラーの発生状況を開発者に問い合わせて下さい。")
			break;
		case "UNKNOWN_ERROR":
			window.alert("サーバーでエラーが発生しました。時間をおいて追加ボタンを押して下さい。")
			break;
		case "ZERO_RESULTS":
			window.alert("ルートが見つかりません。目的地を変更してください。")
			break;
		default:
			window.alert("エラーが発生しました。エラー名：" + status + "。エラーの発生状況を開発者に問い合わせて下さい。")
	}
}

//マップ初期化関数
function clear() {
	marker.setMap(null);
	directionsRenderer.setMap(null);
	infoWindow.close();
	for (let i = 0; i < shopMarkers.length; i++) {
		shopMarkers[i].setMap(null);
	}
}

//ルート検索関数
function calculateAndDisplayRoute(directionsService) {
	clear();
	directionsRenderer.setMap(map)
	waypts = [];

	//空欄の経由地欄を削除
	$(".waypoint").each(function() {
		if ($(this).val() === '') {
			$(this).closest("tr").find("*").remove();
		}
	});

	//入力された全地名を取得
	enteredPlaceNames = [];
	$(".placeName").each(function() {
		enteredPlaceNames.push($(this).val());
	});

	//googleMapAPI検索オプションの経由地を設定
	for (let i = 0; enteredPlaceNames.length - 2 > i; i++) {
		waypts.push({
			location: enteredPlaceNames[i + 1],
			stopover: true, //経由地を停止地点とする
		});
	}

	//googleMapAPIでルートを検索、結果をマップと表に表示
	let request = {
		origin: enteredPlaceNames[0],
		destination: enteredPlaceNames[enteredPlaceNames.length - 1],
		waypoints: waypts,
		optimizeWaypoints: document.getElementById('optimizeWaypoints').checked,  //経由地最適化の有無
		travelMode: google.maps.TravelMode.DRIVING,  //自動車で移動
		avoidHighways: !document.getElementById('permitHighways').checked,  //高速道路使用の有無
		avoidTolls: !document.getElementById('permitHighways').checked,  //有料道路使用の有無
	}

	directionsService.route(request, function(response, status) {
		if (status == "NOT_FOUND") {
			let errorPlaceNames = [];
			response.geocoded_waypoints.forEach((waypoint, index) => {
				if (waypoint.geocoder_status !== "OK") {
					errorPlaceNames.push(enteredPlaceNames[index]);
				}
			})
			window.alert(errorPlaceNames + "の名称が見つかりません。名称を変更するか、名称の後ろに都道府県や地名を追加して再度実行してください。");
		} else if (status !== "OK") {
			alertErrorMessage(status);
		} else {
			directionsRenderer.setDirections(response);

			const route = response.routes[0];

			//経由地最適化適用で経由地、滞在時間を並び替えて表示
			//googleMapApiのメモ
			//response.geocoded_waypoints[0].place_idで位置情報取得。
			//response.geocoded_waypointsは最適化有の場合、並び替え後の順で格納されている。
			//response.routes[0].waypoint_orderに経由地の順番が配列で格納されている。
			if (response.request.optimizeWaypoints === true) {
				//経由地並び替え
				$(".waypoint").each(function(index) {
					$(this).val(waypts[route.waypoint_order[index]].location);
				});
				//滞在時間並び替え
				let tmpStayTimeValue = [];
				$("#sortableRow .entered-stay-times").each(function() {
					tmpStayTimeValue.push($(this).val());
				});
				$("#sortableRow .entered-stay-times").each(function(index) {
					$(this).val(tmpStayTimeValue[route.waypoint_order[index]]);
				});
			}

			//ピン情報を表に表示
			const alphabet = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K'];
			$(".pinName").each(function(index) {
				$(this).text(alphabet[index]);
			});

			//移動時間を表に反映
			durationSecondsValues = [];
			route.legs.forEach((leg) => {
				durationSecondsValues.push(leg.duration.value);
			});
			changeTimes();

			//場所の緯度経度を（type="hidden"）に出力
			//latitudeを出力
			let latitudesLength = route.legs.length;;
			$('input[name="latitudes"]').each(function(index) {
				if (index < latitudesLength - 1) {
					$(this).val(route.legs[index].start_location.lat());
				} else if (index == latitudesLength - 1) {
					$(this).val(route.legs[index - 1].end_location.lat());
				}
			});
			//longitudeを出力
			$('input[name="longitudes"]').each(function(index) {
				if (index < latitudesLength - 1) {
					$(this).val(route.legs[index].start_location.lng());
				} else if (index == latitudesLength - 1) {
					$(this).val(route.legs[index - 1].end_location.lng());
				}
			});
		}
	})

}

window.initMap = initMap;

console.log('googleMapHandle.js読み込み完了');