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

/** The interface <code>javax.xml.rpc.Stub</code> is the common base interface
 *  for the stub classes. All generated stub classes are required to 
 *  implement the <code>javax.xml.rpc.Stub</code> interface. An instance
 *  of a stub class represents a client side proxy or stub instance for
 *  the target service endpoint. 
 *
 *  <p>The <code>javax.xml.rpc.Stub</code> interface provides an 
 *  extensible property mechanism for the dynamic configuration of 
 *  a stub instance. 
 *
 *  @version   0.1
 *  @author    Rahul Sharma
 *  @see javax.xml.rpc.Service
**/

public interface Stub {

  /** Standard property: User name for authentication.
   *  <p>Type: java.lang.String
  **/
  public static final String USERNAME_PROPERTY = 
                      "javax.xml.rpc.security.auth.username";
  
  /** Standard property: Password for authentication.
   *  <p>Type: java.lang.String
  **/
  public static final String PASSWORD_PROPERTY = 
                      "javax.xml.rpc.security.auth.password";
 
  /** Standard property: Target service endpoint address. The 
   *  URI scheme for the endpoint address specification must 
   *  correspond to the protocol/transport binding for this 
   *  stub class.
   *  <p>Type: java.lang.String
  **/
  public static final String ENDPOINT_ADDRESS_PROPERTY =  
                      "javax.xml.rpc.service.endpoint.address";

  /** Standard property: This boolean property is used by a service 
   *  client to indicate whether or not it wants to participate in
   *  a session with a service endpoint. If this property is set to
   *  true, the service client indicates that it wants the session
   *  to be maintained. If set to false, the session is not maintained. 
   *  The default value for this property is false.
   *  <p>Type: java.lang.Boolean
  **/
  public static final String SESSION_MAINTAIN_PROPERTY =
                      "javax.xml.rpc.session.maintain";


  /** Sets the name and value of a configuration property
   *  for this Stub instance. If the Stub instances contains
   *  a value of the same property, the old value is replaced.
   *  <p>Note that the <code>_setProperty</code> method may not 
   *  perform validity check on a configured property value. An 
   *  example is the standard property for the target service 
   *  endpoint address that is not checked for validity in the 
   *  <code>_setProperty</code> method.
   *  In this case, stub configuration errors are detected at 
   *  the remote method invocation.
   *
   *  @param name  Name of the configuration property
   *  @param value Value of the property
   *  @throws javax.xml.rpc.JAXRPCException
   *          <UL>
   *          <LI>If an optional standard property name is 
   *              specified, however this Stub implementation
   *              class does not support the configuration of
   *              this property.
   *          <LI>If an invalid or unsupported property name is
   *              specified or if a value of mismatched property
   *              type is passed. 
   *          <LI>If there is any error in the configuration of
   *              a valid property.
   *          </UL>
  **/
  public void _setProperty(String name, Object value);

  /** Gets the value of a specific configuration property.
   *  @param  name Name of the property whose value is to be
   *               retrieved
   *  @return Value of the configuration property
   *  @throws javax.xml.rpc.JAXRPCException if an invalid or 
   *          unsupported property name is passed.
  **/
  public Object _getProperty(String name);

  /** Returns an <code>Iterato</code> view of the names of the properties
   *  that can be configured on this stub instance.
   *
   *  @return Iterator for the property names of the type
   *          <code>java.lang.String</code>
  **/
  public java.util.Iterator _getPropertyNames();

}
