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
public class AssemblerTLOU extends Thread{
    
    private int dayDuration;
    private double dailyProduce = 0.5; //Produce un capítulo cada 2 días
    private int numIntro = 1;
    private int numBeg = 2;
    private int numEnd = 2;
    private int numCredit = 1;
    private int numPlot = 2;
    private boolean stop;
    
    private Semaphore mutexAssembler;

    private Semaphore mutexIntro;
    private Semaphore semIntro; //Productor
    //private Semaphore semEnsIntro; //Consumidor

    private Semaphore mutexBeg; 
    private Semaphore semBeg; //Productor
    //private Semaphore semEnsBeg; //Consumidor

    private Semaphore mutexEnd;
    private Semaphore semEnd; //Productor
    //private Semaphore semEnsEnd; //Consumidor
    
    private Semaphore mutexCredit;
    private Semaphore semCredit; //Productor
    //private Semaphore semEnsCredit; //Consumidor
    
    private Semaphore mutexPlot;
    private Semaphore semPlot; //Productor
    //private Semaphore semEnsPlot; //Consumidor

    public AssemblerTLOU(int dayDuration, Semaphore mutexAssembler, Semaphore mutexIntro, Semaphore semIntro, Semaphore mutexBeg, Semaphore semBeg, Semaphore mutexEnd, Semaphore semEnd, Semaphore mutexCredit, Semaphore semCredit, Semaphore mutexPlot, Semaphore semPlot) {
        this.dayDuration = dayDuration;
        this.mutexAssembler = mutexAssembler;
        this.mutexIntro = mutexIntro;
        this.semIntro = semIntro;
        //this.semEnsIntro = semEnsIntro;
        this.mutexBeg = mutexBeg;
        this.semBeg = semBeg;
        //this.semEnsBeg = semEnsBeg;
        this.mutexEnd = mutexEnd;
        this.semEnd = semEnd;
        //this.semEnsEnd = semEnsEnd;
        this.mutexCredit = mutexCredit;
        this.semCredit = semCredit;
        //this.semEnsCredit = semEnsCredit;
        this.mutexPlot = mutexPlot;
        this.semPlot = semPlot;
        //this.semEnsPlot = semEnsPlot;
    }
    
    @Override
    public void run(){
        
        while(true){
            if (!this.stop) {
                try{
                    
                    //Retirar intro para el capítulo
                    mutexIntro.acquire(numIntro);
                    Dashboard.introDriveTLOU -= numIntro;
                    Dashboard.numChapters.setText(Integer.toString(Dashboard.introDriveTLOU));
                    mutexIntro.release();
                    semIntro.release(numIntro);
                    
                    //Retirar inicios (begginings) para el capítulo
                    mutexBeg.acquire(numBeg);
                    Dashboard.begDriveTLOU -= numBeg;
                    Dashboard.numChapters.setText(Integer.toString(Dashboard.begDriveTLOU));
                    mutexBeg.release();
                    semBeg.release(numBeg);
                    
                    //Retirar cierres (end) para el capítulo
                    mutexEnd.acquire(numEnd);
                    Dashboard.endDriveTLOU -= numEnd;
                    Dashboard.numChapters.setText(Integer.toString(Dashboard.endDriveTLOU));
                    mutexEnd.release();
                    semEnd.release(numEnd);
                    
                    if ((Dashboard.chaptersTLOU % 5) == 0) { //Cada 5 capítulos usar Plot Twist
                        
                        //Retirar Plot twist para el capítulo
                        mutexPlot.acquire(numPlot);
                        Dashboard.plotDriveTLOU -= numPlot;
                        Dashboard.numChapters.setText(Integer.toString(Dashboard.plotDriveTLOU));
                        mutexPlot.release();
                        semPlot.release(numPlot);
                        
}                   else {
                        
                        //Retirar creditos para el capítulo
                        mutexCredit.acquire(numCredit);
                        Dashboard.creditDriveTLOU -= numCredit;
                        Dashboard.numChapters.setText(Integer.toString(Dashboard.creditDriveTLOU));
                        mutexCredit.release();
                        semCredit.release(numCredit);
                        
                    }
                    
                    //Aumentar número de capítulos ensamblados
                    mutexAssembler.acquire();
                    Dashboard.chaptersTLOU += 1;
                    Dashboard.numChapters.setText(Integer.toString(Dashboard.chaptersTLOU));
                    mutexCredit.release();
                    
                }catch(Exception e){

                }
    
            }
        }
    }
}
