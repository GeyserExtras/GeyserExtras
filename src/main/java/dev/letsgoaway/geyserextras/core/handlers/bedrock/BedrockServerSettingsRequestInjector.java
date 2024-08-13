package dev.letsgoaway.geyserextras.core.handlers.bedrock;

import dev.letsgoaway.geyserextras.core.handlers.GeyserHandler;
import org.cloudburstmc.protocol.bedrock.packet.ServerSettingsRequestPacket;
import org.cloudburstmc.protocol.bedrock.packet.ServerSettingsResponsePacket;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.cumulus.form.impl.FormDefinitions;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.bedrock.BedrockServerSettingsRequestTranslator;
import org.geysermc.geyser.util.SettingsUtils;

import java.util.concurrent.TimeUnit;

public class BedrockServerSettingsRequestInjector extends BedrockServerSettingsRequestTranslator {
    @Override
    public void translate(GeyserSession session, ServerSettingsRequestPacket packet) {

        CustomForm form = GeyserHandler.getPlayer(session).getPreferences().getSettingsMenuForm().open(GeyserHandler.getPlayer(session));
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
