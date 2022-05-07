package com.lzh.rpc.server.properties;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author Liu Zihao <liuzihaolzh@126.com>
 * @date 2022-05-07
 * @since 1.0.0
 */
public class YmlPropertySourceFactory extends DefaultPropertySourceFactory {
    private static final String YML_SUFFIX = ".yml";

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        File file = resource.getResource().getFile();
        if (!file.exists()) {
            return super.createPropertySource(name, resource);
        }
        if (file.getName().lastIndexOf(YML_SUFFIX) != file.getName().length() - YML_SUFFIX.length()) {
            return super.createPropertySource(name, resource);
        }
        List<PropertySource<?>> sources = new YamlPropertySourceLoader().load(resource.getResource().getFilename(), resource.getResource());
        return sources.get(0);
    }
}
