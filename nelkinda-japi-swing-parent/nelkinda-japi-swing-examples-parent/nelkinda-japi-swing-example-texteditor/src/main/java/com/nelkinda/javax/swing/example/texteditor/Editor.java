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

package com.nelkinda.javax.swing.example.texteditor;

import com.nelkinda.javax.swing.GuiFactory;
import com.nelkinda.javax.swing.UndoAndRedo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.text.DefaultEditorKit;

import static com.nelkinda.javax.swing.GuiFactory.findJMenu;
import static com.nelkinda.javax.swing.SwingUtilitiesN.initActionFromBundle;
import static com.nelkinda.javax.swing.SwingUtilitiesN.setLookAndFeelFromClassName;
import static com.nelkinda.javax.swing.SwingUtilitiesN.setLookAndFeelFromName;
import static java.awt.BorderLayout.NORTH;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Files.write;
import static java.util.ResourceBundle.getBundle;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.SwingUtilities.invokeAndWait;
import static javax.swing.UIManager.getInstalledLookAndFeels;

/**
 * A text editor as an example for how to use JAPI.
 *
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>
 * @version 0.0.2
 * @since 0.0.2
 */
public class Editor {
    private static final String UNNAMED = "<Unnamed>";

    final JFileChooser fileChooser = new JFileChooser();
    private final JEditorPane editorPane = new JEditorPane();
    private final UndoAndRedo undoAndRedo = new UndoAndRedo();
    private final ActionMap actions = new ActionMap();
    private final ResourceBundle resourceBundle = getBundle(getClass().getName());
    private String documentName = UNNAMED;
    private final JFrame frame = new JFrame("Editor: " + documentName);
    private File file;
    private SwingWorker lastWorker;

    Editor() {
        createActions();
        editorPane.getDocument().addUndoableEditListener(undoAndRedo);
        final GuiFactory guiFactory = new GuiFactory(resourceBundle, actions);
        frame.setJMenuBar(guiFactory.createJMenuBar());
        addLookAndFeelMenuEntries();
        frame.getContentPane().add(new JScrollPane(editorPane));
        frame.getContentPane().add(guiFactory.createJToolBar(), NORTH);
        frame.pack();
        frame.setVisible(true);
    }

    private void createActions() {
        createAction("file", this::dummy);
        createAction("edit", this::dummy);
        createAction("lookAndFeel", this::dummy);
        createAction("new", this::newDocument);
        createAction("open", this::open);
        createAction("save", this::save);
        createAction("saveAs", this::saveAs);
        createAction("quit", this::quit);
        createAction("cut-to-clipboard", new DefaultEditorKit.CutAction());
        createAction("copy-to-clipboard", new DefaultEditorKit.CopyAction());
        createAction("paste-from-clipboard", new DefaultEditorKit.PasteAction());
        createAction("undo", undoAndRedo.getUndoAction());
        createAction("redo", undoAndRedo.getRedoAction());
    }

    private Action createAction(final String actionCommand, final ActionListener actionListener) {
        final Action action = actionListener instanceof Action ? (Action) actionListener : new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                actionListener.actionPerformed(e);
            }
        };
        initActionFromBundle(action, actionCommand, resourceBundle);
        actions.put(actionCommand, action);
        return action;
    }

    private void addLookAndFeelMenuEntries() {
        findJMenu(frame.getJMenuBar(), "lookAndFeel").ifPresent(
                lookAndFeel -> {
                    for (final UIManager.LookAndFeelInfo lookAndFeelInfo : getInstalledLookAndFeels()) {
                        final Action action = createAction("lookAndFeel:" + lookAndFeelInfo.getName(),
                                e -> setLookAndFeelFromClassName(lookAndFeelInfo.getClassName(), frame));
                        action.putValue(Action.NAME, lookAndFeelInfo.getName());
                        lookAndFeel.add(action);
                    }
                }
        );
    }

    public static void main(final String... args) throws InvocationTargetException, InterruptedException {
        invokeAndWait(() -> {
            setLookAndFeelFromName("Nimbus");
            new Editor();
        });
    }

    private void dummy(final ActionEvent e) {
    }

    private void newDocument(final ActionEvent e) {
        editorPane.setText("");
        setFile(null);
    }

    private void setFile(final File file) {
        this.file = file;
        if (file != null)
            documentName = file.getName();
        else
            documentName = "<Unnamed>";
        frame.setTitle("Editor: " + documentName);
    }

    private void open(final ActionEvent e) {
        final int returnValue = fileChooser.showOpenDialog(frame);
        switch (returnValue) {
        case APPROVE_OPTION:
            runWorker(new Loader(fileChooser.getSelectedFile()));
        }
    }

    private synchronized void runWorker(final SwingWorker lastWorker) {
        this.lastWorker = lastWorker;
        lastWorker.execute();
        notify();
    }

    private void save(final ActionEvent e) {
        if (file == null)
            saveAs(e);
        else runWorker(new Saver(file));
    }

    private void saveAs(final ActionEvent e) {
        final int returnValue = fileChooser.showSaveDialog(frame);
        switch (returnValue) {
        case APPROVE_OPTION:
            runWorker(new Saver(fileChooser.getSelectedFile()));
        }
    }

    private void quit(final ActionEvent e) {
        frame.dispose();
    }

    String getDocumentName() {
        return documentName;
    }

    JFrame getWindow() {
        return frame;
    }

    ActionMap getActions() {
        return actions;
    }

    synchronized SwingWorker getLastWorker() throws InterruptedException {
        SwingWorker lastWorker;
        while ((lastWorker = this.lastWorker) == null)
            wait();
        this.lastWorker = null;
        return lastWorker;
    }

    private class Saver extends SwingWorker<Void, Void> {
        private final File file;

        Saver(final File file) {
            this.file = file;
        }

        @Override
        protected Void doInBackground() throws Exception {
            write(file.toPath(), editorPane.getText().getBytes("UTF-8"));
            invokeAndWait(() -> setFile(file));
            return null;
        }
    }

    private class Loader extends SwingWorker<Void, Void> {
        private final File file;

        Loader(final File file) {
            this.file = file;
        }

        @Override
        protected Void doInBackground() throws Exception {
            final String text = new String(readAllBytes(file.toPath()), "UTF-8");
            invokeAndWait(() -> {
                editorPane.setText(text);
                setFile(file);
            });
            return null;
        }
    }
}