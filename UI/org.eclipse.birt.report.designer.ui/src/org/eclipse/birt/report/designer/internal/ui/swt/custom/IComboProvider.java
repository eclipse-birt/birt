/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.swt.custom;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;

/**
 * Keeps models and processes image conversion for a given model.
 */
public interface IComboProvider {

	/**
	 * Gets a image that has the given width and height for a given model object.
	 * <p>
	 * NOTE: The caller code must explicitly invoke <code>Imaage.dispose()</code> to
	 * release system resources.
	 *
	 * @param item    The model object.
	 * @param width   The width of the image.
	 * @param height  The height of the image.
	 * @param control The control that has the image.
	 * @param parent  The parent control of the image control.
	 * @return An image object.
	 */
	Image getImage(Object item, int width, int height, Control control, Control parent);

	/**
	 * Gets the models.
	 *
	 * @return The array contains model objects.
	 */
	Object[] getItems();

	/**
	 * Gets the display models.
	 *
	 * @return The array contains model display objects.
	 */
	Object[] getDisplayItems();

	/**
	 * Sets models.
	 *
	 * @param items The array contains model objects.
	 */
	void setItems(Object[] items);

	void setIndex(Object item);
}
