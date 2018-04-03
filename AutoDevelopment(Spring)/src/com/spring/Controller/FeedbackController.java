package com.spring.Controller;

import java.sql.Time;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.spring.DAO.FeedbackDAO;
import com.spring.VO.ComplaintVO;
import com.spring.VO.FeedbackVO;

@Controller
public class FeedbackController {

	@Autowired
	FeedbackDAO feedbackdao;

	@RequestMapping(value = "/addFeedback.html", method = RequestMethod.GET)
	public String loadFeedback(Model model, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return ("redirect:Login.html");
		}
		model.addAttribute("feedback", new FeedbackVO());
		return ("user/Feedback");
	}

	@RequestMapping(value = "/insertFeedback.html", method = RequestMethod.GET)
	public ModelAndView insertFeedback(@ModelAttribute FeedbackVO feedbackVO, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		Date presentfeedbackdate = new Date();
		Time presentfeedbacktime = new Time(presentfeedbackdate.getTime());
		feedbackVO.setPresentfeedbackdate(presentfeedbackdate);
		feedbackVO.setPresentfeedbacktime(presentfeedbacktime);
		this.feedbackdao.insertFeedback(feedbackVO);
		session.setAttribute("fadeinfadeoutprojectcount", 1);
		return new ModelAndView("redirect:addFeedback.html");
	}

	@RequestMapping(value = "/searchFeedback.html", method = RequestMethod.GET)
	public ModelAndView searchFeedback(@ModelAttribute FeedbackVO feedbackVO, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		List feedbackList = feedbackdao.searchFeedback();
		return new ModelAndView("admin/searchFeedback", "feedbackList", feedbackList);
	}

}