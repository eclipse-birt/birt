package org.eclipse.birt.report.engine.api.impl;

import java.io.IOException;


public interface IReportletDocument extends IInternalReportDocument
{

	boolean isReporltetDocument( ) throws IOException;

	int getReportletElement( ) throws IOException;

	String getReportletBookmark( ) throws IOException;
}
