/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.executor;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.birt.report.engine.extension.IExtendedItem;
import org.eclipse.birt.report.engine.extension.IExtendedItemFactory;
import org.eclipse.birt.report.engine.extension.IReportEventHandler;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.extension.IReportItemPreparation;
import org.eclipse.birt.report.engine.extension.IReportItemPresentation;
import org.eclipse.birt.report.engine.extension.IReportItemQuery;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

public class ExtendedItemManager {

	ExtensionManager manager = ExtensionManager.getInstance();
	HashMap extFactories = new HashMap();
	HashMap extItems = new HashMap();;

	public ExtendedItemManager() {
	}

	protected IExtendedItem getExtendedItem(ExtendedItemHandle handle) {
		if (extItems.containsKey(handle)) {
			return (IExtendedItem) extItems.get(handle);
		}

		IExtendedItemFactory extFactory = null;
		IExtendedItem extItem = null;

		// get factory
		String tagName = handle.getExtensionName();
		if (extFactories.containsKey(tagName)) {
			extFactory = (IExtendedItemFactory) extFactories.get(tagName);
		} else {
			extFactory = manager.createExtendedItemFactory(tagName);
			extFactories.put(tagName, extFactory);
		}

		// get item
		if (extFactory != null) {
			extItem = extFactory.createExtendedItem(handle);
		}
		extItems.put(handle, extItem);
		return extItem;
	}

	public IReportItemQuery createQuery(ExtendedItemHandle handle) {
		IExtendedItem extItem = getExtendedItem(handle);
		if (extItem != null) {
			return extItem.createQuery();
		} else {
			return manager.createQueryItem(handle.getExtensionName());
		}
	}

	public IReportItemPresentation createPresentation(ExtendedItemHandle handle) {
		IExtendedItem extItem = getExtendedItem(handle);
		if (extItem != null) {
			return extItem.createPresentation();
		} else {
			return manager.createPresentationItem(handle.getExtensionName());
		}
	}

	public IReportEventHandler createEventHandler(ExtendedItemHandle handle) {
		IExtendedItem extItem = getExtendedItem(handle);
		if (extItem != null) {
			return extItem.createEventHandler();
		} else {
			return manager.createEventHandler(handle.getExtensionName());
		}
	}

	public IReportItemPreparation createPreparation(ExtendedItemHandle handle) {
		IExtendedItem extItem = getExtendedItem(handle);
		if (extItem != null) {
			return extItem.createPreparation();
		} else {
			return manager.createPreparationItem(handle.getExtensionName());
		}
	}

	public IReportItemExecutor createExecutor(ExtendedItemHandle handle, ExecutorManager exeManager) {
		IExtendedItem extItem = getExtendedItem(handle);
		if (extItem != null) {
			return extItem.createExecutor();
		} else {
			return manager.createReportItemExecutor(exeManager, handle.getExtensionName());
		}
	}

	public void close() {
		Iterator iterator = extItems.values().iterator();
		while (iterator.hasNext()) {
			IExtendedItem extItem = (IExtendedItem) iterator.next();
			if (extItem != null) {
				extItem.release();
			}
		}

		iterator = extFactories.values().iterator();
		while (iterator.hasNext()) {
			IExtendedItemFactory extFactory = (IExtendedItemFactory) iterator.next();
			if (extFactory != null) {
				extFactory.release();
			}
		}

		extFactories.clear();
		extItems.clear();
	}
}
