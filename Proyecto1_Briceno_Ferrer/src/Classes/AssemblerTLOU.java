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
    private Semaphore semEnsIntro; //Consumidor

    private Semaphore mutexBeg; 
    private Semaphore semBeg; //Productor
    private Semaphore semEnsBeg; //Consumidor

    private Semaphore mutexEnd;
    private Semaphore semEnd; //Productor
    private Semaphore semEnsEnd; //Consumidor
    
    private Semaphore mutexCredit;
    private Semaphore semCredit; //Productor
    private Semaphore semEnsCredit; //Consumidor
    
    private Semaphore mutexPlot;
    private Semaphore semPlot; //Productor
    private Semaphore semEnsPlot; //Consumidor

    public AssemblerTLOU(int dayDuration, Semaphore mutexAssembler, Semaphore mutexIntro, Semaphore semIntro, Semaphore semEnsIntro, Semaphore mutexBeg, Semaphore semBeg, Semaphore semEnsBeg, Semaphore mutexEnd, Semaphore semEnd, Semaphore semEnsEnd, Semaphore mutexCredit, Semaphore semCredit, Semaphore semEnsCredit, Semaphore mutexPlot, Semaphore semPlot, Semaphore semEnsPlot) {
        this.dayDuration = dayDuration;
        this.mutexAssembler = mutexAssembler;
        this.mutexIntro = mutexIntro;
        this.semIntro = semIntro;
        this.semEnsIntro = semEnsIntro;
        this.mutexBeg = mutexBeg;
        this.semBeg = semBeg;
        this.semEnsBeg = semEnsBeg;
        this.mutexEnd = mutexEnd;
        this.semEnd = semEnd;
        this.semEnsEnd = semEnsEnd;
        this.mutexCredit = mutexCredit;
        this.semCredit = semCredit;
        this.semEnsCredit = semEnsCredit;
        this.mutexPlot = mutexPlot;
        this.semPlot = semPlot;
        this.semEnsPlot = semEnsPlot;
    }
    
}
