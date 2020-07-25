package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class DownloadFile
 */
@WebServlet("/DownloadFile")
public class DownloadFile extends HttpServlet {
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		try {
			String fn=request.getParameter("fname");
			int pid=Integer.parseInt(request.getParameter("pid"));
			db.DbConnect db=new db.DbConnect();
			byte []b=db.getFile(pid);
			if(b!=null) {
				response.setContentType("APPLICATION/OCTET-STREAM");   
                response.setHeader("Content-Disposition","attachment; filename="+fn); 
                response.getOutputStream().write(b);
			}
		}catch(Exception ex) {
			session.setAttribute("msg", "Something went wrong "+ex);
	        response.sendRedirect("profile.jsp");
		}
	}

}
