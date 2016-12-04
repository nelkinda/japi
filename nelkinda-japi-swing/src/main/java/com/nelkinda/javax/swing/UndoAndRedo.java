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
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

public class UndoAndRedo implements UndoableEditListener {
    private final UndoManager undoManager = new UndoManager();
    private final AbstractUndoRedoAction undoAction = new UndoAction();
    private final AbstractUndoRedoAction redoAction = new RedoAction();

    @Override
    public void undoableEditHappened(final UndoableEditEvent e) {
        undoManager.undoableEditHappened(e);
        updateUndoAndRedoStates();
    }

    private void updateUndoAndRedoStates() {
        undoAction.updateState();
        redoAction.updateState();
    }

    public Action getUndoAction() {
        return undoAction;
    }

    public Action getRedoAction() {
        return redoAction;
    }

    private abstract class AbstractUndoRedoAction extends AbstractAction {
        AbstractUndoRedoAction() {
            setEnabled(false);
        }

        abstract void updateState();
    }

    private class RedoAction extends AbstractUndoRedoAction {
        @Override
        public void actionPerformed(final ActionEvent e) {
            undoManager.redo();
            updateUndoAndRedoStates();
        }

        @Override
        void updateState() {
            final boolean canRedo = undoManager.canRedo();
            setEnabled(canRedo);
            putValue(NAME, canRedo ? undoManager.getRedoPresentationName() : "Redo");
        }
    }

    private class UndoAction extends AbstractUndoRedoAction {
        @Override
        public void actionPerformed(final ActionEvent e) {
            undoManager.undo();
            updateUndoAndRedoStates();
        }

        @Override
        void updateState() {
            final boolean canUndo = undoManager.canUndo();
            setEnabled(canUndo);
            putValue(NAME, canUndo ? undoManager.getUndoPresentationName() : "Undo");
        }
    }
}
