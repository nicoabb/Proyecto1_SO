/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Classes.AlternatorGOT;
import Classes.ProjectManagerGOT;
import Classes.assemblerGOT;
import Classes.prodBegGOT;
import Classes.prodCredGOT;
import Classes.prodEndGOT;
import Classes.prodIntroGOT;
import Classes.prodPlotGOT;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Nicolás Briceño
 */
public class Dashboard extends javax.swing.JFrame {

    public static int dayDuration = 10000; // Duracion de un dia (ms)
    public static int cutDuration = 30;

//  Declarar productores GOT
    private prodIntroGOT prodIntroGOT;
    private prodCredGOT prodCredGOT;
    private prodBegGOT prodBegGOT;
    private prodEndGOT prodEndGOT;
    private prodPlotGOT prodPlotGOT;

//  Declarar semaforos GOT
    public Semaphore semIntroGOT, semCredGOT, semBegGOT, semEndGOT, semPlotGOT;

//  Declarar semaforos mutex GOT    
    public Semaphore semIntroMutexGOT, semCredMutexGOT, semBegMutexGOT, semEndMutexGOT, semPlotMutexGOT, semAssemblerMutexGOT;

//  Declarar semaforos ensamblador GOT
    public Semaphore semEnsIntroGOT, semEnsBegGOT, semEnsCredGOT, semEnsPlotGOT, semEnsEndGOT;
    
//  Declarar contadores GOT
    public static int credsProducedGOT, introsProducedGOT, begsProducedGOT, endsProducedGOT, plotsProducedGOT, chaptersProducedGOT, counterPMGOT = 0;

//  Declarar ensamblador GOT
    private assemblerGOT assemblerGOT;
    
//  Declarar PM
    private Semaphore counter;
    private ProjectManagerGOT pmGOT;
    private AlternatorGOT alternator;
    
    /**
     * Creates new form Dashboard
     */
    public Dashboard() {
        initComponents();
        instantiate();

    }

    public void instantiate() {

        //Instanciar al productor de intro
        this.semIntroGOT = new Semaphore(30); // REVISAR ESE 30, tiene que ver con el tamanio del drive
        this.semIntroMutexGOT = new Semaphore(1);
        this.semEnsIntroGOT = new Semaphore(0);
        this.prodIntroGOT = new prodIntroGOT(semIntroMutexGOT, semIntroGOT, semEnsIntroGOT);
        this.prodIntroGOT.start();

        //Instanciar al productor de creditos
        this.semCredGOT = new Semaphore(25);
        this.semCredMutexGOT = new Semaphore(1);
        this.semEnsCredGOT = new Semaphore(0);
        this.prodCredGOT = new prodCredGOT(semCredMutexGOT, semCredGOT, semEnsCredGOT);
        this.prodCredGOT.start();

        //Instanciar al productor de inicios
        this.semBegGOT = new Semaphore(50);
        this.semBegMutexGOT = new Semaphore(1);
        this.semEnsBegGOT = new Semaphore(0);
        this.prodBegGOT = new prodBegGOT(semBegMutexGOT, semBegGOT, semEnsBegGOT);
        this.prodBegGOT.start();

        //Instanciar al productor de cierres
        this.semEndGOT = new Semaphore(55);
        this.semEndMutexGOT = new Semaphore(1);
        this.semEnsEndGOT = new Semaphore(0);
        this.prodEndGOT = new prodEndGOT(semEndMutexGOT, semEndGOT, semEnsEndGOT);
        this.prodEndGOT.start();

        //Instanciar al productor de plot twists
        this.semPlotGOT = new Semaphore(40);
        this.semPlotMutexGOT = new Semaphore(1);
        this.semEnsPlotGOT = new Semaphore(0);
        this.prodPlotGOT = new prodPlotGOT(semPlotMutexGOT, semPlotGOT, semEnsPlotGOT);
        this.prodPlotGOT.start();
        
        //Instanciar al ensamblador
        this.semAssemblerMutexGOT = new Semaphore(1);
        this.assemblerGOT = new assemblerGOT(semAssemblerMutexGOT, semIntroGOT, semIntroMutexGOT,  semEnsIntroGOT,  semBegGOT,  semBegMutexGOT,  semEnsBegGOT,  semEndGOT,  semEndMutexGOT,  semEnsEndGOT,  semCredGOT,  semCredMutexGOT,  semEnsCredGOT,  semPlotGOT,  semPlotMutexGOT,  semEnsPlotGOT);
        this.assemblerGOT.start();
        
        //Instanciar el PM
        this.counter = new Semaphore(cutDuration);
        this.pmGOT = new ProjectManagerGOT(counter);
//        this.alternator = new AlternatorGOT(this.pmGOT);
        this.pmGOT.start();
        
//        this.alternator.start();
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        qtyIntrosGOT = new javax.swing.JLabel();
        qtyBegsGOT = new javax.swing.JLabel();
        qtyEndsGOT = new javax.swing.JLabel();
        qtyPlotsGOT = new javax.swing.JLabel();
        qtyCredsGOT = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        chaptersMade = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        daysUntilCut = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel7.setText("Intros disponibles");

        jLabel8.setText("Inicios disponibles");

        jLabel9.setText("Cierres disponibles");

        jLabel10.setText("Creditos disponibles");

        jLabel11.setText("Plots disponibles");

        jLabel1.setText("Capitulos hechos");

        jLabel2.setText("Dias para el corte");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(qtyEndsGOT, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(qtyPlotsGOT, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(qtyCredsGOT, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(qtyIntrosGOT, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(qtyBegsGOT, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 126, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(39, 39, 39))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(18, 18, 18)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chaptersMade, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(daysUntilCut, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(578, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(qtyIntrosGOT, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chaptersMade, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(qtyBegsGOT, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(114, 114, 114)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(daysUntilCut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(qtyEndsGOT, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(qtyPlotsGOT, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(qtyCredsGOT, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(116, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JLabel chaptersMade;
    public static javax.swing.JLabel daysUntilCut;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    public static javax.swing.JLabel qtyBegsGOT;
    public static javax.swing.JLabel qtyCredsGOT;
    public static javax.swing.JLabel qtyEndsGOT;
    public static javax.swing.JLabel qtyIntrosGOT;
    public static javax.swing.JLabel qtyPlotsGOT;
    // End of variables declaration//GEN-END:variables
}
