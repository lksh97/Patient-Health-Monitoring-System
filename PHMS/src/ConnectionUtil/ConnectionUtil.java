/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConnectionUtil;

import java.sql.*;
import javax.swing.*;

/**
 *
 * @author AnmoL
 */
public class ConnectionUtil {
    
    Connection conn = null;
    public static Connection connectdb()
    {
        try
        {
            //Class.forName("com.mysql.jdbc.Driver");
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

