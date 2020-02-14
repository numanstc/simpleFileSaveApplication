package com.numanstc.file.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_files")
public class UserFiles implements Serializable {

	private static final long serialVersionUID = 6076154810685276407L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;
	@Column(name = "file_name")
	private String fileName;
	@Column(name = "modified_file_name")
	private String modifiedFileName;
	@Column(name = "file_extension")
	private String filesExtension;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getModifiedFileName() {
		return modifiedFileName;
	}

	public void setModifiedFileName(String modifiedFileName) {
		this.modifiedFileName = modifiedFileName;
	}

	public String getFilesExtension() {
		return filesExtension;
	}

	public void setFilesExtension(String filesExtension) {
		this.filesExtension = filesExtension;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
