import java.util.ArrayList;

public class Pubblicatore {

    private ArrayList<Editoriale> listaEditoriali = new ArrayList<Editoriale>();

    public Pubblicatore (){
        for (TipoRivista tipoRivista: TipoRivista.values()) {
            Editoriale editoriale = new Editoriale(tipoRivista);
            listaEditoriali.add(editoriale);
        }
       
    }


}