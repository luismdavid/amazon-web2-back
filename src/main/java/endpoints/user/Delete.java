package endpoints.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utilities.PropertiesReader;
import utilities.db.ConnectionManager;

/**
 * Servlet implementation class Delete
 */
@WebServlet("/DeleteUser")
@MultipartConfig()
public class Delete extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Delete() {
        super();
    }

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PropertiesReader pr = PropertiesReader.getInstance();
		PrintWriter out = response.getWriter();
		String resObj = "{";
		if (request.getSession(false) == null || !request.getSession(false).getAttribute("userId").equals((String) request.getParameter(("id")))) {
			response.setStatus(401);
			out.write("Se debe logear en la cuenta para realizar esta accion.");
			return;
		}
		try (Connection conn = ConnectionManager.getConnection(); PreparedStatement stm = conn.prepareStatement(pr.getValue("DELETE_USER"))) {
			
			stm.setString(1, (String) request.getParameter("id"));
			stm.execute();
			request.getSession(false).invalidate();
			response.setStatus(200);
			resObj += "\"msg\":\"El usuario ha sido eliminado con exito.\" }";
			out.write(resObj);
		} catch (SQLException e) {
			response.setStatus(500);
			resObj += "\"error\":\"Ha ocurrido un error en el servidor: "+e.getMessage()+"\" }";
			out.write(resObj);
			e.printStackTrace();
		}
	}

}
