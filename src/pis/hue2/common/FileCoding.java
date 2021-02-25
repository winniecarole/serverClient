package pis.hue2.common;


import pis.hue1.Wuerfel;

import java.io.*;
import java.net.*;

public class FileCoding {

    public static void main(String[] args) throws IOException {
        Wuerfel wuerfel=new Wuerfel("THM");
      /*  Socket client=new Socket("local host",6600);

       PrintWriter output = new PrintWriter(client.getOutputStream(), true);

       BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        output.println("PUT");

        output.println(file_name); //Sendet Dateiname zum Server
        */
        System.out.println("in file");
        String file_name = "filecode.txt";
        //DataOutputStream data_output = new DataOutputStream(client.getOutputStream());
        File file = new File(file_name);
        file.createNewFile();
        String s ="hallo new file";
        System.out.println(file.exists());
        FileOutputStream file_output = new FileOutputStream(file);
        System.out.println("after input");
        byte[] message = new byte[(int) file.length()];
        message=s.getBytes();
        //file_intput.read(message, 0, message.length);
        for(byte b:message){
            System.out.print(b);
        }
        System.out.println();
        String m=new String(message);
        String code=wuerfel.kodiere(m);
        byte[] mb=code.getBytes();
        for(byte b:mb){
            System.out.print(b);
        }
        System.out.println();
        String decode= new String(mb);
        byte[]c =(wuerfel.dekodiere(decode)).getBytes();
        for(byte b:c){
            System.out.print(b);
        }

        System.out.println(m);
        file_output.write(message);
       // data_output.write(message);
    }
}
