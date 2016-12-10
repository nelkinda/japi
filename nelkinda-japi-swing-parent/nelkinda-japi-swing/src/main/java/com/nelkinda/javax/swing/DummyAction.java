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

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 * Action which does nothing.
 *
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>
 * @version 0.0.2
 * @since 0.0.2
 */
public class DummyAction extends AbstractAction {
    @Override
    public void actionPerformed(final ActionEvent e) {
        // Dummy
    }
}
