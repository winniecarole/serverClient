package pis.hue2.common;

/**
 * Implementiert die Schnittestelle Codec
 * */
public class Wuerfel implements Codec {
    /**
     * Ein String Objekt code_schluessel als Klassen invariante für das SchluesselWort
     * */
    private String code_schluessel="Scwarzwald";
    /**
     * Int Array "position" fuer das Angeben der Reihenfolge der Buchstaben  in dem Schluesselwort
     * */
    private int[] position;
    /**
     * Int Array "inverse_position" fuer die Bestimmung der Position der Buchstaben nach dem Alphabet von dem Schluesselwort
     * */
    private int[] inverse_position;
    /**
     * @param code_schluessel muss nicht null sein
     * */
    public Wuerfel(String code_schluessel){

        this.code_schluessel=code_schluessel;
    }
    /**
     * @param schluessel als Losungswort
     * @return int[] ein Array   Bestimmung der Position der Buchstaben nach dem Alphabet von dem Schluesselwort
      */


    public int[] findPosition(String schluessel) {
        this.code_schluessel = schluessel;
        this.code_schluessel=this.code_schluessel.toUpperCase();
        int length_Of_schuessel = this.code_schluessel.length();
        position = new int[length_Of_schuessel];
        inverse_position = new int[length_Of_schuessel];

        //Arranging the letters of the Array in ascending oder
        int count1 = 0;
        char min = this.code_schluessel.charAt(count1);
        while (count1 < length_Of_schuessel) {
            int j = 1;
            for (int i = 0; i < length_Of_schuessel; i++) {

                if (this.code_schluessel.charAt(i) < min | (count1 > i && this.code_schluessel.charAt(i) == min)) {
                    j++;
                }
            }
            position[count1] = j;
            count1++;
            if (count1 < length_Of_schuessel) {
                min = this.code_schluessel.charAt(count1);
            }
        }

        //Inverse Permutation
        for (int i = 0; i < inverse_position.length; i++) {
            inverse_position[position[i] - 1] = i;
        }
        return inverse_position;
    }

    public byte[] kodiere(byte[] bytes){
        StringBuilder geheimText_builder = new StringBuilder();
        String klartext=new String(bytes);

        inverse_position=findPosition(this.code_schluessel);

        //coding the Klatext using Stringbuilder

        for(int i=0;i<inverse_position.length;i++){
            for(int count =0; count<klartext.length();count++){
                if(count % code_schluessel.length()==inverse_position[i]){
                    geheimText_builder.append(klartext.charAt(count));
                }
            }
        }

        return geheimText_builder.toString().getBytes();

    }


    public String kodiere(String klartext) {
        StringBuilder geheimText_builder = new StringBuilder();

       inverse_position=findPosition(this.code_schluessel);

        //coding the Klatext using Stringbuilder

        for(int i=0;i<inverse_position.length;i++){
            for(int count =0; count<klartext.length();count++){
                if(count % code_schluessel.length()==inverse_position[i]){
                    geheimText_builder.append(klartext.charAt(count));
                }
            }
        }

        return geheimText_builder.toString();

    }


    public byte[] dekodiere(byte[] geheimeBytes){
        String geheimtext=new String(geheimeBytes);
        inverse_position=findPosition(this.code_schluessel);
        StringBuilder text_rebuilder = new StringBuilder(geheimtext); //Initialising an object of StringBulder containing the Geheimtext
        int pos=0;
        int length_of_key=code_schluessel.length();


        for(int i=0;i<inverse_position.length;i++) {
            int count = 0;

            while ((count) < geheimtext.length()) {
                if (inverse_position[i] == count % length_of_key) {

                    text_rebuilder.insert(count, geheimtext.charAt(pos));
                    text_rebuilder.deleteCharAt(count + 1); //Deleting the letters found the parameter geheimtext on by one
                    //could also use char Array //char [] test = new char[geheimtext.length()];
                    //test[count] = geheimtext.charAt(pos);
                    pos++;
                    count += length_of_key;
                } else {
                    count++;
                }

            }
        }

        return text_rebuilder.toString().getBytes();
    }

    public String dekodiere(String geheimtext){
        inverse_position=findPosition(this.code_schluessel);
        StringBuilder text_rebuilder = new StringBuilder(geheimtext); //Initialising an object of StringBulder containing the Geheimtext
        int pos=0;
        int length_of_key=code_schluessel.length();


    for(int i=0;i<inverse_position.length;i++) {
      int count = 0;

       while ((count) < geheimtext.length()) {
          if (inverse_position[i] == count % length_of_key) {

              text_rebuilder.insert(count, geheimtext.charAt(pos));
              text_rebuilder.deleteCharAt(count + 1); //Deleting the letters found the parameter geheimtext on by one
                                                       //could also use char Array //char [] test = new char[geheimtext.length()];
                                                        //test[count] = geheimtext.charAt(pos);
              pos++;
              count += length_of_key;
          } else {
              count++;
          }

      }
  }

        return text_rebuilder.toString();
    }



    @Override
    public String gibLosung() {
        return this.code_schluessel;
    }

    @Override
    public void setzeLosung(String schluessel) throws IllegalArgumentException {
        try {
            this.code_schluessel = schluessel;
        }catch (IllegalArgumentException e){
            System.out.println("Der Parameter muss ein String Objekt sein");
        }



    }

    public static void main(String[] args) {
        pis.hue2.common.Wuerfel wuerfel=new pis.hue2.common.Wuerfel("hello");
        String m="Hello";
        byte[] bytes=m.getBytes();
        //System.out.println(new String(wuerfel.dekodiere(bytes)));
        //System.out.println(wuerfel.dekodiere(m));

    }



}
