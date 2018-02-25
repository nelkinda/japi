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

import java.util.Arrays;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

/**
 * Matcher that checks whether an element is present in an array.
 *
 * @param <T> Type of the array and the element.
 * @author <a href="mailto:Christian.Hujer@nelkinda.com">Christian Hujer</a>, Nelkinda Software Craft Pvt Ltd
 * @version 0.0.2
 * @since 0.0.2
 */
public class ArrayContainsMatcher<T> extends BaseMatcher<T[]> {

    /**
     * The element that expected in the array.
     */
    private final T expectedElement;

    /**
     * Creates an ArrayContainsMatcher.
     *
     * @param expectedElement Element that is expected in the array.
     * @see #contains(Object)
     */
    private ArrayContainsMatcher(final T expectedElement) {
        this.expectedElement = expectedElement;
    }

    /**
     * Returns a matcher that will check whether {@code expectedElement} is contained in the matched array.
     *
     * @param expectedElement Element that is expected in the array.
     * @param <T>             Type of the array and the element.
     * @return a matcher that will check whether {@code expectedElement} is contained in the matched array.
     */
    public static <T> ArrayContainsMatcher<T> contains(final T expectedElement) {
        return new ArrayContainsMatcher<>(expectedElement);
    }

    @Override
    public boolean matches(final Object item) {
        return Arrays
                .stream((T[]) item)
                .anyMatch(l -> l == expectedElement);
    }

    @Override
    public void describeTo(final Description description) {
        description.appendText("contains").appendValue(expectedElement);
    }
}
