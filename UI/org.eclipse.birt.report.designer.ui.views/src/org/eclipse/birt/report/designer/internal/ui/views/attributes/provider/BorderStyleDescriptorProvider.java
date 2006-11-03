
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class BorderStyleDescriptorProvider extends StyleComboProvider
{

	public BorderStyleDescriptorProvider( )
	{
		super( );
	}

	private static final String LABEL_STYLE = Messages.getString( "BordersPage.Label.Style" ); //$NON-NLS-1$

	public String getDisplayName( )
	{
		return LABEL_STYLE;
	}

	public Object load( )
	{
		String value = getLocalStringValue( StyleHandle.BORDER_LEFT_STYLE_PROP );
		if ( !"".equals( value ) )
		{
			this.indexText = value;
			return value;
		}

		value = getLocalStringValue( StyleHandle.BORDER_RIGHT_STYLE_PROP );
		if ( !"".equals( value ) )
		{
			this.indexText = value;
			return value;
		}

		value = getLocalStringValue( StyleHandle.BORDER_TOP_STYLE_PROP );
		if ( !"".equals( value ) )
		{
			this.indexText = value;
			return value;
		}

		value = getLocalStringValue( StyleHandle.BORDER_BOTTOM_STYLE_PROP );
		if ( !"".equals( value ) )
		{
			this.indexText = value;
			return value;
		}
		return indexText;
	}

	public void save( Object value ) throws SemanticException
	{
		this.indexText = value == null ? "" : value;
		if ( ( (Boolean) styleMap.get( StyleHandle.BORDER_TOP_STYLE_PROP ) ).booleanValue( ) == true )
		{
			save( StyleHandle.BORDER_TOP_STYLE_PROP, value );
		}
		else
			save( StyleHandle.BORDER_TOP_STYLE_PROP, null );

		if ( ( (Boolean) styleMap.get( StyleHandle.BORDER_BOTTOM_STYLE_PROP ) ).booleanValue( ) == true )
		{
			save( StyleHandle.BORDER_BOTTOM_STYLE_PROP, value );
		}
		else
			save( StyleHandle.BORDER_BOTTOM_STYLE_PROP, null );

		if ( ( (Boolean) styleMap.get( StyleHandle.BORDER_LEFT_STYLE_PROP ) ).booleanValue( ) == true )
		{
			save( StyleHandle.BORDER_LEFT_STYLE_PROP, value );
		}
		else
			save( StyleHandle.BORDER_LEFT_STYLE_PROP, null );

		if ( ( (Boolean) styleMap.get( StyleHandle.BORDER_RIGHT_STYLE_PROP ) ).booleanValue( ) == true )
		{
			save( StyleHandle.BORDER_RIGHT_STYLE_PROP, value );
		}
		else
			save( StyleHandle.BORDER_RIGHT_STYLE_PROP, null );
	}

	public void handleModifyEvent( )
	{
		try
		{
			if ( indexText != null )
				save( indexText );
		}
		catch ( Exception e )
		{
		}
	}

}
