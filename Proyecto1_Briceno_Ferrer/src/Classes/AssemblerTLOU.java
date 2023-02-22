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
    private boolean stop = false;
    
    private Semaphore mutexAssembler;

    private Semaphore mutexIntro;
    private Semaphore semIntro; //Productor
    private Semaphore semAssemIntro; //Consumidor

    private Semaphore mutexBeg; 
    private Semaphore semBeg; //Productor
    private Semaphore semAssemBeg; //Consumidor

    private Semaphore mutexEnd;
    private Semaphore semEnd; //Productor
    private Semaphore semAssemEnd; //Consumidor
    
    private Semaphore mutexCredit;
    private Semaphore semCredit; //Productor
    private Semaphore semAssemCredit; //Consumidor
    
    private Semaphore mutexPlot;
    private Semaphore semPlot; //Productor
    private Semaphore semAssemPlot; //Consumidor

    public AssemblerTLOU(int dayDuration, Semaphore mutexAssembler, Semaphore mutexIntro, Semaphore semIntro, Semaphore semEnsIntro, Semaphore mutexBeg, Semaphore semBeg, Semaphore semEnsBeg, Semaphore mutexEnd, Semaphore semEnd, Semaphore semEnsEnd, Semaphore mutexCredit, Semaphore semCredit, Semaphore semEnsCredit, Semaphore mutexPlot, Semaphore semPlot, Semaphore semEnsPlot) {
        this.dayDuration = dayDuration;
        this.mutexAssembler = mutexAssembler;
        this.mutexIntro = mutexIntro;
        this.semIntro = semIntro;
        this.semAssemIntro = semEnsIntro;
        this.mutexBeg = mutexBeg;
        this.semBeg = semBeg;
        this.semAssemBeg = semEnsBeg;
        this.mutexEnd = mutexEnd;
        this.semEnd = semEnd;
        this.semAssemEnd = semEnsEnd;
        this.mutexCredit = mutexCredit;
        this.semCredit = semCredit;
        this.semAssemCredit = semEnsCredit;
        this.mutexPlot = mutexPlot;
        this.semPlot = semPlot;
        this.semAssemPlot = semEnsPlot;
    }
    
    @Override
    public void run(){
        while(!stop){
            try{
                //Entrar a los Drives de cada parte
                semAssemIntro.acquire(numIntro);
                semAssemBeg.acquire(numBeg);
                semAssemEnd.acquire(numEnd);
                semAssemPlot.acquire(numPlot);
                semAssemCredit.acquire(numCredit);

                //Retirar intro para el capítulo
                mutexIntro.acquire();
                Dashboard.introDriveTLOU -= numIntro;
                Dashboard.numIntroTLOU.setText(Integer.toString(Dashboard.introDriveTLOU));
                mutexIntro.release();
                semIntro.release(numIntro);

                //Retirar inicios (begginings) para el capítulo
                mutexBeg.acquire(); 
                Dashboard.begDriveTLOU -= numBeg;
                Dashboard.numBegTLOU.setText(Integer.toString(Dashboard.begDriveTLOU));
                mutexBeg.release();
                semBeg.release(numBeg);

                //Retirar cierres (end) para el capítulo
                mutexEnd.acquire();
                Dashboard.endDriveTLOU -= numEnd;
                Dashboard.numEndTLOU.setText(Integer.toString(Dashboard.endDriveTLOU));
                mutexEnd.release();
                semEnd.release(numEnd);
                
                if (Dashboard.chaptersTLOU > 0 && ((Dashboard.chaptersTLOU % 4) == 0)) { //Cada 4 capítulos usar Plot Twist (para el 5to)
                    
                    //Retirar Plot twist para el capítulo
                    mutexPlot.acquire();
                    Dashboard.plotDriveTLOU -= numPlot;
                    Dashboard.numPlotTLOU.setText(Integer.toString(Dashboard.plotDriveTLOU));
                    mutexPlot.release();
                    semPlot.release(numPlot);

                }else {

                    //Retirar creditos para el capítulo
                    mutexCredit.acquire();
                    Dashboard.creditDriveTLOU -= numCredit;
                    Dashboard.numCreditTLOU.setText(Integer.toString(Dashboard.creditDriveTLOU));
                    mutexCredit.release();
                    semCredit.release(numCredit);

                }

                //Aumentar número de capítulos ensamblados
                mutexAssembler.acquire();
                Thread.sleep(Math.round((dayDuration * 1000) / dailyProduce));
                Dashboard.chaptersTLOU += 1;
                Dashboard.numChapters.setText(Integer.toString(Dashboard.chaptersTLOU));
                mutexAssembler.release();

            }catch(Exception e){

            }
        }
    }
}
