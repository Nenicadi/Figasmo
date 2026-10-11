package dev.nenicadi.figasmo.utils

import org.figuramc.figura.lua.api.event.LuaEvent

interface EventAccessor {
    fun getSpeakEvent(): LuaEvent
}