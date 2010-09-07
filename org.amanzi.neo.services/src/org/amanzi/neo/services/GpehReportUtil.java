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

import java.util.regex.Pattern;

// TODO: Auto-generated Javadoc
/**
 * <p>
 * Utility methods for working with Gpeh Events
 * </p>
 * .
 * 
 * @author tsinkel_a
 * @since 1.0.0
 */
public class GpehReportUtil {

    /**
     * The Class CellReportsProperties.
     */
    public static class CellReportsProperties {

        /** The Constant PERIOD_ID. */
        public static final String PERIOD_ID = "period_id";
        public static final String RNSP_ARRAY = "rnsp_arr";
        public static final String ECNO_ARRAY = "ecno_arr";
    }

    /**
     * The Class MatrixProperties.
     */
    public static class MatrixProperties {

        /** The Constant DISTANCE. */
        public static final String DISTANCE = "distance";

        /** The Constant DEFINED_NBR. */
        public static final String DEFINED_NBR = "defined_nbr";

        /** The Constant NUM_REPORTS_FOR_BEST_CELL. */
        public static final String NUM_REPORTS_FOR_BEST_CELL = "# of MR for best cell";

        /** The Constant EC_NO_DELTA_PREFIX. */
        public static final String EC_NO_DELTA_PREFIX = "EcNo Delta";

        /** The Constant EC_NO_PREFIX. */
        public static final String EC_NO_PREFIX = "EcNo";

        /** The Constant RSCP_DELTA_PREFIX. */
        public static final String RSCP_DELTA_PREFIX = "RSCP Delta";

        /** The Constant POSITION_PREFIX. */
        public static final String POSITION_PREFIX = "Position";

        /**
         * hide constructor.
         */
        private MatrixProperties() {
        }

        /**
         * Gets the RSCPX_Y property name.
         * 
         * @param prfx the prfx
         * @param ecnodmn the ecnodmn
         * @return the rSCPECNO property name
         */
        public static String getRSCPECNOPropertyName(int prfx, int ecnodmn) {
            return new StringBuilder("RSCP").append(prfx).append("_").append(ecnodmn).toString();
        }
    }

    /** Prefixes for Property Names of Measurement Report values. */
    public static final String GPEH_RRC_MR_BSIC_PREFIX = "MR_BSIC";

    /** The Constant GPEH_RRC_MR_UE_TX_POWER_PREFIX. */
    public static final String GPEH_RRC_MR_UE_TX_POWER_PREFIX = "MR_UE-TX-POWER";

    /** The Constant GPEH_RRC_MR_RSCP_PREFIX. */
    public static final String GPEH_RRC_MR_RSCP_PREFIX = "MR_RSCP";

    /** The Constant GPEH_RRC_MR_ECNO_PREFIX. */
    public static final String GPEH_RRC_MR_ECNO_PREFIX = "MR_ECNO";

    /** The Constant GPEH_RRC_SCRAMBLING_PREFIX. */
    public static final String GPEH_RRC_SCRAMBLING_PREFIX = "MR_SCRAMBLING";

    /** The Constant ERR_VALUE. */
    private static final String ERR_VALUE = "Wrong value: %s";

    /** The Constant MR_TYPE. */
    public static final String MR_TYPE = "mr_type";

    /** The Constant MR_TYPE_INTERF. */
    public static final String MR_TYPE_INTERF = "InterFreq";

    /** The Constant MR_TYPE_IRAT. */
    public static final String MR_TYPE_IRAT = "InterRAT";

    /** The Constant MR_TYPE_INTRAF. */
    public static final String MR_TYPE_INTRAF = "IntraF";

    /** The Constant MR_TYPE_UE_INTERNAL. */
    public static final String MR_TYPE_UE_INTERNAL = "Ue_Internal";

    /** The Constant RNC_ID. */
    public static final String RNC_ID = "rncId";

    /** The Constant PRIMARY_SCR_CODE. */
    public static final String PRIMARY_SCR_CODE = "primaryScramblingCode";

    /** The Constant REPORTS_ID. */
    public static final String REPORTS_ID = "report_id";

    /** The Constant RUBY_PROJECT_NAME. */
    public static final String RUBY_PROJECT_NAME = "gpeh_report";

    public static final String MEAS_LIST = "MEAS_LIST";

    /**
     * hide constructor.
     */
    private GpehReportUtil() {
    }

    /**
     * Checks if is report properties.
     * 
     * @param key the key
     * @return true, if is report properties
     */
    public static boolean isReportProperties(String key) {
        return isTxPowerProperty(key) || isECNOProperty(key) || isRSCPProperty(key);
    }

    /**
     * Checks if is tx power property.
     * 
     * @param key the key
     * @return true, if is tx power property
     */
    public static boolean isTxPowerProperty(String key) {
        return Pattern.matches(GPEH_RRC_MR_UE_TX_POWER_PREFIX + "\\d+", key);
    }

    /**
     * Checks if is eCNO property.
     * 
     * @param key the key
     * @return true, if is eCNO property
     */
    public static boolean isECNOProperty(String key) {
        return Pattern.matches(GPEH_RRC_MR_ECNO_PREFIX + "\\d+", key);
    }

    /**
     * Checks if is rSCP property.
     * 
     * @param key the key
     * @return true, if is rSCP property
     */
    public static boolean isRSCPProperty(String key) {
        return Pattern.matches(GPEH_RRC_MR_RSCP_PREFIX + "\\d+", key);
    }

    /**
     * Gets the eCNO range name.
     * 
     * @param value the value
     * @return the eCNO range name
     */
    private static String getECNORangeName(Object value) {
        return null;
    }

    /**
     * Gets the rSCP range name.
     * 
     * @param value the value
     * @return the rSCP range name
     */
    private static String getRSCPRangeName(Object value) {
        return null;
    }

    /**
     * Gets the table id.
     * 
     * @param pscMain the psc main
     * @param psc2 the psc2
     * @return the table id
     */
    public static String getTableId(String pscMain, String psc2) {
        return pscMain + "@" + psc2;
    }

    /**
     * Gets the matrix lucene index name.
     * 
     * @param networkName the network name
     * @param gpehEventsName the gpeh events name
     * @param type the type
     * @return the matrix lucene index name
     */
    public static String getMatrixLuceneIndexName(String networkName, String gpehEventsName, String type) {
        return getReportId(networkName, gpehEventsName).append("@").append(type).toString();
    }

    /**
     * Gets the report id.
     * 
     * @param networkName the network name
     * @param gpehEventsName the gpeh events name
     * @return the report id
     */
    public static StringBuilder getReportId(String networkName, String gpehEventsName) {
        return new StringBuilder(networkName).append("@").append(gpehEventsName);
    }

//    /**
//     * Find prefix.
//     * 
//     * @param gpehEvent the gpeh event
//     * @param psc the psc
//     * @param service the service
//     * @return the integer
//     */
//    public static Integer findPrefix(Node gpehEvent, String psc, GraphDatabaseService service) {
//        Transaction tx = service.beginTx();
//        try {
//            int i = 0;
//            String psc2;
//            while ((psc2 = (String)gpehEvent.getProperty(GpehReportUtil.GPEH_RRC_SCRAMBLING_PREFIX + (++i), null)) != null) {
//                if (psc.equals(psc2)) {
//                    return i;
//                }
//            }
//            return null;
//        } finally {
//            tx.finish();
//        }
//    }

}