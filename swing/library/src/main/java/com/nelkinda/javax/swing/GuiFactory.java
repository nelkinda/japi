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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import org.jetbrains.annotations.NotNull;

import static com.nelkinda.javax.swing.SwingUtilitiesN.initActionFromBundle;
import static java.util.Objects.requireNonNull;

/**
 * The GuiFactory creates components based on a resource bundle and an action map.
 * <p>
 * The following keys are supported in the resource bundle:
 * <table>
 * <caption>Supported keys</caption>
 * <thead>
 * <tr><th>Key</th><th>Purpose</th></tr>
 * </thead>
 * <tbody>
 * <tr><td>{@code menuBar}</td><td>the menus for the menu bar</td></tr>
 * <tr><td>{@code toolBar}</td><td>the buttons for the tool bar</td></tr>
 * <tr><td><code><var>action</var>.menu</code></td><td>the menu items or sub menus for a menu</td></tr>
 * <tr><td><code><var>action</var>.AcceleratorKey</code></td><td>The keystroke for triggering an action</td></tr>
 * <tr><td><code><var>action</var>.SwingDisplayedMnemonicIndexKey</code></td><td>The index of the mnemonic character to underline</td></tr>
 * <tr><td><code><var>action</var>.Name</code></td><td>The main text of the action, appears in menus</td></tr>
 * <tr><td><code><var>action</var>.MnemonicKey</code></td><td>The key to use as mnemonic</td></tr>
 * <tr><td><code><var>action</var>.ShortDescription</code></td><td>The text to show as a tooltip</td></tr>
 * <tr><td><code><var>action</var>.SmallIcon</code></td><td>Small icon, shown in menus</td></tr>
 * <tr><td><code><var>action</var>.SwingLargeIconKey</code></td><td>Large icon, shown in toolbars</td></tr>
 * </tbody>
 * </table>
 *
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
 * @version 0.0.2
 * @since 0.0.2
 */
public class GuiFactory {

    /**
     * The resource bundle from which to get the information about the components that are to be created.
     */
    @NotNull
    private final ResourceBundle resourceBundle;

    /**
     * The action map for actions.
     * Actions are used for toolbar buttons and menu items.
     */
    @NotNull
    private final ActionMap actionMap;

    /**
     * Creates a GuiFactory.
     *
     * @param resourceBundle The resource bundle from which to get the information about the components that are to be created.
     * @param actionMap      The action map for actions which are used for toolbar buttons and menu items.
     */
    public GuiFactory(@NotNull final ResourceBundle resourceBundle, @NotNull final ActionMap actionMap) { // NOSONAR Bug
        this.resourceBundle = requireNonNull(resourceBundle);
        this.actionMap = requireNonNull(actionMap);
    }

    /**
     * Creates a JToolBar with the actions specified by the resource bundle.
     * They key used is {@code base + ".toolBar"}.
     *
     * @param base Base to use.
     * @return Created JToolBar.
     */
    @NotNull
    public JToolBar createJToolBar(@NotNull final String base) {
        return createJToolBarFromKey(base + ".toolBar");
    }

    /**
     * Creates a JToolBar with the actions specified by the resource bundle.
     *
     * @param key Key from which to create the JToolBar.
     * @return Created JToolBar.
     */
    @NotNull
    private JToolBar createJToolBarFromKey(@NotNull final String key) {
        final JToolBar toolBar = new JToolBar();
        toolBar.setFocusable(false);
        processItemList(key, toolBar::addSeparator, itemKey -> toolBar.add(actionMap.get(itemKey)));
        SwingUtilitiesN.makeToolbarComponentsNotFocusable(toolBar);
        return toolBar;
    }

    /**
     * Loops over an item list.
     *
     * @param listKey   Key for which to get the list from the resource bundle.
     * @param separator Code to execute for creating a separator (when the list contains {@code "|"} or {@code "-"}.
     * @param adder     Code to execute for processing a normal list item.
     */
    private void processItemList(@NotNull final String listKey, @NotNull final Runnable separator, @NotNull final Consumer<String> adder) {
        for (final String itemKey : getList(listKey)) {
            processItem(itemKey, separator, adder);
        }
    }

    /**
     * Gets a key from the resource bundle and splits it into a list.
     * The returned list might contain the empty string when the value is empty or contains only whitespace.
     *
     * @param key Key to get from the resource bundle.
     * @return The list split from the value of the {@code key}.
     */
    @NotNull
    private String[] getList(@NotNull final String key) {
        return resourceBundle.getString(key).split("\\s+");
    }

    /**
     * Processes a single list item.
     *
     * @param itemKey   Key of the item. May be the empty string in which case nothing will be done.
     * @param separator Code to execute if the itemKey is a separator ({@code "-"} or {@code "|"}).
     * @param adder     Code to execute if the itemKey is something else.
     */
    private void processItem(@NotNull final String itemKey, @NotNull final Runnable separator, @NotNull final Consumer<String> adder) {
        switch (itemKey) {
        case "":
            break;
        case "-":
        case "|":
            separator.run();
            break;
        default:
            adder.accept(itemKey);
        }
    }

    /**
     * Creates a JToolBar with the actions specified by the resource bundle.
     * The key used is {@code "toolBar"}
     *
     * @return Created JToolBar.
     */
    @NotNull
    public JToolBar createJToolBar() {
        return createJToolBarFromKey("toolBar");
    }

    /**
     * Creates a JMenuBar.
     *
     * @return The created JMenuBar.
     */
    @NotNull
    public JMenuBar createJMenuBar() {
        return createJMenuBarImpl("menuBar");
    }

    @NotNull
    public JMenuBar createJMenuBar(@NotNull final String base) {
        return createJMenuBarImpl(base + ".menuBar");
    }

    /**
     * Creates a JMenuBar.
     *
     * @param menuBarKey Key for the JMenuBar.
     * @return The created JMenuBar.
     */
    @NotNull
    private JMenuBar createJMenuBarImpl(@NotNull final String menuBarKey) {
        final JMenuBar jMenuBar = new JMenuBar();
        for (final String menuKey : getList(menuBarKey)) {
            jMenuBar.add(createJMenu(menuKey));
        }
        return jMenuBar;
    }

    /**
     * Creates a JMenu.
     *
     * @param base The base key for the JMenu to create.
     * @return The created JMenu.
     */
    @NotNull
    private JMenu createJMenu(@NotNull final String base) {
        final JMenu jMenu = new JMenu(actionMap.get(base));
        processItemList(base + ".menu", jMenu::addSeparator, itemKey -> {
            if (resourceBundle.containsKey(itemKey + ".menu")) {
                jMenu.add(createJMenu(itemKey));
            } else {
                jMenu.add(actionMap.get(itemKey));
            }
        });
        return jMenu;
    }

    @NotNull
    public Action createAction(@NotNull final String actionCommand, @NotNull final SerializableRunnable runnable) {
        return createAction(actionCommand, new SimpleAction(runnable));
    }

    @NotNull
    public Action createAction(@NotNull final String actionCommand, @NotNull final ActionListener actionListener) {
        return setupAction(actionCommand, actionListener instanceof Action ? (Action) actionListener : new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                actionListener.actionPerformed(e);
            }
        });
    }

    /**
     * Sets up an Action by initializing it form the resource bundle and storing it in the actionMap.
     *
     * @param actionCommand Action command of the action to initialize.
     * @param action        Action to setup.
     * @return Setup action.
     */
    @NotNull
    public Action setupAction(@NotNull final String actionCommand, @NotNull final Action action) {
        initActionFromBundle(action, actionCommand, resourceBundle);
        actionMap.put(actionCommand, action);
        return action;
    }

    @NotNull
    public Action createAction(@NotNull final String actionCommand) {
        return setupAction(actionCommand, new DummyAction());
    }
}
