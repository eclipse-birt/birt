package org.eclipse.birt.report.model.util;

import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.List;

import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ReportItemThemeHandle;
import org.eclipse.birt.report.model.api.util.DocumentUtil;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;


public class ReportDesignSerializerTest extends BaseTestCase
{
	/**
	 * Test cases:
	 * reference two reportItemTheme with same name, but in different library
	 * 
	 * @throws Exception
	 * 
	 */

	public void testSameReportItemTheme( ) throws Exception
	{
		openDesign( "reportItemThemeFromLib.xml" ); //$NON-NLS-1$
		ReportDesignHandle newDesign = DocumentUtil.serialize( designHandle,
				new ByteArrayOutputStream( ) );
		List<ReportElementHandle> themes = newDesign.getSlot(
				IReportDesignModel.THEMES_SLOT ).getContents( );
		HashSet<String> themeName = new HashSet<String>( );
		for ( ReportElementHandle theme : themes )
		{
			if ( theme instanceof ReportItemThemeHandle )
			{
				ReportItemThemeHandle reportItemTheme = (ReportItemThemeHandle) theme;
				String name = reportItemTheme.getName( );
				assertFalse( themeName.contains( name ) );
				themeName.add( name );
			}
		}

		List<ReportItemHandle> items = newDesign.getSlot(
				IReportDesignModel.BODY_SLOT ).getContents( );
		themeName.clear( );
		for ( ReportItemHandle item : items )
		{
			ReportItemHandle reportItem = (ReportItemHandle) item;
			ReportItemThemeHandle theme = reportItem.getTheme( );
			if ( theme != null )
			{
				assertFalse( themeName.contains( theme.getName( ) ) );
				themeName.add( theme.getName( ) );
			}
		}

	}
}
