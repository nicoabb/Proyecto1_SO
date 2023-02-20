/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Classes.AssemblerTLOU;
import Classes.ProdEndTLOU;
import Classes.ProdBegTLOU;
import Classes.ProdCreditTLOU;
import Classes.ProdIntroTLOU;
import Classes.ProdPlotTLOU;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Nicolás Briceño
 */
public class Dashboard extends javax.swing.JFrame {
    
    private boolean start;
    private int dayDuration;
    private int maxProdsTLOU; //Cantidad máxima de productores TLOU
    
    //Espacio en Drive
    public static volatile int introDriveTLOU = 0;
    public static volatile int creditDriveTLOU = 0;
    public static volatile int begDriveTLOU = 0;
    public static volatile int endDriveTLOU = 0;
    public static volatile int plotDriveTLOU = 0;
    public static volatile int chaptersTLOU = 0;
    
    //Datos del Productor de Intro TLOU
    private ProdIntroTLOU prodIntroTLOU;
    private int introMaxDriveTLOU;
    private Semaphore mutexIntroTLOU, semIntroTLOU, semAssemIntroTLOU;
    
    //Datos del Productor de Creditos TLOU
    private ProdCreditTLOU prodCreditTLOU;
    private int creditMaxDriveTLOU;
    private Semaphore mutexCreditTLOU, semCreditTLOU, semAssemCreditTLOU;
    
    //Datos del Productor de Inicio (Beggining) TLOU
    private ProdBegTLOU prodBegTLOU;
    private int begMaxDriveTLOU;
    private Semaphore mutexBegTLOU, semBegTLOU, semAssemBegTLOU;
    
    //Datos del Productor de Cierre (End) TLOU
    private ProdEndTLOU prodEndTLOU;
    private int endMaxDriveTLOU;
    private Semaphore mutexEndTLOU, semEndTLOU, semAssemEndTLOU;
    
    //Datos del Productor de Plot (Plot Twist) TLOU
    private ProdPlotTLOU prodPlotTLOU;
    private int plotMaxDriveTLOU;
    private Semaphore mutexPlotTLOU, semPlotTLOU, semAssemPlotTLOU;
    
    //Datos del Ensamblador TLOU
    private AssemblerTLOU assemblerTLOU;
    private Semaphore mutexAssembler;
    
    /**
     * Creates new form Dashboard
     */
    public Dashboard() {
        initComponents();
        inicializarTLOU();
    }
    
    public void inicializarTLOU() {
        //Con el JSON tienen que cambiarse los valores de abajo
        this.dayDuration = 1;
        this.introMaxDriveTLOU = 30;
        this.creditMaxDriveTLOU = 25;
        this.begMaxDriveTLOU = 50;
        this.endMaxDriveTLOU = 55;
        this.plotMaxDriveTLOU = 40;
        
        //Creando ProdIntroTLOU
        this.mutexIntroTLOU = new Semaphore(1);
        this.semIntroTLOU = new Semaphore(introMaxDriveTLOU);
        this.semAssemIntroTLOU = new Semaphore(0);
        this.prodIntroTLOU = new ProdIntroTLOU(dayDuration, mutexIntroTLOU, semIntroTLOU, semAssemIntroTLOU);
        prodIntroTLOU.start();
        
        //Creando ProdCreditTLOU
        this.mutexCreditTLOU = new Semaphore(1);
        this.semCreditTLOU = new Semaphore(creditMaxDriveTLOU);
        this.semAssemCreditTLOU = new Semaphore(0);
        this.prodCreditTLOU = new ProdCreditTLOU(dayDuration, mutexCreditTLOU, semCreditTLOU, semAssemCreditTLOU);
        prodCreditTLOU.start();
        
        //Creando ProdBegTLOU
        this.mutexBegTLOU = new Semaphore(1);
        this.semBegTLOU = new Semaphore(begMaxDriveTLOU);
        this.semAssemBegTLOU = new Semaphore(0);
        this.prodBegTLOU = new ProdBegTLOU(dayDuration, mutexBegTLOU, semBegTLOU, semAssemBegTLOU);
        prodBegTLOU.start();
        
        //Creando ProdEndTLOU
        this.mutexEndTLOU = new Semaphore(1);
        this.semEndTLOU = new Semaphore(endMaxDriveTLOU);
        this.semAssemEndTLOU = new Semaphore(0);
        this.prodEndTLOU = new ProdEndTLOU(dayDuration, mutexEndTLOU, semEndTLOU, semAssemEndTLOU);
        prodEndTLOU.start();
        
        //Creando ProdPlotTLOU
        this.mutexPlotTLOU = new Semaphore(1);
        this.semPlotTLOU = new Semaphore(plotMaxDriveTLOU);
        this.semAssemPlotTLOU = new Semaphore(0);
        this.prodPlotTLOU = new ProdPlotTLOU(dayDuration, mutexPlotTLOU, semPlotTLOU, semAssemPlotTLOU);
        prodPlotTLOU.start();
        
        //Creando Ensamblador TLOU
        this.mutexAssembler = new Semaphore(1);
        this.assemblerTLOU = new AssemblerTLOU(dayDuration, mutexAssembler, mutexIntroTLOU, semIntroTLOU, semAssemIntroTLOU, mutexBegTLOU, semBegTLOU, semAssemBegTLOU, mutexEndTLOU, semEndTLOU, semAssemEndTLOU, mutexCreditTLOU, semCreditTLOU, semAssemCreditTLOU, mutexPlotTLOU, semPlotTLOU, semAssemPlotTLOU);
        assemblerTLOU.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        numIntroTLOU = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        numCreditTLOU = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        numBegTLOU = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        numEndTLOU = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        numPlotTLOU = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        numChapters = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setText("Créditos:");

        jLabel3.setText("Intros:");

        jLabel4.setText("Inicio:");

        jLabel5.setText("Cierre:");

        jLabel6.setText("Plot:");

        jLabel7.setText("Ensamblador:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(201, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(numIntroTLOU, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numCreditTLOU, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numBegTLOU, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numEndTLOU, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numPlotTLOU, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numChapters, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(numIntroTLOU, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 1, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(numCreditTLOU, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numBegTLOU, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numEndTLOU, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numPlotTLOU, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(numChapters, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(64, Short.MAX_VALUE))
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    public static javax.swing.JLabel numBegTLOU;
    public static javax.swing.JLabel numChapters;
    public static javax.swing.JLabel numCreditTLOU;
    public static javax.swing.JLabel numEndTLOU;
    public static javax.swing.JLabel numIntroTLOU;
    public static javax.swing.JLabel numPlotTLOU;
    // End of variables declaration//GEN-END:variables
}
