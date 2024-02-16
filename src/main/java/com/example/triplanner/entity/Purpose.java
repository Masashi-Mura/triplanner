package com.example.triplanner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "purposes_master")
@Data
public class Purpose extends AbstractEntity{
	
	/** ID */
	@Id
	@Column
	private Integer id;
	
	/** タグ名 */
	@Column(name = "purpose", length = 255)
	private String purpose;
	
}
