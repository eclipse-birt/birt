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

package javax.xml.rpc.encoding;

/** QNames for XML Types
 *
 *  @version   1.0
 *  @author    Rahul Sharma
**/

import javax.xml.namespace.QName;
import javax.xml.rpc.NamespaceConstants;

/** Constants for common XML Schema and SOAP 1.1 types.
 *  @version 1.0
 *  @author  Rahul Sharma
**/

public class XMLType {
  
  /** The name of the <code>xsd:string</code> type.
  **/
  public static final QName XSD_STRING = new QName(
		NamespaceConstants.NSURI_SCHEMA_XSD, "string");
  /** The name of the <code>xsd:float</code> type.
  **/
  public static final QName XSD_FLOAT = new QName(
		NamespaceConstants.NSURI_SCHEMA_XSD, "float");
  /** The name of the <code>xsd:boolean</code> type.
  **/
  public static final QName XSD_BOOLEAN = new QName(
		NamespaceConstants.NSURI_SCHEMA_XSD, "boolean");
  /** The name of the <code>xsd:double</code> type.
  **/
  public static final QName XSD_DOUBLE = new QName(
		NamespaceConstants.NSURI_SCHEMA_XSD, "double");
  /** The name of the <code>xsd:integer</code> type.
  **/
  public static final QName XSD_INTEGER = new QName(
		NamespaceConstants.NSURI_SCHEMA_XSD, "integer");
  /** The name of the <code>xsd:int</code> type.
  **/
  public static final QName XSD_INT = new QName(
		NamespaceConstants.NSURI_SCHEMA_XSD, "int");
  /** The name of the <code>xsd:long</code> type.
  **/
  public static final QName XSD_LONG = new QName(
		NamespaceConstants.NSURI_SCHEMA_XSD, "long");
  /** The name of the <code>xsd:short</code> type.
  **/
  public static final QName XSD_SHORT = new QName(
		NamespaceConstants.NSURI_SCHEMA_XSD, "short");
  /** The name of the <code>xsd:decimal</code> type.
  **/
  public static final QName XSD_DECIMAL = new QName(
		NamespaceConstants.NSURI_SCHEMA_XSD, "decimal");
  /** The name of the <code>xsd:base64Binary</code> type.
  **/
  public static final QName XSD_BASE64 = new QName(
		NamespaceConstants.NSURI_SCHEMA_XSD, "base64Binary");
  /** The name of the <code>xsd:hexBinary</code> type.
  **/
  public static final QName XSD_HEXBINARY = new QName(
		NamespaceConstants.NSURI_SCHEMA_XSD, "hexBinary");
  /** The name of the <code>xsd:byte</code> type.
  **/
  public static final QName XSD_BYTE = new QName(
		NamespaceConstants.NSURI_SCHEMA_XSD, "byte");
  /** The name of the <code>xsd:dateTime</code> type.
  **/
  public static final QName XSD_DATETIME = new QName(
		NamespaceConstants.NSURI_SCHEMA_XSD, "dateTime");
  /** The name of the <code>xsd:QName</code> type.
  **/
  public static final QName XSD_QNAME = new QName(
		NamespaceConstants.NSURI_SCHEMA_XSD, "QName");

  /** The name of the <code>SOAP-ENC:string</code> type.
  **/
  public static final QName SOAP_STRING = new QName(
		NamespaceConstants.NSURI_SOAP_ENCODING, "string");
  /** The name of the <code>SOAP-ENC:boolean</code> type.
  **/
  public static final QName SOAP_BOOLEAN = new QName(
		NamespaceConstants.NSURI_SOAP_ENCODING, "boolean");
  /** The name of the <code>SOAP-ENC:double</code> type.
  **/
  public static final QName SOAP_DOUBLE = new QName(
		NamespaceConstants.NSURI_SOAP_ENCODING, "double");
  /** The name of the <code>SOAP-ENC:base64</code> type.
  **/
  public static final QName SOAP_BASE64 = new QName(
		NamespaceConstants.NSURI_SOAP_ENCODING, "base64");
  /** The name of the <code>SOAP-ENC:float</code> type.
  **/
  public static final QName SOAP_FLOAT = new QName(
		NamespaceConstants.NSURI_SOAP_ENCODING, "float");
  /** The name of the <code>SOAP-ENC:int</code> type.
  **/
  public static final QName SOAP_INT = new QName(
		NamespaceConstants.NSURI_SOAP_ENCODING, "int");
  /** The name of the <code>SOAP-ENC:long</code> type.
  **/
  public static final QName SOAP_LONG = new QName(
		NamespaceConstants.NSURI_SOAP_ENCODING, "long");
  /** The name of the <code>SOAP-ENC:short</code> type.
  **/
  public static final QName SOAP_SHORT = new QName(
		NamespaceConstants.NSURI_SOAP_ENCODING, "short");
  /** The name of the <code>SOAP-ENC:byte</code> type.
  **/
  public static final QName SOAP_BYTE = new QName(
		NamespaceConstants.NSURI_SOAP_ENCODING, "byte");
  /** The name of the <code>SOAP-ENC:Array</code> type.
  **/
  public static final QName SOAP_ARRAY = new QName(
		NamespaceConstants.NSURI_SOAP_ENCODING, "Array");
}
