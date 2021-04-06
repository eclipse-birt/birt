/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Regression description:</b>
 * <p>
 * Image builder - URI and file source type are treated as same
 * <p>
 * The image item is special item which can access both the global resources and
 * local resources. In the BIRT specification, we can find that the image can
 * access the resources through file or URI expression:
 * <p>
 * Accessing file images:
 * 
 * <pre>
 *        &lt;image id=&quot;4&quot;&gt;
 *            &lt;property name=��source��&gt;file&lt;/property&gt;
 *            &lt;expression name=&quot;uri&quot;&gt;&quot;pict 103.jpg&quot;&lt;/expression&gt;
 *         &lt;/image&gt;
 * </pre>
 * 
 * Accessing URI images
 * 
 * <pre>
 *           &lt;image id=&quot;5&quot;&gt;
 *              &lt;property name=��source��&gt;url&lt;/property&gt;
 *               &lt;expression
 *   name=&quot;uri&quot;&gt;&quot;http://www.google.com/intl/en/images/logo.gif&quot;&lt;/expression&gt;
 *   &lt;/image&gt;
 * </pre>
 * 
 * Unfortunately, the designer doesn��t distinguish those two kinds of
 * resources. The created report design is:
 * 
 * <pre>
 *   &lt;image id=&quot;4&quot;&gt;
 *      &lt;expression name=&quot;uri&quot;&gt;&quot;pict 103.jpg&quot;&lt;/expression&gt;
 *   &lt;/image&gt;
 *   
 *   &lt;image id=&quot;5&quot;&gt;
 *      &lt;expression
 *   name=&quot;uri&quot;&gt;&quot;http://www.google.com/intl/en/images/logo.gif&quot;&lt;/expression&gt;
 *   &lt;/image&gt;
 * </pre>
 * 
 * <p>
 * That means both the local resources and the global resources are specified by
 * the URI expression.
 * <p>
 * <b>Test description:</b> Add two type of image FILE and URL to report, make
 * sure the types are correctly distinguished in the design file.
 * <p>
 * <p>
 */
public class Regression_149922 extends BaseTestCase {

	private final static String GOLDEN = "regression_149922.golden"; //$NON-NLS-1$
	private final static String OUTPUT = "regression_149922.out"; //$NON-NLS-1$

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyGoldenToFile(GOLDEN_FOLDER + "/" + GOLDEN);

	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws Exception
	 */

	public void test_regression_149922() throws Exception {
		this.createDesign();
		ElementFactory factory = designHandle.getElementFactory();

		ImageHandle image = factory.newImage("img1"); //$NON-NLS-1$
		image.setTypeExpression(DesignChoiceConstants.IMAGE_REF_TYPE_FILE);
		image.setFile("\"images/pic1.jpg\""); //$NON-NLS-1$
		designHandle.getBody().add(image);

		ImageHandle image2 = factory.newImage("img1"); //$NON-NLS-1$
		image2.setTypeExpression(DesignChoiceConstants.IMAGE_REF_TYPE_URL);
		image2.setURL("\"http://www.google.com/intl/en/images/logo.gif\""); //$NON-NLS-1$
		designHandle.getBody().add(image2);

		String TempFile = this.genOutputFile(OUTPUT);
		designHandle.saveAs(TempFile);
		assertTrue(compareTextFile(GOLDEN, OUTPUT));

	}
}
