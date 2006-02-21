/**
 * BirtViewerAPI.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Sep 06, 2005 (12:48:20 PDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.endpoint;

public interface BirtViewerAPI extends javax.xml.rpc.Service {
    public java.lang.String getBirtSoapPortAddress();

    public org.eclipse.birt.report.soapengine.endpoint.BirtSoapPort getBirtSoapPort() throws javax.xml.rpc.ServiceException;

    public org.eclipse.birt.report.soapengine.endpoint.BirtSoapPort getBirtSoapPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
