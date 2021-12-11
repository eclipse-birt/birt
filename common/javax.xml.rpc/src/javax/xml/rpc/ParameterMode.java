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

/** The <code>javax.xml.rpc.ParameterMode</code> is a type-safe
 *  enumeration for parameter mode. This class is used in the
 *  <code>Call</code>API to specify parameter passing modes.
 *
 *  @version 1.0
 *  @author  Rahul Sharma
 *  @see javax.xml.rpc.Call
**/

public class ParameterMode {
  
  private final String mode;

  private ParameterMode(String mode) { 
    this.mode = mode; 
  }

  /** Returns a <code>String</code> describing this <code>ParameterMode</code> object. 
   * 
   *  @return  A string representation of the object.
  **/
  public String toString() { return mode; }

  /** IN mode for parameter passing
  **/
  public static final ParameterMode IN = new ParameterMode("IN");

  /** OUT mode for parameter passing
  **/
  public static final ParameterMode OUT = new ParameterMode("OUT");

  /** INOUT mode for parameter passing
  **/
  public static final ParameterMode INOUT  = 
		      new ParameterMode("INOUT");

}
