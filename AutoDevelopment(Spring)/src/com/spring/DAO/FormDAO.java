package com.spring.DAO;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.VO.FormVO;
import com.spring.VO.ModuleVO;

@Repository

public class FormDAO {

	@Autowired
	SessionFactory sessionFactory;

	public int insertForm(FormVO formVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();
			formVO.setDelstatus("Active");
			session.save(formVO);
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return formVO.getFormId();

	}

	public List searchprojectLs(FormVO formVO) {
		List pl = new ArrayList();
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery(
					"select projectName from ProjectVO where projectId='" + formVO.getProjectVO().getProjectId() + "'");
			pl = r.list();
			session.clear();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pl;

	}

	public List searchmoduleLs(FormVO formVO) {
		List pl = new ArrayList();

		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery(
					"select moduleName from ModuleVO where moduleId='" + formVO.getModuleVO().getModuleId() + "'");
			pl = r.list();
			session.clear();
			session.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return pl;

	}

	public List searchForm() {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from FormVO where delstatus='Active'");
			List l = r.list();
			session.clear();
			session.close();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public List editForm(FormVO formVO) {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from FormVO where formId='" + formVO.getFormId() + "'");
			List l = r.list();
			session.clear();
			session.close();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List genForm(FormVO formVO) {
		try {
			ModuleVO moduleVO = new ModuleVO();
			Session session = this.sessionFactory.openSession();
			Query r = session
					.createQuery(" from FormVO where moduleVO_moduleId='" + formVO.getModuleVO().getModuleId() + "'");
			List l = r.list();
			session.clear();
			session.close();

			return l;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateForm(FormVO formVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();

			Query query = session.createQuery("update FormVO set formName='" + formVO.getFormName() + "' where formId='"
					+ formVO.getFormId() + "'");
			int i = query.executeUpdate();
			System.out.println(i);
			tr.commit();
			/*
			 * moduleVO.setDelstatus("Active"); session.saveOrUpdate(moduleVO);
			 * tr.commit();
			 */
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteForm(FormVO formVO) {
		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();

			Query query = session
					.createQuery("update FormVO set delstatus='Deactive' where formId	='" + formVO.getFormId() + "'");
			Query query1 = session.createQuery(
					"update InnerFormVO set delstatus='Deactive' where formVO_formId	='" + formVO.getFormId() + "'");

			query.executeUpdate();
			query1.executeUpdate();
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}