package org.eclipse.birt.build.framework;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.jar.Manifest;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class BundleLoader {

	static final String BUNDLE_SYMBOLIC_NAME = "Bundle-SymbolicName";
	static final String BUNDLE_VERSION = "Bundle-Version";
	static final String BUNDLE_LOCALIZATION = "Bundle-Localization";

	static final String MANIFEST_ENTRY = "META-INF/MANIFEST.MF";
	static final String PLUGIN_ENTRY = "plugin.xml";
	static final String FRAGMENT_ENTRY = "fragment.xml";
	static final String PROPERTIES_ENTRY = "plugin.properties";

	protected URL root;
	protected Bundle bundle;
	protected Properties properties;

	BundleLoader(URL root) {
		this.root = root;
	}

	Bundle load(URL root) throws IOException, ParserConfigurationException, SAXException {
		// properties = loadProperties( root );
		Manifest manifest = loadManifest(root);
		String localizationEntry = (String) manifest.getMainAttributes().get(BUNDLE_LOCALIZATION);
		if (localizationEntry != null) {
			properties = loadProperties(localizationEntry);
		}
		return bundle;
	}

	protected Manifest loadManifest(URL root) throws IOException {
		InputStream in = openInputStream(root, MANIFEST_ENTRY);
		if (in == null) {
			throw new IOException("can't load the " + MANIFEST_ENTRY);
		}
		try {
			return new Manifest(in);
		} finally {
			in.close();
		}
	}

	protected Properties loadProperties(String propertyEntryName) {
		InputStream in = openInputStream(root, propertyEntryName);
		;
		if (in == null) {
			return null;
		}
		try {
			Properties properties = new Properties();
			properties.load(in);
			return properties;
		} catch (IOException ex) {
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
			}
		}
		return null;
	}

	protected String localizeProperty(String key) {
		if (key != null && key.length() > 0) {
			if (key.charAt(0) == '%') {
				key = key.substring(1);
				if (properties != null) {
					String value = properties.getProperty(key);
					if (value != null) {
						return value;
					}
				}
			}
		}
		return key;
	}

	protected InputStream openInputStream(URL root, String name) {
		try {
			URL manifestUrl = new URL(root, name);
			InputStream in = manifestUrl.openStream();
			if (in != null) {
				return in;
			}
		} catch (IOException ex) {
		}
		return null;
	}

}
