package com.spring.VO;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Feedback")
public class FeedbackVO {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int feedbackId;
	@Column
	private String feedbackSubject;
	@Column
	private String feedbackDescription;
	@Column
	private Date presentfeedbackdate;

	@Column
	private Time presentfeedbacktime;
	@Column
	private String delStatus;

	public int getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(int feedbackId) {
		this.feedbackId = feedbackId;
	}

	public String getFeedbackSubject() {
		return feedbackSubject;
	}

	public void setFeedbackSubject(String feedbackSubject) {
		this.feedbackSubject = feedbackSubject;
	}

	public String getFeedbackDescription() {
		return feedbackDescription;
	}

	public void setFeedbackDescription(String feedbackDescription) {
		this.feedbackDescription = feedbackDescription;
	}

	public Date getPresentfeedbackdate() {
		return presentfeedbackdate;
	}

	public void setPresentfeedbackdate(Date presentfeedbackdate) {
		this.presentfeedbackdate = presentfeedbackdate;
	}

	public Time getPresentfeedbacktime() {
		return presentfeedbacktime;
	}

	public void setPresentfeedbacktime(Time presentfeedbacktime) {
		this.presentfeedbacktime = presentfeedbacktime;
	}

	public String getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}

}