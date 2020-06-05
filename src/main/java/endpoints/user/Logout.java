package endpoints.user;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Logout
 */
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Logout() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		String resObj = "{";
		PrintWriter out = response.getWriter();
		
		if (session == null) {
			response.setStatus(401);

			resObj += "\"error\":\"Debe estar logeado para realizar esta accion.\" }";
			out.write(resObj);
		} else {
			session.invalidate();
			response.setStatus(200);
			resObj += "\"msg\":\"Ha cerrado sesion correctamente.\" }";
			out.write(resObj);
		}
		
	}

}
