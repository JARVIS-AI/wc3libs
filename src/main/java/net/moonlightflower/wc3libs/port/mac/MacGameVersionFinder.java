package net.moonlightflower.wc3libs.port.mac;

import net.moonlightflower.wc3libs.port.GameVersion;
import net.moonlightflower.wc3libs.port.GameVersionFinder;
import net.moonlightflower.wc3libs.port.NotFoundException;

import javax.annotation.Nonnull;
import java.io.File;

public class MacGameVersionFinder implements GameVersionFinder {
    @Nonnull
    @Override
    public GameVersion get() throws NotFoundException {
        try {
            GameVersion gameVersion = PListGameVersionParser.get();

            return gameVersion;
        } catch (Exception e) {
            throw new NotFoundException(e);
        }
    }
}
