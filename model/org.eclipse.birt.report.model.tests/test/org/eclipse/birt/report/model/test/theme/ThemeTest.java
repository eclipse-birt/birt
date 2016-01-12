
package org.eclipse.birt.report.model.test.theme;

import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemThemeHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.util.BaseTestCase;

public class ThemeTest extends BaseTestCase
{

	private final static String FILENAME = "theme.rptdesign"; //$NON-NLS-1$

	protected void setUp( ) throws Exception
	{
		super.setUp( );
		openDesign( FILENAME );
	}

	/**
	 * @throws Exception
	 */
	public void testLoadTheme( ) throws Exception
	{
		String themeName = designHandle
				.getStringProperty( ReportDesignHandle.THEME_PROP );
		assertEquals( "theme1", themeName );
		ThemeHandle theme = designHandle.getTheme( );
		assertNotNull( theme );
		assertEquals( "theme1", theme.getName( ) );
		
		TableHandle table1 = (TableHandle) designHandle.getElementByID( 9 );
		themeName = table1.getStringProperty( TableHandle.THEME_PROP );
		assertEquals( "Table-theme1", themeName );
		
		ReportItemThemeHandle tableTheme = table1.getTheme( );
		assertNotNull( tableTheme );
		assertEquals( "Table-theme1", tableTheme.getName( ) );
		
	}
}
