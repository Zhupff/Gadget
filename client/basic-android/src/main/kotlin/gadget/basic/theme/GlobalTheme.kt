package gadget.basic.theme

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.color.utilities.Hct
import com.google.android.material.color.utilities.SchemeTonalSpot
import gadget.App
import gadget.basic.exception.throws
import gadget.basic.tool.mutable

object GlobalTheme {

    val current: LiveData<ThemeScheme> = MutableLiveData()

    init {
        val light = Light
        val night = Night
        light.night = night
        night.light = light

        App.configuration.observeForever { configuration ->
            switch(Light, configuration.isNightModeActive)
        }
    }

    fun update(seed: Int) {
        val oldScheme = current.value
        val newScheme = if (oldScheme is Light) {
            oldScheme.create(seed, oldScheme.night)
        } else if (oldScheme is Night) {
            oldScheme.create(seed, oldScheme.light)
        } else {
            Light.create(seed)
        }
        switch(newScheme)
    }

    private fun switch(
        scheme: ThemeScheme,
        isNightMode: Boolean = App.instance.resources.configuration.isNightModeActive,
    ) {
        val oldScheme = current.value
        val newScheme = if (isNightMode) {
            if (scheme is Light) {
                scheme.night
            } else scheme
        } else {
            if (scheme is Night) {
                scheme.light
            } else scheme
        }
        if (oldScheme == null) {
            current.mutable().value = newScheme
        } else if (newScheme != oldScheme) {
            current.mutable().postValue(newScheme)
        }
    }


    internal open class Light : ThemeScheme {
        companion object : Light()
        override val primaryColor   : Int = 0xFF445E91.toInt()
        override val onPrimaryColor : Int = 0xFFFFFFFF.toInt()
        override val backgroundColor: Int = 0xFFFFFFFF.toInt()
        override val foregroundColor: Int = 0xFF1A1B20.toInt()
        override val surfaceColor   : Int = 0xFFEDEDF4.toInt()
        override val outlineColor   : Int = 0xFF74777F.toInt()
        override val errorColor     : Int = 0xFFBA1A1A.toInt()
        override val onErrorColor   : Int = 0xFFFFFFFF.toInt()
        lateinit var night: Night

        @SuppressLint("RestrictedApi")
        fun create(seed: Int, night: Night? = null): Light {
            if (seed == 0) {
                if (App.debuggable) {
                    IllegalArgumentException("seed should not be 0!").throws()
                } else {
                    return Light
                }
            }
            val scheme = SchemeTonalSpot(Hct.fromInt(seed), false, 0.0)
            val light = object : Light() {
                override val primaryColor   : Int = scheme.primary
                override val onPrimaryColor : Int = scheme.onPrimary
                override val backgroundColor: Int = scheme.surfaceContainerLowest
                override val foregroundColor: Int = scheme.onSurface
                override val surfaceColor   : Int = scheme.surfaceContainer
                override val outlineColor   : Int = scheme.outline
                override val errorColor     : Int = scheme.error
                override val onErrorColor   : Int = scheme.onError
            }
            light.night = night ?: this.night.create(seed, light)
            return light
        }

        override fun equals(other: Any?): Boolean = other is Light && other.primaryColor == this.primaryColor

        override fun hashCode(): Int = primaryColor
    }

    internal open class Night : ThemeScheme {
        companion object : Night()

        override val primaryColor   : Int = 0xFFADC6FF.toInt()
        override val onPrimaryColor : Int = 0xFF102F60.toInt()
        override val backgroundColor: Int = 0xFF0C0E13.toInt()
        override val foregroundColor: Int = 0xFFE2E2E9.toInt()
        override val surfaceColor   : Int = 0xFF1E1F25.toInt()
        override val outlineColor   : Int = 0xFF8E9099.toInt()
        override val errorColor     : Int = 0xFFFFB4AB.toInt()
        override val onErrorColor   : Int = 0xFF690005.toInt()
        lateinit var light: Light

        @SuppressLint("RestrictedApi")
        fun create(seed: Int, light: Light? = null): Night {
            if (seed == 0) {
                if (App.debuggable) {
                    IllegalArgumentException("seed should not be 0!").throws()
                } else {
                    return Night
                }
            }
            val scheme = SchemeTonalSpot(Hct.fromInt(seed), true, 0.0)
            val night = object : Night() {
                override val primaryColor   : Int = scheme.primary
                override val onPrimaryColor : Int = scheme.onPrimary
                override val backgroundColor: Int = scheme.surfaceContainerLowest
                override val foregroundColor: Int = scheme.onSurface
                override val surfaceColor   : Int = scheme.surfaceContainer
                override val outlineColor   : Int = scheme.outline
                override val errorColor     : Int = scheme.error
                override val onErrorColor   : Int = scheme.onError
            }
            night.light = light ?: this.light.create(seed, night)
            return night
        }

        override fun equals(other: Any?): Boolean = other is Night && other.primaryColor == this.primaryColor

        override fun hashCode(): Int = primaryColor
    }
}