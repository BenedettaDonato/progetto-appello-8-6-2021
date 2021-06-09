import java.util.Random;

public class ProduttoreNotizie extends Thread{

    private Random random = new Random();
    private static final String CARATTERI = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private int tempoProduzione;
    private PubblicatoreImpl pubblicatore;

    public ProduttoreNotizie(int tempoProduzione, PubblicatoreImpl pubblicatore) {
        this.tempoProduzione = tempoProduzione;
        this.pubblicatore = pubblicatore;
    }

    private String produciNotizia (){
        StringBuilder notiziaCreata = new StringBuilder();
        int lunghezza = random.nextInt(35) + 5;
        while (notiziaCreata.length() < lunghezza) {
            int index = (int) (random.nextFloat() * CARATTERI.length());
            notiziaCreata.append(CARATTERI.charAt(index));
        }
        return notiziaCreata.toString();
    }

    public void run() {
        while(true) {
            int idRivista = random.nextInt(TipoRivista.values().length);
            TipoRivista rivistaRandom = TipoRivista.values()[idRivista];    //Decide casualmente il tipo T della notizia da generare
            String notizia = produciNotizia();  //Produce in modo causale una notizia
            pubblicatore.riceviNotizie(rivistaRandom, notizia);
            try {
                Thread.sleep(tempoProduzione * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

