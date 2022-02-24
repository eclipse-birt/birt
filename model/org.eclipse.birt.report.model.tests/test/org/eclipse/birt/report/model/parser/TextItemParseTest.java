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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * The Test Case of text item parse.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testProperties()}</td>
 * <td>parse the design file and check the related content of text item, such as
 * static text which has CDATA feature, value expr, help text key and so on.
 * </td>
 * <td>Content of the properties are consistent with the design file</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testWrite()}</td>
 * <td>parse, write and parse, write again. The result of two writer files is
 * the same.</td>
 * <td>The two writer file is the same.</td>
 * </tr>
 * 
 * </table>
 * 
 */

public class TextItemParseTest extends BaseTestCase {

	/*
	 * @see BaseTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

	}

	/**
	 * Test the write for user-defined properties.
	 * 
	 * @throws Exception
	 */

	public void testWrite() throws Exception {
		openDesign("TextItemParseTest.xml"); //$NON-NLS-1$

		TextItemHandle text = (TextItemHandle) designHandle.findElement("text1"); //$NON-NLS-1$

		String contentType = text.getContentType();
		assertEquals(DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML, contentType);

		text.setContentType(DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML);
		text.setContent("new content hello <> <html></html>"); //$NON-NLS-1$
		text.setHasExpression(false);
		text.setTagType("Div"); //$NON-NLS-1$
		text.setLanguage("English"); //$NON-NLS-1$
		text.setAltTextExpression(new Expression("Alt Text", ExpressionType.CONSTANT)); //$NON-NLS-1$
		text.setOrder(1); // $NON-NLS-1$

		text = (TextItemHandle) designHandle.findElement("text2"); //$NON-NLS-1$
		text.setContent("    text & < > ' \" static    ]]>"); //$NON-NLS-1$
		assertEquals("    text & < > ' \" static    ]]>", text.getProperty(TextItem.CONTENT_PROP)); //$NON-NLS-1$
		assertEquals("    text & < > ' \" static    ]]>", text.getStringProperty(TextItem.CONTENT_PROP)); //$NON-NLS-1$

		text = (TextItemHandle) designHandle.findElement("text3"); //$NON-NLS-1$
		text.setContentKey("odd 1"); //$NON-NLS-1$

		save();
		assertTrue(compareFile("TextItemParseTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Test the properties for user-defined properties.
	 * 
	 * @throws Exception
	 */

	public void testProperties() throws Exception {
		openDesign("TextItemParseTest.xml"); //$NON-NLS-1$

		TextItemHandle text = (TextItemHandle) designHandle.findElement("text1"); //$NON-NLS-1$

		String contentType = text.getContentType();
		assertEquals(DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML, contentType);
		assertTrue(text.hasExpression());

		assertEquals("text & < > ' \" static", //$NON-NLS-1$
				text.getContent());

		assertEquals("Div", text.getTagType()); //$NON-NLS-1$
		assertEquals("English", text.getLanguage()); //$NON-NLS-1$
		assertEquals("Alt Text", text.getAltTextExpression().getStringExpression()); //$NON-NLS-1$
		assertEquals(1, text.getOrder()); // $NON-NLS-1$

		text = (TextItemHandle) designHandle.findElement("text2"); //$NON-NLS-1$
		assertNull(text.getContentKey());
		assertEquals(DesignChoiceConstants.TEXT_CONTENT_TYPE_AUTO, text.getContentType());
		assertEquals("    text value expr  ]]>  ", text.getContent()); //$NON-NLS-1$
		// test default value of Role in Text
		assertEquals("p", text.getTagType()); //$NON-NLS-1$

		text = (TextItemHandle) designHandle.findElement("text3"); //$NON-NLS-1$
		assertEquals("dynamic", text.getContentKey()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.TEXT_CONTENT_TYPE_AUTO, text.getContentType());
		assertEquals("text & &lt; &gt; &apos; &quot; static", text.getContent()); //$NON-NLS-1$

		text = (TextItemHandle) designHandle.findElement("text4"); //$NON-NLS-1$
		assertEquals("<hello>text & </hello>&lt; <hello>&gt; &apos; &quot; static</hello>", //$NON-NLS-1$
				text.getContent());
	}

}
