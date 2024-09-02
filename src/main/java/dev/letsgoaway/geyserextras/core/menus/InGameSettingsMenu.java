package dev.letsgoaway.geyserextras.core.menus;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.BedrockForm;
import dev.letsgoaway.geyserextras.core.menus.settings.*;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.geyser.session.GeyserSession;

public class InGameSettingsMenu extends BedrockForm {
    @Override
    public CustomForm.Builder create(ExtrasPlayer player) {
        setTitle("GeyserExtras Settings");
        GeyserSession session = player.getSession();
        SettingsSection.build(this, session, player);
        if (player.isVR()) {
            VRSection.build(this, session, player);
        }
        HudSection.build(this, session, player);
        BindingsSection.build(this, player);
        DebugSection.build(this, session, player);
        CreditsSection.build(this, session, player);
        return super.create(player);
    }

}
