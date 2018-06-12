package com.koedia.activity.activityManager.model.entity;

import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

	
@Entity
@Table(name = "session")
@EntityListeners(AuditingEntityListener.class)
public class Session {
		
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO) 
	@Column(name = "session_id")
	private Integer id;
		
	@NotNull
	@Column(name = "begin_hour")
	private Time beginHour;
	 
	@NotNull
	@Column(name = "end_hour")
	private Time endHour;
	
	@Transient
	private Integer sessionNumber;
	
	public Session(Integer sessionNumber) {
		this.sessionNumber = sessionNumber;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Time getBeginHour() {
		return beginHour;
	}

	public void setBeginHour(Time beginHour) {
		this.beginHour = beginHour;
	}

	public Time getEndHour() {
		return endHour;
	}

	public void setEndHour(Time endHour) {
		this.endHour = endHour;
	}

	public Integer getSessionNumber() {
		return sessionNumber;
	}

	public void setSessionNumber(Integer sessionNumber) {
		this.sessionNumber = sessionNumber;
	}
}
