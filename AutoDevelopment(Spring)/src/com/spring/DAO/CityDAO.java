package com.spring.DAO;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.VO.CityVO;
import com.spring.VO.CountryVO;
import com.spring.VO.StateVO;

@Repository

public class CityDAO {

	@Autowired
	SessionFactory sessionFactory;

	public void insertCity(CityVO cityVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();
			cityVO.setDelstatus("Active");
			session.save(cityVO);
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public List searchCityAJAX(StateVO stateVO) {
		try {
			
			Session session = this.sessionFactory.openSession();
			
			Query r = session.createQuery("from CityVO where delStatus='Active' and stateVO='"+stateVO.getStateId()+"' ");
			List l = r.list();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}


	public List searchCity() {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from CityVO  where delstatus='Active'");

			List l = r.list();
			session.clear();
			session.close();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public void deleteCity(CityVO cityVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();

			Query query = session
					.createQuery("update CityVO set delstatus='Deactive' where cityId='" + cityVO.getCityId() + "'");
			int i = query.executeUpdate();
			System.out.println(i);
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List editCity(CityVO cityVO) {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from CityVO where cityId='" + cityVO.getCityId() + "'");
			List l = r.list();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateCity(CityVO cityVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();
			System.out.println("IN DAO Before Insert");
			cityVO.setDelstatus("Active");
			session.saveOrUpdate(cityVO);
			System.out.println("IN DAO After Insert");
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
