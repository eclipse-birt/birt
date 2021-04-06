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
import org.eclipse.birt.report.model.api.command.CustomMsgEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Translation;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.util.CommandLabelFactory;

/**
 * ActivityRecord to add, remove or change a custom-defined message. As with any
 * command, the caller must have verified that the operation is legal. This one
 * command handles both the add and remove operations, since they are inverse
 * operations.
 * 
 */

public class CustomMsgRecord extends SimpleRecord {

	/**
	 * The ReportDesign that is to be changed.
	 */

	private ReportDesign design = null;

	/**
	 * the translation to be added or removed.
	 */

	private Translation translation = null;

	/**
	 * Action option of the record.
	 */

	private int action = -1;

	/**
	 * Action option meaning to add a new translation.
	 */

	public final static int ADD = 0;

	/**
	 * Action option meaning to drop a translation.
	 */

	public final static int DROP = 1;

	/**
	 * Action option meaning to change the locale of a translation.
	 */

	public final static int CHANGE_LOCALE = 2;

	/**
	 * Action option meaning to change the text of a translation.
	 */

	public final static int CHANGE_TEXT = 3;

	private String newValue;

	private String oldValue;

	/**
	 * Constructs a record to add or drop a translation.
	 * 
	 * @param design      the report design
	 * @param translation the user-defined message
	 * @param action      one of the action options, can be <code>ADD</code> or
	 *                    <code>DROP</code>
	 * 
	 */

	public CustomMsgRecord(ReportDesign design, Translation translation, int action) {
		assert design != null;
		assert translation != null;
		assert action == ADD || action == DROP;

		this.action = action;
		this.design = design;
		this.translation = translation;

		if (action == ADD)
			label = CommandLabelFactory.getCommandLabel(MessageConstants.ADD_TRANSLATION_MESSAGE);
		else if (action == DROP)
			label = CommandLabelFactory.getCommandLabel(MessageConstants.DROP_TRANSLATION_MESSAGE);
	}

	/**
	 * Constructs a record to set locale or text for a translation.
	 * 
	 * @param design      the report design
	 * @param translation the translation item to be changed.
	 * @param value       new value for either translation text or locale.
	 * @param action      one of the action options, can be <code>CHANGE_TEXT</code>
	 *                    or <code>CHANGE_LOCALE</code>
	 */

	public CustomMsgRecord(ReportDesign design, Translation translation, String value, int action) {
		assert design != null;
		assert translation != null;
		assert action == CHANGE_TEXT || action == CHANGE_LOCALE;

		this.design = design;
		this.action = action;
		this.translation = translation;
		this.newValue = value;

		if (action == CHANGE_TEXT)
			oldValue = translation.getText();
		else if (action == CHANGE_LOCALE)
			oldValue = translation.getLocale();

		label = CommandLabelFactory.getCommandLabel(MessageConstants.CHANGE_TRANSLATION_MESSAGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.design.activity.SimpleRecord#perform(boolean )
	 */

	protected void perform(boolean undo) {
		switch (action) {
		case ADD:
			if (undo)
				design.dropTranslation(translation);
			else
				design.addTranslation(translation);
			break;
		case DROP:
			if (undo)
				design.addTranslation(translation);
			else
				design.dropTranslation(translation);
			break;
		case CHANGE_LOCALE:
			if (undo)
				translation.setLocale(oldValue);
			else
				translation.setLocale(newValue);
			break;
		case CHANGE_TEXT:
			if (undo)
				translation.setText(oldValue);
			else
				translation.setText(newValue);
			break;
		default:
			assert false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.design.activity.AbstractElementRecord#getTarget
	 * ()
	 */

	public DesignElement getTarget() {
		return design;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.design.activity.AbstractElementRecord#getEvent
	 * ()
	 */

	public NotificationEvent getEvent() {
		assert state == DONE_STATE || state == UNDONE_STATE || state == REDONE_STATE;

		int event = CustomMsgEvent.ADD;

		switch (action) {
		case ADD:
			event = CustomMsgEvent.ADD;
			break;
		case DROP:
			event = CustomMsgEvent.DROP;
			break;
		case CHANGE_TEXT:
		case CHANGE_LOCALE:
			event = CustomMsgEvent.CHANGE;
			break;
		default:
			assert false;
		}

		return new CustomMsgEvent(design, translation, event);
	}

}