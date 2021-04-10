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

package org.eclipse.birt.report.engine.layout.emitter.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.birt.report.engine.nLayout.area.style.AreaConstants;

public class BackgroundImageLayoutTest extends TestCase {

	/**
	 * Test only the initial image position will be returned when the image is
	 * smaller than the area and repeat mode is no repeat.
	 * 
	 */
	public void testNoRepeatOfSmallImage() {
		Position imagePosition = new Position(2, 2);
		BackgroundImageLayout layout = getLayoutOfSmallImage();
		List positions = layout.getImagePositions(AreaConstants.NO_REPEAT);
		assertEquals(1, positions.size());
		assertEquals(imagePosition, positions.iterator().next());
	}

	public void testNoRepeatOfSmallImage2() {
		Position imagePosition = new Position(2, 2);
		BackgroundImageLayout layout = getLayoutOfSmallImage2();
		List positions = layout.getImagePositions(AreaConstants.NO_REPEAT);
		assertEquals(1, positions.size());
		assertEquals(imagePosition, positions.iterator().next());
	}

	public void testRepeatXOfSmallImage() {
		List expected = new ArrayList();
		for (float x = -1.0f; x <= 8.0f; x += 3.0) {
			expected.add(new Position(x, 2.0f));
		}
		BackgroundImageLayout layout = getLayoutOfSmallImage();
		List actual = layout.getImagePositions(AreaConstants.REPEAT_X);
		assertEquals(expected, actual);
	}

	public void testRepeatYOfSmallImage() {
		List expected = new ArrayList();
		for (float y = -1.0f; y <= 8.0f; y += 3.0) {
			expected.add(new Position(2.0f, y));
		}
		BackgroundImageLayout layout = getLayoutOfSmallImage();
		List actual = layout.getImagePositions(AreaConstants.REPEAT_Y);
		assertEquals(expected, actual);
	}

	public void testRepeatOfSmallImage() {
		List expected = new ArrayList();
		for (float y = -1.0f; y <= 8.0f; y += 3.0) {
			for (float x = -1.0f; x <= 8.0f; x += 3.0) {
				expected.add(new Position(x, y));
			}
		}
		Collections.sort(expected);
		BackgroundImageLayout layout = getLayoutOfSmallImage();
		List actual = layout.getImagePositions(AreaConstants.REPEAT);
		assertEquals(expected, actual);
	}

	/**
	 * Test when image is larger than area, only the initial image position will be
	 * returned.
	 * 
	 */
	public void testNoRepeatOfBigImage() {
		Position areaPosition = new Position(0, 0);
		Position areaSize = new Position(10, 10);
		Position imagePosition = new Position(-1, -1);
		Position imageSize = new Position(11, 11);
		BackgroundImageLayout layout = new BackgroundImageLayout(areaPosition, areaSize, imagePosition, imageSize);
		List positions = layout.getImagePositions(AreaConstants.NO_REPEAT);
		assertEquals(1, positions.size());
		assertEquals(imagePosition, positions.iterator().next());
		positions = layout.getImagePositions(AreaConstants.REPEAT_X);
		assertEquals(1, positions.size());
		assertEquals(imagePosition, positions.iterator().next());
		positions = layout.getImagePositions(AreaConstants.REPEAT_Y);
		assertEquals(1, positions.size());
		assertEquals(imagePosition, positions.iterator().next());
		positions = layout.getImagePositions(AreaConstants.REPEAT);
		assertEquals(1, positions.size());
		assertEquals(imagePosition, positions.iterator().next());
	}

	private BackgroundImageLayout getLayoutOfSmallImage() {
		Position areaPosition = new Position(0, 0);
		Position areaSize = new Position(10, 10);
		Position imagePosition = new Position(2, 2);
		Position imageSize = new Position(3, 3);
		return new BackgroundImageLayout(areaPosition, areaSize, imagePosition, imageSize);
	}

	private BackgroundImageLayout getLayoutOfSmallImage2() {
		Position areaPosition = new Position(2, 2);
		Position areaSize = new Position(10, 10);
		Position imagePosition = new Position(2, 2);
		Position imageSize = new Position(3, 3);
		return new BackgroundImageLayout(areaPosition, areaSize, imagePosition, imageSize);
	}
}
