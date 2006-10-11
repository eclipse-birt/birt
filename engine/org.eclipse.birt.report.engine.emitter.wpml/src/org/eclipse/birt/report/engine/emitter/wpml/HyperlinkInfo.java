package org.eclipse.birt.report.engine.emitter.wpml;

public class HyperlinkInfo
{
	HyperlinkInfo ( int type, String url )
	{
		this.type = type;
		this.url = url;
	}

	int type;

	String url;

	static int BOOKMARK = 0;

	static int HYPERLINK = 1;

	static int DRILL = 2;
}
