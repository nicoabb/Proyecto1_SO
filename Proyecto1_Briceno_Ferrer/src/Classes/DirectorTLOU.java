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
public class DirectorTLOU extends Thread{
    private int dayDuration;
    private boolean stop;
    private Semaphore countMutex; 
    private Semaphore stateMutex; //Mutex del estado, para que no se cambie cuando el director lea
    private ProjectManagerTLOU proManager;
    
    public DirectorTLOU(boolean stop, int dayDuration, Semaphore countMutex, Semaphore stateMutex, ProjectManagerTLOU proManager) {
        this.dayDuration = (dayDuration * 1000);
        this.stop = stop;
        this.countMutex = countMutex;
        this.stateMutex = stateMutex;
        this.proManager = proManager;
    }
    
    @Override
    public void run() {
        while(!stop){
            try {
                //Director revisa el contador
                
                countMutex.acquire();
                
                if(Dashboard.counter == 0) {
                    Dashboard.dirState.setText("Entregando capítulos a HBO MAX");
                    Dashboard.chaptersTLOU = 0;
                    Dashboard.counter = Dashboard.backupCounter;
                }
                
                countMutex.release();
                
                int checkPeriod = getRandom(12,18) * 1000; //Período en el que irá a revisar al PM
                
                while(checkPeriod >= 0) {
                    int checkTime = getRandom(500, 1500); //Tiempo que vigilará a PM (Ya en milisegundos)
                    Thread.sleep(checkTime);
                    
                    Dashboard.dirState.setText("Revisando al PM");
                    stateMutex.acquire();
                    if(proManager.state.contains("Viendo Rick y Morty")){
                        Dashboard.dirState.setText("¡Atrapado!");
                    }
                    stateMutex.release();
                    
                    checkPeriod -= checkTime;
                }

            } catch (InterruptedException ex) {

            }
        
        }
    }
    
    public static int getRandom(int a, int b){
        int c = (int)(Math.random()*(b-a+1)+a);
        return c;
    }
}
