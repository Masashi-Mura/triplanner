package com.example.triplanner.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "trips")
@Data
public class Trips extends AbstractEntity {

	/** ID */
	@Id
	@SequenceGenerator(name = "torip_id_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/** 投稿ユーザ */
	@Column(name = "user_id")
	private Integer userId;

	/** 旅行タイトル */
	@Column(name = "trip_title", length = 255)
	private String tripTitle;

	/** 旅行日数 */
	@Column(name = "total_trip_days")
	private Integer totalTripDays;

	/** 公開設定 */
	@Column(name = "public_id")
	private Integer publicId;

	/** リンク */
	@Column(name = "link", length = 255)
	private String link;

	/** 有効期限 */
	@Column(name = "expiration")
	private LocalDateTime expiration;

	/** 削除済 */
	@Column(name = "deleted")
	private Boolean deleted;

	//FKの設定を書く
	@OneToMany
	@JoinColumn(name = "tag_id")
	private List<TagLists> tagList;
	
	@OneToMany
	@JoinColumn(name = "trip_id", insertable = false, updatable = false)
	private List<Itinerary> itineraries;

	@ManyToOne
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private Users user;
}
