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
    private boolean stop;
    private Semaphore mutex;
    private Semaphore semIntro;
    private int introsPerDay = 3;
    private Semaphore semEns;
    
    public prodIntroGOT(Semaphore mutex, Semaphore semIntro, Semaphore semEns) {
        this.mutex = mutex;
        this.semIntro = semIntro;
        this.semEns = semEns;
    }
    
    @Override
    public void run() {
        while(!stop) {
                try {
                    
//                    System.out.println(count);
                    semIntro.acquire();
//                    System.out.println("A esperar");
                    Thread.sleep(Math.round(Dashboard.dayDuration / introsPerDay)); // Aqui espera 8 horas (que le toma hacer una intro)
                    
                    mutex.acquire();
                    
                    Dashboard.introsProducedGOT++;
                    Dashboard.qtyIntrosGOT.setText(Integer.toString(Dashboard.introsProducedGOT));
                    
                    mutex.release();
                    semEns.release();
//                    System.out.println("Creado!");
                } catch (InterruptedException ex) {
                    Logger.getLogger(prodIntroGOT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

    }
    
    public void setStop() {
        stop = !stop;
    }
    
}
