package com.spring.DAO;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.VO.CountryVO;
import com.spring.VO.StateVO;

@Repository

public class StateDAO {
	@Autowired
	SessionFactory sessionFactory;

	public void insertState(StateVO stateVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();
			stateVO.setDelstatus("Active");
			session.save(stateVO);
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List searchStateAJAX(CountryVO countryVO) {
		try {
			
			Session session = this.sessionFactory.openSession();
			
			Query r = session.createQuery("from StateVO where delStatus='Active' and countryVO='"+countryVO.getCountryId()+"' ");
			List l = r.list();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public List searchState() {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from StateVO where delstatus='Active'");
			List l = r.list();
			session.clear();
			session.close();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public void deleteState(StateVO stateVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();

			Query query = session.createQuery(
					"update StateVO set delstatus='Deactive' where stateId='" + stateVO.getStateId() + "'");
			int i = query.executeUpdate();
			System.out.println(i);
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List editState(StateVO stateVO) {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from StateVO where stateId='" + stateVO.getStateId() + "'");
			List l = r.list();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateState(StateVO stateVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();
			stateVO.setDelstatus("Active");
			session.saveOrUpdate(stateVO);
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
