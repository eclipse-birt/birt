/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.outline.providers;

import java.util.ArrayList;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.TableHandle;

/**
 * This class represents table provider.
 *
 *
 */
public class TableProvider extends DefaultNodeProvider {

	public static final String COLUMNHEADING_DISPALYNAME = "ListingElement.displayname.ColumnHeading"; //$NON-NLS-1$

	public static final String DETAIL_DISPALYNAME = "ListingElement.displayname.Detail"; //$NON-NLS-1$

	public static final String HEADER_DISPALYNAME = "ListingElement.displayname.Header"; //$NON-NLS-1$

	public static final String FOOTER_DISPALYNAME = "ListingElement.displayname.Footer"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.outline.providers.
	 * INodeProvider#getChildren(java.lang.Object)
	 */
	@Override
	public Object[] getChildren(Object object) {
		if (object instanceof TableHandle) {
			// Table element children.
			TableHandle tableHdl = (TableHandle) object;
			ArrayList list = new ArrayList();
			list.add(tableHdl.getSlot(ListingHandle.HEADER_SLOT));
			list.add(tableHdl.getSlot(ListingHandle.DETAIL_SLOT));
			list.add(tableHdl.getSlot(ListingHandle.FOOTER_SLOT));
			list.add(tableHdl.getSlot(ListingHandle.GROUP_SLOT));
			return list.toArray();
		}
		return super.getChildren(object);
	}

}
