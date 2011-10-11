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

package org.amanzi.neo.loader.core.preferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.amanzi.neo.loader.core.preferences.DataLoadPreferences;
import org.amanzi.neo.loader.core.preferences.PreferenceStore;
import org.amanzi.neo.services.INeoConstants;
import org.amanzi.neo.services.NewDatasetService.DatasetTypes;

/**
 * @author Kondratenko_Vladislav
 */
public class DataLoadPreferenceManager {
    private static DataLoadPreferenceInitializer preferenceInitializer;
    public final static String CITY = "city";
    public final static String BSC = "bsc";
    public final static String MSC = "msc";
    public final static String SECTOR = "sector";
    public final static String SITE = "site";
    public final static String AZIMUTH = "azimuth";
    public final static String BEAMWITH = "beamwidth";
    private static Map<String, String[]> currentSynonyms;
    private static Map<String, String[]> networkMap;

    public static void intializeDefault() {
        if (preferenceInitializer == null) {
            preferenceInitializer = new DataLoadPreferenceInitializer();
        }
        preferenceInitializer.initializeDefaultPreferences();
    }

    public DataLoadPreferenceManager() {
        intializeDefault();
    }

    private String[] getPossibleHeaders(String key) {
        String text = PreferenceStore.getPreferenceStore().getValue(key);
        if (text == null) {
            return new String[0];
        }
        String[] array = text.split(",");
        List<String> result = new ArrayList<String>();
        for (String string : array) {
            String value = string.trim();
            if (!value.isEmpty()) {
                result.add(value);
            }
        }
        return result.toArray(new String[0]);
    }

    public void updateSynonyms(DatasetTypes type, Map<String, String[]> newSynonyms) {
        switch (type) {
        case NETWORK:
            networkMap = getNetworkPosibleValues();
            updateSynonyms(networkMap, newSynonyms);
            break;
        case DRIVE:
        case COUNTERS:
        }
    }

    public void removeSynonym(DatasetTypes type, String key) {
        switch (type) {
        case NETWORK:
            networkMap = getNetworkPosibleValues();
            removeSynonym(networkMap, key);
            break;
        case DRIVE:
        case COUNTERS:
        }
    }

    /**
     * @param networkPosibleValues
     * @param newSynonyms
     */
    private void updateSynonyms(Map<String, String[]> networkPosibleValues, Map<String, String[]> newSynonyms) {
        for (String newKey : newSynonyms.keySet()) {
            PreferenceStore.getPreferenceStore().setProperty(newKey, newSynonyms.get(newKey));
            networkMap.put(newKey, newSynonyms.get(newKey));
        }
    }

    public Map<String, String[]> getSynonyms(DatasetTypes type) {
        switch (type) {
        case NETWORK:
            currentSynonyms = getNetworkPosibleValues();
        case DRIVE:
        case COUNTERS:
            break;
        }
        return currentSynonyms;
    }

    private void removeSynonym(Map<String, String[]> synonymsMap, String key) {
        synonymsMap.remove(key);
        PreferenceStore.getPreferenceStore().remove(key);
    }

    private Map<String, String[]> getNetworkPosibleValues() {
        if (networkMap == null) {
            networkMap = new HashMap<String, String[]>();
        }
        if (networkMap.isEmpty()) {
            networkMap.put(CITY, getPossibleHeaders(DataLoadPreferences.NH_CITY));
            networkMap.put(MSC, getPossibleHeaders(DataLoadPreferences.NH_MSC));
            networkMap.put(BSC, getPossibleHeaders(DataLoadPreferences.NH_BSC));
            networkMap.put(SITE, getPossibleHeaders(DataLoadPreferences.NH_SITE));
            networkMap.put(SECTOR, getPossibleHeaders(DataLoadPreferences.NH_SECTOR));
            networkMap.put(AZIMUTH, getPossibleHeaders(DataLoadPreferences.NH_AZIMUTH));
            networkMap.put(BEAMWITH, getPossibleHeaders(DataLoadPreferences.NH_BEAMWIDTH));
            networkMap.put(INeoConstants.PROPERTY_SECTOR_CI, getPossibleHeaders(DataLoadPreferences.NH_SECTOR_CI));
            networkMap.put(INeoConstants.PROPERTY_SECTOR_LAC, getPossibleHeaders(DataLoadPreferences.NH_SECTOR_LAC));
            networkMap.put(INeoConstants.PROPERTY_LAT_NAME, getPossibleHeaders(DataLoadPreferences.NH_LATITUDE));
            networkMap.put(INeoConstants.PROPERTY_LON_NAME, getPossibleHeaders(DataLoadPreferences.NH_LONGITUDE));
            networkMap.put(DataLoadPreferences.MO, getPossibleHeaders(DataLoadPreferences.MO));
            networkMap.put(SITE, getPossibleHeaders(DataLoadPreferences.NH_SITE));
            networkMap.put(SECTOR, getPossibleHeaders(DataLoadPreferences.NH_SECTOR));
            networkMap.put(DataLoadPreferences.CHGR, getPossibleHeaders(DataLoadPreferences.CHGR));
            networkMap.put(DataLoadPreferences.FHOP, getPossibleHeaders(DataLoadPreferences.FHOP));
        }
        return networkMap;
    }
}
