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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.mediator.IMediator;
import org.eclipse.birt.report.designer.core.mediator.IMediatorColleague;
import org.eclipse.birt.report.designer.core.mediator.IMediatorRequest;
import org.eclipse.birt.report.designer.core.mediator.IMediatorState;
import org.eclipse.birt.report.designer.core.mediator.IMediatorStateConverter;

/**
 * MediatorImpl
 */
public class MediatorImpl implements IMediator {

	private static final List<IMediatorColleague> globalListeners = new LinkedList<>();

	public static void addGlobalColleague(IMediatorColleague colleague) {
		if (!globalListeners.contains(colleague)) {
			globalListeners.add(colleague);
		}
	}

	public static void removeGlobalColleague(IMediatorColleague colleague) {
		globalListeners.remove(colleague);
	}

	private final List<IMediatorColleague> listeners = new ArrayList<>();
	private List<MediatorStateImpl> stack = new ArrayList<>();
	private int stackPointer = 0;
	private MediatorStateImpl currentState = new MediatorStateImpl();
	private boolean isDispatching = false;
	private IMediatorStateConverter converter;

	@Override
	public void addColleague(IMediatorColleague colleague) {
		if (!listeners.contains(colleague)) {
			listeners.add(colleague);
		}
	}

	@Override
	public void removeColleague(IMediatorColleague colleague) {
		listeners.remove(colleague);
	}

	@Override
	public void notifyRequest(IMediatorRequest request) {
		if (isDispatching) {
			return;
		}

		isDispatching = true;

		if (request.isSticky()) {
			currentState.copyFrom(convertRequestToState(request));
		}

		int size = listeners.size();
		for (int i = 0; i < size; i++) {
			IMediatorColleague colleague = listeners.get(i);
			if (colleague.isInterested(request)) {
				colleague.performRequest(request);
			}
		}

		size = globalListeners.size();
		for (int i = 0; i < size; i++) {
			IMediatorColleague colleague = globalListeners.get(i);
			if (colleague.isInterested(request)) {
				colleague.performRequest(request);
			}
		}

		isDispatching = false;
	}

	@Override
	public IMediatorState getState() {
		return currentState;
	}

	@Override
	public void pushState() {
		try {
			MediatorStateImpl s;
			if (stack.size() > stackPointer) {
				s = stack.get(stackPointer);
				s.copyFrom(currentState);
			} else {
				stack.add((MediatorStateImpl) currentState.clone());
			}
			stackPointer++;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void popState() {
		stackPointer--;
		if (stackPointer != 0) {
			restoreState(stack.get(stackPointer));
		}
		if (stackPointer == 0) {
			stack.clear();
		}
	}

	@Override
	public void restoreState() {
		restoreState(stack.get(stackPointer - 1));
	}

	private void restoreState(MediatorStateImpl s) {
		currentState.copyFrom(s);
		IMediatorRequest request = convertStateToRequest(s);
		notifyRequest(request);
	}

	private IMediatorState convertRequestToState(IMediatorRequest request) {
		MediatorStateImpl state = new MediatorStateImpl();
		state.setType(request.getType());
		state.setSource(request.getSource());
		state.setData(request.getData());

		Map<?, ?> extras = request.getExtras();
		if (extras != null && !extras.isEmpty()) {
			state.setExtras(new HashMap<Object, Object>(extras));
		}

		return state;
	}

	private IMediatorRequest convertStateToRequest(IMediatorState state) {
		if (converter != null) {
			return converter.convertStateToRequest(state);
		}
		return new MediatorRequestImpl(state);
	}

	@Override
	public void dispose() {
		currentState = null;
		listeners.clear();
		stackPointer = 0;
		stack = null;
	}

	@Override
	public void setStateConverter(IMediatorStateConverter converter) {
		this.converter = converter;
	}

	@Override
	public IMediatorStateConverter getStateConverter() {
		return converter;
	}

}
