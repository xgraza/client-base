/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.api.manager.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.kuma.client.Kuma;
import us.kuma.client.api.manager.InstancedManager;
import us.kuma.client.api.manager.config.JSONSerializable;
import us.kuma.client.impl.module.client.ClickGUIModule;
import us.kuma.client.impl.module.movement.SprintModule;
import us.kuma.client.impl.module.render.FullbrightModule;
import us.kuma.client.util.io.FileUtil;
import us.kuma.client.util.io.JsonUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xgraza
 * @since 1/26/26
 */
public final class ModuleManager implements InstancedManager<Module>, JSONSerializable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleManager.class);
    private static final Minecraft MC = Minecraft.getInstance();

    private static final File CONFIGS_DIRECTORY = FileUtil.resolve("configs");
    private static final String DEFAULT_CONFIG_NAME = "default";

    private final List<Module> moduleInstanceList = new LinkedList<>();

    @Override
    public void init()
    {
        if (!CONFIGS_DIRECTORY.exists())
        {
            final boolean result = CONFIGS_DIRECTORY.mkdir();
            if (!result)
            {
                throw new RuntimeException("Could not create %s"
                        .formatted(CONFIGS_DIRECTORY.getAbsolutePath()));
            }
        }

        // Client
        register(ClickGUIModule.class);

        // Movement
        register(SprintModule.class);

        // Render
        register(FullbrightModule.class);

        LOGGER.info("Registered {} modules", moduleInstanceList.size());
        Kuma.INSTANCE.getConfigManager().register(this);
    }

    private void register(Class<? extends Module> moduleClass)
    {
        try
        {
            Module instance = (Module) moduleClass.getConstructors()[0].newInstance();
            for (final Field field : moduleClass.getDeclaredFields())
            {
                // if you choose to do obfuscation/disable reflection, this would need to be removed
                // granted, id expect module registering to be done from an external loader if you're
                // going that route anyway...
                if (moduleClass.isAssignableFrom(field.getType()) && field.getName().equals("INSTANCE"))
                {
                    field.set(null, instance);
                }
            }
            instance.discoverSettings();
            register(instance);
        } catch (final Throwable e)
        {
            LOGGER.error("Failed to register module", e);
        }
    }

    @Override
    public void register(final Module entity)
    {
        moduleInstanceList.add(entity);
        Kuma.INSTANCE.getBindManager().register(entity.getBind());
    }

    @Override
    public void unregister(final Module entity)
    {
        moduleInstanceList.remove(entity);
    }

    @Override
    public List<Module> getEntities()
    {
        return moduleInstanceList;
    }

    @Override
    public void fromJSON(JsonElement element)
    {

    }

    @Override
    public JsonElement toJSON()
    {
        final JsonObject object = new JsonObject();
        object.addProperty("creator", MC.getGameProfile().name());
        final JsonObject modulesObject = new JsonObject();
        for (final Module module : moduleInstanceList)
        {
            try
            {
                final JsonElement element = module.toJSON();
                if (JsonUtil.isNull(element))
                {
                    LOGGER.warn("{} has no JSON save state", module.getName());
                    continue;
                }
                modulesObject.add(module.getName(), element);
            } catch (final Throwable throwable)
            {
                LOGGER.error("Failed to save a setting", throwable);
            }
        }
        object.add("modules", modulesObject);
        return object;
    }

    @Override
    public File getLocation()
    {
        return FileUtil.resolve(CONFIGS_DIRECTORY, DEFAULT_CONFIG_NAME + ".json");
    }
}
