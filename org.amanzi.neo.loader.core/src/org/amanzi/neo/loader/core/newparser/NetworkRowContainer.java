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

package org.amanzi.neo.loader.core.newparser;

import java.util.HashMap;
import java.util.Map;

import org.amanzi.neo.loader.core.newsaver.IData;

/**
 * common row container contains temporary collection of network information
 * 
 * @author Kondratenko_Vladislav
 */
public class NetworkRowContainer implements IData {
    /**
     * contain pair: K- string header , V- header value;
     */
    private Map<String, Object> headerMap;
    /**
     * headers name;
     */
    private String[] headers;
    private int MINIMAL_SIZE;

    public NetworkRowContainer(int minimalLength) {
        super();
        headerMap=new HashMap<String, Object>();
        MINIMAL_SIZE = minimalLength;
    }

    /**
     * set necessary headers names;
     * 
     * @param headers
     */
    public void setHeaders(String[] headers) {
        this.headers=headers;
        for (String head : headers) {
            headerMap.put(head, null);
            
        }
    }

    /**
     * set values that matched to header
     * 
     * @param values
     */
    public void setValues(String[] values) {
        if (values.length < MINIMAL_SIZE) {
            return;
        }
        if (values.length < headerMap.size()) {
            return;
        }
        int columnNumber = 0;
        for (Object element : values) {
            headerMap.put(headers[columnNumber], element);
            columnNumber++;
        }
    }

    /**
     * get header and Appropriated row values;
     * 
     * @return Returns the headerMap.
     */
    public Map<String, Object> getHeaderMap() {
        return headerMap;
    }
}
