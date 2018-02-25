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

import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * An abstract adapter for receiving {@link AncestorEvent}s.
 * The methods in this adapter are empty.
 * This adapter exists as convenience for creating {@link AncestorListener} objects.
 *
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
 * @version 0.0.2
 * @since 0.0.2
 */
public interface AncestorAdapter extends AncestorListener {
    @Override
    default void ancestorAdded(final AncestorEvent event) {
    }

    @Override
    default void ancestorRemoved(final AncestorEvent event) {
    }

    @Override
    default void ancestorMoved(final AncestorEvent event) {
    }
}
