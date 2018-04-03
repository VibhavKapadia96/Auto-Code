package com.spring.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spring.DAO.FormDAO;
import com.spring.DAO.InnerFormDAO;
import com.spring.DAO.ModuleDAO;
import com.spring.DAO.ProjectDAO;
import com.spring.DAO.loginDAO;
import com.spring.VO.CountryVO;
import com.spring.VO.LoginVO;
import com.spring.VO.ProjectVO;

@Controller
public class ProjectController {

	@Autowired
	ProjectDAO projectdao;

	@Autowired
	ModuleDAO moduledao;
	@Autowired
	FormDAO formdao;

	@Autowired
	InnerFormDAO innerformdao;

	@Autowired
	loginDAO logindao;

	@RequestMapping(value = "/addProject.html", method = RequestMethod.GET)
	public String loadCountry(Model model, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return ("redirect:Login.html");
		}
		model.addAttribute("project", new ProjectVO());

		return ("user/addProject");
	}

	@RequestMapping(value = "/insertProject.html", method = RequestMethod.GET)
	public ModelAndView insertProject(@ModelAttribute ProjectVO projectVO, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}
		LoginVO loginVO = new LoginVO();

		String email = BaseMethods.getCurrentUser().getUsername();
		loginVO.setEmail(email);

		int id = logindao.searchLogin(loginVO);
		System.out.println(id);

		loginVO.setLoginId(id);
		projectVO.setDownloadStatus("NO");
		projectVO.setLoginVO(loginVO);

		this.projectdao.insertProject(projectVO);
		projectVO.getProjectName();
		System.out.println("PRO Name......" + projectVO.getProjectName());
		session.setAttribute("fadeinfadeoutprojectcount", 1);

		/*
		 * if (submit.equals("Add")) { return new
		 * ModelAndView("redirect:addProject.html"); } if
		 * (submit.equals("Submit")) { return new
		 * ModelAndView("redirect:addModule.html");
		 * 
		 * }
		 */
		return new ModelAndView("redirect:addProject.html");

	}

	@RequestMapping(value = "/searchProject.html", method = RequestMethod.GET)
	public ModelAndView searchCountry(@ModelAttribute ProjectVO projectVO, HttpSession session) {
		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		LoginVO loginVO = new LoginVO();

		String email = BaseMethods.getCurrentUser().getUsername();
		System.out.println(email);
		loginVO.setEmail(email);

		int loginid = logindao.searchLogin(loginVO);

		List ls = projectdao.searchProject(loginid);
		List moduleList = moduledao.searchModule();
		List formList = formdao.searchForm();

		return new ModelAndView("user/searchProject", "ls", ls).addObject("moduleList", moduleList)
				.addObject("formList", formList);

	}

	@RequestMapping(value = "/manageProject.html", method = RequestMethod.GET)
	public ModelAndView manageProject(@ModelAttribute ProjectVO projectVO, @RequestParam("projectId") int projectId,
			HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		session.setAttribute("projectId", projectId);
		projectVO.setProjectId(projectId);
		List ls = projectdao.manageProject(projectVO);

		List moduleList = moduledao.searchModule();
		List formList = formdao.searchForm();
		List innerformList = innerformdao.searchinnerForm();
		session.setAttribute("moduleList", moduleList);
		session.setAttribute("formList", formList);
		session.setAttribute("ls", ls);

		return new ModelAndView("user/manageProject", "ls", ls).addObject("moduleList", moduleList)
				.addObject("formList", formList).addObject("innerformList", innerformList);

	}

	@RequestMapping(value = "/deleteProject.html", method = RequestMethod.GET)
	public ModelAndView deleteProject(@ModelAttribute ProjectVO projectVO, HttpSession session,
			@RequestParam("projectId") int projectId) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}
		projectVO.setProjectId(projectId);
		this.projectdao.deleteProject(projectVO);

		session.setAttribute("fadeinfadeoutdeleteprojectcount", 1);
		return new ModelAndView("redirect:searchProject.html");
	}

	@RequestMapping(value = "/editProject.html", method = RequestMethod.GET)
	public ModelAndView editCountry(@ModelAttribute ProjectVO projectVO, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}
		List list = projectdao.editProject(projectVO);
		return new ModelAndView("user/updateProject", "projectlist", (ProjectVO) list.get(0));
	}

	@RequestMapping(value = "/updateProject.html", method = RequestMethod.GET)
	public ModelAndView saveUpdate(@ModelAttribute ProjectVO projectVO, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		projectdao.updateProject(projectVO);
		int projectId = (int) session.getAttribute("projectId");
		session.setAttribute("fadeinfadeoutupdateprojectcount", 1);
		return new ModelAndView("redirect:manageProject.html", "projectId", projectId);
	}

}