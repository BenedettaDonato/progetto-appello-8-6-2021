import java.util.Random;

public class ProduttoreNotizie {

    Random random = new Random();
    private static final String CARATTERI = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    private String produciNotizia (){
        StringBuilder notiziaCreata = new StringBuilder();
        int lunghezza = random.nextInt(35) + 5;
        while (notiziaCreata.length() < lunghezza) {
            int index = (int) (random.nextFloat() * CARATTERI.length());
            notiziaCreata.append(CARATTERI.charAt(index));
        }
        return notiziaCreata.toString();
    }
}

