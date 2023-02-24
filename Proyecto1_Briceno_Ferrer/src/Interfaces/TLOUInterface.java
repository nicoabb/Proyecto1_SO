/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Classes.TLOU.AdministratorTLOU;
import Classes.TLOU.AssemblerTLOU;
import Classes.TLOU.DirectorTLOU;
import Classes.TLOU.ProdEndTLOU;
import Classes.TLOU.ProdBegTLOU;
import Classes.TLOU.ProdCreditTLOU;
import Classes.TLOU.ProdIntroTLOU;
import Classes.TLOU.ProdPlotTLOU;
import Classes.TLOU.ProjectManagerTLOU;
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
    
    private boolean start = true;
    private int dayDuration;
    public static volatile int counter; //Contador del número de días restantes para el corte de comparación
    public static volatile int backupCounter; //El que guardará sin ser modificado la cantidad de días
    private int AvailableProds = 10; //Productoras disponibles
    public static volatile int totalChapters = 0;
    
    //Ganancias por los capítulos
    public static volatile int incomeChapters = 0;

    //Espacio en Drive
    public static volatile int introDriveTLOU = 0;
    public static volatile int creditDriveTLOU = 0;
    public static volatile int begDriveTLOU = 0;
    public static volatile int endDriveTLOU = 0;
    public static volatile int plotDriveTLOU = 0;
    public static volatile int chaptersTLOU = 0;
    
    //Costos y saldos
    private AdministratorTLOU admin;
    public static volatile int costIntro = 0; 
    public static volatile int costCredit = 0; 
    public static volatile int costBeg = 0;
    public static volatile int costEnd = 0; 
    public static volatile int costPlot = 0; 
    public static volatile int costAssembler = 0; 
    public static volatile int salaryPM = 0; 
    public static volatile int salaryDir = 0; 
    public static volatile int numFaults = 0;
    
    //Datos del Productor de Intro TLOU
    //private ProdIntroTLOU prodIntroTLOU;
    private ProdIntroTLOU arrayIntroTLOU[];
    private int introMaxDriveTLOU;
    public static int introProdTLOU;
    private Semaphore mutexIntroTLOU, semIntroTLOU, semAssemIntroTLOU;
    
    //Datos del Productor de Creditos TLOU
    //private ProdCreditTLOU prodCreditTLOU;
    private ProdCreditTLOU arrayCreditTLOU[];
    private int creditMaxDriveTLOU;
    public static int creditProdTLOU;
    private Semaphore mutexCreditTLOU, semCreditTLOU, semAssemCreditTLOU;
    
    //Datos del Productor de Inicio (Beggining) TLOU
    //private ProdBegTLOU prodBegTLOU;
    private ProdBegTLOU arrayBegTLOU[];
    private int begMaxDriveTLOU;
    public static int begProdTLOU;
    private Semaphore mutexBegTLOU, semBegTLOU, semAssemBegTLOU;
    
    //Datos del Productor de Cierre (End) TLOU
    //private ProdEndTLOU prodEndTLOU;
    private ProdEndTLOU arrayEndTLOU[];
    private int endMaxDriveTLOU;
    public static int endProdTLOU;
    private Semaphore mutexEndTLOU, semEndTLOU, semAssemEndTLOU;
    
    //Datos del Productor de Plot (Plot Twist) TLOU
    //private ProdPlotTLOU prodPlotTLOU;
    private ProdPlotTLOU arrayPlotTLOU[];
    private int plotMaxDriveTLOU;
    public static int plotProdTLOU;
    private Semaphore mutexPlotTLOU, semPlotTLOU, semAssemPlotTLOU;
    
    //Datos del Ensamblador TLOU
    //private AssemblerTLOU assemblerTLOU;
    private AssemblerTLOU arrayAssemblerTLOU[];
    public static int assemblersTLOU;
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
            TLOUInterface.counter = ((Long) dayObj.get("counter")).intValue();
            TLOUInterface.backupCounter = counter;

            JSONArray producersArray = (JSONArray) jsonObject.get("producers");

            for (Object obj : producersArray) {
                JSONObject producer = (JSONObject) obj;

                String category = (String) producer.get("category");
                if (category.equals("intro")) {
                    this.introMaxDriveTLOU = ((Long) producer.get("driveGb")).intValue();
                    if(AvailableProds > 0) {
                        this.introProdTLOU = ((Long) producer.get("amountProducers")).intValue();
                        TLOUInterface.numProdIntro.setText(Integer.toString(introProdTLOU));
                        AvailableProds -= introProdTLOU;
                    } else {
                        //throw new IOException();
                    }
                } else if (category.equals("credits")) {
                    this.creditMaxDriveTLOU = ((Long) producer.get("driveGb")).intValue();
                    if(AvailableProds > 0) {
                        this.creditProdTLOU = ((Long) producer.get("amountProducers")).intValue();;
                        TLOUInterface.numProdCredit.setText(Integer.toString(creditProdTLOU));
                        AvailableProds -= creditProdTLOU;
                    } else {
                        //throw new IOException();
                    }
                } else if (category.equals("beginning")) {
                    this.begMaxDriveTLOU = ((Long) producer.get("driveGb")).intValue();
                    if(AvailableProds > 0) {
                        this.begProdTLOU = ((Long) producer.get("amountProducers")).intValue();
                        TLOUInterface.numProdBeg.setText(Integer.toString(begProdTLOU));
                        AvailableProds -= begProdTLOU;
                    } else {
                        //throw new IOException();
                    }
                } else if (category.equals("ending")) {
                    this.endMaxDriveTLOU = ((Long) producer.get("driveGb")).intValue();
                    if(AvailableProds > 0) {
                        this.endProdTLOU = ((Long) producer.get("amountProducers")).intValue();
                        TLOUInterface.numProdEnd.setText(Integer.toString(endProdTLOU));
                        AvailableProds -= endProdTLOU;
                    } else {
                        //throw new IOException();
                    }
                } else if (category.equals("plottwist")) {
                    this.plotMaxDriveTLOU = ((Long) producer.get("driveGb")).intValue();
                    if(AvailableProds > 0) {
                        this.plotProdTLOU = ((Long) producer.get("amountProducers")).intValue();
                        TLOUInterface.numProdPlot.setText(Integer.toString(plotProdTLOU));
                        AvailableProds -= plotProdTLOU;
                    } else {
                        //throw new IOException();
                    }
                }
            }
            
            TLOUInterface.availableProdsLabel.setText(Integer.toString(AvailableProds));
            JSONObject assemblyObj = (JSONObject) jsonObject.get("assemblers");
            this.assemblersTLOU = ((Long) assemblyObj.get("amountAssemblers")).intValue();
            TLOUInterface.numProdAssemblers.setText(Integer.toString(assemblersTLOU));

        } catch (IOException | ParseException e) {
            System.out.println("Mano algo está mal con el JSON");
        }
        
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
        costPMTLOU = new javax.swing.JLabel();
        costDirTLOU = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        costAssemblerLabel = new javax.swing.JLabel();
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
        dirState1 = new javax.swing.JLabel();
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
        incomeChaptersLabel = new javax.swing.JLabel();
        totalChaptersLabel = new javax.swing.JLabel();
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
        availableProdsLabel = new javax.swing.JLabel();
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
        costIntroLabel = new javax.swing.JLabel();
        costCreditLabel = new javax.swing.JLabel();
        costBegLabel = new javax.swing.JLabel();
        costEndLabel = new javax.swing.JLabel();
        costPlotLabel = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        penalizationLabel = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();

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

        costPMTLOU.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        costPMTLOU.setForeground(new java.awt.Color(102, 102, 102));
        costPMTLOU.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        costPMTLOU.setText("$ 0");
        getContentPane().add(costPMTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 470, 80, 26));

        costDirTLOU.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        costDirTLOU.setForeground(new java.awt.Color(102, 102, 102));
        costDirTLOU.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        costDirTLOU.setText("$ 0");
        getContentPane().add(costDirTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 430, 80, 26));

        jLabel15.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(102, 102, 102));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("Prod. Inicio:");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 250, 150, 26));

        costAssemblerLabel.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        costAssemblerLabel.setForeground(new java.awt.Color(102, 102, 102));
        costAssemblerLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        costAssemblerLabel.setText("$ 0");
        getContentPane().add(costAssemblerLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 340, 90, 26));

        begMinusTLOU.setBackground(new java.awt.Color(204, 0, 0));
        begMinusTLOU.setFont(new java.awt.Font("Haettenschweiler", 1, 18)); // NOI18N
        begMinusTLOU.setForeground(new java.awt.Color(255, 255, 255));
        begMinusTLOU.setText("-");
        begMinusTLOU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                begMinusTLOUActionPerformed(evt);
            }
        });
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
        creditMinusTLOU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                creditMinusTLOUActionPerformed(evt);
            }
        });
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

        dirState1.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        dirState1.setForeground(new java.awt.Color(153, 153, 153));
        dirState1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        dirState1.setText("Productoras disponibles (máx 10):");
        getContentPane().add(dirState1, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 380, 180, 30));

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
        jLabel8.setText("PM:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 510, -1, 26));

        pmState.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        pmState.setForeground(new java.awt.Color(153, 153, 153));
        pmState.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        getContentPane().add(pmState, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 510, 180, 26));

        incomeChaptersLabel.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        incomeChaptersLabel.setForeground(new java.awt.Color(102, 102, 102));
        incomeChaptersLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        incomeChaptersLabel.setText("$ 0");
        getContentPane().add(incomeChaptersLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 510, 40, -1));

        totalChaptersLabel.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        totalChaptersLabel.setForeground(new java.awt.Color(102, 102, 102));
        totalChaptersLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        totalChaptersLabel.setText("0");
        getContentPane().add(totalChaptersLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 470, 40, -1));

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
        jLabel10.setText("Generado:");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 510, 80, -1));

        dirState.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        dirState.setForeground(new java.awt.Color(153, 153, 153));
        dirState.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        getContentPane().add(dirState, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 470, 140, 30));

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
        jLabel12.setText("CAPÍTULOS");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 430, 200, 26));

        introMinusTLOU.setBackground(new java.awt.Color(204, 0, 0));
        introMinusTLOU.setFont(new java.awt.Font("Haettenschweiler", 1, 18)); // NOI18N
        introMinusTLOU.setForeground(new java.awt.Color(255, 255, 255));
        introMinusTLOU.setText("-");
        introMinusTLOU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                introMinusTLOUActionPerformed(evt);
            }
        });
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
        endMinusTLOU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endMinusTLOUActionPerformed(evt);
            }
        });
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
        plotMinusTLOU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plotMinusTLOUActionPerformed(evt);
            }
        });
        getContentPane().add(plotMinusTLOU, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 310, -1, -1));

        numProdPlot.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        numProdPlot.setForeground(new java.awt.Color(102, 102, 102));
        numProdPlot.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        numProdPlot.setText("0");
        getContentPane().add(numProdPlot, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 310, 50, 30));

        availableProdsLabel.setFont(new java.awt.Font("Haettenschweiler", 0, 18)); // NOI18N
        availableProdsLabel.setForeground(new java.awt.Color(102, 102, 102));
        availableProdsLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        availableProdsLabel.setText("10");
        getContentPane().add(availableProdsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 380, 30, 30));

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
        assemblerMinusTLOU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                assemblerMinusTLOUActionPerformed(evt);
            }
        });
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
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 390, 180, 26));

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
        jLabel26.setText("Saldo ganado PM:");
        getContentPane().add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 470, 160, 26));

        costIntroLabel.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        costIntroLabel.setForeground(new java.awt.Color(102, 102, 102));
        costIntroLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        costIntroLabel.setText("$ 0");
        getContentPane().add(costIntroLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 190, 90, 26));

        costCreditLabel.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        costCreditLabel.setForeground(new java.awt.Color(102, 102, 102));
        costCreditLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        costCreditLabel.setText("$ 0");
        getContentPane().add(costCreditLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 220, 90, 26));

        costBegLabel.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        costBegLabel.setForeground(new java.awt.Color(102, 102, 102));
        costBegLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        costBegLabel.setText("$ 0");
        getContentPane().add(costBegLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 250, 90, 26));

        costEndLabel.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        costEndLabel.setForeground(new java.awt.Color(102, 102, 102));
        costEndLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        costEndLabel.setText("$ 0");
        getContentPane().add(costEndLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 280, 90, 26));

        costPlotLabel.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        costPlotLabel.setForeground(new java.awt.Color(102, 102, 102));
        costPlotLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        costPlotLabel.setText("$ 0");
        getContentPane().add(costPlotLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 310, 90, 26));

        jLabel27.setFont(new java.awt.Font("Haettenschweiler", 0, 36)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(51, 51, 51));
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("COSTOS");
        getContentPane().add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 150, 160, 26));

        jLabel28.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(102, 102, 102));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel28.setText("Ensambladores:");
        getContentPane().add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 340, 150, 26));

        jLabel29.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(102, 102, 102));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("Penalización:");
        getContentPane().add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 510, 110, 26));

        jLabel30.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(102, 102, 102));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText("Saldo ganado Director:");
        getContentPane().add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 430, 180, 26));

        penalizationLabel.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        penalizationLabel.setForeground(new java.awt.Color(102, 102, 102));
        penalizationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        penalizationLabel.setText("$ 0");
        getContentPane().add(penalizationLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 510, 40, 26));

        jLabel31.setFont(new java.awt.Font("Haettenschweiler", 0, 36)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(51, 51, 51));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("ESTADOS");
        getContentPane().add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 430, 200, 26));

        jLabel32.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(102, 102, 102));
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText("Director:");
        getContentPane().add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 470, -1, -1));

        jLabel33.setFont(new java.awt.Font("Haettenschweiler", 0, 24)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(102, 102, 102));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Totales:");
        getContentPane().add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 470, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void introPlusTLOUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_introPlusTLOUActionPerformed
        if(this.arrayIntroTLOU != null){
            if(AvailableProds > 0){
                
                arrayIntroTLOU[introProdTLOU] = new ProdIntroTLOU(start, dayDuration, mutexIntroTLOU, semIntroTLOU, semAssemIntroTLOU);
                introProdTLOU++;
                TLOUInterface.numProdIntro.setText(Integer.toString(introProdTLOU));
                if(start){
                    this.arrayIntroTLOU[introProdTLOU - 1].start();
                }
                AvailableProds--;
                availableProdsLabel.setText(Integer.toString(AvailableProds));
            }
        }
    }//GEN-LAST:event_introPlusTLOUActionPerformed

    private void creditPlusTLOUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditPlusTLOUActionPerformed
        if(this.arrayCreditTLOU != null){
            if(AvailableProds > 0){
                
                arrayCreditTLOU[creditProdTLOU] = new ProdCreditTLOU(start, dayDuration, mutexIntroTLOU, semIntroTLOU, semAssemIntroTLOU);
                creditProdTLOU++;
                TLOUInterface.numProdCredit.setText(Integer.toString(creditProdTLOU));
                if(start){
                    this.arrayCreditTLOU[creditProdTLOU - 1].start();
                }
                AvailableProds--;
                availableProdsLabel.setText(Integer.toString(AvailableProds));
            }
        }
    }//GEN-LAST:event_creditPlusTLOUActionPerformed

    private void begPlusTLOUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_begPlusTLOUActionPerformed
        if(this.arrayBegTLOU != null){
            if(AvailableProds > 0){
                
                arrayBegTLOU[begProdTLOU] = new ProdBegTLOU(start, dayDuration, mutexIntroTLOU, semIntroTLOU, semAssemIntroTLOU);
                begProdTLOU++;
                TLOUInterface.numProdBeg.setText(Integer.toString(begProdTLOU));
                if(start){
                    this.arrayBegTLOU[begProdTLOU - 1].start();
                }
                AvailableProds--;
                availableProdsLabel.setText(Integer.toString(AvailableProds));
            }
        }
    }//GEN-LAST:event_begPlusTLOUActionPerformed

    private void endPlusTLOUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endPlusTLOUActionPerformed
        if(this.arrayEndTLOU != null){
            if(AvailableProds > 0){
                
                arrayEndTLOU[endProdTLOU] = new ProdEndTLOU(start, dayDuration, mutexIntroTLOU, semIntroTLOU, semAssemIntroTLOU);
                endProdTLOU++;
                TLOUInterface.numProdEnd.setText(Integer.toString(endProdTLOU));
                if(start){
                    this.arrayEndTLOU[endProdTLOU - 1].start();
                }
                AvailableProds--;
                availableProdsLabel.setText(Integer.toString(AvailableProds));
            }
        }
    }//GEN-LAST:event_endPlusTLOUActionPerformed

    private void plotPlusTLOUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plotPlusTLOUActionPerformed
        if(this.arrayPlotTLOU != null){
            if(AvailableProds > 0){
                
                arrayPlotTLOU[plotProdTLOU] = new ProdPlotTLOU(start, dayDuration, mutexIntroTLOU, semIntroTLOU, semAssemIntroTLOU);
                plotProdTLOU++;
                TLOUInterface.numProdPlot.setText(Integer.toString(plotProdTLOU));
                if(start){    
                    this.arrayPlotTLOU[plotProdTLOU - 1].start();
                }
                AvailableProds--;
                availableProdsLabel.setText(Integer.toString(AvailableProds));
            }
        }
    }//GEN-LAST:event_plotPlusTLOUActionPerformed

    private void assemblerPlusTLOUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_assemblerPlusTLOUActionPerformed
        if(this.arrayAssemblerTLOU != null){
                            
            arrayAssemblerTLOU[assemblersTLOU] = new AssemblerTLOU(start, dayDuration, mutexAssembler, mutexIntroTLOU, semIntroTLOU, semAssemIntroTLOU, mutexBegTLOU, semBegTLOU, semAssemBegTLOU, mutexEndTLOU, semEndTLOU, semAssemEndTLOU, mutexCreditTLOU, semCreditTLOU, semAssemCreditTLOU, mutexPlotTLOU, semPlotTLOU, semAssemPlotTLOU);
            assemblersTLOU++;
            TLOUInterface.numProdAssemblers.setText(Integer.toString(assemblersTLOU));
            if(start) {
                this.arrayAssemblerTLOU[assemblersTLOU - 1].start();
            }
        }
    }//GEN-LAST:event_assemblerPlusTLOUActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        this.start = false;
            
            for (int i = 0; i < introProdTLOU; i++) {
                this.arrayIntroTLOU[i].setStart(start);
            }
            for (int i = 0; i < creditProdTLOU; i++) {
                this.arrayCreditTLOU[i].setStart(start);
            }
            for (int i = 0; i < begProdTLOU; i++) {
                this.arrayBegTLOU[i].setStart(start);
            }
            for (int i = 0; i < endProdTLOU; i++) {
                this.arrayEndTLOU[i].setStart(start);
            }
            for (int i = 0; i < plotProdTLOU; i++) {
                this.arrayPlotTLOU[i].setStart(start);
            }
            for (int i = 0; i < assemblersTLOU; i++) {
                this.arrayAssemblerTLOU[i].setStart(start);
            }
            this.dirTLOU.setStart(start);
            this.pmTLOU.setStart(start);
            this.admin.setStart(start);
            
            //Imprimir resultados en Dashboard
            Dashboard.chaptersMadeTLOU.setText(Integer.toString(totalChapters));
            int sumCosts = costIntro + costCredit + costBeg + costEnd + costPlot + costAssembler + salaryDir + (salaryPM - numFaults);
            Dashboard.costsTLOU.setText(Integer.toString(sumCosts));
            int sumEarnings = totalChapters * ((1100000 / 150000) * 100000);
            Dashboard.earningsTLOU.setText(Integer.toString(sumEarnings));
            Dashboard.balanceTLOU.setText(Integer.toString(sumEarnings - sumCosts));
    }//GEN-LAST:event_stopButtonActionPerformed

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        if (this.start) {
            
            // Llenar arrays de productores
            // Creando Intros
            for (int i = 0; i < introProdTLOU; i++) {
                this.arrayIntroTLOU[i] = new ProdIntroTLOU(start, dayDuration, mutexIntroTLOU, semIntroTLOU, semAssemIntroTLOU);
                this.arrayIntroTLOU[i].start();
            }
            
            // Creando Credits
            for (int i = 0; i < creditProdTLOU; i++) {
                this.arrayCreditTLOU[i] = new ProdCreditTLOU(start, dayDuration, mutexCreditTLOU, semCreditTLOU, semAssemCreditTLOU);
                this.arrayCreditTLOU[i].start();
            }
            
            // Creando Beginnings
            for (int i = 0; i < begProdTLOU; i++) {
                this.arrayBegTLOU[i] = new ProdBegTLOU(start, dayDuration, mutexBegTLOU, semBegTLOU, semAssemBegTLOU);
                this.arrayBegTLOU[i].start();
            }

            // Creando Endings
            for (int i = 0; i < endProdTLOU; i++) {
                this.arrayEndTLOU[i] = new ProdEndTLOU(start, dayDuration, mutexEndTLOU, semEndTLOU, semAssemEndTLOU);
                this.arrayEndTLOU[i].start();
            }

            // Creando Plot twists
            for (int i = 0; i < plotProdTLOU; i++) {
                this.arrayPlotTLOU[i] = new ProdPlotTLOU(start, dayDuration, mutexPlotTLOU, semPlotTLOU, semAssemPlotTLOU);
                this.arrayPlotTLOU[i].start();
            }

            // Creando Ensamblador
            for (int i = 0; i < assemblersTLOU; i++) {
                this.arrayAssemblerTLOU[i] = new AssemblerTLOU(start, dayDuration, mutexAssembler, mutexIntroTLOU, semIntroTLOU, semAssemIntroTLOU, mutexBegTLOU, semBegTLOU, semAssemBegTLOU, mutexEndTLOU, semEndTLOU, semAssemEndTLOU, mutexCreditTLOU, semCreditTLOU, semAssemCreditTLOU, mutexPlotTLOU, semPlotTLOU, semAssemPlotTLOU);
                this.arrayAssemblerTLOU[i].start();
            }

            // Creando Project Manager TLOU
            this.stateMutexTLOU = new Semaphore(1);
            this.countMutexTLOU = new Semaphore(1);
            this.pmTLOU = new ProjectManagerTLOU(start, dayDuration, countMutexTLOU, stateMutexTLOU);
            pmTLOU.start();

            // Creando Director TLOU
            this.dirTLOU = new DirectorTLOU(start, dayDuration, countMutexTLOU, stateMutexTLOU, pmTLOU);
            dirTLOU.start();
            
            //Creando Admin TLOU
            this.admin = new AdministratorTLOU(start, dayDuration);
            admin.start();
        }
    }//GEN-LAST:event_startButtonActionPerformed

    private void introMinusTLOUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_introMinusTLOUActionPerformed
        if (this.arrayIntroTLOU != null) {
            if (Integer.parseInt(numProdIntro.getText()) > 0) {
                introProdTLOU--;
                numProdIntro.setText(Integer.toString(introProdTLOU));
                AvailableProds++;
                availableProdsLabel.setText(Integer.toString(AvailableProds));
            }

        }
    }//GEN-LAST:event_introMinusTLOUActionPerformed

    private void creditMinusTLOUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_creditMinusTLOUActionPerformed
        if (this.arrayCreditTLOU != null) {
            if (Integer.parseInt(numProdCredit.getText()) > 0) {
                creditProdTLOU--;
                numProdCredit.setText(Integer.toString(creditProdTLOU));
                AvailableProds++;
                availableProdsLabel.setText(Integer.toString(AvailableProds));
            }

        }
    }//GEN-LAST:event_creditMinusTLOUActionPerformed

    private void begMinusTLOUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_begMinusTLOUActionPerformed
        if (this.arrayBegTLOU != null) {
            if (Integer.parseInt(numProdBeg.getText()) > 0) {
                begProdTLOU--;
                numProdBeg.setText(Integer.toString(begProdTLOU));
                AvailableProds++;
                availableProdsLabel.setText(Integer.toString(AvailableProds));
            }

        }
    }//GEN-LAST:event_begMinusTLOUActionPerformed

    private void endMinusTLOUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endMinusTLOUActionPerformed
        if (this.arrayEndTLOU != null) {
            if (Integer.parseInt(numProdEnd.getText()) > 0) {
                endProdTLOU--;
                numProdEnd.setText(Integer.toString(endProdTLOU));
                AvailableProds++;
                availableProdsLabel.setText(Integer.toString(AvailableProds));
            }

        }
    }//GEN-LAST:event_endMinusTLOUActionPerformed

    private void plotMinusTLOUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plotMinusTLOUActionPerformed
        if (this.arrayPlotTLOU != null) {
            if (Integer.parseInt(numProdPlot.getText()) > 0) {
                plotProdTLOU--;
                numProdPlot.setText(Integer.toString(plotProdTLOU));
                AvailableProds++;
                availableProdsLabel.setText(Integer.toString(AvailableProds));
            }

        }
    }//GEN-LAST:event_plotMinusTLOUActionPerformed

    private void assemblerMinusTLOUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_assemblerMinusTLOUActionPerformed
        if (this.arrayAssemblerTLOU != null) {
            if (Integer.parseInt(numProdAssemblers.getText()) > 0) {
                assemblersTLOU--;
                numProdAssemblers.setText(Integer.toString(assemblersTLOU));
            }

        }
    }//GEN-LAST:event_assemblerMinusTLOUActionPerformed

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
    public static javax.swing.JLabel availableProdsLabel;
    private javax.swing.JLabel backgroundTLOU;
    private javax.swing.JButton begMinusTLOU;
    private javax.swing.JButton begPlusTLOU;
    public static javax.swing.JLabel costAssemblerLabel;
    public static javax.swing.JLabel costBegLabel;
    public static javax.swing.JLabel costCreditLabel;
    public static javax.swing.JLabel costDirTLOU;
    public static javax.swing.JLabel costEndLabel;
    public static javax.swing.JLabel costIntroLabel;
    public static javax.swing.JLabel costPMTLOU;
    public static javax.swing.JLabel costPlotLabel;
    private javax.swing.JButton creditMinusTLOU;
    private javax.swing.JButton creditPlusTLOU;
    public static javax.swing.JLabel daysCounter;
    public static javax.swing.JLabel dirState;
    public static javax.swing.JLabel dirState1;
    private javax.swing.JButton endMinusTLOU;
    private javax.swing.JButton endPlusTLOU;
    public static javax.swing.JLabel incomeChaptersLabel;
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
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
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
    public static javax.swing.JLabel penalizationLabel;
    private javax.swing.JButton plotMinusTLOU;
    private javax.swing.JButton plotPlusTLOU;
    public static javax.swing.JLabel pmState;
    private javax.swing.JToggleButton startButton;
    public static javax.swing.JToggleButton stopButton;
    public static javax.swing.JLabel totalChaptersLabel;
    // End of variables declaration//GEN-END:variables
}
