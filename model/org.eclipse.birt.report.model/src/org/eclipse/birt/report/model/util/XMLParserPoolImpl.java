/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.eclipse.birt.core.util.CommonUtil;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * This is the thread safe implementation of XMLParserPool. This implementation
 * is tuned for caching parsers and handlers created using same loading options.
 * To avoid possible memory leak (in case user is trying to parse documents
 * using different options for every parse), there is a restriction on the size
 * of the pool. The key used for handler caching is based on the option map
 * passed to load.
 */

class XMLParserPoolImpl implements XMLParserPool {

	/**
	 * 
	 */

	private final static int SAXPARSER_DEFAULT_SIZE = 300;

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger.getLogger(XMLParserPoolImpl.class.getName());

	/**
	 * Map to save cached parsers. The key is the parser properties key sets. The
	 * value is the parser.
	 */

	private final Map<Set<?>, List<SAXParser>> parserCache = new HashMap<Set<?>, List<SAXParser>>();

	/**
	 * Creates an instance that caches parsers and caches handlers as specified.
	 * 
	 */
	public XMLParserPoolImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.XMLParserPool#get(java.util.Map)
	 */

	public SAXParser get(Map<String, ?> properties) throws ParserConfigurationException, SAXException {
		Set<String> keys = null;
		SAXParser parser = null;
		if (properties != null) {
			keys = new HashSet<String>(properties.keySet());
		}

		synchronized (this) {
			List<SAXParser> list = parserCache.get(keys);
			if (list != null) {
				int size = list.size();
				if (size > 0) {
					parser = list.remove(size - 1);
				}
			} else
				parserCache.put(keys, new ArrayList<SAXParser>());
		}
		if (parser == null)
			parser = createParser(properties);

		if (properties != null) {
			for (Map.Entry<String, ?> entry : properties.entrySet()) {
				parser.getXMLReader().setProperty(entry.getKey(), entry.getValue());
			}
		}

		return parser;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.XMLParserPool#release(javax.xml.parsers
	 * .SAXParser, java.util.Map)
	 */

	public synchronized void release(SAXParser parser, Map<String, ?> properties) {
		assert parser != null;

		Set<String> keys = null;
		if (properties != null)
			keys = properties.keySet();

		try {
			// release lexical handler
			XMLReader reader = parser.getXMLReader();
			if (keys != null && reader != null) {
				for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
					String key = iterator.next();
					reader.setProperty(key, null);
				}
			}
		} catch (SAXException e) {
			// ignore any exception
		}

		// reset the parser so that make sure no memory leak
		parser.reset();

		synchronized (this) {
			List<SAXParser> list = parserCache.get(keys);
			int currentSize = list.size();

			if (currentSize < SAXPARSER_DEFAULT_SIZE) {
				list.add(parser);
			}
		}
	}

	/**
	 * @param properties
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */

	private SAXParser createParser(Map<String, ?> properties) throws ParserConfigurationException, SAXException {
		SAXParser parser = CommonUtil.createSAXParser();
		logger.log(Level.FINEST, "created a new SAX parser"); //$NON-NLS-1$
		return parser;
	}
}
