package com.spring.VO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


	@Entity
	@Table(name = "Module")
	public class ModuleVO {

		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private int moduleId;

		@Column
		private String moduleName;

		
		@Column
		private String delstatus;
		
		@ManyToOne
		private ProjectVO projectVO;
		
			

		public int getModuleId() {
			return moduleId;
		}

		public void setModuleId(int moduleId) {
			this.moduleId = moduleId;
		}

		public String getModuleName() {
			return moduleName;
		}

		public void setModuleName(String moduleName) {
			this.moduleName = moduleName;
		}

		public String getDelstatus() {
			return delstatus;
		}

		public void setDelstatus(String delstatus) {
			this.delstatus = delstatus;
		}

		public ProjectVO getProjectVO() {
			return projectVO;
		}

		public void setProjectVO(ProjectVO projectVO) {
			this.projectVO = projectVO;
		}

		

}
