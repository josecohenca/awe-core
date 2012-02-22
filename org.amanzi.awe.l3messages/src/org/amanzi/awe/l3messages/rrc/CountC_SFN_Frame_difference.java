
package org.amanzi.awe.l3messages.rrc;
//
// This file was generated by the BinaryNotes compiler.
// See http://bnotes.sourceforge.net 
// Any modifications to this file will be lost upon recompilation of the source ASN.1. 
//

import org.bn.*;
import org.bn.annotations.*;
import org.bn.annotations.constraints.*;
import org.bn.coders.*;
import org.bn.types.*;




    @ASN1PreparedElement
    @ASN1Sequence ( name = "CountC_SFN_Frame_difference", isSet = false )
    public class CountC_SFN_Frame_difference implements IASN1PreparedElement {
            @ASN1Integer( name = "" )
    @ASN1ValueRangeConstraint ( 
		
		min = 0L, 
		
		max = 15L 
		
	   )
	   
        @ASN1Element ( name = "countC-SFN-High", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private Integer countC_SFN_High = null;
                
  @ASN1Integer( name = "" )
    @ASN1ValueRangeConstraint ( 
		
		min = 0L, 
		
		max = 255L 
		
	   )
	   
        @ASN1Element ( name = "off", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private Integer off = null;
                
  
        
        public Integer getCountC_SFN_High () {
            return this.countC_SFN_High;
        }

        

        public void setCountC_SFN_High (Integer value) {
            this.countC_SFN_High = value;
        }
        
  
        
        public Integer getOff () {
            return this.off;
        }

        

        public void setOff (Integer value) {
            this.off = value;
        }
        
  
                    
        
        public void initWithDefaults() {
            
        }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(CountC_SFN_Frame_difference.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }

            
    }
            