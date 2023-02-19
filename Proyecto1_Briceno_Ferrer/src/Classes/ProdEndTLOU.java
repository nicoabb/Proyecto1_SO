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
public class ProdEndTLOU extends Thread{
    
    private int dayDuration;
    private double dailyProduce = 0.25; //Produce 1 cierre cada 4 días
    private Semaphore mutex;
    private Semaphore semPart;
    private boolean stop;

    public ProdEndTLOU( int dayDuration, Semaphore mutexEnd, Semaphore semEnd) {
        this.stop = false;
        this.dayDuration = dayDuration;
        this.mutex = mutexEnd;
        this.semPart = semEnd;
        
    }
    
    @Override
    public void run() {
        while (!stop) {
            try {
                semPart.acquire();
                
                Thread.sleep(Math.round((dayDuration * 1000) / dailyProduce));
                mutex.acquire();
                
                Dashboard.endDriveTLOU++;
                Dashboard.numEndTLOU.setText(Integer.toString(Dashboard.endDriveTLOU));

                mutex.release();

            } catch (Exception e) {

            }
        }
    }
}
