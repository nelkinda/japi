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

package com.nelkinda.javax.swing.example.csveditor;

import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.jetbrains.annotations.NotNull;

import static com.nelkinda.javax.swing.SwingUtilitiesN.callAndWait;
import static com.nelkinda.javax.swing.SwingUtilitiesN.findComponent;
import static com.nelkinda.javax.swing.test.SwingAssert.assertHasFocus;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static javax.swing.SwingUtilities.invokeAndWait;
import static javax.swing.SwingUtilities.invokeLater;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Step definitions for accessing the CsvEditor.
 *
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
 * @version 0.0.2
 * @since 0.0.2
 */
public class CsvEditorStepdefs {

    /**
     * The CSV editor under test.
     */
    private CsvEditor csvEditor;

    /**
     * The table component of the CSV editor under test.
     */
    private JTable csvEditorComponent;

    /**
     * Create CsvEditor Step definitions.
     */
    public CsvEditorStepdefs() {
        // Nothing to do
    }

    /**
     * Starts the CSV editor.
     */
    @Given("^I have just started the csvEditor[,.]?$")
    public void iHaveJustStartedTheCsvEditor() throws Throwable {
        invokeAndWait(() -> {
            csvEditor = new CsvEditor();
            csvEditorComponent = findComponent(JTable.class, csvEditor.getJFrame()).orElseThrow(AssertionError::new);
        });
    }

    @Then("^the table name must be \"([^\"]*)\"[,.]?$")
    public void theTableNameMustBe(@NotNull final String expectedTableName) throws Throwable {
        assertEquals(expectedTableName, csvEditor.getTitle());
    }

    @Then("^the csvEditor MUST have focus[,.]?$")
    public void theCsvEditorHasFocus() throws Throwable {
        assertHasFocus(csvEditorComponent);
    }

    @Then("^the window title MUST be \"([^\"]*)\"[,.]?$")
    public void theWindowTitleMustBe(final String expectedWindowTitle) {
        assertEquals(expectedWindowTitle, csvEditor.getJFrame().getTitle());
    }

    @And("^the table MUST be empty[,.]?$")
    public void theTableMustBeEmpty() throws Throwable {
        theTableContentMustBe(DataTable.create(emptyList()));
    }

    @And("^the table content MUST be:$")
    public void theTableContentMustBe(@NotNull final DataTable expectedDataTable) throws Throwable {
        final DataTable actualDataTable = createDataTable(csvEditorComponent);
        actualDataTable.diff(expectedDataTable);
    }

    /**
     * Creates a Cucumber DataTable from a JTable.
     *
     * @param jTable JTable from which to create the Cucumber DataTable.
     * @return Cucumber DataTable created from {@code jTable}.
     */
    @NotNull
    private static DataTable createDataTable(@NotNull final JTable jTable) {
        return createDataTable(jTable.getModel());
    }

    /**
     * Creates a Cucumber DataTable from a TableModel.
     *
     * @param tableModel TableModel for which to create a Cucumber DataTable.
     * @return DataTable created from {@code tableModel}.
     */
    @NotNull
    private static DataTable createDataTable(@NotNull final TableModel tableModel) {
        final List<List<String>> rawTableData = getTableCellsAs2DList(tableModel);
        return DataTable.create(rawTableData);
    }

    /**
     * Gets the column names of a TableModel.
     *
     * @param tableModel TableModel from which to get the column names.
     * @return The column names of {@code tableModel} as array.
     */
    @NotNull
    private static String[] getColumnNames(@NotNull final TableModel tableModel) {
        final String[] columnHeaders = new String[tableModel.getColumnCount()];
        for (int columnIndex = 0; columnIndex < tableModel.getColumnCount(); columnIndex++)
            columnHeaders[columnIndex] = tableModel.getColumnName(columnIndex);
        return columnHeaders;
    }

    /**
     * Gets the table cells of a TableModel as two-dimensional list.
     *
     * @param tableModel TableModel from which to get the table cells.
     * @return Table cells from {@code tableModel} as two-dimensional list of Strings.
     */
    @NotNull
    private static List<List<String>> getTableCellsAs2DList(@NotNull final TableModel tableModel) {
        final List<List<String>> rawTableData = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < tableModel.getRowCount(); rowIndex++)
            rawTableData.add(getRow(tableModel, rowIndex));
        if (tableModel.getColumnCount() != 0)
            rawTableData.add(0, asList(getColumnNames(tableModel)));
        return rawTableData;
    }

    /**
     * Gets a row from a TableModel as list of Strings.
     *
     * @param tableModel TableModel from which to get a table row.
     * @param rowIndex   Index of the row to get.
     * @return Table row from {@code tableModel} as list of Strings.
     */
    @NotNull
    private static List<String> getRow(@NotNull final TableModel tableModel, final int rowIndex) {
        final List<String> rowData = new ArrayList<>();
        for (int columnIndex = 0; columnIndex < tableModel.getColumnCount(); columnIndex++)
            rowData.add((String) tableModel.getValueAt(rowIndex, columnIndex));
        return rowData;
    }

    @When("^I action \"([^\"]*)\"[,.]?$")
    public void iAction(final String actionCommand) throws InvocationTargetException, InterruptedException {
        invokeLater(() -> {
            final ActionMap actions = csvEditor.getActionMap();
            final Action action = actions.get(actionCommand);
            assertNotNull(action);
            action.actionPerformed(new ActionEvent(csvEditorComponent, 0, actionCommand));
        });
    }

    @After
    public void closeTheEditor() throws InvocationTargetException, InterruptedException, ExecutionException {
        iWaitForAction("quit");
        assertFalse(callAndWait(() -> csvEditorComponent.hasFocus()));
        csvEditorComponent = null;
        csvEditor = null;
        System.gc();
    }

    @When("^I wait for action \"([^\"]*)\"[,.]?$")
    public void iWaitForAction(final String actionCommand) throws InvocationTargetException, InterruptedException {
        invokeAndWait(() -> {
            final Action action = csvEditor.getActionMap().get(actionCommand);
            assertNotNull(action);
            action.actionPerformed(new ActionEvent(csvEditorComponent, 0, actionCommand));
        });
    }

    @Then("^the window is disposed[,.]?$")
    public void theWindowIsDisposed() throws Throwable {
        assertFalse(callAndWait(() -> csvEditorComponent.hasFocus()));
        assertFalse(callAndWait(() -> csvEditor.getJFrame().isVisible()));
        assertFalse(callAndWait(() -> csvEditor.getJFrame().isDisplayable()));
    }

    @Before
    public void setup() {

    }

    @After
    public void after() {

    }
}
