/**
 * BirtSoapBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.2.1 Jun 14, 2005 (09:15:57 EDT) WSDL2Java emitter.
 */

package org.eclipse.birt.report.soapengine.endpoint;


public class BirtSoapBindingStub extends org.apache.axis.client.Stub implements org.eclipse.birt.report.soapengine.endpoint.BirtSoapPort {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[1];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getUpdatedObjects");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "GetUpdatedObjects"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "GetUpdatedObjects"), org.eclipse.birt.report.soapengine.api.GetUpdatedObjects.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "GetUpdatedObjectsResponse"));
        oper.setReturnClass(org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "GetUpdatedObjectsResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

    }

    public BirtSoapBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public BirtSoapBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public BirtSoapBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", ">ReportId>Type");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.ReportIdType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "CascadeParameter");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.CascadeParameter.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Column");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.Column.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Columns");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.Columns.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Data");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.Data.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Export");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.Export.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ExportCriteria");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.ExportCriteria.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Font");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.Font.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Format");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.Format.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "GetUpdatedObjects");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.GetUpdatedObjects.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "GetUpdatedObjectsResponse");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "NumericFormat");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.NumericFormat.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "NumericFormatString");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.NumericFormatString.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Operation");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.Operation.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Oprand");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.Oprand.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Page");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.Page.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "ReportId");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.ReportId.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Select");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.Select.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SelectionList");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.SelectionList.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "SimpleFilter");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.SimpleFilter.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "TOC");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.TOC.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Update");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.Update.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "UpdateContent");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.UpdateContent.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "UpdateData");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.UpdateData.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://schemas.eclipse.org/birt", "Vector");
            cachedSerQNames.add(qName);
            cls = org.eclipse.birt.report.soapengine.api.Vector.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse getUpdatedObjects(org.eclipse.birt.report.soapengine.api.GetUpdatedObjects request) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getUpdatedObjects"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {request});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse) org.apache.axis.utils.JavaUtils.convert(_resp, org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
