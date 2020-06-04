package endpoints.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import models.User;
import utilities.JSONConverter;
import utilities.PasswordHasher;
import utilities.PropertiesReader;
import utilities.db.ConnectionManager;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
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
		Map<String, String[]> map = request.getParameterMap();
		JSONObject record = JSONConverter.requestParamsToJSON(request);
		try (Connection conn = ConnectionManager.getConnection(); PreparedStatement stm = conn.prepareStatement(pr.getValue("GET_USER"))) {
			stm.setString(1, (String) record.get("username"));
			JSONObject user = new JSONObject();
			String savedPass = null;
			try(ResultSet rs = stm.executeQuery()) {
				while(rs.next()) {
					savedPass = rs.getString("password");
					user.put("firstName", rs.getString("firstName"));
					user.put("lastName", rs.getString("lastName"));
					user.put("id", rs.getString("id"));
					user.put("identification", rs.getString("identification"));
					user.put("email", rs.getString("email"));
					user.put("phone", rs.getString("phone"));
				}
					
			}
			if (savedPass != null && savedPass.equals(PasswordHasher.getHash((String) record.get("password")))) {
				HttpSession session = request.getSession();
				session.setAttribute("username", record.get("username"));
				session.setAttribute("userFullName", user.get("firstName") + " " + user.get("lastName"));
				session.setAttribute("userId", user.get("id"));
				response.setStatus(200);	
				response.setContentType("application/json");
				out.write(user.toJSONString());
				return;
			} else {
				response.setStatus(401);
				JSONObject resObj = new JSONObject();
				resObj.put("error", "Contrasena o usuario incorrecto.");
				out.write(resObj.toJSONString());
			}
		} catch (SQLException | NoSuchAlgorithmException e) {
			response.setStatus(500);
			JSONObject resObj = new JSONObject();
			resObj.put("error", "Ha ocurrido un error en el servidor: " + e.getMessage());
			out.write(resObj.toJSONString());
			e.printStackTrace();
		}
	}

}
