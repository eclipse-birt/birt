package org.eclipse.birt.report.engine.api.script.instance;

public interface ITextItemInstance extends IForeignTextInstance
{

	/**
	 * Get the value
	 */
	String getText( );

	/**
	 * Set the value
	 */
	void setText( String value );

}