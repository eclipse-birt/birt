/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.rulers;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Drag guide figure
 */

public class DragEditorGuideFigure extends EditorGuideFigure {

	/**
	 * @param isHorizontal
	 */
	public DragEditorGuideFigure(boolean isHorizontal) {
		super(isHorizontal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.rulers.EditorGuideFigure
	 * #paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		PointList list = new PointList();
		if (isHorizontal()) {
			Rectangle clientArea = getClientArea();
			clientArea.x = clientArea.getTopRight().x - 7;
			clientArea.y++;

			list.addPoint(clientArea.x + 3, clientArea.y);
			list.addPoint(clientArea.x + 6, clientArea.y);
			list.addPoint(clientArea.x + 6, clientArea.y + 6);
			list.addPoint(clientArea.x + 3, clientArea.y + 6);
			list.addPoint(clientArea.x, clientArea.y + 3);

			graphics.fillPolygon(list);
			graphics.setForegroundColor(ColorConstants.buttonDarker);
			graphics.drawPolygon(list);
			graphics.setForegroundColor(ColorConstants.buttonLightest);
			graphics.drawLine(clientArea.x - 1, clientArea.y, clientArea.x - 1, clientArea.y + 6);
			graphics.drawLine(clientArea.x, clientArea.y - 1, clientArea.x + 3, clientArea.y - 1);
			graphics.drawLine(clientArea.x, clientArea.y + 7, clientArea.x + 3, clientArea.y + 7);
		} else {
			Rectangle clientArea = getClientArea();
			clientArea.y = clientArea.getBottomLeft().y - 7;
			clientArea.x++;

			list.addPoint(clientArea.x, clientArea.y + 3);
			list.addPoint(clientArea.x, clientArea.y + 6);
			list.addPoint(clientArea.x + 6, clientArea.y + 6);
			list.addPoint(clientArea.x + 6, clientArea.y + 3);
			list.addPoint(clientArea.x + 3, clientArea.y);

			graphics.fillPolygon(list);
			graphics.setForegroundColor(ColorConstants.buttonDarker);
			graphics.drawPolygon(list);
			graphics.setForegroundColor(ColorConstants.buttonLightest);
			graphics.drawLine(clientArea.x, clientArea.y - 1, clientArea.x + 6, clientArea.y - 1);
			graphics.drawLine(clientArea.x - 1, clientArea.y, clientArea.x - 1, clientArea.y + 3);
			graphics.drawLine(clientArea.x + 7, clientArea.y, clientArea.x + 7, clientArea.y + 3);
		}
	}
}
