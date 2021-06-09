import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FruitoreNotizie extends Remote {
    String getIdFruitoreNotizie() throws RemoteException;
    void riceviEditoriale(String editoriale, TipoRivista tipoEditoriale) throws RemoteException;
}
