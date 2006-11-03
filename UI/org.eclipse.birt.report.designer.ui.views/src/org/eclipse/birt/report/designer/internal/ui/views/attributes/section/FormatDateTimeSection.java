
package org.eclipse.birt.report.designer.internal.ui.views.attributes.section;

import org.eclipse.birt.report.designer.internal.ui.dialogs.IFormatChangeListener;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormatDateTimeDescriptor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class FormatDateTimeSection extends Section
{

	private int style;

	public FormatDateTimeSection( Composite parent, int style,
			boolean isFormStyle )
	{
		super( " ", parent, isFormStyle );
		this.style = style;
	}

	protected FormatDateTimeDescriptor format;

	public void createSection( )
	{
		getFormatControl( parent );
		getGridPlaceholder( parent );
	}

	public FormatDateTimeDescriptor getFormatControl( )
	{
		return format;
	}

	protected FormatDateTimeDescriptor getFormatControl( Composite parent )
	{
		if ( format == null )
		{
			format = new FormatDateTimeDescriptor( style, isFormStyle );
			format.setDescriptorProvider( provider );
			format.createControl( parent );
			format.getControl( )
					.setLayoutData( new GridData( GridData.FILL_BOTH ) );
			format.getControl( ).addDisposeListener( new DisposeListener( ) {

				public void widgetDisposed( DisposeEvent event )
				{
					format = null;
				}
			} );
		}
		else
		{
			checkParent( format.getControl( ), parent );
		}
		return format;
	}

	public void layout( )
	{
		GridData gd = (GridData) format.getControl( ).getLayoutData( );
		if ( getLayoutNum( ) > 0 )
			gd.horizontalSpan = getLayoutNum( ) - placeholder;
		else
			gd.horizontalSpan = ( (GridLayout) parent.getLayout( ) ).numColumns
					- placeholder;
	}

	public void addFormatChangeListener( IFormatChangeListener listener )
	{
		format.addFormatChangeListener( listener );
	}

	public void load( )
	{
		format.load( );
	}

	IDescriptorProvider provider;

	public IDescriptorProvider getProvider( )
	{
		return provider;
	}

	public void setProvider( IDescriptorProvider provider )
	{
		this.provider = provider;
		if ( format != null )
			format.setDescriptorProvider( provider );
	}

	public void setInput( Object input )
	{
		assert ( input != null );
		format.setInput( input );
	}

	public void setHidden( boolean isHidden )
	{
		if ( format != null )
			WidgetUtil.setExcludeGridData( format.getControl( ), isHidden );

	}

	public void setVisible( boolean isVisable )
	{
		if ( format != null )
			format.getControl( ).setVisible( isVisable );

	}

}
