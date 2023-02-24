/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import Classes.GOT.ProjectManagerGOT;
import Classes.GOT.assemblerGOT;
import Classes.GOT.directorGOT;
import Classes.GOT.prodBegGOT;
import Classes.GOT.prodCredGOT;
import Classes.GOT.prodEndGOT;
import Classes.GOT.prodIntroGOT;
import Classes.GOT.prodPlotGOT;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Emilio Ferrer
 */
public class GOTInterface extends javax.swing.JFrame {

    public static boolean start;
    public static int dayDuration; // Duracion de un dia (ms), pasada por el JSON
    public static int hourDuration; // Duracion de una hora (ms)

    public static int freeProds = 0;

    public static int qtyProdIntroGOT, qtyProdBegGOT, qtyProdEndGOT, qtyProdCredGOT, qtyProdPlotGOT, qtyAssemblersGOT;

    public static int introDrive, begDrive, endDrive, credDrive, plotDrive;

    public static int amountRaM = 0;

    public static int cutDuration; // Tiempo entre cortes, sacado del JSON

//  Declarar arrays de productores GOT
    public static prodIntroGOT arrayIntroGOT[];
    public static prodCredGOT arrayCredGOT[];
    public static prodBegGOT arrayBegGOT[];
    public static prodEndGOT arrayEndGOT[];
    public static prodPlotGOT arrayPlotGOT[];

//  Declarar array de ensambladores
    public static assemblerGOT arrayAssemblersGOT[];

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
    private Semaphore counterGOT;
    public static ProjectManagerGOT pmGOT;
    private Semaphore counterMutexGOT;

//  Declarar Director
    private directorGOT directorGOT;

//  Sobre salarios (costos operativos) y ganancias
    public static double salariesTotal;
    public static double salariesProds;
    public static double assemblersSalaries;
    public static double pmSalary;
    public static double directorSalary;
    public static double earnings; 
    public static double balance;

    /**
     * Creates new form Dashboard
     */
    public GOTInterface() {
        initComponents();
        readJson();
    }

    public void readJson() {
        //Esta creacion del array de assemblers no es parte del Json pero me conviene que esté aquí

        JSONParser parser = new JSONParser();

        try ( Reader reader = new FileReader("src/Assets/dataGOT.json")) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            JSONObject dayObj = (JSONObject) jsonObject.get("days");
            JSONObject assemblerObj = (JSONObject) jsonObject.get("assemblers");
            dayDuration = ((Long) dayObj.get("dayLength")).intValue();
            hourDuration = dayDuration / 24;
            cutDuration = ((Long) dayObj.get("cutDuration")).intValue();
            qtyAssemblersGOT = ((Long) assemblerObj.get("amountAssemblers")).intValue();
            qtyAssemblersLabel.setText(Integer.toString(qtyAssemblersGOT));

            JSONArray producersArray = (JSONArray) jsonObject.get("producers");

            for (Object obj : producersArray) {
                JSONObject producer = (JSONObject) obj;

                String category = (String) producer.get("category");
                if (category.equals("intro")) {
                    introDrive = ((Long) producer.get("driveGb")).intValue();
                    qtyProdIntroGOT = ((Long) producer.get("amountProducers")).intValue();
                    GOTInterface.qtyProdIntroGOTLabel.setText(Integer.toString(qtyProdIntroGOT));

                } else if (category.equals("credits")) {
                    credDrive = ((Long) producer.get("driveGb")).intValue();
                    qtyProdCredGOT = ((Long) producer.get("amountProducers")).intValue();;
                    GOTInterface.qtyProdCredsGOTLabel.setText(Integer.toString(qtyProdCredGOT));

                } else if (category.equals("beginning")) {
                    begDrive = ((Long) producer.get("driveGb")).intValue();
                    qtyProdBegGOT = ((Long) producer.get("amountProducers")).intValue();
                    GOTInterface.qtyProdBegGOTLabel.setText(Integer.toString(qtyProdBegGOT));

                } else if (category.equals("ending")) {
                    endDrive = ((Long) producer.get("driveGb")).intValue();
                    qtyProdEndGOT = ((Long) producer.get("amountProducers")).intValue();
                    GOTInterface.qtyProdEndGOTLabel.setText(Integer.toString(qtyProdEndGOT));

                } else if (category.equals("plottwist")) {
                    plotDrive = ((Long) producer.get("driveGb")).intValue();
                    qtyProdPlotGOT = ((Long) producer.get("amountProducers")).intValue();
                    GOTInterface.qtyProdPlotsGOTLabel.setText(Integer.toString(qtyProdPlotGOT));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Instanciar al productor de intro
        this.semIntroGOT = new Semaphore(introDrive);
        this.semIntroMutexGOT = new Semaphore(1);
        this.semEnsIntroGOT = new Semaphore(0);

        this.arrayIntroGOT = new prodIntroGOT[19];

        //Instanciar al productor de creditos
        this.semCredGOT = new Semaphore(credDrive);
        this.semCredMutexGOT = new Semaphore(1);
        this.semEnsCredGOT = new Semaphore(0);

        this.arrayCredGOT = new prodCredGOT[19];

        //Instanciar al productor de inicios
        this.semBegGOT = new Semaphore(begDrive);
        this.semBegMutexGOT = new Semaphore(1);
        this.semEnsBegGOT = new Semaphore(0);

        this.arrayBegGOT = new prodBegGOT[19];

        //Instanciar al productor de cierres
        this.semEndGOT = new Semaphore(endDrive);
        this.semEndMutexGOT = new Semaphore(1);
        this.semEnsEndGOT = new Semaphore(0);

        this.arrayEndGOT = new prodEndGOT[19];

        //Instanciar al productor de plot twists
        this.semPlotGOT = new Semaphore(plotDrive);
        this.semPlotMutexGOT = new Semaphore(1);
        this.semEnsPlotGOT = new Semaphore(0);

        this.arrayPlotGOT = new prodPlotGOT[19];

        this.arrayAssemblersGOT = new assemblerGOT[10];

    }

    public static double calculateProdSalaries() {
        double introProdSalaries = qtyProdIntroGOT * 5 * 24 * cutDuration;
        double credProdSalaries = qtyProdCredGOT * 3 * 24 * cutDuration;
        double begProdSalaries = qtyProdBegGOT * 7 * 24 * cutDuration;
        double endProdSalaries = qtyProdEndGOT * 7.5 * 24 * cutDuration;
        double plotProdSalaries = qtyProdPlotGOT * 10 * 24 * cutDuration;

        return introProdSalaries + credProdSalaries + begProdSalaries + endProdSalaries + plotProdSalaries;

    }

    public static double calculateAssemblerSalary() {
        return qtyAssemblersGOT * 8 * 24 * cutDuration;
    }

    public static double calculatePMSalary() {
        return 7 * 24 * cutDuration - amountRaM; // Le resta al salario la cantidad de veces que lo ve viendo tv
    }

    public static double calculateDirectorSalary() {
        return 100 * cutDuration;
    }

    public static double allSalaries() {
        return calculateProdSalaries() + calculateAssemblerSalary() + calculatePMSalary() + calculateDirectorSalary();
    }
    
    public static double calculateEarnings() {
        return GOTInterface.chaptersProducedGOT * 980000 * 10 / 15; // capitulos producidos, por audiencia, por tasa de ganancia entre cantidad de audiencia
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
        qtyProdPlotsGOTLabel = new javax.swing.JLabel();
        qtyProdCredsGOTLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        chaptersMade = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        daysUntilCut = new javax.swing.JLabel();
        popIntroProdGOT = new javax.swing.JButton();
        pushIntroProdGOT = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        qtyProdIntroGOTLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        popProdBegGOT = new javax.swing.JButton();
        qtyProdBegGOTLabel = new javax.swing.JLabel();
        pushProdBegGOT = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        popProdEndGOT = new javax.swing.JButton();
        qtyProdEndGOTLabel = new javax.swing.JLabel();
        pushProdEndGOT = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        popProdPlotGOT = new javax.swing.JButton();
        qtyPlotsGOT = new javax.swing.JLabel();
        pushProdPlotGOT = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        popAssemblerGOT = new javax.swing.JButton();
        qtyCredGOT = new javax.swing.JLabel();
        pushProdCredGOT = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        freeProdsGOTLabel = new javax.swing.JLabel();
        startButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        qtyAssemblersLabel = new javax.swing.JLabel();
        popProdCredGOT = new javax.swing.JButton();
        pushAssemblerGOT = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        prodSalariesLabel = new javax.swing.JLabel();
        assemblersSalariesLabel = new javax.swing.JLabel();
        pmSalaryLabel = new javax.swing.JLabel();
        directorSalaryLabel = new javax.swing.JLabel();
        RaMLabel = new javax.swing.JLabel();
        earningsLabel = new javax.swing.JLabel();
        salariesLabel = new javax.swing.JLabel();
        balanceLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setText("Intros disponibles");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 230, 110, -1));

        jLabel8.setText("Inicios disponibles");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 290, -1, -1));

        jLabel9.setText("Cierres disponibles");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, 110, -1));

        jLabel10.setText("Creditos disponibles");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 450, -1, -1));

        jLabel11.setText("Plots disponibles");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 390, 110, -1));
        getContentPane().add(qtyIntrosGOT, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 230, 43, 16));
        getContentPane().add(qtyBegsGOT, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 290, 44, 16));
        getContentPane().add(qtyEndsGOT, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 340, 37, 16));
        getContentPane().add(qtyProdPlotsGOTLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 440, 49, 22));
        getContentPane().add(qtyProdCredsGOTLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 490, 49, 22));

        jLabel1.setText("Balance:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 560, -1, 20));
        getContentPane().add(chaptersMade, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 290, 37, 20));

        jLabel2.setText("Dias para el corte");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 320, 111, -1));
        getContentPane().add(daysUntilCut, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 320, 37, 16));

        popIntroProdGOT.setText("-");
        popIntroProdGOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popIntroProdGOTActionPerformed(evt);
            }
        });
        getContentPane().add(popIntroProdGOT, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 300, 30, -1));

        pushIntroProdGOT.setText("+");
        pushIntroProdGOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pushIntroProdGOTActionPerformed(evt);
            }
        });
        getContentPane().add(pushIntroProdGOT, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 300, -1, -1));

        jLabel4.setText("Introducciones");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 300, 90, -1));
        getContentPane().add(qtyProdIntroGOTLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 290, 49, 22));

        jLabel5.setText("Inicios");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 340, 90, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("Ensambladores");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 540, 168, -1));

        popProdBegGOT.setText("-");
        popProdBegGOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popProdBegGOTActionPerformed(evt);
            }
        });
        getContentPane().add(popProdBegGOT, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 340, 30, -1));
        getContentPane().add(qtyProdBegGOTLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 340, 49, 22));

        pushProdBegGOT.setText("+");
        pushProdBegGOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pushProdBegGOTActionPerformed(evt);
            }
        });
        getContentPane().add(pushProdBegGOT, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 340, -1, -1));

        jLabel12.setText("Cierres");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 390, 90, -1));

        popProdEndGOT.setText("-");
        popProdEndGOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popProdEndGOTActionPerformed(evt);
            }
        });
        getContentPane().add(popProdEndGOT, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 390, 30, -1));
        getContentPane().add(qtyProdEndGOTLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 390, 49, 22));

        pushProdEndGOT.setText("+");
        pushProdEndGOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pushProdEndGOTActionPerformed(evt);
            }
        });
        getContentPane().add(pushProdEndGOT, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 390, -1, -1));

        jLabel13.setText("Plot twists");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 440, -1, -1));

        popProdPlotGOT.setText("-");
        popProdPlotGOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popProdPlotGOTActionPerformed(evt);
            }
        });
        getContentPane().add(popProdPlotGOT, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 440, 30, -1));
        getContentPane().add(qtyPlotsGOT, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 390, 41, 21));

        pushProdPlotGOT.setText("+");
        pushProdPlotGOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pushProdPlotGOTActionPerformed(evt);
            }
        });
        getContentPane().add(pushProdPlotGOT, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 440, -1, -1));

        jLabel14.setText("Créditos");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 490, -1, -1));

        popAssemblerGOT.setText("-");
        popAssemblerGOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popAssemblerGOTActionPerformed(evt);
            }
        });
        getContentPane().add(popAssemblerGOT, new org.netbeans.lib.awtextra.AbsoluteConstraints(780, 540, -1, -1));
        getContentPane().add(qtyCredGOT, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 450, 41, 22));

        pushProdCredGOT.setText("+");
        pushProdCredGOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pushProdCredGOTActionPerformed(evt);
            }
        });
        getContentPane().add(pushProdCredGOT, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 490, -1, -1));

        jLabel15.setText("Productores desocupados");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 240, 140, 26));
        getContentPane().add(freeProdsGOTLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 240, 43, 26));
        freeProdsGOTLabel.setText(Integer.toString(freeProds));

        startButton.setText("PLAY");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });
        getContentPane().add(startButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 190, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/got.png"))); // NOI18N
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1240, 180));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel16.setText("RESULTADOS");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 460, 190, -1));
        getContentPane().add(qtyAssemblersLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 540, 30, 20));

        popProdCredGOT.setText("-");
        popProdCredGOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popProdCredGOTActionPerformed(evt);
            }
        });
        getContentPane().add(popProdCredGOT, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 490, 30, -1));

        pushAssemblerGOT.setText("+");
        pushAssemblerGOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pushAssemblerGOTActionPerformed(evt);
            }
        });
        getContentPane().add(pushAssemblerGOT, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 540, -1, -1));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel17.setText("PRODUCTORES");
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 210, 168, -1));

        jLabel18.setText("Salarios:");
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 520, 100, 30));

        jLabel19.setText("Productores:");
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 250, 100, 30));

        jLabel20.setText("Ensambladores:");
        getContentPane().add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 290, 100, 30));

        jLabel21.setText("Project manager:");
        getContentPane().add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 330, 100, 30));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel22.setText("GASTOS EN SALARIOS");
        getContentPane().add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 210, 210, -1));

        jLabel23.setText("Regaños por ver R&M:");
        getContentPane().add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 410, 130, 30));

        jLabel24.setText("Ganancia:");
        getContentPane().add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 490, 100, 30));

        jLabel25.setText("Capitulos hechos");
        getContentPane().add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 290, -1, 20));

        jLabel26.setText("Director:");
        getContentPane().add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 370, 100, 30));
        getContentPane().add(prodSalariesLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 260, 80, 20));
        getContentPane().add(assemblersSalariesLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 300, 80, 20));
        getContentPane().add(pmSalaryLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 340, 80, 20));
        getContentPane().add(directorSalaryLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 380, 90, 20));
        getContentPane().add(RaMLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 420, 80, 20));
        getContentPane().add(earningsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 500, 80, 20));
        getContentPane().add(salariesLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 530, 80, 20));
        getContentPane().add(balanceLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 560, 70, 20));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void popIntroProdGOTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popIntroProdGOTActionPerformed
        if (this.arrayIntroGOT != null) {
            if (Integer.parseInt(this.qtyProdIntroGOTLabel.getText()) > 0) {
                qtyProdIntroGOT--;
                qtyProdIntroGOTLabel.setText(Integer.toString(qtyProdIntroGOT));
                freeProds++;
                freeProdsGOTLabel.setText(Integer.toString(freeProds));
                if (this.start) {
                    this.arrayIntroGOT[qtyProdIntroGOT].setStop(true);
                }
            }

        }

    }//GEN-LAST:event_popIntroProdGOTActionPerformed

    private void popProdBegGOTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popProdBegGOTActionPerformed
        if (this.arrayBegGOT != null) {
            if (Integer.parseInt(this.qtyProdBegGOTLabel.getText()) > 0) {
                qtyProdBegGOT--;
                qtyProdBegGOTLabel.setText(Integer.toString(qtyProdBegGOT));
                freeProds++;
                freeProdsGOTLabel.setText(Integer.toString(freeProds));
                if (this.start) {
                    this.arrayBegGOT[qtyProdBegGOT].setStop(true);
                }
            }

        }
    }//GEN-LAST:event_popProdBegGOTActionPerformed

    private void popProdPlotGOTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popProdPlotGOTActionPerformed
        if (this.arrayPlotGOT != null) {
            if (Integer.parseInt(this.qtyProdPlotsGOTLabel.getText()) > 0) {
                qtyProdPlotGOT--;
                qtyProdPlotsGOTLabel.setText(Integer.toString(qtyProdPlotGOT));
                freeProds++;
                freeProdsGOTLabel.setText(Integer.toString(freeProds));
                if (this.start) {
                    this.arrayPlotGOT[qtyProdPlotGOT].setStop(true);
                }
            }

        }
    }//GEN-LAST:event_popProdPlotGOTActionPerformed

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        if (!this.start) {
            start = true;

            // Llenar arrays de productores
            // Intros
            for (int i = 0; i < qtyProdIntroGOT; i++) {
                this.arrayIntroGOT[i] = new prodIntroGOT(semIntroMutexGOT, semIntroGOT, semEnsIntroGOT);
                this.arrayIntroGOT[i].start();
            }

            // Beginnings
            for (int i = 0; i < qtyProdBegGOT; i++) {
                this.arrayBegGOT[i] = new prodBegGOT(semBegMutexGOT, semBegGOT, semEnsBegGOT);
                this.arrayBegGOT[i].start();
            }

            // Endings
            for (int i = 0; i < qtyProdEndGOT; i++) {
                this.arrayEndGOT[i] = new prodEndGOT(semEndMutexGOT, semEndGOT, semEnsEndGOT);
                this.arrayEndGOT[i].start();
            }

            // Credits
            for (int i = 0; i < qtyProdCredGOT; i++) {
                this.arrayCredGOT[i] = new prodCredGOT(semCredMutexGOT, semCredGOT, semEnsCredGOT);
                this.arrayCredGOT[i].start();
            }

            // Plot twists
            for (int i = 0; i < qtyProdPlotGOT; i++) {
                this.arrayPlotGOT[i] = new prodPlotGOT(semPlotMutexGOT, semPlotGOT, semEnsPlotGOT);
                this.arrayPlotGOT[i].start();
            }

            //Llenar array de ensambladores
            this.semAssemblerMutexGOT = new Semaphore(1);
            for (int i = 0; i < qtyAssemblersGOT; i++) {
                this.arrayAssemblersGOT[i] = new assemblerGOT(semAssemblerMutexGOT, semIntroGOT, semIntroMutexGOT, semEnsIntroGOT, semBegGOT, semBegMutexGOT, semEnsBegGOT, semEndGOT, semEndMutexGOT, semEnsEndGOT, semCredGOT, semCredMutexGOT, semEnsCredGOT, semPlotGOT, semPlotMutexGOT, semEnsPlotGOT);
                this.arrayAssemblersGOT[i].start();
            }
//            this.assemblerGOT = new assemblerGOT(semAssemblerMutexGOT, semIntroGOT, semIntroMutexGOT, semEnsIntroGOT, semBegGOT, semBegMutexGOT, semEnsBegGOT, semEndGOT, semEndMutexGOT, semEnsEndGOT, semCredGOT, semCredMutexGOT, semEnsCredGOT, semPlotGOT, semPlotMutexGOT, semEnsPlotGOT);
//            this.assemblerGOT.start();

            //Instanciar el PM
            this.counterGOT = new Semaphore(cutDuration);
            this.counterMutexGOT = new Semaphore(1);
            this.pmGOT = new ProjectManagerGOT(counterGOT, counterMutexGOT);
            this.pmGOT.start();

            //Instanciar el director
            this.directorGOT = new directorGOT(pmGOT, counterGOT, counterMutexGOT);
            this.directorGOT.start();
        }
    }//GEN-LAST:event_startButtonActionPerformed

    private void pushIntroProdGOTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pushIntroProdGOTActionPerformed
        if (this.arrayIntroGOT != null) {
            if (freeProds > 0) {

                arrayIntroGOT[qtyProdIntroGOT] = new prodIntroGOT(semIntroMutexGOT, semIntroGOT, semEnsIntroGOT);
                qtyProdIntroGOT++;
                this.qtyProdIntroGOTLabel.setText(Integer.toString(qtyProdIntroGOT));
                if (this.start) {
                    this.arrayIntroGOT[qtyProdIntroGOT - 1].start();
                }
                freeProds--;
                freeProdsGOTLabel.setText(Integer.toString(freeProds));
            }
        }
    }//GEN-LAST:event_pushIntroProdGOTActionPerformed

    private void pushProdBegGOTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pushProdBegGOTActionPerformed
        if (this.arrayBegGOT != null) {
            if (freeProds > 0) {

                arrayBegGOT[qtyProdBegGOT] = new prodBegGOT(semBegMutexGOT, semBegGOT, semEnsBegGOT);
                qtyProdBegGOT++;
                this.qtyProdBegGOTLabel.setText(Integer.toString(qtyProdBegGOT));
                if (this.start) {
                    this.arrayBegGOT[qtyProdBegGOT - 1].start();
                }
                freeProds--;
                freeProdsGOTLabel.setText(Integer.toString(freeProds));
            }
        }
    }//GEN-LAST:event_pushProdBegGOTActionPerformed

    private void popProdEndGOTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popProdEndGOTActionPerformed
        if (this.arrayEndGOT != null) {
            if (Integer.parseInt(qtyProdEndGOTLabel.getText()) > 0) {
                qtyProdEndGOT--;
                qtyProdEndGOTLabel.setText(Integer.toString(qtyProdEndGOT));
                freeProds++;
                freeProdsGOTLabel.setText(Integer.toString(freeProds));
                if (this.start) {
                    this.arrayEndGOT[qtyProdEndGOT].setStop(true);
                }
            }

        }
    }//GEN-LAST:event_popProdEndGOTActionPerformed

    private void pushProdEndGOTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pushProdEndGOTActionPerformed
        if (this.arrayEndGOT != null) {
            if (freeProds > 0) {

                arrayEndGOT[qtyProdEndGOT] = new prodEndGOT(semEndMutexGOT, semEndGOT, semEnsEndGOT);
                qtyProdEndGOT++;
                this.qtyProdEndGOTLabel.setText(Integer.toString(qtyProdEndGOT));
                if (this.start) {
                    this.arrayEndGOT[qtyProdEndGOT - 1].start();
                }
                freeProds--;
                freeProdsGOTLabel.setText(Integer.toString(freeProds));
            }
        }
    }//GEN-LAST:event_pushProdEndGOTActionPerformed

    private void pushProdPlotGOTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pushProdPlotGOTActionPerformed
        if (this.arrayPlotGOT != null) {
            if (freeProds > 0) {

                arrayPlotGOT[qtyProdPlotGOT] = new prodPlotGOT(semPlotMutexGOT, semPlotGOT, semEnsPlotGOT);
                qtyProdPlotGOT++;
                this.qtyProdPlotsGOTLabel.setText(Integer.toString(qtyProdPlotGOT));
                if (this.start) {
                    this.arrayPlotGOT[qtyProdPlotGOT - 1].start();
                }
                freeProds--;
                freeProdsGOTLabel.setText(Integer.toString(freeProds));
            }
        }
    }//GEN-LAST:event_pushProdPlotGOTActionPerformed

    private void popAssemblerGOTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popAssemblerGOTActionPerformed
        if (qtyAssemblersGOT > 1) {
            qtyAssemblersGOT--;
            qtyAssemblersLabel.setText(Integer.toString(qtyAssemblersGOT));
            if (this.start) {
                arrayAssemblersGOT[qtyAssemblersGOT].setStop(true);
            }
        }
    }//GEN-LAST:event_popAssemblerGOTActionPerformed

    private void pushProdCredGOTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pushProdCredGOTActionPerformed
        if (this.arrayCredGOT != null) {
            if (freeProds > 0) {

                arrayCredGOT[qtyProdCredGOT] = new prodCredGOT(semCredMutexGOT, semCredGOT, semEnsCredGOT);
                qtyProdCredGOT++;
                this.qtyProdCredsGOTLabel.setText(Integer.toString(qtyProdCredGOT));
                if (this.start) {
                    this.arrayCredGOT[qtyProdCredGOT - 1].start();
                }
                freeProds--;
                freeProdsGOTLabel.setText(Integer.toString(freeProds));
            }
        }
    }//GEN-LAST:event_pushProdCredGOTActionPerformed

    private void popProdCredGOTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popProdCredGOTActionPerformed
        if (this.arrayCredGOT != null) {
            if (Integer.parseInt(qtyProdCredsGOTLabel.getText()) > 0) {
                qtyProdCredGOT--;
                qtyProdCredsGOTLabel.setText(Integer.toString(qtyProdCredGOT));
                freeProds++;
                freeProdsGOTLabel.setText(Integer.toString(freeProds));
                if (this.start) {
                    this.arrayCredGOT[qtyProdCredGOT].setStop(true);
                }
            }

        }
    }//GEN-LAST:event_popProdCredGOTActionPerformed

    private void pushAssemblerGOTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pushAssemblerGOTActionPerformed
        if (this.arrayAssemblersGOT != null && this.qtyAssemblersGOT < 9) {
            arrayAssemblersGOT[qtyAssemblersGOT] = new assemblerGOT(semAssemblerMutexGOT, semIntroGOT, semIntroMutexGOT, semEnsIntroGOT, semBegGOT, semBegMutexGOT, semEnsBegGOT, semEndGOT, semEndMutexGOT, semEnsEndGOT, semCredGOT, semCredMutexGOT, semEnsCredGOT, semPlotGOT, semPlotMutexGOT, semEnsPlotGOT);
            arrayAssemblersGOT[qtyAssemblersGOT].start();
            qtyAssemblersGOT++;
            qtyAssemblersLabel.setText(Integer.toString(qtyAssemblersGOT));
        }
    }//GEN-LAST:event_pushAssemblerGOTActionPerformed

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
            java.util.logging.Logger.getLogger(GOTInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GOTInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GOTInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GOTInterface.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GOTInterface().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JLabel RaMLabel;
    public static javax.swing.JLabel assemblersSalariesLabel;
    public static javax.swing.JLabel balanceLabel;
    public static javax.swing.JLabel chaptersMade;
    public static javax.swing.JLabel daysUntilCut;
    public static javax.swing.JLabel directorSalaryLabel;
    public static javax.swing.JLabel earningsLabel;
    public static javax.swing.JLabel freeProdsGOTLabel;
    private javax.swing.JLabel jLabel1;
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    public static javax.swing.JLabel pmSalaryLabel;
    public static javax.swing.JButton popAssemblerGOT;
    public static javax.swing.JButton popIntroProdGOT;
    public static javax.swing.JButton popProdBegGOT;
    private javax.swing.JButton popProdCredGOT;
    public static javax.swing.JButton popProdEndGOT;
    public static javax.swing.JButton popProdPlotGOT;
    public static javax.swing.JLabel prodSalariesLabel;
    public static javax.swing.JButton pushAssemblerGOT;
    public static javax.swing.JButton pushIntroProdGOT;
    public static javax.swing.JButton pushProdBegGOT;
    public static javax.swing.JButton pushProdCredGOT;
    public static javax.swing.JButton pushProdEndGOT;
    public static javax.swing.JButton pushProdPlotGOT;
    public static javax.swing.JLabel qtyAssemblersLabel;
    public static javax.swing.JLabel qtyBegsGOT;
    public static javax.swing.JLabel qtyCredGOT;
    public static javax.swing.JLabel qtyEndsGOT;
    public static javax.swing.JLabel qtyIntrosGOT;
    public static javax.swing.JLabel qtyPlotsGOT;
    public static javax.swing.JLabel qtyProdBegGOTLabel;
    public static javax.swing.JLabel qtyProdCredsGOTLabel;
    public static javax.swing.JLabel qtyProdEndGOTLabel;
    public static javax.swing.JLabel qtyProdIntroGOTLabel;
    public static javax.swing.JLabel qtyProdPlotsGOTLabel;
    public static javax.swing.JLabel salariesLabel;
    private javax.swing.JButton startButton;
    // End of variables declaration//GEN-END:variables
}
