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
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.HighlightDescriptorProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.HighlightRuleBuilder;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.item.crosstab.ui.views.dialogs.CrosstabHighlightRuleBuilder;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.jface.window.Window;

/**
 * 
 */

public class CrosstabHighlightDescriptorProvider extends HighlightDescriptorProvider {
	public CrosstabHighlightDescriptorProvider() {
		super();
	}

	public CrosstabHighlightDescriptorProvider(int expressionType) {
		super(expressionType);
	}

	protected HighlightRuleBuilder createAddDialog(int handleCount) {
		CrosstabHighlightRuleBuilder builder = new CrosstabHighlightRuleBuilder(UIUtil.getDefaultShell(),
				Messages.getString("HighlightsPage.Dialog.NewHighlight"), //$NON-NLS-1$
				this);

		builder.updateHandle(null, handleCount);

		builder.setDesignHandle(getDesignElementHandle());
		if (getDesignElementHandle() instanceof ReportItemHandle) {
			builder.setReportElement((ReportItemHandle) getDesignElementHandle());
		} else if (getDesignElementHandle() instanceof GroupHandle) {
			builder.setReportElement((ReportItemHandle) ((GroupHandle) getDesignElementHandle()).getContainer());
		}
		return builder;
	}

	public boolean edit(Object input, int handleCount) {
		boolean result = false;
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();

		try {
			stack.startTrans(Messages.getString("HighlightsPage.trans.Edit")); //$NON-NLS-1$

			CrosstabHighlightRuleBuilder builder = new CrosstabHighlightRuleBuilder(UIUtil.getDefaultShell(),
					Messages.getString("HighlightsPage.Dialog.EditHighlight"), //$NON-NLS-1$
					this);

			HighlightRuleHandle handle = (HighlightRuleHandle) input;

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
}
