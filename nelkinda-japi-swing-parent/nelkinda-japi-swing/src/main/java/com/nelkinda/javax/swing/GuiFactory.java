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

import java.util.ResourceBundle;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import org.jetbrains.annotations.NotNull;

import static com.nelkinda.javax.swing.SwingUtilitiesN.initActionFromBundle;

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
 * <tr><td><code>menuBar</code></td><td>the menus for the menu bar</td></tr>
 * <tr><td><code>toolBar</code></td><td>the buttons for the tool bar</td></tr>
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
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>
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
    public GuiFactory(@NotNull final ResourceBundle resourceBundle, @NotNull final ActionMap actionMap) {
        this.resourceBundle = resourceBundle;
        this.actionMap = actionMap;
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
     * @return Created JToolBar.
     */
    @NotNull
    private JToolBar createJToolBarFromKey(@NotNull final String key) {
        final JToolBar toolBar = new JToolBar();
        toolBar.setFocusable(false);
        for (final String actionCommand : resourceBundle.getString(key).split("\\s+")) {
            switch (actionCommand) {
            case "-":
            case "|":
                toolBar.addSeparator();
                break;
            default:
                toolBar.add(actionMap.get(actionCommand));
            }
        }
        SwingUtilitiesN.makeToolbarComponentsNotFocusable(toolBar);
        return toolBar;
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
        for (final String menuKey : resourceBundle.getString(menuBarKey).split("\\s+")) {
            jMenuBar.add(createJMenu(menuKey));
        }
        return jMenuBar;
    }

    /**
     * Creates a JMenu.
     *
     * @param menuKey The base key for the JMenu to create.
     * @return The created JMenu.
     */
    @NotNull
    private JMenu createJMenu(@NotNull final String menuKey) {
        final String spec = resourceBundle.getString(menuKey + ".menu");
        final JMenu jMenu = new JMenu(actionMap.get(menuKey));
        for (final String key : spec.split("\\s+")) {
            switch (key) {
            case "":
                break;
            case "-":
            case "|":
                jMenu.addSeparator();
                break;
            default:
                if (resourceBundle.containsKey(key + ".menu")) {
                    jMenu.add(createJMenu(key));
                } else {
                    jMenu.add(actionMap.get(key));
                }
            }
        }
        return jMenu;
    }
}
