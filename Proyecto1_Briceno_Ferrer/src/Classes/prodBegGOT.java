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
    private boolean stop;
    private Semaphore semBeg;
    private double begsPerDay = 0.25;
    private Semaphore semEns;
    
    public prodBegGOT(Semaphore mutex, Semaphore semBeg, Semaphore semEns) {
        
        this.mutex = mutex;
        this.semBeg = semBeg;
        this.semEns = semEns;
    }
    
    @Override
    public void run() {
        while(!stop) {
                try {
                    semBeg.acquire();
                    Thread.sleep(Math.round(Interfaces.GOTInterface.dayDuration / begsPerDay)); // Aqui espera 4 dias (que le toma hacer un inicio)
                    
                    mutex.acquire();

                    Interfaces.GOTInterface.begsProducedGOT++;
                    Interfaces.GOTInterface.qtyBegsGOT.setText(Integer.toString(Interfaces.GOTInterface.begsProducedGOT));
                    
                    mutex.release();
                    semEns.release();
                } catch (InterruptedException ex) {
                    Logger.getLogger(prodIntroGOT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

    }
    
    public void setStop (boolean stop) {
        this.stop = stop;
    }
    
}
