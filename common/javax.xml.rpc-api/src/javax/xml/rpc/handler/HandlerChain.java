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

import java.util.List;
import java.util.Map;
import java.util.Iterator;

/** The <code>javax.xml.rpc.handler.HandlerChain</code> represents
 *  a list of handlers. All elements in the HandlerChain are of 
 *  the type <code>javax.xml.rpc.handler.Handler</code>.
 *
 *  <p>An implementation class for the <code>HandlerChain</code>
 *  interface abstracts the policy and mechanism for the invocation
 *  of the registered handlers. 
 *
 *  @version 1.0
 *  @author  Rahul Sharma
 *  @see javax.xml.rpc.handler.HandlerChain
**/

public interface HandlerChain extends java.util.List {

  /** The <code>handleRequest</code> method initiates the request 
   *  processing for this handler chain.
   *
   *  @param context  MessageContext parameter provides access to
   *                  the request SOAP message.
   *  @return boolean Returns <code>true</code> if all handlers in
   *                  chain have been processed. Returns <code>false</code>
   *                  if a handler in the chain returned
   *                  <code>false</code> from its handleRequest
   *                  method.
   *  @throws JAXRPCException if any processing error happens 
   *  @see javax.xml.rpc.handler.Handler#handleRequest
  **/
  public boolean handleRequest(MessageContext context);

  /** The <code>handleResponse</code> method initiates the response
   *  processing for this handler chain.
   *
   *  @param context MessageContext parameter provides access to
   *                 the response SOAP message.
   *  @return boolean Returns <code>true</code> if all handlers in
   *                  chain have been processed. Returns <code>false</code>
   *                  if a handler in the chain returned
   *                  <code>false</code> from its handleResponse method.
   *  @throws JAXRPCException if any processing error happens 
   *  @see javax.xml.rpc.handler.Handler#handleResponse
  **/
  public boolean handleResponse(MessageContext context);

  /** The <code>handleFault</code> method initiates the SOAP
   *  fault processing for this handler chain.
   *
   *  @param context MessageContext parameter provides access
   *                 to the SOAP message.
   *  @return boolean Returns <code>true</code> if all handlers in
   *                  chain have been processed. Returns <code>false</code>
   *                  if a handler in the chain returned
   *                  <code>false</code> from its handleFault method.
   *  @throws JAXRPCException if any processing error happens 
   *  @see javax.xml.rpc.handler.Handler#handleFault
  **/
  public boolean handleFault(MessageContext context);

  /** Initializes the configuration for a HandlerChain.
   *
   *  @param  config     Configuration for the initialization of
   *                     this handler chain
   *  @throws JAXRPCException If any error during initialization
  **/
  public void init(java.util.Map config);

  /** Indicates the end of lifecycle for a HandlerChain.
   *
   *  @throws JAXRPCException If any error during destroy
  **/
  public void destroy();

  /** Sets SOAP Actor roles for this <code>HandlerChain</code>. This 
   *  specifies the set of roles in which this HandlerChain is to act
   *  for the SOAP message processing at this SOAP node. These roles
   *  assumed by a HandlerChain must be invariant during the 
   *  processing of an individual SOAP message through the HandlerChain.
   *
   *  <p>A <code>HandlerChain</code> always acts in the role of the
   *  special SOAP actor <code>next</code>. Refer to the SOAP 
   *  specification for the URI name for this special SOAP actor. 
   *  There is no need to set this special role using this method.
   *
   *  @param soapActorNames   URIs for SOAP actor name 
   *
   *  @see javax.xml.rpc.NamespaceConstants
  **/
  public void setRoles(String[] soapActorNames);

  /** Gets SOAP actor roles registered for this HandlerChain at 
   *  this SOAP node. The returned array includes the special 
   *  SOAP actor <code>next</code>.
   *
   *  @return String[] SOAP Actor roles as URIs
   *  @see javax.xml.rpc.NamespaceConstants
  **/
  public String[] getRoles();

}
