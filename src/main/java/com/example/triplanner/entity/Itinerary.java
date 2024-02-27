package com.example.triplanner.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "itineraries")
@Data
public class Itinerary extends AbstractEntity{
	
	/** ID */
	@Id
    @SequenceGenerator(name = "itinerary_id_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	/** 旅id */
	@ManyToOne
	@JoinColumn(name = "trip_id")
	private Trips trip;
	
	/** 行番号 */
	@Column(name = "row_sequence")
	private Integer rowSequence;
	
	/** 目的 */
	@Column(name = "purpose_id")
	private Integer purposeId;
	
	/** 開始時刻 */
	@Column(name = "start_time")
	private LocalDateTime startTime;
	
	/** 終了時刻 */
	@Column(name = "end_time")
	private LocalDateTime endTime;
	
	/** 開始地点 */
	@Column(name = "departure_name", length = 255)
	private String departureName;
	
	/** 開始地点の都道府県id */
	@Column(name = "departure_prefecture_id")
	private Integer departurePrefectureId;
	
	/** 終了地点 */
	@Column(name = "arrival_name", length = 255)
	private String arrivalName;
	
	/** タイトル */
	@Column(name = "title", length = 255)
	private String title;
	
	/** 内容 */
	@Column(name = "description", length = 255)
	private String description;
	
	//FKの設定を書く
}
