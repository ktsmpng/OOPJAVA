package ie.gmit.dip;

import java.io.*;
public class PortaCipher implements Serializable{
    private char[] key;
    private static final int MINIMUM_KEY_LENGTH = 5;
    private String fileName;
    private String file;
    private char[][] cipher = {
        {' ','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'},
        {'A','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','A','B','C','D','E','F','G','H','I','J','K','L','M'},
        {'B','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','A','B','C','D','E','F','G','H','I','J','K','L','M'},
		{'C','O','P','Q','R','S','T','U','V','W','X','Y','Z','N','M','A','B','C','D','E','F','G','H','I','J','K','L'},
		{'D','O','P','Q','R','S','T','U','V','W','X','Y','Z','N','M','A','B','C','D','E','F','G','H','I','J','K','L'},
		{'E','P','Q','R','S','T','U','V','W','X','Y','Z','N','O','L','M','A','B','C','D','E','F','G','H','I','J','K'},
		{'F','P','Q','R','S','T','U','V','W','X','Y','Z','N','O','L','M','A','B','C','D','E','F','G','H','I','J','K'},
		{'G','Q','R','S','T','U','V','W','X','Y','Z','N','O','P','K','L','M','A','B','C','D','E','F','G','H','I','J'},
		{'H','Q','R','S','T','U','V','W','X','Y','Z','N','O','P','K','L','M','A','B','C','D','E','F','G','H','I','J'},
		{'I','R','S','T','U','V','W','X','Y','Z','N','O','P','Q','J','K','L','M','A','B','C','D','E','F','G','H','I'},
		{'J','R','S','T','U','V','W','X','Y','Z','N','O','P','Q','J','K','L','M','A','B','C','D','E','F','G','H','I'},
        {'K','S','T','U','V','W','X','Y','Z','N','O','P','Q','R','I','J','K','L','M','A','B','C','D','E','F','G','H'},
		{'L','S','T','U','V','W','X','Y','Z','N','O','P','Q','R','I','J','K','L','M','A','B','C','D','E','F','G','H'},
		{'M','T','U','V','W','X','Y','Z','N','O','P','Q','R','S','H','I','J','K','L','M','A','B','C','D','E','F','G'},
		{'N','T','U','V','W','X','Y','Z','N','O','P','Q','R','S','H','I','J','K','L','M','A','B','C','D','E','F','G'},
		{'O','U','V','W','X','Y','Z','N','O','P','Q','R','S','T','G','H','I','J','K','L','M','A','B','C','D','E','F'},
		{'P','U','V','W','X','Y','Z','N','O','P','Q','R','S','T','G','H','I','J','K','L','M','A','B','C','D','E','F'},
		{'Q','V','W','X','Y','Z','N','O','P','Q','R','S','T','U','F','G','H','I','J','K','L','M','A','B','C','D','E'},
		{'R','V','W','X','Y','Z','N','O','P','Q','R','S','T','U','F','G','H','I','J','K','L','M','A','B','C','D','E'},
		{'S','W','X','Y','Z','N','O','P','Q','R','S','T','U','V','E','F','G','H','I','J','K','L','M','A','B','C','D'},
		{'T','W','X','Y','Z','N','O','P','Q','R','S','T','U','V','E','F','G','H','I','J','K','L','M','A','B','C','D'},
		{'U','X','Y','Z','N','O','P','Q','R','S','T','U','V','W','D','E','F','G','H','I','J','K','L','M','A','B','C'},
		{'V','X','Y','Z','N','O','P','Q','R','S','T','U','V','W','D','E','F','G','H','I','J','K','L','M','A','B','C'},
		{'W','Y','Z','N','O','P','Q','R','S','T','U','V','W','X','C','D','E','F','G','H','I','J','K','L','M','A','B'},
		{'Y','Y','Z','N','O','P','Q','R','S','T','U','V','W','X','C','D','E','F','G','H','I','J','K','L','M','A','B'},
		{'X','Z','N','O','P','Q','R','S','T','U','V','W','X','Y','B','C','D','E','F','G','H','I','J','K','L','M','A'},
		{'Z','Z','N','O','P','Q','R','S','T','U','V','W','X','Y','B','C','D','E','F','G','H','I','J','K','L','M','A'},
    };
    
    public PortaCipher(String key) throws Exception{
        setKey(key);
    }
    
    public PortaCipher(String key, String fileName) throws Exception{
        setKey(key);
        setFile(fileName);
    }
    
    public PortaCipher(String key, String fileName, String contents) throws Exception{
        setKey(key);
        setFile(fileName);
        setFileContents(contents);
    }
    
    public void setKey(String key) throws Exception{
        if (key == null || key.length() < MINIMUM_KEY_LENGTH) throw new Exception("Invalid Key! Minimum key length must be: " + MINIMUM_KEY_LENGTH);
        this.key = key.trim().toUpperCase().toCharArray();
    }
    
    public char[] getKey(){
        return this.key;
    }
    
    public void setFile(String fileName){
        this.fileName = fileName;
    }
    
    public String getFile(){
        return this.fileName;
    }
    
    public void setFileContents(String contents){
        this.file = contents;
    }
    
    public String getFileContents(){
        return this.file;
    }
    
    public String encrypt(String plainText)throws Exception{
        if (!isValidText(plainText)) throw new Exception("Plain Text must be less than key length: " + key.length);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < plainText.length(); i++){
            sb.append(getEncryptedCharacter(key[i], plainText.charAt(i)));
        }
        return sb.toString();
    }
    
    //FIND ROW WITH KEY EQUIVALENCE, THEN FIND COLUMN WITH PLAINTEXT EQUIVALENCE IN THAT ROW, In cipher[0][In that same column] = encrypted character.
    private char getEncryptedCharacter(char key, char plain){
        for (int row = 0; row < cipher.length; row++){ //Loops through each row, if row variable is less than the length of number of elements in cipher
            if (cipher[row][0] == key && key != plain){ //
                for (int col = 0; col < cipher[row].length; col++){
                    int ch = (int)(cipher[0][col]);
                    if (cipher[row][col] == plain){
                        char encChar = (char)(ch);
                        //System.out.println("Letter at row 0 col " + col + " equals " + cipher[0][col] + " Plain letter is: " + plain + " Key is: " + key);
                        return encChar;
                    }
                }
            }else if(cipher[row][0] == key && cipher[row][0] == plain){
                for (int col = cipher[row].length - 1; col > 0; col--){
                    int ch = (int)(cipher[0][col]);
                    if(cipher[row][col] == plain){
                        char encChar = (char)(ch);
                        //System.out.println("Letter at row 0 col " + col + " equals " + cipher[0][col] + " Plain letter is: " + plain + " Key is: " + key);
                        return encChar;
                    }
                }
            }
        }
        //System.out.println("did not encrypt ");
        return plain;
    }
    

    
    public String decrypt(String cipherText){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cipherText.length(); i++){
            sb.append(getDecryptedCharacter(key[i], cipherText.charAt(i)));
        }
        return sb.toString();
    }
    
    private char getDecryptedCharacter(char key, char cipher){
        char dec = getEncryptedCharacter(key, cipher);
        return dec;
    }
    
    private boolean isValidText(String text){
        if (text != null && text.length() < key.length){
            return true;
        }else{
            return false;
        }
    }
    
    public void ensureCapacity(int capacity){
        if (capacity > key.length){
            resize(capacity);
        }
    }
    
    private void resize(int capacity){
        char[] temp = new char[capacity];
        int index = 0;
        while (index < temp.length){
            for (int i = 0; i < key.length && index < temp.length; i++){
                temp[index] = key[i];
                index++;
            }
        }
        key = temp;
    }
}
