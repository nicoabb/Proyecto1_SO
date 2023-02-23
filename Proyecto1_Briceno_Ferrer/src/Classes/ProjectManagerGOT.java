/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Interfaces.Dashboard;
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
                System.out.println("");
                Thread.sleep(Dashboard.dayDuration * 10 / 24);
                if (counter.availablePermits() > 0){
                    counterMutex.acquire();
                    System.out.println("El contador va por: " + counter.availablePermits() );
                    counter.acquire();
                    Dashboard.counterPMGOT = counter.availablePermits() + 1;
                    Dashboard.daysUntilCut.setText(Integer.toString(Dashboard.counterPMGOT));
                    counterMutex.release();
                }

                while (waste <= 35) {
                    watchingRaM = true;
//                    System.out.println("viendo rikimorti");
                    Thread.sleep(Dashboard.hourDuration * 24 / 60); // dividir el dia en horas y luego la hora en 24 minutos
                    watchingRaM = false;
                    
                    if(waste == 34) break;

                    checkingSprints = true;
//                    System.out.println("sprinteando");
                    Thread.sleep(Dashboard.hourDuration * 24 / 60); // dividir el dia en horas y luego la hora en 24 minutos
                    checkingSprints = false;
                    waste++;
                }
                waste = 0;
            } catch (InterruptedException ex) {
                Logger.getLogger(ProjectManagerGOT.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
