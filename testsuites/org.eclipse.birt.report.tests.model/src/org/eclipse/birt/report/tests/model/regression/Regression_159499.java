/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.io.IOException;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TOCHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Bug Description:</b>
 * </p>
 * Table of Contents Formatting can apply style
 * <p>
 * Test cases:
 * <ol>
 * <li>Set Custom Style to TOC.
 * <li>Set Custom Style to TOC/Extend from library
 * <li>Change Style of TOC
 * <li>Multi-level TOC
 * <li>Use HighlightRule Structure
 * <li>Predefined styles
 * </ol>
 * <p>
 * <b>Test Description:</b>
 *
 * @author Tianli Zhang
 */

public class Regression_159499 extends BaseTestCase {

	private final static String REPORT = "Regression_159499.xml"; //$NON-NLS-1$
	private final static String LIB = "Regression_159499_lib.xml"; //$NON-NLS-1$
	private final static String LIB_TEMP = "Regression_159499_lib_temp.xml";

	@Override
	public void setUp() throws Exception {
		super.setUp();
		removeResource();

		copyResource_INPUT(REPORT, REPORT);
		copyResource_INPUT(LIB, LIB);
		copyResource_INPUT(LIB_TEMP, LIB_TEMP);
	}

	@Override
	public void tearDown() {
		removeResource();
	}

	/**
	 * <p>
	 * <b>Test1 Description</b>
	 * <li>1, New a report.
	 * <li>2, New a custom style "Style", set Font=Arial, Font Color= Red,
	 * Background color=Yellow.
	 * <li>3, New a label, go to property editor, select "Table of Contents", set
	 * "Label", set Style for TOC.
	 * <p>
	 * <br>
	 *
	 * @throws DesignFileException
	 * @throws SemanticException
	 * @throws IOException
	 */
	public void test_Regression_159499_Test1() throws DesignFileException, SemanticException, IOException {
		openDesign(REPORT);

		// find label
		LabelHandle label = (LabelHandle) designHandle.findElement("label");
		assertNotNull(label);

		// find style
		StyleHandle style1 = designHandle.findStyle("NewStyle");
		StyleHandle style2 = designHandle.findStyle("Style2");
		assertNotNull(style1);
		assertNotNull(style2);

		// set a toc
		TOC toc = StructureFactory.createTOC("toc111");
		TOCHandle tocHandle = (TOCHandle) label.addTOC(toc);
		tocHandle.setStyleName(style1.getName());
		assertEquals("NewStyle", tocHandle.getStyleName());

		assertEquals("#FF0000", tocHandle.getColor().getStringValue());
		assertEquals("\"Arial\"", tocHandle.getFontFamily().getValue());
		assertEquals("#FFFF00", tocHandle.getBackgroundColor().getStringValue());

	}

	/**
	 * <p>
	 * <b>Test2 Description</b>
	 * <li>1, New a libray.
	 * <li>2, New a custom style "Style1", set Font=Arial, Font Color= Red,
	 * Background color=Yellow.
	 * <li>3, New a label, go to property editor, select "Table of Contents", set
	 * "Label", set Style for TOC.
	 * <li>4, New a report, extend the label from the library.
	 * <p>
	 * <br>
	 *
	 * @throws DesignFileException
	 * @throws SemanticException
	 * @throws IOException
	 * @throws IOException
	 */
	public void test_Regression_159499_Test2() throws DesignFileException, SemanticException, IOException {
		/**
		 * openLibrary( LIB ); // find label LabelHandle lib_label = (LabelHandle)
		 * libraryHandle.findElement( "label1" ); assertNotNull( lib_label ); // find
		 * style StyleHandle style = libraryHandle.findStyle( "Style" ); assertNotNull(
		 * style ); // set a toc TOC toc = StructureFactory.createTOC( ); TOCHandle
		 * tocHandle = (TOCHandle) lib_label.addTOC( toc ); tocHandle.setStyleName(
		 * style.getName( ) ); assertEquals( "Style", tocHandle.getStyleName( ) );
		 * assertEquals( "#FF0000", tocHandle.getColor( ).getStringValue( ) );
		 * assertEquals( "\"Arial\"", tocHandle.getFontFamily( ).getValue( ) );
		 * assertEquals( "#FFFF00", tocHandle.getBackgroundColor( ).getStringValue( ) );
		 * saveAs( libraryHandle, "Regression_159499_lib_temp.xml" );
		 */

		openLibrary(LIB_TEMP);

		// find lib_label
		LabelHandle lib_label = (LabelHandle) libraryHandle.findElement("label1");
		assertNotNull(lib_label);

		// find style
		StyleHandle style = libraryHandle.findStyle("Style");
		assertNotNull(style);

		openDesign(REPORT);

		// Using label in library
		designHandle.includeLibrary(LIB_TEMP, "lib");
		LabelHandle label = (LabelHandle) designHandle.getElementFactory().newElementFrom(lib_label, "label1");
		assertNotNull(label);

		// get label's toc
		TOCHandle tocHandle = label.getTOC();
		assertNotNull(tocHandle);
		assertEquals("#FF0000", tocHandle.getColor().getStringValue());
		assertEquals("\"Arial\"", tocHandle.getFontFamily().getValue());
		assertEquals("#FFFF00", tocHandle.getBackgroundColor().getStringValue());

	}

	/**
	 * <p>
	 * <b>Test3 Description</b>
	 * <li>1, New a report.
	 * <li>2, New a custom style "Style1", set Font=Arial, Font Color= Red,
	 * Background color=Yellow. New a custom style "Style2", set Font=Cursive, Font
	 * Color= Fuchsia, Background color=Blue.
	 * <li>3, New a label, go to property editor,Select "Table of Contents", set
	 * "Label", set Style1 for TOC.
	 * <li>4, Set Style2 for TOC
	 * <p>
	 * <br>
	 *
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_Regression_159499_Test3() throws DesignFileException, SemanticException {
		openDesign(REPORT);

		// find label
		LabelHandle label = (LabelHandle) designHandle.findElement("label");
		assertNotNull(label);

		// find style
		StyleHandle style1 = designHandle.findStyle("NewStyle");
		StyleHandle style2 = designHandle.findStyle("Style2");

		assertNotNull(style1);
		assertNotNull(style2);

		// set a toc
		TOC toc = StructureFactory.createTOC();
		TOCHandle tocHandle = (TOCHandle) label.addTOC(toc);
		tocHandle.setStyleName(style1.getName());
		assertEquals("NewStyle", tocHandle.getStyleName());

		assertEquals("#FF0000", tocHandle.getColor().getStringValue());
		assertEquals("\"Arial\"", tocHandle.getFontFamily().getValue());
		assertEquals("#FFFF00", tocHandle.getBackgroundColor().getStringValue());

		TOC toc1 = StructureFactory.createTOC();
		TOCHandle tocHandle1 = (TOCHandle) label.addTOC(toc1);
		tocHandle1.setStyleName(style2.getName());
		assertEquals("Style2", tocHandle1.getStyleName());

		assertEquals("#FF00FF", tocHandle1.getColor().getStringValue());
		assertEquals("cursive", tocHandle1.getFontFamily().getValue());
		assertEquals("#0000FF", tocHandle1.getBackgroundColor().getStringValue());

	}

	/**
	 * <p>
	 * <b>Test4 Description</b>
	 * <P>
	 * Multi TOC
	 * <P>
	 * <p>
	 * <br>
	 *
	 * @throws DesignFileException
	 * @throws SemanticException
	 * @throws SemanticException
	 */
	public void test_Regression_159499_Test4() throws DesignFileException, SemanticException {
		openDesign(REPORT);

		TableHandle tableHandle = (TableHandle) designHandle.findElement("table");
		// System.out.println( designHandle.getAllTocs( ).size( ) );
		SlotHandle slot = tableHandle.getGroups();

		GroupHandle group1 = (GroupHandle) slot.get(0);
		GroupHandle group2 = (GroupHandle) slot.get(1);
		GroupHandle group3 = (GroupHandle) slot.get(2);

		assertEquals("row[\"COUNTRY\"]", group1.getTOC().getExpression());
		assertEquals("row[\"STATE\"]", group2.getTOC().getExpression());
		assertEquals("row[\"CITY\"]", group3.getTOC().getExpression());

		TOCHandle tocHandle1 = group1.getTOC();
		TOCHandle tocHandle2 = group2.getTOC();
		TOCHandle tocHandle3 = group3.getTOC();

		tocHandle1.setStyleName("NewStyle");
		tocHandle2.setStyleName("Style2");
		tocHandle3.setStyleName("Style3");

		assertEquals("NewStyle", tocHandle1.getStyleName());
		assertEquals("Style2", tocHandle2.getStyleName());
		assertEquals("Style3", tocHandle3.getStyleName());

		assertEquals("#FF0000", tocHandle1.getColor().getStringValue());
		assertEquals("\"Arial\"", tocHandle1.getFontFamily().getValue());
		assertEquals("#FFFF00", tocHandle1.getBackgroundColor().getStringValue());

		assertEquals("#FF00FF", tocHandle2.getColor().getStringValue());
		assertEquals("cursive", tocHandle2.getFontFamily().getValue());
		assertEquals("#0000FF", tocHandle2.getBackgroundColor().getStringValue());

		assertEquals("#FFA500", tocHandle3.getColor().getStringValue());
		assertEquals("fantasy", tocHandle3.getFontFamily().getValue());
		assertEquals("#808080", tocHandle3.getBackgroundColor().getStringValue());

	}

	/**
	 * <p>
	 * <b>Test4 Description</b>
	 * <P>
	 * Using highlight style
	 * <P>
	 * <p>
	 * <br>
	 *
	 * @throws DesignFileException
	 * @throws SemanticException
	 * @throws SemanticException
	 * @throws SemanticException
	 */
	public void test_Regression_159499_Test5() throws DesignFileException, SemanticException {
		openDesign(REPORT);
		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("label1");
		assertNotNull(labelHandle);

		StyleHandle style = designHandle.findStyle("Style4");
		assertNotNull(style);

	}
}
