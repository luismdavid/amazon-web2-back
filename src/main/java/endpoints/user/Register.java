package endpoints.user;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import utilities.JSONConverter;
import utilities.PasswordHasher;
import utilities.PropertiesReader;
import utilities.db.ConnectionManager;


/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
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
		JSONObject resObj = new JSONObject();
		PrintWriter out = response.getWriter();
		Map<String, String[]> map = request.getParameterMap();
		JSONObject record = JSONConverter.requestParamsToJSON(request);
		try (Connection conn = ConnectionManager.getConnection(); PreparedStatement stm = conn.prepareStatement(PropertiesReader.getInstance().getValue("REGISTER_USER"))) {
			String uuid = UUID.randomUUID().toString();
			stm.setString(1, uuid);
			stm.setString(2, (String) record.get("email"));
			stm.setString(3, (String) record.get("username"));
			stm.setString(4, (String) record.get("firstName"));
			stm.setString(5, (String) record.get("lastName"));
			stm.setString(6, PasswordHasher.getHash((String) record.get("password")));
			stm.setString(7, (String) record.get("identification"));
			stm.setString(8, (String) record.get("phone"));
			stm.execute();
			response.setStatus(200);
			resObj.put("msg", "El usuario ha sido registrado con exito.");
			out.write(resObj.toJSONString());
		} catch (SQLException | NoSuchAlgorithmException e) {
			response.setStatus(500);
			resObj.put("error", "Ha ocurrido un error al registrar el usuario: " + e.getMessage());
			out.write(resObj.toJSONString());
			e.printStackTrace();
		}
		
	}

}
