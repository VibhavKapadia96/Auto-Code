package com.spring.Controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.DAO.ComplaintDAO;
import com.spring.DAO.ProjectDAO;
import com.spring.DAO.RegDAO;
import com.spring.DAO.loginDAO;
import com.spring.VO.LoginVO;
import com.spring.VO.ProjectVO;
import com.spring.VO.RegVO;

@Controller
public class MainController {

	@Autowired
	loginDAO loginDAO;

	@Autowired
	ProjectDAO projectdao;

	@Autowired
	ComplaintDAO complaintdao;

	@Autowired
	RegDAO regdao;

	int countcheck = 0;

	@RequestMapping(value = { "/", "/index.html" }, method = RequestMethod.GET)
	public String loadindex(HttpSession session) {
		System.out.println("***MainIndex***");

		String emailLogin = (String) session.getAttribute("email");

		if (emailLogin != null) {

			return ("admin/mainindex2");

		}

		else {
			return ("admin/MainIndex");

		}

	}

	@RequestMapping(value = "contact.html", method = RequestMethod.GET)
	public String contcat(HttpSession session) {
		System.out.println("***Contact***");

		return ("admin/Contact");

	}

	@RequestMapping(value = "/admin.html", method = RequestMethod.GET)
	public String loadadmin(HttpSession session) {
		System.out.println("***Index***");

		String email = BaseMethods.getCurrentUser().getUsername();

		session.setAttribute("email", email);

		int users = loginDAO.countusers();
		session.setAttribute("userCounts", users);

		int projects = projectdao.countprojects();
		session.setAttribute("projectCounts", projects);

		int complaints = complaintdao.countcomplaints();
		session.setAttribute("complaintCounts", complaints);

		int downloads = projectdao.countdownprojects();
		session.setAttribute("downloadCounts", downloads);

		if (countcheck == 0) {
			session.setAttribute("fadeinfadeoutindexcount", 1);
			countcheck++;
		}
		session.setAttribute("fadeinfadeoutaddfieldcount", 0);
		session.setAttribute("fadeinfadeoutupdatefieldcount", 0);
		session.setAttribute("fadeinfadeoutdeletefieldcount", 0);

		return ("admin/index");

	}

	@RequestMapping(value = "/user.html", method = RequestMethod.GET)
	public String loaduser(HttpSession session) {
		System.out.println("***Index***");

		String email = BaseMethods.getCurrentUser().getUsername();
		session.setAttribute("email", email);

		LoginVO loginVO = new LoginVO();
		loginVO.setEmail(email);

		int loginid = loginDAO.searchLogin(loginVO);

		int projects = projectdao.countloginprojects(loginid);
		session.setAttribute("projectuserCounts", projects);

		int downloads = projectdao.countuserdownprojects(loginid);
		session.setAttribute("downloaduserCounts", downloads);

		RegVO regVO = new RegVO();
		regVO.setEmail(email);
		List regList = regdao.searchRegforIndex(regVO);
		String firstNameindex = regList.get(0).toString();
		String lastNameindex = regList.get(1).toString();
		session.setAttribute("firstNameindex", firstNameindex);
		session.setAttribute("lastNameindex", lastNameindex);
		
		session.setAttribute("UserLoginId", loginid);
		if (countcheck == 0) {
			session.setAttribute("fadeinfadeoutindexcount", 1);
			countcheck++;
		}
		session.setAttribute("fadeinfadeoutprojectcount", 0);
		session.setAttribute("fadeinfadeoutmodulecount", 0);
		session.setAttribute("fadeinfadeoutformcount", 0);
		session.setAttribute("fadeinfadeoutcodegencount", 0);
		session.setAttribute("fadeinfadeoutupdateprojectcount", 0);
		session.setAttribute("fadeinfadeoutdeleteprojectcount", 0);
		session.setAttribute("fadeinfadeoutupdatemodulecount", 0);
		session.setAttribute("fadeinfadeoutdeletemodulecount", 0);
		session.setAttribute("fadeinfadeoutupdateformcount", 0);
		session.setAttribute("fadeinfadeoutdeleteformcount", 0);
		session.setAttribute("fadeinfadeoutupdateinnerformcount", 0);
		session.setAttribute("fadeinfadeoutdeleteinnerformcount", 0);

		return ("user/index");

	}

	@RequestMapping(value = "/Login.html", method = RequestMethod.GET)
	public String login(HttpSession session) {

		countcheck = 0;
		session.setAttribute("fadeinfadeoutindexcount", 0);

		LoginVO loginVO = new LoginVO();

		String emailLogin = (String) session.getAttribute("email");

		if (emailLogin != null) {
			loginVO.setEmail(emailLogin);

			int loginid = loginDAO.searchLogin(loginVO);

			if (loginid == 1) {

				return ("redirect:admin.html");
			} else {

				return ("redirect:user.html");
			}
		}

		else {
			return "admin/Login";
		}
	}

	@RequestMapping(value = "/loginerror.html", method = RequestMethod.GET)
	public String loginerror(HttpSession session) {

		LoginVO loginVO = new LoginVO();

		String emailLogin = (String) session.getAttribute("email");

		if (emailLogin != null) {
			loginVO.setEmail(emailLogin);

			int loginid = loginDAO.searchLogin(loginVO);

			if (loginid == 1) {

				return ("redirect:admin.html");
			} else {

				return ("redirect:user.html");
			}
		}

		else {
			return "admin/Login";
		}

	}

}
