package com.spring.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spring.DAO.FieldDAO;
import com.spring.DAO.FormDAO;
import com.spring.DAO.InnerFormDAO;
import com.spring.DAO.ModuleDAO;
import com.spring.DAO.ProjectDAO;
import com.spring.DAO.loginDAO;
import com.spring.VO.CityVO;
import com.spring.VO.FieldVO;
import com.spring.VO.FormVO;
import com.spring.VO.InnerFormVO;
import com.spring.VO.LoginVO;
import com.spring.VO.ModuleVO;
import com.spring.VO.ProjectVO;
import com.spring.VO.StateVO;

@Controller
public class FormController {

	@Autowired
	FieldDAO fielddao;

	@Autowired
	ModuleDAO moduledao;

	@Autowired
	ProjectDAO projectdao;

	@Autowired
	FormDAO formdao;

	@Autowired
	InnerFormDAO innerformdao;

	@Autowired
	loginDAO logindao;

	List<InnerFormVO> TempInnerFormList = new ArrayList<InnerFormVO>();
	List vlist = new ArrayList();
	List vvlist = new ArrayList();

	int i = 0;
	int j = 0;
	int k = 0;

	@RequestMapping(value = "/addForm.html", method = RequestMethod.GET)
	public String loadForm(Model model, HttpSession session) {

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
		List moduleList = this.moduledao.searchModule();
		List fieldList = this.fielddao.searchField();
		model.addAttribute("form", new FormVO());
		session.setAttribute("projectList", projectList);
		session.setAttribute("moduleList", moduleList);
		session.setAttribute("fieldList", fieldList);
		return ("user/addForm");
	}

	@RequestMapping(value = "/insertinnerForm.html", method = RequestMethod.GET)
	public ModelAndView insertinnerform(@RequestParam("innerfieldName") String fieldName,
			@RequestParam("innerfieldValue") String fieldValue, @RequestParam("innerfieldType") int fieldId,
			HttpServletRequest request, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		System.out.println("insertinnerForm.html");

		TempInnerFormList = (ArrayList<InnerFormVO>) request.getSession().getAttribute("TempInnerFormList");
		if (TempInnerFormList == null) {
			this.TempInnerFormList = new ArrayList<InnerFormVO>();
		}

		FieldVO fieldVO = new FieldVO();
		fieldVO.setFieldId(fieldId);

		InnerFormVO innerformVO = new InnerFormVO();
		innerformVO.setInnerfieldName(fieldName);
		innerformVO.setInnerfieldValue(fieldValue);
		innerformVO.setFieldVO(fieldVO);
		innerformVO.setDelStatus("Active");

		this.TempInnerFormList.add(innerformVO);

		request.getSession().setAttribute("TempInnerFormList", TempInnerFormList);

		System.out.println(TempInnerFormList.size());

		return new ModelAndView("admin/freeJason");
	}

	@RequestMapping(value = "/insertForm.html", method = RequestMethod.GET)
	public ModelAndView insertform(HttpServletRequest request, @ModelAttribute FormVO formVO, HttpSession session)
			throws IOException {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		/* Form insert */
		int formId = this.formdao.insertForm(formVO);
		/* Form insert */

		System.out.println("formId---->>>" + formId);

		List InnerFormList = (List<InnerFormVO>) request.getSession().getAttribute("TempInnerFormList");

		for (Iterator iterator = InnerFormList.iterator(); iterator.hasNext();) {

			InnerFormVO innerFormVO = (InnerFormVO) iterator.next();
			FormVO vo = new FormVO();
			vo.setFormId(formId);
			innerFormVO.setFormVO(vo);

			System.out.println("Field Name:::::" + innerFormVO.getInnerfieldName());
			System.out.println("Field Value::::" + innerFormVO.getInnerfieldValue());
			System.out.println("Field Type:::::" + innerFormVO.getFieldVO().getFieldId());
			System.out.println("Form ID:::::" + innerFormVO.getFormVO().getFormId());

			this.innerformdao.insertinnerForm(innerFormVO);
			List fl = this.innerformdao.searchFieldLs(innerFormVO);
			String fls = (String) fl.get(0);
			FieldVO fieldVO = new FieldVO();
			fieldVO.setFieldName(fls);
			System.out.println("fieldname>>>>>>>>>." + innerFormVO.getInnerfieldName());
			System.out.println("fieldtype>>>>>>>>." + fieldVO.getFieldName());
			System.out.println("fieldValule......." + innerFormVO.getInnerfieldValue());

		}

		request.getSession().removeAttribute("TempInnerFormList");
		session.setAttribute("fadeinfadeoutprojectcount", 1);

		return new ModelAndView("redirect:addForm.html");

	}

	@RequestMapping(value = "/editForm.html", method = RequestMethod.GET)
	public ModelAndView editModule(@ModelAttribute FormVO formVO, @RequestParam("formId") int formId,
			HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		formVO.setFormId(formId);

		List formlist = this.formdao.editForm(formVO);

		return new ModelAndView("user/updateForm", "formlist", (FormVO) formlist.get(0));
	}

	@RequestMapping(value = "/updateForm.html", method = RequestMethod.GET)
	public ModelAndView saveUpdate(@ModelAttribute FormVO formVO, @RequestParam("formId") int formId,
			HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		formVO.setFormId(formId);
		formdao.updateForm(formVO);
		int projectId = (int) session.getAttribute("projectId");
		session.setAttribute("fadeinfadeoutupdateformcount", 1);

		return new ModelAndView("redirect:manageProject.html", "projectId", projectId);
	}

	@RequestMapping(value = "/deleteForm.html", method = RequestMethod.GET)
	public ModelAndView deleteField(@ModelAttribute FormVO formVO, @RequestParam("formId") int formId,
			HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}
		formVO.setFormId(formId);
		this.formdao.deleteForm(formVO);
		int projectId = (int) session.getAttribute("projectId");
		session.setAttribute("fadeinfadeoutdeleteformcount", 1);

		return new ModelAndView("redirect:manageProject.html", "projectId", projectId);
	}

	@RequestMapping(value = "/addinnerformfield.html", method = RequestMethod.GET)
	public String addinnerformfield(@ModelAttribute FormVO formVO, @RequestParam("formId") int formId,
			HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return ("redirect:Login.html");
		}

		System.out.println(formId);
		session.setAttribute("formId", formId);
		List fieldList = fielddao.searchField();
		session.setAttribute("fieldList", fieldList);
		InnerFormVO innerFormVO = new InnerFormVO();
		formVO.setFormId(formId);
		innerFormVO.setFormVO(formVO);
		return ("user/addinnerformfield");
	}

	@RequestMapping(value = "/insertinnformfield.html", method = RequestMethod.GET)
	public ModelAndView insertinnformfield(@ModelAttribute FormVO formVO,
			@RequestParam("innerfieldValue") String innerfieldValue, @RequestParam("innerfieldType") int innerfieldType,
			@RequestParam("innerfieldName") String innerfieldName, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		int formid = (int) session.getAttribute("formId");
		FieldVO fieldVO = new FieldVO();
		fieldVO.setFieldId(innerfieldType);

		formVO.setFormId(formid);
		System.out.println(formVO.getFormId());
		InnerFormVO innerFormVO = new InnerFormVO();
		innerFormVO.setFieldVO(fieldVO);
		innerFormVO.setFormVO(formVO);
		innerFormVO.setInnerfieldName(innerfieldName);
		innerFormVO.setInnerfieldValue(innerfieldValue);
		innerFormVO.setDelStatus("Active");
		innerformdao.insertinnformfield(innerFormVO);
		int projectId = (int) session.getAttribute("projectId");

		return new ModelAndView("redirect:manageProject.html", "projectId", projectId);
	}

	@RequestMapping(value = "/searchModuleAJAX.html", method = RequestMethod.GET)
	public String loadStateAJAX(Model model, @RequestParam("projectId") int projectId, HttpSession session) {
		System.out.println("aaaaaaaa");
		ProjectVO projectVO = new ProjectVO();
		projectVO.setProjectId(projectId);
		ModuleVO moduleVO = new ModuleVO();
		moduleVO.setProjectVO(projectVO);

		List moduleList = this.moduledao.searchModuleAJAX(moduleVO);
		session.setAttribute("moduleList", moduleList);
		System.out.println("bbbbbbbbb");
		return ("user/addModulejason");
	}

}