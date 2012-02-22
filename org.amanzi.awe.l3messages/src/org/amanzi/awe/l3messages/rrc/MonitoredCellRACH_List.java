
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
    @ASN1BoxedType ( name = "MonitoredCellRACH_List" )
    public class MonitoredCellRACH_List implements IASN1PreparedElement {
                
            @ASN1ValueRangeConstraint ( 
		
		min = 1L, 
		
		max = 8L 
		
	   )
	   
            @ASN1SequenceOf( name = "MonitoredCellRACH-List" , isSetOf = false)
	    private java.util.Collection<MonitoredCellRACH_Result> value = null; 
    
            public MonitoredCellRACH_List () {
            }
        
            public MonitoredCellRACH_List ( java.util.Collection<MonitoredCellRACH_Result> value ) {
                setValue(value);
            }
                        
            public void setValue(java.util.Collection<MonitoredCellRACH_Result> value) {
                this.value = value;
            }
            
            public java.util.Collection<MonitoredCellRACH_Result> getValue() {
                return this.value;
            }            
            
            public void initValue() {
                setValue(new java.util.LinkedList<MonitoredCellRACH_Result>()); 
            }
            
            public void add(MonitoredCellRACH_Result item) {
                value.add(item);
            }

	    public void initWithDefaults() {
	    }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(MonitoredCellRACH_List.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }


    }
            