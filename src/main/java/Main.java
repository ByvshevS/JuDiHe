import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;
import static spark.Spark.get;

public class Main {
	
	public static void main(String[] args) {
		
		try {
			Connection con = getConnection();
			
			Statement stmt = con.createStatement();
			
			stmt.execute("select * from accounts");
						
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		get("/", (req, res) -> "Hello World");
		
		get("/db", (req, res) -> {
		      Connection con = null;
		      Map<String, Object> attributes = new HashMap<>();
		      try {
		    	  con = getConnection();
					
				  Statement stmt = con.createStatement();
					
   			      ResultSet rs = stmt.executeQuery("select * from accounts");		    
   			      
   			      ArrayList<String> output = new ArrayList<String>();
   			      
   			      while (rs.next()) {
   			    	  output.add( "Read from DB: " + rs.getInt("secret"));
   			      }

   			      attributes.put("results", output);
   			      return new ModelAndView(attributes, "db.ftl");
   			      
		      } catch (Exception e) {
		    	  attributes.put("message", "There was an error: " + e);
		    	  return new ModelAndView(attributes, "error.ftl");
		      } finally {
		    	  if (con != null) try{con.close();} catch(SQLException e){}
		      }
		   	}, new FreeMarkerEngine());
		
		
		
	}
	
	private static Connection getConnection() throws URISyntaxException, SQLException {
	    URI dbUri = new URI(System.getenv("DATABASE_URL"));

	    String username = dbUri.getUserInfo().split(":")[0];
	    String password = dbUri.getUserInfo().split(":")[1];
	    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();

	    return DriverManager.getConnection(dbUrl, username, password);
	}
}