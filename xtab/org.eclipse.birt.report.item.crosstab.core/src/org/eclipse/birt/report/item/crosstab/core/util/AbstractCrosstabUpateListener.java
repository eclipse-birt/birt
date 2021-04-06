/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public void setContext(ICrosstabUpdateContext context) {
		this.context = context;
	}

	public void onCreated(int type, Object model) {
		onCreated(type, model, null);
	}

	public void onValidate(int type, Object model) {
		onValidate(type, model, null);
	}

}
