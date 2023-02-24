/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.TLOU;

import Interfaces.TLOUInterface;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Nicolás Briceño
 */
public class ProdCreditTLOU extends Thread{
    
    private boolean start;
    private int dayDuration;
    private double dailyProduce = 4; //Produce 4 créditos al día
    private Semaphore mutex, semPart, semAssembler;

    public ProdCreditTLOU(boolean start, int dayDuration, Semaphore mutexCredit, Semaphore semCredit, Semaphore semAssemCredit) {
        this.start = start;
        this.dayDuration = dayDuration;
        this.mutex = mutexCredit;
        this.semPart = semCredit;
        this.semAssembler = semAssemCredit;
    }
    
    @Override
    public void run() {
        while (start) {
            try {
                semPart.acquire();
                
                Thread.sleep(Math.round((dayDuration * 1000) / dailyProduce));
                mutex.acquire();
                
                TLOUInterface.creditDriveTLOU++;
                TLOUInterface.numCreditTLOU.setText(Integer.toString(TLOUInterface.creditDriveTLOU));

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
