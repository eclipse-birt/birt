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

package org.eclipse.birt.report.designer.ui.dialogs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.ExpressionButton;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.DefaultProjectFileServiceHelper;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IProjectFileServiceHelper;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IProjectFileServiceHelperProvider;
import org.eclipse.birt.report.designer.internal.ui.dialogs.parameters.IHyperlinkParameter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.parameters.IHyperlinkParameterProvider;
import org.eclipse.birt.report.designer.internal.ui.dialogs.parameters.IReportHyperlinkParameter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.parameters.ReportHyperlinkParameter;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.CLabel;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.ui.widget.ComboBoxCellEditor;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.engine.api.EmitterInfo;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.model.api.AbstractScalarParameterHandle;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * The builder for hyper link
 */

public class HyperlinkBuilder extends BaseDialog {

	private static final String TITLE = Messages.getString("HyperlinkBuilder.DialogTitle"); //$NON-NLS-1$
	private static final String LABEL_SELECT_TYPE = Messages.getString("HyperlinkBuilder.Label.SelectType"); //$NON-NLS-1$
	private static final String LABEL_LOCATION = Messages.getString("HyperlinkBuilder.Label.Location"); //$NON-NLS-1$
	private static final String LABEL_TARGET = Messages.getString("HyperlinkBuilder.Label.Target"); //$NON-NLS-1$
	private static final String LABEL_TOOLTIP = Messages.getString("HyperlinkBuilder.Label.Tooltip"); //$NON-NLS-1$
	private static final String LABEL_BOOKMARK = Messages.getString("HyperlinkBuilder.Label.Bookmark"); //$NON-NLS-1$
	private static final String LABEL_LINKED_EXPRESSION = Messages.getString("HyperlinkBuilder.Label.LinkedExpression"); //$NON-NLS-1$
	private static final String LABEL_REPORT_PARAMETER = Messages.getString("HyperlinkBuilder.Label.Parameters"); //$NON-NLS-1$
	private static final String RADIO_NONE = Messages.getString("HyperlinkBuilder.Radio.None"); //$NON-NLS-1$
	private static final String RADIO_URI = Messages.getString("HyperlinkBuilder.Radio.Uri"); //$NON-NLS-1$
	private static final String RADIO_BOOKMARK = Messages.getString("HyperlinkBuilder.Radio.Bookmark"); //$NON-NLS-1$
	private static final String RADIO_DRILLTHROUGH = Messages.getString("HyperlinkBuilder.Radio.DrillThrough"); //$NON-NLS-1$

	private static final String COLUMN_PARAMETER = Messages.getString("HyperlinkBuilder.Column.Parameters"); //$NON-NLS-1$
	private static final String COLUMN_VALUE = Messages.getString("HyperlinkBuilder.Column.Values"); //$NON-NLS-1$
	private static final String COLUMN_REQUIRED = Messages.getString("HyperlinkBuilder.ParameterRequired"); //$NON-NLS-1$
	private static final String COLUMN_DATA_TYPE = Messages.getString("HyperlinkBuilder.Column.DataType"); //$NON-NLS-1$

	private static final Image IMAGE_OPEN_FILE = ReportPlatformUIImages
			.getImage(IReportGraphicConstants.ICON_OPEN_FILE);

	private static final Image REQUIRED_ICON = ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_DEFAULT);
	private static final Image ERROR_ICON = ReportPlatformUIImages.getImage(ISharedImages.IMG_OBJS_ERROR_TSK);

	private static final String ERROR_MSG_REPORT_REQUIRED = Messages
			.getString("HyperlinkBuilder.ErrorMessage.ReportReqired"); //$NON-NLS-1$
	private static final String ERROR_MSG_INVALID_REPORT = Messages
			.getString("HyperlinkBuilder.ErrorMessage.InvalidReport"); //$NON-NLS-1$

	private static final String TOOLTIP_BROWSE_FILE = Messages.getString("HyperlinkBuilder.BrowseForFile"); //$NON-NLS-1$
	// private static final String TOOLTIP_EXPRESSION = Messages.getString(
	// "HyperlinkBuilder.OpenExpression" ); //$NON-NLS-1$
	private static final String[] STEPS = { "", //$NON-NLS-1$
			Messages.getString("HyperlinkBuilder.Step.1"), //$NON-NLS-1$
			Messages.getString("HyperlinkBuilder.Step.2"), //$NON-NLS-1$
			Messages.getString("HyperlinkBuilder.Step.3"), //$NON-NLS-1$
			Messages.getString("HyperlinkBuilder.Step.4"), //$NON-NLS-1$
			Messages.getString("HyperlinkBuilder.Step.5"), //$NON-NLS-1$
			Messages.getString("HyperlinkBuilder.Step.6") //$NON-NLS-1$
	};

	private static final String REQUIED_MARK = "*"; //$NON-NLS-1$

	private static final IChoiceSet CHOICESET_TARGET = DEUtil.getMetaDataDictionary()
			.getChoiceSet(DesignChoiceConstants.CHOICE_TARGET_NAMES_TYPE);

	// private static final ParamBinding dummyParameterBinding =
	// StructureFactory.createParamBinding( );

	private ActionHandle inputHandle;

	private Composite displayArea;

	private String selectedType;
	// Radios
	private Button noneRadio, uriRadio, bookmarkRadio, drillRadio;

	private Combo bookmarkChooser, targetChooser;

	private Text bookmarkEditor;

	/**
	 * Target report design location.
	 */
	private Text locationEditor;

	private CLabel messageLine;

	private Label toolTip;
	private TableViewer paramBindingTable;

	private ComboBoxCellEditor parameterChooser;

	private ArrayList<ParamBinding> paramBindingList = new ArrayList<>();

	private List<String> typeFilterList = new ArrayList<>(2);
	private boolean bTargetEnabled = true;
	private boolean bTooltipEnabled = true;

	private ExpressionProvider provider;

	private Object targetReportHandle;

	private ArrayList<IHyperlinkParameter> paramList = new ArrayList<>();
	private HashMap<String, String> paramTypes = new HashMap<>();

	private IStructuredContentProvider contentProvider = new IStructuredContentProvider() {

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			ArrayList list = ((ArrayList) inputElement);
			ArrayList elementsList = (ArrayList) list.clone();
			// if ( bindingList.size( ) != parameterList.size( ) )
			// {
			// To check if all parameters have been bound.
			// elementsList.add( dummyParameterBinding );
			// }
			return elementsList.toArray();
		}
	};

	private ITableLabelProvider labelProvider = new ITableLabelProvider() {

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			if (columnIndex == 1) {
				ParamBinding parameterBinding = ((ParamBinding) element);
				boolean isRequired = isParameterRequired(parameterBinding.getParamName());
				if (isRequired) {
					return REQUIRED_ICON;
				}
			}
			// if(columnIndex == 0){
			// String text = ( (ParamBinding) element ).getParamName( );
			// Object handle = getParameter(text);
			// if(handle!=null){
			// INodeProvider provider = ProviderFactory.createProvider( handle
			// );
			// if(provider!=null){
			// return provider.getNodeIcon( handle );
			// }
			// }
			// }
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			String text = null;
			ParamBinding parameterBinding = ((ParamBinding) element);
			// if ( parameterBinding != dummyParameterBinding )
			// {
			if (columnIndex == 0) {
				text = parameterBinding.getParamName();

			} else if (columnIndex == 2) {
				String name = parameterBinding.getParamName();

				Object object = getParameter(name);
				if (object instanceof AbstractScalarParameterHandle) {
					return getDisplayDataType(((AbstractScalarParameterHandle) object).getDataType());
				}

				return ""; //$NON-NLS-1$
			} else if (columnIndex == 3) {
				text = parameterBinding.getExpression();
			}
			// }
			if (text == null) {
				text = ""; //$NON-NLS-1$
			}
			return text;
		}

		@Override
		public void addListener(ILabelProviderListener listener) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
		}

	};

	// private ICellModifier cellModifier = new ICellModifier( ) {
	//
	// public boolean canModify( Object element, String property )
	// {
	// if ( element == dummyParameterBinding
	// && COLUMN_VALUE.equals( property ) )
	// {
	// return false;
	// }
	// return true;
	// }
	//
	// public Object getValue( Object element, String property )
	// {
	// ParamBinding paramBinding = ( (ParamBinding) element );
	// Object value = null;
	// if ( COLUMN_VALUE.equals( property ) )
	// {
	// Expression expression = getParamBindingExpression( paramBinding );
	// if ( paramCellEditorFacotryAdapter != null )
	// {
	// String fileName = getTargetReportFile( );
	// return new Object[]{
	// fileName,
	// getParameter( paramBinding.getParamName( ) ),
	// expression
	// };
	// }
	// else
	// return expression == null ? "" : expression;
	// }
	// else if ( COLUMN_PARAMETER.equals( property ) )
	// {
	// value = paramBinding.getParamName( );
	// if ( value == null )
	// {
	// value = ""; //$NON-NLS-1$
	// }
	//
	// buildParameterChoices( (String) value );
	// // int index = -1;
	// // for ( int i = 0; i < parameterChooser.getItems( ).length; i++
	// // )
	// // {
	// // if ( parameterChooser.getItems( )[i].equals( paramBinding.
	// // getParamName( ) ) )
	// // {
	// // index = i;
	// // break;
	// // }
	// // }
	//
	// }
	// return value;
	// }
	//
	// public void modify( Object element, String property, Object value )
	// {
	// if ( element instanceof Item )
	// {
	// element = ( (Item) element ).getData( );
	// }
	// ParamBinding paramBinding = ( (ParamBinding) element );
	// if ( COLUMN_VALUE.equals( property ) )
	// {
	// List<Expression> expressions = new ArrayList<Expression>( );
	// expressions.add( (Expression) value );
	// paramBinding.setExpression( expressions );
	// }
	// else if ( COLUMN_PARAMETER.equals( property ) )
	// {
	// // int index = -1;
	// // for ( int i = 0; i < parameterChooser.getItems( ).length; i++
	// // )
	// // {
	// // if ( parameterChooser.getItems( )[i].equals( value ) )
	// // {
	// // index = i;
	// // break;
	// // }
	// // }
	//
	// if ( ( value != null ) && ( (String) value ).length( ) > 0 )
	// {
	// if ( paramBinding == dummyParameterBinding )
	// {
	// paramBinding = StructureFactory.createParamBinding( );
	// paramBindingList.add( paramBinding );
	// }
	// paramBinding.setParamName( (String) value );
	// }
	// }
	// paramBindingTable.refresh( );
	// validateTables( );
	// }
	// };

	/**
	 * Drillthrough, target report design.
	 */
	private Button reportDesignButton;

	/**
	 * Drillthrough, target report document.
	 */
	private Button reportDocumentButton;

	/**
	 * Drillthrough, target report document location.
	 */
	private Text documentEditor;

	/**
	 * Drillthrough, targetBookmark.
	 */
	private Button targetBookmarkButton;

	/**
	 * Drillthrough, toc entity.
	 */
	private Button tocButton;

	private Button sameFrameButton;
	private Button newWindowButton;
	private Button wholePageButton;
	private Button parentFrameButton;

	private String[] supportedFormats;

	private Combo anchorChooser, targetFormatsChooser;
	private Button checkButton;
	private Group targetGroup;

	private boolean showBookMark = true;

	private boolean isIDE = false;
	private boolean isRelativeToProjectRoot = false;
	private Text tooltipText;
	private ScrolledComposite scrollContent;
	private Composite paramButtonContainer;
	private Button addParamButton;
	private Button editParamButton;
	private Button removeParamButton;
	private Button removeAllParamButton;

	private IProjectFileServiceHelper projectFileServiceHelper;

	public HyperlinkBuilder(Shell parentShell) {
		this(parentShell, false);
	}

	protected Object getParameter(String paramName) {
		if (paramList != null) {
			for (int i = 0; i < paramList.size(); i++) {
				IHyperlinkParameter parameter = paramList.get(i);
				if (parameter.getName().equals(paramName) && parameter instanceof ReportHyperlinkParameter) {
					return ((ReportHyperlinkParameter) parameter).getParameterHandle();
				}
			}
		}
		return null;
	}

	public HyperlinkBuilder() {
		this(UIUtil.getDefaultShell(), false);
	}

	public HyperlinkBuilder(Shell parentShell, boolean isIDE) {
		this(parentShell, isIDE, false);
	}

	public HyperlinkBuilder(Shell parentShell, boolean isIDE, boolean isRelativeToProjectRoot) {
		super(parentShell, TITLE);
		this.isIDE = isIDE;
		this.isRelativeToProjectRoot = isRelativeToProjectRoot;

		// *********** try using a helper provider ****************
		IProjectFileServiceHelperProvider helperProvider = (IProjectFileServiceHelperProvider) ElementAdapterManager
				.getAdapter(this, IProjectFileServiceHelperProvider.class);

		if (helperProvider != null) {
			projectFileServiceHelper = helperProvider.createHelper();
		} else {
			projectFileServiceHelper = new DefaultProjectFileServiceHelper();
		}
	}

	public HyperlinkBuilder(boolean isIDE) {
		this(UIUtil.getDefaultShell(), isIDE);
	}

	public void showBookMark(boolean showBookMark) {
		this.showBookMark = showBookMark;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		createSelectionArea(composite);
		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		scrollContent = new ScrolledComposite(composite, SWT.H_SCROLL | SWT.V_SCROLL);
		scrollContent.setLayoutData(new GridData(GridData.FILL_BOTH));
		scrollContent.setExpandHorizontal(true);
		scrollContent.setExpandVertical(true);

		scrollContent.addControlListener(new ControlAdapter() {

			@Override
			public void controlResized(ControlEvent e) {
				computeSize();
			}
		});

		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();

		int height = shell.getBounds().height < 510 + 200 ? shell.getBounds().height - 200 : 510;
		if (!bTargetEnabled) {
			height -= 70;
		}
		if (!bTooltipEnabled) {
			height -= 50;
		}

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.minimumWidth = 600;
		gd.minimumHeight = height;
		scrollContent.setLayoutData(gd);

		displayArea = new Composite(scrollContent, SWT.NONE);
		displayArea.setLayout(new GridLayout(3, false));
		displayArea.setLayoutData(new GridData(GridData.FILL_BOTH));

		scrollContent.setContent(displayArea);

		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		UIUtil.bindHelp(parent, IHelpContextIds.HYPERLINK_BUILDER_ID);

		return composite;
	}

	protected void computeSize() {
		Point size = displayArea.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		scrollContent.setMinSize(size.x, size.y + 10);
		scrollContent.layout();
	}

	private void createSelectionArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite.setLayout(new GridLayout(2, false));

		new Label(composite, SWT.NONE).setText(LABEL_SELECT_TYPE);

		if (!typeFilterList.contains(DesignChoiceConstants.ACTION_LINK_TYPE_NONE)) {
			noneRadio = new Button(composite, SWT.RADIO);
			noneRadio.setText(RADIO_NONE);
			addRadioListener(noneRadio, DesignChoiceConstants.ACTION_LINK_TYPE_NONE);
		}

		if (!typeFilterList.contains(DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK)) {
			UIUtil.createBlankLabel(composite);

			uriRadio = new Button(composite, SWT.RADIO);
			uriRadio.setText(RADIO_URI);
			addRadioListener(uriRadio, DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK);
		}

		if (!(SessionHandleAdapter.getInstance().getReportDesignHandle() instanceof LibraryHandle)
				&& !typeFilterList.contains(DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK)) {

			if (showBookMark) {
				UIUtil.createBlankLabel(composite);

				bookmarkRadio = new Button(composite, SWT.RADIO);
				bookmarkRadio.setText(RADIO_BOOKMARK);
				addRadioListener(bookmarkRadio, DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK);
			}
		}

		if (!typeFilterList.contains(DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH)) {
			UIUtil.createBlankLabel(composite);

			drillRadio = new Button(composite, SWT.RADIO);
			drillRadio.setText(RADIO_DRILLTHROUGH);
			addRadioListener(drillRadio, DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH);
		}

	}

	/**
	 * Adds hyperlink type filter to disable one type
	 *
	 * @param disabledType hyperlink type string
	 * @see DesignChoiceConstants#ACTION_LINK_TYPE_NONE
	 * @see DesignChoiceConstants#ACTION_LINK_TYPE_HYPERLINK
	 * @see DesignChoiceConstants#ACTION_LINK_TYPE_BOOKMARK_LINK
	 * @see DesignChoiceConstants#ACTION_LINK_TYPE_DRILL_THROUGH
	 */
	public void addHyperlinkTypeFilter(String disabledType) {
		typeFilterList.add(disabledType);
	}

	public void setTargetEnabled(boolean bEnabled) {
		this.bTargetEnabled = bEnabled;
	}

	public void setTooltipEnabled(boolean bEnabled) {
		this.bTooltipEnabled = bEnabled;
	}

	private void switchTo(String type) {
		selectedType = type;
		clearArea();
		closeTargetReport();

		displayArea.setLayout(new GridLayout(3, false));

		if (DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals(type)) {
			switchToURI();
		} else if (DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals(type) && showBookMark) {
			switchToBookmark();
		} else if (DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equals(type)) {
			switchToDrillthrough();
		}

		initDisplayArea();
		displayArea.layout();
		computeSize();
	}

	private void switchToURI() {
		new Label(displayArea, SWT.NONE);
		new Label(displayArea, SWT.NONE).setText(Messages.getString("HyperlinkBuilder.Label.Location.hint")); //$NON-NLS-1$
		new Label(displayArea, SWT.NONE);

		new Label(displayArea, SWT.NONE).setText(REQUIED_MARK + LABEL_LOCATION);
		locationEditor = new Text(displayArea, SWT.BORDER | SWT.MULTI);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = locationEditor.computeSize(SWT.DEFAULT, SWT.DEFAULT).y - locationEditor.getBorderWidth() * 2;
		locationEditor.setLayoutData(gd);
		locationEditor.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				updateButtons();
			}
		});

		locationEditor.addTraverseListener(new TraverseListener() {

			@Override
			public void keyTraversed(TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
					e.doit = true;
				}
			}
		});

		Composite buttonArea = new Composite(displayArea, SWT.NONE);
		buttonArea.setLayout(UIUtil.createGridLayoutWithoutMargin(2, false));
		buttonArea.setLayoutData(new GridData());
		createBrowerButton(buttonArea, locationEditor, true, false);
		createComplexExpressionButton(buttonArea, locationEditor);
		createTargetBar();
		createTooltipBar();
		locationEditor.forceFocus();
	}

	private void createTooltipBar() {
		if (bTooltipEnabled) {
			new Label(displayArea, SWT.NONE).setText(LABEL_TOOLTIP);
			tooltipText = new Text(displayArea, SWT.BORDER);
			tooltipText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			UIUtil.createBlankLabel(displayArea);
		}
	}

	private void switchToBookmark() {
		createBookmarkBar(true);
		createTooltipBar();
	}

	private void switchToDrillthrough() {
		// new Label( displayArea, SWT.NONE ).setText( REQUIED_MARK +
		// LABEL_REPORT );
		// locationEditor = new Text( displayArea, SWT.BORDER | SWT.SINGLE );
		// locationEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
		// ) );
		// locationEditor.addModifyListener( new ModifyListener( ) {
		//
		// public void modifyText( ModifyEvent e )
		// {
		// closeReport( );
		// initParamterBindings( );
		// initBookmarkList( reportHandle );
		// updateButtons( );
		// }
		//
		// } );
		// createBrowerButton( displayArea, locationEditor, false, true );
		//
		// UIUtil.createBlankLabel( displayArea );
		// messageLine = new CLabel( displayArea, SWT.NONE );
		// GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		// gd.horizontalSpan = 2;
		// messageLine.setLayoutData( gd );
		//
		// createBindingTable( displayArea );
		// createBookmarkBar( false );
		// createTargetBar( );
		// createFormatBar( );

		displayArea.setLayout(new GridLayout());
		// final ScrolledComposite scrolledContainer = new ScrolledComposite(
		// displayArea,
		// SWT.NONE );

		final Composite container = new Composite(displayArea, SWT.NONE);
		container.setLayout(new GridLayout());
		// scrolledContainer.setContent( container );
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		messageLine = new CLabel(container, SWT.NONE);
		messageLine.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createDrillthroughSelectTargetReport(container);
		createDrillthroughSelectTargetAnchor(container);
		createDrillthroughCreateLinkExpression(container);
		if (bTargetEnabled) {
			createDrillthroughSelectShowTarget(container);
		}
		createDrillthroughSelectFormat(container);
		if (bTooltipEnabled) {
			createDrillthroughTooltip(container);
		}

		container.setSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	private void createDrillthroughTooltip(Composite container) {
		Group formatsGroup = new Group(container, SWT.NONE);
		formatsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		formatsGroup.setText(STEPS[6] + Messages.getString("HyperlinkBuilder.DrillThrough.Tooltip")); //$NON-NLS-1$
		formatsGroup.setLayout(new GridLayout(2, false));
		toolTip = new Label(formatsGroup, SWT.NONE);
		toolTip.setText(LABEL_TOOLTIP);
		tooltipText = new Text(formatsGroup, SWT.BORDER);
		tooltipText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	private void createDrillthroughSelectTargetReport(Composite container) {
		targetGroup = new Group(container, SWT.NONE);
		targetGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		targetGroup.setText(STEPS[1] + Messages.getString("HyperlinkBuilder.DrillThrough.SelectTargetReport")); //$NON-NLS-1$
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		targetGroup.setLayout(layout);

		reportDesignButton = new Button(targetGroup, SWT.RADIO);
		reportDesignButton.setText(Messages.getString("HyperlinkBuilder.ReportDesignButton")); //$NON-NLS-1$
		reportDesignButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				initTargetReport(locationEditor.getText());
				selectRadio(targetGroup, reportDesignButton);
				initParamterBindings(false);
				deSelectAnchor();
				updateButtons();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		locationEditor = new Text(targetGroup, SWT.BORDER | SWT.SINGLE);
		locationEditor.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				String target = locationEditor.getText();
				handleTargetReportChange(target);
			}

		});
		locationEditor.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createBrowerButton(targetGroup, locationEditor, false, true);

		reportDocumentButton = new Button(targetGroup, SWT.RADIO);
		reportDocumentButton.setText(Messages.getString("HyperlinkBuilder.ReportDocumentButton")); //$NON-NLS-1$

		reportDocumentButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				initTargetReport(documentEditor.getText());
				selectRadio(targetGroup, reportDocumentButton);
				initParamterBindings(false);
				deSelectAnchor();
				updateButtons();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		documentEditor = new Text(targetGroup, SWT.BORDER | SWT.SINGLE);
		documentEditor.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				String target = documentEditor.getText();
				handleTargetReportChange(target);
			}

		});
		documentEditor.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		createBrowerButton(targetGroup, documentEditor, false, true, new String[] { "*.rptdocument" }); //$NON-NLS-1$

		createBindingTable(targetGroup);

	}

	private void createDrillthroughSelectTargetAnchor(Composite container) {
		final Group group = new Group(container, SWT.NONE);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setText(STEPS[2] + Messages.getString("HyperlinkBuilder.DrillThrough.SelectTargetAnchor")); //$NON-NLS-1$
		group.setLayout(new GridLayout());

		targetBookmarkButton = new Button(group, SWT.RADIO);
		targetBookmarkButton.setText(Messages.getString("HyperlinkBuilder.DrillThroughTargetBookmark")); //$NON-NLS-1$
		targetBookmarkButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				initAnchorChooser(targetReportHandle, false);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		tocButton = new Button(group, SWT.RADIO);
		tocButton.setText(Messages.getString("HyperlinkBuilder.DrillThroughTargetToc")); //$NON-NLS-1$

		tocButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				initAnchorChooser(targetReportHandle, true);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});

		anchorChooser = new Combo(group, SWT.BORDER | SWT.READ_ONLY);
		anchorChooser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		anchorChooser.setVisibleItemCount(30);
		anchorChooser.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (anchorChooser.getData() instanceof List) {
					List value = (List) anchorChooser.getData();
					bookmarkEditor.setText((String) value.get(anchorChooser.getSelectionIndex()));
				} else {
					bookmarkEditor.setText(anchorChooser.getText());
				}
				updateButtons();
			}

		});
	}

	private void createDrillthroughCreateLinkExpression(Composite container) {
		Group group = new Group(container, SWT.NONE);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setText(STEPS[3] + Messages.getString("HyperlinkBuilder.DrillThrough.CreateLinkExpr")); //$NON-NLS-1$
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		group.setLayout(layout);
		new Label(group, SWT.NONE).setText(Messages.getString("HyperlinkBuilder.DrillThroughLinkExpression")); //$NON-NLS-1$

		bookmarkEditor = new Text(group, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = bookmarkEditor.computeSize(SWT.DEFAULT, SWT.DEFAULT).y - bookmarkEditor.getBorderWidth() * 2;
		bookmarkEditor.setLayoutData(gd);
		createExpressionButton(group, bookmarkEditor);
	}

	private void createDrillthroughSelectShowTarget(Composite container) {
		Group group = new Group(container, SWT.NONE);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setText(STEPS[4] + Messages.getString("HyperlinkBuilder.DrillThrough.ShowTargetReport")); //$NON-NLS-1$
		group.setLayout(new GridLayout(2, false));

		newWindowButton = new Button(group, SWT.RADIO);
		newWindowButton.setText(Messages.getString("HyperlinkBuilder.DrillThroughNewWindow")); //$NON-NLS-1$

		parentFrameButton = new Button(group, SWT.RADIO);
		parentFrameButton.setText(Messages.getString("HyperlinkBuilder.DrillThroughParentFrame")); //$NON-NLS-1$

		sameFrameButton = new Button(group, SWT.RADIO);
		sameFrameButton.setText(Messages.getString("HyperlinkBuilder.DrillThroughSameFrame")); //$NON-NLS-1$

		wholePageButton = new Button(group, SWT.RADIO);
		wholePageButton.setText(Messages.getString("HyperlinkBuilder.DrillThroughWholePage")); //$NON-NLS-1$

	}

	private void createDrillthroughSelectFormat(Composite container) {
		ReportEngine engine = new ReportEngine(new EngineConfig());
		supportedFormats = new String[0];
		EmitterInfo[] emitters = engine.getEmitterInfo();
		if (emitters != null && emitters.length > 0) {
			List<String> temp = new ArrayList<>();
			for (int i = 0; i < emitters.length; i++) {
				EmitterInfo info = emitters[i];
				if (!info.isHidden()) {
					temp.add(info.getFormat());
				}
			}
			supportedFormats = temp.toArray(new String[temp.size()]);
		}

		Group formatsGroup = new Group(container, SWT.NONE);
		formatsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		formatsGroup.setText((bTargetEnabled ? STEPS[5] : STEPS[4])
				+ Messages.getString("HyperlinkBuilder.DrillThrough.SelectFormat")); //$NON-NLS-1$
		formatsGroup.setLayout(new GridLayout(2, false));

		checkButton = new Button(formatsGroup, SWT.CHECK);
		checkButton.setText(Messages.getString("HyperlinkBuilder.TargetReportFormat")); //$NON-NLS-1$
		checkButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				targetFormatsChooser.setEnabled(((Button) e.widget).getSelection());
				if (!((Button) e.widget).getSelection() && targetFormatsChooser.getSelectionIndex() != -1) {
					targetFormatsChooser.deselect(targetFormatsChooser.getSelectionIndex());
				}
			}
		});

		targetFormatsChooser = new Combo(formatsGroup, SWT.BORDER | SWT.READ_ONLY);
		targetFormatsChooser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		targetFormatsChooser.setVisibleItemCount(30);
		targetFormatsChooser.setItems(supportedFormats);
		targetFormatsChooser.setEnabled(false);
		// select format affects getting TOCTree from ReportDocument
		targetFormatsChooser.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (tocButton.getSelection() && targetReportHandle instanceof IReportDocument) {
					initAnchorChooser(targetReportHandle, true);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	private void createBindingTable(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(LABEL_REPORT_PARAMETER);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		label.setLayoutData(gd);

		paramBindingTable = new TableViewer(parent, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		Table table = paramBindingTable.getTable();
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		gd.minimumHeight = 120;
		table.setLayoutData(gd);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		TableColumn parameterColumn = new TableColumn(table, SWT.LEFT);
		parameterColumn.setText(COLUMN_PARAMETER);
		parameterColumn.setResizable(true);
		parameterColumn.setWidth(140);

		parameterColumn = new TableColumn(table, SWT.CENTER);
		parameterColumn.setText(COLUMN_REQUIRED);
		parameterColumn.setResizable(true);
		parameterColumn.setWidth(70);

		TableColumn dataTypeColumn = new TableColumn(table, SWT.LEFT);
		dataTypeColumn.setText(COLUMN_DATA_TYPE);
		dataTypeColumn.setResizable(true);
		dataTypeColumn.setWidth(70);

		TableColumn valueColumn = new TableColumn(table, SWT.LEFT);
		valueColumn.setText(COLUMN_VALUE);
		valueColumn.setResizable(true);
		valueColumn.setWidth(150);

		table.addKeyListener(new KeyAdapter() {

			/**
			 * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt
			 *      .events.KeyEvent)
			 */
			@Override
			public void keyReleased(KeyEvent e) {
				// If Delete pressed, delete the selected row
				if (e.keyCode == SWT.DEL) {
					deleteRow();
				}
			}

		});

		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				editRow();
			}
		});

		paramBindingTable.setColumnProperties(
				new String[] { COLUMN_PARAMETER, COLUMN_REQUIRED, COLUMN_DATA_TYPE, COLUMN_VALUE });

		parameterChooser = new ComboBoxCellEditor(table, new String[0], SWT.NONE);

		// ExpressionCellEditor valueEditor = new ExpressionCellEditor( table,
		// SWT.PUSH );
		// paramCellEditorFacotryAdapter = ElementAdapterManager.getAdapter(
		// this.inputHandle,
		// IExpressionCellEditorFactory.class );
		// if ( paramCellEditorFacotryAdapter instanceof
		// IExpressionCellEditorFactory )
		// {
		// valueEditor = ( (IExpressionCellEditorFactory)
		// paramCellEditorFacotryAdapter ).createExpressionCellEditor( table,
		// SWT.PUSH );
		// }
		//
		// valueEditor.setExpressionInput( getExpressionProvider( ),
		// inputHandle.getElementHandle( ) );
		// // valueEditor.setExpressionProvider( getExpressionProvider( ) );
		// paramBindingTable.setCellEditors( new CellEditor[]{
		// parameterChooser, null, null, valueEditor
		// } );
		paramBindingTable.setContentProvider(contentProvider);
		paramBindingTable.setLabelProvider(labelProvider);
		// paramBindingTable.setCellModifier( cellModifier );
		paramBindingTable.setInput(paramBindingList);

		paramBindingTable.getTable().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				updateParamBindingButtons();
			}

		});

		paramButtonContainer = new Composite(parent, SWT.NONE);
		paramButtonContainer.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 0;
		paramButtonContainer.setLayout(layout);

		addParamButton = new Button(paramButtonContainer, SWT.PUSH);
		addParamButton.setText(Messages.getString("HyperlinkBuilder.Button.AddParam")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalAlignment = SWT.END;
		gd.grabExcessVerticalSpace = true;
		addParamButton.setLayoutData(gd);
		addParamButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				HyperlinkParameterBuilder builder = new HyperlinkParameterBuilder(
						Messages.getString("HyperlinkBuilder.ParamterBuilder.Add.Title")); //$NON-NLS-1$
				builder.setHyperlinkBuilder(HyperlinkBuilder.this);
				builder.setActionHandle(inputHandle);
				ArrayList<String> avaliableList = getAvailableParamList(null);
				builder.setItems(avaliableList.toArray(new String[0]));
				if (builder.open() == Window.OK) {
					paramBindingList.add((ParamBinding) builder.getResult());
					refreshParamBindingTable();
					paramBindingTable.getTable().select(paramBindingTable.getTable().getItemCount() - 1);
					updateParamBindingButtons();
					validateTables();
				}
			}
		});

		editParamButton = new Button(paramButtonContainer, SWT.PUSH);
		editParamButton.setText(Messages.getString("HyperlinkBuilder.Button.EditParam")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		editParamButton.setLayoutData(gd);
		editParamButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				editRow();
				updateParamBindingButtons();
				validateTables();
			}

		});

		removeParamButton = new Button(paramButtonContainer, SWT.PUSH);
		removeParamButton.setText(Messages.getString("HyperlinkBuilder.Button.RemoveParam")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		removeParamButton.setLayoutData(gd);
		removeParamButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteRow();
				updateParamBindingButtons();
				validateTables();
			}
		});

		removeAllParamButton = new Button(paramButtonContainer, SWT.PUSH);
		removeAllParamButton.setText(Messages.getString("HyperlinkBuilder.Button.RemoveAllParam")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.BEGINNING;
		removeAllParamButton.setLayoutData(gd);
		removeAllParamButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				paramBindingList.clear();
				refreshParamBindingTable();
				validateTables();
			}
		});
	}

	private void clearArea() {
		Control[] controls = displayArea.getChildren();
		for (int i = 0; i < controls.length; i++) {
			controls[i].dispose();
		}
	}

	private void createExpressionButton(Composite parent, final Text text) {
		Listener listener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				updateButtons();
			}

		};
		ExpressionButtonUtil.createExpressionButton(parent, text, getExpressionProvider(),
				inputHandle.getElementHandle(), listener);
	}

	private void createComplexExpressionButton(Composite parent, final Text text) {
		ExpressionButtonUtil.createExpressionButton(parent, text, getExpressionProvider(),
				inputHandle.getElementHandle(), true, SWT.PUSH);
	}

	/**
	 * Configures the expression builder which is to be opened in the hyper-link
	 * builder
	 *
	 * @param builder Expression builder
	 */
	protected void configureExpressionBuilder(ExpressionBuilder builder) {
		builder.setExpressionProvier(getExpressionProvider());
	}

	/**
	 * @return
	 */
	protected ExpressionProvider getExpressionProvider() {
		if (provider == null) {
			return new ExpressionProvider(inputHandle.getElementHandle());
		} else {
			return provider;
		}
	}

	public void setExpressionProvider(ExpressionProvider provider) {
		this.provider = provider;
	}

	private Button createBrowerButton(Composite parent, final Text text, final boolean needQuote,
			final boolean needFilter) {
		List<String> extensionList = ReportPlugin.getDefault().getReportExtensionNameList();
		String[] extensionNames = new String[extensionList.size()];
		for (int i = 0; i < extensionNames.length; i++) {
			extensionNames[i] = "*." + extensionList.get(i); //$NON-NLS-1$
		}
		return createBrowerButton(parent, text, needQuote, needFilter, extensionNames);
	}

	private Button createBrowerButton(Composite parent, final Text text, final boolean needQuote,
			final boolean needFilter, final String[] fileExt) {
		Button button = new Button(parent, SWT.PUSH);
		GridData gd = new GridData();
		if (!Platform.getOS().equals(Platform.OS_MACOSX)) {
			gd.widthHint = 20;
			gd.heightHint = 20;
		}
		button.setLayoutData(gd);
		button.setImage(IMAGE_OPEN_FILE);
		button.setToolTipText(TOOLTIP_BROWSE_FILE);

		// Carl: Create a final variable reference to this so it's available in the
		// anonymous class below
		final HyperlinkBuilder builder = this;

		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean projectMode = false;
				String userSelection = builder.projectFileServiceHelper.getUserSelection(isIDE, getProjectFolder(),
						needFilter, projectMode, fileExt, selectedType, isRelativeToProjectRoot);
				String filename = builder.projectFileServiceHelper.getFilePath(userSelection);

				if (!isIDE || getProjectFolder() == null) {
				} else {
					projectMode = true;
				}

				try {
					if (filename != null) {

						File file = new File(filename);
						if (!(file.isFile() && file.exists())) {
							ExceptionHandler.openErrorMessageBox(
									Messages.getString("HyperlinkBuilder.FileNameError.Title"), //$NON-NLS-1$
									Messages.getString("HyperlinkBuilder.FileNameError.Message")); //$NON-NLS-1$
							return;
						}

						filename = file.toURL().toString();

						// should check extensions in Linux enviroment
						if (needFilter && !checkExtensions(fileExt, filename)) {
							ExceptionHandler.openErrorMessageBox(
									Messages.getString("HyperlinkBuilder.FileNameError.Title"), //$NON-NLS-1$
									Messages.getString("HyperlinkBuilder.FileNameError.Message")); //$NON-NLS-1$
							return;
						}

						if (projectMode && isRelativeToProjectRoot) {
							filename = URIUtil.getRelativePath(getProjectFolder(), filename);

							// force to absolute path syntax
							if (!filename.startsWith("/")) //$NON-NLS-1$
							{
								filename = "/" + filename; //$NON-NLS-1$
							}
						} else {
							filename = URIUtil.getRelativePath(getBasePath(), filename);
						}

						filename = new Path(filename).toString();

						if (text.getData(ExpressionButtonUtil.EXPR_BUTTON) != null) {
							text.setData(ExpressionButtonUtil.EXPR_TYPE, ExpressionType.CONSTANT);
							((ExpressionButton) text.getData(ExpressionButtonUtil.EXPR_BUTTON)).refresh();
						} else if (needQuote) {
							filename = "\"" + filename + "\""; //$NON-NLS-1$ //$NON-NLS-2$
						}

						String location = builder.projectFileServiceHelper.getTargetReportLocation(filename,
								userSelection);
						text.setText(location);
						text.setFocus();
					}

					updateButtons();
				} catch (Exception ex) {
					ExceptionHandler.handle(ex);
				}
			}

		});
		return button;
	}

	private void createTargetBar() {
		if (bTargetEnabled) {
			new Label(displayArea, SWT.NONE).setText(LABEL_TARGET);
			targetChooser = new Combo(displayArea, SWT.READ_ONLY | SWT.BORDER);
			targetChooser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			targetChooser.setVisibleItemCount(30);
			targetChooser.setItems(ChoiceSetFactory.getDisplayNamefromChoiceSet(CHOICESET_TARGET));
			UIUtil.createBlankLabel(displayArea);
		}
	}

	private void createBookmarkBar(boolean isRequired) {
		String label;
		if (isRequired) {
			label = REQUIED_MARK + LABEL_BOOKMARK;
		} else {
			label = LABEL_BOOKMARK;
		}
		new Label(displayArea, SWT.NONE).setText(label);
		bookmarkChooser = new Combo(displayArea, SWT.BORDER | SWT.READ_ONLY);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		// gd.horizontalSpan = 2;
		bookmarkChooser.setLayoutData(gd);
		bookmarkChooser.setVisibleItemCount(30);
		bookmarkChooser.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Expression expr = (Expression) bookmarkChooser.getData(bookmarkChooser.getText());
				bookmarkEditor.setText(expr.getStringExpression());
				bookmarkEditor.setData(ExpressionButtonUtil.EXPR_TYPE, expr.getType());
				ExpressionButton exprButton = ExpressionButtonUtil.getExpressionButton(bookmarkEditor);
				if (exprButton != null) {
					exprButton.refresh();
				}
				updateButtons();
			}

		});
		new Label(displayArea, SWT.NONE);
		new Label(displayArea, SWT.NONE).setText(LABEL_LINKED_EXPRESSION);
		bookmarkEditor = new Text(displayArea, SWT.BORDER | SWT.READ_ONLY | SWT.MULTI);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = bookmarkEditor.computeSize(SWT.DEFAULT, SWT.DEFAULT).y - bookmarkEditor.getBorderWidth() * 2;
		bookmarkEditor.setLayoutData(gd);
		createExpressionButton(displayArea, bookmarkEditor);
	}

	public void setURI(String expr, String exprType) throws SemanticException {
		Expression expression = new Expression(expr, exprType);
		inputHandle.setExpressionProperty(Action.URI_MEMBER, expression);
	}

	public ExpressionHandle getURI() {
		return inputHandle.getExpressionProperty(Action.URI_MEMBER);
	}

	@Override
	protected void okPressed() {
		try {
			// Remove original settings
			inputHandle.setToolTip(null);
			setURI(null, null);
			inputHandle.setTargetBookmark(null);
			inputHandle.setTargetBookmarkType(null);
			inputHandle.setTargetWindow(null);
			inputHandle.setTargetFileType(null);
			inputHandle.setReportName(null);
			inputHandle.setFormatType(null);
			inputHandle.getMember(Action.PARAM_BINDINGS_MEMBER).setValue(null);

			if (DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals(selectedType)) {
				setURI(locationEditor.getText(), (String) locationEditor.getData(ExpressionButtonUtil.EXPR_TYPE));
				if (bTargetEnabled) {
					inputHandle.setTargetWindow(
							ChoiceSetFactory.getValueFromChoiceSet(targetChooser.getText(), CHOICESET_TARGET));
				}
				saveTooltip();
			} else if (DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals(selectedType) && showBookMark) {
				ExpressionButtonUtil.saveExpressionButtonControl(bookmarkEditor, inputHandle,
						Action.TARGET_BOOKMARK_MEMBER);
				saveTooltip();
			} else if (DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equals(selectedType)) {
				if (reportDesignButton.getSelection()) {
					inputHandle.setTargetFileType(DesignChoiceConstants.ACTION_TARGET_FILE_TYPE_REPORT_DESIGN);
					inputHandle.setReportName(locationEditor.getText());
					for (Iterator<ParamBinding> iter = paramBindingList.iterator(); iter.hasNext();) {
						inputHandle.addParamBinding(iter.next());
					}
				} else if (reportDocumentButton.getSelection()) {
					inputHandle.setTargetFileType(DesignChoiceConstants.ACTION_TARGET_FILE_TYPE_REPORT_DOCUMENT);
					inputHandle.setReportName(documentEditor.getText());
					for (Iterator<ParamBinding> iter = paramBindingList.iterator(); iter.hasNext();) {
						inputHandle.addParamBinding(iter.next());
					}
				}

				if (!StringUtil.isBlank(bookmarkEditor.getText()) && !bookmarkEditor.getText().equals("---")) //$NON-NLS-1$
				{
					ExpressionButtonUtil.saveExpressionButtonControl(bookmarkEditor, inputHandle,
							Action.TARGET_BOOKMARK_MEMBER);
				}

				if (targetBookmarkButton.getSelection()) {
					inputHandle.setTargetBookmarkType(DesignChoiceConstants.ACTION_BOOKMARK_TYPE_BOOKMARK);
				} else if (tocButton.getSelection()) {
					inputHandle.setTargetBookmarkType(DesignChoiceConstants.ACTION_BOOKMARK_TYPE_TOC);
				}

				if (bTargetEnabled) {
					if (sameFrameButton.getSelection()) {
						inputHandle.setTargetWindow(DesignChoiceConstants.TARGET_NAMES_TYPE_SELF);
					} else if (newWindowButton.getSelection()) {
						inputHandle.setTargetWindow(DesignChoiceConstants.TARGET_NAMES_TYPE_BLANK);
					} else if (wholePageButton.getSelection()) {
						inputHandle.setTargetWindow(DesignChoiceConstants.TARGET_NAMES_TYPE_TOP);
					} else if (parentFrameButton.getSelection()) {
						inputHandle.setTargetWindow(DesignChoiceConstants.TARGET_NAMES_TYPE_PARENT);
					}
				}

				// for ( int i = 0; i < supportedFormats.length; i++ )
				// {
				// if ( ( (Button) formatCheckBtns.get( supportedFormats[i] )
				// ).getSelection( ) )
				// {
				// inputHandle.setFormatType( supportedFormats[i] );
				// }
				// }
				int index = targetFormatsChooser.getSelectionIndex();
				if (checkButton.getSelection() && index != -1) {
					inputHandle.setFormatType(targetFormatsChooser.getItem(index));
				} else {
					inputHandle.setFormatType(null);
				}
				saveTooltip();
			}
			inputHandle.setLinkType(selectedType);
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
		setResult(inputHandle);
		super.okPressed();
	}

	private void saveTooltip() throws SemanticException {
		if (bTooltipEnabled) {
			if (tooltipText.getText().trim().length() == 0) {
				inputHandle.setToolTip(null);
			} else {
				inputHandle.setToolTip(tooltipText.getText().trim());
			}
		}
	}

	@Override
	public boolean close() {
		closeTargetReport();
		return super.close();
	}

	/**
	 * Set the action to edit.
	 *
	 * @param input the action to edit.
	 */
	public void setInput(ActionHandle input) {
		inputHandle = input;
	}

	@Override
	protected boolean initDialog() {
		switchTo(inputHandle.getLinkType());

		if (DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals(selectedType)) {
			uriRadio.setSelection(true);
		} else if (DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals(selectedType) && showBookMark) {
			if (bookmarkRadio != null) {
				bookmarkRadio.setSelection(true);
			} else {
				noneRadio.setSelection(true);
			}
		} else if (DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equals(selectedType)) {
			drillRadio.setSelection(true);
		} else {
			noneRadio.setSelection(true);
		}

		this.getShell().addListener(SWT.Resize, new Listener() {

			@Override
			public void handleEvent(Event event) {
				GridData gd = (GridData) scrollContent.getLayoutData();
				if (gd.horizontalAlignment != SWT.FILL || gd.verticalAlignment != SWT.FILL) {
					scrollContent.setLayoutData(new GridData(GridData.FILL_BOTH));
				}
				scrollContent.layout(false, true);
				displayArea.layout(false, true);
			}
		});
		return super.initDialog();
	}

	private void initDisplayArea() {
		if (DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals(selectedType)) {
			ExpressionHandle uri = getURI();

			locationEditor.setText(uri == null || uri.getExpression() == null ? "" : (String) uri.getExpression()); //$NON-NLS-1$
			locationEditor.setData(ExpressionButtonUtil.EXPR_TYPE,
					uri == null || uri.getType() == null ? ExpressionType.CONSTANT : (String) uri.getType());
			ExpressionButton button = (ExpressionButton) locationEditor.getData(ExpressionButtonUtil.EXPR_BUTTON);
			if (button != null) {
				button.refresh();
			}

			if (bTargetEnabled) {
				if (inputHandle.getTargetWindow() != null) {
					targetChooser.setText(ChoiceSetFactory.getDisplayNameFromChoiceSet(inputHandle.getTargetWindow(),
							CHOICESET_TARGET));
				} else {
					targetChooser.select(0);
				}
			}
			loadTooltip();
		} else if (DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals(selectedType) && showBookMark) {
			if (inputHandle.getTargetBookmark() != null) {
				ExpressionButtonUtil.initExpressionButtonControl(bookmarkEditor, inputHandle,
						Action.TARGET_BOOKMARK_MEMBER);
			}
			initBookmarkList(SessionHandleAdapter.getInstance().getReportDesignHandle());
			loadTooltip();
		} else if (DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equals(selectedType)) {
			// if ( inputHandle.getTargetBookmark( ) != null )
			// {
			// bookmarkEditor.setText( inputHandle.getTargetBookmark( ) );
			// }
			// if ( inputHandle.getReportName( ) != null )
			// {
			// locationEditor.setText( inputHandle.getReportName( ) );
			// }
			// else
			// {
			// initParamterBindings( );
			// initBookmarkList( null );
			// }
			// if ( inputHandle.getTargetWindow( ) != null )
			// {
			// targetChooser.setText(
			// ChoiceSetFactory.getDisplayNameFromChoiceSet(
			// inputHandle.getTargetWindow( ),
			// CHOICESET_TARGET ) );
			// }
			// else
			// {
			// targetChooser.select( 0 );
			// }
			// if ( inputHandle.getFormatType( ) != null )
			// {
			// formatChooser.setText(
			// ChoiceSetFactory.getDisplayNameFromChoiceSet(
			// inputHandle.getFormatType( ),
			// CHOICESET_FORMAT ) );
			// }
			// else
			// {
			// formatChooser.select( 0 );
			// }

			if (DesignChoiceConstants.ACTION_TARGET_FILE_TYPE_REPORT_DOCUMENT.equals(inputHandle.getTargetFileType())) {
				reportDocumentButton.setSelection(true);
				if (inputHandle.getReportName() != null) {
					documentEditor.setText(inputHandle.getReportName());
				}
				selectRadio(targetGroup, reportDocumentButton);
			} else {
				reportDesignButton.setSelection(true);
				selectRadio(targetGroup, reportDesignButton);
				if (inputHandle.getReportName() != null) {
					locationEditor.setText(inputHandle.getReportName());
				}
			}
			// edit mode, initail pre-setting
			if (inputHandle.getReportName() != null) {
				initTargetReport(inputHandle.getReportName());
			}

			if (reportDocumentButton.getSelection()) {
				handleTargetReportChange(documentEditor.getText());
			} else if (reportDesignButton.getSelection()) {
				initTargetReport(locationEditor.getText());
			}
			initParamterBindings(true);

			if (DesignChoiceConstants.ACTION_BOOKMARK_TYPE_BOOKMARK.equals(inputHandle.getTargetBookmarkType())) {
				targetBookmarkButton.setSelection(true);
				initAnchorChooser(targetReportHandle, false);
			} else if (DesignChoiceConstants.ACTION_BOOKMARK_TYPE_TOC.equals(inputHandle.getTargetBookmarkType())) {
				tocButton.setSelection(true);
				initAnchorChooser(targetReportHandle, true);
			}
			if (inputHandle.getTargetBookmark() != null) {
				ExpressionButtonUtil.initExpressionButtonControl(bookmarkEditor, inputHandle,
						Action.TARGET_BOOKMARK_MEMBER);
			} else {
				bookmarkEditor.setText("---"); //$NON-NLS-1$
			}

			if (bTargetEnabled) {
				if (DesignChoiceConstants.TARGET_NAMES_TYPE_BLANK.equals(inputHandle.getTargetWindow())) {
					newWindowButton.setSelection(true);
				} else if (DesignChoiceConstants.TARGET_NAMES_TYPE_SELF.equals(inputHandle.getTargetWindow())) {
					sameFrameButton.setSelection(true);
				} else if (DesignChoiceConstants.TARGET_NAMES_TYPE_TOP.equals(inputHandle.getTargetWindow())) {
					wholePageButton.setSelection(true);
				} else if (DesignChoiceConstants.TARGET_NAMES_TYPE_PARENT.equals(inputHandle.getTargetWindow())) {
					parentFrameButton.setSelection(true);
				} else {
					newWindowButton.setSelection(true);
				}
			}

			if (inputHandle.getFormatType() != null) {
				for (int index = 0; index < supportedFormats.length; index++) {
					if (supportedFormats[index].equals(inputHandle.getFormatType())) {
						checkButton.setSelection(true);
						targetFormatsChooser.setEnabled(true);
						targetFormatsChooser.select(index);
						break;
					}
				}
			}
			loadTooltip();
		}
		updateButtons();
	}

	private void loadTooltip() {
		if (bTooltipEnabled) {
			if (inputHandle.getToolTip() != null) {
				tooltipText.setText(inputHandle.getToolTip());
			}
		}
	}

	private void initParamterBindings(boolean first) {
		if (targetReportHandle != null) {
			paramList.clear();
			paramTypes.clear();

			String errorMessage = null;
			String newFilename = null;
			if (reportDesignButton.getSelection()) {
				String location = locationEditor.getText();
				newFilename = this.projectFileServiceHelper.getFilePath(location);
			} else if (reportDocumentButton.getSelection()) {
				newFilename = documentEditor.getText();
			}
			if (newFilename == null || newFilename.length() == 0) {
				errorMessage = ERROR_MSG_REPORT_REQUIRED;
			} else {
				ReportDesignHandle tmpReportDesign = null;
				if (targetReportHandle instanceof IReportDocument) {
					tmpReportDesign = ((IReportDocument) targetReportHandle).getReportDesign();
				} else if (targetReportHandle instanceof ReportDesignHandle) {
					tmpReportDesign = (ReportDesignHandle) targetReportHandle;
				}

				if (tmpReportDesign != null) {
					if (targetReportHandle instanceof ReportDesignHandle
							|| targetReportHandle instanceof IReportDocument) {
						// TODO pass in current format
						Map<String, List<IHyperlinkParameter>> hparams = getHyperlinkParameters(tmpReportDesign, null);

						// TODO should check and prevent duplicate param names
						for (Entry<String, List<IHyperlinkParameter>> ent : hparams.entrySet()) {
							String cat = ent.getKey();
							List<IHyperlinkParameter> hps = ent.getValue();

							if (hps != null) {
								for (IHyperlinkParameter hp : hps) {
									if (hp instanceof ReportHyperlinkParameter
											&& targetReportHandle instanceof IReportDocument) {
										continue;
									}
									paramList.add(hp);
									paramTypes.put(hp.getName(), hp.getDataType());
								}
							}
						}
					}
				}
			}

			if (errorMessage != null) {
				messageLine.setText(errorMessage);
				messageLine.setImage(ERROR_ICON);
			} else {
				messageLine.setText(""); //$NON-NLS-1$
				messageLine.setImage(null);
			}

			updateButtons();
		}

		if (first) {
			paramBindingList.clear();
			for (Iterator iter = inputHandle.paramBindingsIterator(); iter.hasNext();) {
				ParamBindingHandle handle = (ParamBindingHandle) iter.next();
				paramBindingList.add((ParamBinding) handle.getStructure());
			}
		}

		// parammeter table is always enabled
		paramBindingTable.getTable().setEnabled(messageLine.getText().length() == 0);
		refreshParamBindingTable();
		enableParamButtons(paramBindingTable.getTable().getEnabled());
	}

	private void enableParamButtons(boolean enabled) {
		paramButtonContainer.setEnabled(enabled);
		if (!enabled) {
			Control[] children = paramButtonContainer.getChildren();
			for (int i = 0; i < children.length; i++) {
				children[i].setEnabled(false);
			}
		} else {
			updateParamBindingButtons();
		}
	}

	private Map<String, List<IHyperlinkParameter>> getHyperlinkParameters(ReportDesignHandle design, String format) {
		Map<String, List<IHyperlinkParameter>> result = new LinkedHashMap<>();

		Object[] adapters = ElementAdapterManager.getAdapters(design, IHyperlinkParameterProvider.class);

		if (adapters != null) {
			for (Object adapt : adapters) {
				IHyperlinkParameterProvider paramProvider = (IHyperlinkParameterProvider) adapt;

				if (paramProvider != null) {
					String[] categories = paramProvider.getCategories();

					if (categories != null) {
						for (String cat : categories) {
							IHyperlinkParameter[] params = paramProvider.getParameters(cat, format);

							if (params != null) {
								List<IHyperlinkParameter> buk = result.get(cat);

								if (buk == null) {
									buk = new ArrayList<>();
									result.put(cat, buk);
								}

								for (IHyperlinkParameter pm : params) {
									buk.add(pm);
								}
							}
						}
					}
				}
			}
		}

		return result;
	}

	private List<Object> getAllBookMarkExpressions(ReportDesignHandle reportDesignHandle) {
		ReportDesign reportDesign = (ReportDesign) reportDesignHandle.getModule();
		return reportDesign.collectPropValues(reportDesignHandle.getBody().getSlotID(), IReportItemModel.BOOKMARK_PROP);
	}

	private void initBookmarkList(Object handle) {
		bookmarkChooser.removeAll();
		if (handle instanceof ReportDesignHandle) {
			List<Object> expressions = getAllBookMarkExpressions((ReportDesignHandle) handle);
			for (Object obj : expressions) {
				Expression expr = (Expression) obj;
				bookmarkChooser.add(expr.getStringExpression());
				bookmarkChooser.setData(expr.getStringExpression(), expr);
			}
			bookmarkChooser.setText(bookmarkEditor.getText());
		}
		bookmarkChooser.setEnabled(bookmarkChooser.getItemCount() > 0);
	}

	private void initAnchorChooser(Object handle, boolean isToc) {
		anchorChooser.removeAll();
		if (handle instanceof ReportDesignHandle) {
			List chooserItems = new ArrayList();
			if (isToc) {
				chooserItems.addAll(((ReportDesignHandle) handle).getAllTocs());

			} else {
				chooserItems.addAll(((ReportDesignHandle) handle).getAllBookmarks());
			}

			List displayItems = new ArrayList();
			displayItems.add(0, "---"); //$NON-NLS-1$
			for (int i = 0; i < chooserItems.size(); i++) {
				displayItems.add(DEUtil.resolveNull((String) chooserItems.get(i)));
			}
			// anchorChooser.setItems( (String[]) ( (ReportDesignHandle)
			// handle ).getAllTocs( )
			// .toArray( new String[0] ) );
			anchorChooser.setItems((String[]) displayItems.toArray(new String[0]));
		} else if (handle instanceof IReportDocument) {
			if (isToc) {
				String format = "html"; //$NON-NLS-1$
				if (targetFormatsChooser.getSelectionIndex() != -1) {
					format = supportedFormats[targetFormatsChooser.getSelectionIndex()];
				}
				ITOCTree tocTree = ((IReportDocument) handle).getTOCTree(format,
						SessionHandleAdapter.getInstance().getSessionHandle().getULocale());
				TOCNode rootTocNode = tocTree.getRoot();
				// TOCNode rootTocNode = ( (IReportDocument) handle ).findTOC(
				// null );
				List<String> chooserItems = getAllTocDisplayString(rootTocNode);
				chooserItems.add(0, "---"); //$NON-NLS-1$
				// anchorChooser.setItems( (String[]) getAllTocDisplayString(
				// rootTocNode ).toArray( new String[0] ) );
				anchorChooser.setItems(chooserItems.toArray(new String[0]));
			} else {
				anchorChooser.setItems(getDocumentBookmarks((IReportDocument) handle));
			}
		}

		bookmarkEditor.setText(""); //$NON-NLS-1$

		String bookmark = inputHandle.getTargetBookmark();
		String[] chooserValues = anchorChooser.getItems();
		if (bookmark != null && chooserValues != null) {
			for (int i = 0; i < chooserValues.length; i++) {
				if (bookmark.equals(chooserValues[i])) {
					anchorChooser.select(i);
					bookmarkEditor.setText(anchorChooser.getText());
					break;
				}
			}
		}

		anchorChooser.setEnabled(anchorChooser.getItemCount() > 0);
	}

	private String[] getDocumentBookmarks(IReportDocument rdoc) {
		List bookmarks = rdoc.getBookmarks();
		String[] bookmarkArray = new String[bookmarks.size() + 1];
		bookmarkArray[0] = "---"; //$NON-NLS-1$
		int i = 1;
		for (Iterator iter = bookmarks.iterator(); iter.hasNext();) {
			bookmarkArray[i] = "\"" + iter.next() + "\""; //$NON-NLS-1$//$NON-NLS-2$
			i++;
		}
		return bookmarkArray;
	}

	private List<String> getAllTocDisplayString(TOCNode parent) {
		List<String> tocList = new ArrayList<>();
		if (parent.getParent() != null) {
			tocList.add("\"" + parent.getDisplayString() + "\""); //$NON-NLS-1$//$NON-NLS-2$
		}
		List childToc = parent.getChildren();
		for (Iterator iter = childToc.iterator(); iter.hasNext();) {
			TOCNode node = (TOCNode) iter.next();
			tocList.addAll(getAllTocDisplayString(node));
		}
		return tocList;
	}

	private void addRadioListener(Button radio, final String type) {
		radio.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (selectedType != type) {
					switchTo(type);
				}
			}
		});
	}

	private void updateButtons() {
		boolean okEnable = true;
		if (DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK.equals(selectedType)) {
			okEnable = !StringUtil.isBlank(locationEditor.getText());
		} else if (DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK.equals(selectedType) && showBookMark) {
			okEnable = !StringUtil.isBlank(bookmarkEditor.getText());
		} else if (DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH.equals(selectedType)) {
			okEnable = ((!StringUtil.isBlank(locationEditor.getText()) && reportDesignButton.getSelection())
					|| (!StringUtil.isBlank(documentEditor.getText()) && reportDocumentButton.getSelection()))
					&& messageLine.getText().length() == 0;
		}
		getOkButton().setEnabled(okEnable);
	}

	private void validateTables() {
		for (int i = 0; i < paramBindingList.size(); i++) {
			for (int j = i + 1; j < paramBindingList.size(); j++) {
				if (paramBindingList.get(i).getParamName().equals(paramBindingList.get(j).getParamName())) {
					String errorMessage = Messages
							.getString("HyperlinkBuilder.DrillThrough.ErrorMsg.DuplicateParameterName"); //$NON-NLS-1$
					messageLine.setText(errorMessage);
					messageLine.setImage(ERROR_ICON);
					getOkButton().setEnabled(false);
					return;
				}
			}
		}
		messageLine.setText(""); //$NON-NLS-1$
		messageLine.setImage(null);
		updateButtons();
	}

	private ArrayList<String> getAvailableParamList(String selectedParameter) {
		ArrayList<String> avaliableList = new ArrayList<>();

		for (Iterator<IHyperlinkParameter> iter = paramList.iterator(); iter.hasNext();) {
			IHyperlinkParameter parameter = iter.next();
			avaliableList.add(parameter.getName());
		}

		for (Iterator<ParamBinding> iter = paramBindingList.iterator(); iter.hasNext();) {
			ParamBinding paramBinding = iter.next();
			if (!paramBinding.getParamName().equals(selectedParameter)) {
				avaliableList.remove(paramBinding.getParamName());
			}
		}
		return avaliableList;
	}

	private void deleteRow() {
		ParamBinding paramBinding = getSelectedBinding();
		if (paramBinding != null) {
			int index = paramBindingTable.getTable().getSelectionIndex();
			paramBindingList.remove(paramBinding);
			refreshParamBindingTable();
			if (paramBindingTable.getTable().getItemCount() <= index) {
				index--;
			}
			if (index >= 0) {
				paramBindingTable.getTable().select(index);
			}
		}
	}

	private void editRow() {
		ParamBinding paramBinding = getSelectedBinding();
		if (paramBinding == null) {
			return;
		}
		HyperlinkParameterBuilder builder = new HyperlinkParameterBuilder(
				Messages.getString("HyperlinkBuilder.ParamterBuilder.Edit.Title")); //$NON-NLS-1$
		builder.setHyperlinkBuilder(HyperlinkBuilder.this);
		builder.setActionHandle(inputHandle);
		builder.setParamBinding(paramBinding);
		if (builder.open() == Window.OK) {
			refreshParamBindingTable();
			validateTables();
		}
	}

	private ParamBinding getSelectedBinding() {
		IStructuredSelection selection = (IStructuredSelection) paramBindingTable.getSelection();
		if (selection.size() == 1) {
			return (ParamBinding) selection.getFirstElement();
		}
		return null;
	}

	private void closeTargetReport() {
		if (targetReportHandle instanceof ReportDesignHandle) {
			((ReportDesignHandle) targetReportHandle).close();
		} else if (targetReportHandle instanceof IReportDocument) {
			((IReportDocument) targetReportHandle).close();
		}
		targetReportHandle = null;
	}

	private void initTargetReport(String location) {
		String newFilename = this.projectFileServiceHelper.getFilePath(location);
		closeTargetReport();
		targetReportHandle = null;
		// String errorMessage = null;
		if (newFilename != null && newFilename.trim().length() > 0) {
			if (newFilename.toLowerCase().endsWith(".rptdocument")) //$NON-NLS-1$
			{
				ReportEngine engine = new ReportEngine(new EngineConfig());

				try {
					targetReportHandle = engine.openReportDocument(resolvePath(newFilename));
				} catch (EngineException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
					// errorMessage = e.getMessage( );
				}
			} else {
				try {
					targetReportHandle = SessionHandleAdapter.getInstance().getSessionHandle().openDesign(newFilename);
				} catch (DesignFileException e) {
					try {
						targetReportHandle = SessionHandleAdapter.getInstance().getSessionHandle()
								.openDesign(resolvePath(newFilename));
					} catch (DesignFileException e1) {
						// errorMessage = ERROR_MSG_INVALID_REPORT;
					}
				}
			}
		}
		messageLine.setText(""); //$NON-NLS-1$
		messageLine.setImage(null);

	}

	private String resolvePath(String file_path) {
		String rootPath = null;
		if (file_path.startsWith("/")) //$NON-NLS-1$
		{
			rootPath = getProjectFolder();
		} else {
			rootPath = getBasePath();
		}
		return URIUtil.resolveAbsolutePath(rootPath, file_path);
	}

	private String getBasePath() {
		String baseFile = SessionHandleAdapter.getInstance().getReportDesignHandle().getFileName();
		return new File(baseFile).getParent();
	}

	/**
	 * Set the action to edit with a serialized string
	 *
	 * @param input  the serialized string
	 * @param handle DesignElementHandle
	 * @throws DesignFileException
	 */
	public void setInputString(String input, DesignElementHandle handle) throws DesignFileException {
		setInput(ModuleUtil.deserializeAction(input, handle));
	}

	/**
	 * Returns the serialized result action.
	 *
	 * @return the serialized result action
	 * @throws IOException
	 */
	public String getResultString() throws IOException {
		return ModuleUtil.serializeAction((ActionHandle) getResult());
	}

	/**
	 * Set radio members enable, and others radio's members disable.
	 *
	 * @param container
	 * @param radio
	 * @param enable
	 */
	private void selectRadio(Composite container, Button radio) {
		if (!radio.getSelection()) {
			return;
		}
		Control[] children = container.getChildren();
		boolean isChoiceChild = false;
		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof Label) {
				continue;
			}
			// break if style is radio
			if ((children[i].getStyle() & SWT.RADIO) != 0) {
				if (children[i] == radio) {
					isChoiceChild = true;
				} else {
					isChoiceChild = false;
				}
				continue;
			}
			if (!isChoiceChild) {
				children[i].setEnabled(false);
			} else {
				children[i].setEnabled(true);
			}
		}
	}

	private void deSelectAnchor() {
		targetBookmarkButton.setSelection(false);
		tocButton.setSelection(false);
		anchorChooser.removeAll();
		bookmarkEditor.setText(""); //$NON-NLS-1$
	}

	protected boolean isParameterRequired(String paramName) {
		if (paramName == null) {
			return false;
		}

		if (paramList != null) {
			for (Iterator<IHyperlinkParameter> iter = paramList.iterator(); iter.hasNext();) {
				IHyperlinkParameter obj = iter.next();

				if (obj instanceof IReportHyperlinkParameter && obj.getName().equals(paramName)) {
					ParameterHandle paramHandle = ((IReportHyperlinkParameter) obj).getParameterHandle();

					if (paramHandle instanceof AbstractScalarParameterHandle) {
						return ((AbstractScalarParameterHandle) paramHandle).isRequired();
					}
				}
			}
		}
		return false;
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

	protected String getProjectFolder() {
		return UIUtil.getProjectFolder();
	}

	protected String getDisplayDataType(ParamBinding binding) {
		String name = binding.getParamName();
		String dataType = paramTypes.get(name);
		if (dataType == null) {
			return ""; //$NON-NLS-1$
		}
		return getDisplayDataType(dataType);
	}

	protected String getDisplayDataType(String dataType) {
		final IChoiceSet DATA_TYPE_CHOICE_SET = DEUtil.getMetaDataDictionary()
				.getElement(ReportDesignConstants.SCALAR_PARAMETER_ELEMENT)
				.getProperty(ScalarParameterHandle.DATA_TYPE_PROP).getAllowedChoices();

		IChoice choice = DATA_TYPE_CHOICE_SET.findChoice(dataType);
		if (choice == null) {
			return ""; //$NON-NLS-1$
		}

		return choice.getDisplayName();
	}

	protected Expression getParamBindingExpression(ParamBinding param) {
		List<Expression> expressions = param.getExpressionList();
		if (expressions == null || expressions.isEmpty()) {
			return null;
		}
		return expressions.get(0);
	}

	private void handleTargetReportChange(String target) {
		closeTargetReport();
		initTargetReport(target);
		initParamterBindings(false);
		updateButtons();
		deSelectAnchor();
	}

	protected String getTargetReportFile() {
		if (targetReportHandle instanceof IReportDocument) {
			return ((IReportDocument) targetReportHandle).getReportDesign().getFileName();
		} else if (targetReportHandle instanceof ReportDesignHandle) {
			return ((ReportDesignHandle) targetReportHandle).getFileName();
		}
		return null;
	}

	private void refreshParamBindingTable() {
		paramBindingTable.refresh();
		updateParamBindingButtons();
	}

	protected void updateParamBindingButtons() {
		editParamButton.setEnabled(getSelectedBinding() != null);
		removeParamButton.setEnabled(getSelectedBinding() != null);
		removeAllParamButton.setEnabled(paramBindingTable.getTable().getItemCount() > 0);
		addParamButton.setEnabled(true);
	}
}
