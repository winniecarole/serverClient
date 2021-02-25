package pis.hue2.server;

import javax.swing.*;
import java.awt.*;

/**
 *der  Konstruktor
 */
public class ServerGui extends JFrame{

    Container c;
    Box vb=Box.createVerticalBox();
    JPanel p=new JPanel();
    JButton server_startet =new JButton("server startet"),
     server_stoppen =new JButton("server stoppen"),
     ausgelieferte_datei =new JButton("ausgelieferte Datei"),
     dateiAnzeige =new JButton("Datei anzeige");

    ServerGui(){
        c=getContentPane();
    vb.add(server_startet);
    vb.add(server_stoppen);
    vb.add(ausgelieferte_datei);
    vb.add(dateiAnzeige);
    p.add(vb,BorderLayout.CENTER);
    c.add(p);

    }






    public static void main(String[] args) {
        ServerGui s=new ServerGui();
        s.setTitle("server Gui");

        s.setSize(250,150);
        s.setVisible(true);
        s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
/*import javafx.application.Application;
import javafx.stage.Stage;

public class servergui extends Application {

    public static void main(String[] args) {

    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}*/
