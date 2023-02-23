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
public class ProjectManagerTLOU extends Thread{
    
    private int dayDuration;
    private boolean stop;
    private Semaphore countMutex; 
    private int remainderTime;  //Cantidad de horas para alternar entre ver Rick y Morty y revisar sprints reviews
    private String state; //Estado del PM (RyM o sprints reviews)
    private Semaphore stateMutex; //Mutex del estado, para que no se cambie cuando el director lea
    
    public ProjectManagerTLOU(boolean stop, int dayDuration, Semaphore countMutex, Semaphore stateMutex) {
        this.dayDuration = (dayDuration * 1000);
        this.stop = stop;
        this.countMutex = countMutex;
        this.stateMutex = stateMutex;
        this.remainderTime = this.dayDuration - 1000;
        
    }
    
    @Override
    public void run() {
        while(!stop){
            try {
                //Project Manager cambia el contador
                
                countMutex.acquire();
                
                stateMutex.acquire();
                state = "Cambiando el contador";
                Dashboard.pmState.setText(state);
                stateMutex.release();
                
                Dashboard.counter--;
                Dashboard.daysCounter.setText(Integer.toString(Dashboard.counter));
                Thread.sleep(1000);
                
                countMutex.release();
                
                while(remainderTime >= 0){
                    
                    stateMutex.acquire();
                    state = "Viendo Rick y Morty";
                    Dashboard.pmState.setText(state);
                    stateMutex.release();
                    
                    Thread.sleep(250); //1000 ms es 1 hora, 1/4 de hora son 15 minutos, de ahí los 250 ms
                    remainderTime -= 250;
                    
                    stateMutex.acquire();
                    state = "Revisando sprints reviews";
                    Dashboard.pmState.setText(state);
                    stateMutex.release();
                    
                    Thread.sleep(250);
                    remainderTime -= 250;
                    
                }
                
                remainderTime = dayDuration - 1000;

            } catch (InterruptedException ex) {

            }
        
        }
    }
    
    
}
