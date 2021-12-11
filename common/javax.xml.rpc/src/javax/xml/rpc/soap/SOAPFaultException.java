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

package javax.xml.rpc.soap;


import javax.xml.namespace.QName;
import javax.xml.soap.Detail;

/** The <code>SOAPFaultException</code> exception represents a 
 *  SOAP fault.
 *
 *  <p>The message part in the SOAP fault maps to the contents of
 *  <code>faultdetail</code> element accessible through the 
 *  <code>getDetail</code> method on the <code>SOAPFaultException</code>.
 *  The method <code>createDetail</code> on the 
 *  <code>javax.xml.soap.SOAPFactory</code> creates an instance 
 *  of the <code>javax.xml.soap.Detail</code>. 
 *
 *  <p>The <code>faultstring</code> provides a human-readable 
 *  description of the SOAP fault. The <code>faultcode</code> 
 *  element provides an algorithmic mapping of the SOAP fault.
 * 
 *  <p>Refer to SOAP 1.1 and WSDL 1.1 specifications for more
 *  details of the SOAP faults. 
 *
 *  @version 1.0
 *  @author  Rahul Sharma
 *  @see javax.xml.soap.Detail
 *  @see javax.xml.soap.SOAPFactory#createDetail
**/

public class SOAPFaultException extends java.lang.RuntimeException  {
  
  private QName faultcode;
  private String faultstring;
  private String faultactor;
  private Detail detail;

  /** Constructor for the SOAPFaultException
   *  @param faultcode   <code>QName</code> for the SOAP faultcode
   *  @param faultstring <code>faultstring</code> element of SOAP fault 
   *  @param faultactor  <code>faultactor</code> element of SOAP fault
   *  @param faultdetail <code>faultdetail</code> element of SOAP fault 
   *
   *  @see javax.xml.soap.SOAPFactory#createDetail
  **/
  public SOAPFaultException(QName faultcode,
		   String faultstring,
		   String faultactor,
		   javax.xml.soap.Detail faultdetail) { 
    super(faultstring);
    this.faultcode = faultcode;
    this.faultstring = faultstring;
    this.faultactor = faultactor;
    this.detail = faultdetail;
  }

  /** Gets the <code>faultcode</code> element. The <code>faultcode</code>
   *  element provides an algorithmic mechanism for identifying the
   *  fault. SOAP defines a small set of SOAP fault codes covering 
   *  basic SOAP faults.
   *
   *  @return QName of the faultcode element
  **/
  public QName getFaultCode() {
    return this.faultcode;
  }

  /** Gets the <code>faultstring</code> element. The <code>faultstring</code>
   *  provides a human-readable description of the SOAP fault and 
   *  is not intended for algorithmic processing.
   *
   *  @return faultstring element of the SOAP fault
  **/
  public String getFaultString() {
    return this.faultstring;
  }

  /** Gets the <code>faultactor</code> element. The <code>faultactor</code>
   *  element provides information about which SOAP node on the 
   *  SOAP message path caused the fault to happen. It indicates 
   *  the source of the fault.
   * 
   *  @return <code>faultactor</code> element of the SOAP fault 
  **/
  public String getFaultActor() {
    return this.faultactor;
  }

  /** Gets the detail element. The detail element is intended for
   *  carrying application specific error information related to
   *  the SOAP Body.
   *
   *  @return <code>detail</code> element of the SOAP fault
   *  @see javax.xml.soap.Detail
  **/
  public Detail getDetail() {
    return this.detail;
  }
}
