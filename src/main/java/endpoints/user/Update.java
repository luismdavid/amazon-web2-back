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
 * Servlet implementation class Update
 */
@WebServlet("/UpdateUser")
@MultipartConfig()
public class Update extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Update() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PropertiesReader pr = PropertiesReader.getInstance();
		PrintWriter out = response.getWriter();
		if (request.getSession(false) == null || !request.getSession(false).getAttribute("userId").equals((String) request.getParameter("id"))) {
			response.setStatus(401);
			out.write("Se debe logear en la cuenta para realizar esta accion.");
			return;
		}
		
		try(Connection conn = ConnectionManager.getConnection(); PreparedStatement stm = conn.prepareStatement(pr.getValue("UPDATE_USER"))) {

			stm.setString(1, (String) request.getParameter("email"));
			stm.setString(2, (String) request.getParameter("username"));
			stm.setString(3, (String) request.getParameter("firstName"));
			stm.setString(4, (String) request.getParameter("lastName"));
			stm.setString(6, (String) request.getParameter("identification"));
			stm.setString(7, (String) request.getParameter("phone"));
			stm.setString(8, (String) request.getParameter("id"));
			stm.execute();
			response.setStatus(200);
			out.write("Usuario actualizado con exito.");
		} catch (SQLException e) {
			response.setStatus(500);
			out.write("Ha ocurrido un error en el servidor: " + e.getMessage());
			e.printStackTrace();
		}
	}

}
