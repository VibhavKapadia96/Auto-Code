package com.spring.VO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="Attachment")
public class AttachmentVO {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int atttachmentId;
	@Column
	private String fileName;
	@Column
	private String filePath;
	@ManyToOne
	private ComplaintVO complaintVO;
	
	
	
	public ComplaintVO getComplaintVO() {
		return complaintVO;
	}
	public void setComplaintVO(ComplaintVO complaintVO) {
		this.complaintVO = complaintVO;
	}
	public int getAtttachmentId() {
		return atttachmentId;
	}
	public void setAtttachmentId(int atttachmentId) {
		this.atttachmentId = atttachmentId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	
	
	
	
	
}
