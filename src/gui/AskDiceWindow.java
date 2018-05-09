package gui;

import config.LanguageManager;
import entity.Die;
import entity.Player;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;

public class AskDiceWindow extends javax.swing.JDialog {

    private final Player player;
    private final DefaultComboBoxModel<Die> dieModel1 = new DefaultComboBoxModel<>(new Die[]{Die.LLAMA, Die.TWO, Die.THREE, Die.FOUR, Die.FIVE, Die.SIX});
    private final DefaultComboBoxModel<Die> dieModel2 = new DefaultComboBoxModel<>(new Die[]{Die.LLAMA, Die.TWO, Die.THREE, Die.FOUR, Die.FIVE, Die.SIX});
    private final DefaultComboBoxModel<Die> dieModel3 = new DefaultComboBoxModel<>(new Die[]{Die.LLAMA, Die.TWO, Die.THREE, Die.FOUR, Die.FIVE, Die.SIX});
    private final DefaultComboBoxModel<Die> dieModel4 = new DefaultComboBoxModel<>(new Die[]{Die.LLAMA, Die.TWO, Die.THREE, Die.FOUR, Die.FIVE, Die.SIX});
    private final DefaultComboBoxModel<Die> dieModel5 = new DefaultComboBoxModel<>(new Die[]{Die.LLAMA, Die.TWO, Die.THREE, Die.FOUR, Die.FIVE, Die.SIX});

    public AskDiceWindow(Player p) {
        player = p;
        setModal(true);
        setResizable(false);
        initComponents();
        setLocationRelativeTo(null);
        setTitle(LanguageManager.getValue("INSERT DICE"));
    }
    
    public void setBoxNumber() {
        die1.setEnabled(false);
        die2.setEnabled(false);
        die3.setEnabled(false);
        die4.setEnabled(false);
        die5.setEnabled(false);
        
        int num = player.getDiceNumber();
        die1.setEnabled(true);
        if(num >= 2) {
            die2.setEnabled(true);
        }
        if(num >= 3) {
            die3.setEnabled(true);
        }
        if(num >= 4) {
            die4.setEnabled(true);
        }
        if(num >= 5) {
            die5.setEnabled(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        die1 = new javax.swing.JComboBox<Die>();
        die2 = new javax.swing.JComboBox<Die>();
        die3 = new javax.swing.JComboBox<Die>();
        die4 = new javax.swing.JComboBox<Die>();
        die5 = new javax.swing.JComboBox<Die>();
        OKButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), LanguageManager.getValue("XXX's dice").replace("XXX", player.getName())));

        die1.setModel(dieModel1);
        die1.setMinimumSize(new java.awt.Dimension(35, 20));
        die1.setPreferredSize(new java.awt.Dimension(35, 20));

        die2.setModel(dieModel2);
        die2.setMinimumSize(new java.awt.Dimension(35, 20));
        die2.setPreferredSize(new java.awt.Dimension(35, 20));

        die3.setModel(dieModel3);
        die3.setMinimumSize(new java.awt.Dimension(35, 20));
        die3.setPreferredSize(new java.awt.Dimension(35, 20));

        die4.setModel(dieModel4);
        die4.setMinimumSize(new java.awt.Dimension(35, 20));
        die4.setPreferredSize(new java.awt.Dimension(35, 20));

        die5.setModel(dieModel5);
        die5.setMinimumSize(new java.awt.Dimension(35, 20));
        die5.setPreferredSize(new java.awt.Dimension(35, 20));

        OKButton.setText("OK");
        OKButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        OKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(die1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(die2, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(die3, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(55, 55, 55)
                        .addComponent(die4, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(die5, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(162, 162, 162)
                        .addComponent(OKButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(die1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(die2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(die3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(die4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(die5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(OKButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void OKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKButtonActionPerformed
        ArrayList<Die> values = new ArrayList<>();
        int num = player.getDiceNumber();
        if (num >= 1) {
            values.add(Die.getFromNumber(die1.getSelectedIndex() + 1));
        }
        if (num >= 2) {
            values.add(Die.getFromNumber(die2.getSelectedIndex() + 1));
        }
        if (num >= 3) {
            values.add(Die.getFromNumber(die3.getSelectedIndex() + 1));
        }
        if (num >= 4) {
            values.add(Die.getFromNumber(die4.getSelectedIndex() + 1));
        }
        if (num >= 5) {
            values.add(Die.getFromNumber(die5.getSelectedIndex() + 1));
        }

        player.setDice(values);

        setVisible(false);
    }//GEN-LAST:event_OKButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton OKButton;
    private javax.swing.JComboBox<Die> die1;
    private javax.swing.JComboBox<Die> die2;
    private javax.swing.JComboBox<Die> die3;
    private javax.swing.JComboBox<Die> die4;
    private javax.swing.JComboBox<Die> die5;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
