package gui;

import config.CPULevel;
import config.Language;
import config.LanguageManager;
import miscellanea.NameGenerator;
import config.Settings;
import entity.PlayerTO;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import miscellanea.AvatarGenerator;
import miscellanea.Constants;
import miscellanea.Handler;
import miscellanea.Utility;

public class SettingsWindow extends javax.swing.JDialog {

    private DefaultTableModel players;
    private String nomePlayer = Settings.playersTO.get(0).getName();

    private int lastPlayerLoaded = 0;

    private Boolean sexChange = false;
    private String newSex;
    private JFileChooser fileChooser = new JFileChooser(Constants.PERUDO_HOME_PATH);

    private boolean typeChange;
    private String newType;

    private CPULevel CPULevel;
    private Language language;
    private int playersNum;
    private ArrayList<PlayerTO> playersTO;

    public SettingsWindow() {
        setModal(true);
        setResizable(false);
        initComponents();
        setLocationRelativeTo(null);

        CPULevel = Settings.CPULevel;
        language = Settings.language;
        playersNum = Settings.playersNum;

        disableTextField(fileChooser.getComponents());
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(Constants.PLAYER_FILE_EXTENSION);
            }

            @Override
            public String getDescription() {
                return LanguageManager.getValue("Player profile") + " (" + Constants.PLAYER_FILE_EXTENSION + ")";
            }
        });

        players = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return row != 0 || (column != 1 && column != 2);
            }

        };
        players.addColumn(LanguageManager.getValue("Player name"));
        players.addColumn(LanguageManager.getValue("Type"));
        players.addColumn(LanguageManager.getValue("Gender"));
        playerTable.setModel(players);
        players = (DefaultTableModel) playerTable.getModel();
        for (int i = 1; i < Constants.MAXIMUM_PLAYERS_NUMBER; i++) {
            oppNumberBox.addItem(i);
        }
        oppNumberBox.setSelectedIndex(Constants.DEFAULT_PLAYERS_NUMBER - 2);
        List<String> langList = Language.getLanguagesList();
        for (String langList1 : langList) {
            languageBox.addItem(langList1);
        }
        languageBox.setSelectedItem(Settings.language.toString());
        List<String> CPUList = CPULevel.getCPUList();
        for (String CPUList1 : CPUList) {
            CPUBox.addItem(CPUList1);
        }
        CPUBox.setSelectedItem(Settings.CPULevel.toString());
        TableColumn typeColumn = playerTable.getColumnModel().getColumn(1);
        final JComboBox typeComboBox = new JComboBox();
        typeComboBox.addItem(LanguageManager.getValue("Human"));
        typeComboBox.addItem("CPU");
        typeComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                typeChange = true;
                newType = (String) typeComboBox.getSelectedItem();

            }
        });

        typeColumn.setCellEditor(new DefaultCellEditor(typeComboBox));
        final JComboBox sexComboBox = new JComboBox();
        TableColumn sexColumn = playerTable.getColumnModel().getColumn(2);
        sexComboBox.removeAllItems();
        sexComboBox.addItem(LanguageManager.getValue("Male"));
        sexComboBox.addItem(LanguageManager.getValue("Female"));

        sexComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                sexChange = true;
                newSex = (String) sexComboBox.getSelectedItem();
            }
        });

        sexColumn.setCellEditor(new DefaultCellEditor(sexComboBox));
        playerNameTextField.setText(nomePlayer);

        playersTO = new ArrayList<>();
        players.insertRow(players.getRowCount(), new Object[]{nomePlayer, LanguageManager.getValue("Human"), ""});
        PlayerTO player = Settings.playersTO.get(0);
        playersTO.add(new PlayerTO(nomePlayer, true, null, true, false, player.getBluffCoeff(), player.getBetMadeNumber(), player.getExactBetMadeNumber(), player.getBluffBetPercentage()));

        for (int i = 1; i < Constants.DEFAULT_PLAYERS_NUMBER; i++) {
            boolean randomMan = Utility.random.nextInt(2) > 0;
            String gender = randomMan ? LanguageManager.getValue("Male") : LanguageManager.getValue("Female");
            Object[] newChal = getNewPlayer(gender);
            players.insertRow(players.getRowCount(), newChal);

            playersTO.add(new PlayerTO((String) newChal[0], randomMan, AvatarGenerator.getRandomAvatar(randomMan), false, true, Constants.DEFAULT_BLUFF_COEFF, 0, 0, 0));
        }

        players.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                TableModel model = (TableModel) e.getSource();
                Object data = null;
                if (row >= 0 && column > 0) {
                    if (sexChange) {
                        data = model.getValueAt(row, column);
                        PlayerTO p = playersTO.get(row);
                        Object[] newChal = getNewPlayer(newSex);
                        p.setName((String) newChal[0]);
                        p.setMan(newSex.equals(LanguageManager.getValue("Male")));
                        if (newSex.equals(LanguageManager.getValue("Male"))) {
                            p.setAvatar(AvatarGenerator.getRandomAvatar(true));
                        } else {
                            p.setAvatar(AvatarGenerator.getRandomAvatar(false));
                        }
                        sexChange = false;
                        model.setValueAt(p.getName(), row, 0);
                        playersTO.remove(row);
                        playersTO.add(row, p);
                    }
                    if (typeChange) {
                        data = model.getValueAt(row, column);
                        PlayerTO p = playersTO.get(row);
                        p.setCPUControlled((newType.equals("CPU")));
                        playersTO.remove(row);
                        playersTO.add(row, p);
                        typeChange = false;
                        model.setValueAt(newType, row, 1);
                    }
                }
                if (column == 0) {
                    data = model.getValueAt(row, column);
                    if (row == 0) {
                        playerNameTextField.setText((String) data);
                    }
                    PlayerTO p = playersTO.get(row);
                    p.setName((String) data);
                    playersTO.remove(row);
                    playersTO.add(row, p);
                }
            }
        });

        saveSettings();

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        playerNameLabel = new javax.swing.JLabel();
        playerNameTextField = new javax.swing.JTextField();
        oppLabel = new javax.swing.JLabel();
        oppNumberBox = new javax.swing.JComboBox<Integer>();
        CPULevelLabel = new javax.swing.JLabel();
        CPUBox = new javax.swing.JComboBox<String>();
        languageLabel = new javax.swing.JLabel();
        languageBox = new javax.swing.JComboBox<String>();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        playerTable = new javax.swing.JTable();
        loadPlayerButton = new javax.swing.JButton();
        saveAndStart = new javax.swing.JButton();

        setTitle(LanguageManager.getValue("SETTINGS"));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), LanguageManager.getValue("Game options")));

        playerNameLabel.setText(LanguageManager.getValue("Your name:"));

        playerNameTextField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                playerNameTextFieldFocusLost(evt);
            }
        });

        oppLabel.setText(LanguageManager.getValue("Opponents number:"));

        oppNumberBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                oppNumberBoxItemStateChanged(evt);
            }
        });

        CPULevelLabel.setText(config.LanguageManager.getValue("Difficulty:")
        );

        CPUBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CPUBoxItemStateChanged(evt);
            }
        });

        languageLabel.setText(LanguageManager.getValue("Language:"));

        languageBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                languageBoxItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(playerNameLabel)
                    .addComponent(oppLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(playerNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(oppNumberBox, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(CPULevelLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CPUBox, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(languageLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(languageBox, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playerNameLabel)
                    .addComponent(playerNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(oppNumberBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(oppLabel)
                    .addComponent(CPULevelLabel)
                    .addComponent(CPUBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(languageLabel)
                    .addComponent(languageBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), LanguageManager.getValue("Partecipants")));

        playerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome Giocatore", "Tipo", "Sesso"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(playerTable);

        loadPlayerButton.setText(LanguageManager.getValue("Load opponent"));
        loadPlayerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadPlayerButtonActionPerformed(evt);
            }
        });

        saveAndStart.setText(LanguageManager.getValue("Save and start match"));
        saveAndStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveAndStartActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(loadPlayerButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(saveAndStart, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(loadPlayerButton)
                    .addComponent(saveAndStart))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void oppNumberBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_oppNumberBoxItemStateChanged
        int numAvv = (int) oppNumberBox.getSelectedItem() + 1;
        if (this.isActive()) {
            if (numAvv > playersNum) {
                for (int i = playersNum; i < numAvv; i++) {
                    Object[] newChal = getNewPlayer(LanguageManager.getValue("Male"));
                    playersTO.add(new PlayerTO((String) newChal[0], true, AvatarGenerator.getRandomAvatar(true), false, true, Constants.DEFAULT_BLUFF_COEFF, 0, 0, 0));
                    players.insertRow(i, newChal);
                }
            } else if (numAvv < playersNum) {
                for (int i = playersNum; i > numAvv; i--) {
                    players.removeRow((i - 1));
                    playersTO.remove(i - 1);
                    if (i < lastPlayerLoaded) {
                        lastPlayerLoaded = lastPlayerLoaded - 1;
                    }
                }
            }
            playersNum = numAvv;
        }
    }//GEN-LAST:event_oppNumberBoxItemStateChanged

    private void CPUBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CPUBoxItemStateChanged
        if (this.isActive()) {
            CPULevel = CPULevel.getLanguageFromString((String) CPUBox.getSelectedItem());
        }
    }//GEN-LAST:event_CPUBoxItemStateChanged

    private void languageBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_languageBoxItemStateChanged
        if (this.isActive()) {
            language = Language.getLanguageFromString((String) languageBox.getSelectedItem());
            if(Utility.updateSettingsFile(Settings.playersTO.get(0), language)) {
                JOptionPane.showMessageDialog(null, LanguageManager.getValue("Please restart the game to apply changes."), LanguageManager.getValue("ATTENTION"), JOptionPane.INFORMATION_MESSAGE);
            } else { 
                JOptionPane.showMessageDialog(null, LanguageManager.getValue("Error in changing language."), LanguageManager.getValue("ERROR"), JOptionPane.ERROR_MESSAGE);
            }
                
        }
    }//GEN-LAST:event_languageBoxItemStateChanged

    private void loadPlayerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadPlayerButtonActionPerformed
        int result = fileChooser.showOpenDialog(SettingsWindow.this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String playerPath = fileChooser.getSelectedFile().toString();

            lastPlayerLoaded = ((lastPlayerLoaded + 1) % playersTO.size());
            if (lastPlayerLoaded == 0) {
                lastPlayerLoaded = 1;
            }
            if (lastPlayerLoaded < playersNum) {
                playersTO.remove(lastPlayerLoaded);
                PlayerTO loadP = Utility.loadPlayerFromFile(playerPath);
                playersTO.add(lastPlayerLoaded, loadP);
                players.setValueAt(loadP.getName(), lastPlayerLoaded, 0);
                players.setValueAt(loadP.isPhysical() ? LanguageManager.getValue("Human") : "CPU", lastPlayerLoaded, 1);
                players.setValueAt(loadP.isMan() ? LanguageManager.getValue("Male") : LanguageManager.getValue("Female"), lastPlayerLoaded, 2);
            }
        }
    }//GEN-LAST:event_loadPlayerButtonActionPerformed

    private void playerNameTextFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_playerNameTextFieldFocusLost
        PlayerTO p = playersTO.get(0);
        p.setName(playerNameTextField.getText());
        playersTO.remove(0);
        playersTO.add(0, p);
        players.setValueAt(playerNameTextField.getText(), 0, 0);
        Utility.updateSettingsFile(playersTO.get(0), language);
    }//GEN-LAST:event_playerNameTextFieldFocusLost

    private void saveAndStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAndStartActionPerformed
        saveSettings();
        setVisible(false);
        Handler.resetMatch();
        Handler.initMatch();
    }//GEN-LAST:event_saveAndStartActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> CPUBox;
    private javax.swing.JLabel CPULevelLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> languageBox;
    private javax.swing.JLabel languageLabel;
    private javax.swing.JButton loadPlayerButton;
    private javax.swing.JLabel oppLabel;
    private javax.swing.JComboBox<Integer> oppNumberBox;
    private javax.swing.JLabel playerNameLabel;
    private javax.swing.JTextField playerNameTextField;
    private javax.swing.JTable playerTable;
    private javax.swing.JButton saveAndStart;
    // End of variables declaration//GEN-END:variables

    private Object[] getNewPlayer(String type) {
        boolean isMan = type.equals(LanguageManager.getValue("Male"));
        return (new Object[]{NameGenerator.getRandomName(isMan), "CPU", type});
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

    private void saveSettings() {
        Settings.CPULevel = CPULevel;
        Settings.language = language;
        Settings.playersNum = playersNum;
        Settings.playersTO.clear();
        for (PlayerTO p : playersTO) {
            Settings.playersTO.add(p);
        }
    }

}
