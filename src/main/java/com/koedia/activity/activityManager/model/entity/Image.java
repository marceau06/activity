package com.koedia.activity.activityManager.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.URL;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "image")
@EntityListeners(AuditingEntityListener.class)
public class Image {
	
	@Id
   	@GeneratedValue(strategy = GenerationType.AUTO) 
	@Column(name = "image_id")
	private String id;
	
	@Column(name = "path")
	private String path;
	
	@URL
	@Column(name = "url")
	private String url;
	
	@Transient
	private MultipartFile file;
	
	public Image() {
		super();
	}
	
	public Image(String path) {
		this.path = path;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	
	
}