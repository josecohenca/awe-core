
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
    @ASN1Sequence ( name = "RRCConnectionSetupComplete_r3_add_ext_IEs", isSet = false )
    public class RRCConnectionSetupComplete_r3_add_ext_IEs implements IASN1PreparedElement {
            
        @ASN1Element ( name = "rrcConnectionSetupComplete-v650ext", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private RRCConnectionSetupComplete_v650ext_IEs rrcConnectionSetupComplete_v650ext = null;
                
  

       @ASN1PreparedElement
       @ASN1Sequence ( name = "v680NonCriticalExtensions" , isSet = false )
       public static class V680NonCriticalExtensionsSequenceType implements IASN1PreparedElement {
                
        @ASN1Element ( name = "rrcConnectionSetupComplete-v680ext", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private RRCConnectionSetupComplete_v680ext_IEs rrcConnectionSetupComplete_v680ext = null;
                
  

       @ASN1PreparedElement
       @ASN1Sequence ( name = "nonCriticalExtensions" , isSet = false )
       public static class NonCriticalExtensionsSequenceType implements IASN1PreparedElement {
                
                
                
        public void initWithDefaults() {
            
        }

        public IASN1PreparedElementData getPreparedData() {
            return preparedData_NonCriticalExtensionsSequenceType;
        }

       private static IASN1PreparedElementData preparedData_NonCriticalExtensionsSequenceType = CoderFactory.getInstance().newPreparedElementData(NonCriticalExtensionsSequenceType.class);
                
       }

       
                
        @ASN1Element ( name = "nonCriticalExtensions", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private NonCriticalExtensionsSequenceType nonCriticalExtensions = null;
                
  
        
        public RRCConnectionSetupComplete_v680ext_IEs getRrcConnectionSetupComplete_v680ext () {
            return this.rrcConnectionSetupComplete_v680ext;
        }

        

        public void setRrcConnectionSetupComplete_v680ext (RRCConnectionSetupComplete_v680ext_IEs value) {
            this.rrcConnectionSetupComplete_v680ext = value;
        }
        
  
        
        public NonCriticalExtensionsSequenceType getNonCriticalExtensions () {
            return this.nonCriticalExtensions;
        }

        
        public boolean isNonCriticalExtensionsPresent () {
            return this.nonCriticalExtensions != null;
        }
        

        public void setNonCriticalExtensions (NonCriticalExtensionsSequenceType value) {
            this.nonCriticalExtensions = value;
        }
        
  
                
                
        public void initWithDefaults() {
            
        }

        public IASN1PreparedElementData getPreparedData() {
            return preparedData_V680NonCriticalExtensionsSequenceType;
        }

       private static IASN1PreparedElementData preparedData_V680NonCriticalExtensionsSequenceType = CoderFactory.getInstance().newPreparedElementData(V680NonCriticalExtensionsSequenceType.class);
                
       }

       
                
        @ASN1Element ( name = "v680NonCriticalExtensions", isOptional =  true , hasTag =  false  , hasDefaultValue =  false  )
    
	private V680NonCriticalExtensionsSequenceType v680NonCriticalExtensions = null;
                
  
        
        public RRCConnectionSetupComplete_v650ext_IEs getRrcConnectionSetupComplete_v650ext () {
            return this.rrcConnectionSetupComplete_v650ext;
        }

        
        public boolean isRrcConnectionSetupComplete_v650extPresent () {
            return this.rrcConnectionSetupComplete_v650ext != null;
        }
        

        public void setRrcConnectionSetupComplete_v650ext (RRCConnectionSetupComplete_v650ext_IEs value) {
            this.rrcConnectionSetupComplete_v650ext = value;
        }
        
  
        
        public V680NonCriticalExtensionsSequenceType getV680NonCriticalExtensions () {
            return this.v680NonCriticalExtensions;
        }

        
        public boolean isV680NonCriticalExtensionsPresent () {
            return this.v680NonCriticalExtensions != null;
        }
        

        public void setV680NonCriticalExtensions (V680NonCriticalExtensionsSequenceType value) {
            this.v680NonCriticalExtensions = value;
        }
        
  
                    
        
        public void initWithDefaults() {
            
        }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(RRCConnectionSetupComplete_r3_add_ext_IEs.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }

            
    }
            