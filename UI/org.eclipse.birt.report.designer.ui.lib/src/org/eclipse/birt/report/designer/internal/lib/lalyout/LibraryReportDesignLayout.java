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

package org.eclipse.birt.report.designer.internal.lib.lalyout;

import org.eclipse.birt.report.designer.internal.ui.layout.AbstractPageFlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;

/**
 * 
 */

public class LibraryReportDesignLayout extends AbstractPageFlowLayout {

	/**
	 * The constructor.
	 * 
	 * @param viewer
	 */
	public LibraryReportDesignLayout(GraphicalEditPart owner) {
		super(owner);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
	 */
	public void layout(IFigure parent) {
		super.layout(parent);

		Dimension prefSize = getPreferredSize(parent, getInitSize().width, -1).getCopy();

		Rectangle bounds = parent.getBounds().getCopy();

		bounds.height = Math.max(prefSize.height, getInitSize().height);
		bounds.width = getInitSize().width;

		// bounds = new PrecisionRectangle( bounds);

		// owner.getFigure().translateToAbsolute( bounds );

		Result result = getReportBounds(bounds);

		bounds = result.reportSize;

		parent.setBounds(bounds);
		Rectangle rect = new Rectangle(0, 0, bounds.x + bounds.width + result.rightSpace,
				bounds.y + bounds.height + result.bottomSpace);
		setViewProperty(rect, bounds);

		// parent.getParent( ).setSize( rect.getSize( ) );
	}

}
