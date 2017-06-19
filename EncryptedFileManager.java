package ie.gmit.dip;

import java.io.*;
public class EncryptedFileManager implements Serializable{
    private static final int INITIAL_CAPACITY = 20;
    private PortaCipher[] encFiles = null;
    
    public EncryptedFileManager(){
        encFiles = new PortaCipher[INITIAL_CAPACITY];
    }
    
    public boolean add(PortaCipher encFile){
        for (int i = 0; i < encFiles.length; i++){
            if(encFiles[i] == null){
                encFiles[i] = encFile;
                return true;
            }
        }
        return false;
    }
    
    public PortaCipher getFileByName(String file){
       for (int i = 0; i < encFiles.length; i++){
            if (encFiles[i] != null && encFiles[i].getFile().equals(file)){
                return encFiles[i];        
            }
        }
        return null;
    }
    
    public int size(){
        int total = 0; 
        for (int i = 0; i < encFiles.length; i++){
            if (encFiles[i] != null){
                total++;
            }
        }
        
        return total;
    }
}
