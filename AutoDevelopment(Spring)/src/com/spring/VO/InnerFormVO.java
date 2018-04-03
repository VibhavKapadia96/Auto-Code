package com.spring.VO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "InnerForm")
public class InnerFormVO {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int innerFormId;

	@Column
	private String innerfieldName;

	@Column
	private String innerfieldValue;

	@ManyToOne
	private FieldVO fieldVO;

	@Column
	private String delStatus;
	
	@ManyToOne
	private FormVO formVO;

	public int getInnerFormId() {
		return innerFormId;
	}

	public void setInnerFormId(int innerFormId) {
		this.innerFormId = innerFormId;
	}

	public String getInnerfieldName() {
		return innerfieldName;
	}

	public void setInnerfieldName(String innerfieldName) {
		this.innerfieldName = innerfieldName;
	}

	public String getInnerfieldValue() {
		return innerfieldValue;
	}

	public void setInnerfieldValue(String innerfieldValue) {
		this.innerfieldValue = innerfieldValue;
	}

	public FieldVO getFieldVO() {
		return fieldVO;
	}

	public void setFieldVO(FieldVO fieldVO) {
		this.fieldVO = fieldVO;
	}

	public String getDelStatus() {
		return delStatus;
	}

	public void setDelStatus(String delStatus) {
		this.delStatus = delStatus;
	}

	public FormVO getFormVO() {
		return formVO;
	}

	public void setFormVO(FormVO formVO) {
		this.formVO = formVO;
	}
}