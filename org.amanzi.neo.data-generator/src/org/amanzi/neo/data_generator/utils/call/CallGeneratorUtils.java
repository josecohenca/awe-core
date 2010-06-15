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

package org.amanzi.neo.data_generator.utils.call;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.amanzi.neo.data_generator.data.calls.Call;
import org.amanzi.neo.data_generator.data.calls.CallGroup;
import org.amanzi.neo.data_generator.data.calls.CallParameterNames;
import org.amanzi.neo.data_generator.utils.RandomValueGenerator;

/**
 * <p>
 * Util methods for call data generators 
 * </p>
 * @author Shcharbatsevich_A
 * @since 1.0.0
 */
public class CallGeneratorUtils {
       
    public static final int MILLISECONDS = 1000;
    public static final long HOUR = 60*60*MILLISECONDS;
    
    /**
     * Build map of calls duration in all hours.
     *
     * @return HashMap<Integer, List<Long>> (key - hour, value - list of durations).
     */
    public static HashMap<Integer, List<Long>> buildHourMap(int hours, int calls,int callVariance, float[] durationBorders){
        HashMap<Integer, List<Long>> result = new HashMap<Integer, List<Long>>(hours);
        for(int i = 0; i<hours; i++){
            int count = calls + getRandomGenerator().getIntegerValue(-callVariance, callVariance);
            result.put(i, buildDurationMap(count,durationBorders));
        }
        return result;
    }
    
    /**
     * Build map of calls duration in one hour.
     *
     * @param allCountPerHour Integer (call count)
     * @return list of durations
     */
    public static List<Long> buildDurationMap(Integer allCountPerHour, float[] durationBorders){
        List<Long> result = new ArrayList<Long>(allCountPerHour);
        RandomValueGenerator generator = getRandomGenerator();
        for(int i=0; i<allCountPerHour; i++) {
            int period = generator.getIntegerValue(0, CallConstants.CALL_DURATION_PERIODS_COUNT);
            Long[] borders = getPeriodBorders(period,durationBorders);
            Long duration = generator.getLongValue(borders[0], borders[1]);            
            result.add(duration);
        }
        return result;
    }

    private static RandomValueGenerator getRandomGenerator() {
        return RandomValueGenerator.getGenerator();
    }

    /**
     * Returns borders of duration period.
     *
     * @param period Integer (period number)
     * @return Long[]
     */
    public static Long[] getPeriodBorders(Integer period, float[] durationBorders){
        Long start = (long)(durationBorders[period]*MILLISECONDS);
        Long end = (long)(durationBorders[period+1]*MILLISECONDS);
        return new Long[]{start,end};
    }
    
    public static Call createCall(Long startOfHour, Long setupDuration, Integer priority,CallGroup group,float[] audioQualityBorders, int[] audioDelayBorders, Long minCallDuration){
        RandomValueGenerator generator = getRandomGenerator();
        boolean inDistTime = generator.getBooleanValue();
        long callDuration;
        Long minDuration = minCallDuration;
        if(setupDuration>=minDuration){
            callDuration = generator.getLongValue(setupDuration, CallGeneratorUtils.HOUR-setupDuration);
        } else if(inDistTime){
            callDuration = generator.getLongValue(minDuration, CallGeneratorUtils.HOUR-minDuration);
        }else{
            callDuration = generator.getLongValue(setupDuration, minDuration);
        }
        Long start = startOfHour+generator.getLongValue(1L, CallGeneratorUtils.HOUR-callDuration);
        Call call = getEmptyCall(start,priority);
        call.addParameter(CallParameterNames.SETUP_TIME, setupDuration);
        call.addParameter(CallParameterNames.DURATION_TIME, callDuration);
        Integer pesqCount = generator.getIntegerValue(1, 6);
        List<Float> audioQuals = new ArrayList<Float>(pesqCount);
        String probeName = group.getSourceName();
        for(int i=0; i<pesqCount;i++){
            audioQuals.add(generator.getFloatValue(audioQualityBorders[0], audioQualityBorders[1]));
        }
        call.addParameter(CallParameterNames.AUDIO_QUALITY+probeName, audioQuals);
        for(String name : group.getReceiverNames()){
            audioQuals = new ArrayList<Float>(pesqCount);
            for(int i=0; i<pesqCount;i++){
                audioQuals.add(generator.getFloatValue(audioQualityBorders[0], audioQualityBorders[1]));
            }
            call.addParameter(CallParameterNames.AUDIO_QUALITY+name, audioQuals);
        }
        if(audioDelayBorders!=null){
            List<Integer> audioDelays = new ArrayList<Integer>(pesqCount);
            probeName = group.getSourceName();
            for(int i=0; i<pesqCount;i++){
                audioDelays.add(generator.getIntegerValue(audioDelayBorders[0], audioDelayBorders[1]));
            }
            call.addParameter(CallParameterNames.AUDIO_DELAY+probeName, audioDelays);
            for(String name : group.getReceiverNames()){
                audioDelays = new ArrayList<Integer>(pesqCount);
                for(int i=0; i<pesqCount;i++){
                    audioDelays.add(generator.getIntegerValue(audioDelayBorders[0], audioDelayBorders[1]));
                }
                call.addParameter(CallParameterNames.AUDIO_DELAY+name, audioDelays);
            } 
        }
        return call;
    }
    
    public static Call createCallWithHoCc(Long startOfHour, Long setupDuration, Integer priority,CallGroup group,float[] audioQualityBorders, int[] audioDelayBorders, Long minCallDuration){
        Call call = createCall(startOfHour, setupDuration, priority, group, audioQualityBorders, audioDelayBorders, minCallDuration);
        Long start = call.getStartTime();
        Long callDuration = (Long)call.getParameter(CallParameterNames.DURATION_TIME);
        boolean needHo = getRandomGenerator().getBooleanValue();
        if(needHo){
            call.addParameter(CallParameterNames.HO_TIME, getRamdomTime(0L, callDuration-setupDuration));
        }
        boolean needCc = getRandomGenerator().getBooleanValue();
        if(needCc){
            Long endOfHour = startOfHour+HOUR;
            Long end = callDuration+start;
            if(endOfHour<end){
                endOfHour+=HOUR;
            }
            call.addParameter(CallParameterNames.CC_TIME, getRamdomTime(0L, endOfHour-end));
        }
        return call;
    }
    
    /**
     * Create new empty call
     *
     * @param start
     * @return Call.
     */
    public static Call getEmptyCall(Long start, Integer priority){
        return new Call(start, priority);
    }
    
    /**
     * Generate pairs of probe numbers.
     *
     * @param probesCount Integer
     * @return List<List<Integer>>
     */
    public static List<List<Integer>> initCallPairs(Integer probesCount) {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        int sourceCount = probesCount/2+(probesCount%2==0?0:1);
        RandomValueGenerator generator = getRandomGenerator();
        while (result.isEmpty()) {
            for (int i = 1; i <= sourceCount; i++) {
                for (int j = sourceCount + 1; j <= probesCount; j++) {
                    boolean canBePair = generator.getBooleanValue();
                    if (canBePair) {
                        result.add(Arrays.asList(i, j));
                        result.add(Arrays.asList(j, i));
                    }
                }
            }
        }
        return result;
    }
    
    /**
     * Build all possible gropes.
     *
     * @param size int Group size
     * @param probesCount int all probes count.
     * @return List<List<Integer>>
     */
    public static List<List<Integer>> buildAllGroups(int size, int probesCount){
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        if(size == 1){
            for(int i=1;i<=probesCount; i++){
                List<Integer> group = new ArrayList<Integer>(size);
                group.add(i);
                result.add(group);
            }
            return result;
        }
        List<List<Integer>> before = buildAllGroups(size-1, probesCount);
        for(int i=1;i<=probesCount; i++){
            for(List<Integer> group : before){
                if(!group.contains(i)){
                    List<Integer> newGroup = new ArrayList<Integer>(size);
                    newGroup.addAll(group);
                    newGroup.add(i);
                    result.add(newGroup);
                }                
            }
        }
        return result;
    }
    
    public static Call createITSICall(Long startBorder, Long endBorder, int priority){
        Long duration = getRamdomTime(CallConstants.ITSI_DURATION_BORDERS[0], CallConstants.ITSI_DURATION_BORDERS[1]);
        Long start = getRamdomTime(startBorder, endBorder-duration/2);
        Call call = CallGeneratorUtils.getEmptyCall(start,priority);
        call.addParameter(CallParameterNames.DURATION_TIME, duration);
        return call;
    }
    
    /**
     * Create messages for one call. 
     *
     * @param startAll
     * @param messCount
     * @param priority
     * @param borders
     * @param acknBorders
     * @param messages
     * @return Call[]
     */
     public static Call[] createMessages(Long startAll, int messCount, int priority, Long[] borders,Long[] acknBorders, String... messages){
        RandomValueGenerator generator = RandomValueGenerator.getGenerator();
        Call[] result = new Call[messCount];
        Long start = startAll;
        for(int i=0; i<messCount; i++){
            Long duration = generator.getLongValue(borders[0], borders[1]);
            start = generator.getLongValue(start, start+duration/2);
            Call call = CallGeneratorUtils.getEmptyCall(start,priority);
            call.addParameter(CallParameterNames.DURATION_TIME, duration);
            call.addParameter(CallParameterNames.MESSAGE, messages[i]);
            Long maxAckn = duration<acknBorders[1]?duration:acknBorders[1];
            Long acknTime = generator.getLongValue(acknBorders[0], maxAckn);
            call.addParameter(CallParameterNames.ACKNOWLEDGE_TIME, acknTime);
            start = start+duration;
            result[i] = call;
        }
        return result;
    }
    
    /**
     * Returns random time in interval.
     *
     * @param start Long
     * @param end Long
     * @return Long
     */
    public static Long getRamdomTime(Long start, Long end) {
        if(start.equals(end)){
            return start;
        }
        Long time = getRandomGenerator().getLongValue(start, end);
        while((start-end>1)&&(time.equals(start)||time.equals(end))){
            time = getRandomGenerator().getLongValue(start, end);
        }
        return time;
    }
    
    /**
     * Convert ASCII string to hex string.
     *
     * @param ascii
     * @return
     */
    public static String convertAsciiToHex(String ascii){
        StringBuilder hex = new StringBuilder();
        
        for (int i=0; i < ascii.length(); i++) {
            hex.append(Integer.toHexString(ascii.charAt(i)));
        }
        
        return hex.toString();
    }
}