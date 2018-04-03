package com.spring.VO;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Complaint")
public class ComplaintVO {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int complaintId;

	@Column
	private String subject;

	@Column
	private String complaintDescription;

	@Column
	private Date presentdate;

	@Column
	private Time presenttime;

	@Column
	private String delStatus;

	@Column
	private String complaintStatus;
	@Column
	private String complaintReplay;

	@ManyToOne
	private LoginVO complaintTo;

	@ManyToOne
	private LoginVO complaintFrom;

	public String getComplaintReplay() {
		return complaintReplay;
	}

	public void setComplaintReplay(String complaintReplay) {
		this.complaintReplay = complaintReplay;
	}

	public String getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}

	public String getComplaintStatus() {
		return complaintStatus;
	}

	public void setComplaintStatus(String complaintStatus) {
		this.complaintStatus = complaintStatus;
	}

	public Date getPresentdate() {
		return presentdate;
	}

	public void setPresentdate(Date presentdate) {
		this.presentdate = presentdate;
	}

	public Date getPresenttime() {
		return presenttime;
	}

	public void setPresenttime(Time presenttime) {
		this.presenttime = presenttime;
	}

	public String getComplaintDescription() {
		return complaintDescription;
	}

	public void setComplaintDescription(String complaintDescription) {
		this.complaintDescription = complaintDescription;
	}

	public int getComplaintId() {
		return complaintId;
	}

	public void setComplaintId(int complaintId) {
		this.complaintId = complaintId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public LoginVO getComplaintTo() {
		return complaintTo;
	}

	public void setComplaintTo(LoginVO complaintTo) {
		this.complaintTo = complaintTo;
	}

	public LoginVO getComplaintFrom() {
		return complaintFrom;
	}

	public void setComplaintFrom(LoginVO complaintFrom) {
		this.complaintFrom = complaintFrom;
	}

}
