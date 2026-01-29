/*
 * Copyright (c) xgraza 2026
 */

package us.kuma.client.util.io;

import net.fabricmc.loader.impl.FabricLoaderImpl;
import us.kuma.client.Kuma;
import us.kuma.client.util.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
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
        try (final InputStream is = Files.newInputStream(file.toPath()))
        {
            final StringBuilder builder = new StringBuilder();
            int b;
            while ((b = is.read()) != -1)
            {
                builder.append((char) b);
            }
            return builder.toString();
        }
    }

    public static void writeFile(final File file, final String content) throws IOException
    {
        try (final OutputStream os = Files.newOutputStream(file.toPath()))
        {
            final byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
            os.write(bytes, 0, bytes.length);
        }
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
