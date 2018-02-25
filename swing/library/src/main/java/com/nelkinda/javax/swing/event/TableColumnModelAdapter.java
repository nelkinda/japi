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

import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

/**
 * An abstract adapter for receiving {@link TableColumnModelEvent}s, {@link ChangeEvent}s and {@link ListSelectionEvent}s.
 * The methods in this adapter are empty.
 * This adapter exists as convenience for creating {@link TableColumnModelListener} objects.
 *
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
 * @version 0.0.3
 * @since 0.0.3
 */
public interface TableColumnModelAdapter extends TableColumnModelListener {
    @Override
    default void columnAdded(final TableColumnModelEvent e) {
    }

    @Override
    default void columnRemoved(final TableColumnModelEvent e) {
    }

    @Override
    default void columnMoved(final TableColumnModelEvent e) {
    }

    @Override
    default void columnMarginChanged(final ChangeEvent e) {
    }

    @Override
    default void columnSelectionChanged(final ListSelectionEvent e) {
    }
}
