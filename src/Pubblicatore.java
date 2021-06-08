import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Pubblicatore extends Remote {
    void iscrizioneFruitore(FruitoreNotizie fruitoreNotizie, TipoRivista tipoRivista) throws RemoteException;
    void rimozioneFruitore (FruitoreNotizie fruitoreNotizie, TipoRivista tipoRivista) throws RemoteException;
    void stampaEditoriali() throws RemoteException;

}
