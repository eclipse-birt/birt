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
import javax.xml.rpc.soap.SOAPFaultException;
import javax.xml.namespace.QName;

/** The <code>javax.xml.rpc.handler.Handler</code> interface is 
 *  required to be implemented by a SOAP message handler. The 
 *  <code>handleRequest</code>, <code>handleResponse</code> 
 *  and <code>handleFault</code> methods for a SOAP message 
 *  handler get access to the <code>SOAPMessage</code> from the
 *  <code>SOAPMessageContext</code>. The implementation of these
 *  methods can modify the <code>SOAPMessage</code> including the
 *  headers and body elements.
 *  
 *  @version 1.0
 *  @author  Rahul Sharma
**/

public interface Handler {

  /** The <code>handleRequest</code> method processes the request 
   *  message. 
   *
   *  @param context <code>MessageContext</code> parameter provides 
   *                 access to the request message.
   *  @return boolean Indicates the processing mode
   *                 <UL>
   *                 <LI>Return <code>true</code> to indicate continued 
   *                     processing of the request handler chain. The 
   *                     <code>HandlerChain</code>
   *                     takes the responsibility of invoking the next 
   *                     entity. The next entity may be the next handler 
   *                     in the <code>HandlerChain</code> or if this 
   *                     handler is the last handler in the chain, the 
   *                     next entity is the service endpoint object.
   *                 <LI>Return <code>false</code> to indicate blocking 
   *                     of the request handler chain. In this case, 
   *                     further processing of the request handler chain
   *                     is blocked and the target service endpoint is 
   *                     not dispatched. The JAX-RPC runtime system takes
   *                     the responsibility of invoking the response 
   *                     handler chain next with the SOAPMessageContext. 
   *                     The Handler implementation class has the the 
   *                     responsibility of setting the appropriate response
   *                     SOAP message in either handleRequest and/or 
   *                     handleResponse method. In the default processing
   *                     model, the response handler chain starts processing
   *                     from the same Handler instance (that returned false)
   *                     and goes backward in the execution sequence.
   *                  </UL>
   *  @throws JAXRPCException This exception indicates handler
   *                     specific runtime error. If JAXRPCException is thrown
   *                     by a handleRequest method, the HandlerChain 
   *                     terminates the further processing of this handler
   *                     chain. On the server side, the HandlerChain 
   *                     generates a SOAP fault that indicates that the 
   *                     message could not be processed for reasons not 
   *                     directly attributable to the contents of the 
   *                     message itself but rather to a runtime error 
   *                     during the processing of the message. On the 
   *                     client side, the exception is propagated to 
   *                     the client code
   *  @throws SOAPFaultException This indicates a SOAP fault. The Handler 
   *                     implementation class has the the responsibility 
   *                     of setting the SOAP fault in the SOAP message in
   *                     either handleRequest and/or handleFault method. 
   *                     If SOAPFaultException is thrown by a server-side 
   *                     request handler's handleRequest method, the 
   *                     HandlerChain terminates the further processing 
   *                     of the request handlers in this handler chain 
   *                     and invokes the handleFault method on the 
   *                     HandlerChain with the SOAP message context. Next,
   *                     the HandlerChain invokes the handleFault method 
   *                     on handlers registered in the handler chain, 
   *                     beginning with the Handler instance that threw 
   *                     the exception and going backward in execution. The
   *                     client-side request handler's handleRequest method 
   *                     should not throw the SOAPFaultException.
  **/
  public boolean handleRequest(MessageContext context);

  /** The <code>handleResponse</code> method processes the response 
   *  SOAP message.
   *
   *  @param context MessageContext parameter provides access to
   *                 the response SOAP message
   *  @return boolean Indicates the processing mode
   *                 <UL>
   *                 <LI>Return <code>true</code> to indicate continued 
   *                     processing ofthe response handler chain. The 
   *                     HandlerChain invokes the <code>handleResponse</code>
   *                     method on the next <code>Handler</code> in 
   *                     the handler chain.
   *                 <LI>Return <code>false</code> to indicate blocking 
   *                     of the response handler chain. In this case, no
   *                     other response handlers in the handler chain 
   *                     are invoked.
   *                 </UL>
   * @throws JAXRPCException Indicates handler specific runtime error. 
   *                     If JAXRPCException is thrown by a handleResponse
   *                     method, the HandlerChain terminates the further 
   *                     processing of this handler chain. On the server side, 
   *                     the HandlerChain generates a SOAP fault that 
   *                     indicates that the message could not be processed
   *                     for reasons not directly attributable to the contents
   *                     of the message itself but rather to a runtime error
   *                     during the processing of the message. On the client 
   *                     side, the runtime exception is propagated to the
   *                     client code.
   * 
  **/
  public boolean handleResponse(MessageContext context);

  /** The <code>handleFault</code> method processes the SOAP faults 
   *  based on the SOAP message processing model.
   *
   *  @param context MessageContext parameter provides access to
   *                 the SOAP message
   *  @return boolean Indicates the processing mode
   *                 <UL>
   *                 <LI>Return <code>true</code> to indicate continued 
   *                     processing of SOAP Fault. The HandlerChain invokes
   *                     the <code>handleFault</code> method on the 
   *                     next <code>Handler</code> in the handler chain.
   *                 <LI>Return <code>false</code> to indicate end 
   *                     of the SOAP fault processing. In this case, no 
   *                     other handlers in the handler chain 
   *                     are invoked.
   *                 </UL>
   * @throws JAXRPCException Indicates handler specific runtime error. 
   *                     If JAXRPCException is thrown by a handleFault
   *                     method, the HandlerChain terminates the further 
   *                     processing of this handler chain. On the server side, 
   *                     the HandlerChain generates a SOAP fault that 
   *                     indicates that the message could not be processed
   *                     for reasons not directly attributable to the contents
   *                     of the message itself but rather to a runtime error
   *                     during the processing of the message. On the client 
   *                     side, the JAXRPCException is propagated to the
   *                     client code.
  **/
  public boolean handleFault(MessageContext context);

  /** The <code>init</code> method enables the Handler instance to 
   *  initialize itself. The <code>init</code> method passes the 
   *  handler configuration as a <code>HandlerInfo</code> instance.
   *  The HandlerInfo is used to configure the Handler (for example: 
   *  setup access to an external resource or service) during the
   *  initialization.
   *
   *  <p>In the init method, the Handler class may get access to 
   *  any resources (for example; access to a logging service or
   *  database) and maintain these as part of its instance variables.
   *  Note that these instance variables must not have any state 
   *  specific to the SOAP message processing performed in the 
   *  various handle method.
   *
   *  @param config Configuration for the initialization of
   *                     this handler
   *  @throws JAXRPCException If initialization of the handler fails
  **/
  public void init(HandlerInfo config);

  /** The <code>destroy</code> method indicates the end of lifecycle 
   *  for a Handler instance.  The Handler implementation class should
   *  release its resources and perform cleanup in the implementation
   *  of the <code>destroy</code> method.
   *
   *  @throws JAXRPCException If any error during destroy 
  **/
  public void destroy();

  /** Gets the header blocks that can be processed by this Handler
   *  instance.
   *
   *  @return Array of QNames of header blocks processed by this
   *           handler instance. <code>QName</code> is the qualified 
   *           name of the outermost element of the Header block.
  **/
  public QName[] getHeaders();
}
