package dev.letsgoaway.geyserextras.core.menus.settings.menus;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.BedrockForm;
import dev.letsgoaway.geyserextras.core.menus.settings.sections.Section;
import org.geysermc.cumulus.form.CustomForm;
import org.geysermc.geyser.session.GeyserSession;

public class SectionMenu extends BedrockForm {
    private Section section;

    public SectionMenu(Section section) {
        this.section = section;
    }

    @Override
    public CustomForm.Builder create(ExtrasPlayer player) {
        setTitle(section.getHeader().get(0));
        GeyserSession session = player.getSession();
        section.create(this,session,player);
        return super.create(player);
    }

    @Override
    public void onClose(ExtrasPlayer player){
        player.sendForm(new SettingsMenu());
    }

    @Override
    public void onSubmit(ExtrasPlayer player){
        player.sendForm(new SettingsMenu());
    }
}
