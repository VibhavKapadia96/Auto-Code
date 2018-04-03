package com.spring.DAO;

import java.util.ArrayList;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.VO.ComplaintVO;
import com.spring.VO.LoginVO;
import com.spring.VO.RegVO;

@Repository
public class loginDAO {

	@Autowired
	SessionFactory sessionFactory;

	public int insertLogin(LoginVO loginVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();

			session.save(loginVO);
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginVO.getLoginId();
	}

	public String searchPasswordAJAX(LoginVO loginVO) {

		List<LoginVO> ls = new ArrayList<LoginVO>();

		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from LoginVO where  email='" + loginVO.getEmail() + "'");
			ls = r.list();
			session.clear();
			session.close();
			return ls.get(0).getPassword();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public int searchLogin(LoginVO loginVO) {

		List<LoginVO> ls = new ArrayList<LoginVO>();

		try {

			Session session = sessionFactory.openSession();

			Query r = session.createQuery("from LoginVO where email='" + loginVO.getEmail() + "'");
			ls = r.list();

			session.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ls.get(0).getLoginId();
	}

	public List searchLoginadmin() {
		// TODO Auto-generated method stub
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from LoginVO where role='ROLE_ADMIN'");

			List l = r.list();
			session.clear();
			session.close();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public List searchLoginuser() {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from ComplaintVO where role='ROLE_ADMIN'");

			List l = r.list();
			session.clear();
			session.close();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public int countusers() {

		int usercount = 0;
		try {
			Session session = this.sessionFactory.openSession();
			usercount = ((Long) session.createQuery("select count(loginId) from LoginVO").uniqueResult()).intValue();

			System.out.println(usercount);

			session.clear();
			session.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return usercount;
	}

}
