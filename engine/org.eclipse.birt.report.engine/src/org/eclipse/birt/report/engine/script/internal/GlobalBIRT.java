
package org.eclipse.birt.report.engine.script.internal;

import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.mozilla.javascript.ScriptableObject;

public class GlobalBIRT extends ScriptableObject {

	private static final long serialVersionUID = -2660909218558681397L;
	protected ExecutionContext context;

	public GlobalBIRT() {
	}

	public GlobalBIRT(ExecutionContext context) {
		this.context = context;
	}

	public String getClassName() {
		return "GlobalBIRT";
	}

	public int jsGet_CurrentPage() {
		if (context != null) {
			return (int) context.getPageNumber();
		}
		return 0;
	}

	public int jsGet_TotalPage() {
		if (context != null) {
			return (int) context.getTotalPage();
		}
		return 0;
	}
}
