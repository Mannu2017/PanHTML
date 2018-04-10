package DBConnn;

import java.sql.Connection;
import java.sql.DriverManager;

import javax.swing.JOptionPane;

public class DbConn {
	static Connection conn;
	public static Connection conn(){
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn=DriverManager.getConnection("jdbc:sqlserver://192.168.84.90;user=sa;password=Karvy@123;database=pandotnet");
		} catch(Exception e){
	        JOptionPane.showMessageDialog(null, "Error:"+e, "PanfileUpload", JOptionPane.ERROR_MESSAGE);
	    }
		return conn;
	}
}
