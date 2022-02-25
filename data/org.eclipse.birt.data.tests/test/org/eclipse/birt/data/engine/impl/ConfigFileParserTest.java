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

package org.eclipse.birt.data.engine.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.ConfigFileParser.Node;
import org.junit.Test;

import testutil.BaseTestCase;

/**
 * the test class that test whether class Node can work properly
 *
 */
public class ConfigFileParserTest extends BaseTestCase {

	private Node node;

	private ConfigFileParser parser;

	/**
	 * a test case that check whether method parseXML2DOM() works properly
	 *
	 * @throws IOException
	 */
	@Test
	public void testParseXML2DOM() throws IOException {
		parser = new ConfigFileParser(this.getInputFolder("testDomTree.txt"));
		node = parser.getNode();

		assertFalse("parseXML2DOM() failed !!! ", node == null);

		assertTrue("parseXML2DOM() failed !!! ", parser.containDataSet("dset1"));
		assertFalse("parseXML2DOM() failed !!! ", parser.containDataSet("dataSet1"));

		assertEquals("parseXML2DOM() occurrs errors !!!", 4, node.getChildren().size());

		assertEquals("parseXML2DOM() occurrs errors !!!", "report", node.getName());

		assertEquals("parseXML2DOM() occurrs errors !!!", "dset2",
				((Node) node.getChildren().get(1)).getAttrValue("id"));

		assertEquals("parseXML2DOM() occurrs errors !!!", "query-text",
				((Node) ((Node) node.getChildren().get(0)).getChildren().get(1)).getName());

		assertEquals("parseXML2DOM() occurrs errors !!!", "value1",
				((Node) ((Node) ((Node) node.getChildren().get(2)).getChildren().get(2)).getChildren().get(0))
						.getValue());

		this.openOutputFile();
		this.print(node, "");
		this.closeOutputFile();
		this.checkOutputFile();

	}

	private void initConfigFileParser() throws IOException {
		parser = new ConfigFileParser(this.getInputFolder("testDomTree.txt"));
	}

	/**
	 * Test method getQueryTextByID()
	 *
	 * @throws IOException
	 * @throws DataException
	 */
	@Test
	public void testGetQueryTextByID() throws IOException, DataException {
		initConfigFileParser();
		assertEquals("getQueryTextByID() failed!!!",
				"select * from BBHEGDCGHEBEA where ${timestamp-column}$ >=date('${date}$')",
				parser.getQueryTextByID("dset1"));
	}

	/**
	 * Test method getModeByID()
	 *
	 * @throws IOException
	 * @throws DataException
	 */
	@Test
	public void testGetModeByID() throws IOException, DataException {
		initConfigFileParser();
		assertEquals("getModeByID() failed!!!", "expire", parser.getModeByID("dset1"));

		assertEquals("getModeByID() failed!!!", "persistent", parser.getModeByID("dset2"));
	}

	/**
	 * Test method getModeByID()
	 *
	 * @throws IOException
	 * @throws DataException
	 */
	@Test
	public void testGetTimeStampColumnByID() throws IOException, DataException {
		initConfigFileParser();
		assertEquals("getTimeStampColumnByID() failed!!!", "timestamp1", parser.getTimeStampColumnByID("dset3"));

		assertEquals("getTimeStampColumnByID() failed!!!", "timestamp2", parser.getTimeStampColumnByID("dset4"));
	}

	/**
	 * Test method getParametersByID()
	 *
	 * @throws IOException
	 * @throws DataException
	 */
	@Test
	public void testGetParametersByID() throws IOException {
		initConfigFileParser();
		assertEquals("getParametersByID() failed!!!", "value1",
				((HashMap) parser.getParametersByID("dset3")).get("param1"));

		assertEquals("getParametersByID() failed!!!", "value2",
				((HashMap) parser.getParametersByID("dset3")).get("param2"));
	}

	/**
	 * Test method getTSFormatByID()
	 *
	 * @throws IOException
	 * @throws DataException
	 */
	@Test
	public void testGetTSFormatByID() throws IOException, DataException {
		initConfigFileParser();
		assertEquals("getTSFormatByID() failed!!!", "yy-MM-dd hh:mm:ss", parser.getTSFormatByID("dset1"));

		assertEquals("getTSFormatByID() failed!!!", "yy-MM-dd", parser.getTSFormatByID("dset3"));
	}

	/**
	 * a private utility method that help to check whether method parseXML2DOM()
	 * works properly
	 *
	 * @param Node   n
	 * @param String padding
	 */
	private void print(Node n, String padding) {
		this.testPrintln(padding + "{" + n.getName() + "}");
		this.testPrintln(padding + "\t" + "value:" + n.getValue());
		Iterator it = n.getAttributes().keySet().iterator();
		while (it.hasNext()) {
			String key = it.next().toString();
			String value = n.getAttrValue(key);
			this.testPrintln(padding + "\t" + "[" + key + "]:" + value);
		}

		if (n.getChildren() != null) {
			for (int i = 0; i < n.getChildren().size(); i++) {
				this.print((Node) n.getChildren().get(i), padding + "\t");
			}
		}
	}
}
