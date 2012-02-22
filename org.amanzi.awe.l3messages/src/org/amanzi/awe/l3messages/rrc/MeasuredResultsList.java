
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
    @ASN1BoxedType ( name = "MeasuredResultsList" )
    public class MeasuredResultsList implements IASN1PreparedElement {
                
            @ASN1ValueRangeConstraint ( 
		
		min = 1L, 
		
		max = 4L 
		
	   )
	   
            @ASN1SequenceOf( name = "MeasuredResultsList" , isSetOf = false)
	    private java.util.Collection<MeasuredResults> value = null; 
    
            public MeasuredResultsList () {
            }
        
            public MeasuredResultsList ( java.util.Collection<MeasuredResults> value ) {
                setValue(value);
            }
                        
            public void setValue(java.util.Collection<MeasuredResults> value) {
                this.value = value;
            }
            
            public java.util.Collection<MeasuredResults> getValue() {
                return this.value;
            }            
            
            public void initValue() {
                setValue(new java.util.LinkedList<MeasuredResults>()); 
            }
            
            public void add(MeasuredResults item) {
                value.add(item);
            }

	    public void initWithDefaults() {
	    }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(MeasuredResultsList.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }


    }
            