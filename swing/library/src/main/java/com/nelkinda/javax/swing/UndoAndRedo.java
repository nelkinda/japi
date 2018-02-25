/*
 * Copyright © 2016 - 2018 Nelkinda Software Craft Pvt Ltd.
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
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

/**
 * Manages Undo and Redo for an application.
 * It's a wrapper for {@link UndoManager} which provides a more lean and application-oriented interface.
 *
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
 * @version 0.0.2
 * @since 0.0.2
 */
public class UndoAndRedo implements UndoableEditListener {

    /**
     * The UndoManager from Swing.
     */
    private final UndoManager undoManager = new UndoManager();

    /**
     * The Action to preform an Undo.
     */
    private final AbstractUndoRedoAction undoAction = new UndoAction();

    /**
     * The Action to perform Redo.
     */
    private final AbstractUndoRedoAction redoAction = new RedoAction();

    @Override
    public void undoableEditHappened(final UndoableEditEvent e) {
        undoManager.undoableEditHappened(e);
        updateUndoAndRedoStates();
    }

    /**
     * Updates the states of the Undo and Redo actions.
     */
    private void updateUndoAndRedoStates() {
        undoAction.update();
        redoAction.update();
    }

    /**
     * Returns an Action that can be used for Undo.
     *
     * @return an Action that can be used for Undo.
     */
    public Action getUndoAction() {
        return undoAction;
    }

    /**
     * Returns an Action that can be used for Redo.
     *
     * @return an Action that can be used for Redo.
     */
    public Action getRedoAction() {
        return redoAction;
    }

    /**
     * Action super-class for performing an Undo or Redo.
     *
     * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
     * @version 0.0.2
     * @since 0.0.2
     */
    private abstract class AbstractUndoRedoAction extends AbstractAction {
        /**
         * Create an Action for performing an Undo or Redo.
         */
        AbstractUndoRedoAction() {
            setEnabled(false);
        }

        /**
         * Updates this action.
         * There are two things to update:
         * <ul>
         * <li>The enabled state.</li>
         * <li>The name of the action displayed to the user, which depends on the action that would be undone or redone.</li>
         * </ul>
         */
        abstract void update();
    }

    /**
     * The Action to perform a Redo.
     *
     * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
     * @version 0.0.2
     * @since 0.0.2
     */
    private class RedoAction extends AbstractUndoRedoAction {
        @Override
        public void actionPerformed(final ActionEvent e) {
            undoManager.redo();
            updateUndoAndRedoStates();
        }

        @Override
        void update() {
            final boolean canRedo = undoManager.canRedo();
            setEnabled(canRedo);
            final String name = canRedo ? undoManager.getRedoPresentationName() : "Redo";
            putValue(NAME, name);
        }
    }

    /**
     * The Action to perform an Undo.
     *
     * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
     * @version 0.0.2
     * @since 0.0.2
     */
    private class UndoAction extends AbstractUndoRedoAction {
        @Override
        public void actionPerformed(final ActionEvent e) {
            undoManager.undo();
            updateUndoAndRedoStates();
        }

        @Override
        void update() {
            final boolean canUndo = undoManager.canUndo();
            setEnabled(canUndo);
            final String name = canUndo ? undoManager.getUndoPresentationName() : "Undo";
            putValue(NAME, name);
        }
    }
}
