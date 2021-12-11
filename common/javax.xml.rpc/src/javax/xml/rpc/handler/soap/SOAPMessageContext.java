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

package javax.xml.rpc.handler.soap;

import javax.xml.rpc.JAXRPCException;
import javax.xml.soap.SOAPMessage;

/** The interface <code>javax.xml.rpc.soap.SOAPMessageContext</code> 
 *  provides access to the SOAP message for either RPC request or 
 *  response. The <code>javax.xml.soap.SOAPMessage</code> specifies
 *  the standard Java API for the representation of a SOAP 1.1 message
 *  with attachments.
 *
 *  @version 1.0
 *  @author  Rahul Sharma
 *  @see javax.xml.soap.SOAPMessage
**/

public interface SOAPMessageContext 
                    extends javax.xml.rpc.handler.MessageContext {

  /** Gets the SOAPMessage from this message context
   *
   *  @return Returns the SOAPMessage; returns null if no 
   *          SOAPMessage is present in this message context
  **/
  public SOAPMessage getMessage();
  
  /** Sets the SOAPMessage in this message context
   *
   *  @param  message SOAP message
   *  @throws JAXRPCException If any error during the setting
   *          of the SOAPMessage in this message context
   *  @throws java.lang.UnsupportedOperationException If this
   *          operation is not supported
  **/
  public void setMessage(SOAPMessage message);

  /** Gets the SOAP actor roles associated with an execution
   *  of the HandlerChain and its contained Handler instances.
   *  Note that SOAP actor roles apply to the SOAP node and
   *  are managed using <code>HandlerChain.setRoles</code> and
   *  <code>HandlerChain.getRoles</code>. Handler instances in
   *  the HandlerChain use this information about the SOAP actor
   *  roles to process the SOAP header blocks. Note that the
   *  SOAP actor roles are invariant during the processing of
   *  SOAP message through the HandlerChain.
   *
   *  @return Array of URIs for SOAP actor roles
   *  @see javax.xml.rpc.handler.HandlerChain#setRoles
   *  @see javax.xml.rpc.handler.HandlerChain#getRoles
  **/
  public String[] getRoles();

}
