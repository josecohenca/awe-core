
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
    @ASN1Sequence ( name = "CellToReport", isSet = false )
    public class CellToReport implements IASN1PreparedElement {
            
        @ASN1Element ( name = "bsicReported", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private BSICReported bsicReported = null;
                
  
        
        public BSICReported getBsicReported () {
            return this.bsicReported;
        }

        

        public void setBsicReported (BSICReported value) {
            this.bsicReported = value;
        }
        
  
                    
        
        public void initWithDefaults() {
            
        }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(CellToReport.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }

            
    }
            