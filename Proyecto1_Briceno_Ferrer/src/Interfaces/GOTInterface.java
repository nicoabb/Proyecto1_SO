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
import Classes.ProjectManagerGOT;
import Classes.assemblerGOT;
import Classes.directorGOT;
import Classes.prodBegGOT;
import Classes.prodCredGOT;
import Classes.prodEndGOT;
import Classes.prodIntroGOT;
import Classes.prodPlotGOT;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Nicolás Briceño
 */
public class GOTInterface extends javax.swing.JFrame {

    public static boolean start;
    public static int dayDuration; // Duracion de un dia (ms), pasada por el JSON
    public static int hourDuration; // Duracion de una hora (ms)

    public static int freeProds = 0;
    
    public static int qtyProdIntroGOT, qtyProdBegGOT, qtyProdEndGOT, qtyProdCredGOT, qtyProdPlotGOT;

    public static int introDrive, begDrive, endDrive, credDrive, plotDrive;

    public static int cutDuration; // Tiempo entre cortes, sacado del JSON

//  Declarar productores GOT
    private prodIntroGOT prodIntroGOT;
    private prodIntroGOT arrayIntroGOT[];

    private prodCredGOT prodCredGOT;
    private prodCredGOT arrayCredGOT[];

    private prodBegGOT prodBegGOT;
    private prodBegGOT arrayBegGOT[];

    private prodEndGOT prodEndGOT;
    private prodEndGOT arrayEndGOT[];

    private prodPlotGOT prodPlotGOT;
    private prodPlotGOT arrayPlotGOT[];

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
    private ProjectManagerGOT pmGOT;
    private Semaphore counterMutexGOT;

//  Declarar Director
    private directorGOT directorGOT;

    /**
     * Creates new form Dashboard
     */
    public GOTInterface() {
        initComponents();
        readJson();
    }

    public void readJson() {
        JSONParser parser = new JSONParser();

        try ( Reader reader = new FileReader("src/data.json")) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            JSONObject dayObj = (JSONObject) jsonObject.get("days");
            dayDuration = ((Long) dayObj.get("dayLength")).intValue();
            hourDuration = dayDuration / 24;
            cutDuration = ((Long) dayObj.get("cutDuration")).intValue();

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
//        this.prodCredGOT = new prodCredGOT(semCredMutexGOT, semCredGOT, semEnsCredGOT);
//        this.prodCredGOT.start();

        //Instanciar al productor de inicios
        this.semBegGOT = new Semaphore(begDrive);
        this.semBegMutexGOT = new Semaphore(1);
        this.semEnsBegGOT = new Semaphore(0);

        this.arrayBegGOT = new prodBegGOT[19];
//        this.prodBegGOT = new prodBegGOT(semBegMutexGOT, semBegGOT, semEnsBegGOT);
//        this.prodBegGOT.start();

        //Instanciar al productor de cierres
        this.semEndGOT = new Semaphore(endDrive);
        this.semEndMutexGOT = new Semaphore(1);
        this.semEnsEndGOT = new Semaphore(0);

        this.arrayEndGOT = new prodEndGOT[19];
//        this.prodEndGOT = new prodEndGOT(semEndMutexGOT, semEndGOT, semEnsEndGOT);
//        this.prodEndGOT.start();

        //Instanciar al productor de plot twists
        this.semPlotGOT = new Semaphore(plotDrive);
        this.semPlotMutexGOT = new Semaphore(1);
        this.semEnsPlotGOT = new Semaphore(0);

        this.arrayPlotGOT = new prodPlotGOT[19];
//        this.prodPlotGOT = new prodPlotGOT(semPlotMutexGOT, semPlotGOT, semEnsPlotGOT);
//        this.prodPlotGOT.start();

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
        jLabel3 = new javax.swing.JLabel();
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
        popProdCredGOT = new javax.swing.JButton();
        qtyCredGOT = new javax.swing.JLabel();
        pushProdCredGOT = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        freeProdsGOTLabel = new javax.swing.JLabel();
        startButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel7.setText("Intros disponibles");

        jLabel8.setText("Inicios disponibles");

        jLabel9.setText("Cierres disponibles");

        jLabel10.setText("Creditos disponibles");

        jLabel11.setText("Plots disponibles");

        jLabel1.setText("Capitulos hechos");

        jLabel2.setText("Dias para el corte");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 3, 24)); // NOI18N
        jLabel3.setText("Game of Thrones - Emilio Ferrer");

        popIntroProdGOT.setText("-");
        popIntroProdGOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popIntroProdGOTActionPerformed(evt);
            }
        });

        pushIntroProdGOT.setText("+");
        pushIntroProdGOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pushIntroProdGOTActionPerformed(evt);
            }
        });

        jLabel4.setText("Introducciones");

        jLabel5.setText("Inicios");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("PRODUCTORES");

        popProdBegGOT.setText("-");
        popProdBegGOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popProdBegGOTActionPerformed(evt);
            }
        });

        pushProdBegGOT.setText("+");
        pushProdBegGOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pushProdBegGOTActionPerformed(evt);
            }
        });

        jLabel12.setText("Cierres");

        popProdEndGOT.setText("-");
        popProdEndGOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popProdEndGOTActionPerformed(evt);
            }
        });

        pushProdEndGOT.setText("+");
        pushProdEndGOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pushProdEndGOTActionPerformed(evt);
            }
        });

        jLabel13.setText("Plot twists");

        popProdPlotGOT.setText("-");
        popProdPlotGOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popProdPlotGOTActionPerformed(evt);
            }
        });

        pushProdPlotGOT.setText("+");
        pushProdPlotGOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pushProdPlotGOTActionPerformed(evt);
            }
        });

        jLabel14.setText("Créditos");

        popProdCredGOT.setText("-");
        popProdCredGOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popProdCredGOTActionPerformed(evt);
            }
        });

        pushProdCredGOT.setText("+");
        pushProdCredGOT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pushProdCredGOTActionPerformed(evt);
            }
        });

        jLabel15.setText("Directores desocupados");

        startButton.setText("PLAY");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(57, 107, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addGap(269, 269, 269))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(startButton))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(popIntroProdGOT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(popProdBegGOT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(popProdEndGOT)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel14)
                                            .addComponent(jLabel13))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(popProdPlotGOT, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(popProdCredGOT, javax.swing.GroupLayout.Alignment.TRAILING))))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(qtyProdIntroGOTLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(qtyProdBegGOTLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(qtyProdEndGOTLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(qtyProdPlotsGOTLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(qtyProdCredsGOTLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(pushIntroProdGOT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(pushProdBegGOT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(pushProdEndGOT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(pushProdPlotGOT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(pushProdCredGOT))
                                        .addGap(115, 115, 115))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(45, 45, 45)
                                        .addComponent(freeProdsGOTLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(qtyPlotsGOT, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(qtyEndsGOT, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(qtyCredGOT, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(qtyBegsGOT, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(167, 167, 167))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(qtyIntrosGOT, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(39, 39, 39))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chaptersMade, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(daysUntilCut, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(286, 286, 286))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(startButton))
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                            .addComponent(freeProdsGOTLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(qtyProdIntroGOTLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(popIntroProdGOT)
                                .addComponent(pushIntroProdGOT)
                                .addComponent(jLabel4)))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(popProdBegGOT)
                            .addComponent(qtyProdBegGOTLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pushProdBegGOT))
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(popProdEndGOT)
                            .addComponent(qtyProdEndGOTLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pushProdEndGOT))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel13)
                                    .addComponent(popProdPlotGOT))
                                .addGap(31, 31, 31)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel14)
                                    .addComponent(popProdCredGOT)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(pushProdPlotGOT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(qtyProdPlotsGOTLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(31, 31, 31)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(pushProdCredGOT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(qtyProdCredsGOTLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(qtyIntrosGOT, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(41, 41, 41)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(37, 37, 37)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(qtyEndsGOT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(32, 32, 32)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addGap(42, 42, 42))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(qtyPlotsGOT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(37, 37, 37)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(qtyCredGOT, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(qtyBegsGOT, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(104, 104, 104))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(chaptersMade, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(daysUntilCut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2))))
                .addGap(246, 246, 246))
        );

        freeProdsGOTLabel.setText(Integer.toString(freeProds));

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

            //Instanciar al ensamblador
            this.semAssemblerMutexGOT = new Semaphore(1);
            this.assemblerGOT = new assemblerGOT(semAssemblerMutexGOT, semIntroGOT, semIntroMutexGOT, semEnsIntroGOT, semBegGOT, semBegMutexGOT, semEnsBegGOT, semEndGOT, semEndMutexGOT, semEnsEndGOT, semCredGOT, semCredMutexGOT, semEnsCredGOT, semPlotGOT, semPlotMutexGOT, semEnsPlotGOT);
            this.assemblerGOT.start();

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
        if(this.arrayIntroGOT != null){
            if(freeProds > 0){
                
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
        if(this.arrayBegGOT != null){
            if(freeProds > 0){
                
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
        if(this.arrayEndGOT != null){
            if(freeProds > 0){
                
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
        if(this.arrayPlotGOT != null){
            if(freeProds > 0){
                
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

    private void pushProdCredGOTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pushProdCredGOTActionPerformed
        if(this.arrayCredGOT != null){
            if(freeProds > 0){
                
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
    public static javax.swing.JLabel chaptersMade;
    public static javax.swing.JLabel daysUntilCut;
    public static javax.swing.JLabel freeProdsGOTLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    public static javax.swing.JButton popIntroProdGOT;
    public static javax.swing.JButton popProdBegGOT;
    public static javax.swing.JButton popProdCredGOT;
    public static javax.swing.JButton popProdEndGOT;
    public static javax.swing.JButton popProdPlotGOT;
    public static javax.swing.JButton pushIntroProdGOT;
    public static javax.swing.JButton pushProdBegGOT;
    public static javax.swing.JButton pushProdCredGOT;
    public static javax.swing.JButton pushProdEndGOT;
    public static javax.swing.JButton pushProdPlotGOT;
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
    private javax.swing.JButton startButton;
    // End of variables declaration//GEN-END:variables
}
