
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
        name = "MaxHcContextSpace"
    )
    public class MaxHcContextSpace implements IASN1PreparedElement {        
        public enum EnumType {
            
            @ASN1EnumItem ( name = "dummy", hasTag = true , tag = 0 )
            dummy , 
            @ASN1EnumItem ( name = "by1024", hasTag = true , tag = 1 )
            by1024 , 
            @ASN1EnumItem ( name = "by2048", hasTag = true , tag = 2 )
            by2048 , 
            @ASN1EnumItem ( name = "by4096", hasTag = true , tag = 3 )
            by4096 , 
            @ASN1EnumItem ( name = "by8192", hasTag = true , tag = 4 )
            by8192 , 
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

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(MaxHcContextSpace.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }


    }
            