package Pool;

import java.util.ArrayList;
import PropertyHandler.PropertyHandler;

public enum Pool{

    INSTANCE;

    private PropertyHandler prop = new PropertyHandler("src/Pool/Cnf.properties");

    protected int NC = Integer.parseInt(prop.getProp("NC", "20"));

    protected int NMC = Integer.parseInt(prop.getProp("NMC", "100"));
    
    protected int CR = Integer.parseInt(prop.getProp("CR", "10"));

    protected int NCML = Integer.parseInt(prop.getProp("NCML", "20"));

    protected ArrayList<BDConx> connections = new ArrayList<BDConx>();

    private Pool(){
        for (int i = 0; i < NC; i++) {
            BDConx conn = new BDConx(i);
            connections.add(conn);
        }
    }
}
