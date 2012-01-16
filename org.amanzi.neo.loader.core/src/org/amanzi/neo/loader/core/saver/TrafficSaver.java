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

import java.util.Map;

import org.amanzi.neo.loader.core.config.NetworkConfiguration;
import org.amanzi.neo.loader.core.parser.MappedData;
import org.amanzi.neo.services.exceptions.AWEException;
import org.amanzi.neo.services.model.IDataElement;
import org.amanzi.neo.services.model.INetworkModel;

/**
 * 
 * TODO Purpose of 
 * <p>
 *  Saver for traffic data
 * </p>
 * @author Ladornaya_A
 * @since 1.0.0
 */
public class TrafficSaver extends AbstractNetworkSaver<INetworkModel, NetworkConfiguration> {

    /*
     * Name of Dataset Synonyms for traffic
     */
    private static final String SYNONYMS_DATASET_TYPE = "traffic";

    @SuppressWarnings("unchecked")
    @Override
    public void saveElement(MappedData dataElement) throws AWEException {
        Map<String, Object> values = getDataElementProperties(getMainModel(), null, dataElement, true);

        IDataElement trafficElement = getNetworkElement(getSectorNodeType(), "sector_name", values);
        
        Map<String,Object> newPropertyMap = (Map<String, Object>)values.get("traffic");

        getMainModel().completeProperties(trafficElement, newPropertyMap, true);
    }

    @Override
    protected boolean isRenderable() {
        return false;
    }

    @Override
    protected INetworkModel createMainModel(NetworkConfiguration configuration) throws AWEException {
        return getActiveProject().getNetwork(configuration.getDatasetName());      
    }

    @Override
    protected String getDatasetType() {
        return SYNONYMS_DATASET_TYPE;
    }

    @Override
    protected String getSubType() {
        return null;
    }
}

