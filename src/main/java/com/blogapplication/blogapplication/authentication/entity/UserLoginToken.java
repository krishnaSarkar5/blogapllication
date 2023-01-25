package com.blogapplication.blogapplication.authentication.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.blogapplication.blogapplication.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name="user_login_token")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginToken implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4364146766872476461L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@Column(name="token",columnDefinition = "text", nullable = true)
	private String token;
	
	@Column(name="login_time",nullable = false)
	private LocalDateTime loginTime;
	
	@Column(name="logout_time",nullable = true)
	private LocalDateTime logoutTime;
	
//	 COMMENT '1 = Active, 0 = Inactive'
	@Column(name="status",columnDefinition = " int4 NOT NULL default '1'")
	private int status;
	
	@Column(name="channel",columnDefinition = " varchar(225) NOT NULL default '1'")
	private String channel;
	
	@Column(name="created_at",nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name="updated_at",nullable = false)
	private LocalDateTime updatedAt;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
}
