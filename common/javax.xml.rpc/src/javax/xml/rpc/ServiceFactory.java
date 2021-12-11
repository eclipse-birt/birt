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

import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import javax.xml.namespace.QName;

/** The <code>javax.xml.rpc.ServiceFactory</code> is an abstract class
 *  that provides a factory for the creation of instances of the type 
 *  <code>javax.xml.rpc.Service</code>. This abstract class follows the
 *  abstract static factory design pattern. This enables a J2SE based 
 *  client to create a <code>Service instance</code> in a portable manner
 *  without using the constructor of the <code>Service</code>
 *  implementation class.
 *
 *  <p>The ServiceFactory implementation class is set using the
 *   system property <code>SERVICEFACTORY_PROPERTY</code>.
 *  
 *
 *  @version 1.1
 *  @author  Rahul Sharma
 *  @author  Roberto  Chinnici
 *  @see javax.xml.rpc.Service
 **/
public abstract class ServiceFactory {

  /**
   * A constant representing the property used to lookup the
   * name of a <code>ServiceFactory</code> implementation 
   * class.
   */
  static public final String SERVICEFACTORY_PROPERTY
        = "javax.xml.rpc.ServiceFactory";

  /**
   * A constant representing the name of the default 
   * <code>ServiceFactory</code> implementation class.
  **/
  static private final String DEFAULT_SERVICEFACTORY
        = "com.sun.xml.rpc.client.ServiceFactoryImpl";

  protected ServiceFactory () {}

  /** Gets an instance of the <code>ServiceFactory</code>
   *
   * <p>Only one copy of a factory exists and is returned to the 
   * application each time this method is called.
   *
   * <p> The implementation class to be used can be overridden by 
   * setting the javax.xml.rpc.ServiceFactory system property.
  **/
  public static ServiceFactory newInstance() throws ServiceException {
    try {
        return (ServiceFactory)
            FactoryFinder.find(SERVICEFACTORY_PROPERTY,
                               DEFAULT_SERVICEFACTORY);
    } catch (ServiceException ex) {
        throw ex;
    } catch (Exception ex) {
        throw new ServiceException("Unable to create Service Factory: "+
                                ex.getMessage());
    }
  }

  /** Create a <code>Service</code> instance.
   *
   *  @param wsdlDocumentLocation URL for the WSDL document location
   *                              for the service
   *  @param serviceName QName for the service
   *  @throws ServiceException If any error in creation of the
   *                     specified service
  **/
  public abstract Service createService(
			    java.net.URL wsdlDocumentLocation,
			    QName serviceName)
                   throws ServiceException;

  /** Create a <code>Service</code> instance.
   *
   *  @param serviceName QName for the service
   *  @throws ServiceException If any error in creation of the
   *                     specified service
  **/
  public abstract Service createService(
			    QName serviceName)
                   throws ServiceException;

  /**
   * Create an instance of the generated service implementation class
   * for a given service interface, if available.
   *
   *  @param serviceInterface Service interface
   *  @throws ServiceException If there is any error while creating the
   *                     specified service, including the case where
   *                     a generated service implementation class cannot
   *                     be located
   **/
  public abstract Service loadService(
                         Class serviceInterface)
                   throws ServiceException;

  /** Create an instance of the generated service implementation class
   *  for a given service interface, if available.
   *
   *  An implementation  may use the provided <code>wsdlDocumentLocation</code>
   *  and <code>properties</code> to help locate the generated implementation
   *  class. If no such class is present, a <code>ServiceException</code>
   *  will be thrown.
   *  
   *  @param wsdlDocumentLocation URL for the WSDL document location
   *                              for the service or null
   *  @param serviceInterface Service interface
   *  @param properties A set of implementation-specific properties
   *                    to help locate the generated service
   *                    implementation class
   *  @throws ServiceException If there is any error while creating the
   *                     specified service, including the case where
   *                     a generated service implementation class cannot
   *                     be located
  **/
  public abstract Service loadService(
			    java.net.URL wsdlDocumentLocation,
			    Class serviceInterface,
                         java.util.Properties properties)
                   throws ServiceException;

  /** Create an instance of the generated service implementation
   *  class for a given service, if available.
   *
   *  The service is uniquely identified by the <code>wsdlDocumentLocation</code>
   *  and <code>serviceName</code> arguments.
   *
   *  An implementation  may use the provided <code>properties</code> to help
   *  locate the generated implementation class. If no such class is present,
   *  a <code>ServiceException</code>  will be thrown.
   *
   *  @param wsdlDocumentLocation URL for the WSDL document location
   *                              for the service or null
   *  @param serviceName Qualified name for the service
   *  @param properties A set of implementation-specific properties
   *                    to help locate the generated service
   *                    implementation class
   *  @throws ServiceException If there is any error while creating the
   *                     specified service, including the case where
   *                     a generated service implementation class cannot
   *                     be located
  **/
  public abstract Service loadService(
			    java.net.URL wsdlDocumentLocation,
			    QName serviceName,
                         java.util.Properties properties)
                   throws ServiceException;
}
