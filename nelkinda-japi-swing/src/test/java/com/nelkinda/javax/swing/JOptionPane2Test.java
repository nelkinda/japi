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

import java.awt.AWTException;
import java.awt.Robot;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.swing.JPasswordField;
import org.junit.Test;

import static com.nelkinda.javax.swing.JOptionPane2.showPasswordDialog;
import static com.nelkinda.javax.swing.SwingAssert.assertHasFocus;
import static com.nelkinda.javax.swing.SwingAssert.assertNotHasFocus;
import static com.nelkinda.javax.swing.SwingUtilitiesN.callAndWait;
import static com.nelkinda.javax.swing.SwingUtilitiesN.callLater;
import static com.nelkinda.javax.swing.SwingUtilitiesN.findComponent;
import static java.awt.Window.getWindows;
import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_ESCAPE;
import static java.awt.event.KeyEvent.VK_SHIFT;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JOptionPane2Test {
    @Test
    public void testShowPasswordDialogOk() throws InterruptedException, ExecutionException, InvocationTargetException, AWTException {
        Future<char[]> future = callLater(() -> showPasswordDialog(null));
        JPasswordField passwordField = callAndWait(() -> findComponent(JPasswordField.class, getWindows())).orElseThrow(AssertionError::new);
        assertNotNull(passwordField);
        assertHasFocus(passwordField);
        Robot robot = new Robot();
        robot.setAutoWaitForIdle(true);
        robot.keyPress(VK_SHIFT);
        robot.keyPress(VK_A);
        robot.keyRelease(VK_A);
        robot.keyRelease(VK_SHIFT);
        robot.keyPress(VK_ENTER);
        robot.keyRelease(VK_ENTER);
        assertNotHasFocus(passwordField);
        assertArrayEquals("A".toCharArray(), future.get());
    }

    @Test
    public void testShowPasswordDialogCancel() throws InterruptedException, ExecutionException, InvocationTargetException, AWTException {
        Future<char[]> future = callLater(() -> showPasswordDialog(null));
        JPasswordField passwordField = callAndWait(() -> findComponent(JPasswordField.class, getWindows())).orElseThrow(AssertionError::new);
        assertNotNull(passwordField);
        assertHasFocus(passwordField);
        Robot robot = new Robot();
        robot.setAutoWaitForIdle(true);
        robot.keyPress(VK_SHIFT);
        robot.keyPress(VK_A);
        robot.keyRelease(VK_A);
        robot.keyRelease(VK_SHIFT);
        robot.keyPress(VK_ESCAPE);
        robot.keyRelease(VK_ESCAPE);
        assertNotHasFocus(passwordField);
        assertNull(future.get());
    }
}
