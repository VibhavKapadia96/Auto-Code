package com.spring.DAO;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.spring.VO.FeedbackVO;

@Repository
public class FeedbackDAO {

	@Autowired
	SessionFactory sessionFactory;

	public void insertFeedback(FeedbackVO feedbackVO) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();

			feedbackVO.setDelStatus("Active");

			session.save(feedbackVO);
			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List searchFeedback() {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from FeedbackVO ORDER BY presentfeedbackdate DESC ");
			List l = r.list();
			session.clear();
			session.close();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

}
