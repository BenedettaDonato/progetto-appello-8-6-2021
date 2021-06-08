import java.util.ArrayList;
import java.util.HashMap;

public class PubblicatoreImpl {

    private ArrayList<Editoriale> listaEditoriali = new ArrayList<>();
    private HashMap<FruitoreNotizie, TipoRivista> listaFruitori = new HashMap<>();

    public PubblicatoreImpl(){
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

    public synchronized void stampaEditoriali() {
        for(Editoriale editoriale : listaEditoriali){
            System.out.println("\nTipoRivista: " + editoriale.getTipoRivista() + "\n" + editoriale.stampaNotizia());
            editoriale.svuotaEditoriale();
        }
    }

    //Fruitori notizie
    public void iscrizioneFruitore (FruitoreNotizie fruitoreNotizie, TipoRivista tipoRivista) {
        listaFruitori.put(fruitoreNotizie, tipoRivista);
        System.out.println("Il fruitore " + fruitoreNotizie.getIdFruitoreNotizie() + " è interessato alle notizie: " + tipoRivista);
    }

    public void rimozioneFruitore (FruitoreNotizie fruitoreNotizie, TipoRivista tipoRivista) {
        listaFruitori.remove(fruitoreNotizie, tipoRivista);
        System.out.println("Il fruitore " + fruitoreNotizie.getIdFruitoreNotizie() + " NON è più interessato alle notizie: " + tipoRivista);
    }

    public static void main(String[] args) throws InterruptedException {
        PubblicatoreImpl pubblicatore = new PubblicatoreImpl();
        int numeroProduttoriNotizie = 5;
        ProduttoreNotizie arrayProduttoreNotizie[] = new ProduttoreNotizie[numeroProduttoriNotizie];
        for (int i = 0; i < numeroProduttoriNotizie ; ++i){
            ProduttoreNotizie produttoreNotizie = new ProduttoreNotizie( 1, pubblicatore);      //Cambiando con float da problemi e non stampa
            produttoreNotizie.start();
            arrayProduttoreNotizie[i] = produttoreNotizie;
        }

        while (true) {
            Thread.sleep(5000);
            pubblicatore.stampaEditoriali();
        }

    }
}