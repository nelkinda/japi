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

import javax.swing.ImageIcon;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class IconCacheTest {

    private static final String ICON_ABOUT_SMALL_URL = "toolbarButtonGraphics/general/About16.gif";

    private final IconCache iconCache = new IconCache();

    @Test
    public void unavailableImage_returnsNull() {
        assertNull(iconCache.getImageIcon("this.does.not.exist"));
    }

    @Test
    public void emptyCache_loadsImage() {
        final ImageIcon imageIcon = iconCache.getImageIcon(ICON_ABOUT_SMALL_URL);
        assertNotNull(imageIcon);
    }

    @Test
    public void filledCache_returnsSameImage() {
        final ImageIcon imageIcon1 = iconCache.getImageIcon(ICON_ABOUT_SMALL_URL);
        final ImageIcon imageIcon2 = iconCache.getImageIcon(ICON_ABOUT_SMALL_URL);
        assertSame(imageIcon1, imageIcon2);
    }
}
