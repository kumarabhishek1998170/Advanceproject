package servlets;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GetPhoto
 */
@WebServlet("/GetPhoto")
public class GetPhoto extends HttpServlet {
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			String e=request.getParameter("email");
			db.DbConnect db=new db.DbConnect();
			byte[] b=db.getPhoto(e);
			if(b!=null){
				response.getOutputStream().write(b);
			}else {
				ServletContext ctx=getServletContext();
	            InputStream fin=ctx.getResourceAsStream("/img/xyz.jpg");
	            b=new byte[3500];
	            fin.read(b);
	            response.getOutputStream().write(b);
			}
		} catch (Exception ex) {
			ServletContext ctx=getServletContext();
            InputStream fin=ctx.getResourceAsStream("/img/xyz.jpg");
            byte []b=new byte[3500];
            fin.read(b);
            response.getOutputStream().write(b);
        }
	}

}
