package pis.hue1;

public interface Codec {

    /**
     * Codec ist die Schnittstelle zu sein verschiedenen Verschlüsselungsmethoden
     */

        /**
         * @param klartext ist der Input-String für die kodiere-Methode. Dieser Text wird mittels der Losung verschlüsselt.
         *                 Der Klartext ist ein Text bestehend aus Zeichen des ASCII-Zeichencodes. Die Länge ist auf die
         *                 Länge eines Strings begrenzt.
         * @return Der Rückgabewert ist vom Typ String und beinhaltet den kodierten Text.
         */
        String kodiere(String klartext);

        /**
         * @param bytes ist das Input-Byte-Array für die kodiere-Methode. Dieses Array wird mittels der Losung verschlüsselt.
         *                 Der Klartext ist ein Array bestehend aus Bytes. Die Länge ist auf die
         *                 Länge eines Arrays begrenzt.
         * @return Der Rückgabewert ist vom Typ byte[] und beinhaltet das verschlüsselte Array.
         */
        byte[] kodiere(byte[] bytes);

        /**
         * @param geheimtext ist der Input-String für die dekodiere-Methode. Diese Methode dekodiert den String geheimtext
         *                   mittels der Losung. Der Klartext ist ein Text bestehend aus Zeichen des ASCII-Zeichencodes.
         *                   Die Länge ist auf die Länge eines Strings begrenzt.
         * @return Der Rückgabewert ist vom Typ String und beinhaltet den dekodierten Text.
         */
        String dekodiere(String geheimtext);

        /**
         * @param geheimeBytes ist das Input-Byte-Array für die dekodiere-Methode. Diese Methode dekodiert das Array geheimeBytes
         *                   mittels der Losung. Der Klartext ist ein Array bestehend aus Bytes.
         *                   Die Länge ist auf die Länge eines Arrays begrenzt.
         * @return Der Rückgabewert ist vom Typ byte[] und beinhaltet das dekodierte Array.
         */
        byte[] dekodiere(byte[] geheimeBytes);

        /**
         * @return Die Methode gibLosung() gibt das Losungswort als String zurück.
         */
        String gibLosung();

        /**
         * @param schluessel ist der Input-String für die setzeLosung-Methode. Der String schluessel gibt der Klasse das
         *                   Losungswort an, dass zur Ent- und Verschlüsselung genutzt wird.
         *                   Die Kriterien für den Eingabe-String:
         *                   Der Eingabe-String darf nicht null sein.
         *                   Der Eingabe-String darf nicht länger als 23 Zeichen lang sein.
         *                   Der Eingabe-String muss aus kleinen oder großen Buchstaben des deutschen Alphabets bestehen.
         *                   Ob es kleine oder große Buchstaben sind, wird nicht beachtet.
         * @throws IllegalArgumentException wenn die Kriterien nicht eingehalten werden, wird ein Fehler geworfen
         */
        void setzeLosung(String schluessel) throws IllegalArgumentException; // bei ungeeignetem Schlüssel!
    }


