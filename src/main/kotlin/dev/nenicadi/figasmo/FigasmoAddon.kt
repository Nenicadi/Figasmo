package dev.nenicadi.figasmo

import dev.nenicadi.figasmo.utils.VoiceAPI
import org.figuramc.figura.avatar.Avatar
import org.figuramc.figura.entries.FiguraAPI
import org.figuramc.figura.entries.annotations.FiguraAPIPlugin
import org.figuramc.figura.lua.FiguraAPIManager.API_GETTERS
import org.figuramc.figura.lua.FiguraAPIManager.WHITELISTED_CLASSES
import org.figuramc.figura.lua.LuaWhitelist
import org.figuramc.figura.lua.docs.LuaTypeDoc
import java.util.function.Function

@FiguraAPIPlugin
@LuaWhitelist
object FigasmoAddon: FiguraAPI {
    val figasmoPluginClasses: Array<Class<*>> = arrayOf(
        this::class.java,
        VoiceAPI::class.java,
    )

    fun init() {
        Figasmo.logger.info("Figura addon initialized!")

        WHITELISTED_CLASSES.add(VoiceAPI::class.java)
        API_GETTERS["voice"] = Function { VoiceAPI }
    }

    lateinit var avatar: Avatar
    override fun build(avatar: Avatar): FiguraAPI {
        this.avatar = avatar
        return this
    }

    override fun getName(): String = Figasmo.MOD_ID

    override fun getDocsClasses(): MutableList<Class<*>> {
        val classesToRegister: MutableList<Class<*>> = ArrayList()
        for (aClass in figasmoPluginClasses) {
            if (aClass.isAnnotationPresent(LuaTypeDoc::class.java)) {
                classesToRegister.add(aClass)
            }
        }
        return classesToRegister
    }

    override fun getWhitelistedClasses(): MutableCollection<Class<*>> {
        val classesToRegister: MutableList<Class<*>> = ArrayList()
        for (aClass in figasmoPluginClasses) {
            if (aClass.isAnnotationPresent(LuaWhitelist::class.java))
                classesToRegister.add(aClass)
        }
        return classesToRegister
    }
}