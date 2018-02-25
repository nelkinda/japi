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

package com.nelkinda.javax.swing.event;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * An abstract adapter for receiving {@link WindowEvent}s.
 * The methods in this adapter are empty.
 * This adapter exists as convenience for creating {@link WindowListener} objects.
 *
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
 * @version 0.0.2
 * @since 0.0.2
 */
public interface WindowAdapter extends WindowListener {
    @Override
    default void windowOpened(final WindowEvent e) {
    }

    @Override
    default void windowClosing(final WindowEvent e) {
    }

    @Override
    default void windowClosed(final WindowEvent e) {
    }

    @Override
    default void windowIconified(final WindowEvent e) {
    }

    @Override
    default void windowDeiconified(final WindowEvent e) {
    }

    @Override
    default void windowActivated(final WindowEvent e) {
    }

    @Override
    default void windowDeactivated(final WindowEvent e) {
    }
}
