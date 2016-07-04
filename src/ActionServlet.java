
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class ActionServlet
 */
@WebServlet("/ActionServlet")
public class ActionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ResultSet resultSet=null;
	private PreparedStatement preparedStatement=null;
	String queryVersion=new StringBuilder()
			.append("select v.name from versions v ")
			.append("join products p on p.id=v.product_id ")
			.append("where p.name=?")
			.toString();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ActionServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Connection con = null;
		List<String> versions=new ArrayList<>();
		String productName=request.getParameter("productName");
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			System.out.println("***********class not found********");
			e1.printStackTrace();
		}
 	      try {
			con = DriverManager.getConnection(
			    "jdbc:mysql://bz3-m-db3.eng.vmware.com:3306/bugzilla", "mts", "mts");
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			System.out.println("******DB not found**********");
			e1.printStackTrace();
		}
 	     try {
			versions=getVersions(con,productName,queryVersion);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		String json = null ;
		
		json= new Gson().toJson(versions);

		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private List<String> getVersions(Connection con,String product,String query) throws SQLException{
		String version=null;
	
		List<String> versions=new ArrayList<>();
		preparedStatement = con.prepareStatement(query);
		preparedStatement.setString(1, product);
		resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
		      
		       version = resultSet.getString("name");
		       versions.add(version);
		     
		    }
		
		return versions;
		
	}

}
