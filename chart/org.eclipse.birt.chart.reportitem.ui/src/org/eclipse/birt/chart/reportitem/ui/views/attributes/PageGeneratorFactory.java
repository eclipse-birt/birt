
package org.eclipse.birt.chart.reportitem.ui.views.attributes;

import org.eclipse.birt.report.designer.ui.views.IPageGenerator;
import org.eclipse.core.runtime.IAdapterFactory;

public class PageGeneratorFactory implements IAdapterFactory
{

	public Object getAdapter( Object adaptableObject, Class adapterType )
	{
		return new ChartPageGenerator( );
	}

	public Class[] getAdapterList( )
	{
		return new Class[]{
			IPageGenerator.class
		};
	}

}
