/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.manager.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import joptsimple.internal.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.kuma.client.api.manager.InstancedManager;
import us.kuma.client.util.io.FileUtil;
import us.kuma.client.util.io.JsonUtil;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xgraza
 * @since 1/26/26
 */
public final class ConfigManager implements InstancedManager<JSONSerializable>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigManager.class);

    private final List<JSONSerializable> configList = new LinkedList<>();

    @Override
    public void init()
    {
        Runtime.getRuntime().addShutdownHook(new Thread(() ->
        {
            saveAll();
            LOGGER.info("Saved all configs");
        }, "Config Manager Shutdown Thread"));
    }

    public void loadAll()
    {
        int loaded = 0;
        for (final JSONSerializable serializable : configList)
        {
            if (serializable.getLocation() != null && !serializable.getLocation().exists())
            {
                LOGGER.warn("{} does not exist", serializable.getLocation());
                continue;
            }

            try
            {
                load(serializable);
                ++loaded;
            } catch (final Throwable e)
            {
                LOGGER.error("Failed to load configurable", e);
            }
        }
        LOGGER.info("Loaded {}/{} configurations", loaded, configList.size());
    }

    public void load(final JSONSerializable serializable) throws IOException
    {
        if (serializable.getLocation() == null || !serializable.getLocation().exists())
        {
            return;
        }

        final String content = FileUtil.readFile(serializable.getLocation());
        if (Strings.isNullOrEmpty(content))
        {
            LOGGER.warn("Content from {} is null/empty for {}",
                    serializable.getLocation().getAbsolutePath(),
                    serializable);
            return;
        }
        try
        {
            serializable.fromJSON(JsonParser.parseString(content));
        } catch (final Throwable throwable)
        {
            LOGGER.error("Failed to read JSON from file", throwable);
        }
    }

    public void saveAll()
    {
        int saved = 0;
        for (final JSONSerializable serializable : configList)
        {
            try
            {
                save(serializable);
                ++saved;
            } catch (final Throwable e)
            {
                LOGGER.error("Failed to save configurable", e);
            }
        }
        LOGGER.info("Saved {}/{} configurations", saved, configList.size());
    }

    public void save(final JSONSerializable serializable) throws IOException
    {
        final File file = serializable.getLocation();
        if (file != null && !file.exists())
        {
            final boolean result = file.createNewFile();
            if (!result)
            {
                throw new RuntimeException("Failed to create %s"
                        .formatted(file.getAbsolutePath()));
            }
            LOGGER.info("Created {} successfully", file.getAbsolutePath());
        }

        try
        {
            final JsonElement element = serializable.toJSON();
            if (element == null)
            {
                LOGGER.warn("JSON is empty/null for {}", serializable);
                return;
            }
            FileUtil.writeFile(serializable.getLocation(), JsonUtil.GSON.toJson(element));
        } catch (final Throwable throwable)
        {
            LOGGER.error("Failed to write to file", throwable);
        }
    }

    @Override
    public void register(final JSONSerializable entity)
    {
        configList.add(entity);
    }

    @Override
    public void unregister(final JSONSerializable entity)
    {
        configList.remove(entity);
    }

    @Override
    public List<JSONSerializable> getEntities()
    {
        return configList;
    }
}
