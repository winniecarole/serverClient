package pis.hue2.client;
/**

*Interface Ausgabe wird von der Klasse ClientGui implementiert
*Zeige all Nachrichten ,die der Server an den Client sendet
*@author Winnie Tongle und Carelle Djuffo
 */
public interface Ausgabe {
    /**
     *
     * @param message ist die Nachricht,die der Server an den Client zusendet
     *
     */


    public void zeigeNachricht(String message);

    /**
     *
     * @param filenames ist die Liste von Serverdatein
     */
    public void zeigeListe(String filenames);
}
