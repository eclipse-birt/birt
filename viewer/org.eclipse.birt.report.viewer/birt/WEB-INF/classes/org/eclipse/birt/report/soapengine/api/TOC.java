/**
 * TOC.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.api;

public class TOC  implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
	private java.lang.String id;
    private java.lang.String displayName;
    private java.lang.String bookmark;
    private java.lang.String style;
    private java.lang.Boolean isLeaf;
    private org.eclipse.birt.report.soapengine.api.TOC[] child;

    public TOC() {
    }

    public TOC(
           java.lang.String id,
           java.lang.String displayName,
           java.lang.String bookmark,
           java.lang.String style,
           java.lang.Boolean isLeaf,
           org.eclipse.birt.report.soapengine.api.TOC[] child) {
           this.id = id;
           this.displayName = displayName;
           this.bookmark = bookmark;
           this.style = style;
           this.isLeaf = isLeaf;
           this.child = child;
    }


    /**
     * Gets the id value for this TOC.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this TOC.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the displayName value for this TOC.
     * 
     * @return displayName
     */
    public java.lang.String getDisplayName() {
        return displayName;
    }


    /**
     * Sets the displayName value for this TOC.
     * 
     * @param displayName
     */
    public void setDisplayName(java.lang.String displayName) {
        this.displayName = displayName;
    }


    /**
     * Gets the bookmark value for this TOC.
     * 
     * @return bookmark
     */
    public java.lang.String getBookmark() {
        return bookmark;
    }


    /**
     * Sets the bookmark value for this TOC.
     * 
     * @param bookmark
     */
    public void setBookmark(java.lang.String bookmark) {
        this.bookmark = bookmark;
    }

    /**
     * Gets the style value for this TOC.
     * 
     * @return style
     */
    public java.lang.String getStyle() {
        return style;
    }


    /**
     * Sets the style value for this TOC.
     * 
     * @param style
     */
    public void setStyle(java.lang.String style) {
        this.style = style;
    }

    /**
     * Gets the isLeaf value for this TOC.
     * 
     * @return isLeaf
     */
    public java.lang.Boolean getIsLeaf() {
        return isLeaf;
    }


    /**
     * Sets the isLeaf value for this TOC.
     * 
     * @param isLeaf
     */
    public void setIsLeaf(java.lang.Boolean isLeaf) {
        this.isLeaf = isLeaf;
    }


    /**
     * Gets the child value for this TOC.
     * 
     * @return child
     */
    public org.eclipse.birt.report.soapengine.api.TOC[] getChild() {
        return child;
    }


    /**
     * Sets the child value for this TOC.
     * 
     * @param child
     */
    public void setChild(org.eclipse.birt.report.soapengine.api.TOC[] child) {
        this.child = child;
    }

    public org.eclipse.birt.report.soapengine.api.TOC getChild(int i) {
        return this.child[i];
    }

    public void setChild(int i, org.eclipse.birt.report.soapengine.api.TOC _value) {
        this.child[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TOC)) return false;
        TOC other = (TOC) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.displayName==null && other.getDisplayName()==null) || 
             (this.displayName!=null &&
              this.displayName.equals(other.getDisplayName()))) &&
            ((this.bookmark==null && other.getBookmark()==null) || 
             (this.bookmark!=null &&
              this.bookmark.equals(other.getBookmark()))) &&
            ((this.style==null && other.getStyle()==null) || 
             (this.style!=null &&
              this.style.equals(other.getStyle()))) &&
              ((this.isLeaf==null && other.getIsLeaf()==null) || 
             (this.isLeaf!=null &&
              this.isLeaf.equals(other.getIsLeaf()))) &&
            ((this.child==null && other.getChild()==null) || 
             (this.child!=null &&
              java.util.Arrays.equals(this.child, other.getChild())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getDisplayName() != null) {
            _hashCode += getDisplayName().hashCode();
        }
        if (getBookmark() != null) {
            _hashCode += getBookmark().hashCode();
        }
        if (getStyle() != null) {
            _hashCode += getStyle().hashCode();
        }        
        if (getIsLeaf() != null) {
            _hashCode += getIsLeaf().hashCode();
        }
        if (getChild() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getChild());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getChild(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TOC.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TOC"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("displayName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "DisplayName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bookmark");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Bookmark"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("style");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Style"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isLeaf");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "IsLeaf"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("child");
        elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Child"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TOC"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
