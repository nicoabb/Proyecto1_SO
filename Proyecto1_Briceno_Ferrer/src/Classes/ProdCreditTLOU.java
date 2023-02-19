/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Interfaces.Dashboard;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Nicolás Briceño
 */
public class ProdCreditTLOU {
    
    private int dayDuration;
    private double dailyProduce = 4; //Produce 4 créditos al día
    private Semaphore mutex;
    private Semaphore semPart;
    private boolean stop;

    public ProdCreditTLOU( int dayDuration, Semaphore mutexCredit, Semaphore semCredit) {
        this.stop = false;
        this.dayDuration = dayDuration;
        this.mutex = mutexCredit;
        this.semPart = semCredit;
        
    }
    
    public void run() {
        while (!stop) {
            try {
                semPart.acquire();
                
                Thread.sleep(Math.round((dayDuration * 1000) / dailyProduce));
                mutex.acquire();
                
                Dashboard.creditDriveTLOU++;
                Dashboard.numCredit.setText(Integer.toString(Dashboard.creditDriveTLOU));

                mutex.release();

            } catch (Exception e) {

            }
        }
    }
    
}
