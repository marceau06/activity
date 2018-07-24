package com.koedia.activity.activityManager.model.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.koedia.common.tool.CollectionUtil;

@Entity
@Table(name = "activity")
//@EndDateAfterBeginDate
@EntityListeners(AuditingEntityListener.class)
public class Activity {

	@Id
   	@GeneratedValue(strategy = GenerationType.AUTO) 
	@Column(name = "activity_id")
   	private Integer id;
	
	@Column(name = "user_id")
	private Integer userId;
	
	// Step title
	@Length(min = 5, max = 100, message = "{Size.activity.title}", groups = {StepTitle.class})
	@NotEmpty(message = "{Size.activity.title}", groups = {StepTitle.class})
	@Column(name = "title")
   	private String title;
	
	// Step Category
	@NotEmpty(message = "{NotEmpty.activity.category}", groups = {StepCategory.class})
	@Column(name = "category")
   	private String category;
	
 
	// Step Description
	@Length(min = 20, max = 1000, message = "{Size.activity.description}", groups = {StepDescription.class})
	@NotEmpty(message = "{Size.activity.description}", groups = {StepDescription.class})
	@Column(name = "description_fre")
   	private String descriptionFre;
	
	@Length(min = 20, max = 1000, message = "{Size.activity.description}", groups = {StepDescription.class})
	@Column(name = "description_eng")
   	private String descriptionEng;
	
	@Length(min = 20, max = 1000, message = "{Size.activity.description}", groups = {StepDescription.class})
	@Column(name = "description_esp")
   	private String descriptionEsp;
	
 
	// Step MeetingPoint
	@NotEmpty(message = "{NotEmpty.activity.meetingpoint}", groups = {StepMeetingPoint.class})
	@Column(name = "meeting_point")
	private String meetingPoint;
	
	@NotEmpty(message = "{NotEmpty.activity.address}", groups = {StepMeetingPoint.class})
	@Length(min = 3, max = 1000, message="{Size.activity.address}", groups = {StepMeetingPoint.class})
	@Column(name = "address")
   	private String address;
	
	@NotEmpty(message = "{NotEmpty.activity.city}", groups = {StepMeetingPoint.class})
	@Column(name = "city")
	private String city;
	
	@NotEmpty(message = "{NotEmpty.activity.country}", groups = {StepMeetingPoint.class})
	@Column(name="country")
	private String country; 
	
	@NotEmpty(message = "{NotEmpty.activity.zipcode}", groups = {StepMeetingPoint.class})	
	@Column(name="zip_code")
	private String zipCode;
	
	@Column(name = "latitude")
	private Double latitude;

	@Column(name = "longitude")
	private Double longitude;
	

   	// Step Schedules
	@NotNull(groups = {StepPeriod.class})
//	@Future
//	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy") 
	@Column(name = "begin_date")
   	private Date beginDate;

	@NotNull(groups = {StepPeriod.class})
//	@Future
//	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy") 
	@Column(name = "end_date")
	private Date endDate;
	
	@NotEmpty(groups = {StepSchedules.class})
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="activity_id")
    @OrderBy("id")
	@Valid
	private List<Schedule> usualSchedules = new ArrayList<Schedule>();
	
//	@NotEmpty(groups = {StepSchedules.class})
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="activity_id")
    @OrderBy("id")
	@Valid
	private List<Schedule> customSchedules = new ArrayList<Schedule>();
	
	// Step Participation criterias
	@NotNull(groups = {StepParticipationCriterias.class})
	@Column(name="required_id")
	private Boolean requiredId;

//	@NotEmpty(groups = {StepParticipationCriterias.class})
	@Column(name="additional_infos")
	private String additionalInfos;
	
	@NotNull(groups = {StepParticipationCriterias.class})
	@Range(min = 1, max = 99)	
	@Column(name="minimum_age")
	private Integer minimumAge;
	
	@NotNull(groups = {StepDefaultPrice.class})
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinColumn(name = "defaultpax_id")
	@Valid
	private PaxPrice defaultPax;
	
	// Step paxs
	@NotEmpty(groups = {StepPaxs.class})
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="activity_id")
    @OrderBy("price")
	@Valid
	private List<PaxPrice> paxs = new ArrayList<PaxPrice>();

	// Step group capacity
	@NotNull(groups = {StepGroupCapacity.class})
	@Min(1)
	@Column(name="min_capacity_group")
	private Integer minCapacityGroup;
	
	@NotNull(groups = {StepGroupCapacity.class})
	@Min(1)
	@Column(name="max_capacity_group")
	private Integer maxCapacityGroup;
	
	
	// Transient and not validated properties
	@Column(name = "creation_date")
	private Date creationDate;
	
	@Column(name = "active")
	private boolean active;
	
	// Step Images
	// Step paxs
	@NotEmpty(groups = {StepImages.class})
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="activity_id")
	@Valid
	private List<Image> images = new ArrayList<Image>();
	
	@Transient
	private String stepToValidate;
	
	public Activity() {
		super();
	}

	/********** Getters and setters *************/
	
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

	public Boolean getRequiredId() {
		return requiredId;
	}

	public void setRequiredId(Boolean requiredId) {
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

	public String getStepToValidate() {
		return stepToValidate;
	}

	public void setStepToValidate(String stepToValidate) {
		this.stepToValidate = stepToValidate;
	}
	
	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}
	
	public PaxPrice getDefaultPax() {
		return defaultPax;
	}

	public void setDefaultPax(PaxPrice defaultPax) {
		this.defaultPax = defaultPax;
	}
	
	public Integer getNbImages() {
		if(!CollectionUtil.isEmpty(this.images)) {
			return (this.images.size());
		}
		return 0;
	}

	/********** Validation groups *************/
	
	public interface StepTitle {};
	
	public interface StepCategory {};
	
	public interface StepMeetingPoint {};
	
	public interface StepDescription {};
	
	public interface StepPeriod {};
	
	public interface StepSchedules {};
	
	public interface StepImages {};
	
	public interface StepParticipationCriterias {};
	
	public interface StepDefaultPrice {};
	
	public interface StepPaxs {};
	
	public interface StepGroupCapacity {};
	
	public interface StepMainPicture {};
}
