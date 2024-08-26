package dev.letsgoaway.geyserextras.core.features.skinsaver;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import org.geysermc.geyser.session.auth.BedrockClientData;
import org.geysermc.geyser.util.FileUtils;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;
import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;

public class SkinSaver {
    public static void save(ExtrasPlayer player) {
        try {
            // Skins
            BedrockClientData data = player.getSession().getClientData();
            Path skinsFolder = Files.createDirectories(SERVER.getPluginFolder().resolve("skins/" + data.getSkinId().replace(data.getCapeId(), "") + "/"));

            File skinFile = skinsFolder.resolve("skin.png").toFile();
            byte[] skinBytes = Base64.getDecoder().decode(data.getSkinData().getBytes(StandardCharsets.UTF_8));
            ImageIO.write(getFromSkinData(data.getSkinImageWidth(), data.getSkinImageHeight(), skinBytes), "png", skinFile);

            String geometryName = new String(Base64.getDecoder().decode(data.getGeometryName().getBytes(StandardCharsets.UTF_8)));
            String geometryData = new String(Base64.getDecoder().decode(data.getGeometryData().getBytes(StandardCharsets.UTF_8)));
            String animationData = new String(Base64.getDecoder().decode(data.getSkinAnimationData().getBytes(StandardCharsets.UTF_8)));

            FileUtils.writeFile(skinsFolder.resolve("skin.geometry.json").toFile(), geometryData.toCharArray());
            FileUtils.writeFile(skinsFolder.resolve("skin.geometry_name.json").toFile(), geometryName.toCharArray());
            if (!animationData.isEmpty()) {
                FileUtils.writeFile(skinsFolder.resolve("skin.animation.json").toFile(), animationData.toCharArray());
            }
            // Capes
            if (!data.getCapeId().isEmpty()) {
                Path capesFolder = Files.createDirectories(SERVER.getPluginFolder().resolve("capes/" + data.getCapeId() + "/"));
                File capeFile = capesFolder.resolve("cape.png").toFile();

                byte[] capeBytes = data.getCapeData();
                ImageIO.write(getFromSkinData(data.getCapeImageWidth(), data.getCapeImageHeight(), capeBytes), "png", capeFile);
            }

        } catch (IOException e) {
        }
    }

    private static BufferedImage getFromSkinData(int width, int height, byte[] pixelData) {
        BufferedImage argbImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int[] pixels = new int[width * height];
        for (int i = 0; i < pixels.length; i++) {
            int a = (pixelData[i * 4 + 3] & 0xFF) << 24; // Alpha
            int r = (pixelData[i * 4] & 0xFF) << 16; // Red
            int g = (pixelData[i * 4 + 1] & 0xFF) << 8;  // Green
            int b = (pixelData[i * 4 + 2] & 0xFF);      // Blue
            pixels[i] = a | r | g | b;
        }

        argbImage.setRGB(0, 0, width, height, pixels, 0, width);
        return argbImage;
    }
}
