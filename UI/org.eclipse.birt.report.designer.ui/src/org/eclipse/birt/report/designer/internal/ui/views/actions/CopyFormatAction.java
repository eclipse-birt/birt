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

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.ContextSelectionAction;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.ui.IWorkbenchPart;

/**
 *
 */

public class CopyFormatAction extends ContextSelectionAction {

	private static class ElementFormatWrapper implements Listener {

		private DesignElementHandle element;

		public ElementFormatWrapper(DesignElementHandle element) {
			this.element = element;
			element.addListener(this);
		}

		public DesignElementHandle getElement() {
			if (element != null && element.getContainer() != null) {
				return element;
			}
			return null;
		}

		public void dispose() {
			if (this.element != null) {
				this.element.removeListener(this);
			}
			this.element = null;
		}

		@Override
		public void elementChanged(DesignElementHandle focus, NotificationEvent ev) {
			this.element = null;
		}
	}

	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.views.actions.CopyFormatAction"; //$NON-NLS-1$
	public static final String ACTION_TEXT = Messages.getString("CopyFormatAction.text"); //$NON-NLS-1$

	public static ElementFormatWrapper publicElementFormat;
	private static int instanceCount;

	private ElementFormatWrapper elementFormat;
	private boolean isDisposed;

	public static DesignElementHandle getDesignElementHandle() {
		return publicElementFormat == null ? null : publicElementFormat.getElement();
	}

	public CopyFormatAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText(ACTION_TEXT);
		setImageDescriptor(ReportPlatformUIImages.getImageDescriptor(IReportGraphicConstants.ICON_COPY_FORMAT));
		instanceCount++;
	}

	@Override
	public boolean calculateEnabled() {
		if (getSelectedObjects().size() == 1) {
			Object object = getSelectedObjects().get(0);
			if (object instanceof ReportElementEditPart) {
				return ((ReportElementEditPart) object).getModel() instanceof DesignElementHandle;
			}
		}
		return false;
	}

	@Override
	public void run() {
		Object object = getSelectedObjects().get(0);
		if (object instanceof ReportElementEditPart) {
			if (elementFormat != null) {
				elementFormat.dispose();
			}
			if (publicElementFormat != null) {
				publicElementFormat.dispose();
			}
			elementFormat = new ElementFormatWrapper((DesignElementHandle) ((ReportElementEditPart) object).getModel());
			publicElementFormat = elementFormat;
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		if (!isDisposed) {
			if (elementFormat != null) {
				elementFormat.dispose();
			}
			if (instanceCount > 0) {
				if (instanceCount == 1 && publicElementFormat != null) {
					publicElementFormat.dispose();
				}
				instanceCount--;
			}
			isDisposed = true;
		}
	}
}
