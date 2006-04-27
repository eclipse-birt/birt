
package org.eclipse.birt.report.engine.content;

public interface IAutoTextContent extends ITextContent
{
	public static final byte TOTAL_PAGE = 0;
	public static final byte PAGE_NUMBER = 1;
	
	void setType ( byte type );
	
	byte getType ( );
}
