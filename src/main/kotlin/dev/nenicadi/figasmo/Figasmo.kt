package dev.nenicadi.figasmo

import dev.nenicadi.figasmo.modules.PlasmoVoiceAddon
import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import su.plo.voice.api.client.PlasmoVoiceClient

class Figasmo: ModInitializer {

    companion object {
        const val MOD_ID: String = "figasmo"
        const val MOD_NAME: String = "Figasmo"
        const val MOD_VERSION: String = "1.0.0"
        const val MOD_AUTHORS: String = "Nenicadi"

        val instance: FabricLoader = FabricLoader.getInstance()
        val logger: Logger = LoggerFactory.getLogger(Figasmo::class.java.name)
    }

    override fun onInitialize() {
        logger.info("Initializing Figasmo...")
        FigasmoAddon().init()

        if (instance.isModLoaded("plasmovoice"))
            PlasmoVoiceClient.getAddonsLoader().load(PlasmoVoiceAddon())
    }

}
