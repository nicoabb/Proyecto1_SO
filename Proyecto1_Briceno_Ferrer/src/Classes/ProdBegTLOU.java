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
public class ProdBegTLOU extends Thread{
    
    private boolean start;
    private int dayDuration;
    private double dailyProduce = 0.5; //Produce 1 inicio cada 2 días
    private Semaphore mutex, semPart, semAssembler;

    public ProdBegTLOU(boolean start, int dayDuration, Semaphore mutexBeg, Semaphore semBeg, Semaphore semAssemBeg) {
        this.start = start;
        this.dayDuration = dayDuration;
        this.mutex = mutexBeg;
        this.semPart = semBeg;
        this.semAssembler = semAssemBeg;
    }
    
    @Override
    public void run() {
        while (start) {
            try {
                semPart.acquire();
                
                Thread.sleep(Math.round((dayDuration * 1000) / dailyProduce));
                mutex.acquire();
                
                TLOUInterface.begDriveTLOU++;
                TLOUInterface.numBegTLOU.setText(Integer.toString(TLOUInterface.begDriveTLOU));

                mutex.release();
                semAssembler.release();

            } catch (Exception e) {

            }
        }
    }

    public void setStart(boolean start) {
        this.start = start;
    }
    
}
