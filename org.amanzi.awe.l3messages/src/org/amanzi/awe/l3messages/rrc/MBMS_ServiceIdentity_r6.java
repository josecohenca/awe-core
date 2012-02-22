
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
    @ASN1Sequence ( name = "MBMS_ServiceIdentity_r6", isSet = false )
    public class MBMS_ServiceIdentity_r6 implements IASN1PreparedElement {
            @ASN1OctetString( name = "" )
    
            @ASN1SizeConstraint ( max = 3L )
        
        @ASN1Element ( name = "serviceIdentity", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private byte[] serviceIdentity = null;
                
  
        
    @ASN1PreparedElement
    @ASN1Choice ( name = "plmn_Identity" )
    public static class Plmn_IdentityChoiceType implements IASN1PreparedElement {
            
        @ASN1Null ( name = "sameAsMIB-PLMN-Id" ) 
    
        @ASN1Element ( name = "sameAsMIB-PLMN-Id", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private org.bn.types.NullObject sameAsMIB_PLMN_Id = null;
                
  
        
    @ASN1PreparedElement
    @ASN1Choice ( name = "other" )
    public static class OtherChoiceType implements IASN1PreparedElement {
            @ASN1Integer( name = "" )
    @ASN1ValueRangeConstraint ( 
		
		min = 1L, 
		
		max = 5L 
		
	   )
	   
        @ASN1Element ( name = "sameAsMIB-MultiPLMN-Id", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private Integer sameAsMIB_MultiPLMN_Id = null;
                
  
        @ASN1Element ( name = "explicitPLMN-Id", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private PLMN_Identity explicitPLMN_Id = null;
                
  
        
        public Integer getSameAsMIB_MultiPLMN_Id () {
            return this.sameAsMIB_MultiPLMN_Id;
        }

        public boolean isSameAsMIB_MultiPLMN_IdSelected () {
            return this.sameAsMIB_MultiPLMN_Id != null;
        }

        private void setSameAsMIB_MultiPLMN_Id (Integer value) {
            this.sameAsMIB_MultiPLMN_Id = value;
        }

        
        public void selectSameAsMIB_MultiPLMN_Id (Integer value) {
            this.sameAsMIB_MultiPLMN_Id = value;
            
                    setSameAsMIB_MultiPLMN_Id(null);
                
                    setExplicitPLMN_Id(null);
                            
        }

        
  
        
        public PLMN_Identity getExplicitPLMN_Id () {
            return this.explicitPLMN_Id;
        }

        public boolean isExplicitPLMN_IdSelected () {
            return this.explicitPLMN_Id != null;
        }

        private void setExplicitPLMN_Id (PLMN_Identity value) {
            this.explicitPLMN_Id = value;
        }

        
        public void selectExplicitPLMN_Id (PLMN_Identity value) {
            this.explicitPLMN_Id = value;
            
                    setSameAsMIB_MultiPLMN_Id(null);
                
                    setExplicitPLMN_Id(null);
                            
        }

        
  

	    public void initWithDefaults() {
	    }

        public IASN1PreparedElementData getPreparedData() {
            return preparedData_OtherChoiceType;
        }

        private static IASN1PreparedElementData preparedData_OtherChoiceType = CoderFactory.getInstance().newPreparedElementData(OtherChoiceType.class);

    }

                
        @ASN1Element ( name = "other", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private OtherChoiceType other = null;
                
  
        
        public org.bn.types.NullObject getSameAsMIB_PLMN_Id () {
            return this.sameAsMIB_PLMN_Id;
        }

        public boolean isSameAsMIB_PLMN_IdSelected () {
            return this.sameAsMIB_PLMN_Id != null;
        }

        private void setSameAsMIB_PLMN_Id (org.bn.types.NullObject value) {
            this.sameAsMIB_PLMN_Id = value;
        }

        
        public void selectSameAsMIB_PLMN_Id () {
            selectSameAsMIB_PLMN_Id (new org.bn.types.NullObject());
	}
	
        public void selectSameAsMIB_PLMN_Id (org.bn.types.NullObject value) {
            this.sameAsMIB_PLMN_Id = value;
            
                    setSameAsMIB_PLMN_Id(null);
                
                    setOther(null);
                            
        }

        
  
        
        public OtherChoiceType getOther () {
            return this.other;
        }

        public boolean isOtherSelected () {
            return this.other != null;
        }

        private void setOther (OtherChoiceType value) {
            this.other = value;
        }

        
        public void selectOther (OtherChoiceType value) {
            this.other = value;
            
                    setSameAsMIB_PLMN_Id(null);
                            
        }

        
  

	    public void initWithDefaults() {
	    }

        public IASN1PreparedElementData getPreparedData() {
            return preparedData_Plmn_IdentityChoiceType;
        }

        private static IASN1PreparedElementData preparedData_Plmn_IdentityChoiceType = CoderFactory.getInstance().newPreparedElementData(Plmn_IdentityChoiceType.class);

    }

                
        @ASN1Element ( name = "plmn_Identity", isOptional =  false , hasTag =  false  , hasDefaultValue =  false  )
    
	private Plmn_IdentityChoiceType plmn_Identity = null;
                
  
        
        public byte[] getServiceIdentity () {
            return this.serviceIdentity;
        }

        

        public void setServiceIdentity (byte[] value) {
            this.serviceIdentity = value;
        }
        
  
        
        public Plmn_IdentityChoiceType getPlmn_Identity () {
            return this.plmn_Identity;
        }

        

        public void setPlmn_Identity (Plmn_IdentityChoiceType value) {
            this.plmn_Identity = value;
        }
        
  
                    
        
        public void initWithDefaults() {
            
        }

        private static IASN1PreparedElementData preparedData = CoderFactory.getInstance().newPreparedElementData(MBMS_ServiceIdentity_r6.class);
        public IASN1PreparedElementData getPreparedData() {
            return preparedData;
        }

            
    }
            