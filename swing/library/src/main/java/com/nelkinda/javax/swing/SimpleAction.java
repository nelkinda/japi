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

package com.nelkinda.javax.swing;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 * An action which delegates to a {@link Runnable}.
 *
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
 * @version 0.0.2
 * @since 0.0.2
 */
public class SimpleAction extends AbstractAction {

    /**
     * The Runnable to which to delegate the action.
     */
    private final SerializableRunnable runnable;

    /**
     * Creates a SimpleAction.
     *
     * @param runnable Runnable to call in case of {@link #actionPerformed(ActionEvent)}.
     */
    public SimpleAction(final SerializableRunnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        runnable.run();
    }
}

