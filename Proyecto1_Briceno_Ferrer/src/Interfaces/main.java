/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

/**
 *
 * @author Nicolás Briceño
 */
public class main extends javax.swing.JFrame {
    
    TLOUInterface tlou = new TLOUInterface();
    Dashboard dash = new Dashboard();
    GOTInterface got = new GOTInterface();
    /**
     * Creates new form Dashboard
     */
    public main() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        GOTbutton = new javax.swing.JToggleButton();
        TLOUbutton = new javax.swing.JToggleButton();
        DashboardButton = new javax.swing.JToggleButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Proyecto 1 Sistemas Operativos");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 100, -1, -1));

        GOTbutton.setText("Empresa GOT (Emilio)");
        GOTbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GOTbuttonActionPerformed(evt);
            }
        });
        getContentPane().add(GOTbutton, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 170, 180, -1));

        TLOUbutton.setText("Empresa TLOU (Nico)");
        TLOUbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TLOUbuttonActionPerformed(evt);
            }
        });
        getContentPane().add(TLOUbutton, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 210, 180, -1));

        DashboardButton.setText("Dashboard");
        DashboardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DashboardButtonActionPerformed(evt);
            }
        });
        getContentPane().add(DashboardButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 250, 180, -1));

        jLabel2.setText("Emilio Ferrer y Nicolas Briceño. Febrero 2023");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 130, 240, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/micaracuando.png"))); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 196, 215));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TLOUbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TLOUbuttonActionPerformed
        tlou.setVisible(true);
        dash.setVisible(false);
        got.setVisible(false);
        
        
    }//GEN-LAST:event_TLOUbuttonActionPerformed

    private void DashboardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DashboardButtonActionPerformed
        tlou.setVisible(false);
        dash.setVisible(true);
        got.setVisible(false);
    }//GEN-LAST:event_DashboardButtonActionPerformed

    private void GOTbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GOTbuttonActionPerformed
        tlou.setVisible(false);
        dash.setVisible(false);
        got.setVisible(true);
        
    }//GEN-LAST:event_GOTbuttonActionPerformed

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
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton DashboardButton;
    private javax.swing.JToggleButton GOTbutton;
    private javax.swing.JToggleButton TLOUbutton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables
}
