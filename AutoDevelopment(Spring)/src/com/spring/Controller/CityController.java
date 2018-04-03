package com.spring.Controller;

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

import com.spring.DAO.CityDAO;
import com.spring.DAO.CountryDAO;
import com.spring.DAO.StateDAO;
import com.spring.VO.CityVO;
import com.spring.VO.CountryVO;
import com.spring.VO.StateVO;

@Controller
public class CityController {

	@Autowired
	CityDAO citydao;

	@Autowired
	StateDAO statedao;

	@Autowired
	CountryDAO countrydao;

	@RequestMapping(value = "/searchCityAJAX.html", method = RequestMethod.GET)
	public String loadStateAJAX(Model model, @RequestParam("stateId") int stateId, HttpSession session) {

		StateVO stateVO = new StateVO();
		stateVO.setStateId(stateId);
		CityVO cityVO = new CityVO();
		cityVO.setStateVO(stateVO);
		List cityList = this.citydao.searchCityAJAX(stateVO);
		session.setAttribute("cityList", cityList);
		return ("admin/addCityjason");
	}

	@RequestMapping(value = "/addCity.html", method = RequestMethod.GET)
	public String loadState(Model model, HttpSession session) {

		String email = (String) session.getAttribute("email");
		if (email == null) {

			return ("redirect:Login.html");
		}
		List countryList = this.countrydao.searchCountry();
		List stateList = this.statedao.searchState();
		model.addAttribute("city", new CityVO());

		session.setAttribute("countryList", countryList);
		session.setAttribute("stateList", stateList);
		return ("admin/addCity");
	}

	@RequestMapping(value = "/insertCity.html", method = RequestMethod.GET)
	public ModelAndView insertState(@ModelAttribute CityVO cityVO, @RequestParam("countryId") int countryId,
			@RequestParam("stateId") int stateId, HttpSession session) {

		String email = (String) session.getAttribute("email");
		if (email == null) {

			return new ModelAndView("redirect:Login.html");
		}

		CountryVO countryVO = new CountryVO();
		countryVO.setCountryId(countryId);
		StateVO stateVO = new StateVO();
		stateVO.setStateId(stateId);
		cityVO.setCountryVO(countryVO);
		cityVO.setStateVO(stateVO);
		this.citydao.insertCity(cityVO);
		return new ModelAndView("redirect:addCity.html");
	}

	@RequestMapping(value = "/searchCity.html", method = RequestMethod.GET)
	public ModelAndView searchState(@ModelAttribute CityVO cityVO, HttpSession session) {
		String email = (String) session.getAttribute("email");
		if (email == null) {

			return new ModelAndView("redirect:Login.html");
		}

		List ls = citydao.searchCity();
		return new ModelAndView("admin/searchCity", "ls", ls);

	}

	@RequestMapping(value = "/deleteCity.html", method = RequestMethod.GET)
	public ModelAndView deleteCountry(@ModelAttribute CityVO cityVO, HttpSession session) {

		String email = (String) session.getAttribute("email");
		if (email == null) {

			return new ModelAndView("redirect:Login.html");
		}

		this.citydao.deleteCity(cityVO);

		return new ModelAndView("redirect:searchCity.html");
	}

	@RequestMapping(value = "/editCity.html", method = RequestMethod.GET)
	public ModelAndView editCountry(@ModelAttribute CityVO cityVO, @RequestParam("cityId") int cityId,
			HttpSession session) {

		String email = (String) session.getAttribute("email");
		if (email == null) {

			return new ModelAndView("redirect:Login.html");
		}
		cityVO.setCityId(cityId);
		CountryVO countryVO = new CountryVO();
		cityVO.setCountryVO(countryVO);
		List countryList = this.countrydao.searchCountry();
		System.out.println("****CountryList****" + countryList.size());
		StateVO stateVO = new StateVO();
		cityVO.setStateVO(stateVO);
		List stateList = this.statedao.searchState();
		System.out.println("****StateList****" + stateList.size());
		List citylist = this.citydao.editCity(cityVO);

		System.out.println("****CityList****" + citylist.size());
		return new ModelAndView("admin/updateCity", "citylist", (CityVO) citylist.get(0))
				.addObject("countryList", countryList).addObject("stateList", stateList);
	}

	@RequestMapping(value = "/updateCity.html", method = RequestMethod.GET)
	public ModelAndView saveUpdate(@ModelAttribute CityVO cityVO, @RequestParam("cityId") int cityId,
			HttpSession session) {

		String email = (String) session.getAttribute("email");
		if (email == null) {

			return new ModelAndView("redirect:Login.html");
		}

		cityVO.setCityId(cityId);
		citydao.updateCity(cityVO);

		return new ModelAndView("redirect:searchCity.html");
	}

}
