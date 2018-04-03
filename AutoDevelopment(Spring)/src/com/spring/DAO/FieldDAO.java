package com.spring.DAO;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.VO.FieldVO;

@Repository
public class FieldDAO {

	@Autowired
	SessionFactory sessionFactory;

	public void insertField(FieldVO fieldVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();
			fieldVO.setDelstatus("Active");

			session.save(fieldVO);
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List searchField() {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from FieldVO where delstatus='Active'");
			List l = r.list();
			session.clear();
			session.close();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public void deleteField(FieldVO fieldVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();
			
			Query query=session.createQuery("update FieldVO set delstatus='Deactive' where FieldId='"+fieldVO.getFieldId()+"'");
			int i=query.executeUpdate();
			System.out.println(i);
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List editField(FieldVO fieldVO) {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from FieldVO where  FieldId='" + fieldVO.getFieldId() + "'");
			List l = r.list();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateField(FieldVO fieldVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();
			fieldVO.setDelstatus("Active");
			session.update(fieldVO);
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
