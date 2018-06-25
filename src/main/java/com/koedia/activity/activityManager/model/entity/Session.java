package com.koedia.activity.activityManager.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.joda.time.LocalDateTime;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

	
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
	@DateTimeFormat(pattern = "HH:mm")
	private LocalDateTime beginHour;
	 
	@NotNull
	@Column(name = "end_hour")
	@DateTimeFormat(pattern = "HH:mm")
	private LocalDateTime endHour;
	
	@Column(name = "session_name")
	private String sessionName;
	
	@Transient
	private Integer sessionNumber;
	
	public Session() {
		
	}
	
	public Session(Integer sessionNumber) {
		this.sessionNumber = sessionNumber;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getBeginHour() {
		return beginHour;
	}

	public void setBeginHour(LocalDateTime beginHour) {
		this.beginHour = beginHour;
	}

	public LocalDateTime getEndHour() {
		return endHour;
	}

	public void setEndHour(LocalDateTime endHour) {
		this.endHour = endHour;
	}

	public Integer getSessionNumber() {
		return sessionNumber;
	}

	public void setSessionNumber(Integer sessionNumber) {
		this.sessionNumber = sessionNumber;
	}

	public String getSessionName() {
		return sessionName;
	}

	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
}
