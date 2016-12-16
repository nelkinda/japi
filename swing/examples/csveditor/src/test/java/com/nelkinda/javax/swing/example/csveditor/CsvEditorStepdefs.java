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

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import javax.swing.JTable;
import org.jetbrains.annotations.NotNull;

import static com.nelkinda.javax.swing.SwingUtilitiesN.findComponent;
import static com.nelkinda.javax.swing.test.SwingAssert.assertHasFocus;
import static javax.swing.SwingUtilities.invokeAndWait;
import static org.junit.Assert.assertEquals;

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
            csvEditorComponent = findComponent(JTable.class, csvEditor.getWindow()).orElseThrow(AssertionError::new);
        });
    }

    @Then("^the table name must be \"([^\"]*)\"[,.]?$")
    public void theTableNameMustBe(@NotNull final String expectedTableName) throws Throwable {
        assertEquals(expectedTableName, csvEditor.getTitle());
    }

    @Then("^the csvEditor must have focus[,.]?$")
    public void theCsvEditorHasFocus() throws Throwable {
        assertHasFocus(csvEditorComponent);
    }

}
