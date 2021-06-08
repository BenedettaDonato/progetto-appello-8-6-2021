import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;

public class PubblicatoreImpl extends UnicastRemoteObject implements Pubblicatore {
    private ArrayList<Editoriale> listaEditoriali = new ArrayList<>();
    private HashMap<FruitoreNotizie, TipoRivista> listaFruitori = new HashMap<>();

    public PubblicatoreImpl() throws RemoteException {
        super();
        for (TipoRivista tipoRivista: TipoRivista.values()) {
            Editoriale editoriale = new Editoriale(tipoRivista);
            listaEditoriali.add(editoriale);
        }
    }

    //Editoriali
    public synchronized void riceviNotizie (TipoRivista tipoRivista, String notizia){
        for(Editoriale editoriale : listaEditoriali) {
            if (tipoRivista.equals(editoriale.getTipoRivista())){
                System.out.println("Notizia ricevuta: " + notizia + " Del tipo: " + tipoRivista);
                editoriale.concatenaNotiza(notizia);
                break;
            }
            //questo non dovrebbe mai accadere
        }
    }
    @Override
    public synchronized void stampaEditoriali() throws RemoteException {
        for(Editoriale editoriale : listaEditoriali){
            System.out.println("\nTipoRivista: " + editoriale.getTipoRivista() + "\n" + editoriale.stampaNotizia());
            for(TipoRivista tipoRivista : this.listaFruitori.values()){
                for(FruitoreNotizie fruitoreNotizie : this.listaFruitori.keySet()){
                    if(tipoRivista == editoriale.getTipoRivista()){
                        fruitoreNotizie.notificaFruitori(editoriale.stampaNotizia());
                    }
                }

            }
            editoriale.svuotaEditoriale();
        }
    }

    //Fruitori notizie
    @Override
    public synchronized void iscrizioneFruitore (FruitoreNotizie fruitoreNotizie, TipoRivista tipoRivista) throws RemoteException {
        listaFruitori.put(fruitoreNotizie, tipoRivista);
        System.out.println("\nIl fruitore " + fruitoreNotizie.getIdFruitoreNotizie() + " è interessato alle notizie: " + tipoRivista + "\n");
    }

    @Override
    public synchronized void rimozioneFruitore (FruitoreNotizie fruitoreNotizie, TipoRivista tipoRivista) throws RemoteException {
        listaFruitori.remove(fruitoreNotizie, tipoRivista);
        System.out.println("\nIl fruitore " + fruitoreNotizie.getIdFruitoreNotizie() + " NON è più interessato alle notizie: " + tipoRivista + "\n");
    }

    public static void main(String[] args) throws InterruptedException, RemoteException {
        Pubblicatore pubblicatoreImpl = new PubblicatoreImpl();
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("Pubblicatore", pubblicatoreImpl);

        int numeroProduttoriNotizie = 5;
        ProduttoreNotizie arrayProduttoreNotizie[] = new ProduttoreNotizie[numeroProduttoriNotizie];
        for (int i = 0; i < numeroProduttoriNotizie ; ++i){
            ProduttoreNotizie produttoreNotizie = new ProduttoreNotizie(1, (PubblicatoreImpl) pubblicatoreImpl);      //Cambiando con float da problemi e non stampa
            produttoreNotizie.start();
            arrayProduttoreNotizie[i] = produttoreNotizie;
        }

        while (true) {
            Thread.sleep(5000);
            pubblicatoreImpl.stampaEditoriali();
        }

    }
}