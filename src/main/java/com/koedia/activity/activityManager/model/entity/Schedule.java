package com.koedia.activity.activityManager.model.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "schedule")
@EntityListeners(AuditingEntityListener.class)
public class Schedule {
	
	@Id
   	@GeneratedValue(strategy = GenerationType.AUTO) 
	@Column(name = "schedule_id")
   	private Integer id;
	
	@NotNull
	@Column(name = "activity_id")
	private Integer activityId;
 
	@Column(name = "weekday_name")
   	private String weekdayName;
	
	@Column(name = "weekday_number")
	private Integer weekdayNumber;
 
	@Column(name = "date")
   	private Date descriptionFre;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="schedule_id")
	@Size(min=1, max=10)
	private List<Session> sessions = new ArrayList<Session>();
	
	public Schedule(Integer weekdayNumber) {
		super();
		this.weekdayNumber = weekdayNumber;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public String getWeekdayName() {
		return weekdayName;
	}

	public void setWeekdayName(String weekdayName) {
		this.weekdayName = weekdayName;
	}

	public Date getDescriptionFre() {
		return descriptionFre;
	}

	public void setDescriptionFre(Date descriptionFre) {
		this.descriptionFre = descriptionFre;
	}

	public List<Session> getSessions() {
		return sessions;
	}

	public void setSessions(List<Session> sessions) {
		this.sessions = sessions;
	}

	public Integer getWeekdayNumber() {
		return weekdayNumber;
	}

	public void setWeekdayNumber(Integer weekdayNumber) {
		this.weekdayNumber = weekdayNumber;
	}
	
	public void addSession(Session s) {
		this.sessions.add(s);
	}

}
