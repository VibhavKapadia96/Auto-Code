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

import com.spring.DAO.CountryDAO;
import com.spring.VO.CountryVO;

@Controller
public class CountryController {

	@Autowired
	CountryDAO countrydao;

	@RequestMapping(value = "/addCountry.html", method = RequestMethod.GET)
	public String loadCountry(Model model, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return ("redirect:Login.html");
		}

		model.addAttribute("country", new CountryVO());
		return ("admin/addCountry");
	}

	@RequestMapping(value = "/insertCountry.html", method = RequestMethod.GET)
	public ModelAndView insertCountry(@ModelAttribute CountryVO countryVO, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		this.countrydao.insertCountry(countryVO);
		return new ModelAndView("redirect:addCountry.html");
	}

	@RequestMapping(value = "/searchCountry.html", method = RequestMethod.GET)
	public ModelAndView searchCountry(@ModelAttribute CountryVO countryVO, HttpSession session) {
		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		List ls = countrydao.searchCountry();

		return new ModelAndView("admin/searchCountry", "ls", ls);
	}

	@RequestMapping(value = "/deleteCountry.html", method = RequestMethod.GET)
	public ModelAndView deleteCountry(@ModelAttribute CountryVO countryVO, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		this.countrydao.deleteCountry(countryVO);

		return new ModelAndView("redirect:searchCountry.html");
	}

	@RequestMapping(value = "/editCountry.html", method = RequestMethod.GET)
	public ModelAndView editCountry(@ModelAttribute CountryVO countryVO, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		List list = countrydao.editCountry(countryVO);
		return new ModelAndView("admin/updateCountry", "list", (CountryVO) list.get(0));
	}

	@RequestMapping(value = "/updateCountry.html", method = RequestMethod.GET)
	public ModelAndView saveUpdate(@ModelAttribute CountryVO countryVO, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		countrydao.updateCountry(countryVO);

		return new ModelAndView("redirect:/searchCountry.html");
	}
}
