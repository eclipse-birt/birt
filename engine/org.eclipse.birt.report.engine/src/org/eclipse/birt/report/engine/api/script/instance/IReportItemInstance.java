package org.eclipse.birt.report.engine.api.script.instance;


public interface IReportItemInstance extends IReportElementInstance
{

	/**
	 * Get the hyperlink
	 * 
	 * @return the hyperlink
	 */
	String getHyperlink( );

	/**
	 * Get the name
	 */
	String getName( );

	/**
	 * Get the name
	 */
	void setName( String name );

	/**
	 * Get the help text
	 */
	String getHelpText( );

	/**
	 * Get the help text
	 */
	void setHelpText( String helpText );

	/**
	 * Get the horizontal position
	 */
	String getHorizontalPosition( );

	/**
	 * Set the horizontal position
	 */
	void setHorizontalPosition( String position );

	/**
	 * Get the vertical position
	 */
	String getVerticalPosition( );
	
	/**
	 * Set the vertical position
	 */
	void setVerticalPosition( String position );


}