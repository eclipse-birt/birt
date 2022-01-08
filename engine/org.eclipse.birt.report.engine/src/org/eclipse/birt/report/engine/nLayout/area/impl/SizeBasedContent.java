/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
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
package org.eclipse.birt.report.engine.nLayout.area.impl;

import org.eclipse.birt.report.engine.content.IContent;

public class SizeBasedContent {
	/**
	 * The original content.
	 */
	public IContent content;

	/**
	 * The value indicates how to place the rendered sizeBasedContent. It presents
	 * the floating horizontal position for the sizeBasedContent. For block text,
	 * the value is normally 0; for inline text, the value is the first line start x
	 * position.
	 */
	public int floatPos;

	/**
	 * The value indicates from where (relative to the original content)to render
	 * the sizeBasedContent. For block text, the value is the total height which
	 * have been rendered in the previous pages. For inline text, the value is the
	 * total width which have been rendered in the previous pages.
	 */
	public int offsetInContent;

	/**
	 * For block text, it indicates from offsetInContent, the height of dimension
	 * need to be rendered. For inline text, it indicates from offsetInContent, the
	 * width of dimension need to be rendered.
	 */
	public int dimension;

	/**
	 * The width restriction for the sizeBasedContent.
	 */
	public int width;

	public boolean isChildrenRemoved;

}
