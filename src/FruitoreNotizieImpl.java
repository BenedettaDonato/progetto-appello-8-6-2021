import java.rmi.ConnectException;
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
    public synchronized void riceviEditoriale(String editoriale, TipoRivista tipoEditoriale) throws RemoteException {
        System.out.println("Il fruitore " + getIdFruitoreNotizie() + " ha ricevuto \n" + tipoEditoriale + "\n" + editoriale);
        return;
    }


    public static void main (String[] args) throws RemoteException, NotBoundException, InterruptedException {
        Pubblicatore pubblicatore = null;
        while (true) {
            try{
                Registry reg = null;
                if(args != null && args.length >= 1){
                    reg = LocateRegistry.getRegistry(args[0],1099);     //Nel caso in cui FruitoreNotizie si trova su una macchina differente da Pubblicatore
                } else {
                    reg = LocateRegistry.getRegistry(1099);             //Nel caso in cui FruitoreNotizie si trova sulla stessa macchina di Pubblicatore
                }
                pubblicatore = (Pubblicatore) reg.lookup("Pubblicatore");
                break;
            }catch (ConnectException e) {
                int x = 3;
                System.err.println("Server NON trovato! Riprovo tra " + x + " secondi");
                Thread.sleep(x * 1000);
            }
        }

        if(pubblicatore != null) {
            FruitoreNotizieImpl fruitoreNotizie = new FruitoreNotizieImpl("1");
            pubblicatore.aggiungiInteresseFruitore(fruitoreNotizie, TipoRivista.POLITICA);
            pubblicatore.aggiungiInteresseFruitore(fruitoreNotizie, TipoRivista.ATTUALITA);

            pubblicatore.rimuoviInteresseFruitore(fruitoreNotizie, TipoRivista.POLITICA);

            FruitoreNotizieImpl fruitoreNotizie1 = new FruitoreNotizieImpl("2");
            pubblicatore.aggiungiInteresseFruitore(fruitoreNotizie1, TipoRivista.POLITICA);
        }else {
            //Non dovrebbe mai accadere!
            throw new NullPointerException("Pubblicatore non inizializzato, nonostante la connessione sia andata a buon fine");
        }
    }
}
