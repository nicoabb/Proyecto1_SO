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
public class prodIntroGOT extends Thread {

    private Semaphore mutex;
    private boolean stop = false;
    private Semaphore semIntro;
    private int introsPerDay = 3;
    
    public prodIntroGOT(Semaphore mutex, Semaphore semIntro) {
        this.mutex = mutex;
        this.semIntro = semIntro;
    }
    
    @Override
    public void run() {
        while(!stop) {
                try {
                    semIntro.acquire();
                    Thread.sleep(1000 / introsPerDay); // Aqui espera 8 horas (que le toma hacer una intro)
                    
                    mutex.acquire();
                    
                    Interfaces.Dashboard.introsProducedGOT++;
                    Interfaces.Dashboard.cantIntrosGOT.setText(Integer.toString(Interfaces.Dashboard.introsProducedGOT));
                    
                    mutex.release();
//                    semIntro.release();
                } catch (InterruptedException ex) {
                    Logger.getLogger(prodIntroGOT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

    }
    
}
