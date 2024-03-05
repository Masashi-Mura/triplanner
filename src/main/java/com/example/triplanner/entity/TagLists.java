package com.example.triplanner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tag_lists")
@Data
public class TagLists extends AbstractEntity{
	
	/** ID */
	@Id
    @SequenceGenerator(name = "tag_lists_id_seq")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	/** trip_id */
	@Column(name = "trip_id")
	private Integer tripId;
	
	/** tag_master_id */
	@Column(name = "tag_id")
	private Integer tagId;
	
	@OneToOne
	@JoinColumn(name = "tag_Id", insertable = false, updatable = false)
	private Tag tag;
}
