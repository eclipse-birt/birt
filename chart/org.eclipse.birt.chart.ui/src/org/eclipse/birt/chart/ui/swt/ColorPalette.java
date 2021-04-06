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

package org.eclipse.birt.chart.ui.swt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

/**
 * Manages and register color resource.
 */

public final class ColorPalette {

	private static ColorPalette instance = null;
	private List<RGB> colorLib = new ArrayList<RGB>();
	private Stack<RGB> colorAvailable = new Stack<RGB>();
	private HashMap<String, Color> hmColorUsed = new HashMap<String, Color>();

	private ColorPalette() {
		initColorLibrary();
		restore();
	}

	public synchronized static ColorPalette getInstance() {
		if (instance == null) {
			instance = new ColorPalette();
		}
		return instance;
	}

	private void initColorLibrary() {
		colorLib.add(new RGB(170, 200, 255));
		colorLib.add(new RGB(255, 255, 128));
		colorLib.add(new RGB(128, 255, 128));
		colorLib.add(new RGB(128, 255, 255));
		colorLib.add(new RGB(255, 128, 255));
		colorLib.add(new RGB(255, 128, 64));
		colorLib.add(new RGB(0, 255, 128));
		colorLib.add(new RGB(200, 156, 156));
		colorLib.add(new RGB(128, 128, 255));
		colorLib.add(new RGB(210, 210, 210));
		colorLib.add(new RGB(184, 184, 114));
		colorLib.add(new RGB(128, 128, 128));
		Collections.reverse(colorLib);
	}

	/**
	 * This map stores color name - Color pairs, used to quickly lookup a Color of a
	 * predefined color.
	 * 
	 * @param rgb RGB value of color
	 */
	private Color getColor(RGB rgb) {
		if (rgb == null) {
			return null;
		}

		String key = rgb.toString();
		Color color = JFaceResources.getColorRegistry().get(key);
		if (color == null) {
			JFaceResources.getColorRegistry().put(key, rgb);
			color = JFaceResources.getColorRegistry().get(key);
		}
		return color;
	}

	private Color getAvailableColor() {
		RGB rgb = colorAvailable.isEmpty() ? null : (RGB) colorAvailable.pop();
		if (rgb == null) {
			return null;
		}
		return getColor(rgb);
	}

	/**
	 * Registers the expression with a color. Duplicate expression will be ignored.
	 * 
	 * @param expression registered expression
	 */
	public void putColor(String expression) {
		if (expression != null && expression.length() > 0) {
			expression = expression.toUpperCase();
			if (!hmColorUsed.containsKey(expression)) {
				hmColorUsed.put(expression, getAvailableColor());
			}
		}
	}

	public void retrieveColor(String expression) {
		if (expression != null && expression.length() > 0) {
			expression = expression.toUpperCase();
			if (hmColorUsed.containsKey(expression)) {
				Color oldColor = hmColorUsed.remove(expression);
				colorAvailable.push(oldColor.getRGB());
			}
		}
	}

	/**
	 * Fetches the color by registered expression
	 * 
	 * @param expression registered expression
	 * @return the registered color or null if not found
	 */
	public Color getColor(String expression) {
		if (expression != null && expression.length() > 0) {
			return hmColorUsed.get(expression.toUpperCase());
		}
		return null;
	}

	/**
	 * Restores the current to the initial state.
	 * 
	 */
	public void restore() {
		colorAvailable.clear();
		colorAvailable.addAll(colorLib);
		hmColorUsed.clear();
	}

	public void updateKeys(Collection<String> keys) {
		Set<String> newKeys = new HashSet<String>();
		for (String key : keys) {
			newKeys.add(key.toUpperCase());
		}

		Set<String> oldKeys = hmColorUsed.keySet();

		Set<String> keysToRemove = new HashSet<String>(oldKeys);
		keysToRemove.removeAll(newKeys);

		for (String key : keysToRemove) {
			retrieveColor(key);
		}

		Set<String> keysToAdd = newKeys;
		keysToAdd.removeAll(oldKeys);

		for (String key : keysToAdd) {
			putColor(key);
		}
	}

}