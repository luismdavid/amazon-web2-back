package endpoints.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utilities.PasswordHasher;
import utilities.PropertiesReader;
import utilities.db.ConnectionManager;


/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
@MultipartConfig()
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Register() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String resObj = "{";
		PrintWriter out = response.getWriter();
		try (Connection conn = ConnectionManager.getConnection(); PreparedStatement stm = conn.prepareStatement(PropertiesReader.getInstance().getValue("REGISTER_USER"))) {
			String uuid = UUID.randomUUID().toString();
			stm.setString(1, uuid);
			stm.setString(2, (String) request.getParameter("email"));
			stm.setString(3, (String) request.getParameter("username"));
			stm.setString(4, (String) request.getParameter("firstName"));
			stm.setString(5, (String) request.getParameter("lastName"));
			stm.setString(6, PasswordHasher.getHash((String) request.getParameter("password")));
			stm.setString(7, (String) request.getParameter("identification"));
			stm.setString(8, (String) request.getParameter("phone"));
			stm.execute();
			response.setStatus(200);
			resObj += "\"msg\":\"El usuario ha sido registrado con exito.\" }";
			out.write(resObj);
		} catch (SQLException | NoSuchAlgorithmException e) {
			response.setStatus(500);
			resObj += "\"error\":\"Ha ocurrido un error al registrar el usuario: \"" + e.getMessage()  + "\" }";
			out.write(resObj);
			e.printStackTrace();
		}
		
	}

}
