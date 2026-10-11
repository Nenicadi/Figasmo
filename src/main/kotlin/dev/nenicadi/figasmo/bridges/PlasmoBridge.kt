package dev.nenicadi.figasmo.bridges

import dev.nenicadi.figasmo.accessors.VoiceAccessor
import dev.nenicadi.figasmo.modules.PlasmoVoiceAddon
import java.util.UUID

class PlasmoBridge: VoiceAccessor {
    override fun isMuted(player: UUID): Boolean {
        return PlasmoVoiceAddon.isMuted(player)
    }
}