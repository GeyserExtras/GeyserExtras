package dev.letsgoaway.geyserextras.core.menus.settings.sections;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import dev.letsgoaway.geyserextras.core.form.BedrockForm;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Action;
import dev.letsgoaway.geyserextras.core.preferences.bindings.Remappable;
import dev.letsgoaway.geyserextras.core.form.elements.Dropdown;
import dev.letsgoaway.geyserextras.core.locale.BedrockLocale;
import org.geysermc.cumulus.util.FormImage;
import org.geysermc.geyser.session.GeyserSession;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class BindingsSection extends Section {
    @Override
    public void build(BedrockForm menu, GeyserSession session, ExtrasPlayer player) {
        LinkedHashMap<String, Action> actionNames = new LinkedHashMap<>();
        for (Action action : Action.values()) {
            actionNames.put(action.translate(player), action);
        }
        for (Remappable binding : Remappable.values()) {
            menu.add(new Dropdown(binding.translate(player),
                    new ArrayList<>(actionNames.keySet()),
                    player.getPreferences().getAction(binding).translate(player), (str) -> {
                player.getPreferences().setAction(binding, actionNames.get(str));
            }));
        }
    }

    @Override
    public List<String> getHeader() {
        return List.of(BedrockLocale.CONTROLLER.BINDINGS, "");
    }

    @Override
    public FormImage getImage() {
        return FormImage.of(FormImage.Type.PATH, "textures/ui/controller_glyph_color.png");
    }
}
