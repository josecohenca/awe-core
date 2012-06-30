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
package org.amanzi.neo.services;

/**
 * Constans for AWE specific Neo-Database properties
 * 
 * @author Lagutko_N
 * @since 1.0.0
 */

public interface INeoConstants {

    /***********************************************************
     * Properties used by the org.amanzi.neo.loader clases and the data structures in neo4j that are
     * built by them and used by various elements in other plugins.
     */
    // Node name property
    public static final String PROPERTY_NAME_NAME = "name";
    // Node type property
    public static final String PROPERTY_TYPE_NAME = "type";
    // timestamp property
    public static final String PROPERTY_TIMESTAMP_NAME = "timestamp";
    // location
    // latitude
    public static final String PROPERTY_LAT_NAME = "lat";
    // longitude
    public static final String PROPERTY_LON_NAME = "lon";
    public static final String PROPERTY_DOMAIN_NAME = "domain";
    public static final String PROPERTY_SECTOR_LAC = "lac";
    public static final String PROPERTY_SECTOR_CI = "ci";
    public static final String PROPERTY_SECTOR_BSIC = "bsic";
    public static final String PROPERTY_SECTOR_ARFCN = "arfcn";
    public static final String PROPERTY_MAL = "mal";
    public static final String PROPERTY_SECTOR_NAME = "name";
    public static final String PROPERTY_GIS_TYPE_NAME = "gis_type";
    public static final String PROPERTY_FILENAME_NAME = "filename";
    public static final String PROPERTY_MW_NAME = "mw";
    public static final String PROPERTY_DBM_NAME = "dbm";
    public static final String PROPERTY_CODE_NAME = "code";
    public static final String PRPOPERTY_CHANNEL_NAME = "channel";
    public static final String PROPERTY_LAST_LINE_NAME = "last_line";
    public static final String PROPERTY_FIRST_LINE_NAME = "first_line";
    public static final String PROPERTY_TIME_NAME = "timestamp";
    public static final String PROPERTY_PARAMS_NAME = "event_parameters";
    public static final String HEADER_MS = "ms";
    public static final String PROPERTY_BBOX_NAME = "bbox";
    public static final String PROPERTY_CRS_NAME = "crs";
    public static final String PROPERTY_CRS_HREF_NAME = "crs_href";
    public static final String PROPERTY_CRS_TYPE_NAME = "crs_type";
    public static final String PROPERTY_WKT_CRS = "crs_wkt";
    public static final String PROPERTY_PROJECT_NAME = "project";
    public static final String PROPERTY_DESCRIPTION_NAME = "description";
    public static final String PROPERTY_DATA = "data_properties";
    public static final String PROPERTY_STATS = "stats_properties";
    public static final String EVENT_ID = "event_id";
    public static final String EVENT_CONTEXT_ID = "context_id";
    public static final String PROPERTY_FLAGGED_NAME = "flagged";
    public static final String PROPERTY_CANCELED_NAME = "canceled";
    // dataset property
    public static final String DRIVE_TYPE = "drive_type";
    public static final String PROPERTY_NAME_SELECTED_PROPERTIES = "selected_propertis";
    /*
     * Type Splash
     */
    public static final String SPLASH_TYPE_NAME = "splash";

    /******************************************************************
     * Properties used in the Reuse analysis
     */

    public static final String PROPERTY_VALUE_NAME = "value";
    public static final String PROPERTY_DISTRIBUTE_NAME = "distribute";
    public static final String PROPERTY_SELECT_NAME = "select";
    public static final String COUNT_TYPE_NAME = "count";// Constant used not only
                                                                         // as a node type.
    /*
     * Which is the current or most recent selected aggregation for a gis node. Used by the reuse
     * analyser for saving state, and by the star analysis for determining property to use.
     */
    public static final String PROPERTY_SELECTED_AGGREGATION = "selected_aggregation";

    /**
     * Name of min property
     */
    public static final String PROPERTY_NAME_MIN_VALUE = "min";
    /**
     * name of max property
     */
    public static final String PROPERTY_NAME_MAX_VALUE = "max";
    /**
     * name of chart error property
     */
    public static final String PROPERTY_CHART_ERROR_NAME = "error_node";
    public static final String PROPERTY_CHART_ERROR_DESCRIPTION = "error_description";
    /** INeoConstants PROPERTY_ALL_CHANNEL_NAME field */
    public static final String PROPERTY_ALL_CHANNELS_NAME = "All Channels";
    /** INeoConstants PROPERTY_OLD_NAME field */
    public static final String PROPERTY_OLD_NAME = "old_name";
    /** INeoConstants NEIGHBOUR_TYPE_NAME field */
    public static final String NEIGHBOUR_NAME = "neighbours_list";
    /** INeoConstants LIST_NUMERIC_PROPERTIES field */
    public static final String LIST_NUMERIC_PROPERTIES = "list_numeric_field";
    /** INeoConstants LIST_DATA_PROPERTIES field */
    public static final String LIST_DATA_PROPERTIES = "list_data_field";
    /** INeoConstants LIST_ALL_PROPERTIES field */
    public static final String LIST_ALL_PROPERTIES = "list_all_field";
    public static final String NEIGHBOUR_NUMBER = "# neighbours listname";

    /*
     * Constants for DriveLoader data structures
     */
    public static final String NODE_TYPE_PROPERTIES = "properties";
    /** INeoConstants LIST_DOUBLE_PROPERTIES field */
    public static final String LIST_DOUBLE_PROPERTIES = "list_double_field";
    /** INeoConstants LIST_INTEGER_PROPERTIES field */
    public static final String LIST_INTEGER_PROPERTIES = "list_integer_field";
    /** INeoConstants PALETTE_NAME field */
    public static final String PALETTE_NAME = "palette";
    /** INeoConstants AGGREGATION_COLOR field */
    public static final String AGGREGATION_COLOR = "column color";
    /** INeoConstants PROPERTY_AGGR_PARENT_ID field */
    public static final String PROPERTY_AGGR_PARENT_ID = "agr node id";
    /** INeoConstants COLOR_LEFT field */
    public static final String COLOR_LEFT = "color left";
    /** INeoConstants COLOR_RIGHT field */
    public static final String COLOR_RIGHT = "color right";
    public static final String COLOR_MIDDLE = "color middle";
    public static final String MIDDLE_RANGE = "MIDDLE_RANGE";
    public static final String ROOT_SECTOR_DRIVE = "root_sector_drive";// Constant used not only as a node type.
    public static final String PROPERTY_TYPE_EVENT = "event_type";
    public static final String PROPERTY_DRIVE_TYPE_EVENT = "event_drive_type";

    public static final String EVENTS_LUCENE_INDEX_NAME = "events";

    public static final String SECTOR_ID_PROPERTIES = "sector_id";
    public static final String DRIVE_GIS_NAME = "drive gis name";
    public static final String NETWORK_GIS_NAME = "network gis name";
    public static final String MIN_TIMESTAMP = "min_timestamp";
    public static final String MAX_TIMESTAMP = "max_timestamp";
    public static final String MIN_TIMESTAMP_STRING_FORMAT = "min_timestamp_string_format";
    public static final String MAX_TIMESTAMP_STRING_FORMAT = "max_timestamp_string_format";
    public static final String MIN_VALUE = "min value";
    public static final String MAX_VALUE = "max value";

    public static final String COMMAND_PROPERTY_NAME = "command";

    public static final String PROPERTY_SITE_NO = "site_no";
    public static final String GPEH_FILE_VER = "file ver.";
    public static final String GPEH_DAY = "day";
    public static final String GPEH_MONTH = "month";
    public static final String GPEH_SECOND = "second";
    public static final String GPEH_YEAR = "year";
    public static final String GPEH_LOGIC_NAME = "logic_name";
    public static final String GPEH_USER_LABEL = "label";
    public static final String GPEH_MINUTE = "minute";
    public static final String PROPERTY_EVENT_ID = "event id";
    public static final String LAST_CHILD_ID = "last_child_id";
    public static final String URTAN_DATA_TYPE = "utran_data_type";
    public static final String SECTOR_TYPE = "sector_type";

    public static final String M_EVENT_TYPE = "m_type";

    public static final String PRIMARY_TYPE_ID = "primary_type";
    public static final String PROPERTY_IS_INCONCLUSIVE = "is_inconclusive";
    public static final String PROPERTY_INCONCLUSIVE_STATE = "inconclusive_state";
    public static final String PROPERTY_INCONCLUSIVE_CODE = "inconclusive_code";

    public static final String SECTOR_ID_TYPE = "sector_id_type";

    public static final String SECTOR_COUNT = "sector_count";
    public static final String PROPERTY_COUNT_NAME = "count";
    public static final String PROPERTY_BCCH_NAME = "bcch";
    public static final String PROPERTY_HOPPING_TYPE_NAME = "hopping_type";
    public static final String PROPERTY_TCH_NAME = "TCH";
    public static final String PROPERTY_SC_NAME = "SC";
    public static final String PROPERTY_PN_NAME = "PN";
    public static final String PROPERTY_EcIo_NAME = "ECIO";
    public static final String PROPERTY_RSSI_NAME = "RSSI";
    public static final String PROPERTY_CI_NAME = "CI";
    public static final String PROPERTY_LATITUDE_NAME = "latitude";
    public static final String PROPERTY_LONGITUDE_NAME = "longitude";

    public static final String PROPERTY_STRUCTURE_NAME = "structure";
    public static final String PROPERTY_SUMMARY_NAME = "summary";

    public static final String SECTOR_PROPERTY_NAME_PREFIX = "sector.property.name_";
    public static final String SITE_PROPERTY_NAME_PREFIX = "site.property.name_";
    public static final String BSC_PROPERTY_NAME_PREFIX = "bsc.property.name_";
    public static final String PROPERTY_NAME_PREFIX = ".property.name_";
    public static final String CITY_PROPERTY_NAME_PREFIX = "city.property.name_";

    public static final String PROPERTY_TEMPLATE_NAME = "template";

    public static final String PROPERTY_USED_NODES = "used nodes";
    public static final String PROPERTY_TOTAL_NODES = "total nodes";
    public static final String PROPERTY_FIXED_COLOR = "fixed_color";
    public static final String INDEX_REL_MULTY = "multy-aggregate";
    public static final String PROPERTY_MAIO = "maio";

    public static final String PROPERTY_CALL_RESULT = "call_result";
    public static final String PROPERTY_CALL_TYPE = "call_type";
    public static final String PROPERTY_DATASET_ID = "dataset_id";
    public static final String PROPERTY_IS_VALID = "is_valid";
    public static final String PROPERTY_RESELECTION_TIME = "reselection time";
    public static final String PROPERTY_SETUP_DURATION = "setup_duration";
    public static final String PROPERTY_TERMINATION_DURATION = "termination_duration";
    public static final String PROPERTY_AUDIO_DELAY = "Audio delay";
    public static final String PROPERTY_AVERAGE_AUDIO_DELAY = "Average audio delay";
    public static final String PROPERTY_AVERAGE_LISTENING_QUALITY = "Average listening quality";
    public static final String PROPERTY_LISTENING_QUALITY = "Listening quality";
    public static final String PROPERTY_MAXIMUM_DELAY = "Maximum audio delay";
    public static final String PROPERTY_MINIMUM_DELAY = "Minimum audio delay";
    public static final String PROPERTY_MAXIMUM_LISTENING_QUALITY = "Maximum Listening quality";
    public static final String PROPERTY_MINIMUM_LISTENING_QUALITY = "Minimum listening quality";
    public static final String PROPERTY_PHONENUMBER = "phoneNumber";
    public static final String PROPERTY_CALL_DURATION = "call_duration";
    public static final String PROPERTY_HANDOVER_TIME = "handover time";
    public static final String PROPERTY_MESSAGE_ACKNOWLEDGE_TIME = "message acknowledge time";
    public static final String PROPERTY_MESSAGE_RECIEVED_TIME = "message recieved time";
    
    public static final String PROPERTY_TOC_AUDIO_DELAY="TOC Audio Delay";
    public static final String PROPERTY_TTC_AUDIO_DELAY="TTC Audio Delay";
    public static final String PROPERTY_TOC_LISTENING_QUALITY="TOC Listening Quality";
    public static final String PROPERTY_TTC_LISTENING_QUALITY="TTC Listening Quality";
    
    // TODO may be unused 
    public static final String NETWORK_MODEL_NAME = "NetworkModel";
    public static final String DRIVE_MODEL_NAME = "DriveModel";
}