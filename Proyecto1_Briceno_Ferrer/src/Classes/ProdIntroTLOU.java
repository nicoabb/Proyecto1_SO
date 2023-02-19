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
public class ProdIntroTLOU extends Thread{
    
    private int dayDuration;
    private double dailyProduce = 1; //Produce 1 intro al día
    private Semaphore mutex;
    private Semaphore semPart;
    private boolean stop;

    public ProdIntroTLOU( int dayDuration, Semaphore mutexIntro, Semaphore semIntro) {
        this.stop = false;
        this.dayDuration = dayDuration;
        this.mutex = mutexIntro;
        this.semPart = semIntro;
        
    }
    
    @Override
    public void run() {
        while (!stop) {
            try {
                semPart.acquire();
                
                Thread.sleep(Math.round((dayDuration * 1000) / dailyProduce));
                mutex.acquire();
                
                Dashboard.introDriveTLOU++;
                Dashboard.numIntro.setText(Integer.toString(Dashboard.introDriveTLOU));

                mutex.release();

            } catch (Exception e) {

            }
        }
    }
    
}
