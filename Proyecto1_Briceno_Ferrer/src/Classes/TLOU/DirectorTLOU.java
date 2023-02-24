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
public class DirectorTLOU extends Thread{
    private int dayDuration;
    private boolean start;
    private Semaphore countMutex; 
    private Semaphore stateMutex; //Mutex del estado, para que no se cambie cuando el director lea
    private ProjectManagerTLOU proManager;
    private int eqHour; //Variable para trabajar las horas de acuerdo a la duración del día establecida
    private int eqMinute; //Variable para trabajar los minutos de acuerdo a la duración del día establecida
    
    public DirectorTLOU(boolean start, int dayDuration, Semaphore countMutex, Semaphore stateMutex, ProjectManagerTLOU proManager) {
        this.dayDuration = (dayDuration * 1000);
        this.start = start;
        this.countMutex = countMutex;
        this.stateMutex = stateMutex;
        this.proManager = proManager;
        this.eqHour = this.dayDuration / 24;
        this.eqMinute = this.dayDuration / 1440;
    }
    
    @Override
    public void run() {
        while(start){
            try {
                int randPeriod = getRandom(12,18);
                int checkPeriod =  randPeriod * eqHour; //Período en el que irá a revisar al PM
                int remainderTime = (24 - randPeriod) * eqHour; //El tiempo que le sobrará en el día
                
                //Director revisa el contador
                countMutex.acquire();
                if(TLOUInterface.counter <= 0) {
                    //COMENTAR EL DO CLICK SI QUIERES QUE DURE MÁS DE 1 CONTEO
                    TLOUInterface.stopButton.doClick();
                    
                    TLOUInterface.dirState.setText("Entregando capítulos a HBO MAX");
                    
                    //Reiniciando conteo
                    TLOUInterface.chaptersTLOU = 0;
                    TLOUInterface.numChapters.setText(Integer.toString(TLOUInterface.chaptersTLOU));
                    
                    //Actualizando capítulos totales
                    TLOUInterface.totalChaptersLabel.setText(Integer.toString(TLOUInterface.totalChapters));
                    
                    //Sumando las ganancias por capítulo
                    TLOUInterface.incomeChapters = TLOUInterface.totalChapters * ((1100000 / 150000) * 100000);
                    TLOUInterface.incomeChaptersLabel.setText("$ " + Integer.toString(TLOUInterface.incomeChapters));
                    TLOUInterface.counter = TLOUInterface.backupCounter;
                    TLOUInterface.daysCounter.setText(Integer.toString(TLOUInterface.counter));
                }
                countMutex.release();
                                                
                while(checkPeriod >= 0) {
                    int randTime = getRandom(30, 90); //Tiempo aleatorio entre 30 minutos y 90 minutos
                    int checkTime = randTime * eqMinute; //Tiempo que vigilará a PM (Ya en milisegundos)
                    Thread.sleep(checkTime);
                    
                    TLOUInterface.dirState.setText("Revisando al PM");
                    stateMutex.acquire();
                    if(proManager.state.contains("Viendo Rick y Morty")){
                        TLOUInterface.dirState.setText("¡Atrapado!");
                        TLOUInterface.numFaults++;
                        TLOUInterface.penalizationLabel.setText("$ " + Integer.toString(TLOUInterface.numFaults));
                    }
                    stateMutex.release();
                    
                    checkPeriod -= checkTime;
                }
                
                Thread.sleep(remainderTime); //Que duerma mientras no le toca revisar ni vigilar

            } catch (InterruptedException ex) {

            }
        
        }
    }
    
    public static int getRandom(int a, int b){
        int c = (int)(Math.random()*(b-a+1)+a);
        return c;
    }

    public void setStart(boolean start) {
        this.start = start;
    }
    
}
