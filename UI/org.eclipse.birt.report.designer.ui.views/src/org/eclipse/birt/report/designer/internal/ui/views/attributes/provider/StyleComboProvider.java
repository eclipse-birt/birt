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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.IComboProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * IComboProvider realization for line style and line width
 */
public abstract class StyleComboProvider extends BorderDescriptorProvider implements IComboProvider {

	public StyleComboProvider() {
		super();
	}

	private static final String LINE_STYLE_NONE = Messages.getString("StyleComboProvider.LineStyle.None"); //$NON-NLS-1$
	private Object[] items;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.
	 * IComboProvider#getItems()
	 */
	public Object[] getItems() {
		return items;
	}

	public Object[] getDisplayItems() {
		return items;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.
	 * IComboProvider#setItems(java.lang.Object[])
	 */
	public void setItems(Object[] items) {
		this.items = items;
	}

	protected Object indexText = ""; //$NON-NLS-1$

	public void setIndex(Object index) {
		indexText = index;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.
	 * IComboProvider#getImage(java.lang.Object, int, int,
	 * org.eclipse.swt.widgets.Control, org.eclipse.swt.widgets.Control)
	 */
	public Image getImage(Object item, int width, int height, Control control, Control parent) {
		assert control != null;
		assert parent != null;

		Color foreground = parent.getForeground();
		Color background = parent.getBackground();
		Color rectColor = control.getBackground();
		Display display = parent.getDisplay();

		Color shadowColor = display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
		Color hShadowColor = display.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
		PaletteData palette = new PaletteData(new RGB[] { foreground.getRGB(), background.getRGB(),
				shadowColor.getRGB(), hShadowColor.getRGB(), rectColor.getRGB() });

		ImageData imageData = new ImageData(width - 2, height, 8, palette);
		imageData.transparentPixel = 1;
		Image image = new Image(display, imageData);
		GC gc = new GC(image);
		gc.setBackground(background);
		gc.fillRectangle(0, 0, width, height);

		gc.setForeground(rectColor);
		Rectangle r = image.getBounds();
		gc.setLineWidth(4);
		gc.drawRectangle(r.x, r.y, r.width - 1, r.height - 1);

		gc.setLineWidth(1);
		gc.setForeground(foreground);
		String key = null;
		if (item instanceof String)
			key = (String) item;
		else if (item instanceof IChoice)
			key = ((IChoice) item).getName();
		if (DesignChoiceConstants.LINE_STYLE_NONE.equals(key)) {
			Point textSize = gc.textExtent(item.toString());
			gc.drawString(LINE_STYLE_NONE, 5, (height - textSize.y) / 2);
		} else if (DesignChoiceConstants.LINE_STYLE_DOUBLE.equals(key)) {
			gc.drawLine(4, height / 2 - 1, width - 4, height / 2 - 1);
			gc.drawLine(4, height / 2 + 1, width - 4, height / 2 + 1);
		} else {
			if (DesignChoiceConstants.LINE_STYLE_DOTTED.equals(key))
				gc.setLineStyle(SWT.LINE_DOT);
			else if (DesignChoiceConstants.LINE_STYLE_DASHED.equals(key))
				gc.setLineStyle(SWT.LINE_DASH);
			else if (DesignChoiceConstants.LINE_STYLE_SOLID.equals(key))
				gc.setLineStyle(SWT.LINE_SOLID);
			else if (DesignChoiceConstants.LINE_WIDTH_THIN.equals(key))
				gc.setLineWidth(1);
			else if (DesignChoiceConstants.LINE_WIDTH_MEDIUM.equals(key))
				gc.setLineWidth(2);
			else if (DesignChoiceConstants.LINE_WIDTH_THICK.equals(key))
				gc.setLineWidth(3);
			else {
				try {
					int customWidth = (int) DimensionValue.parse(key).getMeasure();
					if (DimensionValue.parse(key).getUnits().equals(DesignChoiceConstants.UNITS_PX))
						gc.setLineWidth(customWidth);
				} catch (Exception e) {
					ExceptionUtil.handle(e);
				}
			}
			gc.drawLine(4, height / 2, width - 4, height / 2);
		}
		gc.dispose();

		return image;
	}

}