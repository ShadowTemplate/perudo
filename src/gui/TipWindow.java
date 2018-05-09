package gui;

import miscellanea.Handler;
import config.LanguageManager;

public class TipWindow extends javax.swing.JDialog {
    private static double bluffValue = 0.5;
    private static double riskValue = 0.5;
    
    private static final String[] values = {LanguageManager.getValue("None"), LanguageManager.getValue("A little bit"), LanguageManager.getValue("Sufficiently"), LanguageManager.getValue("A lot"), LanguageManager.getValue("Absolutely")};

    public TipWindow() {
        setModal(true);
        setResizable(false);
        initComponents();
        setLocationRelativeTo(null);
        
        bluffSlider.setValue(0);
        riskSlider.setValue(0);
        
        bluffDescription.setText(values[(int)(bluffValue * 4)]);
        riskDescription.setText(values[(int)(riskValue * 4)]);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        okButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        bluffSlider = new javax.swing.JSlider();
        riskSlider = new javax.swing.JSlider();
        bluffLabel = new javax.swing.JLabel();
        riskLabel = new javax.swing.JLabel();
        bluffDescription = new javax.swing.JLabel();
        riskDescription = new javax.swing.JLabel();
        cancelButton = new javax.swing.JButton();

        setTitle(LanguageManager.getValue("ASK FOR HELP"));

        okButton.setText("OK");
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), LanguageManager.getValue("Settings for suggestion")));

        bluffSlider.setToolTipText("");
        bluffSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                bluffSliderStateChanged(evt);
            }
        });

        riskSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                riskSliderStateChanged(evt);
            }
        });

        bluffLabel.setText(LanguageManager.getValue("Bluff will:"));

        riskLabel.setText(LanguageManager.getValue("Risk will:"));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bluffLabel)
                    .addComponent(riskLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(riskSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(bluffSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bluffDescription)
                    .addComponent(riskDescription))
                .addContainerGap(141, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bluffSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bluffLabel)
                    .addComponent(bluffDescription))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(riskSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(riskLabel)
                    .addComponent(riskDescription))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cancelButton.setText(LanguageManager.getValue("Cancel"));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(okButton)
                    .addComponent(cancelButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bluffSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_bluffSliderStateChanged
        bluffValue = bluffSlider.getValue() / (double) 100;
        bluffDescription.setText(values[(int)(bluffValue * 4)]);
    }//GEN-LAST:event_bluffSliderStateChanged

    private void riskSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_riskSliderStateChanged
        riskValue = riskSlider.getValue() / (double) 100;
        riskDescription.setText(values[(int)(riskValue * 4)]);
    }//GEN-LAST:event_riskSliderStateChanged

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        setVisible(false);
        Handler.calculateBetForPlayer();
    }//GEN-LAST:event_okButtonActionPerformed

    public static double getRiskValue() {
        return riskValue;
    }
    
    public static double getBluffValue() {
        return bluffValue;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bluffDescription;
    private javax.swing.JLabel bluffLabel;
    private javax.swing.JSlider bluffSlider;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton okButton;
    private javax.swing.JLabel riskDescription;
    private javax.swing.JLabel riskLabel;
    private javax.swing.JSlider riskSlider;
    // End of variables declaration//GEN-END:variables
}
