package com.spring.Controller;

import java.util.ArrayList;
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
import com.spring.DAO.InnerFormDAO;
import com.spring.VO.FieldVO;
import com.spring.VO.FormVO;
import com.spring.VO.InnerFormVO;

import sun.print.resources.serviceui;

@Controller
public class InnerFormController {

	@Autowired
	InnerFormDAO innerformdao;

	@Autowired
	FieldDAO fielddao;

	List TempValueList = new ArrayList();

	@RequestMapping(value = "/insertvalue.html", method = RequestMethod.GET)
	public ModelAndView insertinnerform(@RequestParam("value") String value, HttpServletRequest request,
			HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}
		System.out.println("value.html");

		TempValueList = (ArrayList) request.getSession().getAttribute("TempValueList");
		if (TempValueList == null) {
			this.TempValueList = new ArrayList();
		}

		this.TempValueList.add(value);

		request.getSession().setAttribute("TempValueList", TempValueList);

		System.out.println(TempValueList.size());

		return new ModelAndView("admin/freeJason");
	}

	@RequestMapping(value = "/editinnerform.html", method = RequestMethod.GET)
	public ModelAndView editinnerform(@ModelAttribute InnerFormVO innerFormVO,
			@RequestParam("innerFormId") int innerFormId, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		innerFormVO.setInnerFormId(innerFormId);

		List innerformlist = this.innerformdao.editinnerForm(innerFormVO);
		List fieldList = this.fielddao.searchField();
		System.out.println("listsize>>>>>>>>" + innerformlist.size());
		return new ModelAndView("user/updateInnerForm", "innerformlist", (InnerFormVO) innerformlist.get(0))
				.addObject("fieldList", fieldList);
	}

	@RequestMapping(value = "/updateinnerForm.html", method = RequestMethod.GET)
	public ModelAndView updateinnerform(@ModelAttribute InnerFormVO innerFormVO, HttpSession session,
			@RequestParam("innerFormId") int innerFormId, @RequestParam("formVO.formId") int formId) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}
		FormVO formVO = new FormVO();
		formVO.setFormId(formId);
		innerFormVO.setFormVO(formVO);
		innerFormVO.setInnerFormId(innerFormId);

		innerformdao.updateinnerform(innerFormVO);
		int projectId = (int) session.getAttribute("projectId");
		session.setAttribute("fadeinfadeoutupdateinnerformcount", 1);
		return new ModelAndView("redirect:manageProject.html", "projectId", projectId);

	}

	@RequestMapping(value = "/deleteinnerform.html", method = RequestMethod.GET)
	public ModelAndView deleteField(@ModelAttribute InnerFormVO innerFormVO,
			@RequestParam("innerFormId") int innerFormId, HttpSession session) {
		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}
		innerFormVO.setInnerFormId(innerFormId);
		this.innerformdao.deleteinnerForm(innerFormVO);
		int projectId = (int) session.getAttribute("projectId");
		session.setAttribute("fadeinfadeoutdeleteinnerformcount", 1);
		return new ModelAndView("redirect:manageProject.html", "projectId", projectId);
	}

}
