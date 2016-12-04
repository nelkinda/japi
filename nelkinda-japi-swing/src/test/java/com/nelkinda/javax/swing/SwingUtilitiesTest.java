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
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.LookAndFeel;
import javax.swing.UnsupportedLookAndFeelException;
import org.junit.Test;

import static com.nelkinda.javax.swing.SwingUtilities.callAndWait;
import static com.nelkinda.javax.swing.SwingUtilities.callLater;
import static com.nelkinda.javax.swing.SwingUtilities.getImageIcon;
import static com.nelkinda.javax.swing.SwingUtilities.initActionFromBundle;
import static com.nelkinda.javax.swing.SwingUtilities.setLookAndFeelFromName;
import static javax.swing.Action.ACCELERATOR_KEY;
import static javax.swing.Action.DISPLAYED_MNEMONIC_INDEX_KEY;
import static javax.swing.Action.LARGE_ICON_KEY;
import static javax.swing.Action.LONG_DESCRIPTION;
import static javax.swing.Action.MNEMONIC_KEY;
import static javax.swing.Action.NAME;
import static javax.swing.Action.SHORT_DESCRIPTION;
import static javax.swing.Action.SMALL_ICON;
import static javax.swing.KeyStroke.getKeyStroke;
import static javax.swing.UIManager.getLookAndFeel;
import static javax.swing.UIManager.getSystemLookAndFeelClassName;
import static javax.swing.UIManager.setLookAndFeel;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class SwingUtilitiesTest {
    @Test
    public void testFindNonExistingComponentInContainer() {
        // Given
        final Container parent = new Container();

        // When
        final Component actual = SwingUtilities.findComponent(parent, JButton.class);

        // Then
        assertNull(actual);
    }

    @Test
    public void testFindComponentInContainer() {
        // Given
        final Component expected = new JButton();
        final Container parent = new Container();
        parent.add(expected);

        // When
        final Component actual = SwingUtilities.findComponent(parent, JButton.class);

        // Then
        assertSame(expected, actual);
    }

    @Test
    public void testFindComponentInNestedContainer() {
        // Given
        final Component expected = new JButton();
        final Container parent = new Container();
        final Container nested = new Container();
        parent.add(nested);
        nested.add(expected);

        // When
        final Component actual = SwingUtilities.findComponent(parent, JButton.class);

        // Then
        assertSame(expected, actual);
    }

    @Test
    public void testCallAndWait() throws InterruptedException, ExecutionException, InvocationTargetException {
        final Callable<String> callable = () -> "foo";
        final String actual = callAndWait(callable);
        assertEquals("foo", actual);
    }

    @Test
    public void testCallAndWaitWithException() throws InterruptedException, InvocationTargetException {
        final Callable<String> callable = () -> {
            throw new RuntimeException("foo");
        };
        try {
            callAndWait(callable);
            fail("Expected ExecutionException.");
        } catch (final ExecutionException expected) {
            final Throwable cause = expected.getCause();
            assertTrue(cause instanceof RuntimeException);
            assertEquals("foo", cause.getMessage());
        }
    }

    @Test
    public void testCallLater() throws InterruptedException, ExecutionException {
        // Given
        final BlockingQueue<String> queue1 = new ArrayBlockingQueue<>(1);
        final Callable<String> callable = queue1::take;

        // When
        final Future<String> future = callLater(callable);

        // Then
        assertFalse(future.isDone());

        // When
        queue1.put("foo");

        // Then
        assertEquals("foo", future.get());
        assertTrue(future.isDone());
    }

    @Test
    public void testInitActionFromBundle() {
        final Action action = new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
            }
        };
        final ResourceBundle bundle = ResourceBundle.getBundle(SwingUtilitiesTest.class.getName());
        initActionFromBundle(action, "testAction", bundle);
        assertEquals(getKeyStroke("ctrl pressed T"), action.getValue(ACCELERATOR_KEY));
        assertEquals(5, action.getValue(DISPLAYED_MNEMONIC_INDEX_KEY));
        assertEquals("Test Action", action.getValue(NAME));
        assertEquals((int) 'A', action.getValue(MNEMONIC_KEY));
        assertEquals("A test action for testing.", action.getValue(SHORT_DESCRIPTION));
        assertEquals("This is a test action for testing.", action.getValue(LONG_DESCRIPTION));
        assertNotNull(action.getValue(SMALL_ICON));
        assertEquals(getImageIcon("toolbarButtonGraphics/general/About16.gif"), action.getValue(SMALL_ICON));
        assertNotNull(action.getValue(LARGE_ICON_KEY));
        assertEquals(getImageIcon("toolbarButtonGraphics/general/About24.gif"), action.getValue(LARGE_ICON_KEY));
    }

    @Test
    public void testSetLookAndFeel() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        setLookAndFeel(getSystemLookAndFeelClassName());
        final LookAndFeel systemLookAndFeel = getLookAndFeel();
        setLookAndFeelFromName("Nimbus");
        final LookAndFeel currentLookAndFeel = getLookAndFeel();
        assertNotNull(currentLookAndFeel);
        assertNotEquals(systemLookAndFeel, currentLookAndFeel);
    }
}
