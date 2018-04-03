package com.spring.Controller;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.spring.DAO.ComplaintDAO;
import com.spring.DAO.loginDAO;
import com.spring.VO.AttachmentVO;
import com.spring.VO.ComplaintVO;
import com.spring.VO.LoginVO;

@Controller
public class ComplaintController {

	@Autowired
	ComplaintDAO complaintdao;

	@Autowired
	loginDAO loginDAO;

	@RequestMapping(value = "/addComplaint.html", method = RequestMethod.GET)
	public String loadCountry(Model model, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return ("redirect:Login.html");
		}

		model.addAttribute("complaint", new ComplaintVO());
		return ("user/Complaint");
	}

	@RequestMapping(value = "/openfile.html", method = RequestMethod.GET)
	public String fileopen(Model model, HttpSession session, @RequestParam("filepath") String filepath) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return ("redirect:Login.html");
		}

		File file = new File(filepath);

		try {

			// Open the file using Desktop class

			Desktop.getDesktop().open(file);

		} catch (IOException exception) {

			exception.printStackTrace();

		}
		return null;

	}

	@RequestMapping(value = "/insertComplaint.html", method = RequestMethod.POST)
	public ModelAndView insertComplaint(@ModelAttribute ComplaintVO complaintVO,
			@RequestParam CommonsMultipartFile file, HttpSession session) throws IOException {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		LoginVO complaintFrom = new LoginVO();

		String email = BaseMethods.getCurrentUser().getUsername();
		System.out.println(email);
		complaintFrom.setEmail(email);

		int id = loginDAO.searchLogin(complaintFrom);
		System.out.println(id);
		complaintFrom.setLoginId(id);

		List ls = loginDAO.searchLoginadmin();

		LoginVO complainTo = (LoginVO) ls.get(0);

		AttachmentVO attachmentVO = new AttachmentVO();
		String path = session.getServletContext().getRealPath("/");
		String filename = file.getOriginalFilename();

		if (!filename.isEmpty()) {

			System.out.println(path + " " + filename);

			byte barr[] = file.getBytes();

			BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(path + "/doc/" + filename));
			bout.write(barr);
			bout.flush();
			bout.close();

			attachmentVO.setFileName(filename);
			attachmentVO.setFilePath(path);
			attachmentVO.setComplaintVO(complaintVO);
		}

		Date presentdate = new Date();
		Time presenttime = new Time(presentdate.getTime());
		complaintVO.setPresentdate(presentdate);
		complaintVO.setPresenttime(presenttime);
		complaintVO.setComplaintTo(complainTo);
		complaintVO.setComplaintFrom(complaintFrom);

		this.complaintdao.insertComaplaint(complaintVO);
		session.setAttribute("fadeinfadeoutprojectcount", 1);
		if (!filename.isEmpty()) {

			this.complaintdao.insertAttachmnet(attachmentVO);
		}
		return new ModelAndView("redirect:addComplaint.html");
	}

	@RequestMapping(value = "/searchComplaint.html", method = RequestMethod.GET)
	public ModelAndView searchComplaint(@ModelAttribute ComplaintVO complaintVO, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		List complaintList = complaintdao.searchComplaint();
		return new ModelAndView("admin/searchComplaint", "complaintList", complaintList);
	}

	@RequestMapping(value = "/viewComplaint.html", method = RequestMethod.GET)
	public ModelAndView viewComplaint(@ModelAttribute ComplaintVO complaintVO, HttpSession session) {
		LoginVO loginVO = new LoginVO();

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		loginVO.setEmail(emailLogin);

		int id = loginDAO.searchLogin(loginVO);

		List complaintList = complaintdao.searchviewComplaint(id);
		return new ModelAndView("user/searchComplaint", "complaintList", complaintList);
	}

	@RequestMapping(value = "/replayComplaint.html", method = RequestMethod.GET)
	public ModelAndView replayComplaint(@ModelAttribute ComplaintVO complaintVO, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}
		String attachmentPath = complaintdao.searchAttachmnet(complaintVO);

		session.setAttribute("attachmentPath", attachmentPath);

		List complaintList = complaintdao.editComplaint(complaintVO);
		
		return new ModelAndView("admin/replayComplaint", "complaintList", (ComplaintVO) complaintList.get(0));
	}

	@RequestMapping(value = "/updateComplaint.html", method = RequestMethod.POST)
	public ModelAndView updateComplaint(@ModelAttribute ComplaintVO complaintVO, HttpSession session,
			@RequestParam CommonsMultipartFile file) throws IOException {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return new ModelAndView("redirect:Login.html");
		}

		LoginVO complaintFrom = new LoginVO();

		String email = BaseMethods.getCurrentUser().getUsername();

		complaintFrom.setEmail(email);

		int To_id = loginDAO.searchLogin(complaintFrom);
		System.out.println("To id  " + To_id);

		int from_Id = complaintdao.searchComplaintById(complaintVO);
		System.out.println("From id" + from_Id);

		String path = session.getServletContext().getRealPath("/");

		String filename = file.getOriginalFilename();

		if (!filename.isEmpty()) {

			System.out.println(path + " " + filename);

			byte barr[] = file.getBytes();

			BufferedOutputStream bout = new BufferedOutputStream(
					new FileOutputStream(path + "/doc/admindoc/" + filename));
			bout.write(barr);
			bout.flush();
			bout.close();

		}

		String to = "lucky.vibs@gmail.com";

		// Sender's email ID needs to be mentioned
		String from = "lucky.vibs96@gmail.com";

		final String password = "30011996";// change accordingly

		// Assuming you are sending email through relay.jangosmtp.net

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		// Get the Session object.
		Session session1 = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(from, password);
			}
		});

		try {
			// Create a default MimeMessage object.
			Message message = new MimeMessage(session1);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

			// Set Subject: header field
			message.setSubject("Testing Subject");

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Now set the actual message
			messageBodyPart.setText("This is message body");

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
			filename = path + "/doc/admindoc/" + filename;
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);
			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			message.setContent(multipart);

			// Send message
			Transport.send(message);

			System.out.println("Sent message successfully....");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		complaintdao.updateComplaint(complaintVO, To_id, from_Id);
		session.setAttribute("fadeinfadeoutupdatefieldcount", 1);
		return new ModelAndView("redirect:searchComplaint.html");
	}
}
