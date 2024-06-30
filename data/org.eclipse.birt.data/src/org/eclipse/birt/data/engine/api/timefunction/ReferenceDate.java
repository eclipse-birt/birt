/*
 *************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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
 *
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api.timefunction;

import java.util.Date;

public class ReferenceDate implements IReferenceDate {
	private Date referenceDate;

	public ReferenceDate(Date referenceDate) {
		this.referenceDate = referenceDate;
	}

	@Override
	public Date getDate() {
		return this.referenceDate;
	}

}
