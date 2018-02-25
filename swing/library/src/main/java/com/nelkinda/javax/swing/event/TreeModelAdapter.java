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

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;

/**
 * An abstract adapter for receiving {@link TreeModelEvent}s.
 * The methods in this adapter are empty.
 * This adapter exists as convenience for creating {@link TreeModelListener} objects.
 *
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
 * @version 0.0.3
 * @since 0.0.3
 */
public interface TreeModelAdapter extends TreeModelListener {
    @Override
    default void treeNodesChanged(final TreeModelEvent e) {
    }

    @Override
    default void treeNodesInserted(final TreeModelEvent e) {
    }

    @Override
    default void treeNodesRemoved(final TreeModelEvent e) {
    }

    @Override
    default void treeStructureChanged(final TreeModelEvent e) {
    }
}
