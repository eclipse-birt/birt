package org.eclipse.birt.report.service.api;

import java.util.HashMap;
import java.util.Map;

public class OutputOptions {
	public static final String OPT_REPORT_GENERATION_COMPLETED = "completed"; //$NON-NLS-1$

	public static final String OPT_CONNECTIONHANDLE = "connectionhandle"; //$NON-NLS-1$

	private Map options;

	public OutputOptions() {
		this.options = new HashMap();
	}

	public void setOption(String optName, Object optValue) {
		options.put(optName, optValue);
	}

	public Object getOption(String optName) {
		return options.get(optName);
	}

	public Map getOptions() {
		return options;
	}

}
