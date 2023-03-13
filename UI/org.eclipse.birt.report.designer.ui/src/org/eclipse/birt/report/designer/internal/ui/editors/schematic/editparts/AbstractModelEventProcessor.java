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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IModelEventFilter;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.event.IModelEventProcessor;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;

/**
 * AbstractModelEventProcessor
 */
public abstract class AbstractModelEventProcessor implements IModelEventProcessor {

	private List<IModelEventInfo> infoList = new ArrayList<>();
	private IModelEventFactory factory;
	private IModelEventFilter filter;
	private IModelEventInfoFactory eventInfoFactory = createModelEventInfoFactory();

	/**
	 * @param factory
	 */
	public AbstractModelEventProcessor(IModelEventFactory factory) {
		this.factory = factory;
	}

	public IModelEventFactory getFactory() {
		return factory;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.parts.event.
	 * IModelEventProcessor
	 * #addElementEvent(org.eclipse.birt.report.model.api.DesignElementHandle,
	 * org.eclipse.birt.report.model.api.activity.NotificationEvent)
	 */
	@Override
	public void addElementEvent(DesignElementHandle focus, NotificationEvent ev) {
		boolean isAccept = false;
		List<IModelEventInfo> temp = new ArrayList<>(infoList);
		int size = temp.size();
		IModelEventInfo newInfo = eventInfoFactory.createModelEventInfo(focus, ev);
		for (int i = 0; i < size; i++) {
			IModelEventInfo info = temp.get(i);
			if (info.canAcceptModelEvent(newInfo)) {
				info.addModelEvent(newInfo);
				isAccept = true;
				break;
			}
			if (newInfo.canAcceptModelEvent(info)) {
				newInfo.addModelEvent(info);
				infoList.remove(info);
				continue;
			}
		}
		if (!isAccept) {
			infoList.add(newInfo);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.parts.event.
	 * IModelEventProcessor#clear()
	 */
	@Override
	public void clear() {
		infoList.clear();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.parts.event.
	 * IModelEventProcessor#postElementEvent()
	 */
	@Override
	public void postElementEvent() {
		int size = infoList.size();
		for (int i = 0; i < size; i++) {
			IModelEventInfo info = infoList.get(i);
			Runnable run = factory.createModelEventRunnable(info.getTarget(), info.getType(), info.getOtherInfo());
			if (run != null) {
				run.run();
			}

		}
		infoList.clear();
	}

	/**
	 * IModelEventFactory
	 */
	public interface IModelEventFactory {

		/**
		 * Gets the reportrunnable from the model event infomation.
		 *
		 * @param focus
		 * @param type
		 * @param args
		 * @return
		 */
		Runnable createModelEventRunnable(Object focus, int type, Map args);

		/**
		 * @return
		 */
		boolean isDispose();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IModelEventFilter.class) {
			if (filter == null) {
				filter = new IModelEventFilter() {

					@Override
					public boolean filterModelEvent(DesignElementHandle focus, NotificationEvent ev) {
						if (includeEventType(ev.getEventType())) {
							return false;
						} else {
							return true;
						}
					}

				};
			}
			return filter;
		}
		return null;
	}

	/**
	 * Filter the event.
	 *
	 * @param type
	 * @return
	 */
	protected boolean includeEventType(int type) {
		return true;
	}

	/**
	 * IModelEventInfo
	 */
	public interface IModelEventInfo {

		int getType();

		DesignElementHandle getTarget();

		Map getOtherInfo();

		boolean canAcceptModelEvent(IModelEventInfo info);

		void addModelEvent(IModelEventInfo info);

	}

	/**
	 * Because the model event, Be care the target and type.Maybe in the future,
	 * need the detail infomation.
	 */
	protected static class ModelEventInfo implements IModelEventInfo {

		private DesignElementHandle target;
		private int type;
		private Map otherInfo = new HashMap();

		/**
		 * @param focus
		 * @param ev
		 */
		public ModelEventInfo(DesignElementHandle focus, NotificationEvent ev) {
			setTarget(focus);
			setType(ev.getEventType());
		}

		/**
		 * @param focus
		 * @param ev
		 * @return
		 */
		@Override
		public boolean canAcceptModelEvent(IModelEventInfo info) {
			return getTarget().equals(info.getTarget()) && info.getType() == getType();
		}

		/**
		 * @param focus
		 * @param ev
		 */
		@Override
		public void addModelEvent(IModelEventInfo info) {
			// do nothing now
		}

		/**
		 * @return
		 */
		@Override
		public DesignElementHandle getTarget() {
			return target;
		}

		/**
		 * @param target
		 */
		public void setTarget(DesignElementHandle target) {
			this.target = target;
		}

		/**
		 * @return
		 */
		@Override
		public int getType() {
			return type;
		}

		/**
		 * @param type
		 */
		public void setType(int type) {
			this.type = type;
		}

		/**
		 * @return
		 */
		@Override
		public Map getOtherInfo() {
			// now retrun null
			return otherInfo;
		}
	}

	/**
	 * Creats the factory to creat the event info object.
	 */
	protected abstract IModelEventInfoFactory createModelEventInfoFactory();

	/**
	 * IModelEventInfoFactory
	 */
	public interface IModelEventInfoFactory {

		/**
		 * Creats the event info object for given event.
		 *
		 * @param focus
		 * @param ev
		 * @return
		 */
		IModelEventInfo createModelEventInfo(DesignElementHandle focus, NotificationEvent ev);

	}

}
