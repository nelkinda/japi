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

package com.nelkinda.javax.swing.example.csveditor;

import com.nelkinda.javax.swing.GuiFactory;
import java.awt.event.ActionEvent;
import javax.swing.ActionMap;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import static com.nelkinda.javax.swing.SwingUtilitiesN.setLookAndFeelFromName;
import static java.awt.BorderLayout.NORTH;
import static java.util.ResourceBundle.getBundle;
import static javax.swing.SwingUtilities.invokeLater;

/**
 * A CSV editor as an example for how to use JAPI.
 *
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
 * @version 0.0.2
 * @since 0.0.2
 */
public class CsvEditor {

    /**
     * The title of an unnamed document.
     */
    private static final String UNNAMED = "<Unnamed>";

    /**
     * The actual csv editor.
     */
    private final JTable jTable = new JTable(new CsvTableModel());

    private final ActionMap actionMap = new ActionMap();

    private final GuiFactory guiFactory;

    private String title = UNNAMED;

    private final JFrame jFrame = new JFrame("CSV Editor: " + title);

    CsvEditor() {
        guiFactory = new GuiFactory(getBundle(getClass().getName()), actionMap);
        createActions();
        jFrame.setJMenuBar(guiFactory.createJMenuBar());
        jFrame.getContentPane().add(new JScrollPane(jTable));
        jFrame.getContentPane().add(guiFactory.createJToolBar(), NORTH);
        jFrame.pack();
        jFrame.setVisible(true);
    }

    /**
     * Creates the actions.
     */
    private void createActions() {
        guiFactory.createAction("file");
        guiFactory.createAction("openRecent");
        guiFactory.createAction("new", this::newFile);
        guiFactory.createAction("open", this::open);
        guiFactory.createAction("save", this::save);
        guiFactory.createAction("saveAs", this::saveAs);
        guiFactory.createAction("quit", this::quit);
        guiFactory.createAction("edit");
        guiFactory.createAction("columnInsertBefore", this::columnInsertBefore);
        guiFactory.createAction("columnInsertAfter", this::columnInsertAfter);
        guiFactory.createAction("columnDelete", this::columnDelete);
        guiFactory.createAction("rowInsertBefore", this::rowInsertBefore);
        guiFactory.createAction("rowInsertAfter", this::rowInsertAfter);
        guiFactory.createAction("rowDelete", this::rowDelete);
        guiFactory.createAction("find", this::find);
        guiFactory.createAction("findAgain", this::findAgain);
        guiFactory.createAction("help");
        guiFactory.createAction("about", this::about);
    }

    JFrame getJFrame() {
        return jFrame;
    }

    String getTitle() {
        return title;
    }

    public ActionMap getActionMap() {
        return actionMap;
    }

    public void columnInsertBefore(final ActionEvent e) {
        // TODO
    }

    public void columnInsertAfter(final ActionEvent e) {
        final int editingColumn = jTable.getEditingColumn();
        ((CsvTableModel) jTable.getModel()).columnInsertAfter(editingColumn, generateColumnName(jTable.getModel().getColumnCount()));
        jTable.setEditingColumn(editingColumn + 1);
    }

    private String generateColumnName(final int columnCount) {
        String columnName = "";
        for (int columnRemainder = columnCount + 1; columnRemainder > 0; columnRemainder /= 26)
            columnName = ((char) ('A' - 1 + columnRemainder % 26)) + columnName;
        return "Unnamed_" + columnName;
    }

    public void columnDelete(final ActionEvent e) {
        // TODO
    }

    public void newFile(final ActionEvent e) {
        // TODO
    }

    public void open(final ActionEvent e) {
        // TODO
    }

    public void save(final ActionEvent e) {
        // TODO
    }

    public void saveAs(final ActionEvent e) {
        // TODO
    }

    public void quit() {
        jFrame.dispose();
    }

    public void rowInsertBefore(final ActionEvent e) {
        // TODO
    }

    public void rowInsertAfter(final ActionEvent e) {
        // TODO
    }

    public void rowDelete(final ActionEvent e) {
        // TODO
    }

    public void find(final ActionEvent e) {
        // TODO
    }

    public void findAgain(final ActionEvent e) {
        // TODO
    }

    public void about() {
        // TODO
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
                new CsvEditor();
            });
        }
    }
}
