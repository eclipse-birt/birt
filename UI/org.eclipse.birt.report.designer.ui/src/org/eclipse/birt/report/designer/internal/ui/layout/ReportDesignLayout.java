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

package org.eclipse.birt.report.designer.internal.ui.layout;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Provides layout management for ReportDesign element.
 * This class is extened from ReportFlowLayout. The main behavior is similar with
 * flowlayout but add inline and block support.
 */

public class ReportDesignLayout extends ReportFlowLayout
{

	Rectangle initSize = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
	 */
	public void layout( IFigure parent )
	{
		super.layout( parent );

		Dimension prefSize = getPreferredSize( parent, initSize.width, -1 ).getCopy( );

		Rectangle bounds = parent.getBounds( ).getCopy( );

		bounds.height = Math.max( prefSize.height, initSize.height );
		bounds.width = initSize.width;

		parent.setBounds( bounds );
	}

	/**
	 * Set the init size of bounds.
	 * @param rect
	 */
	public void setInitSize( Rectangle rect )
	{
		initSize = rect.getCopy( );
	}
}