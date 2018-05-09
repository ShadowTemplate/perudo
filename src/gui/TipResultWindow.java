package gui;

import entity.PlayerBet;
import config.LanguageManager;

public class TipResultWindow extends javax.swing.JDialog {

    private PlayerBet suggestedBet;
    private GameWindow GUI;
    
    public TipResultWindow(GameWindow gui) {
        setModal(true);
        setResizable(false);
        initComponents();
        setLocationRelativeTo(null);
        this.GUI = gui;
        
        reasoningArea.setEditable(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        reasoningArea = new javax.swing.JTextArea();
        acceptButton = new javax.swing.JButton();
        refuseButton = new javax.swing.JButton();
        explainButton = new javax.swing.JButton();

        setTitle(LanguageManager.getValue("SUGGESTION"));

        reasoningArea.setColumns(20);
        reasoningArea.setRows(5);
        jScrollPane1.setViewportView(reasoningArea);

        acceptButton.setText(LanguageManager.getValue("Accept"));
        acceptButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptButtonActionPerformed(evt);
            }
        });

        refuseButton.setText(LanguageManager.getValue("Reject"));
        refuseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refuseButtonActionPerformed(evt);
            }
        });

        explainButton.setText(LanguageManager.getValue("Why?"));
        explainButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                explainButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 520, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(explainButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(acceptButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refuseButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(acceptButton)
                    .addComponent(refuseButton)
                    .addComponent(explainButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void acceptButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_acceptButtonActionPerformed
        setVisible(false);
        if(suggestedBet.getTimes() != 0) { //Non Dudo
            GUI.updateComboBoxWithSuggestedBet(suggestedBet);
        }
    }//GEN-LAST:event_acceptButtonActionPerformed

    private void refuseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refuseButtonActionPerformed
        setVisible(false);
    }//GEN-LAST:event_refuseButtonActionPerformed

    private void explainButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_explainButtonActionPerformed
        reasoningArea.setText(suggestedBet.getExplanation());
    }//GEN-LAST:event_explainButtonActionPerformed

    public void setBet(PlayerBet suggestedBet) {
        this.suggestedBet = suggestedBet;
        reasoningArea.setText(suggestedBet.getExplanation().substring(0, suggestedBet.getExplanation().indexOf("\n")));
    }
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton acceptButton;
    private javax.swing.JButton explainButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea reasoningArea;
    private javax.swing.JButton refuseButton;
    // End of variables declaration//GEN-END:variables
}
