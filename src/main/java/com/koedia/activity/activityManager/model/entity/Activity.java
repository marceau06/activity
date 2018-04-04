package com.koedia.activity.activityManager.model.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "activity")
@EntityListeners(AuditingEntityListener.class)
public class Activity {

	@Id
   	@GeneratedValue(strategy = GenerationType.AUTO) 
	@Column(name = "id")
   	private Integer id;
	
	@Column(name = "user_id")
	private Integer userId;
 
	@NotNull
    @Size(min=2, max=60)
	@Column(name = "title")
   	private String title;
 
	@NotNull
    @Size(min=10, max=1000)
	@Column(name = "description")
   	private String description;
 
	@Column(name = "category")
   	private String category;
   	
	@Column(name = "address")
   	private String address;
   	
	@Column(name = "begin_date")
   	private Date beginDate;

	@Column(name = "end_date")
   	private Date endDate;
   	
	@Column(name = "price")
   	private Double price;
   	
	@Column(name = "minimum_age")
   	private Integer minimuAge;

	@Column(name = "main_picture")
	private String mainPicture;

	@Column(name = "second_picture")
	private String secondPicture;
	
	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;
	
	@Column(name = "meeting_point")
	private String meetingPoint;
	
	@Column(name = "active")
	private boolean active;
	
	@Column(name="nb_persons")
	private Integer nbPersons; 
	
	@Column(name = "city")
	private String city;
	
	@Column(name="country")
	private String country; 
	
	@Column(name="zip_code")
	private String zipCode;
	
	@Transient
	private MultipartFile mainPictureFile;
	
	public Activity() {
		super();
	}

	public Activity(int id, int userId, String title, String description,
			String category, String address, Date beginDate, Date endDate,
			String planification, Double price, Integer minimumAge,
			String mainPicture, String secondPicture, boolean active, 
			int nbPersons, String city, String country) {
		
		super();
		
		this.id = id;
		this.userId = userId;
		this.title = title;
		this.description = description;
		this.category = category;
		this.address = address;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.price = price;
		this.minimuAge = minimumAge;
		this.mainPicture = mainPicture;
		this.secondPicture = secondPicture;
		this.active = active;
		this.nbPersons = nbPersons;
		this.city = city;
		this.country = country;
	}


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getMinimumAge() {
		return minimuAge;
	}

	public void setMinimumAge(Integer minimumAge) {
		this.minimuAge = minimumAge;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getMeetingPoint() {
		return meetingPoint;
	}

	public void setMeetingPoint(String meetingPoint) {
		this.meetingPoint = meetingPoint;
	}

	public Integer getNbPersons() {
		return nbPersons;
	}

	public void setNbPersons(Integer nbPersons) {
		this.nbPersons = nbPersons;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getMainPicture() {
		return mainPicture;
	}

	public void setMainPicture(String mainPicture) {
		this.mainPicture = mainPicture;
	}

	public String getSecondPicture() {
		return secondPicture;
	}

	public void setSecondPicture(String secondPicture) {
		this.secondPicture = secondPicture;
	}

	public MultipartFile getMainPictureFile() {
		return mainPictureFile;
	}

	public void setMainPictureFile(MultipartFile mainPictureFile) {
		this.mainPictureFile = mainPictureFile;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
}
