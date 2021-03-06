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


import org.junit.jupiter.api.Test;

import javax.swing.event.PopupMenuListener;

public class PopupMenuAdapterTest {
    @Test
    public void providesDefaultsForAllMethods() {
        final PopupMenuListener popupMenuListener = new PopupMenuAdapter() {
        };
        popupMenuListener.popupMenuCanceled(null);
        popupMenuListener.popupMenuWillBecomeInvisible(null);
        popupMenuListener.popupMenuWillBecomeVisible(null);
    }
}
