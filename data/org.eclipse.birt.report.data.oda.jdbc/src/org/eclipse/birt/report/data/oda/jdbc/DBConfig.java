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
package org.eclipse.birt.report.data.oda.jdbc;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * This class fetch the DBConfig from config.xml file.
 * 
 *
 */
public class DBConfig {
	private static final String CONFIG_XML = "config.xml";
	public static final int NORMAL = 0;
	public static final int EXEC_QUERY_AND_CACHE = 1;
	public static final int EXEC_QUERY_WITHOUT_CACHE = 2;
	public static final int DEFAULT_POLICY = -1;
	public static final int IGNORE_UNIMPORTANT_EXCEPTION = 3;
	public static final int TRY_COMMIT_THEN_CLOSE = 4;
	public static final int SET_COMMIT_TO_FALSE = 5;
	private HashMap<Integer, Set<String>> driverPolicy = null;
	private volatile static DBConfig config = null;

	public static DBConfig getInstance() {
		if (config == null) {
			synchronized (DBConfig.class) {
				if (config == null)
					config = new DBConfig();
			}
		}
		return config;
	}

	//
	DBConfig() {
		driverPolicy = new HashMap<Integer, Set<String>>();
		new SaxParser(this).parse();
	}

	/**
	 * 
	 * @param driverName
	 * @return
	 */
	public boolean qualifyPolicy(String driverName, int policyNumber) {
		if (driverName == null)
			return false;
		Set<String> policySet = driverPolicy.get(policyNumber);
		if (policySet == null)
			return false;
		return policySet.contains(driverName.toUpperCase());
	}

	/**
	 * 
	 * @param driverName
	 * @param policy
	 */
	public void putPolicy(String driverName, int policy) {
		if (driverName == null)
			return;
		if (!driverPolicy.containsKey(policy)) {
			driverPolicy.put(policy, new HashSet<String>());
		}
		driverPolicy.get(policy).add(driverName.toUpperCase());
	}

	/**
	 * 
	 * @return
	 */
	public URL getConfigURL() {
		URL u = this.getClass().getResource(CONFIG_XML);
		return u;
	}

}

/**
 * 
 * @author Administrator
 *
 */
class SaxParser extends DefaultHandler {
	//
	private static final Logger logger = Logger.getLogger(SaxParser.class.getName());
	private static final String TYPE = "type";
	private static final String POLICY = "Policy";
	private static final String NAME = "name";
	private static final String DRIVER = "Driver";
	private int currentPolicy = DBConfig.DEFAULT_POLICY;
	private DBConfig dbConfig;

	/**
	 * Constructor
	 * 
	 * @param config
	 */
	public SaxParser(DBConfig config) {
		this.dbConfig = config;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
	 * java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String uri, String name, String qName, Attributes atts) {
		String elementName = qName;
		if (elementName.equals(DRIVER)) {
			dbConfig.putPolicy(atts.getValue(NAME), currentPolicy);
		} else if (elementName.equals(POLICY)) {
			String type = atts.getValue(TYPE);
			try {
				currentPolicy = Integer.parseInt(type);
			} catch (NumberFormatException e) {
				currentPolicy = DBConfig.DEFAULT_POLICY;
			}
		}
	}

	/**
	 * 
	 */
	public void parse() {
		if (this.dbConfig.getConfigURL() == null)
			return;
		try {
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			xmlReader.setContentHandler(this);
			xmlReader.setErrorHandler(this);
			InputStream is = new BufferedInputStream(this.dbConfig.getConfigURL().openStream());
			try {
				InputSource source = new InputSource(is);
				source.setEncoding(source.getEncoding());
				xmlReader.parse(source);
			} finally {
				is.close();
			}
		} catch (Exception e) {
			logger.log(java.util.logging.Level.WARNING, "failed to parse" + dbConfig.getConfigURL(), e);
		}
	}

}
