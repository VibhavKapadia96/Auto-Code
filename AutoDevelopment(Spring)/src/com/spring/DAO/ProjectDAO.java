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

import com.spring.VO.AttachmentVO;
import com.spring.VO.CountryVO;
import com.spring.VO.FormVO;
import com.spring.VO.ProjectVO;

@Repository
public class ProjectDAO {

	@Autowired
	SessionFactory sessionFactory;

	public int countprojects() {

		int projectcount = 0;
		try {
			Session session = this.sessionFactory.openSession();
			projectcount = ((Long) session.createQuery("select count(projectId) from ProjectVO").uniqueResult())
					.intValue();

			System.out.println(projectcount);

			session.clear();
			session.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return projectcount;
	}

	public void insertProject(ProjectVO projectVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();
			projectVO.setDelStatus("Active");

			session.save(projectVO);
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List searchProject(int id) {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from ProjectVO where delstatus='Active' and loginVO_loginId='" + id + "'");
			List l = r.list();
			session.clear();
			session.close();

			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public void deleteProject(ProjectVO projectVO) {

		List<FormVO> ls = new ArrayList<FormVO>();

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();

			Query query = session.createQuery(
					"update ProjectVO set delstatus='Deactive' where projectId='" + projectVO.getProjectId() + "'");
			query.executeUpdate();
			Query query1 = session.createQuery("update ModuleVO set delstatus='Deactive' where projectVO_projectId='"
					+ projectVO.getProjectId() + "'");

			query1.executeUpdate();
			Query query2 = session.createQuery("update FormVO set delstatus='Deactive' where projectVO_projectId='"
					+ projectVO.getProjectId() + "'");
			Query q = session.createQuery("from FormVO  where projectVO_projectId='" + projectVO.getProjectId() + "'");
			query2.executeUpdate();
			ls = q.list();
			if (ls.size() > 0) {
				int formid = ls.get(0).getFormId();
				if (formid != 0) {

					Query query3 = session.createQuery(
							"update InnerFormVO set  delstatus='Deactive' where formVO_formId='" + formid + "' ");
					query3.executeUpdate();
				}
			}

			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List editProject(ProjectVO projectVO) {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from ProjectVO where  projectId='" + projectVO.getProjectId() + "'");
			List l = r.list();
			session.clear();
			session.close();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List genProject(ProjectVO projectVO) {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery(
					"select projectName from ProjectVO where  projectId='" + projectVO.getProjectId() + "'");
			List l = r.list();
			session.clear();
			session.close();

			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List manageProject(ProjectVO projectVO) {
		try {

			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from ProjectVO where  projectId='" + projectVO.getProjectId() + "'");
			List l = r.list();
			session.clear();
			session.close();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateProject(ProjectVO projectVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();
			projectVO.setDelStatus("Active");
			Query query = session.createQuery("update ProjectVO set projectName='" + projectVO.getProjectName()
					+ "', downloadStatus='NO' where  projectId='" + projectVO.getProjectId() + "'");
			query.executeUpdate();
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setDownloadyes(ProjectVO projectVO) {

		try {

			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();
			Query query = session.createQuery(
					"update ProjectVO set downloadStatus='YES' where projectId='" + projectVO.getProjectId() + "' ");
			int i = query.executeUpdate();

			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String searchProjectbyId(int id) {

		List<ProjectVO> prols = new ArrayList<ProjectVO>();
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from ProjectVO where delstatus='Active' and projectId='" + id + "'");
			prols = r.list();
			session.clear();
			session.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return prols.get(0).getProjectName();

	}

	public int countdownprojects() {
		int downprojects = 0;
		try {
			Session session = this.sessionFactory.openSession();
			Query query = session.createQuery("from ProjectVO where downloadStatus = 'YES'");
			List ls = query.list();
			downprojects = ls.size();

			System.out.println(downprojects);

			session.clear();
			session.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return downprojects;
	}

	public int countloginprojects(int id) {
		int projectcount = 0;
		try {
			Session session = this.sessionFactory.openSession();
			Query query = session.createQuery("from ProjectVO where loginVO_loginId = '" + id + "'");
			List ls = query.list();
			projectcount = ls.size();

			System.out.println(projectcount);

			session.clear();
			session.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return projectcount;
	}

	public int countuserdownprojects(int id) {
		int downprojects = 0;
		try {
			Session session = this.sessionFactory.openSession();
			Query query = session
					.createQuery("from ProjectVO where downloadStatus = 'YES'  and loginVO_loginId = '" + id + "'");
			List ls = query.list();
			downprojects = ls.size();

			System.out.println(downprojects);

			session.clear();
			session.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return downprojects;
	}
}
