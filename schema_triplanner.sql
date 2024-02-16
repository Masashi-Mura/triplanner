DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS regions_master CASCADE;
DROP TABLE IF EXISTS prefectures_master CASCADE;
DROP TABLE IF EXISTS purposes_master CASCADE;
DROP TABLE IF EXISTS public_options_master CASCADE;
DROP TABLE IF EXISTS tags_master CASCADE;
DROP TABLE IF EXISTS trips CASCADE;
DROP TABLE IF EXISTS tag_maps CASCADE;
DROP TABLE IF EXISTS itineraries CASCADE;
DROP TABLE IF EXISTS favorites CASCADE;

-- ▽▽▽▽以下マスタ関係▽▽▽▽
CREATE TABLE IF NOT EXISTS regions_master (
  id INTEGER CHECK(id > 0) NOT NULL,
  region_name VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS prefectures_master (
  id INTEGER CHECK(id >= 1) NOT NULL,
  region_id INTEGER NOT NULL,
  prefecture_name VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE prefectures_master ADD CONSTRAINT FK_prefectures_region FOREIGN KEY (region_id) REFERENCES regions_master (id);

-- 日本語文字化けのため英語のテスト用データを仮使用
INSERT INTO regions_master VALUES
  -- (1,'hokkaido_region',NOW(),NOW()),
  -- (2,'touhoku_region',NOW(),NOW());
  (1,'北海道地方',NOW(),NOW()),
  (2,'東北地方',NOW(),NOW()),
  (3,'関東地方',NOW(),NOW()),
  (4,'中部地方',NOW(),NOW()),
  (5,'近畿地方',NOW(),NOW()),
  (6,'中国地方',NOW(),NOW()),
  (7,'四国地方',NOW(),NOW()),
  (8,'九州地方',NOW(),NOW());

-- 日本語文字化けのため英語のテスト用データを仮使用
INSERT INTO prefectures_master VALUES
  -- (1,1,'hokkaido',NOW(),NOW()),
  -- (2,2,'aomoriken',NOW(),NOW()),
  -- (3,2,'iwateken',NOW(),NOW()),
  -- (4,2,'miyagiken',NOW(),NOW()),
  -- (5,2,'akitaken',NOW(),NOW());
  (1,1,'北海道',NOW(),NOW()),
  (2,2,'青森県',NOW(),NOW()),
  (3,2,'岩手県',NOW(),NOW()),
  (4,2,'宮城県',NOW(),NOW()),
  (5,2,'秋田県',NOW(),NOW()),
  (6,2,'山形県',NOW(),NOW()),
  (7,2,'福島県',NOW(),NOW()),
  (8,3,'茨城県',NOW(),NOW()),
  (9,3,'栃木県',NOW(),NOW()),
  (10,3,'群馬県',NOW(),NOW()),
  (11,3,'埼玉県',NOW(),NOW()),
  (12,3,'千葉県',NOW(),NOW()),
  (13,3,'東京都',NOW(),NOW()),
  (14,3,'神奈川県',NOW(),NOW()),
  (15,4,'新潟県',NOW(),NOW()),
  (16,4,'富山県',NOW(),NOW()),
  (17,4,'石川県',NOW(),NOW()),
  (18,4,'福井県',NOW(),NOW()),
  (19,4,'山梨県',NOW(),NOW()),
  (20,4,'長野県',NOW(),NOW()),
  (21,4,'岐阜県',NOW(),NOW()),
  (22,4,'静岡県',NOW(),NOW()),
  (23,4,'愛知県',NOW(),NOW()),
  (24,5,'三重県',NOW(),NOW()),
  (25,5,'滋賀県',NOW(),NOW()),
  (26,5,'京都府',NOW(),NOW()),
  (27,5,'大阪府',NOW(),NOW()),
  (28,5,'兵庫県',NOW(),NOW()),
  (29,5,'奈良県',NOW(),NOW()),
  (30,5,'和歌山県',NOW(),NOW()),
  (31,6,'鳥取県',NOW(),NOW()),
  (32,6,'島根県',NOW(),NOW()),
  (33,6,'岡山県',NOW(),NOW()),
  (34,6,'広島県',NOW(),NOW()),
  (35,6,'山口県',NOW(),NOW()),
  (36,7,'徳島県',NOW(),NOW()),
  (37,7,'香川県',NOW(),NOW()),
  (38,7,'愛媛県',NOW(),NOW()),
  (39,7,'高知県',NOW(),NOW()),
  (40,8,'福岡県',NOW(),NOW()),
  (41,8,'佐賀県',NOW(),NOW()),
  (42,8,'長崎県',NOW(),NOW()),
  (43,8,'熊本県',NOW(),NOW()),
  (44,8,'大分県',NOW(),NOW()),
  (45,8,'宮崎県',NOW(),NOW()),
  (46,8,'鹿児島県',NOW(),NOW()),
  (47,8,'沖縄県',NOW(),NOW());


CREATE TABLE IF NOT EXISTS purposes_master (
  id INTEGER CHECK(id > 0) NOT NULL,
  purpose VARCHAR(255) DEFAULT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

-- 日本語文字化けのため英語のテスト用データを仮使用
INSERT INTO purposes_master VALUES
  (1,'移動',NOW(),NOW()),
  (2,'観光',NOW(),NOW()),
  (3,'食事',NOW(),NOW()),
  (4,'宿泊',NOW(),NOW()),
  (5,'その他',NOW(),NOW());

CREATE TABLE IF NOT EXISTS public_options_master (
  id INTEGER CHECK(id > 0) NOT NULL,
  public_option VARCHAR(255) DEFAULT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

-- 日本語文字化けのため英語のテスト用データを仮使用
INSERT INTO public_options_master VALUES
  (1,'一般公開',NOW(),NOW()),
  (2,'非公開',NOW(),NOW()),
  (3,'限定公開',NOW(),NOW());

CREATE TABLE IF NOT EXISTS tags_master (
  id INTEGER CHECK(id > 0) NOT NULL,
  tag_name VARCHAR(255) DEFAULT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

-- 日本語文字化けのため英語のテスト用データを仮使用
INSERT INTO tags_master VALUES
  -- (1,'hitoritabi',NOW(),NOW()),
  -- (2,'couple',NOW(),NOW()),
  -- (3,'kodure',NOW(),NOW()),
  -- (4,'friends',NOW(),NOW());
  (1,'一人旅',NOW(),NOW()),
  (2,'カップル',NOW(),NOW()),
  (3,'子連れ',NOW(),NOW()),
  (4,'友人',NOW(),NOW()),
  (5,'学生',NOW(),NOW()),
  (6,'シニア',NOW(),NOW()),
  (7,'ファミリー',NOW(),NOW()),
  (8,'観光',NOW(),NOW()),
  (9,'グルメ',NOW(),NOW()),
  (10,'温泉',NOW(),NOW()),
  (11,'体験',NOW(),NOW()),
  (12,'ビジネス',NOW(),NOW()),
  (13,'パワースポット',NOW(),NOW()),
  (14,'春',NOW(),NOW()),
  (15,'夏',NOW(),NOW()),
  (16,'秋',NOW(),NOW()),
  (17,'冬',NOW(),NOW());

-- ▽▽▽▽以下テーブル作成▽▽▽▽
CREATE TABLE IF NOT EXISTS users (
  user_id SERIAL NOT NULL,
  authority VARCHAR(255) NOT NULL,
  nickname VARCHAR(255) NOT NULL,
  login_password VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (user_id)
);

-- usersテーブルテスト用データ
INSERT INTO users (authority, nickname, login_password, created_at, updated_at) VALUES
  ('test1@test.com','tester1','password',NOW(),NOW()),
  ('test2@test.com','tester2','password',NOW(),NOW());

CREATE TABLE IF NOT EXISTS trips (
  id SERIAL NOT NULL,
  user_id INTEGER NOT NULL,
  trip_title VARCHAR(255) NOT NULL,
  total_trip_days INTEGER NOT NULL,
  public_id INTEGER NOT NULL,
  link VARCHAR(255),
  expiration TIMESTAMP,
  deleted BOOL NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE trips ADD CONSTRAINT FK_trip_user FOREIGN KEY (user_id) REFERENCES users (user_id);

-- tripテーブルテスト用データ
INSERT INTO trips (user_id, trip_title, total_trip_days, public_id, deleted, created_at, updated_at) VALUES
  (1,'test1',1,1,false,NOW(),NOW()),
  (1,'test2',2,2,false,NOW(),NOW()),
  (2,'test3',1,2,false,NOW(),NOW()),
  (2,'test4',2,2,false,NOW(),NOW());

CREATE TABLE IF NOT EXISTS tag_maps (
  id SERIAL NOT NULL,
  trip_id INTEGER NOT NULL,
  tag_id INTEGER NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE tag_maps ADD CONSTRAINT FK_tag_trip FOREIGN KEY (trip_id) REFERENCES trips (id);
ALTER TABLE tag_maps ADD CONSTRAINT FK_tag_tag FOREIGN KEY (tag_id) REFERENCES tags_master (id);

-- tag_mapsテーブルテスト用データ
INSERT INTO tag_maps (trip_id, tag_id, created_at, updated_at) VALUES
  (1,1,NOW(),NOW()),
  (1,2,NOW(),NOW()),

  (2,2,NOW(),NOW()),
  (2,3,NOW(),NOW()),

  (3,1,NOW(),NOW()),
  (3,2,NOW(),NOW()),
  (3,3,NOW(),NOW()),

  (4,4,NOW(),NOW());

CREATE TABLE IF NOT EXISTS itineraries (
  id SERIAL NOT NULL,
  trip_id INTEGER NOT NULL,
  row_sequence INTEGER NOT NULL,
  purpose_id INTEGER NOT NULL,
  start_time TIMESTAMP NOT NULL,
  end_time TIMESTAMP NOT NULL,
  departure_name VARCHAR(255),
  departure_prefecture_id INTEGER,
  arrival_name VARCHAR(255),
  title VARCHAR(255),
  description VARCHAR(1000),
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE itineraries ADD CONSTRAINT FK_itinerary_trip FOREIGN KEY (trip_id) REFERENCES trips (id);
ALTER TABLE itineraries ADD CONSTRAINT FK_itinerary_purpose FOREIGN KEY (purpose_id) REFERENCES purposes_master (id);
ALTER TABLE itineraries ADD CONSTRAINT FK_itinerary_departure FOREIGN KEY (departure_prefecture_id) REFERENCES prefectures_master (id);

-- itinerariesテーブル用テストデータ
INSERT INTO itineraries (trip_id, row_sequence, purpose_id, start_time, end_time, departure_name, departure_prefecture_id, 
                    arrival_name, title, description, created_at, updated_at) VALUES
  (1,1,1,NOW(),NOW(),'departurePlace1',1,'arrivalPlace1',NULL,NULL,NOW(),NOW()),
  (1,2,2,NOW(),NOW(),NULL,NULL,NULL,'title1','description1',NOW(),NOW()),
  (1,3,1,NOW(),NOW(),'departurePlace1-2',2,'arrivalPlace1-2',NULL,NULL,NOW(),NOW()),

  (2,1,1,NOW(),NOW(),'departurePlace2',2,'arrivalPlace2',NULL,NULL,NOW(),NOW()),
  (2,2,2,NOW(),NOW(),NULL,NULL,NULL,'title2','description2',NOW(),NOW()),
  (2,3,1,NOW(),NOW(),'departurePlace2-2',3,'arrivalPlace2-2',NULL,NULL,NOW(),NOW()),

  (3,1,1,NOW(),NOW(),'departurePlace3',3,'arrivalPlace3',NULL,NULL,NOW(),NOW()),
  (3,2,2,NOW(),NOW(),NULL,NULL,NULL,'title3','description3',NOW(),NOW()),
  (3,3,1,NOW(),NOW(),'departurePlace3-2',3,'arrivalPlace3-2',NULL,NULL,NOW(),NOW()),
  (3,4,2,NOW(),NOW(),NULL,NULL,NULL,'title3','description3',NOW(),NOW()),
  (3,5,1,NOW(),NOW(),'departurePlace3-3',3,'arrivalPlace3-3',NULL,NULL,NOW(),NOW()),

  (4,1,1,NOW(),NOW(),'departurePlace4-1',3,'arrivalPlace4-1',NULL,NULL,NOW(),NOW()),
  (4,2,2,NOW(),NOW(),NULL,NULL,NULL,'title4-1','description4-1',NOW(),NOW()),
  (4,3,1,NOW(),NOW(),'departurePlace4-2',3,'arrivalPlace4-2',NULL,NULL,NOW(),NOW()),
  (4,4,2,NOW(),NOW(),NULL,NULL,NULL,'title4-2','description4-2',NOW(),NOW()),
  (4,5,1,NOW(),NOW(),'departurePlace4-3',2,'arrivalPlace4-3',NULL,NULL,NOW(),NOW()),
  (4,6,2,NOW(),NOW(),NULL,NULL,NULL,'title4-3','description4-3',NOW(),NOW()),
  (4,7,1,NOW(),NOW(),'departurePlace4-4',1,'arrivalPlace4-4',NULL,NULL,NOW(),NOW());

CREATE TABLE IF NOT EXISTS favorites (
  id SERIAL NOT NULL,
  user_id INT NOT NULL,
  trip_id INT NOT NULL,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);

ALTER TABLE favorites ADD CONSTRAINT FK_favorite_users FOREIGN KEY (user_id) REFERENCES users (user_id);
ALTER TABLE favorites ADD CONSTRAINT FK_favorite_trip FOREIGN KEY (trip_id) REFERENCES trips (id);

-- favorite favorites
INSERT INTO favorites (user_id, trip_id, created_at, updated_at) VALUES
  (1,1,NOW(),NOW()),
  (1,2,NOW(),NOW()),
  (2,2,NOW(),NOW());

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO triplanner;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO triplanner;