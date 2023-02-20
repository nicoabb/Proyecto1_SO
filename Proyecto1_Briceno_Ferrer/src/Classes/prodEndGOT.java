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
    private boolean stop = false;
    private Semaphore semEnd;
    private double endsPerDay = 0.33333;
    
    public prodEndGOT(Semaphore mutex, Semaphore semEnd) {
        this.mutex = mutex;
        this.semEnd = semEnd;
    }
    
    @Override
    public void run() {
        while(!stop) {
                try {
                    semEnd.acquire();
                    
                    Thread.sleep(Math.round(1000 / endsPerDay)); // Aqui espera 3 dias (que le toma hacer un ending) y luego entra en mutex y actualiza el valor
                    mutex.acquire();

                    Interfaces.Dashboard.endsProducedGOT++;
                    Interfaces.Dashboard.cantEndsGOT.setText(Integer.toString(Interfaces.Dashboard.endsProducedGOT));
                    
                    mutex.release();
//                    semCred.release(); Este
                } catch (InterruptedException ex) {
                    Logger.getLogger(prodIntroGOT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

    }
    
}
