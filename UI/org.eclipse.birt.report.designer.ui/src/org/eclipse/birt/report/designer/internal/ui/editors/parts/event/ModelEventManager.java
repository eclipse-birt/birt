/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.parts.event;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.command.WrapperCommandStack;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.outline.ListenerElementVisitor;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.ActivityStackEvent;
import org.eclipse.birt.report.model.api.activity.ActivityStackListener;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.core.Listener;

/**
 * To manager the all model evetn, It is a facade for the model event and the
 * model listener. It is listener to the model then pass this event to the
 * processor. And through the command stack to listener the model trans if
 * commit or roll back.
 */
public class ModelEventManager implements Listener, IModelEventManager {

	/**
	 * Flag for the model event status.
	 */
	private boolean isPost = false;
	private List listenerList = new ArrayList();

	/**
	 * To add the listener to the all model element.
	 */
	private ListenerElementVisitor visitor;

	/**
	 * The root, It must not a mudulehandle
	 */
	private Object root;

	/**
	 * To listener the model trans status.(Commit or Roll back)
	 */
	private ActivityStackListener commandStackListener = new ActivityStackListener() {

		public void stackChanged(ActivityStackEvent event) {
			postEvent(event);
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.core.Listener#elementChanged(org.eclipse
	 * .birt.report.model.api.DesignElementHandle,
	 * org.eclipse.birt.report.model.api.activity.NotificationEvent)
	 */
	public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
		List temp = new ArrayList(listenerList);
		int size = temp.size();
		for (int i = 0; i < size; i++) {
			IModelEventProcessor processor = (IModelEventProcessor) temp.get(i);
			if (processor instanceof IFastConsumerProcessor && ((IFastConsumerProcessor) processor).isOverdued()) {
				listenerList.remove(processor);
				continue;
			}
			Object filter = processor.getAdapter(IModelEventFilter.class);
			if (filter instanceof IModelEventFilter) {
				if (((IModelEventFilter) filter).filterModelEvent(focus, ev)) {
					continue;
				}
			}
			processor.addElementEvent(focus, ev);
		}
		getListenerElementVisitor().addListener(focus);
	}

	private void postEvent(ActivityStackEvent event) {
		List temp = new ArrayList(listenerList);
		int size = temp.size();
		for (int i = 0; i < size; i++) {
			IModelEventProcessor processor = (IModelEventProcessor) temp.get(i);
			if (processor instanceof IFastConsumerProcessor && ((IFastConsumerProcessor) processor).isOverdued()) {
				listenerList.remove(processor);
				continue;
			}
		}
		checkStatus();
		switch (event.getAction()) {
		case ActivityStackEvent.DONE:
		case ActivityStackEvent.REDONE:
		case ActivityStackEvent.UNDONE:
			postModelEvent();
			break;
		case ActivityStackEvent.ROLL_BACK:
			clearEvent();
			break;
		default:
			break;

		}
	}

	/**
	 * Post the model event, when the model trans commit.
	 */
	protected void postModelEvent() {
		// when post the event, throw the exception, reset the flag.Make the
		// code strengh.
		try {
			List post = new ArrayList(listenerList);
			isPost = true;
			int size = post.size();
			for (int i = 0; i < size; i++) {
				IModelEventProcessor processor = (IModelEventProcessor) post.get(i);
				processor.postElementEvent();
			}
		} catch (Exception e) {
			ExceptionHandler.handle(e);
		} finally {
			isPost = false;
		}
	}

	/**
	 * Clear the model event, when the model trans Roll back.
	 */
	protected void clearEvent() {
		int size = listenerList.size();
		List list = new ArrayList(listenerList);
		for (int i = 0; i < size; i++) {
			IModelEventProcessor processor = (IModelEventProcessor) list.get(i);
			processor.clear();
		}
	}

	/**
	 * When post the event, don't allow to change the model.So don't change the
	 * model when receive tje model event or the processor change the model when
	 * collet the model event.Because Ecluipse don't allow new a thread to run
	 * freely ,don;t check the mulit_thread. If you must change the model when post
	 * the event, suggest use the Display.asyncExec. Ofcause can new a job to change
	 * the model event, but suggest don't do it, anybody know the result.
	 */
	// The data view receive the model event maybe to change the model.Maybe use
	// other method to resolve it
	// (Don't change the model when reveive the model event).
	private void checkStatus() {
		if (isPost) {
			throw new RuntimeException("The event is post now"); //$NON-NLS-1$
		}
	}

	/**
	 * Add the processor
	 * 
	 * @param processor
	 */
	public void addModelEventProcessor(IModelEventProcessor processor) {
		if (!listenerList.contains(processor)) {
			listenerList.add(processor);
		}
	}

	/**
	 * Remove the processor.
	 * 
	 * @param processor
	 */
	public void removeModelEventProcessor(IModelEventProcessor processor) {
		listenerList.remove(processor);
	}

	/**
	 * Dispose
	 */
	public void dispose() {
		listenerList.clear();
		if (root instanceof DesignElementHandle) {
			visitor.removeListener((DesignElementHandle) root);
		}
		root = null;
		visitor = null;
	}

	/**
	 * Gets the visitor.
	 * 
	 * @return the visitor
	 */
	private ListenerElementVisitor getListenerElementVisitor() {
		if (visitor == null) {
			visitor = new ListenerElementVisitor(this);
		}
		return visitor;
	}

	/**
	 * Hook the command stack, to add the command stack listener to the stack.
	 * 
	 * @param stack
	 */
	// Through the command stack know the model trans commit or roolback.But
	// command stack is command stack,
	// should not add the other responsibility to it(already add the rool back
	// status to the command stack).
	// In the future can know the trans status through the trans listener.
	public void hookCommandStack(WrapperCommandStack stack) {
		if (stack != null) {
			stack.addCommandStackListener(commandStackListener);
		}
	}

	/**
	 * Unhook the root.
	 * 
	 * @param stack
	 */
	public void unhookCommandStack(WrapperCommandStack stack) {
		if (stack != null) {
			stack.removeCommandStackListener(commandStackListener);
		}
	}

	/**
	 * Hook the root.
	 * 
	 * @param obj
	 */
	public void hookRoot(Object obj) {
		if (root == obj) {
			return;
		}
		unhookRoot(root);

		// listenerList.clear( );
		if (obj instanceof DesignElementHandle) {
			getListenerElementVisitor().addListener(((DesignElementHandle) obj).getModuleHandle());
		}
		this.root = obj;
	}

	/**
	 * Unhook the rootz
	 * 
	 * @param obj
	 */
	public void unhookRoot(Object obj) {
		if (obj instanceof DesignElementHandle) {
			getListenerElementVisitor().removeListener((DesignElementHandle) obj);
		}
	}

}
