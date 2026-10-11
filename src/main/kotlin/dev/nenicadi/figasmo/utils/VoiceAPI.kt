package dev.nenicadi.figasmo.utils

import dev.nenicadi.figasmo.Figasmo
import org.figuramc.figura.lua.LuaWhitelist
import org.figuramc.figura.lua.docs.LuaMethodDoc
import org.figuramc.figura.lua.docs.LuaMethodOverload
import org.figuramc.figura.lua.docs.LuaTypeDoc
import java.util.*

@LuaWhitelist
@LuaTypeDoc(
    name = "Voice Addon",
    value = "voice"
)
object VoiceAPI {

    private val voiceLevelsCache: MutableMap<UUID, Double> = mutableMapOf()

    fun saveVoiceLevel(playerUuid: UUID, audioPercent: Double) {
        if (audioPercent <= 0)
            voiceLevelsCache.remove(playerUuid)
        else
            voiceLevelsCache[playerUuid] = audioPercent
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = [LuaMethodOverload(
            argumentTypes = [String::class],
            argumentNames = ["uuid"]
        )],
        value = "voice.get_voice_level"
    )
    @Suppress("UNUSED")
    fun getVoiceLevel(uuid: String): Double =
        voiceLevelsCache[UUID.fromString(uuid)] ?: 0.0

    override fun toString(): String = "${this::class.java.name} (${Figasmo.MOD_NAME})"

}