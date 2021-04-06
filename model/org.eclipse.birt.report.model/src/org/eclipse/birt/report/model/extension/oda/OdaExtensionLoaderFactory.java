
package org.eclipse.birt.report.model.extension.oda;

public class OdaExtensionLoaderFactory implements IOdaExtensionLoaderFactory {

	/**
	 * the base factory used to return the real oda extension loader factory.
	 */
	private static volatile IOdaExtensionLoaderFactory baseFactory = null;

	/**
	 * oad extension loader instance.
	 */
	private static volatile OdaExtensionLoaderFactory instance = null;

	/**
	 * Initializes the factory to set the base factory whcih can return teh real oda
	 * extension loader.
	 * 
	 * @param base the base oda extension loader factory.
	 */
	public synchronized static void initeFactory(IOdaExtensionLoaderFactory base) {
		if (baseFactory != null)
			return;

		baseFactory = base;
	}

	public static IOdaExtensionLoaderFactory getFactory() {
		if (baseFactory != null) {
			return baseFactory;
		}
		synchronized (OdaExtensionLoaderFactory.class) {
			if (baseFactory == null) {
				try {
					Class clazz = Class.forName("org.eclipse.birt.report.model.plugin.OdaBaseExtensionLoaderFactory");
					baseFactory = (IOdaExtensionLoaderFactory) clazz.newInstance();
				} catch (Exception ex) {
				}
			}
			return baseFactory;
		}
	}

	/**
	 * returns the oda extension loader factory instance.
	 * 
	 * @return oda extension loader factory.
	 */
	public static OdaExtensionLoaderFactory getInstance() {
		if (instance != null) {
			return instance;
		}
		synchronized (OdaExtensionLoaderFactory.class) {
			if (instance == null) {
				instance = new OdaExtensionLoaderFactory();
			}
			return instance;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.oda.IOdaExtensionLoaderFactory
	 * #createOdaExtensionLoader()
	 */
	public IOdaExtensionLoader createOdaExtensionLoader() {
		return getFactory().createOdaExtensionLoader();
	}

	/**
	 * Singleton instance release method.
	 */
	public static void releaseInstance() {
		baseFactory = null;
		instance = null;
	}
}
