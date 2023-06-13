/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pk;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.*;
import javax.naming.*;

/**
 *
 * @author usuario
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
         Context c; 
        QueueConnectionFactory f; 
        QueueSession qs; 
        Queue q; 
        QueueReceiver r = null; 
        Mensaje m = null; 
        QueueConnection cc = null;
        ObjectMessage om = null;
        
        int counter=0;
        int code;
        
        
        try{
            c=new InitialContext(); 
            f=(QueueConnectionFactory)c.lookup("factoriaConexiones"); 
            q=(Queue)c.lookup("cola"); 
            cc=f.createQueueConnection(); 
            qs=cc.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            r = qs.createReceiver((javax.jms.Queue) q); 
            cc.start();
        }catch (NamingException | JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error conectando con el servidor", ex);
            System.exit(1);
        }
        
       
       
        
        do{
            
            try {
                om = (ObjectMessage)r.receive(0); // EL argumento 0 indica que la espera es indefinida 
                m = (Mensaje)om.getObject();
            } catch (JMSException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error recibiendo mensaje", ex);
                break;
            }
            code = m.op;
            System.out.println("Mensaje recibido: Código" + code + " Valor: " + m.valor + " Contador: " + counter);
            switch(code){
                case 0:
                    break;
                case 1:
                    counter+= m.valor;
                    break;
                case 2:
                    counter-= m.valor;
                    break;  
            }
            System.out.println("Cuenta: " + counter);
            
        }while(code!=0 && counter<=100);
        
        
        
        try {
            cc.close();
        } catch (JMSException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, "Error intentando cerrar la comunicación", ex);
        }
        
    }
    
}
