import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

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
        System.out.println("Il fruitore " + getIdFruitoreNotizie() + " ha ricevuto \n" + tipoEditoriale + "\n\n" + editoriale);
        return;
    }


    public static void main (String[] args) throws RemoteException, NotBoundException, InterruptedException {
        Pubblicatore pubblicatore = null;
        while (true) {
            try{
                Registry reg = null;
                if(args != null && args.length >= 1){
                    reg = LocateRegistry.getRegistry(args[0],1099);     //Nel caso in cui FruitoreNotizie si trova su una macchina differente da Pubblicatore.
                } else {
                    reg = LocateRegistry.getRegistry(1099);             //Nel caso in cui FruitoreNotizie si trova sulla stessa macchina di Pubblicatore.
                }
                pubblicatore = (Pubblicatore) reg.lookup("Pubblicatore");
                break;
            }catch (ConnectException e) {
                int x = 3;
                System.err.println("Server NON trovato. Riprovo tra " + x + " secondi");
                Thread.sleep(x * 1000);
            }
        }

        if(pubblicatore != null) {
            Random random = new Random();
            int numeroRandomFruitori = random.nextInt(5) + 5;

            FruitoreNotizieImpl [] fruitoreNotizie = new FruitoreNotizieImpl[numeroRandomFruitori];

            for(int i = 0; i < numeroRandomFruitori; i++){              //Creazione dei Fruitori Notizie.
                int idFruitori = random.nextInt(999) + 1;        //Settando il Bound di nextInt a 999, diminuisce la probabilita che i fruitori creati abbiano lo stesso ID.
                fruitoreNotizie[i] = new FruitoreNotizieImpl("ID " + idFruitori);
                int idRivista = random.nextInt(TipoRivista.values().length);
                pubblicatore.aggiungiInteresseFruitore(fruitoreNotizie[i], TipoRivista.values()[idRivista]);
            }

            FruitoreNotizieImpl fruitoreNotizie1 = new FruitoreNotizieImpl("Test1");
            FruitoreNotizieImpl fruitoreNotizie2 = new FruitoreNotizieImpl("Test2");

            pubblicatore.rimuoviInteresseFruitore(fruitoreNotizie1, TipoRivista.SPORT);
            pubblicatore.rimuoviInteresseFruitore(fruitoreNotizie2, TipoRivista.ATTUALITA);

            pubblicatore.aggiungiInteresseFruitore(fruitoreNotizie2, TipoRivista.SPORT);
            pubblicatore.aggiungiInteresseFruitore(fruitoreNotizie2, TipoRivista.SPORT);

            pubblicatore.aggiungiInteresseFruitore(fruitoreNotizie1, TipoRivista.POLITICA);
            pubblicatore.rimuoviInteresseFruitore(fruitoreNotizie1, TipoRivista.POLITICA);
            pubblicatore.rimuoviInteresseFruitore(fruitoreNotizie1, TipoRivista.POLITICA);

        }else{
            //Non dovrebbe mai accadere.
            throw new NullPointerException("Pubblicatore non inizializzato, nonostante la connessione sia andata a buon fine.");
        }
    }
}
