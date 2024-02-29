package com.example.triplanner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class Users extends AbstractEntity{
	
	/** ID */
	@Id
    @SequenceGenerator(name = "users")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	/** ユーザId*/
	@Column(name = "user_id")
	private Integer userId;
	
	/** 権限 */
	@Column(name = "authority", length = 255)
	private String authority;
	
	/** ニックネーム */
	@Column(name = "nickname", length = 255)
	private Integer nickname;
	
	/** ユーザネーム */
	@Column(name = "username", length = 255)
	private Integer username;
	
	/** パスワード */
	@Column(name = "login_password", length = 255)
	private String loginPassword;
	
}
