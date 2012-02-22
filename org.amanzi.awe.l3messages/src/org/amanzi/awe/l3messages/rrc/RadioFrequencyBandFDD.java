
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
        name = "RadioFrequencyBandFDD"
    )
    public class RadioFrequencyBandFDD implements IASN1PreparedElement {        
        public enum EnumType {
            
            @ASN1EnumItem ( name = "fdd2100", hasTag = true , tag = 0 )
            fdd2100 , 
            @ASN1EnumItem ( name = "fdd1900", hasTag = true , tag = 1 )
            fdd1900 , 
            @ASN1EnumItem ( name = "fdd1800", hasTag = true , tag = 2 )
            fdd1800 , 
            @ASN1EnumItem ( name = "bandVI", hasTag = true , tag = 3 )
            bandVI , 
            @ASN1EnumItem ( name = "bandIV", hasTag = true , tag = 4 )
            bandIV , 
            @ASN1EnumItem ( name = "bandV", hasTag = true , tag = 5 )
            bandV , 
            @ASN1EnumItem ( name = "bandVII", hasTag = true , tag = 6 )
            bandVII , 
            @ASN1EnumItem ( name = "extension-indicator", hasTag = true , tag = 7 )
            extension_indicator , 
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

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(RadioFrequencyBandFDD.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }


    }
            