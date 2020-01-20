/*
 * Copyright © 2016 - 2020 Nelkinda Software Craft Pvt Ltd.
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

package com.nelkinda.javax.swing.filechooser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FileChooserUI;
import javax.swing.plaf.basic.BasicFileChooserUI;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class FileSuffixUpdater implements PropertyChangeListener {
    private static String replaceSuffix(final String oldFilename, final String suffix) {
        final int dotIndex = oldFilename.lastIndexOf('.');
        final String basename = dotIndex >= 0 ? oldFilename.substring(0, dotIndex) : oldFilename;
        return basename + suffix;
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        final Object source = evt.getSource();
        if (!(source instanceof JFileChooser)) {
            return;
        }
        final JFileChooser fileChooser = (JFileChooser) source;
        final Object newValue = evt.getNewValue();
        if (newValue instanceof FileNameExtensionFilter) {
            final FileNameExtensionFilter fileFilter = (FileNameExtensionFilter) newValue;
            final FileChooserUI fileChooserUI = fileChooser.getUI();
            if (fileChooserUI instanceof BasicFileChooserUI) {
                final BasicFileChooserUI basicFileChooserUI = (BasicFileChooserUI) fileChooserUI;
                final String oldFilename = basicFileChooserUI.getFileName();
                final String newFilename = replaceSuffix(oldFilename, "." + fileFilter.getExtensions()[0]);
                basicFileChooserUI.setFileName(newFilename);
            }
        }
    }
}
