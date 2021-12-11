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

package javax.xml.rpc.handler;

import javax.xml.rpc.JAXRPCException;
import javax.xml.namespace.QName;

/** The <code>javax.xml.rpc.handler.GenericHandler</code> class
 *  implements the <code>Handler</code> interface. SOAP Message
 *  Handler developers should typically subclass 
 *  <code>GenericHandler</code> class unless the Handler class 
 *  needs another class as a superclass.
 *
 *  <p>The <code>GenericHandler</code> class is a convenience abstract
 *  class that makes writing Handlers easy. This class provides 
 *  default implementations of the lifecycle methods <code>init</code>
 *  and <code>destroy</code> and also different handle methods. 
 *  A Handler developer should only override methods that it needs
 *  to specialize as part of the derived <code>Handler</code> 
 *  implementation class.
 *  
 *  @version 1.0
 *  @author  Rahul Sharma
**/

public abstract class GenericHandler implements Handler {

  /** Default constructor
  **/
  protected GenericHandler() {}

  /** The <code>handleRequest</code> method processes the request 
   *  SOAP message. The default implementation of this method returns 
   *  <code>true</code>. This indicates that the handler chain
   *  should continue processing of the request SOAP message.
   *  This method should be overridden if the derived Handler class
   *  needs to specialize implementation of this method.   
   *
   *  @see javax.xml.rpc.handler.Handler#handleRequest
  **/
  public boolean handleRequest(MessageContext context) {
    return true;
  }

  /** The <code>handleResponse</code> method processes the response 
   *  message. The default implementation of this method returns 
   *  <code>true</code>. This indicates that the handler chain
   *  should continue processing of the response SOAP message.
   *  This method should be overridden if the derived Handler class 
   *  needs to specialize implementation of this method.
   *
   *  @see javax.xml.rpc.handler.Handler#handleResponse
  **/
  public boolean handleResponse(MessageContext context) {
    return true;
  }

  /** The <code>handleFault</code> method processes the SOAP faults 
   *  based on the SOAP message processing model. The default
   *  implementation of this method returns <code>true</code>. This 
   *  indicates that the handler chain should continue processing
   *  of the SOAP fault. This method should be overridden if
   *  the derived Handler class needs to specialize implementation
   *  of this method.
   *
   *  @see javax.xml.rpc.handler.Handler#handleFault
  **/
  public boolean handleFault(MessageContext context) {
    return true;
  }

  /** The <code>init</code> method to enable the Handler instance to 
   *  initialize itself. This method should be overridden if
   *  the derived Handler class needs to specialize implementation
   *  of this method.
   *
   *  @see javax.xml.rpc.handler.Handler#init
  **/
  public void init(HandlerInfo config) {
  }

  /** The <code>destroy</code> method indicates the end of lifecycle 
   *  for a Handler instance. This method should be overridden if
   *  the derived Handler class needs to specialize implementation
   *  of this method.
   *
   *  @see javax.xml.rpc.handler.Handler#destroy
  **/
  public void destroy() {
  }

  /** Gets the header blocks processed by this Handler instance.
   *
   *  @return  Array of QNames of header blocks processed by this
   *           handler instance. <code>QName</code> is the qualified 
   *           name of the outermost element of the Header block.
  **/
  public abstract QName[] getHeaders();
}
