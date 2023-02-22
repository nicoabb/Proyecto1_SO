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
//    public boolean waste;
    public int waste = 0;
    public boolean watchingRaM;
    public boolean checkingSprints;
//    public AlternatorGOT alternator;

    public ProjectManagerGOT(Semaphore counter) {
        this.counter = counter;
//        this.alternator = new AlternatorGOT(this);
    }

    @Override
    public void run() {
        while (true) {

            try {
                Thread.sleep(Dashboard.dayDuration * 10 / 24);
                if (counter.availablePermits() == 0) {
                    counter.release(30);
                } else {
                    System.out.println("El contador va por: " + counter.availablePermits() + 1);
                    counter.acquire();
                    Dashboard.counterPMGOT = counter.availablePermits() + 1;
                    Dashboard.daysUntilCut.setText(Integer.toString(Dashboard.counterPMGOT));
                }

//                alternator.start();
//                while (waste <= 35) {
                System.out.println("llegue aqui " + waste);
                while (waste <= 35) {
                    watchingRaM = true;
                    System.out.println("viendo rikimorti");
                    Thread.sleep((Dashboard.dayDuration / 24)*24/60); // dividir el dia en horas y luego la hora en 24 minutos
                    watchingRaM = false;
                    
                    if(waste == 34) break;

                    checkingSprints = true;
                    System.out.println("sprinteando");
                    Thread.sleep((Dashboard.dayDuration / 24)*24/60); // dividir el dia en horas y luego la hora en 24 minutos
                    checkingSprints = false;
                    waste++;
                }
                waste = 0;

//                alternator.interrupt();
            } catch (InterruptedException ex) {
                Logger.getLogger(ProjectManagerGOT.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

}
