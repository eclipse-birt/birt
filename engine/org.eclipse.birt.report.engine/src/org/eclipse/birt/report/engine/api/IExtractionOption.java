
package org.eclipse.birt.report.engine.api;

import java.io.OutputStream;

public interface IExtractionOption extends ITaskOption
{

	public static final String OUTPUT_FORMAT = "Format"; //$NON-NLS-1$

	public static final String OUTPUT_FILE_NAME = "outputFile"; //$NON-NLS-1$

	public static final String OUTPUT_STREAM = "outputStream"; //$NON-NLS-1$

	/**
	 * Set output format.
	 * 
	 * @param format
	 *            output format.
	 */
	void setOutputFormat( String format );

	/**
	 * Get output format.
	 */
	String getOutputFormat( );

	/**
	 * Set output stream.
	 * 
	 * @param out
	 *            output stream.
	 */
	void setOutputStream( OutputStream out );

	/**
	 * Get output stream.
	 */
	OutputStream getOutputStream( );


	/**
	 * Set output file.
	 * 
	 * @param filename
	 *            name of the output file.
	 */
	void setOutputFile( String filename );

	/**
	 * Get output file name.
	 */
	String getOutputFile( );
}
