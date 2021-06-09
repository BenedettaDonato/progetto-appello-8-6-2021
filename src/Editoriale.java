public class Editoriale {

    private TipoRivista tipoRivista;
    private String contenuto;

    public Editoriale(TipoRivista tipoRivista) {
        this.tipoRivista = tipoRivista;
        this.contenuto = new String();
    }

    public void concatenaNotiza (String notizia) {
        contenuto += notizia + "\n";
    }

    public void svuotaEditoriale() {    //Cancella l'elenco delle notizie
        contenuto = new String();
    }

    public String getEditoriale() {
        return contenuto;
    }

    public TipoRivista getTipoRivista() {
        return this.tipoRivista;
    }
}
