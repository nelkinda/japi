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

package com.nelkinda.javax.swing.example.texteditor;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import static java.awt.Toolkit.getDefaultToolkit;
import static java.awt.datatransfer.DataFlavor.stringFlavor;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Step definitions for dealing with the system clipboard.
 *
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
 * @version 0.0.2
 * @since 0.0.2
 */
public class SystemClipboardStepdefs {
    @Given("^the system clipboard is empty[,.]?$")
    public void theSystemClipboardIsEmpty() throws Throwable {
        theSystemClipboardContains("");
    }

    @Given("^the system clipboard contains \"([^\"]*)\"[,.]?$")
    public void theSystemClipboardContains(final String clipboardText) throws Throwable {
        final Clipboard clipboard = getDefaultToolkit().getSystemClipboard();
        final StringSelection stringSelection = new StringSelection(clipboardText);
        attemptSetClipboardContents(clipboard, stringSelection);
    }

    private static void attemptSetClipboardContents(final Clipboard clipboard, final StringSelection stringSelection) throws InterruptedException {
        for (int count = 0; count < 3; count++)
            try {
                setClipboardContent(clipboard, stringSelection);
                return;
            } catch (final IllegalStateException ignore) {
                Thread.sleep(10);
            }
        setClipboardContent(clipboard, stringSelection);
    }

    private static void setClipboardContent(final Clipboard clipboard, final StringSelection stringSelection) {
        clipboard.setContents(stringSelection, stringSelection);
    }

    @Then("^the system clipboard must contain the text \"([^\"]*)\"[,.]?$")
    public void theSystemClipboardMustContainTheText(final String expectedClipboardContent) throws Throwable {
        final Clipboard clipboard = getDefaultToolkit().getSystemClipboard();
        final Transferable transferable = clipboard.getContents(null);
        assertNotNull(transferable);
        final String contents = (String) transferable.getTransferData(stringFlavor);
        assertEquals(expectedClipboardContent, contents);
    }

}
