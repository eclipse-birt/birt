/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
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

package org.eclipse.birt.report.designer.core.mediator.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.core.mediator.IMediatorRequest;
import org.eclipse.birt.report.designer.core.mediator.IMediatorState;

/**
 * 
 */

public class MediatorRequestImpl implements IMediatorRequest {

	private String type;
	private Object data;
	private Object source;
	private Map<?, ?> extras;

	MediatorRequestImpl(IMediatorState state) {
		this.type = state.getType();
		this.data = state.getData();
		this.source = state.getSource();
		if (state.getExtras() != null) {
			this.extras = new HashMap<Object, Object>(state.getExtras());
		}
	}

	public String getType() {
		return type;
	}

	public Object getData() {
		return data;
	}

	public Object getSource() {
		return source;
	}

	public boolean isSticky() {
		return false;
	}

	public Map<?, ?> getExtras() {
		return extras;
	}

}
