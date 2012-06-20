
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
    @ASN1Sequence ( name = "HandoverFromUtranFailure_v590ext_IEs", isSet = false )
    public class HandoverFromUtranFailure_v590ext_IEs implements IASN1PreparedElement {
            
        @ASN1Element ( name = "geranIu-MessageList", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private GERANIu_MessageList geranIu_MessageList = null;
                
  
        
        public GERANIu_MessageList getGeranIu_MessageList () {
            return this.geranIu_MessageList;
        }

        
        public boolean isGeranIu_MessageListPresent () {
            return this.geranIu_MessageList != null;
        }
        

        public void setGeranIu_MessageList (GERANIu_MessageList value) {
            this.geranIu_MessageList = value;
        }
        
  
                    
        
        public void initWithDefaults() {
            
        }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(HandoverFromUtranFailure_v590ext_IEs.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }

            
    }
            