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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * common loader configuration
 * 
 * @author Kondratenko_Vladislav
 */
public class ConfigurationDataImpl implements IConfiguration {
    /**
     * 
     */
    public ConfigurationDataImpl() {
        super();
    }

    /**
     * selected directory
     */
    private Collection<File> sourceFile;
    /**
     * dataset names collection
     */
    private Map<Object, String> datasetNames;

    /**
     * init source with file or directory path
     */
    public ConfigurationDataImpl(String source) {
        sourceFile = new LinkedList<File>();
        sourceFile.add(new File(source));
    }

    @Override
    public boolean isMultiFile() {
        if (getFilesToLoad().size() > 1) {
            return true;
        }
        return false;
    }

    @Override
    public List<File> getFilesToLoad() {
        List<File> fileList = new LinkedList<File>();
        if (sourceFile != null) {
            for (File file : sourceFile) {
                fileList.addAll(getRootsFiles(file));
            }
        }
        return fileList;
    }

    // private Long extractTimeStampFromFileName(String fileName) {
    // String longString = fileName.substring(fileName.lastIndexOf("#") + 1,
    // fileName.lastIndexOf("."));
    // return Long.parseLong(longString);
    //
    // }

    /**
     * get all files from directory and subdirectories
     * 
     * @param sourceFile2
     * @return
     */
    private List<File> getRootsFiles(File sourceFile2) {
        List<File> filelist = new ArrayList<File>();
        File[] rootList;
        if (sourceFile2.isDirectory()) {
            rootList = sourceFile2.listFiles();
        } else {
            rootList = new File[1];
            rootList[0] = sourceFile2;
        }
        if (rootList != null) {
            for (int i = 0; i < rootList.length; i++) {
                if (rootList[i].isFile()) {
                    filelist.add(rootList[i]);
                } else if (rootList[i].isDirectory()) {
                    getRootsFiles(rootList[i]);
                }
            }
        }
        return filelist;
    }

    @Override
    public Map<Object, String> getDatasetNames() {
        if (datasetNames == null) {
            datasetNames = new HashMap<Object, String>();
        }
        return datasetNames;
    }

    @Override
    public void setSourceFile(Collection<File> sourceFile) {
        this.sourceFile = sourceFile;
    }

    @Override
    public void setDatasetNames(Map<Object, String> datasetNames) {
        this.datasetNames = datasetNames;
    }

}