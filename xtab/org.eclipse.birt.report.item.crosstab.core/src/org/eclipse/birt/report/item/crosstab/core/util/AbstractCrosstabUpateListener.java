/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

package org.eclipse.birt.report.item.crosstab.core.util;

/**
 * AbstractCrosstabUpateListener
 */
public abstract class AbstractCrosstabUpateListener implements ICrosstabUpdateListener {

	protected ICrosstabUpdateContext context;

	@Override
	public void setContext(ICrosstabUpdateContext context) {
		this.context = context;
	}

	@Override
	public void onCreated(int type, Object model) {
		onCreated(type, model, null);
	}

	@Override
	public void onValidate(int type, Object model) {
		onValidate(type, model, null);
	}

}
