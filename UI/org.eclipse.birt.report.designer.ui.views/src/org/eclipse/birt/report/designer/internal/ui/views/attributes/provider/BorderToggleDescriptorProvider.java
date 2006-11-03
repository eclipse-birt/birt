
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class BorderToggleDescriptorProvider extends BorderDescriptorProvider implements
		IToggleDescriptorProvider
{

	private String property;

	BorderDescriptorProvider[] dependedProviders;

	public BorderToggleDescriptorProvider( String property,
			BorderDescriptorProvider[] dependedProviders )
	{
		this.property = property;
		this.dependedProviders = dependedProviders;
	}

	public String getImageName( )
	{
		if ( property.equals( StyleHandle.BORDER_LEFT_STYLE_PROP ) )
			return IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_LEFT;
		if ( property.equals( StyleHandle.BORDER_RIGHT_STYLE_PROP ) )
			return IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_RIGHT;
		if ( property.equals( StyleHandle.BORDER_TOP_STYLE_PROP ) )
			return IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_TOP;
		if ( property.equals( StyleHandle.BORDER_BOTTOM_STYLE_PROP ) )
			return IReportGraphicConstants.ICON_ATTRIBUTE_BORDER_BOTTOM;
		return "";
	}

	public String getToogleValue( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getTooltipText( )
	{
		if ( property.equals( StyleHandle.BORDER_LEFT_STYLE_PROP ) )
			return Messages.getString( "BordersPage.Tooltip.Left" );
		if ( property.equals( StyleHandle.BORDER_RIGHT_STYLE_PROP ) )
			return Messages.getString( "BordersPage.Tooltip.Right" );
		if ( property.equals( StyleHandle.BORDER_TOP_STYLE_PROP ) )
			return Messages.getString( "BordersPage.Tooltip.Top" );
		if ( property.equals( StyleHandle.BORDER_BOTTOM_STYLE_PROP ) )
			return Messages.getString( "BordersPage.Tooltip.Bottom" );
		return "";
	}

	public String getDisplayName( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Object load( )
	{
		Boolean handle;
		String value = getLocalStringValue( property );
		if ( value.equals( "" ) )
			handle = new Boolean( false );
		else
			handle = new Boolean( true );
		try
		{
			for ( int i = 0; i < dependedProviders.length; i++ )
			{
				dependedProviders[i].setStyleProperty( property, handle );
			}
		}
		catch ( Exception e )
		{
		}
		return handle;
	}

	public void save( Object value ) throws SemanticException
	{
		for ( int i = 0; i < dependedProviders.length; i++ )
		{
			dependedProviders[i].setStyleProperty( property, (Boolean) value );
			dependedProviders[i].handleModifyEvent( );
		}

	}

	void handleModifyEvent( )
	{
		// TODO Auto-generated method stub

	}

}
