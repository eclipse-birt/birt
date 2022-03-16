/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

package org.eclipse.birt.chart.tests.engine.model.attribute;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.impl.PaletteImpl;

import junit.framework.TestCase;

/**
 *
 */

public class PaletteTest extends TestCase {

	private void testColorDefinition(Palette p, int index, int red, int green, int blue) {
		ColorDefinition cd = (ColorDefinition) p.getEntries().get(index);
		assertEquals(red, cd.getRed());
		assertEquals(green, cd.getGreen());
		assertEquals(blue, cd.getBlue());
	}

	public void testShift() {
		Palette p = PaletteImpl.create(0, true);

		// Test standard update
		p.shift(0);
		assertEquals(32, p.getEntries().size());
		testColorDefinition(p, 0, 80, 166, 218);
		testColorDefinition(p, 8, 192, 192, 192);

		// Test left shift
		p.shift(-1);
		assertEquals(32, p.getEntries().size());
		testColorDefinition(p, 0, 242, 88, 106);// 1
		testColorDefinition(p, 8, 255, 255, 128);// 9
		testColorDefinition(p, 31, 80, 166, 218);// 0

		// Test right shift
		p.shift(1);
		assertEquals(32, p.getEntries().size());
		testColorDefinition(p, 0, 255, 128, 0);// 31
		testColorDefinition(p, 8, 128, 128, 0);// 7

		// Test invalid step
		p.shift(-32);
		assertEquals(32, p.getEntries().size());
		testColorDefinition(p, 0, 80, 166, 218);
		testColorDefinition(p, 8, 192, 192, 192);

		// Test invalid step
		p.shift(33);
		assertEquals(32, p.getEntries().size());
		testColorDefinition(p, 0, 80, 166, 218);
		testColorDefinition(p, 8, 192, 192, 192);
	}

	public void testShiftWithSize() {
		Palette p = PaletteImpl.create(0, true);

		p.shift(0, 32);
		assertEquals(32, p.getEntries().size());
		testColorDefinition(p, 0, 80, 166, 218);
		testColorDefinition(p, 8, 192, 192, 192);

		p.shift(-1, 8);
		assertEquals(8, p.getEntries().size());
		testColorDefinition(p, 0, 242, 88, 106);// 1
		testColorDefinition(p, 7, 80, 166, 218);// 0

		p.shift(-1, 3);
		assertEquals(3, p.getEntries().size());
		testColorDefinition(p, 0, 242, 88, 106);// 1
		testColorDefinition(p, 1, 232, 172, 57);// 2
		testColorDefinition(p, 2, 80, 166, 218);// 0

		p.shift(1, 8);
		assertEquals(8, p.getEntries().size());
		testColorDefinition(p, 0, 128, 128, 0);// 7
		testColorDefinition(p, 1, 80, 166, 218);// 0

		p.shift(1, 3);
		assertEquals(3, p.getEntries().size());
		testColorDefinition(p, 0, 232, 172, 57);// 2
		testColorDefinition(p, 1, 80, 166, 218);// 0
		testColorDefinition(p, 2, 242, 88, 106);// 1

		// Test invalid size
		p.shift(0, 33);
		assertEquals(32, p.getEntries().size());
		testColorDefinition(p, 0, 80, 166, 218);
		testColorDefinition(p, 8, 192, 192, 192);
	}
}
