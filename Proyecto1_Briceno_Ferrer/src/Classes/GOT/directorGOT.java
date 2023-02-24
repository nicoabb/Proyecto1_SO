/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.GOT;

import Interfaces.GOTInterface;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Emilio Ferrer
 */
public class directorGOT extends Thread {

    private ProjectManagerGOT pm;
    private Semaphore counter;
    private Semaphore counterMutex;
    private boolean stop;

    public directorGOT(ProjectManagerGOT pm, Semaphore counter, Semaphore counterMutex) {
        this.pm = pm;
        this.counter = counter;
        this.counterMutex = counterMutex;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                double guardLength = (Math.random() * (GOTInterface.dayDuration * 0.75 - GOTInterface.dayDuration / 2)) + GOTInterface.dayDuration / 2;
                counterMutex.acquire(); // Trata de agarrar exclusividad sobre el semaforo del contador
                if (counter.availablePermits() <= 0) {

                    GOTInterface.salariesProds = GOTInterface.calculateProdSalaries();
                    GOTInterface.assemblersSalaries = GOTInterface.calculateAssemblerSalary();
                    GOTInterface.pmSalary = GOTInterface.calculatePMSalary();
                    GOTInterface.directorSalary = GOTInterface.calculateDirectorSalary();
                    GOTInterface.salariesTotal = GOTInterface.allSalaries();

                    GOTInterface.prodSalariesLabel.setText(Double.toString(GOTInterface.salariesProds));
                    GOTInterface.assemblersSalariesLabel.setText(Double.toString(GOTInterface.assemblersSalaries));
                    GOTInterface.pmSalaryLabel.setText(Double.toString(GOTInterface.pmSalary));
                    GOTInterface.RaMLabel.setText(Integer.toString(GOTInterface.amountRaM));
                    GOTInterface.salariesLabel.setText("-"+Double.toString(GOTInterface.salariesTotal));

                    GOTInterface.earnings = GOTInterface.calculateEarnings();
                    GOTInterface.earningsLabel.setText("+" + Double.toString(GOTInterface.earnings));
                    GOTInterface.balance = GOTInterface.earnings - GOTInterface.salariesTotal;
                    if (GOTInterface.earnings > GOTInterface.salariesTotal) {
                        GOTInterface.balanceLabel.setText("+" + Double.toString(GOTInterface.balance));
                    } else {
                        GOTInterface.balanceLabel.setText("-" + Double.toString(GOTInterface.balance));
                    };

//                    counter.release(30);
                    for (int i = 0; i < 10; i++) {
                        if (GOTInterface.arrayAssemblersGOT[i] != null) {
                            GOTInterface.arrayAssemblersGOT[i].setStop(true);
                        }
                    }
                    for (int i = 0; i < 19; i++) {
                        if (GOTInterface.arrayBegGOT[i] != null) {
                            GOTInterface.arrayBegGOT[i].setStop(true);
                        }
                        if (GOTInterface.arrayCredGOT[i] != null) {
                            GOTInterface.arrayCredGOT[i].setStop(true);
                        }
                        if (GOTInterface.arrayEndGOT[i] != null) {
                            GOTInterface.arrayEndGOT[i].setStop(true);
                        }
                        if (GOTInterface.arrayIntroGOT[i] != null) {
                            GOTInterface.arrayIntroGOT[i].setStop(true);
                        }
                        if (GOTInterface.arrayPlotGOT[i] != null) {
                            GOTInterface.arrayPlotGOT[i].setStop(true);
                        }
                    }
                    GOTInterface.pmGOT.setStop(true);

                    this.setStop(true);
                }
                counterMutex.release();
                double elapsedTime = 0;
                double startTime = System.currentTimeMillis();
                while (elapsedTime < guardLength) {

                    double timeBetweenCheck = Math.random() * ((1.5 * GOTInterface.hourDuration) - (0.5 * GOTInterface.hourDuration)) + 0.5 * GOTInterface.hourDuration;
                    Thread.sleep(Math.round(timeBetweenCheck));
                    // Aqui va y ve qué está haciendo el PM
//                    System.out.println("Revisando...");
                    if (pm.watchingRaM) {
                        GOTInterface.amountRaM++;
//                        System.out.println("Está viendo Rikimorti el PM!!!");
                    }
                    elapsedTime = System.currentTimeMillis() - startTime;

                }
//                System.out.println("descansando");

                Thread.sleep((GOTInterface.dayDuration - Math.round(guardLength)));
//                System.out.println("ahora a repetir el ciclo");
            } catch (InterruptedException ex) {
                Logger.getLogger(directorGOT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

}
