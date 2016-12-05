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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.function.Function;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.TableModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.awt.event.KeyEvent.getExtendedKeyCodeForChar;
import static java.util.Collections.unmodifiableMap;
import static java.util.Objects.requireNonNull;
import static java.util.function.Function.identity;
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
import static javax.swing.SwingUtilities.invokeAndWait;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.UIManager.getInstalledLookAndFeels;
import static javax.swing.UIManager.setLookAndFeel;

/**
 * A collection of utility methods for Swing.
 *
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
 * @version 0.0.2
 * @see javax.swing.SwingUtilities
 * @since 0.0.2
 */
public enum SwingUtilitiesN {
    ; // NOSONAR Bug in SonarQube: https://jira.sonarsource.com/browse/SONARJAVA-1909

    /**
     * The Logger to use for Logging.
     */
    private static final Logger LOG = getLogger("com.nelkinda.javax.swing");

    /**
     * The cache for caching icons.
     */
    private static final IconCache iconCache = new IconCache();

    /**
     * Functions that convert String arguments to the corresponding objects expected as values of an {@link Action}.
     * This is used by {@link #initActionFromBundle} methods to initialize the values of an Action from a ResourceBundle.
     */
    private static final Map<String, Function<String, ?>> ACTION_CONVERTERS = Loader.createActionConverters();

    /**
     * Finds a component in a list of containers.
     *
     * @param componentClass Class of the component to find.
     * @param containers     Containers in which to find a component of the specified class.
     * @param <T>            Type of the component to find.
     * @return Found component or {@code null}.
     * @throws NullPointerException     in case {@code componentClass} or {@code containers} is {@code null}.
     * @throws IllegalArgumentException in case {@code componentClass} or {@code containers} is {@code null} (IntelliJ IDEA).
     */
    public static <T extends Component> Optional<T> findComponent(@NotNull final Class<T> componentClass, @NotNull final Container... containers) {
        requireNonNull(componentClass);
        for (final Container container : requireNonNull(containers)) {
            for (final Component c : container.getComponents()) {
                if (componentClass.isInstance(c))
                    return Optional.of(componentClass.cast(c));
                if (c instanceof Container) {
                    final Optional<T> component = findComponent(componentClass, (Container) c);
                    if (component.isPresent())
                        return component;
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Causes {@code callable.call()} to be executed synchronously on the AWT event dispatching thread.
     * This blocks until all pending AWT events have been processed and (then) {@code callable.call()} returns.
     * This method should be used when an application thread needs to update the GUI.
     * It shouldn't be called from the event dispatching thread.
     * Here's an Example that shows a dialog from an application thread.
     * <pre>
     * new Thread(() -&gt; {
     *     try {
     *         System.err.println(SwingUtilitiesN.callAndWait(() -&gt; JOptionPane.showInputDialog("Enter your name")));
     *     } catch (final InvocationTargetException | InterruptedException | ExecutionException e) {
     *         e.printStackTrace();
     *     }
     * }).start();
     * </pre>
     *
     * @param callable Callable to execute on the event thread.
     * @param <T>      The type of the return value of {@code callable}
     * @return The return value of {@code callable}
     * @throws ExecutionException        if an exception is thrown while running {@code callable}
     * @throws InterruptedException      if we're interrupted while waiting for the event dispatching thread to finish executing {@code callable.call()}
     * @throws InvocationTargetException if an exception is thrown within FutureTask before or after {@code callable.call()}
     * @throws NullPointerException      in case {@code callable} is {@code null}.
     * @throws IllegalArgumentException  in case {@code callable} is {@code null} (IntelliJ IDEA).
     */
    public static <T> T callAndWait(@NotNull final Callable<T> callable) throws InvocationTargetException, InterruptedException, ExecutionException {
        final FutureTask<T> futureTask = new FutureTask<>(requireNonNull(callable));
        invokeAndWait(futureTask);
        return futureTask.get();
    }

    /**
     * Causes {@code callable.call()} to be executed asynchronously on the AWT event dispatching thread.
     * This will happen after all pending AWT events have been processed.
     * This method should be used when an application thread needs to update the GUI.
     *
     * @param callable Callable to execute on the event thread.
     * @param <T>      The type of the return value of {@code callable}
     * @return The future representing the task for running {@code callable}.
     * @throws NullPointerException     in case {@code callable} is {@code null}.
     * @throws IllegalArgumentException in case {@code callable} is {@code null} (IntelliJ IDEA).
     */
    public static <T> FutureTask<T> callLater(@NotNull final Callable<T> callable) {
        final FutureTask<T> futureTask = new FutureTask<>(requireNonNull(callable));
        invokeLater(futureTask);
        return futureTask;
    }

    /**
     * Sets the current look and feel to the look and feel specified by {@code lookAndFeelName}.
     *
     * @param lookAndFeelName Name of the look and feel to set, for example {@code "Metal"} or {@code "Nimbus"}.
     *                        If this is {@code null}, the look and feel remains unchanged.
     */
    public static void setLookAndFeelFromName(@Nullable final String lookAndFeelName) {
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

    /**
     * Initializes an action from a ResourceBundle.
     *
     * @param action         Action to initialize.
     * @param actionCommand  actionCommand, at the same time the base for the keys in the {@code resourceBundle}.
     * @param resourceBundle ResourceBundle from which to initialize the action.
     * @throws NullPointerException     in case {@code action} or {@code actionCommand} or {@code resourceBundle} is {@code null}.
     * @throws IllegalArgumentException in case {@code action} or {@code actionCommand} or {@code resourceBundle} is {@code null} (IntelliJ IDEA).
     */
    public static void initActionFromBundle(@NotNull final Action action, @NotNull final String actionCommand, @NotNull final ResourceBundle resourceBundle) {
        action.putValue(ACTION_COMMAND_KEY, actionCommand);
        initActionFromBundle(action, resourceBundle);
    }

    /**
     * Initializes an action from a ResourceBundle.
     * The action must have {@link Action#ACTION_COMMAND_KEY} set or this method will throw a {@link NullPointerException}.
     *
     * @param action         Action to initialize (with set {@link Action#ACTION_COMMAND_KEY}).
     * @param resourceBundle ResourceBundle from which to initialize the action.
     * @throws NullPointerException     in case {@code action} or {@code actionCommand.getValue(ACTION_COMMAND_KEY)} or {@code resourceBundle} is {@code null}.
     * @throws IllegalArgumentException in case {@code action} or {@code actionCommand.getValue(ACTION_COMMAND_KEY)} or {@code resourceBundle} is {@code null} (IntelliJ IDEA).
     */
    public static void initActionFromBundle(@NotNull final Action action, @NotNull final ResourceBundle resourceBundle) {
        @NotNull final String actionCommand = requireNonNull((String) action.getValue(ACTION_COMMAND_KEY));
        for (final Map.Entry<String, Function<String, ?>> entry : ACTION_CONVERTERS.entrySet()) {
            final String actionKey = entry.getKey();
            final Function<String, ?> function = entry.getValue();
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

        private static Map<String, Function<String, ?>> createActionConverters() {
            final Function<String, String> identity = identity();
            final Map<String, Function<String, ?>> actionConverters = new HashMap<>();
            actionConverters.put(ACCELERATOR_KEY, KeyStroke::getKeyStroke);
            actionConverters.put(DISPLAYED_MNEMONIC_INDEX_KEY, Integer::parseInt);
            actionConverters.put(NAME, identity);
            actionConverters.put(MNEMONIC_KEY, s -> getExtendedKeyCodeForChar(s.codePointAt(0)));
            actionConverters.put(SHORT_DESCRIPTION, identity);
            actionConverters.put(LONG_DESCRIPTION, identity);
            actionConverters.put(SMALL_ICON, SwingUtilitiesN::getImageIcon);
            actionConverters.put(LARGE_ICON_KEY, SwingUtilitiesN::getImageIcon);
            return unmodifiableMap(actionConverters);
        }
    }
}
