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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemThemeHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.css.StyleSheetException;
import org.eclipse.birt.report.model.api.metadata.IPredefinedStyle;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * Wizard page for selecting CSS style to import to report design.
 */

public class WizardSelectCssStylePage extends WizardPage {

	private Text fileNameField;

	private Button selectButton;

	private Table stylesTable;

	private Table notificationsTable;

	private String fileName;

	private CssStyleSheetHandle cssHandle;

	private Map styleMap = new HashMap();

	private List styleNames = new ArrayList();

	private List unSupportedStyleNames = new ArrayList();

	private Label title;

	private Button selectAll;

	private Button deselectAll;

	private ReportItemThemeHandle theme;

	public WizardSelectCssStylePage(String pageName) {
		super(pageName);
	}

	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite topComposite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		topComposite.setLayout(layout);
		topComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		createFileNameComposite(topComposite);

		createStyleComposite(topComposite);

		setControl(topComposite);

		setPageComplete(validateFileName());
		setErrorMessage(null);
		setMessage(null);

		UIUtil.bindHelp(parent, IHelpContextIds.IMPORT_CSS_STYLE_WIZARD_ID);
	}

	private void createFileNameComposite(Composite parent) {
		Composite nameComposite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(3, false);
		layout.marginWidth = 0;
		nameComposite.setLayout(layout);
		nameComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label title = new Label(nameComposite, SWT.NULL);
		title.setText(Messages.getString("WizardSelectCssStylePage.label.fileName")); //$NON-NLS-1$

		fileNameField = new Text(nameComposite, SWT.BORDER);
		fileNameField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fileNameField.addListener(SWT.Modify, new Listener() {

			@Override
			public void handleEvent(Event e) {
				setPageComplete(validateFileName());
				refresh();
			}
		});

		selectButton = new Button(nameComposite, SWT.PUSH);
		selectButton.setText(Messages.getString("WizardSelectCssStylePage.button.label.browse")); //$NON-NLS-1$

		selectButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileSelector = new FileDialog(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
						SWT.NULL);

				fileSelector.setFilterExtensions(new String[] { "*.css;*.CSS" });//$NON-NLS-1$

				String fileName = fileSelector.open();
				if (fileName != null) {
					// should check extensions in Linux enviroment
					if (!checkExtensions(new String[] { "*.css", "*.CSS" }, fileName)) //$NON-NLS-1$ //$NON-NLS-2$
					{
						ExceptionHandler.openErrorMessageBox(
								Messages.getString("WizardSelectCssStylePage.FileNameError.Title"), //$NON-NLS-1$
								Messages.getString("WizardSelectCssStylePage.FileNameError.Message")); //$NON-NLS-1$

					} else {
						fileNameField.setText(fileName);
					}

				}
			}
		});

		setButtonLayoutData(selectButton);

	}

	private void createStyleComposite(Composite parent) {
		Composite styleComposite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		styleComposite.setLayout(layout);
		styleComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		title = new Label(styleComposite, SWT.NULL);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		title.setLayoutData(data);
		title.setText(""); //$NON-NLS-1$

		createStyleList(styleComposite);

		createButtons(styleComposite);
	}

	public void createStyleList(Composite parent) {
		Composite styleComposite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		styleComposite.setLayout(layout);
		styleComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		stylesTable = new Table(styleComposite, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER | SWT.CHECK);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.heightHint = 100;
		stylesTable.setLayoutData(data);
		stylesTable.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshButtons();
			}
		});

		// stylesTable.addSelectionListener( new SelectionAdapter( ) {
		//
		// public void widgetSelected( SelectionEvent e )
		// {
		// TableItem[] children = notificationsTable.getItems( );
		// for ( int i = 0; i < children.length; i++ )
		// {
		// children[i].dispose( );
		// }
		//
		// String styleName = ( (TableItem) e.item ).getText( );
		//
		// // SharedStyleHandle sh = (SharedStyleHandle) styleMap.get(
		// // styleName );
		//
		// {
		// TableItem notify = new TableItem( notificationsTable,
		// SWT.NULL );
		// notify.setText( styleName
		// + Messages.getString( "WizardSelectCssStylePage.info.canNotImport" )
		// ); //$NON-NLS-1$
		// notify.setImage( ReportPlatformUIImages.getImage(
		// IReportGraphicConstants.ICON_ELEMENT_STYLE ) );
		// }
		// }
		// } );

		new Label(styleComposite, SWT.NULL).setText(Messages.getString("WizardSelectCssStylePage.label.notifications")); //$NON-NLS-1$

		notificationsTable = new Table(styleComposite, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
		data = new GridData(GridData.FILL_BOTH);
		data.minimumHeight = 60;
		notificationsTable.setLayoutData(data);

	}

	private void createButtons(Composite parent) {
		Composite buttonsComposite = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		buttonsComposite.setLayout(layout);
		buttonsComposite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

		selectAll = new Button(buttonsComposite, SWT.PUSH);
		selectAll.setText(Messages.getString("WizardSelectCssStylePage.button.label.selectAll")); //$NON-NLS-1$
		selectAll.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] ch = stylesTable.getItems();
				for (int i = 0; i < ch.length; i++) {
					ch[i].setChecked(true);
				}
				refreshButtons();
			}
		});

		setButtonLayoutData(selectAll);

		deselectAll = new Button(buttonsComposite, SWT.PUSH);
		deselectAll.setText(Messages.getString("WizardSelectCssStylePage.button.label.deselectAll")); //$NON-NLS-1$
		deselectAll.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] ch = stylesTable.getItems();
				for (int i = 0; i < ch.length; i++) {
					ch[i].setChecked(false);
				}
				refreshButtons();
			}
		});
		setButtonLayoutData(deselectAll);
	}

	public void refresh() {
		String file = getFileName();

		if (file != null) {
			initiate();
		}
	}

	public void initiate() {
		styleMap.clear();
		styleNames.clear();
		unSupportedStyleNames.clear();
		fileName = null;

		TableItem[] ch = stylesTable.getItems();
		for (int i = 0; i < ch.length; i++) {
			ch[i].dispose();
		}

		ch = notificationsTable.getItems();
		for (int i = 0; i < ch.length; i++) {
			ch[i].dispose();
		}

		title.setText(""); //$NON-NLS-1$

		if (validateFileName()) {
			fileName = getFileName();
			title.setText(Messages.getString("WizardSelectCssStylePage.label.selectStylesFrom") //$NON-NLS-1$
					+ new File(this.fileName).getName()
					+ Messages.getString("WizardSelectCssStylePage.label.importToReportDesign")); //$NON-NLS-1$
			try {
				cssHandle = SessionHandleAdapter.getInstance().getReportDesignHandle().openCssStyleSheet(fileName);

				CssErrDialog cssErrorDialg = null;
				if (!cssHandle.getParserFatalErrors().isEmpty()) {
					cssErrorDialg = new CssErrDialog(this.getShell(), cssHandle.getParserFatalErrors(),
							CssErrDialog.FATAL_ERROR);
				} else if (!cssHandle.getParserErrors().isEmpty()) {
					cssErrorDialg = new CssErrDialog(this.getShell(), cssHandle.getParserErrors(), CssErrDialog.ERROR);

				} else if (!cssHandle.getParserWarnings().isEmpty()) {
					cssErrorDialg = new CssErrDialog(this.getShell(), cssHandle.getParserWarnings(),
							CssErrDialog.WARNING);
				}

				if (cssErrorDialg != null) {
					cssErrorDialg.open();
				}

				List availableStyles = null;
				if (theme != null) {
					availableStyles = new ArrayList(Arrays.asList(getPredefinedStyleNames(theme.getType())));
				}

				List themeStyleNames = getThemeStyleNames();

				Iterator styleIter = cssHandle.getStyleIterator();
				while (styleIter.hasNext()) {
					SharedStyleHandle styleHandle = (SharedStyleHandle) styleIter.next();

					if (theme != null) {
						if (availableStyles.contains(styleHandle.getName())
								&& !themeStyleNames.contains(styleHandle.getName())) {
							styleMap.put(styleHandle.getName(), styleHandle);
							styleNames.add(styleHandle.getName());
						} else {
							unSupportedStyleNames.add(styleHandle.getName()
									+ Messages.getString("WizardSelectCssStylePage.text.cannot.import.style")); //$NON-NLS-1$
						}
					} else {
						styleMap.put(styleHandle.getName(), styleHandle);
						styleNames.add(styleHandle.getName());
					}
				}

				List unSupportedStyles = cssHandle.getUnsupportedStyles();
				for (Iterator iter = unSupportedStyles.iterator(); iter.hasNext();) {
					String name = (String) iter.next();
					unSupportedStyleNames
							.add(name + Messages.getString("WizardSelectCssStylePage.text.cannot.import.style")); //$NON-NLS-1$
				}
			} catch (StyleSheetException e) {
				ExceptionHandler.handle(e);
			}

			TableItem item;
			for (int i = 0; i < styleNames.size(); i++) {
				String sn = (String) styleNames.get(i);
				item = new TableItem(stylesTable, SWT.NULL);
				item.setText(sn);
				item.setImage(ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ELEMENT_STYLE));
			}

			for (int i = 0; i < unSupportedStyleNames.size(); i++) {
				String sn = (String) unSupportedStyleNames.get(i);
				item = new TableItem(notificationsTable, SWT.NULL);
				item.setText(sn);
				item.setImage(ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_ELEMENT_STYLE));
			}

			refreshButtons();
		}
	}

	public CssStyleSheetHandle getCssHandle() {
		return cssHandle;
	}

	private void refreshButtons() {
		deselectAll.setEnabled(getStyleList().size() > 0);
	}

	public List getStyleList() {
		List sl = new ArrayList();

		TableItem[] ch = stylesTable.getItems();

		for (int i = 0; i < ch.length; i++) {
			if (ch[i].getChecked()) {
				SharedStyleHandle handle = (SharedStyleHandle) styleMap.get(ch[i].getText());
				sl.add(handle);
			}
		}

		return sl;

	}

	private String getFileName() {
		return fileNameField.getText();
	}

	private boolean validateFileName() {
		if ("".equals(getFileName())) //$NON-NLS-1$
		{
			setErrorMessage(null);
			setMessage(Messages.getString("WizardSelectCssStylePage.msg.FileNameEmpty")); //$NON-NLS-1$
			return false;
		}
		File file = new File(getFileName());
		if (!file.exists() || !file.isFile()) {
			setErrorMessage(Messages.getString("WizardSelectCssStylePage.errorMsg.FileNotExist")); //$NON-NLS-1$
			return false;
		}

		// if ( !getFileName( ).endsWith( ".css" ) ) //$NON-NLS-1$
		// {
		// setErrorMessage( null );
		// setMessage( "File type is wrong" ); //$NON-NLS-1$
		// return false;
		// }

		setErrorMessage(null);
		setMessage(null);
		return true;
	}

	private boolean checkExtensions(String fileExt[], String fileName) {
		for (int i = 0; i < fileExt.length; i++) {
			String ext = fileExt[i].substring(fileExt[i].lastIndexOf('.'));
			if (fileName.toLowerCase().endsWith(ext.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	private String[] getPredefinedStyleNames(String type) {
		List preStyles = null;
		if (type == null) {
			preStyles = DEUtil.getMetaDataDictionary().getPredefinedStyles();
		} else {
			preStyles = DEUtil.getMetaDataDictionary().getPredefinedStyles(type);
		}
		if (preStyles == null) {
			return new String[] {};
		}
		String[] names = new String[preStyles.size()];
		for (int i = 0; i < preStyles.size(); i++) {
			names[i] = ((IPredefinedStyle) preStyles.get(i)).getName();
		}
		Arrays.sort(names);
		return names;
	}

	private List getThemeStyleNames() {
		List styleNames = new ArrayList();
		if (theme != null) {
			getChildrenNameBySlotHandle(theme.getStyles(), styleNames);
		}
		return styleNames;
	}

	private void getChildrenNameBySlotHandle(SlotHandle slotHandle, List styleNames) {
		Iterator itor = slotHandle.iterator();
		while (itor.hasNext()) {
			Object obj = itor.next();
			if (obj instanceof DesignElementHandle) {
				DesignElementHandle eleHandle = (DesignElementHandle) obj;
				styleNames.add(eleHandle.getName());
			}
		}
	}

	public void setTheme(ReportItemThemeHandle theme) {
		this.theme = theme;
	}
}
