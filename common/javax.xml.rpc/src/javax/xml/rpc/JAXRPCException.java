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

/** The <code>javax.xml.rpc.JAXRPCException</code> is thrown from 
 *  the core JAX-RPC APIs to indicate an exception related to the 
 *  JAX-RPC runtime mechanisms.
 *
 *  @version 1.0
 *  @author  Rahul Sharma
**/

public class JAXRPCException extends java.lang.RuntimeException {
  
  private Throwable cause;

  /** Constructs a new exception with <code>null</code> as its 
   *  detail message. The cause is not initialized.
  **/
  public JAXRPCException() { 
    super();
    this.cause = null;
  }

  /** Constructs a new exception with the specified detail 
   *  message.  The cause is not initialized.
   *  @param message The detail message which is later 
   *                 retrieved using the getMessage method
  **/
  public JAXRPCException(String message) {
    super(message);
    this.cause = null;
  }

  /** Constructs a new exception with the specified detail 
   *  message and cause.
   *
   *  @param message The detail message which is later retrieved
   *                 using the getMessage method
   *  @param cause   The cause which is saved for the later
   *                 retrieval throw by the getCause method 
  **/ 
  public JAXRPCException(String message, Throwable cause) {
    super(message);
    this.cause = cause;
  }

  /** Constructs a new JAXRPCException with the specified cause
   *  and a detail message of <tt>(cause==null ? null : 
   *  cause.toString())</tt> (which typically contains the 
   *  class and detail message of <tt>cause</tt>).
   *
   *  @param cause   The cause which is saved for the later
   *                 retrieval throw by the getCause method.
   *                 (A <tt>null</tt> value is permitted, and
   *                 indicates that the cause is nonexistent or
     *               unknown.)
  **/ 
  public JAXRPCException(Throwable cause) {
    super(cause==null ? null : cause.toString());
    this.cause = cause;
  }

  /** Gets the Linked cause
   * 
   *  @return The cause of this Exception or <code>null</code>
   *          if the cause is noexistent or unknown
  **/
  public Throwable getLinkedCause() {
    return this.cause;
  }
}
