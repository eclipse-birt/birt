
package org.eclipse.birt.report.model.tests.box;

import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.scripts.ClassInfo;
import org.eclipse.birt.report.model.api.scripts.IScriptableObjectClassInfo;

public class ScriptableFactory implements IScriptableObjectClassInfo {

	public IClassInfo getScriptableClass(String className) {
		if ("org.eclipse.birt.report.model.tests.box.Box".equalsIgnoreCase(className))
			return new ClassInfo(Box.class);

		return null;
	}
}
