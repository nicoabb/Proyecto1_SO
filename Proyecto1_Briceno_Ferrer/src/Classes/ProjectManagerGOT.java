/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Interfaces.GOTInterface;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author emilo
 */
public class ProjectManagerGOT extends Thread {

    private Semaphore counter;
    private Semaphore counterMutex;
    public int waste = 0;
    public boolean watchingRaM;
    public boolean checkingSprints;

    public ProjectManagerGOT(Semaphore counter, Semaphore counterMutex) {
        this.counter = counter;
        this.counterMutex = counterMutex;
    }

    @Override
    public void run() {
        while (true) {

            try {
                GOTInterface.counterPMGOT = counter.availablePermits();
                GOTInterface.daysUntilCut.setText(Integer.toString(GOTInterface.counterPMGOT));
                while (waste <= 35) {
                    watchingRaM = true;
//                    System.out.println("viendo rikimorti");
                    Thread.sleep((long) (GOTInterface.hourDuration * 0.4)); // dividir el dia en horas y luego la hora en 24 minutos
                    watchingRaM = false;
                    waste++;

                    if (waste == 34) {
                        break;
                    }

                    checkingSprints = true;
//                    System.out.println("sprinteando");
                    Thread.sleep((long) (GOTInterface.hourDuration * 0.4)); // dividir el dia en horas y luego la hora en 24 minutos
                    checkingSprints = false;
                    waste++;
                }
                Thread.sleep(GOTInterface.hourDuration * 10);
                if (counter.availablePermits() > 0) {
                    counterMutex.acquire();
                    
                    counter.acquire();
//                    System.out.println("El contador va por: " + counter.availablePermits());
                    GOTInterface.counterPMGOT = counter.availablePermits();
                    
                    GOTInterface.daysUntilCut.setText(Integer.toString(GOTInterface.counterPMGOT));
                    counterMutex.release();
                }

                
                waste = 0;
            } catch (InterruptedException ex) {
                Logger.getLogger(ProjectManagerGOT.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
