package pis.hue2.client;

import pis.hue2.common.*;

import javax.swing.plaf.LabelUI;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
/**
 * @autor winnie tongle und carelle Djuffo
 * Lauchclient  ist unsere client
 * @version java 15.0.1
 */

/**
 * Klasse LaunchCllient
 */

public class LaunchClient {
    /**
     * Socket client für die Erstellung der Verbindung
     */
    Socket client;
    /**
     * TCP port
     */
    private int port;

    protected boolean isConnect =false;
    /**
     * DataOutputStream
     */
    DataOutputStream dataoutput;
    /**
     * DataOutputStream
     */
    DataInputStream datainput;
    /**
     * Client Files
     */
    File client_file =new File("Fileclient");
    String []listOfClientFiles=client_file.list();

    /**
     * der Konstruktor
     * @param port
     */
    public LaunchClient(int port) {
        this.port = port;
        try {
            client = new Socket("127.0.0.1", this.port);
            datainput=new DataInputStream(client.getInputStream());
            dataoutput =new DataOutputStream(client.getOutputStream());
        }catch (Exception e){
            System.out.println(e);
        }

    }



    /**
     * diese Methode schickt Datei an dem Server
     * @param file_name
     */

    public void sendFile(String file_name){
        try {
            Wuerfel wuerfel=new Wuerfel("javadateienabgeben");

            OutputStream outputStream = client.getOutputStream();
            //Sendet Dateiname zum Server
            File file = new File("Fileclient\\"+ file_name);
            //File receive=new File("Fileclient\\crypted.txt");
            //FileOutputStream fileoutput=new FileOutputStream(receive);

            FileInputStream file_intput = new FileInputStream(file);
            byte[] message = new byte[ 5000]; //muss nicht mehr als 5000 sein sonst utf-8 entcoding problem
            int  bytes= file_intput.read(message, 0, message.length);//reads file into byte Array

            String klartext=new String(message);
            // System.out.println(klartext);
            message=(wuerfel.kodiere(klartext)).getBytes(StandardCharsets.UTF_8);
            // System.out.println(new String(message));
            // fileoutput.write(message);


            outputStream.write(message ,0 ,message.length);
            //  System.out.println("Transfering file: "+ file_name);

            //  System.out.println("Transfering bytes: " +bytes);
            outputStream.flush();
            file_intput.close();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    /**
     * Diese Methode empfangt Nachricht von dem Server
     * @param file_name
     */
    public void receiveFile(String file_name){
        try {

            Wuerfel wuerfel=new Wuerfel("javadateienabgeben");
            InputStream inputStream = client.getInputStream();
            File file = new File("Fileclient\\" + file_name);
            FileOutputStream file_ouput = new FileOutputStream(file);
            byte[] message_byte = new byte[5000];
            int bytes_read = inputStream.read(message_byte, 0, message_byte.length);
            // System.out.println("file content: " + new String(message_byte));
            String geheimfile = new String(message_byte);
            message_byte = (wuerfel.dekodiere(geheimfile)).getBytes();
            file_ouput.write(message_byte);//write file into byte
            file_ouput.flush();
            file_ouput.close();
        }catch (Exception e){
            System.out.println(e);
        }

    }
    /**
     * Diese Methode Delete ein Datei von der Server
     * @param file_name
     */
    public void deleteFile(String file_name){
        try {

            dataoutput.writeUTF(file_name);
        }catch (Exception e){
            System.out.println(e);
        }
    }
    public void listFile(){

        String fileFromServer;

        try {

            while ((fileFromServer = datainput.readUTF()) != null) {
                System.out.println(fileFromServer);


            }
        }catch (Exception e){
            System.out.println(e);
        }
    } /**
     * Diese Methode schreibt eine Zeichenkette Ack unter Vewendung einer modifizierten UTF-8-Kodeierung
     */
    public synchronized void sendAck(){
        try {
            dataoutput.writeUTF(Instruction.ACK.toString()+"\n");
            dataoutput.flush();
        }catch (Exception e){
            System.out.println(e);
        }
    } /**
     * Diese Methode schreibt eine Zeichenkette GET unter Vewendung einer modifizierten UTF-8-Kodeierung
     */
    public void sendGet(){
        try {
            dataoutput.writeUTF(Instruction.GET.toString()+"\n");
            dataoutput.flush();
        }catch (Exception e){
            System.out.println(e);
        }
        /**
         * Diese Methode schreibt eine Zeichenkette put unter Vewendung einer modifizierten UTF-8-Kodeierung
         */
    }
    public void sendPut(){
        try {
            dataoutput.writeUTF(Instruction.PUT.toString()+"\n");
            dataoutput.flush();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    /**
     * Diese Methode schreibt eine Zeichenkette DAT unter Vewendung einer modifizierten UTF-8-Kodeierung
     */
    public void sendDat(){
        try {
            dataoutput.writeUTF(Instruction.DAT.toString()+"\n");
            dataoutput.flush();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    /**
     * Diese Methode schreibt eine Zeichenkette DSC unter Vewendung einer modifizierten UTF-8-Kodeierung
     */
    public void sendDsc(){
        try {
            dataoutput.writeUTF(Instruction.DSC.toString()+"\n");
            dataoutput.flush();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    /**
     * Diese Methode schreibt eine Zeichenkette CON unter Vewendung einer modifizierten UTF-8-Kodeierung
     */
    public void sendcon(){
        try {
            dataoutput.writeUTF(Instruction.CON.toString()+"\n");
            dataoutput.flush();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    /**
     * Diese Methode schreibt eine Zeichenkette DEL unter Vewendung einer modifizierten UTF-8-Kodeierung
     */
    public void sendDel(){
        try{
            dataoutput.writeUTF(Instruction.DEL.toString()+"\n");
            dataoutput.flush();
        }catch (Exception e){
            System.out.println(e);
        }
    }
    /**
     * Diese Methode schreibt eine Zeichenkette LST unter Vewendung einer modifizierten UTF-8-Kodeierung
     */
    public void sendLst(){
        try{
            dataoutput.writeUTF(Instruction.LST.toString()+"\n");
            dataoutput.flush();
        }catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * Die main Methode
     * @param args
     */
    public static void main(String[] args) {
        Thread thread =new Thread(new Runnable() {
            @Override
            public void run() {
                LaunchClient client=new LaunchClient(6600);
            }
        });
        thread.start();
    }


}