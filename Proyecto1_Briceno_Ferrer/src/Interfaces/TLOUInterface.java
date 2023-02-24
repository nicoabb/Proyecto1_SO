/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Classes.AssemblerTLOU;
import Classes.DirectorTLOU;
import Classes.ProdEndTLOU;
import Classes.ProdBegTLOU;
import Classes.ProdCreditTLOU;
import Classes.ProdIntroTLOU;
import Classes.ProdPlotTLOU;
import Classes.ProjectManagerTLOU;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Nicolás Briceño
 */
public class TLOUInterface extends javax.swing.JFrame {
    
    private boolean stop;
    private int dayDuration;
    private int maxProdsTLOU; //Cantidad máxima de productores TLOU
    public static volatile int counter; //Contador del número de días restantes para el corte de comparación
    public static volatile int backupCounter; //El que guardará sin ser modificado la cantidad de días
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
    
    //Datos del Project Manager
    private ProjectManagerTLOU pmTLOU;
    private Semaphore stateMutexTLOU, countMutexTLOU;
    
    //Datos del Director
    private DirectorTLOU dirTLOU;
    
    /**
     * Creates new form Dashboard
     */
    public TLOUInterface() {
        initComponents();
        inicializarTLOU();
    }
    
    public void inicializarTLOU() {
        //Con el JSON tienen que cambiarse los valores de abajo
        this.stop = false;
        this.dayDuration = 2;
        this.backupCounter = 30;
        this.counter = 30;
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
        
        //Creando Project Manager TLOU
        this.stateMutexTLOU = new Semaphore(1);
        this.countMutexTLOU = new Semaphore(1);
        this.pmTLOU = new ProjectManagerTLOU(stop, dayDuration, countMutexTLOU, stateMutexTLOU);
        pmTLOU.start();
        
        //Creando Director TLOU
        this.dirTLOU = new DirectorTLOU(stop, dayDuration, countMutexTLOU, stateMutexTLOU, pmTLOU);
        dirTLOU.start();
        
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
        jLabel15 = new javax.swing.JLabel();
        introMinusTLOU2 = new javax.swing.JButton();
        numProdIntro2 = new javax.swing.JLabel();
        introPlusTLOU2 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        introMinusTLOU1 = new javax.swing.JButton();
        numProdIntro1 = new javax.swing.JLabel();
        introPlusTLOU1 = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        numProdIntro = new javax.swing.JLabel();
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
        jLabel8 = new javax.swing.JLabel();
        pmState = new javax.swing.JLabel();
        daysCounter = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        dirState = new javax.swing.JLabel();
        TLOUlogo = new javax.swing.JLabel();
        TLOUlogo1 = new javax.swing.JLabel();
        TLOUlogo2 = new javax.swing.JLabel();
        TLOUlogo3 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        backgroundTLOU = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        introMinusTLOU = new javax.swing.JButton();
        introPlusTLOU = new javax.swing.JButton();
        numProdIntro3 = new javax.swing.JLabel();
        introMinusTLOU3 = new javax.swing.JButton();
        introPlusTLOU3 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        introMinusTLOU4 = new javax.swing.JButton();
        numProdIntro4 = new javax.swing.JLabel();
        introPlusTLOU4 = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        introMinusTLOU5 = new javax.swing.JButton();
        numProdIntro5 = new javax.swing.JLabel();
        introPlusTLOU5 = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));
        setMinimumSize(new java.awt.Dimension(812, 475));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Créditos en Drive:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 220, 140, 26));

        jLabel15.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(102, 102, 102));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Prod. Inicio:");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 250, 150, 26));

        introMinusTLOU2.setBackground(new java.awt.Color(204, 0, 0));
        introMinusTLOU2.setFont(new java.awt.Font("Haettenschweiler", 1, 18)); // NOI18N
        introMinusTLOU2.setForeground(new java.awt.Color(255, 255, 255));
        introMinusTLOU2.setText("-");
        getContentPane().add(introMinusTLOU2, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 250, -1, -1));

        numProdIntro2.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        numProdIntro2.setForeground(new java.awt.Color(102, 102, 102));
        numProdIntro2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        numProdIntro2.setText("0");
        getContentPane().add(numProdIntro2, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 250, 40, 30));

        introPlusTLOU2.setBackground(new java.awt.Color(0, 102, 0));
        introPlusTLOU2.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        introPlusTLOU2.setForeground(new java.awt.Color(255, 255, 255));
        introPlusTLOU2.setText("+");
        introPlusTLOU2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        introPlusTLOU2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                introPlusTLOU2ActionPerformed(evt);
            }
        });
        getContentPane().add(introPlusTLOU2, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 250, -1, -1));

        jLabel14.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(102, 102, 102));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Prod. Crédito:");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 220, 150, 26));

        introMinusTLOU1.setBackground(new java.awt.Color(204, 0, 0));
        introMinusTLOU1.setFont(new java.awt.Font("Haettenschweiler", 1, 18)); // NOI18N
        introMinusTLOU1.setForeground(new java.awt.Color(255, 255, 255));
        introMinusTLOU1.setText("-");
        getContentPane().add(introMinusTLOU1, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 220, -1, -1));

        numProdIntro1.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        numProdIntro1.setForeground(new java.awt.Color(102, 102, 102));
        numProdIntro1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        numProdIntro1.setText("0");
        getContentPane().add(numProdIntro1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 220, 40, 30));

        introPlusTLOU1.setBackground(new java.awt.Color(0, 102, 0));
        introPlusTLOU1.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        introPlusTLOU1.setForeground(new java.awt.Color(255, 255, 255));
        introPlusTLOU1.setText("+");
        introPlusTLOU1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        introPlusTLOU1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                introPlusTLOU1ActionPerformed(evt);
            }
        });
        getContentPane().add(introPlusTLOU1, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 220, -1, -1));

        jLabel13.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Prod. Intro:");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 190, 160, 26));

        numProdIntro.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        numProdIntro.setForeground(new java.awt.Color(102, 102, 102));
        numProdIntro.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        numProdIntro.setText("0");
        getContentPane().add(numProdIntro, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 190, 40, 30));

        numIntroTLOU.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        numIntroTLOU.setForeground(new java.awt.Color(102, 102, 102));
        numIntroTLOU.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        numIntroTLOU.setText("0");
        getContentPane().add(numIntroTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 190, 40, 26));

        jLabel3.setFont(new java.awt.Font("Haettenschweiler", 0, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(51, 51, 51));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("DISPONIBILIDAD");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, 190, 26));

        numCreditTLOU.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        numCreditTLOU.setForeground(new java.awt.Color(102, 102, 102));
        numCreditTLOU.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        numCreditTLOU.setText("0");
        getContentPane().add(numCreditTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 220, 40, 27));

        jLabel4.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Inicios en Drive:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 250, 120, 26));

        numBegTLOU.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        numBegTLOU.setForeground(new java.awt.Color(102, 102, 102));
        numBegTLOU.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        numBegTLOU.setText("0");
        getContentPane().add(numBegTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 250, 40, 26));

        jLabel5.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(102, 102, 102));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Cierres en Drive:");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 280, -1, 26));

        numEndTLOU.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        numEndTLOU.setForeground(new java.awt.Color(102, 102, 102));
        numEndTLOU.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        numEndTLOU.setText("0");
        getContentPane().add(numEndTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 280, 40, 26));

        jLabel6.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 102, 102));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Plot twists en Drive:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 170, 26));

        numPlotTLOU.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        numPlotTLOU.setForeground(new java.awt.Color(102, 102, 102));
        numPlotTLOU.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        numPlotTLOU.setText("0");
        getContentPane().add(numPlotTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 310, 40, 26));

        jLabel7.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(102, 102, 102));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Capítulos disponibles:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, -1, 26));

        numChapters.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        numChapters.setForeground(new java.awt.Color(102, 102, 102));
        numChapters.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        numChapters.setText("0");
        getContentPane().add(numChapters, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 340, 40, 26));

        jLabel8.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(102, 102, 102));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Estado del PM:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 260, -1, 26));

        pmState.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        pmState.setForeground(new java.awt.Color(153, 153, 153));
        pmState.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(pmState, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 290, 240, 26));

        daysCounter.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        daysCounter.setForeground(new java.awt.Color(102, 102, 102));
        daysCounter.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        daysCounter.setText("30");
        getContentPane().add(daysCounter, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 370, 40, -1));

        jLabel9.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(102, 102, 102));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Días para entregar:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 370, -1, 26));

        jLabel10.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(102, 102, 102));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("Estado del Director:");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 190, -1, -1));

        dirState.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        dirState.setForeground(new java.awt.Color(153, 153, 153));
        dirState.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        getContentPane().add(dirState, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 220, 234, 26));

        TLOUlogo.setFont(new java.awt.Font("Haettenschweiler", 0, 72)); // NOI18N
        TLOUlogo.setForeground(new java.awt.Color(204, 204, 204));
        TLOUlogo.setText("US");
        getContentPane().add(TLOUlogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, -1, -1));

        TLOUlogo1.setFont(new java.awt.Font("Haettenschweiler", 0, 72)); // NOI18N
        TLOUlogo1.setForeground(new java.awt.Color(204, 204, 204));
        TLOUlogo1.setText("LAST");
        getContentPane().add(TLOUlogo1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 40, -1, -1));

        TLOUlogo2.setFont(new java.awt.Font("Haettenschweiler", 0, 72)); // NOI18N
        TLOUlogo2.setForeground(new java.awt.Color(204, 204, 204));
        TLOUlogo2.setText("THE");
        getContentPane().add(TLOUlogo2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, -1, -1));

        TLOUlogo3.setFont(new java.awt.Font("Haettenschweiler", 0, 72)); // NOI18N
        TLOUlogo3.setForeground(new java.awt.Color(204, 204, 204));
        TLOUlogo3.setText("OF");
        getContentPane().add(TLOUlogo3, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 40, -1, -1));

        jLabel11.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(102, 102, 102));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("Intros en Drive:");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 190, 130, 26));

        backgroundTLOU.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/backgroundTLOU.jpg"))); // NOI18N
        getContentPane().add(backgroundTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 130));

        jLabel12.setFont(new java.awt.Font("Haettenschweiler", 0, 36)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(51, 51, 51));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("ESTADOS");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 150, 160, 26));

        introMinusTLOU.setBackground(new java.awt.Color(204, 0, 0));
        introMinusTLOU.setFont(new java.awt.Font("Haettenschweiler", 1, 18)); // NOI18N
        introMinusTLOU.setForeground(new java.awt.Color(255, 255, 255));
        introMinusTLOU.setText("-");
        getContentPane().add(introMinusTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 190, -1, -1));

        introPlusTLOU.setBackground(new java.awt.Color(0, 102, 0));
        introPlusTLOU.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        introPlusTLOU.setForeground(new java.awt.Color(255, 255, 255));
        introPlusTLOU.setText("+");
        introPlusTLOU.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        introPlusTLOU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                introPlusTLOUActionPerformed(evt);
            }
        });
        getContentPane().add(introPlusTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 190, -1, -1));

        numProdIntro3.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        numProdIntro3.setForeground(new java.awt.Color(102, 102, 102));
        numProdIntro3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        numProdIntro3.setText("0");
        getContentPane().add(numProdIntro3, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 280, 40, 30));

        introMinusTLOU3.setBackground(new java.awt.Color(204, 0, 0));
        introMinusTLOU3.setFont(new java.awt.Font("Haettenschweiler", 1, 18)); // NOI18N
        introMinusTLOU3.setForeground(new java.awt.Color(255, 255, 255));
        introMinusTLOU3.setText("-");
        getContentPane().add(introMinusTLOU3, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 280, -1, -1));

        introPlusTLOU3.setBackground(new java.awt.Color(0, 102, 0));
        introPlusTLOU3.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        introPlusTLOU3.setForeground(new java.awt.Color(255, 255, 255));
        introPlusTLOU3.setText("+");
        introPlusTLOU3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        introPlusTLOU3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                introPlusTLOU3ActionPerformed(evt);
            }
        });
        getContentPane().add(introPlusTLOU3, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 280, -1, -1));

        jLabel16.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(102, 102, 102));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Prod. Cierre:");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 280, 150, 26));

        jLabel17.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(102, 102, 102));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("Prod. Plot Twist:");
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 310, 150, 26));

        introMinusTLOU4.setBackground(new java.awt.Color(204, 0, 0));
        introMinusTLOU4.setFont(new java.awt.Font("Haettenschweiler", 1, 18)); // NOI18N
        introMinusTLOU4.setForeground(new java.awt.Color(255, 255, 255));
        introMinusTLOU4.setText("-");
        getContentPane().add(introMinusTLOU4, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 310, -1, -1));

        numProdIntro4.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        numProdIntro4.setForeground(new java.awt.Color(102, 102, 102));
        numProdIntro4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        numProdIntro4.setText("0");
        getContentPane().add(numProdIntro4, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 310, 40, 30));

        introPlusTLOU4.setBackground(new java.awt.Color(0, 102, 0));
        introPlusTLOU4.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        introPlusTLOU4.setForeground(new java.awt.Color(255, 255, 255));
        introPlusTLOU4.setText("+");
        introPlusTLOU4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        introPlusTLOU4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                introPlusTLOU4ActionPerformed(evt);
            }
        });
        getContentPane().add(introPlusTLOU4, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 310, -1, -1));

        jLabel18.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(102, 102, 102));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Ensambladores:");
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 340, 150, 26));

        introMinusTLOU5.setBackground(new java.awt.Color(204, 0, 0));
        introMinusTLOU5.setFont(new java.awt.Font("Haettenschweiler", 1, 18)); // NOI18N
        introMinusTLOU5.setForeground(new java.awt.Color(255, 255, 255));
        introMinusTLOU5.setText("-");
        getContentPane().add(introMinusTLOU5, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 340, -1, -1));

        numProdIntro5.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        numProdIntro5.setForeground(new java.awt.Color(102, 102, 102));
        numProdIntro5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        numProdIntro5.setText("0");
        getContentPane().add(numProdIntro5, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 340, 40, 30));

        introPlusTLOU5.setBackground(new java.awt.Color(0, 102, 0));
        introPlusTLOU5.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        introPlusTLOU5.setForeground(new java.awt.Color(255, 255, 255));
        introPlusTLOU5.setText("+");
        introPlusTLOU5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        introPlusTLOU5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                introPlusTLOU5ActionPerformed(evt);
            }
        });
        getContentPane().add(introPlusTLOU5, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 340, -1, -1));

        jLabel19.setFont(new java.awt.Font("Haettenschweiler", 0, 36)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(51, 51, 51));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("CANTIDADES");
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 150, 160, 26));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void introPlusTLOUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_introPlusTLOUActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_introPlusTLOUActionPerformed

    private void introPlusTLOU1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_introPlusTLOU1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_introPlusTLOU1ActionPerformed

    private void introPlusTLOU2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_introPlusTLOU2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_introPlusTLOU2ActionPerformed

    private void introPlusTLOU3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_introPlusTLOU3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_introPlusTLOU3ActionPerformed

    private void introPlusTLOU4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_introPlusTLOU4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_introPlusTLOU4ActionPerformed

    private void introPlusTLOU5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_introPlusTLOU5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_introPlusTLOU5ActionPerformed

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
            java.util.logging.Logger.getLogger(TLOUInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TLOUInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TLOUInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TLOUInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TLOUInterface().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel TLOUlogo;
    private javax.swing.JLabel TLOUlogo1;
    private javax.swing.JLabel TLOUlogo2;
    private javax.swing.JLabel TLOUlogo3;
    private javax.swing.JLabel backgroundTLOU;
    public static javax.swing.JLabel daysCounter;
    public static javax.swing.JLabel dirState;
    private javax.swing.JButton introMinusTLOU;
    private javax.swing.JButton introMinusTLOU1;
    private javax.swing.JButton introMinusTLOU2;
    private javax.swing.JButton introMinusTLOU3;
    private javax.swing.JButton introMinusTLOU4;
    private javax.swing.JButton introMinusTLOU5;
    private javax.swing.JButton introPlusTLOU;
    private javax.swing.JButton introPlusTLOU1;
    private javax.swing.JButton introPlusTLOU2;
    private javax.swing.JButton introPlusTLOU3;
    private javax.swing.JButton introPlusTLOU4;
    private javax.swing.JButton introPlusTLOU5;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    public static javax.swing.JLabel numBegTLOU;
    public static javax.swing.JLabel numChapters;
    public static javax.swing.JLabel numCreditTLOU;
    public static javax.swing.JLabel numEndTLOU;
    public static javax.swing.JLabel numIntroTLOU;
    public static javax.swing.JLabel numPlotTLOU;
    public static javax.swing.JLabel numProdIntro;
    public static javax.swing.JLabel numProdIntro1;
    public static javax.swing.JLabel numProdIntro2;
    public static javax.swing.JLabel numProdIntro3;
    public static javax.swing.JLabel numProdIntro4;
    public static javax.swing.JLabel numProdIntro5;
    public static javax.swing.JLabel pmState;
    // End of variables declaration//GEN-END:variables
}
