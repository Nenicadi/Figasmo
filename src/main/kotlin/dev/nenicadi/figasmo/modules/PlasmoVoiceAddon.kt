package dev.nenicadi.figasmo.modules

import dev.nenicadi.figasmo.Figasmo
import dev.nenicadi.figasmo.utils.AudioUtils
import dev.nenicadi.figasmo.utils.AudioUtils.speakEvent
import dev.nenicadi.figasmo.utils.VoiceAPI
import net.minecraft.client.Minecraft
import su.plo.voice.api.addon.AddonInitializer
import su.plo.voice.api.addon.InjectPlasmoVoice
import su.plo.voice.api.addon.annotation.Addon
import su.plo.voice.api.client.PlasmoVoiceClient
import su.plo.voice.api.client.event.audio.capture.AudioCaptureEvent
import su.plo.voice.api.client.event.audio.source.AudioSourceWriteEvent
import su.plo.voice.api.client.event.connection.UdpClientPacketReceivedEvent
import su.plo.voice.api.client.event.connection.UdpClientPacketSendEvent
import su.plo.voice.api.event.EventSubscribe
import su.plo.voice.proto.data.audio.source.PlayerSourceInfo
import su.plo.voice.proto.data.audio.source.SourceInfo
import su.plo.voice.proto.packets.udp.clientbound.SelfAudioInfoPacket
import su.plo.voice.proto.packets.udp.serverbound.PlayerAudioPacket

@Addon(
    id = Figasmo.MOD_ID,
    name = Figasmo.MOD_NAME,
    version = Figasmo.MOD_VERSION,
    authors = [Figasmo.MOD_AUTHORS],
)
class PlasmoVoiceAddon: AddonInitializer {

    @InjectPlasmoVoice
    lateinit var voiceClient: PlasmoVoiceClient

    override fun onAddonInitialize() {
        Figasmo.logger.info("PlasmoVoice hooked!")
    }

    private var lastAudioLevel: Double = 0.0

    @EventSubscribe
    @Suppress("UNUSED")
    private fun onAudioCapture(event: AudioCaptureEvent) {
        lastAudioLevel = AudioUtils.calculateAudioLevel(event.samples)
    }

    @EventSubscribe
    @Suppress("UNUSED")
    private fun onSourceWrite(event: AudioSourceWriteEvent) {
        val source = event.source

        val sourceInfo: SourceInfo = source.sourceInfo
        if (sourceInfo !is PlayerSourceInfo) return

        val audioLevel = AudioUtils.calculateAudioLevel(event.samples)
        val audioPercent = AudioUtils.levelToPercent(audioLevel)

        val playerId = sourceInfo.playerInfo.playerId
        VoiceAPI.saveVoiceLevel(playerId, audioPercent)

        speakEvent(playerId)
    }

    @EventSubscribe
    @Suppress("UNUSED")
    private fun onSelfAudioPacket(event: UdpClientPacketReceivedEvent) {
        if (event.packet !is SelfAudioInfoPacket) return

        val player = Minecraft.getInstance().player ?: return
        val audioPercent = AudioUtils.levelToPercent(lastAudioLevel)

        VoiceAPI.saveVoiceLevel(player.uuid, audioPercent)
    }

    @EventSubscribe
    @Suppress("UNUSED")
    private fun onPlayerSpeak(event: UdpClientPacketSendEvent) {
        if (event.packet is PlayerAudioPacket) speakEvent()
    }

}