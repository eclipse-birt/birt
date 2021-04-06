/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.nLayout.area.impl;

import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;

public class ListArea extends RepeatableArea {
	public ListArea(ContainerArea parent, LayoutContext context, IContent content) {
		super(parent, context, content);
	}

	public ListArea(ListArea area) {
		super(area);
	}

	protected boolean isInHeaderBand() {
		if (children.size() > 0) {
			ContainerArea child = (ContainerArea) children.get(children.size() - 1);
			IContent childContent = child.getContent();
			if (childContent != null) {
				if (childContent.getContentType() == IContent.LIST_GROUP_CONTENT) {
					return false;
				}
				IContent band = (IContent) childContent.getParent();
				if (band instanceof IBandContent) {
					int type = ((IBandContent) band).getBandType();
					if (type != IBandContent.BAND_HEADER) {
						return false;
					}
				}
			}
		}
		return true;
	}

	protected boolean needRepeat() {
		IListContent list = (IListContent) content;
		if (list != null && list.isHeaderRepeat()) {
			return true;
		}
		return false;
	}

	public ListArea cloneArea() {
		return new ListArea(this);
	}

}
