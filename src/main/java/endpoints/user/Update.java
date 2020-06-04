package endpoints.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import utilities.JSONConverter;
import utilities.PropertiesReader;
import utilities.db.ConnectionManager;

/**
 * Servlet implementation class Update
 */
@WebServlet("/UpdateUser")
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
		JSONObject record = JSONConverter.requestParamsToJSON(request); 
		if (request.getSession(false) == null || !request.getSession(false).getAttribute("userId").equals((String) record.get("id"))) {
			response.setStatus(401);
			out.write("Se debe logear en la cuenta para realizar esta accion.");
			return;
		}
		
		try(Connection conn = ConnectionManager.getConnection(); PreparedStatement stm = conn.prepareStatement(pr.getValue("UPDATE_USER"))) {

			stm.setString(1, (String) record.get("email"));
			stm.setString(2, (String) record.get("username"));
			stm.setString(3, (String) record.get("firstName"));
			stm.setString(4, (String) record.get("lastName"));
			stm.setString(6, (String) record.get("identification"));
			stm.setString(7, (String) record.get("phone"));
			stm.setString(8, (String) record.get("id"));
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
