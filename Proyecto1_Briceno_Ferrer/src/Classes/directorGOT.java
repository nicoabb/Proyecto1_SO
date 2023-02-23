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
public class directorGOT extends Thread {

    private ProjectManagerGOT pm;
    private Semaphore counter;
    private Semaphore counterMutex;
    private boolean cicle;

    public directorGOT(ProjectManagerGOT pm, Semaphore counter, Semaphore counterMutex) {
        this.pm = pm;
        this.counter = counter;
        this.counterMutex = counterMutex;
    }

    @Override
    public void run() {
        while (true) {
            try {
//                cicle = true;
                double guardLength = (Math.random() * (Dashboard.dayDuration * 0.75 - Dashboard.dayDuration / 2)) + Dashboard.dayDuration / 2;
//                counterMutex.acquire(); // Trata de agarrar exclusividad sobre el semaforo del contador
//                if (counter.availablePermits() == 0) {
//                    counter.release(30);
////                    System.out.println("Liberé el corotro mibró!");
//                }
//                counterMutex.release();
                double elapsedTime = 0;
                double startTime = System.currentTimeMillis();
                while (elapsedTime < guardLength) {

                    double timeBetweenCheck = Math.random() * ((1.5 * Dashboard.hourDuration) - (0.5 * Dashboard.hourDuration)) + 0.5 * Dashboard.hourDuration;
                    Thread.sleep(Math.round(timeBetweenCheck));
                    // Aqui va y ve qué está haciendo el PM
//                    System.out.println("Revisando...");
                    if (pm.watchingRaM) {
//                        System.out.println("Está viendo Rikimorti el PM!!!");
                    }
                    elapsedTime = System.currentTimeMillis() - startTime;

                }
                System.out.println("descansando");

                Thread.sleep((Dashboard.dayDuration - Math.round(guardLength)));
//                System.out.println("ahora a repetir el ciclo");
            } catch (InterruptedException ex) {
                Logger.getLogger(directorGOT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
