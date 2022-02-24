/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.report.designer.ui.dialogs;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.data.engine.api.DataEngineContext.DataEngineFlowMode;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.designer.data.ui.util.SelectValueFetcher;
import org.eclipse.birt.report.designer.internal.ui.data.DataService;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ImportValueDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.SelectionChoiceDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.ExpressionButton;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.ExpressionEditor;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.IExpressionHelper;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.DefaultParameterDialogControlTypeHelper;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelper;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelperProvider;
import org.eclipse.birt.report.designer.internal.ui.expressions.ExpressionContextFactoryImpl;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionContextFactory;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionConverter;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.ITableAreaModifier;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.TableArea;
import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil.ExpressionHelper;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionUtility;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.birt.report.designer.ui.parameters.ParameterUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.LinkedDataSetAdapter;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.FormatValueHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SelectionChoiceHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.elements.structures.SelectionChoice;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.util.ParameterValidationUtil;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.interfaces.IScalarParameterModel;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
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
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.util.ULocale;

/**
 * The dialog used to create or edit a parameter
 */

public class ParameterDialog extends BaseTitleAreaDialog {

	private static final String NULL_VALUE = Messages.getString("ParameterDialog.Value.Null"); //$NON-NLS-1$

	private static final String EMPTY_VALUE = Messages.getString("ParameterDialog.Value.Empty"); //$NON-NLS-1$

	private static final String CHOICE_NO_DEFAULT = Messages.getString("ParameterDialog.Choice.NoDefault"); //$NON-NLS-1$

	private static final String CHOICE_DISPLAY_TEXT = Messages.getString("ParameterDialog.Choice.DisplayText"); //$NON-NLS-1$

	private static final String CHOICE_VALUE_COLUMN = Messages.getString("ParameterDialog.Choice.ValueColumn"); //$NON-NLS-1$

	private static final String CHOICE_ASCENDING = Messages.getString("ParameterDialog.Choice.ASCENDING"); //$NON-NLS-1$

	private static final String CHOICE_DESCENDING = Messages.getString("ParameterDialog.Choice.DESCENDING"); //$NON-NLS-1$

	private static final String CHOICE_SELECT_VALUE = Messages.getString("ParameterDialog.Choice.SelectValue"); //$NON-NLS-1$

	private static final String GROUP_MORE_OPTION = Messages.getString("ParameterDialog.Group.MoreOption"); //$NON-NLS-1$

	private static final String LABEL_NAME = Messages.getString("ParameterDialog.Label.Name"); //$NON-NLS-1$

	private static final String LABEL_DATETIME_PROMPT = Messages.getFormattedString("ParameterDialog.datetime.prompt", //$NON-NLS-1$
			new String[] { "yyyy-MM-dd HH:mm:ss.SSS" }); //$NON-NLS-1$

	private static final String LABEL_DATE_PROMPT = Messages.getFormattedString("ParameterDialog.date.prompt", //$NON-NLS-1$
			new String[] { "yyyy-MM-dd" }); //$NON-NLS-1$

	private static final String LABEL_TIME_PROMPT = Messages.getFormattedString("ParameterDialog.time.prompt", //$NON-NLS-1$
			new String[] { "hh:mm:ss" }); //$NON-NLS-1$

	private static final String LABEL_PROMPT_TEXT = Messages.getString("ParameterDialog.Label.PromptText"); //$NON-NLS-1$

	private static final String LABEL_PARAM_DATA_TYPE = Messages.getString("ParameterDialog.Label.DataType"); //$NON-NLS-1$

	private static final String LABEL_DISPALY_TYPE = Messages.getString("ParameterDialog.Label.DisplayType"); //$NON-NLS-1$

	private static final String LABEL_DEFAULT_VALUE = Messages.getString("ParameterDialog.Label.DefaultValue"); //$NON-NLS-1$

	private static final String LABEL_HELP_TEXT = Messages.getString("ParameterDialog.Label.HelpText"); //$NON-NLS-1$

	private static final String LABEL_LIST_OF_VALUE = Messages.getString("ParameterDialog.Label.ListOfValue"); //$NON-NLS-1$

	private static final String LABEL_SORT_GROUP = Messages.getString("ParameterDialog.Label.SortGroup"); //$NON-NLS-1$

	private static final String LABEL_VALUES = Messages.getString("ParameterDialog.Label.Value"); //$NON-NLS-1$

	private static final String LABEL_FORMAT = Messages.getString("ParameterDialog.Label.Format"); //$NON-NLS-1$

	private static final String LABEL_LIST_LIMIT = Messages.getString("ParameterDialog.Label.Listlimit"); //$NON-NLS-1$

	private static final String LABEL_NULL = ParameterUtil.LABEL_NULL;

	private static final String LABEL_SELECT_DISPLAY_TEXT = Messages
			.getString("ParameterDialog.Label.SelectDisplayText"); //$NON-NLS-1$

	private static final String LABEL_SELECT_VALUE_COLUMN = Messages
			.getString("ParameterDialog.Label.SelectValueColumn"); //$NON-NLS-1$

	private static final String LABEL_SELECT_DATA_SET = Messages.getString("ParameterDialog.Label.SelectDataSet"); //$NON-NLS-1$

	private static final String LABEL_PREVIEW = Messages.getString("ParameterDialog.Label.Preview"); //$NON-NLS-1$

	private static final String LABEL_SORT_KEY = Messages.getString("ParameterDialog.Label.SortKey"); //$NON-NLS-1$

	private static final String LABEL_SORT_DIRECTION = Messages.getString("ParameterDialog.Label.SortDirection"); //$NON-NLS-1$

	private static final String CHECKBOX_ISREQUIRED = Messages.getString("ParameterDialog.CheckBox.IsRequired"); //$NON-NLS-1$

	private static final String CHECKBOX_DO_NOT_ECHO = Messages.getString("ParameterDialog.CheckBox.DoNotEchoInput"); //$NON-NLS-1$

	private static final String CHECKBOX_HIDDEN = Messages.getString("ParameterDialog.CheckBox.Hidden"); //$NON-NLS-1$

	private static final String CHECKBOX_DISTINCT = Messages.getString("ParameterDialog.CheckBox.Distinct"); //$NON-NLS-1$

	private static final String BUTTON_LABEL_CHANGE_FORMAT = Messages.getString("ParameterDialog.Button.ChangeFormat"); //$NON-NLS-1$

	private static final String BUTTON_LABEL_IMPORT = Messages.getString("ParameterDialog.Button.ImportValue"); //$NON-NLS-1$

	private static final String BUTTON_LABEL_SET_DEFAULT = Messages.getString("ParameterDialog.Button.SetDefault"); //$NON-NLS-1$

	private static final String BUTTON_LABEL_REMOVE_DEFAULT = Messages
			.getString("ParameterDialog.Button.RemoveDefault"); //$NON-NLS-1$

	private static final String BUTTON_CREATE_DATA_SET = Messages.getString("ParameterDialog.Button.CreateDataSet"); //$NON-NLS-1$

	private static final String RADIO_DYNAMIC = Messages.getString("ParameterDialog.Radio.Dynamic"); //$NON-NLS-1$

	private static final String CHECK_ALLOW_MULTI = Messages.getString("ParameterDialog.Check.AllowMulti"); //$NON-NLS-1$

	private static final String RADIO_STATIC = Messages.getString("ParameterDialog.Radio.Static"); //$NON-NLS-1$

	private static final String ERROR_TITLE_INVALID_LIST_LIMIT = Messages
			.getString("ParameterDialog.ErrorTitle.InvalidListLimit"); //$NON-NLS-1$

	private static final String ERROR_MSG_CANNOT_BE_BLANK = Messages
			.getString("ParameterDialog.ErrorMessage.CanootBeBlank"); //$NON-NLS-1$

	private static final String ERROR_MSG_CANNOT_BE_NULL = Messages
			.getString("ParameterDialog.ErrorMessage.CanootBeNull"); //$NON-NLS-1$

	private static final String ERROR_MSG_DUPLICATED_VALUE = Messages
			.getString("ParameterDialog.ErrorMessage.DuplicatedValue"); //$NON-NLS-1$

	private static final String ERROR_MSG_DUPLICATED_LABEL = Messages
			.getString("ParameterDialog.ErrorMessage.DuplicatedLabel"); //$NON-NLS-1$

	private static final String ERROR_MSG_DUPLICATED_LABELKEY = Messages
			.getString("ParameterDialog.ErrorMessage.DuplicatedLabelKey"); //$NON-NLS-1$

	private static final String ERROR_MSG_MISMATCH_DATA_TYPE = Messages
			.getString("ParameterDialog.ErrorMessage.MismatchDataType"); //$NON-NLS-1$

	private static final String ERROR_MSG_DUPLICATED_NAME = Messages
			.getString("ParameterDialog.ErrorMessage.DuplicatedName"); //$NON-NLS-1$

	private static final String ERROR_MSG_NAME_IS_EMPTY = Messages.getString("ParameterDialog.ErrorMessage.EmptyName"); //$NON-NLS-1$

	private static final String ERROR_MSG_NO_AVAILABLE_COLUMN = Messages
			.getString("ParameterDialog.ErrorMessage.NoAvailableColumn"); //$NON-NLS-1$

	private static final String ERROR_MSG_VALUE_COLUMN_EMPTY = Messages
			.getString("ParameterDialog.ErrorMessage.ValueColumnEmpty"); //$NON-NLS-1$

	private static final String ERROR_MSG_INVALID_LIST_LIMIT = Messages
			.getString("ParameterDialog.ErrorMessage.InvalidListLimit"); //$NON-NLS-1$

	private static final String FLAG_DEFAULT = Messages.getString("ParameterDialog.Flag.Default"); //$NON-NLS-1$

	private static final String COLUMN_VALUE = Messages.getString("ParameterDialog.Column.Value"); //$NON-NLS-1$

	private static final String COLUMN_DISPLAY_TEXT = Messages.getString("ParameterDialog.Column.DisplayText"); //$NON-NLS-1$

	private static final String COLUMN_DISPLAY_TEXT_KEY = Messages.getString("ParameterDialog.Column.DisplayTextKey"); //$NON-NLS-1$

	private static final String COLUMN_IS_DEFAULT = Messages.getString("ParameterDialog.Column.Default"); //$NON-NLS-1$

	private static final String BOOLEAN_TRUE = Messages.getString("ParameterDialog.Boolean.True"); //$NON-NLS-1$

	private static final String BOOLEAN_FALSE = Messages.getString("ParameterDialog.Boolean.False"); //$NON-NLS-1$

	public static final String PARAM_CONTROL_LIST = DesignChoiceConstants.PARAM_CONTROL_LIST_BOX + "/List"; //$NON-NLS-1$

	public static final String PARAM_CONTROL_COMBO = DesignChoiceConstants.PARAM_CONTROL_LIST_BOX + "/Combo"; //$NON-NLS-1$

	public static final String DISPLAY_NAME_CONTROL_LIST = Messages.getString("ParameterDialog.DisplayLabel.List"); //$NON-NLS-1$

	public static final String DISPLAY_NAME_CONTROL_COMBO = Messages.getString("ParameterDialog.DisplayLabel.Combo"); //$NON-NLS-1$

	private static final String NONE_DISPLAY_TEXT = Messages.getString("ParameterDialog.Label.None"); //$NON-NLS-1$

	private static final Image DEFAULT_ICON = ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_DEFAULT);

	private static final Image NOT_DEFAULT_ICON = ReportPlatformUIImages
			.getImage(IReportGraphicConstants.ICON_DEFAULT_NOT);

	public static final String CONTROLTYPE_VALUE = "controltype";//$NON-NLS-1$

	public static final String DATATYPE_VALUE = "datatype";//$NON-NLS-1$

	public static final String STATIC_VALUE = "static";//$NON-NLS-1$

	public static final String HELPER_KEY_CONTROLTYPE = "controlType";//$NON-NLS-1$

	public static final String HELPER_KEY_STARTPOINT = "autoSuggestStartPoint";//$NON-NLS-1$

	public static final String CONTROLTYPE_INPUTVALUE = "controltypeinput"; //$NON-NLS-1$

	public static final String STARTPOINT_INPUTVALUE = "startpointinput"; //$NON-NLS-1$

	public static final String STARTPOINT_VALUE = "startpoint"; //$NON-NLS-1$

	private boolean allowMultiValueVisible = true;

	private HashMap dirtyProperties = new HashMap(5);

	private ArrayList choiceList = new ArrayList();
	private Map<SelectionChoice, SelectionChoice> editChoiceMap = new HashMap<SelectionChoice, SelectionChoice>();

	private static final IChoiceSet DATA_TYPE_CHOICE_SET = DEUtil.getMetaDataDictionary()
			.getElement(ReportDesignConstants.SCALAR_PARAMETER_ELEMENT)
			.getProperty(ScalarParameterHandle.DATA_TYPE_PROP).getAllowedChoices();

	private static final double DEFAULT_PREVIEW_NUMBER = 1234.56;

	private static final int DEFAULT_PREVIEW_INTEGER = 123456;

	private ScalarParameterHandle inputParameter;

	private boolean loading = true;

	private Text nameEditor, promptTextEditor, helpTextEditor, formatField;

	// Prompt message line
	private Label promptMessageLine;

	// Error message line
	// private CLabel errorMessageLine;

	// Check boxes
	private Button isRequired, doNotEcho, isHidden, distinct;

	// Push buttons
	private Button importValue, changeDefault, changeFormat, createDataSet;

	// Radio buttons
	private Button dynamicRadio, staticRadio;

	private Button allowMultiChoice;

	// Combo chooser for static
	private Combo dataTypeChooser;
	private CCombo defaultValueChooser;

	// Combo chooser for dynamic
	private Combo dataSetChooser, columnChooser, displayTextChooser, sortKeyChooser, sortDirectionChooser;

	// Label
	private Label sortKeyLabel, sortDirectionLabel;

	private CLabel previewLabel;

	private TableViewer valueTable;

	private String lastDataType, lastControlType;

	private String formatCategroy, formatPattern;

	private ULocale formatLocale;

	private List<Expression> defaultValueList;

	private Composite valueArea, sorttingArea;

	private List columnList;

	private TableArea staticTableArea;

	private IDialogHelper controlTypeHelper;

	private IDialogHelper startPointTypeHelper;

	private IStructuredContentProvider contentProvider = new IStructuredContentProvider() {

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		public Object[] getElements(Object inputElement) {
			ArrayList list = ((ArrayList) inputElement);
			ArrayList elementsList = (ArrayList) list.clone();
			return elementsList.toArray();
		}
	};

	private ITableLabelProvider labelProvider = new ITableLabelProvider() {

		public Image getColumnImage(Object element, int columnIndex) {
			if (valueTable.getColumnProperties().length == 5 && columnIndex == 1) {
				SelectionChoice choice = ((SelectionChoice) element);
				if (isDefaultChoice(choice)) {
					return DEFAULT_ICON;
				} else
					return NOT_DEFAULT_ICON;
			}
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			SelectionChoice choice = ((SelectionChoice) element);
			final int valueIndex = valueTable.getColumnProperties().length - 3;
			String text = null;
			if (valueTable.getColumnProperties().length == 5 && columnIndex == 1) {
				if (isDefaultChoice(choice)) {
					text = FLAG_DEFAULT;
				}
			} else if (columnIndex == valueIndex) {
				text = choice.getValue();
				if (text == null)
					text = NULL_VALUE;
				else if (text.equals("")) //$NON-NLS-1$
					text = EMPTY_VALUE;
			} else if (columnIndex == valueIndex + 1) {
				text = choice.getLabel();
				if (text == null)
					return ""; //$NON-NLS-1$
			} else if (columnIndex == valueIndex + 2) {
				text = choice.getLabelResourceKey();
			}
			if (text == null) {
				text = ""; //$NON-NLS-1$
			}
			return text;
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

	private final ITableAreaModifier tableAreaModifier = new ITableAreaModifier() {

		public boolean editItem(final Object element) {
			final SelectionChoice oldChoice = (SelectionChoice) element;
			final SelectionChoice tempChoice = cloneSelectionChoice(oldChoice);

			String oldVal = oldChoice.getValue();
			boolean isDefault = isDefaultChoice(oldChoice);
			SelectionChoiceDialog dialog = new SelectionChoiceDialog(
					Messages.getString("ParameterDialog.SelectionDialog.Edit"), canBeNull(), canUseEmptyValue()); //$NON-NLS-1$
			dialog.setInput(tempChoice);
			dialog.setValidator(new SelectionChoiceDialog.ISelectionChoiceValidator() {

				public String validate(String displayLableKey, String displayLabel, String value) {
					return validateChoice(oldChoice, displayLableKey, displayLabel, value);
				}

			});
			if (dialog.open() == Dialog.OK) {
				// choice.setValue( convertToStandardFormat( choice.getValue( )
				// ) );
//				oldChoice.setValue( tempChoice.getValue( ) );
				editChoiceMap.put(tempChoice, oldChoice);
				choiceList.set(choiceList.indexOf(oldChoice), tempChoice);
				if (isDefault) {
					changeDefaultValue(oldVal, tempChoice.getValue());
				}
				return true;
			}
			return false;
		}

		private SelectionChoice cloneSelectionChoice(SelectionChoice choice) {
			SelectionChoice tempChoice = StructureFactory.createSelectionChoice();
			tempChoice.setValue(choice.getValue());
			tempChoice.setLabel(choice.getLabel());
			tempChoice.setLabelResourceKey(choice.getLabelResourceKey());
			return tempChoice;
		}

		public boolean newItem() {
			SelectionChoice choice = StructureFactory.createSelectionChoice();
			SelectionChoiceDialog dialog = new SelectionChoiceDialog(
					Messages.getString("ParameterDialog.SelectionDialog.New"), canBeNull(), canUseEmptyValue()); //$NON-NLS-1$
			dialog.setInput(choice);
			dialog.setValidator(new SelectionChoiceDialog.ISelectionChoiceValidator() {

				public String validate(String displayLabelKey, String displayLabel, String value) {
					return validateChoice(null, displayLabelKey, displayLabel, value);
				}
			});
			if (dialog.open() == Dialog.OK) {
				// choice.setValue( convertToStandardFormat( choice.getValue( )
				// ) );
				// choice.setValue( choice.getValue( ) );
				choiceList.add(choice);
				return true;
			}
			return false;
		}

		public boolean removeItem(Object[] elements) {
			for (int i = 0; i < elements.length; i++) {
				if (isDefaultChoice((SelectionChoice) elements[i])) {
					removeDefaultValue(((SelectionChoice) elements[i]).getValue());
				}
				choiceList.remove(elements[i]);
			}
			return true;
		}

		public boolean removeItemAll() {
			choiceList.clear();
			if (defaultValueList != null && defaultValueList.size() > 0) {
				defaultValueList.clear();
				valueTable.refresh();
			}
			return true;
		}
	};

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
					String value = ((Expression) element).getStringExpression();
					if (value == null)
						return NULL_VALUE;
					else if (value.equals("")) //$NON-NLS-1$
						return EMPTY_VALUE;
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

	private Text listLimit;

	private Composite displayArea;

	private boolean isInitialized;

	private TableViewer defaultValueViewer;

	private Button delBtn;

	private Button editBtn;

	private Button delAllBtn;

	private Group valuesDefineSection;

	private Button addButton;

	private Composite rightPart;

	/**
	 * Create a new parameter dialog with given title under the active shell
	 * 
	 * @param title the title of the dialog
	 */
	public ParameterDialog(String title) {
		this(UIUtil.getDefaultShell(), title);
	}

	/**
	 * Create a new parameter dialog with given title under the specified shell
	 * 
	 * @param parentShell the parent shell of the dialog
	 * @param title       the title of the dialog
	 */
	public ParameterDialog(Shell parentShell, String title) {
		super(parentShell);
		this.title = title;
	}

	public ParameterDialog(Shell parentShell, String title, boolean allowMultiValueVisible) {
		this(parentShell, title);
		this.allowMultiValueVisible = allowMultiValueVisible;
	}

	protected Control createDialogArea(Composite parent) {
		setMessage(Messages.getString("ParameterDialog.message")); //$NON-NLS-1$
		ScrolledComposite scrollContent = new ScrolledComposite((Composite) super.createDialogArea(parent),
				SWT.H_SCROLL | SWT.V_SCROLL);
		scrollContent.setAlwaysShowScrollBars(false);
		scrollContent.setExpandHorizontal(true);
		scrollContent.setMinWidth(600);
		scrollContent.setLayout(new FillLayout());
		scrollContent.setLayoutData(new GridData(GridData.FILL_BOTH));

		displayArea = new Composite(scrollContent, SWT.NONE);

		Composite topComposite = new Composite(displayArea, SWT.NONE);
		topComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		topComposite.setLayout(new GridLayout(2, false));

		createPropertiesSection(topComposite);
		createDisplayOptionsSection(topComposite);
		createValuesDefineSection(displayArea);
		displayArea.setLayout(new GridLayout());

		Point size = displayArea.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		displayArea.setSize(size);

		scrollContent.setContent(displayArea);

		UIUtil.bindHelp(parent, IHelpContextIds.PARAMETER_DIALOG_ID);
		return scrollContent;
	}

	private void createPropertiesSection(Composite composite) {

		Composite propertiesSection = new Composite(composite, SWT.NONE);
		propertiesSection.setLayout(new GridLayout());
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.minimumWidth = 200;
		propertiesSection.setLayoutData(gd);

		createLabel(propertiesSection, LABEL_NAME);
		nameEditor = new Text(propertiesSection, SWT.BORDER);
		nameEditor.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nameEditor.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				updateMessageLine();
			}
		});
		createLabel(propertiesSection, LABEL_PROMPT_TEXT);
		promptTextEditor = new Text(propertiesSection, SWT.BORDER);
		promptTextEditor.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createLabel(propertiesSection, LABEL_PARAM_DATA_TYPE);
		dataTypeChooser = new Combo(propertiesSection, SWT.READ_ONLY | SWT.DROP_DOWN);
		dataTypeChooser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		dataTypeChooser.setItems(ChoiceSetFactory.getDisplayNamefromChoiceSet(DATA_TYPE_CHOICE_SET));
		dataTypeChooser.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				changeDataType();
				updateCheckBoxArea();
				refreshColumns(true);

				// Reset control type status
				handleControlTypeSelectionEvent();
			}
		});
		createLabel(propertiesSection, LABEL_DISPALY_TYPE);
		createControlTypeChooser(propertiesSection);

	}

	private void createControlTypeChooser(Composite propertiesSection) {
		IDialogHelperProvider helperProvider = (IDialogHelperProvider) ElementAdapterManager.getAdapter(this,
				IDialogHelperProvider.class);

		if (helperProvider != null) {
			controlTypeHelper = helperProvider.createHelper(this, HELPER_KEY_CONTROLTYPE);
		}

		if (controlTypeHelper == null) {
			controlTypeHelper = new DefaultParameterDialogControlTypeHelper();
		}
		controlTypeHelper.setContainer(this);
		controlTypeHelper.createContent(propertiesSection);
		controlTypeHelper.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		controlTypeHelper.addListener(SWT.Selection, new Listener() {

			public void handleEvent(Event event) {
				handleControlTypeSelectionEvent();
			}
		});

	}

	private void createDisplayOptionsSection(Composite composite) {
		Group displayOptionSection = new Group(composite, SWT.NONE);
		displayOptionSection.setText(GROUP_MORE_OPTION);
		displayOptionSection.setLayout(new GridLayout(2, false));
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.minimumWidth = 400;
		displayOptionSection.setLayoutData(gd);
		createLabel(displayOptionSection, LABEL_HELP_TEXT);
		helpTextEditor = new Text(displayOptionSection, SWT.BORDER);
		helpTextEditor.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createLabel(displayOptionSection, LABEL_FORMAT);
		Composite formatSection = new Composite(displayOptionSection, SWT.NONE);
		formatSection.setLayout(UIUtil.createGridLayoutWithoutMargin(2, false));
		formatSection.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		formatField = new Text(formatSection, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		GridData fFgd = new GridData(GridData.FILL_HORIZONTAL);
		fFgd.minimumWidth = 180;
		formatField.setLayoutData(fFgd);
		changeFormat = new Button(formatSection, SWT.PUSH);
		changeFormat.setText(BUTTON_LABEL_CHANGE_FORMAT);
		setButtonLayoutData(changeFormat);
		changeFormat.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				popupFormatBuilder(true);
			}

		});
		createLabel(displayOptionSection, null);
		Group previewArea = new Group(displayOptionSection, SWT.NONE);
		previewArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		previewArea.setLayout(UIUtil.createGridLayoutWithoutMargin());
		previewArea.setText(LABEL_PREVIEW);
		previewLabel = new CLabel(previewArea, SWT.NONE);
		previewLabel.setAlignment(SWT.CENTER);
		previewLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// start create list limitation area
		createLabel(displayOptionSection, LABEL_LIST_LIMIT);

		Composite limitArea = new Composite(displayOptionSection, SWT.NULL);
		GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		limitArea.setLayout(layout);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.verticalSpan = 1;
		limitArea.setLayoutData(data);

		listLimit = new Text(limitArea, SWT.BORDER);
		data = new GridData();
		data.widthHint = 80;
		listLimit.setLayoutData(data);
		listLimit.addVerifyListener(new VerifyListener() {

			public void verifyText(VerifyEvent e) {
				e.doit = ("0123456789\0\b\u007f".indexOf(e.character) != -1); //$NON-NLS-1$
			}
		});
		Label values = new Label(limitArea, SWT.NULL);
		values.setText(Messages.getString("ParameterDialog.Label.values")); //$NON-NLS-1$
		values.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		// end

		createLabel(displayOptionSection, null); // Dummy
		Composite checkBoxArea = new Composite(displayOptionSection, SWT.NONE);
		checkBoxArea.setLayout(UIUtil.createGridLayoutWithoutMargin(2, false));
		checkBoxArea.setLayoutData(new GridData(GridData.FILL_BOTH));

		isRequired = new Button(checkBoxArea, SWT.CHECK);
		isRequired.setText(CHECKBOX_ISREQUIRED);
		addCheckBoxListener(isRequired, CHECKBOX_ISREQUIRED);

		doNotEcho = new Button(checkBoxArea, SWT.CHECK);
		doNotEcho.setText(CHECKBOX_DO_NOT_ECHO);
		addCheckBoxListener(doNotEcho, CHECKBOX_DO_NOT_ECHO);
		isHidden = new Button(checkBoxArea, SWT.CHECK);
		isHidden.setText(CHECKBOX_HIDDEN);
		addCheckBoxListener(isHidden, CHECKBOX_HIDDEN);

		distinct = new Button(checkBoxArea, SWT.CHECK);
		distinct.setText(CHECKBOX_DISTINCT);
		distinct.setSelection(false);
		addCheckBoxListener(distinct, CHECKBOX_DISTINCT);

		createStartPointSection(displayOptionSection);
	}

	private void createStartPointSection(Group displayOptionSection) {
		IDialogHelperProvider helperProvider = (IDialogHelperProvider) ElementAdapterManager.getAdapter(this,
				IDialogHelperProvider.class);

		if (helperProvider != null) {
			startPointTypeHelper = helperProvider.createHelper(this, HELPER_KEY_STARTPOINT);
		}

		if (startPointTypeHelper != null) {
			startPointTypeHelper.createContent(displayOptionSection);
			GridData data = new GridData(GridData.FILL_HORIZONTAL);
			data.horizontalSpan = 2;
			startPointTypeHelper.getControl().setLayoutData(data);
			startPointTypeHelper.addListener(SWT.Modify, new Listener() {

				public void handleEvent(Event event) {
					startPointTypeHelper.update(false);
				}

			});
		}
	}

	private void hideStartPointSection(boolean hide) {
		if (startPointTypeHelper != null && startPointTypeHelper.getControl() != null) {
			GridData gd = (GridData) startPointTypeHelper.getControl().getLayoutData();
			gd.exclude = hide;
			startPointTypeHelper.getControl().setVisible(!hide);
			startPointTypeHelper.getControl().getParent().layout();
			if (!hide)
				startPointTypeHelper.update(true);
		}
	}

	private void createValuesDefineSection(Composite composite) {
		valuesDefineSection = new Group(composite, SWT.NONE);
		valuesDefineSection.setText(LABEL_LIST_OF_VALUE);
		valuesDefineSection.setLayout(new GridLayout(2, false));
		valuesDefineSection.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Composite choiceArea = new Composite(valuesDefineSection, SWT.NONE);
		choiceArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		choiceArea.setLayout(UIUtil.createGridLayoutWithoutMargin(4, true));
		staticRadio = new Button(choiceArea, SWT.RADIO);
		staticRadio.setText(RADIO_STATIC);
		GridData gd = new GridData();
		gd.widthHint = Math.max(staticRadio.computeSize(SWT.DEFAULT, SWT.DEFAULT).x, 100);
		gd.horizontalIndent = 40;
		staticRadio.setLayoutData(gd);
		staticRadio.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (staticRadio.getSelection())
					switchParamterType();
			}

		});
		dynamicRadio = new Button(choiceArea, SWT.RADIO);
		dynamicRadio.setText(RADIO_DYNAMIC);
		dynamicRadio.setLayoutData(new GridData());
		dynamicRadio.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (dynamicRadio.getSelection())
					switchParamterType();
			}
		});

		allowMultiChoice = new Button(choiceArea, SWT.CHECK);
		allowMultiChoice.setText(CHECK_ALLOW_MULTI);
		gd = new GridData();
		gd.horizontalSpan = 2;
		gd.horizontalAlignment = SWT.END;
		gd.grabExcessHorizontalSpace = true;
		allowMultiChoice.setLayoutData(gd);
		allowMultiChoice.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				// keep one value of default value list when switch from allow
				// to disallow
				if (isStatic() && !allowMultiChoice.getSelection()) {
					if (defaultValueList != null && defaultValueList.size() > 0) {
						Expression expression = getFirstDefaultValue();
						defaultValueList.clear();
						defaultValueList.add(expression);
						valueTable.refresh();
					}

					// Switch from multiple selection to single selection.
					// Deselect all if more than one item is selected.
					Table table = staticTableArea.getTableViewer().getTable();
					if (table.getSelectionCount() > 1) {
						table.deselectAll();
						updateStaticTableButtons();
					}
				}
				initDefaultValueViewer();
			}
		});

		valueArea = new Composite(valuesDefineSection, SWT.NONE);
		valueArea.setLayout(UIUtil.createGridLayoutWithoutMargin(2, false));
		gd = new GridData(GridData.FILL_BOTH);
		gd.minimumHeight = 320;
		gd.widthHint = 550;
		gd.horizontalSpan = 2;
		valueArea.setLayoutData(gd);

		createLabel(valuesDefineSection, null);
		// errorMessageLine = new CLabel( valuesDefineSection, SWT.NONE );
		// GridData msgLineGridData = new GridData( GridData.FILL_HORIZONTAL );
		// msgLineGridData.horizontalSpan = 2;
		// errorMessageLine.setLayoutData( msgLineGridData );
	}

	/**
	 * Set the input of the dialog, which cannot be null
	 * 
	 * @param input the input of the dialog, which cannot be null
	 */
	public void setInput(Object input) {
		// Assert.isNotNull( input );
		// Assert.isLegal( input instanceof ScalarParameterHandle );
		inputParameter = (ScalarParameterHandle) input;
	}

	protected boolean initDialog() {
		// Assert.isNotNull( inputParameter );
		nameEditor.setText(inputParameter.getName());
		if (!StringUtil.isBlank(inputParameter.getPromptText())) {
			promptTextEditor.setText(inputParameter.getPromptText());
		}
		helpTextEditor.setText(UIUtil.convertToGUIString(inputParameter.getHelpText()));

		for (Iterator iter = inputParameter.getPropertyHandle(ScalarParameterHandle.SELECTION_LIST_PROP)
				.iterator(); iter.hasNext();) {
			SelectionChoiceHandle choiceHandle = (SelectionChoiceHandle) iter.next();
			choiceList.add(choiceHandle.getStructure());
		}

		if (inputParameter.getValueType().equals(DesignChoiceConstants.PARAM_VALUE_TYPE_STATIC)) {
			staticRadio.setSelection(true);
		} else {
			dynamicRadio.setSelection(true);
		}

		defaultValueList = inputParameter.getDefaultValueList();

		if (enableAllowMultiValueVisible()) {
			allowMultiChoice.setVisible(true);
			if (DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE.endsWith(inputParameter.getParamType())) {
				allowMultiChoice.setSelection(true);
			} else {
				allowMultiChoice.setSelection(false);
			}

		} else {
			allowMultiChoice.setVisible(false);
		}

		if (inputParameter.getPropertyHandle(ScalarParameterHandle.LIST_LIMIT_PROP).isSet()) {
			listLimit.setText(String.valueOf(inputParameter.getListlimit()));
		}

		isHidden.setSelection(inputParameter.isHidden());
		isRequired.setSelection(inputParameter.isRequired());
		doNotEcho.setSelection(inputParameter.isConcealValue());
		distinct.setSelection(!inputParameter.distinct());
		if (this.startPointTypeHelper != null) {
			startPointTypeHelper.setProperty(STARTPOINT_INPUTVALUE, inputParameter.getAutoSuggestThreshold());
			startPointTypeHelper.update(true);
		}
		changeDataType();
		dataTypeChooser.setText(DATA_TYPE_CHOICE_SET.findChoice(inputParameter.getDataType()).getDisplayName());
		switchParamterType();
		loading = false;

		Point size = displayArea.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		displayArea.setSize(size);

		isInitialized = true;

		return true;
	}

	private void initValueArea() {
		String controlType = getSelectedControlType();

		if (DesignChoiceConstants.PARAM_CONTROL_AUTO_SUGGEST.equals(controlType)) {
			listLimit.setEnabled(false);
		}

		Expression expression = getFirstDefaultValue();
		String defaultValue = expression == null ? null : expression.getStringExpression();
		String expressionType = expression == null ? null : expression.getType();
		if (defaultValue != null) {
			defaultValue = defaultValue.trim();
		}
		if (isStatic()) {
			if (DesignChoiceConstants.PARAM_CONTROL_CHECK_BOX.equals(controlType)) {
				if (expressionType != null) {
					defaultValueChooser.setData(ExpressionButtonUtil.EXPR_TYPE, expressionType);
					ExpressionButton button = (ExpressionButton) defaultValueChooser
							.getData(ExpressionButtonUtil.EXPR_BUTTON);
					if (button != null)
						button.refresh();

				}
				String type = (String) defaultValueChooser.getData(ExpressionButtonUtil.EXPR_TYPE);
				if (ExpressionType.CONSTANT.equals(type)) {
					defaultValueChooser.setEditable(false);
					if (defaultValueChooser.getItemCount() == 0) {
						defaultValueChooser.add(CHOICE_NO_DEFAULT);
						defaultValueChooser.add(BOOLEAN_TRUE);
						defaultValueChooser.add(BOOLEAN_FALSE);
					}
				} else {
					defaultValueChooser.setEditable(true);
					if (defaultValueChooser.getItemCount() != 0) {
						defaultValueChooser.removeAll();
					}
				}

				if (isValidValue(defaultValue, expressionType) != null) {
					defaultValue = null;
					if (ExpressionType.CONSTANT.equals(type))
						defaultValueChooser.select(defaultValueChooser.indexOf(CHOICE_NO_DEFAULT));
					else
						defaultValueChooser.setText("");//$NON-NLS-1$
				} else {

					if (defaultValue == null) {
						if (ExpressionType.CONSTANT.equals(type))
							defaultValueChooser.select(defaultValueChooser.indexOf(CHOICE_NO_DEFAULT));
						else
							defaultValueChooser.setText("");//$NON-NLS-1$
					} else if (ExpressionType.CONSTANT.equals(expressionType)) {

						if (Boolean.valueOf(defaultValue).booleanValue()) {
							defaultValueChooser.select(defaultValueChooser.indexOf(BOOLEAN_TRUE));
						} else {
							defaultValueChooser.select(defaultValueChooser.indexOf(BOOLEAN_FALSE));
						}
					} else {
						defaultValueChooser.setText(defaultValue);
					}

				}

				handleDefaultValueModifyEvent();
			} else if (DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX.equals(controlType)) {
				if (getSelectedDataType().equals(DesignChoiceConstants.PARAM_TYPE_STRING)) {
					if (expressionType != null) {
						defaultValueChooser.setData(ExpressionButtonUtil.EXPR_TYPE, expressionType);
						ExpressionButton button = (ExpressionButton) defaultValueChooser
								.getData(ExpressionButtonUtil.EXPR_BUTTON);
						if (button != null)
							button.refresh();
					}
					defaultValueChooser.setText(DEUtil.resolveNull(defaultValue));
				} else if (defaultValue != null) {
					if ((defaultValue.equals(Boolean.toString(true)) || defaultValue.equals(Boolean.toString(false)))) {
						defaultValue = null;
					} else {
						if (expressionType != null) {
							defaultValueChooser.setData(ExpressionButtonUtil.EXPR_TYPE, expressionType);
							ExpressionButton button = (ExpressionButton) defaultValueChooser
									.getData(ExpressionButtonUtil.EXPR_BUTTON);
							if (button != null)
								button.refresh();
						}
						defaultValueChooser.setText(defaultValue);
					}
				}
				handleDefaultValueModifyEvent();
			} else if (PARAM_CONTROL_COMBO.equals(controlType) || PARAM_CONTROL_LIST.equals(controlType)
					|| DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON.endsWith(controlType)) {
				initSorttingArea();

				// To fix bug Bugzilla 169927
				// Please also refer to Bugzilla 175788
				if (lastControlType != null && lastControlType.equals(DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX)) {
					defaultValue = null;
				}
			}
			refreshStaticValueTable();
		} else {
			refreshDataSets();
			if (inputParameter.getDataSet() != null) {
				dataSetChooser.setText(inputParameter.getDataSet().getName());
			}
			refreshColumns(false);
			refreshSortByItems();

			ExpressionHandle columnValueValue = inputParameter
					.getExpressionProperty(ScalarParameterHandle.VALUE_EXPR_PROP);
			{
				columnChooser.setData(ExpressionButtonUtil.EXPR_TYPE,
						columnValueValue == null || columnValueValue.getType() == null ? UIUtil.getDefaultScriptType()
								: (String) columnValueValue.getType());

				String columnName = getColumnName(columnChooser, inputParameter.getValueExpr());
				if (columnName != null) {
					columnChooser.setText(columnName);
				}

				Object button = columnChooser.getData(ExpressionButtonUtil.EXPR_BUTTON);
				if (button instanceof ExpressionButton) {
					((ExpressionButton) button).refresh();
				}
			}

			ExpressionHandle columnValue = inputParameter.getExpressionProperty(ScalarParameterHandle.LABEL_EXPR_PROP);
			{
				displayTextChooser.setData(ExpressionButtonUtil.EXPR_TYPE,
						columnValue == null || columnValue.getType() == null ? UIUtil.getDefaultScriptType()
								: (String) columnValue.getType());

				String columnName = getColumnName(displayTextChooser, inputParameter.getLabelExpr());
				if (columnName != null) {
					displayTextChooser.setText(columnName);
				}

				Object button = displayTextChooser.getData(ExpressionButtonUtil.EXPR_BUTTON);
				if (button instanceof ExpressionButton) {
					((ExpressionButton) button).refresh();
				}
			}

			if (expressionType != null) {
				defaultValueChooser.setData(ExpressionButtonUtil.EXPR_TYPE, expressionType);
				ExpressionButton button = (ExpressionButton) defaultValueChooser
						.getData(ExpressionButtonUtil.EXPR_BUTTON);
				if (button != null)
					button.refresh();
			}

			if (canUseEmptyValue() && "".equals(defaultValue)) {
				defaultValue = EMPTY_VALUE;
			}
			if (getSelectedDataType().equals(DesignChoiceConstants.PARAM_TYPE_STRING)) {
				defaultValueChooser.setText(DEUtil.resolveNull(defaultValue));
			} else if (defaultValue != null) {
				defaultValueChooser.setText(defaultValue);
			}

			handleDefaultValueModifyEvent();

			initSorttingArea();
		}
		updateMessageLine();
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
		Expression expression = new Expression(value, type);
		if (!defaultValueList.contains(expression))
			defaultValueList.add(0, expression);
		updateMessageLine();
		updateFormatField();
	}

	private void initSorttingArea() {
		refreshSortByItems();
		if (!inputParameter.isFixedOrder()) {
			sortKeyLabel.setEnabled(true);
			sortKeyChooser.setEnabled(true);
			sortDirectionLabel.setEnabled(true);
			sortDirectionChooser.setEnabled(true);
			distinct.setEnabled(true);

			distinct.setSelection(!inputParameter.distinct());
			boolean isStatic = DesignChoiceConstants.PARAM_VALUE_TYPE_STATIC.equals(inputParameter.getValueType());
			boolean isDynamic = DesignChoiceConstants.PARAM_VALUE_TYPE_DYNAMIC.equals(inputParameter.getValueType());
			if (isStatic) {
				String sortKey = inputParameter.getSortBy();
				if (sortKey == null || sortKey.equals(DesignChoiceConstants.PARAM_SORT_VALUES_LABEL)) {
					sortKeyChooser.setText(CHOICE_DISPLAY_TEXT);
				} else {
					sortKeyChooser.setText(CHOICE_VALUE_COLUMN);
				}
			} else if (isDynamic) {
				String columnName = getColumnName(inputParameter.getSortByColumn());
				if (columnName != null && sortKeyChooser.indexOf(columnName) >= 0) {
					sortKeyChooser.setText(columnName);
				}

			} else {
				sortKeyChooser.select(0);
			}

			String sortDirection = inputParameter.getSortDirection();
			if (sortDirection == null || sortDirection.equals(DesignChoiceConstants.SORT_DIRECTION_ASC)) {
				sortDirectionChooser.setText(CHOICE_ASCENDING);
			} else {
				sortDirectionChooser.setText(CHOICE_DESCENDING);
			}
		} else {
			sortKeyLabel.setEnabled(true);
			sortKeyChooser.setEnabled(true);
			sortDirectionLabel.setEnabled(false);
			sortDirectionChooser.setEnabled(false);
			distinct.setEnabled(false);
		}
	}

	private void initFormatField() {
		String type = getSelectedDataType();
		if ((DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals(lastControlType)
				&& DesignChoiceConstants.PARAM_TYPE_FLOAT.equals(type))
				|| (DesignChoiceConstants.PARAM_TYPE_FLOAT.equals(lastControlType)
						&& DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals(type))) {
			return;
		}
		IChoiceSet choiceSet = getFormatChoiceSet(type);
		if (choiceSet == null) {
			formatCategroy = formatPattern = null;
		} else {
			if (!loading || ((inputParameter.getCategory() == null && inputParameter.getPattern() == null))) {
				if (DesignChoiceConstants.PARAM_TYPE_STRING.equals(type)) {
					formatCategroy = choiceSet.findChoice(DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED)
							.getName();
				} else if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(type)) {
					formatCategroy = choiceSet.findChoice(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_UNFORMATTED)
							.getName();
				} else if (DesignChoiceConstants.PARAM_TYPE_DATE.equals(type)) {
					formatCategroy = choiceSet.findChoice(DesignChoiceConstants.DATE_FORMAT_TYPE_UNFORMATTED).getName();
				} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equals(type)) {
					formatCategroy = choiceSet.findChoice(DesignChoiceConstants.DATE_FORMAT_TYPE_UNFORMATTED).getName();
				} else if (DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals(type)
						|| DesignChoiceConstants.PARAM_TYPE_FLOAT.equals(type)
						|| DesignChoiceConstants.PARAM_TYPE_INTEGER.equals(type)) {
					formatCategroy = choiceSet.findChoice(DesignChoiceConstants.NUMBER_FORMAT_TYPE_UNFORMATTED)
							.getName();
				}
				formatPattern = null;
			} else {
				formatCategroy = inputParameter.getCategory();
				if (formatCategroy == null) {
					formatCategroy = DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED;
				}
				formatPattern = inputParameter.getPattern();

				Object formatValue = inputParameter.getProperty(IScalarParameterModel.FORMAT_PROP);
				if (formatValue instanceof FormatValue) {
					PropertyHandle propHandle = inputParameter.getPropertyHandle(IScalarParameterModel.FORMAT_PROP);
					FormatValue formatValueToSet = (FormatValue) formatValue;
					FormatValueHandle formatHandle = (FormatValueHandle) formatValueToSet.getHandle(propHandle);
					formatLocale = formatHandle.getLocale();
				}
			}
		}
		updateFormatField();
	}

	private List getColumnValueList() {
		try {
			if (columnChooser.getText() == null || columnChooser.getText().equals("")) //$NON-NLS-1$
			{
				MessageDialog.openWarning(getShell(),
						Messages.getString("ParameterDialog.emptyColumnValueExpression.title"), //$NON-NLS-1$
						Messages.getString("ParameterDialog.emptyColumnValueExpression.message")); //$NON-NLS-1$
				columnChooser.forceFocus();
				return Collections.EMPTY_LIST;
			}
			ArrayList valueList = new ArrayList();

			// Flow mode PARAM_EVALUATION_FLOW is propagated to data engine execution to
			// exclude filters defined on data set.
			valueList.addAll(SelectValueFetcher.getSelectValueList(ExpressionButtonUtil.getExpression(columnChooser),
					getDataSetHandle(), DataEngineFlowMode.PARAM_EVALUATION_FLOW));
			java.util.Collections.sort(valueList);
			return valueList;
		} catch (Exception e) {
			ExceptionHandler.handle(e);
			return Collections.EMPTY_LIST;
		}
	}

	private void refreshDataSets() {
		String selectedDataSetName = dataSetChooser.getText();
		String[] oldList = dataSetChooser.getItems();

		List dataSetList = new ArrayList();

		for (Iterator iterator = inputParameter.getModuleHandle().getVisibleDataSets().iterator(); iterator
				.hasNext();) {
			DataSetHandle DataSetHandle = (DataSetHandle) iterator.next();
			dataSetList.add(DataSetHandle.getQualifiedName());
		}

		// if(staticRadio.getSelection()) // linked data model is not applicable to
		// dynamic params.
		{
			for (Iterator itr = new LinkedDataSetAdapter().getVisibleLinkedDataSets().iterator(); itr.hasNext();) {
				dataSetList.add(itr.next().toString());
			}
		}
		if (inputParameter.getDataSet() != null && !dataSetList.contains(inputParameter.getDataSet().getName())) {
			dataSetList.add(0, inputParameter.getDataSet().getName());
		}

		if (oldList.length != dataSetList.size()) // it means new data set
		// is created.
		{
			String newName = findNewDataSet(Arrays.asList(oldList), dataSetList);
			if (newName != null) {
				selectedDataSetName = newName;
			}

			dataSetChooser.setItems((String[]) dataSetList.toArray(new String[] {}));
			if (StringUtil.isBlank(selectedDataSetName)) {
				dataSetChooser.select(0);
				refreshColumns(false);
			} else if (selectedDataSetName != null && dataSetChooser.indexOf(selectedDataSetName) != -1) {
				dataSetChooser.select(dataSetChooser.indexOf(selectedDataSetName));
				refreshColumns(false);
			}
			refreshSortByItems();
		}
	}

	private String findNewDataSet(List existingDataSets, List newDataSets) {
		for (int i = 0; i < newDataSets.size(); i++) {
			if (!existingDataSets.contains(newDataSets.get(i))) {
				return (String) newDataSets.get(i);
			}
		}
		return null;
	}

	private DataSetHandle getDataSetHandle() {
		return DataUtil.findDataSet(dataSetChooser.getText(), inputParameter.getModuleHandle());
	}

	private void refreshColumns(boolean onlyFilter) {
		if (columnChooser == null || columnChooser.isDisposed()) {
			return;
		}
		if (!onlyFilter) {
			DataSetHandle dataSetHandle = getDataSetHandle();

			try {
				columnList = DataUtil.getColumnList(dataSetHandle);
			} catch (SemanticException e) {
				ExceptionHandler.handle(e);
			}
			displayTextChooser.removeAll();
			displayTextChooser.add(NONE_DISPLAY_TEXT);
			for (Iterator iter = columnList.iterator(); iter.hasNext();) {
				displayTextChooser.add(((ResultSetColumnHandle) iter.next()).getColumnName());
			}

			Object button = displayTextChooser.getData(ExpressionButtonUtil.EXPR_BUTTON);
			if (button instanceof ExpressionButton) {
				if (!((ExpressionButton) button).isSupportType(ExpressionType.JAVASCRIPT)) {
					displayTextChooser.removeAll();
				}
			}

			displayTextChooser.setText(NONE_DISPLAY_TEXT);
		}
		String originalSelection = columnChooser.getText();
		columnChooser.removeAll();

		for (Iterator iter = columnList.iterator(); iter.hasNext();) {
			ResultSetColumnHandle cachedColumn = (ResultSetColumnHandle) iter.next();
			if (matchDataType(cachedColumn)) {
				columnChooser.add(cachedColumn.getColumnName());
			}
		}
		if (columnChooser.indexOf(originalSelection) != -1) {
			columnChooser.setText(originalSelection);
		}
		if (columnChooser.getItemCount() == 0) {
			columnChooser.add(""); //$NON-NLS-1$
		}

		Object button = columnChooser.getData(ExpressionButtonUtil.EXPR_BUTTON);
		if (button instanceof ExpressionButton) {
			if (!((ExpressionButton) button).isSupportType(ExpressionType.JAVASCRIPT)) {
				columnChooser.removeAll();
			}
		}

		// columnChooser.setEnabled( columnChooser.getItemCount( ) > 0 );
		// valueColumnExprButton.setEnabled( columnChooser.getItemCount( ) > 0
		// );
		updateMessageLine();
	}

	private boolean matchDataType(ResultSetColumnHandle column) {
		if (getSelectedDataType().equals(DesignChoiceConstants.PARAM_TYPE_STRING)
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_ANY.equals(column.getDataType())) {
			return true;
		}
		int type = DataAdapterUtil.modelDataTypeToCoreDataType(getSelectedDataType());
		int columnType = DataAdapterUtil.adaptModelDataType(column.getDataType());
		try {
			int[] compatibleTypes = DataAdapterUtil.getCompatibleDataTypes(type);
			for (int at : compatibleTypes) {
				if (columnType == at)
					return true;
			}
		} catch (AdapterException e) {
		}
		return false;
	}

	private String getInputControlType() {
		String type = null;
		if (inputParameter.getControlType() == null) {
			type = DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX;
		} else if (DesignChoiceConstants.PARAM_CONTROL_LIST_BOX.equals(inputParameter.getControlType())) {
			if (inputParameter.isMustMatch()) {
				type = PARAM_CONTROL_LIST;
			} else {
				type = PARAM_CONTROL_COMBO;
			}
		} else {
			type = inputParameter.getControlType();
		}
		return type;
	}

	private String getSelectedDataType() {
		String type = null;
		if (StringUtil.isBlank(dataTypeChooser.getText())) {
			type = inputParameter.getDataType();
		} else {
			IChoice choice = DATA_TYPE_CHOICE_SET.findChoiceByDisplayName(dataTypeChooser.getText());
			type = choice.getName();
		}
		return type;
	}

	/**
	 * Gets the internal name of the control type from the display name
	 */
	private String getSelectedControlType() {
		if (this.controlTypeHelper == null) {
			return getInputControlType();
		}
		controlTypeHelper.update(false);
		String type = (String) controlTypeHelper.getProperty(CONTROLTYPE_VALUE);
		if (type == null)
			return getInputControlType();
		else
			return type;
	}

	private void changeDataType() {
		String type = getSelectedDataType();
		if (type.equals(lastDataType)) {
			return;
		}

		// When data type is changed, validate the default value first. if the
		// old default value is invalid,
		// then set the default value to null, else let in remain it unchanged.
		// -- Begin --
		makeUniqueAndValid();
		// -- End --

		buildControlTypeList(type);

		initFormatField();

		if (type.equals(DesignChoiceConstants.PARAM_TYPE_STRING)) {
			clearDefaultValueChooser(isRequired.getSelection());
		} else if (!type.equals(DesignChoiceConstants.PARAM_TYPE_BOOLEAN)) {
			clearDefaultValueText();
			clearDefaultValueChooserSelections();
		}

		if ((isStatic())) {
			refreshStaticValueTable();
		} else {
			refreshColumns(true);
			refreshDynamicValueTable();
		}

		lastDataType = type;
		updateMessageLine();
	}

	private void buildControlTypeList(String type) {
		controlTypeHelper.setProperty(DATATYPE_VALUE, type);
		controlTypeHelper.setProperty(STATIC_VALUE, isStatic());
		controlTypeHelper.setProperty(CONTROLTYPE_INPUTVALUE, getInputControlType());
		controlTypeHelper.update(true);
	}

	// false: change anything; true: remove duplicated
	private boolean makeUniqueAndValid() {
		boolean change = false;
		try {

			Set set = new HashSet();

			if ((isStatic() && !distinct.isEnabled()) || (distinct.isEnabled() && !distinct.getSelection())) {
				if (choiceList != null) {
					for (Iterator iter = choiceList.iterator(); iter.hasNext();) {
						SelectionChoice choice = (SelectionChoice) iter.next();
						if (isValidValue(choice.getValue()) != null || set.contains(validateValue(choice.getValue()))) {
							if (enableAllowMultiValueVisible()) {
								iter.remove();
							}
							change = true;
						} else {
							set.add(validateValue(choice.getValue()));
						}
					}
				}
				set.clear();
			}

			if (defaultValueList != null) {
				for (Iterator iter = defaultValueList.iterator(); iter.hasNext();) {
					Expression expression = (Expression) iter.next();
					if (expression != null) {
						if (isValidValue(expression) != null || set.contains(validateValue(expression))) {
							if (enableAllowMultiValueVisible()) {
								iter.remove();
							}
							change = true;
						} else {
							set.add(validateValue(expression));
						}
					}
				}
			}
		} catch (BirtException e) {
			ExceptionHandler.handle(e);
		}
		return change;
	}

	private void changeControlType() {
		String type = getSelectedControlType();

		if (isStatic()) {

			if (!type.equals(lastControlType)) {
				if (DesignChoiceConstants.PARAM_CONTROL_CHECK_BOX.equals(type)) {
					clearArea(valueArea);
					switchToCheckBox();
				} else if (PARAM_CONTROL_COMBO.equals(type) || PARAM_CONTROL_LIST.equals(type)
						|| DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON.equals(type)) {
					// Radio ,Combo and List has the same UI
					if (!PARAM_CONTROL_COMBO.equals(lastControlType) && !PARAM_CONTROL_LIST.equals(lastControlType)
							&& !DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON.equals(lastControlType)) {
						clearArea(valueArea);
						switchToList();
					}
				} else if (DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX.equals(type)) {
					clearArea(valueArea);
					switchToText();
				}
				valueArea.layout();
				initValueArea();
				lastControlType = type;
			}
		}

		updateCheckBoxArea();
		boolean radioEnable = false;
		if (PARAM_CONTROL_COMBO.equals(getSelectedControlType()) || (PARAM_CONTROL_LIST.equals(getSelectedControlType())
				&& !DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN.equals(getSelectedDataType()))) {
			radioEnable = true;
		}
		if (radioEnable != staticRadio.isEnabled()) {
			staticRadio.setEnabled(radioEnable);
			dynamicRadio.setEnabled(radioEnable);
		}
		if (DesignChoiceConstants.PARAM_CONTROL_AUTO_SUGGEST.equals(getSelectedControlType())) {
			if (!dynamicRadio.getSelection()) {
				staticRadio.setSelection(false);
				dynamicRadio.setSelection(true);
				switchParamterType();
			}
			hideStartPointSection(false);
			listLimit.setEnabled(false);
		} else {
			hideStartPointSection(true);
			if (isStatic()) {
				listLimit.setEnabled(false);
			} else {
				listLimit.setEnabled(true);
			}

		}
		if (PARAM_CONTROL_LIST.equals(getSelectedControlType()) && allowMultiValueVisible) {
			allowMultiChoice.setVisible(true);
		} else {
			allowMultiChoice.setVisible(false);
		}

		// if change control type, keep first default value
		// if control type is text, do not save null default value
		if (isInitialized && defaultValueList != null && defaultValueList.size() > 0) {
			Expression expression = getFirstDefaultValue();
			if (enableAllowMultiValueVisible() && allowMultiChoice.getSelection()) {

			} else {
				defaultValueList.clear();
				if (isStatic() && (PARAM_CONTROL_COMBO.equals(type) || PARAM_CONTROL_LIST.equals(type)
						|| DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON.equals(type))) {
					defaultValueList.add(expression);
					valueTable.refresh();
				} else if (expression != null) {
					defaultValueList.add(expression);
				}
			}
		}
		updateMessageLine();
	}

	private void switchParamterType() {
		clearArea(valueArea);
		lastControlType = null;
		if (isStatic()) {
			switchToStatic();
		} else {
			switchToDynamic();
		}
		buildControlTypeList(getSelectedDataType());
		valueArea.layout();
		initValueArea();
		updateCheckBoxArea();

		Point size = displayArea.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		displayArea.setSize(size);
		displayArea.getParent().layout();
	}

	private void switchToCheckBox() {
		createLabel(valueArea, LABEL_DEFAULT_VALUE);

		Composite composite = new Composite(valueArea, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 0;
		layout.numColumns = 2;
		composite.setLayout(layout);

		defaultValueChooser = new CCombo(composite, SWT.BORDER);
		defaultValueChooser.add(CHOICE_NO_DEFAULT);
		defaultValueChooser.add(BOOLEAN_TRUE);
		defaultValueChooser.add(BOOLEAN_FALSE);
		defaultValueChooser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		defaultValueChooser.setVisibleItemCount(30);
		defaultValueChooser.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				String type = (String) defaultValueChooser.getData(ExpressionButtonUtil.EXPR_TYPE);

				List list = new ArrayList();
				list.addAll(Arrays.asList(defaultValueChooser.getItems()));

				switch (list.indexOf(defaultValueChooser.getText())) {
				case 0:
					defaultValueList = null;
					break;
				case 1:
					addDefaultValue("true", type);//$NON-NLS-1$
					break;
				case 2:
					addDefaultValue("false", type);//$NON-NLS-1$
					break;
				default:
					addDefaultValue(defaultValueChooser.getText(), type);// $NON-NLS-1$
				}
				updateMessageLine();
			}
		});

		IExpressionHelper helper = new IExpressionHelper() {

			public String getExpression() {
				if (defaultValueChooser != null) {
					return getDefaultValueChooserValue();
				} else
					return ""; //$NON-NLS-1$
			}

			public void setExpression(String expression) {
				if (defaultValueChooser != null)
					defaultValueChooser.setText(expression);
			}

			public void notifyExpressionChangeEvent(String oldExpression, String newExpression) {
				// preview( DEUtil.removeQuote( newExpression ) );
				if (defaultValueChooser != null)
					defaultValueChooser.setFocus();
			}

			public IExpressionProvider getExpressionProvider() {
				ExpressionProvider provider = new ExpressionProvider(inputParameter);
				provider.addFilter(new ParameterVariableFilter());
				return provider;
			}

			public String getExpressionType() {
				return (String) defaultValueChooser.getData(ExpressionButtonUtil.EXPR_TYPE);
			}

			public void setExpressionType(String exprType) {
				String value = getDefaultValueChooserValue();

				defaultValueChooser.setData(ExpressionButtonUtil.EXPR_TYPE, exprType);
				if (ExpressionType.CONSTANT.equals(exprType)) {
					defaultValueChooser.setEditable(false);
					if (defaultValueChooser.getItemCount() == 0) {
						defaultValueChooser.add(CHOICE_NO_DEFAULT);
						defaultValueChooser.add(BOOLEAN_TRUE);
						defaultValueChooser.add(BOOLEAN_FALSE);
					}
				} else {
					if (defaultValueChooser.getItemCount() != 0) {
						defaultValueChooser.remove(CHOICE_NO_DEFAULT);
						defaultValueChooser.remove(BOOLEAN_TRUE);
						defaultValueChooser.remove(BOOLEAN_FALSE);
					}
					defaultValueChooser.setEditable(true);
				}

				if (value == null || value.trim().length() == 0) {
					if (ExpressionType.CONSTANT.equals(exprType))
						defaultValueChooser.select(defaultValueChooser.indexOf(CHOICE_NO_DEFAULT));
					else
						defaultValueChooser.setText("");//$NON-NLS-1$
				} else if (ExpressionType.CONSTANT.equals(exprType)) {

					if (Boolean.valueOf(value).booleanValue()) {
						defaultValueChooser.select(defaultValueChooser.indexOf(BOOLEAN_TRUE));
					} else {
						defaultValueChooser.select(defaultValueChooser.indexOf(BOOLEAN_FALSE));
					}
				} else {
					defaultValueChooser.setText(value);
				}

				defaultValueChooser.notifyListeners(SWT.Modify, new Event());
			}

			public Object getContextObject() {
				return inputParameter;
			}

			public IExpressionContextFactory getExpressionContextFactory() {
				return new ExpressionContextFactoryImpl(getContextObject(), getExpressionProvider());
			}

		};
		ExpressionButton expressionButton = UIUtil.createExpressionButton(composite, SWT.PUSH);
		expressionButton.setExpressionHelper(helper);
		defaultValueChooser.setData(ExpressionButtonUtil.EXPR_BUTTON, expressionButton);
		defaultValueChooser.setData(ExpressionButtonUtil.EXPR_TYPE, ExpressionType.CONSTANT);
		expressionButton.refresh();
	}

	private void switchToList() {
		createLabel(valueArea, LABEL_VALUES);
		Composite tableAreaComposite = new Composite(valueArea, SWT.NONE);
		tableAreaComposite.setLayout(UIUtil.createGridLayoutWithoutMargin());
		tableAreaComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		staticTableArea = new TableArea(tableAreaComposite, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER,
				tableAreaModifier);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.heightHint = staticTableArea.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		staticTableArea.setLayoutData(data);

		Table table = staticTableArea.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		String[] columns;
		int[] columnWidth;
		columns = new String[] { null, COLUMN_IS_DEFAULT, COLUMN_VALUE, COLUMN_DISPLAY_TEXT, COLUMN_DISPLAY_TEXT_KEY };
		columnWidth = new int[] { 10, 70, 100, 100, 100 };

		for (int i = 0; i < columns.length; i++) {
			TableColumn column = new TableColumn(table, SWT.LEFT);
			column.setResizable(columns[i] != null);
			if (columns[i] != null) {
				column.setText(columns[i]);
			}
			column.setWidth(columnWidth[i]);
		}

		valueTable = staticTableArea.getTableViewer();
		valueTable.setColumnProperties(columns);
		valueTable.setContentProvider(contentProvider);
		valueTable.setLabelProvider(labelProvider);
		staticTableArea.setInput(choiceList);

		valueTable.getTable().addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (e.item instanceof TableItem) {
					if (!allowMultiChoice.getSelection() && valueTable.getTable().getSelectionCount() > 1) {
						valueTable.getTable().deselectAll();
						valueTable.getTable().setSelection((TableItem) e.item);
					}
					updateStaticTableButtons();

				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		Composite buttonBar = new Composite(tableAreaComposite, SWT.NONE);
		buttonBar.setLayout(UIUtil.createGridLayoutWithoutMargin(4, false));
		buttonBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		importValue = new Button(buttonBar, SWT.PUSH);
		importValue.setText(BUTTON_LABEL_IMPORT);
		setButtonLayoutData(importValue);
		// Disabled when no date set defined

		importValue.setEnabled(!inputParameter.getModuleHandle().getVisibleDataSets().isEmpty()
				// linked data model is not applicable to dynamic params.
				|| (staticRadio.getSelection() ? !new LinkedDataSetAdapter().getVisibleLinkedDataSets().isEmpty()
						: false));
		importValue.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				String type = getSelectedDataType();
				List choices = new ArrayList();
				Map labelMap = new HashMap();
				for (Iterator iter = choiceList.iterator(); iter.hasNext();) {
					SelectionChoice choice = (SelectionChoice) iter.next();
					choices.add(choice.getValue());
					if (choice.getLabel() != null) {
						labelMap.put(choice.getValue(), choice.getLabel());
					}
				}
				ImportValueDialog dialog = new ImportValueDialog(type, choices);
				if (distinct.isEnabled() && distinct.getSelection()) {
					dialog.setDistinct(false);
				} else {
					dialog.setDistinct(true);
				}

				if (isRequired.isEnabled() && !isRequired.getSelection()) {
					dialog.setRequired(false);
				} else {
					dialog.setRequired(true);
				}

				dialog.setValidate(new ImportValueDialog.IAddChoiceValidator() {

					public String validateString(String value) {
						String errorMessage = isValidValue(value);
						if (errorMessage != null) {
							return errorMessage;
						}
						return null;
					}
				});

				if (dialog.open() == OK) {
					// remove unexist default values
					String[] importValues = (String[]) dialog.getResult();
					choiceList.clear();
					for (int i = 0; i < importValues.length; i++) {
						SelectionChoice choice = StructureFactory.createSelectionChoice();
						choice.setValue(importValues[i]);
						if (labelMap.get(importValues[i]) != null) {
							choice.setLabel((String) labelMap.get(importValues[i]));
						}
						choiceList.add(choice);
					}
					if (defaultValueList != null) {
						List<String> importList = Arrays.asList(importValues);
						for (Expression expression : defaultValueList.toArray(new Expression[] {})) {
							if (!importList.contains(expression == null ? null : expression.getStringExpression()))
								defaultValueList.remove(expression);
						}
					}
					refreshStaticValueTable();
				}
			}
		});

		changeDefault = new Button(buttonBar, SWT.TOGGLE);
		changeDefault.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				SelectionChoice choice = (SelectionChoice) ((IStructuredSelection) valueTable.getSelection())
						.getFirstElement();
				if (isDefaultChoice(choice)) {
					// changeDefaultValue( null );
					Iterator iter = ((IStructuredSelection) valueTable.getSelection()).iterator();
					while (iter.hasNext()) {
						Object obj = iter.next();
						if (obj instanceof SelectionChoice) {
							removeDefaultValue(((SelectionChoice) obj).getValue());
						}
					}
				} else {
					// changeDefaultValue( choice.getValue( ) );
					Iterator iter = ((IStructuredSelection) valueTable.getSelection()).iterator();
					while (iter.hasNext()) {
						Object obj = iter.next();
						if (obj instanceof SelectionChoice) {
							addDefaultValue(((SelectionChoice) obj).getValue());
						}
					}
				}
				refreshStaticValueTable();
				changeDefault.getParent().layout();
			}
		});

		int width1 = UIUtil.getStringWidth(BUTTON_LABEL_REMOVE_DEFAULT, changeDefault) + 10;
		int width2 = UIUtil.getStringWidth(BUTTON_LABEL_SET_DEFAULT, changeDefault) + 10;
		int width = width1 >= width2 ? width1 : width2;

		GridData gd = new GridData();
		gd.widthHint = width;
		changeDefault.setLayoutData(gd);

		createPromptLine(tableAreaComposite);
		updateStaticTableButtons();
		createSortingArea(valueArea);
	}

	private void addDefaultValue(String value) {
		addDefaultValue(value, null);
	}

	private void addDefaultValue(String value, String exprType) {
		String type = ExpressionType.CONSTANT;
		if (exprType != null)
			type = exprType;
		if (defaultValueList == null) {
			defaultValueList = new ArrayList<Expression>();
		} else {
			// support multiple values when control type is list
			// and allow mulit choice is selected
			if (!PARAM_CONTROL_LIST.equals(getSelectedControlType()) || !allowMultiChoice.getSelection())
				defaultValueList.clear();
		}
		Expression expression = new Expression(value, type);
		if (!defaultValueList.contains(expression))
			defaultValueList.add(expression);
		updateMessageLine();
		updateFormatField();
	}

	private void removeDefaultValue(String value) {
		if (defaultValueList != null) {
			Expression expression = new Expression(value, ExpressionType.CONSTANT);
			defaultValueList.remove(expression);
			updateMessageLine();
			updateFormatField();
		}
	}

	private void changeDefaultValue(String oldVal, String newVal) {
		if (defaultValueList != null) {
			Expression expression = new Expression(oldVal, ExpressionType.CONSTANT);
			defaultValueList.remove(expression);

			expression = new Expression(newVal, ExpressionType.CONSTANT);
			if (!defaultValueList.contains(expression))
				defaultValueList.add(expression);
			updateMessageLine();
			updateFormatField();
		}
	}

	private void switchToText() {
		createDefaultEditor();
		createLabel(valueArea, null);
		createPromptLine(valueArea);
	}

	private void switchToStatic() {
		changeControlType();
		listLimit.setEnabled(false);
	}

	private void switchToDynamic() {
		changeControlType();
		createLabel(valueArea, LABEL_SELECT_DATA_SET);

		Composite dataSetArea = createExprArea();

		dataSetChooser = new Combo(dataSetArea, SWT.BORDER | SWT.READ_ONLY);
		dataSetChooser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		dataSetChooser.setVisibleItemCount(30);
		dataSetChooser.addSelectionListener(new SelectionAdapter() {

			public void widgetDefaultSelected(SelectionEvent e) {
				refreshColumns(false);
				refreshSortByItems();
			}

			public void widgetSelected(SelectionEvent e) {
				refreshColumns(false);
				refreshSortByItems();
			}
		}

		);
		createDataSet = new Button(dataSetArea, SWT.PUSH);
		createDataSet.setText(BUTTON_CREATE_DATA_SET);
		setButtonLayoutData(createDataSet);
		createDataSet.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				DataService.getInstance().createDataSet();

				refreshDataSets();
			}

		});

		createLabel(valueArea, LABEL_SELECT_VALUE_COLUMN);

		Composite columnArea = createExprArea();
		// columnChooser = new Combo( composite, SWT.BORDER | SWT.READ_ONLY );
		columnChooser = new Combo(columnArea, SWT.BORDER | SWT.DROP_DOWN);
		columnChooser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		columnChooser.setVisibleItemCount(30);
		columnChooser.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				updateMessageLine();
			}
		});

		ExpressionHelper columnHelper = new ExpressionHelper() {

			public String getExpression() {
				return ParameterDialog.this.getExpression(columnChooser, columnChooser.getText());
			}

			public void setExpression(String expression) {
				ParameterDialog.this.setExpression(columnChooser, expression);
			}

			public IExpressionProvider getExpressionProvider() {
//				return new ParameterDataSetExpressionProvider( inputParameter );
				ExpressionProvider provider = new ParameterDataSetExpressionProvider(inputParameter);
				provider.addFilter(new ParameterVariableFilter());

				return provider;
			}

			public IExpressionContextFactory getExpressionContextFactory() {
				Map<String, Object> extras = new HashMap<String, Object>();
				DataSetHandle dataSetHandle = inputParameter.getModuleHandle().findDataSet(dataSetChooser.getText());
				extras.put(ExpressionProvider.DATASETS, dataSetHandle);

				return new ExpressionContextFactoryImpl(getContextObject(), getExpressionProvider(), extras);
			}
		};

		ExpressionButtonUtil.createExpressionButton(columnArea, columnChooser, null, inputParameter, null, false,
				SWT.PUSH, columnHelper);

		// createLabel( composite, null );
		createLabel(valueArea, LABEL_SELECT_DISPLAY_TEXT);
		// displayTextChooser = new Combo( composite, SWT.BORDER | SWT.READ_ONLY
		// );
		Composite displayArea = createExprArea();
		displayTextChooser = new Combo(displayArea, SWT.BORDER | SWT.DROP_DOWN);
		displayTextChooser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		displayTextChooser.setVisibleItemCount(30);
		ExpressionHelper displayTextHelper = new ExpressionHelper() {

			public String getExpression() {
				return ParameterDialog.this.getExpression(displayTextChooser, displayTextChooser.getText());
			}

			public void setExpression(String expression) {
				ParameterDialog.this.setExpression(displayTextChooser, expression);
			}

			public IExpressionProvider getExpressionProvider() {
				ExpressionProvider provider = new ParameterExpressionProvider(inputParameter, dataSetChooser.getText());
				provider.addFilter(new ParameterVariableFilter());
				return provider;

			}

			public IExpressionContextFactory getExpressionContextFactory() {
				Map<String, Object> extras = new HashMap<String, Object>();
				DataSetHandle dataSetHandle = inputParameter.getModuleHandle().findDataSet(dataSetChooser.getText());
				extras.put(ExpressionProvider.DATASETS, dataSetHandle);

				return new ExpressionContextFactoryImpl(getContextObject(), getExpressionProvider(), extras);
			}
		};

		ExpressionButtonUtil.createExpressionButton(displayArea, displayTextChooser, null, inputParameter, null, false,
				SWT.PUSH, displayTextHelper);

		createDefaultEditor();

		createSortingArea(valueArea);
		createLabel(valueArea, null);
		createPromptLine(valueArea);
		listLimit.setEnabled(true);
	}

	private Composite createExprArea() {
		Composite exprArea = new Composite(valueArea, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 0;
		exprArea.setLayout(layout);
		exprArea.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return exprArea;
	}

	private void refreshSortByItems() {
		if (sortKeyChooser == null || sortKeyChooser.isDisposed()) {
			return;
		}
		sortKeyChooser.removeAll();
		sortKeyChooser.add(NONE_DISPLAY_TEXT);
		if (staticRadio.getSelection()) {
			sortKeyChooser.add(CHOICE_DISPLAY_TEXT);
			sortKeyChooser.add(CHOICE_VALUE_COLUMN);
		} else if (dynamicRadio.getSelection()) {
			for (Iterator iter = columnList.iterator(); iter.hasNext();) {
				sortKeyChooser.add(((ResultSetColumnHandle) iter.next()).getColumnName());
			}

		}

		sortKeyChooser.setText(NONE_DISPLAY_TEXT);
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
		// refresSortByItems();
		sortKeyChooser.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				if (!((Combo) e.widget).getText().equals(NONE_DISPLAY_TEXT)) {
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

		rightPart = new Composite(parent, SWT.NONE);
		data = new GridData(GridData.FILL_VERTICAL);
		rightPart.setLayoutData(data);
		GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 0;
		rightPart.setLayout(layout);

		editBtn = new Button(rightPart, SWT.PUSH);
		editBtn.setText(Messages.getString("FilterConditionBuilder.button.edit")); //$NON-NLS-1$
		editBtn.setToolTipText(Messages.getString("FilterConditionBuilder.button.edit.tooltip")); //$NON-NLS-1$
		setButtonLayoutData(editBtn);
		editBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				editTableValue();
			}

		});

		delBtn = new Button(rightPart, SWT.PUSH);
		delBtn.setText(Messages.getString("FilterConditionBuilder.button.delete")); //$NON-NLS-1$
		delBtn.setToolTipText(Messages.getString("FilterConditionBuilder.button.delete.tooltip")); //$NON-NLS-1$
		setButtonLayoutData(delBtn);
		delBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				delTableValue();
			}

		});

		delAllBtn = new Button(rightPart, SWT.PUSH);
		delAllBtn.setText(Messages.getString("FilterConditionBuilder.button.deleteall")); //$NON-NLS-1$
		delAllBtn.setToolTipText(Messages.getString("FilterConditionBuilder.button.deleteall.tooltip")); //$NON-NLS-1$
		setButtonLayoutData(delAllBtn);
		GridData gd = (GridData) delAllBtn.getLayoutData();
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.BEGINNING;
		delAllBtn.setLayoutData(gd);
		delAllBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				int count = defaultValueList.size();
				if (count > 0) {
					defaultValueList.clear();
					defaultValueViewer.refresh();
					updateDynamicTableButtons();
				} else {
					delAllBtn.setEnabled(false);
				}
			}

		});
	}

	protected void delTableValue() {
		int index = defaultValueViewer.getTable().getSelectionIndex();
		if (index > -1 && defaultValueList != null) {
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
			if (defaultValueList == null) {
				defaultValueViewer.getTable().removeAll();
				updateDynamicTableButtons();
			}
			delBtn.setEnabled(false);
		}
	}

	protected void editTableValue() {

		IStructuredSelection selection = (IStructuredSelection) defaultValueViewer.getSelection();
		if (selection.getFirstElement() != null && selection.getFirstElement() instanceof Expression) {
			Expression expression = (Expression) selection.getFirstElement();

			ExpressionProvider provider = new ExpressionProvider(inputParameter);

			ExpressionEditor editor = new ExpressionEditor(
					Messages.getString("ParameterDialog.ExpressionEditor.Title")); //$NON-NLS-1$
			editor.setInput(inputParameter, provider, true);
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
			editBtn.setEnabled(false);
		}

	}

	private void clearDefaultValueText() {
		if (defaultValueChooser == null || defaultValueChooser.isDisposed())
			return;
		String textValue = defaultValueChooser.getText();
		if (textValue != null && (textValue.trim().length() == 0 || textValue.trim().equals(EMPTY_VALUE))) {
			defaultValueChooser.setText(""); //$NON-NLS-1$
		}
	}

	private void clearDefaultValueChooserSelections() {
		if (defaultValueChooser == null || defaultValueChooser.isDisposed())
			return;
		if (defaultValueChooser.getItemCount() > 1) {
			String text = defaultValueChooser.getText();
			defaultValueChooser.removeAll();

			if (!isStatic()) {
				defaultValueChooser.add(EMPTY_VALUE);
				defaultValueChooser.add(CHOICE_SELECT_VALUE);
			}

			if (canUseEmptyValue()) {
				defaultValueChooser.setText(text);
			} else {
				if (EMPTY_VALUE.equals(text) || NULL_VALUE.equals(text)) {

				} else {
					defaultValueChooser.setText(text);
				}
			}
		}
	}

	private void createDefaultEditor() {
		Label label = createLabel(valueArea, LABEL_DEFAULT_VALUE);
		GridData gd = (GridData) label.getLayoutData();
		gd.verticalAlignment = SWT.BEGINNING;
		label.setLayoutData(gd);

		Composite composite = new Composite(valueArea, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 0;
		layout.numColumns = 3;
		composite.setLayout(layout);

		defaultValueChooser = new CCombo(composite, SWT.BORDER);
		defaultValueChooser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		defaultValueChooser.setVisibleItemCount(30);
		if (!isStatic()) {
			defaultValueChooser.add(EMPTY_VALUE);
			defaultValueChooser.add(CHOICE_SELECT_VALUE);
		}
		// if ( getSelectedDataType( ).equals(
		// DesignChoiceConstants.PARAM_TYPE_STRING )
		// && !isRequired.getSelection( ) )
		// {
		// defaultValueChooser.add( CHOICE_NULL_VALUE );
		// defaultValueChooser.add( CHOICE_BLANK_VALUE );
		// }

		defaultValueChooser.addVerifyListener(new VerifyListener() {

			public void verifyText(VerifyEvent e) {
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

		defaultValueChooser.addMouseListener(new MouseAdapter() {

			public void mouseDown(MouseEvent e) {
				if (!isStatic()) {
					if (canUseEmptyValue()) {
						if (defaultValueChooser.indexOf(EMPTY_VALUE) != -1)
							defaultValueChooser.remove(EMPTY_VALUE);
						defaultValueChooser.add(EMPTY_VALUE);
					} else {
						if (defaultValueChooser.indexOf(EMPTY_VALUE) != -1)
							defaultValueChooser.remove(EMPTY_VALUE);
					}
				}
			}

		});

		defaultValueChooser.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (defaultValueChooser.getSelectionIndex() == -1)
					return;
				String selection = defaultValueChooser.getItem(defaultValueChooser.getSelectionIndex());
				if (selection.equals(CHOICE_SELECT_VALUE)) {

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

								IExpressionConverter exprConverter = ExpressionUtility.getExpressionConverter(
										(String) defaultValueChooser.getData(ExpressionButtonUtil.EXPR_TYPE));
								if (exprConverter != null) {
									selectedValue = exprConverter.getConstantExpression(selectedValue,
											getSelectedDataType());
								}
								String value = DEUtil.resolveNull(selectedValue);
								if (value.equals("")) //$NON-NLS-1$
									defaultValueChooser.setText(EMPTY_VALUE);
								else
									defaultValueChooser.setText(value);
								addDynamicDefaultValue();
							}
						}
					}
				} else {
					if (isStatic()) {
						refreshStaticValueTable();
					} else {
						addDynamicDefaultValue();
					}
				}
			}
		});

		defaultValueChooser.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				handleDefaultValueModifyEvent();
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
				ExpressionProvider provider = new ExpressionProvider(inputParameter);
				provider.addFilter(new ParameterVariableFilter());
				return provider;
			}

			public String getExpressionType() {
				return (String) defaultValueChooser.getData(ExpressionButtonUtil.EXPR_TYPE);
			}

			public void setExpressionType(String exprType) {
				defaultValueChooser.setData(ExpressionButtonUtil.EXPR_TYPE, exprType);
				defaultValueChooser.notifyListeners(SWT.Modify, new Event());
			}

			public Object getContextObject() {
				return inputParameter;
			}

			public IExpressionContextFactory getExpressionContextFactory() {
				return new ExpressionContextFactoryImpl(inputParameter, getExpressionProvider());
			}

		};
		ExpressionButton expressionButton = UIUtil.createExpressionButton(composite, SWT.PUSH);
		expressionButton.setExpressionHelper(helper);
		defaultValueChooser.setData(ExpressionButtonUtil.EXPR_BUTTON, expressionButton);
		defaultValueChooser.setData(ExpressionButtonUtil.EXPR_TYPE, ExpressionType.CONSTANT);
		expressionButton.refresh();

		addButton = new Button(composite, SWT.PUSH);
		addButton.setText(Messages.getString("ParameterDialog.DefaultValue.Add")); //$NON-NLS-1$
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		addButton.setLayoutData(gd);
		addButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				addDynamicDefaultValue();
				updateDynamicTableButtons();
			}

		});

		createMulitipleValueListComposite(composite);

		initDefaultValueViewer();
	}

	private void createPromptLine(Composite parent) {
		promptMessageLine = new Label(parent, SWT.NONE);
		promptMessageLine.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	private Object validateValue(String value) throws BirtException {
		return validateValue(value, null);
	}

	private Object validateValue(Expression expression) throws BirtException {
		if (expression == null)
			return validateValue(null, null);
		else
			return validateValue(expression.getStringExpression(), expression.getType());
	}

	private Object validateValue(String value, String type) throws BirtException {
		String tempdefaultValue = value;
		String exprType = ExpressionType.CONSTANT;
		if (type != null)
			exprType = type;

		if (!((DesignChoiceConstants.PARAM_TYPE_STRING.endsWith(getSelectedDataType()))
				|| (DesignChoiceConstants.PARAM_TYPE_BOOLEAN.endsWith(getSelectedDataType())))) {
			if (ExpressionType.CONSTANT.equals(exprType)) {
				if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(getSelectedDataType())) {
					tempdefaultValue = ParameterUtil.convertToStandardFormat(DataTypeUtil.toDate(tempdefaultValue));
				} else if (DesignChoiceConstants.PARAM_TYPE_DATE.equals(getSelectedDataType())) {
					tempdefaultValue = ParameterUtil.convertToStandardFormat(DataTypeUtil.toSqlDate(tempdefaultValue));
				} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equals(getSelectedDataType())) {
					tempdefaultValue = ParameterUtil.convertToStandardFormat(DataTypeUtil.toSqlTime(tempdefaultValue));
				}

				return ParameterValidationUtil.validate(getSelectedDataType(), ParameterUtil.STANDARD_DATE_TIME_PATTERN,
						tempdefaultValue, ULocale.getDefault());
			} else
				return tempdefaultValue;

		}
		if (DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals(getSelectedDataType())) {
			if (tempdefaultValue != null && tempdefaultValue.equals(CHOICE_NO_DEFAULT)) {
				return DataTypeUtil.toBoolean(null);
			}
			if (ExpressionType.CONSTANT.equals(exprType))
				return DataTypeUtil.toBoolean(tempdefaultValue);
			else
				return tempdefaultValue;
		} else
			return tempdefaultValue;
	}

	private void validateValueList(List<Expression> values) throws BirtException {
		if (values != null) {
			for (Expression value : values) {
				validateValue(value);
			}
		}
	}

	protected void okPressed() {
		// Validate the date first -- begin -- bug 164765

		try {
			validateValueList(defaultValueList);
		} catch (BirtException e1) {
			ExceptionHandler.handle(e1);
			return;
		}

		// Validate the date first -- end --

		try {
			// Save the name and display name
			inputParameter.setName(nameEditor.getText());
			inputParameter.setPromptText(UIUtil.convertToModelString(promptTextEditor.getText(), true));

			inputParameter.setParamType(DesignChoiceConstants.SCALAR_PARAM_TYPE_SIMPLE);
			String newControlType = getSelectedControlType();
			if (PARAM_CONTROL_COMBO.equals(newControlType)) {
				newControlType = DesignChoiceConstants.PARAM_CONTROL_LIST_BOX;
				// inputParameter.setMustMatch( true );
				inputParameter.setMustMatch(false);
			} else if (PARAM_CONTROL_LIST.equals(newControlType)) {
				newControlType = DesignChoiceConstants.PARAM_CONTROL_LIST_BOX;
				// inputParameter.setMustMatch( false );
				inputParameter.setMustMatch(true);

				if (allowMultiChoice.isVisible() && allowMultiChoice.getSelection()) {
					inputParameter.setParamType(DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE);
				}
			} else {
				inputParameter.setProperty(ScalarParameterHandle.MUCH_MATCH_PROP, null);
			}

			// Save control type
			inputParameter.setControlType(newControlType);

			if (!isStatic() || (DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX.equals(newControlType)
					|| DesignChoiceConstants.PARAM_CONTROL_CHECK_BOX.equals(newControlType)))
				inputParameter.setDefaultValueList(defaultValueList);
			else if (isStatic()) {
				boolean flag = false;
				for (int i = 0; defaultValueList != null && i < defaultValueList.size(); i++) {
					Expression expr = defaultValueList.get(i);
					String value = null;
					if (expr != null) {
						value = expr.getStringExpression();
					}

					if (choiceList != null) {
						for (Iterator iter = choiceList.iterator(); iter.hasNext();) {
							SelectionChoice choice = (SelectionChoice) iter.next();
							if (isEqual(choice.getValue(), value)) {
								flag = true;
							}
						}
					}
					if (!flag) {
						defaultValueList.remove(i);
						i--;
					}
				}
				if (flag)
					inputParameter.setDefaultValueList(defaultValueList);
				else
					inputParameter.setDefaultValueList(null);
			} else
				inputParameter.setDefaultValueList(null);

			// Set data type
			inputParameter
					.setDataType(DATA_TYPE_CHOICE_SET.findChoiceByDisplayName(dataTypeChooser.getText()).getName());

			PropertyHandle selectionChioceList = inputParameter
					.getPropertyHandle(ScalarParameterHandle.SELECTION_LIST_PROP);
			// Clear original choices list
			selectionChioceList.setValue(new ArrayList());

			if (isStatic()) {
				// Save static choices list
				inputParameter.setValueType(DesignChoiceConstants.PARAM_VALUE_TYPE_STATIC);
				if (!DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX.equals(newControlType)
						&& !DesignChoiceConstants.PARAM_CONTROL_CHECK_BOX.equals(newControlType)) {
					if (choiceList != null) {
						SelectionChoice originalChoice = null;
						for (Iterator iter = choiceList.iterator(); iter.hasNext();) {
							SelectionChoice choice = (SelectionChoice) iter.next();
							originalChoice = editChoiceMap.get(choice);
							if (originalChoice != null) {
								originalChoice.setValue(choice.getValue());
								originalChoice.setLabel(choice.getLabel());
								originalChoice.setLabelResourceKey(choice.getLabelResourceKey());
								choice = originalChoice;
							}
							if (isValidValue(choice.getValue()) == null) {
								selectionChioceList.addItem(choice);
							}
						}
					}
				}
				inputParameter.setDataSet(null);
				inputParameter.setValueExpr(null);
				inputParameter.setLabelExpr(null);
			} else {
				// Save dynamic settings
				inputParameter.setValueType(DesignChoiceConstants.PARAM_VALUE_TYPE_DYNAMIC);
				inputParameter.setDataSet(DataUtil.findDataSet(dataSetChooser.getText()));
				// inputParameter.setValueExpr( getExpression(
				// columnChooser.getText( ) ) );
				{
					Expression expression = new Expression(getExpression(columnChooser, columnChooser.getText()),
							(String) columnChooser.getData(ExpressionButtonUtil.EXPR_TYPE));
					inputParameter.setExpressionProperty(ScalarParameterHandle.VALUE_EXPR_PROP, expression);
				}
				if (displayTextChooser.getText().equals(LABEL_NULL)) {
					inputParameter.setLabelExpr(""); //$NON-NLS-1$
				} else {
					Expression expression = new Expression(
							getExpression(displayTextChooser, displayTextChooser.getText()),
							(String) displayTextChooser.getData(ExpressionButtonUtil.EXPR_TYPE));
					inputParameter.setExpressionProperty(ScalarParameterHandle.LABEL_EXPR_PROP, expression);
				}
				if (startPointTypeHelper != null) {
					if (startPointTypeHelper.getControl() != null && startPointTypeHelper.getControl().isVisible()) {
						startPointTypeHelper.update(false);
						inputParameter
								.setAutoSuggestThreshold((Integer) startPointTypeHelper.getProperty(STARTPOINT_VALUE));
					}
				}
			}

			// Save help text
			inputParameter.setHelpText(UIUtil.convertToModelString(helpTextEditor.getText(), false));

			// Save format
			inputParameter.setCategory(formatCategroy);
			inputParameter.setPattern(formatPattern);

			Object value = inputParameter.getProperty(IScalarParameterModel.FORMAT_PROP);
			if (value instanceof FormatValue) {
				PropertyHandle propHandle = inputParameter.getPropertyHandle(IScalarParameterModel.FORMAT_PROP);
				FormatValue formatValueToSet = (FormatValue) value;
				FormatValueHandle formatHandle = (FormatValueHandle) formatValueToSet.getHandle(propHandle);
				formatHandle.setLocale(formatLocale);
			}

			// if ( isStatic( )
			// && ( PARAM_CONTROL_COMBO.equals( getSelectedControlType( ) ) ||
			// DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON.equals(
			// getSelectedControlType( ) ) )
			// && !containValue( null, defaultValue, COLUMN_VALUE ) )
			// {
			// defaultValue = null;
			// }

			// Save options
			if (dirtyProperties.containsKey(CHECKBOX_HIDDEN)) {
				inputParameter.setHidden(getProperty(CHECKBOX_HIDDEN));
			}

			if (dirtyProperties.containsKey(CHECKBOX_ISREQUIRED)) {
				inputParameter.setIsRequired(getProperty(CHECKBOX_ISREQUIRED));
			}

			if (doNotEcho.isEnabled()) {
				if (dirtyProperties.containsKey(CHECKBOX_DO_NOT_ECHO)) {
					inputParameter.setConcealValue(getProperty(CHECKBOX_DO_NOT_ECHO));
				}
			} else {
				inputParameter.setProperty(ScalarParameterHandle.CONCEAL_VALUE_PROP, null);
			}

			if (distinct.isEnabled()) {
				inputParameter.setDistinct(!distinct.getSelection());
			} else {
				inputParameter.setDistinct(true);
			}

			if (sorttingArea != null && !sorttingArea.isDisposed() && sorttingArea.isVisible()) {
				if (sortKeyChooser.getText().equals(NONE_DISPLAY_TEXT)) {
					inputParameter.setFixedOrder(true);
					inputParameter.setSortBy(null);
					inputParameter.setSortDirection(null);
					inputParameter.setSortByColumn(null);
				} else {
					if (isStatic()) {
						inputParameter.setFixedOrder(false);
						if (sortKeyChooser.getText().equals(CHOICE_DISPLAY_TEXT)) {
							inputParameter.setSortBy(DesignChoiceConstants.PARAM_SORT_VALUES_LABEL);
						} else if (sortKeyChooser.getText().equals(CHOICE_VALUE_COLUMN)) {
							inputParameter.setSortBy(DesignChoiceConstants.PARAM_SORT_VALUES_VALUE);
						}
						inputParameter.setSortByColumn(null);
					} else if (dynamicRadio.getSelection()) {
						inputParameter.setSortBy(null);
						inputParameter.setFixedOrder(false);
						inputParameter.setSortByColumn(getExpression(sortKeyChooser.getText()));
					}

					if (sortDirectionChooser.getText().equals(CHOICE_ASCENDING)) {
						inputParameter.setSortDirection(DesignChoiceConstants.SORT_DIRECTION_ASC);
					} else if (sortDirectionChooser.getText().equals(CHOICE_DESCENDING)) {
						inputParameter.setSortDirection(DesignChoiceConstants.SORT_DIRECTION_DESC);
					}
				}
			} else {
				inputParameter.setProperty(ScalarParameterHandle.FIXED_ORDER_PROP, null);
			}

			// Save limits
			if (!isStatic() && !StringUtil.isBlank(listLimit.getText())
					&& !DesignChoiceConstants.PARAM_CONTROL_AUTO_SUGGEST.equals(getSelectedControlType())) {
				try {
					inputParameter.setListlimit(Integer.parseInt(listLimit.getText()));
				} catch (NumberFormatException ex) {
					ExceptionHandler.openErrorMessageBox(ERROR_TITLE_INVALID_LIST_LIMIT, MessageFormat.format(
							ERROR_MSG_INVALID_LIST_LIMIT, new Object[] { Integer.toString(Integer.MAX_VALUE) }));
				}
			} else {
				inputParameter.setProperty(ScalarParameterHandle.LIST_LIMIT_PROP, null);
			}
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
			return;
		}
		setResult(inputParameter);
		super.okPressed();
	}

	private Label createLabel(Composite parent, String content) {
		Label label = new Label(parent, SWT.NONE);
		if (content != null) {
			label.setText(content);
		}
		setLabelLayoutData(label);
		return label;
	}

	private void setLabelLayoutData(Label label) {
		GridData gd = new GridData();
		if (label.getText().equals(LABEL_VALUES)) {
			gd.verticalAlignment = GridData.BEGINNING;
		}
		label.setLayoutData(gd);

	}

	private void addCheckBoxListener(final Button checkBox, final String key) {
		checkBox.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				checkBoxChange(checkBox, key);
			}
		});
	}

	/**
	 * @param key
	 * @param checkBox
	 * 
	 */
	protected void checkBoxChange(Button checkBox, String key) {
		dirtyProperties.put(key, Boolean.valueOf(checkBox.getSelection()));
		if (CHECKBOX_ISREQUIRED.equals(key) || CHECKBOX_DISTINCT.equals(key)) {
			if ((isStatic() && !distinct.isEnabled()) || (distinct.isEnabled() && !distinct.getSelection())) {
				boolean change = makeUniqueAndValid();
				if (change) {
					if (isStatic()) {
						refreshStaticValueTable();
					} else {
						refreshDynamicValueTable();
					}
				}
			}
			if (getSelectedDataType().equals(DesignChoiceConstants.PARAM_TYPE_STRING)) {
				clearDefaultValueChooser(checkBox.getSelection());
			}

			handleDefaultValueModifyEvent();

			updateMessageLine();
		}
	}

	private void clearDefaultValueChooser(boolean isChecked) {
		if (isChecked) {
			clearDefaultValueText();
			clearDefaultValueChooserSelections();
		} else {
			if (defaultValueChooser == null || defaultValueChooser.isDisposed()
					|| defaultValueChooser.getItemCount() > 1)
				return;
			// defaultValueChooser.add( CHOICE_NULL_VALUE );
			// defaultValueChooser.add( CHOICE_BLANK_VALUE );
		}
	}

	private void clearArea(Composite area) {
		Control[] children = area.getChildren();
		for (int i = 0; i < children.length; i++) {
			children[i].dispose();
		}
	}

	private void updateDynamicTableButtons() {
		StructuredSelection selection = (StructuredSelection) defaultValueViewer.getSelection();
		boolean enable = (selection.size() == 1);
		editBtn.setEnabled(enable);
		delBtn.setEnabled(!selection.isEmpty());
		delAllBtn.setEnabled(defaultValueViewer.getTable().getItemCount() > 0);

		String type = (String) defaultValueChooser.getData(ExpressionButtonUtil.EXPR_TYPE);
		String value = UIUtil.convertToModelString(defaultValueChooser.getText(), false);
		if (value != null) {
			if (value.equals(EMPTY_VALUE))
				value = ""; //$NON-NLS-1$
			else if (value.equals(NULL_VALUE))
				value = null;
		}
		Expression expression = null;
		if (value != null)
			expression = new Expression(value, type);

		if (defaultValueChooser.getText().trim().length() == 0) {
			addButton.setEnabled(false);
		} else {
			if (defaultValueList != null && defaultValueList.contains(expression))
				addButton.setEnabled(false);
			else
				addButton.setEnabled(true);
		}
		updateMessageLine();
	}

	private void updateStaticTableButtons() {
		staticTableArea.updateButtons();
		boolean isEnable = true;
		SelectionChoice selectedChoice = null;

		if (valueTable.getSelection().isEmpty()) {
			isEnable = false;
		} else if (((IStructuredSelection) valueTable.getSelection()).size() > 1) {
			selectedChoice = (SelectionChoice) ((IStructuredSelection) valueTable.getSelection()).getFirstElement();
			boolean firstIsDefault = isDefaultChoice(selectedChoice);
			Iterator iter = ((IStructuredSelection) valueTable.getSelection()).iterator();
			while (iter.hasNext()) {
				Object obj = iter.next();
				if (obj instanceof SelectionChoice) {
					// contains both default and non-default items
					if (firstIsDefault ^ isDefaultChoice((SelectionChoice) obj)) {
						isEnable = false;
						break;
					}
				}
			}
		} else {
			selectedChoice = (SelectionChoice) ((IStructuredSelection) valueTable.getSelection()).getFirstElement();
			String value = selectedChoice.getValue();
			try {
				validateValue(value);
			} catch (BirtException e) {
				isEnable = false;
			}
			// bre can't support null constant value.
			if (value == null)
				isEnable = false;
		}
		boolean isDefault = isEnable && isDefaultChoice(selectedChoice);
		if (isDefault) {
			changeDefault.setText(BUTTON_LABEL_REMOVE_DEFAULT);
		} else {
			changeDefault.setText(BUTTON_LABEL_SET_DEFAULT);
		}

		changeDefault.setSelection(isDefault);
		changeDefault.setEnabled(isEnable);
		updateMessageLine();
	}

	protected void updateButtons() {
		boolean canFinish = !StringUtil.isBlank(nameEditor.getText());
		if (canFinish) {
			if (columnChooser != null && !columnChooser.isDisposed() && !isStatic()) {
				String expression = getExpression(columnChooser, columnChooser.getText());
				canFinish = canFinish && (expression != null && expression.length() > 0);
			}
		}
		getOkButton().setEnabled(canFinish);
		super.updateButtons();
	}

	private void updateCheckBoxArea() {

		// Do not echo check
		if (DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX.equals(getSelectedControlType())
				|| DesignChoiceConstants.PARAM_CONTROL_AUTO_SUGGEST.equals(getSelectedControlType())) {
			if (!DesignChoiceConstants.PARAM_CONTROL_AUTO_SUGGEST.equals(getSelectedControlType()))
				doNotEcho.setEnabled(true);
			else
				doNotEcho.setEnabled(false);
			distinct.setEnabled(false);
		} else {
			doNotEcho.setEnabled(false);
			distinct.setEnabled(true);
		}
	}

	private void updateMessageLine() {
		String errorMessage = validateName();
		if (errorMessage == null) {
			// 1. No available column error
			if (!isStatic() && columnChooser != null && !columnChooser.isDisposed()) {
				if (columnChooser.getItemCount() == 0) {
					errorMessage = ERROR_MSG_NO_AVAILABLE_COLUMN;
				} else if (columnChooser.getText().trim().length() == 0) {
					errorMessage = ERROR_MSG_VALUE_COLUMN_EMPTY;
				}
			}

			// 2. No default value error
			// if ( defaultValue == null
			// && ( PARAM_CONTROL_COMBO.equals( getSelectedControlType( ) ) ||
			// DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON.equals(
			// getSelectedControlType( ) ) ) )
			// {
			// // if ( isStatic( ) )
			// // {
			// // errorMessage = ( !canBeNull( ) || !containValue( null,
			// // null,
			// // COLUMN_VALUE ) ) ? ERROR_MSG_NO_DEFAULT_VALUE
			// // : null;
			// // }
			// // else
			// // {
			// // errorMessage = canBeNull( ) ? null
			// // : ERROR_MSG_NO_DEFAULT_VALUE;
			// // }
			// errorMessage = canBeNull( ) ? null : ERROR_MSG_NO_DEFAULT_VALUE;
			// }
		}
		if (errorMessage != null) {
			// errorMessageLine.setText( errorMessage );
			// errorMessageLine.setImage( ERROR_ICON );
			setErrorMessage(errorMessage);
		} else {
			// errorMessageLine.setText( "" ); //$NON-NLS-1$
			// errorMessageLine.setImage( null );
			setErrorMessage(null);
		}
		if (promptMessageLine != null && !promptMessageLine.isDisposed()) {
			if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equals(getSelectedDataType())) {
				promptMessageLine.setText(LABEL_DATETIME_PROMPT);
			} else if (DesignChoiceConstants.PARAM_TYPE_DATE.equals(getSelectedDataType())) {
				promptMessageLine.setText(LABEL_DATE_PROMPT);
			} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equals(getSelectedDataType())) {
				promptMessageLine.setText(LABEL_TIME_PROMPT);
			} else {
				promptMessageLine.setText(""); //$NON-NLS-1$
			}
		}
		updateButtons();
	}

	private String validateName() {
		String name = nameEditor.getText().trim();
		if (name.length() == 0) {
			return ERROR_MSG_NAME_IS_EMPTY;
		}
		if (!name.equals(inputParameter.getName()) && inputParameter.getModuleHandle().findParameter(name) != null) {
			return ERROR_MSG_DUPLICATED_NAME;
		}
		try {
			validateValueList(defaultValueList);
		} catch (BirtException e) {
			return ERROR_MSG_MISMATCH_DATA_TYPE;
		}

		return null;
	}

	private String getDefaultValueChooserValue() {
		String defaultValue;
		List list = new ArrayList();
		list.addAll(Arrays.asList(defaultValueChooser.getItems()));
		switch (list.indexOf(defaultValueChooser.getText())) {
		case 0:
			defaultValue = null;
			break;
		case 1:
			defaultValue = "true";//$NON-NLS-1$
			break;
		case 2:
			defaultValue = "false";//$NON-NLS-1$
			break;
		default:
			defaultValue = defaultValueChooser.getText();// $NON-NLS-1$
		}
		return defaultValue;
	}

	private void refreshStaticValueTable() {
		if (valueTable != null && !valueTable.getTable().isDisposed()) {
			valueTable.refresh();
			updateStaticTableButtons();
		}
	}

	private void refreshDynamicValueTable() {
		if (defaultValueViewer != null && !defaultValueViewer.getTable().isDisposed()) {
			defaultValueViewer.refresh();
			updateDynamicTableButtons();
		}
	}

	private boolean getProperty(String key) {
		return ((Boolean) dirtyProperties.get(key)).booleanValue();
	}

	// private String format( String string )
	// {
	// return ParameterUtil.format( string,
	// getSelectedDataType( ),
	// formatCategroy,
	// formatPattern,
	// canBeNull( ) );
	// }

	/**
	 * Check if the specified value is valid
	 * 
	 * @param value the value to check
	 * @return Returns the error message if the input value is invalid,or null if it
	 *         is valid
	 */

	private String isValidValue(String value) {
		return isValidValue(value, null);
	}

	private String isValidValue(Expression expression) {
		if (expression == null) {
			return isValidValue(null, null);
		} else
			return isValidValue(expression.getStringExpression(), expression.getType());
	}

	private String isValidValue(String value, String exprType) {
		if (canBeNull()) {
			if (value == null || value.length() == 0) {
				return null;
			}
		} else {
			if (value == null || value.length() == 0) {
				return ERROR_MSG_CANNOT_BE_NULL;
			}
		}
		// bug 153405
		// if ( value == null || value.length( ) == 0 )
		// {
		// return ERROR_MSG_CANNOT_BE_NULL;
		// }
		if (canBeBlank()) {
			if (StringUtil.isBlank(value)) {
				return null;
			}
		} else {
			if (StringUtil.isBlank(value)) {
				return ERROR_MSG_CANNOT_BE_BLANK;
			}
		}
		try {
			validateValue(value, exprType);
		} catch (BirtException e) {
			return ERROR_MSG_MISMATCH_DATA_TYPE;
		}
		return null;
	}

	private boolean isEqual(String value1, String value2) {
		Object v1 = null;
		Object v2 = null;
		if ((value1 == null && value2 != null) || (value1 != null && value2 == null)) {
			return false;
		}

		try {
			v1 = validateValue(value1);
			v2 = validateValue(value2);
		} catch (BirtException e) {
			return false;
		}
		if (v1 == null) {
			return v2 == null;
		}
		if (v1 instanceof Double && v2 instanceof Double) {
			return ((Double) v1).compareTo((Double) v2) == 0;
		}
		if (v1 instanceof BigDecimal && v2 instanceof BigDecimal) {
			return ((BigDecimal) v1).compareTo((BigDecimal) v2) == 0;
		}
		if (v1 instanceof Integer && v2 instanceof Integer) {
			return ((Integer) v1).compareTo((Integer) v2) == 0;
		}
		return v1.equals(v2);
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

	private void updateFormatField() {
		ULocale locale = formatLocale;
		if (locale == null)
			locale = ULocale.getDefault();

		String displayFormat;
		String previewString;
		String type = getSelectedDataType();
		IChoiceSet choiceSet = getFormatChoiceSet(type);

		Expression expression = getFirstDefaultValue();
		String defaultValue = expression == null ? null : expression.getStringExpression();
		String exprType = expression == null ? ExpressionType.CONSTANT : expression.getType();
		if (defaultValue != null) {
			defaultValue = defaultValue.trim();
		}
		if (ExpressionType.JAVASCRIPT.equals(exprType))
			defaultValue = null;

		if (choiceSet == null) { // Boolean type;
			displayFormat = DEUtil.getMetaDataDictionary().getChoiceSet(DesignChoiceConstants.CHOICE_STRING_FORMAT_TYPE)
					.findChoice(DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED).getDisplayName();
			previewString = "True"; //$NON-NLS-1$
		} else {
			if (formatCategroy == null || choiceSet.findChoice(formatCategroy) == null) {
				return;
			}
			if (choiceSet.findChoice(formatCategroy) == null) {
				return;
			}
			displayFormat = choiceSet.findChoice(formatCategroy).getDisplayName();
			if (ParameterUtil.isCustomCategory(formatCategroy)) {
				displayFormat += ": " + formatPattern; //$NON-NLS-1$
			}

			if (type.equals(DesignChoiceConstants.PARAM_TYPE_DATETIME)) {
				previewString = new DateFormatter(ParameterUtil.isCustomCategory(formatCategroy) ? formatPattern
						: (formatCategroy.equals(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_UNFORMATTED)
								? DateFormatter.DATETIME_UNFORMATTED
								: formatCategroy),
						locale).format(new Date());
			} else if (type.equals(DesignChoiceConstants.PARAM_TYPE_DATE)) {
				previewString = new DateFormatter(ParameterUtil.isCustomCategory(formatCategroy) ? formatPattern
						: (formatCategroy.equals(DesignChoiceConstants.DATE_FORMAT_TYPE_UNFORMATTED)
								? DateFormatter.DATE_UNFORMATTED
								: formatCategroy),
						locale).format(new Date());
			} else if (type.equals(DesignChoiceConstants.PARAM_TYPE_TIME)) {
				previewString = new DateFormatter(ParameterUtil.isCustomCategory(formatCategroy) ? formatPattern
						: (formatCategroy.equals("Unformatted") ? DateFormatter.TIME_UNFORMATTED //$NON-NLS-1$
								: formatCategroy),
						locale).format(new Date());
			} else if (type.equals(DesignChoiceConstants.PARAM_TYPE_STRING)) {
				previewString = new StringFormatter(
						ParameterUtil.isCustomCategory(formatCategroy) ? formatPattern : formatCategroy, locale).format(
								defaultValue == null ? Messages.getString("FormatStringPage.default.preview.text") //$NON-NLS-1$
										: defaultValue);
			} else if (type.equals(DesignChoiceConstants.PARAM_TYPE_INTEGER)) {
				int intValue = DEFAULT_PREVIEW_INTEGER;

				if (defaultValue != null) {
					try {
						intValue = java.lang.Integer.parseInt(defaultValue);
					} catch (NumberFormatException e) {
					}
				}
				previewString = new NumberFormatter(
						(ParameterUtil.isCustomCategory(formatCategroy) || (isNumberFormat(formatCategroy)))
								? formatPattern
								: formatCategroy,
						locale).format(intValue);
			} else if (type.equals(DesignChoiceConstants.PARAM_TYPE_DECIMAL)
					|| type.equals(DesignChoiceConstants.PARAM_TYPE_FLOAT)) {
				double doulbeValue = DEFAULT_PREVIEW_NUMBER;

				if (defaultValue != null) {
					try {
						doulbeValue = Double.parseDouble(defaultValue);
					} catch (NumberFormatException e) {
					}
				}

				String realformatPattern = (ParameterUtil.isCustomCategory(formatCategroy)
						|| (isNumberFormat(formatCategroy))) ? formatPattern : formatCategroy;
				NumberFormatter tempFormater = new NumberFormatter(realformatPattern, locale);
				previewString = tempFormater.format(doulbeValue);
				if (Double.isInfinite(doulbeValue)) {
					BigDecimal tempDecimal = new BigDecimal(defaultValue);

					if (realformatPattern == null) {
						previewString = tempDecimal.toString();
					} else {
						previewString = tempFormater.format(tempDecimal);
					}

				}
			} else {
				previewString = new NumberFormatter(
						ParameterUtil.isCustomCategory(formatCategroy) ? formatPattern : formatCategroy, locale)
								.format(DEFAULT_PREVIEW_NUMBER);
			}
		}
		// }
		formatField.setText(displayFormat);
		previewLabel.setText(convertNullString(previewString));
		changeFormat.setEnabled(choiceSet != null);
	}

	private boolean isNumberFormat(String formatCatogory) {
		if (formatCatogory.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_UNFORMATTED)
				|| formatCatogory.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_GENERAL_NUMBER)
				|| formatCatogory.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY)
				|| formatCatogory.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_FIXED)
				|| formatCatogory.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_PERCENT)
				|| formatCatogory.equals(DesignChoiceConstants.NUMBER_FORMAT_TYPE_SCIENTIFIC)) {
			return true;
		}
		return false;
	}

	private String convertNullString(String str) {
		if (str == null) {
			return "";//$NON-NLS-1$
		}
		return str;

	}

	private boolean containValue(SelectionChoice selectedChoice, String newValue, String property) {
		for (Iterator iter = choiceList.iterator(); iter.hasNext();) {
			SelectionChoice choice = (SelectionChoice) iter.next();
			if (choice != selectedChoice) {
				String value = null;
				if (COLUMN_VALUE.equals(property)) {
					value = choice.getValue();
					if (isEqual(value, newValue)) {
						return true;
					}
				}
				if (COLUMN_DISPLAY_TEXT_KEY.equals(property)) {
					value = choice.getLabelResourceKey();
					if (value == null) {
						value = choice.getValue();
					}
					if (value == null) {
						value = LABEL_NULL;
					}
					if (value.equals(newValue)) {
						return true;
					}
				}
				if (COLUMN_DISPLAY_TEXT.equals(property)) {
					value = choice.getLabel();
					if (value == null) {
						value = choice.getValue();
					}
					if (value == null) {
						value = LABEL_NULL;
					}
					if (value.equals(newValue)) {
						return true;
					}
				}
			}
		}
		return false;
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
		String previewText = null;
		if (getFirstDefaultValue() != null)
			previewText = getFirstDefaultValue().getStringExpression();
		if (previewText != null)
			formatBuilder.setPreviewText(previewText);
		if (formatBuilder.open() == OK) {
			formatCategroy = (String) ((Object[]) formatBuilder.getResult())[0];
			formatPattern = (String) ((Object[]) formatBuilder.getResult())[1];
			formatLocale = (ULocale) ((Object[]) formatBuilder.getResult())[2];
			updateFormatField();
			if (refresh) {
				refreshStaticValueTable();
			}
		}
	}

	private boolean canBeBlank() {
		boolean canBeBlank = false;
		// if ( PARAM_CONTROL_LIST.equals( getSelectedControlType( ) )
		// || DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX.equals(
		// getSelectedControlType( ) ) )
		{
			if (dirtyProperties.containsKey(CHECKBOX_ISREQUIRED)) {
				canBeBlank = !(((Boolean) dirtyProperties.get(CHECKBOX_ISREQUIRED)).booleanValue());
			} else {
				canBeBlank = !(inputParameter.isRequired());
			}
		}
		return canBeBlank;
	}

	private boolean canBeNull() {
		boolean canBeNull = true;
		if (dirtyProperties.containsKey(CHECKBOX_ISREQUIRED)) {
			canBeNull = !(((Boolean) dirtyProperties.get(CHECKBOX_ISREQUIRED)).booleanValue());
		} else {
			canBeNull = !(inputParameter.isRequired());
		}
		return canBeNull;
	}

	private boolean isDefaultChoice(SelectionChoice choice) {
		String choiceValue = choice.getValue();
		// String defaultValue = convertToStandardFormat( this.defaultValue );
		if (canBeNull() && choiceValue == null && defaultValueList != null) {
			if (defaultValueList.contains(null))
				return true;
			else if (defaultValueList.contains(new Expression(null, ExpressionType.CONSTANT)))
				return true;
		}
		return choiceValue != null && defaultValueList != null
				&& defaultValueList.contains(new Expression(choiceValue, ExpressionType.CONSTANT));
	}

	private boolean isStatic() {
		return staticRadio.getSelection();
	}

	private String getExpression(String columnName) {
		if (columnName.equals(NONE_DISPLAY_TEXT)) {
			return null;
		}
		for (Iterator iter = columnList.iterator(); iter.hasNext();) {
			ResultSetColumnHandle cachedColumn = (ResultSetColumnHandle) iter.next();
			if (cachedColumn.getColumnName().equals(columnName)) {
				return DEUtil.getExpression(cachedColumn);
			}
		}
		// return null;
		return columnName;
	}

	private String getExpression(Control control, String columnName) {
		if (columnName.equals(NONE_DISPLAY_TEXT)) {
			return null;
		}
		if (columnList == null)
			return null;
		for (Iterator iter = columnList.iterator(); iter.hasNext();) {
			ResultSetColumnHandle cachedColumn = (ResultSetColumnHandle) iter.next();
			if (cachedColumn.getColumnName().equals(columnName)) {
				IExpressionConverter converter = ExpressionButtonUtil.getCurrentExpressionConverter(control);
				return ExpressionUtility.getExpression(cachedColumn, converter);
			}
		}
		// return null;
		return columnName;
	}

	private String getColumnName(String expression) {
		for (Iterator iter = columnList.iterator(); iter.hasNext();) {
			ResultSetColumnHandle cachedColumn = (ResultSetColumnHandle) iter.next();
			if (DEUtil.getExpression(cachedColumn).equals(expression)) {
				return cachedColumn.getColumnName();
			}
		}
		// return null;
		return expression;
	}

	private String getColumnName(Control control, String expression) {
		IExpressionConverter converter = ExpressionButtonUtil.getCurrentExpressionConverter(control);
		for (Iterator iter = columnList.iterator(); iter.hasNext();) {
			ResultSetColumnHandle cachedColumn = (ResultSetColumnHandle) iter.next();
			if (ExpressionUtility.getExpression(cachedColumn, converter).equals(expression)) {
				return cachedColumn.getColumnName();
			}
		}
		// return null;
		return expression;
	}

	private String validateChoice(SelectionChoice choice, String displayLabelKey, String displayLabel, String value) {
		String errorMessage = isValidValue(value);
		if (errorMessage != null) {
			return errorMessage;
		}
		// String newValue = convertToStandardFormat( value );
		if (distinct.isEnabled() && distinct.getSelection()) {
			if (containValue(choice, displayLabelKey, COLUMN_DISPLAY_TEXT_KEY)) {
				return ERROR_MSG_DUPLICATED_LABELKEY;
			} else
				return null;
		}
		if (containValue(choice, value, COLUMN_VALUE)) {
			return ERROR_MSG_DUPLICATED_VALUE;
		}
		if ((displayLabel == null && containValue(choice, value, COLUMN_DISPLAY_TEXT))
				|| (containValue(choice, displayLabel, COLUMN_DISPLAY_TEXT))) {
			return ERROR_MSG_DUPLICATED_LABEL;
		}
		if (containValue(choice, displayLabelKey, COLUMN_DISPLAY_TEXT_KEY)) {
			return ERROR_MSG_DUPLICATED_LABELKEY;
		}
		return null;
	}

	private void setExpression(Combo chooser, String key) {
		chooser.deselectAll();
		key = StringUtil.trimString(key);
		if (StringUtil.isBlank(key)) {
			chooser.setText(""); //$NON-NLS-1$
			return;
		}

		IExpressionConverter converter = ExpressionButtonUtil.getCurrentExpressionConverter(chooser);
		for (int i = 0; i < columnList.size(); i++) {
			if (key.equals(ExpressionUtility.getExpression(columnList.get(i), converter))) {
				// chooser.select( i );
				chooser.setText(((ResultSetColumnHandle) columnList.get(i)).getColumnName());
				return;
			}
		}
		chooser.setText(key);
	}

	// public String getSelectedExprValue( String value )
	// {
	// String exprValue = null;
	//
	// if ( value == null || columnChooser == null )
	// {
	// return "null"; //$NON-NLS-1$
	// }
	// else
	// {
	// String dataType = getSelectedDataType( );
	// if ( dataType == null )
	// return "null"; //$NON-NLS-1$
	// if ( DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN.equals( dataType )
	// || DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER.equals( dataType )
	// || DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT.equals( dataType ) )
	// {
	// exprValue = value;
	// }
	// else if ( DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL.equals( dataType
	// ) )
	// {
	// exprValue = "new java.math.BigDecimal(\"" + value + "\")"; //$NON-NLS-1$
	// //$NON-NLS-2$
	// }
	// else
	// {
	// exprValue = "\"" //$NON-NLS-1$
	// + JavascriptEvalUtil.transformToJsConstants( value )
	// + "\""; //$NON-NLS-1$
	// }
	// }
	//
	// return exprValue;
	// }

	private void handleControlTypeSelectionEvent() {
		controlTypeHelper.update(false);

		String type = getSelectedControlType();
		if (DesignChoiceConstants.PARAM_CONTROL_CHECK_BOX.equals(type)
				|| DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX.equals(type)
				|| DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON.equals(type)) {
			staticRadio.setSelection(true);
			dynamicRadio.setSelection(false);
			staticRadio.setEnabled(false);
			dynamicRadio.setEnabled(false);
			switchParamterType();
		} else if (PARAM_CONTROL_LIST.equals(type)) {
			if (DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN.equals(getSelectedDataType())) {
				staticRadio.setSelection(false);
				dynamicRadio.setSelection(true);
				staticRadio.setEnabled(false);
				dynamicRadio.setEnabled(false);
				switchParamterType();
			}
		}

		changeControlType();

		Point size = displayArea.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		displayArea.setSize(size);
		displayArea.getParent().layout();
	}

	private void initDefaultValueViewer() {
		if (defaultValueViewer != null && !defaultValueViewer.getTable().isDisposed()) {
			if (!isStatic() && (enableAllowMultiValueVisible())) {
				WidgetUtil.setExcludeGridData(defaultValueViewer.getTable(), !allowMultiChoice.getSelection());
				WidgetUtil.setExcludeGridData(rightPart, !allowMultiChoice.getSelection());
				WidgetUtil.setExcludeGridData(addButton, !allowMultiChoice.getSelection());
				if (allowMultiChoice.getSelection()) {
					defaultValueViewer.setInput(defaultValueList);
					updateDynamicTableButtons();
				}
			} else {
				WidgetUtil.setExcludeGridData(defaultValueViewer.getTable(), true);
				WidgetUtil.setExcludeGridData(rightPart, true);
				WidgetUtil.setExcludeGridData(addButton, true);
			}

			addButton.getParent().layout();
			defaultValueViewer.getTable().getParent().layout();
			valueArea.layout();

			Point size = displayArea.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			displayArea.setSize(size);
			displayArea.getParent().layout();
		}
	}

	private boolean enableAllowMultiValueVisible() {
		return PARAM_CONTROL_LIST.endsWith(getSelectedControlType()) && allowMultiValueVisible;
	}

	private void addDynamicDefaultValue() {
		if (!isStatic() && enableAllowMultiValueVisible() && allowMultiChoice.getSelection()) {
			String type = (String) defaultValueChooser.getData(ExpressionButtonUtil.EXPR_TYPE);
			String value = UIUtil.convertToModelString(defaultValueChooser.getText(), false);

			if (value.equals(EMPTY_VALUE))
				value = ""; //$NON-NLS-1$
			else if (value.equals(NULL_VALUE))
				value = null;

			setFirstDefaultValue(value, type);
			refreshDynamicValueTable();
			defaultValueChooser.setFocus();
			defaultValueChooser.setText("");
		}
	}

	private boolean canUseEmptyValue() {
		return canBeNull() && DesignChoiceConstants.COLUMN_DATA_TYPE_STRING.equals(getSelectedDataType());
	}

	private void handleDefaultValueModifyEvent() {
		if (defaultValueChooser == null || defaultValueChooser.isDisposed())
			return;

		if (!isStatic() && enableAllowMultiValueVisible() && allowMultiChoice.getSelection()) {
			updateDynamicTableButtons();
			return;
		}
		String value = defaultValueChooser.getText();
		String type = (String) defaultValueChooser.getData(ExpressionButtonUtil.EXPR_TYPE);

		if (!isStatic()) {
			if (value.equals(EMPTY_VALUE))
				value = ""; //$NON-NLS-1$
			else if (value.equals(NULL_VALUE) || value.equals(""))
				value = null;
		}

		// if ( value.equals( CHOICE_NULL_VALUE )
		// || value.equals( CHOICE_BLANK_VALUE ) )
		// return;
		if (defaultValueList != null)
			defaultValueList.clear();
		if ("".equals(value) && canUseEmptyValue()) {
			setFirstDefaultValue(value, type);
		} else {
			String modelValue = UIUtil.convertToModelString(value, false);
			if (modelValue != null) {
				setFirstDefaultValue(modelValue, type);
			} else {
				updateMessageLine();
				updateFormatField();
			}
		}
		if (isStatic()) {
			refreshStaticValueTable();
		}
	}

	private class ParameterDataSetExpressionProvider extends ExpressionProvider {
		public ParameterDataSetExpressionProvider(DesignElementHandle handle) {
			super(handle);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider#
		 * getCategoryList()
		 */
		protected List getCategoryList() {
			ArrayList<Object> categoryList = (ArrayList<Object>) super.getCategoryList();
			if (categoryList != null && !categoryList.contains(DATASETS)) {
				if (elementHandle.getModuleHandle() instanceof ReportDesignHandle
						&& ((ReportDesignHandle) elementHandle.getModuleHandle()).getDataSets() != null) {
					if (categoryList.contains(OPERATORS)) {
						categoryList.add(categoryList.indexOf(OPERATORS) + 1, DATASETS);
					} else {
						categoryList.add(DATASETS);
					}

				}
			}
			return categoryList;
		}
	}

	private static class ParameterVariableFilter extends ExpressionFilter {

		@Override
		public boolean select(Object parentElement, Object element) {
			if (parentElement != null && element != null && parentElement instanceof String
					&& element instanceof String) {
				if (ExpressionFilter.CATEGORY.equals(parentElement) && ExpressionProvider.VARIABLES.equals(element)) {
					return false;
				}
			}
			return true;
		}

	}
}
