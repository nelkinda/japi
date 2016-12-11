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
import java.io.File;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.text.DefaultEditorKit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.nelkinda.javax.swing.SwingUtilitiesN.findJMenu;
import static com.nelkinda.javax.swing.SwingUtilitiesN.setLookAndFeelFromClassName;
import static com.nelkinda.javax.swing.SwingUtilitiesN.setLookAndFeelFromName;
import static java.awt.BorderLayout.NORTH;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Files.write;
import static java.util.ResourceBundle.getBundle;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.SwingUtilities.invokeAndWait;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.UIManager.getInstalledLookAndFeels;

/**
 * A text editor as an example for how to use JAPI.
 *
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
 * @version 0.0.2
 * @since 0.0.2
 */
public class TextEditor {
    private static final String UNNAMED = "<Unnamed>";

    final JFileChooser fileChooser = new JFileChooser();

    private final JEditorPane editorPane = new JEditorPane();

    private final UndoAndRedo undoAndRedo = new UndoAndRedo();

    private final ActionMap actions = new ActionMap();

    private final GuiFactory guiFactory;

    private String documentName = UNNAMED;

    private final JFrame frame = new JFrame("TextEditor: " + documentName);

    /**
     * The current file.
     * May be {@code null} when the file is unnamed.
     */
    @Nullable
    private File file;

    /**
     * The last SwingWorker.
     * This information is provided for testing asynchronous operations.
     */
    private SwingWorker lastWorker;

    TextEditor() {
        guiFactory = new GuiFactory(getBundle(getClass().getName()), actions);
        createActions();
        editorPane.getDocument().addUndoableEditListener(undoAndRedo);
        frame.setJMenuBar(guiFactory.createJMenuBar());
        addLookAndFeelMenuEntries();
        frame.getContentPane().add(new JScrollPane(editorPane));
        frame.getContentPane().add(guiFactory.createJToolBar(), NORTH);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Creates the actions.
     */
    private void createActions() {
        guiFactory.createAction("file");
        guiFactory.createAction("edit");
        guiFactory.createAction("lookAndFeel");
        guiFactory.createAction("new", this::newDocument);
        guiFactory.createAction("open", this::open);
        guiFactory.createAction("save", this::save);
        guiFactory.createAction("saveAs", this::saveAs);
        guiFactory.createAction("quit", this::quit);
        guiFactory.createAction("cut-to-clipboard", new DefaultEditorKit.CutAction());
        guiFactory.createAction("copy-to-clipboard", new DefaultEditorKit.CopyAction());
        guiFactory.createAction("paste-from-clipboard", new DefaultEditorKit.PasteAction());
        guiFactory.createAction("undo", undoAndRedo.getUndoAction());
        guiFactory.createAction("redo", undoAndRedo.getRedoAction());
    }

    private void addLookAndFeelMenuEntries() {
        findJMenu(frame.getJMenuBar(), "lookAndFeel")
                .ifPresent(this::addLookAndFeelMenuEntries);
    }

    private void addLookAndFeelMenuEntries(final JMenu lookAndFeel) {
        for (final UIManager.LookAndFeelInfo lookAndFeelInfo : getInstalledLookAndFeels())
            addLookAndFeelMenuEntry(lookAndFeel, lookAndFeelInfo);
    }

    private void addLookAndFeelMenuEntry(final JMenu lookAndFeel, final UIManager.LookAndFeelInfo lookAndFeelInfo) {
        lookAndFeel.add(createLookAndFeelAction(lookAndFeelInfo));
    }

    @NotNull
    private Action createLookAndFeelAction(final UIManager.LookAndFeelInfo lookAndFeelInfo) {
        final Action action = guiFactory.createAction("lookAndFeel:" + lookAndFeelInfo.getName(),
                () -> setLookAndFeelFromClassName(lookAndFeelInfo.getClassName(), frame));
        action.putValue(Action.NAME, lookAndFeelInfo.getName());
        return action;
    }

    private void newDocument() {
        editorPane.setText("");
        setFile(null);
    }

    private void setFile(@Nullable final File file) {
        this.file = file;
        if (file != null)
            documentName = file.getName();
        else
            documentName = "<Unnamed>";
        frame.setTitle("TextEditor: " + documentName);
    }

    private void open() {
        if (APPROVE_OPTION == fileChooser.showOpenDialog(frame))
            runWorker(new Loader(fileChooser.getSelectedFile()));
    }

    private synchronized void runWorker(final SwingWorker lastWorker) {
        this.lastWorker = lastWorker;
        lastWorker.execute();
        notify();
    }

    private void save() {
        if (file == null)
            saveAs();
        else
            runWorker(new Saver(file));
    }

    private void saveAs() {
        if (APPROVE_OPTION == fileChooser.showSaveDialog(frame)) {
            runWorker(new Saver(fileChooser.getSelectedFile()));
        }
    }

    /**
     * Quits the application.
     */
    private void quit() {
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

    /**
     * Returns the last SwingWorker.
     * If there is no last SwingWorker, waits until there is one.
     *
     * @return The last SwingWorker.
     * @throws InterruptedException In case waiting for the last SwingWorker was interrupted.
     */
    synchronized SwingWorker getLastWorker() throws InterruptedException {
        SwingWorker lastWorker;
        while ((lastWorker = this.lastWorker) == null)
            wait();
        this.lastWorker = null;
        return lastWorker;
    }

    /**
     * Main program and loader.
     *
     * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
     * @version 0.0.2
     * @since 0.0.2
     */
    public enum Main {
        ; // NOSONAR Bug in SonarQube: https://jira.sonarsource.com/browse/SONARJAVA-1909

        /**
         * Main program.
         *
         * @param args Command line arguments (ignored).
         */
        public static void main(final String... args) {
            invokeLater(() -> {
                setLookAndFeelFromName("Nimbus");
                new TextEditor();
            });
        }
    }

    /**
     * SwingWorker to save a file from the editor.
     *
     * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>
     * @version 0.0.2
     * @since 0.0.2
     */
    private class Saver extends SwingWorker<Void, Void> {

        /**
         * File to save.
         */
        private final File file;

        /**
         * Create a Saver.
         *
         * @param file File to save.
         */
        Saver(final File file) {
            this.file = file;
        }

        @Override
        protected Void doInBackground() throws Exception {
            write(file.toPath(), editorPane.getText().getBytes(UTF_8));
            invokeAndWait(() -> setFile(file));
            return null;
        }

    }

    /**
     * SwingWorker to load a file into the editor.
     *
     * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>
     * @version 0.0.2
     * @since 0.0.2
     */
    private class Loader extends SwingWorker<Void, Void> {
        /**
         * File to load.
         */
        private final File file;

        /**
         * Create a Loader.
         *
         * @param file File to load.
         */
        Loader(final File file) {
            this.file = file;
        }

        @Override
        protected Void doInBackground() throws Exception {
            final String text = new String(readAllBytes(file.toPath()), UTF_8);
            invokeAndWait(() -> {
                editorPane.setText(text);
                setFile(file);
            });
            return null;
        }
    }

}