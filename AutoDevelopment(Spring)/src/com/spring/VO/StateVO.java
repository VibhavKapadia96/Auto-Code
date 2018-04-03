package com.spring.VO;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


	@Entity
	@Table(name = "State")
	public class StateVO {


		@Id
		@GeneratedValue(strategy = GenerationType.AUTO)
		private int stateId;

		@Column
		private String stateName;

		@Column
		private String stateDescription;

		@Column
		private String delstatus;
		
		public String getDelstatus() {
			return delstatus;
		}

		public void setDelstatus(String delstatus) {
			this.delstatus = delstatus;
		}

		@ManyToOne
		private CountryVO countryVO;

		public int getStateId() {
			return stateId;
		}

		public void setStateId(int stateId) {
			this.stateId = stateId;
		}

		public String getStateName() {
			return stateName;
		}

		public void setStateName(String stateName) {
			this.stateName = stateName;
		}

		public String getStateDescription() {
			return stateDescription;
		}

		public void setStateDescription(String stateDescription) {
			this.stateDescription = stateDescription;
		}

		public CountryVO getCountryVO() {
			return countryVO;
		}

		public void setCountryVO(CountryVO countryVO) {
			this.countryVO = countryVO;
		}
		
		
	

}
