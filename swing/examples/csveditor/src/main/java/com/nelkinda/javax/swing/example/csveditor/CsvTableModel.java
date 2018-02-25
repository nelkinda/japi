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

package com.nelkinda.javax.swing.example.csveditor;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class CsvTableModel implements TableModel {
    /**
     * List of listeners.
     */
    private final EventListenerList listenerList = new EventListenerList();

    /**
     * The number of columns currently present in this model.
     */
    private int columnCount;

    /**
     * The number of rows currently present in this model.
     */
    private int rowCount;

    /**
     * The names of the columns.
     */
    private final List<String> columnNames = new ArrayList<>();

    /** The rows of columns.
     * The expectation is that adding rows is a more frequent operation than adding columns.
     * Therefore this model is storing rows of columns, not columns of rows.
     */
    private final List<List<String>> values = new ArrayList<>();

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public String getColumnName(final int columnIndex) {
        return columnNames.get(columnIndex);
    }

    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        // TODO
        return false;
    }

    @Override
    public String getValueAt(final int rowIndex, final int columnIndex) {
        return values.get(rowIndex).get(columnIndex);
    }

    @Override
    public void setValueAt(@NotNull final Object aValue, final int rowIndex, final int columnIndex) {
        setValueAt(aValue.toString(), rowIndex, columnIndex);
    }

    public void setValueAt(@NotNull final String value, final int rowIndex, final int columnIndex) {
        values.get(rowIndex).set(columnIndex, value);
    }

    @Override
    public final void addTableModelListener(@NotNull final TableModelListener l) {
        listenerList.add(TableModelListener.class, l);
    }

    @Override
    public final void removeTableModelListener(@NotNull final TableModelListener l) {
        listenerList.remove(TableModelListener.class, l);
    }

    public final TableModelListener[] getTableModelListeners() {
        return listenerList.getListeners(TableModelListener.class);
    }

    /**
     * Inserts a new column named {@code columnName} before the column with index {@code columnIndex}.
     *
     * @param columnIndex Index of the column before which to insert a new column.
     * @param columnName  Name of the column to insert.
     * @since 0.0.3
     */
    public void columnInsertBefore(final int columnIndex, final String columnName) {
        columnInsertAfter(columnIndex - 1, columnName);
    }

    /**
     * Inserts a new column named {@code columnName} after the column with index {@code columnIndex}.
     *
     * @param columnIndex Index of the column after which to insert a new column.
     * @param columnName  Name of the column to insert.
     */
    public void columnInsertAfter(final int columnIndex, final String columnName) {
        columnCount++;
        columnNames.add(columnIndex + 1, columnName);
        for (final List<String> row : values)
            row.add(columnIndex + 1, "");
    }

    /**
     * Inserts a new row before the row with index {@code rowIndex}.
     *
     * @param rowIndex Index of the row after which to insert a new row.
     * @since 0.0.3
     */
    public void rowInsertBefore(final int rowIndex) {
        rowInsertAfter(rowIndex - 1);
    }

    /**
     * Inserts a new row after the row with index {@code rowIndex}.
     *
     * @param rowIndex Index of the row after which to insert a new row.
     * @since 0.0.3
     */
    public void rowInsertAfter(final int rowIndex) {
        rowCount++;
        final List<String> row = new ArrayList<>();
        for (int i = 0; i < columnCount; i++)
            row.add("");
        values.add(rowIndex + 1, row);
    }

    /**
     * Notifies all listeners that the table's structure has changed.
     * The number of columns in the table, and the names and types of the new columns may be different from the previous state.
     * If the {@code JTable} receives this event and its {@code autoCreateColumnsFromModel} flag is set it discards any table columns that it had and reallocates default columns in the order they appear in the model.
     * This is the same as calling {@code setModel(TableModel)} on the {@code JTable}.
     *
     * @see TableModelEvent
     * @see EventListenerList
     */
    public void fireTableStructureChanged() {
        fireTableChanged(new TableModelEvent(this, TableModelEvent.HEADER_ROW));
    }

    /**
     * Forwards the given notification event to all
     * {@code TableModelListeners} that registered
     * themselves as listeners for this table model.
     *
     * @param e  the event to be forwarded
     *
     * @see #addTableModelListener
     * @see TableModelEvent
     * @see EventListenerList
     */
    private void fireTableChanged(final TableModelEvent e) {
        final Object[] listeners = listenerList.getListenerList();
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == TableModelListener.class) {
                ((TableModelListener) listeners[i + 1]).tableChanged(e);
            }
        }
    }
}
