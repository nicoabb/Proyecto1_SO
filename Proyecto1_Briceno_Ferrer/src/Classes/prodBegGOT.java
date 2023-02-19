/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author emilo
 */
public class prodBegGOT extends Thread {

    private Semaphore mutex;
    private boolean stop = false;
    private Semaphore semBeg;
    private double begsPerDay = 0.25;
    
    public prodBegGOT(Semaphore mutex, Semaphore semBeg) {
        this.mutex = mutex;
        this.semBeg = semBeg;
    }
    
    @Override
    public void run() {
        while(!stop) {
                try {
                    semBeg.acquire();
                    Thread.sleep(Math.round(1000 / begsPerDay)); // Aqui espera 8 horas (que le toma hacer una intro) y luego 
                    
                    mutex.acquire();

                    Interfaces.Dashboard.begsProduced++;
                    Interfaces.Dashboard.cantBegs.setText(Integer.toString(Interfaces.Dashboard.begsProduced));
                    
                    mutex.release();
//                    semCred.release(); Este
                } catch (InterruptedException ex) {
                    Logger.getLogger(prodIntroGOT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

    }
    
}
