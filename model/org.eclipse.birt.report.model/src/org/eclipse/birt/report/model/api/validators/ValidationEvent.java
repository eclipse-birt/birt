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

package org.eclipse.birt.report.model.api.validators;

import java.util.List;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.core.DesignElement;

/**
 * Notification event that says that the validation is performed and whether
 * error exists. This event provides the information: the validated element, the
 * validation ID and the error list.
 */

public class ValidationEvent extends NotificationEvent {

	/**
	 * The error list returned from validator, each of which is the instance of
	 * <code>ErrorDetail</code>.
	 */

	private List errors;

	/**
	 * The ID of one validation, which is used to identify one validation.
	 */

	private String validationID;

	/**
	 * Constructs the validation event.
	 * 
	 * @param obj          the element which is validated.
	 * @param validationID the validation ID
	 * @param errors       the error list which is the validation result. Each one
	 *                     is the instance of <code>ErrorDetail</code>.
	 */

	public ValidationEvent(DesignElement obj, String validationID, List errors) {
		super(obj);

		deliveryPath = DIRECT;
		this.validationID = validationID;
		this.errors = errors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.activity.NotificationEvent#getEventType()
	 */

	public int getEventType() {
		return VALIDATION_EVENT;
	}

	/**
	 * Returns the error list which is the validation result. Each of the list is
	 * the instance of <code>ErrorDetail</code>.
	 * 
	 * @return the error list returned after validation
	 */

	public List getErrors() {
		return errors;
	}

	/**
	 * Returns the ID of the validation this event represents.
	 * 
	 * @return the validation ID
	 */

	public String getValidationID() {
		return validationID;
	}
}