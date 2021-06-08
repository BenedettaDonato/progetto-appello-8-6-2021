import java.util.ArrayList;

public class Pubblicatore {

    private ArrayList<Editoriale> listaEditoriali = new ArrayList<Editoriale>();

    public Pubblicatore (){
        for (TipoRivista tipoRivista: TipoRivista.values()) {
            Editoriale editoriale = new Editoriale(tipoRivista);
            listaEditoriali.add(editoriale);
        }
    }

    public synchronized void riceviNotizie (TipoRivista tipoRivista, String notizia){
        for(Editoriale editoriale : listaEditoriali) {
            if (tipoRivista.equals(editoriale.getTipoRivista())){
                System.out.println("Notizia ricevuta: " + notizia + " Del tipo: " + tipoRivista);
                editoriale.concatenaNotiza(notizia);
                break;
            }
        }
    }

    public static void main(String[] args) {
        Pubblicatore pubblicatore = new Pubblicatore();
        int numeroProduttoriNotizie = 5;
        ProduttoreNotizie arrayProduttoreNotizie[] = new ProduttoreNotizie[numeroProduttoriNotizie];
        for (int i = 0; i < numeroProduttoriNotizie ; ++i){
            ProduttoreNotizie produttoreNotizie = new ProduttoreNotizie(1, pubblicatore);
            produttoreNotizie.start();
            arrayProduttoreNotizie[i] = produttoreNotizie;
        }
    }
}