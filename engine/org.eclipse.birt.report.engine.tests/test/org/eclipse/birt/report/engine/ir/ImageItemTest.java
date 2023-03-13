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

package org.eclipse.birt.report.engine.ir;

/**
 * Image Item test
 *
 */
public class ImageItemTest extends ReportItemTestCase {

	public ImageItemTest() {
		super(new ImageItemDesign());

	}

	/**
	 * Test get/setAction methods
	 *
	 * set the action
	 *
	 * then get it to test if they work correctly
	 */
	public void testAction() {
		ImageItemDesign image = new ImageItemDesign();
		ActionDesign action = new ActionDesign();

		// Set
		image.setAction(action);

		// Get
		assertEquals(image.getAction(), action);

	}

	/**
	 * Test get/setAltText methods
	 *
	 * set the texts
	 *
	 * then get them to test if they work correctly
	 */
	public void testAltText() {
		ImageItemDesign image = new ImageItemDesign();

		// Set
		String key = "TestKey";
		String text = "AltText";
		image.setAltText(Expression.newConstant(text));
		image.setAltTextKey(key);

		// Get
		assertEquals(image.getAltText().toString(), text);
		assertEquals(image.getAltTextKey(), key);

	}

	/**
	 * Test setImageExpression and getImageFormat methods
	 *
	 * set the image by a expression
	 *
	 * then get the expression and check the source type to test if they work
	 * correctly
	 */
	public void testExpression() {
		ImageItemDesign image = new ImageItemDesign();
		Expression exp = Expression.newScript("exp");
		Expression typeExp = Expression.newScript("typeExp");

		// Set
		image.setImageExpression(exp, typeExp);

		// Get
		assertEquals(image.getImageSource(), ImageItemDesign.IMAGE_EXPRESSION);
		assertEquals(image.getImageFormat(), typeExp);
	}

	/**
	 * Test get/setImageName methods
	 *
	 * set the image by a filename
	 *
	 * then get the name and check the source type to test if they work correctly
	 */
	public void testName() {
		ImageItemDesign image = new ImageItemDesign();

		// Set
		Expression imageName = Expression.newScript("TestImage.bmp");
		image.setImageName(imageName);

		// Get
		assertEquals(image.getImageName(), imageName);
		assertEquals(image.getImageSource(), ImageItemDesign.IMAGE_NAME);

	}

	/**
	 * Test get/setImageUri methods
	 *
	 * set the image by a URI address
	 *
	 * then get the address and check the source type to test if they work correctly
	 */
	public void testUri() {
		ImageItemDesign image = new ImageItemDesign();

		// test constant
		image.setImageUri(Expression.newConstant("http://www.actuate.com/images/navimages/v8/logo.gif"));
		assertEquals(image.getImageUri().getScriptText(), "http://www.actuate.com/images/navimages/v8/logo.gif");
		assertEquals(image.getImageSource(), ImageItemDesign.IMAGE_URI);

		// test expression
		Expression imageUriExpr = Expression.newScript("http://www.actuate.com/images/navimages/v8/logo.gif");
		image.setImageUri(imageUriExpr);
		assertEquals(image.getImageUri(), imageUriExpr);
		assertEquals(image.getImageSource(), ImageItemDesign.IMAGE_URI);
	}
}
