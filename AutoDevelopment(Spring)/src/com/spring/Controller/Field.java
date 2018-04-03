package com.spring.Controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.spring.DAO.FieldDAO;
import com.spring.VO.FieldVO;

@Controller
public class Field {

	@Autowired
	FieldDAO fielddao;

	@RequestMapping(value = "/addField.html", method = RequestMethod.GET)
	public String loadField(Model model, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return ("redirect:Login.html");
		}

		model.addAttribute("field", new FieldVO());
		return ("admin/addField");
	}

	@RequestMapping(value = "/insertField.html", method = RequestMethod.GET)
	public ModelAndView insertField(@ModelAttribute FieldVO fieldVO, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		session.setAttribute("fadeinfadeoutaddfieldcount", 1);

		this.fielddao.insertField(fieldVO);
		return new ModelAndView("redirect:addField.html");
	}

	@RequestMapping(value = "/searchField.html", method = RequestMethod.GET)
	public ModelAndView searchField(@ModelAttribute FieldVO fieldVO, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}
		List ls = fielddao.searchField();

		return new ModelAndView("admin/searchField", "ls", ls);
	}

	@RequestMapping(value = "/deleteField.html", method = RequestMethod.GET)
	public ModelAndView deleteField(@ModelAttribute FieldVO fieldVO, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		this.fielddao.deleteField(fieldVO);
		session.setAttribute("fadeinfadeoutdeletefieldcount", 1);

		return new ModelAndView("redirect:searchField.html");
	}

	@RequestMapping(value = "/editField.html", method = RequestMethod.GET)
	public ModelAndView editField(@ModelAttribute FieldVO fieldVO, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		List list = fielddao.editField(fieldVO);
		return new ModelAndView("admin/updateField", "fieldlist", (FieldVO) list.get(0));
	}

	@RequestMapping(value = "/updateField.html", method = RequestMethod.GET)
	public ModelAndView saveUpdate(@ModelAttribute FieldVO fieldVO, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		fielddao.updateField(fieldVO);
		session.setAttribute("fadeinfadeoutupdatefieldcount", 1);

		return new ModelAndView("redirect:/searchField.html");
	}
}
