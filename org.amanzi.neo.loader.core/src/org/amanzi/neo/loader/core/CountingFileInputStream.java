/* AWE - Amanzi Wireless Explorer
 * http://awe.amanzi.org
 * (C) 2008-2009, AmanziTel AB
 *
 * This library is provided under the terms of the Eclipse Public License
 * as described at http://www.eclipse.org/legal/epl-v10.html. Any use,
 * reproduction or distribution of the library constitutes recipient's
 * acceptance of this agreement.
 *
 * This library is distributed WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */
package org.amanzi.neo.loader.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * A class that extends FileInputStream and enabled counting of the bytes read and calculation of the percentage read
 * @author craig
 * @since 1.0.0
 */
public class CountingFileInputStream extends FileInputStream {
    long bytesRead = 0;
    long totalBytes = 0;

    public CountingFileInputStream(File file) throws FileNotFoundException {
        super(file);
        totalBytes = file.length();
    }

    public int read(byte[] b) throws IOException {
        int ans = super.read(b);
        if (ans > 0)
            bytesRead += ans;
        return ans;
    }

    public int read(byte[] b, int off, int len) throws IOException {
        int ans = super.read(b,off,len);
        if (ans > 0)
            bytesRead += ans;
        return ans;
    }

    public long tell() {
        return bytesRead;
    }

    public int percentage() {
        return (int)(100.0 * (float)bytesRead / (float)totalBytes);
    }
}