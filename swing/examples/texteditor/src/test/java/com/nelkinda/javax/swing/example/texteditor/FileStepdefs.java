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

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.io.File;
import java.io.IOException;

import static java.nio.file.Files.readAllBytes;
import static java.nio.file.Files.write;
import static java.nio.file.Paths.get;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Step definitions for dealing with files.
 *
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
 * @version 0.0.2
 * @since 0.0.2
 */
public class FileStepdefs {
    @Given("^the file \"([^\"]*)\" has the following content:$")
    public void theFileHasTheFollowingContent(final String filename, final String content) throws IOException {
        write(get(filename), content.getBytes("UTF-8"));
    }

    @Given("^the file \"([^\"]*)\" does not exist[,.]?$")
    public void theFileDoesNotExist(final String filename) {
        final File file = new File(filename);
        if (file.exists())
            assertTrue(file.delete());
    }

    @Then("^the file \"([^\"]*)\" must have the following content:$")
    public void theFileMustHaveTheFollowingContent(final String filename, final String expectedContent)
            throws IOException {
        final String actualContent = new String(readAllBytes(get(filename)), "UTF-8");
        assertEquals(expectedContent, actualContent);
    }
}
