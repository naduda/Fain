package com.practicaljava.lesson22;
// Class EmployeeList displays Employees from the table EMP 
// using JDBC drivers of type 4

import java.sql.*;

class EmployeeList {

  public static void main(String argv[]) {
   Connection conn=null;
   Statement stmt=null;
   ResultSet rs=null;
   

   try {
    conn = DriverManager.getConnection("jdbc:postgresql://93.183.238.170:5434/pilot", "postgres", "12345678"); 
    String sqlQuery = "SELECT * from d_valti order by dt desc limit 10"; 
//    String sqlQuery = "select * from d_valti where dt > '2014-06-20 18:30' order by dt desc";
    stmt = conn.createStatement(); 
    rs = stmt.executeQuery(sqlQuery);  

    while (rs.next()) { 
    	System.out.println(rs.getString(1) + "   -   " + rs.getString(2) + "   -   " + rs.getString(3) + 
    			"   -   " + rs.getString(4) + "   -   " + rs.getString(5));
    }

   } catch( SQLException se ) {
      System.out.println ("SQLError: " + se.getMessage ()
           + " code: " + se.getErrorCode ());

   } catch( Exception e ) {
      System.out.println(e.getMessage()); 
      e.printStackTrace(); 
   } finally{
       try{
	   rs.close();     
	   stmt.close(); 
	   conn.close();  
       } catch(Exception e){
           e.printStackTrace();
       } 
   }
}
}
