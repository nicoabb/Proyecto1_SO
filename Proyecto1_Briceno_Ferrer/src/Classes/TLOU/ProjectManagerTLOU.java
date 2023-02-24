/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes.TLOU;

import Interfaces.TLOUInterface;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Nicolás Briceño
 */
public class ProjectManagerTLOU extends Thread{
    
    private int dayDuration;
    private boolean start;
    private Semaphore countMutex; 
    private int remainderTime;  //Cantidad de horas para alternar entre ver Rick y Morty y revisar sprints reviews
    public String state; //Estado del PM (RyM o sprints reviews)
    private Semaphore stateMutex; //Mutex del estado, para que no se cambie cuando el director lea
    private int eqHour; //Horas equivalentes para la duración del día
    private int eqMinute; //Minutos equivalentes para la duración del día
    
    public ProjectManagerTLOU(boolean start, int dayDuration, Semaphore countMutex, Semaphore stateMutex) {
        this.dayDuration = (dayDuration * 1000);
        this.start = start;
        this.countMutex = countMutex;
        this.stateMutex = stateMutex;
        this.eqHour = this.dayDuration / 24;
        this.eqMinute = this.dayDuration / 1440;
        this.remainderTime = this.dayDuration - eqHour;
        
    }
    
    @Override
    public void run() {
        while(start){
            try {
                //Project Manager cambia el contador
                
                countMutex.acquire();
                
                stateMutex.acquire();
                state = "Cambiando el contador";
                TLOUInterface.pmState.setText(state);
                stateMutex.release();
                
                TLOUInterface.counter--;
                TLOUInterface.daysCounter.setText(Integer.toString(TLOUInterface.counter));
                Thread.sleep(eqHour); //En TLOU es 1 hora
                
                countMutex.release();
                
                while(remainderTime >= 0){
                    
                    stateMutex.acquire();
                    state = "Viendo Rick y Morty";
                    TLOUInterface.pmState.setText(state);
                    stateMutex.release();
                    
                    Thread.sleep(eqMinute * 15); //15 minutos
                    remainderTime -= (eqMinute * 15);
                    
                    stateMutex.acquire();
                    state = "Revisando sprints reviews";
                    TLOUInterface.pmState.setText(state);
                    stateMutex.release();
                    
                    Thread.sleep(eqMinute * 15); //15 minutos
                    remainderTime -= (eqMinute * 15);
                    
                }
                
                remainderTime = dayDuration - eqHour;

            } catch (InterruptedException ex) {

            }
        
        }
    }

    public void setStart(boolean start) {
        this.start = start;
    }
    
    
    
}
