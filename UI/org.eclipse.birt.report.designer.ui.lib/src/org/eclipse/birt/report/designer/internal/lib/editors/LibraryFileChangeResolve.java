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

package org.eclipse.birt.report.designer.internal.lib.editors;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.internal.ui.editors.IRelatedFileChangeResolve;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.LibrarySaveChangeEvent;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.views.IReportResourceChangeEvent;
import org.eclipse.birt.report.designer.ui.views.IReportResourceSynchronizer;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.jface.dialogs.MessageDialog;

/**
 * 
 */

public class LibraryFileChangeResolve implements IRelatedFileChangeResolve {

	protected static final Logger logger = Logger.getLogger(LibraryFileChangeResolve.class.getName());

	public boolean acceptType(int type) {
		return type == IReportResourceChangeEvent.LibraySaveChange;
	}

	public boolean isReload(IReportResourceChangeEvent event, ModuleHandle owner) {
		return ModuleUtil.isInclude(owner, ((LibrarySaveChangeEvent) event).getFileName());
	}

	public boolean isReset(IReportResourceChangeEvent event, ModuleHandle owner) {
		return owner.getFileName().equals(((LibrarySaveChangeEvent) event).getFileName());
	}

	public void notifySaveFile(ModuleHandle owner) {
		if (owner instanceof LibraryHandle) {
			IReportResourceSynchronizer synchronizer = ReportPlugin.getDefault().getResourceSynchronizerService();

			if (synchronizer != null) {
				synchronizer.notifyResourceChanged(new LibrarySaveChangeEvent(owner, null,
						IReportResourceChangeEvent.LibraySaveChange, owner.getFileName()));
			}
		}

	}

	public boolean reload(ModuleHandle owner) {
		if (owner.needsSave()) {
			MessageDialog md = new MessageDialog(UIUtil.getDefaultShell(),
					Messages.getString("MultiPageReportEditor.ConfirmVersion.Dialog.Title"), //$NON-NLS-1$
					null, Messages.getString("MultiPageReportEditor.ConfirmVersion.Dialog.SaveAndReloadMessage"), //$NON-NLS-1$
					MessageDialog.QUESTION_WITH_CANCEL,
					new String[] { Messages.getString("MultiPageReportEditor.SaveButton"), //$NON-NLS-1$
							// Messages.getString( "MultiPageReportEditor.DiscardButton" ), //$NON-NLS-1$
							Messages.getString("MultiPageReportEditor.CancelButton") //$NON-NLS-1$
					}, 0);

			switch (md.open()) {
			case 0:
				try {
					owner.save();
				} catch (IOException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
				UIUtil.reloadModuleHandleLibraries(owner);
				return true;
			// case 1 :
			// UIUtil.reloadModuleHandleLibraries( owner );
			// return true;
			default:
				return false;
			}
		} else if (MessageDialog.openConfirm(UIUtil.getDefaultShell(),
				Messages.getString("MultiPageReportEditor.ConfirmVersion.Dialog.Title"), //$NON-NLS-1$
				Messages.getString("MultiPageReportEditor.ConfirmVersion.Dialog.ReloadMessage"))) //$NON-NLS-1$
		{
			UIUtil.reloadModuleHandleLibraries(owner);
			return true;
		}
		return false;
	}

	public boolean reset() {
		if (MessageDialog.openConfirm(UIUtil.getDefaultShell(),
				Messages.getString("MultiPageReportEditor.ConfirmVersion.Dialog.Title"), //$NON-NLS-1$
				Messages.getString("MultiPageReportEditor.ConfirmVersion.Dialog.ResetMessage"))) //$NON-NLS-1$
		{
			return true;
		}
		return false;
	}
}
