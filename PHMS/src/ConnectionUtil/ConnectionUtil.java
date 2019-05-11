package ConnectionUtil;

import java.sql.*;
import javax.swing.*;

/**
 *
 * @author Vaibhav Shukla
 */
public class ConnectionUtil {
    
    Connection conn = null;
    public static Connection connectdb()
    {
        try
        {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/health","root","kalyani12");
            return conn;
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, e);
            return null;
        }
    }
}

