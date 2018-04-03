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
import com.spring.VO.ComplaintVO;
import com.spring.VO.CountryVO;
import com.spring.VO.LoginVO;

@Repository
public class ComplaintDAO {

	@Autowired
	SessionFactory sessionFactory;

	public void insertComaplaint(ComplaintVO complaintVO) {

		try {

			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();
			AttachmentVO attachmentVO = new AttachmentVO();
			complaintVO.setComplaintStatus("Pending");
			complaintVO.setDelStatus("Active");

			session.save(complaintVO);

			tr.commit();

			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List searchComplaint() {
		try {
			Session session = this.sessionFactory.openSession();
			Query r = session.createQuery("from ComplaintVO  ORDER BY presentdate DESC   ");
			List l = r.list();
			session.clear();
			session.close();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public List editComplaint(ComplaintVO complaintVO) {
		try {
			Session session = this.sessionFactory.openSession();

			Query r = session.createQuery("from ComplaintVO where  complaintId='" + complaintVO.getComplaintId() + "'");
			List l = r.list();
			return l;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateComplaint(ComplaintVO complaintVO, int To, int From) {

		try {
			Session session = sessionFactory.openSession();
			Transaction tr = session.beginTransaction();

			Query query = session.createQuery("update ComplaintVO set complaintStatus='Replay', complaintReplay='"
					+ complaintVO.getComplaintReplay() + "', complaintFrom_loginId  = '" + To
					+ "', complaintTo_loginId='" + From + "'  where complaintId='" + complaintVO.getComplaintId()
					+ "'");

			query.executeUpdate();

			tr.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertAttachmnet(AttachmentVO attachmentVO) {

		Session session = sessionFactory.openSession();
		Transaction tr = session.beginTransaction();
		session.save(attachmentVO);
		tr.commit();
		session.close();

	}

	public String searchAttachmnet(ComplaintVO complaintVO) {
		List<AttachmentVO> Attachls = new ArrayList<AttachmentVO>();
		String AttachPath = null;
		String AttachfileName = null;
		String FilePath = null;
		try {
			Session session = sessionFactory.openSession();
			Query r = session.createQuery(
					" from AttachmentVO where complaintVO_complaintId='" + complaintVO.getComplaintId() + "'");
			Attachls = r.list();
			AttachPath = Attachls.get(0).getFilePath();
			AttachfileName = Attachls.get(0).getFileName();
			FilePath = AttachPath + "//doc//" + AttachfileName;
			session.clear();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (AttachfileName == null) {
			return "Null";
		} else {
			return FilePath;
		}
	}

	public int searchComplaintById(ComplaintVO complaintVO) {

		List<ComplaintVO> ls = new ArrayList<ComplaintVO>();

		try {

			Session session = sessionFactory.openSession();

			Query r = session.createQuery(" from ComplaintVO where complaintId='" + complaintVO.getComplaintId() + "'");
			ls = r.list();
			session.clear();
			session.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ls.get(0).getComplaintFrom().getLoginId();
	}

	public List searchviewComplaint(int id) {

		List<ComplaintVO> ls = new ArrayList<ComplaintVO>();

		try {

			Session session = sessionFactory.openSession();

			Query r = session.createQuery(
					"from ComplaintVO where complaintFrom_loginId='" + id + "' or complaintTo_loginId='" + id + "'");
			ls = r.list();
			session.clear();
			session.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return ls;

	}

	public int countcomplaints() {

		int complaintcount = 0;
		try {
			Session session = this.sessionFactory.openSession();
			complaintcount = ((Long) session.createQuery("select count(complaintId) from ComplaintVO").uniqueResult())
					.intValue();

			System.out.println(complaintcount);

			session.clear();
			session.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return complaintcount;
	}

}
