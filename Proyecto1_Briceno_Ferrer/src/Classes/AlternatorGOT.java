/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import Interfaces.Dashboard;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author emilo
 */
public class AlternatorGOT extends Thread {

    private boolean bool;
    private ProjectManagerGOT pm;
    
    public AlternatorGOT(ProjectManagerGOT projectManager) {
        this.pm = projectManager;
        this.bool = bool;
    }
    
    @Override
    public void run() {
         try {
             Thread.sleep(Dashboard.dayDuration * 10/24);
             
             Thread.sleep(Dashboard.dayDuration * 14/24);
//             pm.waste = false;
        } catch (InterruptedException ex) {
            Logger.getLogger(AlternatorGOT.class.getName()).log(Level.SEVERE, null, ex);
        }   
        
    }
        
    
}
