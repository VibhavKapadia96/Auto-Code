package com.spring.VO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Form")
public class FormVO {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int formId;

	@ManyToOne
	private ProjectVO projectVO;

	@ManyToOne
	private ModuleVO moduleVO;

	@Column
	private String formName;

	@Column
	private String delstatus;

	public int getFormId() {
		return formId;
	}

	public void setFormId(int formId) {
		this.formId = formId;
	}

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public ProjectVO getProjectVO() {
		return projectVO;
	}

	public void setProjectVO(ProjectVO projectVO) {
		this.projectVO = projectVO;
	}

	public String getDelstatus() {
		return delstatus;
	}

	public void setDelstatus(String delstatus) {
		this.delstatus = delstatus;
	}

	public ModuleVO getModuleVO() {
		return moduleVO;
	}

	public void setModuleVO(ModuleVO moduleVO) {
		this.moduleVO = moduleVO;
	}
}