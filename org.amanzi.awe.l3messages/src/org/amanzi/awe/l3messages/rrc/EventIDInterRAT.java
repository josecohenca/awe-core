
package org.amanzi.awe.l3messages.rrc;
//
// This file was generated by the BinaryNotes compiler.
// See http://bnotes.sourceforge.net 
// Any modifications to this file will be lost upon recompilation of the source ASN.1. 
//

import org.bn.CoderFactory;
import org.bn.annotations.ASN1Enum;
import org.bn.annotations.ASN1EnumItem;
import org.bn.annotations.ASN1PreparedElement;
import org.bn.coders.IASN1PreparedElement;
import org.bn.coders.IASN1PreparedElementData;




    @ASN1PreparedElement
    @ASN1Enum (
        name = "EventIDInterRAT"
    )
    public class EventIDInterRAT implements IASN1PreparedElement {        
        public enum EnumType {
            
            @ASN1EnumItem ( name = "e3a", hasTag = true , tag = 0 )
            e3a , 
            @ASN1EnumItem ( name = "e3b", hasTag = true , tag = 1 )
            e3b , 
            @ASN1EnumItem ( name = "e3c", hasTag = true , tag = 2 )
            e3c , 
            @ASN1EnumItem ( name = "e3d", hasTag = true , tag = 3 )
            e3d , 
        }
        
        private EnumType value;
        private Integer integerForm;
        
        public EnumType getValue() {
            return this.value;
        }
        
        public void setValue(EnumType value) {
            this.value = value;
        }
        
        public Integer getIntegerForm() {
            return integerForm;
        }
        
        public void setIntegerForm(Integer value) {
            integerForm = value;
        }

	    public void initWithDefaults() {
	    }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(EventIDInterRAT.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }


    }
            