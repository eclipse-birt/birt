/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.StyleException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * TestCases for ImageHandle.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testChangeImageType()}</td>
 * <td>Sets a valid uri image.</td>
 * <td>The image is correctly set.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Sets a valid embedded image.</td>
 * <td>The image is correctly set.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Sets a valid expression image.</td>
 * <td>The image is correctly set.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetImageProperties()}</td>
 * <td>Gets different properties, like Color, Dimension</td>
 * <td>The property value or handle is returned correctly.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testSearchFile()}</td>
 * <td>The image file exists.</td>
 * <td>The image is valid and no exception.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>The image file does not exist.</td>
 * <td>Throws <code>SemanticException</code> with error code.
 * <code>SEMANTIC_ERROR_IMAGE_FILE_NOT_EXIST.</code></td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testSetImageProperties()}</td>
 * <td>Gets different properties.</td>
 * <td>Values are set correctly and the output file matches with the golden
 * file.</td>
 * </tr>
 * 
 * </table>
 * 
 * 
 */

public class ImageHandleTest extends BaseTestCase {

	ImageHandle fileImage, exprImage, urlImage;

	protected void setUp() throws Exception {
		super.setUp();
		openDesign("ImageItemHandleTest.xml"); //$NON-NLS-1$
		assertNotNull(designHandle);

		fileImage = (ImageHandle) designHandle.findElement("Image1"); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.IMAGE_REF_TYPE_FILE, fileImage.getSource());

		exprImage = (ImageHandle) designHandle.findElement("Image3"); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.IMAGE_REF_TYPE_EXPR, exprImage.getSource());

		urlImage = (ImageHandle) designHandle.findElement("Image6"); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.IMAGE_REF_TYPE_URL, urlImage.getSource());
	}

	/**
	 * Tests the image file searching mechanism.
	 * 
	 * @throws Exception
	 */

	public void testSearchFile() throws Exception {
		assertEquals(0, design.getErrorList().size());
		assertEquals(1, designHandle.getWarningList().size());
		assertEquals(0, designHandle.getErrorList().size());

		assertEquals("image2.jpg", fileImage.getURI()); //$NON-NLS-1$

		assertTrue(fileImage.isValid());
	}

	/**
	 * Tests to set different kinds of image source.
	 * 
	 * 
	 * @throws SemanticException if the image source is invalid.
	 */

	public void testChangeImageType() throws SemanticException {

		assertEquals("Image1", fileImage.getName()); //$NON-NLS-1$
		String refType = fileImage.getSource();
		assertEquals(DesignChoiceConstants.IMAGE_REF_TYPE_FILE, refType);

		// set the image-name value

		fileImage.setImageName("ImageName.jpg"); //$NON-NLS-1$
		assertEquals("ImageName.jpg", fileImage.getImageName()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.IMAGE_REF_TYPE_EMBED, fileImage.getSource());

		// get the third image which is expression type
		refType = exprImage.getSource();
		assertEquals(DesignChoiceConstants.IMAGE_REF_TYPE_EXPR, refType);
		exprImage.setImageName("newName.jpg"); //$NON-NLS-1$
		refType = exprImage.getSource();
		assertEquals(DesignChoiceConstants.IMAGE_REF_TYPE_EMBED, refType);

		// Change source type from url to file.

		urlImage.setURL("file:///c:\\logo.gif"); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.IMAGE_REF_TYPE_URL, urlImage.getSource());
		assertEquals("file:///c:\\logo.gif", urlImage.getURI()); //$NON-NLS-1$

	}

	/**
	 * Tests to set different kinds of properties of an image.
	 * 
	 * @throws Exception
	 */

	public void testSetImageProperties() throws Exception {

		// set embed image for a uri image.
		fileImage.setImageName("hello noway.jpg"); //$NON-NLS-1$

		// set the expression value
		exprImage.setValueExpression("newExpression"); //$NON-NLS-1$
		assertEquals("newExpression", exprImage.getValueExpression()); //$NON-NLS-1$

		exprImage.setValueExpression(null); // $NON-NLS-1$

		// get a image which hasn't any reference type. Then set the image
		// property for it and save to design file.
		ImageHandle image = (ImageHandle) designHandle.findMasterPage("My Page").getSlot(0).get(5); //$NON-NLS-1$

		assertNotNull(image);
		assertEquals(DesignChoiceConstants.IMAGE_REF_TYPE_FILE, image.getSource());

		image.setHeight("99999mm"); //$NON-NLS-1$
		assertEquals("99999mm", image.getHeight().getStringValue()); //$NON-NLS-1$
		assertEquals("mm", image.getHeight().getUnits()); //$NON-NLS-1$

		image.setWidth("10mm"); //$NON-NLS-1$
		assertEquals("10mm", image.getWidth().getStringValue()); //$NON-NLS-1$

		image.setSize(DesignChoiceConstants.IMAGE_SIZE_SCALE_TO_ITEM); // $NON-NLS-1$
		assertEquals(DesignChoiceConstants.IMAGE_SIZE_SCALE_TO_ITEM, image.getStringProperty(ImageItem.SIZE_PROP)); // $NON-NLS-1$

		image.setScale(0.1);
		image.setName("newNameIgmage4"); //$NON-NLS-1$

		Style style = new Style();
		style.getHandle(design).setName("testStyle"); //$NON-NLS-1$

		// add a styleHandle which is not in the design
		try {
			image.setStyle((SharedStyleHandle) style.getHandle(design));
			fail();
		} catch (StyleException se) {
			assertEquals(StyleException.DESIGN_EXCEPTION_NOT_FOUND, se.getErrorCode());
			System.out.println(se.getMessage());
		}

		// add a style which is not in the design
		try {
			image.setStyleElement(style);
			fail();
		} catch (StyleException se) {
			assertEquals(StyleException.DESIGN_EXCEPTION_NOT_FOUND, se.getErrorCode());
		}

		image.setStyleName("My-Style"); //$NON-NLS-1$
		assertEquals("My-Style", image.getStyle().getName()); //$NON-NLS-1$

		image.setBookmark("bookMark"); //$NON-NLS-1$
		assertEquals("bookMark", image.getBookmark()); //$NON-NLS-1$

		image.setX("100mm"); //$NON-NLS-1$
		assertEquals("100mm", image.getX().getStringValue()); //$NON-NLS-1$

		image.setY("1009mm"); //$NON-NLS-1$
		assertEquals("1009mm", image.getY().getStringValue()); //$NON-NLS-1$

		image.setProperty(ImageItem.ALT_TEXT_KEY_PROP, "alt key"); //$NON-NLS-1$
		assertEquals("alt key", image.getAltTextKey()); //$NON-NLS-1$

		image.setProperty(ImageItem.ALT_TEXT_PROP, "alt text"); //$NON-NLS-1$
		assertEquals("alt text", image.getAltText()); //$NON-NLS-1$

		String uri = image.getURI();
		image.setFile("C:\\image.jsp"); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.IMAGE_REF_TYPE_FILE, image.getSource());
		assertEquals("C:\\image.jsp", image.getURI()); //$NON-NLS-1$
		image.setURL(uri);
		assertEquals(DesignChoiceConstants.IMAGE_REF_TYPE_URL, image.getSource());

		Expression expr = new Expression("C:\\image.jsp", null);
		image.setFile(expr);
		assertEquals(DesignChoiceConstants.IMAGE_REF_TYPE_FILE, image.getSource());
		assertEquals("C:\\image.jsp", image.getURI()); //$NON-NLS-1$

		image.setURL(expr);
		assertEquals(DesignChoiceConstants.IMAGE_REF_TYPE_URL, image.getSource());
		assertEquals("C:\\image.jsp", image.getURI()); //$NON-NLS-1$

	}

	/**
	 * Tests to get all kinds of properties of an image.
	 * 
	 */

	public void testGetImageProperties() {

		ImageHandle imageHandle = (ImageHandle) designHandle.findMasterPage("My Page").getSlot(0).get(5); //$NON-NLS-1$
		assertNotNull(imageHandle);
		assertEquals(DesignChoiceConstants.IMAGE_REF_TYPE_FILE, imageHandle.getSource());

		// get the actionHandle
		ActionHandle action = fileImage.getActionHandle();
		assertNotNull(action);
		assertEquals(fileImage, action.getElementHandle());

		IChoice choiceList[] = fileImage.getChoices(ImageItem.SIZE_PROP);
		assertEquals(3, choiceList.length);
		assertNotNull(fileImage.getColorProperty(Style.BACKGROUND_COLOR_PROP));

		assertEquals("green", fileImage.getExtends().getProperty( //$NON-NLS-1$
				Style.BACKGROUND_COLOR_PROP));

		assertEquals("newImage", fileImage.getExtends().getName()); //$NON-NLS-1$

		assertEquals(design.findPage("My Page"), fileImage.getContainer()); //$NON-NLS-1$
		assertEquals(0, fileImage.getContainerSlotHandle().getSlotID());

		DimensionHandle dh = fileImage.getDimensionProperty(ImageItem.WIDTH_PROP);
		assertNotNull(dh);

		dh = fileImage.getHeight();
		assertNotNull(dh);

		// get the display label
		assertEquals("Image1", fileImage.getDisplayLabel()); //$NON-NLS-1$

		// get the extends

		assertEquals(designHandle.getComponents().get(0), fileImage.getExtends());

	}

	/**
	 * Test getImageName with prefix.
	 * 
	 */

	public void testGetImageName() {
		GridHandle myGrid1 = (GridHandle) designHandle.findElement("myGrid1"); //$NON-NLS-1$
		CellHandle cell1 = (CellHandle) ((RowHandle) myGrid1.getRows().get(0)).getCells().get(0);
		ImageHandle imageHandle = (ImageHandle) cell1.getContent().get(0);
		assertEquals("A_001.jpg", imageHandle.getImageName()); //$NON-NLS-1$

		GridHandle myGrid2 = (GridHandle) designHandle.findElement("myGrid2"); //$NON-NLS-1$
		RowHandle row2 = (RowHandle) myGrid2.getRows().get(0);
		CellHandle cell2 = (CellHandle) row2.getCells().get(0);

		imageHandle = (ImageHandle) cell2.getContent().get(0);
		assertEquals("Lib2.002.jpg", imageHandle.getImageName()); //$NON-NLS-1$

	}

	/**
	 * Tests source property.
	 * 
	 * @throws DesignFileException
	 * 
	 */

	public void testGetSource() throws DesignFileException {
		openDesign("ImageItemHandleTest_1.xml"); //$NON-NLS-1$
		ImageHandle image = (ImageHandle) designHandle.findElement("Image"); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.IMAGE_REF_TYPE_FILE, image.getSource());
		designHandle.close();
		designHandle = null;
	}
}