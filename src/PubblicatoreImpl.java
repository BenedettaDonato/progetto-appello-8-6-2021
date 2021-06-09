import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class PubblicatoreImpl extends UnicastRemoteObject implements Pubblicatore {
    private ArrayList<Editoriale> listaEditoriali = new ArrayList<>();
    private HashMap<FruitoreNotizie, HashSet<TipoRivista>> listaFruitori = new HashMap<>();     //Abbiamo scelto di utilizzare un HashSet perche non accetta duplicati rispetto ad un arrayList.
                                                                                                //L'ordine in cui si estraggono gli elementi e' CASUALE come per l'ArrayList ma l'ordine non ci interessa.

    private ProduttoreNotizie arrayProduttoreNotizie[];

    public PubblicatoreImpl(int numeroProduttoriNotizie) throws RemoteException, UnknownHostException {
        super();
        Random tempoRandomCreazioneNotizie = new Random();
        InetAddress inetAddress = InetAddress.getLocalHost();
        System.out.println("IP Pubblicatore: " + inetAddress.getHostAddress());       //Stampa indirizzo IP su consolle, nel caso in cui si voglia eseguire il programma su due macchine distinte.

        for (TipoRivista tipoRivista: TipoRivista.values()) {
            Editoriale editoriale = new Editoriale(tipoRivista);
            listaEditoriali.add(editoriale);
        }

        arrayProduttoreNotizie = new ProduttoreNotizie[numeroProduttoriNotizie];
        for (int i = 0; i < numeroProduttoriNotizie ; ++i){
            ProduttoreNotizie produttoreNotizie = new ProduttoreNotizie(tempoRandomCreazioneNotizie.nextInt(3) + 1, this);
            produttoreNotizie.start();
            arrayProduttoreNotizie[i] = produttoreNotizie;
        }
    }

    //L'editoriale e' accessibile INDIRETTAMENTE da ProduttoreNotizie dato che l'editoriale e' di proprietà di Pubblicatore.
    public synchronized void riceviNotizie (TipoRivista tipoRivista, String notizia){
        for(Editoriale editoriale : listaEditoriali) {
            if (tipoRivista.equals(editoriale.getTipoRivista())){
                System.out.println("Notizia ricevuta: " + notizia + " Del tipo: " + tipoRivista);
                editoriale.concatenaNotiza(notizia); //concatena la notizia al contenuto dell’editoriale di tipo considerato.
                break;
            }
        }
    }

    @Override
    public synchronized void trasmettiEditoriali() throws RemoteException {
        for(Editoriale editoriale : listaEditoriali){
            System.out.println("\nTipoRivista: " + editoriale.getTipoRivista() + "\n" + editoriale.getEditoriale());
            String notizieCopia = (editoriale.getEditoriale());                 //Facciamo una copia dell'editoriale (notizieCopia), in modo da non perdere il valore dell'editoriale quando lo cancelliamo.
            editoriale.svuotaEditoriale();                                      //Dopo aver fatto la copia, svuotiamo l'editoriale; le notizie le abbiamo salvate nella variabile notizieCopia, quindi non vanno perse.

            for (Map.Entry<FruitoreNotizie, HashSet<TipoRivista>> entry : listaFruitori.entrySet()) {
                FruitoreNotizie fruitoreNotizie = entry.getKey();
                HashSet<TipoRivista> interessiFruitore = entry.getValue();
                if(interessiFruitore.contains(editoriale.getTipoRivista())){    //Verifichiamo se il fruitore e' interessato all'editoriale.
                    try{
                        fruitoreNotizie.riceviEditoriale(notizieCopia, editoriale.getTipoRivista());
                    }catch (ConnectException e) {
                        System.err.println("Il Fruitore non e' piu raggiungibile");
                    }
                }
            }
        }
    }

    @Override
    public synchronized void aggiungiInteresseFruitore(FruitoreNotizie fruitoreNotizie, TipoRivista tipoRivista) throws RemoteException {
        //Caso 0: Non esiste l'HashSet collegata al fruitore di notizie considerato  (Non e' presente la chiave nell'HashMap).
        if(!listaFruitori.containsKey(fruitoreNotizie)) {
            HashSet<TipoRivista> interessiFruitore = new HashSet<>();
            interessiFruitore.add(tipoRivista);
            listaFruitori.put(fruitoreNotizie, interessiFruitore);
        }else{
            //Caso 1: Esiste l'HashSet collegata al fruitore di notizie considerato  (E' presente la chiave nell'HashMap).
            listaFruitori.get(fruitoreNotizie).add(tipoRivista);    //Non dobbiamo fare il controllo se il fruitore dice di essere interessato piu di una volta ad un stesso TipoRivista,
                                                                    //proprio perche l'HashSet non consente di inserire due valori uguali.
        }
        System.out.println("\nIl fruitore " + fruitoreNotizie.getIdFruitoreNotizie() + " e' interessato alle notizie: " + tipoRivista + "\n");
    }

    @Override
    public synchronized void rimuoviInteresseFruitore(FruitoreNotizie fruitoreNotizie, TipoRivista tipoRivista) throws RemoteException {
        //Caso 0: Verifico se il fruitore e' iscritto ad almeno un TipoRivista
        if(listaFruitori.containsKey(fruitoreNotizie)){

            //Caso 1: Rimuoviamo l'interesse del Fruitore
            HashSet<TipoRivista> interessiFruitore = listaFruitori.get(fruitoreNotizie);
            boolean interesseRimosso = interessiFruitore.remove(tipoRivista);

            //Caso 2: Verifichiamo se il fruitore era iscritto all'interesse
            if(!interesseRimosso){
                System.err.println("Il Fruitore: " + fruitoreNotizie.getIdFruitoreNotizie() + " NON era iscritto all' interesse");
            }else if(interessiFruitore.size() == 0) {

                //Caso 3: Controlliamo se il fruitore e' iscritto ad altri interessi. In caso contrario lo rimuoviamo.
                listaFruitori.remove(fruitoreNotizie);
            }
         }else{
            System.err.println("Il fruitore: " + fruitoreNotizie.getIdFruitoreNotizie() + " NON e' iscritto");
        }
        System.out.println("\nIl fruitore " + fruitoreNotizie.getIdFruitoreNotizie() + " NON e' piu interessato alle notizie: " + tipoRivista + "\n");
    }

    public static void main(String[] args) throws InterruptedException, RemoteException, UnknownHostException {
        Pubblicatore pubblicatoreImpl = new PubblicatoreImpl(5);
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("Pubblicatore", pubblicatoreImpl);

        while (true) {
            Thread.sleep(5000);
            pubblicatoreImpl.trasmettiEditoriali();
        }
    }
}