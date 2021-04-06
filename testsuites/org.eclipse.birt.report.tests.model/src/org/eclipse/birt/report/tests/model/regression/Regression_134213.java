/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * It's a backward compatibility bug. Unsupport old format property. Steps to
 * reproduce:
 * <p>
 * 1.The following is part of the design file:
 * 
 * <pre>
 *     &lt;scalar-parameter name=&quot;Param 2&quot;&gt;
 *        &lt;property name=&quot;valueType&quot;&gt;dynamic&lt;/property&gt;
 *        &lt;property name=&quot;dataType&quot;&gt;float&lt;/property&gt;
 *        &lt;property name=&quot;hidden&quot;&gt;false&lt;/property&gt;
 *        &lt;text-property name=&quot;helpText&quot; key=&quot;help&quot;&gt;scalar para help&lt;/text-  
 *        		property&gt;
 *        &lt;property name=&quot;controlType&quot;&gt;list-box&lt;/property&gt;
 *        &lt;property name=&quot;concealValue&quot;&gt;false&lt;/property&gt;
 *        &lt;property name=&quot;allowBlank&quot;&gt;false&lt;/property&gt;
 *       	&lt;property name=&quot;allowNull&quot;&gt;true&lt;/property&gt;
 *      	&lt;property name=&quot;format&quot;&gt;##,###.##&lt;/property&gt;
 *     	&lt;property name=&quot;alignment&quot;&gt;left&lt;/property&gt;
 *    	&lt;property name=&quot;listLimit&quot;&gt;5&lt;/property&gt;
 *     &lt;/scalar-parameter&gt;
 * </pre>
 * 
 * 2. Use ScalarParameterHandle.getFormat()/getPattern(), the return value is
 * null
 * <p>
 * <b>Expected result:</b>
 * <p>
 * Return value is "##,###.##" Please add judge condition for the old format
 * </p>
 * <b>Test description:</b>
 * <p>
 * Parser old the design file, get category and pattern correctly.
 * </p>
 */

public class Regression_134213 extends BaseTestCase {

	private final static String INPUT = "regression_134213.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(INPUT, INPUT);

	}

	/**
	 * @throws DesignFileException
	 */
	public void test_regression_134213() throws DesignFileException {
		openDesign(INPUT);
		ScalarParameterHandle param1 = (ScalarParameterHandle) designHandle.getParameters().get(0);
		ScalarParameterHandle param2 = (ScalarParameterHandle) designHandle.getParameters().get(1);
		ScalarParameterHandle param3 = (ScalarParameterHandle) designHandle.getParameters().get(2);
		ScalarParameterHandle param4 = (ScalarParameterHandle) designHandle.getParameters().get(3);

		assertEquals("##,###.##", param1.getPattern()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM, param2.getCategory());
		assertEquals("@@.@@", param2.getPattern()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY, param3.getCategory());
		assertEquals("abc:##:00", param4.getPattern()); //$NON-NLS-1$
	}
}
