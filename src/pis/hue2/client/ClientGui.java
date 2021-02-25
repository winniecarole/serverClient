package pis.hue2.client;

import pis.hue2.common.Instruction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
/**
 * @autor winnie tongle und carelle Djuffo
 * ClientGui ist unsere Grafische Benutzeroberfläche
 * @version java 15.0.1
 */
public class ClientGui extends JFrame implements  Ausgabe{
    LaunchClient client;
    Container c;
    File clientfile = new File("Fileclient");


    JTextField text1= new JTextField("127.0.0.1"),
            text2 =new JTextField("0000"),
            text3=new JTextField("6600"),text4;
    JTextArea jput=new JTextArea("filename"),jget=new JTextArea("filename"),
            jdelete=new JTextArea("filename"),client_File=new JTextArea("client File"),
            jmessage_from_server=new JTextArea("");

    Box vb=Box.createVerticalBox();

    Box vb1=Box.createVerticalBox();
    Box hb1 =Box.createHorizontalBox();
    Box hb2 =Box.createHorizontalBox();
    Box hb3=Box.createHorizontalBox();
    Box hbb =Box.createHorizontalBox();
    Box hcenter =Box.createHorizontalBox();
    Box vcenter=Box.createVerticalBox();
    Box vmessage_from_server=Box.createVerticalBox();


    JPanel panel =new JPanel(new BorderLayout());
    JPanel panel1 =new JPanel(new FlowLayout());
    JPanel panel2 =new JPanel(new FlowLayout());
    JPanel panel3 =new JPanel(new FlowLayout());

    JButton lst =new JButton("LST"),
            put=new JButton("PUT"),
            get=new JButton("GET"),
            del=new JButton("Delete"),
            sitzungAufBauen=new JButton("sitzungAufBauen"),
            sitzungAbBauen= new JButton("sitzungAbBauen");
    /**
     * Diese Methode gib der list unsere Server file als String züruck
     * @return es wird ein String zürück gegeben
     */

    public String clientfilemethode(){
        String clientfilel = "Liste von Client-File:\n";
        File [] file = clientfile.listFiles();
        for(int i=0; i<file.length; i++){
            clientfilel = clientfilel +  file[i].getName()+"\n";
        }
        return clientfilel;
    }

    /**
     * Konstructor
     */
    public ClientGui() {
        this.setTitle("Clientgui");
        this.setSize(690, 250);
        this.setVisible(true);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        set_button_enable(false);
        c = getContentPane();
        hbb.add(new JLabel("Datei vorhanden :"));
        hbb.add(lst);
        vb.add(hbb);

        hb1.add(new JLabel("Datei Hochladen :"));
        hb1.add(jput);
        hb1.add(put);
        vb.add(hb1);

        hb2.add(new JLabel("Datei Herhunterladen  :"));
        hb2.add(jget);
        hb2.add(get);
        vb.add(hb2);

        hb3.add(new JLabel("Datei loeschen:"));
        hb3.add(jdelete);
        hb3.add(del);
        vb.add(hb3);


        panel1.add(vb);

        panel.add(panel1, BorderLayout.WEST);

        hcenter.add(sitzungAbBauen);
        hcenter.add(sitzungAufBauen);
        panel2.add(hcenter);

        vcenter.add(client_File);
        vmessage_from_server.add(jmessage_from_server);
        panel2.add(vcenter);
        panel2.add(vmessage_from_server);

        panel.add(panel2, BorderLayout.CENTER);

        vb1.add(text1);
        vb1.add(text2);
        vb1.add(text3);
        panel3.add(vb1);
        panel.add(panel3, BorderLayout.EAST);

        panel1.setBorder(BorderFactory.createRaisedBevelBorder());
        panel2.setBorder(BorderFactory.createRaisedBevelBorder());
        panel3.setBorder(BorderFactory.createRaisedBevelBorder());
        c.add(panel);


        /** c.add(lst, new BorderLayout(10,10));
         c.add(put,new BorderLayout(15,20));
         c.add(get,new BorderLayout(20,25));
         c.add(del,new BorderLayout(30,35));*/
        lst.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                new SwingWorker<String, Void>() {
                    String filename;

                    @Override
                    protected String doInBackground() throws Exception {
                        try {
                            client.sendLst();
                            String message_from_server = client.datainput.readUTF();


                            if (message_from_server.equals(Instruction.ACK.toString() + "\n")) {
                                System.out.println("ack received from server");
                                client.sendAck();

                                if (client.datainput.readUTF().equals(Instruction.DAT.toString() + "\n")) {
                                    //  System.out.println("client received Dat from server");
                                    client.sendAck();
                                    filename = client.datainput.readUTF();
                                    //  System.out.println(filename);

                                }
                            }
                            zeigeNachricht("List: " + message_from_server);
                        } catch (Exception ex) {
                            // System.out.println(ex);
                        }

                        return filename;
                    }

                    @Override
                    protected void done() {
                        try {
                            String fileList = get();
                            System.out.println(fileList);
                            zeigeListe(fileList);
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }

                }.execute();

            }

        });
        put.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SwingWorker<String, Void>() {
                    String message_from_server="file not found";

                    @Override
                    protected String doInBackground() throws Exception {

                        try {
                            String file_name=jput.getText();
                            for(String name:client.listOfClientFiles) {
                                if(name.equals(file_name)==false){
                                    continue;
                                }
                                client.sendPut();
                                if (client.datainput.readUTF().equals(Instruction.ACK.toString() + "\n")) {
                                    //System.out.println("ack received from server");
                                    client.sendDat();

                                    client.dataoutput.writeUTF(file_name);
                                    message_from_server = client.datainput.readUTF();

                                    if (message_from_server.equals(Instruction.ACK.toString() + "\n")) {
                                        // System.out.println("another ack received from server");
                                        client.sendFile(jput.getText());
                                    }
                                    //zeigeNachricht("PUT: " + message_from_server);
                                }
                            }
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }

                        return message_from_server;
                    }

                    @Override
                    protected void done() {
                        try {
                            message_from_server = get();
                            zeigeNachricht("PUT: " + message_from_server);
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }

                }.execute();
            }
        });
        get.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new SwingWorker<String, Void>() {
                    String final_message_From_server;

                    @Override
                    protected String doInBackground() throws Exception {


                        // System.out.println("in client get");
                        try {
                            client.sendGet();
                            // System.out.println("get is send to server");
                            String message_From_server1 = client.datainput.readUTF();
                            //System.out.println(message_From_server1);
                            if (message_From_server1.equals(Instruction.ACK.toString() + "\n")) {

                                // System.out.println("server ack received");
                                client.sendAck();
                                client.dataoutput.writeUTF(jget.getText());
                                // System.out.println("ack send to server");
                                String message_From_server2 = client.datainput.readUTF();
                                //  System.out.println("server message: "+ message_From_server2);
                                if (message_From_server2.equals(Instruction.DAT.toString() + "\n")) {
                                    // System.out.println("client has receive dat from server");
                                    client.sendAck();

                                    // System.out.println(jget.getText());
                                    client.receiveFile(jget.getText());
                                    final_message_From_server=message_From_server1;


                                }
                                else if(message_From_server2.equals(Instruction.DND.toString() + "\n")) {
                                    final_message_From_server=message_From_server2;
                                }

                            }

                            //zeigeNachricht("Get:" + message_From_server1);

                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                        return final_message_From_server;
                    }

                    @Override
                    protected void done() {
                        try {
                            final_message_From_server = get();
                            zeigeNachricht("Get:" + final_message_From_server);
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }

                }.execute();
            }


        });
        del.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SwingWorker<String, Void>() {
                    String message_from_server;

                    @Override
                    protected String doInBackground() throws Exception {

                        try {

                            client.sendDel();
                            client.dataoutput.writeUTF(jdelete.getText());
                            client.dataoutput.flush();
                            //System.out.println(client.datainput.readUTF());
                            message_from_server = client.datainput.readUTF();
                            //  System.out.println("in delete" + message_from_server);
                            if (message_from_server.equals("ACK")) {

                                //  System.out.println("ACK");
                            }
                            //zeigeNachricht("Delete:"+ message_from_server);
                        } catch (Exception ex) {
                            System.out.println(e);
                        }
                        return message_from_server;
                    }

                    @Override
                    protected void done() {
                        try {
                            message_from_server = get();
                            zeigeNachricht("Delete:" + message_from_server);
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }
                }.execute();
            }

        });


        sitzungAbBauen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SwingWorker<String, Void>() {
                    String message_from_server;

                    @Override
                    protected String doInBackground() throws Exception {

                        client.sendDsc();

                        try {
                            message_from_server = client.datainput.readUTF();

                            if (message_from_server.equals(Instruction.DSC.toString() + "\n")) {
                                System.exit(0);
                                //set_button_enable(false);
                                //sitzungAufBauen.setEnabled(true);
                            }
                        } catch (Exception ex) {
                            System.out.println(e);
                        }
                        return message_from_server;
                    }

                    @Override
                    protected void done() {
                        try {
                            message_from_server = get();
                            zeigeNachricht("Desconnect:" + message_from_server);
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }
                }.execute();
            }
        });

        sitzungAufBauen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SwingWorker<String, Void>() {
                    String message_from_server="client could not connect";

                    @Override
                    protected String doInBackground() throws Exception {

                        try {

                            client = new LaunchClient(6600);
                            System.out.println("in con");
                            client.sendcon();
                            // sendcon();
                            message_from_server = client.datainput.readUTF();
                            System.out.println("after con");
                            if (message_from_server.equals(Instruction.ACK.toString() + "\n")) {


                                client.isConnect = true;
                                client_File.setEditable(false);//man kann nicht ändern
                                client_File.setText(clientfilemethode());
                                System.out.println("ack received");
                            } else {
                                System.out.println("DND");
                            }
                            //zeigeNachricht("Connect :"+message_from_server);
                        } catch (Exception ex) {
                            System.out.println(e);
                        }
                        if (client.isConnect == true) {
                            set_button_enable(true);
                            sitzungAufBauen.setEnabled(false);
                        }
                        return message_from_server;
                    }

                    @Override
                    protected void done() {
                        try {
                            message_from_server = get();
                            zeigeNachricht("Connect:" + message_from_server);
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }
                }.execute();
            }
        });


    }
    /*
    Diese Methode sperrt die button lst,put,get,del,sizungAbBauen
    * */
    public void set_button_enable(boolean enable){
        lst.setEnabled(enable);
        put.setEnabled(enable);
        get.setEnabled(enable);
        del.setEnabled(enable);
        sitzungAbBauen.setEnabled(enable);
    }
    public void zeigeNachricht(String message){
        jmessage_from_server.setText("Nachricht: "+ message);
        jmessage_from_server.setEditable(false);
    }
    public void zeigeListe(String filelists){
        JOptionPane.showMessageDialog(new JPanel(), filelists, "ServerFile", JOptionPane.INFORMATION_MESSAGE, null);
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() { //to communicate on only one thread per server
            @Override
            public void run() {
                ClientGui guiclient1 =new ClientGui();

            }
        });


    }


}