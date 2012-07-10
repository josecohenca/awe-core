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

package org.amanzi.neo.loader.core.parser;

import java.util.List;

import org.amanzi.neo.loader.core.config.IConfiguration;
import org.amanzi.neo.loader.core.saver.ISaver;
import org.amanzi.neo.services.exceptions.AWEException;
import org.amanzi.neo.services.exceptions.DatabaseException;
import org.amanzi.neo.services.model.IModel;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * common parser interface
 * 
 * @author Kondratenko_Vladislav
 */
public interface IParser<T1 extends ISaver< ? extends IModel, T3, T2>, T2 extends IConfiguration, T3 extends IData> {
    /**
     * initialize required parser data;
     * 
     * @param configuration common configuration data
     * @param saver which saver use for saving data to database
     */
    public void init(T2 configuration, List<T1> saver);

    /**
     * run parser and save parsed files in database. For saving data parser use saver which it was
     * initialize in <B>init</B> method;
     * 
     * @throws AWEException
     */
    public void run(IProgressMonitor monitor) throws DatabaseException, AWEException;
}