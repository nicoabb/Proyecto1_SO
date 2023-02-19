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
public class ProdBegTLOU extends Thread{
    
    private int dayDuration;
    private double dailyProduce = 0.5; //Produce 4 créditos al día
    private Semaphore mutex;
    private Semaphore semPart;
    private boolean stop;

    public ProdBegTLOU( int dayDuration, Semaphore mutexBeg, Semaphore semBeg) {
        this.stop = false;
        this.dayDuration = dayDuration;
        this.mutex = mutexBeg;
        this.semPart = semBeg;
        
    }
    
    @Override
    public void run() {
        while (!stop) {
            try {
                semPart.acquire();
                
                Thread.sleep(Math.round((dayDuration * 1000) / dailyProduce));
                mutex.acquire();
                
                Dashboard.begDriveTLOU++;
                Dashboard.numBegTLOU.setText(Integer.toString(Dashboard.begDriveTLOU));

                mutex.release();

            } catch (Exception e) {

            }
        }
    }
}
