package org.eclipse.birt.report.service;

import java.io.ByteArrayOutputStream;

import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;

public class BirtViewerReportDesignHandle implements IViewerReportDesignHandle
{

	private String contentType;

	private String fileName;

	public BirtViewerReportDesignHandle( String contentType, String fileName )
	{
		this.contentType = contentType;
		this.fileName = fileName;
	}

	public String getContentType( )
	{
		return contentType;
	}

	public String getFileName( )
	{
		return fileName;
	}

	public void setFileName( String name )
	{
		this.fileName = name;
	}

	public Object getDesignObject( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	public void setDesignObject( Object obj )
	{
		// TODO Auto-generated method stub

	}

	public ByteArrayOutputStream getObjectStream( )
	{
		// TODO What to do here??
		return null;
	}

}
