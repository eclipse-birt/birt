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

import java.util.Map;

import org.eclipse.birt.report.designer.core.mediator.IMediatorState;

/**
 * MediatorStateImpl
 */
public class MediatorStateImpl implements IMediatorState, Cloneable {

	private String type;
	private Object data;
	private Object source;
	private Map<?, ?> extras;

	MediatorStateImpl() {

	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		MediatorStateImpl state = new MediatorStateImpl();
		state.type = type;
		state.data = data;
		state.extras = extras;
		return state;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public Object getData() {
		return data;
	}

	@Override
	public Object getSource() {
		return source;
	}

	@Override
	public Map<?, ?> getExtras() {
		return extras;
	}

	void copyFrom(IMediatorState state) {
		this.type = state.getType();
		this.data = state.getData();
		this.source = state.getSource();
		this.extras = state.getExtras();
	}

	void setType(String type) {
		this.type = type;
	}

	void setData(Object data) {
		this.data = data;
	}

	void setSource(Object source) {
		this.source = source;
	}

	void setExtras(Map<?, ?> extras) {
		this.extras = extras;
	}
}
