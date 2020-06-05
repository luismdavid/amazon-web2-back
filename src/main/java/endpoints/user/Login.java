package endpoints.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import models.User;
import utilities.PasswordHasher;
import utilities.PropertiesReader;
import utilities.db.ConnectionManager;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
@MultipartConfig()
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static PropertiesReader pr = PropertiesReader.getInstance();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String resObj = "{";
		try (Connection conn = ConnectionManager.getConnection(); PreparedStatement stm = conn.prepareStatement(pr.getValue("GET_USER"))) {
			stm.setString(1, (String) request.getParameter("username"));
			User userObject = new User();
			String user = "{";
			String template = "\"%s\":\"%s\",";
			String savedPass = null;
			try(ResultSet rs = stm.executeQuery()) {
				while(rs.next()) {
					userObject.firstName = rs.getString("firstName");
					userObject.lastName = rs.getString("lastName");
					userObject.id = rs.getString("id");
					savedPass = rs.getString("password");
					user += String.format(template, "firstName", rs.getString("firstName"));
					user += String.format(template, "lastName", rs.getString("lastName"));
					user += String.format(template, "phone", rs.getString("phone"));
					user += String.format(template, "email", rs.getString("email"));
					user += String.format(template, "identification", rs.getString("identification"));
					user += String.format(template, "id", rs.getString("id"));
				}
				user += "}";
			}
			if (savedPass != null && savedPass.equals(PasswordHasher.getHash((String) request.getParameter("password")))) {
				HttpSession session = request.getSession();
				session.setAttribute("username", request.getParameter("username"));
				session.setAttribute("userFullName", userObject.firstName + " " + userObject.lastName);
				session.setAttribute("userId", userObject.id);
				response.setStatus(200);	
				response.setContentType("application/json");
				out.write(user);
				return;
			} else {
				response.setStatus(401);
				resObj += "\"error\":\"Contrasena o usuario incorrecto.\" }";
				out.write(resObj);
			}
		} catch (SQLException | NoSuchAlgorithmException e) {
			response.setStatus(500);
			resObj += "\"error\":\"Ha ocurrido un error en el servidor: "+e.getMessage()+"\" }";
			out.write(resObj);
			e.printStackTrace();
		}
	}

}
