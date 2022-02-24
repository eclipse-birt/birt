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

package org.eclipse.birt.report.engine.layout.emitter.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.engine.nLayout.area.style.AreaConstants;

/**
 * Represents the algorithm to calculate the positions of background images for
 * a rectangle area. The background maybe set to "no-repeat", "repeat-x",
 * "repeat-y" or no "repeat"
 * 
 */
public class BackgroundImageLayout {

	private Position areaPosition, areaSize, imagePosition, imageSize;

	/**
	 * Constructor.
	 * 
	 * @param areaPosition  the left up corner of the area which need to be filled
	 *                      by the background.
	 * @param areaSize      the size of the area. Width is represented by
	 *                      <code>Position.x</code>, height by
	 *                      <code>Position.y</code>.
	 * @param imagePosition the initial position of the image. This image is used to
	 *                      specify the offset of the image.
	 * @param imageSize     the image size.
	 */
	public BackgroundImageLayout(Position areaPosition, Position areaSize, Position imagePosition, Position imageSize) {
		this.areaPosition = areaPosition;
		this.areaSize = areaSize;
		this.imagePosition = imagePosition;
		this.imageSize = imageSize;
	}

	public List<Position> getImagePositions(int repeat) {
		if (repeat < AreaConstants.NO_REPEAT || repeat > AreaConstants.REPEAT) {
			throw new IllegalArgumentException(" repeat should in range 0-3 : " + repeat);
		}
		Set<Position> positions = new HashSet<Position>();
		calculateRepeatX(imagePosition, repeat, positions);
		if (isRepeatY(repeat)) {
			float x = imagePosition.x;
			float y = imagePosition.y;
			while (y > areaPosition.y) {
				y = y - imageSize.y;
				calculateRepeatX(new Position(x, y), repeat, positions);
			}
			y = imagePosition.y;
			while (y + imageSize.y < areaPosition.y + areaSize.y) {
				y = y + imageSize.y;
				calculateRepeatX(new Position(x, y), repeat, positions);
			}
		}

		// Conver set to list and sort the list.
		List<Position> list = Arrays.asList(positions.toArray(new Position[positions.size()]));
		Collections.sort(list);
		return list;
	}

	public List<Position> getImagePositions(String repeat) {
		int repeatMode = AreaConstants.REPEAT;
		if (!("repeat".equals(repeat))) {
			if ("repeat-x".equals(repeat)) {
				repeatMode = AreaConstants.REPEAT_X;
			} else if ("repeat-y".equals(repeat)) {
				repeatMode = AreaConstants.REPEAT_Y;
			} else if ("no-repeat".equals(repeat)) {
				repeatMode = AreaConstants.NO_REPEAT;
			}
		}
		return getImagePositions(repeatMode);
	}

	private void calculateRepeatX(Position initPosition, int repeat, Set<Position> positions) {
		positions.add(initPosition);
		if (isRepeatX(repeat)) {
			float x = initPosition.x;
			float y = initPosition.y;
			while (x > areaPosition.x) {
				x = x - imageSize.x;
				positions.add(new Position(x, y));
			}
			x = initPosition.x;
			while (x + imageSize.x < areaPosition.x + areaSize.x) {
				x = x + imageSize.x;
				positions.add(new Position(x, y));
			}
		}
	}

	private static boolean isRepeatX(int repeat) {
		return repeat == AreaConstants.REPEAT || repeat == AreaConstants.REPEAT_X;
	}

	private static boolean isRepeatY(int repeat) {
		return repeat == AreaConstants.REPEAT || repeat == AreaConstants.REPEAT_Y;
	}

}
