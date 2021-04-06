package org.eclipse.birt.report.engine.emitter.config;

/**
 * RenderOptionDefn
 */
public class RenderOptionDefn {
	private String key;
	private String value;
	private boolean enabled;

	public RenderOptionDefn(String key, String value, boolean enabled) {
		super();
		this.key = key;
		this.value = value;
		this.enabled = enabled;
	}

	public String getKey() {
		return key;
	}

	public String getValue() {
		return value;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String toString() {
		return "RenderOptionDefn [key=" + key + ", value=" + value + ", enabled=" + enabled + "]";
	}
}
