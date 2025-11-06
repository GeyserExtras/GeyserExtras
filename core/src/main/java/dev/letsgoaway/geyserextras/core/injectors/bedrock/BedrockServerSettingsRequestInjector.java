package dev.letsgoaway.geyserextras.core.injectors.bedrock;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.injectors.GeyserHandler;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Remappable;
import dev.letsgoaway.geyserextras.core.utils.IsAvailable;
import org.cloudburstmc.protocol.bedrock.packet.ServerSettingsRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.ServerSettingsResponsePacket;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.form.impl.FormDefinitions;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.bedrock.BedrockServerSettingsRequestTranslator;

import java.util.concurrent.TimeUnit;

import static dev.letsgoaway.geyserextras.core.GeyserExtras.GE;

public class BedrockServerSettingsRequestInjector extends BedrockServerSettingsRequestTranslator {
    @Override
    public void translate(GeyserSession session, ServerSettingsRequestPacket packet) {
        if (!GE.getConfig().isEnableGeyserExtrasMenu()) {
            super.translate(session, packet);
            return;
        }

        ExtrasPlayer player = ExtrasPlayer.get(session);
        // Sending settings form with floodgate causes linkage error :/
        if (IsAvailable.floodgate()) {
            player.sendSystemToast(player.translateGE("ge.form_error_toast.line1"), player.translateGE("ge.form_error_toast.line2"));
            super.translate(session, packet);
            return;
        }

        player.getPreferences().runAction(Remappable.SETTINGS);
        CustomForm form = player.getPreferences().getSettingsMenuForm().open(player);
        int formId = session.getFormCache().addForm(form);

        String jsonData = FormDefinitions.instance().codecFor(form).jsonData(form);

        // Fixes https://bugs.mojang.com/browse/MCPE-94012 because of the delay
        session.scheduleInEventLoop(() -> {
            ServerSettingsResponsePacket serverSettingsResponsePacket = new ServerSettingsResponsePacket();
            serverSettingsResponsePacket.setFormData(jsonData);
            serverSettingsResponsePacket.setFormId(formId);
            session.sendUpstreamPacket(serverSettingsResponsePacket);
        }, 1, TimeUnit.SECONDS);
    }
}
