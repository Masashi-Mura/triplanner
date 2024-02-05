package com.example.triplanner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "regions_master")
@Data
public class Region extends AbstractEntity{
	
	/** ID */
	@Id
	@Column
	private Integer id;
	
	/** エリア名 */
	@Column(name = "region_name", length = 255)
	private String name;
	
}
