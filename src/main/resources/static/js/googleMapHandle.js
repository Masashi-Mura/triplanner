let marker;
let directionsRenderer;
let infoWindow;
let map;
let waypts;
let shopMarkers = [];
let currentInfoWindow = null;
let keywordPlaceName;//テスト

function initMap() {
	//マップ初期化
	const directionsService = new google.maps.DirectionsService();
	directionsRenderer = new google.maps.DirectionsRenderer();
	map = new google.maps.Map(document.getElementById("map"), {
		zoom: 8,
		center: { lat: 35.717, lng: 139.731 },
		scaleControl: true,
	});

	//ルート検索ボタンでgoogleMap経路検索api実行
	document.getElementById("routeSearch").addEventListener("click", () => {
		calculateAndDisplayRoute(directionsService);
	});

	//場所検索ボタンでgoogleMap経路検索apiから位置情報取得しマーカー表示
	marker = new google.maps.Marker();
	infoWindow = new google.maps.InfoWindow();	//情報ウィンドウクラス
	document.getElementById("submitPlaceName").addEventListener("click", () => {
		searchPlace(directionsService);
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

//単体場所確認のマーカー作成
function createMarker(response) {
	marker.setMap(map);
	marker.setPosition(response.routes[0].legs[0].start_location);

	map.setZoom(12);
	map.setCenter(response.routes[0].legs[0].start_location);

	//マーカーに目的地追加ボタンを作成
	const content = document.createElement("div");
	const addButton = document.createElement("button");
	addButton.id = "addPlaceNameButton";
	addButton.classList.add("poi-info-window", "full-width");
	addButton.textContent = "ここを経由地に追加";
	content.appendChild(addButton);

	//addButtonにクリックイベントリスナーを追加
	addButton.addEventListener("click", function() {
		addPlaceName(document.getElementById("searchPlaceName").value);
	});

	// keywordPlaceName の値を取得して新しい div に追加
	const keywordDiv = document.createElement("div");
	keywordDiv.classList.add("poi-info-window", "full-width");
	keywordDiv.textContent = document.getElementById("searchPlaceName").value;
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
function searchPlace(directionsService) {
	clear();
	directionsService
		.route({
			origin: document.getElementById("searchPlaceName").value,
			destination: document.getElementById("searchPlaceName").value,
			travelMode: google.maps.TravelMode.DRIVING,
			avoidHighways: true,
			avoidTolls: true,
		})
		.then((response) => {
			createMarker(response, map);
		})
		.catch((e) => window.alert("Directions request failed due to " + e));
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
	let enterdPlaceNames = [];
	$(".placeName").each(function() {
		enterdPlaceNames.push($(this).val());
	});

	//googleMapAPI検索オプションの経由地を設定
	for (let i = 0; enterdPlaceNames.length - 2 > i; i++) {
		waypts.push({
			location: enterdPlaceNames[i + 1],
			stopover: true, //経由地を停止地点とする
		});
	}

	//googleMapAPIでルートを検索、結果をマップと表に表示
	directionsService
		.route({
			origin: enterdPlaceNames[0],
			destination: enterdPlaceNames[enterdPlaceNames.length - 1],
			waypoints: waypts,
			optimizeWaypoints: document.getElementById('optimizeWaypoints').checked,  //経由地最適化の有無
			travelMode: google.maps.TravelMode.DRIVING,  //自動車で移動
			avoidHighways: !document.getElementById('permitHighways').checked,  //高速道路使用の有無
			avoidTolls: !document.getElementById('permitHighways').checked,  //有料道路使用の有無
		})
		.then((response) => {
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

		})
		.catch((e) => window.alert("Directions request failed due to " + e));
}

window.initMap = initMap;

console.log('googleMapHandle.js読み込み完了');