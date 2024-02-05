package com.example.triplanner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "prefectures_master")
@Data
public class Prefecture extends AbstractEntity{
	
	/** ID */
	@Id
	@Column
	private Integer id;
	
	/** reagion_id */
	@Column(name = "region_id")
	private Integer regionId;
	
	/** 都道府県名 */
	@Column(name = "prefecture_name", length = 255)
	private String name;
	
}
