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

package javax.xml.rpc.server;

import javax.xml.rpc.ServiceException;

/** The <code>javax.xml.rpc.server.ServiceLifecycle</code> defines
 *  a lifecycle interface for a JAX-RPC service endpoint. If the 
 *  service endpoint class implements the <code>ServiceLifeycle</code>
 *  interface, the servlet container based JAX-RPC runtime system 
 *  is required to manage the lifecycle of the corresponding service
 *  endpoint objects.
 *
 *  @version 1.0
 *  @author  Rahul Sharma
**/

public interface ServiceLifecycle {

  /** Used for initialization of a service endpoint. After a service
   *  endpoint instance (an instance of a service endpoint class) is 
   *  instantiated, the JAX-RPC runtime system invokes the 
   *  <code>init</code> method. The service endpoint class uses the
   *  <code>init</code> method to initialize its configuration 
   *  and setup access to any external resources. The context parameter
   *  in the <code>init</code> method enables the endpoint instance to
   *  access the endpoint context provided by the underlying JAX-RPC 
   *  runtime system.
   *  
   *  <p>The init method implementation should typecast the context
   *  parameter to an appropriate Java type. For service endpoints 
   *  deployed on a servlet container based JAX-RPC runtime system, 
   *  the <code>context</code> parameter is of the Java type 
   *  <code>javax.xml.rpc.server.ServletEndpointContext</code>. The
   *  <code>ServletEndpointContext</code> provides an endpoint context
   *  maintained by the underlying servlet container based JAX-RPC 
   *  runtime system    
   ** 
   *  @param context Endpoint context for a JAX-RPC service endpoint
   *  @throws ServiceException If any error in initialization of the
   *                 service endpoint; or if any illegal context has
   *                 been provided in the init method
  **/

  public void init(Object context) throws ServiceException;

  /** JAX-RPC runtime system ends the lifecycle of a service endpoint 
   *  instance by invoking the destroy method. The service endpoint 
   *  releases its resourcesin the implementation of the destroy 
   *  method.
   *
   *
  **/
  public void destroy();

} 
