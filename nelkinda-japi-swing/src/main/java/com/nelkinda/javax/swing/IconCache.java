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

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.swing.ImageIcon;

public class IconCache {
    private final Map<String, ImageIcon> iconCache = new HashMap<>(); // NOSONAR Naming field like class is okay in this case.

    public static String getImageIconUrlAsString(final String urlString) {
        return getResource(urlString).toString();
    }

    private static URL getResource(final String urlString) {
        return IconCache.class.getClassLoader().getResource(urlString);
    }

    ImageIcon getImageIcon(final String urlString) {
        return iconCache
                .computeIfAbsent(urlString, u -> Optional
                        .of(urlString)
                        .map(IconCache::getResource)
                        .map(ImageIcon::new)
                        .orElse(null)
                );
    }
}
