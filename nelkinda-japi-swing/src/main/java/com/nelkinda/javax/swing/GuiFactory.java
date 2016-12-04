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
import java.util.Optional;
import java.util.ResourceBundle;
import javax.swing.ActionMap;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

import static javax.swing.Action.ACTION_COMMAND_KEY;

public class GuiFactory {

    private final ResourceBundle resourceBundle;
    private final ActionMap actionMap;

    public GuiFactory(final ResourceBundle resourceBundle, final ActionMap actionMap) {
        this.resourceBundle = resourceBundle;
        this.actionMap = actionMap;
    }

    private static void makeToolbarComponentsNotFocusable(final JToolBar toolBar) {
        for (final Component c : toolBar.getComponents()) {
            c.setFocusable(false);
        }
    }

    public static Optional<JMenu> findJMenu(final JMenuBar jMenuBar, final String actionCommand) {
        for (int i = 0; i < jMenuBar.getMenuCount(); i++) {
            final JMenu jMenu = jMenuBar.getMenu(i);
            if (actionCommand.equals(jMenu.getAction().getValue(ACTION_COMMAND_KEY))) {
                return Optional.of(jMenu);
            }
            Optional<JMenu> candidate = findJMenu(jMenu, actionCommand);
            if (candidate.isPresent()) {
                return candidate;
            }
        }
        return Optional.empty();
    }

    public static Optional<JMenu> findJMenu(final JMenu jMenu, final String actionCommand) {
        for (int i = 0; i < jMenu.getItemCount(); i++) {
            final JMenuItem jMenuItem = jMenu.getItem(i);
            if (jMenuItem instanceof JMenu) {
                if (actionCommand.equals(jMenuItem.getAction().getValue(ACTION_COMMAND_KEY))) {
                    return Optional.of((JMenu) jMenuItem);
                }
                Optional<JMenu> candidate = findJMenu((JMenu) jMenuItem, actionCommand);
                if (candidate.isPresent()) {
                    return candidate;
                }
            }
        }
        return Optional.empty();
    }

    public JToolBar createJToolBar() {
        final JToolBar toolBar = new JToolBar();
        toolBar.setFocusable(false);
        for (final String actionCommand : resourceBundle.getString("toolBar").split("\\s+")) {
            switch (actionCommand) {
            case "-":
            case "|":
                toolBar.addSeparator();
                break;
            default:
                toolBar.add(actionMap.get(actionCommand));
            }
        }
        makeToolbarComponentsNotFocusable(toolBar);
        return toolBar;
    }

    private JMenu createJMenu(final String menuKey) {
        final String spec = resourceBundle.getString(menuKey + ".menu");
        final JMenu jMenu = new JMenu(actionMap.get(menuKey));
        for (final String key : spec.split("\\s+")) {
            switch (key) {
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

    public JMenuBar createJMenuBar() {
        final JMenuBar jMenuBar = new JMenuBar();
        for (final String key : resourceBundle.getString("menuBar").split("\\s+")) {
            jMenuBar.add(createJMenu(key));
        }
        return jMenuBar;
    }
}
