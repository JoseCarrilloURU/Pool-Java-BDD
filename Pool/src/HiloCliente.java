import Pool.PoolManager;

public class HiloCliente extends Thread{

    private String DBID;
    private int ID;
    private static String[] acciones = {"query", "release", "sleep"};

    public HiloCliente(String DBID, int ID){
        this.ID = ID;
        this.DBID = DBID;
    }

    @Override
    public void run(){
        PoolManager poolManager = new PoolManager(this.ID);

        poolManager.conn = PoolManager.getConnection(this.DBID, this.ID);
        while (true){
            int accrand = (int) (Math.random() * acciones.length);
            String accion = acciones[accrand]; 

            if (accion.equals("query")){
                poolManager.executeQuery("SELECT marca FROM autos");
            } 
            else if (accion.equals("release")){
                PoolManager.returnConnection(poolManager);
                PoolManager.elapsed = System.nanoTime()-PoolManager.start;
                System.out.println("Conexión #" + ID + " soltada. Tiempo transcurrido: " + PoolManager.elapsed/1000000 + " ms.");
                break;
            } 
            else if (accion.equals("sleep")){
                try {
                    Thread.sleep(200);
                    System.out.println("Pool Manager #" + ID + " descansa.\n");
                } catch (InterruptedException e){
                    System.err.println("Fallo ocurrido en el descanso del hilo.");
                }
            }
           
            try{
                Thread.sleep(250);
            } catch (InterruptedException e){
                System.err.println("Fallo ocurrido en el descanso del hilo.");
            }
        }
    }

}
