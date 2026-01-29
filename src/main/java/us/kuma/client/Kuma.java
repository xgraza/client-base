/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.kuma.client.api.event.EventBus;
import us.kuma.client.api.manager.bind.BindManager;
import us.kuma.client.api.manager.command.CommandManager;
import us.kuma.client.api.manager.config.ConfigManager;
import us.kuma.client.api.manager.module.ModuleManager;
import us.kuma.client.api.manager.server.CombatManager;
import us.kuma.client.api.manager.server.RotationManager;
import us.kuma.client.util.BuildConfig;

/**
 * @author xgraza
 * @since 1/26/26
 */
public enum Kuma
{
    INSTANCE;

    public static final Logger LOGGER = LoggerFactory.getLogger(Kuma.class);
    public static final EventBus EVENT_BUS = new EventBus();

    private final ConfigManager configManager = new ConfigManager();
    private final BindManager bindManager = new BindManager();
    private final CommandManager commandManager = new CommandManager();
    private final ModuleManager moduleManager = new ModuleManager();
    private final RotationManager rotationManager = new RotationManager();
    private final CombatManager combatManager = new CombatManager();

    void init()
    {
        LOGGER.info("Beginning initialization of {} {}",
                BuildConfig.NAME,
                getVersion());

        configManager.init();
        bindManager.init();
        commandManager.init();
        moduleManager.init();
        rotationManager.init();
        combatManager.init();

        configManager.loadAll();
    }

    public ConfigManager getConfigManager()
    {
        return configManager;
    }

    public BindManager getBindManager()
    {
        return bindManager;
    }

    public CommandManager getCommandManager()
    {
        return commandManager;
    }

    public ModuleManager getModuleManager()
    {
        return moduleManager;
    }

    public static String getVersion()
    {
        return String.format("%s/%s-%s",
                BuildConfig.VERSION,
                BuildConfig.BRANCH,
                BuildConfig.HASH);
    }
}
