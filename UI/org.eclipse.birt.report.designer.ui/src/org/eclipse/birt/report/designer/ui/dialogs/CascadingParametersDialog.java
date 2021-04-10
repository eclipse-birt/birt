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

package org.eclipse.birt.report.designer.ui.dialogs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.data.engine.api.DataEngineContext.DataEngineFlowMode;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetUIUtil;
import org.eclipse.birt.report.designer.data.ui.util.SelectValueFetcher;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.ExpressionButton;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.ExpressionEditor;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.IExpressionHelper;
import org.eclipse.birt.report.designer.internal.ui.expressions.ExpressionContextFactoryImpl;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionContextFactory;
import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.FormatValueHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.util.ParameterValidationUtil;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.interfaces.IScalarParameterModel;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;

import com.ibm.icu.util.ULocale;

/**
 * Cascading Parameter Dialog.
 */

public class CascadingParametersDialog extends BaseDialog {

	private Composite sorttingArea;
	private Label sortKeyLabel, sortDirectionLabel;
	private Combo sortDirectionChooser, sortKeyChooser;
	private static final String LABEL_SORT_GROUP = Messages.getString("ParameterDialog.Label.SortGroup"); //$NON-NLS-1$
	private static final String LABEL_SORT_KEY = Messages.getString("ParameterDialog.Label.SortKey"); //$NON-NLS-1$
	private static final String CHOICE_NONE = Messages.getString("ParameterDialog.Label.None"); //$NON-NLS-1$
	private static final String LABEL_SORT_DIRECTION = Messages.getString("ParameterDialog.Label.SortDirection"); //$NON-NLS-1$
	private static final String CHOICE_ASCENDING = Messages.getString("ParameterDialog.Choice.ASCENDING"); //$NON-NLS-1$
	private static final String CHOICE_DESCENDING = Messages.getString("ParameterDialog.Choice.DESCENDING"); //$NON-NLS-1$

	private static final Image ERROR_ICON = ReportPlatformUIImages.getImage(ISharedImages.IMG_OBJS_ERROR_TSK);

	private static final String CHOICE_NULL_VALUE = Messages.getString("CascadingParametersDialog.Choice.NullValue"); //$NON-NLS-1$

	private static final String CHOICE_BLANK_VALUE = Messages.getString("CascadingParametersDialog.Choice.BlankValue"); //$NON-NLS-1$

	private static final String CHOICE_SELECT_VALUE = Messages
			.getString("CascadingParametersDialog.Choice.SelectValue"); //$NON-NLS-1$

	private static final String LABEL_PARAMTER_PROMPT_TEXT = Messages
			.getString("CascadingParametersDialog.Label.parameterPromptText"); //$NON-NLS-1$

	private static final String LABEL_VALUES = Messages.getString("CascadingParametersDialog.Label.values"); //$NON-NLS-1$

	private static final String LABEL_GROUP_GENERAL = Messages
			.getString("CascadingParametersDialog.Label.group.general"); //$NON-NLS-1$

	private static final String LABEL_CASCADING_PARAMETER_NAME = Messages
			.getString("CascadingParametersDialog.Label.cascadingParam.name"); //$NON-NLS-1$

	private static final String LABEL_GROUP_PROMPT_TEXT = Messages
			.getString("CascadingParametersDialog.Label.groupPromptText"); //$NON-NLS-1$

	private static final String ERROR_MSG_MISMATCH_DATA_TYPE = Messages
			.getString("ParameterDialog.ErrorMessage.MismatchDataType"); //$NON-NLS-1$

	// private static final String LABEL_GROUP_PROMPT_TEXT = Messages.getString(
	// "CascadingParametersDialog.Label.promptText" ); //$NON-NLS-1$

	// private static final String LABEL_DATA_SETS = Messages.getString(
	// "CascadingParametersDialog.Label.dataSets" ); //$NON-NLS-1$

	// private static final String LABEL_BUTTON_CREATE_NEW_DATASET =
	// Messages.getString(
	// "CascadingParametersDialog.Label.button.createNew.dataset" );
	// //$NON-NLS-1$

	private static final String LABEL_PARAMETERS = Messages.getString("CascadingParametersDialog.Label.parameters"); //$NON-NLS-1$

	private static final String LABEL_GROUP_PROPERTIES = Messages
			.getString("CascadingParametersDialog.Label.group.properties"); //$NON-NLS-1$

	private static final String LABEL_PARAM_NAME = Messages.getString("CascadingParametersDialog.Label.param.name"); //$NON-NLS-1$

	private static final String LABEL_LIST_LIMIT = Messages.getString("CascadingParametersDialog.Label.listLimit"); //$NON-NLS-1$

	private static final String LABEL_DATA_TYPE = Messages.getString("CascadingParametersDialog.Label.dataType"); //$NON-NLS-1$

	private static final String LABEL_DISPLAY_TYPE = Messages.getString("CascadingParametersDialog.Label.displayType"); //$NON-NLS-1$

	private static final String LABEL_DEFAULT_VALUE = Messages
			.getString("CascadingParametersDialog.Label.defaultValue"); //$NON-NLS-1$

	private static final String LABEL_GROUP_MORE_OPTIONS = Messages
			.getString("CascadingParametersDialog.Label.group.moreOptions"); //$NON-NLS-1$

	private static final String LABEL_HELP_TEXT = Messages.getString("CascadingParametersDialog.Label.helpText"); //$NON-NLS-1$

	private static final String LABEL_FORMAT_AS = Messages.getString("CascadingParametersDialog.Label.formatAs"); //$NON-NLS-1$

	private static final String LABEL_CHANGE_FORMAT_BUTTON = Messages
			.getString("CascadingParametersDialog.Label.button.changeFormat"); //$NON-NLS-1$

	private static final String LABEL_PREVIEW_WITH_FORMAT = Messages
			.getString("CascadingParametersDialog.Label.preview"); //$NON-NLS-1$

	// private static final String LABEL_CREATE_NEW_PARAMETER =
	// Messages.getString( "CascadingParametersDialog.Label.createNewParam" );
	// //$NON-NLS-1$

	private static final String LABEL_SELECT_DATA_SET = Messages
			.getString("CascadingParametersDialog.Label.selectDataSet"); //$NON-NLS-1$

	private static final String LABEL_SELECT_DISPLAY_COLUMN = Messages
			.getString("CascadingParametersDialog.Label.selectDisplayColumn"); //$NON-NLS-1$

	private static final String LABEL_SELECT_VALUE_COLUMN = Messages
			.getString("CascadingParametersDialog.Label.selectValueColumn"); //$NON-NLS-1$

	private static final String LABEL_NO_COLUMN_AVAILABLE = Messages
			.getString("CascadingParametersDialog.Label.NoColumnAvailable"); //$NON-NLS-1$

	private static final String BUTTON_IS_REQUIRED = Messages.getString("CascadingParametersDialog.Button.isRequired"); //$NON-NLS-1$

	// private static final String LABEL_SELECT_DATA_SET_MODE =
	// Messages.getString( "CascadingParametersDialog.Label.SelectDataSetMode"
	// ); //$NON-NLS-1$

	private static final String RADIO_SINGLE = Messages.getString("CascadingParametersDialog.Radio.Single"); //$NON-NLS-1$

	private static final String RADIO_MULTIPLE = Messages.getString("CascadingParametersDialog.Radio.Mutli"); //$NON-NLS-1$

	private static final String COLUMN_NAME = Messages.getString("CascadingParametersDialog.Label.column.name"); //$NON-NLS-1$

	private static final String COLUMN_DATA_SET = Messages.getString("CascadingParametersDialog.Label.column.dataSet"); //$NON-NLS-1$

	private static final String COLUMN_VALUE = Messages.getString("CascadingParametersDialog.Label.column.value"); //$NON-NLS-1$

	private static final String COLUMN_DISPLAY_TEXT = Messages
			.getString("CascadingParametersDialog.Label.column.displayText"); //$NON-NLS-1$

	private static final String PARAM_CONTROL_LIST = DesignChoiceConstants.PARAM_CONTROL_LIST_BOX + "/List"; //$NON-NLS-1$

	private static final String PARAM_CONTROL_COMBO = DesignChoiceConstants.PARAM_CONTROL_LIST_BOX + "/Combo"; //$NON-NLS-1$

	private static final String DISPLAY_NAME_CONTROL_LIST = Messages
			.getString("CascadingParametersDialog.display.controlType.listBox"); //$NON-NLS-1$

	private static final String DISPLAY_NAME_CONTROL_COMBO = Messages
			.getString("CascadingParametersDialog.display.controlType.comboBox"); //$NON-NLS-1$

	private static final double DEFAULT_PREVIEW_NUMBER = Double.parseDouble("1234.56"); //$NON-NLS-1$

	private static final int DEFAULT_PREVIEW_INTEGER_NUMBER = 123456;

	private static final String STANDARD_DATE_TIME_PATTERN = "MM/dd/yyyy hh:mm:ss a"; //$NON-NLS-1$

	private static final String DEFAULT_PREVIEW_STRING = Messages
			.getString("CascadingParametersDialog.default.preview.string"); //$NON-NLS-1$

	private static final String ERROR_TITLE_INVALID_LIST_LIMIT = Messages
			.getString("ParameterDialog.ErrorTitle.InvalidListLimit"); //$NON-NLS-1$

	private static final String ERROR_MSG_INVALID_LIST_LIMIT = Messages
			.getString("ParameterDialog.ErrorMessage.InvalidListLimit"); //$NON-NLS-1$

	private Group optionsGroup;
	private Group propertiesGroup;

	private Text cascadingNameEditor;
	private Text promptTextEditor;
	private Text paramNameEditor;
	private Text helpTextEditor;
	// private Text defaultValueEditor;
	private Combo defaultValueChooser;
	private Text formatField;

	private Text listLimit;
	private Text promptText;

	private Combo dataTypeChooser;
	private Combo displayTypeChooser;

	private Button changeFormat;

	private Button singleDataSet, multiDataSet;

	private CLabel previewLable;

	private Table table;
	private TableViewer valueTable;
	private Button addBtn, editBtn, delBtn;

	private List<Expression> defaultValueList;

	private static final IChoiceSet DATA_TYPE_CHOICE_SET = DEUtil.getMetaDataDictionary()
			.getElement(ReportDesignConstants.SCALAR_PARAMETER_ELEMENT)
			.getProperty(ScalarParameterHandle.DATA_TYPE_PROP).getAllowedChoices();

	private CascadingParameterGroupHandle inputParameterGroup;

	private ScalarParameterHandle selectedParameter;

	// private String lastDataType;

	private String formatPattern;

	private String formatCategroy;

	private ULocale formatLocale;

	private Button isRequired;

	private int maxStrLengthProperty;

	private int maxStrLengthOption;

	private String PROPERTY_LABEL_STRING[] = { LABEL_PARAM_NAME, LABEL_GROUP_PROMPT_TEXT, LABEL_PARAMTER_PROMPT_TEXT,
			LABEL_DATA_TYPE, LABEL_DISPLAY_TYPE, LABEL_DEFAULT_VALUE };

	CLabel errorMessageLine;

	private String OPTION_LABEL_STRING[] = { LABEL_HELP_TEXT, LABEL_FORMAT_AS, LABEL_LIST_LIMIT };
	private Button isMultiple;

	protected IStructuredContentProvider tableContentProvider = new IStructuredContentProvider() {

		public void dispose() {
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement == null) {
				return new Object[0];
			} else if (inputElement instanceof List) {
				return ((List) inputElement).toArray();
			}
			return null;
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	};

	protected ITableLabelProvider tableLableProvier = new ITableLabelProvider() {

		public void addListener(ILabelProviderListener listener) {
		}

		public void dispose() {
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (columnIndex == 0) {
				if (element instanceof Expression) {
					return ((Expression) element).getStringExpression();
				}
				return element.toString();
			}
			return ""; //$NON-NLS-1$
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {
		}
	};

	protected int getMaxStrLength(String string[], Control control) {
		int len = UIUtil.getMaxStringWidth(string, control);
		return len;
	}

	/**
	 * 
	 * Constructor.
	 * 
	 * @param parentShell
	 * @param title
	 */
	public CascadingParametersDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}

	/**
	 * Constructor.
	 * 
	 * @param title
	 */
	public CascadingParametersDialog(String title) {
		super(title);
	}

	protected Control createDialogArea(Composite parent) {
		// Composite composite = (Composite) super.createDialogArea( parent );

		ScrolledComposite sc = new ScrolledComposite((Composite) super.createDialogArea(parent),
				SWT.H_SCROLL | SWT.V_SCROLL);
		sc.setLayout(new FillLayout());
		sc.setLayoutData(new GridData(GridData.FILL_BOTH));
		applyDialogFont(sc);

		mainContent = new Composite(sc, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_MARGIN);
		layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
		layout.verticalSpacing = convertVerticalDLUsToPixels(IDialogConstants.VERTICAL_SPACING);
		layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
		mainContent.setLayout(layout);

		UIUtil.bindHelp(parent, IHelpContextIds.CASCADING_PARAMETER_DIALOG_ID);

		GridData data = new GridData(GridData.FILL_BOTH);

		maxStrLengthProperty = getMaxStrLength(PROPERTY_LABEL_STRING, mainContent);

		maxStrLengthOption = getMaxStrLength(OPTION_LABEL_STRING, mainContent);

		mainContent.setLayoutData(data);

		createGeneralPart(mainContent);

		createChoicePart(mainContent);

		createDynamicParamsPart(mainContent);

		createPropertiesPart(mainContent);

		createSortingArea(mainContent);

		createOptionsPart(mainContent);

		createLabel(mainContent, null);
		errorMessageLine = new CLabel(mainContent, SWT.NONE);
		GridData msgLineGridData = new GridData(GridData.FILL_HORIZONTAL);
		msgLineGridData.horizontalSpan = 2;
		errorMessageLine.setLayoutData(msgLineGridData);

		sc.setContent(mainContent);
		sc.setExpandHorizontal(true);
		// sc.setExpandVertical( true );
		sc.setMinWidth(500);
		// sc.setMinHeight( 570 );

		Point size = mainContent.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		mainContent.setSize(size);

		return sc;
	}

	private String validateDefaultValue() {
		if (selectedParameter == null) {
			return null;
		}
		String tempDefaultValue = defaultValueChooser.getText();

		String tempType = DATA_TYPE_CHOICE_SET.findChoiceByDisplayName(dataTypeChooser.getText()).getName();

		if (DesignChoiceConstants.PARAM_TYPE_STRING.endsWith(tempType)
				|| DesignChoiceConstants.PARAM_TYPE_BOOLEAN.endsWith(tempType)) {
			return null;
		}

		if (tempDefaultValue.length() > 0) {
			try {

				if (!((DesignChoiceConstants.PARAM_TYPE_STRING.endsWith(getSelectedDataType()))
						|| (DesignChoiceConstants.PARAM_TYPE_BOOLEAN.endsWith(getSelectedDataType())))) {
					if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(getSelectedDataType())) {
						tempDefaultValue = convertToStandardFormat(DataTypeUtil.toDate(tempDefaultValue));
					} else if (DesignChoiceConstants.PARAM_TYPE_DATE.equals(getSelectedDataType())) {
						tempDefaultValue = convertToStandardFormat(DataTypeUtil.toSqlDate(tempDefaultValue));
					} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equals(getSelectedDataType())) {
						tempDefaultValue = convertToStandardFormat(DataTypeUtil.toSqlTime(tempDefaultValue));
					}

					ParameterValidationUtil.validate(getSelectedDataType(), STANDARD_DATE_TIME_PATTERN,
							tempDefaultValue, ULocale.getDefault());

				}
			} catch (BirtException e) {
				return ERROR_MSG_MISMATCH_DATA_TYPE;
			}
		}

		return null;
	}

	private String convertToStandardFormat(Date date) {
		if (date == null) {
			return null;
		}
		return new DateFormatter(STANDARD_DATE_TIME_PATTERN, ULocale.getDefault()).format(date);
	}

	private void updateMessageLine() {
		String errorMessage = validateDefaultValue();

		if (errorMessage != null) {
			errorMessageLine.setText(errorMessage);
			errorMessageLine.setImage(ERROR_ICON);
		} else {
			errorMessageLine.setText(""); //$NON-NLS-1$
			errorMessageLine.setImage(null);
		}
		updateButtons();
	}

	private void createLabel(Composite parent, String content) {
		Label label = new Label(parent, SWT.NONE);
		if (content != null) {
			label.setText(content);
		}
		GridData gd = new GridData();
		if (label.getText().equals(LABEL_VALUES)) {
			gd.verticalAlignment = GridData.BEGINNING;
		}
		label.setLayoutData(gd);
	}

	private void createGeneralPart(Composite parent) {
		Group group = new Group(parent, SWT.NULL);
		group.setText(LABEL_GROUP_GENERAL);
		group.setLayout(new GridLayout(2, false));
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(group, SWT.NULL).setText(LABEL_CASCADING_PARAMETER_NAME);

		cascadingNameEditor = new Text(group, SWT.BORDER);
		cascadingNameEditor.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		new Label(group, SWT.NULL).setText(LABEL_GROUP_PROMPT_TEXT);
		promptTextEditor = new Text(group, SWT.BORDER);
		promptTextEditor.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

	}

	private void updateDataSets() {
		ArrayList elementsList = new ArrayList(inputParameterGroup.getParameters().getContents());
		if (elementsList == null || elementsList.size() == 0) {
			return;
		}

		DataSetHandle dataSet = inputParameterGroup.getDataSet();
		for (Iterator iter = elementsList.iterator(); iter.hasNext();) {
			ScalarParameterHandle handle = (ScalarParameterHandle) iter.next();
			try {
				handle.setDataSet(dataSet);
			} catch (SemanticException e1) {

			}
		}
	}

	private void createChoicePart(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout(2, true));

		// new Label( composite, SWT.NONE ).setText( LABEL_SELECT_DATA_SET_MODE
		// );

		singleDataSet = new Button(composite, SWT.RADIO);
		singleDataSet.setText(RADIO_SINGLE);
		singleDataSet.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {

				updateDataSets();
				refreshValueTable();
				updateButtons();
			}
		});

		multiDataSet = new Button(composite, SWT.RADIO);
		multiDataSet.setText(RADIO_MULTIPLE);
		multiDataSet.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				refreshValueTable();
				updateButtons();
			}
		});

	}

	private void createDynamicParamsPart(Composite parent) {
		Composite comp = new Composite(parent, SWT.NULL);
		GridLayout layout = UIUtil.createGridLayoutWithoutMargin();
		layout.numColumns = 2;
		comp.setLayout(layout);
		comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label label = new Label(comp, SWT.NULL);
		label.setText(LABEL_PARAMETERS);

		// creat dummy label
		new Label(comp, SWT.NULL);

		table = new Table(comp, SWT.FULL_SELECTION | SWT.BORDER);

		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.heightHint = 100;
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				// If Delete pressed, delete the selected row
				if (e.keyCode == SWT.DEL) {
					deleteRow();
				}
			}
		});

		int[] columnWidths = new int[] { 120, 100, 120, 135, };
		String[] columns = new String[] { COLUMN_NAME, COLUMN_DATA_SET, COLUMN_VALUE, COLUMN_DISPLAY_TEXT };

		for (int i = 0; i < columns.length; i++) {
			TableColumn column = new TableColumn(table, SWT.LEFT);
			column.setResizable(true);
			column.setText(columns[i]);
			column.setWidth(columnWidths[i]);
		}
		table.setLayoutData(data);
		table.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {
				int index = table.getSelectionIndex();
				boolean setBtnEnable = true;
				if (index == -1) {
					setBtnEnable = false;
				}

				editBtn.setEnabled(setBtnEnable);
				delBtn.setEnabled(setBtnEnable);

			}

			public void focusLost(FocusEvent e) {
				int index = table.getSelectionIndex();
				boolean setBtnEnable = true;
				if (index == -1) {
					setBtnEnable = false;
				}

				editBtn.setEnabled(setBtnEnable);
				delBtn.setEnabled(setBtnEnable);
			}

		});

		valueTable = new TableViewer(table);

		valueTable.setColumnProperties(columns);
		valueTable.setContentProvider(contentProvider);
		valueTable.setLabelProvider(labelProvider);

		valueTable.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = event.getSelection();
				Object param = ((StructuredSelection) selection).getFirstElement();
				if (param != selectedParameter) {
					if (param instanceof ScalarParameterHandle) {
						try {
							saveParameterProperties();
							saveSortingProperties();
							selectedParameter = (ScalarParameterHandle) param;
							defaultValueList = selectedParameter.getDefaultValueList();
						} catch (SemanticException e) {
							ExceptionHandler.handle(e);
							valueTable.setSelection(new StructuredSelection(selectedParameter));
						}
						refreshParameterProperties();
						initSorttingArea();
						updateButtons();
					}
				}
			}
		});

		valueTable.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				editParameter(selectedParameter);

			}

		});

		// create Add, edit, and delete buttons.
		Composite composite = new Composite(comp, SWT.NULL);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

		addBtn = new Button(composite, SWT.NONE);
		addBtn.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		addBtn.setText(Messages.getString("CascadingParametersDialog.Button.Add")); //$NON-NLS-1$
		addBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				AddEditCascadingParameterDialog dialog = new AddEditCascadingParameterDialog(
						Messages.getString("CascadingParametersDialog.Title.AddCascadingParameter")); //$NON-NLS-1$
				if (dialog.open() != Dialog.OK) {
					return;
				}
				try {
					inputParameterGroup.getParameters().add(dialog.getParameter());
					for (int i = 0; i < inputParameterGroup.getParameters().getCount() - 1; i++) {
						ScalarParameterHandle parameter = (ScalarParameterHandle) inputParameterGroup.getParameters()
								.get(i);
						parameter.setParamType(DesignChoiceConstants.SCALAR_PARAM_TYPE_SIMPLE);
						List valueLisit = parameter.getDefaultValueList();
						if (valueLisit != null && valueLisit.size() > 0) {
							Object expression = valueLisit.get(0);
							valueLisit.clear();
							valueLisit.add(expression);
						}
						parameter.setDefaultValueList(valueLisit);
					}
				} catch (ContentException e1) {
					ExceptionHandler.handle(e1);
				} catch (NameException e1) {
					ExceptionHandler.handle(e1);
				} catch (SemanticException e1) {
					ExceptionHandler.handle(e1);
				}

				refreshValueTable();
				valueTable.setSelection(new StructuredSelection(dialog.getParameter()));
				updateButtons();
			}
		});

		editBtn = new Button(composite, SWT.NONE);
		editBtn.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		editBtn.setText(Messages.getString("CascadingParametersDialog.Button.Edit")); //$NON-NLS-1$
		editBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				editParameter(selectedParameter);
			}
		});

		delBtn = new Button(composite, SWT.NONE);
		delBtn.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		delBtn.setText(Messages.getString("CascadingParametersDialog.Button.Delete")); //$NON-NLS-1$
		delBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				deleteRow();
				updateButtons();
			}
		});

	}

	protected void editParameter(ScalarParameterHandle param) {
		if (param == null) {
			return;
		}

		CommandStack cmdStack = SessionHandleAdapter.getInstance().getReportDesignHandle().getCommandStack();
		cmdStack.startTrans(Messages.getString("CascadingParametersDialog.Title.EditCascadingParameter")); //$NON-NLS-1$

		AddEditCascadingParameterDialog dialog = new AddEditCascadingParameterDialog(
				Messages.getString("CascadingParametersDialog.Title.EditCascadingParameter")); //$NON-NLS-1$
		dialog.setParameter(param);
		if (dialog.open() != Dialog.OK) {
			cmdStack.rollback();
			return;
		}
		cmdStack.commit();

		refreshValueTable();
		refreshParameterProperties();
		initSorttingArea();
		updateButtons();
	}

	private void createPropertiesPart(Composite parent) {
		propertiesGroup = new Group(parent, SWT.NULL);
		propertiesGroup.setText(LABEL_GROUP_PROPERTIES);
		propertiesGroup.setLayout(new GridLayout(2, false));
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		propertiesGroup.setLayoutData(gd);

		createLabel(propertiesGroup, LABEL_PARAM_NAME, maxStrLengthProperty);

		paramNameEditor = new Text(propertiesGroup, SWT.BORDER | SWT.READ_ONLY);
		paramNameEditor.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		paramNameEditor.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				valueTable.refresh(selectedParameter);
			}

		});

		createLabel(propertiesGroup, LABEL_PARAMTER_PROMPT_TEXT, maxStrLengthProperty);

		promptText = new Text(propertiesGroup, SWT.BORDER);
		promptText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createLabel(propertiesGroup, LABEL_DATA_TYPE, maxStrLengthProperty);
		dataTypeChooser = new Combo(propertiesGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
		dataTypeChooser.setVisibleItemCount(30);
		dataTypeChooser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		dataTypeChooser.setItems(ChoiceSetFactory.getDisplayNamefromChoiceSet(DATA_TYPE_CHOICE_SET));
		dataTypeChooser.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (selectedParameter != null) {
					changeDataType(DATA_TYPE_CHOICE_SET.findChoiceByDisplayName(dataTypeChooser.getText()).getName());
					try {
						selectedParameter.setDataType(
								DATA_TYPE_CHOICE_SET.findChoiceByDisplayName(dataTypeChooser.getText()).getName());
					} catch (SemanticException e1) {
						ExceptionHandler.handle(e1);
					}
				}
				updateMessageLine();
			}
		});

		createLabel(propertiesGroup, LABEL_DISPLAY_TYPE, maxStrLengthProperty);
		displayTypeChooser = new Combo(propertiesGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
		displayTypeChooser.setVisibleItemCount(30);
		displayTypeChooser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		displayTypeChooser.setItems(new String[] { DISPLAY_NAME_CONTROL_LIST, DISPLAY_NAME_CONTROL_COMBO });
		displayTypeChooser.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (selectedParameter != null) {
					try {
						String newControlType = getSelectedDisplayType();
						if (PARAM_CONTROL_COMBO.equals(newControlType)) {
							newControlType = DesignChoiceConstants.PARAM_CONTROL_LIST_BOX;
							// selectedParameter.setMustMatch( true );
							selectedParameter.setMustMatch(false);
							isMultiple.setEnabled(false);
							isMultiple.setSelection(false);
							selectedParameter.setParamType(DesignChoiceConstants.SCALAR_PARAM_TYPE_SIMPLE);
							simpleDefaultValueList();
						} else if (PARAM_CONTROL_LIST.equals(newControlType)) {
							newControlType = DesignChoiceConstants.PARAM_CONTROL_LIST_BOX;
							// selectedParameter.setMustMatch( false );
							selectedParameter.setMustMatch(true);
							if (selectedParameter == inputParameterGroup.getParameters()
									.get(inputParameterGroup.getParameters().getCount() - 1)) {
								isMultiple.setEnabled(true);
								if (isMultiple.getSelection())
									selectedParameter.setParamType(DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE);
								else {
									selectedParameter.setParamType(DesignChoiceConstants.SCALAR_PARAM_TYPE_SIMPLE);
									simpleDefaultValueList();
								}
							} else {
								isMultiple.setEnabled(false);
								isMultiple.setSelection(false);
								selectedParameter.setParamType(DesignChoiceConstants.SCALAR_PARAM_TYPE_SIMPLE);
								simpleDefaultValueList();
							}
						} else {
							selectedParameter.setProperty(ScalarParameterHandle.MUCH_MATCH_PROP, null);
						}
						selectedParameter.setControlType(newControlType);
						initDefaultValueViewer();
					} catch (SemanticException e1) {
						ExceptionHandler.handle(e1);
					}
				}
			}
		});

		createLabel(propertiesGroup, LABEL_DEFAULT_VALUE, maxStrLengthProperty);

		Composite composite = new Composite(propertiesGroup, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 0;
		layout.numColumns = 3;
		composite.setLayout(layout);

		defaultValueChooser = new Combo(composite, SWT.BORDER);
		defaultValueChooser.setVisibleItemCount(30);
		defaultValueChooser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		defaultValueChooser.add(CHOICE_SELECT_VALUE);
		defaultValueChooser.addVerifyListener(new VerifyListener() {

			public void verifyText(VerifyEvent e) {
				// TODO Auto-generated method stub
				String selection = e.text;
				if (defaultValueChooser.indexOf(selection) == -1) {
					e.doit = true;
					return;
				}
				if (selection.equals(CHOICE_SELECT_VALUE)) {
					e.doit = false;
				} else {
					e.doit = true;
				}

			}
		});
		defaultValueChooser.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (defaultValueChooser.getSelectionIndex() == -1)
					return;
				String selection = defaultValueChooser.getItem(defaultValueChooser.getSelectionIndex());
				if (selection.equals(CHOICE_SELECT_VALUE)) {
					// defaultValueChooser.setText( "" ); //$NON-NLS-1$

					List columnValueList = getColumnValueList();
					if (columnValueList.isEmpty())
						return;
					SelectParameterDefaultValueDialog dialog = new SelectParameterDefaultValueDialog(
							Display.getCurrent().getActiveShell(),
							Messages.getString("SelectParameterDefaultValueDialog.Title")); //$NON-NLS-1$
					dialog.setColumnValueList(columnValueList, getSelectedDataType());
					int status = dialog.open();
					if (status == Window.OK) {
						String[] selectedValues = dialog.getSelectedValue();
						if (selectedValues != null) {
							for (int i = 0; i < selectedValues.length; i++) {
								String selectedValue = selectedValues[i];
								if (ExpressionType.JAVASCRIPT
										.equals(defaultValueChooser.getData(ExpressionButtonUtil.EXPR_TYPE)))
									selectedValue = getSelectedExprValue(selectedValue);
								defaultValueChooser.setText(DEUtil.resolveNull(selectedValue));
								addDynamicDefaultValue();
							}
						}
					} else if (status == Window.CANCEL) {
						// defaultValueChooser.setText( "" ); //$NON-NLS-1$
					}
				}

			}
		});

		defaultValueChooser.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (!(isMultiple.isEnabled() && isMultiple.getSelection())) {
					String value = defaultValueChooser.getText();
					String type = (String) defaultValueChooser.getData(ExpressionButtonUtil.EXPR_TYPE);
					if (defaultValueList != null)
						defaultValueList.clear();
					String modelValue = UIUtil.convertToModelString(value, false);
					if (modelValue != null) {
						setFirstDefaultValue(modelValue, type);
					}
				} else {
					updateDynamicTableButtons();
				}
				updateMessageLine();
			}

		});

		IExpressionHelper helper = new IExpressionHelper() {

			public String getExpression() {
				if (defaultValueChooser != null)
					return defaultValueChooser.getText();
				else
					return ""; //$NON-NLS-1$
			}

			public void setExpression(String expression) {
				if (defaultValueChooser != null)
					defaultValueChooser.setText(expression);
			}

			public void notifyExpressionChangeEvent(String oldExpression, String newExpression) {
				if (defaultValueChooser != null)
					defaultValueChooser.setFocus();
				if (newExpression != null && newExpression.trim().length() > 0
						&& !newExpression.equals(oldExpression)) {
					addDynamicDefaultValue();
					updateDynamicTableButtons();
				}
			}

			public IExpressionProvider getExpressionProvider() {
				return new ExpressionProvider(selectedParameter);
			}

			public String getExpressionType() {
				return (String) defaultValueChooser.getData(ExpressionButtonUtil.EXPR_TYPE);
			}

			public void setExpressionType(String exprType) {
				defaultValueChooser.setData(ExpressionButtonUtil.EXPR_TYPE, exprType);
				defaultValueChooser.notifyListeners(SWT.Modify, new Event());
			}

			public Object getContextObject() {
				return selectedParameter;
			}

			public IExpressionContextFactory getExpressionContextFactory() {
				return new ExpressionContextFactoryImpl(selectedParameter, getExpressionProvider());
			}

		};
		expressionButton = UIUtil.createExpressionButton(composite, SWT.PUSH);
		expressionButton.setExpressionHelper(helper);
		defaultValueChooser.setData(ExpressionButtonUtil.EXPR_BUTTON, expressionButton);
		defaultValueChooser.setData(ExpressionButtonUtil.EXPR_TYPE, ExpressionType.CONSTANT);
		expressionButton.refresh();

		addValueButton = new Button(composite, SWT.PUSH);
		addValueButton.setText(Messages.getString("CascadingParametersDialog.DefalutValue.Add")); //$NON-NLS-1$
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		addValueButton.setLayoutData(gd);
		addValueButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				addDynamicDefaultValue();
				updateDynamicTableButtons();
			}

		});

		createMulitipleValueListComposite(composite);

		initDefaultValueViewer();
	}

	private void createMulitipleValueListComposite(Composite parent) {
		int tableStyle = SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION;
		Table table = new Table(parent, tableStyle);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		data.heightHint = 100;
		table.setLayoutData(data);
		table.setHeaderVisible(false);
		table.setLinesVisible(true);
		TableColumn column;
		int i;
		String[] columNames = new String[] { Messages.getString("FilterConditionBuilder.list.item1"), //$NON-NLS-1$
		};
		int[] columLength = new int[] { 288 };
		for (i = 0; i < columNames.length; i++) {
			column = new TableColumn(table, SWT.NONE, i);
			column.setText(columNames[i]);
			column.setWidth(columLength[i]);
		}
		table.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				updateDynamicTableButtons();
			}
		});

		table.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.DEL) {
					delTableValue();
				}

			}

			public void keyReleased(KeyEvent e) {
			}

		});
		table.addMouseListener(new MouseAdapter() {

			public void mouseDoubleClick(MouseEvent e) {
				editTableValue();
			}
		});

		defaultValueViewer = new TableViewer(table);
		defaultValueViewer.setUseHashlookup(true);
		defaultValueViewer.setColumnProperties(columNames);
		defaultValueViewer.setLabelProvider(tableLableProvier);
		defaultValueViewer.setContentProvider(tableContentProvider);

		rightButtonsPart = new Composite(parent, SWT.NONE);
		data = new GridData(GridData.FILL_VERTICAL);
		rightButtonsPart.setLayoutData(data);
		GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 0;
		rightButtonsPart.setLayout(layout);

		editValueBtn = new Button(rightButtonsPart, SWT.PUSH);
		editValueBtn.setText(Messages.getString("FilterConditionBuilder.button.edit")); //$NON-NLS-1$
		editValueBtn.setToolTipText(Messages.getString("FilterConditionBuilder.button.edit.tooltip")); //$NON-NLS-1$
		setButtonLayoutData(editValueBtn);
		editValueBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				editTableValue();
			}

		});

		delValueBtn = new Button(rightButtonsPart, SWT.PUSH);
		delValueBtn.setText(Messages.getString("FilterConditionBuilder.button.delete")); //$NON-NLS-1$
		delValueBtn.setToolTipText(Messages.getString("FilterConditionBuilder.button.delete.tooltip")); //$NON-NLS-1$
		setButtonLayoutData(delValueBtn);
		delValueBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				delTableValue();
			}

		});

		delAllValuesBtn = new Button(rightButtonsPart, SWT.PUSH);
		delAllValuesBtn.setText(Messages.getString("FilterConditionBuilder.button.deleteall")); //$NON-NLS-1$
		delAllValuesBtn.setToolTipText(Messages.getString("FilterConditionBuilder.button.deleteall.tooltip")); //$NON-NLS-1$
		setButtonLayoutData(delAllValuesBtn);
		delAllValuesBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				int count = defaultValueList.size();
				if (count > 0) {
					defaultValueList.clear();
					defaultValueViewer.refresh();
					updateDynamicTableButtons();
				} else {
					delAllValuesBtn.setEnabled(false);
				}
			}

		});
	}

	protected void delTableValue() {
		int index = defaultValueViewer.getTable().getSelectionIndex();
		if (index > -1) {
			defaultValueList.remove(index);
			defaultValueViewer.refresh();
			if (defaultValueList.size() > 0) {
				if (defaultValueList.size() <= index) {
					index = index - 1;
				}
				defaultValueViewer.getTable().select(index);
			}
			updateDynamicTableButtons();
		} else {
			delValueBtn.setEnabled(false);
		}
	}

	protected void editTableValue() {

		IStructuredSelection selection = (IStructuredSelection) defaultValueViewer.getSelection();
		if (selection.getFirstElement() != null && selection.getFirstElement() instanceof Expression) {
			Expression expression = (Expression) selection.getFirstElement();

			ExpressionProvider provider = new ExpressionProvider(selectedParameter);

			ExpressionEditor editor = new ExpressionEditor(
					Messages.getString("CascadingParametersDialog.ExpressionEditor.Title")); //$NON-NLS-1$
			editor.setInput(selectedParameter, provider, true);
			editor.setExpression(expression);
			if (editor.open() == OK) {
				Expression value = editor.getExpression();
				if (DEUtil.resolveNull(value.getStringExpression()).length() == 0) {
					MessageDialog.openInformation(getShell(), Messages.getString("MapRuleBuilderDialog.MsgDlg.Title"), //$NON-NLS-1$
							Messages.getString("MapRuleBuilderDialog.MsgDlg.Msg")); //$NON-NLS-1$
					return;
				}
				int index = defaultValueViewer.getTable().getSelectionIndex();
				defaultValueList.remove(index);
				defaultValueList.add(index, value);
				defaultValueViewer.refresh();
				defaultValueViewer.getTable().select(index);
			}
			updateDynamicTableButtons();
		} else {
			editValueBtn.setEnabled(false);
		}

	}

	private void initDefaultValueViewer() {
		if (defaultValueViewer != null) {
			if (isMultiple != null && isMultiple.isEnabled()) {
				WidgetUtil.setExcludeGridData(defaultValueViewer.getTable(), !isMultiple.getSelection());
				WidgetUtil.setExcludeGridData(rightButtonsPart, !isMultiple.getSelection());
				WidgetUtil.setExcludeGridData(addValueButton, !isMultiple.getSelection());
				if (isMultiple.getSelection()) {
					defaultValueViewer.setInput(defaultValueList);
					updateDynamicTableButtons();
				}
			} else {
				WidgetUtil.setExcludeGridData(defaultValueViewer.getTable(), true);
				WidgetUtil.setExcludeGridData(rightButtonsPart, true);
				WidgetUtil.setExcludeGridData(addValueButton, true);
			}

			addValueButton.getParent().layout();
			defaultValueViewer.getTable().getParent().layout();

			Point size = mainContent.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			mainContent.setSize(size);

			mainContent.getParent().layout();

			int y = getShell().computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
			int screecY = Display.getDefault().getClientArea().height;
			if (y < screecY) {
				if (isMultiple != null && isMultiple.isEnabled() && isMultiple.getSelection()) {
					getShell().pack();

					int realY = getShell().toDisplay(getShell().getBounds().width, getShell().getBounds().height).y;
					if (realY > screecY) {
						getShell().setLocation(getShell().getLocation().x,
								getShell().getLocation().y + screecY - realY);
					}
				}
			}

		}
	}

	private List getColumnList() {
		List columnList = new ArrayList();
		DataSetHandle dataSetHandle = getDataSet(selectedParameter);
		try {
			columnList = DataUtil.getColumnList(dataSetHandle);
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
		return (columnList.isEmpty()) ? Collections.EMPTY_LIST : columnList;
	}

	private List getColumnValueList() {
		ArrayList valueList = new ArrayList();

		DataSetHandle dataSet = getDataSet(selectedParameter);

		try {
			String queryExpr = selectedParameter.getValueExpr();

			// Flow mode PARAM_EVALUATION_FLOW is propagated to data engine execution to
			// exclude filters defined on data set.
			valueList.addAll(SelectValueFetcher.getSelectValueList(new Expression(queryExpr, ExpressionType.JAVASCRIPT),
					dataSet, DataEngineFlowMode.PARAM_EVALUATION_FLOW));

		} catch (Exception e) {
			ExceptionHandler.handle(e);
			return Collections.EMPTY_LIST;
		}
		java.util.Collections.sort(valueList);
		return valueList;
	}

	private void createOptionsPart(Composite parent) {
		optionsGroup = new Group(parent, SWT.NULL);
		optionsGroup.setText(LABEL_GROUP_MORE_OPTIONS);
		optionsGroup.setLayout(new GridLayout(2, false));
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		optionsGroup.setLayoutData(gd);

		createLabel(optionsGroup, LABEL_HELP_TEXT, maxStrLengthOption);

		helpTextEditor = new Text(optionsGroup, SWT.BORDER);
		helpTextEditor.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label lable = new Label(optionsGroup, SWT.NULL);
		lable.setText(LABEL_FORMAT_AS);
		lable.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));

		Composite formatArea = new Composite(optionsGroup, SWT.NONE);
		formatArea.setLayout(UIUtil.createGridLayoutWithoutMargin(2, false));
		formatArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		formatField = new Text(formatArea, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		formatField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		changeFormat = new Button(formatArea, SWT.PUSH);
		changeFormat.setText(LABEL_CHANGE_FORMAT_BUTTON);
		setButtonLayoutData(changeFormat);
		changeFormat.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				popupFormatBuilder(true);
			}

		});

		Group preview = new Group(formatArea, SWT.NULL);
		preview.setText(LABEL_PREVIEW_WITH_FORMAT);
		preview.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		preview.setLayout(new GridLayout());
		previewLable = new CLabel(preview, SWT.CENTER | SWT.HORIZONTAL | SWT.VIRTUAL);
		previewLable.setText(""); //$NON-NLS-1$
		previewLable.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createLabel(optionsGroup, LABEL_LIST_LIMIT, maxStrLengthOption);

		Composite composite = new Composite(optionsGroup, SWT.NULL);
		composite.setLayout(UIUtil.createGridLayoutWithoutMargin(3, false));
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Composite limitArea = new Composite(composite, SWT.NULL);
		limitArea.setLayout(UIUtil.createGridLayoutWithoutMargin(2, false));
		limitArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		listLimit = new Text(limitArea, SWT.BORDER);
		GridData gridData = new GridData();
		gridData.widthHint = 80;
		listLimit.setLayoutData(gridData);

		listLimit.addVerifyListener(new VerifyListener() {

			public void verifyText(VerifyEvent e) {
				e.doit = ("0123456789\0\b\u007f".indexOf(e.character) != -1); //$NON-NLS-1$
			}
		});
		listLimit.addModifyListener(new ModifyListener() {

			private String oldValue = ""; //$NON-NLS-1$

			public void modifyText(ModifyEvent e) {
				try {
					if (!StringUtil.isBlank(listLimit.getText())) {
						Integer.parseInt(listLimit.getText());
						oldValue = listLimit.getText();
					}
				} catch (NumberFormatException e1) {
					ExceptionHandler.openErrorMessageBox(ERROR_TITLE_INVALID_LIST_LIMIT, MessageFormat.format(
							ERROR_MSG_INVALID_LIST_LIMIT, new Object[] { Integer.toString(Integer.MAX_VALUE) }));
					listLimit.setText(oldValue);
				}
			}
		});
		new Label(limitArea, SWT.NONE).setText(LABEL_VALUES);

		isRequired = new Button(composite, SWT.CHECK);
		isRequired.setText(BUTTON_IS_REQUIRED);
		isRequired.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		isRequired.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				isRequiredChange(isRequired.getSelection());
				if (selectedParameter != null) {
					try {
						selectedParameter.setIsRequired(isRequired.getSelection());
					} catch (SemanticException e1) {
						ExceptionHandler.handle(e1);
					}
				}

			}

		});

		isMultiple = new Button(composite, SWT.CHECK);
		isMultiple.setText(Messages.getString("CascadingParametersDialog.Button.IsMultiple")); //$NON-NLS-1$
		isMultiple.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		isMultiple.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (selectedParameter != null) {
					try {
						if (isMultiple.getSelection())
							selectedParameter.setParamType(DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE);
						else
							selectedParameter.setParamType(DesignChoiceConstants.SCALAR_PARAM_TYPE_SIMPLE);

						if (!isMultiple.getSelection()) {
							if (defaultValueList != null && defaultValueList.size() > 0) {
								Expression expression = getFirstDefaultValue();
								defaultValueList.clear();
								defaultValueList.add(expression);
							}
						}
						initDefaultValueViewer();
					} catch (SemanticException e1) {
						ExceptionHandler.handle(e1);
					}
				}

			}

		});
	}

	private void isRequiredChange(boolean isRequired) {
		if (getSelectedDataType().equals(DesignChoiceConstants.PARAM_TYPE_STRING)) {
			clearDefaultValueChooser(isRequired);
		}
	}

	private void clearDefaultValueChooser(boolean isChecked) {
		if (isChecked) {
			clearDefaultValueText();
			clearDefaultValueChooserSelections();
		} else {
			if (defaultValueChooser == null || defaultValueChooser.isDisposed()
					|| defaultValueChooser.getItemCount() > 0)
				return;
			// defaultValueChooser.add( CHOICE_NULL_VALUE );
			// defaultValueChooser.add( CHOICE_BLANK_VALUE );
		}
	}

	private void createLabel(Composite parent, String content, int width) {
		Label label = new Label(parent, SWT.NONE);
		setLabelLayoutData(label, width);
		if (content != null) {
			label.setText(content);
		}
	}

	private void setLabelLayoutData(Control control, int width) {
		GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.widthHint = width;
		control.setLayoutData(gd);
	}

	/*
	 * set input for dialog
	 */
	public void setInput(Object input) {
		Assert.isLegal(input instanceof CascadingParameterGroupHandle);
		inputParameterGroup = (CascadingParameterGroupHandle) input;
	}

	// initiate dialog
	protected boolean initDialog() {
		cascadingNameEditor.setText(inputParameterGroup.getName());
		promptTextEditor.setText(UIUtil.convertToGUIString(inputParameterGroup.getPromptText()));

		if (DesignChoiceConstants.DATA_SET_MODE_MULTIPLE.equals(inputParameterGroup.getDataSetMode())) {
			multiDataSet.setSelection(true);
		} else {
			singleDataSet.setSelection(true);
		}

		valueTable.setInput(inputParameterGroup);

		initSorttingArea();
		updateButtons();

		refreshParameterProperties();

		Point size = mainContent.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		mainContent.setSize(size);

		return true;
	}

	private void validateDefaultValues() throws BirtException {
		ArrayList elementsList = new ArrayList(inputParameterGroup.getParameters().getContents());
		if (elementsList == null || elementsList.size() == 0) {
			return;
		}

		for (Iterator iter = elementsList.iterator(); iter.hasNext();) {
			ScalarParameterHandle handle = (ScalarParameterHandle) iter.next();
			String tempDefaultValue = handle.getDefaultValue();
			String tempType = handle.getDataType();

			if (DesignChoiceConstants.PARAM_TYPE_STRING.endsWith(tempType)
					|| DesignChoiceConstants.PARAM_TYPE_BOOLEAN.endsWith(tempType)) {
				continue;
			}

			if (!((DesignChoiceConstants.PARAM_TYPE_STRING.endsWith(tempType))
					|| (DesignChoiceConstants.PARAM_TYPE_BOOLEAN.endsWith(tempType)))) {
				if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(tempType)) {
					tempDefaultValue = convertToStandardFormat(DataTypeUtil.toDate(tempDefaultValue));
				} else if (DesignChoiceConstants.PARAM_TYPE_DATE.equals(tempType)) {
					tempDefaultValue = convertToStandardFormat(DataTypeUtil.toSqlDate(tempDefaultValue));
				} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equals(tempType)) {
					tempDefaultValue = convertToStandardFormat(DataTypeUtil.toSqlTime(tempDefaultValue));
				}

				ParameterValidationUtil.validate(tempType, STANDARD_DATE_TIME_PATTERN, tempDefaultValue,
						ULocale.getDefault());

			}

		}
	}

	// ok pressed
	protected void okPressed() {
		try {
			saveParameterProperties();
			saveSortingProperties();
			// Validate default value first -- begin -- bug 164765
			validateDefaultValues();
			// Validate default value first -- end --
			inputParameterGroup.setName(UIUtil.convertToModelString(cascadingNameEditor.getText(), true));
			inputParameterGroup.setPromptText(promptTextEditor.getText());

			if (isSingle()) {
				inputParameterGroup.setDataSetMode(DesignChoiceConstants.DATA_SET_MODE_SINGLE);
			} else {
				inputParameterGroup.setDataSetMode(DesignChoiceConstants.DATA_SET_MODE_MULTIPLE);
			}
		} catch (BirtException e) {
			ExceptionHandler.handle(e);
			refreshParameterProperties();
			initSorttingArea();
			return;
		}
		setResult(inputParameterGroup);
		super.okPressed();

	}

	private void deleteRow() {
		int index = valueTable.getTable().getSelectionIndex();
		boolean setBtnEnable = true;
		if (index == -1) {
			setBtnEnable = false;
		}

		editBtn.setEnabled(setBtnEnable);
		delBtn.setEnabled(setBtnEnable);

		ScalarParameterHandle choice = (ScalarParameterHandle) ((IStructuredSelection) valueTable.getSelection())
				.getFirstElement();

		if (choice == null) {
			return;
		}
		try {
			inputParameterGroup.getParameters().drop(choice);
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
			return;
		}
		refreshValueTable();

		index--;
		if (index < 0 && valueTable.getTable().getItemCount() > 1) {
			index = 0;
		}
		StructuredSelection selection = null;
		if (index != -1) {
			selection = new StructuredSelection(valueTable.getTable().getItem(index).getData());
			this.selectedParameter = (ScalarParameterHandle) ((IStructuredSelection) valueTable.getSelection())
					.getFirstElement();
		} else {
			selection = StructuredSelection.EMPTY;
			this.selectedParameter = null;
		}
		valueTable.setSelection(selection);

		refreshParameterProperties();
		initSorttingArea();
		updateButtons();
	}

	private String[] getDataSetColumns(ScalarParameterHandle handle, boolean needFilter) {

		DataSetHandle dataSet = getDataSet(handle);
		if (dataSet == null) {
			return new String[0];
		}
		CachedMetaDataHandle metaHandle = dataSet.getCachedMetaDataHandle();
		if (metaHandle == null) {
			try {
				metaHandle = DataSetUIUtil.getCachedMetaDataHandle(dataSet);
			} catch (SemanticException e) {
				ExceptionHandler.handle(e);
				return new String[0];
			}
		}
		if (metaHandle == null || metaHandle.getResultSet() == null) {
			return new String[0];
		}
		ArrayList valueList = new ArrayList();
		List dataTypeList = new ArrayList();
		for (Iterator iter = metaHandle.getResultSet().iterator(); iter.hasNext();) {
			ResultSetColumnHandle columnHandle = (ResultSetColumnHandle) iter.next();
			if (!needFilter || matchDataType(handle, columnHandle)) {
				valueList.add(columnHandle.getColumnName());
				dataTypeList.add(columnHandle.getDataType());
			}
		}
		return (String[]) valueList.toArray(new String[0]);
	}

	private DataSetHandle getDataSet(ScalarParameterHandle handle) {
		if (!isSingle()) {
			if (handle != null && handle.getDataSet() != null) {
				return handle.getDataSet();
			}
			return null;
		}
		return inputParameterGroup.getDataSet();
	}

	private IStructuredContentProvider contentProvider = new IStructuredContentProvider() {

		public Object[] getElements(Object inputElement) {
			ArrayList elementsList = new ArrayList(inputParameterGroup.getParameters().getContents());
			for (Iterator iter = elementsList.iterator(); iter.hasNext();) {
				ScalarParameterHandle handle = (ScalarParameterHandle) iter.next();

				String[] columns = getDataSetColumns(handle, false);
				boolean found = false;
				for (int i = 0; i < columns.length; i++) {
					if (DEUtil.getColumnExpression(columns[i]).equals(handle.getValueExpr())
							|| DEUtil.getResultSetColumnExpression(columns[i]).equals(handle.getValueExpr())) {
						found = true;
						break;
					}
				}
				if (!found) {
					try {
						handle.setValueExpr(null);
					} catch (SemanticException e) {
						ExceptionHandler.handle(e);
					}
				}
			}
			return elementsList.toArray();
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

	};

	private ITableLabelProvider labelProvider = new ITableLabelProvider() {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			String value = null;
			ScalarParameterHandle paramHandle = null;

			if (element instanceof ScalarParameterHandle) {
				paramHandle = (ScalarParameterHandle) element;
			}

			{
				switch (columnIndex) {
				case 0: {

					String paramName;
					if (paramHandle != selectedParameter) {
						paramName = paramHandle.getName();
					} else {
						paramName = paramNameEditor.getText().trim();
					}
					value = getDummyText(paramHandle) + paramName;

					break;
				}
				case 1: {
					DataSetHandle dataSet = null;
					if (isSingle()) {
						if (paramHandle != getFirstParameter()) {
							break;
						}
						dataSet = inputParameterGroup.getDataSet();
					} else {
						dataSet = getDataSet(paramHandle);
					}
					if (dataSet == null) {
						value = LABEL_SELECT_DATA_SET;
					} else {
						value = dataSet.getName();
					}
					break;
				}
				case 2: {
					if (paramHandle.getValueExpr() != null) {
						value = getColumnName(paramHandle, COLUMN_VALUE);
					} else if (getDataSetColumns(paramHandle, true).length > 0) {
						value = LABEL_SELECT_VALUE_COLUMN;
					} else {
						value = LABEL_NO_COLUMN_AVAILABLE;
					}
					break;
				}
				case 3: {
					value = getColumnName(paramHandle, COLUMN_DISPLAY_TEXT);
					if (value == null) {
						if (getDataSetColumns(paramHandle, false).length > 0) {
							value = LABEL_SELECT_DISPLAY_COLUMN;
						} else {
							value = LABEL_NO_COLUMN_AVAILABLE;
						}

					}
					break;
				}
				}
			}

			if (value == null) {
				value = ""; //$NON-NLS-1$
			}
			return value;
		}

		private String getDummyText(Object element) {
			// String dummyText = ""; //$NON-NLS-1$
			StringBuffer buffer = new StringBuffer();
			int index = getTableIndex(element);
			for (int i = 0; i < index; i++) {
				buffer.append("    "); //$NON-NLS-1$
			}

			return buffer.toString();
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
	private Composite mainContent;
	private TableViewer defaultValueViewer;
	private Button addValueButton;
	private Button editValueBtn;
	private Button delValueBtn;
	private Button delAllValuesBtn;
	private ExpressionButton expressionButton;
	private Composite rightButtonsPart;

	protected int getTableIndex(Object element) {
		Object[] input = ((IStructuredContentProvider) valueTable.getContentProvider())
				.getElements(valueTable.getInput());

		int index = 0;

		for (int i = 0; i < input.length; i++) {
			if (element == input[i]) {
				index = i;
				break;
			}
		}

		return index;
	}

	private void refreshValueTable() {
		if (valueTable != null && !valueTable.getTable().isDisposed()) {
			valueTable.refresh();
		}
	}

	private void refreshParameterProperties() {
		if (selectedParameter == null) {
			clearParamProperties();
			setControlEnabled(false);
			initDefaultValueViewer();
			return;
		}

		setControlEnabled(true);

		paramNameEditor.setText(selectedParameter.getName());

		if (selectedParameter.getPromptText() == null) {
			promptText.setText(""); //$NON-NLS-1$
		} else {
			promptText.setText(selectedParameter.getPromptText());
		}

		dataTypeChooser.setText(DATA_TYPE_CHOICE_SET.findChoice(selectedParameter.getDataType()).getDisplayName());

		if (getInputDisplayName() == null) {
			displayTypeChooser.clearSelection();
		} else {
			displayTypeChooser.setText(getInputDisplayName());
		}

		Expression expression = getFirstDefaultValue();
		String defaultValue = expression == null ? null : expression.getStringExpression();
		String expressionType = expression == null ? null : expression.getType();

		if (expressionType != null) {
			defaultValueChooser.setData(ExpressionButtonUtil.EXPR_TYPE, expressionType);
			ExpressionButton button = (ExpressionButton) defaultValueChooser.getData(ExpressionButtonUtil.EXPR_BUTTON);
			if (button != null)
				button.refresh();
		}

		if (getSelectedDataType().equals(DesignChoiceConstants.PARAM_TYPE_STRING)) {
			defaultValueChooser.setText(DEUtil.resolveNull(defaultValue));

		} else if (defaultValue != null) {
			defaultValueChooser.setText(defaultValue);
		} else {
			defaultValueChooser.setText("");
		}

		helpTextEditor.setText(UIUtil.convertToGUIString(selectedParameter.getHelpText()));

		if (selectedParameter.getPropertyHandle(ScalarParameterHandle.LIST_LIMIT_PROP).isSet()) {
			listLimit.setText(String.valueOf(selectedParameter.getListlimit()));
		} else {
			listLimit.setText(""); //$NON-NLS-1$
		}

		// allowNull.setSelection( selectedParameter.allowNull( ) );
		isRequired.setSelection(selectedParameter.isRequired());

		if (selectedParameter == inputParameterGroup.getParameters()
				.get(inputParameterGroup.getParameters().getCount() - 1) && selectedParameter.isMustMatch())// Add
																											// isMustMatch
																											// expression
																											// to
																											// control
																											// isMultiple
																											// disable
																											// if
																											// display
																											// type is
																											// combo box
																											// when
																											// parameter
																											// selected.
		{
			isMultiple.setEnabled(true);
			isMultiple.setSelection(
					DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE.equals(selectedParameter.getParamType()));

		} else {
			isMultiple.setEnabled(false);
			isMultiple.setSelection(false);
		}

		defaultValueList = selectedParameter.getDefaultValueList();
		initDefaultValueViewer();

		changeDataType(selectedParameter.getDataType());

		formatCategroy = selectedParameter.getCategory();
		formatPattern = selectedParameter.getPattern();
		updateFormatField();
	}

	private void clearParamProperties() {
		paramNameEditor.setText(""); //$NON-NLS-1$
		promptText.setText(""); //$NON-NLS-1$
		dataTypeChooser.select(-1);
		displayTypeChooser.select(-1);
		defaultValueChooser.setText(""); //$NON-NLS-1$
		helpTextEditor.setText(""); //$NON-NLS-1$
		formatField.setText(""); //$NON-NLS-1$
		listLimit.setText(""); //$NON-NLS-1$

		previewLable.setText(""); //$NON-NLS-1$
		// allowNull.setSelection( false );
		isRequired.setSelection(false);
	}

	private void setControlEnabled(boolean enable) {
		paramNameEditor.setEnabled(enable);
		promptText.setEnabled(enable);
		dataTypeChooser.setEnabled(enable);
		displayTypeChooser.setEnabled(enable);
		defaultValueChooser.setEnabled(enable);
		helpTextEditor.setEnabled(enable);
		formatField.setEnabled(enable);
		listLimit.setEnabled(enable);
		changeFormat.setEnabled(enable);
		isRequired.setEnabled(enable);
		isMultiple.setEnabled(enable);
		if (!isMultiple.isEnabled())
			isMultiple.setSelection(false);
		expressionButton.setEnabled(enable);
	}

	private void changeDataType(String type) {
		// if ( type.equals( lastDataType ) )
		// return;
		if (type.equals(DesignChoiceConstants.PARAM_TYPE_STRING)) {
			clearDefaultValueChooser(isRequired.getSelection());
		} else {
			clearDefaultValueText();
			clearDefaultValueChooserSelections();
		}
		// lastDataType = type;
		initFormatField(type);
		refreshValueTable();
		updateButtons();
	}

	private void clearDefaultValueText() {
		if (defaultValueChooser == null || defaultValueChooser.isDisposed())
			return;
		String textValue = defaultValueChooser.getText();
		if (textValue != null && (textValue.equals(CHOICE_NULL_VALUE) || textValue.equals(CHOICE_BLANK_VALUE))) {
			defaultValueChooser.setText(""); //$NON-NLS-1$
		}
	}

	private void clearDefaultValueChooserSelections() {
		if (defaultValueChooser == null || defaultValueChooser.isDisposed())
			return;
		if (defaultValueChooser.getItemCount() > 1) {
			defaultValueChooser.remove(1, defaultValueChooser.getItemCount() - 1);
		}
	}

	private void initFormatField(String selectedDataType) {
		IChoiceSet choiceSet = getFormatChoiceSet(selectedDataType);
		if (choiceSet == null) {
			formatCategroy = formatPattern = null;
		} else {
			if ((formatCategroy != null && choiceSet.findChoice(formatCategroy) == null)
					|| (selectedParameter.getCategory() == null && selectedParameter.getPattern() == null)) {
				if (DesignChoiceConstants.PARAM_TYPE_STRING.equals(selectedDataType)) {
					formatCategroy = choiceSet.findChoice(DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED)
							.getName();
				} else if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(selectedDataType)) {
					formatCategroy = choiceSet.findChoice(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_UNFORMATTED)
							.getName();
				} else if (DesignChoiceConstants.PARAM_TYPE_DATE.equals(selectedDataType)) {
					formatCategroy = choiceSet.findChoice(DesignChoiceConstants.DATE_FORMAT_TYPE_UNFORMATTED).getName();
				} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equals(selectedDataType)) {
					formatCategroy = choiceSet.findChoice(DesignChoiceConstants.DATE_FORMAT_TYPE_UNFORMATTED).getName();
				} else if (DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals(selectedDataType)
						|| DesignChoiceConstants.PARAM_TYPE_FLOAT.equals(selectedDataType)
						|| DesignChoiceConstants.PARAM_TYPE_INTEGER.equals(selectedDataType)) {
					formatCategroy = choiceSet.findChoice(DesignChoiceConstants.NUMBER_FORMAT_TYPE_UNFORMATTED)
							.getName();
				}
				formatPattern = null;
			} else {
				formatCategroy = selectedParameter.getCategory();
				if (formatCategroy == null) {// back compatible
					formatCategroy = DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED;
				}
				formatPattern = selectedParameter.getPattern();

				Object formatValue = selectedParameter.getProperty(IScalarParameterModel.FORMAT_PROP);
				if (formatValue instanceof FormatValue) {
					PropertyHandle propHandle = selectedParameter.getPropertyHandle(IScalarParameterModel.FORMAT_PROP);
					FormatValue formatValueToSet = (FormatValue) formatValue;
					FormatValueHandle formatHandle = (FormatValueHandle) formatValueToSet.getHandle(propHandle);
					formatLocale = formatHandle.getLocale();
				}
			}
		}
		updateFormatField();
	}

	private String getInputDisplayName() {
		String displayName = null;
		if (DesignChoiceConstants.PARAM_CONTROL_LIST_BOX.equals(selectedParameter.getControlType())) {
			if (selectedParameter.isMustMatch()) {
				// displayName = DISPLAY_NAME_CONTROL_COMBO;
				displayName = DISPLAY_NAME_CONTROL_LIST;
			} else {
				// displayName = DISPLAY_NAME_CONTROL_LIST;
				displayName = DISPLAY_NAME_CONTROL_COMBO;
			}
		}
		return displayName;
	}

	private IChoiceSet getFormatChoiceSet(String type) {
		IChoiceSet choiceSet = null;
		if (DesignChoiceConstants.PARAM_TYPE_STRING.equals(type)) {
			choiceSet = DEUtil.getMetaDataDictionary().getChoiceSet(DesignChoiceConstants.CHOICE_STRING_FORMAT_TYPE);
		} else if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(type)) {
			choiceSet = DEUtil.getMetaDataDictionary().getChoiceSet(DesignChoiceConstants.CHOICE_DATETIME_FORMAT_TYPE);
		} else if (DesignChoiceConstants.PARAM_TYPE_DATE.equals(type)) {
			choiceSet = DEUtil.getMetaDataDictionary().getChoiceSet(DesignChoiceConstants.CHOICE_DATE_FORMAT_TYPE);
		} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equals(type)) {
			choiceSet = DEUtil.getMetaDataDictionary().getChoiceSet(DesignChoiceConstants.CHOICE_TIME_FORMAT_TYPE);
		} else if (DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals(type)
				|| DesignChoiceConstants.PARAM_TYPE_FLOAT.equals(type)
				|| DesignChoiceConstants.PARAM_TYPE_INTEGER.equals(type)) {
			choiceSet = DEUtil.getMetaDataDictionary().getChoiceSet(DesignChoiceConstants.CHOICE_NUMBER_FORMAT_TYPE);
		}
		return choiceSet;
	}

	private String getSelectedDataType() {
		String type = null;
		if (StringUtil.isBlank(dataTypeChooser.getText())) {
			if (selectedParameter != null) {
				type = selectedParameter.getDataType();
			} else {
				type = DesignChoiceConstants.PARAM_TYPE_STRING;
			}
		} else {
			IChoice choice = DATA_TYPE_CHOICE_SET.findChoiceByDisplayName(dataTypeChooser.getText());
			type = choice.getName();
		}
		return type;
	}

	/**
	 * Gets the internal name of the control type from the display name
	 */
	private String getSelectedDisplayType() {
		String displayText = displayTypeChooser.getText();
		if (displayText.length() == 0) {
			return null;
		}
		if (DISPLAY_NAME_CONTROL_COMBO.equals(displayText)) {
			return PARAM_CONTROL_COMBO;
		}
		if (DISPLAY_NAME_CONTROL_LIST.equals(displayText)) {
			return PARAM_CONTROL_LIST;
		}
		return null;
	}

	private void popupFormatBuilder(boolean refresh) {
		String dataType = getSelectedDataType();
		int formatType;
		if (DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals(dataType)) {
			return;
		}
		if (DesignChoiceConstants.PARAM_TYPE_STRING.equals(dataType)) {
			formatType = FormatBuilder.STRING;
		} else if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(dataType)) {
			formatType = FormatBuilder.DATETIME;
		} else if (DesignChoiceConstants.PARAM_TYPE_DATE.equals(dataType)) {
			formatType = FormatBuilder.DATE;
		} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equals(dataType)) {
			formatType = FormatBuilder.TIME;
		} else {
			formatType = FormatBuilder.NUMBER;
		}
		FormatBuilder formatBuilder = new FormatBuilder(formatType);
		formatBuilder.setInputFormat(formatCategroy, formatPattern, formatLocale);
		// formatBuilder.setPreviewText( defaultValue );
		if (formatBuilder.open() == OK) {
			formatCategroy = (String) ((Object[]) formatBuilder.getResult())[0];
			formatPattern = (String) ((Object[]) formatBuilder.getResult())[1];
			formatLocale = (ULocale) ((Object[]) formatBuilder.getResult())[2];
			updateFormatField();
			try {
				saveParameterProperties();
			} catch (SemanticException e) {
				ExceptionHandler.handle(e);
			}
		}
	}

	private void updateFormatField() {
		String displayFormat;
		IChoiceSet choiceSet = getFormatChoiceSet(getSelectedDataType());
		if (choiceSet == null) {// Boolean type;
			displayFormat = DEUtil.getMetaDataDictionary().getChoiceSet(DesignChoiceConstants.CHOICE_STRING_FORMAT_TYPE)
					.findChoice(DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED).getDisplayName();
		} else {
			if (formatCategroy == null || choiceSet.findChoice(formatCategroy) == null)
				return;
			displayFormat = choiceSet.findChoice(formatCategroy).getDisplayName();
			if (isCustom()) {
				displayFormat += ":  " + formatPattern; //$NON-NLS-1$
			}
		}
		formatField.setText("" + displayFormat); //$NON-NLS-1$
		changeFormat.setEnabled(choiceSet != null);

		if (selectedParameter != null) {
			ULocale locale = formatLocale;
			if (locale == null)
				locale = ULocale.getDefault();
			doPreview(isCustom() ? formatPattern : formatCategroy, locale);
		}
	}

	private boolean isCustom() {
		if (DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM.equals(formatCategroy)
				|| DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM.equals(formatCategroy)
				|| DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM.equals(formatCategroy)
				|| DesignChoiceConstants.DATE_FORMAT_TYPE_CUSTOM.equals(formatCategroy)
				|| DesignChoiceConstants.TIME_FORMAT_TYPE_CUSTOM.equals(formatCategroy)
				|| DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY.equals(formatCategroy)) {
			return true;
		}
		return false;
	}

	private void doPreview(String pattern, ULocale locale) {
		String type = getSelectedDataType();

		String formatStr = ""; //$NON-NLS-1$
		if (DesignChoiceConstants.PARAM_TYPE_STRING.equals(type)) {
			formatStr = new StringFormatter(pattern, locale).format(DEFAULT_PREVIEW_STRING);
		} else if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(type)) {
			pattern = pattern.equals(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_UNFORMATTED)
					? DateFormatter.DATETIME_UNFORMATTED
					: pattern;
			formatStr = new DateFormatter(pattern, locale).format(new Date());
		} else if (DesignChoiceConstants.PARAM_TYPE_DATE.equals(type)) {
			pattern = pattern.equals(DesignChoiceConstants.DATE_FORMAT_TYPE_UNFORMATTED)
					? DateFormatter.DATE_UNFORMATTED
					: pattern;
			formatStr = new DateFormatter(pattern, locale).format(new Date());
		} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equals(type)) {
			pattern = pattern.equals("Unformatted") ? DateFormatter.TIME_UNFORMATTED //$NON-NLS-1$
					: pattern;
			formatStr = new DateFormatter(pattern, locale).format(new Date());
		} else if (DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals(type)
				|| DesignChoiceConstants.PARAM_TYPE_FLOAT.equals(type)) {
			formatStr = new NumberFormatter(pattern, locale).format(DEFAULT_PREVIEW_NUMBER);
		} else if (DesignChoiceConstants.PARAM_TYPE_INTEGER.equals(type)) {
			formatStr = new NumberFormatter(pattern, locale).format(DEFAULT_PREVIEW_INTEGER_NUMBER);
		}
		previewLable.setText(UIUtil.convertToGUIString(formatStr));
	}

	private String getColumnName(ScalarParameterHandle handle, String column) {
		CachedMetaDataHandle cmdh = null;
		try {
			DataSetHandle dataSet = getDataSet(handle);
			if (dataSet == null) {
				return null;
			}
			cmdh = DataSetUIUtil.getCachedMetaDataHandle(dataSet);
		} catch (SemanticException e) {
		}
		String value = null;
		if (COLUMN_VALUE.equals(column)) {
			value = handle.getValueExpr();
		} else {
			value = handle.getLabelExpr();
		}
		if (cmdh != null) {
			for (Iterator iter = cmdh.getResultSet().iterator(); iter.hasNext();) {
				ResultSetColumnHandle element = (ResultSetColumnHandle) iter.next();
				if (DEUtil.getColumnExpression(element.getColumnName()).equalsIgnoreCase(value)
						|| DEUtil.getResultSetColumnExpression(element.getColumnName()).equalsIgnoreCase(value)) {
					return element.getColumnName();
				}
			}
		}

		return null;
	}

	private void updateButtons() {
		int index = valueTable.getTable().getSelectionIndex();
		boolean setBtnEnable = true;
		if (index == -1) {
			setBtnEnable = false;
		}

		editBtn.setEnabled(setBtnEnable);
		delBtn.setEnabled(setBtnEnable);

		boolean okEnable = true;

		if (errorMessageLine != null && !errorMessageLine.isDisposed()) {
			okEnable = (errorMessageLine.getImage() == null);
			if (okEnable == false) {
				getOkButton().setEnabled(okEnable);
				return;
			}
		}

		Iterator iter = inputParameterGroup.getParameters().iterator();
		if (!iter.hasNext()) {
			okEnable = false;
		} else {
			int count = 0;
			while (iter.hasNext()) {
				Object obj = iter.next();
				if (obj instanceof ScalarParameterHandle) {
					ScalarParameterHandle param = (ScalarParameterHandle) obj;
					count++;
					if (!checkParameter(param)) {
						okEnable = false;
						break;
					}
				}
			}
			okEnable &= (count != 0);
		}

		getOkButton().setEnabled(okEnable);

	}

	private boolean matchDataType(ScalarParameterHandle handle, ResultSetColumnHandle columnHandle) {
		String type = handle.getDataType();
		if (handle == selectedParameter && dataTypeChooser.isEnabled()) {
			type = getSelectedDataType();
		}
		if (DesignChoiceConstants.PARAM_TYPE_STRING.equals(type)
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_ANY.equals(columnHandle.getDataType())) {
			return true;
		} else if (DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals(type)) {
			return DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN.equals(columnHandle.getDataType());
		} else if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(type)) {
			return DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME.equals(columnHandle.getDataType());
		} else if (DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals(type)) {
			return DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL.equals(columnHandle.getDataType());
		} else if (DesignChoiceConstants.PARAM_TYPE_FLOAT.equals(type)) {
			return DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT.equals(columnHandle.getDataType());
		} else if (DesignChoiceConstants.PARAM_TYPE_INTEGER.equals(type)) {
			return DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER.equals(columnHandle.getDataType());
		} else if (DesignChoiceConstants.PARAM_TYPE_DATE.equals(columnHandle.getDataType())) {
			return DesignChoiceConstants.COLUMN_DATA_TYPE_DATE.equals(type);
		} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equals(columnHandle.getDataType())) {
			return DesignChoiceConstants.COLUMN_DATA_TYPE_TIME.equals(type);
		}
		return false;
	}

	private void saveParameterProperties() throws SemanticException {
		if (selectedParameter != null) {
			selectedParameter.setPromptText(UIUtil.convertToModelString(promptText.getText(), false));
			selectedParameter.setHelpText(UIUtil.convertToModelString(helpTextEditor.getText(), true));

			selectedParameter.setDefaultValueList(defaultValueList);

			if (StringUtil.isBlank(listLimit.getText())) {
				selectedParameter.setProperty(ScalarParameterHandle.LIST_LIMIT_PROP, null);
			} else {
				selectedParameter.setListlimit(Integer.parseInt(listLimit.getText()));
			}
			// selectedParameter.setAllowNull( allowNull.getSelection( ) );
			selectedParameter.setIsRequired(isRequired.getSelection());
			selectedParameter.setName(UIUtil.convertToModelString(paramNameEditor.getText(), true));

			selectedParameter.setCategory(formatCategroy);
			selectedParameter.setPattern(formatPattern);

			Object value = selectedParameter.getProperty(IScalarParameterModel.FORMAT_PROP);
			if (value instanceof FormatValue) {
				PropertyHandle propHandle = selectedParameter.getPropertyHandle(IScalarParameterModel.FORMAT_PROP);
				FormatValue formatValueToSet = (FormatValue) value;
				FormatValueHandle formatHandle = (FormatValueHandle) formatValueToSet.getHandle(propHandle);
				formatHandle.setLocale(formatLocale);
			}

			refreshValueTable();
		}
		;
	}

	private boolean isSingle() {
		return singleDataSet.getSelection();
	}

	private ScalarParameterHandle getFirstParameter() {
		if (inputParameterGroup.getParameters().getCount() > 0) {
			return (ScalarParameterHandle) inputParameterGroup.getParameters().get(0);
		}
		return null;
	}

	private boolean checkParameter(ScalarParameterHandle paramHandle) {
		if (paramHandle.getValueExpr() == null || getColumnName(paramHandle, COLUMN_VALUE) == null) {
			return false;
		}
		return true;
	}

	class AddEditCascadingParameterDialog extends BaseDialog {

		public final String DATASET_NONE = Messages.getString("CascadingParametersDialog.items.None"); //$NON-NLS-1$

		public final String DISPLAY_TEXT_NONE = Messages.getString("CascadingParametersDialog.items.None"); //$NON-NLS-1$

		private final String ERROR_MSG_DUPLICATED_NAME = Messages
				.getString("ParameterDialog.ErrorMessage.DuplicatedName"); //$NON-NLS-1$

		Text name;
		Combo dataset, value, displayText;
		private String[] dataTypes;
		protected ScalarParameterHandle parameter = null;
		protected CLabel editErrorMessage;

		public ScalarParameterHandle getParameter() {
			return parameter;
		}

		protected AddEditCascadingParameterDialog(String title) {
			super(title);
		}

		protected AddEditCascadingParameterDialog(Shell parentShell, String title) {
			super(parentShell, title);
		}

		protected void updateButtons() {
			if (editErrorMessage != null && !editErrorMessage.isDisposed()) {
				if (editErrorMessage.getImage() != null) {
					getOkButton().setEnabled(false);
					return;
				}
			}

			if ((dataset.getItemCount() == 1 && dataset.getItem(0).equals(DATASET_NONE))
					|| value.getText().length() == 0) {
				getOkButton().setEnabled(false);
			} else {
				getOkButton().setEnabled(true);
			}

		}

		protected void okPressed() {
			if (name.getText().trim().length() != 0) {
				try {
					parameter.setName(name.getText().trim());
				} catch (NameException e) {
					// TODO Auto-generated catch block
					ExceptionHandler.handle(e);
				}
			}

			super.okPressed();
		}

		public void setParameter(ScalarParameterHandle param) {
			parameter = param;
		}

		protected Control createDialogArea(Composite parent) {
			UIUtil.bindHelp(parent, IHelpContextIds.ADD_EDIT_CASCADING_PARAMETER_DIALOG_ID);
			Composite topComposite = (Composite) super.createDialogArea(parent);

			Composite composite = new Composite(topComposite, SWT.NONE);
			composite.setLayout(UIUtil.createGridLayoutWithoutMargin(2, false));
			composite.setLayoutData(new GridData(GridData.FILL_BOTH));

			Label labelName = new Label(composite, SWT.NONE);
			labelName.setText(Messages.getString("AddEditCascadingParameterDialog.label.name")); //$NON-NLS-1$
			name = new Text(composite, SWT.BORDER);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.minimumWidth = 250;
			name.setLayoutData(gd);
			name.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					updateEditErrorMsg();
					updateButtons();
				}

			});

			Label labelDataset = new Label(composite, SWT.NONE);
			labelDataset.setText(Messages.getString("AddEditCascadingParameterDialog.label.dataset")); //$NON-NLS-1$
			dataset = new Combo(composite, SWT.READ_ONLY);
			dataset.setVisibleItemCount(30);
			dataset.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			dataset.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					if (dataset.getSelectionIndex() == -1
							|| (dataset.getItemCount() == 1 && dataset.getText().equals(DATASET_NONE))) {
						value.setEnabled(false);
						displayText.setEnabled(false);
					} else {
						try {
							parameter.setDataSet(DataUtil.findDataSet(dataset.getText()));
							if (parameter.getDataSet() != null) {
								if (getFirstParameter() == null || parameter == getFirstParameter()) {
									inputParameterGroup.setDataSet(parameter.getDataSet());
								}

								updateComboFromDataSet();
							}
						} catch (SemanticException e1) {

						}

					}
					updateButtons();
				}
			});

			Label labelValue = new Label(composite, SWT.NONE);
			labelValue.setText(Messages.getString("AddEditCascadingParameterDialog.label.value")); //$NON-NLS-1$
			value = new Combo(composite, SWT.READ_ONLY);
			value.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			value.setVisibleItemCount(30);
			value.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					try {
						parameter.setValueExpr(DEUtil.getColumnExpression(value.getText()));
						if (dataTypes.length == value.getItemCount() && value.getSelectionIndex() > -1) {
							parameter.setDataType(dataTypes[value.getSelectionIndex()]);
						}
					} catch (SemanticException e1) {
					}
					updateButtons();
				}
			});

			Label labelDisplayText = new Label(composite, SWT.NONE);
			labelDisplayText.setText(Messages.getString("AddEditCascadingParameterDialog.label.displaytext")); //$NON-NLS-1$
			displayText = new Combo(composite, SWT.READ_ONLY);
			displayText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			displayText.setVisibleItemCount(30);
			displayText.addSelectionListener(new SelectionAdapter() {

				public void widgetSelected(SelectionEvent e) {
					try {
						if (displayText.getSelectionIndex() == 0) // it
						// means
						// DISPLAY_TEXT_NONE
						{
							parameter.setLabelExpr(null);
						} else {
							parameter.setLabelExpr(DEUtil.getColumnExpression(displayText.getText()));
						}

					} catch (SemanticException e1) {
						ExceptionHandler.handle(e1);
					}
					updateButtons();
				}
			});

			createLabel(composite, null);
			editErrorMessage = new CLabel(composite, SWT.NONE);
			GridData msgLineGridData = new GridData(GridData.FILL_HORIZONTAL);
			msgLineGridData.horizontalSpan = 2;
			editErrorMessage.setLayoutData(msgLineGridData);

			return topComposite;
		}

		protected void updateEditErrorMsg() {
			String errorMsg = null;
			String paraName = name.getText().trim();
			if (!paraName.equals(parameter.getName()) && parameter.getModuleHandle().findParameter(paraName) != null) {
				errorMsg = ERROR_MSG_DUPLICATED_NAME;
			}
			if (errorMsg != null) {
				editErrorMessage.setText(errorMsg);
				editErrorMessage.setImage(ERROR_ICON);
			} else {
				editErrorMessage.setText(""); //$NON-NLS-1$
				editErrorMessage.setImage(null);
			}
		}

		protected boolean initDialog() {
			dataset.setItems(ChoiceSetFactory.getDataSets());
			if (dataset.getItemCount() == 0) {
				dataset.add(DATASET_NONE);
				dataset.select(0);
			}
			if (isSingle() && (getFirstParameter() != null && getFirstParameter() != parameter)) {
				if (inputParameterGroup.getDataSet() != null) {
					dataset.select(dataset.indexOf(inputParameterGroup.getDataSet().getName()));
					dataset.setEnabled(false);
				}
			}

			updateComboFromDataSet();
			if (parameter.getName().trim().length() != 0) {
				name.setText(parameter.getName());
			}
			updateButtons();
			return super.initDialog();
		}

		private String[] getDataSetColumns(ScalarParameterHandle handle, boolean needFilter) {

			DataSetHandle dataSet = getDataSet(handle);
			if (dataSet == null) {
				return new String[0];
			}
			CachedMetaDataHandle metaHandle = dataSet.getCachedMetaDataHandle();
			if (metaHandle == null) {
				try {
					metaHandle = DataSetUIUtil.getCachedMetaDataHandle(dataSet);
				} catch (SemanticException e) {
					ExceptionHandler.handle(e);
					return new String[0];
				}
			}
			if (metaHandle == null || metaHandle.getResultSet() == null) {
				return new String[0];
			}
			ArrayList valueList = new ArrayList();
			List dataTypeList = new ArrayList();
			for (Iterator iter = metaHandle.getResultSet().iterator(); iter.hasNext();) {
				ResultSetColumnHandle columnHandle = (ResultSetColumnHandle) iter.next();
				valueList.add(columnHandle.getColumnName());
				dataTypeList.add(ModuleUtil.convertColumnTypeToParamType(columnHandle.getDataType()));
			}

			dataTypes = (String[]) dataTypeList.toArray(new String[0]);
			return (String[]) valueList.toArray(new String[0]);
		}

		private void updateComboFromDataSet() {
			value.setEnabled(false);
			displayText.setEnabled(false);
			if (parameter != null) {
				DataSetHandle dataSet = getDataSet(parameter);
				if (dataSet != null) {
					// name.setText( parameter.getName( ) );
					dataset.select(dataset.indexOf(dataSet.getName()));
					value.setItems(getDataSetColumns(parameter, true));
					displayText.removeAll();
					displayText.setItems(getDataSetColumns(parameter, false));
					displayText.add(DISPLAY_TEXT_NONE, 0);

					value.setEnabled(true);
					String temp = getColumnName(parameter, COLUMN_VALUE);
					if (temp != null) {
						value.select(value.indexOf(temp));
					}

					displayText.setEnabled(true);
					temp = getColumnName(parameter, COLUMN_DISPLAY_TEXT);
					if (temp != null) {
						displayText.select(displayText.indexOf(temp));
					} else {
						displayText.select(0);
					}

				}
			} else
			// ( newParameter == null )
			{
				parameter = DesignElementFactory.getInstance().newScalarParameter(null);
				try {
					parameter.setControlType(DesignChoiceConstants.PARAM_CONTROL_LIST_BOX);
					parameter.setValueType(DesignChoiceConstants.PARAM_VALUE_TYPE_DYNAMIC);
				} catch (SemanticException e) {
					ExceptionHandler.handle(e);
				}

				// name.setText( parameter.getName( ) );
				if (getFirstParameter() != null && !(multiDataSet.isEnabled() && multiDataSet.getSelection())) {
					try {
						parameter.setDataSet(inputParameterGroup.getDataSet());
						value.setItems(getDataSetColumns(parameter, true));
						displayText.setItems(getDataSetColumns(parameter, false));
						displayText.add(DISPLAY_TEXT_NONE, 0);
						value.setEnabled(true);
						displayText.setEnabled(true);

						String temp = getColumnName(parameter, COLUMN_DISPLAY_TEXT);
						if (temp != null) {
							displayText.select(displayText.indexOf(temp));
						} else {
							displayText.select(0);
						}
					} catch (SemanticException e) {
						logger.log(Level.SEVERE, e.getMessage(), e);
					}

				}

			}

		}
	}

	private void createSortingArea(Composite parent) {
		// Sorting conditions here
		sorttingArea = new Composite(parent, SWT.NONE);
		GridData sorttingAreaGridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_END);
		sorttingAreaGridData.horizontalSpan = 2;
		sorttingArea.setLayoutData(sorttingAreaGridData);
		sorttingArea.setLayout(UIUtil.createGridLayoutWithoutMargin(1, false));

		Group sortGroup = new Group(sorttingArea, SWT.NONE);
		sortGroup.setText(LABEL_SORT_GROUP);
		sortGroup.setLayout(new GridLayout(2, false));
		sortGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Composite sortKeyArea = new Composite(sortGroup, SWT.NONE);
		sortKeyArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		sortKeyArea.setLayout(new GridLayout(2, false));
		// createLabel( sortKeyArea, LABEL_SORT_KEY );
		sortKeyLabel = new Label(sortKeyArea, SWT.NONE);
		sortKeyLabel.setText(LABEL_SORT_KEY);
		sortKeyChooser = new Combo(sortKeyArea, SWT.BORDER | SWT.READ_ONLY);
		sortKeyChooser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		sortKeyChooser.setVisibleItemCount(30);
		// sortKeyChooser.add( CHOICE_NONE );
		// sortKeyChooser.add( CHOICE_DISPLAY_TEXT );
		// sortKeyChooser.add( CHOICE_VALUE_COLUMN );
		// sortKeyChooser.setText( CHOICE_NONE );
		sortKeyChooser.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if (!((Combo) e.widget).getText().equals(CHOICE_NONE)) {
					sortDirectionLabel.setEnabled(true);
					sortDirectionChooser.setEnabled(true);
				} else {
					sortDirectionLabel.setEnabled(false);
					sortDirectionChooser.setEnabled(false);
				}
			}

		});

		Composite sortDirectionArea = new Composite(sortGroup, SWT.NONE);
		sortDirectionArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		sortDirectionArea.setLayout(new GridLayout(2, false));
		// createLabel( sortDirectionArea, LABEL_SORT_DIRECTION );
		sortDirectionLabel = new Label(sortDirectionArea, SWT.NONE);
		sortDirectionLabel.setText(LABEL_SORT_DIRECTION);
		sortDirectionChooser = new Combo(sortDirectionArea, SWT.BORDER | SWT.READ_ONLY);
		sortDirectionChooser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		sortDirectionChooser.setVisibleItemCount(30);
		sortDirectionChooser.add(CHOICE_ASCENDING);
		sortDirectionChooser.add(CHOICE_DESCENDING);
		sortDirectionChooser.setText(CHOICE_ASCENDING);
	}

	private void setSortingDefault() {
		sortKeyChooser.setText(CHOICE_NONE);
		sortDirectionChooser.setText(CHOICE_ASCENDING);
	}

	private void initSorttingArea() {
		refreshSortItems();
		if (selectedParameter == null) {
			setSortingDefault();
			sortKeyLabel.setEnabled(false);
			sortKeyChooser.setEnabled(false);
			sortDirectionLabel.setEnabled(false);
			sortDirectionChooser.setEnabled(false);
			return;
		}

		if (!selectedParameter.isFixedOrder()) {
			sortKeyLabel.setEnabled(true);
			sortKeyChooser.setEnabled(true);
			sortDirectionLabel.setEnabled(true);
			sortDirectionChooser.setEnabled(true);

			// String sortKey = selectedParameter.getSortBy( );
			// if ( sortKey == null
			// || sortKey.equals( DesignChoiceConstants.PARAM_SORT_VALUES_LABEL
			// ) )
			// {
			// sortKeyChooser.setText( CHOICE_DISPLAY_TEXT );
			// }
			// else
			// {
			// sortKeyChooser.setText( CHOICE_VALUE_COLUMN );
			// }
			String columnExp = selectedParameter.getSortByColumn();
			String columnName = getColumnName(columnExp);
			if (columnName != null && sortKeyChooser.indexOf(columnName) >= 0) {
				sortKeyChooser.setText(columnName);
			} else {
				sortKeyChooser.select(0);
			}

			String sortDirection = selectedParameter.getSortDirection();
			if (sortDirection == null || sortDirection.equals(DesignChoiceConstants.SORT_DIRECTION_ASC)) {
				sortDirectionChooser.setText(CHOICE_ASCENDING);
			} else {
				sortDirectionChooser.setText(CHOICE_DESCENDING);
			}
		} else {
			setSortingDefault();

			sortKeyLabel.setEnabled(true);
			sortKeyChooser.setEnabled(true);
			sortDirectionLabel.setEnabled(false);
			sortDirectionChooser.setEnabled(false);
		}
	}

	private void saveSortingProperties() {

		if (selectedParameter == null) {
			return;
		}

		if (sorttingArea != null && !sorttingArea.isDisposed() && sorttingArea.isVisible()) {
			try {

				if (sortKeyChooser.getText().equals(CHOICE_NONE)) {
					selectedParameter.setFixedOrder(true);
					selectedParameter.setSortBy(null);
					selectedParameter.setSortDirection(null);
					selectedParameter.setSortByColumn(null);
				} else {

					selectedParameter.setFixedOrder(false);
					selectedParameter.setSortBy(null);

					selectedParameter.setSortByColumn(getExpression(sortKeyChooser.getText()));

					if (sortDirectionChooser.getText().equals(CHOICE_ASCENDING)) {
						selectedParameter.setSortDirection(DesignChoiceConstants.SORT_DIRECTION_ASC);
					} else if (sortDirectionChooser.getText().equals(CHOICE_DESCENDING)) {
						selectedParameter.setSortDirection(DesignChoiceConstants.SORT_DIRECTION_DESC);
					}
				}
			} catch (SemanticException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				selectedParameter.setProperty(ScalarParameterHandle.FIXED_ORDER_PROP, null);
			} catch (SemanticException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private String getExpression(String columnName) {
		if (columnName.equals(CHOICE_NONE)) {
			return null;
		}
		List columnList = getColumnList();
		for (Iterator iter = columnList.iterator(); iter.hasNext();) {
			ResultSetColumnHandle cachedColumn = (ResultSetColumnHandle) iter.next();
			if (cachedColumn.getColumnName().equals(columnName)) {
				return DEUtil.getExpression(cachedColumn);
			}
		}
		// return null;
		return columnName;
	}

	protected void refreshSortItems() {
		if (sortDirectionChooser == null || sortDirectionChooser.isDisposed()) {
			return;
		}
		sortKeyChooser.removeAll();
		sortKeyChooser.add(CHOICE_NONE);
		List columnList = getColumnList();
		for (Iterator iter = columnList.iterator(); iter.hasNext();) {
			ResultSetColumnHandle cachedColumn = (ResultSetColumnHandle) iter.next();
			sortKeyChooser.add(cachedColumn.getColumnName());
		}

	}

	private String getColumnName(String expression) {
		List columnList = getColumnList();
		for (Iterator iter = columnList.iterator(); iter.hasNext();) {
			ResultSetColumnHandle cachedColumn = (ResultSetColumnHandle) iter.next();
			if (DEUtil.getExpression(cachedColumn).equals(expression)) {
				return cachedColumn.getColumnName();
			}
		}
		// return null;
		return expression;
	}

	public String getSelectedExprValue(String value) {
		String exprValue = null;

		if (value == null) {
			return "null"; //$NON-NLS-1$
		} else {
			String dataType = getSelectedDataType();
			if (dataType == null)
				return "null"; //$NON-NLS-1$
			if (DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN.equals(dataType)
					|| DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER.equals(dataType)
					|| DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT.equals(dataType)) {
				exprValue = value;
			} else if (DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL.equals(dataType)) {
				exprValue = "new java.math.BigDecimal(\"" + value + "\")"; //$NON-NLS-1$ //$NON-NLS-2$
			} else {
				exprValue = "\"" //$NON-NLS-1$
						+ JavascriptEvalUtil.transformToJsConstants(value) + "\""; //$NON-NLS-1$
			}
		}

		return exprValue;
	}

	private void addDynamicDefaultValue() {
		if (isMultiple.isEnabled() && isMultiple.getSelection()) {
			String type = (String) defaultValueChooser.getData(ExpressionButtonUtil.EXPR_TYPE);
			String value = UIUtil.convertToModelString(defaultValueChooser.getText(), false);
			setFirstDefaultValue(value, type);
			refreshDynamicValueTable();
			defaultValueChooser.setFocus();
			defaultValueChooser.setText("");
		}
	}

	private void updateDynamicTableButtons() {
		StructuredSelection selection = (StructuredSelection) defaultValueViewer.getSelection();
		boolean enable = (selection.size() == 1);
		editValueBtn.setEnabled(enable);
		delValueBtn.setEnabled(!selection.isEmpty());
		delAllValuesBtn.setEnabled(defaultValueViewer.getTable().getItemCount() > 0);

		Expression expression = ExpressionButtonUtil.getExpression(defaultValueChooser);
		if (defaultValueChooser.getText().trim().length() == 0) {
			addValueButton.setEnabled(false);
		} else {
			if (defaultValueList != null && defaultValueList.contains(expression))
				addValueButton.setEnabled(false);
			else
				addValueButton.setEnabled(true);
		}
	}

	private void refreshDynamicValueTable() {
		if (defaultValueViewer != null && !defaultValueViewer.getTable().isDisposed()) {
			defaultValueViewer.refresh();
			updateDynamicTableButtons();
		}
	}

	private Expression getFirstDefaultValue() {
		if (defaultValueList != null && defaultValueList.size() > 0)
			return defaultValueList.get(0);
		return null;
	}

	private void setFirstDefaultValue(String value, String type) {
		if (defaultValueList == null) {
			defaultValueList = new ArrayList<Expression>();
			initDefaultValueViewer();
		}
		Expression expression = null;
		if (value != null)
			expression = new Expression(value, type);
		if (!defaultValueList.contains(expression))
			defaultValueList.add(0, expression);
		updateMessageLine();
		updateFormatField();
	}

	private void simpleDefaultValueList() {
		if (defaultValueList != null && defaultValueList.size() > 0) {
			Expression expression = getFirstDefaultValue();
			defaultValueList.clear();
			defaultValueList.add(expression);
		}
	}
}