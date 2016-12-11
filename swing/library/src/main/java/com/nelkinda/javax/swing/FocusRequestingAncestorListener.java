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

import com.nelkinda.javax.swing.event.AncestorAdapter;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import org.jetbrains.annotations.NotNull;

/**
 * {@link AncestorListener} which requests the focus when an ancestor is added.
 * Useful to gain the focus when there is no control over the direct container hierarchy, like in {@link JOptionPane}.
 * <p>
 * This implementation of an AncestorAdapter doesn't hold any state.
 * Use {@link #INSTANCE} to access its Singleton instance.
 *
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
 * @version 0.0.2
 * @since 0.0.2
 */
enum FocusRequestingAncestorListener implements AncestorAdapter {

    /**
     * The singleton instance.
     */
    INSTANCE;

    @Override
    public void ancestorAdded(@NotNull final AncestorEvent event) {
        final JComponent component = event.getComponent();
        component.requestFocusInWindow();
        component.removeAncestorListener(this);
    }
}
