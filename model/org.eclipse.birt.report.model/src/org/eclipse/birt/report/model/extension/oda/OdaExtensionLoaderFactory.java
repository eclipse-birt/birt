
package org.eclipse.birt.report.model.extension.oda;

public class OdaExtensionLoaderFactory implements IOdaExtensionLoaderFactory
{

	/**
	 * the base factory used to return the real oda extension loader factory.
	 */
	private static IOdaExtensionLoaderFactory baseFactory = null;

	/**
	 * oad extension loader instance.
	 */
	private static OdaExtensionLoaderFactory instance = null;

	/**
	 * Initializes the factory to set the base factory whcih can return teh real
	 * oda extension loader.
	 * 
	 * @param base
	 *            the base oda extension loader factory.
	 */
	public synchronized static void initeFactory(
			IOdaExtensionLoaderFactory base )
	{
		if ( baseFactory != null )
			return;

		baseFactory = base;
	}

	/**
	 * returns the oda extension loader factory instance.
	 * 
	 * @return oda extension loader factory.
	 */
	public static OdaExtensionLoaderFactory getInstance( )
	{
		if ( instance == null )
			instance = new OdaExtensionLoaderFactory( );
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.extension.oda.IOdaExtensionLoaderFactory
	 * #createOdaExtensionLoader()
	 */
	public IOdaExtensionLoader createOdaExtensionLoader( )
	{
		if ( baseFactory != null )
			return baseFactory.createOdaExtensionLoader( );

		return null;
	}

}
