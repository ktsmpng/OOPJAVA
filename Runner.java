package ie.gmit.dip;

public class Runner {
    
    public static void main(String[] args)throws Exception{
        /*------------------------Menu Test----------------------------*/
        
        Menu m = new Menu();
        m.start();
        /*-------------------------------------------------------------*/
        
        /*------------------------Quick Test----------------------------*/
        /*
        String key = "HAPPYHAPPYHAPPYHAPPYHAPPYHAPPYHAPPYHAPPYHAPPYHAPPYHAPPYHAPPYHAPPYHAPPYHAPPYHAPPY";
        String plainText = "THE CURFEW TOLLS THE KNELL OF PARTING DAY";
        try{
            PortaCipher cipher = new PortaCipher(key);
            String cipherText = cipher.encrypt(plainText);
            System.out.println(cipherText);
            System.out.println(cipher.decrypt(cipherText));
        }catch(Exception e){
            System.out.println(e);
        }
        /*------------------------Quick Test----------------------------*/
        
    }
}
