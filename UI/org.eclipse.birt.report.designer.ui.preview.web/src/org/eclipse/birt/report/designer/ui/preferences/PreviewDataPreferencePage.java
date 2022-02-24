/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.birt.report.viewer.utilities.WebViewer;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * PreviewDataPreferencePage
 */
public class PreviewDataPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	public static final int MAX_DATASET_ROW_DEFAULT = ViewerPlugin.DEFAULT_MAX_ROWS;

//	public static final int MAX_CUBE_ROW_LEVEL_MEMBER_DEFAULT = ViewerPlugin.DEFAULT_MAX_CUBEROWLEVELS;

//	public static final int MAX_CUBE_COLUMN_LEVEL_MEMBER_DEFAULT = ViewerPlugin.DEFAULT_MAX_CUBECOLUMNLEVELS;

	public static final int MAX_IN_MEMORY_CUBE_SIZE_DEFAULT = ViewerPlugin.DEFAULT_MAX_IN_MEMORY_CUBE_SIZE;

	public static final int DATASET_DISPLAY_ALL_ROW = 0;

	/** max Row preference name */
	public static final String PREVIEW_MAXROW = WebViewer.PREVIEW_MAXROW;

//	public static final String PREVIEW_MAX_ROW_LEVEL_MEMBER = WebViewer.PREVIEW_MAXCUBEROWLEVEL;

//	public static final String PREVIEW_MAX_COLUMN_LEVEL_MEMBER = WebViewer.PREVIEW_MAXCUBECOLUMNLEVEL;

	public static final String PREVIEW_MAX_IN_MEMORY_CUBE_SIZE = WebViewer.PREVIEW_MAXINMEMORYCUBESIZE;

	private transient DisplayNumberFieldEditor txtMaxDataSetRow;

//	private transient IntegerFieldEditor txtMaxRowLevelMember;

//	private transient IntegerFieldEditor txtMaxColumnLevelMember;

	private transient IntegerFieldEditor txtMaxInMemoryCubeSize;

	private transient Button noLimitBtn;

	protected Control createContents(Composite parent) {
		UIUtil.bindHelp(parent, IHelpContextIds.PREFERENCE_BIRT_PREVIEW_DATA_ID);

		Composite cmpTop = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, false);
		cmpTop.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		cmpTop.setLayoutData(gd);

		createDispalyRowLimitGroup(cmpTop);

//		txtMaxRowLevelMember = new IntegerFieldEditor( PREVIEW_MAX_ROW_LEVEL_MEMBER,
//				Messages.getString( "designer.preview.preference.resultset.maxrowlevelmember.description" ), cmpTop ); //$NON-NLS-1$ 
//		txtMaxRowLevelMember.setPage( this );
//		txtMaxRowLevelMember.setValidateStrategy( StringFieldEditor.VALIDATE_ON_KEY_STROKE );
//		txtMaxRowLevelMember.setEmptyStringAllowed( false );
//		txtMaxRowLevelMember.setPropertyChangeListener( new IPropertyChangeListener( ) {
//
//			public void propertyChange( PropertyChangeEvent event )
//			{
//				if ( event.getProperty( ).equals( FieldEditor.IS_VALID ) )
//					setValid( txtMaxRowLevelMember.isValid( ) );
//			}
//		} );
//		
//		txtMaxColumnLevelMember = new IntegerFieldEditor( PREVIEW_MAX_COLUMN_LEVEL_MEMBER,
//				Messages.getString( "designer.preview.preference.resultset.maxcolumnlevelmember.description" ), cmpTop ); //$NON-NLS-1$ 
//		txtMaxColumnLevelMember.setPage( this );
//		txtMaxColumnLevelMember.setValidateStrategy( StringFieldEditor.VALIDATE_ON_KEY_STROKE );
//		txtMaxColumnLevelMember.setEmptyStringAllowed( false );
//		txtMaxColumnLevelMember.setPropertyChangeListener( new IPropertyChangeListener( ) {
//
//			public void propertyChange( PropertyChangeEvent event )
//			{
//				if ( event.getProperty( ).equals( FieldEditor.IS_VALID ) )
//					setValid( txtMaxColumnLevelMember.isValid( ) );
//			}
//		} );

		txtMaxInMemoryCubeSize = new IntegerFieldEditor(PREVIEW_MAX_IN_MEMORY_CUBE_SIZE,
				Messages.getString("designer.preview.preference.resultset.maxinmemorycubesize.description"), cmpTop); //$NON-NLS-1$
		txtMaxInMemoryCubeSize.setPage(this);
		txtMaxInMemoryCubeSize.setValidateStrategy(StringFieldEditor.VALIDATE_ON_KEY_STROKE);
		txtMaxInMemoryCubeSize.setEmptyStringAllowed(false);
		txtMaxInMemoryCubeSize.setPropertyChangeListener(new IPropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(FieldEditor.IS_VALID))
					setValid(txtMaxInMemoryCubeSize.isValid());
			}
		});

		initControlValues();

		return cmpTop;
	}

	private void createDispalyRowLimitGroup(Composite cmpTop) {
		Group dispalyRowLimitGroup = new Group(cmpTop, SWT.NONE);
		dispalyRowLimitGroup.setText(
				Messages.getString("designer.preview.preference.resultset.groupTitle.DispalyRowLimitsSetting"));
		GridLayout layout = new GridLayout();
		layout.makeColumnsEqualWidth = true;
		layout.marginWidth = layout.marginHeight = 6;
		dispalyRowLimitGroup.setLayout(layout);
		GridData groupData = new GridData(GridData.FILL_HORIZONTAL);
		groupData.horizontalSpan = 2;
		dispalyRowLimitGroup.setLayoutData(groupData);

		Composite container = new Composite(dispalyRowLimitGroup, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		noLimitBtn = new Button(container, SWT.CHECK);
		GridData noLimitBtnData = new GridData(GridData.FILL_HORIZONTAL);
		noLimitBtnData.horizontalSpan = 2;
		noLimitBtn.setText(Messages.getString("designer.preview.preference.resultset.NoLimitOfDispalyRow"));
		noLimitBtn.setLayoutData(noLimitBtnData);
		noLimitBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				txtMaxDataSetRow.setEnabled(!noLimitBtn.getSelection(), noLimitBtn.getParent());

				if (!noLimitBtn.getSelection()) {
					txtMaxDataSetRow.setFocus();
				}
				if (txtMaxDataSetRow.checkState()) {
				}
			}

		});

		txtMaxDataSetRow = new DisplayNumberFieldEditor(PREVIEW_MAXROW,
				Messages.getString("designer.preview.preference.resultset.maxrow.description"), //$NON-NLS-1$
				container);
		txtMaxDataSetRow.setPage(this);
		txtMaxDataSetRow.setValidRange(1, Integer.MAX_VALUE);
		txtMaxDataSetRow.setValidateStrategy(StringFieldEditor.VALIDATE_ON_KEY_STROKE);
		txtMaxDataSetRow.setEmptyStringAllowed(false);
		txtMaxDataSetRow.setPropertyChangeListener(new IPropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(FieldEditor.IS_VALID))
					setValid(txtMaxDataSetRow.isValid());
			}
		});
	}

	private void initControlValues() {
		String defaultMaxRow = ViewerPlugin.getDefault().getPluginPreferences().getString(PREVIEW_MAXROW);

		if (defaultMaxRow == null || defaultMaxRow.trim().length() <= 0) {
			defaultMaxRow = String.valueOf(MAX_DATASET_ROW_DEFAULT);
		}

		boolean previewAllRows = String.valueOf(DATASET_DISPLAY_ALL_ROW).equals(defaultMaxRow.trim());
		noLimitBtn.setSelection(previewAllRows);
		txtMaxDataSetRow.setEnabled(!previewAllRows, noLimitBtn.getParent());

		txtMaxDataSetRow.setStringValue(previewAllRows ? "" : defaultMaxRow);

//		defaultMaxRow = ViewerPlugin.getDefault( )
//				.getPluginPreferences( )
//				.getString( PREVIEW_MAX_ROW_LEVEL_MEMBER );
//
//		if ( defaultMaxRow == null || defaultMaxRow.trim( ).length( ) <= 0 )
//		{
//			defaultMaxRow = String.valueOf( MAX_CUBE_ROW_LEVEL_MEMBER_DEFAULT );
//		}
//		txtMaxRowLevelMember.setStringValue( defaultMaxRow );
//
//		defaultMaxRow = ViewerPlugin.getDefault( )
//				.getPluginPreferences( )
//				.getString( PREVIEW_MAX_COLUMN_LEVEL_MEMBER );
//
//		if ( defaultMaxRow == null || defaultMaxRow.trim( ).length( ) <= 0 )
//		{
//			defaultMaxRow = String.valueOf( MAX_CUBE_COLUMN_LEVEL_MEMBER_DEFAULT );
//		}
//		txtMaxColumnLevelMember.setStringValue( defaultMaxRow );

		defaultMaxRow = ViewerPlugin.getDefault().getPluginPreferences().getString(PREVIEW_MAX_IN_MEMORY_CUBE_SIZE);

		if (defaultMaxRow == null || defaultMaxRow.trim().length() <= 0) {
			defaultMaxRow = String.valueOf(MAX_IN_MEMORY_CUBE_SIZE_DEFAULT);
		}
		txtMaxInMemoryCubeSize.setStringValue(defaultMaxRow);

	}

	public void init(IWorkbench workbench) {

	}

	protected void performDefaults() {
		txtMaxDataSetRow.setStringValue(String.valueOf(MAX_DATASET_ROW_DEFAULT));
//		txtMaxRowLevelMember.setStringValue( String.valueOf( MAX_CUBE_ROW_LEVEL_MEMBER_DEFAULT ) );
//		txtMaxColumnLevelMember.setStringValue( String.valueOf( MAX_CUBE_COLUMN_LEVEL_MEMBER_DEFAULT ) );
		txtMaxInMemoryCubeSize.setStringValue(String.valueOf(MAX_IN_MEMORY_CUBE_SIZE_DEFAULT));

		super.performDefaults();
	}

	public boolean performOk() {
		int maxRowValue = noLimitBtn.getSelection() ? DATASET_DISPLAY_ALL_ROW : txtMaxDataSetRow.getIntValue();
		ViewerPlugin.getDefault().getPluginPreferences().setValue(PREVIEW_MAXROW, maxRowValue);

//		ViewerPlugin.getDefault( )
//				.getPluginPreferences( )
//				.setValue( PREVIEW_MAX_ROW_LEVEL_MEMBER,
//						txtMaxRowLevelMember.getIntValue( ) );
//
//		ViewerPlugin.getDefault( )
//				.getPluginPreferences( )
//				.setValue( PREVIEW_MAX_COLUMN_LEVEL_MEMBER,
//						txtMaxColumnLevelMember.getIntValue( ) );

		ViewerPlugin.getDefault().getPluginPreferences().setValue(PREVIEW_MAX_IN_MEMORY_CUBE_SIZE,
				txtMaxInMemoryCubeSize.getIntValue());

		ViewerPlugin.getDefault().savePluginPreferences();

		return super.performOk();
	}

	class DisplayNumberFieldEditor extends IntegerFieldEditor {

		public DisplayNumberFieldEditor(String s1, String s2, Composite parent) {
			super(s1, s2, parent);
		}

		public boolean checkState() {
			if (noLimitBtn.getSelection()) {
				setValid(true);
				if (!super.checkState())
					clearErrorMessage();
				return true;
			} else {
				setValid(isValid());
			}
			return super.checkState();
		}
	};
}
