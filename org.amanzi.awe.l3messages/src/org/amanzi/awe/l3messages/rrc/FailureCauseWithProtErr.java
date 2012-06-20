
package org.amanzi.awe.l3messages.rrc;
//
// This file was generated by the BinaryNotes compiler.
// See http://bnotes.sourceforge.net 
// Any modifications to this file will be lost upon recompilation of the source ASN.1. 
//

import org.bn.CoderFactory;
import org.bn.annotations.ASN1Choice;
import org.bn.annotations.ASN1Element;
import org.bn.annotations.ASN1Null;
import org.bn.annotations.ASN1PreparedElement;
import org.bn.coders.IASN1PreparedElement;
import org.bn.coders.IASN1PreparedElementData;




    @ASN1PreparedElement
    @ASN1Choice ( name = "FailureCauseWithProtErr" )
    public class FailureCauseWithProtErr implements IASN1PreparedElement {
            
        @ASN1Null ( name = "configurationUnsupported" ) 
    
        @ASN1Element ( name = "configurationUnsupported", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private org.bn.types.NullObject configurationUnsupported = null;
                
  
        @ASN1Null ( name = "physicalChannelFailure" ) 
    
        @ASN1Element ( name = "physicalChannelFailure", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private org.bn.types.NullObject physicalChannelFailure = null;
                
  
        @ASN1Null ( name = "incompatibleSimultaneousReconfiguration" ) 
    
        @ASN1Element ( name = "incompatibleSimultaneousReconfiguration", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private org.bn.types.NullObject incompatibleSimultaneousReconfiguration = null;
                
  
        @ASN1Element ( name = "compressedModeRuntimeError", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private TGPSI compressedModeRuntimeError = null;
                
  
        @ASN1Element ( name = "protocolError", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private ProtocolErrorInformation protocolError = null;
                
  
        @ASN1Null ( name = "cellUpdateOccurred" ) 
    
        @ASN1Element ( name = "cellUpdateOccurred", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private org.bn.types.NullObject cellUpdateOccurred = null;
                
  
        @ASN1Null ( name = "invalidConfiguration" ) 
    
        @ASN1Element ( name = "invalidConfiguration", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private org.bn.types.NullObject invalidConfiguration = null;
                
  
        @ASN1Null ( name = "configurationIncomplete" ) 
    
        @ASN1Element ( name = "configurationIncomplete", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private org.bn.types.NullObject configurationIncomplete = null;
                
  
        @ASN1Null ( name = "unsupportedMeasurement" ) 
    
        @ASN1Element ( name = "unsupportedMeasurement", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private org.bn.types.NullObject unsupportedMeasurement = null;
                
  
        @ASN1Null ( name = "mbmsSessionAlreadyReceivedCorrectly" ) 
    
        @ASN1Element ( name = "mbmsSessionAlreadyReceivedCorrectly", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private org.bn.types.NullObject mbmsSessionAlreadyReceivedCorrectly = null;
                
  
        @ASN1Null ( name = "lowerPriorityMBMSService" ) 
    
        @ASN1Element ( name = "lowerPriorityMBMSService", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private org.bn.types.NullObject lowerPriorityMBMSService = null;
                
  
        @ASN1Null ( name = "spare5" ) 
    
        @ASN1Element ( name = "spare5", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private org.bn.types.NullObject spare5 = null;
                
  
        @ASN1Null ( name = "spare4" ) 
    
        @ASN1Element ( name = "spare4", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private org.bn.types.NullObject spare4 = null;
                
  
        @ASN1Null ( name = "spare3" ) 
    
        @ASN1Element ( name = "spare3", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private org.bn.types.NullObject spare3 = null;
                
  
        @ASN1Null ( name = "spare2" ) 
    
        @ASN1Element ( name = "spare2", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private org.bn.types.NullObject spare2 = null;
                
  
        @ASN1Null ( name = "spare1" ) 
    
        @ASN1Element ( name = "spare1", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private org.bn.types.NullObject spare1 = null;
                
  
        
        public org.bn.types.NullObject getConfigurationUnsupported () {
            return this.configurationUnsupported;
        }

        public boolean isConfigurationUnsupportedSelected () {
            return this.configurationUnsupported != null;
        }

        private void setConfigurationUnsupported (org.bn.types.NullObject value) {
            this.configurationUnsupported = value;
        }

        
        public void selectConfigurationUnsupported () {
            selectConfigurationUnsupported (new org.bn.types.NullObject());
	}
	
        public void selectConfigurationUnsupported (org.bn.types.NullObject value) {
            this.configurationUnsupported = value;
            
                    setPhysicalChannelFailure(null);
                
                    setIncompatibleSimultaneousReconfiguration(null);
                
                    setCompressedModeRuntimeError(null);
                
                    setProtocolError(null);
                
                    setCellUpdateOccurred(null);
                
                    setInvalidConfiguration(null);
                
                    setConfigurationIncomplete(null);
                
                    setUnsupportedMeasurement(null);
                
                    setMbmsSessionAlreadyReceivedCorrectly(null);
                
                    setLowerPriorityMBMSService(null);
                
                    setSpare5(null);
                
                    setSpare4(null);
                
                    setSpare3(null);
                
                    setSpare2(null);
                
                    setSpare1(null);
                            
        }

        
  
        
        public org.bn.types.NullObject getPhysicalChannelFailure () {
            return this.physicalChannelFailure;
        }

        public boolean isPhysicalChannelFailureSelected () {
            return this.physicalChannelFailure != null;
        }

        private void setPhysicalChannelFailure (org.bn.types.NullObject value) {
            this.physicalChannelFailure = value;
        }

        
        public void selectPhysicalChannelFailure () {
            selectPhysicalChannelFailure (new org.bn.types.NullObject());
	}
	
        public void selectPhysicalChannelFailure (org.bn.types.NullObject value) {
            this.physicalChannelFailure = value;
            
                    setConfigurationUnsupported(null);
                
                    setIncompatibleSimultaneousReconfiguration(null);
                
                    setCompressedModeRuntimeError(null);
                
                    setProtocolError(null);
                
                    setCellUpdateOccurred(null);
                
                    setInvalidConfiguration(null);
                
                    setConfigurationIncomplete(null);
                
                    setUnsupportedMeasurement(null);
                
                    setMbmsSessionAlreadyReceivedCorrectly(null);
                
                    setLowerPriorityMBMSService(null);
                
                    setSpare5(null);
                
                    setSpare4(null);
                
                    setSpare3(null);
                
                    setSpare2(null);
                
                    setSpare1(null);
                            
        }

        
  
        
        public org.bn.types.NullObject getIncompatibleSimultaneousReconfiguration () {
            return this.incompatibleSimultaneousReconfiguration;
        }

        public boolean isIncompatibleSimultaneousReconfigurationSelected () {
            return this.incompatibleSimultaneousReconfiguration != null;
        }

        private void setIncompatibleSimultaneousReconfiguration (org.bn.types.NullObject value) {
            this.incompatibleSimultaneousReconfiguration = value;
        }

        
        public void selectIncompatibleSimultaneousReconfiguration () {
            selectIncompatibleSimultaneousReconfiguration (new org.bn.types.NullObject());
	}
	
        public void selectIncompatibleSimultaneousReconfiguration (org.bn.types.NullObject value) {
            this.incompatibleSimultaneousReconfiguration = value;
            
                    setConfigurationUnsupported(null);
                
                    setPhysicalChannelFailure(null);
                
                    setCompressedModeRuntimeError(null);
                
                    setProtocolError(null);
                
                    setCellUpdateOccurred(null);
                
                    setInvalidConfiguration(null);
                
                    setConfigurationIncomplete(null);
                
                    setUnsupportedMeasurement(null);
                
                    setMbmsSessionAlreadyReceivedCorrectly(null);
                
                    setLowerPriorityMBMSService(null);
                
                    setSpare5(null);
                
                    setSpare4(null);
                
                    setSpare3(null);
                
                    setSpare2(null);
                
                    setSpare1(null);
                            
        }

        
  
        
        public TGPSI getCompressedModeRuntimeError () {
            return this.compressedModeRuntimeError;
        }

        public boolean isCompressedModeRuntimeErrorSelected () {
            return this.compressedModeRuntimeError != null;
        }

        private void setCompressedModeRuntimeError (TGPSI value) {
            this.compressedModeRuntimeError = value;
        }

        
        public void selectCompressedModeRuntimeError (TGPSI value) {
            this.compressedModeRuntimeError = value;
            
                    setConfigurationUnsupported(null);
                
                    setPhysicalChannelFailure(null);
                
                    setIncompatibleSimultaneousReconfiguration(null);
                
                    setProtocolError(null);
                
                    setCellUpdateOccurred(null);
                
                    setInvalidConfiguration(null);
                
                    setConfigurationIncomplete(null);
                
                    setUnsupportedMeasurement(null);
                
                    setMbmsSessionAlreadyReceivedCorrectly(null);
                
                    setLowerPriorityMBMSService(null);
                
                    setSpare5(null);
                
                    setSpare4(null);
                
                    setSpare3(null);
                
                    setSpare2(null);
                
                    setSpare1(null);
                            
        }

        
  
        
        public ProtocolErrorInformation getProtocolError () {
            return this.protocolError;
        }

        public boolean isProtocolErrorSelected () {
            return this.protocolError != null;
        }

        private void setProtocolError (ProtocolErrorInformation value) {
            this.protocolError = value;
        }

        
        public void selectProtocolError (ProtocolErrorInformation value) {
            this.protocolError = value;
            
                    setConfigurationUnsupported(null);
                
                    setPhysicalChannelFailure(null);
                
                    setIncompatibleSimultaneousReconfiguration(null);
                
                    setCompressedModeRuntimeError(null);
                
                    setCellUpdateOccurred(null);
                
                    setInvalidConfiguration(null);
                
                    setConfigurationIncomplete(null);
                
                    setUnsupportedMeasurement(null);
                
                    setMbmsSessionAlreadyReceivedCorrectly(null);
                
                    setLowerPriorityMBMSService(null);
                
                    setSpare5(null);
                
                    setSpare4(null);
                
                    setSpare3(null);
                
                    setSpare2(null);
                
                    setSpare1(null);
                            
        }

        
  
        
        public org.bn.types.NullObject getCellUpdateOccurred () {
            return this.cellUpdateOccurred;
        }

        public boolean isCellUpdateOccurredSelected () {
            return this.cellUpdateOccurred != null;
        }

        private void setCellUpdateOccurred (org.bn.types.NullObject value) {
            this.cellUpdateOccurred = value;
        }

        
        public void selectCellUpdateOccurred () {
            selectCellUpdateOccurred (new org.bn.types.NullObject());
	}
	
        public void selectCellUpdateOccurred (org.bn.types.NullObject value) {
            this.cellUpdateOccurred = value;
            
                    setConfigurationUnsupported(null);
                
                    setPhysicalChannelFailure(null);
                
                    setIncompatibleSimultaneousReconfiguration(null);
                
                    setCompressedModeRuntimeError(null);
                
                    setProtocolError(null);
                
                    setInvalidConfiguration(null);
                
                    setConfigurationIncomplete(null);
                
                    setUnsupportedMeasurement(null);
                
                    setMbmsSessionAlreadyReceivedCorrectly(null);
                
                    setLowerPriorityMBMSService(null);
                
                    setSpare5(null);
                
                    setSpare4(null);
                
                    setSpare3(null);
                
                    setSpare2(null);
                
                    setSpare1(null);
                            
        }

        
  
        
        public org.bn.types.NullObject getInvalidConfiguration () {
            return this.invalidConfiguration;
        }

        public boolean isInvalidConfigurationSelected () {
            return this.invalidConfiguration != null;
        }

        private void setInvalidConfiguration (org.bn.types.NullObject value) {
            this.invalidConfiguration = value;
        }

        
        public void selectInvalidConfiguration () {
            selectInvalidConfiguration (new org.bn.types.NullObject());
	}
	
        public void selectInvalidConfiguration (org.bn.types.NullObject value) {
            this.invalidConfiguration = value;
            
                    setConfigurationUnsupported(null);
                
                    setPhysicalChannelFailure(null);
                
                    setIncompatibleSimultaneousReconfiguration(null);
                
                    setCompressedModeRuntimeError(null);
                
                    setProtocolError(null);
                
                    setCellUpdateOccurred(null);
                
                    setConfigurationIncomplete(null);
                
                    setUnsupportedMeasurement(null);
                
                    setMbmsSessionAlreadyReceivedCorrectly(null);
                
                    setLowerPriorityMBMSService(null);
                
                    setSpare5(null);
                
                    setSpare4(null);
                
                    setSpare3(null);
                
                    setSpare2(null);
                
                    setSpare1(null);
                            
        }

        
  
        
        public org.bn.types.NullObject getConfigurationIncomplete () {
            return this.configurationIncomplete;
        }

        public boolean isConfigurationIncompleteSelected () {
            return this.configurationIncomplete != null;
        }

        private void setConfigurationIncomplete (org.bn.types.NullObject value) {
            this.configurationIncomplete = value;
        }

        
        public void selectConfigurationIncomplete () {
            selectConfigurationIncomplete (new org.bn.types.NullObject());
	}
	
        public void selectConfigurationIncomplete (org.bn.types.NullObject value) {
            this.configurationIncomplete = value;
            
                    setConfigurationUnsupported(null);
                
                    setPhysicalChannelFailure(null);
                
                    setIncompatibleSimultaneousReconfiguration(null);
                
                    setCompressedModeRuntimeError(null);
                
                    setProtocolError(null);
                
                    setCellUpdateOccurred(null);
                
                    setInvalidConfiguration(null);
                
                    setUnsupportedMeasurement(null);
                
                    setMbmsSessionAlreadyReceivedCorrectly(null);
                
                    setLowerPriorityMBMSService(null);
                
                    setSpare5(null);
                
                    setSpare4(null);
                
                    setSpare3(null);
                
                    setSpare2(null);
                
                    setSpare1(null);
                            
        }

        
  
        
        public org.bn.types.NullObject getUnsupportedMeasurement () {
            return this.unsupportedMeasurement;
        }

        public boolean isUnsupportedMeasurementSelected () {
            return this.unsupportedMeasurement != null;
        }

        private void setUnsupportedMeasurement (org.bn.types.NullObject value) {
            this.unsupportedMeasurement = value;
        }

        
        public void selectUnsupportedMeasurement () {
            selectUnsupportedMeasurement (new org.bn.types.NullObject());
	}
	
        public void selectUnsupportedMeasurement (org.bn.types.NullObject value) {
            this.unsupportedMeasurement = value;
            
                    setConfigurationUnsupported(null);
                
                    setPhysicalChannelFailure(null);
                
                    setIncompatibleSimultaneousReconfiguration(null);
                
                    setCompressedModeRuntimeError(null);
                
                    setProtocolError(null);
                
                    setCellUpdateOccurred(null);
                
                    setInvalidConfiguration(null);
                
                    setConfigurationIncomplete(null);
                
                    setMbmsSessionAlreadyReceivedCorrectly(null);
                
                    setLowerPriorityMBMSService(null);
                
                    setSpare5(null);
                
                    setSpare4(null);
                
                    setSpare3(null);
                
                    setSpare2(null);
                
                    setSpare1(null);
                            
        }

        
  
        
        public org.bn.types.NullObject getMbmsSessionAlreadyReceivedCorrectly () {
            return this.mbmsSessionAlreadyReceivedCorrectly;
        }

        public boolean isMbmsSessionAlreadyReceivedCorrectlySelected () {
            return this.mbmsSessionAlreadyReceivedCorrectly != null;
        }

        private void setMbmsSessionAlreadyReceivedCorrectly (org.bn.types.NullObject value) {
            this.mbmsSessionAlreadyReceivedCorrectly = value;
        }

        
        public void selectMbmsSessionAlreadyReceivedCorrectly () {
            selectMbmsSessionAlreadyReceivedCorrectly (new org.bn.types.NullObject());
	}
	
        public void selectMbmsSessionAlreadyReceivedCorrectly (org.bn.types.NullObject value) {
            this.mbmsSessionAlreadyReceivedCorrectly = value;
            
                    setConfigurationUnsupported(null);
                
                    setPhysicalChannelFailure(null);
                
                    setIncompatibleSimultaneousReconfiguration(null);
                
                    setCompressedModeRuntimeError(null);
                
                    setProtocolError(null);
                
                    setCellUpdateOccurred(null);
                
                    setInvalidConfiguration(null);
                
                    setConfigurationIncomplete(null);
                
                    setUnsupportedMeasurement(null);
                
                    setLowerPriorityMBMSService(null);
                
                    setSpare5(null);
                
                    setSpare4(null);
                
                    setSpare3(null);
                
                    setSpare2(null);
                
                    setSpare1(null);
                            
        }

        
  
        
        public org.bn.types.NullObject getLowerPriorityMBMSService () {
            return this.lowerPriorityMBMSService;
        }

        public boolean isLowerPriorityMBMSServiceSelected () {
            return this.lowerPriorityMBMSService != null;
        }

        private void setLowerPriorityMBMSService (org.bn.types.NullObject value) {
            this.lowerPriorityMBMSService = value;
        }

        
        public void selectLowerPriorityMBMSService () {
            selectLowerPriorityMBMSService (new org.bn.types.NullObject());
	}
	
        public void selectLowerPriorityMBMSService (org.bn.types.NullObject value) {
            this.lowerPriorityMBMSService = value;
            
                    setConfigurationUnsupported(null);
                
                    setPhysicalChannelFailure(null);
                
                    setIncompatibleSimultaneousReconfiguration(null);
                
                    setCompressedModeRuntimeError(null);
                
                    setProtocolError(null);
                
                    setCellUpdateOccurred(null);
                
                    setInvalidConfiguration(null);
                
                    setConfigurationIncomplete(null);
                
                    setUnsupportedMeasurement(null);
                
                    setMbmsSessionAlreadyReceivedCorrectly(null);
                
                    setSpare5(null);
                
                    setSpare4(null);
                
                    setSpare3(null);
                
                    setSpare2(null);
                
                    setSpare1(null);
                            
        }

        
  
        
        public org.bn.types.NullObject getSpare5 () {
            return this.spare5;
        }

        public boolean isSpare5Selected () {
            return this.spare5 != null;
        }

        private void setSpare5 (org.bn.types.NullObject value) {
            this.spare5 = value;
        }

        
        public void selectSpare5 () {
            selectSpare5 (new org.bn.types.NullObject());
	}
	
        public void selectSpare5 (org.bn.types.NullObject value) {
            this.spare5 = value;
            
                    setConfigurationUnsupported(null);
                
                    setPhysicalChannelFailure(null);
                
                    setIncompatibleSimultaneousReconfiguration(null);
                
                    setCompressedModeRuntimeError(null);
                
                    setProtocolError(null);
                
                    setCellUpdateOccurred(null);
                
                    setInvalidConfiguration(null);
                
                    setConfigurationIncomplete(null);
                
                    setUnsupportedMeasurement(null);
                
                    setMbmsSessionAlreadyReceivedCorrectly(null);
                
                    setLowerPriorityMBMSService(null);
                
                    setSpare4(null);
                
                    setSpare3(null);
                
                    setSpare2(null);
                
                    setSpare1(null);
                            
        }

        
  
        
        public org.bn.types.NullObject getSpare4 () {
            return this.spare4;
        }

        public boolean isSpare4Selected () {
            return this.spare4 != null;
        }

        private void setSpare4 (org.bn.types.NullObject value) {
            this.spare4 = value;
        }

        
        public void selectSpare4 () {
            selectSpare4 (new org.bn.types.NullObject());
	}
	
        public void selectSpare4 (org.bn.types.NullObject value) {
            this.spare4 = value;
            
                    setConfigurationUnsupported(null);
                
                    setPhysicalChannelFailure(null);
                
                    setIncompatibleSimultaneousReconfiguration(null);
                
                    setCompressedModeRuntimeError(null);
                
                    setProtocolError(null);
                
                    setCellUpdateOccurred(null);
                
                    setInvalidConfiguration(null);
                
                    setConfigurationIncomplete(null);
                
                    setUnsupportedMeasurement(null);
                
                    setMbmsSessionAlreadyReceivedCorrectly(null);
                
                    setLowerPriorityMBMSService(null);
                
                    setSpare5(null);
                
                    setSpare3(null);
                
                    setSpare2(null);
                
                    setSpare1(null);
                            
        }

        
  
        
        public org.bn.types.NullObject getSpare3 () {
            return this.spare3;
        }

        public boolean isSpare3Selected () {
            return this.spare3 != null;
        }

        private void setSpare3 (org.bn.types.NullObject value) {
            this.spare3 = value;
        }

        
        public void selectSpare3 () {
            selectSpare3 (new org.bn.types.NullObject());
	}
	
        public void selectSpare3 (org.bn.types.NullObject value) {
            this.spare3 = value;
            
                    setConfigurationUnsupported(null);
                
                    setPhysicalChannelFailure(null);
                
                    setIncompatibleSimultaneousReconfiguration(null);
                
                    setCompressedModeRuntimeError(null);
                
                    setProtocolError(null);
                
                    setCellUpdateOccurred(null);
                
                    setInvalidConfiguration(null);
                
                    setConfigurationIncomplete(null);
                
                    setUnsupportedMeasurement(null);
                
                    setMbmsSessionAlreadyReceivedCorrectly(null);
                
                    setLowerPriorityMBMSService(null);
                
                    setSpare5(null);
                
                    setSpare4(null);
                
                    setSpare2(null);
                
                    setSpare1(null);
                            
        }

        
  
        
        public org.bn.types.NullObject getSpare2 () {
            return this.spare2;
        }

        public boolean isSpare2Selected () {
            return this.spare2 != null;
        }

        private void setSpare2 (org.bn.types.NullObject value) {
            this.spare2 = value;
        }

        
        public void selectSpare2 () {
            selectSpare2 (new org.bn.types.NullObject());
	}
	
        public void selectSpare2 (org.bn.types.NullObject value) {
            this.spare2 = value;
            
                    setConfigurationUnsupported(null);
                
                    setPhysicalChannelFailure(null);
                
                    setIncompatibleSimultaneousReconfiguration(null);
                
                    setCompressedModeRuntimeError(null);
                
                    setProtocolError(null);
                
                    setCellUpdateOccurred(null);
                
                    setInvalidConfiguration(null);
                
                    setConfigurationIncomplete(null);
                
                    setUnsupportedMeasurement(null);
                
                    setMbmsSessionAlreadyReceivedCorrectly(null);
                
                    setLowerPriorityMBMSService(null);
                
                    setSpare5(null);
                
                    setSpare4(null);
                
                    setSpare3(null);
                
                    setSpare1(null);
                            
        }

        
  
        
        public org.bn.types.NullObject getSpare1 () {
            return this.spare1;
        }

        public boolean isSpare1Selected () {
            return this.spare1 != null;
        }

        private void setSpare1 (org.bn.types.NullObject value) {
            this.spare1 = value;
        }

        
        public void selectSpare1 () {
            selectSpare1 (new org.bn.types.NullObject());
	}
	
        public void selectSpare1 (org.bn.types.NullObject value) {
            this.spare1 = value;
            
                    setConfigurationUnsupported(null);
                
                    setPhysicalChannelFailure(null);
                
                    setIncompatibleSimultaneousReconfiguration(null);
                
                    setCompressedModeRuntimeError(null);
                
                    setProtocolError(null);
                
                    setCellUpdateOccurred(null);
                
                    setInvalidConfiguration(null);
                
                    setConfigurationIncomplete(null);
                
                    setUnsupportedMeasurement(null);
                
                    setMbmsSessionAlreadyReceivedCorrectly(null);
                
                    setLowerPriorityMBMSService(null);
                
                    setSpare5(null);
                
                    setSpare4(null);
                
                    setSpare3(null);
                
                    setSpare2(null);
                            
        }

        
  

	    public void initWithDefaults() {
	    }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(FailureCauseWithProtErr.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }


    }
            