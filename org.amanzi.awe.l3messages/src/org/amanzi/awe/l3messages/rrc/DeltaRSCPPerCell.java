
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
    @ASN1Sequence ( name = "DeltaRSCPPerCell", isSet = false )
    public class DeltaRSCPPerCell implements IASN1PreparedElement {
            
        @ASN1Element ( name = "deltaRSCP", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private DeltaRSCP deltaRSCP = null;
                
  
        
        public DeltaRSCP getDeltaRSCP () {
            return this.deltaRSCP;
        }

        
        public boolean isDeltaRSCPPresent () {
            return this.deltaRSCP != null;
        }
        

        public void setDeltaRSCP (DeltaRSCP value) {
            this.deltaRSCP = value;
        }
        
  
                    
        
        public void initWithDefaults() {
            
        }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(DeltaRSCPPerCell.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }

            
    }
            