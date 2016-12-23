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

package com.nelkinda.javax.swing.example.csveditor;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.junit.Test;

import static com.nelkinda.javax.swing.example.csveditor.ArrayContainsMatcher.contains;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * Unit Test for {@link CsvTableModel}.
 *
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
 * @version 0.0.2
 * @since 0.0.2
 */
public class CsvTableModelTest {

    @Test
    public void emptyTableModel() {
        final TableModel tableModel = new CsvTableModel();
        assertThat(tableModel.getColumnCount(), is(0));
        assertThat(tableModel.getRowCount(), is(0));
    }

    @Test
    public void registersEventListeners() {
        final TableModelListener tableModelListener = e -> {
        };
        final TableModelListener tableModelListener2 = org.mockito.Mockito.mock(TableModelListener.class);
        final CsvTableModel tableModel = new CsvTableModel();
        tableModel.addTableModelListener(tableModelListener);
        assertThat(tableModel.getTableModelListeners(), contains(tableModelListener));
        tableModel.addTableModelListener(tableModelListener2);
        assertThat(tableModel.getTableModelListeners(), contains(tableModelListener));
        assertThat(tableModel.getTableModelListeners(), contains(tableModelListener2));
        tableModel.removeTableModelListener(tableModelListener);
        assertThat(tableModel.getTableModelListeners(), contains(tableModelListener2));
        assertThat(tableModel.getTableModelListeners(), not(contains(tableModelListener)));
        tableModel.removeTableModelListener(tableModelListener2);
        assertThat(tableModel.getTableModelListeners(), not(contains(tableModelListener)));
        assertThat(tableModel.getTableModelListeners(), not(contains(tableModelListener2)));
    }
}