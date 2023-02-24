/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.GOT;

import Interfaces.GOTInterface;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Emilio Ferrer
 */
public class prodCredGOT extends Thread {

    private Semaphore mutex;
    private boolean stop;
    private Semaphore semCred;
    private int credsPerDay = 3;
    private Semaphore semEns;
    
    public prodCredGOT(Semaphore mutex, Semaphore semCred, Semaphore semEns) {
        this.mutex = mutex;
        this.semCred = semCred;
        this.semEns = semEns;
    }
    
    @Override
    public void run() {
        while(!stop) {
                try {
                    semCred.acquire();
                    Thread.sleep(Math.round(Interfaces.GOTInterface.dayDuration/ credsPerDay)); // Aqui espera 8 horas (que le toma hacer un credito) y luego 
                    
                    mutex.acquire();

                    Interfaces.GOTInterface.credsProducedGOT++;
                    Interfaces.GOTInterface.qtyCredGOT.setText(Integer.toString(Interfaces.GOTInterface.credsProducedGOT));
                    
                    mutex.release();
                    semEns.release();
                } catch (InterruptedException ex) {
                    Logger.getLogger(prodIntroGOT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

    }
    
    public void setStop(boolean stop) {
        this.stop = stop;
    }
    
}