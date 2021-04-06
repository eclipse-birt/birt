/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.re;

import org.eclipse.birt.report.engine.extension.ExtendedItemFactoryBase;
import org.eclipse.birt.report.engine.extension.IExtendedItem;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.script.internal.handler.CrosstabHandlerCache;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * CrosstabExtendedItemFactory
 */
public class CrosstabExtendedItemFactory extends ExtendedItemFactoryBase implements ICrosstabConstants {

	private CrosstabHandlerCache handlerCache;
	private CrosstabExtendedItem crosstabSingleton;
	private CrosstabCellExtendedItem crosstabCellSingleton;

	@Override
	public IExtendedItem createExtendedItem(ExtendedItemHandle handle) {
		String exName = handle.getExtensionName();

		if (CROSSTAB_EXTENSION_NAME.equals(exName)) {
			if (crosstabSingleton == null) {
				crosstabSingleton = new CrosstabExtendedItem();
			}
			return crosstabSingleton;
		} else if (CROSSTAB_CELL_EXTENSION_NAME.equals(exName) || AGGREGATION_CELL_EXTENSION_NAME.equals(exName)) {
			if (crosstabCellSingleton == null) {
				handlerCache = new CrosstabHandlerCache();
				crosstabCellSingleton = new CrosstabCellExtendedItem(handlerCache);
			}
			return crosstabCellSingleton;
		}

		return super.createExtendedItem(handle);
	}

	@Override
	public void release() {
		if (crosstabSingleton != null) {
			crosstabSingleton.release();
			crosstabSingleton = null;
		}

		if (crosstabCellSingleton != null) {
			handlerCache.dispose();
			handlerCache = null;

			crosstabCellSingleton.release();
			crosstabCellSingleton = null;
		}

		super.release();
	}
}
