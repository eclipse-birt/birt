
package org.eclipse.birt.report.engine.content;

public interface IDataContent extends ITextContent
{

	String getHelpText( );

	String getHelpKey( );

	void setValue( Object value );

	Object getValue( );

	String getLabelText( );

	String getLabelKey( );

	void setLabelText( String text );

	void setLabelKey( String key );

}
