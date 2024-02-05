package com.example.triplanner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tags_master")
@Data
public class Tag extends AbstractEntity{
	
	/** ID */
	@Id
	@Column
	private Integer id;
	
	/** タグ名 */
	@Column(name = "tag_name", length = 255)
	private String name;
	
}
