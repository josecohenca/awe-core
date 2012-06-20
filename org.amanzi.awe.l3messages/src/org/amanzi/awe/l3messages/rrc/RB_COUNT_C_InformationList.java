
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
    @ASN1BoxedType ( name = "RB_COUNT_C_InformationList" )
    public class RB_COUNT_C_InformationList implements IASN1PreparedElement {
                
            @ASN1ValueRangeConstraint ( 
		
		min = 1L, 
		
		max = 27L 
		
	   )
	   
            @ASN1SequenceOf( name = "RB-COUNT-C-InformationList" , isSetOf = false)
	    private java.util.Collection<RB_COUNT_C_Information> value = null; 
    
            public RB_COUNT_C_InformationList () {
            }
        
            public RB_COUNT_C_InformationList ( java.util.Collection<RB_COUNT_C_Information> value ) {
                setValue(value);
            }
                        
            public void setValue(java.util.Collection<RB_COUNT_C_Information> value) {
                this.value = value;
            }
            
            public java.util.Collection<RB_COUNT_C_Information> getValue() {
                return this.value;
            }            
            
            public void initValue() {
                setValue(new java.util.LinkedList<RB_COUNT_C_Information>()); 
            }
            
            public void add(RB_COUNT_C_Information item) {
                value.add(item);
            }

	    public void initWithDefaults() {
	    }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(RB_COUNT_C_InformationList.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }


    }
            