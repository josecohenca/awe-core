
package org.amanzi.awe.l3messages.rrc;
//
// This file was generated by the BinaryNotes compiler.
// See http://bnotes.sourceforge.net 
// Any modifications to this file will be lost upon recompilation of the source ASN.1. 
//

import org.bn.CoderFactory;
import org.bn.annotations.ASN1BoxedType;
import org.bn.annotations.ASN1Integer;
import org.bn.annotations.ASN1PreparedElement;
import org.bn.annotations.constraints.ASN1ValueRangeConstraint;
import org.bn.coders.IASN1PreparedElement;
import org.bn.coders.IASN1PreparedElementData;




    @ASN1PreparedElement
    @ASN1BoxedType ( name = "MidambleShiftLong" )
    public class MidambleShiftLong implements IASN1PreparedElement {
    
            @ASN1Integer( name = "MidambleShiftLong" )
            @ASN1ValueRangeConstraint ( 
		
		min = 0L, 
		
		max = 15L 
		
	   )
	   
            private Integer value;
            
            public MidambleShiftLong() {
            }

            public MidambleShiftLong(Integer value) {
                this.value = value;
            }
            
            public void setValue(Integer value) {
                this.value = value;
            }
            
            public Integer getValue() {
                return this.value;
            }

	    public void initWithDefaults() {
	    }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(MidambleShiftLong.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }


    }
            