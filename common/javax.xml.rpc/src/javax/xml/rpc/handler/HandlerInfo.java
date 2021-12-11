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

import java.io.Serializable;
import java.util.Map;
import java.util.Vector;
import java.util.HashMap;
import javax.xml.namespace.QName;

/** The <code>javax.xml.rpc.handler.HandlerInfo</code> represents
 *  information about a handler in the HandlerChain. A HandlerInfo 
 *  instance is passed in the <code>Handler.init</code> method to 
 *  initialize a <code>Handler</code> instance.
 *
 *  @version 1.0
 *  @author  Rahul Sharma
 *  @see javax.xml.rpc.handler.HandlerChain
 **/

public class HandlerInfo implements java.io.Serializable {

  private Class handlerClass;
  private Map config;
  private Vector headers;

  /** Default constructor
  **/
  public HandlerInfo() {
    this.handlerClass = null;
    this.config = new HashMap();
    this.headers = new Vector();
  }

  /** Constructor for HandlerInfo
   *  @param handlerClass Java Class for the Handler
   *  @param config       Handler Configuration as a java.util.Map
   *  @param headers      QNames for the header blocks processed
   *                      by this Handler. QName is the qualified
   *                      name of the outermost element of a header
   *                      block
  **/
  public HandlerInfo(Class handlerClass, Map config,
		     QName[] headers) {
    this.handlerClass = handlerClass;
    this.config = config;
    this.headers = new Vector();
    if (headers != null) {
      for (int i = 0; i < headers.length; i++) {
        this.headers.add(i, headers[i]);
      }
    } 
  }

  /** Sets the Handler class
   *
   *  @param handlerClass Class for the Handler
  **/
  public void setHandlerClass(Class handlerClass) { 
    this.handlerClass = handlerClass;
  }

  /** Gets the Handler class
   *  @return Returns null if no Handler class has been
   *          set; otherwise the set handler class
  **/
  public Class getHandlerClass() { 
    return handlerClass;
  }

  /** Sets the Handler configuration as <code>java.util.Map</code>
   *
   *  @param config  Configuration map
  **/
  public void setHandlerConfig(Map config) { 
    this.config = config;
  }

  /** Gets the Handler configuration
   *  @return  Returns empty Map if no configuration map
   *           has been set; otherwise returns the set
   *           configuration map
  **/
  public java.util.Map getHandlerConfig() { 
    return config;
  }


  /** Sets the header blocks processed by this Handler.
   *
   *  @param headers QNames of the header blocks. QName
   *                 is the qualified name of the outermost
   *                 element of the SOAP header block
  **/
  public void setHeaders(QName[] headers) { 
    this.headers.clear();
    if (headers != null) {
      for (int i = 0; i < headers.length; i++) {
        this.headers.add(i, headers[i]);
      }
    }
  }

  /** Gets the header blocks processed by this Handler.
   *
   *  @return  Array of QNames for the header blocks. Returns
   *           <code>null</code> if no header blocks have been
   *           set using the <code>setHeaders</code> method.
  **/
  public QName[] getHeaders() { 
    if (this.headers.size() == 0) {
      return null;
    }
    QName[] qns = new QName[this.headers.size()];
    headers.copyInto(qns);
    return qns;
  }
  
}
