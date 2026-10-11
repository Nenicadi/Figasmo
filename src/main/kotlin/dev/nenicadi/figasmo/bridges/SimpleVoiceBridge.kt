package dev.nenicadi.figasmo.bridges

import dev.nenicadi.figasmo.accessors.VoiceAccessor
import dev.nenicadi.figasmo.modules.SimpleVoiceAddon
import java.util.UUID

class SimpleVoiceBridge: VoiceAccessor {
    override fun isMuted(player: UUID): Boolean {
        return SimpleVoiceAddon.isMuted(player)
    }
}