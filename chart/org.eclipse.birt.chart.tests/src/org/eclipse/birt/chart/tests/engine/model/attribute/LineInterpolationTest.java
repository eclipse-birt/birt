/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
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

package org.eclipse.birt.chart.tests.engine.model.attribute;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.LineInterpolation;
import org.eclipse.emf.ecore.EEnum;

import junit.framework.TestCase;

public class LineInterpolationTest extends TestCase {

	public void testConstant() {
		assertEquals(LineInterpolation.LINEAR, LineInterpolation.LINEAR_LITERAL.getValue());
		assertEquals(LineInterpolation.STEP_BEFORE, LineInterpolation.STEP_BEFORE_LITERAL.getValue());
		assertEquals(LineInterpolation.STEP_AFTER, LineInterpolation.STEP_AFTER_LITERAL.getValue());
		assertEquals(LineInterpolation.STEP_CENTER, LineInterpolation.STEP_CENTER_LITERAL.getValue());
		assertEquals(4, LineInterpolation.VALUES.size());
	}

	public void testGet() {
		assertEquals(LineInterpolation.STEP_AFTER_LITERAL, LineInterpolation.get(LineInterpolation.STEP_AFTER));
		// XML literals equal the enum names (no underscore, like AxisType.DateTime)
		assertEquals(LineInterpolation.LINEAR_LITERAL, LineInterpolation.get("Linear")); //$NON-NLS-1$
		assertEquals(LineInterpolation.STEP_BEFORE_LITERAL, LineInterpolation.get("StepBefore")); //$NON-NLS-1$
		assertEquals(LineInterpolation.STEP_AFTER_LITERAL, LineInterpolation.get("StepAfter")); //$NON-NLS-1$
		assertEquals(LineInterpolation.STEP_CENTER_LITERAL, LineInterpolation.get("StepCenter")); //$NON-NLS-1$
		assertNull(LineInterpolation.get("Step_After")); //$NON-NLS-1$
		assertNull(LineInterpolation.get("stepafter")); //$NON-NLS-1$
		assertNull(LineInterpolation.get("No Match")); //$NON-NLS-1$
	}

	public void testGetByName() {
		assertEquals(LineInterpolation.STEP_AFTER_LITERAL, LineInterpolation.getByName("StepAfter")); //$NON-NLS-1$
		assertEquals("StepAfter", LineInterpolation.STEP_AFTER_LITERAL.getLiteral()); //$NON-NLS-1$
		assertEquals("StepAfter", LineInterpolation.STEP_AFTER_LITERAL.toString()); //$NON-NLS-1$
		assertNull(LineInterpolation.getByName("Step_After")); //$NON-NLS-1$
	}

	public void testPackageWiring() {
		EEnum interpolation = AttributePackage.Literals.LINE_INTERPOLATION;
		assertSame(interpolation,
				AttributePackage.eINSTANCE.getEClassifiers().get(AttributePackage.LINE_INTERPOLATION));
		assertEquals(AttributePackage.LINE_INTERPOLATION, interpolation.getClassifierID());
		assertEquals(AttributePackage.LINE_INTERPOLATION_OBJECT,
				AttributePackage.Literals.LINE_INTERPOLATION_OBJECT.getClassifierID());
		assertEquals(LineInterpolation.STEP_AFTER_LITERAL,
				AttributeFactory.eINSTANCE.createFromString(interpolation, "StepAfter")); //$NON-NLS-1$
		assertEquals("StepAfter", //$NON-NLS-1$
				AttributeFactory.eINSTANCE.convertToString(interpolation, LineInterpolation.STEP_AFTER_LITERAL));
		try {
			AttributeFactory.eINSTANCE.createFromString(interpolation, "Step_After"); //$NON-NLS-1$
			fail("the underscore spelling is not a literal"); //$NON-NLS-1$
		} catch (IllegalArgumentException expected) {
			// expected
		}
	}
}
