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

import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.LabeledBorder;
import org.eclipse.draw2d.SchemeBorder;
import org.eclipse.draw2d.SimpleLoweredBorder;
import org.eclipse.draw2d.TitleBarBorder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

/**
 * The Border used by the TableNodeFigure for representing a Table object
 * 
 */
public class TableBorderFigure extends CompoundBorder implements LabeledBorder {

	protected TitleBarBorder titleBar;

	private static final SchemeBorder.Scheme raisedBorderScheme = new SchemeBorder.Scheme(
			new Color[] { ColorConstants.button, ColorConstants.buttonLightest, ColorConstants.button },
			new Color[] { ColorConstants.buttonDarkest, ColorConstants.buttonDarker, ColorConstants.button });

	private static final FontData fontData = Display.getCurrent().getSystemFont().getFontData()[0];

	private final Font selectedFont = FontManager.getFont(fontData.getName(), fontData.getHeight(), SWT.BOLD);

	public TableBorderFigure() {
		outer = new SchemeBorder(raisedBorderScheme);
		titleBar = new TitleBarBorder();
		inner = new CompoundBorder(titleBar, new SimpleLoweredBorder(5));
		titleBar.setTextColor(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_FOREGROUND));
		titleBar.setTextAlignment(1);
		titleBar.setPadding(3);
	}

	public String getLabel() {
		return titleBar.getLabel();
	}

	public void setFont(Font f) {
		titleBar.setFont(f);
	}

	public void setLabel(String s) {
		titleBar.setLabel(s);
	}

	public void setSelectedColors(boolean isFact) {
		if (!isFact)
			titleBar.setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND));
		else
			titleBar.setBackgroundColor(Fact_BACKGROUND);
		titleBar.setTextColor(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		titleBar.setFont(selectedFont);
	}

	private Color Fact_INACTIVE_BACKGROUND = ColorManager.getColor(75, 75, 75);
	private Color Fact_BACKGROUND = ColorManager.getColor(25, 25, 25);

	public void setDeselectedColors(boolean isFact) {
		if (!isFact)
			titleBar.setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND));
		else
			titleBar.setBackgroundColor(Fact_INACTIVE_BACKGROUND);
		titleBar.setTextColor(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		titleBar.setFont(selectedFont);
	}
}