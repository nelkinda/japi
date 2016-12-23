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

package com.nelkinda.javax.swing.test;

import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static com.nelkinda.javax.swing.SwingUtilitiesN.callAndWait;
import static java.lang.Thread.currentThread;
import static javax.swing.SwingUtilities.invokeAndWait;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public enum SwingAssert {
    ; // NOSONAR Bug in SonarQube: https://jira.sonarsource.com/browse/SONARJAVA-1909

    public static void assertHasFocus(final Component component) throws InterruptedException, InvocationTargetException, ExecutionException {
        final Callable<Boolean> hasFocus = component::hasFocus;
        final Object monitor = new Object();
        final FocusAdapter focusAdapter = new FocusAdapter() {
            @Override
            public void focusGained(final FocusEvent e) {
                synchronized (monitor) {
                    monitor.notify();
                }
            }
        };
        invokeAndWait(() -> component.addFocusListener(focusAdapter));
        wait(monitor, hasFocus);
        invokeAndWait(() -> component.removeFocusListener(focusAdapter));
        assertTrue(callAndWait(hasFocus));
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    private static void wait(final Object monitor, final Callable<Boolean> test) throws InterruptedException, ExecutionException, InvocationTargetException {
        if (!callAndWait(test)) {
            synchronized (monitor) {
                // Do not check again, checking again on the event thread while having the lock might lead to deadlock here!
                // Also, do not loop, we're not really waiting, just giving a chance to the AWT / Swing EDT.
                try {
                    monitor.wait(100);
                } catch (final InterruptedException ignored) {
                    currentThread().interrupt();
                    fail();
                }
            }
        }
    }

    public static void assertNotHasFocus(final Component component) throws InterruptedException, InvocationTargetException, ExecutionException {
        final Callable<Boolean> notHasFocus = () -> !component.hasFocus();
        final Object monitor = new Object();
        final FocusAdapter focusAdapter = new FocusAdapter() {
            @Override
            public void focusLost(final FocusEvent e) {
                synchronized (monitor) {
                    monitor.notify();
                }
            }
        };
        invokeAndWait(() -> component.addFocusListener(focusAdapter));
        wait(monitor, notHasFocus);
        invokeAndWait(() -> component.removeFocusListener(focusAdapter));
        assertTrue(callAndWait(notHasFocus));
    }

}
