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
	 * @return Returns the extension.
	 */
	String getExtension( );

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
	 * Returns the type of image source
	 */
	int getImageSource( );

	/**
	 * Set the image source
	 * 
	 * @param source
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

}