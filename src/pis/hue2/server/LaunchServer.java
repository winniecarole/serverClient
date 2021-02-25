package pis.hue2.server;


import pis.hue2.common.*;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @autor winnie tongle und carelle Djuffo
 *Lauchserver ist der Server
 * @version java 15.0.1
 */


public class LaunchServer {
    /**
     * ServerSocket für die TCP Verbingung
     */
    private ServerSocket server;
    /**
     * Client Socket für die Verbindung zwischen Client und Server
     */
    private Socket client;
    /**
     * NumberOFClients die verbinden sind
     */

    private  static  int numberOfClients=0;
    /**
     * Liste von Serverdatein
     */
    private  static String[] listOfFilesName;
    /**
     * Datein von Server
     */
    private static File file_server=new File("/home/winnie/Dokumente/Semester3/PIS/pishue2/FileServer");
    /**
     *
     * @param port
     * Die Methode startet den Server
     *
     */

    public void startServer(int port) {

/**
 * listOfFilesName
 */
        listOfFilesName=file_server.list();
        for(String name:listOfFilesName){
            //System.out.println(name);
        }

/**
 * Verbindung mit TCP  ServerSocket wird aufgebaut
 */

        try {
            server = new ServerSocket(port);
            // System.out.println("Server connected...\nwaiting for client");


            while(true) {
/**
 * Verbindung mmit Client Socket wird aufgebaut
 */

                client = server.accept();
                ClientHandler newClient = new ClientHandler(client);
                newClient.start();
                // System.out.println(" client accepted");
                // System.out.println("number o clients ="+numberOfClients);




            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
     * Static Klasse ClientHandler
     */

    /**
     * Empfänger-thread für das Durchführen von Server Aktivitaäten
     *Wenn ein Client eine Nachricht empfängt, wird dies
     *in einem eigenen Empfänger-Thread
     *geschehen
     **/
    private static class ClientHandler  extends Thread{
        private Socket client;
        private PrintWriter output;
        private BufferedReader input;
        private boolean isalive=true;
        DataOutputStream dataoutput;
        DataInputStream datainput;


        /**
         * Der Konstruktor
         * @param client
         * client ist ein client Socket
         */

        public ClientHandler(Socket client) {

            this.client = client;
            try{
                datainput=new DataInputStream(client.getInputStream());
                dataoutput =new DataOutputStream(client.getOutputStream());
            }catch (Exception e){
                System.out.println(e);
            }
        }
        private File serverfile = new File("/home/winnie/Dokumente/Semester3/PIS/pishue2/FileServer");

        /**
         * Diese Methode Die list der Datei des Servers
         * @return es wird ein String züruckgegeben
         */

        public String serverfilemethode(){
            String serverfilel = "Liste von Server-File:\n";
            File [] file = serverfile.listFiles();
            for(int i=0; i<file.length; i++){
                serverfilel = serverfilel +  file[i].getName()+"\n";
            }
            return serverfilel;
        }

        /**
         * Startet eine Client Verbindung zum Server mittels Threads
         */

        public void run() {
            Thread thread=new Thread(new Runnable() {
                @Override
                public void run() {
                    //System.out.println("in run");
                    /**
                     * Protokol zum Zusenden der Nachrichten
                     */
                    try {
                        output = new PrintWriter(client.getOutputStream(), true);
                        input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        String nachricht = "";


                        outer:  while (isalive) {
                            // System.out.println("in while");
                            nachricht = datainput.readUTF();
                            // System.out.println(nachricht);
                            if (nachricht.equals(Instruction.CON.toString()+"\n")) {
                                //  System.out.println("in con");
                                //System.out.println("number of clients in con:"+numberOfClients);
                                if(numberOfClients<3) { //number of clients starts with 0
                                    sendAck();
                                    numberOfClients++;
                                }
                                else {
                                    sendDnd();
                                    client.close();
                                }
                            }
                            else if (nachricht.equals(Instruction.GET.toString()+"\n")) {
                                //   System.out.println("in server get");
                                sendAck();
                                // System.out.println("server has send ack");
                                if (datainput.readUTF().equals(Instruction.ACK.toString()+"\n")) {
                                    // System.out.println("ack from client received");
                                    String file_name = datainput.readUTF();
                                    //  System.out.println(file_name);
                                    listOfFilesName=file_server.list();
                                    for (String name : listOfFilesName) {
                                        //  System.out.println(name);
                                        if (name.equals(file_name)) {
                                            //   System.out.println("file present in server");
                                            sendDat();
                                            if (datainput.readUTF().equals(Instruction.ACK.toString()+"\n")) {
                                                sendFile(file_name);
                                                continue outer;
                                            }
                                        }

                                    }
                                    //System.out.println("file not present in server");
                                    sendDnd();
                                    //  System.out.println("dnd send to client");


                                }


                            } else if (nachricht.equals(Instruction.PUT.toString()+"\n")) {
                                System.out.println("in put");
                                sendAck();
                                System.out.println("ack send to client");
                                listOfFilesName=file_server.list();
                                System.out.println("number of files in server: "+listOfFilesName.length);

                                if (datainput.readUTF().equals(Instruction.DAT.toString()+"\n")) {
                                    System.out.println("dat received from client");
                                    String file_name = datainput.readUTF();
                                    for(String name:listOfFilesName){
                                        if(name.equals(file_name)){
                                            sendDnd();
                                            System.out.println("Server already has file");
                                            continue outer;
                                        }
                                    }
                                    sendAck();
                                    receiveFile(file_name);
                                }

                            } else if (nachricht.equals(Instruction.DSC.toString()+"\n")) {
                                System.out.println("before"+numberOfClients);
                                numberOfClients=numberOfClients-1;
                                System.out.println("after"+numberOfClients);
                                isalive = false;
                                sendDSC();
                                client.close();

                            }else if(nachricht.equals(Instruction.DEL.toString() +"\n")){
                                //System.out.println("in delete");
                                String file_name=datainput.readUTF();
                                if(deleteFile(file_name)==true) {
                                    sendAck();
                                    // System.out.println("file "+ file_name +" deleted");
                                }else{
                                    sendDnd();
                                    // System.out.println("file"+ file_name + "could not be deleted");
                                }

                            }else if(nachricht.equals(Instruction.LST.toString()+"\n")){
                                sendAck();
                                if (datainput.readUTF().equals(Instruction.ACK.toString()+"\n")){
                                    sendDat();
                                    dataoutput.writeUTF(serverfilemethode());
                                    dataoutput.flush();

                                }



                            }

                        }

                    } catch (Exception e) {
                        System.out.println(e);
                    }


                }
            });
            thread.start();



        }
        /**
         * diese Methode Schickt ein Datei an dem client
         * @param file_name
         */

        public void sendFile(String file_name){
            try {
                System.out.println("Sending file .......");
                Wuerfel wuerfel=new Wuerfel("javadateienabgeben");
                output = new PrintWriter(client.getOutputStream(), true);
                input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                OutputStream outputStream = client.getOutputStream();

                //dataoutput.writeUTF(file_name); //Sendet Dateiname zum Server
                File file = new File("/home/winnie/Dokumente/Semester3/PIS/pishue2/FileServer/"+ file_name);
                FileInputStream file_intput = new FileInputStream(file);
                byte[] message = new byte[5000]; //muss nicht mehr als 5000 sein sonst utf-8 entcoding problem
                int  bytes= file_intput.read(message, 0, message.length);

                String klartext=new String(message);
                //System.out.println(klartext);
                message=(wuerfel.kodiere(klartext)).getBytes();
                // System.out.println(new String(message));


                outputStream.write(message ,0 ,message.length);
                // System.out.println("sending file: "+ file_name);

                //  System.out.println("Transfering bytes: " +bytes);
                outputStream.flush();
                file_intput.close();
            }catch (Exception e){
                System.out.println(e);

            }
        }
        /**
         * Diese methode emphängt Datei von der client
         * @param file_name
         */
        public void receiveFile(String file_name){
            try {
                Wuerfel wuerfel=new Wuerfel("javadateienabgeben");
                InputStream inputStream = client.getInputStream();

                File file = new File("/home/winnie/Dokumente/Semester3/PIS/pishue2/FileServer/" + file_name);
                // File file2 = new File("C:\\Users\\Djuffo carelle\\pishue2\\FileServer\\crypted.txt");

                FileOutputStream file_ouput = new FileOutputStream(file);
                // FileOutputStream fileoutput = new FileOutputStream(file2);


                byte[] message_byte = new byte[5000];
                int bytes_read = inputStream.read(message_byte, 0, message_byte.length);
                //fileoutput.write(message_byte);

                // System.out.println("file content:" + new String(message_byte));
                String geheimfile = new String(message_byte);
                message_byte = wuerfel.dekodiere(message_byte);
                file_ouput.write(message_byte);
                // System.out.println("receiving file: "+ file_name);
                // System.out.println(new String(message_byte));
                file_ouput.flush();
                file_ouput.close();
            }catch (Exception e){
                System.out.println(e);
            }

        }
        /**
         * DIese Methode delete ein Datei der Sever
         * @param file_name
         * @return es wird true für Erfolg und false für nicht Erfolg zurüch gegeben
         */
        public boolean deleteFile(String file_name){

            File file = new File("/home/winnie/Dokumente/Semester3/PIS/pishue2/FileServer/" + file_name);
            System.out.println(file);
            boolean success=file.delete();
            //  System.out.println("File deleted? " + success);
            return success;
        }
        /**
         * Diese Methode schreibt eine Zeichenkette DND unter Vewendung einer modifizierten UTF-8-Kodeierung
         */
        public synchronized void sendAck(){
            try{
                dataoutput.writeUTF(Instruction.ACK.toString()+"\n");
            }catch (Exception e){
                System.out.println(e);
            }
        }
        /**
         * Diese Methode schreibt eine Zeichenkette DND unter Vewendung einer modifizierten UTF-8-Kodeierung
         */
        public void sendDnd(){
            try{
                dataoutput.writeUTF(Instruction.DND.toString() +"\n");
            }catch (Exception e){
                System.out.println(e);
            }
        }
        /**
         * Diese Methode schreibt eine Zeichenkette DAT unter Vewendung einer modifizierten UTF-8-Kodeierung
         */
        public void sendDat(){
            try{
                dataoutput.writeUTF(Instruction.DAT.toString()+"\n");
            }catch (Exception e){
                System.out.println(e);
            }
        }
        /**
         * Diese Methode schreibt eine Zeichenkette DSC unter Vewendung einer modifizierten UTF-8-Kodeierung
         */
        public void sendDSC(){
            try{
                dataoutput.writeUTF(Instruction.DSC.toString()+"\n");
            }catch (Exception e){
                System.out.println(e);
            }
        }

    }





    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try{
                    LaunchServer server=new LaunchServer();
                    server.startServer(6600);
                }catch (Exception e){
                    System.out.println(e);
                }
            }
        });
    }
}