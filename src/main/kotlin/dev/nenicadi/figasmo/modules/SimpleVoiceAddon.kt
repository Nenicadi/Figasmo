package dev.nenicadi.figasmo.modules

import de.maxhenkel.voicechat.api.VoicechatApi
import de.maxhenkel.voicechat.api.VoicechatClientApi
import de.maxhenkel.voicechat.api.VoicechatPlugin
import de.maxhenkel.voicechat.api.events.ClientReceiveSoundEvent
import de.maxhenkel.voicechat.api.events.ClientReceiveSoundEvent.*
import de.maxhenkel.voicechat.api.events.ClientSoundEvent
import de.maxhenkel.voicechat.api.events.ClientVoicechatInitializationEvent
import de.maxhenkel.voicechat.api.events.EventRegistration
import dev.nenicadi.figasmo.Figasmo
import dev.nenicadi.figasmo.utils.AudioUtils
import dev.nenicadi.figasmo.utils.AudioUtils.speakEvent
import dev.nenicadi.figasmo.utils.VoiceAPI
import net.minecraft.client.Minecraft
import java.util.UUID

object SimpleVoiceAddon: VoicechatPlugin {

    lateinit var voicechatApi: VoicechatApi
    lateinit var clientApi: VoicechatClientApi

    override fun getPluginId(): String = Figasmo.MOD_ID
    override fun initialize(api: VoicechatApi) {
        Figasmo.logger.info("VoiceChat hooked!")
        voicechatApi = api
    }

    override fun registerEvents(registration: EventRegistration) {
        registration.registerEvent(ClientSoundEvent::class.java, this::onLocalPlayerEvent)
        registration.registerEvent(EntitySound::class.java, this::onAnotherPlayerEvent)
        registration.registerEvent(StaticSound::class.java, this::onAnotherPlayerEvent)
        registration.registerEvent(LocationalSound::class.java, this::onAnotherPlayerEvent)

        registration.registerEvent(ClientVoicechatInitializationEvent::class.java, this::onClientInit)
    }

    private fun onAnotherPlayerEvent(event: ClientReceiveSoundEvent) {
        val voicechat = event.voicechat
        if (voicechat.volumeConfig.getVolume(event.id) <= 0) return

        val audioLevel = AudioUtils.calculateAudioLevel(event.rawAudio)
        val audioPercent = AudioUtils.levelToPercent(audioLevel)

        VoiceAPI.saveVoiceLevel(event.id, audioPercent)
        speakEvent(event.id)
    }

    private fun onClientInit(event: ClientVoicechatInitializationEvent) {
        clientApi = event.voicechat
    }

    private fun onLocalPlayerEvent(event: ClientSoundEvent) {
        val player = Minecraft.getInstance().player ?: return

        val audioLevel = AudioUtils.calculateAudioLevel(event.rawAudio)
        val audioPercent = AudioUtils.levelToPercent(audioLevel)

        VoiceAPI.saveVoiceLevel(player.uuid, audioPercent)
        speakEvent()
    }

    fun isMuted(player: UUID): Boolean {
        return clientApi.isDisabled(player)
                || clientApi.isDisconnected(player)
    }

}