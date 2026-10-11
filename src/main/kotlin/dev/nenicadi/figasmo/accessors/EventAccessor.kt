package dev.nenicadi.figasmo.accessors

import org.figuramc.figura.lua.api.event.LuaEvent

interface EventAccessor {
    fun getSpeakEvent(): LuaEvent
}