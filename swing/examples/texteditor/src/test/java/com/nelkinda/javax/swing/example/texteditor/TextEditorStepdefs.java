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

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;

import static com.nelkinda.javax.swing.SwingUtilitiesN.callAndWait;
import static com.nelkinda.javax.swing.SwingUtilitiesN.findComponent;
import static javax.swing.SwingUtilities.invokeAndWait;
import static javax.swing.SwingUtilities.invokeLater;
import static javax.swing.UIManager.getCrossPlatformLookAndFeelClassName;
import static javax.swing.UIManager.getLookAndFeel;
import static javax.swing.UIManager.setLookAndFeel;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Step definitions for accessing the textEditor.
 *
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
 * @version 0.0.2
 * @since 0.0.2
 */
public class TextEditorStepdefs {

    /**
     * The monitor to synchronize within steps.
     */
    private static final Object MONITOR = new Object();

    /**
     * The text editor under test.
     */
    private TextEditor textEditor;

    /**
     * The text component of the text editor under test.
     */
    private JTextComponent textEditorComponent;

    /**
     * Create TextEditor Step definitions.
     */
    public TextEditorStepdefs() {
        // Nothing to do
    }

    /**
     * Starts the text editor.
     */
    @Given("^I have just started the textEditor[,.]?$")
    public void iHaveJustStartedTheEditor() throws Throwable {
        invokeAndWait(() -> {
            textEditor = new TextEditor();
            textEditorComponent = findComponent(JTextComponent.class, textEditor.getWindow()).orElseThrow(AssertionError::new);
        });
        assertNotNull(textEditorComponent);
    }

    @When("^I enter the text \"([^\"]*)\"[,.]?$")
    public void iEnterTheText(final String text) throws BadLocationException, InvocationTargetException, InterruptedException {
        invokeAndWait(() -> {
            final Document document = textEditorComponent.getDocument();
            try {
                document.insertString(textEditorComponent.getCaretPosition(), text, null);
            } catch (final BadLocationException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @When("^I action \"([^\"]*)\"[,.]?$")
    public void iAction(final String actionCommand) throws InvocationTargetException, InterruptedException {
        invokeLater(() -> {
            final ActionMap actions = textEditor.getActions();
            final Action action = actions.get(actionCommand);
            assertNotNull(action);
            action.actionPerformed(new ActionEvent(textEditorComponent, 0, actionCommand));
        });
    }

    @When("^I enter the filename \"([^\"]*)\"[,.]?$")
    public void iEnterTheFilename(final String filename) throws InvocationTargetException, InterruptedException {
        final File file = new File(filename);
        invokeAndWait(() -> {
            textEditor.fileChooser.setSelectedFile(file);
            textEditor.fileChooser.approveSelection();
        });
    }

    @When("^I wait for I/O to be completed[,.]$")
    public void iWaitForIOToBeCompleted() throws InterruptedException, ExecutionException {
        textEditor.getLastWorker().get();
    }

    @Then("^the textEditor has focus[,.]?$")
    public void theEditorHasFocus() throws InvocationTargetException, InterruptedException, ExecutionException {
        final FocusAdapter focusAdapter = new FocusAdapter() {
            @SuppressWarnings({"NN_NAKED_NOTIFY", "NN_NAKED_NOTIFY"})
            @Override
            public void focusGained(final FocusEvent e) {
                synchronized (MONITOR) {
                    MONITOR.notify();
                }
            }
        };
        final Callable<Boolean> hasFocus = () -> textEditorComponent.hasFocus();
        final Runnable addFocusListener = () -> textEditorComponent.addFocusListener(focusAdapter);
        final Runnable removeFocusListener = () -> textEditorComponent.removeFocusListener(focusAdapter);
        invokeAndWait(addFocusListener);
        if (!callAndWait(hasFocus))
            synchronized (MONITOR) {
                try {
                    if (!callAndWait(hasFocus))
                        MONITOR.wait();
                } catch (final InterruptedException ignored) {
                    // Ignore
                }
            }
        invokeAndWait(removeFocusListener);
        assertTrue(callAndWait(hasFocus));
    }

    @Then("^the document name must be \"([^\"]*)\"[,.]?$")
    public void theDocumentNameMustBe(final String expectedDocumentName) throws InterruptedException {
        assertEquals(expectedDocumentName, textEditor.getTitle());
    }

    @Then("^the window title must be \"([^\"]*)\"[,.]?$")
    public void theWindowTitleMustBe(final String expectedWindowTitle) {
        assertEquals(expectedWindowTitle, textEditor.getWindow().getTitle());
    }

    @Then("^the document must have the following content:$")
    public void theDocumentMustHaveTheFollowingContent(final String expectedDocumentText) {
        assertEquals(expectedDocumentText, textEditorComponent.getText());
    }

    @Then("^I must be asked for a filename[,.]?$")
    public void iAmAskedForAFilename() throws InterruptedException, ExecutionException, InvocationTargetException {
        assertTrue(isFileChooserShowing());
    }

    private boolean isFileChooserShowing() throws InterruptedException, InvocationTargetException, ExecutionException {
        return callAndWait(() -> textEditor.fileChooser.isShowing());
    }

    @Then("^I must not be asked for a filename[,.]?$")
    public void iAmNotAskedForAFilename() throws InterruptedException, ExecutionException, InvocationTargetException {
        assertFalse(isFileChooserShowing());
    }

    @When("^I set the caret to position (\\d+)[,.]?$")
    public void iSetTheCursorToPosition(final int caretPosition) throws InvocationTargetException, InterruptedException {
        invokeAndWait(() -> textEditorComponent.setCaretPosition(caretPosition));
    }

    @When("^I mark from position (\\d+) to position (\\d+),$")
    public void iMarkFromPositionToPosition(final int start, final int end) throws InvocationTargetException, InterruptedException {
        invokeAndWait(() -> textEditorComponent.select(start, end));
    }

    @edu.umd.cs.findbugs.annotations.SuppressWarnings("DM_GC")
    @After
    public void closeTheEditor() throws InvocationTargetException, InterruptedException, ExecutionException {
        iWaitForAction("quit");
        assertFalse(callAndWait(() -> textEditorComponent.hasFocus()));
        textEditorComponent = null;
        textEditor = null;
        System.gc();
    }

    @When("^I wait for action \"([^\"]*)\"[,.]?$")
    public void iWaitForAction(final String actionCommand) throws InvocationTargetException, InterruptedException {
        invokeAndWait(() -> {
            final Action action = textEditor.getActions().get(actionCommand);
            assertNotNull(action);
            action.actionPerformed(new ActionEvent(textEditorComponent, 0, actionCommand));
        });
    }

    @Given("^the current look and feel is the cross-platform look and feel[,.]?$")
    public void theCurrentLookAndFeelIs() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        setLookAndFeel(getCrossPlatformLookAndFeelClassName());
    }

    @When("^I set the look and feel to \"([^\"]*)\"[,.]?$")
    public void iSetTheLookAndFeelTo(final String lookAndFeelName) throws InvocationTargetException, InterruptedException {
        iWaitForAction("lookAndFeel:" + lookAndFeelName);
    }

    @Then("^the look and feel must be \"([^\"]*)\"[,.]?$")
    public void theLookAndFeelMustBe(final String lookAndFeelName) {
        assertEquals(lookAndFeelName, getLookAndFeel().getName());
    }
}