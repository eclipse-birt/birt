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

/** The interface <code>MessageContext</code> abstracts the message
 *  context that is processed by a handler in the <code>handle</code>
 *  method. 
 *
 *  <p>The <code>MessageContext</code> interface provides methods to 
 *  manage a property set. <code>MessageContext</code> properties 
 *  enable handlers in a handler chain to share processing related
 *  state.
 *
 *  @version 1.1
 *  @author  Rahul Sharma
 *  @author  Roberto Chinnici
 *  @see javax.xml.rpc.handler.Handler
**/

public interface MessageContext {

  /** Sets the name and value of a property associated with the
   *  <code>MessageContext</code>. If the <code>MessageContext</code>
   *  contains a value of the same property, the old value is replaced.
   *
   *  @param name Name of the property associated with the 
   *              <code>MessageContext</code>
   *  @param value Value of the property
   *  @throws java.lang.IllegalArgumentException If some aspect
   *              of the property is prevents it from being stored
   *              in the context
   *  @throws UnsupportedOperationException If this method is 
   *              not supported.
   *             
  **/
  public void setProperty(String name, Object value);

  /** Gets the value of a specific property from the 
   *  <code>MessageContext</code>
   *  @param  name Name of the property whose value is to be
   *               retrieved
   *  @return Value of the property
   *  @throws java.lang.IllegalArgumentException if an illegal
   *              property name is specified
  **/
  public Object getProperty(String name);

  /** Removes a property (name-value pair) from the <code>MessageContext</code>
   *  @param name Name of the property to be removed
   *  @throws java.lang.IllegalArgumentException if an illegal
   *              property name is specified
  **/
  public void removeProperty(String name);

  /** Returns true if the <code>MessageContext</code> contains a property
   *  with the specified name.
   *  @param name Name of the property whose presense is to be
   *              tested
   *  @return Returns true if the MessageContext contains the
   *          property; otherwise false
  **/
  public boolean containsProperty(String name);

  /** Returns an Iterator view of the names of the properties
   *  in this <code>MessageContext</code>
   *
   *  @return Iterator for the property names
  **/
  public java.util.Iterator getPropertyNames();
 
}
