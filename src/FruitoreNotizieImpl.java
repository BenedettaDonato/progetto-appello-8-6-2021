import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class FruitoreNotizieImpl extends UnicastRemoteObject implements FruitoreNotizie{

    private String idFruitoreNotizie;
    public FruitoreNotizieImpl(String idFruitoreNotizie) throws RemoteException {
        super();
        this.idFruitoreNotizie = idFruitoreNotizie;
    }

    public String getIdFruitoreNotizie() {
        return this.idFruitoreNotizie;
    }

    @Override
    public void notificaFruitori(String editoriale) throws RemoteException {
        System.out.println(editoriale);
        return;
    }


    public static void main (String[] args) throws RemoteException, NotBoundException {
        Registry reg = LocateRegistry.getRegistry(1099);
        Pubblicatore pubblicatore = (Pubblicatore) reg.lookup("Pubblicatore");

        FruitoreNotizieImpl fruitoreNotizie = new FruitoreNotizieImpl("1");
        pubblicatore.iscrizioneFruitore(fruitoreNotizie, TipoRivista.POLITICA);
        pubblicatore.iscrizioneFruitore(fruitoreNotizie, TipoRivista.ATTUALITA);

        pubblicatore.rimozioneFruitore(fruitoreNotizie, TipoRivista.POLITICA);
    }

}
