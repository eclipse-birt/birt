
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.metadata.PredefinedStyle;

public class SimpleComboPropertyDescriptorProvider extends
		PropertyDescriptorProvider
{

	public SimpleComboPropertyDescriptorProvider( String property,
			String element )
	{
		super( property, element );
	}

	public String[] getItems( )
	{
		String[] items = null;

		if ( ReportItemHandle.DATA_SET_PROP.equals( getProperty( ) ) )
			items = ChoiceSetFactory.getDataSets( );
		else if ( StyleHandle.MASTER_PAGE_PROP.equals( getProperty( ) ) )
			items = ChoiceSetFactory.getMasterPages( );
		else if ( ReportItemHandle.STYLE_PROP.equals( getProperty( ) ) )
		{
			items = ChoiceSetFactory.getStyles( );
			items = filterPreStyles( items );
		}
		else if ( ReportDesignHandle.THEME_PROP.equals( getProperty( ) ) )
			items = ChoiceSetFactory.getThemes( );
		return items;
	}

	private String[] filterPreStyles( String items[] )
	{
		List preStyles = DesignEngine.getMetaDataDictionary( )
				.getPredefinedStyles( );
		List preStyleNames = new ArrayList( );

		for ( int i = 0; i < preStyles.size( ); i++ )
		{
			preStyleNames.add( ( (PredefinedStyle) preStyles.get( i ) ).getName( ) );
		}

		List sytleNames = new ArrayList( );
		for ( int i = 0; i < items.length; i++ )
		{
			if ( preStyleNames.indexOf( items[i] ) == -1 )
			{
				sytleNames.add( items[i] );
			}
		}

		return (String[]) ( sytleNames.toArray( new String[]{} ) );

	}

	public boolean isSpecialProperty( )
	{
		return ReportItemHandle.STYLE_PROP.equals( getProperty( ) )
				|| ReportDesignHandle.THEME_PROP.equals( getProperty( ) )
				|| StyleHandle.MASTER_PAGE_PROP.equals( getProperty( ) );

	}

}
