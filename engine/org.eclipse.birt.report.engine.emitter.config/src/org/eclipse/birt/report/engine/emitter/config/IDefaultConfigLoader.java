package org.eclipse.birt.report.engine.emitter.config;

import java.util.Map;

public interface IDefaultConfigLoader {
	Map<String, RenderOptionDefn> loadConfigFor(String bundleName, IEmitterDescriptor descriptor);

	int getPriority();
}
