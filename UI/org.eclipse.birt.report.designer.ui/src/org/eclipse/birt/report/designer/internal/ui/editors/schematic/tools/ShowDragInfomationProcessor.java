/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.DeferredGraphicalViewer;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;

/**
 *
 */

public class ShowDragInfomationProcessor {
	private static final Insets INSETS = new Insets(2, 4, 2, 4);
	private static final int DISTANCE = 30;
	private EditPart editpart;

	public ShowDragInfomationProcessor(EditPart editpart) {
		super();
		this.editpart = editpart;
	}

	public Label getLabelFigure() {
		return labelFigure;
	}

	public void setLabelFigure(Label labelFigure) {
		this.labelFigure = labelFigure;
	}

	private Label labelFigure;

	private int maxWidth;

	public Label getInfomationLabel(String infomation, Point statrtPoint) {
		if (labelFigure == null) {
			labelFigure = new Label();
			labelFigure.setBorder(new MarginBorder(new Insets(0, 3, 0, 0)) {
				@Override
				public void paint(IFigure figure, Graphics graphics, Insets insets) {
					tempRect.setBounds(getPaintRectangle(figure, insets));
					if (getWidth() % 2 != 0) {
						tempRect.width--;
						tempRect.height--;
					}
					tempRect.shrink(getWidth() / 2, getWidth() / 2);
					graphics.setLineWidth(getWidth());

					graphics.drawRectangle(tempRect);
				}

				private int getWidth() {
					return 1;
				}

			});
			labelFigure.setLabelAlignment(PositionConstants.LEFT);
			labelFigure.setOpaque(true);

			labelFigure.setBackgroundColor(ReportColorConstants.TableGuideFillColor);

			addFeedback(labelFigure);
			Dimension size = FigureUtilities.getTextExtents(infomation, labelFigure.getFont());

			Dimension newSize = size.getCopy().expand(INSETS.getWidth(), INSETS.getHeight());
			labelFigure.setSize(newSize);

			maxWidth = size.width;

			setLabelLocation(statrtPoint);

			// Insets insets = getInfomationLabel( ).getInsets( );
			adjustLocation(statrtPoint);

		}
		return labelFigure;
	}

	public void removeLabelFigue() {
		if (labelFigure != null) {
			LayerManager lm = (LayerManager) editpart.getViewer().getEditPartRegistry().get(LayerManager.ID);
			if (lm == null) {
				return;
			}
			lm.getLayer(LayerConstants.FEEDBACK_LAYER).remove(labelFigure);
			labelFigure = null;
		}
	}

	public void updateInfomation(String label, Point p) {
		if (labelFigure == null) {
			return;
		}
		labelFigure.setText(label);
		Dimension size = FigureUtilities.getTextExtents(label, labelFigure.getFont());
		// Insets insets = getInfomationLabel( ).getInsets( );
		Insets insets = INSETS;
		Dimension newSize = size.getCopy().expand(insets.getWidth(), insets.getHeight());
		if (size.width > maxWidth) {
			maxWidth = size.width;
		} else {
			newSize = new Dimension(maxWidth, size.height).expand(insets.getWidth(), insets.getHeight());
		}
		labelFigure.setSize(newSize);
		setLabelLocation(p);
		adjustLocation(p);
	}

	private void setLabelLocation(Point p) {
		if (labelFigure == null) {
			return;
		}

		labelFigure.translateToRelative(p);
		labelFigure.setLocation(new Point(p.x, p.y - DISTANCE));
	}

	private void adjustLocation(Point statrtPoint) {
		if (labelFigure == null) {
			return;
		}
		Rectangle rect = labelFigure.getBounds();
		Dimension dim = getDistance(statrtPoint);
		Point p = labelFigure.getLocation().getCopy();
		if (dim.width < rect.width) {
			p.x = p.x - (rect.width - dim.width);
		}
		if (dim.height < rect.height + DISTANCE) {
			p.y = p.y + (rect.height + DISTANCE - dim.height);
		}

		labelFigure.setLocation(p);
	}

	private Dimension getDistance(Point p) {
		FigureCanvas canvas = ((DeferredGraphicalViewer) editpart.getViewer()).getFigureCanvas();
		org.eclipse.swt.graphics.Rectangle rect = canvas.getBounds();

		Dimension retValue = new Dimension(rect.width - p.x, p.y);
		if (canvas.getVerticalBar().isVisible()) {
			retValue.width = retValue.width - canvas.getVerticalBar().getSize().x;
		}
		return retValue;
	}

	protected void addFeedback(IFigure figure) {
		LayerManager lm = (LayerManager) editpart.getViewer().getEditPartRegistry().get(LayerManager.ID);
		if (lm == null) {
			return;
		}
		lm.getLayer(LayerConstants.FEEDBACK_LAYER).add(figure);
	}
}
