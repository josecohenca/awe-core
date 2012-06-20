
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
    @ASN1Sequence ( name = "RLC_Capability", isSet = false )
    public class RLC_Capability implements IASN1PreparedElement {
            
        @ASN1Element ( name = "totalRLC-AM-BufferSize", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private TotalRLC_AM_BufferSize totalRLC_AM_BufferSize = null;
                
  
        @ASN1Element ( name = "maximumRLC-WindowSize", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private MaximumRLC_WindowSize maximumRLC_WindowSize = null;
                
  
        @ASN1Element ( name = "maximumAM-EntityNumber", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private MaximumAM_EntityNumberRLC_Cap maximumAM_EntityNumber = null;
                
  
        
        public TotalRLC_AM_BufferSize getTotalRLC_AM_BufferSize () {
            return this.totalRLC_AM_BufferSize;
        }

        

        public void setTotalRLC_AM_BufferSize (TotalRLC_AM_BufferSize value) {
            this.totalRLC_AM_BufferSize = value;
        }
        
  
        
        public MaximumRLC_WindowSize getMaximumRLC_WindowSize () {
            return this.maximumRLC_WindowSize;
        }

        

        public void setMaximumRLC_WindowSize (MaximumRLC_WindowSize value) {
            this.maximumRLC_WindowSize = value;
        }
        
  
        
        public MaximumAM_EntityNumberRLC_Cap getMaximumAM_EntityNumber () {
            return this.maximumAM_EntityNumber;
        }

        

        public void setMaximumAM_EntityNumber (MaximumAM_EntityNumberRLC_Cap value) {
            this.maximumAM_EntityNumber = value;
        }
        
  
                    
        
        public void initWithDefaults() {
            
        }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(RLC_Capability.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }

            
    }
            