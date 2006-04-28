
package org.eclipse.birt.report.engine.content;

public interface IAutoTextContent extends ITextContent
{
	public static final int TOTAL_PAGE = 0;
	public static final int PAGE_NUMBER = 1;
	
	void setType ( int type );
	
	int getType ( );
}
