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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.designer.data.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.ExpressionButton;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelper;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelperProvider;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.CubeACLExpressionProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.CubeMeasureExpressionProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.LinkToCubeExpressionProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.BuilderConstants;
import org.eclipse.birt.report.designer.ui.dialogs.BaseTitleAreaDialog;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.FormatValueHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.elements.interfaces.IMeasureModel;
import org.eclipse.birt.report.model.elements.olap.Measure;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.util.ULocale;

public class MeasureDialog extends BaseTitleAreaDialog
{
    private static final String ARGUMENT_EXPRESSION = "Expression";  

	private boolean isEdit = false;
	private boolean isAutoPrimaryKeyChecked = false;
	private CubeMeasureExpressionProvider provider;
	private Combo typeCombo;
	private Text expressionText;
	private Text txtFilter;

	private Button derivedMeasureBtn, visibilityBtn;
	private Label exprDesc;
	private FunctionUI functionUI; 
	
	private MeasureHandle input;
	private Text nameText;
	private static IChoice[] dataTypes = DEUtil.getMetaDataDictionary( )
			.getElement( ReportDesignConstants.MEASURE_ELEMENT )
			.getProperty( IMeasureModel.DATA_TYPE_PROP )
			.getAllowedChoices( )
			.getChoices( );
	private Object result;
	private IDialogHelper securityHelper;
	private IDialogHelper formatHelper;
	private IDialogHelper alignmentHelper;
	private IMeasureDialogHelper measureHelper;
	private Text displayNameText;

	public MeasureDialog( boolean newOrEdit )
	{
		super( UIUtil.getDefaultShell( ) );
		setShellStyle( getShellStyle( ) | SWT.RESIZE | SWT.MAX );
		this.isEdit = !newOrEdit;
	}

	private String[] getDataTypeNames( )
	{
		IChoice[] choices = dataTypes;
		if ( choices == null )
			return new String[0];

		String[] names = new String[choices.length];
		for ( int i = 0; i < choices.length; i++ )
		{
			names[i] = choices[i].getName( );
		}
		return names;
	}

	private String getDataTypeDisplayName( String name )
	{
		return ChoiceSetFactory.getDisplayNameFromChoiceSet( name,
				DEUtil.getMetaDataDictionary( )
						.getElement( ReportDesignConstants.MEASURE_ELEMENT )
						.getProperty( IMeasureModel.DATA_TYPE_PROP )
						.getAllowedChoices( ) );
	}

	private String[] getDataTypeDisplayNames( )
	{
		IChoice[] choices = dataTypes;
		if ( choices == null )
			return new String[0];

		String[] displayNames = new String[choices.length];
		for ( int i = 0; i < choices.length; i++ )
		{
			displayNames[i] = choices[i].getDisplayName( );
		}
		return displayNames;
	}
	
	public void setInput( MeasureHandle input )
	{
		this.input = input;
	}

	public void setAutoPrimaryKeyStatus( boolean isChecked )
	{
		this.isAutoPrimaryKeyChecked = isChecked;
	}

    @Override
	protected Control createDialogArea( Composite parent )
	{
		UIUtil.bindHelp( parent, IHelpContextIds.MEASURE_DIALOG );

		Composite area = (Composite) super.createDialogArea( parent );

        ScrolledComposite sc = new ScrolledComposite( area, SWT.V_SCROLL );
        sc.setAlwaysShowScrollBars( false );
        sc.setExpandHorizontal( true );
        sc.setLayoutData( new GridData( GridData.FILL_BOTH ) );
        
		Composite contents = new Composite( sc, SWT.NONE);
        sc.setContent( contents );

        GridLayout layout = new GridLayout( );
        layout.verticalSpacing = 0;
        layout.marginWidth = 20;
        contents.setLayout( layout );

        GridData data = new GridData( GridData.FILL_BOTH );
        data.widthHint = convertWidthInCharsToPixels( 70 );
        contents.setLayoutData( data );
        
		createMeasureArea( contents );

		createVisibilityGroup( contents);

		WidgetUtil.createGridPlaceholder( contents, 1, true );

		initMeasureDialog( );

        // calculate the size explicitly as it is in scrollable composite
        Point size = contents.computeSize( SWT.DEFAULT, SWT.DEFAULT );
        contents.setSize( Math.max( size.x, 400 ), Math.max( size.y, 320 ) );

		return contents;
	}

	protected Control createContents( Composite parent )
	{
		Control control = super.createContents( parent );
		setTitle( Messages.getString( "MeasureDialog.Title.Description" ) ); //$NON-NLS-1$
		setMessage( Messages.getString( "MeasureDialog.Text.Description" ) ); //$NON-NLS-1$	
		return control;
	}

	private void initMeasureDialog( )
	{
        functionUI.initUI( );
		typeCombo.setItems( getDataTypeDisplayNames( ) );
		if ( !isEdit )
		{
			if ( typeCombo.getItemCount( ) > 0 )
			{
				typeCombo.select( 0 );
			}
			handleFunctionSelectEvent( );
		}
		else
		{
            try
            {
                String funcName = DataAdapterUtil.adaptModelAggregationType( input.getFunction( ) );
                Map<String, Expression> arguments = getArguments( input );
                ExpressionHandle argExpr = input.getExpressionProperty( Measure.MEASURE_EXPRESSION_PROP );
                functionUI.setAggregation( funcName, arguments, (Expression) argExpr.getValue( ) );
            }
            catch ( AdapterException e )
            {
                ExceptionUtil.handle( e );
            }

			ExpressionButtonUtil.initExpressionButtonControl( expressionText,
					input,
					MeasureHandle.MEASURE_EXPRESSION_PROP );

			nameText.setText( input.getName( ) == null ? "" : input.getName( ) ); //$NON-NLS-1$
			displayNameText.setText( input.getDisplayName( ) == null ? "" : input.getDisplayName( ) ); //$NON-NLS-1$
			handleFunctionSelectEvent( );
			typeCombo.setText( getDataTypeDisplayName( input.getDataType( ) ) == null ? "" //$NON-NLS-1$
					: getDataTypeDisplayName( input.getDataType( ) ) );
			derivedMeasureBtn.setSelection( input.isCalculated( ) );
			updateDerivedMeasureStatus( );
		}

        if ( measureHelper != null && measureHelper.hasFilter( input ) )
        {
            Expression filterExpr = measureHelper.getFilter( input );
            if ( filterExpr != null )
            {
                ExpressionButtonUtil.initExpressionButtonControl( txtFilter, filterExpr );
            }
        }
		
		if ( formatHelper != null )
		{
			if ( typeCombo.getSelectionIndex( ) > -1 )
			{
				formatHelper.setProperty( BuilderConstants.FORMAT_VALUE_TYPE,
						getDataTypeNames( )[typeCombo.getSelectionIndex( )] );
			}
			formatHelper.update( true );
		}
		if ( alignmentHelper != null )
		{
			if ( input.getAlignment( ) != null )
			{
				alignmentHelper.setProperty( BuilderConstants.ALIGNMENT_VALUE,
						input.getAlignment( ) );
			}
			else if ( !isEdit && input.getDataType( ) != null )
			{
				if ( isNumber( input.getDataType( ) ) )
				{
					alignmentHelper.setProperty( BuilderConstants.ALIGNMENT_VALUE,
							DesignChoiceConstants.TEXT_ALIGN_RIGHT );
				}
				else
				{
					alignmentHelper.setProperty( BuilderConstants.ALIGNMENT_VALUE,
							DesignChoiceConstants.TEXT_ALIGN_LEFT );
				}
			}
			alignmentHelper.update( true );
		}
	}

	private boolean isNumber( String dataType )
	{
		return ( DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL.equals( dataType )
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT.equals( dataType ) || DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER.equals( dataType ) );
	}

	public Object getResult( )
	{
		return result;
	}

    private boolean hasFilter( MeasureHandle measure )
    {
        if ( measureHelper != null)
        {
            return measureHelper.hasFilter( measure );
        }
        return false;
    }

    private Expression getFilter( MeasureHandle measure )
    {
        if ( this.measureHelper != null )
        {
            return measureHelper.getFilter( measure );
        }
        return null;
    }

    private void setFilter( MeasureHandle measure, Expression expr ) throws SemanticException
    {
        if ( this.measureHelper != null )
        {
            measureHelper.setFilter( measure, expr );
        }
    }

    private Map<String, Expression> getArguments( MeasureHandle measure )
    {
        if ( measureHelper != null )
        {
            return measureHelper.getArguments( measure );
        }
        return null;
    }

    private void setArguments( MeasureHandle measure, Map<String, Expression> arguments ) throws SemanticException
    {
        if ( measureHelper != null )
        {
            measureHelper.setArguments( measure, arguments );
        }
    }

    private MeasureHandle createMeasure( String name ) throws SemanticException
    {
        if ( this.measureHelper != null )
        {
            return measureHelper.createMeasure( name );
        }

        return DesignElementFactory.getInstance( ).newTabularMeasure( nameText.getText( ) );
    }

    @Override
    protected void okPressed( )
    {
        try
        {

            MeasureHandle measure = input;
            if ( measure == null )
            {
                measure = createMeasure( nameText.getText( ).trim( ) );
            }
            else
            {
                input.setName( nameText.getText( ).trim( ) );
            }
            if ( displayNameText.getText( ).trim( ).length( ) > 0 )
            {
                measure.setDisplayName( displayNameText.getText( ).trim( ) );
            }
            else
            {
                measure.setDisplayName( null );
            }

            measure.setCalculated( derivedMeasureBtn.getSelection( ) );

            if ( derivedMeasureBtn.getSelection( ) )
            {
                measure.setFunction( null );
                setArguments( measure, null );
            }
            else
            {

                IAggrFunction func = functionUI.getSelectedFunction( );
                if ( func != null )
                {
                    String modelFuncName = DataAdapterUtil.toModelAggregationType( func.getName( ) );
                    measure.setFunction( modelFuncName );
                }
                setArguments( measure, functionUI.getArguments( ) );
            }

            measure.setDataType( getDataTypeNames( )[typeCombo.getSelectionIndex( )] );
            if ( expressionText.isEnabled( ) )
            {
                ExpressionButtonUtil.saveExpressionButtonControl( expressionText, measure,
                        MeasureHandle.MEASURE_EXPRESSION_PROP );
            }

            if ( !derivedMeasureBtn.getSelection( ) )
            {
                if ( securityHelper != null )
                {
                    securityHelper.validate( );
                    measure.setExpressionProperty( MeasureHandle.ACL_EXPRESSION_PROP,
                            (Expression) securityHelper.getProperty( BuilderConstants.SECURITY_EXPRESSION_PROPERTY ) );
                }
                if ( hasFilter( measure ) )
                {
                    setFilter( measure, ExpressionButtonUtil.getExpression( txtFilter ) );
                }
            }
            else
            {
                measure.setExpressionProperty( MeasureHandle.ACL_EXPRESSION_PROP, null );
                if ( hasFilter( measure ) )
                {
                    setFilter( measure, null );
                }
            }

            if ( alignmentHelper != null )
            {
                measure.setAlignment( (String) alignmentHelper.getProperty( BuilderConstants.ALIGNMENT_VALUE ) );
            }
            if ( formatHelper != null
                    && formatHelper.getProperty( BuilderConstants.FORMAT_VALUE_RESULT ) instanceof Object[] )
            {
                Object[] formatValue = (Object[]) formatHelper.getProperty( BuilderConstants.FORMAT_VALUE_RESULT );
                Object value = measure.getProperty( Measure.FORMAT_PROP );
                if ( value == null )
                {
                    FormatValue formatValueToSet = new FormatValue( );
                    formatValueToSet.setCategory( (String) formatValue[0] );
                    formatValueToSet.setPattern( (String) formatValue[1] );
                    formatValueToSet.setLocale( (ULocale) formatValue[2] );
                    measure.setProperty( Measure.FORMAT_PROP, formatValueToSet );
                }
                else
                {
                    PropertyHandle propHandle = measure.getPropertyHandle( Measure.FORMAT_PROP );
                    FormatValue formatValueToSet = (FormatValue) value;
                    FormatValueHandle formatHandle = (FormatValueHandle) formatValueToSet.getHandle( propHandle );
                    formatHandle.setCategory( (String) formatValue[0] );
                    formatHandle.setPattern( (String) formatValue[1] );
                    formatHandle.setLocale( (ULocale) formatValue[2] );
                }

            }
            measure.setVisible( !visibilityBtn.getSelection( ) );
            result = measure;
        }
        catch ( SemanticException e )
        {
            ExceptionUtil.handle( e );
            return;
        }
        super.okPressed( );
    }

	protected void createButtonsForButtonBar( Composite parent )
	{
		super.createButtonsForButtonBar( parent );
		checkOkButtonStatus( );
	}

	private Composite createMeasureArea( Composite parent )
	{
        DesignElementHandle container = input.getContainer( );
        DesignElementHandle superContainer = container.getContainer( );
        this.measureHelper = (IMeasureDialogHelper) ElementAdapterManager
                .getAdapter( superContainer, IMeasureDialogHelper.class );
	    
		getShell( ).setText( Messages.getString( "MeasureDialog.Title.Property" ) ); //$NON-NLS-1$
		Group group = new Group( parent, SWT.NONE );
		GridData gd = new GridData( );
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		group.setLayoutData( gd );

		GridLayout layout = new GridLayout( );
		layout.numColumns = 3;
		group.setLayout( layout );

		Label nameLabel = new Label( group, SWT.NONE );
		nameLabel.setText( Messages.getString( "MeasureDialog.Label.Name" ) ); //$NON-NLS-1$
		nameText = new Text( group, SWT.BORDER );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		nameText.setLayoutData( gd );
		nameText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				checkOkButtonStatus( );
			}

		} );

		Label displayNameLabel = new Label( group, SWT.NONE );
		displayNameLabel.setText( Messages.getString( "MeasureDialog.Label.DisplayName" ) ); //$NON-NLS-1$
		displayNameText = new Text( group, SWT.BORDER );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		displayNameText.setLayoutData( gd );
		displayNameText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				checkOkButtonStatus( );
			}

		} );

		new Label( group, SWT.NONE );
		derivedMeasureBtn = new Button( group, SWT.CHECK );
		derivedMeasureBtn.setText( Messages.getString( "MeasureDialog.Label.DerivedMeasure" ) ); //$NON-NLS-1$

		derivedMeasureBtn.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				updateDerivedMeasureStatus( );
				if ( !derivedMeasureBtn.getSelection( ) )
				{
					handleTypeSelectEvent( );
				}
				checkOkButtonStatus();
			}

		} );
		new Label( group, SWT.NONE );
		
		functionUI = createFunctionUI( group );
		functionUI.createUI( );
		
		Label typeLabel = new Label( group, SWT.NONE );
		typeLabel.setText( Messages.getString( "MeasureDialog.Label.DataType" ) ); //$NON-NLS-1$
		typeCombo = new Combo( group, SWT.BORDER | SWT.READ_ONLY );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		typeCombo.setLayoutData( gd );
		typeCombo.setVisibleItemCount( 30 );
		typeCombo.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( !derivedMeasureBtn.getSelection( ) )
				{
					handleTypeSelectEvent( );
				}
				checkOkButtonStatus( );
				if ( formatHelper != null )
				{
					if ( typeCombo.getSelectionIndex( ) > -1 )
						formatHelper.setProperty( BuilderConstants.FORMAT_VALUE_TYPE,
								getDataTypeNames( )[typeCombo.getSelectionIndex( )] );
					formatHelper.update( true );
				}
			}
		} );

		Label expressionLabel = new Label( group, SWT.NONE );
		expressionLabel.setText( Messages.getString( "MeasureDialog.Label.Expression" ) ); //$NON-NLS-1$
		expressionText = new Text( group, SWT.BORDER | SWT.MULTI );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.heightHint = expressionText.computeSize( SWT.DEFAULT,
				SWT.DEFAULT ).y
				- expressionText.getBorderWidth( )
				* 2;
		expressionText.setLayoutData( gd );
		expressionText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				checkOkButtonStatus( );
			}

		} );

		if ( measureHelper != null )
		{
			provider = measureHelper.getExpressionProvider( input );
		}
		else
		{
			provider = new CubeMeasureExpressionProvider( input,
					input.isCalculated( ) );
		}
		ExpressionButtonUtil.createExpressionButton( group,
				expressionText,
				provider,
				input );

		new Label( group, SWT.NONE );
		exprDesc = new Label( group, SWT.NONE );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		gd.grabExcessHorizontalSpace = true;
		exprDesc.setLayoutData( gd );
		exprDesc.setText( Messages.getString( Messages.getString( derivedMeasureBtn.getSelection( ) ? "MeasureDialog.Label.ExprDesc.Derived" : "MeasureDialog.Label.ExprDesc" ) ) ); //$NON-NLS-1$ //$NON-NLS-2$
		exprDesc.setForeground( ColorManager.getColor( 128, 128, 128 ) );
		// new Label( group, SWT.NONE );

        if ( hasFilter( input ) )
        {
            createFilterPart( group );
        }
		if ( measureHelper == null || !measureHelper.hideSecurityPart( ) )
		{
			createSecurityPart( group );
		}
		if ( measureHelper == null || !measureHelper.hideHyperLinkPart( ) )
		{
			createHyperLinkPart( group );
		}
		if ( measureHelper == null || !measureHelper.hideFormatPart( ) )
		{
			createFormatPart( group );
		}
		if ( measureHelper == null || !measureHelper.hideAlignmentPart( ) )
		{
			createAlignmentPart( group );
		}
		return group;
	}

	private Composite createVisibilityGroup( Composite parent )
	{
		Group group = new Group( parent, SWT.NONE );
		GridData gd = new GridData( );
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		group.setLayoutData( gd );

		GridLayout layout = new GridLayout( );
		layout.numColumns = 2;
		group.setLayout( layout );

		group.setText( Messages.getString( "MeasureDialog.Label.Visibility.Group" ) ); //$NON-NLS-1$

		visibilityBtn = new Button( group, SWT.CHECK );
		visibilityBtn.setText( Messages.getString( "MeasureDialog.Label.Visibility" ) ); //$NON-NLS-1$
		visibilityBtn.setSelection( !input.isVisible( ) );

		return group;
	}

	private IDialogHelper createHyperLinkPart( Composite parent )
	{
		Object[] helperProviders = ElementAdapterManager.getAdapters( input,
				IDialogHelperProvider.class );
		if ( helperProviders != null )
		{
			for ( int i = 0; i < helperProviders.length; i++ )
			{
				IDialogHelperProvider helperProvider = (IDialogHelperProvider) helperProviders[i];
				if ( helperProvider != null )
				{
					final IDialogHelper hyperLinkHelper = helperProvider.createHelper( this,
							BuilderConstants.HYPERLINK_HELPER_KEY );
					if ( hyperLinkHelper != null )
					{
						hyperLinkHelper.setProperty( BuilderConstants.HYPERLINK_LABEL,
								Messages.getString( "MeasureDialog.Label.LinkTo" ) ); //$NON-NLS-1$
						hyperLinkHelper.setProperty( BuilderConstants.HYPERLINK_BUTTON_TEXT,
								Messages.getString( "MeasureDialog.Button.Text.Edit" ) ); //$NON-NLS-1$
						hyperLinkHelper.setProperty( BuilderConstants.HYPERLINK_REPORT_ITEM_HANDLE,
								input );
						hyperLinkHelper.setProperty( BuilderConstants.HYPERLINK_REPORT_ITEM_PROVIDER,
								new LinkToCubeExpressionProvider( input ) );
						hyperLinkHelper.createContent( parent );
						hyperLinkHelper.addListener( SWT.Modify,
								new Listener( ) {

									public void handleEvent( Event event )
									{
										hyperLinkHelper.update( false );
									}
								} );
						hyperLinkHelper.update( true );
						return hyperLinkHelper;
					}
				}
			}
		}
		return null;
	}

	private IDialogHelper createFormatPart( Composite parent )
	{
		Object[] helperProviders = ElementAdapterManager.getAdapters( input,
				IDialogHelperProvider.class );
		if ( helperProviders != null )
		{
			for ( int i = 0; i < helperProviders.length; i++ )
			{
				IDialogHelperProvider helperProvider = (IDialogHelperProvider) helperProviders[i];
				if ( helperProvider != null )
				{
					formatHelper = helperProvider.createHelper( this,
							BuilderConstants.FORMAT_HELPER_KEY );
					if ( formatHelper != null )
					{
						formatHelper.setProperty( BuilderConstants.FORMAT_LABEL,
								Messages.getString( "MeasureDialog.Label.Format" ) ); //$NON-NLS-1$
						formatHelper.setProperty( BuilderConstants.FORMAT_BUTTON_TEXT,
								Messages.getString( "MeasureDialog.Button.Format.Edit" ) ); //$NON-NLS-1$
						PropertyHandle propHandle = input.getPropertyHandle( Measure.FORMAT_PROP );
						if ( input.getProperty( Measure.FORMAT_PROP ) != null )
						{
							Object value = input.getProperty( Measure.FORMAT_PROP );
							FormatValue formatValueToSet = (FormatValue) value;
							FormatValueHandle formatHandle = (FormatValueHandle) formatValueToSet.getHandle( propHandle );
							formatHelper.setProperty( BuilderConstants.FORMAT_VALUE,
									formatHandle );
						}
						formatHelper.createContent( parent );
						formatHelper.update( true );
						return formatHelper;
					}
				}
			}
		}
		return null;
	}

	private IDialogHelper createAlignmentPart( Composite parent )
	{
		Object[] helperProviders = ElementAdapterManager.getAdapters( input,
				IDialogHelperProvider.class );
		if ( helperProviders != null )
		{
			for ( int i = 0; i < helperProviders.length; i++ )
			{
				IDialogHelperProvider helperProvider = (IDialogHelperProvider) helperProviders[i];
				if ( helperProvider != null )
				{
					alignmentHelper = helperProvider.createHelper( this,
							BuilderConstants.ALIGNMENT_HELPER_KEY );
					if ( alignmentHelper != null )
					{
						alignmentHelper.setProperty( BuilderConstants.ALIGNMENT_LABEL,
								Messages.getString( "MeasureDialog.Label.Alignment" ) ); //$NON-NLS-1$
						alignmentHelper.setProperty( BuilderConstants.ALIGNMENT_VALUE,
								input.getAlignment( ) );
						alignmentHelper.createContent( parent );
						alignmentHelper.update( true );
						return alignmentHelper;
					}
				}
			}
		}
		return null;
	}

	private void createSecurityPart( Composite parent )
	{
		Object[] helperProviders = ElementAdapterManager.getAdapters( input,
				IDialogHelperProvider.class );
		if ( helperProviders != null )
		{
			for ( int i = 0; i < helperProviders.length; i++ )
			{
				IDialogHelperProvider helperProvider = (IDialogHelperProvider) helperProviders[i];
				if ( helperProvider != null && securityHelper == null )
				{
					securityHelper = helperProvider.createHelper( this,
							BuilderConstants.SECURITY_HELPER_KEY );
					if ( securityHelper != null )
					{
						securityHelper.setProperty( BuilderConstants.SECURITY_EXPRESSION_LABEL,
								Messages.getString( "MeasureDialog.Access.Control.List.Expression" ) ); //$NON-NLS-1$
						securityHelper.setProperty( BuilderConstants.SECURITY_EXPRESSION_CONTEXT,
								input );
						securityHelper.setProperty( BuilderConstants.SECURITY_EXPRESSION_PROVIDER,
								new CubeACLExpressionProvider( input ) );
						securityHelper.setProperty( BuilderConstants.SECURITY_EXPRESSION_PROPERTY,
								input.getACLExpression( ) );
						securityHelper.createContent( parent );
						securityHelper.addListener( SWT.Modify,
								new Listener( ) {

									public void handleEvent( Event event )
									{
										securityHelper.update( false );
									}
								} );
						securityHelper.update( true );
					}
				}
			}
		}
	}
	
    private void createFilterPart( Composite composite )
    {
        new Label( composite, SWT.NONE ).setText( Messages.getString( "MeasureDialog.Label.Filter" ) ); //$NON-NLS-1$
        txtFilter = new Text( composite, SWT.BORDER | SWT.MULTI );
        GridData gd = new GridData( GridData.FILL_HORIZONTAL );
        gd.heightHint = txtFilter.computeSize( SWT.DEFAULT, SWT.DEFAULT ).y - txtFilter.getBorderWidth( ) * 2;
        gd.horizontalSpan = 1;
        txtFilter.setLayoutData( gd );

        txtFilter.addModifyListener( new ModifyListener( ) {

            public void modifyText( ModifyEvent arg0 )
            {
                checkOkButtonStatus( );
            }
        } );
        ExpressionButtonUtil.createExpressionButton( composite,
                txtFilter,
                provider,
                input );        
    }

	protected void handleTypeSelectEvent( )
	{
		IAggrFunction function = functionUI.getSelectedFunction( );
		if ( function == null )
			return;
	    int returnType = function.getDataType( );
	    String typeName = DataAdapterUtil.adapterToModelDataType( returnType );
	    String recommendType = getDataTypeDisplayName( typeName ); 
	    //recommendType may be any, which is not in the type list
		if ( !typeCombo.getText( ).equals( recommendType )
				&& typeCombo.indexOf( recommendType ) != -1 )
		{
			if ( !MessageDialog.openQuestion( getShell( ),
					Messages.getString( "MeasureDialog.MessageDialog.Title" ), //$NON-NLS-1$
					Messages.getFormattedString( "MeasureDialog.MessageDialog.Message", //$NON-NLS-1$
							new Object[]{
								recommendType
							} ) ) )
				typeCombo.setText( recommendType );
		}
	}

    private FunctionProvider createFuncProvider( )
    {
        IAggrFunction[] funcs = null;
        if ( measureHelper != null )
        {
            funcs = measureHelper.getAggregationFunctions( input );
        }
        else
        {
            try
            {
                @SuppressWarnings("unchecked")
                List<IAggrFunction> aggrInfoList = DataUtil.getAggregationManager( )
                        .getAggregations( AggregationManager.AGGR_MEASURE );
                funcs = (IAggrFunction[]) aggrInfoList.toArray( new IAggrFunction[0] );
            }
            catch ( BirtException e )
            {
                ExceptionUtil.handle( e );
                funcs = new IAggrFunction[0];
            }
        }

        return new FunctionProvider( funcs );
    }

    protected Runnable createFunctionUICallback( )
    {
        return new Runnable( ) {

            public void run( )
            {
                handleFunctionSelectEvent( );
                checkOkButtonStatus( );
            }
        };
    }

    protected FunctionUI createFunctionUI( Composite composite )
    {
        FunctionProvider funcProvider = createFuncProvider( );
        Runnable callback = createFunctionUICallback( );

        FunctionUI ui = new FunctionUI( composite, input, funcProvider, provider, null, callback );
        return ui;
    }
    
    protected void createFunctionUIPart( Composite composite )
    {
        functionUI = createFunctionUI( composite);
        functionUI.createUI( );
        functionUI.initUI();
    }


    private void handleFunctionSelectEvent( )
    {
        IAggrFunction func = functionUI.getSelectedFunction( );
        if ( func == null )
            return;

        int returnType = func.getDataType( );
        String typeName = DataAdapterUtil.adapterToModelDataType( returnType );
        String typeDisplayText = getDataTypeDisplayName( typeName );
        typeCombo.setText( typeDisplayText );

        int parameterLength = func.getParameterDefn( ).length;
        expressionText.setEnabled( parameterLength > 0 );
        ( (ExpressionButton) expressionText.getData( ExpressionButtonUtil.EXPR_BUTTON ) )
                .setEnabled( parameterLength > 0 );
    }

    private boolean isValidExpression( Expression expr )
    {
        return expr != null && expr.getStringExpression( ) != null
                && expr.getStringExpression( ).trim( ).length( ) != 0;
    }

    protected String checkExpression( )
    {
        if ( expressionText.getText( ) == null || expressionText.getText( ).trim( ).length( ) == 0 )
        {
            return Messages.getString( "MeasureDialog.Message.BlankExpression" ); //$NON-NLS-1$
        }

        if ( !this.derivedMeasureBtn.getSelection( ) )
        {
            IAggrFunction function = functionUI.getSelectedFunction( );
            if ( function == null )
            {
                return Messages.getString( "MeasureDialog.Message.BlankFunction" ); //$NON-NLS-1$
            }

            IParameterDefn[] paramDefs = function.getParameterDefn( );
            if ( paramDefs != null )
            {
                Map<String, Expression> argValues = functionUI.getArguments( );
                for ( IParameterDefn paramDef : paramDefs )
                {
                    if ( !paramDef.isOptional( ) )
                    {
                        String argName = DataAdapterUtil.adaptArgumentName( paramDef.getName( ) );
                        // ARGUMENT_EXPRESSION is defined in expressionText
                        if ( !ARGUMENT_EXPRESSION.equals( argName ) )
                        {
                            Expression value = argValues.get( argName );
                            if ( !isValidExpression( value ) )
                            {
                                return Messages
                                        .getFormattedString( "MessageDialog.Message.BlankArgument", //$NON-NLS-1$
                                                new String[]{paramDef.getDisplayName( )
                                                        .replaceAll( "\\(&[a-zA-Z0-9]\\)", "" )
                                                        .replaceAll( "&", "" )} );
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private String checkName( )
    {
        if ( nameText.getText( ) == null || nameText.getText( ).trim( ).equals( "" ) ) //$NON-NLS-1$
        {
            return Messages.getString( "MeasureDialog.Message.BlankName" ); //$NON-NLS-1$
        }

        if ( !UIUtil.validateDimensionName( nameText.getText( ) ) )
        {
            return Messages.getString( "MeasureDialog.Message.NumericName" ); //$NON-NLS-1$
        }
        return null;
    }

    private String checkType( )
    {
        if ( typeCombo.getSelectionIndex( ) == -1 )
        {
            return Messages.getString( "MeasureDialog.Message.BlankType" ); //$NON-NLS-1$
        }
        return null;
    }

    protected void checkOkButtonStatus( )
    {
        String errorMessage = null;
        if ( ( errorMessage = checkName( ) ) == null )
        {
            if ( ( errorMessage = checkType( ) ) == null )
            {
                if ( ( errorMessage = checkExpression( ) ) == null )
                {
                    if ( getButton( IDialogConstants.OK_ID ) != null )
                    {
                        getButton( IDialogConstants.OK_ID ).setEnabled( true );
                        setErrorMessage( null );
                        setMessage( Messages.getString( "MeasureDialog.Text.Description" ) ); //$NON-NLS-1$
                        return;
                    }
                }
            }
        }
        // there are errors here
        if ( getButton( IDialogConstants.OK_ID ) != null )
        {
            getButton( IDialogConstants.OK_ID ).setEnabled( false );
            setMessage( null );
            setErrorMessage( errorMessage );
        }
    }

    private void updateDerivedMeasureStatus( )
    {
        boolean isDerivedMeasure = derivedMeasureBtn.getSelection( );
        if ( txtFilter != null )
        {
            txtFilter.setEnabled( !isDerivedMeasure );
            ExpressionButtonUtil.getExpressionButton( txtFilter ).setEnabled( !isDerivedMeasure );
        }
        functionUI.setEnabled( !( isDerivedMeasure || isAutoPrimaryKeyChecked ) );
        if ( securityHelper != null )
        {
            securityHelper.setProperty( BuilderConstants.SECURITY_EXPRESSION_ENABLE, !isDerivedMeasure );
            securityHelper.update( true );
        }
        exprDesc.setText( Messages.getString(
                isDerivedMeasure ? "MeasureDialog.Label.ExprDesc.Derived" : "MeasureDialog.Label.ExprDesc" ) ); //$NON-NLS-1$ //$NON-NLS-2$
        provider.setDerivedMeasure( isDerivedMeasure );
    }

    protected static class FunctionUI
    {
        
        static final String FUNCTION = Messages.getString( "MeasureDialog.Label.Function" ) ; //$NON-NLS-1$
        
        protected Composite parent;
        
        private Combo cmbFunction;
        private Composite paramsComposite;
        private Map<String, Control> paramsMap = new HashMap<String, Control>( );

        private DesignElementHandle bindingHolder;
        private ExpressionProvider exprProvider;
        private FunctionProvider funcProvider;
        private ComputedColumn[] bindings;
        private Runnable callback;
        
        private Map<String, Expression> paramsValueMap = new HashMap<String, Expression>( );

        public FunctionUI( Composite parent, DesignElementHandle bindingHolder, FunctionProvider funcProvider, ExpressionProvider exprProvider, ComputedColumn[] bindings, Runnable callback )
        {
            this.parent = parent;
            this.bindingHolder = bindingHolder;
            this.exprProvider = exprProvider;
            this.funcProvider = funcProvider;
            this.bindings = bindings;
            this.callback = callback;
        }
        
        /**
         * the initial defined function name.
         * 
         * The name is saved in report design.
         * 
         * @param functions
         */
        public void setAggregation( String funcName, Map<String, Expression> args, Expression expr )
        {
            IAggrFunction func = funcProvider.getFunction( funcName );
            cmbFunction.setText( func == null ? "" : func.getDisplayName( ) ); //$NON-NLS-1$
            if ( args != null )
            {
                paramsValueMap.putAll( args );
            }
            updateArgumentUI( );
        }

        public void setEnabled( boolean enabled )
        {
            cmbFunction.setEnabled( enabled );
            paramsComposite.setEnabled( enabled );
        }

        /**
         * selected aggregate function.
         * 
         * @return
         */
        public IAggrFunction getSelectedFunction( )
        {
            String displayText = cmbFunction.getText( );
            return funcProvider.getFunctionByDisplayText( displayText );
        }

        public String getSelectedFunctionName( )
        {
            IAggrFunction func = getSelectedFunction( );
            return func == null ? "" : func.getName( ); //$NON-NLS-1$ ;
        }
        
        /**
         * Arguments of the function.
         * 
         * @return
         */
        public Map<String, Expression> getArguments( )
        {
            HashMap<String, Expression> results = new HashMap<String, Expression>( );
            for ( String argName : paramsMap.keySet( ) )
            {
                Control control = paramsMap.get( argName );
                Expression expr = ExpressionButtonUtil.getExpression( control );
                results.put( argName, expr );
            }
            return results;
        }

        private int getParentLayoutColumns( )
        {
            assert parent.getLayout( ) instanceof GridLayout;
            Layout layout = parent.getLayout( );
            if ( layout instanceof GridLayout )
            {
                return ( (GridLayout) layout ).numColumns;
            }
            return 3;
        }

        public void createUI( )
        {
            //assume the parent has grid layout with 4 columns.
            int columns = getParentLayoutColumns();
            
            new Label( parent, SWT.NONE ).setText( FUNCTION );
            cmbFunction = new Combo( parent, SWT.BORDER | SWT.READ_ONLY );
            GridData gd = new GridData( GridData.FILL_HORIZONTAL );
            gd.horizontalSpan = gd.horizontalSpan = columns - 1;
            cmbFunction.setLayoutData( gd );
            cmbFunction.setVisibleItemCount( 30 );
            // WidgetUtil.createGridPlaceholder( composite, 1, false );

            cmbFunction.addSelectionListener( new SelectionAdapter( ) {

                public void widgetSelected( SelectionEvent e )
                {
                    String funcText = cmbFunction.getText( );
                    IAggrFunction func = funcProvider.getFunctionByDisplayText( funcText );
                    if ( func != null )
                    {
                        updateArgumentUI( );
                        callback.run( );
                    }
                }
            } );

            paramsComposite = new Composite( parent, SWT.NONE );
            GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
            gridData.horizontalIndent = 0;
            gridData.horizontalSpan = columns;
            gridData.exclude = true;
            paramsComposite.setLayoutData( gridData );
            GridLayout layout = new GridLayout( );
            layout.marginWidth = layout.marginHeight = 0;
            layout.numColumns = 3;
            Layout parentLayout = parent.getLayout( );
            if ( parentLayout instanceof GridLayout )
                layout.horizontalSpacing = ( (GridLayout) parentLayout ).horizontalSpacing;
            paramsComposite.setLayout( layout );
        }

        private void initUI( )
        {
            // initiate function UI
            cmbFunction.setItems( funcProvider.getDisplayTexts( ) );
            cmbFunction.select( 0 );
            // create argument UI part
            updateArgumentUI( );
        }

        private void resetArgumentUI( )
        {
            Control[] children = paramsComposite.getChildren( );
            for ( int i = 0; i < children.length; i++ )
            {
                children[i].dispose( );
            }
            paramsMap.clear( );
            // don't display the parameter
            ( (GridData) paramsComposite.getLayoutData( ) ).exclude = true;
            ( (GridData) paramsComposite.getLayoutData( ) ).heightHint = 0;
        }

        private int getMaxLabelWidth( Composite parent )
        {
            int maxWidth = 0;
            Control[] controls = parent.getChildren( );
            for ( int i = 0; i < controls.length; i++ )
            {
                if ( controls[i] instanceof Label )
                {
                    int labelWidth = getLabelWidth( (Label) controls[i] );
                    if ( labelWidth > maxWidth )
                    {
                        maxWidth = labelWidth;
                    }
                }
            }
            return maxWidth;
        }

        private int getLabelWidth( Label label )
        {
            Object layout = label.getLayoutData( );
            if ( layout instanceof GridData )
            {
                if ( ( (GridData) layout ).horizontalSpan == 1 )
                {
                    return label.getBounds( ).width - label.getBorderWidth( ) * 2;
                }
            }
            return 0;
        }

        private void createArgumentUI( )
        {
            IAggrFunction function = getSelectedFunction( );
            if ( function != null )
            {
                IParameterDefn[] params = function.getParameterDefn( );
                if ( params != null && params.length > 0 )
                {

                    ( (GridData) paramsComposite.getLayoutData( ) ).exclude = false;
                    ( (GridData) paramsComposite.getLayoutData( ) ).heightHint = SWT.DEFAULT;

                    int width = getMaxLabelWidth( parent );

                    for ( final IParameterDefn param : params )
                    {
                        String paramName =      DataAdapterUtil.adaptArgumentName(param.getName( ) );
                        //Expression is handled as measureExpression 
                        if ( ARGUMENT_EXPRESSION.equals( paramName ) )
                        {
                            continue;
                        }
                                
                        Label lblParam = new Label( paramsComposite, SWT.NONE );
                        lblParam.setText(
                                param.getDisplayName( ) + Messages.getString( "MeasureDialog.Text.Colon" ) ); //$NON-NLS-1$
                        GridData gd = new GridData( );
                        gd.widthHint = lblParam.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x;
                        if ( gd.widthHint < width )
                            gd.widthHint = width;
                        lblParam.setLayoutData( gd );

                        if ( param.isDataField( ) )
                        {
                            createComboControl( paramsComposite, param );
                        }

                        else
                        {
                            createTextControl( paramsComposite, param );
                        }
                    }
                }
            }
        }

        private void createComboControl( Composite paramsComposite, final IParameterDefn param )
        {
            assert param.isDataField( );
            final Combo cmbDataField = new Combo( paramsComposite, SWT.BORDER );
            cmbDataField.setLayoutData( new GridData( GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL ) );
            cmbDataField.setVisibleItemCount( 30 );
            createExpressionButton( paramsComposite, cmbDataField, param );
            paramsMap.put( param.getName( ), cmbDataField );

            cmbDataField.addModifyListener( new ModifyListener( ) {

                public void modifyText( ModifyEvent e )
                {
                    paramsValueMap.put( param.getName( ), new Expression( cmbDataField.getText( ),
                            (String) cmbDataField.getData( ExpressionButtonUtil.EXPR_TYPE ) ) );
                    callback.run( );
                }
            } );

            cmbDataField.addSelectionListener( new SelectionAdapter( ) {

                public void widgetSelected( SelectionEvent e )
                {
                    Expression expr = createBindingExpression( cmbDataField );
                    if ( expr != null )
                    {
                        cmbDataField.setText( expr.getStringExpression( ) );
                    }

                    if ( expr == null )
                    {
                        expr = new Expression( cmbDataField.getText( ),
                                (String) cmbDataField.getData( ExpressionButtonUtil.EXPR_TYPE ) );
                    }
                    paramsValueMap.put( param.getName( ), expr );
                }
            } );
        }

        private void createTextControl( Composite paramsComposite, final IParameterDefn param )
        {
            assert!param.isDataField( );
            final Text txtParam = new Text( paramsComposite, SWT.BORDER | SWT.MULTI );
            GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
            gridData.heightHint = txtParam.computeSize( SWT.DEFAULT, SWT.DEFAULT ).y - txtParam.getBorderWidth( ) * 2;
            gridData.horizontalIndent = 0;
            txtParam.setLayoutData( gridData );
            createExpressionButton( paramsComposite, txtParam, param );
            paramsMap.put( param.getName( ), txtParam );

            txtParam.addModifyListener( new ModifyListener( ) {

                public void modifyText( ModifyEvent e )
                {
                    paramsValueMap.put( param.getName( ), new Expression( txtParam.getText( ),
                            (String) txtParam.getData( ExpressionButtonUtil.EXPR_TYPE ) ) );
                    callback.run( );
                }

            } );
        }

        private void initArgumentUI( )
        {
            IAggrFunction function = getSelectedFunction( );
            if ( function != null )
            {
                IParameterDefn[] params = function.getParameterDefn( );

                for ( final IParameterDefn param : params )
                {
                    initArgumentField( param );
                }
            }
        }

        /**
         * Create function parameters area. If parameter is data field type, create
         * a combo box filled with binding holder's computed column.
         */
        protected void updateArgumentUI( )
        {
            resetArgumentUI( );
            createArgumentUI( );
            initArgumentUI( );

            Point size = parent.getParent( ).computeSize( SWT.DEFAULT, SWT.DEFAULT );
            parent.getParent( ).setSize( Math.max( size.x, 400 ), Math.max( size.y, 320 ) );
        }

        private void initArgumentField( IParameterDefn param )
        {
            Expression expr = paramsValueMap.get( param.getName( ) );
            if ( expr != null )
            {
                String text = expr.getStringExpression( ) == null ? "" : expr.getStringExpression( );
                String type = expr.getType( ) == null ? ExpressionType.JAVASCRIPT : expr.getType( );
                Control control = paramsMap.get( param.getName( ) );
                assert control != null;
                if ( control instanceof Text )
                {
                    Text txtParam = (Text) control;
                    txtParam.setText( text );
                    txtParam.setData( ExpressionButtonUtil.EXPR_TYPE, type );
                }
                else if ( control instanceof Combo )
                {
                    Combo cmbDataField = (Combo) control;
                    cmbDataField.setItems( getColumnBindings( ) );
                    cmbDataField.setText( text );
                    cmbDataField.setData( ExpressionButtonUtil.EXPR_TYPE, type);
                }
                ExpressionButton button = (ExpressionButton) control.getData( ExpressionButtonUtil.EXPR_BUTTON );
                if ( button != null )
                    button.refresh( );
            }
        }

        private void createExpressionButton( final Composite parent, final Control control, final IParameterDefn param )
        {
            Listener listener = new Listener( ) {

                public void handleEvent( Event event )
                {
                    callback.run( );
                }
            };

            ExpressionButtonUtil.createExpressionButton( parent, control, exprProvider,
                    this.bindingHolder, listener );
        }

        private String[] getColumnBindings( )
        {
            if ( bindings == null )
            {
                return new String[]{};
            }
            String[] names = new String[bindings.length];
            for ( int i = 0; i < bindings.length; i++ )
            {
                names[i] = bindings[i].getName( );

            }
            return names;
        }

        private Expression createBindingExpression( Combo combo )
        {
            String text = combo.getText( );
            String expr = ExpressionButtonUtil.getCurrentExpressionConverter( combo ).getBindingExpression( text );
            if ( expr != null )
            {
                return new Expression( expr, ExpressionType.JAVASCRIPT );
            }
            return new Expression( text, ExpressionType.JAVASCRIPT );
        }
    }
    
    private class FunctionProvider
    {

        private IAggrFunction[] funcs;
        private HashMap<String, IAggrFunction> displayName2Funcs;
        private HashMap<String, IAggrFunction> name2Funcs;

        public FunctionProvider( IAggrFunction[] funcs )
        {
            this.funcs = funcs == null ? new IAggrFunction[0] : funcs;
            this.displayName2Funcs = new HashMap<String, IAggrFunction>( funcs.length );
            this.name2Funcs = new HashMap<String, IAggrFunction>( funcs.length );
            for ( IAggrFunction func : funcs )
            {
                displayName2Funcs.put( func.getDisplayName( ), func );
                name2Funcs.put( func.getName( ), func );
            }
        }

        public IAggrFunction getFunction( String funcName )
        {
            return name2Funcs.get( funcName );
        }

        public IAggrFunction getFunctionByDisplayText( String displayText )
        {
            return displayName2Funcs.get( displayText );
        }

        public String[] getDisplayTexts( )
        {
            String[] displayTexts = new String[funcs.length];
            for ( int i = 0; i < funcs.length; i++ )
            {
                displayTexts[i] = funcs[i].getDisplayName( );
            }
            Arrays.sort( displayTexts, new AlphabeticallyComparator( ) );
            return displayTexts;
        }
    }
}
