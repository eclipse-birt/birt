/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/

package org.eclipse.birt.report.model.parser;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.FreeFormHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.elements.FreeForm;
import org.eclipse.birt.report.model.util.XMLParserException;

/**
 * <table border="1" cellpadding="0" cellspacing="0" style="border-collapse:
 * collapse" bordercolor="#111111" width="100%" id="AutoNumber5" height="99">
 * <tr>
 * <td width="33%" height="16"><b>Method </b></td>
 * <td width="33%" height="16"><b>Test Case </b></td>
 * <td width="34%" height="16"><b>Expected Result </b></td>
 * </tr>
 * <tr>
 * <td width="33%" height="16">{@link #testParser()}</td>
 * <td width="33%" height="16">Test the freeForm propertyies</td>
 * <td width="34%" height="16">the correct value returned</td>
 * </tr>
 * <tr>
 * <td width="33%" height="14"></td>
 * <td width="33%" height="14">Use iterator to test the reportItems slot in
 * freeform</td>
 * <td width="34%" height="14">content can be retrieved.</td>
 * </tr>
 * <tr>
 * <td width="33%" height="16"></td>
 * <td width="33%" height="16">Test the freeform extends relationship</td>
 * <td width="34%" height="16">extend relationship correct.</td>
 * </tr>
 * <tr>
 * <td width="33%" height="16">{@link #testSemanticCheck()}</td>
 * <td width="33%" height="16">Test the freeform's name should not be duplicate
 * </td>
 * <td width="34%" height="16">Semantic error</td>
 * </tr>
 * <tr>
 * <td width="33%" height="16"></td>
 * <td width="33%" height="16">Test the reportItems slot can not hold
 * master-page</td>
 * <td width="34%" height="16">Semantic error</td>
 * </tr>
 * <tr>
 * <td width="33%" height="16">{@link #testWritter()}</td>
 * <td width="33%" height="16">Set new value to properties and save it.</td>
 * <td width="34%" height="16">new value should be save into the output file.
 * </td>
 * </tr>
 * <tr>
 * <td width="33%" height="16"></td>
 * <td width="33%" height="16"></td>
 * <td width="34%" height="16"></td>
 * </tr>
 * <tr>
 * <td width="33%" height="16"></td>
 * <td width="33%" height="16"></td>
 * <td width="34%" height="16"></td>
 * </tr>
 * </table>
 *
 *
 *
 */
public class FreeFormParseTest extends ParserTestCase {
	String fileName = "FreeFormParseTest.xml"; //$NON-NLS-1$
	String outFileName = "FreeFormParseTest_out.xml"; //$NON-NLS-1$
	String goldenFileName = "FreeFormParseTest_golden.xml"; //$NON-NLS-1$
	String semanticCheckFileName = "FreeFormParseTest_1.xml"; //$NON-NLS-1$

	/*
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * test parser.
	 *
	 * @throws Exception
	 */

	/*
	 * <free-form> <comments>Sample Section </comments> <display-name>The Section
	 * </display-name> <toc>"This Section" </toc> <style name="My
	 * Style"> <border fill-color="red"> </style> <report-items> <label
	 * name="StyleLabel" x="1" y="2" height="3" width="4"> <style name="My
	 * Style"> <font size="14"/> </style> <text>Customer Name </text> </label>
	 * <free-form name="freeFrom1" extends="freeForm2"> <comments>Inner freeform
	 * </comments> <report-items>
	 *
	 * <label x="6" y=".5" height=".25" width="1"> <text>Today's Date </text>
	 * </label> <free-form name="level3FreeForm"/> </report-items> </free-from>
	 * <free-form name="freeForm2"/> <data distinct="repeat" distinct-reset="foo">
	 * <value-expr>[price] * [quan] </value-expr> <help-text>This is a data item.
	 * </help-text> </data> <text> <value-expr>[foo.bar] </value-expr> </text> <grid
	 * name="grid"/>
	 *
	 * </report-items> </free-form>
	 */

	public void testParser() throws Exception {
		FreeFormHandle freeFormHandle = getFreeForm();
		assertEquals("Sample Section", freeFormHandle.getStringProperty(FreeForm.COMMENTS_PROP)); //$NON-NLS-1$
		assertEquals("The Section", freeFormHandle.getStringProperty(FreeForm.DISPLAY_NAME_PROP)); //$NON-NLS-1$

		SlotHandle reportItem = freeFormHandle.getSlot(0);
		assertNotNull(reportItem);
		assertEquals(3, reportItem.getCount());

		// test in the content of reportItems slot

		Iterator it = reportItem.iterator();

		Object label = it.next();
		assertTrue(label instanceof LabelHandle);

		Object freeForm1 = it.next();
		assertTrue(freeForm1 instanceof FreeFormHandle);

		Object freeForm2 = it.next();
		assertTrue(freeForm2 instanceof FreeFormHandle);

		// test the inner freeform1

		assertEquals("Inner freeform", ((FreeFormHandle) freeForm1).getStringProperty(FreeForm.COMMENTS_PROP)); //$NON-NLS-1$
		assertEquals(2, ((FreeFormHandle) freeForm1).getSlot(0).getCount());

		// retrieve the 3d leve freeform---level3FreeForm

		FreeFormHandle level3FreeForm = (FreeFormHandle) ((FreeFormHandle) freeForm1).getSlot(0).get(1);
		assertNotNull(level3FreeForm);
		assertEquals("level3FreeForm", level3FreeForm.getName()); //$NON-NLS-1$

	}

	/**
	 * test write().
	 *
	 * @throws Exception
	 */
	public void testWritter() throws Exception {
		FreeFormHandle freeFormHandle = getFreeForm();

		freeFormHandle.setStringProperty(FreeForm.COMMENTS_PROP, "new comment"); //$NON-NLS-1$
		assertEquals("new comment", freeFormHandle.getStringProperty(FreeForm.COMMENTS_PROP)); //$NON-NLS-1$

		freeFormHandle.setProperty(FreeForm.STYLE_PROP, null);
		SlotHandle reportItem = freeFormHandle.getSlot(0);
		FreeFormHandle freeForm1 = (FreeFormHandle) reportItem.get(1);

		SlotHandle innerReportItem = freeForm1.getSlot(0);
		freeForm1.setExtendsElement(null);
		innerReportItem.dropAndClear(0);

		FreeFormHandle freeForm2 = (FreeFormHandle) reportItem.get(2);
		reportItem.dropAndClear(2);
		innerReportItem.add(freeForm2);
		save();
		assertTrue(compareFile(goldenFileName));

	}

	/**
	 * Test semantic errors.
	 *
	 */
	public void testSemanticCheck() {

		try {
			openDesign(semanticCheckFileName);

		} catch (DesignFileException e) {
			assertEquals(2, e.getErrorList().size());
			int i = 0;

			assertEquals(NameException.DESIGN_EXCEPTION_DUPLICATE,
					((ErrorDetail) e.getErrorList().get(i++)).getErrorCode());
			assertEquals(XMLParserException.DESIGN_EXCEPTION_UNKNOWN_TAG,
					((ErrorDetail) e.getErrorList().get(i++)).getErrorCode());
		}

	}

	private FreeFormHandle getFreeForm() throws Exception {

		openDesign(fileName);
		// printErrors ( );
		assertEquals(0, design.getErrorList().size());

		SlotHandle body = designHandle.getBody();
		Iterator it = body.iterator();

		Object freeFormHandle = it.next();
		assertTrue(freeFormHandle instanceof FreeFormHandle);

		return (FreeFormHandle) freeFormHandle;

	}

}
