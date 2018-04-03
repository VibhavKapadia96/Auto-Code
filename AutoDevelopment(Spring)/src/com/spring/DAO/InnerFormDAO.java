package com.spring.DAO;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.VO.FormVO;
import com.spring.VO.InnerFormVO;

@Repository
public class InnerFormDAO {

	@Autowired
	SessionFactory sessionFactory;

	public void insertinnerForm(InnerFormVO innerformVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();
			session.save(innerformVO);
			tr.commit();
			System.out.println("DONE");
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List searchFieldLs(InnerFormVO innerFormVO) {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery(
					"select fieldName from FieldVO where fieldId= '" + innerFormVO.getFieldVO().getFieldId() + "'");
			List l = r.list();
			session.clear();
			session.close();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public List searchinnerForm() {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from InnerFormVO where delstatus='Active'");
			List l = r.list();
			session.clear();
			session.close();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public List editinnerForm(InnerFormVO innerformVO) {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from InnerFormVO where innerFormId='" + innerformVO.getInnerFormId() + "'");
			List l = r.list();
			session.clear();
			session.close();

			return l;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List genInnerForm(InnerFormVO innerformVO) {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session
					.createQuery("from InnerFormVO where formVO_formId='" + innerformVO.getFormVO().getFormId() + "'");
			List l = r.list();
			session.clear();
			session.close();
			return l;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateinnerform(InnerFormVO innerformVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();
			innerformVO.setDelStatus("Active");
			session.saveOrUpdate(innerformVO);
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteinnerForm(InnerFormVO innerFormVO) {
		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();

			Query query = session.createQuery("update InnerFormVO set delstatus='Deactive' where innerFormId	='"
					+ innerFormVO.getInnerFormId() + "'");
			int i = query.executeUpdate();
			System.out.println(i);
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void insertinnformfield(InnerFormVO innerFormVO) {
		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();
			session.save(innerFormVO);
			tr.commit();
			System.out.println("DONE");
			session.clear();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
