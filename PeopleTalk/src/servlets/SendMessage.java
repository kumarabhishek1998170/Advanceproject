package servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 * Servlet implementation class SendMessage
 */
@WebServlet("/SendMessage")
@MultipartConfig
public class SendMessage extends HttpServlet {

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
try{
			 HashMap userDetails=(HashMap)session.getAttribute("userDetails");
		if(userDetails!=null){
			String temail=request.getParameter("temail");
			String email=(String)userDetails.get("email");
			String message=request.getParameter("message");
			Part p=request.getPart("ufile");
			InputStream is=null;
			String fname="";
			if(p!=null) {
				is=p.getInputStream();
				fname=p.getSubmittedFileName();
			}
			db.DbConnect db=new db.DbConnect();
            String m=db.sendMessage(email,temail, message, fname, is);
            db.disconnect();
            if(m.equalsIgnoreCase("Success")){
            	session.setAttribute("msg", "Message Send success!");
                response.sendRedirect("talk.jsp?temail="+temail);
            }else {
                session.setAttribute("msg", "Message Send failed!");
                response.sendRedirect("talk.jsp?temail="+temail);
            }
		}else{
			session.setAttribute("msg", "Plz login First!");
		    response.sendRedirect("home.jsp");
		}
 } catch (Exception ex) {
        session.setAttribute("msg", "Something went wrong "+ex);
        response.sendRedirect("profile.jsp");
    }
	}

}
