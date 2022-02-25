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

package org.eclipse.birt.report.designer.internal.ui.command;

import java.io.File;

import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseWizardDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.ReportResourceChangeEvent;
import org.eclipse.birt.report.designer.internal.ui.wizards.ExportReportWizardPage;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.views.IReportResourceChangeEvent;
import org.eclipse.birt.report.designer.ui.views.IReportResourceSynchronizer;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.util.ElementExportUtil;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 *
 */

public class ExportLibraryHandler extends SelectionHandler {

	private static String windowTitle = Messages.getString("ExportToLibraryAction.wizard.windowTitle"); //$NON-NLS-1$

	private final static String DIALOG_TITLE = Messages.getString("ExportToLibraryAction.Dialog.Title"); //$NON-NLS-1$
	private final static String DIALOG_MESSAGE = Messages.getString("ExportToLibraryAction.Dialog.Message"); //$NON-NLS-1$
	private final static String BUTTON_YES = Messages.getString("ExportToLibraryAction.Button.Yes"); //$NON-NLS-1$
	private final static String BUTTON_NO = Messages.getString("ExportToLibraryAction.Button.No"); //$NON-NLS-1$
	private final static String BUTTON_CANCEL = Messages.getString("ExportToLibraryAction.Button.Cancel"); //$NON-NLS-1$
	private final static String REMEMBER_DECISION = Messages
			.getString("ExportToLibraryAction.Message.RememberDecision"); //$NON-NLS-1$

	private boolean saveDecision;
	private int pref;

	private DesignElementHandle selection;

	/**
	 * Overwrite exist element.
	 */
	public static final int PREF_OVERWRITE = 1;

	/**
	 * Do not overwrite exist element.
	 */
	public static final int PREF_NOT_OVERWRITE = 2;

	/**
	 * Prompt
	 */
	public static final int PREF_PROMPT = 0;

	/**
	 * ExportToLibraryAction preference key.
	 */
	public static final String PREF_KEY = "ExportToLibraryAction.Pref"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands
	 * .ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		boolean retBoolean = true;
		super.execute(event);
		selection = (DesignElementHandle) getElementHandles().get(0);
		ExportReportWizard exportReportWizard = new ExportReportWizard();
		WizardDialog wDialog = new BaseWizardDialog(UIUtil.getDefaultShell(), exportReportWizard);
		wDialog.setPageSize(500, 250);
		wDialog.open();

		return Boolean.valueOf(retBoolean);
	}

	public class ExportReportWizard extends Wizard {

		private ExportReportWizardPage page;

		/**
		 *
		 */
		public ExportReportWizard() {
			super();
			setWindowTitle(windowTitle);
			page = new ExportReportWizardPage(""); //$NON-NLS-1$
			addPage(page);

		}

		@Override
		public Image getDefaultPageImage() {
			return ReportPlugin.getImage("/icons/wizban/create_project_wizard.gif"); //$NON-NLS-1$
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.jface.wizard.Wizard#performFinish()
		 */

		@Override
		public boolean performFinish() {
			// TODO Auto-generated method stub

			try {
				String filename = page.getFullName();

				if (!filename.endsWith(".rptlibrary")) //$NON-NLS-1$
				{
					filename += ".rptlibrary"; //$NON-NLS-1$
				}
				pref = ReportPlugin.getDefault().getPreferenceStore().getInt(PREF_KEY);
				if (filename != null) {

					if (pref == PREF_PROMPT && new File(filename).exists()) {

						MessageDialog prefDialog = new MessageDialog(UIUtil.getDefaultShell(), DIALOG_TITLE, null,
								DIALOG_MESSAGE, MessageDialog.INFORMATION,
								new String[] { BUTTON_YES, BUTTON_NO, BUTTON_CANCEL }, 0) {

							/*
							 * (non-Javadoc)
							 *
							 * @seeorg.eclipse.jface.dialogs.MessageDialog# createCustomArea
							 * (org.eclipse.swt.widgets.Composite)
							 */
							@Override
							protected Control createCustomArea(Composite parent) {
								Composite container = new Composite(parent, SWT.NONE);
								GridLayout gridLayout = new GridLayout();
								gridLayout.marginWidth = 20;
								// gridLayout.marginTop = 15;
								container.setLayout(gridLayout);

								Button chkbox = new Button(container, SWT.CHECK);
								chkbox.setText(REMEMBER_DECISION);
								chkbox.addSelectionListener(new SelectionListener() {

									@Override
									public void widgetSelected(SelectionEvent e) {
										saveDecision = !saveDecision;
									}

									@Override
									public void widgetDefaultSelected(SelectionEvent e) {
										saveDecision = false;
									}
								});

								return super.createCustomArea(parent);
							}

							/*
							 * (non-Javadoc)
							 *
							 * @see org.eclipse.jface.dialogs.MessageDialog#buttonPressed (int)
							 */
							@Override
							protected void buttonPressed(int buttonId) {
								switch (buttonId) {
								case 0:
									pref = PREF_OVERWRITE;
									break;
								case 1:
									pref = PREF_NOT_OVERWRITE;
									break;
								default:
									break;
								}
								if (saveDecision) {
									ReportPlugin.getDefault().getPreferenceStore().setValue(PREF_KEY, pref);
								}
								super.buttonPressed(buttonId);
							}

						};
						if (prefDialog.open() == 2) {
							return true;
						}

					}
					if (selection instanceof ReportDesignHandle) {
						ElementExportUtil.exportDesign((ReportDesignHandle) selection, filename, pref == PREF_OVERWRITE,
								true);
					} else {
						ElementExportUtil.exportElement((DesignElementHandle) selection, filename,
								pref == PREF_OVERWRITE);
					}

					IReportResourceSynchronizer synchronizer = ReportPlugin.getDefault()
							.getResourceSynchronizerService();

					if (synchronizer != null) {
						synchronizer.notifyResourceChanged(new ReportResourceChangeEvent(this,
								Path.fromOSString(filename), IReportResourceChangeEvent.NewResource));
					}
				}
			} catch (Exception e) {
				ExceptionHandler.handle(e);
			}

			return true;
		}

	}

}
