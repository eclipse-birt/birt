package org.eclipse.birt.report.engine.api.script.instance;

public interface IImageInstance extends IReportItemInstance
{

	/**
	 * Get the alt text
	 * 
	 */
	String getAltText( );

	/**
	 * Set the alt text
	 * 
	 * @param altText
	 */
	void setAltText( String altText );

	/**
	 * Get the alt text
	 * 
	 */
	String getAltTextKey( );

	/**
	 * Set the alt text
	 * 
	 * @param altText
	 */
	void setAltTextKey( String altTextKey );

	/**
	 * Get the image URI
	 * 
	 */
	String getURI( );

	/**
	 * Set the image URI
	 * 
	 */
	void setURI( String uri );

	/**
	 * Returns the type of image source Can be one of the following:
	 * org.eclipse.birt.report.engine.ir.ImageItemDesign.IMAGE_URI
	 * org.eclipse.birt.report.engine.ir.ImageItemDesign.IMAGE_NAME
	 * org.eclipse.birt.report.engine.ir.ImageItemDesign.IMAGE_EXPRESSION
	 * org.eclipse.birt.report.engine.ir.ImageItemDesign.IMAGE_FILE
	 */
	int getImageSource( );

	/**
	 * Set the image source
	 * 
	 * Can be one of the following:
	 * org.eclipse.birt.report.engine.ir.ImageItemDesign.IMAGE_URI
	 * org.eclipse.birt.report.engine.ir.ImageItemDesign.IMAGE_NAME
	 * org.eclipse.birt.report.engine.ir.ImageItemDesign.IMAGE_EXPRESSION
	 * org.eclipse.birt.report.engine.ir.ImageItemDesign.IMAGE_FILE
	 */
	void setImageSource( int source );

	/**
	 * Get the image name
	 */
	String getImageName( );

	/**
	 * Set the image name
	 */
	void setImageName( String imageName );
	
	/**
	 * Returns the data for a named image
	 */
	byte[] getData( );

	/**
	 * Set the data for a named image
	 */
	void setData( byte[] data );
	
	/**
	 * Get the MIME Type
	 */
	String getMimeType();
	
	/**
	 * Set the MIME Type
	 */
	void setMimeType(String type);

}