/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import org.eclipse.birt.chart.model.attribute.LineDecorator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * Draw the graphics within the HeadStyleChooser 
 * 
 */
public class HeadStyleCanvas extends Canvas implements PaintListener 
{
	int iLineDecorator = 0;

	public HeadStyleCanvas(Composite parent, int iStyle, int iLineDecorator) 
	{
		super(parent, iStyle);
		this.iLineDecorator = iLineDecorator;
		this.addPaintListener(this);
	}

	public int getHeadStyle() 
	{
		return this.iLineDecorator;
	}

	public void setHeadStyle(int iLineDecorator) 
	{
		this.iLineDecorator = iLineDecorator;
	}

	public void paintControl(PaintEvent pe) 
	{
		Color cForeground = null;
		Color cBackground = null;
		cForeground = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
		cBackground = Display.getCurrent().getSystemColor(
				SWT.COLOR_LIST_BACKGROUND);

		GC gc = pe.gc;
		gc.setBackground(cBackground);
		gc.setForeground(cForeground);
		gc.setLineWidth(1);
		gc.fillRectangle(0, 0, this.getSize().x, this.getSize().y);

		gc.drawLine(10, this.getSize().y / 2, this.getSize().x - 10, this
				.getSize().y / 2);
		if (iLineDecorator == LineDecorator.ARROW) {
			int[] points = { this.getSize().x - 15, this.getSize().y / 2 - 3,
					this.getSize().x - 15, this.getSize().y / 2 + 3,
					this.getSize().x - 10, this.getSize().y / 2 };	
			gc.setLineWidth(3);
			gc.drawPolygon(points);
		} else if (iLineDecorator == LineDecorator.CIRCLE) {
			gc.setLineWidth(4);
			gc.drawOval(this.getSize().x - 14, this.getSize().y / 2 - 3,
							6, 6);
		}
		
		//Render a boundary line to indicate focus
		if ( isEnabled( ) && isFocusControl( ) )
		{
			gc.setLineStyle( SWT.LINE_DOT );
			gc.setLineWidth( 1 );
			gc.setForeground( Display.getCurrent( )
					.getSystemColor( SWT.COLOR_BLACK ) );
			gc.drawRectangle( 1, 1, getSize( ).x - 3, this.getSize( ).y - 3 );
		}
	}
}
