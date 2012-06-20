
package org.amanzi.awe.l3messages.rrc;
//
// This file was generated by the BinaryNotes compiler.
// See http://bnotes.sourceforge.net 
// Any modifications to this file will be lost upon recompilation of the source ASN.1. 
//

import org.bn.CoderFactory;
import org.bn.annotations.ASN1Element;
import org.bn.annotations.ASN1PreparedElement;
import org.bn.annotations.ASN1Sequence;
import org.bn.coders.IASN1PreparedElement;
import org.bn.coders.IASN1PreparedElementData;




    @ASN1PreparedElement
    @ASN1Sequence ( name = "SIR_MeasurementResults", isSet = false )
    public class SIR_MeasurementResults implements IASN1PreparedElement {
            
        @ASN1Element ( name = "tfcs-ID", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private TFCS_IdentityPlain tfcs_ID = null;
                
  
        @ASN1Element ( name = "sir-TimeslotList", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private SIR_TimeslotList sir_TimeslotList = null;
                
  
        
        public TFCS_IdentityPlain getTfcs_ID () {
            return this.tfcs_ID;
        }

        

        public void setTfcs_ID (TFCS_IdentityPlain value) {
            this.tfcs_ID = value;
        }
        
  
        
        public SIR_TimeslotList getSir_TimeslotList () {
            return this.sir_TimeslotList;
        }

        

        public void setSir_TimeslotList (SIR_TimeslotList value) {
            this.sir_TimeslotList = value;
        }
        
  
                    
        
        public void initWithDefaults() {
            
        }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(SIR_MeasurementResults.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }

            
    }
            