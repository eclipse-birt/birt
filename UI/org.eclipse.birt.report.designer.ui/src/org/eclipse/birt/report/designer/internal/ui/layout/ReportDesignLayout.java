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

import org.eclipse.birt.report.designer.internal.ui.editors.parts.DeferredGraphicalViewer;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.ReportDesignMarginBorder;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;

/**
 * Provides layout management for ReportDesign element. This class is extened
 * from ReportFlowLayout. The main behavior is similar with flowlayout but add
 * inline and block support.
 */

public class ReportDesignLayout extends AbstractPageFlowLayout
{

	
	private boolean isAuto = false;
	/**
	 * The constructor.
	 * 
	 * @param viewer
	 */
	public ReportDesignLayout( GraphicalEditPart owner )
	{
		super(owner);			
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.LayoutManager#layout(org.eclipse.draw2d.IFigure)
	 */
	public void layout( IFigure parent )
	{
 		super.layout( parent );

		Dimension prefSize = getPreferredSize( parent, getInitSize().width, -1 )
				.getCopy( );

		Rectangle bounds = parent.getBounds( ).getCopy( );

		bounds.height = Math.max( prefSize.height, getInitSize().height );
		//if (!isAuto())
		if (true)
		{
			if (bounds.width < getInitSize().width)
			{
				bounds.width = getInitSize().width;
			}
		}
		else
		{
			bounds.width = Math.max( prefSize.width, getInitSize().width );
		}
		
		//add for the auto layout
		
		
		//bounds = new PrecisionRectangle( bounds);

		//owner.getFigure().translateToAbsolute( bounds );

		
		
		Result result = getReportBounds( bounds );
		bounds = result.reportSize;
		
	
		

		
		Rectangle rect = new Rectangle( 0, 0, bounds.x + bounds.width
				+ result.rightSpace, bounds.y + bounds.height
				+ result.bottomSpace );
		
		
		ReportDesignMarginBorder border = (ReportDesignMarginBorder)parent.getBorder();
		Insets insets = border.getInsets(parent);
		int contentWidth = prefSize.width - insets.getWidth() - getInitSize().width + getInitInsets().getWidth();
		if (insets.right < contentWidth)
		{
			
			ReportDesignMarginBorder reportDesignMarginBorder = new ReportDesignMarginBorder( new Insets(insets.top, insets.left, insets.bottom,
					contentWidth ));
			reportDesignMarginBorder.setBackgroundColor(border.getBackgroundColor());
			parent.setBorder( reportDesignMarginBorder );
			bounds.width = bounds.width + contentWidth - insets.right;
		}
		else if (getInitInsets().right > contentWidth && insets.right != getInitInsets().right)
		{
			ReportDesignMarginBorder reportDesignMarginBorder = new ReportDesignMarginBorder( new Insets(insets.top, insets.left, insets.bottom,
					getInitInsets().right ));
			reportDesignMarginBorder.setBackgroundColor(border.getBackgroundColor());
			parent.setBorder( reportDesignMarginBorder );
			bounds.width = getInitSize().width;;
		}
		setViewProperty(rect, bounds);
		
		Rectangle temp = bounds.getCopy();
		temp.width = getInitSize().width;
		getOwner( ).getViewer( ).setProperty(
				DeferredGraphicalViewer.RULER_SIZE, new Rectangle(bounds.x, bounds.y, getInitSize().width, bounds.height) );
		
		parent.setBounds( bounds );

		//parent.getParent( ).setSize( rect.getSize( ) );
	}

	public boolean isAuto() {
		return isAuto;
	}

	public void setAuto(boolean isAuto) {
		this.isAuto = isAuto;
	}
	
}