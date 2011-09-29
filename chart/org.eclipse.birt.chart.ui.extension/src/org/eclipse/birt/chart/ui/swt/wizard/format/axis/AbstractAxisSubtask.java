/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.axis;

import java.util.List;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AngleType;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.ComboSelectionComposite;
import org.eclipse.birt.chart.ui.swt.composites.DateTimeDataElementComposite;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.FontDefinitionComposite;
import org.eclipse.birt.chart.ui.swt.composites.FormatSpecifierDialog;
import org.eclipse.birt.chart.ui.swt.composites.LocalizedNumberEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.NumberDataElementComposite;
import org.eclipse.birt.chart.ui.swt.composites.VisibleComboSelection;
import org.eclipse.birt.chart.ui.swt.fieldassist.FieldAssistHelper;
import org.eclipse.birt.chart.ui.swt.fieldassist.TextNumberEditorAssistField;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataElementComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.InteractivitySheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.axis.AxisGridLinesSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.axis.AxisLabelSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.axis.AxisMarkersSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.axis.AxisScaleSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.axis.AxisTitleSheet;
import org.eclipse.birt.chart.ui.util.ChartCacheManager;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.birt.chart.util.TriggerSupportMatrix;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * Axis subtask
 * 
 */
abstract class AbstractAxisSubtask extends SubtaskSheetImpl implements
		Listener,
		SelectionListener,
		ModifyListener
{
	
	private Label lblCategoryAxis;
	
	private Combo cmbCategoryAxis;
	
	private Label lblReverse;
	
	private Combo cmbReverse;

	private ExternalizedTextEditorComposite txtTitle;
	
	private VisibleComboSelection vcsTitleVisible;

	private Combo cmbTypes;

	private Combo cmbOrigin;

	private Button btnFormatSpecifier;

	private Label lblValue;

	private IDataElementComposite txtValue;

	private VisibleComboSelection vcsLabelVisible;

	private FontDefinitionComposite fdcFont;

	private ComboSelectionComposite cscStaggered;
	
	private LocalizedNumberEditorComposite lneLabelSpan;

	private Button btnFixLabelSpan;

	private Button btnTxtValueAuto;

	AbstractAxisSubtask( )
	{
		super( );
	}

	abstract protected Axis getAxisForProcessing( );

	/**
	 * Returns the axis angle type
	 * 
	 * @return <code>AngleType.X</code>, <code>AngleType.Y</code> or
	 *         <code>AngleType.Z</code>
	 */
	abstract protected int getAxisAngleType( );

	protected boolean isChart3D( )
	{
		if ( getChart( ) instanceof ChartWithAxes )
		{
			return ( getChart( ).getDimension( ) == ChartDimension.THREE_DIMENSIONAL_LITERAL );
		}
		return false;
	}

	@Override
	public void createControl( Composite parent )
	{
		cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( 2, false );
			cmpContent.setLayout( glContent );
			GridData gd = new GridData( GridData.FILL_BOTH );
			cmpContent.setLayoutData( gd );
		}

		Composite cmpBasic = new Composite( cmpContent, SWT.NONE );
		{
			cmpBasic.setLayout( new GridLayout( 3, false ) );
			cmpBasic.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		}

		if ( getAxisAngleType( ) == AngleType.X )
		{
			lblCategoryAxis = new Label( cmpBasic, SWT.NONE );
			lblCategoryAxis.setText( Messages.getString("AbstractAxisSubtask.Label.IsCategoryAxis") ); //$NON-NLS-1$
			
			cmbCategoryAxis = ChartUIExtensionUtil.createCombo( cmpBasic,
					ChartUIExtensionUtil.getTrueFalseComboItems( ) );
			GridData gd = new GridData( );
			gd.horizontalSpan = 2;
			gd.horizontalIndent = 5;
			cmbCategoryAxis.setLayoutData( gd );
			cmbCategoryAxis.select( getAxisForProcessing( ).isSetCategoryAxis( ) ? (getAxisForProcessing( ).isCategoryAxis( ) ? 1 : 2 ): 0 );
			updateCategoryAxisUI( !AxisType.TEXT_LITERAL.equals( getAxisForProcessing( ).getType( ) ) );	
			cmbCategoryAxis.addSelectionListener( this );

			lblReverse = new Label( cmpBasic, SWT.NONE );
			lblReverse.setText( Messages.getString("AbstractAxisSubtask.Label.ReverseCategories") ); //$NON-NLS-1$
			
			cmbReverse = ChartUIExtensionUtil.createCombo( cmpBasic,
					ChartUIExtensionUtil.getTrueFalseComboItems( ) );
			gd = new GridData( );
			gd.horizontalSpan = 2;
			gd.horizontalIndent = 5;
			cmbReverse.setLayoutData( gd );
			cmbReverse.select( ( (ChartWithAxes) getChart( ) ).isSetReverseCategory( ) ? ( ( (ChartWithAxes) getChart( ) ).isReverseCategory( ) ? 1 : 2 ) : 0 );
			updateReverseUI( cmbCategoryAxis.getSelectionIndex( ) == 1 );
			cmbReverse.addSelectionListener( this );
		}

		Label lblTitle = new Label( cmpBasic, SWT.NONE );
		lblTitle.setText( Messages.getString( "AxisYSheetImpl.Label.Title" ) ); //$NON-NLS-1$

		List<String> keys = null;
		IUIServiceProvider serviceprovider = getContext( ).getUIServiceProvider( );
		if ( serviceprovider != null )
		{
			keys = serviceprovider.getRegisteredKeys( );
		}

		txtTitle = new ExternalizedTextEditorComposite( cmpBasic,
				SWT.BORDER | SWT.SINGLE,
				-1,
				-1,
				keys,
				serviceprovider,
				getAxisForProcessing( ).getTitle( ).getCaption( ).getValue( ) );
		{
			GridData gd = new GridData( );
			gd.widthHint = 250;
			gd.horizontalIndent = 5;
			txtTitle.setLayoutData( gd );
			txtTitle.addListener( this );
		}

		vcsTitleVisible = new VisibleComboSelection( cmpBasic, SWT.NONE );
		vcsTitleVisible.select( getAxisForProcessing( ).getTitle( ).isSetVisible( ) ? (getAxisForProcessing( ).getTitle( ).isVisible( ) ? 1 :2 ) :0 );
		vcsTitleVisible.addSelectionListener( this );

		if ( getAxisAngleType( ) != AngleType.Z )
		{
			Label lblType = new Label( cmpBasic, SWT.NONE );
			lblType.setText( Messages.getString( "OrthogonalAxisDataSheetImpl.Lbl.Type" ) ); //$NON-NLS-1$

			cmbTypes = new Combo( cmpBasic, SWT.DROP_DOWN | SWT.READ_ONLY );
			{
				GridData gd = new GridData( );
				gd.widthHint = 220;
				gd.horizontalIndent = 5;
				cmbTypes.setLayoutData( gd );
				cmbTypes.addSelectionListener( this );
			}

			btnFormatSpecifier = new Button( cmpBasic, SWT.PUSH );
			{
				GridData gdBTNFormatSpecifier = new GridData( );
				gdBTNFormatSpecifier.horizontalIndent = -3;
				btnFormatSpecifier.setLayoutData( gdBTNFormatSpecifier );
				//btnFormatSpecifier.setImage( UIHelper.getImage( "icons/obj16/formatbuilder.gif" ) ); //$NON-NLS-1$
				btnFormatSpecifier.setToolTipText( Messages.getString( "Shared.Tooltip.FormatSpecifier" ) ); //$NON-NLS-1$
				btnFormatSpecifier.addSelectionListener( this );
				// btnFormatSpecifier.getImage( )
				// .setBackground( btnFormatSpecifier.getBackground( ) );
				btnFormatSpecifier.setText( Messages.getString("Format.Button.Label") ); //$NON-NLS-1$
			}

			// Origin is not supported in 3D
			if ( getChart( ).getDimension( ).getValue( ) != ChartDimension.THREE_DIMENSIONAL )
			{
				Label lblOrigin = new Label( cmpBasic, SWT.NONE );
				lblOrigin.setText( Messages.getString( "OrthogonalAxisDataSheetImpl.Lbl.Origin" ) ); //$NON-NLS-1$

				cmbOrigin = new Combo( cmpBasic, SWT.DROP_DOWN | SWT.READ_ONLY );
				{
					GridData gd = new GridData( );
					gd.widthHint = 220;
					gd.horizontalSpan = 2;
					gd.horizontalIndent = 5;
					cmbOrigin.setLayoutData( gd );
					cmbOrigin.addSelectionListener( this );
				}

				boolean bValueOrigin = false;
				if ( getAxisForProcessing( ).getOrigin( ) != null )
				{
					if ( getAxisForProcessing( ).getOrigin( ).isSetType( )
							&& getAxisForProcessing( ).getOrigin( )
									.getType( )
									.equals( IntersectionType.VALUE_LITERAL ) )
					{
						bValueOrigin = true;
					}
				}

				lblValue = new Label( cmpBasic, SWT.NONE );
				{
					lblValue.setText( Messages.getString( "BaseAxisDataSheetImpl.Lbl.Value" ) ); //$NON-NLS-1$
					lblValue.setEnabled( bValueOrigin );
				}

				txtValue = createDataElementComposite( cmpBasic );
				{
					GridData gd = new GridData( );
					gd.widthHint = 245;
					gd.horizontalSpan = 1;
					gd.horizontalIndent = 5;
					txtValue.setLayoutData( gd );
					txtValue.addListener( this );
					txtValue.setEnabled( bValueOrigin );
				}
				
				btnTxtValueAuto = new Button( cmpBasic, SWT.CHECK );
				btnTxtValueAuto.setText( ChartUIExtensionUtil.getAutoMessage( ) );
				btnTxtValueAuto.setSelection( bValueOrigin
						&& getAxisForProcessing( ).getOrigin( ).getValue( ) == null );
				btnTxtValueAuto.setEnabled( bValueOrigin );
				btnTxtValueAuto.addSelectionListener( this );
				if ( btnTxtValueAuto.getSelection( ) )
				{
					txtValue.setEnabled( false );
				}
			}

			populateLists( );
		}

		new Label( cmpBasic, SWT.NONE ).setText( Messages.getString( "AxisYSheetImpl.Label.Labels" ) ); //$NON-NLS-1$

		fdcFont = new FontDefinitionComposite( cmpBasic,
				SWT.NONE,
				getContext( ),
				getAxisForProcessing( ).getLabel( ).getCaption( ).getFont( ),
				getAxisForProcessing( ).getLabel( ).getCaption( ).getColor( ),
				false );
		{
			GridData gdFDCFont = new GridData( );
			// gdFDCFont.heightHint = fdcFont.getPreferredSize( ).y;
			gdFDCFont.widthHint = 250;
			gdFDCFont.horizontalIndent = 5;
			fdcFont.setLayoutData( gdFDCFont );
			fdcFont.addListener( this );
		}

		Composite cmpLabel = new Composite( cmpBasic, SWT.NONE );
		{
			GridLayout layout = new GridLayout( 2, false );
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			cmpLabel.setLayout( layout );
		}

		
		vcsLabelVisible = new VisibleComboSelection( cmpLabel, SWT.NONE );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		vcsLabelVisible.setLayoutData( gd );
		vcsLabelVisible.select( getAxisForProcessing( ).getLabel( )
				.isSetVisible( ) ? ( getAxisForProcessing( ).getLabel( )
				.isVisible( ) ? 1 : 2 ) : 0 );
		vcsLabelVisible.addSelectionListener( this );
			
		cscStaggered = new ComboSelectionComposite( cmpLabel, SWT.NONE, Messages.getString("AbstractAxisSubtask.Label.Staggered"), //$NON-NLS-1$
				ChartUIExtensionUtil.getTrueFalseComboItems( ) );
		{
			Axis ax = getAxisForProcessing( );
			boolean bStaggered = ax.isSetStaggered( ) && ax.isStaggered( );
			boolean bNot3D = !isChart3D( );

			cscStaggered.select( ax.isSetStaggered( ) ? ( ( ax.isStaggered( ) && bNot3D ) ? 1
					: 2 )
					: 0 );
			cscStaggered.setEnabled( bNot3D );
			cscStaggered.addSelectionListener( this );
		}

		if ( getChart( ).getDimension( ).getValue( ) != ChartDimension.THREE_DIMENSIONAL )
		{
			Label l = new Label( cmpBasic, SWT.NONE );
			l.setText( Messages.getString( "AbstractAxisSubtask.Label.LabelSpan" ) ); //$NON-NLS-1$
			FieldAssistHelper.getInstance( ).addRequiredFieldIndicator( l );
			
			Composite cmpEditorWithUnit = new Composite( cmpBasic, SWT.NONE );
			{
				gd = new GridData( );
				gd.widthHint = 250;
				cmpEditorWithUnit.setLayoutData( gd );
				GridLayout layout = new GridLayout( 2, false );
				layout.marginWidth = 0;
				layout.marginHeight = 0;
				cmpEditorWithUnit.setLayout( layout );
			}

			lneLabelSpan = new LocalizedNumberEditorComposite( cmpEditorWithUnit,
					SWT.BORDER );
			new TextNumberEditorAssistField( lneLabelSpan.getTextControl( ),
					null );
			{
				lneLabelSpan.setValue( getAxisForProcessing( ).getLabelSpan( ) );
				lneLabelSpan.addModifyListener( this );
				gd = new GridData( GridData.FILL_HORIZONTAL );
				gd.horizontalIndent = 5;
				lneLabelSpan.setLayoutData( gd );
				lneLabelSpan.setEnabled( getAxisForProcessing( ).isSetLabelSpan( ) );
			}
			new Label( cmpEditorWithUnit, SWT.NONE ).setText( Messages.getString( "AbstractAxisSubtask.Label.Points" ) ); //$NON-NLS-1$

			btnFixLabelSpan = new Button( cmpBasic, SWT.CHECK );
			{
				btnFixLabelSpan.setText( Messages.getString( "AbstractAxisSubtask.Button.Fixed" ) ); //$NON-NLS-1$
				btnFixLabelSpan.addSelectionListener( this );
				btnFixLabelSpan.setSelection( getAxisForProcessing( ).isSetLabelSpan( ) );
			}
		}

		createButtonGroup( cmpContent );

		setStateOfTitle( );
		setStateOfLabel( );
	}


	protected void updateCategoryAxisUI( boolean enabled )
	{
		cmbCategoryAxis.setEnabled( enabled );
		lblCategoryAxis.setEnabled( enabled );
	}

	protected void updateReverseUI( boolean enabled )
	{
		cmbReverse.setEnabled( enabled );
		lblReverse.setEnabled( enabled );
	}

	private void setStateOfTitle( )
	{
		boolean isTitleEnabled = getAxisForProcessing( ).getTitle( )
				.isSetVisible( )
				&& getAxisForProcessing( ).getTitle( ).isVisible( );
		txtTitle.setEnabled( isTitleEnabled );
		setToggleButtonEnabled( BUTTON_TITLE, isTitleEnabled );
	}

	private void setStateOfLabel( )
	{
		Axis ax = getAxisForProcessing( );
		boolean isLabelEnabled = ax.getLabel( ).isSetVisible( )
				&& ax.getLabel( ).isVisible( );
		fdcFont.setEnabled( isLabelEnabled );
		cscStaggered.setEnabled( !isChart3D( ) && isLabelEnabled );
		setToggleButtonEnabled( BUTTON_LABEL, isLabelEnabled );
	}

	private void createButtonGroup( Composite parent )
	{
		Composite cmp = new Composite( parent, SWT.NONE );
		{
			cmp.setLayout( new GridLayout( 6, false ) );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.horizontalSpan = 2;
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			gridData.horizontalAlignment = SWT.BEGINNING;
			cmp.setLayoutData( gridData );
		}

		ITaskPopupSheet popup;
		if ( getAxisAngleType( ) != AngleType.Z )
		{
			// Scale
			popup = new AxisScaleSheet( Messages.getString( "AxisYSheetImpl.Label.Scale" ), //$NON-NLS-1$
					getContext( ),
					getAxisForProcessing( ),
					getAxisAngleType( ) );
			Button btnScale = createToggleButton( cmp,
					BUTTON_SCALE,
					Messages.getString( "AxisYSheetImpl.Label.Scale&" ), //$NON-NLS-1$
					popup );
			btnScale.addSelectionListener( this );
		}

		// Title
		popup = new AxisTitleSheet( Messages.getString( "AxisYSheetImpl.Label.TitleFormat" ), //$NON-NLS-1$
				getContext( ),
				getAxisForProcessing( ),
				getAxisAngleType( ) );
		Button btnAxisTitle = createToggleButton( cmp,
				BUTTON_TITLE,
				Messages.getString( "AxisYSheetImpl.Label.TitleFormat&" ), //$NON-NLS-1$
				popup,
				vcsTitleVisible.getSelectionIndex( ) == 1);
		btnAxisTitle.addSelectionListener( this );

		// Label
		popup = new AxisLabelSheet( Messages.getString( "AxisYSheetImpl.Label.LabelFormat" ), //$NON-NLS-1$
				getContext( ),
				getAxisForProcessing( ),
				getAxisAngleType( ) );
		Button btnAxisLabel = createToggleButton( cmp,
				BUTTON_LABEL,
				Messages.getString( "AxisYSheetImpl.Label.LabelFormat&" ), //$NON-NLS-1$
				popup,
				vcsLabelVisible.getSelectionIndex( ) == 1 );
		btnAxisLabel.addSelectionListener( this );

		// Gridlines
		popup = new AxisGridLinesSheet( Messages.getString( "AxisYSheetImpl.Label.Gridlines" ), //$NON-NLS-1$
				getContext( ),
				getAxisForProcessing( ),
				getAxisAngleType( ) );
		Button btnGridlines = createToggleButton( cmp,
				BUTTON_GRIDLINES,
				Messages.getString( "AxisYSheetImpl.Label.Gridlines&" ), //$NON-NLS-1$
				popup );
		btnGridlines.addSelectionListener( this );

		if ( getAxisAngleType( ) != AngleType.Z )
		{
			// Marker
			// Marker is not supported for 3D
			createMarkersUI( cmp );
		}

		// Interactivity
		if ( getContext().isInteractivityEnabled( ) )
		{
			popup = new InteractivitySheet( Messages.getString( "AbstractAxisSubtask.Label.Interactivity" ), //$NON-NLS-1$
					getContext( ),
					getAxisForProcessing( ).getTriggers( ),
					getAxisForProcessing( ),
					TriggerSupportMatrix.TYPE_AXIS,
					false,
					true );
			Button btnInteractivity = createToggleButton( cmp,
					BUTTON_INTERACTIVITY,
					Messages.getString( "SeriesYSheetImpl.Label.Interactivity&" ), //$NON-NLS-1$
					popup,
					getChart( ).getInteractivity( ).isEnable( ) );
			btnInteractivity.addSelectionListener( this );
		}
	}

	protected void createMarkersUI( Composite cmp )
	{
		ITaskPopupSheet popup = new AxisMarkersSheet( Messages.getString( "AxisYSheetImpl.Label.Markers" ), //$NON-NLS-1$
				getContext( ),
				getAxisForProcessing( ) );
		Button btnMarkers = createToggleButton( cmp,
				BUTTON_MARKERS,
				Messages.getString( "AxisYSheetImpl.Label.Markers&" ), //$NON-NLS-1$
				popup,
				!ChartUIUtil.is3DType( getChart( ) ) );
		btnMarkers.addSelectionListener( this );
	}

	private void populateLists( )
	{
		// Populate axis types combo
		NameSet ns = LiteralHelper.axisTypeSet;
		if ( getAxisAngleType( ) == AngleType.Y )
		{
			ns = ChartUIUtil.getCompatibleAxisType( getAxisForProcessing( ).getSeriesDefinitions( )
					.get( 0 )
					.getDesignTimeSeries( ) );

		}
		cmbTypes.setItems( ChartUIExtensionUtil.getItemsWithAuto( ns.getDisplayNames( ) ) );
		if ( !getAxisForProcessing( ).isSetType( ) )
		{
			cmbTypes.select( 0 );
		}
		else
		{
			cmbTypes.setText( ns.getDisplayNameByName( getAxisForProcessing( ).getType( )
				.getName( ) ) );
		}
		// Populate origin types combo
		if ( getChart( ).getDimension( ).getValue( ) != ChartDimension.THREE_DIMENSIONAL )
		{
			ns = LiteralHelper.intersectionTypeSet;
			cmbOrigin.setItems( ChartUIExtensionUtil.getItemsWithAuto( ns.getDisplayNames( ) ) );
			if ( !getAxisForProcessing( ).getOrigin( ).isSetType( ) )
			{
				cmbOrigin.select( 0 );
			}
			else
			{
				cmbOrigin.select( ns.getSafeNameIndex( getAxisForProcessing( ).getOrigin( )
					.getType( )
					.getName( ) ) + 1 );
			}
		}

		if ( txtValue != null
				&& getAxisForProcessing( ).getOrigin( )
						.getType( )
						.equals( IntersectionType.VALUE_LITERAL ) )
		{
			txtValue.setDataElement( getAxisForProcessing( ).getOrigin( )
					.getValue( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.
	 * Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( txtTitle ) )
		{
			getAxisForProcessing( ).getTitle( )
					.getCaption( )
					.setValue( (String) event.data );
		}
		else if ( event.widget.equals( txtValue ) )
		{
			DataElement de = txtValue.getDataElement( );
			if ( de != null )
			{
				getAxisForProcessing( ).getOrigin( ).setValue( de );
			}
		}
		else if ( event.widget.equals( fdcFont ) )
		{
			getAxisForProcessing( ).getLabel( )
					.getCaption( )
					.setFont( fdcFont.getFontDefinition( ) );
			getAxisForProcessing( ).getLabel( )
					.getCaption( )
					.setColor( fdcFont.getFontColor( ) );
		}
	}

	private double suggestLabelSpan( Axis ax )
	{
		boolean bHorizontal = ( (ChartWithAxes) getChart( ) ).isTransposed( ) != ( ax.getOrientation( ) == Orientation.HORIZONTAL_LITERAL );

		if ( !bHorizontal )
		{
			if ( ax.getType( ) == AxisType.LINEAR_LITERAL
					|| ax.getType( ) == AxisType.LOGARITHMIC_LITERAL )
			{
				return 30;
			}
			return 50;
		}

		return 16;
	}

	public void widgetSelected( SelectionEvent e )
	{
		// Detach popup dialog if there's selected button.
		if ( detachPopup( e.widget ) )
		{
			return;
		}

		if ( isRegistered( e.widget ) )
		{
			attachPopup( ( (Button) e.widget ).getData( ).toString( ) );
		}

		if ( e.widget == btnFixLabelSpan )
		{
			boolean bLabelThickFixed = btnFixLabelSpan.getSelection( );
			lneLabelSpan.setEnabled( bLabelThickFixed );
			if ( !bLabelThickFixed )
			{
				getAxisForProcessing( ).unsetLabelSpan( );
			}
			else
			{
				if ( lneLabelSpan.getValue( ) == 0 )
				{
					double value = suggestLabelSpan( getAxisForProcessing( ) );
					lneLabelSpan.setValue( value );
				}
				getAxisForProcessing( ).setLabelSpan( lneLabelSpan.getValue( ) );
			}
		}

		if ( e.widget == btnTxtValueAuto )
		{
			if ( btnTxtValueAuto.getSelection( ) )
			{
				getAxisForProcessing( ).getOrigin( ).setValue( null );
				txtValue.setEnabled( false );
			}
			else
			{
				DataElement de = txtValue.getDataElement( );
				getAxisForProcessing( ).getOrigin( ).setValue( de );
				txtValue.setEnabled( true );
			}
		}
		else if ( e.widget.equals( cmbTypes ) )
		{
			final AxisType axisType = AxisType.getByName( LiteralHelper.axisTypeSet.getNameByDisplayName( cmbTypes.getText( ) ) );
			if ( getAxisForProcessing( ).isSetType( )
					&& getAxisForProcessing( ).getType( ) == axisType )
			{
				// Prevent redundant operations
				return;
			}

			// Update the Sample Data and clean related format specifiers
			ChartAdapter.beginIgnoreNotifications( );
			{
				convertSampleData( axisType );
				getAxisForProcessing( ).setFormatSpecifier( null );

				EList<MarkerLine> markerLines = getAxisForProcessing( ).getMarkerLines( );
				for ( int i = 0; i < markerLines.size( ); i++ )
				{
					markerLines.get( i ).setFormatSpecifier( null );
				}

				EList<MarkerRange> markerRanges = getAxisForProcessing( ).getMarkerRanges( );
				for ( int i = 0; i < markerRanges.size( ); i++ )
				{
					markerRanges.get( i ).setFormatSpecifier( null );
				}
			}
			ChartAdapter.endIgnoreNotifications( );

			// Set type and refresh the preview
			if ( cmbTypes.getSelectionIndex( ) == 0 )
			{
				getAxisForProcessing( ).unsetType( );
			}
			else
			{
				getAxisForProcessing( ).setType( axisType );
			}
			if ( cmbCategoryAxis != null )
			{
				updateCategoryAxisUI( !AxisType.TEXT_LITERAL.equals( axisType ) );
			}
			// Update popup UI
			refreshPopupSheet( );
		}
		else if ( e.widget.equals( cmbOrigin ) )
		{
			if ( IntersectionType.VALUE_LITERAL.getName( )
					.equals( LiteralHelper.intersectionTypeSet.getNameByDisplayName( cmbOrigin.getText( ) ) ) )
			{
				lblValue.setEnabled( true );
				boolean enabled = true && ( getAxisForProcessing( ).getOrigin( ).getValue( ) != null );
				txtValue.setEnabled( enabled );
				btnTxtValueAuto.setEnabled( true );
				
				getAxisForProcessing( ).getOrigin( )
						.setType( IntersectionType.getByName( LiteralHelper.intersectionTypeSet.getNameByDisplayName( cmbOrigin.getText( ) ) ) );
			}
			else
			{
				if ( cmbOrigin.getSelectionIndex( ) == 0 )
				{
					getAxisForProcessing( ).getOrigin( ).unsetType( );
					getAxisForProcessing( ).getOrigin( ).setValue( null );
				}
				else
				{
					getAxisForProcessing( ).getOrigin( )
					.setType( IntersectionType.getByName( LiteralHelper.intersectionTypeSet.getNameByDisplayName( cmbOrigin.getText( ) ) ) );
				}
				lblValue.setEnabled( false );
				txtValue.setEnabled( false );
				btnTxtValueAuto.setEnabled( false );
			}
			
			if ( getAxisForProcessing( ).getOrigin( ).isSetType( )
					&& getAxisForProcessing( ).getOrigin( )
							.getType( )
							.getValue( ) == IntersectionType.VALUE )
			{
				// reset value to convert type
				getAxisForProcessing( ).getOrigin( )
						.setValue( txtValue.getDataElement( ) );
			}
		}
		else if ( e.widget.equals( cmbCategoryAxis ) )
		{
			int index = cmbCategoryAxis.getSelectionIndex( );
			if ( cmbCategoryAxis.getSelectionIndex( ) == 0 )
			{
				// Auto case.
				getAxisForProcessing( ).unsetCategoryAxis( );
			}
			else
			{
				getAxisForProcessing( ).setCategoryAxis( index == 1 );
			}
			ChartCacheManager.getInstance( )
					.cacheCategory( ( (ChartWithAxes) getChart( ) ).getType( ),
							index == 1 );
			refreshPopupSheet( );

			// Reset reverse category settings which is only available when axis
			// is category
			cmbReverse.setEnabled( index == 1 );
			updateReverseUI( index == 1 );
			if ( index == 0 )
			{
				cmbReverse.select( 0 );
				( (ChartWithAxes) getChart( ) ).unsetReverseCategory( );
			}
			else if ( index == 2 )
			{
				cmbReverse.select( 2 );
				( (ChartWithAxes) getChart( ) ).setReverseCategory( false );
			}
		}
		else if ( e.widget.equals( cmbReverse ) )
		{
			if ( cmbReverse.getSelectionIndex( ) ==  0 )
			{
				( (ChartWithAxes) getChart( ) ).unsetReverseCategory( );
			}
			else
			{
				( (ChartWithAxes) getChart( ) ).setReverseCategory( cmbReverse.getSelectionIndex( ) == 1 );
			}
		}
		else if ( e.widget == vcsTitleVisible )
		{
			if ( vcsTitleVisible.getSelectionIndex( ) == 0 )
			{
				getAxisForProcessing( ).getTitle( ).unsetVisible( );
			}
			else
			{
				getAxisForProcessing( ).getTitle( )
					.setVisible( vcsTitleVisible.getSelectionIndex( ) == 1 );
			}
			setStateOfTitle( );
			Button btnAxisTitle = getToggleButton( BUTTON_TITLE );
			if ( !( vcsTitleVisible.getSelectionIndex( ) == 1 )
					&& btnAxisTitle.getSelection( ) )
			{
				btnAxisTitle.setSelection( false );
				detachPopup( btnAxisTitle );
			}
			else
			{
				refreshPopupSheet( );
			}
		}
		else if ( e.widget == vcsLabelVisible )
		{
			if ( vcsLabelVisible.getSelectionIndex( ) == 0 )
			{
				getAxisForProcessing( ).getLabel( ).unsetVisible( );
			}
			else
			{
				getAxisForProcessing( ).getLabel( )
					.setVisible( vcsLabelVisible.getSelectionIndex( ) == 1 );
			}
			setStateOfLabel( );
			Button btnAxisLabel = getToggleButton( BUTTON_LABEL );
			if ( !( vcsLabelVisible.getSelectionIndex( ) == 1 ) 
					&& btnAxisLabel.getSelection( ) )
			{
				btnAxisLabel.setSelection( false );
				detachPopup( btnAxisLabel );
			}
			else
			{
				refreshPopupSheet( );
			}
		}
		else if ( e.widget.equals( btnFormatSpecifier ) )
		{
			handleFormatBtnSelected( );
		}
		else if ( e.widget == cscStaggered )
		{
			if ( cscStaggered.getSelectionIndex( ) == 0 )
			{
				getAxisForProcessing( ).unsetStaggered( );
			}
			else
			{
				getAxisForProcessing( ).setStaggered( cscStaggered.getSelectionIndex( ) == 1);
			}
		}
	}

	protected void handleFormatBtnSelected( )
	{
		String sAxisTitle = Messages.getString( "OrthogonalAxisDataSheetImpl.Lbl.OrthogonalAxis" ); //$NON-NLS-1$
		try
		{
			String sTitleString = getAxisForProcessing( ).getTitle( )
					.getCaption( )
					.getValue( );
			int iSeparatorIndex = sTitleString.indexOf( ExternalizedTextEditorComposite.SEPARATOR );
			if ( iSeparatorIndex > 0 )
			{
				sTitleString = sTitleString.substring( iSeparatorIndex );
			}
			else if ( iSeparatorIndex == 0 )
			{
				sTitleString = sTitleString.substring( ExternalizedTextEditorComposite.SEPARATOR.length( ) );
			}
			sAxisTitle += " (" + sTitleString + ")"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		catch ( NullPointerException e1 )
		{
		}

		FormatSpecifier formatspecifier = null;
		if ( getAxisForProcessing( ).getFormatSpecifier( ) != null )
		{
			formatspecifier = getAxisForProcessing( ).getFormatSpecifier( );
		}
		FormatSpecifierDialog editor = new FormatSpecifierDialog( cmpContent.getShell( ),
				formatspecifier,
				getAxisForProcessing( ).getType( ),
				sAxisTitle );
		if ( editor.open( ) == Window.OK )
		{
			if ( editor.getFormatSpecifier( ) == null )
			{
				getAxisForProcessing( ).eUnset( ComponentPackage.eINSTANCE.getAxis_FormatSpecifier( ) );
			}
			else
			{
				getAxisForProcessing( ).setFormatSpecifier( editor.getFormatSpecifier( ) );
			}
		}
	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub
	}

	private void convertSampleData( AxisType axisType )
	{
		if ( getAxisAngleType( ) == AngleType.X )
		{
			if ( axisType == null )
			{
				axisType = AxisType.TEXT_LITERAL;
			}
			BaseSampleData bsd = getChart( ).getSampleData( )
					.getBaseSampleData( )
					.get( 0 );
			bsd.setDataSetRepresentation( ChartUIUtil.getConvertedSampleDataRepresentation( axisType,
					bsd.getDataSetRepresentation( ),
					0 ) );
		}
		else if ( getAxisAngleType( ) == AngleType.Y )
		{
			if ( axisType == null )
			{
				axisType = AxisType.LINEAR_LITERAL;
			}
			// Run the conversion routine for ALL orthogonal sample data entries
			// related to series definitions for this axis
			// Get the start and end index of series definitions that fall under
			// this axis
			int iStartIndex = getFirstSeriesDefinitionIndexForAxis( );
			int iEndIndex = iStartIndex
					+ getAxisForProcessing( ).getSeriesDefinitions( ).size( );
			// for each entry in orthogonal sample data, if the series index for
			// the
			// entry is in this range...run conversion
			// routine
			int iOSDSize = getChart( ).getSampleData( )
					.getOrthogonalSampleData( )
					.size( );
			for ( int i = 0; i < iOSDSize; i++ )
			{
				OrthogonalSampleData osd = getChart( ).getSampleData( )
						.getOrthogonalSampleData( )
						.get( i );
				if ( osd.getSeriesDefinitionIndex( ) >= iStartIndex
						&& osd.getSeriesDefinitionIndex( ) <= iEndIndex )
				{
					osd.setDataSetRepresentation( ChartUIUtil.getConvertedSampleDataRepresentation( axisType,
							osd.getDataSetRepresentation( ),
							i ) );
				}
			}
		}
	}

	private int getFirstSeriesDefinitionIndexForAxis( )
	{
		int iTmp = 0;
		for ( int i = 0; i < getIndex( ); i++ )
		{
			iTmp += ChartUIUtil.getAxisYForProcessing( (ChartWithAxes) getChart( ),
					i )
					.getSeriesDefinitions( )
					.size( );
		}
		return iTmp;
	}

	public void modifyText( ModifyEvent e )
	{
		if ( e.widget == lneLabelSpan )
		{
			getAxisForProcessing( ).setLabelSpan( lneLabelSpan.getValue( ) );
		}
	}
	
	private Axis getOppositeAxis( )
	{
		if ( getAxisAngleType( ) == AngleType.X )
		{
			return ChartUIUtil.getAxisYForProcessing( (ChartWithAxes) getChart( ),
					0 );
		}
		return ChartUIUtil.getAxisXForProcessing( (ChartWithAxes) getChart( ) );
	}

	private IDataElementComposite createDataElementComposite( Composite parent )
	{
		Axis oAxis = getOppositeAxis( );
		DataElement data = getAxisForProcessing( ).getOrigin( ).getValue( );
		if ( oAxis.getType( ).getValue( ) == AxisType.DATE_TIME
				&& !( oAxis.isCategoryAxis( ) ) )
		{
			if ( !( data instanceof DateTimeDataElement ) )
			{
				data = DateTimeDataElementImpl.create( new CDateTime( 1970,
						1,
						1,
						0,
						0,
						0 ) );
			}
			return new DateTimeDataElementComposite( parent,
					SWT.BORDER,
					(DateTimeDataElement) data,
					false );
		}

		// Use number value for Linear or Text element
		if ( !( data instanceof NumberDataElement ) )
		{
			data = NumberDataElementImpl.create( 0 );
		}
		return new NumberDataElementComposite( parent, data );
	}
}