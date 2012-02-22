
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
        name = "MaxTransportBlocksDL"
    )
    public class MaxTransportBlocksDL implements IASN1PreparedElement {        
        public enum EnumType {
            
            @ASN1EnumItem ( name = "tb4", hasTag = true , tag = 0 )
            tb4 , 
            @ASN1EnumItem ( name = "tb8", hasTag = true , tag = 1 )
            tb8 , 
            @ASN1EnumItem ( name = "tb16", hasTag = true , tag = 2 )
            tb16 , 
            @ASN1EnumItem ( name = "tb32", hasTag = true , tag = 3 )
            tb32 , 
            @ASN1EnumItem ( name = "tb48", hasTag = true , tag = 4 )
            tb48 , 
            @ASN1EnumItem ( name = "tb64", hasTag = true , tag = 5 )
            tb64 , 
            @ASN1EnumItem ( name = "tb96", hasTag = true , tag = 6 )
            tb96 , 
            @ASN1EnumItem ( name = "tb128", hasTag = true , tag = 7 )
            tb128 , 
            @ASN1EnumItem ( name = "tb256", hasTag = true , tag = 8 )
            tb256 , 
            @ASN1EnumItem ( name = "tb512", hasTag = true , tag = 9 )
            tb512 , 
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

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(MaxTransportBlocksDL.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }


    }
            