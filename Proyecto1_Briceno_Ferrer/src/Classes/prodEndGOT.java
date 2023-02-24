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
public class prodEndGOT extends Thread {

    private Semaphore mutex;
    private boolean stop;
    private Semaphore semEnd;
    private double endsPerDay = 0.33333;
    private Semaphore semEns;
    
    public prodEndGOT(Semaphore mutex, Semaphore semEnd, Semaphore semEns) {
        this.mutex = mutex;
        this.semEnd = semEnd;
        this.semEns = semEns;
    }
    
    @Override
    public void run() {
        while(!stop) {
                try {
                    semEnd.acquire();
                    
                    Thread.sleep(Math.round(Interfaces.GOTInterface.dayDuration / endsPerDay)); // Aqui espera 3 dias (que le toma hacer un ending) y luego entra en mutex y actualiza el valor
                    mutex.acquire();

                    Interfaces.GOTInterface.endsProducedGOT++;
                    Interfaces.GOTInterface.qtyEndsGOT.setText(Integer.toString(Interfaces.GOTInterface.endsProducedGOT));
                    
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
