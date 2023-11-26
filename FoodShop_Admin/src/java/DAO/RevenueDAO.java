/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DAO;

import DBUntils.DBUtils;
import static java.lang.System.out;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Revenue;

/**
 *
 * @author DELL
 */
public class RevenueDAO {
    public static List<Revenue> getMonth() throws Exception{
        List<Revenue> list = new ArrayList();
        Connection cn = DBUtils.makeConnection();
        String PreparedStatement;
        String sql = "select \n" +
"     MONTH(datebuy) as 'Month', \n" +
"	 Sum(total)as 'sum'\n" +
"from orders\n" +
"where Year(Getdate())=year(datebuy) and status =2\n" +
"group by MONTH(datebuy)  \n" +
"order by MONTH(datebuy) asc";
        PreparedStatement st = cn.prepareStatement(sql);

        try {
            ResultSet r = st.executeQuery();
            while (r.next()) {
               int i = r.getInt("Month") ; 
               double money = r.getDouble("sum");
               String month=null; 
               switch (i){
                   case 1: month="January"; break;
                   case 2: month="February"; break;
                   case 3: month="March"; break;
                   case 4: month="April"; break;
                   case 5: month="May"; break;
                   case 6: month="June"; break;
                   case 7: month="July"; break;
                   case 8: month="August"; break;
                   case 9: month="September"; break;
                   case 10: month="October"; break;
                   case 11: month="November"; break;
                   case 12: month="December"; break;
               }
               Revenue a= new Revenue(money,month);
               list.add(a);
            }

        } catch (Exception e) {
            out.println("Error: " + e);
        } finally {
            cn.close();
        }
        return list;
    }
    
}
