package dev.letsgoaway.geyserextras.core.commands;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.locale.GELocale;
import dev.letsgoaway.geyserextras.core.preferences.JavaPreferencesData;

import java.util.List;
import java.util.UUID;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;
import static dev.letsgoaway.geyserextras.core.GeyserExtras.SERVER;


public class EmoteChatCommand implements BedrockCommand {

    // dont do anything bc this is configured in the chat settings on bedrock edition
    // and geyserextras doesnt send a message anyway
    @Override
    public void onExecute(ExtrasPlayer player, List<String> args, String label) {
        return;
    }

    @Override
    public void onJavaExecute(UUID playerUUID, List<String> args, String label) {
        JavaPreferencesData data = GE.getJavaPreferencesData(playerUUID);
        if (label.equalsIgnoreCase("unmuteemotechat")) {
            data.muteEmoteChat = false;
        } else if (label.equalsIgnoreCase("muteemotechat")) {
            data.muteEmoteChat = true;
        } else {
            data.muteEmoteChat = !data.muteEmoteChat;
        }
        data.save();
        SERVER.sendRawMessage(playerUUID, GELocale.translate("ge.settings.java.emotechat." + (data.muteEmoteChat ? "muted" : "unmuted"), GE.getConfig().getDefaultLocale()));
    }
}
