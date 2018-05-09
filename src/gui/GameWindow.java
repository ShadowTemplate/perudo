package gui;

import config.LanguageManager;
import entity.Bet;
import entity.Die;
import entity.ListEntry;
import entity.PlayerBet;
import entity.SerializableBufferedImage;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;
import miscellanea.Constants;
import miscellanea.Handler;
import miscellanea.Utility;

public class GameWindow extends javax.swing.JFrame {

    private static RulesWindow rulesWindow;
    private static DevelopersWindow developersWindow;
    private static SettingsWindow settingsWindow;
    private static TipWindow tipWindow;
    private static TipResultWindow tipResultWindow;

    private final ArrayList<JButton> diceList = new ArrayList<>();
    private HashMap<Die, Integer> minimumPossibleValues;
    private final DefaultComboBoxModel<Integer> physicalPlayerTimesModel;
    private final DefaultComboBoxModel<Die> physicalPlayerDiceModel;
    private final DefaultComboBoxModel<Integer> virtualPlayerTimesModel;
    private final DefaultComboBoxModel<Die> virtualPlayerDiceModel;

    private final DefaultComboBoxModel<String> filterBoxModel;

    private final DefaultListModel<ListEntry> infoModel = new DefaultListModel<>();

    private final DefaultTableModel betLog;
    private final TableRowSorter sorter;

    private JFileChooser fileChooser = new JFileChooser(Constants.PERUDO_HOME_PATH);

    public GameWindow() {
        initComponents();
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);

        JTableHeader th = logTable.getTableHeader();
        TableColumnModel tcm = th.getColumnModel();
        TableColumn tc = tcm.getColumn(0);
        tc.setHeaderValue(LanguageManager.getValue("Player"));
        tc = tcm.getColumn(1);
        tc.setHeaderValue(LanguageManager.getValue("Bet2"));
        th.repaint();

        diceList.add(die1);
        diceList.add(die2);
        diceList.add(die3);
        diceList.add(die4);
        diceList.add(die5);

        physicalPlayerTimesModel = new DefaultComboBoxModel<>();
        timesBox.setModel(physicalPlayerTimesModel);
        physicalPlayerDiceModel = new DefaultComboBoxModel<>();
        dieBox.setModel(physicalPlayerDiceModel);

        virtualPlayerTimesModel = new DefaultComboBoxModel<>();
        oppTimesBox.setModel(virtualPlayerTimesModel);
        virtualPlayerDiceModel = new DefaultComboBoxModel<>();
        oppDieBox.setModel(virtualPlayerDiceModel);

        timesBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updatePossibleDice(true);
            }
        });

        oppTimesBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updatePossibleDice(false);
            }
        });

        filterBoxModel = new DefaultComboBoxModel<>();
        filterBox.setModel(filterBoxModel);

        infoList.setModel(infoModel);

        betLog = (DefaultTableModel) logTable.getModel();
        sorter = new TableRowSorter<>(betLog);
        logTable.setRowSorter(sorter);

        disableTextField(fileChooser.getComponents());
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(Constants.MATCH_FILE_EXTENSION);
            }

            @Override
            public String getDescription() {
                return LanguageManager.getValue("Match") + " (" + Constants.MATCH_FILE_EXTENSION + ")";
            }
        });

        rulesWindow = new RulesWindow();
        developersWindow = new DevelopersWindow();
        settingsWindow = new SettingsWindow();
        tipWindow = new TipWindow();
        tipResultWindow = new TipResultWindow(this);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        logPanel = new javax.swing.JPanel();
        tablePanel = new javax.swing.JScrollPane();
        logTable = new javax.swing.JTable();
        filterLabel = new javax.swing.JLabel();
        filterBox = new javax.swing.JComboBox<String>();
        dicePanel = new javax.swing.JPanel();
        die1 = new javax.swing.JButton();
        die5 = new javax.swing.JButton();
        die2 = new javax.swing.JButton();
        die4 = new javax.swing.JButton();
        die3 = new javax.swing.JButton();
        infoPanel = new javax.swing.JPanel();
        infoArea = new javax.swing.JScrollPane();
        infoList = new javax.swing.JList<ListEntry>();
        opponentsPanel = new javax.swing.JPanel();
        opponentNameLabel = new javax.swing.JLabel();
        oppTimesBox = new javax.swing.JComboBox<Integer>();
        oppDieBox = new javax.swing.JComboBox();
        oppDudoButton = new javax.swing.JButton();
        nextBetButton = new javax.swing.JButton();
        avatarLabel = new javax.swing.JLabel();
        playerPanel = new javax.swing.JPanel();
        timesBox = new javax.swing.JComboBox<Integer>();
        dieBox = new javax.swing.JComboBox<Die>();
        betButton = new javax.swing.JButton();
        dudoButton = new javax.swing.JButton();
        helpButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMatchLabel = new javax.swing.JMenuItem();
        saveMatchLabel = new javax.swing.JMenuItem();
        loadMatchLabel = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        settingsLabel = new javax.swing.JMenuItem();
        rulesLabel = new javax.swing.JMenuItem();
        developersLabel = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        exitLabel = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(LanguageManager.getValue("PERUDO"));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        logPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), LanguageManager.getValue("Bet log")));

        logTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Giocatore", "Scommessa"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        logTable.setEnabled(false);
        tablePanel.setViewportView(logTable);
        if (logTable.getColumnModel().getColumnCount() > 0) {
            logTable.getColumnModel().getColumn(1).setResizable(false);
        }

        filterLabel.setText(LanguageManager.getValue("Filter:"));

        filterBox.setEnabled(false);
        filterBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                filterBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout logPanelLayout = new javax.swing.GroupLayout(logPanel);
        logPanel.setLayout(logPanelLayout);
        logPanelLayout.setHorizontalGroup(
            logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(logPanelLayout.createSequentialGroup()
                .addComponent(filterLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filterBox, 0, 204, Short.MAX_VALUE))
        );
        logPanelLayout.setVerticalGroup(
            logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logPanelLayout.createSequentialGroup()
                .addComponent(tablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 353, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(filterBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterLabel))
                .addContainerGap())
        );

        dicePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), LanguageManager.getValue("Your dice")));

        die1.setEnabled(false);
        die1.setPreferredSize(new java.awt.Dimension(60, 60));

        die5.setEnabled(false);
        die5.setPreferredSize(new java.awt.Dimension(60, 60));

        die2.setEnabled(false);
        die2.setPreferredSize(new java.awt.Dimension(60, 60));

        die4.setEnabled(false);
        die4.setPreferredSize(new java.awt.Dimension(60, 60));

        die3.setEnabled(false);
        die3.setPreferredSize(new java.awt.Dimension(60, 60));

        javax.swing.GroupLayout dicePanelLayout = new javax.swing.GroupLayout(dicePanel);
        dicePanel.setLayout(dicePanelLayout);
        dicePanelLayout.setHorizontalGroup(
            dicePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dicePanelLayout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(die1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(die2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(die3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(die4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(die5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dicePanelLayout.setVerticalGroup(
            dicePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dicePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dicePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(die1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(die5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(die2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(die4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(die3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        infoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), LanguageManager.getValue("Match info")));

        infoList.setEnabled(false);
        infoArea.setViewportView(infoList);

        javax.swing.GroupLayout infoPanelLayout = new javax.swing.GroupLayout(infoPanel);
        infoPanel.setLayout(infoPanelLayout);
        infoPanelLayout.setHorizontalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(infoArea, javax.swing.GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
        );
        infoPanelLayout.setVerticalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(infoArea, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        opponentsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), LanguageManager.getValue("Opponent round")));

        opponentNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        opponentNameLabel.setAlignmentY(0.0F);
        opponentNameLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        oppTimesBox.setEnabled(false);

        oppDieBox.setEnabled(false);

        oppDudoButton.setText(LanguageManager.getValue("Dudo"));
        oppDudoButton.setEnabled(false);
        oppDudoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oppDudoButtonActionPerformed(evt);
            }
        });

        nextBetButton.setText(LanguageManager.getValue("Next bet"));
        nextBetButton.setEnabled(false);
        nextBetButton.setMaximumSize(new java.awt.Dimension(150, 23));
        nextBetButton.setMinimumSize(new java.awt.Dimension(150, 23));
        nextBetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextBetButtonActionPerformed(evt);
            }
        });

        avatarLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout opponentsPanelLayout = new javax.swing.GroupLayout(opponentsPanel);
        opponentsPanel.setLayout(opponentsPanelLayout);
        opponentsPanelLayout.setHorizontalGroup(
            opponentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(opponentNameLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(avatarLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(opponentsPanelLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(oppTimesBox, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(oppDieBox, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(opponentsPanelLayout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(oppDudoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nextBetButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        opponentsPanelLayout.setVerticalGroup(
            opponentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(opponentsPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(opponentNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 131, Short.MAX_VALUE)
                .addComponent(avatarLabel)
                .addGap(10, 10, 10)
                .addGroup(opponentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(oppTimesBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(oppDieBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(opponentsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nextBetButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(oppDudoButton))
                .addGap(18, 18, 18))
        );

        playerPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), LanguageManager.getValue("Your turn")));

        timesBox.setEnabled(false);

        dieBox.setEnabled(false);

        betButton.setText(LanguageManager.getValue("Bet"));
        betButton.setEnabled(false);
        betButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                betButtonActionPerformed(evt);
            }
        });

        dudoButton.setText(LanguageManager.getValue("Dudo"));
        dudoButton.setEnabled(false);
        dudoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dudoButtonActionPerformed(evt);
            }
        });

        helpButton.setText(LanguageManager.getValue("Help"));
        helpButton.setEnabled(false);
        helpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                helpButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout playerPanelLayout = new javax.swing.GroupLayout(playerPanel);
        playerPanel.setLayout(playerPanelLayout);
        playerPanelLayout.setHorizontalGroup(
            playerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(playerPanelLayout.createSequentialGroup()
                .addComponent(timesBox, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dieBox, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(betButton, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dudoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(helpButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        playerPanelLayout.setVerticalGroup(
            playerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(playerPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(playerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timesBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dieBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(betButton)
                    .addComponent(dudoButton)
                    .addComponent(helpButton)))
        );

        fileMenu.setText("Menu");

        newMatchLabel.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newMatchLabel.setText(LanguageManager.getValue("New Match"));
        newMatchLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newMatchLabelActionPerformed(evt);
            }
        });
        fileMenu.add(newMatchLabel);

        saveMatchLabel.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMatchLabel.setText(LanguageManager.getValue("Save Match"));
        saveMatchLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMatchLabelActionPerformed(evt);
            }
        });
        fileMenu.add(saveMatchLabel);

        loadMatchLabel.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        loadMatchLabel.setText(LanguageManager.getValue("Load Match"));
        loadMatchLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadMatchLabelActionPerformed(evt);
            }
        });
        fileMenu.add(loadMatchLabel);
        fileMenu.add(jSeparator1);

        settingsLabel.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        settingsLabel.setText(LanguageManager.getValue("Settings"));
        settingsLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsLabelActionPerformed(evt);
            }
        });
        fileMenu.add(settingsLabel);

        rulesLabel.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        rulesLabel.setText(LanguageManager.getValue("Rules"));
        rulesLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rulesLabelActionPerformed(evt);
            }
        });
        fileMenu.add(rulesLabel);

        developersLabel.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        developersLabel.setText(LanguageManager.getValue("Developers"));
        developersLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                developersLabelActionPerformed(evt);
            }
        });
        fileMenu.add(developersLabel);
        fileMenu.add(jSeparator2);

        exitLabel.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        exitLabel.setText(LanguageManager.getValue("Exit"));
        exitLabel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitLabelActionPerformed(evt);
            }
        });
        fileMenu.add(exitLabel);

        menuBar.add(fileMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dicePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(infoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(opponentsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(playerPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(logPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(infoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(opponentsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dicePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(playerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(5, 5, 5))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newMatchLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newMatchLabelActionPerformed
        Handler.resetMatch();
        Handler.initMatch();
    }//GEN-LAST:event_newMatchLabelActionPerformed

    private void rulesLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rulesLabelActionPerformed
        rulesWindow.setVisible(true);
    }//GEN-LAST:event_rulesLabelActionPerformed

    private void settingsLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsLabelActionPerformed
        settingsWindow.setVisible(true);
    }//GEN-LAST:event_settingsLabelActionPerformed

    private void exitLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitLabelActionPerformed
        if (JOptionPane.showConfirmDialog(rootPane, LanguageManager.getValue("The current match will be lost.\\nAre you sure you want to exit?"), LanguageManager.getValue("CONFIRM OPERATION"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_exitLabelActionPerformed

    private void developersLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_developersLabelActionPerformed
        developersWindow.setVisible(true);
    }//GEN-LAST:event_developersLabelActionPerformed

    private void nextBetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextBetButtonActionPerformed
        if (oppTimesBox.isEnabled() && virtualPlayerTimesModel.getSize() != 0) { //The opponent is human, with at least one available bet: combobox will tell us his bet
            Handler.handleNewBet(new Bet((int) oppTimesBox.getSelectedItem(), (Die) oppDieBox.getSelectedItem()));
        } else if (oppTimesBox.isEnabled() && virtualPlayerTimesModel.getSize() == 0) { //The opponent is human, without any available bet: he shall doubt
            JOptionPane.showMessageDialog(null, LanguageManager.getValue("Due to the last bet the player can not raise.\\nHe can only Dudo... Do you think this is a bad move?"), LanguageManager.getValue("ATTENTION"), JOptionPane.ERROR_MESSAGE);
        } else { //The opponent is a CPU: the system shall calculate a bet for him
            Handler.calculateBetForOpponent();
        }
    }//GEN-LAST:event_nextBetButtonActionPerformed

    private void betButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_betButtonActionPerformed
        if (physicalPlayerTimesModel.getSize() != 0) { //He had at least one available bet and he choose it
            Handler.handleNewBet(new Bet((int) timesBox.getSelectedItem(), (Die) dieBox.getSelectedItem()));
        } else {
            JOptionPane.showMessageDialog(null, LanguageManager.getValue("Due to the last bet you can not raise.\\nYou can only Dudo... Do you think this is a bad move?"), LanguageManager.getValue("ATTENTION"), JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_betButtonActionPerformed

    private void filterBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_filterBoxItemStateChanged
        if (isActive()) {
            addFilter((String) filterBox.getSelectedItem());
        }
    }//GEN-LAST:event_filterBoxItemStateChanged

    private void dudoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dudoButtonActionPerformed
        Handler.handleNewBet(new Bet(0, Die.LLAMA));
    }//GEN-LAST:event_dudoButtonActionPerformed

    private void helpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_helpButtonActionPerformed
        tipWindow.setVisible(true);
    }//GEN-LAST:event_helpButtonActionPerformed

    private void oppDudoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oppDudoButtonActionPerformed
        Handler.handleNewBet(new Bet(0, Die.LLAMA));
    }//GEN-LAST:event_oppDudoButtonActionPerformed

    private void saveMatchLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMatchLabelActionPerformed
        Handler.saveMatch();
    }//GEN-LAST:event_saveMatchLabelActionPerformed

    private void loadMatchLabelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadMatchLabelActionPerformed
        int result = fileChooser.showOpenDialog(GameWindow.this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String matchPath = fileChooser.getSelectedFile().toString();
            Handler.loadMatch(matchPath);
        }
    }//GEN-LAST:event_loadMatchLabelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel avatarLabel;
    private javax.swing.JButton betButton;
    private javax.swing.JMenuItem developersLabel;
    private javax.swing.JPanel dicePanel;
    private javax.swing.JButton die1;
    private javax.swing.JButton die2;
    private javax.swing.JButton die3;
    private javax.swing.JButton die4;
    private javax.swing.JButton die5;
    private javax.swing.JComboBox<Die> dieBox;
    private javax.swing.JButton dudoButton;
    private javax.swing.JMenuItem exitLabel;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JComboBox<String> filterBox;
    private javax.swing.JLabel filterLabel;
    private javax.swing.JButton helpButton;
    private javax.swing.JScrollPane infoArea;
    private javax.swing.JList<ListEntry> infoList;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JMenuItem loadMatchLabel;
    private javax.swing.JPanel logPanel;
    private javax.swing.JTable logTable;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem newMatchLabel;
    private javax.swing.JButton nextBetButton;
    private javax.swing.JComboBox oppDieBox;
    private javax.swing.JButton oppDudoButton;
    private javax.swing.JComboBox<Integer> oppTimesBox;
    private javax.swing.JLabel opponentNameLabel;
    private javax.swing.JPanel opponentsPanel;
    private javax.swing.JPanel playerPanel;
    private javax.swing.JMenuItem rulesLabel;
    private javax.swing.JMenuItem saveMatchLabel;
    private javax.swing.JMenuItem settingsLabel;
    private javax.swing.JScrollPane tablePanel;
    private javax.swing.JComboBox<Integer> timesBox;
    // End of variables declaration//GEN-END:variables

    public static void initGUI() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
        }
    }

    public void setPlayerDiceStatus(boolean flag) {
        for (JButton b : diceList) {
            b.setEnabled(flag);
        }
    }

    public void setPlayerElementsStatus(boolean flag) {
        timesBox.setEnabled(flag);
        dieBox.setEnabled(flag);
        betButton.setEnabled(flag);
        dudoButton.setEnabled(flag);
        helpButton.setEnabled(flag);
    }

    public void setOpponentNextBetStatus(boolean flag) {
        nextBetButton.setEnabled(flag);
    }

    public void setOpponentBoxStatus(boolean flag) {
        oppTimesBox.setEnabled(flag);
        oppDieBox.setEnabled(flag);
        oppDudoButton.setEnabled(flag);
    }

    public void setLogStatus(boolean flag) {
        logTable.setEnabled(flag);
        filterBox.setEnabled(flag);
        filterLabel.setEnabled(flag);
    }

    public void setInfoStatus(boolean flag) {
        infoList.setEnabled(flag);
    }

    public void addInfo(ListEntry e) {
        infoModel.addElement(e);
    }

    public void clearInfo() {
        infoModel.removeAllElements();
    }

    public void setPossibleBets(boolean forPhysicalPlayer, HashMap<Die, Integer> minValues, int totalDiceNumber) {
        DefaultComboBoxModel<Integer> currTimesModel;
        DefaultComboBoxModel<Die> currDiceModel;
        if (forPhysicalPlayer) {
            currTimesModel = physicalPlayerTimesModel;
            currDiceModel = physicalPlayerDiceModel;
        } else {
            currTimesModel = virtualPlayerTimesModel;
            currDiceModel = virtualPlayerDiceModel;
        }

        minimumPossibleValues = minValues;
        currTimesModel.removeAllElements();

        Integer min = Collections.min(minValues.values());

        boolean hasAvailableBet = false;

        int max = totalDiceNumber + 1;
        max += 1 + Utility.random.nextInt(5); //Avoid suggesting the total dice number currently in the game
        for (int i = min; i < max; i++) {
            currTimesModel.addElement(i);
            hasAvailableBet = true;
        }

        currDiceModel.removeAllElements();

        if (!hasAvailableBet) {
            return;
        }

        Set<Die> possibleDiceList = minValues.keySet();
        TreeSet<Die> sortedDiceList = new TreeSet<>(possibleDiceList);
        for (Die d : sortedDiceList) {
            if (minValues.get(d) <= min) {
                currDiceModel.addElement(d);
            }
        }
    }

    private void updatePossibleDice(boolean forPhysicalPlayer) {
        DefaultComboBoxModel<Integer> currTimesModel;
        DefaultComboBoxModel<Die> currDiceModel;
        if (forPhysicalPlayer) {
            currTimesModel = physicalPlayerTimesModel;
            currDiceModel = physicalPlayerDiceModel;
        } else {
            currTimesModel = virtualPlayerTimesModel;
            currDiceModel = virtualPlayerDiceModel;
        }

        Integer i = (Integer) currTimesModel.getSelectedItem();
        if (i == null) {
            return;
        }

        currDiceModel.removeAllElements();

        Set<Die> possibleDiceList = minimumPossibleValues.keySet();
        TreeSet<Die> sortedDiceList = new TreeSet<>(possibleDiceList);
        for (Die d : sortedDiceList) {
            if (minimumPossibleValues.get(d) <= i) {
                currDiceModel.addElement(d);
            }
        }
    }

    public void setDice(ArrayList<Die> dice) {
        int i = 0;
        String label;
        for (Die d : dice) {
            label = d.toString();
            if (label.length() == 1) {
                ImageIcon icon = new ImageIcon(Constants.DIE_FOLDER.replace("NUM", label));
                if (icon.getIconHeight() == -1) {
                    diceList.get(i).setText(label);
                } else {
                    diceList.get(i).setIcon(icon);
                }
            } else {
                ImageIcon icon = new ImageIcon(Constants.DIE_FOLDER.replace("NUM", "Lama"));
                if (icon.getIconHeight() == -1) {
                    diceList.get(i).setText("L");
                } else {
                    diceList.get(i).setIcon(icon);
                }
            }
            i++;
        }
    }

    public void initLogNames(String[] playersNames) {
        filterBoxModel.removeAllElements();
        filterBoxModel.addElement(LanguageManager.getValue("All"));
        for (String playersName : playersNames) {
            filterBoxModel.addElement(playersName);
        }
    }

    public void setOpponentName(String name) {
        opponentNameLabel.setText(name);
    }

    public void addLogBet(String playerName, String bet) {
        betLog.addRow(new Object[]{playerName, bet});
    }

    private void addFilter(String filterValue) {
        if (filterBox.getItemAt(0) == null) {
            return;
        }

        RowFilter<DefaultTableModel, String> rf = null;
        try {
            if (!filterValue.equals(filterBox.getItemAt(0))) {
                rf = RowFilter.regexFilter(filterValue, 0);
            } else {
                rf = RowFilter.regexFilter("", 0);
            }
        } catch (java.util.regex.PatternSyntaxException e) {
        }
        sorter.setRowFilter(rf);
    }

    public void setAvatar(SerializableBufferedImage avatar) {
        if (avatar == null) {
            avatarLabel.setVisible(false);
            return;
        }

        avatarLabel.setIcon(new ImageIcon(avatar.get()));
        avatarLabel.setVisible(true);
    }

    public void cleanOldMatch(int num) {
        setAvatar(null);
        opponentNameLabel.setText("");
        physicalPlayerTimesModel.removeAllElements();
        physicalPlayerDiceModel.removeAllElements();
        virtualPlayerTimesModel.removeAllElements();
        virtualPlayerDiceModel.removeAllElements();
        for (JButton b : diceList) {
            b.setVisible(false);
        }
        if (num >= 1) {
            die1.setVisible(true);
        }
        if (num >= 2) {
            die2.setVisible(true);
        }
        if (num >= 3) {
            die3.setVisible(true);
        }
        if (num >= 4) {
            die4.setVisible(true);
        }
        if (num >= 5) {
            die5.setVisible(true);
        }

        resetBetHistory();
    }

    public void resetBetHistory() {
        int rows = betLog.getRowCount();
        for (int i = rows - 1; i >= 0; i--) {
            betLog.removeRow(i);
        }
    }

    public void setNextOpponentButtonLabel(String text) {
        nextBetButton.setText(text);
    }

    public void showTip(PlayerBet suggestedBet) {
        tipResultWindow.setBet(suggestedBet);
        tipResultWindow.setVisible(true);
    }

    void updateComboBoxWithSuggestedBet(PlayerBet suggestedBet) {
        Die face = suggestedBet.getFace();
        int times = suggestedBet.getTimes();
        timesBox.setSelectedItem(times);
        updatePossibleDice(true);
        for (int i = 0; i < physicalPlayerDiceModel.getSize(); i++) {
            if (physicalPlayerDiceModel.getElementAt(i).equals(face)) {
                dieBox.setSelectedIndex(i);
                break;
            }
        }
    }

    private void disableTextField(Component[] comp) {
        for (Component aComp : comp) {
            if (aComp instanceof JPanel) {
                disableTextField(((JPanel) aComp).getComponents());
            } else if (aComp instanceof JTextField) {
                ((JTextField) aComp).setEditable(false);
                return;
            }
        }
    }

}
