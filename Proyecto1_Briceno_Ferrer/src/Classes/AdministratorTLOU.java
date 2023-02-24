/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Interfaces.TLOUInterface;

/**
 *
 * @author Nicolás Briceño
 */
public class AdministratorTLOU extends Thread{
    
    private boolean start;
    private int dayDuration;
    private int backupDayDuration;
    private int eqHour;

    public AdministratorTLOU(boolean start, int dayDuration) {
        this.start = start;
        this.dayDuration = (dayDuration * 1000);
        this.backupDayDuration = dayDuration;
        this.eqHour = this.dayDuration / 24;
    }
    
    @Override
    public void run() {
        while(start){
            
            try {
                while(dayDuration > 0){
                    Thread.sleep(eqHour); //Pasa 1 hora y paga
                    
                    TLOUInterface.costIntro += (5 * TLOUInterface.introProdTLOU);
                    TLOUInterface.costIntroLabel.setText("$ " + Integer.toString(TLOUInterface.costIntro));
                    TLOUInterface.costCredit += (3 * TLOUInterface.creditProdTLOU);
                    TLOUInterface.costCreditLabel.setText("$ " + Integer.toString(TLOUInterface.costCredit));
                    TLOUInterface.costBeg += (7 * TLOUInterface.begProdTLOU);
                    TLOUInterface.costBegLabel.setText("$ " + Integer.toString(TLOUInterface.costBeg));
                    TLOUInterface.costEnd += (7.5 * TLOUInterface.endProdTLOU);
                    TLOUInterface.costEndLabel.setText("$ " + Integer.toString(TLOUInterface.costEnd));
                    TLOUInterface.costPlot += (10 * TLOUInterface.plotProdTLOU);
                    TLOUInterface.costPlotLabel.setText("$ " + Integer.toString(TLOUInterface.costPlot));
                    TLOUInterface.costAssembler += (8 * TLOUInterface.assemblersTLOU);
                    TLOUInterface.costAssemblerLabel.setText("$ " + Integer.toString(TLOUInterface.costAssembler));
                    TLOUInterface.salaryPM += 7;
                    TLOUInterface.costPMTLOU.setText("$ " + Integer.toString(TLOUInterface.salaryPM));
                    
                    dayDuration -= eqHour;
                }
                
                //Pasa 1 día y le pagan
                TLOUInterface.salaryDir += 100;
                TLOUInterface.costDirTLOU.setText("$ " + Integer.toString(TLOUInterface.salaryDir));
                //Se restablecen las 24 horas
                dayDuration = backupDayDuration;
                
            } catch (InterruptedException ex) {
            }
            
        }
    }

    public void setStart(boolean start) {
        this.start = start;
    }
    
}
