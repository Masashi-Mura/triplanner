package com.example.triplanner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "public_options_master")
@Data
public class PublicOption extends AbstractEntity{
	
	/** ID */
	@Id
	@Column
	private Integer id;
	
	/** タグ名 */
	@Column(name = "public_option", length = 255)
	private String name;
	
}
