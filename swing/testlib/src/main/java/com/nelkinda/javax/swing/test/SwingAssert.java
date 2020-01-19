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

package com.nelkinda.javax.swing.test;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static com.nelkinda.javax.swing.SwingUtilitiesN.callAndWait;
import static java.lang.Thread.currentThread;
import static javax.swing.SwingUtilities.invokeAndWait;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Assertions for testing Swing applications.
 *
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>
 * @version 0.0.2
 * @since 0.0.2
 */
public enum SwingAssert {
    ; // NOSONAR Bug in SonarQube: https://jira.sonarsource.com/browse/SONARJAVA-1909

    /**
     * Asserts that a component has focus.
     *
     * @param component Component of which to assert that it does not have focus.
     * @throws InterruptedException In case executing the focus observation on the event thread was interrupted.
     */
    public static void assertHasFocus(final Component component) throws InterruptedException, InvocationTargetException, ExecutionException {
        final Callable<Boolean> hasFocus = component::isFocusOwner;
        assertFocusCondition(component, hasFocus);
    }

    /**
     * Asserts that a component does not have focus.
     *
     * @param component Component of which to assert that it does not have focus.
     * @throws InterruptedException In case executing the focus observation on the event thread was interrupted.
     */
    public static void assertNotHasFocus(final Component component) throws InterruptedException, InvocationTargetException, ExecutionException {
        final Callable<Boolean> notHasFocus = () -> !component.isFocusOwner();
        assertFocusCondition(component, notHasFocus);
    }

    private static void assertFocusCondition(final Component focusableComponent, final Callable<Boolean> condition) throws InterruptedException, InvocationTargetException, ExecutionException {
        final Object monitor = new Object();
        final FocusAdapter focusAdapter = new FocusAdapter() {
            @Override
            public void focusGained(final FocusEvent e) {
                SwingAssert.notify(monitor);
            }

            @Override
            public void focusLost(final FocusEvent e) {
                SwingAssert.notify(monitor);
            }
        };
        invokeAndWait(() -> focusableComponent.addFocusListener(focusAdapter));
        wait(monitor, condition);
        invokeAndWait(() -> focusableComponent.removeFocusListener(focusAdapter));
        assertTrue(callAndWait(condition));
    }

    /**
     * Check for a condition on the event thread, and if it's not satisfied, wait on the supplied monitor.
     *
     * @param monitor Monitor on which to wait.
     * @param test    Condition to test.
     * @throws InterruptedException In case checking the condition on the event thread was interrupted.
     */
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

    /**
     * Synchronize on the specified monitor and notify it.
     *
     * @param monitor Monitor on which to synchronize and notify.
     */
    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    private static void notify(final Object monitor) {
        synchronized (monitor) {
            monitor.notify();
        }
    }
}
