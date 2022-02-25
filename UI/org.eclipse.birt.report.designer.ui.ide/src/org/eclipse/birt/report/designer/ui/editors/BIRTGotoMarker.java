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

package org.eclipse.birt.report.designer.ui.editors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.IRequestConvert;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.lib.commands.SetCurrentEditModelCommand;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.editors.pages.ReportLayoutEditorFormPage;
import org.eclipse.birt.report.designer.ui.editors.pages.ReportMasterPageEditorFormPage;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TemplateElementHandle;
import org.eclipse.birt.report.model.api.TemplateParameterDefinitionHandle;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.editor.IFormPage;
import org.eclipse.ui.ide.IGotoMarker;

/**
 * BIRTGotoMarker
 */
class BIRTGotoMarker implements IGotoMarker {

	protected IDEMultiPageReportEditor editorPart;

	public BIRTGotoMarker(IDEMultiPageReportEditor editorPart) {
		this.editorPart = editorPart;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.ui.ide.IGotoMarker#gotoMarker(org.eclipse.core.resources.IMarker)
	 */
	@Override
	public void gotoMarker(IMarker marker) {
		assert editorPart != null;

		if (!marker.exists()) {
			return;
		}

		ModuleHandle moduleHandle = editorPart.getModel();
		ReportElementHandle reportElementHandle = getReportElementHandle(moduleHandle, marker);
		if (reportElementHandle == null
				|| (reportElementHandle != null && isElementTemplateParameterDefinition(reportElementHandle))) {
			gotoXMLSourcePage(marker);
		} else if (moduleHandle instanceof ReportDesignHandle) {
			// go to master page
			if (isElementInMasterPage(reportElementHandle)) {
				gotoLayoutPage(ReportMasterPageEditorFormPage.ID, marker, reportElementHandle);
			} else
			// go to Layout Page
			{
				gotoLayoutPage(ReportLayoutEditorFormPage.ID, marker, reportElementHandle);
			}
		} else if (moduleHandle instanceof LibraryHandle) {
			// go to master page
			if (isElementInMasterPage(reportElementHandle)) {
				gotoLayoutPage(LibraryMasterPageEditorFormPage.ID, marker, reportElementHandle);
			} else
			// go to Layout Page
			{
				gotoLibraryLayoutPage(marker, reportElementHandle);
			}
		}
	}

	protected void gotoLibraryLayoutPage(IMarker marker, ReportElementHandle reportElementHandle) {
		String pageId = LibraryLayoutEditorFormPage.ID;
		if (!activatePage(pageId)) {
			return;
		}
		ModuleHandle moduleHandle = editorPart.getModel();
		reportElementHandle = getReportElementHandle(moduleHandle, marker);
		if (reportElementHandle != null && (!isElementInMasterPage(reportElementHandle))) {
			SetCurrentEditModelCommand command = new SetCurrentEditModelCommand(reportElementHandle);
			command.execute();
		} else
		// can not find it in this editpage
		{
			MessageDialog.openError(UIUtil.getDefaultShell(), Messages.getString("BIRTGotoMarker.Error.Title"), //$NON-NLS-1$
					Messages.getString("BIRTGotoMarker.Error.Message")); //$NON-NLS-1$
		}

	}

	protected void gotoLayoutPage(String pageId, final IMarker marker, final ReportElementHandle reportElementHandle) {
		if (!activatePage(pageId)) {
			return;
		}

		Display.getCurrent().asyncExec(new Runnable() {

			@Override
			public void run() {
				gotoLayoutMarker(marker, reportElementHandle);
			}
		});
	}

	protected void gotoXMLSourcePage(final IMarker marker) {
		if (!activatePage(MultiPageReportEditor.XMLSourcePage_ID)) {
			return;
		}

		final IReportEditorPage reportXMLSourcePage = (IReportEditorPage) editorPart.getActivePageInstance();
		Display.getCurrent().asyncExec(new Runnable() {

			@Override
			public void run() {
				gotoXMLSourceMarker(reportXMLSourcePage, marker);
			}
		});
	}

	protected void gotoXMLSourceMarker(IReportEditorPage reportXMLSourcePage, IMarker marker) {
		reportXMLSourcePage.selectReveal(marker);
	}

	protected boolean activatePage(String pageId) {
		if (pageId.equals(editorPart.getActivePageInstance().getId())) {
			return true;
		}

		IFormPage formPage = editorPart.setActivePage(pageId);
		if (formPage != null) {
			return true;
		}
		return false;
	}

	protected ReportElementHandle getReportElementHandle(ModuleHandle moduleHandle, IMarker marker) {
		Integer elementId = 0;
		try {
			elementId = (Integer) marker.getAttribute(IDEMultiPageReportEditor.ELEMENT_ID);
		} catch (CoreException e) {
			ExceptionUtil.handle(e);
		}
		if (elementId != null && elementId.intValue() > 0) {
			DesignElementHandle elementHandle = moduleHandle.getElementByID(elementId.intValue());
			if (elementHandle == null || !(elementHandle instanceof ReportElementHandle)) {
				return null;
			}
			if (elementHandle instanceof CellHandle || elementHandle instanceof ColumnHandle
					|| elementHandle instanceof MasterPageHandle || elementHandle instanceof ReportItemHandle
					|| elementHandle instanceof RowHandle || elementHandle instanceof TemplateElementHandle) {
				return (ReportElementHandle) elementHandle;
			}
		}
		return null;
	}

	/**
	 * Select the report element in the layout(including report design and library)
	 *
	 * @param marker the marker to go to
	 */
	protected void gotoLayoutMarker(IMarker marker, ReportElementHandle reportElementHandle) {
		ModuleHandle moduleHandle = editorPart.getModel();
		reportElementHandle = getReportElementHandle(moduleHandle, marker);

		if (reportElementHandle == null) {
			MessageDialog.openError(UIUtil.getDefaultShell(), Messages.getString("BIRTGotoMarker.Error.Title"), //$NON-NLS-1$
					Messages.getString("BIRTGotoMarker.Error.Message")); //$NON-NLS-1$
			return;
		}

		List list = new ArrayList();
		list.add(reportElementHandle);
		ReportRequest r = new ReportRequest();
		r.setType(ReportRequest.SELECTION);
		r.setRequestConvert(new IRequestConvert() {

			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * org.eclipse.birt.report.designer.core.util.mediator.request.IRequestConvert#
			 * convertSelectionToModelLisr(java.util.List)
			 */
			@Override
			public List convertSelectionToModelLisr(List list) {
				List lst = new ArrayList();

				for (Iterator itr = list.iterator(); itr.hasNext();) {
					Object obj = itr.next();

					// if ( obj instanceof ReportElementModel )
					// {
					// lst.add( ( (ReportElementModel) obj ).getSlotHandle( ) );
					// }
					lst.add(obj);
				}
				return lst;
			}
		});

		r.setSelectionObject(list);
		SessionHandleAdapter.getInstance().getMediator().notifyRequest(r);

	}

	protected boolean isElementInMasterPage(DesignElementHandle elementHandle) {
		ModuleHandle root = elementHandle.getRoot();
		DesignElementHandle container = elementHandle;
		while (container != null && container != root) {
			if (container instanceof MasterPageHandle) {
				return true;
			}
			container = container.getContainer();
		}

		return false;
	}

	protected boolean isElementTemplateParameterDefinition(DesignElementHandle elementHandle) {
		ModuleHandle root = elementHandle.getRoot();
		DesignElementHandle container = elementHandle;
		while (container != null && container != root) {
			if (container instanceof TemplateParameterDefinitionHandle) {
				return true;
			}
			container = container.getContainer();
		}
		return false;
	}
}
