package org.eclipse.birt.report.service.api;

import java.io.ByteArrayOutputStream;

public interface IViewerReportDesignHandle
{
	
	public final static String RPT_DESIGN_OBJECT = "rptDesignObject";
 	public final static String RPT_RUNNABLE_OBJECT = "rptRunnableObject";

    /**
	 * Get the content type
	 * 
	 * Two types are supported: rptDesignFile and rptDesignObject
	 * 
	 * @return
	 */
	String getContentType( );

	/**
	 * Get the file name
	 * 
	 * @return
	 */
	String getFileName( );

	/**
	 * Set the filename
	 * 
	 * @param name
	 */
	void setFileName( String name );

	/**
	 * Get the design object
	 * 
	 * @return
	 */
	Object getDesignObject( );

	/**
	 * Set the design object
	 * 
	 * @param obj
	 */
	void setDesignObject( Object obj );

	/**
	 * Return a stream of the design
	 * 
	 * @return
	 */
	ByteArrayOutputStream getObjectStream( );

}
