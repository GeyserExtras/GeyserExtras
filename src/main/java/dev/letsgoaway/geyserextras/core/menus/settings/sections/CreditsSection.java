package dev.letsgoaway.geyserextras.core.menus.settings.sections;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.cache.Cache;
import dev.letsgoaway.geyserextras.core.form.BedrockForm;
import dev.letsgoaway.geyserextras.core.form.elements.Label;
import dev.letsgoaway.geyserextras.core.locale.BedrockLocale;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.geyser.session.GeyserSession;

import java.util.List;

public class CreditsSection extends Section {
    @Override
    public void build(BedrockForm menu, GeyserSession session, ExtrasPlayer player) {
        menu.add(new Label(Cache.CREDITS_TEXT));
    }

    @Override
    public List<String> getHeader(ExtrasPlayer player) {
        return List.of(BedrockLocale.OPTIONS.CREDITS, "");
    }

    @Override
    public FormImage getImage() {
        return FormImage.of(FormImage.Type.PATH, "textures/ui/permissions_member_star.png");
    }
}
