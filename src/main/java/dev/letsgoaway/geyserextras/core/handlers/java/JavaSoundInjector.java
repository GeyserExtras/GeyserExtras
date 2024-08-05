package dev.letsgoaway.geyserextras.core.handlers.java;

import dev.letsgoaway.geyserextras.core.Config;
import dev.letsgoaway.geyserextras.core.SoundReplacer;
import org.cloudburstmc.math.vector.Vector3f;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.translator.protocol.PacketTranslator;
import org.geysermc.geyser.translator.protocol.java.level.JavaSoundTranslator;
import org.geysermc.geyser.util.SoundUtils;
import org.geysermc.mcprotocollib.protocol.data.game.level.sound.Sound;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundSoundPacket;

public class JavaSoundInjector extends PacketTranslator<ClientboundSoundPacket> {
    JavaSoundTranslator translator = new JavaSoundTranslator();

    @Override
    public void translate(GeyserSession session, ClientboundSoundPacket packet) {
        if (!Config.javaCombatSounds) {
            translator.translate(session, packet);
            return;
        }
        Vector3f position = Vector3f.from(packet.getX(), packet.getY(), packet.getZ());
        SoundUtils.playSound(session, replaceSound(packet.getSound(), SoundReplacer.getSound(packet.getSound().getName())), position, packet.getVolume(), packet.getPitch());
    }

    public Sound replaceSound(Sound oldsound, String name) {
        return new Sound() {
            @Override
            public String getName() {
                return name;
            }

            @Override
            public boolean isNewSystem() {
                return oldsound.isNewSystem();
            }

            @Override
            public float getRange() {
                return oldsound.getRange();
            }
        };
    }
}
