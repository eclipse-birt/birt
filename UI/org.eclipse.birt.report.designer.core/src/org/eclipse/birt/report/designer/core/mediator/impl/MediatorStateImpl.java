/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public String getType() {
		return type;
	}

	public Object getData() {
		return data;
	}

	public Object getSource() {
		return source;
	}

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
