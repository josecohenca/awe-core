
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
    @ASN1Enum (
        name = "MaxNumberOfTF"
    )
    public class MaxNumberOfTF implements IASN1PreparedElement {        
        public enum EnumType {
            
            @ASN1EnumItem ( name = "tf32", hasTag = true , tag = 0 )
            tf32 , 
            @ASN1EnumItem ( name = "tf64", hasTag = true , tag = 1 )
            tf64 , 
            @ASN1EnumItem ( name = "tf128", hasTag = true , tag = 2 )
            tf128 , 
            @ASN1EnumItem ( name = "tf256", hasTag = true , tag = 3 )
            tf256 , 
            @ASN1EnumItem ( name = "tf512", hasTag = true , tag = 4 )
            tf512 , 
            @ASN1EnumItem ( name = "tf1024", hasTag = true , tag = 5 )
            tf1024 , 
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

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(MaxNumberOfTF.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }


    }
            