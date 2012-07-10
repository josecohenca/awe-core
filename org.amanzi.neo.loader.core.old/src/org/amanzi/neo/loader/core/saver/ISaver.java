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

package org.amanzi.neo.loader.core.saver;

import org.amanzi.neo.loader.core.config.IConfiguration;
import org.amanzi.neo.loader.core.parser.IData;
import org.amanzi.neo.services.exceptions.AWEException;
import org.amanzi.neo.services.model.IModel;

/**
 * common saver Interface
 * 
 * @author Kondratenko_Vladislav
 */
public interface ISaver<M extends IModel, D extends IData, C extends IConfiguration> {
    /**
     * initialize required saver data;
     * 
     * @param configuration
     * @param dataElement
     */
    public void init(C configuration) throws AWEException;

    /**
     * save dataElement to database;
     * 
     * @param dataElement
     * @throws AWEException
     */
    public boolean save(D dataElement) throws AWEException;
    

    /**
     * common finishing actions
     */
    public void finishUp() throws AWEException;
    
    public void setStartElement(String startElement);
    
    public void setAllElementsFor(String allElementsFor);
}