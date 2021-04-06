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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.section;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormText;

public class FormTextSection extends Section {

	/**
	 * The text field, or <code>null</code> if none.
	 */
	protected FormText textField;

	protected String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public FormTextSection(String labelText, Composite parent, boolean formStyle) {
		super(labelText, parent, formStyle);
	}

	private boolean fillText = false;

	private int width = -1;

	public void createSection() {
		getLabelControl(parent);
		getTextControl(parent);
		getGridPlaceholder(parent);
	}

	public void layout() {
		GridData gd = (GridData) textField.getLayoutData();
		if (getLayoutNum() > 0)
			gd.horizontalSpan = getLayoutNum() - 1 - placeholder;
		else
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - 1 - placeholder;
		if (width > -1) {
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		} else
			gd.grabExcessHorizontalSpace = fillText;

		if (height > -1) {
			gd.heightHint = height;
			gd.grabExcessVerticalSpace = false;
			if (displayLabel != null) {
				gd = (GridData) displayLabel.getLayoutData();
				gd.verticalAlignment = GridData.VERTICAL_ALIGN_FILL;
			}
		} else {
			gd.grabExcessVerticalSpace = fillText;
			if (fillText) {
				gd.verticalAlignment = GridData.FILL;
				if (displayLabel != null) {
					gd = (GridData) displayLabel.getLayoutData();
					gd.verticalAlignment = GridData.VERTICAL_ALIGN_FILL;
				}
			}

		}
		if (fillText) {
			gd = (GridData) textField.getLayoutData();
			gd.grabExcessVerticalSpace = true;
			gd.verticalAlignment = GridData.FILL;
		}

	}

	public FormText getTextControl() {
		return textField;
	}

	protected FormText getTextControl(Composite parent) {
		if (textField == null) {
			textField = FormWidgetFactory.getInstance().createFormText(parent, false);
			textField.setLayoutData(new GridData());
			textField.addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent event) {
					textField = null;
				}
			});
			if (text != null) {
				textField.setText(text, true, false);
			}
			Iterator iter = map.keySet().iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				Object obj = map.get(key);
				if (obj instanceof Image)
					textField.setImage(key, (Image) obj);
				else if (obj instanceof Color)
					textField.setColor(key, (Color) obj);
			}
		} else {
			checkParent(textField, parent);
		}
		return textField;
	}

	public void setFocus() {
		if (textField != null) {
			textField.setFocus();
		}
	}

	public void setStringValue(String value) {
		this.text = value;
		if (textField != null) {
			if (value == null) {
				value = "";//$NON-NLS-1$
			}
			textField.setText(value, true, false);
		}
	}

	public void load() {

	}

	public void setInput(Object input) {

	}

	private int height = -1;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public boolean isFillText() {
		return fillText;
	}

	public void setFillText(boolean fillText) {
		this.fillText = fillText;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setHidden(boolean isHidden) {
		if (displayLabel != null)
			WidgetUtil.setExcludeGridData(displayLabel, isHidden);
		if (textField != null)
			WidgetUtil.setExcludeGridData(textField, isHidden);
		if (placeholderLabel != null)
			WidgetUtil.setExcludeGridData(placeholderLabel, isHidden);
	}

	public void setVisible(boolean isVisible) {
		if (displayLabel != null)
			displayLabel.setVisible(isVisible);
		if (textField != null)
			textField.setVisible(isVisible);
		if (placeholderLabel != null)
			placeholderLabel.setVisible(isVisible);
	}

	private Map map = new HashMap();

	public void setImage(String key, Image image) {
		map.put(key, image);
		if (textField != null) {
			textField.setImage(key, image);
		}
	}

	public void setColor(String key, Color color) {
		map.put(key, color);
		if (textField != null) {
			textField.setColor(key, color);
		}
	}

}
