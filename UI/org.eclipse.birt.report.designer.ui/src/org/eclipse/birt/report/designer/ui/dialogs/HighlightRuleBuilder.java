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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.data.ui.util.SelectValueFetcher;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ExpressionFilter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.PreviewLabel;
import org.eclipse.birt.report.designer.internal.ui.extension.IUseCubeQueryList;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.MultiValueCombo;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.ValueCombo;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.HighlightHandleProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.AttributeConstant;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.ui.widget.ColorBuilder;
import org.eclipse.birt.report.designer.ui.widget.FontSizeBuilder;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
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
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;

/**
 * Dialog for adding or editing highlight Rule.
 */
public class HighlightRuleBuilder extends TitleAreaDialog
{

	private final String NULL_STRING = null;
	protected IExpressionProvider expressionProvider;
	protected transient String bindingName = null;
	protected ReportElementHandle currentItem = null;

	protected String[] popupItems = null;

	protected static String[] EMPTY_ARRAY = new String[]{};

	private static final String NONE_DISPLAY_TEXT = Messages.getString( "HighlightRuleBuilderDialog.displayText.None" ); //$NON-NLS-1$

	protected static String[] actions = new String[]{
			Messages.getString( "ExpressionValueCellEditor.selectValueAction" ), //$NON-NLS-1$
			Messages.getString( "ExpressionValueCellEditor.buildExpressionAction" ), //$NON-NLS-1$
	};

	protected ParamBindingHandle[] bindingParams = null;

	public static final int EXPRESSION_CONTROL_COMBO = 0;
	public static final int EXPRESSION_CONTROL_TEXT = 1;
	private int exprControlType;
	protected String dlgDescription = "";
	protected String dlgTitle = "";
	
	protected Logger logger = Logger.getLogger( HighlightRuleBuilder.class.getName( ) );


	/**
	 * Usable operators for building highlight rule conditions.
	 */
	public static final String[][] OPERATOR;

	protected List columnList;

	/**
	 * Constant, represents empty String array.
	 */
	private static final String[] EMPTY = new String[0];

	protected List valueList = new ArrayList( );

	static
	{
		IChoiceSet chset = ChoiceSetFactory.getStructChoiceSet( HighlightRule.STRUCTURE_NAME,
				HighlightRule.OPERATOR_MEMBER );
		IChoice[] chs = chset.getChoices( new AlphabeticallyComparator( ) );
		OPERATOR = new String[chs.length][2];

		for ( int i = 0; i < chs.length; i++ )
		{
			OPERATOR[i][0] = chs[i].getDisplayName( );
			OPERATOR[i][1] = chs[i].getName( );
		}
	}

	/**
	 * Returns the operator value by its display name.
	 * 
	 * @param name
	 */
	public static String getValueForOperator( String name )
	{
		for ( int i = 0; i < OPERATOR.length; i++ )
		{
			if ( OPERATOR[i][0].equals( name ) )
			{
				return OPERATOR[i][1];
			}
		}

		return null;
	}

	/**
	 * Returns how many value fields this operator needs.
	 * 
	 * @param operatorValue
	 */
	public static int determineValueVisible( String operatorValue )
	{
		if ( DesignChoiceConstants.MAP_OPERATOR_ANY.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_FALSE.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_TRUE.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_NULL.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_NOT_NULL.equals( operatorValue ) )
		{
			return 0;
		}
		else if ( DesignChoiceConstants.MAP_OPERATOR_LT.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_LE.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_EQ.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_NE.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_GE.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_GT.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_LIKE.equals( operatorValue ) )
		{
			return 1;
		}
		else if ( DesignChoiceConstants.MAP_OPERATOR_BETWEEN.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_NOT_BETWEEN.equals( operatorValue ) )
		{
			return 2;
		}
		else if ( DesignChoiceConstants.MAP_OPERATOR_IN.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_NOT_IN.equals( operatorValue ) )
		{
			return 3;
		}

		return 1;
	}

	/**
	 * Returns the operator display name by its value.
	 * 
	 * @param value
	 */
	public static String getNameForOperator( String value )
	{
		for ( int i = 0; i < OPERATOR.length; i++ )
		{
			if ( OPERATOR[i][1].equals( value ) )
			{
				return OPERATOR[i][0];
			}
		}

		return ""; //$NON-NLS-1$
	}

	/**
	 * Returns the index for given operator value in the operator list.
	 * 
	 * @param value
	 */
	public static int getIndexForOperatorValue( String value )
	{
		for ( int i = 0; i < OPERATOR.length; i++ )
		{
			if ( OPERATOR[i][1].equals( value ) )
			{
				return i;
			}
		}

		return 0;
	}

	private HighlightRuleHandle handle;

	private HighlightHandleProvider provider;

	private int handleCount;

	protected Combo expressionCombo, stylesChooser;
	protected Text expressionText;
	private Combo operator;

	protected Composite valueListComposite;
	protected MultiValueCombo addExpressionValue;
	protected Button addBtn, editBtn, delBtn, delAllBtn;
	protected Table table;
	protected TableViewer tableViewer;
	protected int valueVisible;
	protected List compositeList = new ArrayList( );

	private ValueCombo expressionValue1, expressionValue2;

	private Label andLable;

	private Combo font;

	private FontSizeBuilder size;

	private ColorBuilder color;

	private ColorBuilder backColor;

	private Button bold, italic, underline, linethrough;

	private PreviewLabel previewLabel;

	protected DesignElementHandle designHandle;

	private boolean isBoldChanged, isItalicChanged, isUnderlineChanged,
			isLinethroughChanged;

	private static final String DEFAULT_CHOICE = Messages.getString( "HighlightRuleBuilderDialog.text.Default" ); //$NON-NLS-1$

	private static final String[] SYSTEM_FONT_LIST = DEUtil.getSystemFontNames( );

	protected static final String VALUE_OF_THIS_DATA_ITEM = Messages.getString( "HighlightRuleBuilderDialog.choice.ValueOfThisDataItem" ); //$NON-NLS-1$

	private Map styles = new HashMap( );

	/**
	 * Default constructor.
	 * 
	 * @param parentShell
	 *            Parent Shell
	 * @param title
	 *            Windows Title
	 */
	public HighlightRuleBuilder( Shell parentShell, String title,
			HighlightHandleProvider provider )
	{
		super( parentShell );
		this.dlgTitle = title;
		setShellStyle( getShellStyle( ) | SWT.RESIZE );
		this.provider = provider;
	}

	private String[] getDataSetColumns( )
	{
		if ( columnList.isEmpty( ) )
		{
			return EMPTY;
		}
		String[] values = new String[columnList.size( )];
		for ( int i = 0; i < columnList.size( ); i++ )
		{
			values[i] = ( (ComputedColumnHandle) columnList.get( i ) ).getName( );
		}
		return values;
	}

	public void setReportElement( ReportElementHandle reportItem )
	{
		currentItem = reportItem;
	}

	protected SelectionListener expSelListener = new SelectionAdapter( ) {

		public void widgetSelected( SelectionEvent e )
		{
			if ( getExpression( ).equals( VALUE_OF_THIS_DATA_ITEM )
					&& designHandle instanceof DataItemHandle )
			{
				if ( designHandle.getContainer( ) instanceof ExtendedItemHandle )
				{
					setExpression( DEUtil.getDataExpression( ( (DataItemHandle) designHandle ).getResultSetColumn( ) ) );
				}
				else
				{
					setExpression( DEUtil.getColumnExpression( ( (DataItemHandle) designHandle ).getResultSetColumn( ) ) );
				}

			}
			else
			{
				String newValue = getExpression( );
				String value = DEUtil.getExpression( getResultSetColumn( newValue ) );
				if ( value != null )
					newValue = value;
				setExpression( newValue );
			}

			updateButtons( );
		}
	};

	protected Control createDialogArea( Composite parent )
	{

		Composite composite = (Composite) super.createDialogArea( parent );
		GridData gdata;
		GridLayout glayout;

		refreshList( );

		Composite contents = new Composite( composite, SWT.NONE );
		contents.setLayout( new GridLayout( ) );
		contents.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		// createApplyStyleArea( innerParent );

		Group condition = new Group( contents, SWT.NONE );
		gdata = new GridData( GridData.FILL_HORIZONTAL );
		gdata.heightHint = 185;
		condition.setLayoutData( gdata );
		glayout = new GridLayout( 4, false );
		condition.setLayout( glayout );
		condition.setText( Messages.getString( "HighlightRuleBuilderDialog.text.Group.Condition" ) );

		gdata = new GridData( );
		gdata.widthHint = 150;
		if ( exprControlType == EXPRESSION_CONTROL_COMBO )
		{
			expressionCombo = new Combo( condition, SWT.NONE );
			expressionCombo.setLayoutData( gdata );
			expressionCombo.setItems( getDataSetColumns( ) );
			fillExpression( expressionCombo );
			expressionCombo.addSelectionListener( expSelListener );
			expressionCombo.addModifyListener( new ModifyListener( ) {

				public void modifyText( ModifyEvent e )
				{
					updateButtons( );
				}
			} );
		}
		else
		{
			expressionText = new Text( condition, SWT.BORDER );
			expressionText.setLayoutData( gdata );
			if ( handle != null )
			{
				expressionText.setText( handle.getTestExpression( ) );
			}
			expressionText.addModifyListener( new ModifyListener( ) {

				public void modifyText( ModifyEvent e )
				{
					updateButtons( );
				}
			} );
		}

		Button expBuilder = new Button( condition, SWT.PUSH );
		// expBuilder.setText( "..." ); //$NON-NLS-1$
		// gdata = new GridData( );
		// gdata.heightHint = 20;
		// gdata.widthHint = 20;
		// expBuilder.setLayoutData( gdata );
		expBuilder.setToolTipText( Messages.getString( "HighlightRuleBuilderDialog.tooltip.ExpBuilder" ) ); //$NON-NLS-1$
		expBuilder.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{

				editValue( getExpressionControl( ) );
			}
		} );

		UIUtil.setExpressionButtonImage( expBuilder );

		operator = new Combo( condition, SWT.READ_ONLY );
		for ( int i = 0; i < OPERATOR.length; i++ )
		{
			operator.add( OPERATOR[i][0] );
		}
		operator.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				update2ValueStatus( );
			}
		} );

		refreshList( );

		create2ValueComposite( condition );

		createApplyStyleArea( contents );

		Label lb = new Label( contents, SWT.SEPARATOR | SWT.HORIZONTAL );
		lb.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		if ( handle != null )
		{
			syncViewProperties( );
		}
		else
		{
			update2ValueStatus( );
		}

		return composite;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createContents(org.eclipse.swt.widgets
	 * .Composite)
	 */
	protected Control createContents( Composite parent )
	{
		Composite composite = (Composite) super.createContents( parent );
		parent.getShell( ).setText( dlgTitle );
		setTitle( Messages.getString( "HighlightRuleBuilderDialog.text.Title" ) );
		setMessage( dlgDescription );
		UIUtil.bindHelp( parent, IHelpContextIds.HIGHLIGHT_RULE_BUILDER_ID );

		updatePreview( );
		updateButtons( );

		return composite;

	}

	private void update2ValueStatus( )
	{
		String value = getValueForOperator( operator.getText( ) );
		valueVisible = determineValueVisible( value );

		if ( valueVisible == 3 )
		{
			int ret = createValueListComposite( operator.getParent( ) );
			if ( ret != 0 )
			{
				if ( handle != null )
				{
					valueList = new ArrayList( handle.getValue1List( ) );
				}

				tableViewer.setInput( valueList );
			}

		}
		else
		{
			int ret = create2ValueComposite( operator.getParent( ) );
			if ( ret != 0 && handle != null )
			{
				expressionValue1.setText( DEUtil.resolveNull( handle.getValue1( ) ) );
				expressionValue2.setText( DEUtil.resolveNull( handle.getValue2( ) ) );
			}

		}

		if ( valueVisible == 0 )
		{
			expressionValue1.setVisible( false );
			expressionValue2.setVisible( false );
			andLable.setVisible( false );
		}
		else if ( valueVisible == 1 )
		{
			expressionValue1.setVisible( true );
			expressionValue2.setVisible( false );
			andLable.setVisible( false );
		}
		else if ( valueVisible == 2 )
		{
			expressionValue1.setVisible( true );
			expressionValue2.setVisible( true );
			andLable.setVisible( true );
		}

		updateButtons( );
	}

	protected Object getResultSetColumn( String name )
	{
		if ( columnList.isEmpty( ) )
		{
			return null;
		}
		for ( int i = 0; i < columnList.size( ); i++ )
		{
			ComputedColumnHandle column = (ComputedColumnHandle) columnList.get( i );
			if ( column.getName( ).equals( name ) )
			{
				return column;
			}
		}
		return null;
	}

	private Composite createApplyStyleArea( Composite parent )
	{
		Group styleGroup = new Group( parent, SWT.NONE );
		styleGroup.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		styleGroup.setLayout( new GridLayout( 5, false ) );
		styleGroup.setText( Messages.getString( "HighlightRuleBuilderDialog.text.Group.Format" ) );

		Label lb = new Label( styleGroup, SWT.NONE );
		lb.setText( Messages.getString( "HighlightRuleBuilderDialog.text.applyStyle" ) ); //$NON-NLS-1$
		lb.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );

		stylesChooser = new Combo( styleGroup, SWT.READ_ONLY | SWT.DROP_DOWN );
		GridData gdata = new GridData( );
		gdata.widthHint = 100;
		gdata.horizontalSpan = 2;
		stylesChooser.setLayoutData( gdata );
		fillStyles( stylesChooser );
		stylesChooser.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				updateButtons( );
				updatePreview( );
			}
		} );

		createDummy( styleGroup, 3 );

		lb = new Label( styleGroup, SWT.SEPARATOR | SWT.HORIZONTAL );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 5;
		lb.setLayoutData( gd );

		lb = new Label( styleGroup, 0 );
		lb.setText( Messages.getString( "HighlightRuleBuilderDialog.text.Font" ) ); //$NON-NLS-1$		
		lb.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );

		font = new Combo( styleGroup, SWT.READ_ONLY );
		gdata = new GridData( );
		gdata.widthHint = 100;
		font.setLayoutData( gdata );
		IChoiceSet fontSet = ChoiceSetFactory.getElementChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
				StyleHandle.FONT_FAMILY_PROP );
		font.setData( fontSet );
		font.setItems( ChoiceSetFactory.getDisplayNamefromChoiceSet( fontSet,
				new AlphabeticallyComparator( ) ) );
		if ( SYSTEM_FONT_LIST != null && SYSTEM_FONT_LIST.length > 0 )
		{
			for ( int i = 0; i < SYSTEM_FONT_LIST.length; i++ )
			{
				font.add( SYSTEM_FONT_LIST[i] );
			}
		}
		font.add( DEFAULT_CHOICE, 0 );
		font.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				updatePreview( );
			}
		} );
		if ( font.getItemCount( ) > 0 )
		{
			font.select( 0 );
		}

		createDummy( styleGroup, 1 );

		lb = new Label( styleGroup, 0 );
		lb.setText( Messages.getString( "HighlightRuleBuilderDialog.text.Size" ) ); //$NON-NLS-1$
		lb.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );

		size = new FontSizeBuilder( styleGroup, SWT.None );
		if ( designHandle != null )
		{
			size.setDefaultUnit( designHandle.getPropertyHandle( StyleHandle.FONT_SIZE_PROP )
					.getDefaultUnit( ) );
		}
		gdata = new GridData( );
		gdata.widthHint = 200;
		size.setLayoutData( gdata );
		size.setFontSizeValue( null );
		size.addListener( SWT.Modify, new Listener( ) {

			public void handleEvent( Event event )
			{
				updatePreview( );
			}
		} );

		lb = new Label( styleGroup, 0 );
		lb.setText( Messages.getString( "HighlightRuleBuilderDialog.text.Color" ) ); //$NON-NLS-1$
		lb.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );

		color = new ColorBuilder( styleGroup, 0 );
		gdata = new GridData( );
		gdata.widthHint = 100;
		color.setLayoutData( gdata );
		color.setChoiceSet( ChoiceSetFactory.getElementChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
				StyleHandle.COLOR_PROP ) );
		color.setRGB( null );
		color.addListener( SWT.Modify, new Listener( ) {

			public void handleEvent( Event event )
			{
				previewLabel.setForeground( ColorManager.getColor( color.getRGB( ) ) );
				previewLabel.redraw( );
			}
		} );

		createDummy( styleGroup, 2 );

		Composite fstyle = new Composite( styleGroup, 0 );
		gdata = new GridData( GridData.FILL_HORIZONTAL );
		fstyle.setLayoutData( gdata );
		fstyle.setLayout( new GridLayout( 4, false ) );

		bold = createToggleButton( fstyle );
		bold.setImage( ReportPlatformUIImages.getImage( AttributeConstant.FONT_WIDTH ) );
		bold.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				isBoldChanged = true;
				updatePreview( );
			}
		} );
		bold.setToolTipText( Messages.getString( "HighlightRuleBuilderDialog.tooltip.Bold" ) ); //$NON-NLS-1$

		italic = createToggleButton( fstyle );
		italic.setImage( ReportPlatformUIImages.getImage( AttributeConstant.FONT_STYLE ) );
		italic.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				isItalicChanged = true;
				updatePreview( );
			}
		} );
		italic.setToolTipText( Messages.getString( "HighlightRuleBuilderDialog.tooltip.Italic" ) ); //$NON-NLS-1$

		underline = createToggleButton( fstyle );
		underline.setImage( ReportPlatformUIImages.getImage( AttributeConstant.TEXT_UNDERLINE ) );
		underline.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				isUnderlineChanged = true;
				previewLabel.setUnderline( underline.getSelection( ) );
				previewLabel.redraw( );
			}
		} );
		underline.setToolTipText( Messages.getString( "HighlightRuleBuilderDialog.tooltip.Underline" ) ); //$NON-NLS-1$

		linethrough = createToggleButton( fstyle );
		linethrough.setImage( ReportPlatformUIImages.getImage( AttributeConstant.TEXT_LINE_THROUGH ) );
		linethrough.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				isLinethroughChanged = true;
				previewLabel.setLinethrough( linethrough.getSelection( ) );
				previewLabel.redraw( );
			}
		} );
		linethrough.setToolTipText( Messages.getString( "HighlightRuleBuilderDialog.tooltip.Text_Line_Through" ) ); //$NON-NLS-1$

		lb = new Label( styleGroup, 0 );
		lb.setText( Messages.getString( "HighlightRuleBuilderDialog.text.BackgroundColor" ) ); //$NON-NLS-1$
		lb.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_END ) );

		backColor = new ColorBuilder( styleGroup, 0 );
		gdata = new GridData( );
		gdata.widthHint = 100;
		backColor.setLayoutData( gdata );
		backColor.setChoiceSet( ChoiceSetFactory.getElementChoiceSet( ReportDesignConstants.STYLE_ELEMENT,
				StyleHandle.BACKGROUND_COLOR_PROP ) );
		backColor.setRGB( null );
		backColor.addListener( SWT.Modify, new Listener( ) {

			public void handleEvent( Event event )
			{
				previewLabel.setBackground( ColorManager.getColor( backColor.getRGB( ) ) );
				previewLabel.redraw( );
			}
		} );

		createDummy( styleGroup, 3 );

		Composite preview = new Composite( styleGroup, SWT.NONE );
		GridLayout glayout = new GridLayout( );
		preview.setLayout( glayout );
		gdata = new GridData( GridData.FILL_BOTH );
		gdata.horizontalSpan = 5;
		preview.setLayoutData( gdata );

		lb = new Label( preview, SWT.NONE );
		lb.setText( Messages.getString( "HighlightRuleBuilderDialog.text.Preview" ) ); //$NON-NLS-1$

		Composite previewPane = new Composite( preview, SWT.BORDER );
		glayout = new GridLayout( );
		glayout.marginWidth = 0;
		glayout.marginHeight = 0;
		previewPane.setLayout( glayout );
		gdata = new GridData( GridData.FILL_BOTH );
		gdata.heightHint = 60;
		previewPane.setLayoutData( gdata );

		previewLabel = new PreviewLabel( previewPane, 0 );
		previewLabel.setText( Messages.getString( "HighlightRuleBuilderDialog.text.PreviewContent" ) ); //$NON-NLS-1$
		gdata = new GridData( GridData.FILL_BOTH );
		previewLabel.setLayoutData( gdata );

		updatePreview( );

		return styleGroup;
	}

	private Listener textModifyListener = new Listener( ) {

		public void handleEvent( Event event )
		{
			updateButtons( );
		}
	};

	private void refreshList( )
	{
		ArrayList finalItems = new ArrayList( 10 );
		for ( int n = 0; n < actions.length; n++ )
		{
			finalItems.add( actions[n] );
		}

		popupItems = (String[]) finalItems.toArray( EMPTY_ARRAY );
	}

	private List getSelectValueList( ) throws BirtException
	{
		List selectValueList = new ArrayList( );
		ReportItemHandle reportItem = DEUtil.getBindingHolder( currentItem );
		if ( bindingName != null && reportItem != null )
		{
			if ( reportItem instanceof ExtendedItemHandle )
			{
				Object obj = ElementAdapterManager.getAdapters( reportItem,
						IUseCubeQueryList.class );

				if ( obj instanceof Object[] )
				{
					Object arrays[] = (Object[]) obj;
					if ( arrays.length == 1 && arrays[0] != null )
					{
						List valueList = ( (IUseCubeQueryList) arrays[0] ).getQueryList( getExpression( ),
								(ExtendedItemHandle) reportItem );
						selectValueList.addAll( valueList );
					}
				}

			}

			if ( selectValueList.size( ) == 0 )
			{
				selectValueList = SelectValueFetcher.getSelectValueList( getExpression( ),
						reportItem.getDataSet( ),
						false );
			}

		}
		else
		{
			ExceptionHandler.openErrorMessageBox( Messages.getString( "SelectValueDialog.errorRetrievinglist" ), Messages.getString( "SelectValueDialog.noExpressionSet" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return selectValueList;
	}

	// private Composite createTitleArea( Composite parent )
	// {
	// int heightMargins = 3;
	// int widthMargins = 8;
	// final Composite titleArea = new Composite( parent, SWT.NONE );
	// GridLayout layout = new GridLayout( );
	// layout.marginHeight = heightMargins;
	// layout.marginWidth = widthMargins;
	// titleArea.setLayout( layout );
	//
	// Display display = parent.getDisplay( );
	// Color background = JFaceColors.getBannerBackground( display );
	// Color foreground = JFaceColors.getBannerForeground(display);
	// GridData layoutData = new GridData( GridData.FILL_HORIZONTAL );
	// layoutData.heightHint = 60 + ( heightMargins * 2 );
	// titleArea.setLayoutData( layoutData );
	// titleArea.setBackground( background );
	//
	// titleArea.addPaintListener( new PaintListener( ) {
	//
	// public void paintControl( PaintEvent e )
	// {
	// e.gc.setForeground( titleArea.getDisplay( )
	// .getSystemColor( SWT.COLOR_WIDGET_NORMAL_SHADOW ) );
	// Rectangle bounds = titleArea.getClientArea( );
	// bounds.height = bounds.height - 2;
	// bounds.width = bounds.width - 1;
	// e.gc.drawRectangle( bounds );
	// }
	// } );
	//
	//		
	// Label label = new Label( titleArea, SWT.WRAP);
	// label.setBackground( background );
	// label.setFont( FontManager.getFont( label.getFont( ).toString( ),
	// 10,
	// SWT.BOLD ) );
	//		label.setText( Messages.getString( "HighlightRuleBuilderDialog.text.Title" ) ); //$NON-NLS-1$
	// GridData gd = new GridData( );
	// label.setLayoutData( gd );
	//		
	// Text messageLabel = new Text(titleArea, SWT.WRAP | SWT.READ_ONLY);
	// JFaceColors.setColors(messageLabel, foreground, background);
	//		messageLabel.setText(dlgDescription); // two lines//$NON-NLS-1$
	// messageLabel.setFont(JFaceResources.getDialogFont());
	//		
	// // Label description = new Label( titleArea, SWT.WRAP );
	// // description.setBackground( background );
	// // description.setFont( FontManager.getFont( description.getFont( )
	// // .toString( ), 8, SWT.NONE ) );
	// // description.setText( dlgDescription );
	// // GridData data = new GridData( );
	// // data.horizontalIndent = 5;
	// // data.verticalIndent = 5;
	// // description.setLayoutData( data );
	//
	// UIUtil.bindHelp( parent, IHelpContextIds.HIGHLIGHT_RULE_BUILDER_ID );
	// return titleArea;
	// }

	private Composite createDummy( Composite parent, int colSpan )
	{
		Composite dummy = new Composite( parent, SWT.NONE );
		GridData gdata = new GridData( );
		gdata.widthHint = 22;
		gdata.horizontalSpan = colSpan;
		gdata.heightHint = 10;
		dummy.setLayoutData( gdata );

		return dummy;
	}

	private Button createToggleButton( Composite parent )
	{
		Composite wrapper = new Composite( parent, 0 );
		GridLayout layout = new GridLayout( );
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		wrapper.setLayout( layout );

		Button btn = new Button( wrapper, SWT.TOGGLE );
		GridData gdata = new GridData( );
		gdata.widthHint = 22;
		gdata.heightHint = 22;
		btn.setLayoutData( gdata );

		return btn;
	}

	private void updatePreview( )
	{
		if ( stylesChooser.getText( ).equals( NONE_DISPLAY_TEXT ) )
		{
			String familyValue = getFontFamily( );
			int sizeValue = getFontSize( );

			previewLabel.setFontFamily( familyValue );
			previewLabel.setFontSize( sizeValue );
			previewLabel.setBold( bold.getSelection( ) );
			previewLabel.setItalic( italic.getSelection( ) );
			previewLabel.setForeground( ColorManager.getColor( color.getRGB( ) ) );
			previewLabel.setBackground( ColorManager.getColor( backColor.getRGB( ) ) );
			previewLabel.setUnderline( underline.getSelection( ) );
			previewLabel.setLinethrough( linethrough.getSelection( ) );
			previewLabel.setOverline( false );

			previewLabel.updateView( );;
		}
		else
		{
			StyleHandle style = (StyleHandle) styles.get( stylesChooser.getText( ) );

			String familyValue = DEUtil.RemoveQuote( style.getFontFamilyHandle( )
					.getStringValue( ) );
			int sizeValue = DEUtil.getFontSize( style.getFontSize( )
					.getDisplayValue( ) );
			previewLabel.setFontFamily( familyValue );
			previewLabel.setFontSize( sizeValue );
			previewLabel.setForeground( ColorManager.getColor( style.getColor( )
					.getRGB( ) ) );
			previewLabel.setBackground( ColorManager.getColor( style.getBackgroundColor( )
					.getRGB( ) ) );

			if ( style.getFontWeight( )
					.equals( DesignChoiceConstants.FONT_WEIGHT_BOLD ) )
			{
				previewLabel.setBold( true );
			}
			else
			{
				previewLabel.setBold( false );
			}
			if ( style.getFontStyle( )
					.equals( DesignChoiceConstants.FONT_STYLE_ITALIC ) )
			{
				previewLabel.setItalic( true );
			}
			else
			{
				previewLabel.setItalic( false );
			}
			if ( style.getTextUnderline( )
					.equals( DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE ) )
			{
				previewLabel.setUnderline( true );
			}
			else
			{
				previewLabel.setUnderline( false );
			}
			if ( style.getTextLineThrough( )
					.equals( DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH ) )
			{
				previewLabel.setLinethrough( true );
			}
			else
			{
				previewLabel.setLinethrough( false );
			}
			if ( style.getTextOverline( )
					.equals( DesignChoiceConstants.TEXT_OVERLINE_OVERLINE ) )
			{
				previewLabel.setOverline( true );
			}
			else
			{
				previewLabel.setOverline( false );
			}

			previewLabel.updateView( );
		}
	}

	/**
	 * For newly create highlight rule, transfer the handle as <b>null </b>, or
	 * transfer the handle as current highlight rule handle to be modified.
	 * 
	 * @param handle
	 * @param handleCount
	 *            current highlight rule items count.
	 */
	public void updateHandle( HighlightRuleHandle handle, int handleCount )
	{
		this.handle = handle;
		this.handleCount = handleCount;

	}

	/*
	 * Set design handle for HighlightRule builder
	 */
	public void setDesignHandle( DesignElementHandle handle )
	{
		this.designHandle = handle;
		initializeProviderType( );
		inilializeColumnList( handle );
		initializeParamterBinding( handle );
		initilizeDlgDescription( handle );
		exprControlType = getHighlightExpCtrType( this.designHandle );
	}

	protected void inilializeColumnList( DesignElementHandle handle )
	{
		columnList = DEUtil.getVisiableColumnBindingsList( handle );
	}

	private void initializeParamterBinding( DesignElementHandle handle )
	{
		if ( handle instanceof ReportItemHandle )
		{
			ReportItemHandle inputHandle = (ReportItemHandle) handle;
			List list = new ArrayList( );
			for ( Iterator iterator = inputHandle.paramBindingsIterator( ); iterator.hasNext( ); )
			{
				ParamBindingHandle paramBindingHandle = (ParamBindingHandle) iterator.next( );
				list.add( paramBindingHandle );
			}
			bindingParams = new ParamBindingHandle[list.size( )];
			list.toArray( bindingParams );
		}
	}

	/**
	 * Returns current highlight rule handle, this handle can be newly created
	 * or set by the updateHandle method.
	 * 
	 * @return highlight rule handle.
	 */
	public HighlightRuleHandle getHandle( )
	{
		return handle;
	}

	private Text createText( Composite parent )
	{
		Text txt = new Text( parent, SWT.BORDER );
		GridData gdata = new GridData( GridData.FILL_HORIZONTAL );
		gdata.widthHint = 100;
		txt.setLayoutData( gdata );

		return txt;
	}

	private void fillStyles( Combo stylesChooser )
	{
		stylesChooser.removeAll( );
		styles.clear( );
		stylesChooser.add( NONE_DISPLAY_TEXT );
		for ( Iterator iter = DEUtil.getStyles( ); iter.hasNext( ); )
		{
			StyleHandle styleHandle = (StyleHandle) iter.next( );
			if ( styleHandle.isPredefined( ) )
			{
				continue;
			}
			String styleName = styleHandle.getName( );
			stylesChooser.add( styleName );
			styles.put( styleName, styleHandle );
		}

		if ( handle != null && handle.getStyle( ) != null )
		{
			stylesChooser.setText( handle.getStyle( ).getName( ) );
		}
		else
		{
			stylesChooser.setText( NONE_DISPLAY_TEXT );
		}
	}

	private void fillExpression( Combo control )
	{
		String te = "";//$NON-NLS-1$

		if ( handle != null )
		{
			te = handle.getTestExpression( );
		}

		if ( designHandle instanceof DataItemHandle
				&& ( (DataItemHandle) designHandle ).getResultSetColumn( ) != null )
		{
			control.add( VALUE_OF_THIS_DATA_ITEM );
		}

		if ( control.getItemCount( ) == 0 )
		{
			control.add( DEUtil.resolveNull( null ) );
			control.select( control.getItemCount( ) - 1 );
		}

	}

	/**
	 * Refreshes the OK button state.
	 * 
	 */
	protected void updateButtons( )
	{
		enableInput( isExpressionOK( ) );

		if ( getButton( IDialogConstants.OK_ID ) != null )
		{
			getButton( IDialogConstants.OK_ID ).setEnabled( isConditionOK( ) );
		}

	}

	private void enableInput( boolean val )
	{
		boolean val2 = val;

		stylesChooser.setEnabled( val );
		if ( ( !stylesChooser.getText( ).equals( NONE_DISPLAY_TEXT ) )
				|| ( stylesChooser.isEnabled( ) == false ) )
		{
			val2 = false;
		}
		font.setEnabled( val2 );
		size.setEnabled( val2 );
		color.setEnabled( val2 );
		bold.setEnabled( val2 );
		italic.setEnabled( val2 );
		underline.setEnabled( val2 );
		linethrough.setEnabled( val2 );
		backColor.setEnabled( val2 );

		operator.setEnabled( val );

		if ( valueVisible != 3 )
		{
			if ( expressionValue1 != null && ( !expressionValue1.isDisposed( ) ) )
				expressionValue1.setEnabled( val );
			if ( expressionValue2 != null && ( !expressionValue2.isDisposed( ) ) )
				expressionValue2.setEnabled( val );
			if ( andLable != null && ( !andLable.isDisposed( ) ) )
			{
				andLable.setEnabled( val );
			}
		}
		else
		{
			setControlEnable( valueListComposite, val );
			if ( val )
			{
				checkAddButtonStatus( );
				checkEditDelButtonStatus( );
			}
		}
	}

	/**
	 * Gets if the expression field is not empty.
	 */
	private boolean isExpressionOK( )
	{
		if ( getExpressionControl( ) == null )
		{
			return false;
		}

		if ( getExpression( ) == null || getExpression( ).length( ) == 0 )
		{
			return false;
		}

		return true;
	}

	/**
	 * Gets if the condition is available.
	 */
	private boolean isConditionOK( )
	{
		if ( getExpressionControl( ) == null )
		{
			return false;
		}

		if ( !isExpressionOK( ) )
		{
			return false;
		}

		return checkValues( );
	}

	/**
	 * Gets if the values of the condition is(are) available.
	 */
	private boolean checkValues( )
	{
		if ( valueVisible == 3 )
		{
			if ( valueList.size( ) <= 0 )
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			if ( expressionValue1.getVisible( ) )
			{
				if ( expressionValue1.getText( ) == null
						|| expressionValue1.getText( ).length( ) == 0 )
				{
					return false;
				}
			}

			if ( expressionValue2.getVisible( ) )
			{
				if ( expressionValue2.getText( ) == null
						|| expressionValue2.getText( ).length( ) == 0 )
				{
					return false;
				}
			}
		}

		return true;
	}

	private void removeLocalStyleProperties( )
	{
		try
		{
			handle.getFontFamilyHandle( ).setStringValue( null );
			handle.getFontSize( ).setStringValue( null );
			handle.getColor( ).setValue( null );
			handle.getBackgroundColor( ).setValue( null );
			handle.setFontStyle( null );
			handle.setFontWeight( null );
			handle.setTextUnderline( null );
			handle.setTextLineThrough( null );
		}
		catch ( Exception e )
		{
			WidgetUtil.processError( getShell( ), e );
		}
	}

	/**
	 * SYNC the control value according to the handle.
	 */
	private void syncViewProperties( )
	{
		// expression.setText( DEUtil.resolveNull( provider.getTestExpression( )
		// ) );

		if ( handle != null )
		{
			// syn high light test expression from high light rule handle.
			setExpression( DEUtil.resolveNull( handle.getTestExpression( ) ) );
			operator.select( getIndexForOperatorValue( handle.getOperator( ) ) );

			String value = getValueForOperator( operator.getText( ) );
			valueVisible = determineValueVisible( value );
			if ( valueVisible == 3 )
			{
				createValueListComposite( operator.getParent( ) );
				if ( handle != null )
				{
					valueList = new ArrayList( handle.getValue1List( ) );
				}

				tableViewer.setInput( valueList );
			}
			else
			{
				create2ValueComposite( operator.getParent( ) );
				if ( handle != null )
				{
					expressionValue1.setText( DEUtil.resolveNull( handle.getValue1( ) ) );
					expressionValue2.setText( DEUtil.resolveNull( handle.getValue2( ) ) );
				}

			}

			if ( valueVisible == 0 )
			{
				expressionValue1.setVisible( false );
				expressionValue2.setVisible( false );
				andLable.setVisible( false );
			}
			else if ( valueVisible == 1 )
			{
				expressionValue1.setVisible( true );
				expressionValue2.setVisible( false );
				andLable.setVisible( false );
			}
			else if ( valueVisible == 2 )
			{
				expressionValue1.setVisible( true );
				expressionValue2.setVisible( true );
				andLable.setVisible( true );
			}
		}

		syncFamily( );
		syncSize( );

		if ( handle != null && handle.getStyle( ) == null )
		{
			if ( handle.getColor( ).isSet( ) )
			{
				color.setRGB( DEUtil.getRGBValue( handle.getColor( ).getRGB( ) ) );
			}
			if ( handle.getBackgroundColor( ).isSet( ) )
			{
				backColor.setRGB( DEUtil.getRGBValue( handle.getBackgroundColor( )
						.getRGB( ) ) );
			}
			bold.setSelection( DesignChoiceConstants.FONT_WEIGHT_BOLD.equals( handle.getFontWeight( ) ) );
			italic.setSelection( DesignChoiceConstants.FONT_STYLE_ITALIC.equals( handle.getFontStyle( ) ) );
			underline.setSelection( DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE.equals( handle.getTextUnderline( ) ) );
			linethrough.setSelection( DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH.equals( handle.getTextLineThrough( ) ) );
		}
	}

	private void syncFamily( )
	{
		if ( handle != null && handle.getStyle( ) == null )
		{
			String fm = DEUtil.RemoveQuote( handle.getFontFamilyHandle( )
					.getDisplayValue( ) );

			if ( innerSyncFamily( fm ) )
			{
				return;
			}
		}
	}

	private boolean innerSyncFamily( String fm )
	{
		String[] items = font.getItems( );

		// int idx = getChoiceValueIndex( cs, fm );

		int idx = getSelectionIndex( fm, items );

		if ( idx >= 0 )
		{
			font.select( idx );

			return true;
		}

		if ( SYSTEM_FONT_LIST != null && SYSTEM_FONT_LIST.length > 0 )
		{
			for ( int i = 0; i < SYSTEM_FONT_LIST.length; i++ )
			{
				if ( SYSTEM_FONT_LIST[i].equals( fm ) )
				{
					font.select( items.length + i );

					return true;
				}
			}
		}

		return false;
	}

	private int getSelectionIndex( String fm, String[] items )
	{
		for ( int i = 0; i < items.length; i++ )
		{
			if ( items[i].equalsIgnoreCase( fm ) )
			{
				return i;
			}
		}

		return 0;
	}

	private void syncSize( )
	{
		if ( handle != null && handle.getStyle( ) == null )
		{
			size.setFontSizeValue( handle.getFontSize( ).getStringValue( ) );
		}
	}

	// private int getChoiceValueIndex( IChoiceSet cs, String value )
	// {
	// IChoice[] cis = cs.getChoices( );
	//
	// for ( int i = 0; i < cis.length; i++ )
	// {
	// if ( cis[i].getName( ).equals( value ) )
	// {
	// return i;
	// }
	// }
	//
	// return -1;
	// }

	private String getFontFamily( )
	{
		String rfm = getRawFontFamily( );

		if ( rfm == null )
		{
			if ( designHandle != null )
			{
				if ( designHandle instanceof StyleHandle )
				{
					rfm = ( (StyleHandle) designHandle ).getFontFamilyHandle( )
							.getStringValue( );
				}
				else
				{
					rfm = designHandle.getPrivateStyle( )
							.getFontFamilyHandle( )
							.getStringValue( );
				}
			}
			else
			{
				rfm = DesignChoiceConstants.FONT_FAMILY_SERIF;
			}
		}

		return HighlightHandleProvider.getFontFamily( rfm );
	}

	private String getRawFontFamily( )
	{
		String ftName = font.getText( );

		IChoiceSet cs = (IChoiceSet) font.getData( );
		IChoice ci = cs.findChoiceByDisplayName( ftName );

		if ( ci != null )
		{
			return ci.getName( );
		}

		if ( SYSTEM_FONT_LIST != null && SYSTEM_FONT_LIST.length > 0 )
		{
			for ( int i = 0; i < SYSTEM_FONT_LIST.length; i++ )
			{
				if ( SYSTEM_FONT_LIST[i].equals( ftName ) )
				{
					// return DEUtil.AddQuote( ftName );
					return ftName;
				}
			}
		}

		return null;
	}

	private int getFontSize( )
	{
		String rfs = getRawFontSize( );

		if ( rfs == null && designHandle != null )
		{
			if ( designHandle instanceof StyleHandle )
			{
				rfs = ( (StyleHandle) designHandle ).getFontSize( )
						.getStringValue( );

			}
			else
			{
				rfs = designHandle.getPrivateStyle( )
						.getFontSize( )
						.getStringValue( );
			}
		}

		return DEUtil.getFontSize( rfs );
	}

	private String getRawFontSize( )
	{
		return size.getFontSizeValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed( )
	{
		try
		{
			String familyValue = getRawFontFamily( );

			String sizeValue = getRawFontSize( );

			int colorValue = DEUtil.getRGBInt( color.getRGB( ) );
			int backColorValue = DEUtil.getRGBInt( backColor.getRGB( ) );
			String italicValue = italic.getSelection( ) ? DesignChoiceConstants.FONT_STYLE_ITALIC
					: DesignChoiceConstants.FONT_STYLE_NORMAL;
			String weightValue = bold.getSelection( ) ? DesignChoiceConstants.FONT_WEIGHT_BOLD
					: DesignChoiceConstants.FONT_WEIGHT_NORMAL;
			String underlineValue = underline.getSelection( ) ? DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE
					: DesignChoiceConstants.TEXT_UNDERLINE_NONE;
			String lingthroughValue = linethrough.getSelection( ) ? DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH
					: DesignChoiceConstants.TEXT_LINE_THROUGH_NONE;

			// provider.setTestExpression( DEUtil.resolveNull(
			// expression.getText( ) ) );

			if ( handle == null )
			{
				HighlightRule rule = StructureFactory.createHighlightRule( );

				rule.setProperty( HighlightRule.OPERATOR_MEMBER,
						DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );
				if ( valueVisible == 3 )
				{
					rule.setValue1( valueList );
					rule.setValue2( "" ); //$NON-NLS-1$
				}
				else
				{
					if ( expressionValue1.isVisible( ) )
					{
						rule.setProperty( MapRule.VALUE1_MEMBER,
								DEUtil.resolveNull( expressionValue1.getText( ) ) );
					}
					if ( expressionValue2.isVisible( ) )
					{
						rule.setProperty( MapRule.VALUE2_MEMBER,
								DEUtil.resolveNull( expressionValue2.getText( ) ) );
					}
				}
				// set test expression into highlight rule.
				rule.setTestExpression( DEUtil.resolveNull( getExpression( ) ) );

				// Set referenced style of the highlight rule.
				if ( !stylesChooser.getText( ).equals( NONE_DISPLAY_TEXT ) )
				{
					rule.setStyle( (StyleHandle) styles.get( stylesChooser.getText( ) ) );
				}
				else
				{
					/**
					 * Sets our necessary style properties.
					 */
					if ( color.getRGB( ) != null )
					{
						rule.setProperty( HighlightRule.COLOR_MEMBER,
								new Integer( colorValue ) );
					}
					if ( backColor.getRGB( ) != null )
					{
						rule.setProperty( HighlightRule.BACKGROUND_COLOR_MEMBER,
								new Integer( backColorValue ) );
					}
					if ( familyValue != null )
					{
						rule.setProperty( HighlightRule.FONT_FAMILY_MEMBER,
								familyValue );
					}
					if ( sizeValue != null )
					{
						rule.setProperty( HighlightRule.FONT_SIZE_MEMBER,
								sizeValue );
					}
					if ( isItalicChanged )
					{
						rule.setProperty( HighlightRule.FONT_STYLE_MEMBER,
								italicValue );
					}
					if ( isBoldChanged )
					{
						rule.setProperty( HighlightRule.FONT_WEIGHT_MEMBER,
								weightValue );
					}
					if ( isLinethroughChanged )
					{
						rule.setProperty( HighlightRule.TEXT_LINE_THROUGH_MEMBER,
								lingthroughValue );
					}
					if ( isUnderlineChanged )
					{
						rule.setProperty( HighlightRule.TEXT_UNDERLINE_MEMBER,
								underlineValue );
					}
				}
				handle = provider.doAddItem( rule, handleCount );
			}
			else
			{
				// set test expression into highlight rule.
				handle.setTestExpression( DEUtil.resolveNull( getExpression( ) ) );
				handle.setOperator( DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );

				if ( valueVisible != 3 )
				{
					if ( expressionValue1.isVisible( ) )
					{
						handle.setValue1( DEUtil.resolveNull( expressionValue1.getText( ) ) );
					}
					else
					{
						handle.setValue1( NULL_STRING );
					}
					if ( expressionValue2.isVisible( ) )
					{
						handle.setValue2( DEUtil.resolveNull( expressionValue2.getText( ) ) );
					}
					else
					{
						handle.setValue2( NULL_STRING );
					}
				}
				else
				{
					handle.setValue1( valueList );
					handle.setValue2( "" ); //$NON-NLS-1$
				}

				if ( !stylesChooser.getText( ).equals( NONE_DISPLAY_TEXT ) )
				{
					if ( handle.getStyle( ) == null )
					{
						handle.setStyle( (StyleHandle) styles.get( stylesChooser.getText( ) ) );
					}
					else if ( !stylesChooser.getText( )
							.equals( handle.getStyle( ).getName( ) ) )
					{
						handle.setStyle( (StyleHandle) styles.get( stylesChooser.getText( ) ) );
					}
					removeLocalStyleProperties( );
				}
				else
				{
					handle.setStyle( null );

					handle.getFontFamilyHandle( )
							.setStringValue( DEUtil.resolveNull( familyValue ) );
					handle.getFontSize( )
							.setStringValue( DEUtil.resolveNull( sizeValue ) );
					if ( color.getRGB( ) != null )
					{
						handle.getColor( ).setRGB( colorValue );
					}
					else
					{
						handle.getColor( ).setValue( null );
					}
					if ( backColor.getRGB( ) != null )
					{
						handle.getBackgroundColor( ).setRGB( backColorValue );
					}
					else
					{
						handle.getBackgroundColor( ).setValue( null );
					}
					if ( isItalicChanged )
					{
						handle.setFontStyle( italicValue );
					}
					if ( isBoldChanged )
					{
						handle.setFontWeight( weightValue );
					}
					if ( isUnderlineChanged )
					{
						handle.setTextUnderline( underlineValue );
					}
					if ( isLinethroughChanged )
					{
						handle.setTextLineThrough( lingthroughValue );
					}
				}
			}
		}
		catch ( Exception e )
		{
			WidgetUtil.processError( getShell( ), e );
		}

		super.okPressed( );
	}

	private void editValue( Object control )
	{
		String initValue = null;
		if ( control instanceof Text )
		{
			initValue = ( (Text) control ).getText( );
		}
		else if ( control instanceof Combo )
		{
			initValue = ( (Combo) control ).getText( );
		}
		else if ( control instanceof ExpressionValue )
		{
			initValue = ( (ExpressionValue) control ).getText( );
		}
		ExpressionBuilder expressionBuilder = new ExpressionBuilder( getShell( ),
				initValue );

		if ( designHandle != null )
		{
			ExpressionProvider expressionProvider = new ExpressionProvider( designHandle );
			expressionProvider.addFilter( new ExpressionFilter( ) {

				public boolean select( Object parentElement, Object element )
				{
					if ( ExpressionFilter.CATEGORY.equals( parentElement )
							&& ExpressionProvider.CURRENT_CUBE.equals( element ) )
					{
						return false;
					}
					return true;
				}

			} );
			expressionBuilder.setExpressionProvier( expressionProvider );
		}

		if ( expressionBuilder.open( ) == OK )
		{
			String result = DEUtil.resolveNull( expressionBuilder.getResult( ) );
			if ( control instanceof Text )
			{
				( (Text) control ).setText( result );
			}
			else if ( control instanceof Combo )
			{
				( (Combo) control ).setText( result );
			}
		}
		updateButtons( );
	}

	protected String getExpression( String resultSet )
	{
		if ( provider.getExpressionType( ) == HighlightHandleProvider.EXPRESSION_TYPE_ROW )
		{
			return DEUtil.getColumnExpression( resultSet );
		}
		else if ( provider.getExpressionType( ) == HighlightHandleProvider.EXPRESSION_TYPE_DATA )
		{
			return DEUtil.getDataExpression( resultSet );
		}

		return null;

	}

	private void initializeProviderType( )
	{
		if ( designHandle instanceof DataItemHandle )
		{
			DataItemHandle dataItem = (DataItemHandle) designHandle;
			if ( dataItem.getContainer( ) instanceof ExtendedItemHandle )
			{
				provider.setExpressionType( HighlightHandleProvider.EXPRESSION_TYPE_DATA );
			}
			else
			{
				provider.setExpressionType( HighlightHandleProvider.EXPRESSION_TYPE_ROW );
			}
		}
	}

	protected int create2ValueComposite( Composite condition )
	{
		if ( expressionValue1 != null && !expressionValue1.isDisposed( ) )
		{
			return 0;
		}
		disposeComposites( );

		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		expressionValue1 = new ValueCombo( condition, SWT.NONE );
		compositeList.add( expressionValue1 );
		expressionValue1.setLayoutData( gd );

		expressionValue1.setItems( popupItems );

		expressionValue1.addListener( SWT.Modify, textModifyListener );
		// expressionValue1.addListener( SWT.Selection, popBtnSelectionListener
		// );
		expressionValue1.addSelectionListener( 0, selectValueAction );
		expressionValue1.addSelectionListener( 1, expValueAction );

		Composite dummy = createDummy( condition, 3 );
		compositeList.add( dummy );

		andLable = new Label( condition, SWT.NONE );
		andLable.setText( Messages.getString( "HighlightRuleBuilderDialog.text.AND" ) ); //$NON-NLS-1$
		andLable.setVisible( false );
		compositeList.add( andLable );

		dummy = createDummy( condition, 3 );
		compositeList.add( dummy );

		expressionValue2 = new ValueCombo( condition, SWT.NONE );
		expressionValue2.setLayoutData( gd );
		compositeList.add( expressionValue2 );

		expressionValue2.setItems( popupItems );

		expressionValue2.addListener( SWT.Modify, textModifyListener );
		// expressionValue2.addListener( SWT.Selection, popBtnSelectionListener
		// );
		expressionValue2.addSelectionListener( 0, selectValueAction );
		expressionValue2.addSelectionListener( 1, expValueAction );
		expressionValue2.setVisible( false );
		if ( operator.getItemCount( ) > 0
				&& operator.getSelectionIndex( ) == -1 )
		{
			operator.select( 0 );
		}

		condition.getParent( ).layout( true, true );
		return 1;
	}

	private void disposeComposites( )
	{
		if ( compositeList.size( ) > 0 )
		{
			int count = compositeList.size( );
			for ( int i = 0; i < count; i++ )
			{
				Object obj = compositeList.get( i );
				if ( obj != null
						&& obj instanceof Widget
						&& ( !( (Widget) obj ).isDisposed( ) ) )
				{
					( (Widget) obj ).dispose( );
				}
			}
		}
		compositeList.clear( );
	}

	private int createValueListComposite( Composite parent )
	{
		if ( valueListComposite != null && !valueListComposite.isDisposed( ) )
		{
			return 0;
		}
		disposeComposites( );

		valueListComposite = new Composite( parent, SWT.NONE );
		GridData gdata = new GridData( GridData.FILL_HORIZONTAL );
		gdata.horizontalSpan = 4;
		valueListComposite.setLayoutData( gdata );
		GridLayout layout = new GridLayout( );
		layout.numColumns = 4;
		valueListComposite.setLayout( layout );

		compositeList.add( valueListComposite );

		Group group = new Group( valueListComposite, SWT.NONE );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		data.heightHint = 118;
		data.horizontalSpan = 3;
		data.horizontalIndent = 0;
		data.grabExcessHorizontalSpace = true;
		group.setLayoutData( data );
		layout = new GridLayout( );
		layout.numColumns = 4;
		group.setLayout( layout );

		new Label( group, SWT.NONE ).setText( Messages.getString( "FilterConditionBuilder.label.value" ) ); //$NON-NLS-1$

		GridData expgd = new GridData( );
		expgd.widthHint = 100;

		addExpressionValue = new MultiValueCombo( group, SWT.NONE );
		addExpressionValue.setLayoutData( expgd );

		addBtn = new Button( group, SWT.PUSH );
		addBtn.setText( Messages.getString( "FilterConditionBuilder.button.add" ) ); //$NON-NLS-1$
		addBtn.setToolTipText( Messages.getString( "FilterConditionBuilder.button.add.tooltip" ) ); //$NON-NLS-1$
		setButtonLayoutData( addBtn );
		addBtn.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub

			}

			public void widgetSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub
				String value = addExpressionValue.getText( ).trim( );
				if ( valueList.indexOf( value ) < 0 )
				{
					valueList.add( value );
					tableViewer.refresh( );
					updateButtons( );
					addExpressionValue.setFocus( );
					addExpressionValue.setText( "" ); //$NON-NLS-1$
				}
				else
				{
					addBtn.setEnabled( false );
				}

			}
		} );

		new Label( group, SWT.NONE );

		int tableStyle = SWT.SINGLE
				| SWT.BORDER
				| SWT.H_SCROLL
				| SWT.V_SCROLL
				| SWT.FULL_SELECTION;
		table = new Table( group, tableStyle );
		data = new GridData( GridData.FILL_BOTH );
		data.horizontalSpan = 4;
		table.setLayoutData( data );

		table.setHeaderVisible( false );
		table.setLinesVisible( true );
		TableColumn column;
		int i;
		String[] columNames = new String[]{
			Messages.getString( "FilterConditionBuilder.list.item1" ), //$NON-NLS-1$
		};
		int[] columLength = new int[]{
			484
		};
		for ( i = 0; i < columNames.length; i++ )
		{
			column = new TableColumn( table, SWT.NONE, i );
			column.setText( columNames[i] );
			column.setWidth( columLength[i] );
		}
		table.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub
			}

			public void widgetSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub
				checkEditDelButtonStatus( );
			}
		} );

		table.addKeyListener( new KeyListener( ) {

			public void keyPressed( KeyEvent e )
			{
				// TODO Auto-generated method stub
				if ( e.keyCode == SWT.DEL )
				{
					int index = table.getSelectionIndex( );
					if ( index > -1 )
					{
						valueList.remove( index );
						tableViewer.refresh( );
						if ( valueList.size( ) > 0 )
						{
							if ( valueList.size( ) <= index )
							{
								index = index - 1;
							}
							table.select( index );
						}
						updateButtons( );
					}
					else
					{
						delBtn.setEnabled( false );
					}
				}

			}

			public void keyReleased( KeyEvent e )
			{
				// TODO Auto-generated method stub

			}

		} );
		table.addMouseListener( new MouseAdapter( ) {

			public void mouseDoubleClick( MouseEvent e )
			{
				editTableValue( );
			}
		} );

		tableViewer = new TableViewer( table );
		tableViewer.setUseHashlookup( true );
		tableViewer.setColumnProperties( columNames );
		tableViewer.setLabelProvider( tableLableProvier );
		tableViewer.setContentProvider( tableContentProvider );

		Composite rightPart = new Composite( valueListComposite, SWT.NONE );
		data = new GridData( GridData.HORIZONTAL_ALIGN_BEGINNING
				| GridData.VERTICAL_ALIGN_END );
		rightPart.setLayoutData( data );
		layout = new GridLayout( );
		layout.makeColumnsEqualWidth = true;
		rightPart.setLayout( layout );

		editBtn = new Button( rightPart, SWT.PUSH );
		editBtn.setText( Messages.getString( "FilterConditionBuilder.button.edit" ) ); //$NON-NLS-1$
		editBtn.setToolTipText( Messages.getString( "FilterConditionBuilder.button.edit.tooltip" ) ); //$NON-NLS-1$
		setButtonLayoutData( editBtn );
		editBtn.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub

			}

			public void widgetSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub
				editTableValue( );
			}

		} );

		delBtn = new Button( rightPart, SWT.PUSH );
		delBtn.setText( Messages.getString( "FilterConditionBuilder.button.delete" ) ); //$NON-NLS-1$
		delBtn.setToolTipText( Messages.getString( "FilterConditionBuilder.button.delete.tooltip" ) ); //$NON-NLS-1$
		setButtonLayoutData( delBtn );
		delBtn.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub

			}

			public void widgetSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub
				int index = table.getSelectionIndex( );
				if ( index > -1 )
				{
					valueList.remove( index );
					tableViewer.refresh( );
					if ( valueList.size( ) > 0 )
					{
						if ( valueList.size( ) <= index )
						{
							index = index - 1;
						}
						table.select( index );
					}
					updateButtons( );
				}
				else
				{
					delBtn.setEnabled( false );
				}
			}

		} );

		delAllBtn = new Button( rightPart, SWT.PUSH );
		delAllBtn.setText( Messages.getString( "FilterConditionBuilder.button.deleteall" ) ); //$NON-NLS-1$
		delAllBtn.setToolTipText( Messages.getString( "FilterConditionBuilder.button.deleteall.tooltip" ) ); //$NON-NLS-1$
		setButtonLayoutData( delAllBtn );
		delAllBtn.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub

			}

			public void widgetSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub
				int count = valueList.size( );
				if ( count > 0 )
				{
					valueList.clear( );
					tableViewer.refresh( );
					updateButtons( );
				}
				else
				{
					delAllBtn.setEnabled( false );
				}
			}

		} );

		addExpressionValue.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				checkAddButtonStatus( );
				updateButtons( );
			}
		} );

		// addExpressionValue.addListener( SWT.Verify, expValueVerifyListener );
		// addExpressionValue.addListener( SWT.Selection,
		// popBtnSelectionListener );
		addExpressionValue.addSelectionListener( 0, mAddSelValueAction );
		addExpressionValue.addSelectionListener( 1, mAddExpValueAction );
		addExpressionValue.setItems( popupItems );

		parent.getParent( ).layout( true, true );

		return 1;
	}

	protected void checkEditDelButtonStatus( )
	{
		if ( tableViewer == null )
		{
			return;
		}
		boolean enabled = ( tableViewer.getSelection( ) == null ) ? false
				: true;
		if ( enabled == true
				&& tableViewer.getSelection( ) instanceof StructuredSelection )
		{
			StructuredSelection selection = (StructuredSelection) tableViewer.getSelection( );
			if ( selection.toList( ).size( ) <= 0 )
			{
				enabled = false;
			}
		}
		editBtn.setEnabled( enabled );
		delBtn.setEnabled( enabled );

		enabled = table.getItemCount( ) > 0 ? true : false;
		delAllBtn.setEnabled( enabled );

	}

	protected void checkAddButtonStatus( )
	{
		if ( addExpressionValue != null && ( !addExpressionValue.isDisposed( ) ) )
		{
			String value = addExpressionValue.getText( );
			if ( value == null
					|| value.length( ) == 0
					|| value.trim( ).length( ) == 0 )
			{
				addBtn.setEnabled( false );
				return;
			}
			if ( value != null )
			{
				value = value.trim( );
			}
			if ( valueList.indexOf( value ) < 0 )
			{
				addBtn.setEnabled( true );
			}
			else
			{
				addBtn.setEnabled( false );
			}
		}
	}

	protected ITableLabelProvider tableLableProvier = new ITableLabelProvider( ) {

		public Image getColumnImage( Object element, int columnIndex )
		{
			// TODO Auto-generated method stub
			return null;
		}

		public String getColumnText( Object element, int columnIndex )
		{
			// TODO Auto-generated method stub
			if ( columnIndex == 0 )
			{
				return (String) element;
			}
			return ""; //$NON-NLS-1$
		}

		public void addListener( ILabelProviderListener listener )
		{
			// TODO Auto-generated method stub

		}

		public void dispose( )
		{
			// TODO Auto-generated method stub

		}

		public boolean isLabelProperty( Object element, String property )
		{
			// TODO Auto-generated method stub
			return false;
		}

		public void removeListener( ILabelProviderListener listener )
		{
			// TODO Auto-generated method stub

		}
	};

	protected IStructuredContentProvider tableContentProvider = new IStructuredContentProvider( ) {

		public void dispose( )
		{
			// TODO Auto-generated method stub

		}

		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
			// TODO Auto-generated method stub

		}

		public Object[] getElements( Object inputElement )
		{
			// TODO Auto-generated method stub
			if ( inputElement == null )
			{
				return new Object[0];
			}
			else if ( inputElement instanceof List )
			{
				return ( (List) inputElement ).toArray( );
			}
			return null;
		}
	};

	protected void setControlEnable( Control control, boolean bool )
	{
		if ( control == null || control.isDisposed( ) )
		{
			return;
		}
		control.setEnabled( bool );
		Composite tmp = null;
		if ( control instanceof Composite )
		{
			tmp = (Composite) control;
		}
		if ( tmp != null && tmp.getChildren( ) != null )
		{
			for ( int i = 0; i < tmp.getChildren( ).length; i++ )
			{
				setControlEnable( tmp.getChildren( )[i], bool );
			}
		}
	}

	protected ValueCombo.ISelection expValueAction = new ValueCombo.ISelection( ) {

		public String doSelection( String input )
		{
			String retValue = null;

			// TODO Auto-generated method stub
			ExpressionBuilder dialog = new ExpressionBuilder( PlatformUI.getWorkbench( )
					.getDisplay( )
					.getActiveShell( ),
					input );

			if ( expressionProvider == null )
				dialog.setExpressionProvier( new ExpressionProvider( designHandle ) );
			else
				dialog.setExpressionProvier( expressionProvider );

			if ( dialog.open( ) == IDialogConstants.OK_ID )
			{
				retValue = dialog.getResult( );
			}
			return retValue;
		}
	};

	protected ValueCombo.ISelection selectValueAction = new ValueCombo.ISelection( ) {

		public String doSelection( String input )
		{
			String retValue = null;
			// TODO Auto-generated method stub
			for ( Iterator iter = columnList.iterator( ); iter.hasNext( ); )
			{
				String columnName = ( (ComputedColumnHandle) ( iter.next( ) ) ).getName( );

				if ( getExpression( ).equals( VALUE_OF_THIS_DATA_ITEM )
						&& designHandle instanceof DataItemHandle )
				{
					if ( designHandle.getContainer( ) instanceof ExtendedItemHandle )
					{
						if ( DEUtil.getDataExpression( columnName )
								.equals( getExpression( ) ) )
						{
							bindingName = columnName;
							break;
						}
					}
					else
					{
						if ( DEUtil.getColumnExpression( columnName )
								.equals( getExpression( ) ) )
						{
							bindingName = columnName;
							break;
						}
					}

				}
				else
				{
					String value = DEUtil.getExpression( getResultSetColumn( columnName ) );
					if ( value != null && value.equals( getExpression( ) ) )
					{
						bindingName = columnName;
						break;
					}
				}

			}

			if ( bindingName != null )
			{
				try
				{
					List selectValueList = getSelectValueList( );
					if ( selectValueList == null
							|| selectValueList.size( ) == 0 )
					{
						MessageDialog.openInformation( null,
								Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
								Messages.getString( "SelectValueDialog.messages.info.selectVauleUnavailable" ) ); //$NON-NLS-1$

						return null;
					}

					SelectValueDialog dialog = new SelectValueDialog( PlatformUI.getWorkbench( )
							.getDisplay( )
							.getActiveShell( ),
							Messages.getString( "ExpressionValueCellEditor.title" ) ); //$NON-NLS-1$
					dialog.setSelectedValueList( selectValueList );
					if ( bindingParams != null )
					{
						dialog.setBindingParams( bindingParams );
					}

					if ( dialog.open( ) == IDialogConstants.OK_ID )
					{
						retValue = dialog.getSelectedExprValue( );
					}

				}
				catch ( Exception ex )
				{
					MessageDialog.openError( null,
							Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
							Messages.getString( "SelectValueDialog.messages.error.selectVauleUnavailable" ) //$NON-NLS-1$
									+ "\n" //$NON-NLS-1$
									+ ex.getMessage( ) );
				}

			}
			else if ( designHandle instanceof TabularCubeHandle )
			{
				DataSetHandle dataSet = ( (TabularCubeHandle) designHandle ).getDataSet( );
				String expressionString = getExpression( );
				try
				{
					List selectValueList = SelectValueFetcher.getSelectValueList( expressionString,
							dataSet );
					SelectValueDialog dialog = new SelectValueDialog( PlatformUI.getWorkbench( )
							.getDisplay( )
							.getActiveShell( ),
							Messages.getString( "ExpressionValueCellEditor.title" ) ); //$NON-NLS-1$
					dialog.setSelectedValueList( selectValueList );
					if ( dialog.open( ) == IDialogConstants.OK_ID )
					{

						retValue = dialog.getSelectedExprValue( );
					}

				}
				catch ( BirtException e1 )
				{
					MessageDialog.openError( null,
							Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
							Messages.getString( "SelectValueDialog.messages.error.selectVauleUnavailable" ) //$NON-NLS-1$
									+ "\n" //$NON-NLS-1$
									+ e1.getMessage( ) );
				}
			}
			else
			{
				MessageDialog.openInformation( null,
						Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
						Messages.getString( "SelectValueDialog.messages.info.selectVauleUnavailable" ) ); //$NON-NLS-1$
			}

			return retValue;

		}
	};

	protected MultiValueCombo.ISelection mAddExpValueAction = new MultiValueCombo.ISelection( ) {

		public String[] doSelection( String input )
		{
			// TODO Auto-generated method stub
			String[] retValue = null;

			for ( Iterator iter = columnList.iterator( ); iter.hasNext( ); )
			{
				String columnName = ( (ComputedColumnHandle) ( iter.next( ) ) ).getName( );
				if ( DEUtil.getColumnExpression( columnName )
						.equals( getExpression( ) ) )
				{
					bindingName = columnName;
					break;
				}
			}

			ExpressionBuilder dialog = new ExpressionBuilder( PlatformUI.getWorkbench( )
					.getDisplay( )
					.getActiveShell( ),
					input );

			if ( expressionProvider == null )
				dialog.setExpressionProvier( new ExpressionProvider( designHandle ) );
			else
				dialog.setExpressionProvier( expressionProvider );

			if ( dialog.open( ) == IDialogConstants.OK_ID )
			{
				if ( dialog.getResult( ).length( ) != 0 )
				{
					retValue = new String[]{
						dialog.getResult( )
					};
				}

			}

			return retValue;
		}

		public void doAfterSelection( MultiValueCombo combo )
		{
			// TODO Auto-generated method stub
			mAddSelValueAction.doAfterSelection( combo );
		}

	};

	protected MultiValueCombo.ISelection mAddSelValueAction = new MultiValueCombo.ISelection( ) {

		public String[] doSelection( String input )
		{
			// TODO Auto-generated method stub
			String[] retValue = null;
			for ( Iterator iter = columnList.iterator( ); iter.hasNext( ); )
			{
				String columnName = ( (ComputedColumnHandle) ( iter.next( ) ) ).getName( );

				if ( getExpression( ).equals( VALUE_OF_THIS_DATA_ITEM )
						&& designHandle instanceof DataItemHandle )
				{
					if ( designHandle.getContainer( ) instanceof ExtendedItemHandle )
					{
						if ( DEUtil.getDataExpression( columnName )
								.equals( getExpression( ) ) )
						{
							bindingName = columnName;
							break;
						}
					}
					else
					{
						if ( DEUtil.getColumnExpression( columnName )
								.equals( getExpression( ) ) )
						{
							bindingName = columnName;
							break;
						}
					}

				}
				else
				{
					String value = DEUtil.getExpression( getResultSetColumn( columnName ) );
					if ( value != null && value.equals( getExpression( ) ) )
					{
						bindingName = columnName;
						break;
					}
				}

			}

			if ( bindingName != null )
			{
				try
				{
					List selectValueList = getSelectValueList( );
					SelectValueDialog dialog = new SelectValueDialog( PlatformUI.getWorkbench( )
							.getDisplay( )
							.getActiveShell( ),
							Messages.getString( "ExpressionValueCellEditor.title" ) ); //$NON-NLS-1$
					dialog.setSelectedValueList( selectValueList );
					if ( bindingParams != null )
					{
						dialog.setBindingParams( bindingParams );
					}

					dialog.setMultipleSelection( true );
					if ( dialog.open( ) == IDialogConstants.OK_ID )
					{
						retValue = dialog.getSelectedExprValues( );
					}
				}
				catch ( Exception ex )
				{
					MessageDialog.openError( null,
							Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
							Messages.getString( "SelectValueDialog.messages.error.selectVauleUnavailable" ) //$NON-NLS-1$
									+ "\n" //$NON-NLS-1$
									+ ex.getMessage( ) );
				}
			}
			else if ( designHandle instanceof TabularCubeHandle )
			{
				DataSetHandle dataSet = ( (TabularCubeHandle) designHandle ).getDataSet( );
				String expressionString = getExpression( );
				try
				{
					List selectValueList = SelectValueFetcher.getSelectValueList( expressionString,
							dataSet );
					SelectValueDialog dialog = new SelectValueDialog( PlatformUI.getWorkbench( )
							.getDisplay( )
							.getActiveShell( ),
							Messages.getString( "ExpressionValueCellEditor.title" ) ); //$NON-NLS-1$
					dialog.setSelectedValueList( selectValueList );
					dialog.setMultipleSelection( true );
					if ( dialog.open( ) == IDialogConstants.OK_ID )
					{
						retValue = dialog.getSelectedExprValues( );
					}

				}
				catch ( BirtException e1 )
				{
					MessageDialog.openError( null,
							Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
							Messages.getString( "SelectValueDialog.messages.error.selectVauleUnavailable" ) //$NON-NLS-1$
									+ "\n" //$NON-NLS-1$
									+ e1.getMessage( ) );
				}
			}
			else
			{
				MessageDialog.openInformation( null,
						Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
						Messages.getString( "SelectValueDialog.messages.info.selectVauleUnavailable" ) ); //$NON-NLS-1$
			}

			return retValue;
		}

		public void doAfterSelection( MultiValueCombo combo )
		{
			// TODO Auto-generated method stub

			addBtn.setEnabled( false );

			if ( addExpressionValue.getSelStrings( ).length == 1 )
			{
				addExpressionValue.setText( DEUtil.resolveNull( addExpressionValue.getSelStrings( )[0] ) );
			}
			else if ( addExpressionValue.getSelStrings( ).length > 1 )
			{
				addExpressionValue.setText( "" ); //$NON-NLS-1$
			}

			boolean change = false;
			for ( int i = 0; i < addExpressionValue.getSelStrings( ).length; i++ )
			{
				if ( valueList.indexOf( DEUtil.resolveNull( addExpressionValue.getSelStrings( )[i] ) ) < 0 )
				{
					valueList.add( DEUtil.resolveNull( addExpressionValue.getSelStrings( )[i] ) );
					change = true;
				}
			}
			if ( change )
			{
				tableViewer.refresh( );
				updateButtons( );
				addExpressionValue.setFocus( );
			}

		}
	};

	private void editTableValue( )
	{
		IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection( );
		if ( selection.getFirstElement( ) != null
				&& selection.getFirstElement( ) instanceof String )
		{
			String initValue = (String) selection.getFirstElement( );

			ExpressionBuilder expressionBuilder = new ExpressionBuilder( getShell( ),
					initValue );

			if ( designHandle != null )
			{
				if ( expressionProvider == null )
					expressionBuilder.setExpressionProvier( new ExpressionProvider( designHandle ) );
				else
					expressionBuilder.setExpressionProvier( expressionProvider );
			}

			if ( expressionBuilder.open( ) == OK )
			{
				String result = DEUtil.resolveNull( expressionBuilder.getResult( ) );
				if ( result.length( ) == 0 )
				{
					MessageDialog.openInformation( getShell( ),
							Messages.getString( "MapRuleBuilderDialog.MsgDlg.Title" ),
							Messages.getString( "MapRuleBuilderDialog.MsgDlg.Msg" ) );
					return;
				}
				int index = table.getSelectionIndex( );
				valueList.remove( index );
				valueList.add( index, result );
				tableViewer.refresh( );
				table.select( index );
			}
			updateButtons( );
		}
		else
		{
			editBtn.setEnabled( false );
		}
	}

	protected int getHighlightExpCtrType( DesignElementHandle handle )
	{
		int type = EXPRESSION_CONTROL_COMBO;
		Set<Class> comboClassSet = new HashSet<Class>( ) {
		};
		comboClassSet.add( TableHandle.class );
		comboClassSet.add( ListHandle.class );
		comboClassSet.add( GridHandle.class );

		Class handleClass = handle.getClass( );
		if ( comboClassSet.contains( handleClass ) )
		{
			type = EXPRESSION_CONTROL_TEXT;
		}

		return type;
	}

	protected void initilizeDlgDescription( DesignElementHandle handle )
	{
		Class classList[] = new Class[]{
				TableHandle.class,
				ListHandle.class,
				GridHandle.class,
				RowHandle.class,
				ColumnHandle.class,
				DataItemHandle.class,
				CellHandle.class
		};
		String desList[] = new String[]{
				Messages.getString( "HighlightRuleBuilderDialog.text.Description.Element.Table" ),
				Messages.getString( "HighlightRuleBuilderDialog.text.Description.Element.List" ),
				Messages.getString( "HighlightRuleBuilderDialog.text.Description.Element.Grid" ),
				Messages.getString( "HighlightRuleBuilderDialog.text.Description.Element.Row" ),
				Messages.getString( "HighlightRuleBuilderDialog.text.Description.Element.Column" ),
				Messages.getString( "HighlightRuleBuilderDialog.text.Description.Element.DataItem" ),
				Messages.getString( "HighlightRuleBuilderDialog.text.Description.Element.Cell" ),
		};

		Class handleClass = handle.getClass( );
		for ( int i = 0; i < classList.length; i++ )
		{
			if ( classList[i] == handleClass )
			{
				dlgDescription = desList[i];
				break;
			}
		}

		if ( dlgDescription == null || dlgDescription.length( ) == 0 )
		{
			dlgDescription = Messages.getString( "HighlightRuleBuilderDialog.text.Description.Element.ReportElement" );
		}

		dlgDescription = Messages.getFormattedString( "HighlightRuleBuilderDialog.text.Description",
				new Object[]{
					dlgDescription
				} );
	}

	protected void setExpression( String exp )
	{
		if ( exprControlType == EXPRESSION_CONTROL_TEXT
				&& expressionText != null )
		{
			expressionText.setText( exp );
		}
		else if ( exprControlType == EXPRESSION_CONTROL_COMBO
				&& expressionCombo != null )
		{
			expressionCombo.setText( exp );
		}
	}

	protected String getExpression( )
	{
		if ( exprControlType == EXPRESSION_CONTROL_TEXT
				&& expressionText != null )
		{
			return expressionText.getText( );
		}
		else if ( exprControlType == EXPRESSION_CONTROL_COMBO
				&& expressionCombo != null )
		{
			return expressionCombo.getText( );
		}
		return "";
	}

	protected Control getExpressionControl( )
	{
		if ( exprControlType == EXPRESSION_CONTROL_TEXT
				&& expressionText != null )
		{
			return expressionText;
		}
		else if ( exprControlType == EXPRESSION_CONTROL_COMBO
				&& expressionCombo != null )
		{
			return expressionCombo;
		}
		return null;
	}

}