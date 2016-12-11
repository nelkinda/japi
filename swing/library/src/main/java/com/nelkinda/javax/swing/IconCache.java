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

/**
 * A cache for icons so that they would not be unnecessarily loaded multiple times.
 *
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
 * @version 0.0.2
 * @since 0.0.2
 */
public class IconCache {

    /**
     * The actual cache with the icons.
     * Key: URL string from which to load the icon.
     * This can be a URL which is still to be resolved via the class loader.
     * Value: The actual ImageIcon.
     */
    private final Map<String, ImageIcon> iconCache = new HashMap<>(); // NOSONAR Naming field like class is okay in this case.

    /**
     * Creates an icon cache.
     */
    public IconCache() {
        // Nothing to do
    }

    /**
     * Returns an Icon URL as String.
     *
     * @param urlString Relative URL of the icon.
     * @return Absolute URL of the icon so that it can be referenced from Swing HTML.
     */
    public static String getImageIconUrlAsString(final String urlString) {
        return getResource(urlString).toString();
    }

    /**
     * Resolves the {@code urlString} for a resource using the class loader.
     *
     * @param urlString URL to resolve
     * @return Resolved URL using the class loader.
     */
    private static URL getResource(final String urlString) {
        return IconCache.class.getClassLoader().getResource(urlString);
    }

    /**
     * Gets an Icon from the cache.
     * If the icon is not in the cache already, it is loaded to the cache.
     *
     * @param urlString URL for which to get the icon.
     *                  Relative URLs are resolved using the ClassLoader of IconCache.
     *                  Absolute URLs are not yet supported.
     * @return The icon from the cache.
     */
    public ImageIcon getImageIcon(final String urlString) {
        return iconCache.computeIfAbsent(urlString, u -> Optional
                .of(urlString)
                .map(IconCache::getResource)
                .map(ImageIcon::new)
                .orElse(null)
        );
    }
}
