
package org.eclipse.birt.report.designer.internal.ui.views.attributes.section;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DescriptorToolkit;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.ColorPropertyDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class ColorSection extends Section
{

	public ColorSection( String labelText, Composite parent, boolean isFormStyle )
	{
		super( labelText, parent, isFormStyle );
		// TODO Auto-generated constructor stub
	}

	protected ColorPropertyDescriptor color;

	public void createSection( )
	{
		getLabelControl( parent );
		getColorComboControl( parent );
		getGridPlaceholder( parent );

	}

	protected ColorPropertyDescriptor getColorComboControl( Composite parent )
	{
		if ( color == null )
		{
			color = DescriptorToolkit.createColorPropertyDescriptor( true );
			if ( getProvider( ) != null )
				color.setDescriptorProvider( getProvider( ) );
			color.createControl( parent );
			color.getControl( ).setLayoutData( new GridData( ) );
			color.getControl( ).addDisposeListener( new DisposeListener( ) {

				public void widgetDisposed( DisposeEvent event )
				{
					color = null;
				}
			} );
			if ( colorValue != null )
				color.setColorValue( colorValue );
		}
		else
		{
			checkParent( color.getControl( ), parent );
		}
		return color;
	}

	public ColorPropertyDescriptor getColorComboControl( )
	{
		return color;
	}

	public void layout( )
	{
		GridData gd = (GridData) color.getControl( ).getLayoutData( );
		if ( getLayoutNum( ) > 0 )
			gd.horizontalSpan = getLayoutNum( ) - 1 - placeholder;
		else
			gd.horizontalSpan = ( (GridLayout) parent.getLayout( ) ).numColumns
					- 1
					- placeholder;
		gd.horizontalAlignment = SWT.FILL;
		if ( width > -1 )
		{
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		}
		else
			gd.grabExcessHorizontalSpace = fillColor;

	}

	public void load( )
	{
		color.load( );

	}

	IDescriptorProvider provider;

	public IDescriptorProvider getProvider( )
	{
		return provider;
	}

	public void setProvider( IDescriptorProvider provider )
	{
		this.provider = provider;
		if ( color != null )
			color.setDescriptorProvider( provider );
	}

	private int width = -1;

	public int getWidth( )
	{
		return width;
	}

	public void setWidth( int width )
	{
		this.width = width;
	}

	public void setInput( Object input )
	{
		assert ( input != null );
		color.setInput( input );
	}

	boolean fillColor = false;

	public boolean isFillColor( )
	{
		return fillColor;
	}

	public void setFillColor( boolean fillColor )
	{
		this.fillColor = fillColor;
	}

	public void setHidden( boolean isHidden )
	{
		if ( displayLabel != null )
			WidgetUtil.setExcludeGridData( displayLabel, isHidden );
		if ( color != null )
			color.setHidden( isHidden );
		if ( placeholderLabel != null )
			WidgetUtil.setExcludeGridData( placeholderLabel, isHidden );
	}

	public void setVisible( boolean isVisible )
	{
		if ( displayLabel != null )
			displayLabel.setVisible( isVisible );
		if ( color != null )
			color.setVisible( isVisible );
		if ( placeholderLabel != null )
			placeholderLabel.setVisible( isVisible );
	}

	public void setFocus( )
	{
		if ( color != null )
		{
			color.getControl( ).setFocus( );
		}
	}

	private String colorValue;

	public void setColorValue( String value )
	{
		if ( color != null )
			color.setColorValue( value );
		colorValue = value;
	}

	public RGB getColorValue( )
	{
		if ( color != null )
			return color.getColorValue( );
		else
			return null;
	}

}
