/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.api.extension;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * Class that records some status after the report item checks the
 * compatibilities.
 */
public class CompatibilityStatus {

	/**
	 * Status that indicates the report item has no compatibilities and its version
	 * is supported also.
	 */
	public static final int OK_TYPE = 0;

	/**
	 * Status that indicates the version of the report item is not supported.
	 */
	public static final int NOT_SUPPORTED_TYPE = 1;

	/**
	 * Status that indicates the report item has some compatibilities to do.
	 */
	public static final int CONVERT_COMPATIBILITY_TYPE = 2;

	/**
	 * List of the errors after checking compatibilities.
	 */
	protected List<SemanticException> errors = null;

	/**
	 * Type of the status.
	 */
	protected int statusType = OK_TYPE;

	/**
	 * Default constructor.
	 */
	public CompatibilityStatus() {

	}

	/**
	 * Constructs this status with the error list and the type.
	 * 
	 * @param errors
	 * @param type
	 */
	public CompatibilityStatus(List<? extends SemanticException> errors, int type) {
		this.errors = new ArrayList<SemanticException>();
		this.errors.addAll(errors);
		setStatusType(type);
	}

	/**
	 * Gets the error list of this status after checking the compatibilities. Each
	 * item in the list is instance of <code>SemanticException</code>.
	 * 
	 * @return the errors
	 */
	public List<SemanticException> getErrors() {
		return errors;
	}

	/**
	 * Sets the error list of this status.
	 * 
	 * @param errors the errors to set
	 */
	public void setErrors(List<SemanticException> errors) {
		this.errors = errors;
	}

	/**
	 * Gets the type of this status. The possible values are:
	 * 
	 * <ul>
	 * <li>OK_TYPE
	 * <li>NOT_SUPPORTED_TYPE
	 * <li>CONVERT_COMPATIBILITY_TYPE
	 * </ul>
	 * 
	 * By default, type is <code>OK_TYPE</code>.
	 * 
	 * @return the statusType
	 */
	public int getStatusType() {
		return statusType;
	}

	/**
	 * Sets the type of this status. The possible values are:
	 * 
	 * <ul>
	 * <li>OK_TYPE
	 * <li>NOT_SUPPORTED_TYPE
	 * <li>CONVERT_COMPATIBILITY_TYPE
	 * </ul>
	 * 
	 * @param type the status type to set
	 */
	public void setStatusType(int type) {
		switch (type) {
		case OK_TYPE:
		case NOT_SUPPORTED_TYPE:
		case CONVERT_COMPATIBILITY_TYPE:
			this.statusType = type;
			break;
		default:
			break;
		}
	}

}
