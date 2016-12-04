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
import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.Function;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.TableModel;

import static java.awt.event.KeyEvent.getExtendedKeyCodeForChar;
import static java.util.Collections.unmodifiableMap;
import static java.util.logging.Logger.getLogger;
import static javax.swing.Action.ACCELERATOR_KEY;
import static javax.swing.Action.ACTION_COMMAND_KEY;
import static javax.swing.Action.DISPLAYED_MNEMONIC_INDEX_KEY;
import static javax.swing.Action.LARGE_ICON_KEY;
import static javax.swing.Action.LONG_DESCRIPTION;
import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.NAME;
import static javax.swing.Action.SHORT_DESCRIPTION;
import static javax.swing.Action.SMALL_ICON;
import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.showConfirmDialog;
import static javax.swing.SwingUtilities.invokeAndWait;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.UIManager.getInstalledLookAndFeels;
import static javax.swing.UIManager.setLookAndFeel;

public enum SwingUtilities {
    ; // NOSONAR Bug in SonarQube: https://jira.sonarsource.com/browse/SONARJAVA-1909

    private static final Logger LOG = getLogger("com.nelkinda.javax.swing");
    private static final IconCache iconCache = new IconCache();
    private static final Map<String, Function<String, Object>> ACTION_CONVERTERS = Loader.createActionConverters();

    public static <T extends Component> T findComponent(final Container container, final Class<T> componentClass) {
        for (final Component c : container.getComponents()) {
            if (componentClass.isInstance(c))
                return componentClass.cast(c);
            if (c instanceof Container) {
                final T component = findComponent((Container) c, componentClass);
                if (component != null)
                    return component;
            }
        }
        return null;
    }

    public static <T> T callAndWait(final Callable<T> callable) throws InvocationTargetException, InterruptedException, ExecutionException {
        final FutureTask<T> futureTask = new FutureTask<>(callable);
        invokeAndWait(futureTask);
        return futureTask.get();
    }

    public static <T> FutureTask<T> callLater(final Callable<T> callable) {
        final FutureTask<T> futureTask = new FutureTask<>(callable);
        invokeLater(futureTask);
        return futureTask;
    }

    public static void setLookAndFeelFromName(final String lookAndFeelName) {
        try {
            for (final LookAndFeelInfo info : getInstalledLookAndFeels()) {
                if (info.getName().equals(lookAndFeelName)) {
                    setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (final ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            LOG.config(() -> "Could not set LookAndFeel to " + lookAndFeelName + ", reason: " + e);
        }
    }

    public static void initActionFromBundle(final Action action, final String actionCommand, final ResourceBundle resourceBundle) {
        action.putValue(ACTION_COMMAND_KEY, actionCommand);
        initActionFromBundle(action, resourceBundle);
    }

    public static void initActionFromBundle(final Action action, final ResourceBundle resourceBundle) {
        final String actionCommand = (String) action.getValue(ACTION_COMMAND_KEY);
        for (final Map.Entry<String, Function<String, Object>> entry : ACTION_CONVERTERS.entrySet()) {
            final String actionKey = entry.getKey();
            final Function<String, Object> function = entry.getValue();
            final String key = actionCommand + "." + actionKey;
            if (resourceBundle.containsKey(key)) {
                final String stringValue = resourceBundle.getString(key);
                final Object object = function.apply(stringValue);
                action.putValue(actionKey, object);
            }
        }
    }

    public static ImageIcon getImageIcon(final String urlString) {
        return iconCache.getImageIcon(urlString);
    }

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

    public static String[] getColumnNames(final JTable jTable) {
        final TableModel tableModel = jTable.getModel();
        final int columnCount = tableModel.getColumnCount();
        final String[] columnTitles = new String[columnCount];
        for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
            columnTitles[columnIndex] = tableModel.getColumnName(columnIndex);
        }
        return columnTitles;
    }

    public static List<String[]> getTableValues(final JTable jTable) {
        final TableModel tableModel = jTable.getModel();
        final int columnCount = tableModel.getColumnCount();
        final List<String[]> tableValues = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < tableModel.getRowCount(); rowIndex++) {
            final String[] rowValues = new String[columnCount];
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                final String value = (String) tableModel.getValueAt(rowIndex, columnIndex);
                rowValues[columnIndex] = value.length() > 0 ? value : null;
            }
            tableValues.add(rowValues);
        }
        return tableValues;
    }
    private enum Loader {
        ; // NOSONAR Bug in SonarQube: https://jira.sonarsource.com/browse/SONARJAVA-1909

        private static Map<String, Function<String, Object>> createActionConverters() {
            final Map<String, Function<String, Object>> actionConverters = new HashMap<>();
            actionConverters.put(ACCELERATOR_KEY, KeyStroke::getKeyStroke);
            actionConverters.put(DISPLAYED_MNEMONIC_INDEX_KEY, Integer::parseInt);
            actionConverters.put(NAME, s -> s);
            actionConverters.put(MNEMONIC_KEY, s -> getExtendedKeyCodeForChar(s.codePointAt(0)));
            actionConverters.put(SHORT_DESCRIPTION, s -> s);
            actionConverters.put(LONG_DESCRIPTION, s -> s);
            actionConverters.put(SMALL_ICON, SwingUtilities::getImageIcon);
            actionConverters.put(LARGE_ICON_KEY, SwingUtilities::getImageIcon);
            return unmodifiableMap(actionConverters);
        }
    }
}
