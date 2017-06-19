package ie.gmit.dip;

import java.net.*;
import java.io.*;
import java.util.*;


public class Menu {
    private Scanner in;
    private EncryptedFileManager efm;
    private boolean keepRunning = true;
    private PortaCipher pc;
    private static final String FILE_NAME = "filedatabase.ser";
    
    public void start()throws Exception{
        in = new Scanner(System.in);
        load();
        
        if (efm == null){
            efm = new EncryptedFileManager();
        }

        while (keepRunning){
            displayOptions();
            int choice = Integer.parseInt(in.next());
            if (choice == 1){
                createNewFile();
            }else if (choice == 2){
                encryptExistingFile();
            }else if (choice == 3){
                encryptURL();
            }else if (choice == 4){
                decryptFile();
            }else if (choice == 5){
                save();
                System.exit(0);
            }
        
        }
    }
        
    private void displayOptions(){
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("~~                 Porta Cipher v.1.0.                        ~~");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("(1)  Create New Encrypted File                                  ");
        System.out.println("(2)  Encrypt Existing File                                      ");
        System.out.println("(3)  Encrypt From URL                                           ");
        System.out.println("(4)  Decrypt File                                               ");
        System.out.println("(5)  Quit                                                       ");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    }
    
    private void load(){
        try{
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME));
            efm = (EncryptedFileManager) in.readObject();
            in.close();
        }catch(Exception e){

        }
    }
    
    private void save(){
        try{
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME));
            out.writeObject(efm);
            out.flush();
            out.close();
        }catch(Exception e){
            System.out.println("[Error] Cannot save database. Cause: ");
            e.printStackTrace();
        }
    }
    
    private void createNewFile(){
        System.out.println("Enter new file name: ");
        String file = in.next();
        System.out.println("Set Encryption Key: ");
        String key = in.next();
        System.out.println("To create this new file enter contents below: ");
        String contents = in.next();
        
        try{
            pc = new PortaCipher(key, file);
            String encrypted = pc.encrypt(contents.toUpperCase());
            FileWriter fw = new FileWriter(file);
            fw.write(encrypted + "\n");
            fw.flush();
            fw.close();
            boolean result = efm.add(this.pc);
            if (result){
                System.out.println("The file " + file + " has been encrypted.");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    private void encryptExistingFile(){
        System.out.println("Please input file name: ");
        String name = in.next();
        File file = new File(name);

        if (file.exists() && !file.isDirectory()){
                
            System.out.println("Set Encryption Key: ");
            String key = in.next();
                
            System.out.println("Save As: ");
            String out = in.next();
    
            System.out.println("Proceed with Encryption? (y/n): ");
            String verify = in.next().toLowerCase();
            
            if (verify.equals("y")){
                createEncryptedFile(key, file, out);
                save();
            }else if(verify.equals("n")){
                    System.out.println("Returning to Menu...");
            }else{
                    System.out.println("Invalid Input!");
                    System.out.println("Returning to Menu...");
                }
            
        }else{
                System.out.println("The file " + name + " does not exist!");
                System.out.println("Returning to Menu...");
            }
        }

    
    private void encryptURL()throws Exception{
        System.out.println("Input URL you wish to encrypt: ");
        URL input = new URL(in.next()); //Need this to be string
        System.out.println("Set Encryption Key: ");
        String key = in.next();
        System.out.println("Save As: ");
        String out = in.next();
        processUrlWriteOut(key, input, out);
    }
    
    private void decryptFile(){
        System.out.println("Enter file you wish to decrypt: ");
        String name = in.next();
        File file = new File(name);
        
        PortaCipher result = efm.getFileByName(name);
        String resultString = result.getFile();
        
        //If the file is on the encrypted manager system execute:
        if (name.equals(resultString)){
            System.out.println("Enter decryption key: ");
            String key = in.next().toUpperCase();
            String resultKey = convertCharArrToString(result.getKey()); 
        
            String newuserkey = deconcatKey(key, resultKey);
            
            //If the object files' encryption key is equal to user input execute:
            if (key.equals(newuserkey)){
                System.out.println("Save As: ");
                String out = in.next();
                try{
                    guts(resultKey, file, out);
                }catch (Exception e){
                    e.printStackTrace();
                }
            //If user input does not match key:
            }else{
                System.out.println("Invalid Key! The file could not be decrypted.");
            }   
        
        }else{
            System.out.println("The encrypted file " + name + " could not be found.");
        }
    }
    
    private String deconcatKey(String user_input, String realkey){
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < user_input.length(); i++){
            sb.append(realkey.charAt(i));
        }
        
        return sb.toString();
    }
    
    private void processUrlWriteOut(String key, URL input, String output_file){
        try{
            pc = new PortaCipher(key, output_file);
            URLConnection urlConnect = input.openConnection();
            urlConnect.connect();
            int file_size = (int) urlConnect.getContentLength();
            
            if (file_size == -1){
                System.out.println("No Content could be found.");
            }else{
                pc.ensureCapacity(file_size);
                FileWriter fw = new FileWriter(output_file);
                BufferedReader br = new BufferedReader(new InputStreamReader(input.openStream()));
                String line = null;
                
                while((line = br.readLine()) != null){
                    String encrypted = pc.encrypt(line.toUpperCase());
                    fw.write(encrypted + "\n");
                }
                
                br.close();
                fw.flush();
                fw.close();
                boolean result = efm.add(this.pc);
                if (result){
                    System.out.println("The file " + input + " has been encrypted.");
                    System.out.println(efm.size());
                }
            }
            
        }catch(Exception e){
            System.out.println(e);            
        }
    }
    
    private void guts(String key, File file, String output_file)throws Exception{
            pc = new PortaCipher(key, output_file);
            pc.ensureCapacity((int)file.length());
            
            FileWriter fw = new FileWriter(output_file);
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            
            String line = null;
            
            while((line = br.readLine()) != null){
                String encrypted = pc.encrypt(line.toUpperCase());
                fw.write(encrypted + "\n");
            }
            br.close();
            fw.flush();
            fw.close();
    }
    
    private void createEncryptedFile(String key, File file, String output_file){
        try{
            guts(key,file,output_file);
            boolean result = efm.add(this.pc);
            if (result){
                System.out.println("The file " + file + " has been encrypted.");
            }
            System.out.println(efm.size() + " files found");
        }catch(Exception e){
            System.out.println("Yikes! Something went Wrong!: ");
            e.printStackTrace();
        }
    }

    private String convertCharArrToString(char[] key){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < key.length; i++){
            sb.append(key[i]);
        }
        return sb.toString();
    }
}
