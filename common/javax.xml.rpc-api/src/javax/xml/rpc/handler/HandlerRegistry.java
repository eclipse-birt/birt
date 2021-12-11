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
import javax.xml.rpc.JAXRPCException;
import javax.xml.namespace.QName;

/** The <code>javax.xml.rpc.handler.HandlerRegistry</code> 
 *  provides support for the programmatic configuration of 
 *  handlers in a <code>HandlerRegistry</code>.
 *
 *  <p>A handler chain is registered per service endpoint, as 
 *  indicated by the qualified name of a port. The getHandlerChain
 *  returns the handler chain (as a java.util.List) for the 
 *  specified service endpoint. The returned handler chain is 
 *  configured using the java.util.List interface. Each element 
 *  in this list is required to be of the Java type 
 *  <code>javax.xml.rpc.handler.HandlerInfo</code>.
 *
 *  @version 1.0
 *  @author  Rahul Sharma
 *  @see javax.xml.rpc.Service
**/

public interface HandlerRegistry extends java.io.Serializable {

  /** Gets the handler chain for the specified service endpoint.
   *  The returned <code>List</code> is used to configure this
   *  specific handler chain in this <code>HandlerRegistry</code>.
   *  Each element in this list is required to be of the Java type 
   *  <code>javax.xml.rpc.handler.HandlerInfo</code>.
   *
   *  @param portName Qualified name of the target service endpoint
   *  @return java.util.List Handler chain
   *  @throws java.lang.IllegalArgumentException If an invalid
   *          <code>portName</code> is specified
  **/
  public java.util.List getHandlerChain(QName portName);

  /** Sets the handler chain for the specified service endpoint
   *  as a <code>java.util.List</code>. Each element in this list
   *  is required to be of the Java type 
   *  <code>javax.xml.rpc.handler.HandlerInfo</code>.
   *
   *  @param portName Qualified name of the target service endpoint
   *  @param chain    A List representing configuration for the
   *                  handler chain
   *  @throws JAXRPCException If any error in the configuration of
   *                  the handler chain
   *  @throws java.lang.UnsupportedOperationException If this
   *          set operation is not supported. This is done to
   *          avoid any overriding of a pre-configured handler
   *          chain.
   *  @throws java.lang.IllegalArgumentException If an invalid
   *          <code>portName</code> is specified
  **/
  public void setHandlerChain(QName portName, java.util.List chain);

}
