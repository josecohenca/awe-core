
package org.amanzi.awe.l3messages.rrc;
//
// This file was generated by the BinaryNotes compiler.
// See http://bnotes.sourceforge.net 
// Any modifications to this file will be lost upon recompilation of the source ASN.1. 
//

import org.bn.CoderFactory;
import org.bn.annotations.ASN1Choice;
import org.bn.annotations.ASN1Element;
import org.bn.annotations.ASN1PreparedElement;
import org.bn.annotations.ASN1Sequence;
import org.bn.coders.IASN1PreparedElement;
import org.bn.coders.IASN1PreparedElementData;




    @ASN1PreparedElement
    @ASN1Sequence ( name = "CellMeasuredResults", isSet = false )
    public class CellMeasuredResults implements IASN1PreparedElement {
            
        @ASN1Element ( name = "cellIdentity", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private CellIdentity cellIdentity = null;
                
  
        @ASN1Element ( name = "dummy", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private SFN_SFN_ObsTimeDifference dummy = null;
                
  
        @ASN1Element ( name = "cellSynchronisationInfo", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private CellSynchronisationInfo cellSynchronisationInfo = null;
                
  
        
    @ASN1PreparedElement
    @ASN1Choice ( name = "modeSpecificInfo" )
    public static class ModeSpecificInfoChoiceType implements IASN1PreparedElement {
            

       @ASN1PreparedElement
       @ASN1Sequence ( name = "fdd" , isSet = false )
       public static class FddSequenceType implements IASN1PreparedElement {
                
        @ASN1Element ( name = "primaryCPICH-Info", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private PrimaryCPICH_Info primaryCPICH_Info = null;
                
  
        @ASN1Element ( name = "cpich-Ec-N0", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private CPICH_Ec_N0 cpich_Ec_N0 = null;
                
  
        @ASN1Element ( name = "cpich-RSCP", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private CPICH_RSCP cpich_RSCP = null;
                
  
        @ASN1Element ( name = "pathloss", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private Pathloss pathloss = null;
                
  
        
        public PrimaryCPICH_Info getPrimaryCPICH_Info () {
            return this.primaryCPICH_Info;
        }

        

        public void setPrimaryCPICH_Info (PrimaryCPICH_Info value) {
            this.primaryCPICH_Info = value;
        }
        
  
        
        public CPICH_Ec_N0 getCpich_Ec_N0 () {
            return this.cpich_Ec_N0;
        }

        
        public boolean isCpich_Ec_N0Present () {
            return this.cpich_Ec_N0 != null;
        }
        

        public void setCpich_Ec_N0 (CPICH_Ec_N0 value) {
            this.cpich_Ec_N0 = value;
        }
        
  
        
        public CPICH_RSCP getCpich_RSCP () {
            return this.cpich_RSCP;
        }

        
        public boolean isCpich_RSCPPresent () {
            return this.cpich_RSCP != null;
        }
        

        public void setCpich_RSCP (CPICH_RSCP value) {
            this.cpich_RSCP = value;
        }
        
  
        
        public Pathloss getPathloss () {
            return this.pathloss;
        }

        
        public boolean isPathlossPresent () {
            return this.pathloss != null;
        }
        

        public void setPathloss (Pathloss value) {
            this.pathloss = value;
        }
        
  
                
                
        public void initWithDefaults() {
            
        }

        public IASN1PreparedElementData getPreparedData() {
            return preparedData_FddSequenceType;
        }

       private static IASN1PreparedElementData preparedData_FddSequenceType = CoderFactory.getInstance().newPreparedElementData(FddSequenceType.class);
                
       }

       
                
        @ASN1Element ( name = "fdd", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private FddSequenceType fdd = null;
                
  

       @ASN1PreparedElement
       @ASN1Sequence ( name = "tdd" , isSet = false )
       public static class TddSequenceType implements IASN1PreparedElement {
                
        @ASN1Element ( name = "cellParametersID", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private CellParametersID cellParametersID = null;
                
  
        @ASN1Element ( name = "proposedTGSN", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private TGSN proposedTGSN = null;
                
  
        @ASN1Element ( name = "primaryCCPCH-RSCP", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private PrimaryCCPCH_RSCP primaryCCPCH_RSCP = null;
                
  
        @ASN1Element ( name = "pathloss", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private Pathloss pathloss = null;
                
  
        @ASN1Element ( name = "timeslotISCP-List", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private TimeslotISCP_List timeslotISCP_List = null;
                
  
        
        public CellParametersID getCellParametersID () {
            return this.cellParametersID;
        }

        

        public void setCellParametersID (CellParametersID value) {
            this.cellParametersID = value;
        }
        
  
        
        public TGSN getProposedTGSN () {
            return this.proposedTGSN;
        }

        
        public boolean isProposedTGSNPresent () {
            return this.proposedTGSN != null;
        }
        

        public void setProposedTGSN (TGSN value) {
            this.proposedTGSN = value;
        }
        
  
        
        public PrimaryCCPCH_RSCP getPrimaryCCPCH_RSCP () {
            return this.primaryCCPCH_RSCP;
        }

        
        public boolean isPrimaryCCPCH_RSCPPresent () {
            return this.primaryCCPCH_RSCP != null;
        }
        

        public void setPrimaryCCPCH_RSCP (PrimaryCCPCH_RSCP value) {
            this.primaryCCPCH_RSCP = value;
        }
        
  
        
        public Pathloss getPathloss () {
            return this.pathloss;
        }

        
        public boolean isPathlossPresent () {
            return this.pathloss != null;
        }
        

        public void setPathloss (Pathloss value) {
            this.pathloss = value;
        }
        
  
        
        public TimeslotISCP_List getTimeslotISCP_List () {
            return this.timeslotISCP_List;
        }

        
        public boolean isTimeslotISCP_ListPresent () {
            return this.timeslotISCP_List != null;
        }
        

        public void setTimeslotISCP_List (TimeslotISCP_List value) {
            this.timeslotISCP_List = value;
        }
        
  
                
                
        public void initWithDefaults() {
            
        }

        public IASN1PreparedElementData getPreparedData() {
            return preparedData_TddSequenceType;
        }

       private static IASN1PreparedElementData preparedData_TddSequenceType = CoderFactory.getInstance().newPreparedElementData(TddSequenceType.class);
                
       }

       
                
        @ASN1Element ( name = "tdd", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private TddSequenceType tdd = null;
                
  
        
        public FddSequenceType getFdd () {
            return this.fdd;
        }

        public boolean isFddSelected () {
            return this.fdd != null;
        }

        private void setFdd (FddSequenceType value) {
            this.fdd = value;
        }

        
        public void selectFdd (FddSequenceType value) {
            this.fdd = value;
            
                    setTdd(null);
                            
        }

        
  
        
        public TddSequenceType getTdd () {
            return this.tdd;
        }

        public boolean isTddSelected () {
            return this.tdd != null;
        }

        private void setTdd (TddSequenceType value) {
            this.tdd = value;
        }

        
        public void selectTdd (TddSequenceType value) {
            this.tdd = value;
            
                    setFdd(null);
                            
        }

        
  

	    public void initWithDefaults() {
	    }

        public IASN1PreparedElementData getPreparedData() {
            return preparedData_ModeSpecificInfoChoiceType;
        }

        private static IASN1PreparedElementData preparedData_ModeSpecificInfoChoiceType = CoderFactory.getInstance().newPreparedElementData(ModeSpecificInfoChoiceType.class);

    }

                
        @ASN1Element ( name = "modeSpecificInfo", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private ModeSpecificInfoChoiceType modeSpecificInfo = null;
                
  
        
        public CellIdentity getCellIdentity () {
            return this.cellIdentity;
        }

        
        public boolean isCellIdentityPresent () {
            return this.cellIdentity != null;
        }
        

        public void setCellIdentity (CellIdentity value) {
            this.cellIdentity = value;
        }
        
  
        
        public SFN_SFN_ObsTimeDifference getDummy () {
            return this.dummy;
        }

        
        public boolean isDummyPresent () {
            return this.dummy != null;
        }
        

        public void setDummy (SFN_SFN_ObsTimeDifference value) {
            this.dummy = value;
        }
        
  
        
        public CellSynchronisationInfo getCellSynchronisationInfo () {
            return this.cellSynchronisationInfo;
        }

        
        public boolean isCellSynchronisationInfoPresent () {
            return this.cellSynchronisationInfo != null;
        }
        

        public void setCellSynchronisationInfo (CellSynchronisationInfo value) {
            this.cellSynchronisationInfo = value;
        }
        
  
        
        public ModeSpecificInfoChoiceType getModeSpecificInfo () {
            return this.modeSpecificInfo;
        }

        

        public void setModeSpecificInfo (ModeSpecificInfoChoiceType value) {
            this.modeSpecificInfo = value;
        }
        
  
                    
        
        public void initWithDefaults() {
            
        }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(CellMeasuredResults.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }

            
    }
            