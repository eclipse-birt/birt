
package org.eclipse.birt.report.designer.internal.ui.views.attributes.section;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.OutputPropertyDescriptor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class OutputSection extends Section
{

	public OutputSection( Composite parent, boolean isFormStyle )
	{
		super( " ", parent, isFormStyle );
	}

	protected OutputPropertyDescriptor output;

	public void createSection( )
	{
		getOutputControl( parent );
		getGridPlaceholder( parent );
	}

	public OutputPropertyDescriptor getOutputControl( )
	{
		return output;
	}

	protected OutputPropertyDescriptor getOutputControl( Composite parent )
	{
		if ( output == null )
		{
			output = new OutputPropertyDescriptor( true );
			output.setDescriptorProvider( provider );
			output.createControl( parent );
			output.getControl( )
					.setLayoutData( new GridData( GridData.FILL_BOTH ) );
			output.getControl( ).addDisposeListener( new DisposeListener( ) {

				public void widgetDisposed( DisposeEvent event )
				{
					output = null;
				}
			} );
		}
		else
		{
			checkParent( output.getControl( ), parent );
		}
		return output;
	}

	public void layout( )
	{
		GridData gd = (GridData) output.getControl( ).getLayoutData( );
		if ( getLayoutNum( ) > 0 )
			gd.horizontalSpan = getLayoutNum( ) - placeholder;
		else
			gd.horizontalSpan = ( (GridLayout) parent.getLayout( ) ).numColumns
					- placeholder;
		if ( height > -1 )
		{
			gd.heightHint = height;
			gd.grabExcessVerticalSpace = false;
			if ( displayLabel != null )
			{
				gd = (GridData) displayLabel.getLayoutData( );
				gd.verticalAlignment = GridData.VERTICAL_ALIGN_FILL;
			}
		}
	}

	private int height = -1;

	public void setHeight( int height )
	{
		this.height = height;
	}

	public void load( )
	{
		if ( output != null && !output.getControl( ).isDisposed( ) )
			output.load( );
	}

	IDescriptorProvider provider;

	public IDescriptorProvider getProvider( )
	{
		return provider;
	}

	public void setProvider( IDescriptorProvider provider )
	{
		this.provider = provider;
		if ( output != null )
			output.setDescriptorProvider( provider );
	}

	public void setInput( Object input )
	{
		assert ( input != null );
		output.setInput( input );
	}

	public void setHidden( boolean isHidden )
	{
		if ( output != null )
			WidgetUtil.setExcludeGridData( output.getControl( ), isHidden );

	}

	public void setVisible( boolean isVisable )
	{
		if ( output != null )
			output.getControl( ).setVisible( isVisable );

	}

}
