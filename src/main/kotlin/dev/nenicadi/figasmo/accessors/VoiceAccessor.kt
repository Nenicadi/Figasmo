package dev.nenicadi.figasmo.accessors

import java.util.UUID

interface VoiceAccessor {
    fun isMuted(player: UUID): Boolean
}