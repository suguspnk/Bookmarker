/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commons;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anton_000
 */
public class SQLiteDBHelper {

    public static Connection openConnection(String db) {
        Connection c = null;

        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+db);
        } catch (ClassNotFoundException | SQLException e) {
            Commons.printException(e);
        }
        return c;
    }

    public static Object query(String db, String query, Mapper mapper) throws SQLException, Exception {
        Commons.print("[SQLiteDBHelper]query("+db+", "+query+")");
        try (Connection c = openConnection(db)){
            try (Statement stmt = c.createStatement()) {
                return mapper.map(stmt.executeQuery(query));
            }
        }
    }
    
    /**
     * Executes the updateSql statement with the parameters params.
     * @param db
     * @param updateSql
     * @param params
     * @return the rowId of the inserted row
     * @throws java.sql.SQLException 
     */
    public static int executeUpdate(String db, String updateSql, Object ... params) throws SQLException{
        int auto_id = -1;
        try ( Connection c = openConnection(db);
                PreparedStatement stmt = c.prepareStatement(updateSql, Statement.RETURN_GENERATED_KEYS);
            ){
            /*
                Put the parameters
            */
            for(int i = 0; i < params.length; i++){
                stmt.setObject(i+1, params[i]);
            }
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                auto_id = rs.getInt(1);
            }
        }
        return auto_id;
    }
    
    public static void execute(String db, String sql) throws SQLException{
        Commons.print("[SQLiteDBHelper]execute("+db+", "+sql+")");
        try ( Connection c = openConnection(db);
                Statement stmt = c.createStatement();
            ) {
            stmt.execute(sql);
        }
    }

    public static boolean isTableExists(String db, String tableName) {
        try {
            boolean exists = (boolean) query(db, "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "';", 
                    (Mapper<Boolean>) (ResultSet rs) -> {
                        try {
                            if(rs.next()){
                                return true;
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(SQLiteDBHelper.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        return false;
                    });
            return exists;
        } catch (Exception ex) {
            Commons.printException(ex);
        } 
        return false;
    }

    public static Object[] executeBatch(String db, String sqlInsert, Object[][] params) throws SQLException {
        try(Connection c = openConnection(db);
                PreparedStatement stmt = c.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)){
            ResultSet rs;
            ArrayList<Integer> keys = new ArrayList<>();
            for(Object param[] : params){
                for(int i = 0; i < param.length; i++){
                    stmt.setObject(i+1, param[i]);
                }
                stmt.addBatch();
            }
            stmt.executeBatch();
            rs = stmt.getGeneratedKeys();
            
            while(rs.next()){
                keys.add(rs.getInt(1));
            }
            return keys.toArray();
        }
    }
    
    public interface Mapper<T>{
        public T map(ResultSet rs) throws Exception;
    }
}
