import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.JSONObject;


public class Executor {
	private static final String OK = "ok";
	private static final String FAIL = "fail";
	
	public Executor() {
		
	}
	
	public String set(String query) {
		Connection con = null;
		
	    try {
	    	con = getConnection();
				
			Statement stmt = con.createStatement();
				
			//ResultSet rs =
			
			stmt.executeQuery(query);
			
			return OK;
			
	      } catch (Exception e) { 
	    	  //return e.getMessage();
	    	  
	    	  System.out.println(e.getMessage());
	    	  
	    	  return FAIL;
	    	  } finally { 
	    		  if (con != null) try{ con.close(); } 
	    		  catch(SQLException e) {
	    			  System.out.println(e.getMessage());
	    		  }
	      }
	}
	
	public String executeQuery(String query) {
		Connection con = null;
		
	    try {
	    	con = getConnection();
				
			Statement stmt = con.createStatement();
				
			ResultSet rs = stmt.executeQuery(query);		    
			      
	        String s = "";
	      
	        while (rs.next()) { s += rs.toString(); }
			
			return s;
			
	      } catch (Exception e) { 
	    	  //return e.getMessage();
	    	  
	    	  System.out.println(e.getMessage());
	    	  
	    	  return FAIL;
	    	  } finally { 
	    		  if (con != null) try{ con.close(); } 
	    		  catch(SQLException e) {
	    			  System.out.println(e.getMessage());
	    		  }
	      }
	}
	
	public JSONObject getSecret(JSONObject req) {
		Connection con = null;
		
		String name = (String) req.get("name");
		
		String pass = (String) req.get("pass");
		
		JSONObject resp = new JSONObject();
		
	    try {
	    	con = getConnection();
				
			Statement stmt = con.createStatement();
				
			String query = "select secret from accounts where name = '" 
							+ name + "' and pass = '" + pass + "';";
			
			ResultSet rs = stmt.executeQuery(query);		    
			      
	        int secret = 0;
	      
	        while (rs.next()) { secret = rs.getInt("Secret"); }
	   
	        resp.put("secret", secret);
			
			return resp;
			
	      } catch (Exception e) { 
	    	  //return e.getMessage();
	    	  
	    	  System.out.println(e.getMessage());
	    	  
	    	  resp.put("secret", "fail");
	    	  
	    	  return resp;
	    	  } finally { 
	    		  if (con != null) try{ con.close(); } 
	    		  catch(SQLException e) {
	    			  System.out.println(e.getMessage());
	    		  }
	      }
	}
	
	private static Connection getConnection() throws URISyntaxException, SQLException {
	    URI dbUri = new URI(System.getenv("DATABASE_URL"));

	    String username = dbUri.getUserInfo().split(":")[0];
	    String password = dbUri.getUserInfo().split(":")[1];
	    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();

	    return DriverManager.getConnection(dbUrl, username, password);
	}
}