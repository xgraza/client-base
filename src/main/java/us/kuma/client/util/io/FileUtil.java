/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.util.io;

import net.fabricmc.loader.impl.FabricLoaderImpl;
import us.kuma.client.Kuma;
import us.kuma.client.util.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author xgraza
 * @since 1/26/26
 */
public final class FileUtil
{
    public static final File DIRECTORY;

    static
    {
        DIRECTORY = FabricLoaderImpl.INSTANCE.getGameDir().resolve(BuildConfig.NAME).toFile();
        if (!DIRECTORY.exists() || !DIRECTORY.isDirectory())
        {
            final boolean result = DIRECTORY.mkdir();
            if (!result)
            {
                throw new RuntimeException("Failed to create %s"
                        .formatted(DIRECTORY.getAbsolutePath()));
            }
            Kuma.LOGGER.info("Created {} successfully",
                    DIRECTORY.getAbsolutePath());
        }
    }

    public static String readFile(final File file) throws IOException
    {
        return Files.readString(file.toPath());
    }

    public static void writeFile(final File file, final String content) throws IOException
    {
        Files.writeString(file.toPath(), content);
    }

    public static File resolve(final File file, final String... paths)
    {
        Path path = file.toPath();
        for (final String item : paths)
        {
            path = path.resolve(item);
        }
        return path.toFile();
    }

    public static File resolve(final String... paths)
    {
        return resolve(DIRECTORY, paths);
    }
}
