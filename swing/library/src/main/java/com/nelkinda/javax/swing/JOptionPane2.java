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

import java.awt.Component;
import java.awt.HeadlessException;
import java.util.Locale;
import java.util.Optional;
import javax.swing.JPasswordField;
import javax.swing.UIManager;
import org.jetbrains.annotations.Nullable;

import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.showConfirmDialog;

/**
 * Additional dialogs.
 *
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
 * @version 0.0.2
 * @see javax.swing.JOptionPane
 * @since 0.0.2
 */
public enum JOptionPane2 {
    ; // NOSONAR Bug in SonarQube: https://jira.sonarsource.com/browse/SONARJAVA-1909

    /**
     * Shows a question-message dialog requesting a passphrase or password input from the user.
     * The dialog uses the default frame, which usually means it is centered on the screen.
     *
     * @param message the {@code Object} to display
     * @return An {@link Optional} holding the password if the dialog was confirmed, otherwise {@link Optional#empty()}.
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless} returns {@code true}
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static Optional<char[]> showPasswordDialog(final Object message) {
        return showPasswordDialog(null, message);
    }

    /**
     * Shows a question-message dialog requesting a passphrase or password input from the user parented to {@code parentComponent}.
     * The dialog is displayed on top of the {@code Component}'s frame, and is usually positioned below the {@code Component}.
     *
     * @param parentComponent the parent {@code Component} for the dialog
     * @param message         the {@code Object} to display
     * @return An {@link Optional} holding the password if the dialog was confirmed, otherwise {@link Optional#empty()}.
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless} returns {@code true}
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static Optional<char[]> showPasswordDialog(@Nullable final Component parentComponent, final Object message) {
        return showPasswordDialog(parentComponent, message, UIManager.getString("OptionPane.inputDialogTitle", parentComponent == null ? Locale.getDefault() : parentComponent.getLocale()), QUESTION_MESSAGE);
    }

    /**
     * Shows a dialog requesting input from the user parented to {@code parentComponent} with the dialog having the title {@code title} and message type {@code messageType}.
     *
     * @param parentComponent the parent {@code Component} for the dialog
     * @param message         the {@code Object} to display
     * @param title           the {@code String} to display in the dialog
     *                        title bar
     * @param messageType     the type of message that is to be displayed:
     *                        {@code ERROR_MESSAGE},
     *                        {@code INFORMATION_MESSAGE},
     *                        {@code WARNING_MESSAGE},
     *                        {@code QUESTION_MESSAGE},
     *                        or {@code PLAIN_MESSAGE}
     * @return An {@link Optional} holding the password if the dialog was confirmed, otherwise {@link Optional#empty()}.
     * @throws HeadlessException if {@code GraphicsEnvironment.isHeadless} returns {@code true}
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static Optional<char[]> showPasswordDialog(@Nullable final Component parentComponent, final Object message, final String title, final int messageType) {
        final JPasswordField jPasswordField = new JPasswordField();
        jPasswordField.addAncestorListener(FocusRequestingAncestorListener.INSTANCE);
        return OK_OPTION == showConfirmDialog(parentComponent, new Object[]{message, jPasswordField}, title, OK_CANCEL_OPTION, messageType)
                ? Optional.of(jPasswordField.getPassword())
                : Optional.empty();
    }
}
