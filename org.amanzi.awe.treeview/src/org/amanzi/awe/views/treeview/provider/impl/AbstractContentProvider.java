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

package org.amanzi.awe.views.treeview.provider.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.amanzi.awe.views.treeview.provider.ITreeItem;
import org.amanzi.neo.dto.IDataElement;
import org.amanzi.neo.models.IModel;
import org.amanzi.neo.models.exceptions.ModelException;
import org.amanzi.neo.nodeproperties.IGeneralNodeProperties;
import org.amanzi.neo.providers.INetworkModelProvider;
import org.amanzi.neo.providers.IProjectModelProvider;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * <p>
 * common content provider
 * </p>
 * 
 * @author Vladislav_Kondratenko
 * @since 1.0.0
 */
public abstract class AbstractContentProvider<T extends IModel> implements ITreeContentProvider {

    private static final Logger LOGGER = Logger.getLogger(AbstractContentProvider.class);

    private final IGeneralNodeProperties generalNodeProperties;
    private static final DataElementComparator DATA_ELEMENT_COMPARER = new DataElementComparator();
    private Iterable<IDataElement> children = null;

    /**
     * @param networkModelProvider
     * @param projectModelProvider
     */
    protected AbstractContentProvider(INetworkModelProvider networkModelProvider, IProjectModelProvider projectModelProvider,
            IGeneralNodeProperties generalNodeProperties) {
        super();
        this.networkModelProvider = networkModelProvider;
        this.projectModelProvider = projectModelProvider;
        this.generalNodeProperties = generalNodeProperties;
    }

    /**
     * <p>
     * Comparator for treeElements
     * </p>
     * 
     * @author Kondratenko_Vladislav
     * @since 1.0.0
     */
    @SuppressWarnings("rawtypes")
    protected static class DataElementComparator implements Comparator<ITreeItem> {
        @Override
        public int compare(ITreeItem dataElement1, ITreeItem dataElement2) {
            return dataElement1.getDataElement() == null ? -1 : dataElement2.getDataElement() == null ? 1 : dataElement1
                    .getDataElement().getName().compareTo(dataElement2.getDataElement().getName());
        }
    }

    private final INetworkModelProvider networkModelProvider;
    private final IProjectModelProvider projectModelProvider;

    private List<ITreeItem<T>> rootList = new ArrayList<ITreeItem<T>>();

    @Override
    public void inputChanged(final Viewer viewer, final Object oldInput, final Object newInput) {
    }

    @Override
    public void dispose() {
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object[] getChildren(Object parentElement) {
        ITreeItem<T> item = (ITreeItem<T>)parentElement;
        try {
            if (isInRootList(item)) {
                handleRoot(item);

            } else {
                handleInnerElements(item);
            }
        } catch (ModelException e) {
            LOGGER.error("can't get child for parentElement " + parentElement, e);
            return null;
        }
        return processReturment(item.getParent());
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean hasChildren(Object element) {
        Object[] children = null;
        try {
            ITreeItem<T> item = (ITreeItem<T>)element;
            children = getChildren(item);
            return !ArrayUtils.isEmpty(children) && additionalCheckChild(element);
        } catch (ModelException e) {
            LOGGER.error("exception when trying to get child", e);
        }
        return false;
    }

    /**
     * @param element
     * @return
     * @throws ModelException
     */
    protected abstract boolean additionalCheckChild(Object element) throws ModelException;

    /**
     * @param t
     * @return
     */
    protected Object[] processReturment(T model) {
        List<ITreeItem<T>> dataElements = new ArrayList<ITreeItem<T>>();
        for (IDataElement dataElement : children) {
            ITreeItem<T> item = new TreeViewItem<T>(model, dataElement);
            dataElements.add(item);
        }
        Collections.sort(dataElements, getDataElementComparer());
        List<Object> res = new ArrayList<Object>(dataElements);
        return res.toArray();
    }

    /**
     * handle inner elements
     * 
     * @param parentElement
     * @throws ModelException
     */
    protected abstract void handleInnerElements(ITreeItem<T> parentElement) throws ModelException;

    @Override
    public Object[] getElements(Object inputElement) {
        rootList.clear();
        List<T> roots = null;
        try {
            roots = getRootElements();
            for (T root : roots) {
                rootList.add(new TreeViewItem<T>(root, root.asDataElement()));
            }
        } catch (ModelException e) {
            LOGGER.error("can't get roots", e);
        }

        return rootList.toArray();
    }

    /**
     * get root elements
     * 
     * @return
     * @throws ModelException
     */
    protected abstract List<T> getRootElements() throws ModelException;

    /**
     * check if object in rootList
     * 
     * @param object
     * @return
     */
    private boolean isInRootList(Object object) {
        return rootList.contains(object);
    }

    /**
     * handle get roots element child
     */
    protected abstract void handleRoot(ITreeItem<T> item) throws ModelException;

    /**
     * @return Returns the generalNodeProperties.
     */
    protected IGeneralNodeProperties getGeneralNodeProperties() {
        return generalNodeProperties;
    }

    /**
     * @return Returns the rootList.
     */
    protected List<ITreeItem<T>> getRootList() {
        return rootList;
    }

    /**
     * @return Returns the networkModelProvider.
     */
    protected INetworkModelProvider getNetworkModelProvider() {
        return networkModelProvider;
    }

    /**
     * @return Returns the projectModelProvider.
     */
    protected IProjectModelProvider getProjectModelProvider() {
        return projectModelProvider;
    }

    /**
     * @return Returns the DATA_ELEMENT_COMPARATOR.
     */
    public DataElementComparator getDataElementComparer() {
        return DATA_ELEMENT_COMPARER;
    };

    /**
     * @param children
     */
    protected void setChildren(Iterable<IDataElement> children) {
        this.children = children;
    }
}