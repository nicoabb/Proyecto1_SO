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
public class assemblerGOT extends Thread {

    private double dailyProduction = 0.5; // Un cap cada 2 dias
    private int chapterCounter = 1;
    private int cantIntrosGOT = 1; // 1 intro por capitulo
    private int cantBegsGOT = 1; // 1 inicio por capitulo
    private int cantEndsGOT = 2; // 2 cierres por capitulo
    private int cantCredsGOT = 1; // 1 creditos por capitulo
    private int cantPlotsGOT = 2; // 2 pt por capitulo (SOLO c/5)

    private boolean stop = false;

    private Semaphore assemblerMutexGOT; // Mutex de ensambladores de contenido, asi solo uno puede ensamblar a la vez

    // Semaforos de todos los productores
    // Semaforos para intros
    private Semaphore semIntroGOT; // Productor de intros
    private Semaphore semIntroMutexGOT; // Exclusividad al agarrar intros
    private Semaphore semEnsIntroGOT; // Consumidor de intros

    // Semaforos para creditos
    private Semaphore semCredGOT;
    private Semaphore semCredMutexGOT;
    private Semaphore semEnsCredGOT;

    // Semaforos para inicios
    private Semaphore semBegGOT;
    private Semaphore semBegMutexGOT;
    private Semaphore semEnsBegGOT;

    // Semaforos para cierres
    private Semaphore semEndGOT;
    private Semaphore semEndMutexGOT;
    private Semaphore semEnsEndGOT;

    // Semaforos para plot twists
    private Semaphore semPlotGOT;
    private Semaphore semPlotMutexGOT;
    private Semaphore semEnsPlotGOT;

    public assemblerGOT(Semaphore assemblerMutexGOT, Semaphore semIntroGOT, Semaphore semIntroMutexGOT, Semaphore semEnsIntroGOT, Semaphore semBegGOT, Semaphore semBegMutexGOT, Semaphore semEnsBegGOT, Semaphore semEndGOT, Semaphore semEndMutexGOT, Semaphore semEnsEndGOT, Semaphore semCredGOT, Semaphore semCredMutexGOT, Semaphore semEnsCredGOT, Semaphore semPlotGOT, Semaphore semPlotMutexGOT, Semaphore semEnsPlotGOT) {

        // Semaforo mutex para garantizar que solo un ensamblador trabaje a la vez
        this.assemblerMutexGOT = assemblerMutexGOT;

        this.semIntroGOT = semIntroGOT; // 
        this.semIntroMutexGOT = semIntroMutexGOT;
        this.semEnsIntroGOT = semEnsIntroGOT;

        // Semaforos para creditos
        this.semCredGOT = semCredGOT;
        this.semCredMutexGOT = semCredMutexGOT;
        this.semEnsCredGOT = semEnsCredGOT;

        // Semaforos para inicios
        this.semBegGOT = semBegGOT;
        this.semBegMutexGOT = semBegMutexGOT;
        this.semEnsBegGOT = semEnsBegGOT;

        // Semaforos para cierres
        this.semEndGOT = semEndGOT;
        this.semEndMutexGOT = semEndMutexGOT;
        this.semEnsEndGOT = semEnsEndGOT;

        // Semaforos para plot twists
        this.semPlotGOT = semPlotGOT;
        this.semPlotMutexGOT = semPlotMutexGOT;
        this.semEnsPlotGOT = semEnsPlotGOT;

    }

    @Override
    public void run() {
        while (!stop) {
            try {
                Thread.sleep(Math.round(Dashboard.dayDuration / dailyProduction));

                // Hacemos el wait en los semaforos donde adquirimos las cantidades
                semEnsIntroGOT.acquire(cantIntrosGOT);
                semEnsBegGOT.acquire(cantBegsGOT);
                semEnsCredGOT.acquire(cantCredsGOT);

                if (chapterCounter % 5 != 0) {
                    semEnsPlotGOT.acquire(cantPlotsGOT);
                } else {
                    semEnsEndGOT.acquire(cantEndsGOT);
                }

                // Actualizamos cantidades
                // Intros
                semIntroMutexGOT.acquire(); // Entro en CS de Intro
                Dashboard.introsProducedGOT -= cantIntrosGOT;
                Dashboard.qtyIntrosGOT.setText(Integer.toString(Dashboard.introsProducedGOT));
                semIntroMutexGOT.release(); // Salgo de CS de Intro
                semIntroGOT.release(cantIntrosGOT);

                // Beginnings
                semBegMutexGOT.acquire(); // Entro en CS de Beginnings
                Dashboard.begsProducedGOT -= cantBegsGOT;
                Dashboard.qtyBegsGOT.setText(Integer.toString(Dashboard.begsProducedGOT));
                semBegMutexGOT.release();
                semBegGOT.release(cantBegsGOT);

                // Credits
                semCredMutexGOT.acquire(); // Entro en CS de Endings
                Dashboard.credsProducedGOT -= cantCredsGOT;
                Dashboard.qtyProdCredsGOTLabel.setText(Integer.toString(Dashboard.credsProducedGOT));
                semCredMutexGOT.release();
                semCredGOT.release(cantCredsGOT);

                if (chapterCounter % 5 == 0) { // Si es un episodio multiplo de 5 tocan 2 plot
                    // Plot twists
                    semPlotMutexGOT.acquire();
                    Dashboard.plotsProducedGOT -= cantPlotsGOT;
                    Dashboard.qtyProdPlotsGOTLabel.setText(Integer.toString(Dashboard.plotsProducedGOT));
                    semPlotMutexGOT.release();
                    semPlotGOT.release(cantPlotsGOT);
                } else { // Si no es un episodio multiplo de 5 sale 2 endings normales
                    // Endings (cierres)
                    semEndMutexGOT.acquire(); // Entro en CS de Endings
                    Dashboard.endsProducedGOT -= cantEndsGOT;
                    Dashboard.qtyEndsGOT.setText(Integer.toString(Dashboard.endsProducedGOT));
                    semEndMutexGOT.release();
                    semEndGOT.release(cantEndsGOT);
                }
                
                assemblerMutexGOT.acquire();
                Dashboard.chaptersProducedGOT ++;
                Dashboard.chaptersMade.setText(Integer.toString(Dashboard.chaptersProducedGOT));
                chapterCounter++;
                assemblerMutexGOT.release();
            } catch (InterruptedException ex) {
                Logger.getLogger(assemblerGOT.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
}
