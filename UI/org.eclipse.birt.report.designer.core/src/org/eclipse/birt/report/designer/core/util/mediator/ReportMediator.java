/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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

package org.eclipse.birt.report.designer.core.util.mediator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.mediator.IMediator;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;

/**
 * Mediator calss to control the interactive between different views. This class
 * is used for selection sychronization and other tasks.
 * 
 * @deprecated Not used anymore, see {@link IMediator} instead.
 */
public class ReportMediator {

	private boolean isDispatching = false;
	private List listeners = new ArrayList();
	private List stack = new ArrayList();
	private int stackPointer = 0;
	private ReportMediatorState currentState = new ReportMediatorState();

	// suport the globol colleague
	private static List globalListener = new ArrayList();

	/**
	 * Add global colleague
	 * 
	 * @param colleague
	 */
	public static void addGlobalColleague(IColleague colleague) {
		if (!globalListener.contains(colleague)) {
			if (DesignerConstants.TRACING_MEDIATOR_GLOBAL_COLLEAGUE_ADD) {
				System.out.println("ReportMediator >> Add a new global colleage: " //$NON-NLS-1$
						+ colleague);
			}
			globalListener.add(colleague);
		}
	}

	/**
	 * Add a colleague to mediator.
	 * 
	 * @param colleague
	 */
	public void addColleague(IColleague colleague) {
		if (!listeners.contains(colleague)) {
			if (DesignerConstants.TRACING_MEDIATOR_COLLEAGUE_ADD) {
				System.out.println("ReportMediator >> Add a new colleage: " + colleague); //$NON-NLS-1$
			}
			listeners.add(colleague);
		}
	}

	/**
	 * Remove colleagure from mediator.
	 * 
	 * @param colleague
	 */
	public void removeColleague(IColleague colleague) {
		if (DesignerConstants.TRACING_MEDIATOR_COLLEAGUE_REMOVE) {
			System.out.println("ReportMediator >> Remove a colleage: " + colleague); //$NON-NLS-1$
		}
		listeners.remove(colleague);
	}

	/**
	 * Remove colleagure from mediator.
	 * 
	 * @param colleague
	 */
	public static void removeGlobalColleague(IColleague colleague) {
		if (DesignerConstants.TRACING_MEDIATOR_GLOBAL_COLLEAGUE_REMOVE) {
			System.out.println("ReportMediator >> Remove a global colleage: " //$NON-NLS-1$
					+ colleague);
		}
		globalListener.remove(colleague);
	}

	/**
	 * Send a request to mediator. Mediator handle and dispatch this request to
	 * colleaues.
	 * 
	 * @param request
	 */
	public void notifyRequest(ReportRequest request) {
		if (isDispatching)
			return;
		if (DesignerConstants.TRACING_MEDIATOR_NOTIFY) {
			System.out.println("ReportMediator >> Notify a " //$NON-NLS-1$
					+ request.getType() + "request from " //$NON-NLS-1$
					+ request.getSource());
		}
		isDispatching = true;
		if (isInterestRequest(request)) {
			currentState.copyFrom(convertRequestToState(request));
		}
		int size = listeners.size();
		for (int i = 0; i < size; i++) {
			IColleague colleague = (IColleague) listeners.get(i);
			colleague.performRequest(request);
		}

		size = globalListener.size();
		for (int i = 0; i < size; i++) {
			IColleague colleague = (IColleague) globalListener.get(i);
			colleague.performRequest(request);
		}
		isDispatching = false;
	}

	private boolean isInterestRequest(ReportRequest request) {
		return ReportRequest.SELECTION.equals(request.getType());
	}

	/**
	 * Dispose mediator.
	 */
	public void dispose() {
		if (DesignerConstants.TRACING_MEDIATOR_DISPOSE) {
			System.out.println("ReportMediator >> Disposing ..."); //$NON-NLS-1$
		}
		currentState = null;
		listeners.clear();
		stackPointer = 0;
		stack = null;
		if (DesignerConstants.TRACING_MEDIATOR_DISPOSE) {
			System.out.println("ReportMediator >> Disposed"); //$NON-NLS-1$
		}
	}

	/**
	 * Return top state in stack.
	 */
	public void popState() {
		if (DesignerConstants.TRACING_MEDIATOR_STATE_POP) {
			System.out.println("ReportMediator >> Poping state . . ."); //$NON-NLS-1$
		}
		stackPointer--;
		if (stackPointer != 0) {
			restoreState((ReportMediatorState) stack.get(stackPointer));
		}
		if (stackPointer == 0) {
			stack.clear();
		}
		if (DesignerConstants.TRACING_MEDIATOR_STATE_POP) {
			System.out.println("ReportMediator >> Poping finished"); //$NON-NLS-1$
		}
	}

	/**
	 * Gets the current state
	 * 
	 * @return
	 */
	public IMediatorState getCurrentState() {
		return currentState;
	}

	/**
	 * Push state of colleague, which send the notification, into stack.
	 */
	public void pushState() {
		if (DesignerConstants.TRACING_MEDIATOR_STATE_PUSH) {
			System.out.print("ReportMediator >> Pushing state . . ."); //$NON-NLS-1$
		}
		try {
			ReportMediatorState s;
			if (stack.size() > stackPointer) {
				s = (ReportMediatorState) stack.get(stackPointer);
				s.copyFrom(currentState);
			} else {
				stack.add(currentState.clone());
			}
			stackPointer++;
		} catch (CloneNotSupportedException e) {
			if (DesignerConstants.TRACING_MEDIATOR_STATE_PUSH) {
				System.out.println("ReportMediator >> Pushing failed"); //$NON-NLS-1$
			}
			throw new RuntimeException(e.getMessage());
		}
		if (DesignerConstants.TRACING_MEDIATOR_STATE_PUSH) {
			System.out.println("ReportMediator >> Pushing finished"); //$NON-NLS-1$
		}
	}

	private ReportMediatorState convertRequestToState(ReportRequest request) {
		ReportMediatorState retValue = new ReportMediatorState();
		retValue.setSource(request.getSource());
		retValue.setSelectiobObject(request.getSelectionModelList());
		return retValue;
	}

	private ReportRequest convertStateToRequest(ReportMediatorState s) {
		ReportRequest request = new ReportRequest();
		request.setSource(s.getSource());
		request.setSelectionObject(s.getSelectionObject());
		return request;
	}

	/**
	 * Restore previous state and discard the top one.
	 */
	public void restoreState() {
		restoreState((ReportMediatorState) stack.get(stackPointer - 1));
	}

	/**
	 * Sets all State information to that of the given State, called by
	 * restoreState()
	 * 
	 * @param s the State
	 */
	protected void restoreState(ReportMediatorState s) {
		if (DesignerConstants.TRACING_MEDIATOR_STATE_RESTORE) {
			System.out.println("ReportMediator >> Restoring state ..."); //$NON-NLS-1$
		}
		currentState.copyFrom(s);
		ReportRequest request = convertStateToRequest(s);
		notifyRequest(request);
		if (DesignerConstants.TRACING_MEDIATOR_STATE_RESTORE) {
			System.out.println("ReportMediator >> Restoring finised."); //$NON-NLS-1$
		}
	}

	/** Contains the state variables of this SWTGraphics object * */
	protected static class ReportMediatorState implements Cloneable, IMediatorState {

		private List selectiobObject = new ArrayList();
		private Object source;

		/** @see Object#clone() * */
		public Object clone() throws CloneNotSupportedException {
			ReportMediatorState state = new ReportMediatorState();
			state.setSelectiobObject(getSelectionObject());
			return state;
		}

		/**
		 * Copies all state information from the given State to this State
		 * 
		 * @param state The State to copy from
		 */
		protected void copyFrom(ReportMediatorState state) {
			setSelectiobObject(state.getSelectionObject());
			setSource(state.getSource());
		}

		/**
		 * Get selected object
		 * 
		 * @return Returns the selectiobObject.
		 */
		public List getSelectionObject() {
			return selectiobObject;
		}

		/**
		 * Set selected object
		 * 
		 * @param selectiobObject The selectiobObject to set.
		 */
		protected void setSelectiobObject(List selectiobObject) {
			this.selectiobObject = selectiobObject;
		}

		/**
		 * @return Returns the source.
		 */
		public Object getSource() {
			return source;
		}

		/**
		 * @param source The source to set.
		 */
		protected void setSource(Object source) {
			this.source = source;
		}
	}
}
