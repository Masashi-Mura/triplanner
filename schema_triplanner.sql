DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS region_master CASCADE;
DROP TABLE IF EXISTS prefecture_master CASCADE;
DROP TABLE IF EXISTS purpose_master CASCADE;
DROP TABLE IF EXISTS public_master CASCADE;
DROP TABLE IF EXISTS tag_master CASCADE;
DROP TABLE IF EXISTS trip CASCADE;
DROP TABLE IF EXISTS tag_map CASCADE;
DROP TABLE IF EXISTS itinerary CASCADE;
DROP TABLE IF EXISTS favorite CASCADE;

-- ▽▽▽▽以下マスタ関係▽▽▽▽
CREATE TABLE IF NOT EXISTS region_master (
  id INTEGER CHECK(id > 0) NOT NULL,
  name VARCHAR(255) DEFAULT NULL,
  name_kana VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS prefecture_master (
  id INTEGER CHECK(id >= 1) NOT NULL,
  region_id INTEGER DEFAULT NULL,
  name VARCHAR(255) DEFAULT NULL,
  name_kana VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE prefecture_master ADD CONSTRAINT FK_prefecture_region FOREIGN KEY (region_id) REFERENCES region_master (id);

-- 日本語文字化けのため英語のテスト用データを仮使用
INSERT INTO region_master VALUES
  (1,'hokkaido_region','kana_hokkaido_region'),
  (2,'touhoku_region','kana_tohoku_region');
  -- (1,'北海道地方','ホッカイドウ'),
  -- (2,'東北地方','トウホクチホウ'),
  -- (3,'関東地方','カントウチホウ'),
  -- (4,'中部地方','チュウブチホウ'),
  -- (5,'近畿地方','キンキチホウ'),
  -- (6,'中国地方','チュウゴクチホウ'),
  -- (7,'四国地方','シコクチホウ'),
  -- (8,'九州地方','キュウシュウチホウ');

-- 日本語文字化けのため英語のテスト用データを仮使用
INSERT INTO prefecture_master VALUES
  (1,1,'hokkaido','kana_hokkaido'),
  (2,2,'aomoriken','kana_aomoriken'),
  (3,2,'iwateken','kana_iwateken'),
  (4,2,'miyagiken','kana_miyagiken'),
  (5,2,'akitaken','kana_akitaken');
  -- (1,1,'北海道','ホッカイドウ'),
  -- (2,2,'青森県','アオモリケン'),
  -- (3,2,'岩手県','イワテケン'),
  -- (4,2,'宮城県','ミヤギケン'),
  -- (5,2,'秋田県','アキタケン'),
  -- (6,2,'山形県','ヤマガタケン'),
  -- (7,2,'福島県','フクシマケン'),
  -- (8,3,'茨城県','イバラキケン'),
  -- (9,3,'栃木県','トチギケン'),
  -- (10,3,'群馬県','グンマケン'),
  -- (11,3,'埼玉県','サイタマケン'),
  -- (12,3,'千葉県','チバケン'),
  -- (13,3,'東京都','トウキョウト'),
  -- (14,3,'神奈川県','カナガワケン'),
  -- (15,4,'新潟県','ニイガタケン'),
  -- (16,4,'富山県','トヤマケン'),
  -- (17,4,'石川県','イシカワケン'),
  -- (18,4,'福井県','フクイケン'),
  -- (19,4,'山梨県','ヤマナシケン'),
  -- (20,4,'長野県','ナガノケン'),
  -- (21,4,'岐阜県','ギフケン'),
  -- (22,4,'静岡県','シズオカケン'),
  -- (23,4,'愛知県','アイチケン'),
  -- (24,5,'三重県','ミエケン'),
  -- (25,5,'滋賀県','シガケン'),
  -- (26,5,'京都府','キョウトフ'),
  -- (27,5,'大阪府','オオサカフ'),
  -- (28,5,'兵庫県','ヒョウゴケン'),
  -- (29,5,'奈良県','ナラケン'),
  -- (30,5,'和歌山県','ワカヤマケン'),
  -- (31,6,'鳥取県','トットリケン'),
  -- (32,6,'島根県','シマネケン'),
  -- (33,6,'岡山県','オカヤマケン'),
  -- (34,6,'広島県','ヒロシマケン'),
  -- (35,6,'山口県','ヤマグチケン'),
  -- (36,7,'徳島県','トクシマケン'),
  -- (37,7,'香川県','カガワケン'),
  -- (38,7,'愛媛県','エヒメケン'),
  -- (39,7,'高知県','コウチケン'),
  -- (40,8,'福岡県','フクオカケン'),
  -- (41,8,'佐賀県','サガケン'),
  -- (42,8,'長崎県','ナガサキケン'),
  -- (43,8,'熊本県','クマモトケン'),
  -- (44,8,'大分県','オオイタケン'),
  -- (45,8,'宮崎県','ミヤザキケン'),
  -- (46,8,'鹿児島県','カゴシマケン'),
  -- (47,8,'沖縄県','オキナワケン');


CREATE TABLE IF NOT EXISTS purpose_master (
  id INTEGER CHECK(id > 0) NOT NULL,
  purpose VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- 日本語文字化けのため英語のテスト用データを仮使用
INSERT INTO purpose_master VALUES
  (1,'idou'),
  (2,'kankou'),
  (3,'syokuji'),
  (4,'syukuhaku'),
  (5,'other');

CREATE TABLE IF NOT EXISTS public_master (
  id INTEGER CHECK(id > 0) NOT NULL,
  public_option VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- 日本語文字化けのため英語のテスト用データを仮使用
INSERT INTO public_master VALUES
  (1,'ippan_koukai'),
  (2,'no_koukai'),
  (3,'gentei_koukai');

CREATE TABLE IF NOT EXISTS tag_master (
  id INTEGER CHECK(id > 0) NOT NULL,
  tag_name VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- 日本語文字化けのため英語のテスト用データを仮使用
INSERT INTO tag_master VALUES
  (1,'hitoritabi'),
  (2,'couple'),
  (3,'kodure'),
  (4,'friends');

-- ▽▽▽▽以下テーブル作成▽▽▽▽
CREATE TABLE IF NOT EXISTS users (
  user_id SERIAL NOT NULL,
  authority_id VARCHAR(255) NOT NULL,
  nickname VARCHAR(255) NOT NULL,
  login_password VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (user_id)
);

-- usersテーブルテスト用データ
INSERT INTO users (authority_id, nickname, login_password, created_at, updated_at) VALUES
  ('test1@test.com','tester1','password','2023-01-01 19:30:33.423588','2023-01-01 19:30:33.423588'),
  ('test2@test.com','tester2','password','2023-01-02 19:30:33.423588','2023-01-02 19:30:33.423588');

CREATE TABLE IF NOT EXISTS trip (
  id SERIAL NOT NULL,
  total_trip_days INTEGER NOT NULL,
  total_duration_minutes INTEGER NOT NULL,
  public_master_id INTEGER NOT NULL,
  user_id INTEGER NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  deleted BOOL NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE trip ADD CONSTRAINT FK_trip_user FOREIGN KEY (user_id) REFERENCES users (user_id);

-- tripテーブルテスト用データ
INSERT INTO trip (total_trip_days, total_duration_minutes, public_master_id, user_id, created_at, updated_at, deleted) VALUES
  (1,100,1,1,'2023-01-01 19:30:33.423588','2023-01-01 19:30:33.423588', false),
  (2,200,2,1,'2023-01-02 19:30:33.423588','2023-01-02 19:30:33.423588', false),
  (3,300,1,2,'2023-01-03 19:30:33.423588','2023-01-03 19:30:33.423588', false),
  (4,400,2,2,'2023-01-04 19:30:33.423588','2023-01-04 19:30:33.423588', false);

CREATE TABLE IF NOT EXISTS tag_map (
  id SERIAL NOT NULL,
  trip_id INTEGER NOT NULL,
  tag_master_id INTEGER NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE tag_map ADD CONSTRAINT FK_tag_trip FOREIGN KEY (trip_id) REFERENCES trip (id);
ALTER TABLE tag_map ADD CONSTRAINT FK_tag_tag FOREIGN KEY (tag_master_id) REFERENCES tag_master (id);

-- tag_mapテーブルテスト用データ
INSERT INTO tag_map (trip_id, tag_master_id) VALUES
  (1,1),
  (1,2),

  (2,2),
  (2,3),

  (3,1),
  (3,2),
  (3,3),

  (4,4);

CREATE TABLE IF NOT EXISTS itinerary (
  id SERIAL NOT NULL,
  trip_id INTEGER NOT NULL,
  row_sequence INTEGER NOT NULL,
  purpose_master_id INTEGER NOT NULL,
  start_minutes INTEGER NOT NULL,
  end_minutes INTEGER NOT NULL,
  departure_name VARCHAR(255),
  departure_prefecture_id INTEGER,
  arrival_name VARCHAR(255),
  title VARCHAR(255),
  description VARCHAR(1000),
  PRIMARY KEY (id)
);

ALTER TABLE itinerary ADD CONSTRAINT FK_itinerary_trip FOREIGN KEY (trip_id) REFERENCES trip (id);
ALTER TABLE itinerary ADD CONSTRAINT FK_itinerary_purpose FOREIGN KEY (purpose_master_id) REFERENCES purpose_master (id);
ALTER TABLE itinerary ADD CONSTRAINT FK_itinerary_departure FOREIGN KEY (departure_prefecture_id) REFERENCES prefecture_master (id);

-- itineraryテーブル用テストデータ
INSERT INTO itinerary (trip_id, row_sequence, purpose_master_id, start_minutes, end_minutes, departure_name, departure_prefecture_id, 
                    arrival_name, title, description) VALUES
  (1,1,1,11,21,'departurePlace1',1,'arrivalPlace1',NULL,NULL),
  (1,2,2,21,31,NULL,NULL,NULL,'title1','description1'),
  (1,3,1,31,41,'departurePlace1-2',2,'arrivalPlace1-2',NULL,NULL),

  (2,1,1,12,22,'departurePlace2',2,'arrivalPlace2',NULL,NULL),
  (2,2,2,22,32,NULL,NULL,NULL,'title2','description2'),
  (2,3,1,32,42,'departurePlace2-2',3,'arrivalPlace2-2',NULL,NULL),

  (3,1,1,13,23,'departurePlace3',3,'arrivalPlace3',NULL,NULL),
  (3,2,2,23,33,NULL,NULL,NULL,'title3','description3'),
  (3,3,1,33,43,'departurePlace3-2',3,'arrivalPlace3-2',NULL,NULL),
  (3,4,2,43,53,NULL,NULL,NULL,'title3','description3'),
  (3,5,1,53,63,'departurePlace3-3',3,'arrivalPlace3-3',NULL,NULL),

  (4,1,1,14,24,'departurePlace4-1',3,'arrivalPlace4-1',NULL,NULL),
  (4,2,2,24,34,NULL,NULL,NULL,'title4-1','description4-1'),
  (4,3,1,34,44,'departurePlace4-2',3,'arrivalPlace4-2',NULL,NULL),
  (4,4,2,44,54,NULL,NULL,NULL,'title4-2','description4-2'),
  (4,5,1,54,64,'departurePlace4-3',2,'arrivalPlace4-3',NULL,NULL),
  (4,6,2,64,74,NULL,NULL,NULL,'title4-3','description4-3'),
  (4,7,1,74,84,'departurePlace4-4',1,'arrivalPlace4-4',NULL,NULL);

CREATE TABLE IF NOT EXISTS favorite (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  trip_id INT NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE favorite ADD CONSTRAINT FK_favorite_users FOREIGN KEY (user_id) REFERENCES users (user_id);
ALTER TABLE favorite ADD CONSTRAINT FK_favorite_trip FOREIGN KEY (trip_id) REFERENCES trip (id);

-- favorite favorite
INSERT INTO favorite (user_id, trip_id, created_at, updated_at) VALUES
  (1,1,'2023-01-10 19:30:33.423588','2023-01-10 19:30:33.423588'),
  (1,2,'2023-01-20 19:30:33.423588','2023-01-20 19:30:33.423588'),
  (2,2,'2023-01-10 19:30:33.423588','2023-01-10 19:30:33.423588');

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO triplanner;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO triplanner;