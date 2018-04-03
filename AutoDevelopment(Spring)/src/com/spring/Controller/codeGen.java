package com.spring.Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.spring.DAO.FormDAO;
import com.spring.DAO.InnerFormDAO;
import com.spring.DAO.ModuleDAO;
import com.spring.DAO.ProjectDAO;
import com.spring.DAO.RegDAO;
import com.spring.DAO.loginDAO;
import com.spring.VO.FieldVO;
import com.spring.VO.FormVO;
import com.spring.VO.InnerFormVO;
import com.spring.VO.LoginVO;
import com.spring.VO.ModuleVO;
import com.spring.VO.ProjectVO;
import com.spring.VO.RegVO;

@Controller
public class codeGen {
	private String SOURCE_FOLDER = null;

	private String OUTPUT_ZIP_FILE = null;

	@Autowired
	ProjectDAO projectdao;

	@Autowired
	ModuleDAO moduledao;

	@Autowired
	FormDAO formdao;

	@Autowired
	RegDAO regdao;

	@Autowired
	InnerFormDAO innerformdao;

	@Autowired
	loginDAO logindao;

	List<String> fileList = new ArrayList<String>();

	public void zipIt(String zipFile) {
		byte[] buffer = new byte[1024];
		String source = "";
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			try {
				source = SOURCE_FOLDER.substring(SOURCE_FOLDER.lastIndexOf("\\") + 1, SOURCE_FOLDER.length());
			} catch (Exception e) {
				source = SOURCE_FOLDER;
			}
			fos = new FileOutputStream(zipFile);
			zos = new ZipOutputStream(fos);

			System.out.println("Output to Zip : " + zipFile);
			FileInputStream in = null;

			for (String file : this.fileList) {
				System.out.println("File Added : " + file);
				ZipEntry ze = new ZipEntry(source + File.separator + file);
				zos.putNextEntry(ze);
				try {
					in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
					int len;
					while ((len = in.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}
				} finally {
					in.close();
				}
			}

			zos.closeEntry();
			System.out.println("Folder successfully compressed");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void generateFileList(File node) {

		// add file only
		if (node.isFile()) {
			fileList.add(generateZipEntry(node.toString()));

		}

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename));
			}
		}
	}

	private String generateZipEntry(String file) {
		return file.substring(SOURCE_FOLDER.length() + 1, file.length());
	}

	@RequestMapping(value = "/downloadProject.html", method = RequestMethod.GET)
	public String downloadProject(Model model, HttpSession session, @RequestParam("projectId") int projectId) {

		ProjectVO projectVO = new ProjectVO();
		String emailLogin = (String) session.getAttribute("email");
		String name = null;
		RegVO regVO = new RegVO();
		if (emailLogin == null) {

			return ("redirect:Login.html");
		}
		if (emailLogin != null) {

			regVO.setEmail(emailLogin);
			name = regdao.searchfnln(regVO);
			System.out.println(name);

		}

		String projectName = this.projectdao.searchProjectbyId(projectId);
		System.out.println(projectName);
		projectVO.setProjectName(projectName);

		/* ZIP file creation */
		OUTPUT_ZIP_FILE = "C:\\Users\\lucky\\Downloads" + "\\" + projectVO.getProjectName() + ".zip";
		SOURCE_FOLDER = "C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\" + projectVO.getProjectName();

		generateFileList(new File(SOURCE_FOLDER));
		zipIt(OUTPUT_ZIP_FILE);
		System.out.println("DONE Ziiping and download");
		/* ZIP file creation */
		session.setAttribute("fadeinfadeoutcodedownloadcount", 1);

		return ("redirect:codeGen.html");
	}

	@RequestMapping(value = "/codeGen.html", method = RequestMethod.GET)
	public String Gen(Model model, HttpSession session) {

		String emailLogin = (String) session.getAttribute("email");
		if (emailLogin == null) {

			return ("redirect:Login.html");
		}

		LoginVO loginVO = new LoginVO();

		String email = BaseMethods.getCurrentUser().getUsername();
		System.out.println(email);
		loginVO.setEmail(email);

		int loginid = logindao.searchLogin(loginVO);

		fileList.clear();
		List projectList = this.projectdao.searchProject(loginid);

		model.addAttribute("project", new ProjectVO());

		session.setAttribute("projectList", projectList);
		return ("user/codeGen");
	}

	@RequestMapping(value = "/GencodeGen.html", method = RequestMethod.GET)
	public String codeGen(Model model, HttpSession session, ProjectVO projectVO) {

		String emailLogin = (String) session.getAttribute("email");
		String name = null;
		RegVO regVO = new RegVO();
		if (emailLogin == null) {

			return ("redirect:Login.html");
		}
		if (emailLogin != null) {

			regVO.setEmail(emailLogin);
			name = regdao.searchfnln(regVO);
			System.out.println(name);

		}

		int id = projectVO.getProjectId();
		projectVO.setDownloadStatus("YES");
		System.out.println(id);
		projectVO.setProjectId(id);
		projectdao.setDownloadyes(projectVO);

		/* Project Folder Creation */

		List projectList = this.projectdao.genProject(projectVO);
		String proList = (String) projectList.get(0);
		projectVO.setProjectName(proList);
		File fproject = new File("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name);
		Path p = Paths.get(fproject.getAbsolutePath() + "\\" + projectVO.getProjectName());
		File f1 = new File(p.toString());
		f1.mkdirs();
		File f2 = new File(p.toString() + "\\" + "src");

		File f3 = new File(p.toString() + "\\" + "WebContent");
		File f4 = new File(p.toString() + "\\" + "src" + "\\" + "VO");
		File f5 = new File(p.toString() + "\\" + "src" + "\\" + "DAO");
		File f6 = new File(p.toString() + "\\" + "src" + "\\" + "Controller");

		f2.mkdirs();
		f3.mkdirs();
		f4.mkdirs();
		f5.mkdirs();
		f6.mkdirs();

		/* Project Folder Creation END */

		/* Hibernet.cfg.xml file creation */

		File f7 = new File("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\" + projectVO.getProjectName()
				+ "\\" + "src" + "\\" + "hibernate.cfg.xml");
		try {
			f7.createNewFile();
		} catch (IOException e) {

			e.printStackTrace();
		}

		FileInputStream instream = null;
		FileOutputStream outstream = null;

		try {
			File infile = new File("C:\\Users\\lucky\\Desktop\\ProjectName\\hcfg.txt");
			File outfile = new File("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
					+ projectVO.getProjectName() + "\\" + "src" + "\\" + "hibernate.cfg.xml");

			instream = new FileInputStream(infile);
			outstream = new FileOutputStream(outfile);

			byte[] buffer = new byte[1024];

			int length;

			while ((length = instream.read(buffer)) > 0) {
				outstream.write(buffer, 0, length);
			}

			instream.close();
			outstream.close();

			System.out.println("File copied successfully!!");

		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

		/* Hibernet.cfg.xml file creation END */

		/* Module Folder Creation */

		ModuleVO moduleVO1 = new ModuleVO();
		FormVO formVO1 = new FormVO();
		moduleVO1.setProjectVO(projectVO);
		formVO1.setProjectVO(projectVO);
		List moduleList = this.moduledao.genModule(moduleVO1);

		/* Module List iterator */

		for (Iterator iterator = moduleList.iterator(); iterator.hasNext();) {

			ModuleVO moduleVO = (ModuleVO) iterator.next();

			File fmodule = new File("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name);
			Path pmodule = Paths.get(fmodule.getAbsolutePath() + "\\" + projectVO.getProjectName() + "\\" + "WebContent"
					+ "\\" + moduleVO.getModuleName());
			File f1module = new File(pmodule.toString());
			f1module.mkdirs();

			File indexjsp = new File("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name);
			Path indexpjsp = Paths.get(indexjsp.getAbsolutePath() + "\\" + projectVO.getProjectName() + "\\"
					+ "WebContent" + "\\" + moduleVO.getModuleName() + "\\" + "index" + ".jsp");
			File index1jsp = new File(indexpjsp.toString());

			try {
				index1jsp.createNewFile();
			} catch (IOException e) {

				e.printStackTrace();
			}
			try {
				File infile = new File("C:\\Users\\lucky\\Desktop\\ProjectName\\JSP.txt");
				File outfile = new File(
						"C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\" + projectVO.getProjectName()
								+ "\\" + "WebContent" + "\\" + moduleVO.getModuleName() + "\\" + "index" + ".jsp");

				instream = new FileInputStream(infile);
				outstream = new FileOutputStream(outfile);

				byte[] buffer = new byte[1024];

				int length;

				while ((length = instream.read(buffer)) > 0) {
					outstream.write(buffer, 0, length);
				}

				instream.close();
				outstream.close();

				System.out.println(" .JSP File copied successfully!!");

			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

			formVO1.setModuleVO(moduleVO);

			List formList = this.formdao.genForm(formVO1);

			/* Form Iterator */

			for (Iterator itr = formList.iterator(); itr.hasNext();) {

				FormVO formVO = (FormVO) itr.next();
				int i = 0;
				int j = 0;

				/* JSP file creation */

				String contentindexjsp = "\r\n" + "<table>" + "\r\n" + "<tr>" + "\r\n" + "<td>" + formVO.getFormName()
						+ ".jsp</td>" + "\r\n" + "<td><a href=" + '"' + formVO.getFormName() + ".jsp" + '"'
						+ ">Insert</a></td>" + "\r\n" + "<td><a href=" + '"' + "<%=request.getContextPath()%>/"
						+ formVO.getFormName() + "?flag=search" + '"' + ">Search</a></td>" + "\r\n" + "</tr>";
				try {
					Files.write(
							Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
									+ projectVO.getProjectName() + "\\" + "WebContent" + "\\" + moduleVO.getModuleName()
									+ "\\" + "index" + ".jsp"),
							contentindexjsp.getBytes(), StandardOpenOption.APPEND);

					System.out.println("DONE WITH FORM TAG");
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}

				File fjsp = new File("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name);
				Path pjsp = Paths.get(fjsp.getAbsolutePath() + "\\" + projectVO.getProjectName() + "\\" + "WebContent"
						+ "\\" + moduleVO.getModuleName() + "\\" + formVO.getFormName() + ".jsp");
				File f1jsp = new File(pjsp.toString());

				try {
					f1jsp.createNewFile();
				} catch (IOException e) {

					e.printStackTrace();
				}

				try {
					File infile = new File("C:\\Users\\lucky\\Desktop\\ProjectName\\JSP.txt");
					File outfile = new File("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
							+ projectVO.getProjectName() + "\\" + "WebContent" + "\\" + moduleVO.getModuleName() + "\\"
							+ formVO.getFormName() + ".jsp");

					instream = new FileInputStream(infile);
					outstream = new FileOutputStream(outfile);

					byte[] buffer = new byte[1024];

					int length;

					while ((length = instream.read(buffer)) > 0) {
						outstream.write(buffer, 0, length);
					}

					instream.close();
					outstream.close();

					System.out.println(" .JSP File copied successfully!!");

				} catch (IOException ioe) {
					ioe.printStackTrace();
				}

				String contentjsp = "\r\n" + "<form action=" + '"' + "<%=request.getContextPath()%>/"
						+ formVO.getFormName() + '"' + " " + "method=" + '"' + "get" + '"' + ">";

				try {
					Files.write(
							Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
									+ projectVO.getProjectName() + "\\" + "WebContent" + "\\" + moduleVO.getModuleName()
									+ "\\" + formVO.getFormName() + ".jsp"),
							contentjsp.getBytes(), StandardOpenOption.APPEND);

					System.out.println("DONE WITH FORM TAG");
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}
				String inputhidden = "\r\n" + "<input type='hidden' name='flag' value='insert'>";

				try {
					Files.write(
							Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
									+ projectVO.getProjectName() + "\\" + "WebContent" + "\\" + moduleVO.getModuleName()
									+ "\\" + formVO.getFormName() + ".jsp"),
							inputhidden.getBytes(), StandardOpenOption.APPEND);

					System.out.println("DONE");
				} catch (IOException e) {
					// exception handling left as an exercise for the
					// reader
				}

				File fsearchjsp = new File("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name);
				Path psearchjsp = Paths.get(fsearchjsp.getAbsolutePath() + "\\" + projectVO.getProjectName() + "\\"
						+ "WebContent" + "\\" + moduleVO.getModuleName() + "\\" + formVO.getFormName() + "search.jsp");
				File f1searchjsp = new File(psearchjsp.toString());

				try {
					f1searchjsp.createNewFile();
				} catch (IOException e) {

					e.printStackTrace();
				}

				try {
					File infile = new File("C:\\Users\\lucky\\Desktop\\ProjectName\\searchJSP.txt");
					File outfile = new File("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
							+ projectVO.getProjectName() + "\\" + "WebContent" + "\\" + moduleVO.getModuleName() + "\\"
							+ formVO.getFormName() + "search.jsp");

					instream = new FileInputStream(infile);
					outstream = new FileOutputStream(outfile);

					byte[] buffer = new byte[1024];

					int length;

					while ((length = instream.read(buffer)) > 0) {
						outstream.write(buffer, 0, length);
					}

					instream.close();
					outstream.close();

					System.out.println(" .JSP File copied successfully!!");

				} catch (IOException ioe) {
					ioe.printStackTrace();
				}

				String contentsearchjsp = "<table border=1>" + "\r\n" + "<c:forEach items=" + '"' + "${sessionScope.ls}"
						+ '"' + " var=" + '"' + "z" + '"' + ">" + "\r\n" + "<tr>";

				try {
					Files.write(
							Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
									+ projectVO.getProjectName() + "\\" + "WebContent" + "\\" + moduleVO.getModuleName()
									+ "\\" + formVO.getFormName() + "search.jsp"),
							contentsearchjsp.getBytes(), StandardOpenOption.APPEND);

					System.out.println("DONE WITH FORM TAG");
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}

				String contentsearch = "\r\n" + "<td>${z.id}</td>";
				try {
					Files.write(
							Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
									+ projectVO.getProjectName() + "\\" + "WebContent" + "\\" + moduleVO.getModuleName()
									+ "\\" + formVO.getFormName() + "search.jsp"),
							contentsearch.getBytes(), StandardOpenOption.APPEND);

					System.out.println("DONE");
				} catch (IOException e) {
					// exception handling left as an exercise for the
					// reader
				}
				/* JSP file copy END */

				/* CONTROLLER FILE COPY */

				File fcontroller = new File("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name);
				Path pcontroller = Paths.get(fcontroller.getAbsolutePath() + "\\" + projectVO.getProjectName() + "\\"
						+ "src" + "\\" + "Controller" + "\\" + formVO.getFormName() + ".java");
				File fcontroller2 = new File(pcontroller.toString());

				try {
					fcontroller2.createNewFile();
				} catch (IOException e) {

					e.printStackTrace();
				}

				/* Controller Copy */

				try {
					File infile = new File("C:\\Users\\lucky\\Desktop\\ProjectName\\Controller.txt");
					File outfile = new File(
							"C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\" + projectVO.getProjectName()
									+ "\\" + "src" + "\\" + "Controller" + "\\" + formVO.getFormName() + ".java");

					instream = new FileInputStream(infile);
					outstream = new FileOutputStream(outfile);

					byte[] buffer = new byte[1024];

					int length;

					while ((length = instream.read(buffer)) > 0) {
						outstream.write(buffer, 0, length);
					}

					instream.close();
					outstream.close();

					System.out.println("CONTROLLER File copied successfully!!");

				} catch (IOException ioe) {
					ioe.printStackTrace();
				}

				String content1 = "\r\n" + "import" + " " + "DAO." + " " + formVO.getFormName() + "DAO" + ";" + "\r\n"
						+ "import" + " " + "VO." + " " + formVO.getFormName() + "VO" + ";";
				try {
					Files.write(
							Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
									+ projectVO.getProjectName() + "\\" + "src" + "\\" + "Controller" + "\\"
									+ formVO.getFormName() + ".java"),
							content1.getBytes(), StandardOpenOption.APPEND);

					System.out.println("COTRLLER IMPORT DONE");
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}

				String content2 = "\r\n" + "@WebServlet(" + '"' + "/" + formVO.getFormName() + '"' + ")";
				try {
					Files.write(
							Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
									+ projectVO.getProjectName() + "\\" + "src" + "\\" + "Controller" + "\\"
									+ formVO.getFormName() + ".java"),
							content2.getBytes(), StandardOpenOption.APPEND);

					System.out.println("CONTRLLER CLASS DONE");
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}

				String content3 = "\r\n" + "public" + " " + " class" + " " + formVO.getFormName() + " " + "extends"
						+ " " + "HttpServlet" + " " + "{" + "\r\n" + "private" + " " + "static" + " " + "final" + " "
						+ "long" + " " + "serialVersionUID = 1L;" + "\r\n" + "public" + " " + formVO.getFormName()
						+ "()" + " " + "{" + "\r\n" + "super();" + "\r\n" + "}" + "\r\n" + "protected" + " " + "void"
						+ " " + "doGet" + "(HttpServletRequest" + " " + "request," + " " + "HttpServletResponse" + " "
						+ "response)" + " " + "throws" + " " + "ServletException," + " " + "IOException {" + "\r\n"
						+ "response.getWriter().append(" + '"' + "Served" + " " + " at: " + " " + '"' + ")"
						+ ".append(request.getContextPath());" + "\r\n" + formVO.getFormName() + "VO" + " "
						+ formVO.getFormName().toLowerCase() + "VO" + " " + "= new" + " " + formVO.getFormName()
						+ "VO();" + "\r\n" + formVO.getFormName() + "DAO" + " " + formVO.getFormName().toLowerCase()
						+ "DAO" + " " + "= new" + " " + formVO.getFormName() + "DAO();"
						+ "String flag = request.getParameter(" + '"' + "flag" + '"' + ");" + "\r\n"
						+ "if (flag.equals(" + '"' + "search" + '"' + ")) {" + "\r\n" + "List ls ="
						+ formVO.getFormName().toLowerCase() + "DAO.search();" + "\r\n"
						+ "HttpSession s = request.getSession();" + "\r\n" + "s.setAttribute(" + '"' + "ls" + '"'
						+ ", ls);" + "\r\n" + "response.sendRedirect(" + '"' + moduleVO.getModuleName() + "/"
						+ formVO.getFormName() + "search.jsp" + '"' + ");" + "\r\n" + "}" + "\r\n" + "else{" + "\r\n";

				try

				{
					Files.write(
							Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
									+ projectVO.getProjectName() + "\\" + "src" + "\\" + "Controller" + "\\"
									+ formVO.getFormName() + ".java"),
							content3.getBytes(), StandardOpenOption.APPEND);

					System.out.println("MAIN CONTAINT IN CONTROLLER DONE");
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}

				/* Controller File END */

				/* _________________________________________________________DAO_________________________________________________________________________ */

				File fdao = new File("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name);
				Path pdao = Paths.get(fdao.getAbsolutePath() + "\\" + projectVO.getProjectName() + "\\" + "src" + "\\"
						+ "DAO" + "\\" + formVO.getFormName() + "DAO.java");
				File fdao2 = new File(pdao.toString());

				try {
					fdao2.createNewFile();
				} catch (IOException e) {

					e.printStackTrace();
				}

				try {
					File infile = new File("C:\\Users\\lucky\\Desktop\\ProjectName\\DAO.txt");
					File outfile = new File(
							"C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\" + projectVO.getProjectName()
									+ "\\" + "src" + "\\" + "DAO" + "\\" + formVO.getFormName() + "DAO.java");

					instream = new FileInputStream(infile);
					outstream = new FileOutputStream(outfile);

					byte[] buffer = new byte[1024];

					int length;

					while ((length = instream.read(buffer)) > 0) {
						outstream.write(buffer, 0, length);
					}

					instream.close();
					outstream.close();

					System.out.println(" DAO File copied successfully!!");

				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				String content4 = "import VO." + formVO.getFormName() + "VO;" + "\r\n" + "public class" + " "
						+ formVO.getFormName() + "DAO {" + "\r\n" + "public void insert(" + formVO.getFormName() + "VO"
						+ " " + formVO.getFormName().toLowerCase() + "){" + "\r\n" + "try{" + "\r\n"
						+ " SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();"
						+ "\r\n" + "Session session =sessionFactory.openSession();" + "\r\n"
						+ "Transaction tr = session.beginTransaction();" + "\r\n" + "session.save("
						+ formVO.getFormName().toLowerCase() + ");" + "\r\n" + "tr.commit();" + "\r\n" + "}" + "\r\n"
						+ "catch(Exception e1){" + "\r\n" + "}" + "\r\n" + "}" + "\r\n";

				try {
					Files.write(
							Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
									+ projectVO.getProjectName() + "\\" + "src" + "\\" + "DAO" + "\\"
									+ formVO.getFormName() + "DAO.java"),
							content4.getBytes(), StandardOpenOption.APPEND);

					System.out.println("DAO CONTAINT DONE");
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}

				String contentDAO = "\r\n" + "public List search(){" + "\r\n" + "List searchList = new ArrayList();"
						+ "\r\n" + "try{" + "\r\n"
						+ " SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();"
						+ "\r\n" + "Session session =sessionFactory.openSession();" + "\r\n"
						+ "Query query = session.createQuery(" + '"' + "from" + " " + formVO.getFormName() + "VO" + '"'
						+ ");" + "\r\n" + "searchList = query.list();" + "\r\n" + "}" + "\r\n" + "catch(Exception e1){"
						+ "\r\n" + "}" + "return searchList;" + "\r\n" + "}";

				try {
					Files.write(
							Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
									+ projectVO.getProjectName() + "\\" + "src" + "\\" + "DAO" + "\\"
									+ formVO.getFormName() + "DAO.java"),
							contentDAO.getBytes(), StandardOpenOption.APPEND);

					System.out.println("DAO CONTAINT DONE");
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}

				String contentdeleteDAO = "\r\n" + "public void delete(" + formVO.getFormName() + "VO" + " "
						+ formVO.getFormName().toLowerCase() + "){" + "\r\n" + "try{" + "\r\n"
						+ " SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();"
						+ "\r\n" + "Session session =sessionFactory.openSession();" + "\r\n"
						+ "Transaction tr = session.beginTransaction();" + "\r\n" + "session.delete("
						+ formVO.getFormName().toLowerCase() + ");" + "\r\n" + "tr.commit();" + "\r\n" + "}" + "\r\n"
						+ "catch(Exception e1){" + "\r\n" + "}" + "\r\n" + "}" + "\r\n";
				try {
					Files.write(
							Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
									+ projectVO.getProjectName() + "\\" + "src" + "\\" + "DAO" + "\\"
									+ formVO.getFormName() + "DAO.java"),
							contentdeleteDAO.getBytes(), StandardOpenOption.APPEND);

					System.out.println("DAO CONTAINT DONE");
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}

				String contenteditDAO = "\r\n" + "public List edit(" + formVO.getFormName() + "VO" + " "
						+ formVO.getFormName().toLowerCase() + "){" + "\r\n" + "List editList = new ArrayList();"
						+ "\r\n" + "try{" + "\r\n"
						+ " SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();"
						+ "\r\n" + "Session session =sessionFactory.openSession();" + "\r\n"
						+ "Query query = session.createQuery(" + '"' + "from" + " " + formVO.getFormName() + "VO" + " "
						+ "where id=" + "'" + '"' + "+" + formVO.getFormName().toLowerCase() + ".getId()" + "+" + '"'
						+ "'" + '"' + ");" + "\r\n" + " editList = query.list();" + "\r\n" + "}" + "\r\n"
						+ "catch(Exception e1){" + "\r\n" + "}" + "return editList;" + "\r\n" + "}";

				try {
					Files.write(
							Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
									+ projectVO.getProjectName() + "\\" + "src" + "\\" + "DAO" + "\\"
									+ formVO.getFormName() + "DAO.java"),
							contenteditDAO.getBytes(), StandardOpenOption.APPEND);

					System.out.println("DAO CONTAINT DONE");
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}

				String contentupdateDAO = "\r\n" + "public void update(" + formVO.getFormName() + "VO" + " "
						+ formVO.getFormName().toLowerCase() + "){" + "\r\n" + "try{" + "\r\n"
						+ " SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();"
						+ "\r\n" + "Session session =sessionFactory.openSession();" + "\r\n"
						+ "Transaction tr = session.beginTransaction();" + "\r\n" + "session.saveOrUpdate("
						+ formVO.getFormName().toLowerCase() + ");" + "\r\n" + "tr.commit();" + "\r\n" + "}" + "\r\n"
						+ "catch(Exception e1){" + "\r\n" + "}" + "\r\n" + "}" + "\r\n" + "}";
				try {
					Files.write(
							Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
									+ projectVO.getProjectName() + "\\" + "src" + "\\" + "DAO" + "\\"
									+ formVO.getFormName() + "DAO.java"),
							contentupdateDAO.getBytes(), StandardOpenOption.APPEND);

					System.out.println("DAO CONTAINT DONE");
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}

				/* __________________________________________________________DAOEND_____________________________________________________________________________ */

				/*_____________________________________________________VO_______________________________________________________________________________________ */

				File fvo = new File("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name);
				Path pvo = Paths.get(fvo.getAbsolutePath() + "\\" + projectVO.getProjectName() + "\\" + "src" + "\\"
						+ "VO" + "\\" + formVO.getFormName() + "VO.java");
				File fvo2 = new File(pvo.toString());

				if (fvo2.delete()) {
					System.out.println(fvo2.getName() + " is deleted!");
				}
				try {
					fvo2.createNewFile();
				} catch (IOException e) {

					e.printStackTrace();
				}

				String content5 = "package VO;" + "\r\n" + "public class" + " " + formVO.getFormName() + "VO{" + "\r\n"
						+ "private int id;" + "\r\n" + "public int getId() {" + "\r\n" + "return id;" + "\r\n" + "}"
						+ "\r\n" + "public void setId(int id) {" + "\r\n" + "this.id = id;" + "\r\n" + "}";

				try {
					Files.write(
							Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
									+ projectVO.getProjectName() + "\\" + "src" + "\\" + "VO" + "\\"
									+ formVO.getFormName() + "VO.java"),
							content5.getBytes(), StandardOpenOption.APPEND);

					System.out.println("VO UPPER DONE");
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}

				/*_________________________________________________________VOEND_______________________________________________________________________*/

				/*___________________________________________________________HBM________________________________________________________________________*/

				File fhbm = new File("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name);
				Path phbm = Paths.get(fhbm.getAbsolutePath() + "\\" + projectVO.getProjectName() + "\\" + "src" + "\\"
						+ "VO" + "\\" + formVO.getFormName().toLowerCase() + ".hbm.xml");
				File fhbm2 = new File(phbm.toString());

				try {
					fhbm2.createNewFile();
				} catch (IOException e) {

					e.printStackTrace();
				}

				try {
					File infile = new File("C:\\Users\\lucky\\Desktop\\ProjectName\\hbm.txt");
					File outfile = new File(
							"C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\" + projectVO.getProjectName()
									+ "\\" + "src" + "\\" + "VO" + "\\" + formVO.getFormName() + ".hbm.xml");

					instream = new FileInputStream(infile);
					outstream = new FileOutputStream(outfile);

					byte[] buffer = new byte[1024];

					int length;

					while ((length = instream.read(buffer)) > 0) {
						outstream.write(buffer, 0, length);
					}

					instream.close();
					outstream.close();

					System.out.println("HBM File copied successfully!!");

				} catch (IOException ioe) {
					ioe.printStackTrace();
				}

				String content9 = "\r\n" + "<class name=" + '"' + "VO." + formVO.getFormName() + "VO" + '"' + " "
						+ "table=" + '"' + formVO.getFormName().toLowerCase() + '"' + ">" + "\r\n" + "<id name=" + '"'
						+ "id" + '"' + " type=" + '"' + "int" + '"' + " " + "column=" + '"' + "ID" + '"' + ">" + "\r\n"
						+ "<generator class=" + '"' + "increment" + '"' + "/>" + "\r\n" + "</id>";

				try {
					Files.write(
							Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
									+ projectVO.getProjectName() + "\\" + "src" + "\\" + "VO" + "\\"
									+ formVO.getFormName() + ".hbm.xml"),
							content9.getBytes(), StandardOpenOption.APPEND);

					System.out.println("HBM DONE");
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}

				/*_____________________________________________________HBMEND_______________________________________________________________________________*/

				/*_______________________________________________________________cfg_________________________________________________________________________*/

				String contentcfg = "\r\n" + " <mapping resource=" + '"' + "VO/" + formVO.getFormName().toLowerCase()
						+ ".hbm.xml" + '"' + "/>";

				try {
					Files.write(
							Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
									+ projectVO.getProjectName() + "\\" + "src" + "\\" + "hibernate.cfg.xml"),
							contentcfg.getBytes(), StandardOpenOption.APPEND);

					System.out.println("CFG DONE");
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}
				/*________________________________________________________________cfgend_______________________________________________________________*/

				InnerFormVO innerFormVO1 = new InnerFormVO();
				innerFormVO1.setFormVO(formVO);
				List innerformList = this.innerformdao.genInnerForm(innerFormVO1);

				/* Innerform Iterator */

				for (Iterator itr1 = innerformList.iterator(); itr1.hasNext();) {

					InnerFormVO innerFormVO = (InnerFormVO) itr1.next();

					List fl = this.innerformdao.searchFieldLs(innerFormVO);
					String fls = (String) fl.get(0);
					FieldVO fieldVO = new FieldVO();
					fieldVO.setFieldName(fls);

					/* JSP file */

					if (fieldVO.getFieldName().equals("hidden") || fieldVO.getFieldName().equals("file")) {
						String content = "\r\n" + "<input type='" + fieldVO.getFieldName() + "' name='"
								+ innerFormVO.getInnerfieldName() + "' value='" + innerFormVO.getInnerfieldValue()
								+ "'>" + "<br>";

						try {
							Files.write(
									Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
											+ projectVO.getProjectName() + "\\" + "WebContent" + "\\"
											+ moduleVO.getModuleName() + "\\" + formVO.getFormName() + ".jsp"),
									content.getBytes(), StandardOpenOption.APPEND);

							System.out.println("DONE");
						} catch (IOException e) {
							// exception handling left as an exercise for the
							// reader
						}
						continue;
					}

					if (fieldVO.getFieldName().equals("select")) {
						String content = "\r\n" + innerFormVO.getInnerfieldValue() + ":-" + "<select name='"
								+ innerFormVO.getInnerfieldName() + "'>" + "\r\n" + "<option value='abc'>ABC</option>";

						try {
							Files.write(
									Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
											+ projectVO.getProjectName() + "\\" + "WebContent" + "\\"
											+ moduleVO.getModuleName() + "\\" + formVO.getFormName() + ".jsp"),
									content.getBytes(), StandardOpenOption.APPEND);

							System.out.println("DONE");
						} catch (IOException e) {
							// exception handling left as an exercise for the
							// reader
						}

						String contentselect = "\r\n" + "</select>" + "<br>";
						try {
							Files.write(
									Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
											+ projectVO.getProjectName() + "\\" + "WebContent" + "\\"
											+ moduleVO.getModuleName() + "\\" + formVO.getFormName() + ".jsp"),
									contentselect.getBytes(), StandardOpenOption.APPEND);

							System.out.println("DONE");
						} catch (IOException e) {
							// exception handling left as an exercise for the
							// reader
						}
						String contentsearch1 = "\r\n" + "<td>${z." + innerFormVO.getInnerfieldName().toLowerCase()
								+ "}</td>";
						try {
							Files.write(
									Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
											+ projectVO.getProjectName() + "\\" + "WebContent" + "\\"
											+ moduleVO.getModuleName() + "\\" + formVO.getFormName() + "search.jsp"),
									contentsearch1.getBytes(), StandardOpenOption.APPEND);

							System.out.println("DONE");
						} catch (IOException e) {
							// exception handling left as an exercise for the
							// reader
						}

					}

					if (fieldVO.getFieldName().equals("submit") || fieldVO.getFieldName().equals("button")
							|| fieldVO.getFieldName().equals("reset")) {
						String content = "\r\n" + "<input type='" + fieldVO.getFieldName() + "' value='"
								+ innerFormVO.getInnerfieldValue() + "'>" + "<br>";

						try {
							Files.write(
									Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
											+ projectVO.getProjectName() + "\\" + "WebContent" + "\\"
											+ moduleVO.getModuleName() + "\\" + formVO.getFormName() + ".jsp"),
									content.getBytes(), StandardOpenOption.APPEND);

							System.out.println("DONE");
						} catch (IOException e) {
							// exception handling left as an exercise for the
							// reader
						}
						continue;
					}

					if (fieldVO.getFieldName().equals("number") || fieldVO.getFieldName().equals("date")
							|| fieldVO.getFieldName().equals("tel") || fieldVO.getFieldName().equals("time")) {
						String content = "\r\n" + innerFormVO.getInnerfieldValue() + ":-" + "<input type='"
								+ fieldVO.getFieldName() + "' name='" + innerFormVO.getInnerfieldName() + "' value='"
								+ innerFormVO.getInnerfieldValue() + "'>" + "<br>";

						try {
							Files.write(
									Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
											+ projectVO.getProjectName() + "\\" + "WebContent" + "\\"
											+ moduleVO.getModuleName() + "\\" + formVO.getFormName() + ".jsp"),
									content.getBytes(), StandardOpenOption.APPEND);

							System.out.println("DONE");
						} catch (IOException e) {
							// exception handling left as an exercise for the
							// reader
						}
						String contentsearch1 = "\r\n" + "<td>${z." + innerFormVO.getInnerfieldName().toLowerCase()
								+ "}</td>";
						try {
							Files.write(
									Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
											+ projectVO.getProjectName() + "\\" + "WebContent" + "\\"
											+ moduleVO.getModuleName() + "\\" + formVO.getFormName() + "search.jsp"),
									contentsearch1.getBytes(), StandardOpenOption.APPEND);

							System.out.println("DONE");
						} catch (IOException e) {
							// exception handling left as an exercise for the
							// reader
						}

					}
					if (fieldVO.getFieldName().equals("textarea")) {
						String content = "\r\n" + innerFormVO.getInnerfieldValue() + ":-" + "<textarea name='"
								+ innerFormVO.getInnerfieldName() + "'>" + "</textarea>" + "\r\n" + "<br>";

						try {
							Files.write(
									Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
											+ projectVO.getProjectName() + "\\" + "WebContent" + "\\"
											+ moduleVO.getModuleName() + "\\" + formVO.getFormName() + ".jsp"),
									content.getBytes(), StandardOpenOption.APPEND);

							System.out.println("DONE");
						} catch (IOException e) {
							// exception handling left as an exercise for the
							// reader
						}
						String contentsearch1 = "\r\n" + "<td>${z." + innerFormVO.getInnerfieldName().toLowerCase()
								+ "}</td>";
						try {
							Files.write(
									Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
											+ projectVO.getProjectName() + "\\" + "WebContent" + "\\"
											+ moduleVO.getModuleName() + "\\" + formVO.getFormName() + "search.jsp"),
									contentsearch1.getBytes(), StandardOpenOption.APPEND);

							System.out.println("DONE");
						} catch (IOException e) {
							// exception handling left as an exercise for the
							// reader
						}

					}

					if (fieldVO.getFieldName().equals("text") || fieldVO.getFieldName().equals("password")
							|| fieldVO.getFieldName().equals("email")) {
						String content = "\r\n" + innerFormVO.getInnerfieldValue() + ":-" + "<input type='"
								+ fieldVO.getFieldName() + "' name='" + innerFormVO.getInnerfieldName() + "' value='"
								+ innerFormVO.getInnerfieldValue() + "'>" + "<br>";

						try {
							Files.write(
									Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
											+ projectVO.getProjectName() + "\\" + "WebContent" + "\\"
											+ moduleVO.getModuleName() + "\\" + formVO.getFormName() + ".jsp"),
									content.getBytes(), StandardOpenOption.APPEND);

							System.out.println("DONE");
						} catch (IOException e) {
							// exception handling left as an exercise for the
							// reader
						}
						String contentsearch1 = "\r\n" + "<td>${z." + innerFormVO.getInnerfieldName().toLowerCase()
								+ "}</td>";
						try {
							Files.write(
									Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
											+ projectVO.getProjectName() + "\\" + "WebContent" + "\\"
											+ moduleVO.getModuleName() + "\\" + formVO.getFormName() + "search.jsp"),
									contentsearch1.getBytes(), StandardOpenOption.APPEND);

							System.out.println("DONE");
						} catch (IOException e) {
							// exception handling left as an exercise for the
							// reader
						}
					}
					if (fieldVO.getFieldName().equals("radio") || fieldVO.getFieldName().equals("checkbox")) {
						String line;
						int count = 0;
						String rc = "name='" + innerFormVO.getInnerfieldName() + "'";

						try (InputStream fis = new FileInputStream("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\"
								+ name + "\\" + projectVO.getProjectName() + "\\" + "WebContent" + "\\"
								+ moduleVO.getModuleName() + "\\" + formVO.getFormName() + ".jsp");
								InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
								BufferedReader br = new BufferedReader(isr);) {
							try {
								while ((line = br.readLine()) != null) {
									String[] words = line.split(" ");
									for (i = 0; i < words.length; i++) {

										System.out.println(words[i]);
										if (words[i].equalsIgnoreCase(rc)) {

											count++;
											System.out.println(count);

										}
									}
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}

						String content = "\r\n" + innerFormVO.getInnerfieldValue() + " " + ":-" + "<input type='"
								+ fieldVO.getFieldName() + "' name='" + innerFormVO.getInnerfieldName() + "' value='"
								+ innerFormVO.getInnerfieldValue() + "'>" + "<br>";

						try {
							Files.write(
									Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
											+ projectVO.getProjectName() + "\\" + "WebContent" + "\\"
											+ moduleVO.getModuleName() + "\\" + formVO.getFormName() + ".jsp"),
									content.getBytes(), StandardOpenOption.APPEND);

							if (formVO.getFormName().equals("radio")) {
								System.out.println("Radio button DONE");
							}
							if (formVO.getFormName().equals("checkbox")) {
								System.out.println("Checkbox button DONE");
							}

						} catch (IOException e) {
							// exception handling left as an exercise for
							// the
							// reader
						}
						if (count == 0) {

							String contentsearch1 = "\r\n" + "<td>${z." + innerFormVO.getInnerfieldName().toLowerCase()
									+ "}</td>";
							try {
								Files.write(
										Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
												+ projectVO.getProjectName() + "\\" + "WebContent" + "\\"
												+ moduleVO.getModuleName() + "\\" + formVO.getFormName()
												+ "search.jsp"),
										contentsearch1.getBytes(), StandardOpenOption.APPEND);

								System.out.println("DONE");
							} catch (IOException e) {
								// exception handling left as an exercise for
								// the
								// reader
							}
						}

						if (count > 0) {
							count = 0;
							continue;

						}

					}

					/* JSP file */
					/* _____________________________________________________________Controller______________________________________________________________*/

					if (fieldVO.getFieldName().equals("checkbox")) {
						String content7 = "\r\n" + "String[]" + " " + innerFormVO.getInnerfieldName().toLowerCase()
								+ " " + "=request.getParameterValues(" + '"' + innerFormVO.getInnerfieldName() + '"'
								+ ");" + "\r\n" + "String" + " " + innerFormVO.getInnerfieldName().toLowerCase() + "1"
								+ " " + "=" + " " + '"' + '"' + ";" + "\r\n" + "if("
								+ innerFormVO.getInnerfieldName().toLowerCase() + "!=null && "
								+ innerFormVO.getInnerfieldName().toLowerCase() + ".length !=0) {" + "\r\n"
								+ "for(int i=0;i<" + innerFormVO.getInnerfieldName().toLowerCase() + ".length; i++) {"
								+ "\r\n" + "if(" + innerFormVO.getInnerfieldName().toLowerCase() + ".length==i+1){"
								+ "\r\n" + innerFormVO.getInnerfieldName().toLowerCase() + "1" + " " + "=" + " "
								+ innerFormVO.getInnerfieldName().toLowerCase() + "1" + " " + "+" + " "
								+ innerFormVO.getInnerfieldName().toLowerCase() + "[i];" + "\r\n" + "}else{" + "\r\n"
								+ innerFormVO.getInnerfieldName().toLowerCase() + "1" + " " + "=" + " "
								+ innerFormVO.getInnerfieldName().toLowerCase() + "1" + " " + "+" + " "
								+ innerFormVO.getInnerfieldName().toLowerCase() + "[i]+" + '"' + "," + '"' + ";"
								+ "\r\n" + "}" + "\r\n" + "}" + "\r\n" + "}" + "\r\n"
								+ formVO.getFormName().toLowerCase() + "VO" + ".set" + innerFormVO.getInnerfieldName()
								+ "(" + innerFormVO.getInnerfieldName().toLowerCase() + "1" + ");";
						try {
							Files.write(
									Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
											+ projectVO.getProjectName() + "\\" + "src" + "\\" + "Controller" + "\\"
											+ formVO.getFormName() + ".java"),
									content7.getBytes(), StandardOpenOption.APPEND);

							System.out.println("CONTOLLER MIDDLE DONE");
						} catch (IOException e) {
							// exception handling left as an exercise for the
							// reader
						}

					}

					else {

						String content7 = "\r\n" + "String" + " " + innerFormVO.getInnerfieldName().toLowerCase() + " "
								+ "=request.getParameter(" + '"' + innerFormVO.getInnerfieldName() + '"'
								+ ").toString();" + "\r\n" + formVO.getFormName().toLowerCase() + "VO" + ".set"
								+ innerFormVO.getInnerfieldName() + "(" + innerFormVO.getInnerfieldName().toLowerCase()
								+ ");";
						try {
							Files.write(
									Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
											+ projectVO.getProjectName() + "\\" + "src" + "\\" + "Controller" + "\\"
											+ formVO.getFormName() + ".java"),
									content7.getBytes(), StandardOpenOption.APPEND);

							System.out.println("CONTOLLER MIDDLE DONE");
						} catch (IOException e) {
							// exception handling left as an exercise for the
							// reader
						}
					}

					/*_________________________________________________Controllerend____________________________________________________________________ */

					/*______________________________________________________ VO__________________________________________________________________________*/

					String content8 = "\r\n" + "private String" + " " + innerFormVO.getInnerfieldName().toLowerCase()
							+ ";" + "\r\n" + "public String get" + innerFormVO.getInnerfieldName() + "(){" + "\r\n"
							+ "return" + " " + innerFormVO.getInnerfieldName().toLowerCase() + ";" + "\r\n" + "}"
							+ "\r\n" + "public void set" + innerFormVO.getInnerfieldName() + "(String" + " "
							+ innerFormVO.getInnerfieldName().toLowerCase() + "){" + "\r\n" + "this."
							+ innerFormVO.getInnerfieldName().toLowerCase() + " " + "=" + " "
							+ innerFormVO.getInnerfieldName().toLowerCase() + ";" + "\r\n" + "}";

					try {
						Files.write(
								Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
										+ projectVO.getProjectName() + "\\" + "src" + "\\" + "VO" + "\\"
										+ formVO.getFormName() + "VO.java"),
								content8.getBytes(), StandardOpenOption.APPEND);

						System.out.println("VO GETTER SETTER DONE");
					} catch (IOException e) {
						// exception handling left as an exercise for the reader
					}

					/*______________________________________________________ VOEND__________________________________________________________________________*/

					/*______________________________________________________ HBM __________________________________________________________________________*/

					String contenthbm = "\r\n" + "<property name=" + '"' + innerFormVO.getInnerfieldName().toLowerCase()
							+ '"' + ">" + "\r\n" + "<column name=" + '"' + innerFormVO.getInnerfieldName().toUpperCase()
							+ '"' + "/>" + "\r\n" + "</property>";

					try {
						Files.write(
								Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
										+ projectVO.getProjectName() + "\\" + "src" + "\\" + "VO" + "\\"
										+ formVO.getFormName() + ".hbm.xml"),
								contenthbm.getBytes(), StandardOpenOption.APPEND);

						System.out.println("HBM MIDDLE DONE");
					} catch (IOException e) {
						// exception handling left as an exercise for the reader
					}

					/*______________________________________________________ HBMEND__________________________________________________________________________*/

				}

				/* Innerform iterator ends */

				/*______________________________________________________ CONTROLLER__________________________________________________________________________*/
				String content6 = "\r\n" + formVO.getFormName().toLowerCase() + "DAO.insert("
						+ formVO.getFormName().toLowerCase() + "VO);" + "\r\n" + "}" + "\r\n" + "}" + "\r\n"
						+ "protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {"
						+ "\r\n" + "doGet(request, response);" + "\r\n" + "}" + "\r\n" + "}";
				try {
					Files.write(
							Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
									+ projectVO.getProjectName() + "\\" + "src" + "\\" + "Controller" + "\\"
									+ formVO.getFormName() + ".java"),
							content6.getBytes(), StandardOpenOption.APPEND);

					System.out.println("CONTROLLER END DONE");
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}

				/*______________________________________________________ CONTROLLEREND ________________________________________________________________________*/

				/*______________________________________________________ VO ________________________________________________________________________*/

				String contentVO = "\r\n" + "}";
				try {
					Files.write(
							Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
									+ projectVO.getProjectName() + "\\" + "src" + "\\" + "VO" + "\\"
									+ formVO.getFormName() + "VO.java"),
							contentVO.getBytes(), StandardOpenOption.APPEND);

					System.out.println("VO END DONE");
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}

				/*______________________________________________________ VOEND ________________________________________________________________________*/

				/*______________________________________________________ HBM ________________________________________________________________________*/
				String contenthbm = "\r\n" + "	</class>" + "\r\n" + "</hibernate-mapping>";

				try {
					Files.write(
							Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
									+ projectVO.getProjectName() + "\\" + "src" + "\\" + "VO" + "\\"
									+ formVO.getFormName() + ".hbm.xml"),
							contenthbm.getBytes(), StandardOpenOption.APPEND);

					System.out.println("HBM ENDING DONE");
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}

				/*______________________________________________________ HBMEND ________________________________________________________________________*/

				/* JSP file Ending */

				String searchcontent = "\r\n" + "</tr>" + "\r\n" + "</c:forEach>" + "\r\n" + "</table>" + "\r\n"
						+ "</body>" + "</html>";

				try {
					Files.write(
							Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
									+ projectVO.getProjectName() + "\\" + "WebContent" + "\\" + moduleVO.getModuleName()
									+ "\\" + formVO.getFormName() + "search.jsp"),
							searchcontent.getBytes(), StandardOpenOption.APPEND);

					System.out.println("JSP END DONE");
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}

				String content = "\r\n" + "<input type='submit'>" + "\r\n" + "</form>" + "\r\n" + "</body>" + "\r\n"
						+ "</html>";

				try {
					Files.write(
							Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
									+ projectVO.getProjectName() + "\\" + "WebContent" + "\\" + moduleVO.getModuleName()
									+ "\\" + formVO.getFormName() + ".jsp"),
							content.getBytes(), StandardOpenOption.APPEND);

					System.out.println("JSP END DONE");
				} catch (IOException e) {
					// exception handling left as an exercise for the reader
				}

				/* JSP file Ending */

			}

			/* Form Iterator ends */

			String contentindex1 = "	</table>" + "\r\n" + "</body>" + "\r\n" + "</html>";

			try {
				Files.write(
						Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
								+ projectVO.getProjectName() + "\\" + "WebContent" + "\\" + moduleVO.getModuleName()
								+ "\\" + "index" + ".jsp"),
						contentindex1.getBytes(), StandardOpenOption.APPEND);

				System.out.println("DONE WITH FORM TAG");
			} catch (IOException e) {
				// exception handling left as an exercise for the reader
			}

		}
		/*______________________________________________________ CFG ________________________________________________________________________*/

		String contentcfg1 = "\r\n" + "</session-factory>" + "\r\n" + "</hibernate-configuration>";

		try {
			Files.write(
					Paths.get("C:\\Users\\lucky\\Desktop\\UserProjects" + "\\" + name + "\\"
							+ projectVO.getProjectName() + "\\" + "src" + "\\" + "hibernate.cfg.xml"),
					contentcfg1.getBytes(), StandardOpenOption.APPEND);

			System.out.println("CFG END DONE");
		} catch (IOException e) {
			// exception handling left as an exercise for the reader
		}
		/*______________________________________________________ CFGEND ________________________________________________________________________*/

		/* Module Iterator ends */
		session.setAttribute("fadeinfadeoutprojectcount", 1);

		return ("redirect:codeGen.html");
	}

}
