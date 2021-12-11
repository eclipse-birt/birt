/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2003-2018 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://oss.oracle.com/licenses/CDDL+GPL-1.1
 * or LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package javax.xml.rpc;

import java.util.Iterator;
import java.net.URL;
import javax.xml.namespace.QName;
import java.util.Map;
import java.util.List;

/** The <code>javax.xml.rpc.Call</code> interface provides support 
 *  for the dynamic invocation of a service endpoint. The 
 *  <code>javax.xml.rpc.Service</code> interface acts as a factory
 *  for the creation of <code>Call</code> instances.
 *
 *  <p>Once a <code>Call</code> instance is created, various setter 
 *  and getter methods may be used to configure this <code>Call</code>
 *  instance.</p>
 * 
 *  @version 1.0
 *  @author  Rahul Sharma
**/

public interface Call {

  /** Standard property: User name for authentication
   *  <p>Type: <code>java.lang.String</code>
  **/
  public static final String USERNAME_PROPERTY = 
                      "javax.xml.rpc.security.auth.username";
  
  /** Standard property: Password for authentication
   *  <p>Type: <code>java.lang.String</code>
  **/
  public static final String PASSWORD_PROPERTY = 
                      "javax.xml.rpc.security.auth.password";

  /** Standard property for operation style. This property is
   *  set to "rpc" if the operation style is rpc; "document" 
   *  if the operation style is document.
   *  <p>Type: <code>java.lang.String</code>
  **/
  public static final String OPERATION_STYLE_PROPERTY =  
                      "javax.xml.rpc.soap.operation.style";

  /** Standard property for SOAPAction. This boolean property 
   *  indicates whether or not SOAPAction is to be used. The 
   *  default value of this property is false indicating that
   *  the SOAPAction is not used.
   *  <p>Type: <code>java.lang.Boolean</code>
  **/
  public static final String SOAPACTION_USE_PROPERTY =  
                      "javax.xml.rpc.soap.http.soapaction.use";

  /** Standard property for SOAPAction. Indicates the SOAPAction 
   *  URI if the <code>javax.xml.rpc.soap.http.soapaction.use</code>
   *  property is set to <code>true</code>.
   *  <p>Type: <code>java.lang.String</code>
  **/
  public static final String SOAPACTION_URI_PROPERTY =  
                      "javax.xml.rpc.soap.http.soapaction.uri";


  /** Standard property for encoding Style:  Encoding style specified
   *  as a namespace URI. The default value is the SOAP 1.1 encoding
   *  <code>http://schemas.xmlsoap.org/soap/encoding/</code>
   * <p>Type: <code>java.lang.String</code>
  **/
  public static final String ENCODINGSTYLE_URI_PROPERTY =
                      "javax.xml.rpc.encodingstyle.namespace.uri";

  /** Standard property: This boolean property is used by a service 
   *  client to indicate whether or not it wants to participate in
   *  a session with a service endpoint. If this property is set to
   *  true, the service client indicates that it wants the session
   *  to be maintained. If set to false, the session is not maintained. 
   *  The default value for this property is <code>false</code>.
   *  <p>Type: <code>java.lang.Boolean</code>
  **/
  public static final String SESSION_MAINTAIN_PROPERTY =
                      "javax.xml.rpc.session.maintain";

  /** Indicates whether <code>addParameter</code> and 
   *  <code>setReturnType</code> methods
   *  are to be invoked to specify the parameter and return type
   *  specification for a specific operation.
   * 
   *  @param   operationName Qualified name of the operation
   *
   *  @return  Returns true if the Call implementation class
   *           requires addParameter and setReturnType to be 
   *           invoked in the client code for the specified 
   *           operation. This method returns false otherwise.
   *  @throws  java.lang.IllegalArgumentException If invalid
   *           operation name is specified
  **/
  public boolean isParameterAndReturnSpecRequired(QName operationName);

  /** Adds a parameter type and mode for a specific  operation. 
   *  Note that the client code may not call any 
   *  <code>addParameter</code> and <code>setReturnType</code> 
   *  methods before calling the <code>invoke</code> method. In
   *  this case, the Call implementation class determines the 
   *  parameter types by using reflection on parameters, using
   *  the WSDL description and configured type mapping registry.
   * 
   *  @param  paramName  Name of the parameter
   *  @param  xmlType    XML type of the parameter
   *  @param  parameterMode Mode of the parameter-whether
   *                     <code>ParameterMode.IN</code>,
   *                     <code>ParameterMode.OUT</code>,
   *                     or <code>ParameterMode.INOUT</code>,
   *
   *  @throws javax.xml.rpc.JAXRPCException: This exception may
   *          be thrown if the method <code>isParameterAndReturnSpecRequired</code>
   *          returns <code>false</code> for this operation.
   *  @throws java.lang.IllegalArgumentException If any illegal
   *          parameter name or XML type is specified
   *  @see javax.xml.rpc.Call#isParameterAndReturnSpecRequired
  **/
  public void addParameter(String paramName, 
			   QName xmlType,
			   ParameterMode parameterMode);

  /** Adds a parameter type and mode for a specific  operation. 
   *  This method is used to specify the Java type for either 
   *  OUT or INOUT parameters. 
   * 
   *  @param  paramName  Name of the parameter
   *  @param  xmlType    XML type of the parameter
   *  @param  javaType   Java class of the parameter
   *  @param  parameterMode Mode of the parameter-whether
   *                     ParameterMode.IN, OUT or INOUT
   *
   *  @throws javax.xml.rpc.JAXRPCException 
   *          <UL>
   *          <LI>This exception may be thrown if this method is
   *          invoked when the method <code>isParameterAndReturnSpecRequired</code>
   *          returns <code>false</code>.
   *          <LI>If specified XML type and Java type mapping 
   *          is not valid. For example, <code>TypeMappingRegistry</code>
   *          has no serializers for this mapping.
   *          </UL>
   *  @throws java.lang.IllegalArgumentException If any illegal
   *          parameter name or XML type is specified
   *  @throws java.lang.UnsupportedOperationException If this
   *          method is not supported
   *  @see javax.xml.rpc.Call#isParameterAndReturnSpecRequired
  **/
  public void addParameter(String paramName, 
			   QName xmlType,
			   Class javaType,
			   ParameterMode parameterMode);

  /** Gets the XML type of a parameter by name
   *
   *  @param paramName  Name of the parameter
   *  @return Returns XML type for the specified parameter
  **/
  public QName getParameterTypeByName(String paramName);

  /** Sets the return type for a specific operation. Invoking
   *  <code>setReturnType(null)</code> removes the return
   *  type for this Call object.
   *
   *  @param  xmlType  XML data type of the return value
   *  @throws javax.xml.rpc.JAXRPCException This exception
   *          may be thrown when the method 
   *          <code>isParameterAndReturnSpecRequired</code> returns
   *          <code>false</code>.
   *  @throws java.lang.IllegalArgumentException If an illegal
   *          XML type is specified
  **/
  public void setReturnType(QName xmlType);

  /** Sets the return type for a specific operation.
   *
   *  @param  xmlType  XML data type of the return value
   *  @param  javaType Java Class of the return value
   *  @throws javax.xml.rpc.JAXRPCException 
   *          <UL>
   *          <LI>This exception may be thrown if this method is
   *          invoked when the method <code>isParameterAndReturnSpecRequired</code>
   *          returns <code>false</code>.
   *          <LI>If XML type and Java type cannot be mapped
   *          using the standard type mapping or TypeMapping
   *          registry
   *          </UL>
   *  @throws java.lang.UnsupportedOperationException If this
   *          method is not supported
   *  @throws java.lang.IllegalArgumentException If an illegal
   *          XML type is specified
  **/
  public void setReturnType(QName xmlType, Class javaType);

  /** Gets the return type for a specific operation
   *
   *  @return Returns the XML type for the return value
  **/
  public QName getReturnType();
  
  /** Removes all specified parameters from this <code>Call</code> instance.
   *  Note that this method removes only the parameters and not
   *  the return type. The <code>setReturnType(null)</code> is
   *  used to remove the return type.
   *
   *  @throws javax.xml.rpc.JAXRPCException This exception may be
   *          thrown If this method is called when the method 
   *          <code>isParameterAndReturnSpecRequired</code>
   *          returns <code>false</code> for this Call's operation.
  **/
  public void removeAllParameters();

  /** Gets the name of the operation to be invoked using this
   *  <code>Call</code> instance.
   *  @return Qualified name of the operation
  **/
  public QName getOperationName();  

  /** Sets the name of the operation to be invoked using this
   *  <code>Call</code> instance.
   *  @param  operationName QName of the operation to be
   *                        invoked using the Call instance
  **/
  public void setOperationName(QName operationName);

  /** Gets the qualified name of the port type.
   *
   *  @return Qualified name of the port type
  **/
  public QName getPortTypeName();

  /** Sets the qualified name of the port type.
   *
   *  @param portType Qualified name of the port type
  **/
  public void setPortTypeName(QName portType);

  /** Sets the address of the target service endpoint.
   *  This address must correspond to the transport specified
   *  in the binding for this <code>Call</code> instance.
   *
   *  @param address  Address of the target service endpoint; 
   *                  specified as an URI
  **/
  public void setTargetEndpointAddress(String address);

  /** Gets the address of a target service endpoint.
   *
   *  @return Address of the target service endpoint as an URI
  **/
  public String getTargetEndpointAddress();

  /** Sets the value for a named property. JAX-RPC specification 
   *  specifies a standard set of properties that may be passed 
   *  to the <code>Call.setProperty</code> method. 
   *
   *  @param name  Name of the property
   *  @param value Value of the property
   *
   *  @throws javax.xml.rpc.JAXRPCException
   *          <UL>
   *          <LI>If an optional standard property name is 
   *              specified, however this <code>Call</code> implementation
   *              class does not support the configuration of
   *              this property.
   *          <LI>If an invalid (or unsupported) property name is
   *              specified or if a value of mismatched property
   *              type is passed. 
   *          <LI>If there is any error in the configuration of
   *              a valid property.
   *          </UL>
  **/
  public void setProperty(String name, Object value);

  /** Gets the value of a named property.
   *  
   *  @param name Name of the property
   *  @return Value of the named property
   *  @throws javax.xml.rpc.JAXRPCException if an invalid or 
   *          unsupported property name is passed.
  **/
  public Object getProperty(String name);

  /** Removes a named property.
   *  @param name Name of the property
   *  @throws javax.xml.rpc.JAXRPCException if an invalid or 
   *          unsupported property name is passed.
  **/
  public void removeProperty(String name);

  /** Gets the names of configurable properties supported by 
   *  this <code>Call</code> object.   
   *  @return Iterator for the property names
  **/
  public java.util.Iterator getPropertyNames();


  /** Invokes a specific operation using a synchronous request-response 
   *  interaction mode.
   *
   *  @param  inputParams Object[]--Parameters for this invocation. This
   *          includes only the input params
   *  @return Returns the return value or <code>null</code>
   *  @throws java.rmi.RemoteException  if there is any error in the 
   *          remote method invocation
   *  @throws javax.xml.rpc.soap.SOAPFaultException Indicates a SOAP fault
   *  @throws javax.xml.rpc.JAXRPCException 
   *          <UL>
   *          <LI>If there is an error in the configuration of the
   *              <code>Call</code> object
   *          <LI>If <code>inputParams</code> do not match the required parameter
   *              set (as specified through the <code>addParameter</code> 
   *              invocations or in the corresponding WSDL)
   *          <LI>If parameters and return type are incorrectly
   *              specified
   *          </UL>
  **/
  public Object invoke(Object[] inputParams)
                    throws java.rmi.RemoteException;

  /** Invokes a specific operation using a synchronous request-response 
   *  interaction mode.
   *
   *  @param  operationName QName of the operation
   *  @param  inputParams Object[]--Parameters for this invocation. This
   *          includes only the input params.
   *  @return Return value or null
   *  @throws java.rmi.RemoteException  if there is any error in the 
   *          remote method invocation.
   *  @throws javax.xml.rpc.soap.SOAPFaultException Indicates a SOAP fault
   *  @throws javax.xml.rpc.JAXRPCException 
   *          <UL>
   *          <LI>If there is an error in the configuration of the
   *              <code>Cal</code>l object
   *          <LI>If <code>inputParam</code>s do not match the required parameter
   *              set (as specified through the <code>addParameter</code> 
   *              invocations or in the corresponding WSDL)
   *          <LI>If parameters and return type are incorrectly
   *              specified
   *          </UL>
  **/  
  public Object invoke(QName operationName, 
		       Object[] inputParams)
                    throws java.rmi.RemoteException;
  
  /** Invokes a remote method using the one-way interaction mode. The 
   *  client thread does not normally  block waiting for the completion
   *  of the server processing for this remote method invocation. When
   *  the protocol in use is SOAP/HTTP, this method should block until
   *  an HTTP response code has been received or an error occurs.
   *
   * This method 
   *  must not throw any remote exceptions. This method may throw a 
   *  <code>JAXRPCException</code> during the processing of the one-way
   *  remote call.
   *
   *  @param  inputParams Object[]--Parameters for this invocation. This
   *          includes only the input params.
   *  @throws JAXRPCException if there is an error in the 
   *          configuration of the <code>Call</code> object (example: a 
   *          non-void return type has been incorrectly specified for the 
   *          one-way call) or if there is any error during the 
   *          invocation of the one-way remote call
  **/
  public void invokeOneWay(Object[] inputParams);

  /** Returns a <code>Map</code> of {name, value} for the output parameters of 
   *  the last invoked operation. The parameter names in the 
   *  returned Map are of type <code>java.lang.String</code>.
   *
   *  @return Map Output parameters for the last <code>Call.invoke()</code>.
   *              Empty <code>Map</code> is returned if there are no output
   *              parameters. 
   *  @throws JAXRPCException If this method is invoked for a
   *          one-way operation or is invoked before any
   *          <code>invoke</code> method has been called.
  **/
  public Map getOutputParams();

  /** Returns a <code>List</code> values for the output parameters 
   *  of the last invoked operation.
   *
   *  @return java.util.List Values for the output parameters. An
   *              empty <code>List</code> is returned if there are
   *              no output values.
   *  @throws JAXRPCException If this method is invoked for a
   *          one-way operation or is invoked before any
   *          <code>invoke</code> method has been called.
  **/
  public List getOutputValues();
}
