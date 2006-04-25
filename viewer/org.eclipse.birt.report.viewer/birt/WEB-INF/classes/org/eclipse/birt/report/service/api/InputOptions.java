package org.eclipse.birt.report.service.api;

import java.util.HashMap;
import java.util.Map;

public class InputOptions
{
	public static final String OPT_LOCALE = "locale";

	public static final String OPT_FORMAT = "format";

	public static final String OPT_BASE_URL = "baseURL";

	public static final String OPT_REQUEST = "request";
	
	public static final String OPT_IS_MASTER_PAGE_CONTENT = "isMasterPageContent";
	
	public static final String OPT_SVG_FLAG = "svgFlag";
	
	public static final String OPT_RENDER_FORMAT = "format";
	
	public static final String OPT_IS_DESIGNER = "isDesigner";


	// add options here that matches the common engine run or render options

	private Map options;

	public InputOptions( )
	{
		this.options = new HashMap( );
	}

	public void setOption( String optName, Object optValue )
	{
		options.put( optName, optValue );
	}

	public Object getOption( String optName )
	{
		return options.get( optName );
	}

	public Map getOptions( )
	{
		return options;
	}

}
