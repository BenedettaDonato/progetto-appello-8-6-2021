public class FruitoreNotizie {

    private String idFruitoreNotizie;
    PubblicatoreImpl pubblicatore;

    public FruitoreNotizie(String idFruitoreNotizie, PubblicatoreImpl pubblicatore) {
        this.idFruitoreNotizie = idFruitoreNotizie;
        this.pubblicatore = pubblicatore;
    }

    public String getIdFruitoreNotizie() {
        return this.idFruitoreNotizie;
    }

    public void run(){
        pubblicatore.iscrizioneFruitore(this, TipoRivista.POLITICA);
        pubblicatore.iscrizioneFruitore(this, TipoRivista.ATTUALITA);
    }
    /*
    public static void main (String[] args){
        FruitoreNotizie fruitoreNotizie = new FruitoreNotizie("1", );
    }

     */
}
