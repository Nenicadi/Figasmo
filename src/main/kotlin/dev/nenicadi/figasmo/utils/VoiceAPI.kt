package dev.nenicadi.figasmo.utils

import dev.nenicadi.figasmo.Figasmo
import dev.nenicadi.figasmo.accessors.VoiceAccessor
import dev.nenicadi.figasmo.bridges.PlasmoBridge
import dev.nenicadi.figasmo.bridges.SimpleVoiceBridge
import net.fabricmc.loader.api.FabricLoader
import org.figuramc.figura.lua.LuaWhitelist
import org.figuramc.figura.lua.docs.LuaMethodDoc
import org.figuramc.figura.lua.docs.LuaMethodOverload
import org.figuramc.figura.lua.docs.LuaTypeDoc
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@LuaWhitelist
@LuaTypeDoc(
    name = "Voice Addon",
    value = "voice"
)
object VoiceAPI {

    private val voiceLevelsCache = ConcurrentHashMap<UUID, Double>()
    private val scheduler = Executors.newScheduledThreadPool(1)
        { Thread(it).apply { isDaemon = true } }

    init {
        scheduler.scheduleAtFixedRate({
            voiceLevelsCache.forEach { (uuid, audioPercent) ->
                saveVoiceLevel(uuid, (audioPercent / 2))
            }
        }, 0, 100, TimeUnit.MILLISECONDS)
    }

    fun saveVoiceLevel(playerUuid: UUID, audioPercent: Double) {
        if (audioPercent < 0.05)
            voiceLevelsCache.remove(playerUuid)
        else
            voiceLevelsCache[playerUuid] = audioPercent
    }

    private val activeBridge: VoiceAccessor? by lazy {
        val loader = FabricLoader.getInstance()
        when {
            loader.isModLoaded("plasmovoice") -> PlasmoBridge()
            loader.isModLoaded("voicechat") -> SimpleVoiceBridge()
            else -> null
        }
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = [LuaMethodOverload(
            argumentTypes = [String::class],
            argumentNames = ["uuid"]
        )],
        value = "voice.getVoiceLevel"
    )
    @Suppress("UNUSED")
    fun getVoiceLevel(uuid: String): Double =
        voiceLevelsCache[UUID.fromString(uuid)] ?: 0.0

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = [LuaMethodOverload(
            argumentTypes = [String::class],
            argumentNames = ["uuid"]
        )],
        value = "voice.isMuted"
    )
    @Suppress("UNUSED")
    fun isMuted(uuid: String): Boolean {
        val uuid = UUID.fromString(uuid)
        return activeBridge?.isMuted(uuid) ?: true
    }

    override fun toString(): String = "${this::class.java.name} (${Figasmo.MOD_NAME})"

}