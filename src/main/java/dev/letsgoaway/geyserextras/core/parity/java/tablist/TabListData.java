package dev.letsgoaway.geyserextras.core.parity.java.tablist;

import dev.letsgoaway.geyserextras.core.ExtrasPlayer;
import lombok.Getter;
import lombok.Setter;

public class TabListData {
    private final ExtrasPlayer player;

    @Setter
    @Getter
    private String header = " ";

    @Setter
    @Getter
    private String footer = " ";

    public TabListData(ExtrasPlayer player) {
        this.player = player;
    }
}
