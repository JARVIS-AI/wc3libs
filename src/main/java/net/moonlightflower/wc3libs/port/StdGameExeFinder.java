package net.moonlightflower.wc3libs.port;

import net.moonlightflower.wc3libs.bin.GameExe;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class StdGameExeFinder implements GameExeFinder {
    public StdGameExeFinder() {
    }

    public final static File WAR3_EXE_PATH = new File("war3.exe");
    public final static File WARCRAFT_III_EXE_PATH = new File("Warcraft III.exe");
    public final static File FROZEN_THRONE_EXE_PATH = new File("Frozen Throne.exe");

    public final static File X86_DIR = new File("x86");
    public final static File X64_DIR = new File("x86_64");

    public final static File X86_EXE_PATH_131 = new File(X86_DIR, WARCRAFT_III_EXE_PATH.toString());
    public final static File X64_EXE_PATH_131 = new File(X64_DIR, WARCRAFT_III_EXE_PATH.toString());

    @Nonnull
    public static File fromDir(@Nonnull File dir, @Nonnull GameVersion version, @Nonnull Orient.WinArch arch) {
        if (version.compareTo(GameVersion.VERSION_1_31) >= 0) {
            switch (arch) {
                case X86:
                    return new File(dir, X86_EXE_PATH_131.toString());
                case X64:
                    return new File(dir, X64_EXE_PATH_131.toString());
            }
        }
        if (version.compareTo(GameVersion.VERSION_1_29) >= 0) return new File(dir, WARCRAFT_III_EXE_PATH.toString());

        return new File(dir, WAR3_EXE_PATH.toString());
    }

    @Nonnull
    public static File fromDirIgnoreVersion(@Nonnull File dir) throws NotFoundException {
        List<File> relativeSearchPaths = Arrays.asList(WARCRAFT_III_EXE_PATH,
                FROZEN_THRONE_EXE_PATH,
                WAR3_EXE_PATH,
                X86_EXE_PATH_131,
                X64_EXE_PATH_131
        );

        for (File relativeSearchPath : relativeSearchPaths) {
            File inDirPath = new File(dir, relativeSearchPath.toString());

            if (relativeSearchPath.exists()) return inDirPath;
        }

        throw new NotFoundException();
    }

    @Nonnull
    private File getGameExeInDir(@Nonnull File dir) throws NotFoundException {
        GameVersionFinder gameVersionFinder = getGameVersionFinder();

        try {
            GameVersion gameVersion = gameVersionFinder.get();

            return fromDir(dir, gameVersion, Orient.getWinArch());
        } catch (NotFoundException ignored) {
            return fromDirIgnoreVersion(dir);
        }
    }

    protected GameVersionFinder getGameVersionFinder() {
        return Context.getService(GameVersionFinder.class);
    }

    @Nonnull
    @Override
    public File get() throws NotFoundException {
        GameExeFinder registryGameExeFinder = getRegistryGameExeFinder();

        try {
            return registryGameExeFinder.get();
        } catch (NotFoundException e) {
        }

        GameDirFinder gameDirFinder = getGameDirFinder();

        try {
            File gameDir = gameDirFinder.get();

            return getGameExeInDir(gameDir);
        } catch (NotFoundException e) {
        }

        throw new NotFoundException();
    }

    protected GameExeFinder getRegistryGameExeFinder() {
        return Context.getService(RegistryGameExeFinder.class);
    }

    protected GameDirFinder getGameDirFinder() {
        return Context.getService(GameDirFinder.class);
    }
}