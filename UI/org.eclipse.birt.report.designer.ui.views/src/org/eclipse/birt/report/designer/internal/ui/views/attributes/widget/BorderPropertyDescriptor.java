
package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BorderColorDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BorderStyleDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BorderToggleDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.BorderWidthDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IToggleDescriptorProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.views.attributes.IPropertyDescriptor;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.metadata.IColorConstants;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class BorderPropertyDescriptor implements IPropertyDescriptor, Listener
{

	private boolean isFormStyle;

	private BorderInfomation restoreInfo;

	private static final RGB autoColor = DEUtil.getRGBValue( ColorUtil.parsePredefinedColor( "black" ) );

	public BorderPropertyDescriptor( boolean isFormStyle )
	{
		this.isFormStyle = isFormStyle;
	}

	public Control createControl( Composite parent )
	{
		content = new Composite( parent, SWT.NONE );
		GridLayout layout = UIUtil.createGridLayoutWithoutMargin( 2, false );
		layout.marginHeight = 1;
		layout.marginWidth = 1;
		layout.horizontalSpacing = 10;
		content.setLayout( layout );
		content.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		Composite choices = new Composite( content, SWT.NONE );
		GridData data = new GridData( GridData.FILL_BOTH );

		choices.setLayoutData( data );
		layout = WidgetUtil.createGridLayout( 2 );
		layout.marginHeight = 1;
		layout.marginWidth = 2;
		choices.setLayout( layout );

		Label styleLabel = FormWidgetFactory.getInstance( )
				.createLabel( choices, SWT.LEFT, isFormStyle );
		styleLabel.setText( styleProvider.getDisplayName( ) );
		styleLabel.setLayoutData( new GridData( ) );

		if ( isFormStyle )
		{
			styleCombo = FormWidgetFactory.getInstance( )
					.createStyleCombo( choices, (IComboProvider) styleProvider );
		}
		else
		{
			styleCombo = new StyleCombo( choices,
					style,
					(IComboProvider) styleProvider );
		}
		data = new GridData( );
		data.widthHint = 200;
		styleCombo.setLayoutData( data );
		styleCombo.setItems( ( (IComboProvider) styleProvider ).getItems( ) );

		Label colorLabel = FormWidgetFactory.getInstance( )
				.createLabel( choices, SWT.LEFT, isFormStyle );
		colorLabel.setText( colorProvider.getDisplayName( ) );
		colorLabel.setLayoutData( new GridData( ) );

		builder = new ColorBuilder( choices, SWT.NONE, isFormStyle );
		builder.setChoiceSet( colorProvider.getElementChoiceSet( ) );
		colorProvider.setIndex( IColorConstants.BLACK );
		data = new GridData( );
		data.widthHint = 200;
		data.heightHint = builder.computeSize( SWT.DEFAULT, SWT.DEFAULT ).y;
		builder.setLayoutData( data );

		Label widthLabel = FormWidgetFactory.getInstance( )
				.createLabel( choices, SWT.LEFT, isFormStyle );
		widthLabel.setText( widthProvider.getDisplayName( ) );
		widthLabel.setLayoutData( new GridData( ) );

		if ( isFormStyle )
		{
			widthCombo = FormWidgetFactory.getInstance( )
					.createStyleCombo( choices, (IComboProvider) widthProvider );
		}
		else
		{
			widthCombo = new StyleCombo( choices,
					style,
					(IComboProvider) widthProvider );
		}
		widthProvider.setIndex( widthProvider.getItems( )[1].toString( ) );
		data = new GridData( );
		data.widthHint = 200;
		widthCombo.setLayoutData( data );
		widthCombo.setItems( ( (IComboProvider) widthProvider ).getItems( ) );

		Composite composite = new Composite( choices, SWT.NONE );
		layout = new GridLayout( );
		layout.horizontalSpacing = 7;
		layout.numColumns = toggleProviders.length + 1;
		composite.setLayout( layout );
		data = new GridData( );
		data.horizontalSpan = 2;
		composite.setLayoutData( data );

		toggles = new Button[toggleProviders.length];
		for ( int i = 0; i < toggleProviders.length; i++ )
		{
			Button button = new Button( composite, SWT.TOGGLE );
			toggles[i] = button;
			button.setLayoutData( new GridData( ) );
			button.setToolTipText( toggleProviders[i].getTooltipText( ) );
			button.setImage( ReportPlatformUIImages.getImage( toggleProviders[i].getImageName( ) ) );
			final BorderToggleDescriptorProvider provider = toggleProviders[i];
			button.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent e )
				{
					if ( ( (Button) e.widget ).getSelection( ) )
					{
						SessionHandleAdapter.getInstance( )
								.getCommandStack( )
								.startTrans( Messages.getString( "BordersPage.Trans.SelectBorder" ) );

						BorderInfomation information = new BorderInfomation( );

						information.setPosition( provider.getPosition( ) );
						information.setColor( builder.getRGB( ) );
						information.setStyle( (String) styleCombo.getSelectedItem( ) );
						information.setWidth( (String) widthCombo.getSelectedItem( ) );
						previewCanvas.setBorderInfomation( information );
						restoreInfo = information;
						try
						{
							provider.save( information );
						}
						catch ( Exception e1 )
						{
							ExceptionHandler.handle( e1 );
						}
						checkToggleButtons( );

						SessionHandleAdapter.getInstance( )
								.getCommandStack( )
								.commit( );
					}
					else
					{
						BorderInfomation oldInfo = (BorderInfomation) provider.load( );
						RGB oldColor = oldInfo.getColor( );
						RGB selectedColor = builder.getRGB( );
						if ( oldColor == null )
						{
							oldColor = autoColor;
						}
						if ( selectedColor == null )
						{
							selectedColor = autoColor;
						}
						if ( !( oldInfo.getStyle( ).equals( (String) styleCombo.getSelectedItem( ) ) )
								|| !( oldColor.equals( selectedColor ) )
								|| !( oldInfo.getWidth( ).equals( (String) widthCombo.getSelectedItem( ) ) ) )
						{
							SessionHandleAdapter.getInstance( )
									.getCommandStack( )
									.startTrans( Messages.getString( "BordersPage.Trans.SelectBorder" ) );

							BorderInfomation information = new BorderInfomation( );

							information.setPosition( provider.getPosition( ) );
							information.setColor( selectedColor );
							information.setStyle( (String) styleCombo.getSelectedItem( ) );
							information.setWidth( (String) widthCombo.getSelectedItem( ) );
							previewCanvas.setBorderInfomation( information );
							restoreInfo = information;
							try
							{
								provider.save( information );
							}
							catch ( Exception e1 )
							{
								ExceptionHandler.handle( e1 );
							}
							( (Button) e.widget ).setSelection( true );
							SessionHandleAdapter.getInstance( )
									.getCommandStack( )
									.commit( );
						}
						else
						{
							SessionHandleAdapter.getInstance( )
									.getCommandStack( )
									.startTrans( Messages.getString( "BordersPage.Trans.UnSelectBorder" ) );

							previewCanvas.removeBorderInfomation( provider.getPosition( ) );
							if ( allButton.getSelection( ) )
								allButton.setSelection( false );
							try
							{
								provider.reset( );
							}
							catch ( Exception e1 )
							{
								ExceptionHandler.handle( e1 );
							}
							SessionHandleAdapter.getInstance( )
									.getCommandStack( )
									.commit( );
						}
					}
					previewCanvas.redraw( );
				}

			} );
		}

		allButton = new Button( composite, SWT.TOGGLE );
		allButton.setImage( ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_FRAME ) );
		allButton.setToolTipText( Messages.getString( "BordersPage.Tooltip.All" ) );
		allButton.setLayoutData( new GridData( ) );
		allButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				RGB selectedColor = null;
				RGB oldColor = null;
				if ( ( (Button) e.widget ).getSelection( ) )
				{
					SessionHandleAdapter.getInstance( )
							.getCommandStack( )
							.startTrans( Messages.getString( "BordersPage.Trans.SelectAllborders" ) );
					selectedColor = builder.getRGB( );
					if ( selectedColor == null )
					{
						selectedColor = autoColor;
					}
					for ( int i = 0; i < toggleProviders.length; i++ )
					{
						BorderInfomation information = new BorderInfomation( );
						information.setPosition( toggleProviders[i].getPosition( ) );
						information.setColor( selectedColor );
						information.setStyle( (String) styleCombo.getSelectedItem( ) );
						information.setWidth( (String) widthCombo.getSelectedItem( ) );
						toggles[i].setSelection( true );
						previewCanvas.setBorderInfomation( information );
						restoreInfo = information;
						try
						{
							toggleProviders[i].save( information );
						}
						catch ( Exception e1 )
						{
							ExceptionHandler.handle( e1 );
						}
					}
					// restoreInfo = (BorderInfomation)
					// toggleProviders[toggleProviders.length - 1].load( );
					// restoreInfo.setColor( selectedColor );
					SessionHandleAdapter.getInstance( )
							.getCommandStack( )
							.commit( );
				}
				else
				{
					boolean reset = true;
					for ( int i = 0; i < toggleProviders.length; i++ )
					{
						BorderInfomation info = (BorderInfomation) toggleProviders[i].load( );
						oldColor = info.getColor( );
						selectedColor = builder.getRGB( );
						if ( oldColor == null )
						{
							oldColor = autoColor;
						}
						if ( selectedColor == null )
						{
							selectedColor = autoColor;
						}
						if ( !( info.getStyle( ).equals( (String) styleCombo.getSelectedItem( ) ) )
								|| !( oldColor.equals( selectedColor ) )
								|| !( info.getWidth( ).equals( (String) widthCombo.getSelectedItem( ) ) ) )
						{
							reset = false;
							break;
						}
					}
					if ( reset )
					{
						SessionHandleAdapter.getInstance( )
								.getCommandStack( )
								.startTrans( Messages.getString( "BordersPage.Trans.UnSelectAllborders" ) );

						for ( int i = 0; i < toggleProviders.length; i++ )
						{
							previewCanvas.removeBorderInfomation( toggleProviders[i].getPosition( ) );
							toggles[i].setSelection( false );
							try
							{
								toggleProviders[i].reset( );
							}
							catch ( Exception e1 )
							{
								ExceptionHandler.handle( e1 );
							}
						}
						SessionHandleAdapter.getInstance( )
								.getCommandStack( )
								.commit( );
					}
					else
					{
						SessionHandleAdapter.getInstance( )
								.getCommandStack( )
								.startTrans( Messages.getString( "BordersPage.Trans.SelectAllborders" ) );

						for ( int i = 0; i < toggleProviders.length; i++ )
						{
							BorderInfomation information = new BorderInfomation( );

							information.setPosition( toggleProviders[i].getPosition( ) );
							if ( builder.getRGB( ) == null )
							{
								information.setColor( autoColor );
							}
							else
							{
								information.setColor( builder.getRGB( ) );
							}
							information.setStyle( (String) styleCombo.getSelectedItem( ) );
							information.setWidth( (String) widthCombo.getSelectedItem( ) );
							previewCanvas.setBorderInfomation( information );
							restoreInfo = information;
							try
							{
								toggleProviders[i].save( information );
							}
							catch ( Exception e1 )
							{
								ExceptionHandler.handle( e1 );
							}
						}
						( (Button) e.widget ).setSelection( true );
						SessionHandleAdapter.getInstance( )
								.getCommandStack( )
								.commit( );
					}
				}
				previewCanvas.redraw( );
			}
		} );

		Composite previewContainer = new Composite( content, SWT.NONE );
		data = new GridData( GridData.FILL_BOTH );
		previewContainer.setLayoutData( data );
		layout = new GridLayout( );
		layout.numColumns = 2;
		layout.marginHeight = 1;
		layout.marginWidth = 10;
		previewContainer.setLayout( layout );

		Label previewLabel = FormWidgetFactory.getInstance( )
				.createLabel( previewContainer, SWT.LEFT, isFormStyle );
		data = new GridData( GridData.VERTICAL_ALIGN_BEGINNING );
		previewLabel.setLayoutData( data );
		previewLabel.setText( Messages.getString( "BordersPage.text.Preview" ) );

		previewCanvas = new BorderCanvas( previewContainer, SWT.NONE );
		data = new GridData( );
		data.widthHint = 130;
		data.heightHint = 130;
		previewCanvas.setLayoutData( data );
		return content;
	}

	public Control getControl( )
	{
		// TODO Auto-generated method stub
		return content;
	}

	protected Object input;

	public void setInput( Object input )
	{
		this.input = input;
		styleProvider.setInput( input );
		colorProvider.setInput( input );
		widthProvider.setInput( input );
		for ( int i = 0; i < toggleProviders.length; i++ )
		{
			toggleProviders[i].setInput( input );
		}
	}

	void refreshStyle( String value )
	{
		styleCombo.setSelectedItem( value );
	}

	void refreshWidth( String value )
	{
		widthCombo.setSelectedItem( value );
	}

	public void refreshColor( RGB rgb )
	{
		if ( rgb != null )
		{
			builder.setColorValue( ColorUtil.format( ColorUtil.formRGB( rgb.red,
					rgb.green,
					rgb.blue ),
					ColorUtil.CSS_ABSOLUTE_FORMAT ) );
		}
	}

	public void load( )
	{
		// for ( int i = toggleProviders.length - 1; i >= 0; i-- )
		for ( int i = 0; i < toggleProviders.length; i++ )
		{
			BorderInfomation info = (BorderInfomation) toggleProviders[i].load( );
			previewCanvas.setBorderInfomation( info );
			if ( !info.getStyle( ).equals( "" ) )
			{
				toggles[i].setSelection( true );
			}
			else
			{
				toggles[i].setSelection( false );
			}
		}
		previewCanvas.redraw( );
		if ( restoreInfo == null )
		{
			if ( styleCombo.getSelectedItem( ) == null )
			{
				String borderStyle = styleProvider.load( ).toString( );
				refreshStyle( borderStyle );
			}
			if ( widthCombo.getSelectedItem( ) == null )
			{
				String borderWidth = widthProvider.load( ).toString( );
				refreshWidth( borderWidth );
			}
			if ( builder.getRGB( ) == null )
			{
				String borderColor = colorProvider.load( ).toString( );
				refreshColor( borderColor );
			}
		}
		else
		{
			refreshStyle( restoreInfo.getStyle( ) );
			refreshWidth( restoreInfo.getWidth( ) );
			refreshColor( restoreInfo.getColor( ) );
		}
		checkToggleButtons( );
	}

	public void refreshColor( String value )
	{

		boolean stateFlag = ( ( value == null ) == builder.getEnabled( ) );
		if ( stateFlag )
			builder.setEnabled( value != null );
		builder.setColorValue( value );
	}

	public void save( Object obj ) throws SemanticException
	{

	}

	private BorderStyleDescriptorProvider styleProvider = null;

	public void setStyleProvider( IDescriptorProvider provider )
	{
		if ( provider instanceof BorderStyleDescriptorProvider )
			this.styleProvider = (BorderStyleDescriptorProvider) provider;
	}

	private BorderColorDescriptorProvider colorProvider = null;

	public void setColorProvider( IDescriptorProvider provider )
	{
		if ( provider instanceof BorderColorDescriptorProvider )
			this.colorProvider = (BorderColorDescriptorProvider) provider;
	}

	private BorderWidthDescriptorProvider widthProvider = null;
	private Composite content;

	public void setWidthProvider( IDescriptorProvider provider )
	{
		if ( provider instanceof BorderWidthDescriptorProvider )
			this.widthProvider = (BorderWidthDescriptorProvider) provider;
	}

	private int style = SWT.BORDER;
	private StyleCombo styleCombo;
	private StyleCombo widthCombo;

	public int getStyle( )
	{
		return style;
	}

	public void setStyle( int style )
	{
		this.style = style;
	}

	public void setHidden( boolean isHidden )
	{
		WidgetUtil.setExcludeGridData( content, isHidden );
	}

	public void setVisible( boolean isVisible )
	{
		content.setVisible( isVisible );
	}

	BorderToggleDescriptorProvider[] toggleProviders;
	private ColorBuilder builder;
	private Button[] toggles;
	private BorderCanvas previewCanvas;
	private Button allButton;

	public IToggleDescriptorProvider[] getToggleProviders( )
	{
		return toggleProviders;
	}

	public void setToggleProviders(
			BorderToggleDescriptorProvider[] toggleProviders )
	{
		this.toggleProviders = toggleProviders;
	}

	private void checkToggleButtons( )
	{
		boolean allSelected = true;
		for ( int i = 0; i < toggles.length; i++ )
		{
			if ( !toggles[i].getSelection( ) )
			{
				allSelected = false;
				break;
			}
		}
		if ( allSelected )
			allButton.setSelection( true );
		else
			allButton.setSelection( false );
	}

	public void elementChanged( DesignElementHandle focus, NotificationEvent ev )
	{
		PropertyEvent event = (PropertyEvent) ev;
		String propertyName = event.getPropertyName( );
		if ( propertyName.equals( StyleHandle.BORDER_BOTTOM_WIDTH_PROP )
				|| propertyName.equals( StyleHandle.BORDER_TOP_WIDTH_PROP )
				|| propertyName.equals( StyleHandle.BORDER_LEFT_WIDTH_PROP )
				|| propertyName.equals( StyleHandle.BORDER_RIGHT_WIDTH_PROP ) )
		{
			load( );
		}
		else if ( propertyName.equals( StyleHandle.BORDER_BOTTOM_STYLE_PROP )
				|| propertyName.equals( StyleHandle.BORDER_TOP_STYLE_PROP )
				|| propertyName.equals( StyleHandle.BORDER_LEFT_STYLE_PROP )
				|| propertyName.equals( StyleHandle.BORDER_RIGHT_STYLE_PROP ) )
		{
			load( );
		}
		else if ( propertyName.equals( StyleHandle.BORDER_BOTTOM_COLOR_PROP )
				|| propertyName.equals( StyleHandle.BORDER_LEFT_COLOR_PROP )
				|| propertyName.equals( StyleHandle.BORDER_RIGHT_COLOR_PROP )
				|| propertyName.equals( StyleHandle.BORDER_TOP_COLOR_PROP ) )
		{
			load( );
		}
	}
}
