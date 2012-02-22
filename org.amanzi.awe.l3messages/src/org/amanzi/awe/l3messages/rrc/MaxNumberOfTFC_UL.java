
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
        name = "MaxNumberOfTFC_UL"
    )
    public class MaxNumberOfTFC_UL implements IASN1PreparedElement {        
        public enum EnumType {
            
            @ASN1EnumItem ( name = "dummy1", hasTag = true , tag = 0 )
            dummy1 , 
            @ASN1EnumItem ( name = "dummy2", hasTag = true , tag = 1 )
            dummy2 , 
            @ASN1EnumItem ( name = "tfc16", hasTag = true , tag = 2 )
            tfc16 , 
            @ASN1EnumItem ( name = "tfc32", hasTag = true , tag = 3 )
            tfc32 , 
            @ASN1EnumItem ( name = "tfc48", hasTag = true , tag = 4 )
            tfc48 , 
            @ASN1EnumItem ( name = "tfc64", hasTag = true , tag = 5 )
            tfc64 , 
            @ASN1EnumItem ( name = "tfc96", hasTag = true , tag = 6 )
            tfc96 , 
            @ASN1EnumItem ( name = "tfc128", hasTag = true , tag = 7 )
            tfc128 , 
            @ASN1EnumItem ( name = "tfc256", hasTag = true , tag = 8 )
            tfc256 , 
            @ASN1EnumItem ( name = "tfc512", hasTag = true , tag = 9 )
            tfc512 , 
            @ASN1EnumItem ( name = "tfc1024", hasTag = true , tag = 10 )
            tfc1024 , 
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

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(MaxNumberOfTFC_UL.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }


    }
            