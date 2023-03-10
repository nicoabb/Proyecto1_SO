/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.GOT;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Emilio Ferrer
 */
public class prodPlotGOT extends Thread {

    private Semaphore mutex;
    private boolean stop;
    private Semaphore semPlot;
    private double plotsPerDay = 0.33333;
    private Semaphore semEns;
    
    public prodPlotGOT(Semaphore mutex, Semaphore semPlot, Semaphore semEns) {
        this.mutex = mutex;
        this.semPlot = semPlot;
        this.semEns = semEns;
    }
    
    @Override
    public void run() {
        while(!stop) {
                try {
                    semPlot.acquire();
                    
                    Thread.sleep(Math.round(Interfaces.GOTInterface.dayDuration / plotsPerDay)); // Aqui espera 3 dias (que le toma hacer un plot twist) y luego entra en mutex y actualiza el valor
                    mutex.acquire();

                    Interfaces.GOTInterface.plotsProducedGOT++;
                    Interfaces.GOTInterface.qtyPlotsGOT.setText(Integer.toString(Interfaces.GOTInterface.plotsProducedGOT));
                    
                    mutex.release();
                    semEns.release();
                } catch (InterruptedException ex) {
                    Logger.getLogger(prodIntroGOT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

    }
    
    public void setStop(boolean stop) {
        this.stop = stop;
    }
    
}