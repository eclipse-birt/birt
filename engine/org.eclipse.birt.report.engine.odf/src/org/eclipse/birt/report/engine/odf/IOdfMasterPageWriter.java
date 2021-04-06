
package org.eclipse.birt.report.engine.odf;

import org.eclipse.birt.report.engine.odf.style.StyleEntry;

public interface IOdfMasterPageWriter extends IOdfWriter {

	public abstract void start();

	public abstract void end();

	public abstract void startMasterPage(StyleEntry pageLayout, String masterPageName, String displayName);

	public abstract void endMasterPage();

	public abstract void startHeader();

	public abstract void endHeader();

	public abstract void startFooter();

	public abstract void endFooter();

	public abstract void writeString(String s);

}