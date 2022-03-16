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

package org.eclipse.birt.report.designer.ui.lib.explorer.dialog;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import org.eclipse.birt.report.designer.internal.ui.dialogs.resource.IResourceContentProvider;
import org.eclipse.birt.report.designer.internal.ui.dialogs.resource.ResourceFileFolderSelectionDialog;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

/**
 * A dialog for moving reource in resource explorer. User can select a folder to
 * move reource.
 */
public class MoveResourceDialog extends ResourceFileFolderSelectionDialog {

	/**
	 * Constructs a dialog for moving resource.
	 *
	 * @param files
	 */
	public MoveResourceDialog(final Collection<File> files) {
		super(false, false, null);
		setTitle(Messages.getString("MoveResourceDialog.Title"));
		setMessage(Messages.getString("MoveResourceDialog.Message"));
		setDoubleClickSelects(true);
		setAllowMultiple(false);
		setHelpAvailable(false);
		setEmptyFolderShowStatus(IResourceContentProvider.ALWAYS_SHOW_EMPTYFOLDER);
		setValidator(new ISelectionStatusValidator() {

			@Override
			public IStatus validate(Object[] selection) {
				for (Object s : selection) {
					if (s instanceof ResourceEntry) {
						URL url = ((ResourceEntry) s).getURL();
						try {
							url = URIUtil.toURI(url).toURL();
						} catch (Exception e1) {
						}
						for (File f : files) {
							try {
								if (url.equals(f.getParentFile().toURI().toURL()) || url.equals(f.toURI().toURL())) {
									return new Status(IStatus.ERROR, ReportPlugin.REPORT_UI, "");
								}
							} catch (MalformedURLException e) {
							}
						}
					}
				}
				return new Status(IStatus.OK, ReportPlugin.REPORT_UI, "");
			}
		});
	}
}
