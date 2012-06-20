
package org.amanzi.awe.l3messages.rrc;
//
// This file was generated by the BinaryNotes compiler.
// See http://bnotes.sourceforge.net 
// Any modifications to this file will be lost upon recompilation of the source ASN.1. 
//

import org.bn.CoderFactory;
import org.bn.annotations.ASN1Element;
import org.bn.annotations.ASN1PreparedElement;
import org.bn.annotations.ASN1Sequence;
import org.bn.coders.IASN1PreparedElement;
import org.bn.coders.IASN1PreparedElementData;




    @ASN1PreparedElement
    @ASN1Sequence ( name = "UE_RadioAccessCapability_v3a0ext", isSet = false )
    public class UE_RadioAccessCapability_v3a0ext implements IASN1PreparedElement {
            
        @ASN1Element ( name = "ue-PositioningCapabilityExt-v3a0", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private UE_PositioningCapabilityExt_v3a0 ue_PositioningCapabilityExt_v3a0 = null;
                
  
        
        public UE_PositioningCapabilityExt_v3a0 getUe_PositioningCapabilityExt_v3a0 () {
            return this.ue_PositioningCapabilityExt_v3a0;
        }

        

        public void setUe_PositioningCapabilityExt_v3a0 (UE_PositioningCapabilityExt_v3a0 value) {
            this.ue_PositioningCapabilityExt_v3a0 = value;
        }
        
  
                    
        
        public void initWithDefaults() {
            
        }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(UE_RadioAccessCapability_v3a0ext.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }

            
    }
            