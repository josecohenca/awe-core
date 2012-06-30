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

package org.amanzi.neo.services.model;

import java.util.List;

import org.amanzi.neo.services.enums.IDriveType;
import org.amanzi.neo.services.enums.INodeType;
import org.amanzi.neo.services.exceptions.AWEException;
import org.amanzi.neo.services.model.impl.ProjectModel.DistributionItem;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author gerzog
 * @since 1.0.0
 */
public interface IProjectModel extends IModel {

    /**
     * Creates a new drive dataset with the defined <code>name</code> and <code>driveType</code>,
     * attaches it to the current project, and creates a new <code>DriveModel</code>, based on the
     * new dataset.
     * 
     * @param name name of the dataset to create
     * @param driveType drive type of the new dataset
     * @return a <code>DriveModel</code>, based on a new dataset node.
     */
    public abstract IDriveModel createDriveModel(String name, IDriveType driveType);

    /**
     * Creates a <code>DriveModel</code> based on a new dataset, attached to the current project,
     * with the defined <code>name</code> and <code>driveType</code>, and additionally sets drive
     * model's primary node type.
     * 
     * @param name name of the dataset to create
     * @param driveType drive type of the new dataset
     * @param primaryType <code>DriveModel</code> primary type
     * @return a <code>DriveModel</code> object with the defined primary type
     * @throws AWEException
     */
    public abstract IDriveModel createDriveModel(String name, IDriveType driveType, INodeType primaryType) throws AWEException;

    /**
     * Tries to find a dataset node within the current project, with the defined <code>name</code>
     * and <code>driveType</code>. If found, creates a new <code>DriveModel</code>, based on it.
     * 
     * @param name
     * @param driveType
     * @return a <code>DriveModel</code>, based on the found node, or <code>null</code>
     */
    public abstract IDriveModel findDriveModel(String name, IDriveType driveType);

    /**
     * Tries to find a dataset node within the current project, with the defined <code>name</code>
     * and <code>driveType</code>; creates a new dataset node, if nothing found. Creates a new
     * <code>DriveModel</code> object, based on the resulting node.
     * 
     * @param name
     * @param driveType
     * @return a <code>DriveModel</code> object, based on the found or created node with the defined
     *         parameters.
     */
    public abstract IDriveModel getDriveModel(String name, IDriveType driveType);

    /**
     * Tries to find a dataset node within the current project, with the defined <code>name</code>
     * and <code>driveType</code>; creates a new dataset node, if nothing found. Creates a new
     * <code>DriveModel</code> object, based on the resulting node, and sets its primary node type.
     * 
     * @param name
     * @param driveType
     * @param primaryType
     * @throws AWEException
     * @returna <code>DriveModel</code> object with the defined <code>primaryType</code>, based on
     *          the found or created node with the defined parameters.
     */
    public abstract IDriveModel getDrive(String name, IDriveType driveType, INodeType primaryType) throws AWEException;

    /**
     * Creates a new network node with the defined <code>name</code>, attaches it to the current
     * project node, and creates a new <code>NetworkModel</code> object, based on the new network
     * node.
     * 
     * @param name
     * @return a <code>NetworkModel</code>, based on the new network node
     */
    public abstract INetworkModel createNetwork(String name) throws AWEException;

    /**
     * Tries to find a network node with the defined <code>name</code> within the current project.
     * If found, creates a new <code>NetworkModel</code>, based on it.
     * 
     * @param name
     * @return a <code>NetworkModel</code> object, based on the found node, or <code>null</code>
     */
    public abstract INetworkModel findNetwork(String name) throws AWEException;

    /**
     * Tries to find a network node with the defined <code>name</code> within the current project;
     * if not found, creates a new network node. Creates a new <code>NetworkModel</code>, based on
     * the resulting node.
     * 
     * @param name
     * @return a <code>NetworkModel</code>, based on the found or created node.
     */
    public abstract INetworkModel getNetwork(String name) throws AWEException;

    /**
     * TODO: test implementation Looks up for drive and network datasets, included in current
     * project, and build a list of renderable models based on them.
     * 
     * @return a list of renderable models that belong to the current project
     * @throws AWEException if errors occur in database
     */
    public abstract Iterable<IRenderableModel> getAllRenderableModels() throws AWEException;

    /**
     * Looks up for all Networks related to this Project
     * 
     * @return
     * @throws AWEException
     */
    public Iterable<INetworkModel> findAllNetworkModels() throws AWEException;

    /**
     * Looks up for all models related to this Project
     * 
     * @return
     * @throws AWEException
     */
    public Iterable<IPropertyStatisticalModel> findAllModels() throws AWEException;

    /**
     * Collects list of all models that are available for Distribution Analysis
     * 
     * @return
     * @throws AWEException
     */
    public List<DistributionItem> getAllDistributionalModels() throws AWEException;

    /**
     * Looks up for all datasets with required type related to this project
     * 
     * @param type
     * @return
     * @throws AWEException
     */
    public Iterable<IDriveModel> findAllDriveModels() throws AWEException;

    /**
     * create a counters model
     */

    public ICountersModel createCountersModel(String name, ICountersType type, INodeType primaryType) throws AWEException;

    /**
     * create counters model with a default primaryType
     * 
     * @param name
     * @param type
     * @return
     * @throws AWEException
     */
    public ICountersModel createCountersModel(String name, ICountersType type) throws AWEException;

    /**
     * find all countersModel
     */
    public Iterable<ICountersModel> findAllCountersModel() throws AWEException;

    /**
     * try to find required countersModel or create new one if nor exist
     * 
     * @param m
     */
    public ICountersModel getCountersModel(String name, ICountersType countersModel) throws AWEException;

    /**
     * try to find required counter type;
     * 
     * @param name
     * @param driveType
     * @return
     */
    public ICountersModel findCountersModel(String name, ICountersType driveType);
}