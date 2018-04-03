package com.spring.DAO;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.VO.CountryVO;

@Repository
public class CountryDAO {

	@Autowired
	SessionFactory sessionFactory;

	public void insertCountry(CountryVO countryVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();
			countryVO.setDelstatus("Active");

			session.save(countryVO);
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List searchCountry() {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from CountryVO where delstatus='Active'");
			List l = r.list();
			session.clear();
			session.close();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public void deleteCountry(CountryVO countryVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();
			
			Query query=session.createQuery("update CountryVO set delstatus='Deactive' where countryId='"+countryVO.getCountryId()+"'");
			int i=query.executeUpdate();
			System.out.println(i);
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List editCountry(CountryVO countryVO) {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from CountryVO where  countryId='" + countryVO.getCountryId() + "'");
			List l = r.list();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateCountry(CountryVO countryVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();
			countryVO.setDelstatus("Active");
			session.update(countryVO);
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
