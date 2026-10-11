package dev.nenicadi.figasmo.mixins

import dev.nenicadi.figasmo.utils.EventAccessor
import org.figuramc.figura.lua.LuaWhitelist
import org.figuramc.figura.lua.api.event.EventsAPI
import org.figuramc.figura.lua.api.event.LuaEvent
import org.figuramc.figura.lua.docs.LuaFieldDoc
import org.spongepowered.asm.mixin.Final
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.Unique
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(value = [EventsAPI::class], remap = false)
abstract class EventsMixin : EventAccessor {

    @Shadow
    @Final
    private lateinit var events: MutableMap<String, LuaEvent>

    @Unique
    @LuaWhitelist
    @LuaFieldDoc("events.player_speak")
    lateinit var playerSpeak: LuaEvent

    @Inject(method = ["<init>"], at = [At("RETURN")])
    private fun customEvents(ci: CallbackInfo) {
        playerSpeak = LuaEvent()
        events["PLAYER_SPEAK"] = playerSpeak
    }

    override fun getSpeakEvent(): LuaEvent = playerSpeak

}