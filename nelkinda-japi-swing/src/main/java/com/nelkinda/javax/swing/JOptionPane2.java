/*
 * Copyright © 2016 - 2016 Nelkinda Software Craft Pvt Ltd.
 *
 * This file is part of com.nelkinda.japi.
 *
 * com.nelkinda.japi is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * com.nelkinda.japi is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with com.nelkinda.japi.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package com.nelkinda.javax.swing;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.showConfirmDialog;

public enum JOptionPane2 {
    ; // NOSONAR Bug in SonarQube: https://jira.sonarsource.com/browse/SONARJAVA-1909

    public static char[] showPasswordDialog(final JFrame parent) {
        final JPanel jPanel = new JPanel();
        final JPasswordField jPasswordField = new JPasswordField(20);
        jPasswordField.addAncestorListener(new RequestFocusListener());
        final JLabel jLabel = new JLabel("Passphrase: ");
        jPanel.add(jLabel);
        jPanel.add(jPasswordField);
        jLabel.setLabelFor(jPasswordField);
        if (OK_OPTION == showConfirmDialog(parent, jPanel, "Decrypt file", OK_CANCEL_OPTION, QUESTION_MESSAGE)) {
            return jPasswordField.getPassword();
        } else {
            return null;
        }
    }
}
