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

package org.amanzi.neo.data_generator.generate.calls.log_data;

import org.amanzi.neo.data_generator.utils.call.CallConstants;


/**
 * <p>
 * Generate data for SDS messages.
 * </p>
 * @author Shcharbatsevich_A
 * @since 1.0.0
 */
public class SDSDataGenerator extends MessageDataGenerator{
    
    private static final String PAIR_DIRECTORY_POSTFIX = "SDS";
    private static final String[] MESSAGES = new String[]{"Hello","Hello World! This is a SDS","Hello World! This is a full SDS-4 message containing 120 characters of user data. ABCDEFGHIJKLMNOPQRSTUVWXYZ 0123456789"};
    
    
    /**
     * @param aDirectory
     * @param aHours
     * @param aHourDrift
     * @param aCallsPerHour
     * @param aCallPerHourVariance
     * @param aProbes
     */
    public SDSDataGenerator(String aDirectory, Integer aHours, Integer aHourDrift, Integer aCallsPerHour,
            Integer aCallPerHourVariance, Integer aProbes) {
        super(aDirectory, aHours, aHourDrift, aCallsPerHour, aCallPerHourVariance, aProbes);
    }

    @Override
    protected String getTypeKey() {
        return PAIR_DIRECTORY_POSTFIX;
    }

    @Override
    protected Long[] getDurationBorders() {
        return CallConstants.SDS_DURATION_BORDERS;
    }

    @Override
    protected int getMessagesCount() {
        return getRandomGenerator().getIntegerValue(1, 3);
    }

    @Override
    protected Long[] getAcknowledgeBorders() {
        return CallConstants.SDS_ACKNOWLEDGE_BORDERS;
    }

    @Override
    protected Integer getAiService() {
        return CallConstants.SDS_AI_SERVICE;
    }

    @Override
    protected String[] getAllMessages() {
        return MESSAGES;
    }

}
