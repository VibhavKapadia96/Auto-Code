package com.spring.DAO;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.VO.ProjectVO;
import com.spring.VO.AttachmentVO;
import com.spring.VO.FormVO;
import com.spring.VO.ModuleVO;

@Repository

public class ModuleDAO {
	@Autowired
	SessionFactory sessionFactory;

	public void insertModule(ModuleVO moduleVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();
			moduleVO.setDelstatus("Active");
			session.save(moduleVO);
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List searchModule() {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from ModuleVO where delstatus='Active'");
			List l = r.list();
			session.clear();
			session.close();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public List searchpls(ModuleVO moduleVO) {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("select projectName from ProjectVO where projectId='"
					+ moduleVO.getProjectVO().getProjectId() + "'");
			List l = r.list();
			session.clear();
			session.close();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public List manageModule() {
		try {
			ProjectVO projectVO = new ProjectVO();

			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from ModuleVO where projectVO_projectId='" + projectVO.getProjectId() + "'");
			List l = r.list();
			session.clear();
			session.close();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public void deleteModule(ModuleVO moduleVO) {

		List<FormVO> ls = new ArrayList<FormVO>();

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();

			Query query = session.createQuery(
					"update ModuleVO set delstatus='Deactive' where moduleId='" + moduleVO.getModuleId() + "'");
			query.executeUpdate();
			Query query1 = session.createQuery(
					"update FormVO set delstatus='Deactive' where moduleVO_moduleId='" + moduleVO.getModuleId() + "'");
			query1.executeUpdate();
			Query q = session.createQuery("from FormVO  where moduleVO_moduleId='" + moduleVO.getModuleId() + "'");
			ls = q.list();
			if (ls.size() > 0) {
				int formid = ls.get(0).getFormId();
				if (formid != 0) {
					Query query2 = session.createQuery(
							"update InnerFormVO set  delstatus='Deactive' where formVO_formId='" + formid + "' ");

					query2.executeUpdate();
				}
			}
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List editModule(ModuleVO moduleVO) {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from ModuleVO where moduleId='" + moduleVO.getModuleId() + "'");
			List l = r.list();
			session.clear();
			session.close();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List genModule(ModuleVO moduleVO) {
		try {
			ProjectVO projectVO = new ProjectVO();

			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery(
					" from ModuleVO where projectVO_projectId='" + moduleVO.getProjectVO().getProjectId() + "'");
			List l = r.list();
			session.clear();
			session.close();

			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateModule(ModuleVO moduleVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();

			Query query = session.createQuery("update ModuleVO set moduleName='" + moduleVO.getModuleName()
					+ "' where moduleId='" + moduleVO.getModuleId() + "'");
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

	public List searchModuleAJAX(ModuleVO moduleVO) {
		try {
			System.out.println(moduleVO.getProjectVO().getProjectId());

			Session session = this.sessionFactory.openSession();

			Query r = session.createQuery("from ModuleVO where delStatus='Active' and projectVO_projectId='"
					+ moduleVO.getProjectVO().getProjectId() + "' ");
			List l = r.list();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
}
