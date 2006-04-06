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

import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.PreviewLabel;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.HighlightHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.ColorBuilder;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FontSizeBuilder;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.AttributeConstant;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Dialog for adding or editing highlight Rule.
 */

public class HighlightRuleBuilder extends BaseDialog
{

	/**
	 * Usable operators for building highlight rule conditions.
	 */
	public static final String[][] OPERATOR;

	static
	{
		IChoiceSet chset = ChoiceSetFactory.getStructChoiceSet( HighlightRule.STRUCTURE_NAME,
				HighlightRule.OPERATOR_MEMBER );
		IChoice[] chs = chset.getChoices( );
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

	private Combo expression;

	private Combo operator;

	private Text value1, value2;

	private Label andLable;

	private Button valBuilder1, valBuilder2;

	private Combo font;

	private FontSizeBuilder size;

	private ColorBuilder color;

	private ColorBuilder backColor;

	private Button bold, italic, underline, linethrough;

	private PreviewLabel previewLabel;

	private DesignElementHandle designHandle;

	private boolean isBoldChanged, isItalicChanged, isUnderlineChanged,
			isLinethroughChanged;

	private static final String DEFAULT_CHOICE = Messages.getString( "HighlightRuleBuilderDialog.text.Default" ); //$NON-NLS-1$

	private static final String[] SYSTEM_FONT_LIST = DEUtil.getSystemFontNames( );

	private static final String VALUE_OF_THIS_DATA_ITEM = Messages.getString( "HighlightRuleBuilderDialog.choice.ValueOfThisDataItem" ); //$NON-NLS-1$

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
		super( parentShell, title );

		this.provider = provider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents( Composite parent )
	{
		GridData gdata;
		GridLayout glayout;

		createTitleArea( parent );

		Composite composite = new Composite( parent, 0 );
		glayout = new GridLayout( );
		glayout.marginHeight = 0;
		glayout.marginWidth = 0;
		glayout.verticalSpacing = 0;
		composite.setLayout( glayout );
		composite.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		applyDialogFont( composite );
		initializeDialogUnits( composite );

		Composite innerParent = (Composite) createDialogArea( composite );
		createButtonBar( composite );

		Label lb = new Label( innerParent, SWT.NONE );
		lb.setText( Messages.getString( "HighlightRuleBuilderDialog.text.Condition" ) ); //$NON-NLS-1$

		Composite condition = new Composite( innerParent, SWT.NONE );
		condition.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		glayout = new GridLayout( 5, false );
		condition.setLayout( glayout );

		expression = new Combo( condition, SWT.NONE );
		gdata = new GridData( );
		gdata.widthHint = 100;
		expression.setLayoutData( gdata );
		fillExpression( expression );
		expression.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( expression.getText( ).equals( VALUE_OF_THIS_DATA_ITEM )
						&& designHandle instanceof DataItemHandle )
				{
					expression.setText( DEUtil.resolveNull( ( (DataItemHandle) designHandle ).getValueExpr( ) ) );
				}
				updateButtons( );
			}
		} );
		expression.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateButtons( );
			}
		} );

		Button expBuilder = new Button( condition, SWT.PUSH );
		expBuilder.setText( "..." ); //$NON-NLS-1$
		gdata = new GridData( );
		gdata.heightHint = 20;
		gdata.widthHint = 20;
		expBuilder.setLayoutData( gdata );
		expBuilder.setToolTipText( Messages.getString( "HighlightRuleBuilderDialog.tooltip.ExpBuilder" ) ); //$NON-NLS-1$
		expBuilder.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				editValue( expression );
			}
		} );

		operator = new Combo( condition, SWT.READ_ONLY );
		for ( int i = 0; i < OPERATOR.length; i++ )
		{
			operator.add( OPERATOR[i][0] );
		}
		operator.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				String value = getValueForOperator( operator.getText( ) );

				int vv = determineValueVisible( value );

				if ( vv == 0 )
				{
					value1.setVisible( false );
					value2.setVisible( false );
					valBuilder1.setVisible( false );
					valBuilder2.setVisible( false );
					andLable.setVisible( false );
				}
				else if ( vv == 1 )
				{
					value1.setVisible( true );
					valBuilder1.setVisible( true );
					value2.setVisible( false );
					valBuilder2.setVisible( false );
					andLable.setVisible( false );
				}
				else if ( vv == 2 )
				{
					value1.setVisible( true );
					value2.setVisible( true );
					valBuilder1.setVisible( true );
					valBuilder2.setVisible( true );
					andLable.setVisible( true );
				}
				updateButtons( );
			}
		} );

		value1 = createText( condition );
		value1.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateButtons( );
			}
		} );

		valBuilder1 = new Button( condition, SWT.PUSH );
		valBuilder1.setText( "..." ); //$NON-NLS-1$
		gdata = new GridData( );
		gdata.heightHint = 20;
		gdata.widthHint = 20;
		valBuilder1.setLayoutData( gdata );
		valBuilder1.setToolTipText( Messages.getString( "HighlightRuleBuilderDialog.tooltip.ExpBuilder" ) ); //$NON-NLS-1$
		valBuilder1.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				editValue( value1 );
			}
		} );

		createDummy( condition, 3 );

		andLable = new Label( condition, SWT.NONE );
		andLable.setText( Messages.getString( "HighlightRuleBuilderDialog.text.AND" ) ); //$NON-NLS-1$
		andLable.setVisible( false );

		createDummy( condition, 1 );
		createDummy( condition, 3 );

		value2 = createText( condition );
		value2.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateButtons( );
			}
		} );
		value2.setVisible( false );

		valBuilder2 = new Button( condition, SWT.PUSH );
		valBuilder2.setText( "..." ); //$NON-NLS-1$
		gdata = new GridData( );
		gdata.heightHint = 20;
		gdata.widthHint = 20;
		valBuilder2.setLayoutData( gdata );
		valBuilder2.setToolTipText( Messages.getString( "HighlightRuleBuilderDialog.tooltip.ExpBuilder" ) ); //$NON-NLS-1$
		valBuilder2.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				editValue( value2 );
			}
		} );
		valBuilder2.setVisible( false );

		if ( operator.getItemCount( ) > 0 )
		{
			operator.select( 0 );
		}

		lb = new Label( innerParent, SWT.NONE );
		lb.setText( Messages.getString( "HighlightRuleBuilderDialog.text.Format" ) ); //$NON-NLS-1$

		Composite format = new Composite( innerParent, SWT.NONE );
		format.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		glayout = new GridLayout( 7, false );
		format.setLayout( glayout );

		lb = new Label( format, 0 );
		lb.setText( Messages.getString( "HighlightRuleBuilderDialog.text.Font" ) ); //$NON-NLS-1$

		lb = new Label( format, 0 );
		lb.setText( Messages.getString( "HighlightRuleBuilderDialog.text.Size" ) ); //$NON-NLS-1$

		lb = new Label( format, 0 );
		lb.setText( Messages.getString( "HighlightRuleBuilderDialog.text.Color" ) ); //$NON-NLS-1$

		createDummy( format, 4 );

		font = new Combo( format, SWT.READ_ONLY );
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

		size = new FontSizeBuilder( format, SWT.None );
		if ( designHandle != null )
		{
			size.setDefaultUnit( designHandle.getPropertyHandle( StyleHandle.FONT_SIZE_PROP )
					.getDefaultUnit( ) );
		}
		gdata = new GridData( );
		gdata.widthHint = 120;
		size.setLayoutData( gdata );
		size.setFontSizeValue( null );
		size.addListener( SWT.Modify, new Listener( ) {

			public void handleEvent( Event event )
			{
				updatePreview( );
			}
		} );

		color = new ColorBuilder( format, 0 );
		gdata = new GridData( );
		gdata.widthHint = 50;
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

		Composite fstyle = new Composite( format, 0 );
		gdata = new GridData( );
		gdata.horizontalSpan = 4;
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

		italic = createToggleButton( fstyle );
		italic.setImage( ReportPlatformUIImages.getImage( AttributeConstant.FONT_STYLE ) );
		italic.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				isItalicChanged = true;
				updatePreview( );
			}
		} );

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

		Composite back = new Composite( innerParent, SWT.NONE );
		back.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		glayout = new GridLayout( 1, false );
		back.setLayout( glayout );

		lb = new Label( back, 0 );
		lb.setText( Messages.getString( "HighlightRuleBuilderDialog.text.BackgroundColor" ) ); //$NON-NLS-1$

		backColor = new ColorBuilder( back, 0 );
		gdata = new GridData( );
		gdata.widthHint = 50;
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

		Composite preview = new Composite( innerParent, SWT.NONE );
		glayout = new GridLayout( );
		preview.setLayout( glayout );
		gdata = new GridData( GridData.FILL_BOTH );
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

		lb = new Label( innerParent, SWT.SEPARATOR | SWT.HORIZONTAL );
		lb.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		syncViewProperties( );
		updatePreview( );

		updateButtons( );

		return composite;
	}

	private Composite createTitleArea( Composite parent )
	{
		int margins = 2;
		final Composite titleArea = new Composite( parent, SWT.NONE );
		FormLayout layout = new FormLayout( );
		layout.marginHeight = margins;
		layout.marginWidth = margins;
		titleArea.setLayout( layout );

		Display display = parent.getDisplay( );
		Color background = JFaceColors.getBannerBackground( display );
		GridData layoutData = new GridData( GridData.FILL_HORIZONTAL );
		layoutData.heightHint = 20 + ( margins * 3 );
		titleArea.setLayoutData( layoutData );
		titleArea.setBackground( background );

		titleArea.addPaintListener( new PaintListener( ) {

			public void paintControl( PaintEvent e )
			{
				e.gc.setForeground( titleArea.getDisplay( )
						.getSystemColor( SWT.COLOR_WIDGET_NORMAL_SHADOW ) );
				Rectangle bounds = titleArea.getClientArea( );
				bounds.height = bounds.height - 2;
				bounds.width = bounds.width - 1;
				e.gc.drawRectangle( bounds );
			}
		} );

		Label label = new Label( titleArea, SWT.NONE );
		label.setBackground( background );
		label.setFont( FontManager.getFont( label.getFont( ).toString( ),
				10,
				SWT.BOLD ) );
		label.setText( Messages.getString( "HighlightRuleBuilderDialog.text.Title" ) ); //$NON-NLS-1$

		return titleArea;
	}

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

		previewLabel.updateView( );
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

	public void setDesignHandle( DesignElementHandle handle )
	{
		this.designHandle = handle;
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

	private void fillExpression( Combo control )
	{
		String te = "";//$NON-NLS-1$

		if ( handle != null )
		{
			te = handle.getTestExpression( );
		}

		if ( designHandle instanceof DataItemHandle )
		{
			control.add( VALUE_OF_THIS_DATA_ITEM );
		}

		control.add( DEUtil.resolveNull( te ) );

		control.select( control.getItemCount( ) - 1 );
	}

	/**
	 * Refreshes the OK button state.
	 * 
	 */
	private void updateButtons( )
	{
		enableInput( isExpressionOK( ) );

		getOkButton( ).setEnabled( isConditionOK( ) );
	}

	private void enableInput( boolean val )
	{
		operator.setEnabled( val );
		value1.setEnabled( val );
		value2.setEnabled( val );
		valBuilder1.setEnabled( val );
		valBuilder2.setEnabled( val );
		font.setEnabled( val );
		size.setEnabled( val );
		color.setEnabled( val );
		bold.setEnabled( val );
		italic.setEnabled( val );
		underline.setEnabled( val );
		linethrough.setEnabled( val );
		backColor.setEnabled( val );
	}

	/**
	 * Gets if the expression field is not empty.
	 */
	private boolean isExpressionOK( )
	{
		if ( expression == null )
		{
			return false;
		}

		if ( expression.getText( ) == null
				|| expression.getText( ).length( ) == 0 )
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
		if ( expression == null )
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
		if ( value1.getVisible( ) )
		{
			if ( value1.getText( ) == null || value1.getText( ).length( ) == 0 )
			{
				return false;
			}
		}

		if ( value2.getVisible( ) )
		{
			if ( value2.getText( ) == null || value2.getText( ).length( ) == 0 )
			{
				return false;
			}
		}

		return true;
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
			expression.setText( DEUtil.resolveNull( handle.getTestExpression( ) ) );

			operator.select( getIndexForOperatorValue( handle.getOperator( ) ) );

			value1.setText( DEUtil.resolveNull( handle.getValue1( ) ) );

			value2.setText( DEUtil.resolveNull( handle.getValue2( ) ) );

			int vv = determineValueVisible( handle.getOperator( ) );

			if ( vv == 0 )
			{
				value1.setVisible( false );
				value2.setVisible( false );
				valBuilder1.setVisible( false );
				valBuilder2.setVisible( false );
				andLable.setVisible( false );
			}
			else if ( vv == 1 )
			{
				value1.setVisible( true );
				value2.setVisible( false );
				valBuilder1.setVisible( true );
				valBuilder2.setVisible( false );
				andLable.setVisible( false );
			}
			else if ( vv == 2 )
			{
				value1.setVisible( true );
				value2.setVisible( true );
				valBuilder1.setVisible( true );
				valBuilder2.setVisible( true );
				andLable.setVisible( true );
			}
		}

		syncFamily( );
		syncSize( );

		if ( handle != null )
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
		if ( handle != null )
		{
			String fm = handle.getFontFamilyHandle( ).getDisplayValue( );

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
		if ( handle != null )
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
				rule.setProperty( HighlightRule.VALUE1_MEMBER,
						DEUtil.resolveNull( value1.getText( ) ) );
				rule.setProperty( HighlightRule.VALUE2_MEMBER,
						DEUtil.resolveNull( value2.getText( ) ) );

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
					rule.setProperty( HighlightRule.FONT_SIZE_MEMBER, sizeValue );
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

				// set test expression into highlight rule.
				rule.setTestExpression( DEUtil.resolveNull( expression.getText( ) ) );

				handle = provider.doAddItem( rule, handleCount );
			}
			else
			{
				handle.setOperator( DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );

				handle.setValue1( DEUtil.resolveNull( value1.getText( ) ) );
				if ( handle.getOperator( )
						.equals( DesignChoiceConstants.MAP_OPERATOR_BETWEEN )
						|| handle.getOperator( )
								.equals( DesignChoiceConstants.MAP_OPERATOR_NOT_BETWEEN ) )
				{
					handle.setValue2( DEUtil.resolveNull( value2.getText( ) ) );
				}

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

				// set test expression into highlight rule.
				handle.setTestExpression( DEUtil.resolveNull( expression.getText( ) ) );

			}
		}
		catch ( Exception e )
		{
			WidgetUtil.processError( getShell( ), e );
		}

		super.okPressed( );
	}

	private void editValue( Control control )
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
		ExpressionBuilder expressionBuilder = new ExpressionBuilder( getShell( ),
				initValue );

		if ( designHandle != null )
		{
			expressionBuilder.setExpressionProvier( new ExpressionProvider( designHandle ) );
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
}