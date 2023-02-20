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
public class ProdCreditTLOU extends Thread{
    
    private int dayDuration;
    private double dailyProduce = 4; //Produce 4 créditos al día
    private Semaphore mutex, semPart, semAssembler;
    private boolean stop;

    public ProdCreditTLOU( int dayDuration, Semaphore mutexCredit, Semaphore semCredit, Semaphore semAssemCredit) {
        this.stop = false;
        this.dayDuration = dayDuration;
        this.mutex = mutexCredit;
        this.semPart = semCredit;
        this.semAssembler = semAssemCredit;
    }
    
    @Override
    public void run() {
        while (!stop) {
            try {
                semPart.acquire();
                
                Thread.sleep(Math.round((dayDuration * 1000) / dailyProduce));
                mutex.acquire();
                
                Dashboard.creditDriveTLOU++;
                Dashboard.numCreditTLOU.setText(Integer.toString(Dashboard.creditDriveTLOU));

                mutex.release();
                semAssembler.release();

            } catch (Exception e) {

            }
        }
    }
    
}
