/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Interfaces.Dashboard;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author emilo
 */
public class prodCredGOT extends Thread {

    private Semaphore mutex;
    private boolean stop = false;
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
                    Thread.sleep(Math.round(Interfaces.Dashboard.dayDuration/ credsPerDay)); // Aqui espera 8 horas (que le toma hacer un credito) y luego 
                    
                    mutex.acquire();

                    Interfaces.Dashboard.credsProducedGOT++;
                    Interfaces.Dashboard.qtyCredGOT.setText(Integer.toString(Interfaces.Dashboard.credsProducedGOT));
                    
                    mutex.release();
                    semEns.release();
                } catch (InterruptedException ex) {
                    Logger.getLogger(prodIntroGOT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

    }
    
}