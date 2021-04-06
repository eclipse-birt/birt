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

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.MapDescriptorProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.MapRuleBuilder;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.item.crosstab.ui.views.dialogs.CrosstabMapRuleBuilder;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.MapRuleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.jface.window.Window;

/**
 * 
 */

public class CrosstabMapDescriptorProvider extends MapDescriptorProvider {
	public CrosstabMapDescriptorProvider() {
		super();
	}

	public CrosstabMapDescriptorProvider(int expressionType) {
		super(expressionType);
	}

	public boolean edit(Object input, int handleCount) {
		boolean result = false;
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();

		try {
			stack.startTrans(Messages.getString("MapPage.transName.editMapRule")); //$NON-NLS-1$

			CrosstabMapRuleBuilder builder = new CrosstabMapRuleBuilder(UIUtil.getDefaultShell(),
					MapRuleBuilder.DLG_TITLE_EDIT, this);

			MapRuleHandle handle = (MapRuleHandle) input;

			builder.updateHandle(handle, handleCount);

			builder.setDesignHandle(getDesignElementHandle());

			if (getDesignElementHandle() instanceof ReportItemHandle) {
				builder.setReportElement((ReportItemHandle) getDesignElementHandle());
			} else if (getDesignElementHandle() instanceof GroupHandle) {
				builder.setReportElement((ReportItemHandle) ((GroupHandle) getDesignElementHandle()).getContainer());
			}

			if (builder.open() == Window.OK) {
				result = true;
			}
			stack.commit();

		} catch (Exception e) {
			stack.rollback();
			ExceptionUtil.handle(e);
			result = false;
		}
		return result;
	}

	protected MapRuleBuilder createAddDialog(int handleCount) {
		CrosstabMapRuleBuilder builder = new CrosstabMapRuleBuilder(UIUtil.getDefaultShell(),
				MapRuleBuilder.DLG_TITLE_NEW, this);

		builder.updateHandle(null, handleCount);

		builder.setDesignHandle(getDesignElementHandle());

		if (getDesignElementHandle() instanceof ReportItemHandle) {
			builder.setReportElement((ReportItemHandle) getDesignElementHandle());
		} else if (getDesignElementHandle() instanceof GroupHandle) {
			builder.setReportElement((ReportItemHandle) ((GroupHandle) getDesignElementHandle()).getContainer());
		}

		return builder;
	}
}
