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

import org.amanzi.neo.loader.core.IConfiguration;
import org.amanzi.neo.loader.core.ILoaderProgressListener;
import org.amanzi.neo.loader.core.newsaver.ISaver;
import org.amanzi.neo.services.model.IModel;
import org.apache.log4j.Logger;

//TODO: LN: we don't need this class!!!!!!!
/**
 * common new Network parser
 * 
 * @author Kondratenko_Vladislav
 */
public class NewNetworkParser<T1 extends ISaver<IModel, CSVContainer, T2>, T2 extends IConfiguration>
        extends
            AbstractCSVParser<T1, T2> {
  

    /**
     * initialize looger system
     */
    public NewNetworkParser() {
        LOGGER = Logger.getLogger(NewNetworkParser.class);
    }

}
