package com.spring.Controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spring.DAO.ModuleDAO;
import com.spring.DAO.ProjectDAO;
import com.spring.DAO.loginDAO;
import com.spring.VO.LoginVO;
import com.spring.VO.ModuleVO;
import com.spring.VO.ProjectVO;

@Controller
public class ModuleController {

	@Autowired
	ModuleDAO moduledao;

	@Autowired
	ProjectDAO projectdao;

	@Autowired
	loginDAO logindao;

	@RequestMapping(value = "/addModule.html", method = RequestMethod.GET)
	public String loadState(Model model, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return ("redirect:Login.html");
		}

		LoginVO loginVO = new LoginVO();

		String email = BaseMethods.getCurrentUser().getUsername();
		System.out.println(email);
		loginVO.setEmail(email);

		int loginid = logindao.searchLogin(loginVO);

		List projectList = this.projectdao.searchProject(loginid);
		model.addAttribute("module", new ModuleVO());

		session.setAttribute("projectList", projectList);
		return ("user/addModule");
	}

	@RequestMapping(value = "/insertModule.html", method = RequestMethod.GET)
	public ModelAndView insertModule(@ModelAttribute ModuleVO moduleVO, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		this.moduledao.insertModule(moduleVO);

		System.out.println("projectName............." + moduleVO.getProjectVO().getProjectId());
		System.out.println("Module Name......" + moduleVO.getModuleName());

		List pls = this.moduledao.searchpls(moduleVO);
		String pl = (String) pls.get(0);
		System.out.println("name" + pl);
		ProjectVO projectVO = new ProjectVO();
		projectVO.setProjectName(pl);
		session.setAttribute("fadeinfadeoutprojectcount", 1);

		return new ModelAndView("redirect:addModule.html");
	}

	@RequestMapping(value = "/searchModule.html", method = RequestMethod.GET)
	public ModelAndView searchModule(@ModelAttribute ModuleVO moduleVO, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		List ls = moduledao.searchModule();
		return new ModelAndView("user/searchModule", "ls", ls);

	}

	@RequestMapping(value = "/deleteModule.html", method = RequestMethod.GET)
	public ModelAndView deleteModule(@ModelAttribute ModuleVO moduleVO, @RequestParam("moduleId") int moduleId,
			HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}
		moduleVO.setModuleId(moduleId);
		this.moduledao.deleteModule(moduleVO);
		int projectId = (int) session.getAttribute("projectId");
		session.setAttribute("fadeinfadeoutdeletemodulecount", 1);
		

		return new ModelAndView("redirect:manageProject.html", "projectId", projectId);

	}

	@RequestMapping(value = "/editModule.html", method = RequestMethod.GET)
	public ModelAndView editModule(@ModelAttribute ModuleVO moduleVO, @RequestParam("moduleId") int moduleId,
			HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		moduleVO.setModuleId(moduleId);
		ProjectVO projectVO = new ProjectVO();
		moduleVO.setProjectVO(projectVO);

		List modulelist = this.moduledao.editModule(moduleVO);

		return new ModelAndView("user/updateModule", "modulelist", (ModuleVO) modulelist.get(0));
	}

	@RequestMapping(value = "/updateModule.html", method = RequestMethod.GET)
	public ModelAndView saveUpdate(@ModelAttribute ModuleVO moduleVO, @RequestParam("moduleId") int moduleId,
			HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		moduleVO.setModuleId(moduleId);
		moduledao.updateModule(moduleVO);
		int projectId = (int) session.getAttribute("projectId");
		session.setAttribute("fadeinfadeoutupdatemodulecount", 1);
		

		return new ModelAndView("redirect:manageProject.html", "projectId", projectId);
	}
}
