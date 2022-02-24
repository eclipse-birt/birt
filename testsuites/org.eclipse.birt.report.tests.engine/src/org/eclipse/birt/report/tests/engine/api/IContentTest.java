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

package org.eclipse.birt.report.tests.engine.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.ContentVisitorAdapter;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.impl.ActionContent;
import org.eclipse.birt.report.engine.content.impl.ContainerContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * <b>Test IContent API methods</b>
 */
public class IContentTest extends EngineCase {

	private IContent content;

	protected void setUp() throws Exception {
		super.setUp();
		content = new ReportContent().createContainerContent();
	}

	/**
	 * Test set/getX() methods.
	 */
	public void testX() {
		assertNull(content.getX());

		DimensionType x1 = new DimensionType(1, "in");
		content.setX(x1);
		assertEquals(x1, content.getX());

		content.setX(null);
		assertNull(content.getX());
	}

	/**
	 * Test set/getY() methods.
	 */
	public void testY() {
		assertNull(content.getY());

		DimensionType y1 = new DimensionType(1, "in");
		content.setY(y1);
		assertEquals(y1, content.getY());

		content.setY(null);
		assertNull(content.getY());
	}

	/**
	 * Test read/writeContent() methods.
	 * 
	 * @throws IOException
	 */
	public void testContent() throws IOException {
		IReportContent rContent = new ReportContent();
		IContent content1 = rContent.createContainerContent();
		IContent content2 = rContent.createContainerContent();
		content1.setName("myContent");
		content1.setHelpText("myHelpText");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		((ContainerContent) content1).setVersion(1);
		content1.writeContent(dos);
		InputStream is = new ByteArrayInputStream(bos.toByteArray());
		DataInputStream dis = new DataInputStream(is);
		((ContainerContent) content2).setVersion(1);
		content2.readContent(dis, null);
		assertEquals(content1.getName(), content2.getName());
		assertEquals(content1.getHelpText(), content2.getHelpText());
	}

	/**
	 * Test set/getWidth() methods.
	 */
	public void testWidth() {
		assertNull(content.getWidth());

		DimensionType width = new DimensionType(1, "in");
		content.setWidth(width);
		assertEquals(width, content.getWidth());

		content.setWidth(null);
		assertNull(content.getWidth());
	}

	/**
	 * Test set/getTOC() methods.
	 */
	public void testToc() {
		assertNull(content.getTOC());

		Object toc = new Object();
		content.setTOC(toc);
		assertEquals(toc, content.getTOC());

		content.setTOC(null);
		assertNull(content.getTOC());
	}

	/**
	 * Test set/getStyleClass() methods.
	 */
	public void testStyleClass() {
		assertNull(content.getStyleClass());

		content.setStyleClass("style1");
		assertEquals("style1", content.getStyleClass());

		content.setStyleClass(null);
		assertNull(content.getStyleClass());
	}

	/**
	 * Test set/getReportContent() methods.
	 */
	public void testReportContent() {
		IReportContent report = new ReportContent();
		content.setReportContent(report);
		assertEquals(report, content.getReportContent());
	}

	/**
	 * Test set/getName() methods.
	 */
	public void testName() {
		assertNull(content.getName());
		content.setName("name");
		assertEquals("name", content.getName());

		content.setName(null);
		assertNull(content.getName());
	}

	/**
	 * Test set/getInstanceID() methods.
	 */
	public void testInstanceID() {
		assertNull(content.getInstanceID());
		InstanceID iid = new InstanceID(null, 1, null);
		content.setInstanceID(iid);
		assertEquals(iid, content.getInstanceID());
		content.setInstanceID(null);
		assertNull(content.getInstanceID());
	}

	/**
	 * Test set/getInlineStyle() mehtods.
	 */
	public void testInlineStyle() {
		assertNull(content.getInlineStyle());
		IStyle style = new ReportContent().createStyle();
		content.setInlineStyle(style);
		assertEquals(style, content.getInlineStyle());
	}

	/**
	 * Test set/getHyperlinkAction() methods
	 */
	public void testHyperlinkAction() {
		assertNull(content.getHyperlinkAction());
		IHyperlinkAction action = new ActionContent();
		content.setHyperlinkAction(action);
		assertEquals(action, content.getHyperlinkAction());
	}

	/**
	 * Test set/getHelpText() methods
	 */
	public void testHelpText() {
		assertNull(content.getHelpText());
		content.setHelpText("help");
		assertEquals("help", content.getHelpText());
	}

	/**
	 * Test set/getHeight() methods.
	 */
	public void testHeight() {
		assertNull(content.getHeight());

		DimensionType height = new DimensionType(1, "in");
		content.setHeight(height);
		assertEquals(height, content.getHeight());
	}

	/**
	 * Test set/getGenerateBy() methods.
	 */
	public void testGenerateBy() {
		assertNull(content.getGenerateBy());
		Object obj = new Object();
		content.setGenerateBy(obj);
		assertEquals(obj, content.getGenerateBy());
	}

	/**
	 * Test set/getBookmark() methods
	 */
	public void testBookmark() {
		assertNull(content.getBookmark());
		content.setBookmark("bookmark");
		assertEquals("bookmark", content.getBookmark());
	}

	/**
	 * Test set/getExtension() methods.
	 */
	public void testExtension() {
		assertNull(content.getExtension(0));
		assertNull(content.getExtension(1));
		content.setExtension(0, "extension0");
		assertEquals("extension0", content.getExtension(0));
		// content.setExtension( -1, "invalid" );
		// assertEquals( "invalid", content.getExtension( -1 ) );
		content.setExtension(1, null);
		assertNull(content.getExtension(1));
	}

	/**
	 * Test accept() method.
	 * 
	 * @throws BirtException
	 */
	public void testAccept() throws BirtException {
		Object value = new Object();
		IContentVisitor visitor = new ContentVisitorAdapter();
		Object value1 = content.accept(visitor, value);
		assertEquals(value, value1);

		value1 = content.accept(visitor, null);
		assertNull(value1);
	}

}
