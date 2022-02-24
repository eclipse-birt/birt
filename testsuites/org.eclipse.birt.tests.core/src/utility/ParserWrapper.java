/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
/*
 * Copyright 1999-2002,2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package utility;

import org.w3c.dom.Document;
import org.w3c.dom.Text;

import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

/**
 * Encapsulates a DOM parser.
 *
 * @version $Id: ParserWrapper.java,v 1.2 2007/01/07 13:23:12 anonymous Exp $
 */
public interface ParserWrapper {

	//
	// ParserWrapper methods
	//

	/** Parses the specified URI and returns the document. */
	public Document parse(String uri) throws Exception;

	/**
	 * Set the state of a feature.
	 *
	 * Set the state of any feature in a SAX2 parser. The parser might not recognize
	 * the feature, and if it does recognize it, it might not be able to fulfill the
	 * request.
	 *
	 * @param featureId The unique identifier (URI) of the feature.
	 * @param state     The requested state of the feature (true or false).
	 *
	 * @exception org.xml.sax.SAXNotRecognizedException If the requested feature is
	 *                                                  not known.
	 * @exception org.xml.sax.SAXNotSupportedException  If the requested feature is
	 *                                                  known, but the requested
	 *                                                  state is not supported.
	 * @exception org.xml.sax.SAXException              If there is any other
	 *                                                  problem fulfilling the
	 *                                                  request.
	 */
	public void setFeature(String featureId, boolean state) throws SAXNotRecognizedException, SAXNotSupportedException;

	/** Returns the document information. */
	public DocumentInfo getDocumentInfo();

	//
	// Interfaces
	//

	/**
	 * This interface is here to query information about the document implementation
	 * returned by the <code>ParserWrapper#parse</code> method.
	 *
	 * @author Andy Clark, IBM
	 */
	public interface DocumentInfo {

		//
		// DocumentInfo methods
		//

		/**
		 * Returns true if the specified text node is ignorable whitespace.
		 */
		public boolean isIgnorableWhitespace(Text text);

	} // interface DocumentInfo

} // interface ParserWrapper
