
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
    @ASN1BoxedType ( name = "TimeslotNumber" )
    public class TimeslotNumber implements IASN1PreparedElement {
    
            @ASN1Integer( name = "TimeslotNumber" )
            @ASN1ValueRangeConstraint ( 
		
		min = 0L, 
		
		max = 14L 
		
	   )
	   
            private Integer value;
            
            public TimeslotNumber() {
            }

            public TimeslotNumber(Integer value) {
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

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(TimeslotNumber.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }


    }
            