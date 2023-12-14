import Pool.PoolManager;

public class App{
    public static void main(String[] args) throws Exception{
        
        int numClientes = 250;

        HiloCliente[] clients = new HiloCliente[numClientes];

        for (int i = 0; i < numClientes; i++){
            HiloCliente client = new HiloCliente("BDD", i);
            clients[i] = client;
        }
        for (int i = 0; i < numClientes; i++){
            clients[i].start();
            Thread.sleep(500); //Tiempo de espera al iniciar cada hilo
        }

        if (PoolManager.conectados == numClientes){
            System.out.println("Todos los hilos fueron conectados."); 
            System.exit(0);
            }

    }
}