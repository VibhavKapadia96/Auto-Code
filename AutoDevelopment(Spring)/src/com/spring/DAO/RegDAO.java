package com.spring.DAO;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.VO.AttachmentVO;
import com.spring.VO.CityVO;
import com.spring.VO.LoginVO;
import com.spring.VO.RegVO;

@Repository
public class RegDAO {

	@Autowired
	SessionFactory sessionFactory;

	public void insertReg(RegVO regVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();
			regVO.setDelstatus("Active");
			session.save(regVO);
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String searchfnln(RegVO regVO) {

		List<RegVO> fnlnls = new ArrayList<RegVO>();
		String name = null;

		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from RegVO where delstatus='Active' and email='" + regVO.getEmail() + "'");
			fnlnls = r.list();
			String fn = fnlnls.get(0).getFirstName();
			String ln = fnlnls.get(0).getLastName();
			session.clear();
			session.close();
			name = fn + "_" + ln;
			System.out.println(name);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return name;

	}

	public List searchReg(RegVO regVO) {

		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from RegVO ");
			List l = r.list();
			session.clear();
			session.close();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public List searchRegforIndex(RegVO regVO) {

		List<RegVO> ls = new ArrayList<RegVO>();

		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from RegVO where delstatus='Active' and email='" + regVO.getEmail() + "'");
			ls = r.list();

			String fn = ls.get(0).getFirstName();
			String ln = ls.get(0).getLastName();

			List l = new ArrayList();
			l.add(0, fn);
			l.add(1, ln);
			session.clear();
			session.close();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public List searchEmailAJAX(RegVO regVO) {

		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from RegVO where delstatus='Active' and email='" + regVO.getEmail() + "'");
			List l = r.list();
			session.clear();
			session.close();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public List editReg(RegVO regVO) {

		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from RegVO where loginVO_loginId='" + regVO.getLoginVO().getLoginId() + "'");
			List l = r.list();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public void updateReg(RegVO regVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();

			regVO.setDelstatus("Active");
			Query query = session.createQuery("update RegVO set firstName ='" + regVO.getFirstName() + "', lastName = '"
					+ regVO.getLastName() + "', email='" + regVO.getEmail() + "', mobileNO='" + regVO.getMobileNO()
					+ "' where regId='" + regVO.getRegId() + "'");
			query.executeUpdate();

			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updatePassword(RegVO regVO, LoginVO loginVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();

			Query r = session.createQuery(
					"update RegVO set password='" + regVO.getPassword() + "' where email='" + regVO.getEmail() + "'");

			Query r1 = session.createQuery("update LoginVO set password='" + loginVO.getPassword() + "' where email='"
					+ loginVO.getEmail() + "'");

			int i = r.executeUpdate();
			int j = r1.executeUpdate();
			System.out.println(j);
			System.out.println(i);
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteReg(RegVO regVO) {

		List<RegVO> regList = new ArrayList<RegVO>();

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();

			Query query = session
					.createQuery("update RegVO set delstatus='Deactive' where regId='" + regVO.getRegId() + "'");
			int i = query.executeUpdate();
			Query query1 = session.createQuery("from RegVO  where regId='" + regVO.getRegId() + "'");
			regList = query1.list();
			int loginid = regList.get(0).getLoginVO().getLoginId();
			Query query2 = session.createQuery("update LoginVO set enabled='0' where loginId='" + loginid + "'");
			int q = query2.executeUpdate();

			System.out.println(i);
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String searchpassword(RegVO regVO) {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery(
					"select password from RegVO where delstatus='Active' and email='" + regVO.getEmail() + "'");
			List l = r.list();
			String password = (String) l.get(0);
			System.out.println(password);
			session.clear();
			session.close();
			return password;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void activeReg(RegVO regVO) {
		List<RegVO> regList = new ArrayList<RegVO>();

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();

			Query query = session
					.createQuery("update RegVO set delstatus='Active' where regId='" + regVO.getRegId() + "'");
			int i = query.executeUpdate();
			Query query1 = session.createQuery("from RegVO  where regId='" + regVO.getRegId() + "'");
			regList = query1.list();
			int loginid = regList.get(0).getLoginVO().getLoginId();
			Query query2 = session.createQuery("update LoginVO set enabled='1' where loginId='" + loginid + "'");
			int q = query2.executeUpdate();

			System.out.println(i);
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}