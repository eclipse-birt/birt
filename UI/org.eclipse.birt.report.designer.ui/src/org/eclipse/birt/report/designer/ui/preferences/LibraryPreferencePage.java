/*******************************************************************************
 * Copyright (c) 2004,2005 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.preferences;

import java.io.File;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.actions.ExportToLibraryAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class LibraryPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	// The list that displays the current libraries
	private List LibraryList;

	// The title of the dialog
	private Label labelTitle;

	// The 3 buttons
	private Button addButton;
	private Button removeButton;
	private Button removeAllButton;

	// The extensions of the library files
	private String[] extensions = { Messages.getString("report.designer.ui.preferences.extensions"), //$NON-NLS-1$
	};

	// The title of the dialog opened for adding.
	private String openDialogTitle = Messages.getString("report.designer.ui.preferences.dialogtitle"); //$NON-NLS-1$

	private static final String EXPORT_PREF_TITLE = Messages
			.getString("report.designer.ui.preferences.library.export.title"); //$NON-NLS-1$
	private static final String EXPORT_PREF_ALWAYS = Messages
			.getString("report.designer.ui.preferences.library.export.overwrite.always"); //$NON-NLS-1$
	private static final String EXPORT_PREF_NEVER = Messages
			.getString("report.designer.ui.preferences.library.export.overwrite.never"); //$NON-NLS-1$
	private static final String EXPORT_PREF_PROMPT = Messages
			.getString("report.designer.ui.preferences.library.export.overwrite.prompt"); //$NON-NLS-1$

	private int exportPref;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#PreferencePage
	 */
	public LibraryPreferencePage() {
		super();
		noDefaultAndApplyButton();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.PreferencePage#PreferencePage(java.lang.String)
	 */
	public LibraryPreferencePage(String title) {
		super(title);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.PreferencePage#PreferencePage(java.lang.String,
	 * org.eclipse.jface.resource.ImageDescriptor)
	 */
	public LibraryPreferencePage(String title, ImageDescriptor image) {
		super(title, image);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.
	 * widgets.Composite)
	 */
	protected Control createContents(Composite parent) {
		UIUtil.bindHelp(parent, IHelpContextIds.PREFERENCE_BIRT_LIBRARY_ID);
		Composite mainComposite = new Composite(parent, SWT.NULL);

		// Create a data that takes up the extra space in the dialog .
		GridData data = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		data.grabExcessHorizontalSpace = false;
		mainComposite.setLayoutData(data);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		mainComposite.setLayout(layout);

		// Add in a dummy label for spacing
		labelTitle = new Label(mainComposite, SWT.NONE);
		labelTitle.setText(Messages.getString("report.designer.ui.preferences.labeltitle")); //$NON-NLS-1$

		// Add none
		new Label(mainComposite, SWT.NONE);

		// Add a List on the left of the Dialog
		int listStyle = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.HIDE_SELECTION;
		LibraryList = new List(mainComposite, listStyle);
		data = new GridData();

		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.FILL;

		data.grabExcessHorizontalSpace = true;
		data.grabExcessVerticalSpace = true;

		data.heightHint = 80;
		data.widthHint = 250;

		LibraryList.setLayoutData(data);
		LibraryList.setItems(ReportPlugin.getDefault().getLibraryPreference());
		LibraryList.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				removeButton.setEnabled(true);
			}

		});

		// Add a compose on the right which contains 3 buttons
		Composite buttons = new Composite(mainComposite, SWT.NONE);

		layout = new GridLayout();
		buttons.setLayout(layout);
		data = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		data.grabExcessVerticalSpace = true;
		buttons.setLayoutData(data);

		addButton = new Button(buttons, SWT.PUSH);
		addButton.setText(Messages.getString("report.designer.ui.preferences.buttonadd")); //$NON-NLS-1$
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		addButton.setLayoutData(data);
		addButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				FileDialog dialog = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell());
				dialog.setFilterExtensions(extensions);
				dialog.setText(openDialogTitle);
				String fileName = dialog.open();
				if (fileName == null) {
					return;
				}
				// if file does not exist
				if (!(new File(fileName).exists())) {
					ExceptionHandler.openErrorMessageBox(Messages.getString("AddLibraryAction.Error.Title"), //$NON-NLS-1$
							Messages.getFormattedString("AddLibraryAction.Error.FileNotFound", //$NON-NLS-1$
									new String[] { fileName }));
					return;
				}
				// if file is not library
				if (!(fileName.endsWith(".rptlibrary"))) //$NON-NLS-1$
				{
					ExceptionHandler.openErrorMessageBox(Messages.getString("AddLibraryAction.Error.Title"), //$NON-NLS-1$
							Messages.getFormattedString("AddLibraryAction.Error.FileIsNotLibrary", //$NON-NLS-1$
									new String[] { fileName, ".rptlibrary" })); //$NON-NLS-1$
					return;
				}
				// If can't find the name
				if (LibraryList.indexOf(fileName) == -1) {
					LibraryList.add(fileName, LibraryList.getItemCount());
					removeAllButton.setEnabled(true);
				} else
				// find the name
				{
					MessageBox msgBox = new MessageBox(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
							SWT.ICON_ERROR);
					msgBox.setText(Messages.getString("report.designer.ui.preferences.errortitle")); //$NON-NLS-1$
					msgBox.setMessage(Messages.getString("report.designer.ui.preferences.errormessage")); //$NON-NLS-1$
					msgBox.open();
				}

			}
		});

		removeButton = new Button(buttons, SWT.PUSH);
		removeButton.setText(Messages.getString("report.designer.ui.preferences.buttonremove")); //$NON-NLS-1$
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		removeButton.setLayoutData(data);
		removeButton.setEnabled(false);
		removeButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				int index = LibraryList.getSelectionIndex();
				LibraryList.remove(index);

				if (LibraryList.getItemCount() <= 0) {
					removeButton.setEnabled(false);
					removeAllButton.setEnabled(false);
				} else if (LibraryList.getItemCount() > 0) {
					if (index >= LibraryList.getItemCount()) {
						index -= 1;
					}
					LibraryList.setSelection(index);
					removeButton.setEnabled(true);
				}

			}
		});

		removeAllButton = new Button(buttons, SWT.PUSH);
		removeAllButton.setText(Messages.getString("report.designer.ui.preferences.buttonremoveall")); //$NON-NLS-1$
		data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		removeAllButton.setLayoutData(data);

		if (LibraryList.getItemCount() <= 0) {
			removeAllButton.setEnabled(false);
		}
		removeAllButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent event) {
				LibraryList.removeAll();
				removeButton.setEnabled(false);
				removeAllButton.setEnabled(false);
			}
		});

		exportPref = ReportPlugin.getDefault().getPreferenceStore().getInt(ExportToLibraryAction.PREF_KEY);

		Group optionGroup = new Group(parent, SWT.NONE);
		optionGroup.setText(EXPORT_PREF_TITLE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 4;
		gridLayout.marginWidth = 15;
		gridLayout.marginHeight = 15;
		optionGroup.setLayout(gridLayout);
		optionGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Button alwaysButton = new Button(optionGroup, SWT.RADIO);
		alwaysButton.setText(EXPORT_PREF_ALWAYS);
		alwaysButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				exportPref = ExportToLibraryAction.PREF_OVERWRITE;
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				exportPref = ExportToLibraryAction.PREF_OVERWRITE;
			}
		});
		alwaysButton.setSelection(exportPref == ExportToLibraryAction.PREF_OVERWRITE);

		Button neverButton = new Button(optionGroup, SWT.RADIO);
		neverButton.setText(EXPORT_PREF_NEVER);
		neverButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				exportPref = ExportToLibraryAction.PREF_NOT_OVERWRITE;
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				exportPref = ExportToLibraryAction.PREF_NOT_OVERWRITE;
			}
		});
		neverButton.setSelection(exportPref == ExportToLibraryAction.PREF_NOT_OVERWRITE);

		Button promptButton = new Button(optionGroup, SWT.RADIO);
		promptButton.setText(EXPORT_PREF_PROMPT);
		promptButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				exportPref = ExportToLibraryAction.PREF_PROMPT;
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				exportPref = ExportToLibraryAction.PREF_PROMPT;
			}
		});
		promptButton.setSelection(exportPref == ExportToLibraryAction.PREF_PROMPT);

		return mainComposite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
		// Initialize the preference store we wish to use
		setPreferenceStore(ReportPlugin.getDefault().getPreferenceStore());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
	 */
	protected void performDefaults() {
		LibraryList.setItems(ReportPlugin.getDefault().getDefaultLibraryPreference());
		removeButton.setEnabled(false);
		if (LibraryList.getItemCount() <= 0) {
			removeAllButton.setEnabled(false);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.IPreferencePage#performOk()
	 */
	public boolean performOk() {
		ReportPlugin.getDefault().setLibraryPreference(LibraryList.getItems());
		ReportPlugin.getDefault().getPreferenceStore().setValue(ExportToLibraryAction.PREF_KEY, exportPref);
		return super.performOk();
	}

}
