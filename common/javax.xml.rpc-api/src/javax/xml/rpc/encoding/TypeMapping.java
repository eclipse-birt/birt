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

package javax.xml.rpc.encoding;

import javax.xml.rpc.JAXRPCException;
import java.util.Iterator;
import javax.xml.namespace.QName;

/** The <code>javax.xml.rpc.encoding.TypeMapping</code> is the base 
 *  interface for the representation of a type mapping. A TypeMapping 
 *  implementation class may support one or more encoding styles.
 *
 *  <p>For its supported encoding styles, a TypeMapping instance 
 *  maintains a set of tuples of the type {Java type, 
 *  <code>SerializerFactory</code>, 
 *  <code>DeserializerFactory</code>, XML type}. 
 *  
 *  @version   1.0
 *  @author    Rahul Sharma
**/

public interface TypeMapping {

  /** Returns the encodingStyle URIs (as String[]) supported by  
   *  this TypeMapping instance. A TypeMapping that contains only 
   *  encoding style independent serializers and deserializers 
   *  returns <code>null</code> from this method.
   *
   *  @return Array of encodingStyle URIs for the supported 
   *          encoding styles
  **/
  public String[] getSupportedEncodings();

  /** Sets the encodingStyle URIs supported by this TypeMapping 
   *  instance. A TypeMapping that contains only encoding 
   *  independent serializers and deserializers requires 
   *  <code>null</code> as the parameter for this method.
   *
   *  @param encodingStyleURIs Array of encodingStyle URIs for the 
   *                           supported encoding styles
  **/
  public void setSupportedEncodings(String[] encodingStyleURIs);

  /** Checks whether or not type mapping between specified XML 
   *  type and Java type is registered.
   *   
   *  @param javaType Class of the Java type
   *  @param xmlType    Qualified name of the XML data type
   *  @return boolean; <code>true</code> if type mapping between the
   *           specified XML type and Java type is registered;
   *           otherwise <code>false</code>
  **/
  public boolean isRegistered(Class javaType, QName xmlType);

  /** Registers SerializerFactory and DeserializerFactory for a 
   *  specific type mapping between an XML type and Java type.
   *  This method replaces any existing registered SerializerFactory
   *  DeserializerFactory instances.
   *
   *  @param javaType Class of the Java type
   *  @param xmlType    Qualified name of the XML data type
   *  @param sf       SerializerFactory
   *  @param dsf      DeserializerFactory
   *  @throws JAXRPCException If any error during the registration
  **/
  public void register(Class javaType, QName xmlType,
		       SerializerFactory sf,
		       DeserializerFactory dsf);

  /** Gets the SerializerFactory registered for the specified
   *  pair of Java type and XML data type.
   *
   *  @param  javaType Class of the Java type
   *  @param  xmlType    Qualified name of the XML data type
   *  @return Registered SerializerFactory or <code>null</code>
   *          if there is no registered factory
   **/
  public SerializerFactory getSerializer(Class javaType,
					 QName xmlType);

  /** Gets the DeserializerFactory registered for the specified
   *  pair of Java type and XML data type.
   *
   *  @param  javaType Class of the Java type
   *  @param  xmlType    Qualified name of the XML data type
   *  @return Registered DeserializerFactory or <code>null</code>
   *          if there is no registered factory
  **/
  public DeserializerFactory getDeserializer(Class javaType,
					     QName xmlType);

  /** Removes the SerializerFactory registered for the specified
   *  pair of Java type and XML data type.
   * 
   *  @throws JAXRPCException If there is error in removing the
   *          registered SerializerFactory
  **/
  public void removeSerializer(Class javaType, QName xmlType);

  /** Removes the DeserializerFactory registered for the specified
   *  pair of Java type and XML data type.
   * 
   *  @throws JAXRPCException If there is error in removing the
   *          registered DeserializerFactory
  **/
  public void removeDeserializer(Class javaType, QName xmlType);

}

