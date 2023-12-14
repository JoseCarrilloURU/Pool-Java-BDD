package Pool;

import java.sql.ResultSet;

public class PoolManager{

    private static Pool pool = Pool.INSTANCE;
    public BDConx conn;
    public int ID;
    public static int conectados;
    public static long start, elapsed;

    public PoolManager(int ID){
        this.ID = ID;
    }

    public static synchronized BDConx getConnection(String DBID, int PMID){
        while (true){
            for (BDConx connection : pool.connections){
                if (connection.isAvailable){
                    conectados++;
                    System.out.println("Pool manager #" + PMID + " fue la conexion #"+ conectados +" del pool. Conexiones libres: " + (ConexionesLibres() - 1) + ". Tamaño máximo del Pool: " + pool.connections.size() + "\n");
                    PoolManager.EncogerPool(PMID);
                    connection.Connect(DBID);
                    return connection;
                }
            }
            ExtenderPool(PMID);
            continue;
        }
    }
    public static boolean returnConnection(PoolManager PM){
        if (PM.conn.Disconnect()){
            PoolManager.EncogerPool(PM.ID);
            System.out.println("Pool manager #" + PM.ID + " soltó la conexion. Conexiones libres: " + ConexionesLibres() + ". Tamaño máximo del Pool: " + pool.connections.size());
            PM.conn = null;
            return true;
        }
        return false;
    }

    private static synchronized boolean ExtenderPool(int PMID){
        if (pool.connections.size() < pool.NMC){
            for (int i = 0; i < pool.CR; i++){
                BDConx conn = new BDConx(i + pool.connections.size());
                pool.connections.add(conn);
            }
            System.out.println("\nPool manager #" + PMID + " extendió el pool. Conexiones libres: " + ConexionesLibres() + ". Nuevo tamaño máximo del Pool: " + pool.connections.size());
            return true;
        }
        return false;
    }

    
    private static synchronized boolean EncogerPool(int PMID){
        int conxLibres = ConexionesLibres();

        if (conxLibres > pool.NCML){
            int encoger = pool.CR;

            for (int i = 0; i < encoger; i++){
                for (BDConx conn : pool.connections){
                    if (conn.isAvailable){
                        pool.connections.remove(conn);
                        break;
                    }
                }
            }
            System.out.println("Pool manager #" + PMID + " encogió el pool. Conexiones libres: " + ConexionesLibres() + ". Nuevo tamaño máximo del Pool: " + pool.connections.size());
            return true;
        }
        return false;
    }
    private static synchronized int ConexionesLibres(){
        int freeConnections = 0;
        for (BDConx conn : pool.connections){
            if (conn.isAvailable){
                freeConnections++;
            }
        }
        return freeConnections;
    }
   
    public boolean executeQuery(String query){
        System.out.println("Pool manager #" + ID + " ejecutando query.");
        return conn.QueryFromString(query);
    }

    public ResultSet getResultSet(){
        return conn.getResultSet();
    }

}
