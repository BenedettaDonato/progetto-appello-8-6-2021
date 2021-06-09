import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Pubblicatore extends Remote {
    void aggiungiInteresseFruitore(FruitoreNotizie fruitoreNotizie, TipoRivista tipoRivista) throws RemoteException;
    void rimuoviInteresseFruitore(FruitoreNotizie fruitoreNotizie, TipoRivista tipoRivista) throws RemoteException;
    void trasmettiEditoriali() throws RemoteException;
}
