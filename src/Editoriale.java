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

    public void svuotaEditoriale() {
        contenuto = "";
    }

    public String stampaNotizia () {
        return contenuto;
    }


    public TipoRivista getTipoRivista() {
        return this.tipoRivista;
    }
}
