
package org.eclipse.birt.report.engine.content;

public interface ILabelContent extends ITextContent
{

	void setLabelText( String labelText );

	String getLabelText( );

	void setLabelKey( String labelKey );

	String getLabelKey( );

	void setHelpText( String helpText );

	String getHelpText( );

	void setHelpKey( String helpKey );

	String getHelpKey( );
}
