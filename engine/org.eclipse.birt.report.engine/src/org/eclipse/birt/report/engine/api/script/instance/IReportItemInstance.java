package org.eclipse.birt.report.engine.api.script.instance;


public interface IReportItemInstance
{

	IScriptStyle getStyle( );

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

	Object getNamedExpressionValue( String name );

	void setNamedExpressionValue( String name, Object value );

	Object getUserProperty( String name );

	void setUserProperty( String name, Object value );

}