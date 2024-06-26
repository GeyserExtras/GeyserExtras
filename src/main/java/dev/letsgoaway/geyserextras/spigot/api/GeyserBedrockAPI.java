package dev.letsgoaway.geyserextras.spigot.api;

import dev.letsgoaway.geyserextras.spigot.Config;
import dev.letsgoaway.geyserextras.spigot.GeyserExtras;
import dev.letsgoaway.geyserextras.spigot.menus.OptionalPacks;
import dev.letsgoaway.geyserextras.spigot.player.PlayerDevice;
import dev.letsgoaway.geyserextras.spigot.player.PlayerInputType;
import dev.letsgoaway.geyserextras.spigot.player.PlayerUIProfile;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.geysermc.cumulus.form.Form;
import org.geysermc.cumulus.form.util.FormBuilder;
import org.geysermc.event.subscribe.OwnedSubscriber;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.bedrock.camera.GuiElement;
import org.geysermc.geyser.api.connection.GeyserConnection;
import org.geysermc.geyser.api.pack.PackCodec;
import org.geysermc.geyser.api.pack.ResourcePack;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Filter;

public class GeyserBedrockAPI extends BedrockPluginAPI implements org.geysermc.geyser.api.event.EventRegistrar {
    private final org.geysermc.geyser.api.GeyserApi api = org.geysermc.geyser.api.GeyserApi.api();
    private final HashMap<UUID, ResourcePack> resourcePackHashMap = new HashMap<>();
    private ResourcePack GeyserOptionalPack = null;
    private ResourcePack GeyserExtrasPack = null;
    private final HashMap<UUID, Path> resourcePackPathMap = new HashMap<>();

    public GeyserBedrockAPI() {
        super();
        tryRegisterEventBus();
    }

    @Override
    public void disable() {
    }

    ArrayList<OwnedSubscriber<?, ?>> subscribers = new ArrayList<>();

    private void tryRegisterEventBus() {
        subscribers.add(
                api.eventBus().subscribe(this, org.geysermc.geyser.api.event.bedrock.ClientEmoteEvent.class, this::onClientEmoteEvent)
        );
        subscribers.add(
                api.eventBus().subscribe(this, org.geysermc.geyser.api.event.bedrock.SessionLoadResourcePacksEvent.class, this::onResourcePackLoadEvent)
        );
        subscribers.add(
                api.eventBus().subscribe(this, org.geysermc.geyser.api.event.bedrock.SessionDisconnectEvent.class, (ev) -> {
                    if ((ev.disconnectReason().equals("disconnectionScreen.resourcePack")) || (ev.disconnectReason().equals("Bedrock client disconnected") && ev.connection().javaUuid() == null)) {
                        OptionalPacks.loadingResourcePacks.remove(ev.connection().xuid());
                    }
                })
        );
    }

    @Override
    public void onConfigLoad() {
        GeyserExtras.initLog.info("Loading GeyserOptionalPack...");
        GeyserOptionalPack = ResourcePack.create(PackCodec.path(GeyserExtras.plugin.getDataFolder().toPath().resolve("GeyserOptionalPack.mcpack")));
        GeyserExtras.initLog.info("GeyserOptionalPack v" + GeyserOptionalPack.manifest().header().version().toString() + " loaded succesfully!");
        GeyserExtras.initLog.info("Loading GeyserExtrasPack...");
        GeyserExtrasPack = ResourcePack.create(PackCodec.path(GeyserExtras.plugin.getDataFolder().toPath().resolve("GeyserExtrasPack.mcpack")));
        GeyserExtras.initLog.info("GeyserExtrasPack v" + GeyserExtrasPack.manifest().header().version().toString() + " loaded succesfully!");
        if (Config.packsArray.isEmpty()) {
            return;
        }
        GeyserExtras.initLog.logTask("Loading optional packs...", this::loadResources, "Optional packs loaded!");
    }

    private void loadResources() {
        Plugin geyserSpigot = Bukkit.getPluginManager().getPlugin("Geyser-Spigot");
        if (geyserSpigot == null) {
            return;
        }
        /* geyser has an annoying message where it says that paths are too long,
        so i disable the logger for it temporarily here */
        Filter oldFilter = geyserSpigot.getLogger().getFilter();
        geyserSpigot.getLogger().setFilter(record -> false);
        for (File rp : Config.packsArray) {
            ResourcePack resourcePack = ResourcePack.create(PackCodec.path(rp.toPath()));
            resourcePackHashMap.put(resourcePack.manifest().header().uuid(), resourcePack);
            resourcePackPathMap.put(resourcePack.manifest().header().uuid(), rp.toPath());
            GeyserExtras.initLog.info("Pack '" + resourcePack.manifest().header().name() + "' loaded succesfully!");
        }
        /* and reenable it here */
        geyserSpigot.getLogger().setFilter(oldFilter);
    }


    @Subscribe
    public void onClientEmoteEvent(org.geysermc.geyser.api.event.bedrock.ClientEmoteEvent ev) {
        ev.setCancelled(GeyserExtras.bplayers.get(ev.connection().javaUuid()).onPlayerEmoteEvent(ev.emoteId()));
    }

    @Subscribe
    public void onResourcePackLoadEvent(org.geysermc.geyser.api.event.bedrock.SessionLoadResourcePacksEvent ev) {
        ev.register(GeyserOptionalPack);
        ev.register(GeyserExtrasPack);
        if (OptionalPacks.loadingResourcePacks.containsKey(ev.connection().xuid())) {
            for (String id : OptionalPacks.loadingResourcePacks.get(ev.connection().xuid())) {
                ev.register(resourcePackHashMap.get(UUID.fromString(id)));
            }
        }
    }

    public void reconnect(UUID uuid) {
        api.transfer(uuid, Config.externalAddress, Config.externalPort);
    }

    @Override
    public boolean isBedrockPlayer(UUID uuid) {
        return api.isBedrockPlayer(uuid);
    }

    @Override
    public boolean sendForm(UUID uuid, Form form) {
        return api.sendForm(uuid, form);
    }

    @Override
    public boolean sendForm(UUID uuid, FormBuilder<?, ?, ?> form) {
        return this.sendForm(uuid, form.build());
    }

    @Override
    public PlayerDevice getPlayerDevice(UUID uuid) {
        return switch (Objects.requireNonNull(api.connectionByUuid(uuid)).platform()) {
            case GOOGLE -> PlayerDevice.ANDROID;
            case IOS -> PlayerDevice.IOS;
            case OSX -> PlayerDevice.OSX;
            case AMAZON -> PlayerDevice.AMAZON;
            case GEARVR -> PlayerDevice.GEARVR;
            case HOLOLENS -> PlayerDevice.HOLOLENS;
            case UWP, WIN32 -> PlayerDevice.WINDOWS;
            case DEDICATED -> PlayerDevice.DEDICATED;
            case TVOS -> PlayerDevice.TVOS;
            case PS4 -> PlayerDevice.PLAYSTATION;
            case NX -> PlayerDevice.SWITCH;
            case XBOX -> PlayerDevice.XBOX;
            case WINDOWS_PHONE -> PlayerDevice.WINDOWS_PHONE;
            default -> PlayerDevice.UNKNOWN;
        };
    }

    @Override
    public PlayerInputType getPlayerInputType(UUID uuid) {
        return switch (Objects.requireNonNull(api.connectionByUuid(uuid)).inputMode()) {
            case CONTROLLER -> PlayerInputType.CONTROLLER;
            case TOUCH -> PlayerInputType.TOUCH;
            case KEYBOARD_MOUSE -> PlayerInputType.KEYBOARD_MOUSE;
            case VR -> PlayerInputType.VR;
            default -> PlayerInputType.UNKNOWN;
        };
    }

    @Override
    public PlayerUIProfile getPlayerUIProfile(UUID uuid) {
        return switch (Objects.requireNonNull(api.connectionByUuid(uuid)).uiProfile()) {
            case CLASSIC -> PlayerUIProfile.CLASSIC;
            case POCKET -> PlayerUIProfile.POCKET;
        };
    }

    @Override
    public String getXboxUsername(UUID uuid) {
        return Objects.requireNonNull(api.connectionByUuid(uuid)).bedrockUsername();
    }

    @Override
    public String getPlayerXUID(UUID uuid) {
        return Objects.requireNonNull(api.connectionByUuid(uuid)).xuid();
    }

    @Override
    public boolean isLinked(UUID uuid) {
        return Objects.requireNonNull(api.connectionByUuid(uuid)).isLinked();
    }

    @Override
    public void sendFog(UUID uuid, String fog) {
        org.geysermc.geyser.api.connection.GeyserConnection connection = api.connectionByUuid(uuid);
        if (connection == null) {
            return;
        }
        connection.camera().sendFog(fog);
    }

    @Override
    public void removeFog(UUID uuid, String fog) {
        org.geysermc.geyser.api.connection.GeyserConnection connection = api.connectionByUuid(uuid);
        if (connection == null) {
            return;
        }
        connection.camera().removeFog(fog);
    }

    @Override
    public UUID getPackID(Path path) {
        for (Map.Entry<UUID, Path> id : resourcePackPathMap.entrySet()) {
            if (id.getValue().equals(path)) {
                return id.getKey();
            }
        }
        return UUID.randomUUID();
    }

    @Override
    public String getPackName(String id) {
        return resourcePackHashMap.get(UUID.fromString(id)).manifest().header().name();
    }


    @Override
    public String getPackDescription(String id) {
        return resourcePackHashMap.get(UUID.fromString(id)).manifest().header().description();
    }

    @Override
    public String getPackVersion(String id) {
        return resourcePackHashMap.get(UUID.fromString(id)).manifest().header().version().toString();
    }

    @Override
    @Nullable
    public Path getPackPath(String id) {
        return resourcePackPathMap.get(UUID.fromString(id));
    }

    @Override
    public boolean getPackExists(String id) {
        try {
            return resourcePackPathMap.containsKey(UUID.fromString(id));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public void hidePaperDoll(UUID uuid) {
        GeyserConnection connection = api.connectionByUuid(uuid);
        if (connection != null)
            if (!connection.camera().isHudElementHidden(GuiElement.PAPER_DOLL)){
                connection.camera().hideElement(GuiElement.PAPER_DOLL);
            }
    }

}
