package org.eclipse.birt.report.engine.layout;

import org.eclipse.birt.report.engine.layout.html.HTMLReportLayoutEngine;


public class LayoutEngineFactory
{
	public static IReportLayoutEngine createLayoutEngine(String format)
	{
		if ( "pdf".equalsIgnoreCase( format ) ) //$NON-NLS-1$
		{
			try
			{
				Class clazz = Class
						.forName( "org.eclipse.birt.report.engine.layout.pdf.PDFReportLayoutEngine" ); //$NON-NLS-1$
				Object engine = clazz.newInstance( );
				return (IReportLayoutEngine) engine;
			}
			catch ( Exception ex )
			{
			}
			return null;
		}
		return new HTMLReportLayoutEngine( );
	}
}
