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

package org.eclipse.birt.report.model.metadata;

/**
 * OverrideProperty Information see schema #overrideproperty
 *
 */

public class OverridePropertyInfo {

	private String allowedUnits = null;
	private String allowedChoices = null;

	private boolean enableExtraSearch = false;

	/**
	 * Get allowedUnits. for example : in,mm,pt
	 *
	 * @return the allowed units
	 */

	public String getAllowedUnits() {
		return allowedUnits;
	}

	/**
	 * Sets allowedUnits.
	 *
	 * @param allowedUnits
	 */

	public void setAllowedUnits(String allowedUnits) {
		this.allowedUnits = allowedUnits;
	}

	/**
	 * Get allowedUnits. for example : in,mm,pt
	 *
	 * @return the allowed choices by the overridden definition
	 */

	public String getAllowedChoices() {
		return allowedChoices;
	}

	/**
	 * Sets allowedChoices.
	 *
	 * @param allowedChoices
	 */

	void setAllowedChoices(String allowedChoices) {
		this.allowedChoices = allowedChoices;
	}

	/**
	 * @return the enableExtraSearch
	 */

	public boolean useOwnSearch() {
		return enableExtraSearch;
	}

	/**
	 * @param enableExtraSearch the enableExtraSearch to set
	 */

	void setUseOwnSearch(boolean enableExtraSearch) {
		this.enableExtraSearch = enableExtraSearch;
	}

}
