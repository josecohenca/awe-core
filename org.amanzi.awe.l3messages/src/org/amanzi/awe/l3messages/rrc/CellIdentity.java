
package org.amanzi.awe.l3messages.rrc;
//
// This file was generated by the BinaryNotes compiler.
// See http://bnotes.sourceforge.net 
// Any modifications to this file will be lost upon recompilation of the source ASN.1. 
//

import org.bn.CoderFactory;
import org.bn.annotations.ASN1BitString;
import org.bn.annotations.ASN1BoxedType;
import org.bn.annotations.ASN1PreparedElement;
import org.bn.annotations.constraints.ASN1SizeConstraint;
import org.bn.coders.IASN1PreparedElement;
import org.bn.coders.IASN1PreparedElementData;
import org.bn.types.BitString;




    @ASN1PreparedElement
    @ASN1BoxedType ( name = "CellIdentity" )
    public class CellIdentity implements IASN1PreparedElement {
    
            @ASN1BitString( name = "CellIdentity" )
            
            @ASN1SizeConstraint ( max = 28L )
        
            private BitString value = null;
            
            public CellIdentity() {
            }

            public CellIdentity(BitString value) {
                this.value = value;
            }
            
            public void setValue(BitString value) {
                this.value = value;
            }
            
            public BitString getValue() {
                return this.value;
            }

	    public void initWithDefaults() {
	    }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(CellIdentity.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }

    }
            