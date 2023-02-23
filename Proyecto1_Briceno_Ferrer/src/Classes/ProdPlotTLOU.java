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
public class ProdPlotTLOU extends Thread{
     private int dayDuration;
    private double dailyProduce = 0.5; //Produce 1 cierre cada 4 días
    private Semaphore mutex, semPart, semAssembler;
    private boolean stop;

    public ProdPlotTLOU( int dayDuration, Semaphore mutexPlot, Semaphore semPlot, Semaphore semAssemPlot) {
        this.stop = false;
        this.dayDuration = dayDuration;
        this.mutex = mutexPlot;
        this.semPart = semPlot;
        this.semAssembler = semAssemPlot;
        
    }
    
    @Override
    public void run() {
        while (!stop) {
            try {
                semPart.acquire();
                
                Thread.sleep(Math.round((dayDuration * 1000) / dailyProduce));
                mutex.acquire();
                
                TLOUInterface.plotDriveTLOU++;
                TLOUInterface.numPlotTLOU.setText(Integer.toString(TLOUInterface.plotDriveTLOU));

                mutex.release();
                semAssembler.release();

            } catch (Exception e) {

            }
        }
    }
}
