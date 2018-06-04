package com.koedia.activity.activityManager.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pax_price")
public class PaxPrice {
	
	@Id
   	@GeneratedValue(strategy = GenerationType.AUTO) 
	@Column(name = "pax_price_id")
	private Integer paxPriceId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "age_min")
	private Integer ageMin;
	
	@Column(name = "age_max")
	private Integer ageMax;
	
	@Column(name = "price")
	private double price;
	
	@Column(name = "activity_id")
	private Integer activityId;
	
	public PaxPrice() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAgeMin() {
		return ageMin;
	}

	public void setAgeMin(Integer ageMin) {
		this.ageMin = ageMin;
	}

	public Integer getAgeMax() {
		return ageMax;
	}

	public void setAgeMax(Integer ageMax) {
		this.ageMax = ageMax;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Integer getActivityId() {
		return activityId;
	}
//
//	public void setActivityId(Integer activityId) {
//		this.activityId = activityId;
//	}
}
