
package org.eclipse.birt.report.engine.content;

public interface IDataContent extends ITextContent
{

	String getHelpText();
	
	String getHelpKey();

	void setValue( Object value );

	Object getValue( );

}
