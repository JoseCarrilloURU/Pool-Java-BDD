package Pool;

import java.sql.DriverManager;  
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;

import PropertyHandler.PropertyHandler;

public class BDConx{
    private static PropertyHandler prop = new PropertyHandler("src/Pool/Cnf.properties");
    private Connection conn;
    private String DBID; 
    public int connID;
    private ResultSet rs;
    public boolean isAvailable = true;

    protected BDConx(int connID){
        this.connID = connID;
    }

    protected boolean Connect(String DBID){
        String connString = prop.getProp(DBID, "Fallido");
        if (connString.equals("Fallido")){
            System.err.println("Conexión fallida. DBID Inválido: " + DBID);
            return false;
        }

        try{
            isAvailable = false;
            this.DBID = DBID;
            conn = DriverManager.getConnection(connString);
            return true;

        }catch (SQLException e){
            System.err.println(e);
            return false;
        }
    }

    protected boolean Disconnect(){
        try{
            conn.close();
            this.DBID = null;
            isAvailable = true;
            return true;

        }catch (SQLException e){
            System.err.println("Desconexión fallida.");
            return false;
        }
    }

    public boolean QueryFromString(String query){
        try{
            rs = conn.createStatement().executeQuery(query);
            return true;
        } catch (SQLException e){
            System.err.println("Query fallido: " + e);
            return false;
        }
    }
    public String getID(){
        return DBID;
    }
    public ResultSet getResultSet(){
        return rs;
    }

    
}