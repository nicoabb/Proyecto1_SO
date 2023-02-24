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
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



/**
 *
 * @author Nicolás Briceño
 */
public class TLOUInterface extends javax.swing.JFrame {
    
    private boolean stop;
    private int dayDuration;
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
    //private ProdIntroTLOU prodIntroTLOU;
    private ProdIntroTLOU arrayIntroTLOU[];
    private int introMaxDriveTLOU, introProdTLOU;
    private Semaphore mutexIntroTLOU, semIntroTLOU, semAssemIntroTLOU;
    
    //Datos del Productor de Creditos TLOU
    //private ProdCreditTLOU prodCreditTLOU;
    private ProdCreditTLOU arrayCreditTLOU[];
    private int creditMaxDriveTLOU, creditProdTLOU;
    private Semaphore mutexCreditTLOU, semCreditTLOU, semAssemCreditTLOU;
    
    //Datos del Productor de Inicio (Beggining) TLOU
    //private ProdBegTLOU prodBegTLOU;
    private ProdBegTLOU arrayBegTLOU[];
    private int begMaxDriveTLOU, begProdTLOU;
    private Semaphore mutexBegTLOU, semBegTLOU, semAssemBegTLOU;
    
    //Datos del Productor de Cierre (End) TLOU
    //private ProdEndTLOU prodEndTLOU;
    private ProdEndTLOU arrayEndTLOU[];
    private int endMaxDriveTLOU, endProdTLOU;
    private Semaphore mutexEndTLOU, semEndTLOU, semAssemEndTLOU;
    
    //Datos del Productor de Plot (Plot Twist) TLOU
    //private ProdPlotTLOU prodPlotTLOU;
    private ProdPlotTLOU arrayPlotTLOU[];
    private int plotMaxDriveTLOU, plotProdTLOU;
    private Semaphore mutexPlotTLOU, semPlotTLOU, semAssemPlotTLOU;
    
    //Datos del Ensamblador TLOU
    //private AssemblerTLOU assemblerTLOU;
    private AssemblerTLOU arrayAssemblerTLOU[];
    private int assemblersTLOU;
    private Semaphore mutexAssembler;
    
    //Datos del Project Manager
    private ProjectManagerTLOU pmTLOU;
    private Semaphore stateMutexTLOU, countMutexTLOU;
    
    //Datos del Director
    private DirectorTLOU dirTLOU;
    
    /**
     * Creates new form TLOUInterface
     */
    public TLOUInterface() {
        initComponents();
        readJson();
    }
    
    public void readJson() {
        
        JSONParser parser = new JSONParser();
        
        try ( Reader reader = new FileReader("src/Assets/dataTLOU.json")) {
            
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            JSONObject dayObj = (JSONObject) jsonObject.get("days");
            this.dayDuration = ((Long) dayObj.get("dayDuration")).intValue();
            System.out.println("Logre sacar la duración del día y es: " + dayDuration);
            TLOUInterface.counter = ((Long) dayObj.get("counter")).intValue();
            TLOUInterface.backupCounter = counter;

            JSONArray producersArray = (JSONArray) jsonObject.get("producers");

            for (Object obj : producersArray) {
                JSONObject producer = (JSONObject) obj;

                String category = (String) producer.get("category");
                if (category.equals("intro")) {
                    this.introMaxDriveTLOU = ((Long) producer.get("driveGb")).intValue();
                    this.introProdTLOU = ((Long) producer.get("amountProducers")).intValue();
                    TLOUInterface.numProdIntro.setText(Integer.toString(introProdTLOU));

                } else if (category.equals("credits")) {
                    this.creditMaxDriveTLOU = ((Long) producer.get("driveGb")).intValue();
                    this.creditProdTLOU = ((Long) producer.get("amountProducers")).intValue();;
                    TLOUInterface.numProdCredit.setText(Integer.toString(creditProdTLOU));

                } else if (category.equals("beginning")) {
                    this.begMaxDriveTLOU = ((Long) producer.get("driveGb")).intValue();
                    this.begProdTLOU = ((Long) producer.get("amountProducers")).intValue();
                    TLOUInterface.numProdBeg.setText(Integer.toString(begProdTLOU));

                } else if (category.equals("ending")) {
                    this.endMaxDriveTLOU = ((Long) producer.get("driveGb")).intValue();
                    this.endProdTLOU = ((Long) producer.get("amountProducers")).intValue();
                    TLOUInterface.numProdEnd.setText(Integer.toString(endProdTLOU));

                } else if (category.equals("plottwist")) {
                    this.plotMaxDriveTLOU = ((Long) producer.get("driveGb")).intValue();
                    this.plotProdTLOU = ((Long) producer.get("amountProducers")).intValue();
                    TLOUInterface.numProdPlot.setText(Integer.toString(plotProdTLOU));
                }
            }
            
            JSONObject assemblyObj = (JSONObject) jsonObject.get("assemblers");
            this.assemblersTLOU = ((Long) assemblyObj.get("amountAssemblers")).intValue();
            TLOUInterface.numProdAssemblers.setText(Integer.toString(assemblersTLOU));

        } catch (IOException | ParseException e) {
            System.out.println("Estoy poteando");
        }
        
        this.stop = false;
        
        //Creando ProdIntroTLOU
        this.mutexIntroTLOU = new Semaphore(1);
        this.semIntroTLOU = new Semaphore(introMaxDriveTLOU);
        this.semAssemIntroTLOU = new Semaphore(0);
        this.arrayIntroTLOU = new ProdIntroTLOU[10];
        
        //Creando ProdCreditTLOU
        this.mutexCreditTLOU = new Semaphore(1);
        this.semCreditTLOU = new Semaphore(creditMaxDriveTLOU);
        this.semAssemCreditTLOU = new Semaphore(0);
        this.arrayCreditTLOU = new ProdCreditTLOU[10];
        
        //Creando ProdBegTLOU
        this.mutexBegTLOU = new Semaphore(1);
        this.semBegTLOU = new Semaphore(begMaxDriveTLOU);
        this.semAssemBegTLOU = new Semaphore(0);
        this.arrayBegTLOU = new ProdBegTLOU[10];
        
        //Creando ProdEndTLOU
        this.mutexEndTLOU = new Semaphore(1);
        this.semEndTLOU = new Semaphore(endMaxDriveTLOU);
        this.semAssemEndTLOU = new Semaphore(0);
        this.arrayEndTLOU = new ProdEndTLOU[10];
        
        //Creando ProdPlotTLOU
        this.mutexPlotTLOU = new Semaphore(1);
        this.semPlotTLOU = new Semaphore(plotMaxDriveTLOU);
        this.semAssemPlotTLOU = new Semaphore(0);
        this.arrayPlotTLOU = new ProdPlotTLOU[10];
        
        //Creando Ensamblador TLOU
        this.mutexAssembler = new Semaphore(1);
        this.arrayAssemblerTLOU = new AssemblerTLOU[10];
        
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
        costAssemblerTLOU = new javax.swing.JLabel();
        begMinusTLOU = new javax.swing.JButton();
        numProdBeg = new javax.swing.JLabel();
        begPlusTLOU = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        creditMinusTLOU = new javax.swing.JButton();
        numProdCredit = new javax.swing.JLabel();
        creditPlusTLOU = new javax.swing.JButton();
        startButton = new javax.swing.JToggleButton();
        stopButton = new javax.swing.JToggleButton();
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
        numProdEnd = new javax.swing.JLabel();
        endMinusTLOU = new javax.swing.JButton();
        endPlusTLOU = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        plotMinusTLOU = new javax.swing.JButton();
        numProdPlot = new javax.swing.JLabel();
        plotPlusTLOU = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        assemblerMinusTLOU = new javax.swing.JButton();
        numProdAssemblers = new javax.swing.JLabel();
        assemblerPlusTLOU = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        costIntroTLOU = new javax.swing.JLabel();
        costCreditTLOU = new javax.swing.JLabel();
        costBegTLOU = new javax.swing.JLabel();
        costEndTLOU = new javax.swing.JLabel();
        costPlotTLOU = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));
        setMinimumSize(new java.awt.Dimension(812, 600));
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

        costAssemblerTLOU.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        costAssemblerTLOU.setForeground(new java.awt.Color(102, 102, 102));
        costAssemblerTLOU.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        costAssemblerTLOU.setText("$ 0");
        getContentPane().add(costAssemblerTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 340, 40, 26));

        begMinusTLOU.setBackground(new java.awt.Color(204, 0, 0));
        begMinusTLOU.setFont(new java.awt.Font("Haettenschweiler", 1, 18)); // NOI18N
        begMinusTLOU.setForeground(new java.awt.Color(255, 255, 255));
        begMinusTLOU.setText("-");
        getContentPane().add(begMinusTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 250, -1, -1));

        numProdBeg.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        numProdBeg.setForeground(new java.awt.Color(102, 102, 102));
        numProdBeg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        numProdBeg.setText("0");
        getContentPane().add(numProdBeg, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 250, 50, 30));

        begPlusTLOU.setBackground(new java.awt.Color(0, 102, 0));
        begPlusTLOU.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        begPlusTLOU.setForeground(new java.awt.Color(255, 255, 255));
        begPlusTLOU.setText("+");
        begPlusTLOU.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        begPlusTLOU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                begPlusTLOUActionPerformed(evt);
            }
        });
        getContentPane().add(begPlusTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 250, -1, -1));

        jLabel14.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(102, 102, 102));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Prod. Crédito:");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 220, 150, 26));

        creditMinusTLOU.setBackground(new java.awt.Color(204, 0, 0));
        creditMinusTLOU.setFont(new java.awt.Font("Haettenschweiler", 1, 18)); // NOI18N
        creditMinusTLOU.setForeground(new java.awt.Color(255, 255, 255));
        creditMinusTLOU.setText("-");
        getContentPane().add(creditMinusTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 220, -1, -1));

        numProdCredit.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        numProdCredit.setForeground(new java.awt.Color(102, 102, 102));
        numProdCredit.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        numProdCredit.setText("0");
        getContentPane().add(numProdCredit, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 220, 50, 30));

        creditPlusTLOU.setBackground(new java.awt.Color(0, 102, 0));
        creditPlusTLOU.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        creditPlusTLOU.setForeground(new java.awt.Color(255, 255, 255));
        creditPlusTLOU.setText("+");
        creditPlusTLOU.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        creditPlusTLOU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditPlusTLOUActionPerformed(evt);
            }
        });
        getContentPane().add(creditPlusTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 220, -1, -1));

        startButton.setBackground(new java.awt.Color(51, 51, 51));
        startButton.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        startButton.setForeground(new java.awt.Color(153, 255, 153));
        startButton.setText("START");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });
        getContentPane().add(startButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 80, 80, -1));

        stopButton.setBackground(new java.awt.Color(51, 51, 51));
        stopButton.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        stopButton.setForeground(new java.awt.Color(255, 153, 153));
        stopButton.setText("STOP");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });
        getContentPane().add(stopButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 80, 80, -1));

        jLabel13.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(102, 102, 102));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Prod. Intro:");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 190, 160, 26));

        numProdIntro.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        numProdIntro.setForeground(new java.awt.Color(102, 102, 102));
        numProdIntro.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        numProdIntro.setText("0");
        getContentPane().add(numProdIntro, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 190, 50, 30));

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
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 500, -1, 26));

        pmState.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        pmState.setForeground(new java.awt.Color(153, 153, 153));
        pmState.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        getContentPane().add(pmState, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 500, 160, 26));

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
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 460, -1, -1));

        dirState.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        dirState.setForeground(new java.awt.Color(153, 153, 153));
        dirState.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        getContentPane().add(dirState, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 460, 160, 30));

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
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 420, 160, 26));

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

        numProdEnd.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        numProdEnd.setForeground(new java.awt.Color(102, 102, 102));
        numProdEnd.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        numProdEnd.setText("0");
        getContentPane().add(numProdEnd, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 280, 50, 30));

        endMinusTLOU.setBackground(new java.awt.Color(204, 0, 0));
        endMinusTLOU.setFont(new java.awt.Font("Haettenschweiler", 1, 18)); // NOI18N
        endMinusTLOU.setForeground(new java.awt.Color(255, 255, 255));
        endMinusTLOU.setText("-");
        getContentPane().add(endMinusTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 280, -1, -1));

        endPlusTLOU.setBackground(new java.awt.Color(0, 102, 0));
        endPlusTLOU.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        endPlusTLOU.setForeground(new java.awt.Color(255, 255, 255));
        endPlusTLOU.setText("+");
        endPlusTLOU.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        endPlusTLOU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endPlusTLOUActionPerformed(evt);
            }
        });
        getContentPane().add(endPlusTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 280, -1, -1));

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

        plotMinusTLOU.setBackground(new java.awt.Color(204, 0, 0));
        plotMinusTLOU.setFont(new java.awt.Font("Haettenschweiler", 1, 18)); // NOI18N
        plotMinusTLOU.setForeground(new java.awt.Color(255, 255, 255));
        plotMinusTLOU.setText("-");
        getContentPane().add(plotMinusTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 310, -1, -1));

        numProdPlot.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        numProdPlot.setForeground(new java.awt.Color(102, 102, 102));
        numProdPlot.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        numProdPlot.setText("0");
        getContentPane().add(numProdPlot, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 310, 50, 30));

        plotPlusTLOU.setBackground(new java.awt.Color(0, 102, 0));
        plotPlusTLOU.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        plotPlusTLOU.setForeground(new java.awt.Color(255, 255, 255));
        plotPlusTLOU.setText("+");
        plotPlusTLOU.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        plotPlusTLOU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plotPlusTLOUActionPerformed(evt);
            }
        });
        getContentPane().add(plotPlusTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 310, -1, -1));

        jLabel18.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(102, 102, 102));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Ensambladores:");
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 340, 150, 26));

        assemblerMinusTLOU.setBackground(new java.awt.Color(204, 0, 0));
        assemblerMinusTLOU.setFont(new java.awt.Font("Haettenschweiler", 1, 18)); // NOI18N
        assemblerMinusTLOU.setForeground(new java.awt.Color(255, 255, 255));
        assemblerMinusTLOU.setText("-");
        getContentPane().add(assemblerMinusTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 340, -1, -1));

        numProdAssemblers.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        numProdAssemblers.setForeground(new java.awt.Color(102, 102, 102));
        numProdAssemblers.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        numProdAssemblers.setText("0");
        getContentPane().add(numProdAssemblers, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 340, 50, 30));

        assemblerPlusTLOU.setBackground(new java.awt.Color(0, 102, 0));
        assemblerPlusTLOU.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        assemblerPlusTLOU.setForeground(new java.awt.Color(255, 255, 255));
        assemblerPlusTLOU.setText("+");
        assemblerPlusTLOU.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        assemblerPlusTLOU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                assemblerPlusTLOUActionPerformed(evt);
            }
        });
        getContentPane().add(assemblerPlusTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 340, -1, -1));

        jLabel19.setFont(new java.awt.Font("Haettenschweiler", 0, 36)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(51, 51, 51));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("DIRECTOR Y PM");
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 420, 180, 26));

        jLabel20.setFont(new java.awt.Font("Haettenschweiler", 0, 36)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(51, 51, 51));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("CANTIDADES");
        getContentPane().add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 150, 160, 26));

        jLabel21.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(102, 102, 102));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("Prod. Intro:");
        getContentPane().add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 190, 160, 26));

        jLabel22.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(102, 102, 102));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("Prod. Crédito:");
        getContentPane().add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 220, 150, 26));

        jLabel23.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(102, 102, 102));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel23.setText("Prod. Inicio:");
        getContentPane().add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 250, 150, 26));

        jLabel24.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(102, 102, 102));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("Prod. Cierre:");
        getContentPane().add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 280, 150, 26));

        jLabel25.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(102, 102, 102));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel25.setText("Prod. Plot Twist:");
        getContentPane().add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 310, 150, 26));

        jLabel26.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(102, 102, 102));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel26.setText("Ensambladores:");
        getContentPane().add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 340, 150, 26));

        costIntroTLOU.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        costIntroTLOU.setForeground(new java.awt.Color(102, 102, 102));
        costIntroTLOU.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        costIntroTLOU.setText("$ 0");
        getContentPane().add(costIntroTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 190, 40, 26));

        costCreditTLOU.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        costCreditTLOU.setForeground(new java.awt.Color(102, 102, 102));
        costCreditTLOU.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        costCreditTLOU.setText("$ 0");
        getContentPane().add(costCreditTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 220, 40, 26));

        costBegTLOU.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        costBegTLOU.setForeground(new java.awt.Color(102, 102, 102));
        costBegTLOU.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        costBegTLOU.setText("$ 0");
        getContentPane().add(costBegTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 250, 40, 26));

        costEndTLOU.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        costEndTLOU.setForeground(new java.awt.Color(102, 102, 102));
        costEndTLOU.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        costEndTLOU.setText("$ 0");
        getContentPane().add(costEndTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 280, 40, 26));

        costPlotTLOU.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        costPlotTLOU.setForeground(new java.awt.Color(102, 102, 102));
        costPlotTLOU.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        costPlotTLOU.setText("$ 0");
        getContentPane().add(costPlotTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 310, 40, 26));

        jLabel27.setFont(new java.awt.Font("Haettenschweiler", 0, 36)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(51, 51, 51));
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("COSTOS");
        getContentPane().add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 150, 160, 26));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void introPlusTLOUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_introPlusTLOUActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_introPlusTLOUActionPerformed

    private void creditPlusTLOUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditPlusTLOUActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_creditPlusTLOUActionPerformed

    private void begPlusTLOUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_begPlusTLOUActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_begPlusTLOUActionPerformed

    private void endPlusTLOUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endPlusTLOUActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_endPlusTLOUActionPerformed

    private void plotPlusTLOUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plotPlusTLOUActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_plotPlusTLOUActionPerformed

    private void assemblerPlusTLOUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_assemblerPlusTLOUActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_assemblerPlusTLOUActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stopButtonActionPerformed

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        if (!this.stop) {
            
            // Llenar arrays de productores
            // Creando Intros
            System.out.println("Productores de Intro: " + introProdTLOU);
            for (int i = 0; i < introProdTLOU; i++) {
                this.arrayIntroTLOU[i] = new ProdIntroTLOU(stop, dayDuration, mutexIntroTLOU, semIntroTLOU, semAssemIntroTLOU);
                this.arrayIntroTLOU[i].start();
            }
            
            // Creando Credits
            System.out.println("Productores de Credit: " + creditProdTLOU);
            for (int i = 0; i < creditProdTLOU; i++) {
                this.arrayCreditTLOU[i] = new ProdCreditTLOU(stop, dayDuration, mutexCreditTLOU, semCreditTLOU, semAssemCreditTLOU);
                this.arrayCreditTLOU[i].start();
            }
            
            // Creando Beginnings
            System.out.println("Productores de Beg: " + begProdTLOU);
            for (int i = 0; i < begProdTLOU; i++) {
                this.arrayBegTLOU[i] = new ProdBegTLOU(stop, dayDuration, mutexBegTLOU, semBegTLOU, semAssemBegTLOU);
                this.arrayBegTLOU[i].start();
            }

            // Creando Endings
            System.out.println("Productores de End: " + endProdTLOU);
            for (int i = 0; i < endProdTLOU; i++) {
                this.arrayEndTLOU[i] = new ProdEndTLOU(stop, dayDuration, mutexEndTLOU, semEndTLOU, semAssemEndTLOU);
                this.arrayEndTLOU[i].start();
            }

            // Creando Plot twists
            System.out.println("Productores de Plot: " + plotProdTLOU);
            for (int i = 0; i < plotProdTLOU; i++) {
                this.arrayPlotTLOU[i] = new ProdPlotTLOU(stop, dayDuration, mutexPlotTLOU, semPlotTLOU, semAssemPlotTLOU);
                this.arrayPlotTLOU[i].start();
            }

            // Creando Ensamblador
            System.out.println("Ensambladores: " + introProdTLOU);
            for (int i = 0; i < plotProdTLOU; i++) {
                this.arrayAssemblerTLOU[i] = new AssemblerTLOU(stop, dayDuration, mutexAssembler, mutexIntroTLOU, semIntroTLOU, semAssemIntroTLOU, mutexBegTLOU, semBegTLOU, semAssemBegTLOU, mutexEndTLOU, semEndTLOU, semAssemEndTLOU, mutexCreditTLOU, semCreditTLOU, semAssemCreditTLOU, mutexPlotTLOU, semPlotTLOU, semAssemPlotTLOU);
                this.arrayAssemblerTLOU[i].start();
            }

            // Creando Project Manager TLOU
            this.stateMutexTLOU = new Semaphore(1);
            this.countMutexTLOU = new Semaphore(1);
            this.pmTLOU = new ProjectManagerTLOU(stop, dayDuration, countMutexTLOU, stateMutexTLOU);
            pmTLOU.start();

            // Creando Director TLOU
            this.dirTLOU = new DirectorTLOU(stop, dayDuration, countMutexTLOU, stateMutexTLOU, pmTLOU);
            dirTLOU.start();
        }
    }//GEN-LAST:event_startButtonActionPerformed

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
    private javax.swing.JButton assemblerMinusTLOU;
    private javax.swing.JButton assemblerPlusTLOU;
    private javax.swing.JLabel backgroundTLOU;
    private javax.swing.JButton begMinusTLOU;
    private javax.swing.JButton begPlusTLOU;
    public static javax.swing.JLabel costAssemblerTLOU;
    public static javax.swing.JLabel costBegTLOU;
    public static javax.swing.JLabel costCreditTLOU;
    public static javax.swing.JLabel costEndTLOU;
    public static javax.swing.JLabel costIntroTLOU;
    public static javax.swing.JLabel costPlotTLOU;
    private javax.swing.JButton creditMinusTLOU;
    private javax.swing.JButton creditPlusTLOU;
    public static javax.swing.JLabel daysCounter;
    public static javax.swing.JLabel dirState;
    private javax.swing.JButton endMinusTLOU;
    private javax.swing.JButton endPlusTLOU;
    private javax.swing.JButton introMinusTLOU;
    private javax.swing.JButton introPlusTLOU;
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
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
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
    public static javax.swing.JLabel numProdAssemblers;
    public static javax.swing.JLabel numProdBeg;
    public static javax.swing.JLabel numProdCredit;
    public static javax.swing.JLabel numProdEnd;
    public static javax.swing.JLabel numProdIntro;
    public static javax.swing.JLabel numProdPlot;
    private javax.swing.JButton plotMinusTLOU;
    private javax.swing.JButton plotPlusTLOU;
    public static javax.swing.JLabel pmState;
    private javax.swing.JToggleButton startButton;
    private javax.swing.JToggleButton stopButton;
    // End of variables declaration//GEN-END:variables
}
