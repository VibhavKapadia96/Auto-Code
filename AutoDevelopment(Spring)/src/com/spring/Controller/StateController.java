package com.spring.Controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spring.DAO.CountryDAO;
import com.spring.DAO.StateDAO;
import com.spring.VO.CountryVO;
import com.spring.VO.StateVO;

@Controller
public class StateController {

	@Autowired
	StateDAO statedao;

	@Autowired
	CountryDAO countrydao;

	@RequestMapping(value = "/searchStateAJAX.html", method = RequestMethod.GET)
	public String loadStateAJAX(Model model, @RequestParam("countryId") int countryId, HttpSession session) {

		CountryVO countryVO = new CountryVO();
		countryVO.setCountryId(countryId);
		StateVO stateVO = new StateVO();
		stateVO.setCountryVO(countryVO);
		List stateList = this.statedao.searchStateAJAX(countryVO);
		session.setAttribute("stateList", stateList);
		return ("admin/addStatejason");
	}

	@RequestMapping(value = "/addState.html", method = RequestMethod.GET)
	public String loadState(Model model, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return ("redirect:Login.html");
		}

		List countryList = this.countrydao.searchCountry();
		model.addAttribute("state", new StateVO());

		session.setAttribute("countryList", countryList);
		return ("admin/addState");
	}

	@RequestMapping(value = "/insertState.html", method = RequestMethod.GET)
	public ModelAndView insertState(@ModelAttribute StateVO stateVO, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		this.statedao.insertState(stateVO);
		return new ModelAndView("redirect:addState.html");
	}

	@RequestMapping(value = "/searchState.html", method = RequestMethod.GET)
	public ModelAndView searchState(@ModelAttribute StateVO stateVO, HttpSession session) {
		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		List ls = statedao.searchState();
		return new ModelAndView("admin/searchState", "ls", ls);

	}

	@RequestMapping(value = "/deleteState.html", method = RequestMethod.GET)
	public ModelAndView deleteCountry(@ModelAttribute StateVO stateVO, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		this.statedao.deleteState(stateVO);

		return new ModelAndView("redirect:searchState.html");
	}

	@RequestMapping(value = "/editState.html", method = RequestMethod.GET)
	public ModelAndView editCountry(@ModelAttribute StateVO stateVO, @RequestParam("stateId") int stateId,
			HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		stateVO.setStateId(stateId);
		CountryVO countryVO = new CountryVO();
		stateVO.setCountryVO(countryVO);
		List countryList = this.countrydao.searchCountry();
		System.out.println("****CountryList****" + countryList.size());
		List statelist = this.statedao.editState(stateVO);
		System.out.println("****StateList****" + statelist.size());
		return new ModelAndView("admin/updateState", "statelist", (StateVO) statelist.get(0)).addObject("countryList",
				countryList);
	}

	@RequestMapping(value = "/updateState.html", method = RequestMethod.GET)
	public ModelAndView saveUpdate(@ModelAttribute StateVO stateVO, @RequestParam("stateId") int stateId,
			HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		stateVO.setStateId(stateId);
		statedao.updateState(stateVO);

		return new ModelAndView("redirect:/searchState.html");
	}
}
