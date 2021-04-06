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

package org.eclipse.birt.report.model.parser;

import java.util.List;

import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.GraphicMasterPageHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.elements.GraphicMasterPage;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Unit test for ImageItem, the corresponding test file is "imageitem_test.xml"
 * in the 'xml' folder. The test case is:
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="black">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected Result</th>
 * 
 * <tr>
 * <td>testImageContainer</td>
 * <td>In the XML file, MasterPage contains an ImageItem, the name is
 * "ImageItem"</td>
 * <td>Get the image instance to see if its container is a MasterPage</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Get the master page first, then get image item from the slot of the page
 * </td>
 * <td>Image exists in the slot of master page</td>
 * </tr>
 * 
 * <tr>
 * <td>testImageProperties</td>
 * <td>Check all the properties of the image item defined in the XML file.</td>
 * <td>The property values should be the same as which are defined in the XML
 * file.</td>
 * </tr>
 * 
 * <tr>
 * <td>testDefaultProperties</td>
 * <td>Image Size is not correctly set in the design file</td>
 * <td>The error is recorded in the error list, and when get this properties,
 * the default value, 0, is returned</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Get the help text of the image item, which is not set in the design file
 * </td>
 * <td>The help text is null</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Get the default scale property.</td>
 * <td>The default scale is 0.0</td>
 * </tr>
 * 
 * <tr>
 * <td>testErrors</td>
 * <td>Errors are collected in the error list of the design</td>
 * <td>The errors is the same as the golden file.</td>
 * </tr>
 * 
 * 
 * <tr>
 * <td>testSetImageProperties</td>
 * <td>Get a image instance from the design file, then modify the properties and
 * finally write back to an XML file</td>
 * <td>Properties are correctly set on the image item, and the final output file
 * is identical to the golden file</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testErrors()}</td>
 * <td>Test semantic errors with the design file input.</td>
 * <td>The errors are collected, such as the scale is negative.</td>
 * </tr>
 * 
 * </table>
 * <p>
 * Other test cases:
 * <ul>
 * <li>Style test is done in ReportItemTest</li>
 * <li>ActionType is done in ActionParserTest</li>
 * </ul>
 * 
 * 
 * @see org.eclipse.birt.report.model.parser.ActionParseTest
 */

public class ImageItemParseTest extends BaseTestCase {

	protected ImageItem image;
	protected ImageHandle handle;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Return the image item defined in the design file by its name. The test cases
	 * can use this method to get different image instances for test.
	 * 
	 * @param name The name of image item.
	 * @return the image item instance according to the name.
	 */

	private ImageItem findImageItemByName(String name) {
		NameSpace ns = design.getNameHelper().getNameSpace(ReportDesign.ELEMENT_NAME_SPACE);
		assertTrue(ns.contains(name));

		image = (ImageItem) ns.getElement(name);
		handle = image.handle(design);

		return image;
	}

	/**
	 * Find image item by name, then test its container.
	 * 
	 * @throws Exception
	 * 
	 */
	public void testImageContainer() throws Exception {
		openDesign("ImageItemParseTest.xml"); //$NON-NLS-1$

		image = findImageItemByName("Image1"); //$NON-NLS-1$
		Object o = image.getContainer();
		assertTrue(o instanceof MasterPage);

		// Check from container to see the image is stored in the content slot.
		GraphicMasterPage page = (GraphicMasterPage) design.findPage("My Page"); //$NON-NLS-1$
		GraphicMasterPageHandle mHandle = page.handle(design);
		SlotHandle sHandle = mHandle.getContent();
		DesignElementHandle imageHandle = sHandle.get(2); // Image1 Handle
		assertTrue(imageHandle instanceof ImageHandle);
		assertEquals("Image1", ((ImageHandle) imageHandle).getElement().getName()); //$NON-NLS-1$

		imageHandle = sHandle.get(3);
		assertTrue(imageHandle instanceof ImageHandle);
		assertEquals("Image2", ((ImageHandle) imageHandle).getElement().getName()); //$NON-NLS-1$
	}

	/**
	 * Test the properties of the image. These properties are well defined in the
	 * design file.
	 * 
	 * @throws Exception if errors occur when reads the design file
	 */

	public void testReadProperties() throws Exception {
		openDesign("ImageItemParseTest.xml"); //$NON-NLS-1$

		// 1st image
		image = findImageItemByName("Image1"); //$NON-NLS-1$
		double scale = handle.getScale();
		assertTrue(scale == 0.8);

		// test default value of Role in Image
		assertEquals("figure", handle.getTagType()); //$NON-NLS-1$

		String size = handle.getSize();
		assertEquals(DesignChoiceConstants.IMAGE_SIZE_SCALE_TO_ITEM, size);

		String helpText = handle.getAltText();
		assertEquals("Help Test For Image Item", helpText); //$NON-NLS-1$

		String refType = handle.getSource();
		assertEquals(DesignChoiceConstants.IMAGE_REF_TYPE_URL, refType);

		String url = handle.getURI();
		assertEquals("http://www.eclipse.org/birt/test/1.jpg", url); //$NON-NLS-1$

		assertEquals(DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK, handle.getActionHandle().getLinkType());

		assertFalse(handle.fitToContainer());
		assertTrue(handle.isProportionalScale());

		// 2nd image
		image = findImageItemByName("Image2"); //$NON-NLS-1$
		refType = handle.getSource();
		assertEquals(DesignChoiceConstants.IMAGE_REF_TYPE_EMBED, refType);

		String imageName = handle.getImageName();
		assertEquals("image2.jpg", imageName); //$NON-NLS-1$

		helpText = handle.getHelpText();
		assertEquals("Image2-Help-Text", helpText); //$NON-NLS-1$

		// 3nd image
		image = findImageItemByName("Image3"); //$NON-NLS-1$
		refType = handle.getSource();
		assertEquals(DesignChoiceConstants.IMAGE_REF_TYPE_EXPR, refType);

		String typeExpression = handle.getTypeExpression();
		assertEquals("Type Expression", typeExpression); //$NON-NLS-1$

		String valueExpression = handle.getValueExpression();
		assertEquals("Value Expression", valueExpression); //$NON-NLS-1$

		helpText = handle.getHelpText();
		assertEquals("Help Text", helpText); //$NON-NLS-1$

		// 4th image
		image = findImageItemByName("Image4"); //$NON-NLS-1$
		ActionHandle actionHandle = image.handle(design).getActionHandle();
		assertNull(actionHandle);

		image = findImageItemByName("Body Image"); //$NON-NLS-1$
		handle = image.handle(design);

		// make sure that this label exists in the body slot.

		assertEquals(ReportDesign.BODY_SLOT, handle.getContainer().findContentSlot(handle));
		assertEquals("bodyImage.jpg", handle.getImageName()); //$NON-NLS-1$
		assertEquals("BodyImage-Help-Text", handle.getHelpText()); //$NON-NLS-1$
		assertEquals("Body Image Key", handle.getHelpTextKey()); //$NON-NLS-1$

		actionHandle = handle.getActionHandle();
		assertNotNull(actionHandle);

		assertEquals("http://localhost:8080/bodyImage.jpg", actionHandle.getURI()); //$NON-NLS-1$

		assertTrue(handle.fitToContainer());

		assertEquals("Div", handle.getTagType()); //$NON-NLS-1$
		assertEquals("English", handle.getLanguage()); //$NON-NLS-1$
		assertEquals(1, handle.getOrder()); // $NON-NLS-1$
	}

	/**
	 * Print out the errors encountered during parsing the design file.
	 */

	public void testErrors() {
		try {
			openDesign("ImageItemParseTest_1.xml"); //$NON-NLS-1$
			fail();
		} catch (DesignFileException e) {
			List<ErrorDetail> errors = e.getErrorList();
			assertEquals(2, errors.size());

			int i = 0;
			assertEquals(DesignParserException.DESIGN_EXCEPTION_IMAGE_REF_CONFLICT,
					e.getErrorList().get(i++).getErrorCode());
			assertEquals(SemanticError.DESIGN_EXCEPTION_INVALID_IMAGEREF_EXPR_VALUE,
					e.getErrorList().get(i++).getErrorCode());
		}
	}

	/**
	 * Test the properties, which is not correctly set in the design file, of the
	 * image item, to see how the framework handle this situation.
	 * 
	 * @throws Exception if errors occur when reads the design file
	 */

	public void testImageDefaultProperties() throws Exception {
		openDesign("ImageItemParseTest.xml"); //$NON-NLS-1$

		// 3rd image, default property values.
		image = findImageItemByName("Image3"); //$NON-NLS-1$
		assertNotNull(image);

		String size = handle.getSize();
		assertEquals(DesignChoiceConstants.IMAGE_SIZE_SIZE_TO_IMAGE, size);

		String helpText = handle.getAltText();
		assertNull(helpText);

		double scale = handle.getScale();
		assertTrue(scale == 1.0);

	}

	/**
	 * After getting the image item instance from the design file, set properties on
	 * this image to see the property can be correctly set on the image and also can
	 * be written into the design file.
	 * 
	 * @throws Exception if errors occur when reads or writes the design file
	 */

	public void testWriteProperties() throws Exception {
		openDesign("ImageItemParseTest.xml"); //$NON-NLS-1$

		image = findImageItemByName("Image1"); //$NON-NLS-1$
		handle = image.handle(design);
		handle.setProportionalScale(false);

		image = findImageItemByName("Image2"); //$NON-NLS-1$
		handle = image.handle(design);
		handle.setName("ImageTwo"); //$NON-NLS-1$
		assertEquals("ImageTwo", handle.getName()); //$NON-NLS-1$

		handle.setSize(DesignChoiceConstants.IMAGE_SIZE_SIZE_TO_IMAGE);
		assertTrue(handle.getSize().compareToIgnoreCase(DesignChoiceConstants.IMAGE_SIZE_SIZE_TO_IMAGE) == 0);

		handle.setScale(4.0f);
		assertTrue(4.0 == handle.getScale());

		handle.setImageName("image_two.jpg"); //$NON-NLS-1$
		assertEquals("image_two.jpg", handle.getImageName()); //$NON-NLS-1$

		image = findImageItemByName("Image3"); //$NON-NLS-1$
		handle.setTypeExpression("HelloType"); //$NON-NLS-1$
		assertEquals("HelloType", handle.getTypeExpression()); //$NON-NLS-1$

		handle.setValueExpression("HelloValue"); //$NON-NLS-1$
		assertEquals("HelloValue", handle.getValueExpression()); //$NON-NLS-1$

		image = findImageItemByName("Body Image"); //$NON-NLS-1$
		handle = image.handle(design);

		handle.setImageName("BodyImage"); //$NON-NLS-1$
		handle.setBookmark("No bookmark"); //$NON-NLS-1$
		ActionHandle actionHandle = handle.getActionHandle();
		actionHandle.setURI("http://localhost/body.jpg"); //$NON-NLS-1$
		handle.setHelpText("new body image help text"); //$NON-NLS-1$
		handle.setHelpTextKey("new resource key for body image help text"); //$NON-NLS-1$
		handle.setFitToContainer(false);

		handle.setTagType("Figure"); //$NON-NLS-1$
		handle.setLanguage("English"); //$NON-NLS-1$
		handle.setOrder(1); // $NON-NLS-1$

		save();
		assertTrue(compareFile("ImageItemParseTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Test the semantic error in the design file if the image file just specify the
	 * URL value.
	 * 
	 * @throws Exception
	 */
	public void testImageFileExist() throws Exception {
		openDesign("ImageItemParseTest_2.xml"); //$NON-NLS-1$

		ErrorDetail detail = (ErrorDetail) designHandle.getWarningList().get(0);
		assertEquals(SemanticError.DESIGN_EXCEPTION_UNSUPPORTED_ELEMENT, detail.getErrorCode());

		detail = (ErrorDetail) designHandle.getWarningList().get(1);
		assertEquals(SemanticError.DESIGN_EXCEPTION_UNSUPPORTED_ELEMENT, detail.getErrorCode());

		ImageHandle imageHandle = (ImageHandle) designHandle.findElement("image2"); //$NON-NLS-1$
		assertNotNull(imageHandle);

	}
}
