
package org.eclipse.birt.report.tests.engine.api;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleHandle;

public class EngineResourceLocator implements IResourceLocator {

	private String path;

	public EngineResourceLocator(String path) {
		this.path = path;
	}

	public URL findResource(ModuleHandle moduleHandle, String filename, int type) {
		if (path == null || path.equals(""))
			return null;

		try {
			URL url = new URL(path + filename);
			return url;
		} catch (MalformedURLException mue) {
			mue.printStackTrace();
		}

		return null;
	}

	public URL findResource(ModuleHandle moduleHandle, String fileName, int type, Map appContext) {
		// TODO Auto-generated method stub
		return null;
	}

}
