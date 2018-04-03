package com.spring.Controller;

import javax.mail.Authenticator;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.*;
import javax.activation.*;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.apache.catalina.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.spring.DAO.RegDAO;
import com.spring.DAO.CityDAO;
import com.spring.DAO.CountryDAO;
import com.spring.DAO.StateDAO;
import com.spring.DAO.loginDAO;
import com.spring.VO.CityVO;
import com.spring.VO.CountryVO;
import com.spring.VO.LoginVO;
import com.spring.VO.RegVO;
import com.spring.VO.StateVO;
import com.spring.VO.LoginVO;

@Controller
public class RegController {

	@Autowired
	RegDAO regdao;

	@Autowired
	CountryDAO countrydao;

	@Autowired
	StateDAO statedao;

	@Autowired
	CityDAO citydao;

	@Autowired
	loginDAO logindao;

	@RequestMapping(value = "/searchnotEmailAJAX.html", method = RequestMethod.GET)
	public String searchnotEmailAJAX(Model model, @RequestParam("email") String email, HttpSession session) {

		RegVO regVO = new RegVO();
		regVO.setEmail(email);

		List regList = regdao.searchEmailAJAX(regVO);
		System.out.println(regList.size());
		if (regList.size() == 0) {

			return ("user/freeJason");

		} else {
			return null;
		}

	}

	@RequestMapping(value = "/searchEmailAJAX.html", method = RequestMethod.GET)
	public String searchEmailAJAX(Model model, @RequestParam("email") String email, HttpSession session) {

		RegVO regVO = new RegVO();
		regVO.setEmail(email);

		List regList = regdao.searchEmailAJAX(regVO);
		System.out.println(regList.size());
		if (regList.size() > 0) {

			return ("user/freeJason");
		} else {
			return null;
		}

	}

	@RequestMapping(value = "/updatepassword.html", method = RequestMethod.POST)
	public String updatepassword(Model model, HttpSession session, @RequestParam("newpassword") String password) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return ("redirect:Login.html");
		}

		String email = BaseMethods.getCurrentUser().getUsername();
		RegVO regVO = new RegVO();
		LoginVO loginVO = new LoginVO();

		regVO.setEmail(email);
		regVO.setPassword(password);
		loginVO.setEmail(email);
		loginVO.setPassword(password);

		regdao.updatePassword(regVO, loginVO);

		return ("user/index");
	}

	@RequestMapping(value = "/forgot_password.html", method = RequestMethod.GET)
	public String forgot_password(Model model, HttpSession session) {

		return ("user/forgotpassword");
	}

	@RequestMapping(value = "/sendpassword.html", method = RequestMethod.GET)
	public String sendpassword(Model model, HttpSession httpSession, @RequestParam("email") String email) {

		RegVO regVO = new RegVO();
		regVO.setEmail(email);

		String password = regdao.searchpassword(regVO);

		String to = regVO.getEmail();// change accordingly String from =
		String from = "lucky.vibs96@gmail.com";// change accordingly String
												// password =
		String pass = "30011996";// change accordingly

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, new javax.mail.

				Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, pass);
			}
		});
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			message.setText("Your current password is: " + password);

			Transport.send(message);
			System.out.println("message sent successfully");
		} catch (

		MessagingException e) {
			throw new RuntimeException(e);
		}

		return ("admin/Login");
	}

	@RequestMapping(value = "/changepassword.html", method = RequestMethod.GET)
	public String Changepassword(Model model, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return ("redirect:Login.html");
		}

		return ("user/changePassword");
	}

	@RequestMapping(value = "/searchPasswordAJAX.html", method = RequestMethod.GET)
	public String searchPasswordAJAX(Model model, @RequestParam("password") String password, HttpSession session) {

		String email = BaseMethods.getCurrentUser().getUsername();
		System.out.println(email);
		LoginVO loginVO = new LoginVO();
		loginVO.setEmail(email);
		int id = logindao.searchLogin(loginVO);
		System.out.println(id);

		String passlist = logindao.searchPasswordAJAX(loginVO);
		System.out.println(passlist);
		if (passlist.equals(password)) {
			return null;
		} else {

			System.out.println(password);
			return ("admin/freeJason");

		}

	}

	@RequestMapping(value = "/reg.html", method = RequestMethod.GET)
	public String loadreg(Model model, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin != null) {
			session.removeAttribute(emailLogin);
			return ("redirect:Login.html");
		}
		/*
		 * List countryList = this.countrydao.searchCountry();
		 */
		/*
		 * List stateList = this.statedao.searchState();
		 * 
		 * List cityList = this.citydao.searchCity();
		 *//*
			 * model.addAttribute("reg", new RegVO());
			 * 
			 * session.setAttribute("countryList", countryList);
			 */
		/*
		 * session.setAttribute("stateList", stateList);
		 * session.setAttribute("cityList", cityList);
		 */

		return ("admin/Reg");
	}

	@RequestMapping(value = "/insertReg.html", method = RequestMethod.GET)
	public ModelAndView insertUsers(@ModelAttribute RegVO regVO, @RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName, @RequestParam("email") String email,
			@RequestParam("phone") String phone, HttpSession httpSession) {

		File fproject = new File("C:\\Users\\lucky\\Desktop\\UserProjects");
		Path p = Paths.get(fproject.getAbsolutePath() + "\\" + regVO.getFirstName() + "_" + regVO.getLastName());
		File f1 = new File(p.toString());
		f1.mkdirs();

		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+=[]{}/|?><,.abcdefghijklmnopqrstuvwxyz~";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 16) {
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String password = salt.toString();
		System.out.println(password);

		LoginVO loginVO = new LoginVO();
		loginVO.setEmail(regVO.getEmail());
		loginVO.setPassword(password);
		loginVO.setEnabled("1");
		loginVO.setRole("ROLE_USER");
		int loginId = this.logindao.insertLogin(loginVO);
		loginVO.setLoginId(loginId);

		regVO.setLoginVO(loginVO);
		regVO.setPassword(password);
		regVO.setEmail(email);
		regVO.setFirstName(firstName);
		regVO.setLastName(lastName);
		regVO.setMobileNO(phone);

		this.regdao.insertReg(regVO);

		String to = regVO.getEmail();// change accordingly String from =
		String from = "lucky.vibs96@gmail.com";// change accordingly String
												// password =
		String pass = "30011996";// change accordingly

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, new javax.mail.

				Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, pass);
			}
		});
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("Hello thank you for registering.");
			message.setText("Your password is: " + password);

			Transport.send(message);
			System.out.println("message sent successfully");
		} catch (

		MessagingException e) {
			throw new RuntimeException(e);
		}
		return new ModelAndView("redirect:Login.html");

	}

	@RequestMapping(value = "/searchReg.html", method = RequestMethod.GET)
	public ModelAndView manageUsers(@ModelAttribute RegVO regVO, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		List regList = regdao.searchReg(regVO);

		return new ModelAndView("admin/searchReg", "regList", regList);
	}

	@RequestMapping(value = "/editReg.html", method = RequestMethod.POST)
	public ModelAndView editUsers(@ModelAttribute RegVO regVO, @RequestParam("regId") int regId, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}
		LoginVO loginVO = new LoginVO();
		loginVO.setLoginId(regId);
		regVO.setLoginVO(loginVO);

		CountryVO countryVO = new CountryVO();
		regVO.setCountryVO(countryVO);
		List countryList = this.countrydao.searchCountry();
		System.out.println("****CountryList****" + countryList.size());
		StateVO stateVO = new StateVO();
		regVO.setStateVO(stateVO);
		List stateList = this.statedao.searchState();
		System.out.println("****StateList****" + stateList.size());
		CityVO cityVO = new CityVO();
		regVO.setCityVO(cityVO);
		List cityList = this.citydao.searchCity();
		System.out.println("****cityList****" + cityList.size());

		List regList = regdao.editReg(regVO);

		return new ModelAndView("user/accountsetting", "regList", (RegVO) regList.get(0))
				.addObject("countryList", countryList).addObject("stateList", stateList)
				.addObject("cityList", cityList);
	}

	@RequestMapping(value = "/updateReg.html", method = RequestMethod.GET)
	public ModelAndView saveUpdate(@ModelAttribute RegVO regVO, @RequestParam("regId") int regId, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}
		regVO.setRegId(regId);
		regdao.updateReg(regVO);

		return new ModelAndView("redirect:user.html");
	}

	@RequestMapping(value = "/deleteReg.html", method = RequestMethod.GET)
	public ModelAndView deleteCountry(@ModelAttribute RegVO regVO, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		session.setAttribute("fadeinfadeoutdeletefieldcount", 1);
		this.regdao.deleteReg(regVO);

		return new ModelAndView("redirect:searchReg.html");
	}

	@RequestMapping(value = "/activeReg.html", method = RequestMethod.GET)
	public ModelAndView activeReg(@ModelAttribute RegVO regVO, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");

		}
		this.regdao.activeReg(regVO);
		session.setAttribute("fadeinfadeoutupdatefieldcount", 1);
		return new ModelAndView("redirect:searchReg.html");
	}

}