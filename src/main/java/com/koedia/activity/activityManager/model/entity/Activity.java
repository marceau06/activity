package com.koedia.activity.activityManager.model.entity;

import java.sql.Date;
import java.sql.Time;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;

import com.koedia.activity.activityManager.model.entity.Schedule;

@Entity
@Table(name = "activity")
@EntityListeners(AuditingEntityListener.class)
public class Activity {

	@Id
   	@GeneratedValue(strategy = GenerationType.AUTO) 
	@Column(name = "activity_id")
   	private Integer id;
	
	@Column(name = "user_id")
	private Integer userId;
 
	@NotNull
    @Size(min=10, max=60)
	@Valid
	@Column(name = "title")
   	private String title;
 
	@NotNull
    @Size(max=1500)
	@Column(name = "description_fre")
   	private String descriptionFre;
	
    @Size(max=1500)
	@Column(name = "description_eng")
   	private String descriptionEng;
	
    @Size(max=1500)
	@Column(name = "description_esp")
   	private String descriptionEsp;
 
	@Column(name = "category")
   	private String category;
   	
	@Column(name = "address")
   	private String address;
   	
	@Column(name = "begin_date")
   	private Date beginDate;

	@Column(name = "end_date")
   	private Date endDate;
	
	@Column(name="begin_hour")
	private Time beginHour;
	
	@Transient
	private String beginHourText;

	@Column(name="end_hour")
	private Time endHour;
	
	@Transient
	private String endHourText;
	
	@Column(name = "creation_date")
   	private Date creationDate;
   	
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
	
	@Column(name="required_id")
	private boolean requiredId;

	@Column(name = "city")
	private String city;
	
	@Column(name="country")
	private String country; 
	
	@Column(name="zip_code")
	private String zipCode;
	
	@Transient
	private MultipartFile mainPictureFile;

	@Column(name="additional_infos")
	private String additionalInfos;
	
	@Column(name="minimum_age")
	private Integer minimumAge;
	
	@Column(name="min_capacity_group")
	private Integer minCapacityGroup;
	
	@Column(name="max_capacity_group")
	private Integer maxCapacityGroup;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="activity_id")
    @OrderBy("price")
	@Size(min=1, max=4)
	private List<PaxPrice> paxs = new ArrayList<PaxPrice>();
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="activity_id")
    @OrderBy("id")
	private List<Schedule> usualSchedules = new ArrayList<Schedule>();
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="activity_id")
    @OrderBy("id")
	private List<Schedule> customSchedules = new ArrayList<Schedule>();
	
	@Transient
	private String fieldToValidate;
	
	public Activity() {
		super();
	}

//	public Activity(int id, int userId, String title, String descriptionFre, 
//			String descriptionEng, String descriptionEsp, 
//			String category, String address, Date beginDate, Date endDate,
//			String mainPicture, String secondPicture, boolean active, 
//			String city, String country, String zipCode,
//			String additionalInfos, int minCapacityGroup, int maxCapacityGroup) {
//		
//		super();
//		
//		this.id = id;
//		this.userId = userId;
//		this.title = title;
//		this.descriptionFre = descriptionFre;
//		this.descriptionEng = descriptionEng;
//		this.descriptionEsp = descriptionEsp;
//		this.category = category;
//		this.address = address;
//		this.beginDate = beginDate;
//		this.endDate = endDate;
//		this.mainPicture = mainPicture;
//		this.secondPicture = secondPicture;
//		this.active = active;
//		this.city = city;
//		this.country = country;
//		this.additionalInfos = additionalInfos;
//		this.zipCode = zipCode;
//		this.minCapacityGroup = minCapacityGroup;
//		this.maxCapacityGroup = maxCapacityGroup;
//	}


	public Integer getId() {
		return id;
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

	public String getDescriptionFre() {
		return descriptionFre;
	}

	public void setDescriptionFre(String descriptionFre) {
		this.descriptionFre = descriptionFre;
	}
	
	public String getDescriptionEng() {
		return descriptionEng;
	}

	public void setDescriptionEng(String descriptionEng) {
		this.descriptionEng = descriptionEng;
	}

	public String getDescriptionEsp() {
		return descriptionEsp;
	}

	public void setDescriptionEsp(String descriptionEsp) {
		this.descriptionEsp = descriptionEsp;
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

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getAdditionalInfos() {
		return additionalInfos;
	}

	public void setAdditionalInfos(String additional_infos) {
		this.additionalInfos = additional_infos;
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

	public boolean isRequiredId() {
		return requiredId;
	}

	public void setRequiredId(boolean requiredId) {
		this.requiredId = requiredId;
	}

	public Integer getMinCapacityGroup() {
		return minCapacityGroup;
	}

	public void setMinCapacityGroup(Integer minCapacityGroup) {
		this.minCapacityGroup = minCapacityGroup;
	}

	public Integer getMaxCapacityGroup() {
		return maxCapacityGroup;
	}

	public void setMaxCapacityGroup(Integer maxCapacityGroup) {
		this.maxCapacityGroup = maxCapacityGroup;
	}

	public List<PaxPrice> getPaxs() {
		return paxs;
	}

	public void setPaxs(List<PaxPrice> paxs) {
		this.paxs = paxs;
	}

	public String getBeginHourText() {
		return beginHourText;
	}

	public void setBeginHourText(String beginHourText) {
		this.beginHourText = beginHourText;
	}

	public String getEndHourText() {
		return endHourText;
	}

	public void setEndHourText(String endHourText) {
		this.endHourText = endHourText;
	}

	public Integer getMinimumAge() {
		return minimumAge;
	}

	public void setMinimumAge(Integer minimumAge) {
		this.minimumAge = minimumAge;
	}

	public List<Schedule> getUsualSchedules() {
		return usualSchedules;
	}

	public void setUsualSchedules(List<Schedule> usualSchedules) {
		this.usualSchedules = usualSchedules;
	}

	public List<Schedule> getCustomSchedules() {
		return customSchedules;
	}

	public void setCustomSchedules(List<Schedule> customSchedules) {
		this.customSchedules = customSchedules;
	}

	public String getFieldToValidate() {
		return fieldToValidate;
	}

	public void setFieldToValidate(String fieldToValidate) {
		this.fieldToValidate = fieldToValidate;
	}
}
