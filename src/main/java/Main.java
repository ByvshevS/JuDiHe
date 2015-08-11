import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static spark.Spark.*;

public class Main {
	
	public static void main(String[] args) {
		
		port(Integer.valueOf(System.getenv("PORT")));
		
		
		get("/", (req, res) -> {
			String s = "Hello World";
			return s;
		});
	
		get("/db", (req, res) -> {
		      Connection con = null;
		      try {
		    	  con = getConnection();
					
				  Statement stmt = con.createStatement();
					
   			      ResultSet rs = stmt.executeQuery("select * from accounts");		    
   			      
   			      String s = "";
   			      
   			      while (rs.next()) {
   			    	  
   			    	  s += rs.getString("name") + " ";
   			    	  s += rs.getString("pass") + " ";
   			    	  s += rs.getInt("secret") + "\n";
   			      }

   			      return s;
   			      
		      } catch (Exception e) {
		    	  return e.getMessage();
		    	  
		    	  //return "exception";
		      } finally {
		    	  if (con != null) try{con.close();} catch(SQLException e){}
		      }
		   	});
		
		get("/test", (request, response) -> {
            response.type("text/xml");
            return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><news> </news>";
        });
	
		
	}
	
	private static Connection getConnection() throws URISyntaxException, SQLException {
	    URI dbUri = new URI(System.getenv("DATABASE_URL"));

	    String username = dbUri.getUserInfo().split(":")[0];
	    String password = dbUri.getUserInfo().split(":")[1];
	    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();

	    return DriverManager.getConnection(dbUrl, username, password);
	}
	
}