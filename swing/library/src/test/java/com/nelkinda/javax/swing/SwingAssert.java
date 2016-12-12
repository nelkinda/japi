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

import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static com.nelkinda.javax.swing.SwingUtilitiesN.callAndWait;
import static javax.swing.SwingUtilities.invokeAndWait;
import static org.junit.Assert.assertFalse;

public enum SwingAssert {
    ; // NOSONAR Bug in SonarQube: https://jira.sonarsource.com/browse/SONARJAVA-1909

    public static void assertHasFocus(final Component component) throws InterruptedException, InvocationTargetException, ExecutionException {
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
        final Callable<Boolean> hasFocus = component::hasFocus;
        if (!callAndWait(hasFocus)) {
            wait(monitor);
        }
        invokeAndWait(() -> component.removeFocusListener(focusAdapter));
    }

    public static void assertNotHasFocus(final Component component) throws InterruptedException, InvocationTargetException, ExecutionException {
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
        final Callable<Boolean> hasFocus = component::hasFocus;
        if (callAndWait(hasFocus)) {
            wait(monitor);
        }
        invokeAndWait(() -> component.removeFocusListener(focusAdapter));
        assertFalse(callAndWait(hasFocus));
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    private static void wait(final Object monitor) {
        synchronized (monitor) {
            try {
                monitor.wait();
            } catch (final InterruptedException ignored) {
            }
        }
    }
}