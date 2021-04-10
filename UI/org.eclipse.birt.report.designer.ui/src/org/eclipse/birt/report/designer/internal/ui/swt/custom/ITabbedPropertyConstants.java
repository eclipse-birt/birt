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

package org.eclipse.birt.report.designer.internal.ui.swt.custom;

public interface ITabbedPropertyConstants {

	/**
	 * These horizontal margin around the composite. Each section should use a
	 * margin of 0, 0.
	 */
	public static final int HMARGIN = 6;

	/**
	 * These horizontal margin around the composite.
	 */
	public static final int VMARGIN = 6;

	/**
	 * Horizontal space to leave between related widgets. Each section should use
	 * these values for spacing its widgets. For example, you can use +/- HSPACE as
	 * the offset of a left or right FlatFormAttachment.
	 * 
	 * The tabbed property composite also inserts VSPACE pixels between section
	 * composites if more than one section is displayed.
	 */
	public static final int HSPACE = 5;

	/**
	 * Horizontal space to leave between related widgets.
	 */
	public static final int VSPACE = 4;

	/**
	 * Space to leave between the center of the property tab and the closest widget
	 * to the left or right. I.e. for a property tab whose widgets are logically
	 * divided into two halves, the total space between the halves should be
	 * 2*CENTER_SPACE.
	 */
	public static final int CENTER_SPACE = 10;
}
