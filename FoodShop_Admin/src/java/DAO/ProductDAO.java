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
import model.Product;
import model.ProductTop;

/**
 *
 * @author DELL
 */
public class ProductDAO {

    public static List<Product> listProduct() throws Exception {
        List<Product> a = new ArrayList();
        Connection cn = DBUtils.makeConnection();
        String PreparedStatement;
        String sql = "select id, name, description, price, imagePath, categoryID, status from items where status=1 and deleted=1";
        PreparedStatement st = cn.prepareStatement(sql);

        try {
            ResultSet r = st.executeQuery();
            while (r.next()) {
                Product item = new Product(r.getInt("id"), r.getString("name"), r.getString("description"), r.getString("imagePath"), r.getDouble("price"), r.getInt("categoryID"), r.getInt("status"));
                a.add(item);
            }

        } catch (Exception e) {
            out.println("Error: " + e);
        } finally {
            cn.close();
        }
        return a;
    }

    public static boolean updateProduct(int id, String name, String imagepath, double price, String des, int status, int categoryID) throws Exception {
        List<Product> a = new ArrayList();
        Connection cn = DBUtils.makeConnection();
        String PreparedStatement;
        String sql = "UPDATE items\n"
                + " SET name=?, imagePath=?, price=?, description=?,status=?, categoryID=?\n"
                + " WHERE id = ?";
        PreparedStatement st = cn.prepareStatement(sql);
        st.setString(1, name);
        st.setString(2, imagepath);
        st.setDouble(3, price);
        st.setString(4, des);
        st.setInt(5, status);
        st.setInt(6, categoryID);
        st.setInt(7, id);
        int row = 0;
        try {
            row = st.executeUpdate();

        } catch (Exception e) {
            out.println("Error: " + e);
        } finally {
            cn.close();
        }
        return row > 0;
    }

    public static Product getProduct(int sid) throws Exception {
        Product a = null;
        Connection cn = DBUtils.makeConnection();
        String PreparedStatement;
        String sql = "select id, name, description, price, imagePath, categoryID, status\n"
                + "from items\n"
                + "where id=?";
        PreparedStatement st = cn.prepareStatement(sql);
        st.setInt(1, sid);
        try {
            ResultSet r = st.executeQuery();
            while (r.next()) {
                a = new Product(r.getInt("id"), r.getString("name"), r.getString("description"), r.getString("imagePath"), r.getDouble("price"), r.getInt("categoryID"), r.getInt("status"));
            }

        } catch (Exception e) {
            out.println("Error: " + e);
        } finally {
            cn.close();
        }
        return a;
    }

    public static List<Product> listOutStock() throws Exception {
        List<Product> a = new ArrayList();
        Connection cn = DBUtils.makeConnection();
        String PreparedStatement;
        String sql = "select id, name, description, price, imagePath, categoryID, status from items where status=0 and deleted=1";
        PreparedStatement st = cn.prepareStatement(sql);

        try {
            ResultSet r = st.executeQuery();
            while (r.next()) {
                Product item = new Product(r.getInt("id"), r.getString("name"), r.getString("description"), r.getString("imagePath"), r.getDouble("price"), r.getInt("categoryID"), r.getInt("status"));
                a.add(item);
            }

        } catch (Exception e) {
            out.println("Error: " + e);
        } finally {
            cn.close();
        }
        return a;
    }

    public static void toOutStock(int id) throws Exception {

        Connection cn = DBUtils.makeConnection();
        String PreparedStatement;
        String sql = "Update items \n"
                + "set status=0\n"
                + "where id=? ";
        PreparedStatement st = cn.prepareStatement(sql);

        st.setInt(1, id);

        try {
            st.executeUpdate();

        } catch (Exception e) {
            out.println("Error: " + e);
        } finally {
            cn.close();
        }
    }

    public static void toStock(int id) throws Exception {

        Connection cn = DBUtils.makeConnection();
        String PreparedStatement;
        String sql = "Update items \n"
                + "set status=1\n"
                + "where id=? ";
        PreparedStatement st = cn.prepareStatement(sql);

        st.setInt(1, id);

        try {
            st.executeUpdate();

        } catch (Exception e) {
            out.println("Error: " + e);
        } finally {
            cn.close();
        }
    }

    public static List<ProductTop> getTopProduct() throws Exception {
        List<ProductTop> a = new ArrayList();
        Connection cn = DBUtils.makeConnection();
        String PreparedStatement;
        String sql = "select top(5) *\n"
                + "from (select sum(op.quantity)as 'quantity', i.name as 'productname', i.price   from orderProperties as op\n"
                + "                  left join items as i \n"
                + "                	    on i.id=op.itemID \n"
                + "                		left join orders as o \n"
                + "                   	on o.orderID=op.orderID\n"
                + "                		where o.status=2 and i.categoryID != 1005\n"
                + "                		 group by  i.name, i.price) as p\n"
                + "                Order by p.quantity DESC";
        PreparedStatement st = cn.prepareStatement(sql);

        try {
            ResultSet r = st.executeQuery();
            while (r.next()) {
                ProductTop p = new ProductTop(r.getString("productname"), r.getInt("quantity"), r.getDouble("price"));
                a.add(p);
            }

        } catch (Exception e) {
            out.println("Error: " + e);
        } finally {
            cn.close();
        }
        return a;
    }

    public static void insertProduct(Product a) throws Exception {

        Connection cn = DBUtils.makeConnection();
        String PreparedStatement;
        String sql = "insert into items(name, description, price, imagePath, categoryID, status, deleted) \n"
                + "values (?,?,?,?,?,1,1)";
        PreparedStatement st = cn.prepareStatement(sql);

        st.setString(1, a.getName());
        st.setString(2, a.getDescription());
        st.setDouble(3, a.getPrice());
        st.setString(4, a.getImagepath());
        st.setInt(5, a.getIdCategory());

        try {
            st.executeUpdate();

        } catch (Exception e) {
            out.println("Error: " + e);
        } finally {
            cn.close();
        }
    }

    public static List<Product> searchProduct(String search) throws Exception {
        List<Product> a = new ArrayList();
        Connection cn = DBUtils.makeConnection();
        String sql = "select items.id, items.name, description, price, imagePath, categoryID, status \n"
                + "from items \n"
                + "left join categorys \n"
                + "on categorys.id=items.categoryID \n"
                + "where ((items.name like '%" + search + "%') or (categorys.name like '" + search + "')) and deleted=1 and status=1";
        PreparedStatement st = cn.prepareStatement(sql);

        try {
            ResultSet r = st.executeQuery();
            while (r.next()) {
                Product item = new Product(r.getInt("id"), r.getString("name"), r.getString("description"), r.getString("imagePath"), r.getDouble("price"), r.getInt("categoryID"), r.getInt("status"));
                a.add(item);
            }

        } catch (Exception e) {
            out.println("Error: " + e);
        } finally {
            cn.close();
        }
        return a;

    }

    public static List<Product> searchProductOut(String search) throws Exception {
        List<Product> a = new ArrayList();
        Connection cn = DBUtils.makeConnection();
        String sql = "select items.id, items.name, description, price, imagePath, categoryID, status \n"
                + "from items \n"
                + "left join categorys \n"
                + "on categorys.id=items.categoryID \n"
                + "where ((items.name like '%" + search + "%') or (categorys.name like '" + search + "')) and deleted=1 and status=0";
        PreparedStatement st = cn.prepareStatement(sql);

        try {
            ResultSet r = st.executeQuery();
            while (r.next()) {
                Product item = new Product(r.getInt("id"), r.getString("name"), r.getString("description"), r.getString("imagePath"), r.getDouble("price"), r.getInt("categoryID"), r.getInt("status"));
                a.add(item);
            }

        } catch (Exception e) {
            out.println("Error: " + e);
        } finally {
            cn.close();
        }
        return a;

    }

    public static void deleteProduct(int id) throws Exception {

        Connection cn = DBUtils.makeConnection();
        String PreparedStatement;
        String sql = "Update items \n"
                + "set deleted=0\n"
                + "where id=? ";
        PreparedStatement st = cn.prepareStatement(sql);
        st.setInt(1, id);

        try {
            st.executeUpdate();

        } catch (Exception e) {
            out.println("Error: " + e);
        } finally {
            cn.close();
        }
    }
}
