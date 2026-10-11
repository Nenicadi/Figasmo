package dev.nenicadi.figasmo.utils

import org.figuramc.figura.FiguraMod
import org.figuramc.figura.avatar.Avatar
import org.figuramc.figura.avatar.AvatarManager
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaTable
import java.util.UUID
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.sqrt

object AudioUtils {

    fun levelToPercent(lastAudioLevel: Double): Double =
        (1.0 - (abs(lastAudioLevel) / 64)).coerceIn(0.0, 1.0)

    fun calculateAudioLevel(samples: ShortArray?): Double {
        if (samples == null || samples.isEmpty()) return -127.0

        var rms = 0.0
        for (sample in samples) {
            val sample: Double = sample.toDouble() / Short.MAX_VALUE.toDouble()
            rms += sample * sample
        }

        val sampleCount = samples.size / 2
        rms = if (sampleCount == 0) 0.0 else sqrt(rms / sampleCount)

        return if (rms > 0.0) max(20.0 * log10(rms), -127.0) else -127.0;
    }

    fun speakEvent(uuid: UUID? = null) {
        val self = FiguraMod.getLocalPlayerUUID()

        val avatar: Avatar = AvatarManager.getAvatarForPlayer(self) ?: return
        val runtime = avatar.luaRuntime ?: return
        val microphoneEvent = (runtime.events as EventAccessor).getSpeakEvent()

        avatar.run(microphoneEvent, avatar.tick, (uuid ?: self).toString())
    }

}