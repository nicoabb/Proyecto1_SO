/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Interfaces.TLOUInterface;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Nicolás Briceño
 */
public class ProdIntroTLOU extends Thread{
    
    private int dayDuration;
    private double dailyProduce = 1; //Produce 1 intro al día
    private Semaphore mutex, semPart, semAssembler;
    private boolean stop;

    public ProdIntroTLOU( int dayDuration, Semaphore mutexIntro, Semaphore semIntro, Semaphore semAssemIntro) {
        this.stop = false;
        this.dayDuration = dayDuration;
        this.mutex = mutexIntro;
        this.semPart = semIntro;
        semAssembler = semAssemIntro;
        
    }
    
    @Override
    public void run() {
        while (!stop) {
            try {
                semPart.acquire();
                
                Thread.sleep(Math.round((dayDuration * 1000) / dailyProduce));
                mutex.acquire();
                
                TLOUInterface.introDriveTLOU++;
                TLOUInterface.numIntroTLOU.setText(Integer.toString(TLOUInterface.introDriveTLOU));

                mutex.release();
                semAssembler.release();

            } catch (Exception e) {

            }
        }
    }
    
}
