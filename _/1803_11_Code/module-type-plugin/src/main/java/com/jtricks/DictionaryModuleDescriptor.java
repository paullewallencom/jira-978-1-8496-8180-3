package com.jtricks;

import org.dom4j.Element;

import com.atlassian.plugin.Plugin;
import com.atlassian.plugin.PluginParseException;
import com.atlassian.plugin.descriptors.AbstractModuleDescriptor;
import com.atlassian.plugin.module.ModuleFactory;
import com.jtricks.dictionary.Dictionary;

public class DictionaryModuleDescriptor extends AbstractModuleDescriptor<Dictionary> {

	private String language;

	public DictionaryModuleDescriptor(ModuleFactory moduleFactory) {
		super(moduleFactory);
	}

	@Override
	public void init(Plugin plugin, Element element) throws PluginParseException {
		super.init(plugin, element);
		language = element.attributeValue("lang");
	}

	public Dictionary getModule() {
		return moduleFactory.createModule(moduleClassName, this);
	}

	public String getLanguage() {
		return language;
	}

}
