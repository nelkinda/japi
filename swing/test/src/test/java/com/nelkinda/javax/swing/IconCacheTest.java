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

import javax.swing.ImageIcon;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

/**
 * Unit Test for {@link IconCache}.
 *
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
 * @version 0.0.2
 * @since 0.0.2
 */
public class IconCacheTest {

    /**
     * An icon to test loading icons into the cache.
     */
    private static final String ICON_THAT_EXISTS = "toolbarButtonGraphics/general/About16.gif";

    /**
     * An icon that does not exist.
     */
    private static final String ICON_THAT_DOES_NOT_EXIST = "this.does.not.exist";

    /**
     * The {@link IconCache} under test.
     */
    private final IconCache iconCache = new IconCache();

    /**
     * Tests that when getting an icon that does not exist from the cache, the cache returns {@code null}.
     */
    @Test
    public void unavailableImage_returnsNull() {
        assertNull(iconCache.getImageIcon(ICON_THAT_DOES_NOT_EXIST));
    }

    /**
     * Tests that when getting an icon that exists from the cache, the cache returns that icon.
     */
    @Test
    public void emptyCache_loadsImage() {
        assertNotNull(iconCache.getImageIcon(ICON_THAT_EXISTS));
    }

    /**
     * Tests that two reads for the same icon in the cache return the same image.
     */
    @Test
    public void filledCache_returnsSameImage() {
        final ImageIcon imageIcon1 = iconCache.getImageIcon(ICON_THAT_EXISTS);
        final ImageIcon imageIcon2 = iconCache.getImageIcon(ICON_THAT_EXISTS);
        assertSame(imageIcon1, imageIcon2);
    }
}
