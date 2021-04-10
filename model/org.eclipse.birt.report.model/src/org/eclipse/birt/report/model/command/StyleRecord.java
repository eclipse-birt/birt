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

package org.eclipse.birt.report.model.command;

import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.StyleEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.util.CommandLabelFactory;

/**
 * Records a change to the style of an element.
 * 
 */

public class StyleRecord extends SimpleRecord {

	/**
	 * The element to change.
	 */

	protected StyledElement element = null;

	/**
	 * The old style. Can be null.
	 */

	protected Object oldStyle = null;

	/**
	 * The new style. Can be null.
	 */

	// protected StyleElement newStyle = null;
	protected ElementRefValue newStyle = null;

	/**
	 * Constructor.
	 * 
	 * @param obj      the element to modify.
	 * @param newStyle the style to set.
	 */

	public StyleRecord(StyledElement obj, ElementRefValue newStyle) {
		assert obj != null;

		element = obj;
		this.newStyle = newStyle;

		if (element.getStyle() != null)
			oldStyle = element.getStyle();
		else
			oldStyle = element.getStyleName();

		label = CommandLabelFactory.getCommandLabel(MessageConstants.SET_STYLE_MESSAGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.SimpleRecord#perform(boolean)
	 */

	protected void perform(boolean undo) {
		if (undo) {
			if (oldStyle instanceof String)
				element.setStyleName((String) oldStyle);
			else
				element.setStyle((StyleElement) oldStyle);
		} else {

			if (newStyle == null) {
				element.setStyle(null);
			} else {
				StyleElement tmpStyle = (StyleElement) newStyle.getElement();
				if (tmpStyle != null) {
					element.setStyle(tmpStyle);
				} else {
					if (newStyle.getName() != null) {
						element.setStyleName(newStyle.getName());
					} else {
						element.setStyle(null);
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.design.core.AbstractElementRecord#getTarget ()
	 */

	public DesignElement getTarget() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.AbstractElementRecord#getEvent
	 * ()
	 */

	public NotificationEvent getEvent() {
		return new StyleEvent(element);
	}

}