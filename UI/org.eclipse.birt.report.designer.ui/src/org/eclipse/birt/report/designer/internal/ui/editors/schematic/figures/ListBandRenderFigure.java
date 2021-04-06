/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportFlowLayout;
import org.eclipse.birt.report.designer.internal.ui.layout.TableLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gef.LayerConstants;
import org.eclipse.swt.SWT;

/**
 * Presents list band figure for list band render
 * 
 */
public class ListBandRenderFigure extends Figure {

	private static final Insets margin = new Insets(5, 5, 4, 4);

	public static final int HEIGHT = 23;

	public ListBandRenderFigure() {
		setLayoutManager(new ReportFlowLayout() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.birt.report.designer.internal.ui.layout.ReportFlowLayout#
			 * getChildSize(org.eclipse.draw2d.IFigure, int, int)
			 */
			protected Dimension getChildSize(IFigure child, int wHint, int hHint) {
				if (child instanceof TableFigure) {
					IFigure grandFigure = getParent().getParent();

					if (grandFigure instanceof ListFigure && ((ListFigure) grandFigure).isDirty()) {
						int oldWidth = getBounds().width;
						int oldHeight = getBounds().height;
						int width = wHint + getInsets().getWidth();
						int height = hHint + getInsets().getHeight();

						if (width != oldWidth || height != oldHeight) {
							// if ( child instanceof TableFigure )
							{
								IFigure tablePane = ((LayeredPane) ((LayeredPane) ((TableFigure) child).getContents())
										.getLayer(LayerConstants.PRINTABLE_LAYERS))
												.getLayer(LayerConstants.PRIMARY_LAYER);
								LayoutManager layoutManager = tablePane.getLayoutManager();

								if (layoutManager instanceof TableLayout
										&& !(getParent().getParent().getParent() instanceof ListBandRenderFigure)) {
									((ListFigure) grandFigure).markDirty(false);
									((TableLayout) layoutManager).markDirty();
									getBounds().width = width;
									getBounds().height = height;
									tablePane.validate();
									getBounds().width = oldWidth;
									getBounds().height = oldHeight;
								}
							}
						}
					}
				}
				return super.getChildSize(child, wHint, hHint);
			}
		});
		setBorder(new MarginBorder(margin));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		graphics.setForegroundColor(ReportColorConstants.ShadowLineColor);
		graphics.setLineStyle(SWT.LINE_SOLID);
		graphics.drawRectangle(getBounds().getCopy().shrink(2, 2));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int wHint, int hHint) {
		invalidateTree();

		Dimension dim = super.getPreferredSize(wHint, hHint);

		if (dim.height < HEIGHT) {
			dim.height = HEIGHT;
		}
		if (wHint > 0 && dim.width < wHint) {
			dim.width = wHint;
		}
		return dim;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#getMinimumSize(int, int)
	 */
	public Dimension getMinimumSize(int wHint, int hHint) {
		Dimension retValue = super.getMinimumSize(wHint, hHint);
		if (retValue.height < HEIGHT) {
			retValue.height = HEIGHT;
		}
		return retValue;
	}
}