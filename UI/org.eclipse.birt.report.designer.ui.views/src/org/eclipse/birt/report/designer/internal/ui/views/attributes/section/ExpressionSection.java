
package org.eclipse.birt.report.designer.internal.ui.views.attributes.section;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DescriptorToolkit;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.ExpressionPropertyDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class ExpressionSection extends Section
{

	public ExpressionSection( String labelText, Composite parent,
			boolean isFormStyle )
	{
		super( labelText, parent, isFormStyle );
		// TODO Auto-generated constructor stub
	}

	protected ExpressionPropertyDescriptor expression;

	public void createSection( )
	{
		getLabelControl( parent );
		getExpressionControl( parent );
		getGridPlaceholder( parent );

	}

	protected ExpressionPropertyDescriptor getExpressionControl(
			Composite parent )
	{
		if ( expression == null )
		{
			expression = DescriptorToolkit.createExpressionPropertyDescriptor( true );
			if ( getProvider( ) != null )
				expression.setDescriptorProvider( getProvider( ) );
			expression.createControl( parent );
			expression.getControl( ).setLayoutData( new GridData( ) );
			expression.getControl( )
					.addDisposeListener( new DisposeListener( ) {

						public void widgetDisposed( DisposeEvent event )
						{
							expression = null;
						}
					} );
			if ( buttonText != null )
				expression.setButtonText( buttonText );
		}
		else
		{
			checkParent( expression.getControl( ), parent );
		}
		return expression;
	}

	public ExpressionPropertyDescriptor getExpressionControl( )
	{
		return expression;
	}

	public void layout( )
	{
		GridData gd = (GridData) expression.getControl( ).getLayoutData( );
		if ( getLayoutNum( ) > 1 + placeholder )
			gd.horizontalSpan = getLayoutNum( ) - 1 - placeholder;
		else if ( ( (GridLayout) parent.getLayout( ) ).numColumns > -1
				- placeholder )
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
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = GridData.FILL;

	}

	public void load( )
	{
		expression.load( );

	}

	IDescriptorProvider provider;

	public IDescriptorProvider getProvider( )
	{
		return provider;
	}

	public void setProvider( IDescriptorProvider provider )
	{
		this.provider = provider;
		if ( expression != null )
			expression.setDescriptorProvider( provider );
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
		expression.setInput( input );
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
		if ( expression != null )
			expression.setHidden( isHidden );
		if ( placeholderLabel != null )
			WidgetUtil.setExcludeGridData( placeholderLabel, isHidden );
	}

	public void setVisible( boolean isVisible )
	{
		if ( displayLabel != null )
			displayLabel.setVisible( isVisible );
		if ( expression != null )
			expression.setVisible( isVisible );
		if ( placeholderLabel != null )
			placeholderLabel.setVisible( isVisible );
	}

	private String buttonText;

	public void setButtonText( String text )
	{
		buttonText = text;
		if ( expression != null )
			expression.setButtonText( text );
	}

	public String getButtonText( )
	{
		return buttonText;
	}

}
