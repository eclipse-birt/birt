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

/** Constants used in JAX-RPC for namespace prefixes and URIs
 *  @version 1.0
 *  @author  Rahul Sharma
**/

public class NamespaceConstants {

  /** Namespace prefix for SOAP Envelope
  **/
  public static final String NSPREFIX_SOAP_ENVELOPE = "soapenv"; 

  /** Namespace prefix for SOAP Encoding
  **/
  public static final String NSPREFIX_SOAP_ENCODING = "soapenc"; 

  /** Namespace prefix for XML schema XSD
  **/
  public static final String NSPREFIX_SCHEMA_XSD    = "xsd"; 

  /** Namespace prefix for XML Schema XSI
  **/
  public static final String NSPREFIX_SCHEMA_XSI    = "xsi"; 

  /** Nameapace URI for SOAP 1.1 Envelope
  **/
  public static final String NSURI_SOAP_ENVELOPE    = 
	    "http://schemas.xmlsoap.org/soap/envelope/";

  /** Nameapace URI for SOAP 1.1 Encoding
  **/  
  public static final String NSURI_SOAP_ENCODING    =
	    "http://schemas.xmlsoap.org/soap/encoding/";

  /** Nameapace URI for SOAP 1.1 next actor role
  **/
  public static final String NSURI_SOAP_NEXT_ACTOR  =
	    "http://schemas.xmlsoap.org/soap/actor/next";

  /** Namespace URI for XML Schema XSD
  **/
  public static final String NSURI_SCHEMA_XSD = 
            "http://www.w3.org/2001/XMLSchema";


  /** Namespace URI for XML Schema XSI
  **/
  public static final String NSURI_SCHEMA_XSI =
            "http://www.w3.org/2001/XMLSchema-instance";

}
