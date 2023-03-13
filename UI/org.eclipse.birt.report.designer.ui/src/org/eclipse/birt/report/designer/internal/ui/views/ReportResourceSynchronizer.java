/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.ui.views.IReportResourceChangeEvent;
import org.eclipse.birt.report.designer.ui.views.IReportResourceChangeListener;
import org.eclipse.birt.report.designer.ui.views.IReportResourceSynchronizer;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;

/**
 * ReportResourceSynchronizer
 */
public class ReportResourceSynchronizer implements IReportResourceSynchronizer {
	private static final Logger log = Logger.getLogger(ReportResourceSynchronizer.class.getName());

	// protected ListenerList listeners = new ListenerList( );
	private int[] eventTypes = { IReportResourceChangeEvent.NewResource, IReportResourceChangeEvent.LibraySaveChange,
			IReportResourceChangeEvent.ImageResourceChange, IReportResourceChangeEvent.DataDesignSaveChange,
			// IReportResourceChangeEvent.LibrayContentChange
	};
	private Map<Integer, List<IReportResourceChangeListener>> listeners = new HashMap<>();

	protected boolean disabled = false;

	public ReportResourceSynchronizer() {
	}

	private void internalAddListener(int type, IReportResourceChangeListener listener) {
		List<IReportResourceChangeListener> list = listeners.get(type);
		if (list == null) {
			list = new ArrayList<>();
			listeners.put(type, list);
		}
		list.add(listener);
	}

	@Override
	public void addListener(int type, IReportResourceChangeListener listener) {
		if (disabled) {
			return;
		}
		for (int i = 0; i < eventTypes.length; i++) {
			if ((type & eventTypes[i]) != 0) {
				internalAddListener(eventTypes[i], listener);
			}
		}

	}

	@Override
	public void removeListener(int type, IReportResourceChangeListener listener) {
		if (disabled) {
			return;
		}
		for (int i = 0; i < eventTypes.length; i++) {
			if ((type & eventTypes[i]) != 0) {
				internalRemoveListener(eventTypes[i], listener);
			}
		}
	}

	private void internalRemoveListener(int type, IReportResourceChangeListener listener) {
		List<IReportResourceChangeListener> list = listeners.get(type);
		if (list != null) {
			list.remove(listener);
		}
	}

	protected void notifyListeners(final IReportResourceChangeEvent event) {
		log.log(Level.FINE, event.toString());

		// Object[] list = listeners.getListeners( );
		List<IReportResourceChangeListener> list = listeners.get(event.getType());
		if (list == null) {
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			final IReportResourceChangeListener rcl = list.get(i);

			SafeRunner.run(new SafeRunnable() {

				@Override
				public void run() throws Exception {
					rcl.resourceChanged(event);
				}
			});
		}

	}

	@Override
	public void notifyResourceChanged(IReportResourceChangeEvent event) {
		if (disabled) {
			return;
		}

		notifyListeners(event);
	}

}
