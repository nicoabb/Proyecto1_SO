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
    
    public prodCredGOT(Semaphore mutex, Semaphore semCred) {
        this.mutex = mutex;
        this.semCred = semCred;
    }
    
    @Override
    public void run() {
        while(!stop) {
                try {
                    semCred.acquire();
                    Thread.sleep(24000 / credsPerDay); // Aqui espera 8 horas (que le toma hacer una intro) y luego 
                    
                    mutex.acquire();

                    Interfaces.Dashboard.credsProduced++;
                    Interfaces.Dashboard.cantCreds.setText(Integer.toString(Interfaces.Dashboard.credsProduced));
                    
                    mutex.release();
//                    semCred.release(); Este
                } catch (InterruptedException ex) {
                    Logger.getLogger(prodIntroGOT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

    }
    
}