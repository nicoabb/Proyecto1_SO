/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.util.concurrent.Semaphore;

/**
 *
 * @author Nicolás Briceño
 */
public class ProjectManagerTLOU {
    
    private int dayDuration;
    private int counter; //Días para la comparación
    private Semaphore countMutex; 
    private int countChangeTime; //Tiempo que tarda el PM en cambiar el contador
    private int remainderTime;  //Cantidad de horas para alternar entre ver Rick y Morty y revisar sprints reviews
    private String state; //Estado del PM (RyM o sprints reviews)
    private Semaphore stateMutex; //Mutex del estado, para que no se cambie cuando el director lea
    
    public ProjectManagerTLOU(int dayDuration, int counter, Semaphore countMutex, int countChangeTime, int remainderTime) {
        this.dayDuration = dayDuration;
        this.counter = counter;
        this.countMutex = countMutex;
        this.countChangeTime = countChangeTime;
        this.remainderTime = dayDuration - countChangeTime;
    }
    
    
    
    
}
