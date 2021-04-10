/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.joins.figures;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class ColumnConnectionAnchor extends AbstractConnectionAnchor {

	private IFigure chopFigure;

	public ColumnConnectionAnchor(IFigure owner, IFigure chop) {
		super(owner);
		chopFigure = chop;
	}

	public IFigure getChopFigure() {
		return chopFigure;
	}

	public Point getLocation(Point reference) {
		Rectangle chopR = chopFigure.getBounds().getCropped(chopFigure.getInsets());
		Point pos = chopR.getLocation();
		chopFigure.translateToAbsolute(pos);
//		Rectangle startFigureR = this.getOwner( ).getBounds( );
		Point refPoint = getReferencePoint();
		getOwner().translateToAbsolute(getReferencePoint());
		int x = pos.x;
		if (refPoint.x < reference.x) {
			x += chopR.width;
		}
		int y = getReferencePoint().y;
		if (getOwner() instanceof TablePaneFigure) {
			y = chopFigure.getBounds().y + 10;
		}
		Point p = new Point(x, y);
		return p;

	}
}