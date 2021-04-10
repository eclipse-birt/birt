/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelper;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelperProvider;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionConverter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil.ExpressionHelper;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionUtility;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.CubeACLExpressionProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.CubeExpressionProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.LinkToCubeExpressionProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.BuilderConstants;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.OlapUtil;
import org.eclipse.birt.report.designer.ui.dialogs.BaseTitleAreaDialog;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.FormatValueHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.RuleHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.elements.structures.LevelAttribute;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.birt.report.model.elements.interfaces.ILevelModel;
import org.eclipse.birt.report.model.elements.olap.Level;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.util.ULocale;

public class LevelPropertyDialog extends BaseTitleAreaDialog {

	private static final String DEFAULTVALUE_EDIT_LABEL = Messages
			.getString("LevelPropertyDialog.DefaultValue.Edit.Label"); //$NON-NLS-1$
	private static final String DEFAULTVALUE_EDIT_TITLE = Messages
			.getString("LevelPropertyDialog.DefaultValue.Edit.Title"); //$NON-NLS-1$
	private Composite dynamicArea;
	private DataSetHandle dataset;

	private IChoice[] getAvailableDataTypeChoices() {
		IChoice[] dataTypes = DEUtil.getMetaDataDictionary().getElement(ReportDesignConstants.LEVEL_ELEMENT)
				.getProperty(ILevelModel.DATA_TYPE_PROP).getAllowedChoices().getChoices();
		List choiceList = new ArrayList();
		for (int i = 0; i < dataTypes.length; i++) {
			choiceList.add(dataTypes[i]);
		}
		return (IChoice[]) choiceList.toArray(new IChoice[0]);
	}

	public String[] getDataTypeNames() {
		IChoice[] choices = getAvailableDataTypeChoices();
		if (choices == null)
			return new String[0];

		String[] names = new String[choices.length];
		for (int i = 0; i < choices.length; i++) {
			names[i] = choices[i].getName();
		}
		return names;
	}

	public String getDataTypeDisplayName(String name) {
		return ChoiceSetFactory.getDisplayNameFromChoiceSet(name,
				DEUtil.getMetaDataDictionary().getElement(ReportDesignConstants.LEVEL_ELEMENT)
						.getProperty(ILevelModel.DATA_TYPE_PROP).getAllowedChoices());
	}

	private String[] getDataTypeDisplayNames() {
		IChoice[] choices = getAvailableDataTypeChoices();
		if (choices == null)
			return new String[0];

		String[] displayNames = new String[choices.length];
		for (int i = 0; i < choices.length; i++) {
			displayNames[i] = choices[i].getDisplayName();
		}
		return displayNames;
	}

	private boolean isNew;

	public LevelPropertyDialog(boolean isNew) {
		super(UIUtil.getDefaultShell());
		setShellStyle(getShellStyle() | SWT.RESIZE | SWT.MAX);
		this.isNew = isNew;
	}

	protected Control createDialogArea(Composite parent) {
		UIUtil.bindHelp(parent, IHelpContextIds.LEVEL_PROPERTY_DIALOG);
		getShell().setText(Messages.getString("LevelPropertyDialog.Shell.Title")); //$NON-NLS-1$
		if (isNew)
			this.setTitle(Messages.getString("LevelPropertyDialog.Title.Add")); //$NON-NLS-1$
		else
			this.setTitle(Messages.getString("LevelPropertyDialog.Title.Edit")); //$NON-NLS-1$
		this.setMessage(Messages.getString("LevelPropertyDialog.Message")); //$NON-NLS-1$

		Composite area = (Composite) super.createDialogArea(parent);

		Composite contents = new Composite(area, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 20;
		contents.setLayout(layout);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.widthHint = convertWidthInCharsToPixels(80);
		data.heightHint = 400;
		contents.setLayoutData(data);

		createChoiceArea(contents);

		dynamicArea = createDynamicArea(contents);
		staticArea = createStaticArea(contents);

		WidgetUtil.createGridPlaceholder(contents, 1, true);

		initLevelDialog();

		parent.layout();

		return contents;
	}

	private void initLevelDialog() {
		if (input != null) {
			if (input.getLevelType() == null) {
				try {
					input.setLevelType(DesignChoiceConstants.LEVEL_TYPE_DYNAMIC);
				} catch (SemanticException e) {
					ExceptionUtil.handle(e);
				}
			}

			refreshDynamicViewer();
			dataset = OlapUtil.getHierarchyDataset((TabularHierarchyHandle) input.getContainer());
			if (dataset != null)
				attributeItems = OlapUtil.getDataFieldNames(dataset);
			resetEditorItems();

			if (input.getName() != null)
				nameText.setText(input.getName());
			dynamicDataTypeCombo.setItems(getDataTypeDisplayNames());
			dynamicDataTypeCombo.setText(getDataTypeDisplayName(input.getDataType()));

			fieldCombo.setItems(OlapUtil.getDataFieldDisplayNames(dataset));
			if (input.getColumnName() != null) {
				try {
					fieldCombo.setText(
							OlapUtil.getDataFieldDisplayName(OlapUtil.getDataField(dataset, input.getColumnName())));
				} catch (Exception e) {
					fieldCombo.select(0);
				}
			} else
				fieldCombo.select(0);

			displayKeyCombo.setItems(OlapUtil.getDataFieldNames(dataset));
			displayKeyCombo.add(Messages.getString("LevelPropertyDialog.None"), 0); //$NON-NLS-1$

			ExpressionButtonUtil.initExpressionButtonControl(displayKeyCombo, input,
					TabularLevelHandle.DISPLAY_COLUMN_NAME_PROP);

			if (displayKeyCombo.getText().trim().length() == 0) {
				if (displayKeyCombo.getItemCount() > 0)
					displayKeyCombo.select(0);
			}

			staticDataTypeCombo.setItems(getDataTypeDisplayNames());
			staticNameText.setText(input.getName());
			staticDataTypeCombo.setText(getDataTypeDisplayName(input.getDataType()));
			refreshStaticViewer();
			// dynamicViewer.setInput( dynamicAttributes );

			if (input.getLevelType().equals(DesignChoiceConstants.LEVEL_TYPE_DYNAMIC)) {
				dynamicButton.setSelection(true);
				updateButtonStatus(dynamicButton);
				updateFormatHelper(dynamicFormatHelper, dynamicDataTypeCombo);
			} else {
				staticButton.setSelection(true);
				updateButtonStatus(staticButton);
				updateFormatHelper(staticFormatHelper, staticDataTypeCombo);
			}
		}
	}

	private void refreshDynamicViewer() {
		Iterator attrIter = input.attributesIterator();
		final List attrList = new LinkedList();
		while (attrIter.hasNext()) {
			attrList.add(attrIter.next());
		}
		dynamicViewer.setInput(attrList);
	}

	private void refreshStaticViewer() {
		Iterator valuesIter = input.staticValuesIterator();
		final List valuesList = new LinkedList();
		while (valuesIter.hasNext()) {
			valuesList.add(valuesIter.next());
		}

		staticViewer.setInput(valuesList);
		defaultValueViewer.setInput(input);
		checkStaticViewerButtonStatus();

	}

	protected void okPressed() {
		IDialogHelper formatHelper = null;
		if (dynamicButton.getSelection()) {
			try {
				input.setLevelType(DesignChoiceConstants.LEVEL_TYPE_DYNAMIC);
				if (nameText.getText() != null && !nameText.getText().trim().equals("")) //$NON-NLS-1$
				{
					input.setName(nameText.getText());
				}
				if (fieldCombo.getText() != null) {
					input.setColumnName(OlapUtil.getDataFieldNames(dataset)[fieldCombo.getSelectionIndex()]);
				}
				if (displayKeyCombo.getText().trim().length() > 0
						&& !displayKeyCombo.getText().equals(Messages.getString("LevelPropertyDialog.None"))) //$NON-NLS-1$
				{
					ExpressionButtonUtil.saveExpressionButtonControl(displayKeyCombo, input,
							TabularLevelHandle.DISPLAY_COLUMN_NAME_PROP);
				} else
					input.setDisplayColumnName(null);
				if (dynamicDataTypeCombo.getText() != null) {
					input.setDataType(getDataTypeNames()[dynamicDataTypeCombo.getSelectionIndex()]);
				}
				input.getPropertyHandle(ILevelModel.STATIC_VALUES_PROP).clearValue();
			} catch (SemanticException e) {
				ExceptionUtil.handle(e);
				return;
			}
			if (dynamicLevelHelper != null) {
				try {
					dynamicLevelHelper.validate();
					input.setExpressionProperty(LevelHandle.ACL_EXPRESSION_PROP,
							(Expression) dynamicLevelHelper.getProperty(BuilderConstants.SECURITY_EXPRESSION_PROPERTY));
				} catch (SemanticException e) {
					ExceptionUtil.handle(e);
				}
			}
			if (dynamicMemberHelper != null) {
				try {
					dynamicMemberHelper.validate();
					input.setExpressionProperty(LevelHandle.MEMBER_ACL_EXPRESSION_PROP, (Expression) dynamicMemberHelper
							.getProperty(BuilderConstants.SECURITY_EXPRESSION_PROPERTY));
				} catch (SemanticException e) {
					ExceptionUtil.handle(e);
				}
			}
			if (dynamicAlignmentHelper != null) {
				try {
					input.setAlignment((String) dynamicAlignmentHelper.getProperty(BuilderConstants.ALIGNMENT_VALUE));
				} catch (SemanticException e) {
					ExceptionUtil.handle(e);
				}
			}
			formatHelper = dynamicFormatHelper;
		} else if (staticButton.getSelection()) {
			try {
				input.setLevelType(DesignChoiceConstants.LEVEL_TYPE_MIRRORED);
				if (staticNameText.getText() != null && !staticNameText.getText().trim().equals("")) //$NON-NLS-1$
				{
					input.setName(staticNameText.getText());
				}
				input.setColumnName(null);
				input.setDisplayColumnName(null);
				if (staticDataTypeCombo.getText() != null) {
					input.setDataType(getDataTypeNames()[staticDataTypeCombo.getSelectionIndex()]);
				}
				input.getPropertyHandle(ILevelModel.ATTRIBUTES_PROP).clearValue();
			} catch (SemanticException e) {
				ExceptionUtil.handle(e);
				return;
			}
			if (staticLevelHelper != null) {
				try {
					staticLevelHelper.validate();
					input.setExpressionProperty(LevelHandle.ACL_EXPRESSION_PROP,
							(Expression) staticLevelHelper.getProperty(BuilderConstants.SECURITY_EXPRESSION_PROPERTY));
				} catch (SemanticException e) {
					ExceptionUtil.handle(e);
				}
			}
			if (staticMemberHelper != null) {
				try {
					staticMemberHelper.validate();
					input.setExpressionProperty(LevelHandle.MEMBER_ACL_EXPRESSION_PROP,
							(Expression) staticMemberHelper.getProperty(BuilderConstants.SECURITY_EXPRESSION_PROPERTY));
				} catch (SemanticException e) {
					ExceptionUtil.handle(e);
				}
			}
			if (staticAlignmentHelper != null) {
				try {
					input.setAlignment((String) staticAlignmentHelper.getProperty(BuilderConstants.ALIGNMENT_VALUE));
				} catch (SemanticException e) {
					ExceptionUtil.handle(e);
				}
			}
			ActionHandle handle = getActionHandle();
			if (handle != null) {
				try {
					handle.setToolTip(null);
					handle.setExpressionProperty(Action.URI_MEMBER, null);
					handle.setTargetBookmark(null);
					handle.setTargetBookmarkType(null);
					handle.setTargetWindow(null);
					handle.setTargetFileType(null);
					handle.setReportName(null);
					handle.setFormatType(null);
					handle.getMember(Action.PARAM_BINDINGS_MEMBER).setValue(null);
					input.setProperty(LevelHandle.ACTION_PROP, null);
				} catch (SemanticException e) {
					ExceptionHandler.handle(e);
				}
			}
			formatHelper = staticFormatHelper;
		}
		if (formatHelper != null
				&& formatHelper.getProperty(BuilderConstants.FORMAT_VALUE_RESULT) instanceof Object[]) {
			Object[] formatValue = (Object[]) formatHelper.getProperty(BuilderConstants.FORMAT_VALUE_RESULT);
			Object value = input.getProperty(Level.FORMAT_PROP);
			try {
				if (value == null) {
					FormatValue formatValueToSet = new FormatValue();
					formatValueToSet.setCategory((String) formatValue[0]);
					formatValueToSet.setPattern((String) formatValue[1]);
					formatValueToSet.setLocale((ULocale) formatValue[2]);
					input.setProperty(Level.FORMAT_PROP, formatValueToSet);
				} else {
					PropertyHandle propHandle = input.getPropertyHandle(Level.FORMAT_PROP);
					FormatValue formatValueToSet = (FormatValue) value;
					FormatValueHandle formatHandle = (FormatValueHandle) formatValueToSet.getHandle(propHandle);
					formatHandle.setCategory((String) formatValue[0]);
					formatHandle.setPattern((String) formatValue[1]);
					formatHandle.setLocale((ULocale) formatValue[2]);
				}
			} catch (SemanticException e) {
				ExceptionHandler.handle(e);
			}
		}
		super.okPressed();
	}

	private ActionHandle getActionHandle() {
		return DEUtil.getActionHandle(input);
	}

	private static final String dummyChoice = "dummy"; //$NON-NLS-1$

	private IStructuredContentProvider contentProvider = new IStructuredContentProvider() {

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		public Object[] getElements(Object input) {
			if (input instanceof List) {
				List list = (List) input;
				return list.toArray();
			}
			return new Object[0];
		}
	};

	private IStructuredContentProvider defaultValueContentProvider = new IStructuredContentProvider() {

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		public Object[] getElements(Object input) {
			if (input instanceof TabularLevelHandle) {
				List list = new ArrayList();
				if (((TabularLevelHandle) input).getDefaultValue() != null)
					list.add(((TabularLevelHandle) input).getDefaultValue());
				else
					list.add(dummyChoice);
				return list.toArray();
			}
			return new Object[0];
		}
	};

	private ITableLabelProvider staticLabelProvider = new ITableLabelProvider() {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (columnIndex == 1) {
				if (element == dummyChoice)
					return Messages.getString("LevelPropertyDialog.MSG.Static.CreateNew"); //$NON-NLS-1$
				else {
					if (element instanceof RuleHandle) {

						return ((RuleHandle) element).getDisplayExpression();

					}
					return ""; //$NON-NLS-1$
				}
			} else if (columnIndex == 2) {
				if (element == dummyChoice)
					return ""; //$NON-NLS-1$
				else {
					if (element instanceof RuleHandle) {

						return ((RuleHandle) element).getRuleExpression();

					}
					return ""; //$NON-NLS-1$
				}
			} else
				return ""; //$NON-NLS-1$
		}

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}

	};

	private ITableLabelProvider defaultValueLabelProvider = new ITableLabelProvider() {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (columnIndex == 1) {
				if (element == dummyChoice)
					return Messages.getString("LevelPropertyDialog.MSG.DefaultValue"); //$NON-NLS-1$
				else {
					if (element instanceof String) {

						return (String) element;

					}
					return ""; //$NON-NLS-1$
				}
			} else if (columnIndex == 2) {
				return Messages.getString("LevelPropertyDialog.MSG.Tooltip"); //$NON-NLS-1$
			} else
				return ""; //$NON-NLS-1$
		}

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}

	};

	private ITableLabelProvider dynamicLabelProvider = new ITableLabelProvider() {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (columnIndex == 1) {
				if (element == dummyChoice)
					return Messages.getString("LevelPropertyDialog.MSG.Dynamic.CreateNew"); //$NON-NLS-1$
				else {
					if (element instanceof LevelAttributeHandle) {

						return ((LevelAttributeHandle) element).getName();

					}
				}
			}
			return ""; //$NON-NLS-1$
		}

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}

	};

	// private ICellModifier dynamicCellModifier = new ICellModifier( ) {
	//
	// public boolean canModify( Object element, String property )
	// {
	// return true;
	// }
	//
	// public Object getValue( Object element, String property )
	// {
	// if ( element instanceof LevelAttributeHandle )
	// {
	// LevelAttributeHandle handle = (LevelAttributeHandle) element;
	// resetEditorItems( handle.getName( ) );
	// for ( int i = 0; i < editor.getItems( ).length; i++ )
	// if ( handle.getName( ).equals( editor.getItems( )[i] ) )
	// return Integer.valueOf( i );
	// }
	// if ( element instanceof String )
	// {
	// resetEditorItems( );
	// }
	// return Integer.valueOf( -1 );
	// }
	//
	// public void modify( Object element, String property, Object value )
	// {
	// if ( element instanceof Item )
	// element = ( (Item) element ).getData( );
	//
	// if ( ( (Integer) value ).intValue( ) > -1
	// && ( (Integer) value ).intValue( ) < editor.getItems( ).length )
	// {
	// if ( element instanceof LevelAttributeHandle )
	// {
	// LevelAttributeHandle handle = (LevelAttributeHandle) element;
	// try
	// {
	// handle.setName( editor.getItems( )[( (Integer) value ).intValue( )] );
	// if ( dataset != null )
	// {
	// ResultSetColumnHandle dataField = OlapUtil.getDataField( dataset,
	// handle.getName( ) );
	// handle.setDataType( dataField.getDataType( ) );
	// }
	// }
	// catch ( SemanticException e )
	// {
	// ExceptionUtil.handle( e );
	// }
	// }
	// else
	// {
	// LevelAttribute attribute = StructureFactory.createLevelAttribute( );
	// attribute.setName( editor.getItems( )[( (Integer) value ).intValue( )] );
	// if ( dataset != null )
	// {
	// ResultSetColumnHandle dataField = OlapUtil.getDataField( dataset,
	// attribute.getName( ) );
	// attribute.setDataType( dataField.getDataType( ) );
	// }
	// try
	// {
	// input.getPropertyHandle( ILevelModel.ATTRIBUTES_PROP )
	// .addItem( attribute );
	// }
	// catch ( SemanticException e )
	// {
	// ExceptionUtil.handle( e );
	// }
	// }
	// refreshDynamicViewer( );
	// }
	// }
	// };

	private ICellModifier defaultValueCellModifier = new ICellModifier() {

		public boolean canModify(Object element, String property) {
			if (property.equals(Prop_DefaultValue)) {
				return true;
			}
			return false;
		}

		public Object getValue(Object element, String property) {
			if (property.equals(Prop_DefaultValue)) {
				if (element != dummyChoice) {
					if (element instanceof String)
						return (String) element;
				}

			}
			return ""; //$NON-NLS-1$
		}

		public void modify(Object element, String property, Object value) {
			if (property.equals(Prop_DefaultValue)) // $NON-NLS-1$
			{
				try {
					if (!(value.toString().trim().equals("") || value.equals(dummyChoice))) //$NON-NLS-1$
						input.setDefaultValue(value.toString());
					else
						input.setDefaultValue(null);
				} catch (SemanticException e) {
					ExceptionUtil.handle(e);
				}
			}
			refreshStaticViewer();
		}
	};

	private int dynamicSelectIndex;

	protected Composite createDynamicArea(Composite parent) {
		Composite contents = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.numColumns = 2;
		contents.setLayout(layout);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		contents.setLayoutData(data);

		Group groupGroup = new Group(contents, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 3;
		groupGroup.setLayout(layout);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		groupGroup.setLayoutData(gd);

		Label nameLabel = new Label(groupGroup, SWT.NONE);
		nameLabel.setText(Messages.getString("LevelPropertyDialog.Name")); //$NON-NLS-1$
		nameText = new Text(groupGroup, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		nameText.setLayoutData(gd);
		nameText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				checkOkButtonStatus();
			}

		});

		Label fieldLabel = new Label(groupGroup, SWT.NONE);
		fieldLabel.setText(Messages.getString("LevelPropertyDialog.KeyField")); //$NON-NLS-1$
		fieldCombo = new Combo(groupGroup, SWT.BORDER | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		fieldCombo.setLayoutData(gd);
		fieldCombo.setVisibleItemCount(30);
		fieldCombo.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				Iterator attrIter = input.attributesIterator();
				while (attrIter.hasNext()) {
					LevelAttributeHandle handle = (LevelAttributeHandle) attrIter.next();
					if (handle != null && fieldCombo != null && fieldCombo.getText() != null
							&& fieldCombo.getText().equals(handle.getName())) {
						try {
							handle.drop();
						} catch (PropertyValueException e1) {
							ExceptionHandler.handle(e1);
						}
					}
				}
				refreshDynamicViewer();
				checkOkButtonStatus();
			}

		});

		Label displayKeyLabel = new Label(groupGroup, SWT.NONE);
		displayKeyLabel.setText(Messages.getString("LevelPropertyDialog.DisplayField")); //$NON-NLS-1$
		displayKeyCombo = new Combo(groupGroup, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		displayKeyCombo.setLayoutData(gd);
		displayKeyCombo.setVisibleItemCount(30);
		displayKeyCombo.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (displayKeyCombo.getSelectionIndex() > 0) {
					IExpressionConverter converter = ExpressionButtonUtil
							.getCurrentExpressionConverter(displayKeyCombo);
					if (converter != null) {
						String value = ExpressionUtility.getResultSetColumnExpression(displayKeyCombo.getText(),
								converter);
						if (value != null)
							displayKeyCombo.setText(value);
					}
				}
			}
		});

		ExpressionHelper displayKeyHelper = new ExpressionHelper() {

			public String getExpression() {
				if (control instanceof Combo) {
					String text = ((Combo) control).getText();
					if (!Messages.getString("LevelPropertyDialog.None") //$NON-NLS-1$
							.equals(text))
						return text;
				}
				return ""; //$NON-NLS-1$
			}

			public void setExpression(String expression) {
				if (control instanceof Combo) {
					if ("".equals(DEUtil.resolveNull(expression))) //$NON-NLS-1$
						((Combo) control).setText(Messages.getString("LevelPropertyDialog.None")); //$NON-NLS-1$
					else
						((Combo) control).setText(DEUtil.resolveNull(expression));
				}

			}
		};

		ExpressionButtonUtil.createExpressionButton(groupGroup, displayKeyCombo, new CubeExpressionProvider(input),
				input, displayKeyHelper);

		new Label(groupGroup, SWT.NONE).setText(Messages.getString("LevelPropertyDialog.DataType")); //$NON-NLS-1$
		dynamicDataTypeCombo = new Combo(groupGroup, SWT.BORDER | SWT.READ_ONLY);
		dynamicDataTypeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		dynamicDataTypeCombo.setVisibleItemCount(30);
		dynamicDataTypeCombo.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				updateFormatHelper(dynamicFormatHelper, dynamicDataTypeCombo);
				checkOkButtonStatus();
			}

		});
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		dynamicDataTypeCombo.setLayoutData(gd);

		dynamicLevelHelper = createLevelSecurityPart(groupGroup);
		dynamicMemberHelper = createMemberSecurityPart(groupGroup);
		createHyperLinkPart(groupGroup);
		dynamicFormatHelper = createFormatPart(groupGroup);
		dynamicAlignmentHelper = createAlignmentPart(groupGroup);

		dynamicTable = new Table(contents,
				SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER | SWT.VERTICAL | SWT.HORIZONTAL);
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 150;
		gd.verticalSpan = 3;
		dynamicTable.setLayoutData(gd);
		dynamicTable.setLinesVisible(true);
		dynamicTable.setHeaderVisible(true);

		dynamicTable.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.DEL) {
					deleteDynamicAttribute();
				}
			}
		});

		dynamicTable.addMouseListener(new MouseAdapter() {

			public void mouseDoubleClick(MouseEvent e) {
				handleDynamicTableEditEvent();
			}
		});

		dynamicViewer = new TableViewer(dynamicTable);
		String[] columns = new String[] { " ", Messages.getString("LevelPropertyDialog.Label.Attribute") //$NON-NLS-1$ //$NON-NLS-2$
		};

		TableColumn column = new TableColumn(dynamicTable, SWT.LEFT);
		column.setText(columns[0]);
		column.setWidth(15);

		TableColumn column1 = new TableColumn(dynamicTable, SWT.LEFT);
		column1.setResizable(columns[1] != null);
		if (columns[1] != null) {
			column1.setText(columns[1]);
		}
		column1.setWidth(230);

		dynamicViewer.setColumnProperties(new String[] { "", prop_Attribute //$NON-NLS-1$
		});

		dynamicViewer.setContentProvider(contentProvider);
		dynamicViewer.setLabelProvider(dynamicLabelProvider);

		Button dynamicAddButton = new Button(contents, SWT.PUSH);
		dynamicAddButton.setText(Messages.getString("LevelPropertyDialog.DynamicTable.Button.Add")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_VERTICAL);
		gd.verticalAlignment = SWT.END;
		gd.horizontalAlignment = SWT.FILL;
		dynamicAddButton.setLayoutData(gd);
		dynamicAddButton.setEnabled(true);

		dynamicAddButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				resetEditorItems();
				LevelDynamicAttributeDialog dialog = new LevelDynamicAttributeDialog(
						Messages.getString("LevelPropertyDialog.DynamicAttributeDialog.Title.New")); //$NON-NLS-1$
				dialog.setInput(dynamicMemeberItems);
				if (dialog.open() == Window.OK) {
					LevelAttribute attribute = StructureFactory.createLevelAttribute();
					attribute.setName((String) dialog.getResult());
					if (dataset != null) {
						ResultSetColumnHandle dataField = OlapUtil.getDataField(dataset, attribute.getName());
						attribute.setDataType(dataField.getDataType());
					}
					try {
						input.getPropertyHandle(ILevelModel.ATTRIBUTES_PROP).addItem(attribute);
					} catch (SemanticException e1) {
						ExceptionUtil.handle(e1);
					}
					refreshDynamicViewer();
				}
				if (dynamicTable.getItemCount() > 0) {
					dynamicSelectIndex = dynamicTable.getItemCount() - 1;
					dynamicTable.select(dynamicSelectIndex);
					checkDynamicViewerButtonStatus();
				}
			}
		});

		dynamicEditButton = new Button(contents, SWT.PUSH);
		dynamicEditButton.setText(Messages.getString("LevelPropertyDialog.DynamicTable.Button.Edit")); //$NON-NLS-1$
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		dynamicEditButton.setLayoutData(gd);
		dynamicEditButton.setEnabled(false);

		dynamicEditButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleDynamicTableEditEvent();
			}
		});

		dynamicRemoveButton = new Button(contents, SWT.PUSH);
		dynamicRemoveButton.setText(Messages.getString("LevelPropertyDialog.DynamicTable.Button.Remove")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_VERTICAL);
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.BEGINNING;
		dynamicRemoveButton.setLayoutData(gd);
		dynamicRemoveButton.setEnabled(false);
		dynamicRemoveButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				deleteDynamicAttribute();
			}
		});
		dynamicTable.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				checkDynamicViewerButtonStatus();
			}
		});

		return contents;
	}

	private IDialogHelper createLevelSecurityPart(Composite parent) {
		Object[] helperProviders = ElementAdapterManager.getAdapters(input, IDialogHelperProvider.class);
		if (helperProviders != null) {
			for (int i = 0; i < helperProviders.length; i++) {
				IDialogHelperProvider helperProvider = (IDialogHelperProvider) helperProviders[i];
				if (helperProvider != null) {
					final IDialogHelper levelHelper = helperProvider.createHelper(this,
							BuilderConstants.SECURITY_HELPER_KEY);
					if (levelHelper != null) {
						levelHelper.setProperty(BuilderConstants.SECURITY_EXPRESSION_LABEL,
								Messages.getString("LevelPropertyDialog.Access.Control.List.Expression")); //$NON-NLS-1$
						levelHelper.setProperty(BuilderConstants.SECURITY_EXPRESSION_CONTEXT, input);
						levelHelper.setProperty(BuilderConstants.SECURITY_EXPRESSION_PROVIDER,
								new CubeACLExpressionProvider(input));
						levelHelper.setProperty(BuilderConstants.SECURITY_EXPRESSION_PROPERTY,
								input.getACLExpression());
						levelHelper.createContent(parent);
						levelHelper.addListener(SWT.Modify, new Listener() {

							public void handleEvent(Event event) {
								levelHelper.update(false);
							}
						});
						levelHelper.update(true);
						return levelHelper;
					}
				}
			}
		}
		return null;
	}

	private IDialogHelper createMemberSecurityPart(Composite parent) {
		Object[] helperProviders = ElementAdapterManager.getAdapters(input, IDialogHelperProvider.class);
		if (helperProviders != null) {
			for (int i = 0; i < helperProviders.length; i++) {
				IDialogHelperProvider helperProvider = (IDialogHelperProvider) helperProviders[i];
				if (helperProvider != null) {
					final IDialogHelper memberHelper = helperProvider.createHelper(this,
							BuilderConstants.SECURITY_HELPER_KEY);
					if (memberHelper != null) {
						memberHelper.setProperty(BuilderConstants.SECURITY_EXPRESSION_LABEL,
								Messages.getString("LevelPropertyDialog.Member.Access.Control.List.Expression")); //$NON-NLS-1$
						memberHelper.setProperty(BuilderConstants.SECURITY_EXPRESSION_CONTEXT, input);
						memberHelper.setProperty(BuilderConstants.SECURITY_EXPRESSION_PROVIDER,
								new CubeExpressionProvider(input));
						memberHelper.setProperty(BuilderConstants.SECURITY_EXPRESSION_PROPERTY,
								input.getMemberACLExpression());
						memberHelper.createContent(parent);
						memberHelper.addListener(SWT.Modify, new Listener() {

							public void handleEvent(Event event) {
								memberHelper.update(false);
							}
						});
						memberHelper.update(true);
						return memberHelper;
					}
				}
			}
		}
		return null;
	}

	private IDialogHelper createHyperLinkPart(Composite parent) {
		Object[] helperProviders = ElementAdapterManager.getAdapters(input, IDialogHelperProvider.class);
		if (helperProviders != null) {
			for (int i = 0; i < helperProviders.length; i++) {
				IDialogHelperProvider helperProvider = (IDialogHelperProvider) helperProviders[i];
				if (helperProvider != null) {
					final IDialogHelper hyperLinkHelper = helperProvider.createHelper(this,
							BuilderConstants.HYPERLINK_HELPER_KEY);
					if (hyperLinkHelper != null) {
						hyperLinkHelper.setProperty(BuilderConstants.HYPERLINK_LABEL,
								Messages.getString("LevelPropertyDialog.Label.LinkTo")); //$NON-NLS-1$
						hyperLinkHelper.setProperty(BuilderConstants.HYPERLINK_BUTTON_TEXT,
								Messages.getString("LevelPropertyDialog.Button.Text.Edit")); //$NON-NLS-1$
						hyperLinkHelper.setProperty(BuilderConstants.HYPERLINK_REPORT_ITEM_HANDLE, input);
						hyperLinkHelper.setProperty(BuilderConstants.HYPERLINK_REPORT_ITEM_PROVIDER,
								new LinkToCubeExpressionProvider(input));
						hyperLinkHelper.createContent(parent);
						hyperLinkHelper.addListener(SWT.Modify, new Listener() {

							public void handleEvent(Event event) {
								hyperLinkHelper.update(false);
							}
						});
						hyperLinkHelper.update(true);
						return hyperLinkHelper;
					}
				}
			}
		}
		return null;
	}

	private IDialogHelper createFormatPart(Composite parent) {
		Object[] helperProviders = ElementAdapterManager.getAdapters(input, IDialogHelperProvider.class);
		if (helperProviders != null) {
			for (int i = 0; i < helperProviders.length; i++) {
				IDialogHelperProvider helperProvider = (IDialogHelperProvider) helperProviders[i];
				if (helperProvider != null) {
					IDialogHelper formatHelper = helperProvider.createHelper(this, BuilderConstants.FORMAT_HELPER_KEY);
					if (formatHelper != null) {
						formatHelper.setProperty(BuilderConstants.FORMAT_LABEL,
								Messages.getString("LevelPropertyDialog.Label.Format")); //$NON-NLS-1$
						formatHelper.setProperty(BuilderConstants.FORMAT_BUTTON_TEXT,
								Messages.getString("LevelPropertyDialog.Button.Format.Edit")); //$NON-NLS-1$
						PropertyHandle propHandle = input.getPropertyHandle(Level.FORMAT_PROP);
						if (input.getProperty(Level.FORMAT_PROP) != null) {
							Object value = input.getProperty(Level.FORMAT_PROP);
							FormatValue formatValueToSet = (FormatValue) value;
							FormatValueHandle formatHandle = (FormatValueHandle) formatValueToSet.getHandle(propHandle);
							formatHelper.setProperty(BuilderConstants.FORMAT_VALUE, formatHandle);
						}
						formatHelper.createContent(parent);
						formatHelper.update(true);
						return formatHelper;
					}
				}
			}
		}
		return null;
	}

	private IDialogHelper createAlignmentPart(Composite parent) {
		Object[] helperProviders = ElementAdapterManager.getAdapters(input, IDialogHelperProvider.class);
		if (helperProviders != null) {
			for (int i = 0; i < helperProviders.length; i++) {
				IDialogHelperProvider helperProvider = (IDialogHelperProvider) helperProviders[i];
				if (helperProvider != null) {
					IDialogHelper alignmentHelper = helperProvider.createHelper(this,
							BuilderConstants.ALIGNMENT_HELPER_KEY);
					if (alignmentHelper != null) {
						alignmentHelper.setProperty(BuilderConstants.ALIGNMENT_LABEL,
								Messages.getString("LevelPropertyDialog.Label.Alignment")); //$NON-NLS-1$
						if (input.getAlignment() != null) {
							alignmentHelper.setProperty(BuilderConstants.ALIGNMENT_VALUE, input.getAlignment());
						} else if (isNew && input.getDataType() != null) {
							if (isNumber(input.getDataType())) {
								alignmentHelper.setProperty(BuilderConstants.ALIGNMENT_VALUE,
										DesignChoiceConstants.TEXT_ALIGN_RIGHT);
							} else {
								alignmentHelper.setProperty(BuilderConstants.ALIGNMENT_VALUE,
										DesignChoiceConstants.TEXT_ALIGN_LEFT);
							}
						}

						alignmentHelper.createContent(parent);
						alignmentHelper.update(true);
						return alignmentHelper;
					}
				}
			}
		}
		return null;
	}

	private boolean isNumber(String dataType) {
		return (DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL.equals(dataType)
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT.equals(dataType)
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER.equals(dataType));
	}

	String[] attributeItems = new String[0];

	private void resetEditorItems() {
		resetEditorItems(null);
	}

	private String[] dynamicMemeberItems;

	private void resetEditorItems(String name) {
		List list = new ArrayList();
		list.addAll(Arrays.asList(attributeItems));

		Iterator attrIter = input.attributesIterator();
		while (attrIter.hasNext()) {
			LevelAttributeHandle handle = (LevelAttributeHandle) attrIter.next();
			list.remove(handle.getName());
		}

		list.remove(fieldCombo.getText());
		if (name != null && !list.contains(name)) {
			list.add(0, name);
		}
		String[] temps = new String[list.size()];
		list.toArray(temps);
		dynamicMemeberItems = temps;
	}

	protected void handleDynamicDelEvent() {
		if (dynamicViewer.getSelection() != null && dynamicViewer.getSelection() instanceof StructuredSelection) {
			Object element = ((StructuredSelection) dynamicViewer.getSelection()).getFirstElement();
			if (element instanceof LevelAttributeHandle) {
				try {
					((LevelAttributeHandle) element).drop();
				} catch (PropertyValueException e) {
					ExceptionUtil.handle(e);
				}
			}
		}

	}

	protected void handleStaticDelEvent() {
		if (staticViewer.getSelection() != null && staticViewer.getSelection() instanceof StructuredSelection) {
			Object element = ((StructuredSelection) staticViewer.getSelection()).getFirstElement();
			if (element instanceof RuleHandle) {
				try {
					((RuleHandle) element).drop();
				} catch (PropertyValueException e) {
					ExceptionUtil.handle(e);
				}
			}
		}

	}

	private void createChoiceArea(Composite parent) {
		Composite contents = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.numColumns = 2;
		contents.setLayout(layout);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		contents.setLayoutData(data);
		dynamicButton = new Button(contents, SWT.RADIO);
		dynamicButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				try {
					input.setLevelType(DesignChoiceConstants.LEVEL_TYPE_DYNAMIC);
				} catch (SemanticException e1) {
					ExceptionUtil.handle(e1);
				}

				updateButtonStatus(dynamicButton);
			}

		});
		staticButton = new Button(contents, SWT.RADIO);
		staticButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				try {
					input.setLevelType(DesignChoiceConstants.LEVEL_TYPE_MIRRORED);
				} catch (SemanticException e1) {
					ExceptionUtil.handle(e1);
				}

				updateButtonStatus(staticButton);
			}

		});
		dynamicButton.setText(Messages.getString("LevelPropertyDialog.Button.Dynamic")); //$NON-NLS-1$
		staticButton.setText(Messages.getString("LevelPropertyDialog.Button.Static")); //$NON-NLS-1$
	}

	protected void updateButtonStatus(Button button) {
		if (button == dynamicButton) {
			staticButton.setSelection(false);
			dynamicButton.setSelection(true);
			setExcludeGridData(staticArea, true);
			setExcludeGridData(dynamicArea, false);
			updateFormatHelper(dynamicFormatHelper, dynamicDataTypeCombo);

			try {
				input.setLevelType(DesignChoiceConstants.LEVEL_TYPE_DYNAMIC);
			} catch (SemanticException e) {
				ExceptionUtil.handle(e);
			}
		} else if (button == staticButton) {
			dynamicButton.setSelection(false);
			;
			staticButton.setSelection(true);
			setExcludeGridData(dynamicArea, true);
			setExcludeGridData(staticArea, false);
			updateFormatHelper(staticFormatHelper, staticDataTypeCombo);

			try {
				input.setLevelType(DesignChoiceConstants.LEVEL_TYPE_MIRRORED);
			} catch (SemanticException e) {
				ExceptionUtil.handle(e);
			}
		}
		this.getShell().layout();
		checkOkButtonStatus();
	}

	private void updateFormatHelper(IDialogHelper helper, Combo combo) {
		if (helper != null) {
			if (combo.getSelectionIndex() > -1) {
				helper.setProperty(BuilderConstants.FORMAT_VALUE_TYPE, getDataTypeNames()[combo.getSelectionIndex()]);
			}
			helper.update(true);
		}
	}

	private TabularLevelHandle input;
	private TableViewer dynamicViewer;
	private Text nameText;

	private TableViewer staticViewer;
	private Composite staticArea;
	private Button dynamicButton;
	private Button staticButton;
	protected int staticSelectIndex;
	private static final String Prop_Name = "Name"; //$NON-NLS-1$
	private static final String prop_Expression = "Expression"; //$NON-NLS-1$
	private static final String prop_Attribute = "Attribute"; //$NON-NLS-1$
	private static final String Prop_DefaultValue = "DefaultValue";//$NON-NLS-1$
	private static final String prop_Tooltip = "Tooltip";//$NON-NLS-1$
	private Table dynamicTable;
	private Combo staticDataTypeCombo;
	private Text staticNameText;
	private Combo fieldCombo;
	private Combo dynamicDataTypeCombo;
	private Combo displayKeyCombo;
	private TableViewer defaultValueViewer;
	private Table defaultValueTable;
	private TableColumn[] defaultValueColumns;
	private TableColumn[] staticColumns;
	private IDialogHelper dynamicLevelHelper;
	private IDialogHelper dynamicMemberHelper;
	private IDialogHelper staticLevelHelper;
	private IDialogHelper staticMemberHelper;
	private IDialogHelper dynamicFormatHelper;
	private IDialogHelper staticFormatHelper;
	private IDialogHelper dynamicAlignmentHelper;
	private IDialogHelper staticAlignmentHelper;
	private Table staticTable;
	private Button staticEditButton;
	private Button staticRemoveButton;
	private Button dynamicEditButton;
	private Button dynamicRemoveButton;;

	protected Composite createStaticArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 0;
		container.setLayout(layout);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		Group properties = new Group(container, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 3;
		properties.setLayout(layout);
		properties.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label(properties, SWT.NONE).setText(Messages.getString("LevelPropertyDialog.Name")); //$NON-NLS-1$
		staticNameText = new Text(properties, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		staticNameText.setLayoutData(gd);
		staticNameText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				checkOkButtonStatus();
			}

		});
		new Label(properties, SWT.NONE).setText(Messages.getString("LevelPropertyDialog.DataType")); //$NON-NLS-1$
		staticDataTypeCombo = new Combo(properties, SWT.BORDER | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		staticDataTypeCombo.setLayoutData(gd);
		staticDataTypeCombo.setVisibleItemCount(30);
		staticDataTypeCombo.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				updateFormatHelper(staticFormatHelper, staticDataTypeCombo);
				checkOkButtonStatus();
			}

		});

		staticLevelHelper = createLevelSecurityPart(properties);
		staticMemberHelper = createMemberSecurityPart(properties);
		staticFormatHelper = createFormatPart(properties);
		staticAlignmentHelper = createAlignmentPart(properties);

		Group contents = new Group(container, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 2;
		contents.setLayout(layout);
		contents.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		staticTable = new Table(contents, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER | SWT.VERTICAL | SWT.HORIZONTAL);
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 150;
		gd.verticalSpan = 3;
		staticTable.setLayoutData(gd);
		staticTable.setLinesVisible(true);
		staticTable.setHeaderVisible(true);

		staticTable.addKeyListener(new KeyAdapter() {

			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.DEL) {
					deleteStaticAttribute();
				}
			}
		});

		staticTable.addMouseListener(new MouseAdapter() {

			public void mouseDoubleClick(MouseEvent e) {
				handleStaticTableEditEvent();
			}
		});
		staticViewer = new TableViewer(staticTable);
		String[] columns = new String[] { "", //$NON-NLS-1$
				Messages.getString("LevelPropertyDialog.Label.Name"), //$NON-NLS-1$
				Messages.getString("LevelPropertyDialog.Label.Expression") //$NON-NLS-1$
		};

		int[] widths = new int[] { 15, 180, 180 };

		staticColumns = new TableColumn[3];
		for (int i = 0; i < columns.length; i++) {
			staticColumns[i] = new TableColumn(staticTable, SWT.LEFT);
			staticColumns[i].setResizable(columns[i] != null);
			if (columns[i] != null) {
				staticColumns[i].setText(columns[i]);
			}
			staticColumns[i].setWidth(widths[i]);
			staticColumns[i].addControlListener(new ControlListener() {

				public void controlMoved(ControlEvent e) {
				}

				public void controlResized(ControlEvent e) {
					defaultValueColumns[0].setWidth(staticColumns[0].getWidth());
					defaultValueColumns[1].setWidth(staticColumns[1].getWidth());
					defaultValueColumns[2].setWidth(staticColumns[2].getWidth());
				}

			});
		}

		staticViewer.setColumnProperties(new String[] { "", //$NON-NLS-1$
				LevelPropertyDialog.Prop_Name, LevelPropertyDialog.prop_Expression });

		staticViewer.setContentProvider(contentProvider);
		staticViewer.setLabelProvider(staticLabelProvider);

		Button staticAddButton = new Button(contents, SWT.PUSH);
		staticAddButton.setText(Messages.getString("LevelPropertyDialog.StaticTable.Button.Add")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_VERTICAL);
		gd.verticalAlignment = SWT.END;
		gd.horizontalAlignment = SWT.FILL;
		staticAddButton.setLayoutData(gd);
		staticAddButton.setEnabled(true);

		staticAddButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				LevelStaticAttributeDialog dialog = new LevelStaticAttributeDialog(
						Messages.getString("LevelPropertyDialog.StaticAttributeDialog.Title.New")); //$NON-NLS-1$
				dialog.setInput(input);
				if (dialog.open() == Window.OK) {
					refreshStaticViewer();
				}
				if (staticTable.getItemCount() > 0) {
					staticSelectIndex = staticTable.getItemCount() - 1;
					staticTable.select(staticSelectIndex);
					checkStaticViewerButtonStatus();
				}
			}
		});

		staticEditButton = new Button(contents, SWT.PUSH);
		staticEditButton.setText(Messages.getString("LevelPropertyDialog.StaticTable.Button.Edit")); //$NON-NLS-1$
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		staticEditButton.setLayoutData(gd);
		staticEditButton.setEnabled(false);

		staticEditButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleStaticTableEditEvent();
			}
		});

		staticRemoveButton = new Button(contents, SWT.PUSH);
		staticRemoveButton.setText(Messages.getString("LevelPropertyDialog.StaticTable.Button.Remove")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_VERTICAL);
		gd.horizontalAlignment = SWT.FILL;
		gd.verticalAlignment = SWT.BEGINNING;
		staticRemoveButton.setLayoutData(gd);
		staticRemoveButton.setEnabled(false);
		staticRemoveButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				deleteStaticAttribute();
			}
		});
		staticTable.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				checkStaticViewerButtonStatus();
			}
		});

		defaultValueTable = new Table(contents,
				SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER | SWT.VERTICAL | SWT.HORIZONTAL);
		defaultValueTable.setHeaderVisible(false);
		defaultValueTable.setLinesVisible(true);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = defaultValueTable.getItemHeight();
		defaultValueTable.setLayoutData(gd);

		defaultValueViewer = new TableViewer(defaultValueTable);

		defaultValueColumns = new TableColumn[3];
		for (int i = 0; i < columns.length; i++) {
			defaultValueColumns[i] = new TableColumn(defaultValueTable, SWT.LEFT);
			defaultValueColumns[i].setResizable(columns[i] != null);
			defaultValueColumns[i].setWidth(widths[i]);

		}

		defaultValueViewer.setColumnProperties(new String[] { "", //$NON-NLS-1$
				LevelPropertyDialog.Prop_DefaultValue, LevelPropertyDialog.prop_Tooltip });

		CellEditor[] cellEditors = new CellEditor[] { null, new TextCellEditor(defaultValueTable), null };

		defaultValueViewer.setCellEditors(cellEditors);

		defaultValueViewer.setContentProvider(defaultValueContentProvider);
		defaultValueViewer.setLabelProvider(defaultValueLabelProvider);
		defaultValueViewer.setCellModifier(defaultValueCellModifier);

		Button defaultValueEditButton = new Button(contents, SWT.PUSH);
		defaultValueEditButton.setText(Messages.getString("LevelPropertyDialog.DynamicTable.Button.Edit")); //$NON-NLS-1$
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		defaultValueEditButton.setLayoutData(gd);
		defaultValueEditButton.setEnabled(true);

		defaultValueEditButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleDefaultValueEditEvent();
			}
		});

		return container;

	}

	protected void checkStaticViewerButtonStatus() {
		if (staticTable != null && !staticTable.isDisposed()) {
			if (staticTable.getSelectionCount() > 0) {
				setButtonEnabled(staticEditButton, true);
				setButtonEnabled(staticRemoveButton, true);
			} else {
				setButtonEnabled(staticEditButton, false);
				setButtonEnabled(staticRemoveButton, false);
			}
		} else {
			setButtonEnabled(staticEditButton, false);
			setButtonEnabled(staticRemoveButton, false);
		}
	}

	protected void checkDynamicViewerButtonStatus() {
		if (dynamicTable != null && !dynamicTable.isDisposed()) {
			if (dynamicTable.getSelectionCount() > 0) {
				setButtonEnabled(dynamicEditButton, true);
				setButtonEnabled(dynamicRemoveButton, true);
			} else {
				setButtonEnabled(dynamicEditButton, false);
				setButtonEnabled(dynamicRemoveButton, false);
			}
		} else {
			setButtonEnabled(dynamicEditButton, false);
			setButtonEnabled(dynamicRemoveButton, false);
		}
	}

	private void setButtonEnabled(Button button, boolean enabled) {
		if (button != null && !button.isDisposed()) {
			button.setEnabled(enabled);
		}
	}

	public void setInput(TabularLevelHandle level) {
		this.input = level;
	}

	public static void setExcludeGridData(Control control, boolean exclude) {
		Object obj = control.getLayoutData();
		if (obj == null)
			control.setLayoutData(new GridData());
		else if (!(obj instanceof GridData))
			return;
		GridData data = (GridData) control.getLayoutData();
		if (exclude) {
			data.heightHint = 0;
		} else {
			data.heightHint = -1;
		}
		control.setLayoutData(data);
		control.getParent().layout();
		control.setVisible(!exclude);
	}

	protected void checkOkButtonStatus() {
		if (getButton(IDialogConstants.OK_ID) != null)
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		setMessage(null);

		if (dynamicButton.getSelection()) {
			if (nameText.getText() == null || nameText.getText().trim().equals(""))//$NON-NLS-1$
				setErrorMessage(Messages.getString("LevelPropertyDialog.Message.BlankName")); //$NON-NLS-1$
			else if (!UIUtil.validateDimensionName(nameText.getText()))
				setErrorMessage(Messages.getString("LevelPropertyDialog.Message.NumericName")); //$NON-NLS-1$
			else if (fieldCombo.getSelectionIndex() == -1)
				setErrorMessage(Messages.getString("LevelPropertyDialog.Message.BlankKeyField")); //$NON-NLS-1$
			else if (dynamicDataTypeCombo.getSelectionIndex() == -1)
				setErrorMessage(Messages.getString("LevelPropertyDialog.Message.BlankDataType")); //$NON-NLS-1$
			else {
				if (getButton(IDialogConstants.OK_ID) != null)
					getButton(IDialogConstants.OK_ID).setEnabled(true);
				setErrorMessage(null);
				setMessage(Messages.getString("LevelPropertyDialog.Message")); //$NON-NLS-1$
			}
		} else {
			if (staticNameText.getText() == null || staticNameText.getText().trim().equals("")) //$NON-NLS-1$
				setErrorMessage(Messages.getString("LevelPropertyDialog.Message.BlankName")); //$NON-NLS-1$
			else if (!UIUtil.validateDimensionName(staticNameText.getText()))
				setErrorMessage(Messages.getString("LevelPropertyDialog.Message.NumericName")); //$NON-NLS-1$
			else if (staticDataTypeCombo.getSelectionIndex() == -1)
				setErrorMessage(Messages.getString("LevelPropertyDialog.Message.BlankDataType")); //$NON-NLS-1$
			else {
				if (getButton(IDialogConstants.OK_ID) != null)
					getButton(IDialogConstants.OK_ID).setEnabled(true);
				setErrorMessage(null);
				setMessage(Messages.getString("LevelPropertyDialog.Message")); //$NON-NLS-1$
			}
		}
	}

	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		checkOkButtonStatus();
		if (input != null && input.getLevelType() != null) {
			if (input.getLevelType().equals(DesignChoiceConstants.LEVEL_TYPE_DYNAMIC)) {
				dynamicButton.setSelection(true);
				staticButton.setSelection(false);
			} else {
				dynamicButton.setSelection(false);
				staticButton.setSelection(true);
			}
		}
	}

	protected Point getInitialSize() {
		Point shellSize = super.getInitialSize();
		return new Point(Math.max(convertHorizontalDLUsToPixels(400), shellSize.x),
				Math.max(convertVerticalDLUsToPixels(350), shellSize.y));
	}

	private void deleteStaticAttribute() {
		if (staticTable.getSelectionCount() > 0) {
			staticSelectIndex = staticTable.getSelectionIndex();
			try {
				handleStaticDelEvent();
			} catch (Exception e1) {
				ExceptionUtil.handle(e1);
			}
			refreshStaticViewer();
			int itemCount = staticTable.getItemCount();
			if (staticSelectIndex >= itemCount)
				staticSelectIndex = itemCount - 1;
			if (staticSelectIndex >= 0)
				staticTable.select(staticSelectIndex);
			checkStaticViewerButtonStatus();
		}
	}

	private void deleteDynamicAttribute() {
		if (dynamicTable.getSelectionCount() > 0) {
			dynamicSelectIndex = dynamicTable.getSelectionIndex();
			try {
				handleDynamicDelEvent();
			} catch (Exception e1) {
				ExceptionUtil.handle(e1);
			}
			refreshDynamicViewer();
			int itemCount = dynamicTable.getItemCount();
			if (dynamicSelectIndex >= itemCount)
				dynamicSelectIndex = itemCount - 1;
			if (dynamicSelectIndex >= 0)
				dynamicTable.select(dynamicSelectIndex);
			checkDynamicViewerButtonStatus();
		}
	}

	private void handleStaticTableEditEvent() {
		if (((StructuredSelection) staticViewer.getSelection()).getFirstElement() instanceof RuleHandle) {
			staticSelectIndex = staticTable.getSelectionIndex();
			LevelStaticAttributeDialog dialog = new LevelStaticAttributeDialog(
					Messages.getString("LevelPropertyDialog.StaticAttributeDialog.Title.Edit")); //$NON-NLS-1$
			dialog.setInput(input, (RuleHandle) ((StructuredSelection) staticViewer.getSelection()).getFirstElement());
			if (dialog.open() == Window.OK) {
				refreshStaticViewer();
			}
			staticTable.select(staticSelectIndex);
			checkStaticViewerButtonStatus();
		}
	}

	private void handleDynamicTableEditEvent() {
		if (((StructuredSelection) dynamicViewer.getSelection()).getFirstElement() instanceof LevelAttributeHandle) {

			LevelAttributeHandle handle = (LevelAttributeHandle) ((StructuredSelection) dynamicViewer.getSelection())
					.getFirstElement();
			resetEditorItems(handle.getName());
			dynamicSelectIndex = dynamicTable.getSelectionIndex();
			LevelDynamicAttributeDialog dialog = new LevelDynamicAttributeDialog(
					Messages.getString("LevelPropertyDialog.DynamicAttributeDialog.Title.Edit")); //$NON-NLS-1$
			dialog.setInput(dynamicMemeberItems, handle.getName());
			if (dialog.open() == Window.OK) {
				try {
					handle.setName((String) dialog.getResult());
					if (dataset != null) {
						ResultSetColumnHandle dataField = OlapUtil.getDataField(dataset, handle.getName());
						handle.setDataType(dataField.getDataType());
					}
				} catch (SemanticException e) {
					ExceptionUtil.handle(e);
				}
				refreshDynamicViewer();
			}
			dynamicTable.select(dynamicSelectIndex);
			checkDynamicViewerButtonStatus();
		}
	}

	private void handleDefaultValueEditEvent() {
		InputDialog dialog = new InputDialog(this.getShell(), DEFAULTVALUE_EDIT_TITLE, DEFAULTVALUE_EDIT_LABEL,
				DEUtil.resolveNull(input.getDefaultValue()), null);
		if (dialog.open() == Window.OK) {
			String value = dialog.getValue();
			try {
				if (value == null || value.trim().length() == 0)
					input.setDefaultValue(null);
				else
					input.setDefaultValue(value.trim());
				defaultValueViewer.refresh();
			} catch (SemanticException e) {
				ExceptionHandler.handle(e);
			}
		}
	}
}
