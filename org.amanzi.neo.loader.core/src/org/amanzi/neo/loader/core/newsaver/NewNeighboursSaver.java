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

package org.amanzi.neo.loader.core.newsaver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.amanzi.neo.loader.core.ConfigurationDataImpl;
import org.amanzi.neo.services.INeoConstants;
import org.amanzi.neo.services.NewNetworkService.NetworkElementNodeType;
import org.amanzi.neo.services.exceptions.AWEException;
import org.amanzi.neo.services.exceptions.DatabaseException;
import org.amanzi.neo.services.model.IDataElement;
import org.amanzi.neo.services.model.INetworkModel;
import org.amanzi.neo.services.model.INodeToNodeRelationsModel;
import org.amanzi.neo.services.model.impl.NodeToNodeRelationshipModel.N2NRelTypes;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.GraphDatabaseService;

/**
 * @author Kondratneko_Vladislav
 */
public class NewNeighboursSaver extends AbstractN2NSaver {
    /*
     * neighbours
     */
    public final static String NEIGHBOUR_SECTOR_CI = "neigh_sector_ci";
    public final static String NEIGHBOUR_SECTOR_LAC = "neigh_sector_lac";
    public final static String NEIGHBOUR_SECTOR_NAME = "neigh_sector_name";
    public final static String SERVING_SECTOR_CI = "serv_sector_ci";
    public final static String SERVING_SECTOR_LAC = "serv_sector_lac";
    public final static String SERVING_SECTOR_NAME = "serv_sector_name";

    protected NewNeighboursSaver(INodeToNodeRelationsModel model, INetworkModel networkModel, ConfigurationDataImpl data,
            GraphDatabaseService service) {
        super(model, networkModel, data, service);
    }

    /**
     * 
     */
    public NewNeighboursSaver() {
        super();
    }
    
    /**
     * contains appropriation of header synonyms and name inDB</br> <b>key</b>- name in db ,
     * <b>value</b>-file header key
     */
    private Map<String, String> fileSynonyms = new HashMap<String, String>();
    /**
     * name inDB properties values
     */
    private List<String> headers;
    private static Logger LOGGER = Logger.getLogger(NewNetworkSaver.class);
    private Map<String, String[]> preferenceStoreSynonyms;

    @Override
    protected void initSynonyms() {
        preferenceStoreSynonyms = preferenceManager.getNeighbourSynonyms();
        columnSynonyms = new HashMap<String, Integer>();
    }

    /**
     * try create a neighbour relationship between sectors
     * 
     * @param value
     * @throws DatabaseException
     */
    @Override
    protected void saveLine(List<String> row) throws AWEException {
        String neighbSectorName = getValueFromRow(NEIGHBOUR_SECTOR_NAME, row);
        String serviceNeighName = getValueFromRow(SERVING_SECTOR_NAME, row);
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(INeoConstants.PROPERTY_TYPE_NAME, NetworkElementNodeType.SECTOR.getId());
        properties.put(INeoConstants.PROPERTY_NAME_NAME, neighbSectorName);
        IDataElement findedNeighSector = networkModel.findElement(properties);
        properties.put(INeoConstants.PROPERTY_NAME_NAME, serviceNeighName);
        IDataElement findedServiceSector = networkModel.findElement(properties);
        for (String head : headers) {
            if (fileSynonyms.containsValue(head) && !fileSynonyms.get(NEIGHBOUR_SECTOR_NAME).equals(head)
                    && !fileSynonyms.get(SERVING_SECTOR_NAME).equals(head)) {
                properties.put(head.toLowerCase(), getSynonymValueWithAutoparse(head, row));
            }
        }
        if (findedNeighSector != null && findedServiceSector != null) {
            n2nModel.linkNode(findedServiceSector, findedNeighSector, properties);
        } else {
            LOGGER.warn("cann't find service or neighbour sector on line " + lineCounter);
        }
    }

    @Override
    protected INodeToNodeRelationsModel getNode2NodeModel(String name) throws AWEException {
        return networkModel.getNodeToNodeModel(N2NRelTypes.NEIGHBOUR, name, NetworkElementNodeType.SECTOR);
    }

}
