
package org.amanzi.awe.l3messages.rrc;
//
// This file was generated by the BinaryNotes compiler.
// See http://bnotes.sourceforge.net 
// Any modifications to this file will be lost upon recompilation of the source ASN.1. 
//

import org.bn.CoderFactory;
import org.bn.annotations.ASN1BoxedType;
import org.bn.annotations.ASN1PreparedElement;
import org.bn.annotations.ASN1SequenceOf;
import org.bn.annotations.constraints.ASN1ValueRangeConstraint;
import org.bn.coders.IASN1PreparedElement;
import org.bn.coders.IASN1PreparedElementData;




    @ASN1PreparedElement
    @ASN1BoxedType ( name = "IntraFrequencyMeasuredResultsList_v590ext" )
    public class IntraFrequencyMeasuredResultsList_v590ext implements IASN1PreparedElement {
                
            @ASN1ValueRangeConstraint ( 
		
		min = 1L, 
		
		max = 32L 
		
	   )
	   
            @ASN1SequenceOf( name = "IntraFrequencyMeasuredResultsList-v590ext" , isSetOf = false)
	    private java.util.Collection<DeltaRSCPPerCell> value = null; 
    
            public IntraFrequencyMeasuredResultsList_v590ext () {
            }
        
            public IntraFrequencyMeasuredResultsList_v590ext ( java.util.Collection<DeltaRSCPPerCell> value ) {
                setValue(value);
            }
                        
            public void setValue(java.util.Collection<DeltaRSCPPerCell> value) {
                this.value = value;
            }
            
            public java.util.Collection<DeltaRSCPPerCell> getValue() {
                return this.value;
            }            
            
            public void initValue() {
                setValue(new java.util.LinkedList<DeltaRSCPPerCell>()); 
            }
            
            public void add(DeltaRSCPPerCell item) {
                value.add(item);
            }

	    public void initWithDefaults() {
	    }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(IntraFrequencyMeasuredResultsList_v590ext.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }


    }
            