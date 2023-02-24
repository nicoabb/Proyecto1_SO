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
public class ProdEndTLOU extends Thread{
    
    private boolean stop;
    private int dayDuration;
    private double dailyProduce = 0.25; //Produce 1 cierre cada 4 días
    private Semaphore mutex, semPart, semAssembler;

    public ProdEndTLOU(boolean stop, int dayDuration, Semaphore mutexEnd, Semaphore semEnd, Semaphore semAssemEnd) {
        this.stop = stop;
        this.dayDuration = dayDuration;
        this.mutex = mutexEnd;
        this.semPart = semEnd;
        this.semAssembler = semAssemEnd;
        
    }
    
    @Override
    public void run() {
        while (!stop) {
            try {
                semPart.acquire();
                
                Thread.sleep(Math.round((dayDuration * 1000) / dailyProduce));
                mutex.acquire();
                
                TLOUInterface.endDriveTLOU++;
                TLOUInterface.numEndTLOU.setText(Integer.toString(TLOUInterface.endDriveTLOU));

                mutex.release();
                semAssembler.release();

            } catch (Exception e) {

            }
        }
    }
}
