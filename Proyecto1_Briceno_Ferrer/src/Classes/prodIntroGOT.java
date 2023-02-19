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
public class prodIntroGOT extends Thread {

    private Semaphore mutex;
    private String textToInterface = "Lorem Ipsum";
    private boolean stop = false;
    private Semaphore semIntro;
    private int introsPerDay = 3;
    
    public prodIntroGOT(Semaphore mutex, Semaphore semIntro) {
        this.mutex = mutex;
        this.semIntro = semIntro;
    }
    
    public void run() {
        while(!stop) {
                try {
                    semIntro.acquire();
                    Thread.sleep(24000 / introsPerDay); // Aqui espera 8 horas (que le toma hacer una intro) y luego 
                    
                    mutex.acquire();
                    System.out.println("permisos disponibles: "+ semIntro.availablePermits());
                    Interfaces.Dashboard.introsProduced++;
                    Interfaces.Dashboard.cantIntros.setText(Integer.toString(Interfaces.Dashboard.introsProduced));
                    
                    mutex.release();
//                    semIntro.release();
                } catch (InterruptedException ex) {
                    Logger.getLogger(prodIntroGOT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

    }
    
    public void saluda() {
        System.out.println("PROBANDO!!!");
        Dashboard.labelIntro.setText(textToInterface);
    }
    
}
