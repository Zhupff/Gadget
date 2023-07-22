package zhupf.gadget.palette

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.roundToLong
import kotlin.math.sign
import kotlin.math.sin
import kotlin.math.sqrt

class Palette (
    val origin: Int,
    val primary: Int, val onPrimary: Int, val primaryContainer: Int, val onPrimaryContainer: Int,
    val secondary: Int, val onSecondary: Int, val secondaryContainer: Int, val onSecondaryContainer: Int,
    val tertiary: Int, val onTertiary: Int, val tertiaryContainer: Int, val onTertiaryContainer: Int,
    val error: Int, val onError: Int, val errorContainer: Int, val onErrorContainer: Int,
    val background: Int, val onBackground: Int, val surface: Int, val onSurface: Int,
    val surfaceVariant: Int, val onSurfaceVariant: Int, val outline: Int, val outlineVariant: Int,
    val shadow: Int, val scrim: Int, val inverseSurface: Int, val inverseOnSurface: Int,
    val inversePrimary: Int, val inverseSecondary: Int, val inverseTertiary: Int,
    val isLight: Boolean, val isDark: Boolean,
) {

    companion object {
        fun quantize(pixels: IntArray, limit: Int): List<Int> = Quantizer(pixels, limit).quantizedColors
        fun light(color: Int): Palette = Generator(color).generate(true)
        fun dark(color: Int): Palette = Generator(color).generate(false)
    }

    private class Quantizer constructor(pixels: IntArray, limit: Int) {

        private val histograms = IntArray(32768)
        private val colors: IntArray
        val quantizedColors: List<Int>

        init {
            for (i in pixels.indices) {
                histograms[pixels[i].from888To555]++
            }
            var distinctColorCount = 0
            for (color in histograms.indices) {
                if (histograms[color] > 0) {
                    if (filterHSL(color.from555To888)) {
                        histograms[color] = 0
                    } else {
                        distinctColorCount++
                    }
                }
            }
            colors = IntArray(distinctColorCount)
            var distinctColorIndex = 0
            for (color in histograms.indices) {
                if (histograms[color] > 0) {
                    colors[distinctColorIndex++] = color
                }
            }
            if (distinctColorCount <= limit) {
                quantizedColors = colors.map { it.from555To888 }
            } else {
                val boxes = ArrayList<Box>(limit)
                boxes.add(Box().fit(0, colors.lastIndex))
                while (boxes.size < limit) {
                    val box = boxes.removeFirst()
                    if (box.upperIndex > box.lowerIndex) {
                        boxes.add(box.split())
                        boxes.add(box)
                        boxes.sort()
                    } else {
                        break
                    }
                }
                quantizedColors = boxes.map { it.average() }.filter { !filterHSL(it) }
            }
        }

        private fun filterHSL(color: Int): Boolean {
            val rD = color.rIn888 / 255.0; val gD = color.gIn888 / 255.0; val bD = color.bIn888 / 255.0
            val max = max(rD, gD, bD)
            val min = min(rD, gD, bD)
            val diff = max - min
            var h = 0.0; var s = 0.0; var l = (max + min) / 2.0
            if (max != min) {
                h = if (max == rD)
                    ((gD - bD) / diff) % 6.0
                else if (max == gD)
                    ((bD - rD) / diff) + 2.0
                else
                    ((rD - gD) / diff) + 4.0
                s = diff / (1.0 - abs(max + min - 1.0))
            }
            h = (h * 60.0) % 360.0
            if (h < 0.0) h += 360.0
            h = mid(h, 0.0, 360.0); s = mid(s, 0.0, 1.0); l = mid(l, 0.0, 1.0)
            return !(l < 0.95 && l > 0.05 && !(h in 10.0..37.0 && s <= 0.82))
        }

        private inner class Box : Comparable<Box> {

            var lowerIndex: Int = 0; private set
            var upperIndex: Int = 0; private set
            private var population: Int = 0
            private var minR: Int = 0; private var minG: Int = 0; private var minB: Int = 0
            private var maxR: Int = 0; private var maxG: Int = 0; private var maxB: Int = 0
            private val volume: Int; get() = (maxR - minR + 1) * (maxG - minG + 1) * (maxB - minB + 1)

            fun fit(lowerIndex: Int, upperIndex: Int) = apply {
                this.lowerIndex = lowerIndex
                this.upperIndex = upperIndex
                population = 0
                minR = Int.MAX_VALUE; minG = Int.MAX_VALUE; minB = Int.MAX_VALUE
                maxR = Int.MIN_VALUE; maxG = Int.MIN_VALUE; maxB = Int.MIN_VALUE
                for (i in lowerIndex..upperIndex) {
                    val color = colors[i]
                    population += histograms[color]
                    val r = color.rIn555
                    val g = color.gIn555
                    val b = color.bIn555
                    if (r < minR) minR = r
                    if (g < minG) minG = g
                    if (b < minB) minB = b
                    if (r > maxR) maxR = r
                    if (g > maxG) maxG = g
                    if (b > maxB) maxB = b
                }
            }

            fun split(): Box {
                adjust555Octet()
                colors.sort(lowerIndex, upperIndex + 1)
                adjust555Octet()
                var splitPoint = lowerIndex
                val mid = population / 2
                var count = 0
                for (i in lowerIndex..upperIndex) {
                    count += histograms[colors[i]]
                    if (count >= mid) {
                        splitPoint = min(upperIndex - 1, i)
                        break
                    }
                }
                val newBox = Box().fit(splitPoint + 1, upperIndex)
                fit(lowerIndex, splitPoint)
                return newBox
            }

            fun average(): Int {
                var rSum = 0; var gSum = 0; var bSum = 0; var total = 0
                for (i in lowerIndex..upperIndex) {
                    val color = colors[i]
                    val histogram = histograms[color]
                    total += histogram
                    rSum += histogram * color.rIn555
                    gSum += histogram * color.gIn555
                    bSum += histogram * color.bIn555
                }
                val r = (rSum / total.toDouble()).roundToInt()
                val g = (gSum / total.toDouble()).roundToInt()
                val b = (bSum / total.toDouble()).roundToInt()
                return argb8888(255, (r shl 3) and 255, (g shl 3) and 255, (b shl 3) and 255)
            }

            private fun adjust555Octet() {
                val rLength = maxR - minR; val gLength = maxG - minG; val bLength = maxB - minB
                if (rLength >= gLength && rLength >= bLength) { // do nothing
                } else if (gLength >= rLength && gLength >= bLength) { // RGB <-> GRB
                    for (i in lowerIndex..upperIndex) {
                        val color = colors[i]
                        colors[i] = (color.gIn555 shl 10) or (color.rIn555 shl 5) or color.bIn555
                    }
                } else { // RGB <-> BGR
                    for (i in lowerIndex..upperIndex) {
                        val color = colors[i]
                        colors[i] = (color.bIn555 shl 10) or (color.gIn555 shl 5) or color.rIn555
                    }
                }
            }

            override fun compareTo(other: Box): Int = other.volume - this.volume
        }
    }

    private class Generator constructor(private val argb: Int) {

        private val origin    = fixHctIfDisliked(argbToHct(argb))
        private val primary   = origin.hue        to max(origin.chroma, 48.0)
        private val secondary = origin.hue        to 16.0
        private val tertiary  = origin.hue + 60.0 to 24.0
        private val error     = 25.0              to 84.0
        private val neutral   = origin.hue        to 4.0
        private val variant   = origin.hue        to 8.0

        private fun argbToHct(argb: Int): HCT {
            val rL = argb.rIn888.toDouble().linearized
            val gL = argb.gIn888.toDouble().linearized
            val bL = argb.bIn888.toDouble().linearized
            val x = 0.41233895 * rL + 0.35762064 * gL + 0.18051042 * bL
            val y = 0.2126     * rL + 0.7152     * gL + 0.0722     * bL
            val z = 0.01932141 * rL + 0.11916382 * gL + 0.95034478 * bL
            val rD =  0.40978635798415963   * x + 0.6639421705349651   * y - 0.05255082576160473  * z
            val gD = -0.24684126282848978   * x + 1.1879228376313102   * y + 0.045226154625192076 * z
            val bD = -0.0019417038967145981 * x + 0.045719234801333816 * y + 0.8901829773756107   * z
            val rAF = (0.3884814537800353 * abs(rD) / 100.0).pow(0.42)
            val gAF = (0.3884814537800353 * abs(gD) / 100.0).pow(0.42)
            val bAF = (0.3884814537800353 * abs(bD) / 100.0).pow(0.42)
            val rA = sign(rD) * 400.0 * rAF / (rAF + 27.13)
            val gA = sign(gD) * 400.0 * gAF / (gAF + 27.13)
            val bA = sign(bD) * 400.0 * bAF / (bAF + 27.13)
            val a = (11.0 * rA - 12.0 * gA + bA) / 11.0
            val b = (rA + gA - 2.0 * bA) / 9.0
            val degrees = 57.29577951308232 * atan2(b, a)
            val hue = if (degrees < 0) degrees + 360.0 else if  (degrees >= 360)  degrees - 360.0 else degrees
            val huePrime = if (hue < 20.14) hue + 360 else hue
            val p = 977.8069042748804 * (cos(0.017453292519943295 * huePrime + 2.0) + 3.8)
            val alpha = 0.8834525670408592 * (p * hypot(a, b) / (rA + gA + 1.05 * bA + 0.305)).pow(0.9)
            val chroma = alpha * sqrt((0.06783758217583338 * rA + 0.03391879108791669 * gA + 0.0016959395543958346 * bA).pow(1.3173270022537198))
            val tone = 116.0 * (0.01 * y).toLAB - 16.0
            return HCT(hue, chroma, tone)
        }

        private fun hctToArgb(hue: Double, chroma: Double, tone: Double): Int {
            if (chroma < 0.0001 || tone < 0.0001 || tone > 99.9999) {
                return (100.0 * ((tone + 16.0) / 116.0).fromLAB).delinearized.roundToInt().let {
                    argb8888(255, it, it, it)
                }
            }
            val hueRadians = hue / 180.0 * PI
            val y = 100.0 * ((tone + 16.0) / 116.0).fromLAB
            if (y != 0.0) {
                var j = 11.0 * sqrt(y)
                val p1 = 977.8069042748803 * (cos(hueRadians + 2.0) + 3.8)
                val hSin = sin(hueRadians); val hCos = cos(hueRadians)
                for (iterationRound in 0 until 5) {
                    if (j == 0.0) break
                    val p2 = 29.48218282332127 * (0.01 * j).pow(0.759112960023724)
                    val gamma = (p2 + 0.305) / (0.4782608695652174 * hCos + 4.695652173913044 * hSin + p1 / (chroma / sqrt(0.01 * j) * 1.1319226830134397).pow(1.1111111111111112))
                    val a = gamma * hCos; val b = gamma * hSin
                    val rCScaled = ((460.0 * p2 + 451.0 * a + 288.0  * b) / 1403.0).inverseChromaticAdaptation
                    val gCScaled = ((460.0 * p2 - 891.0 * a - 261.0  * b) / 1403.0).inverseChromaticAdaptation
                    val bCScaled = ((460.0 * p2 - 220.0 * a - 6300.0 * b) / 1403.0).inverseChromaticAdaptation
                    val rL =  1373.2198709594231 * rCScaled - 1100.4251190754821 * gCScaled - 7.278681089101213 * bCScaled
                    val gL = -271.815969077903   * rCScaled + 559.6580465940733  * gCScaled - 32.46047482791194 * bCScaled
                    val bL =  1.9622899599665666 * rCScaled - 57.173814538844006 * gCScaled + 308.7233197812385 * bCScaled
                    if (rL < 0.0 || gL < 0.0 || bL < 0.0) break
                    val fnj = 0.2126 * rL + 0.7152 * gL + 0.0722 * bL
                    if (fnj <= 0.0) break
                    if (iterationRound == 4 || abs(fnj - y) < 0.002) {
                        if (rL > 100.01 || gL > 100.01 || bL > 100.01) break
                        val argb = argb8888(255, rL.delinearized.roundToInt(), gL.delinearized.roundToInt(), bL.delinearized.roundToInt())
                        if (argb != 0) return argb
                    }
                    j -= (fnj - y) * j / (2.0 * fnj)
                }
            }
            var left = doubleArrayOf(-1.0, -1.0, -1.0)
            var right = left
            var leftHue = 0.0; var rightHue = 0.0
            var initialized = false
            var uncut = true
            for (n in 0 until 12) {
                val mid = doubleArrayOf(-1.0, -1.0, -1.0)
                val coordA = if (n % 4 <= 1) 0.0 else 100.0
                val coordB = if (n % 2 == 0) 0.0 else 100.0
                if (n < 4) {
                    val r = (y - 0.7152 * coordA - 0.0722 * coordB) / 0.2126
                    if (r in 0.0..100.0) {
                        mid[0] = r; mid[1] = coordA; mid[2] = coordB
                    }
                } else if (n < 8) {
                    val g = (y - 0.0722 * coordA - 0.2126 * coordB) / 0.7152
                    if (g in 0.0..100.0) {
                        mid[0] = coordB; mid[1] = g; mid[2] = coordA
                    }
                } else {
                    val b = (y - 0.2126 * coordA - 0.7152 * coordB) / 0.0722
                    if (b in 0.0..100.0) {
                        mid[0] = coordA; mid[1] = coordB; mid[2] = b
                    }
                }
                if (mid[0] < 0.0) continue
                val midHue = mid.hue
                if (!initialized) {
                    left = mid; right = mid; leftHue = midHue; rightHue = midHue; initialized = true
                    continue
                }
                if (uncut || (midHue - leftHue).sanitizeRadians < (rightHue - leftHue).sanitizeRadians) {
                    uncut = false
                    if ((hueRadians - leftHue).sanitizeRadians < (midHue - leftHue).sanitizeRadians) {
                        right = mid; rightHue = midHue
                    } else {
                        left = mid; leftHue = midHue
                    }
                }
            }
            for (axis in 0 until 3) {
                if (left[axis] != right[axis]) {
                    var lPlane: Int; var rPlane: Int
                    if (left[axis] < right[axis]) {
                        lPlane = floor(left[axis].delinearized - 0.5).toInt()
                        rPlane = ceil(right[axis].delinearized - 0.5).toInt()
                    } else {
                        lPlane = ceil(left[axis].delinearized - 0.5).toInt()
                        rPlane = floor(right[axis].delinearized - 0.5).toInt()
                    }
                    for (i in 0 until 8) {
                        if (abs(rPlane - lPlane) <= 1) break
                        val mPlane = floor((lPlane + rPlane) / 2.0).toInt()
                        val intercept = (CRITICAL_PLANES[mPlane] - left[axis]) / (right[axis] - left[axis])
                        val mid = doubleArrayOf(
                            left[0] + (right[0] - left[0]) * intercept,
                            left[1] + (right[1] - left[1]) * intercept,
                            left[2] + (right[2] - left[2]) * intercept,
                        )
                        val midHue = mid.hue
                        if ((hueRadians - leftHue).sanitizeRadians < (midHue - leftHue).sanitizeRadians) {
                            right = mid; rPlane = mPlane
                        } else {
                            left = mid; leftHue = midHue; lPlane = mPlane
                        }
                    }
                }
            }
            val r = ((left[0] + right[0]) / 2.0).delinearized.roundToInt()
            val g = ((left[1] + right[1]) / 2.0).delinearized.roundToInt()
            val b = ((left[2] + right[2]) / 2.0).delinearized.roundToInt()
            return argb8888(255, r, g, b)
        }

        private fun fixHctIfDisliked(hct: HCT): HCT {
            if (hct.hue.roundToLong() in 90L..111L && hct.chroma.roundToLong() > 16L && hct.tone.roundToLong() < 65L) {
                return argbToHct(hctToArgb(hct.hue, hct.chroma, 70.0))
            }
            return hct
        }

        fun generate(isLight: Boolean): Palette = Palette(
            this.argb,
            hctToArgb(primary.first  , primary.second  , if (isLight) 40.0  else 80.0),
            hctToArgb(primary.first  , primary.second  , if (isLight) 100.0 else 20.0),
            hctToArgb(primary.first  , primary.second  , if (isLight) 90.0  else 30.0),
            hctToArgb(primary.first  , primary.second  , if (isLight) 10.0  else 90.0),
            hctToArgb(secondary.first, secondary.second, if (isLight) 40.0  else 80.0),
            hctToArgb(secondary.first, secondary.second, if (isLight) 100.0 else 20.0),
            hctToArgb(secondary.first, secondary.second, if (isLight) 90.0  else 30.0),
            hctToArgb(secondary.first, secondary.second, if (isLight) 10.0  else 90.0),
            hctToArgb(tertiary.first , tertiary.second , if (isLight) 40.0  else 80.0),
            hctToArgb(tertiary.first , tertiary.second , if (isLight) 100.0 else 20.0),
            hctToArgb(tertiary.first , tertiary.second , if (isLight) 90.0  else 30.0),
            hctToArgb(tertiary.first , tertiary.second , if (isLight) 10.0  else 90.0),
            hctToArgb(error.first    , error.second    , if (isLight) 40.0  else 80.0),
            hctToArgb(error.first    , error.second    , if (isLight) 100.0 else 20.0),
            hctToArgb(error.first    , error.second    , if (isLight) 90.0  else 30.0),
            hctToArgb(error.first    , error.second    , if (isLight) 10.0  else 80.0),
            hctToArgb(neutral.first  , neutral.second  , if (isLight) 99.0  else 10.0),
            hctToArgb(neutral.first  , neutral.second  , if (isLight) 10.0  else 90.0),
            hctToArgb(neutral.first  , neutral.second  , if (isLight) 99.0  else 10.0),
            hctToArgb(neutral.first  , neutral.second  , if (isLight) 10.0  else 90.0),
            hctToArgb(variant.first  , variant.second  , if (isLight) 90.0  else 30.0),
            hctToArgb(variant.first  , variant.second  , if (isLight) 30.0  else 80.0),
            hctToArgb(variant.first  , variant.second  , if (isLight) 50.0  else 60.0),
            hctToArgb(variant.first  , variant.second  , if (isLight) 80.0  else 30.0),
            hctToArgb(neutral.first  , neutral.second  , if (isLight) 0.0   else 0.0 ),
            hctToArgb(neutral.first  , neutral.second  , if (isLight) 0.0   else 0.0 ),
            hctToArgb(neutral.first  , neutral.second  , if (isLight) 20.0  else 90.0),
            hctToArgb(neutral.first  , neutral.second  , if (isLight) 95.0  else 20.0),
            hctToArgb(primary.first  , primary.second  , if (isLight) 80.0  else 40.0),
            hctToArgb(secondary.first, secondary.second, if (isLight) 80.0  else 40.0),
            hctToArgb(tertiary.first , tertiary.second , if (isLight) 80.0  else 40.0),
            isLight = true, isDark = false,
        )

        private class HCT(val hue: Double, val chroma: Double, val tone: Double)
    }
}

private val Int.rIn555: Int; get() = (this shr 10) and 31
private val Int.gIn555: Int; get() = (this shr  5) and 31
private val Int.bIn555: Int; get() = (this       ) and 31

private val Int.rIn888: Int; get() = (this shr 16) and 255
private val Int.gIn888: Int; get() = (this shr  8) and 255
private val Int.bIn888: Int; get() = (this       ) and 255

private val Int.from555To888: Int; get() = argb8888(255, (rIn555 shl 3) and 255, (gIn555 shl 3) and 255, (bIn555 shl 3) and 255)
private val Int.from888To555: Int; get() = rgb555((rIn888 shr 3) and 31, (gIn888 shr 3) and 31, (bIn888 shr 3) and 31)

private fun rgb555(r: Int, g: Int, b: Int) = (r shl 10) or (g shl 5) or b

private fun argb8888(a: Int, r: Int, g: Int, b: Int): Int = (a shl 24) or (r shl 16) or (g shl 8) or b

private fun max(a: Double, b: Double, c: Double): Double = if (a > b) if (a > c) a else c else if (b > c) b else c

private fun mid(a: Double, b: Double, c: Double): Double = if (a > b) if (c > a) a else if (c > b) c else b else if (c > b) b else if (c > a) c else a

private fun min(a: Double, b: Double, c: Double): Double = if (a < b) if (a < c) a else c else if (b < c) b else c

private val Double.linearized: Double get() {
    val normalized = this / 255.0
    return 100.0 * if (normalized <= 0.040449936)
        normalized / 12.92
    else
        ((normalized + 0.055) / 1.055).pow(2.4)
}

private val Double.delinearized: Double get() {
    val normalized = this / 100.0
    return 255.0 * if (normalized <= 0.0031308)
        normalized * 12.92
    else
        normalized.pow(1.0 / 2.4) * 1.055 - 0.055
}

private val Double.chromaticAdaptation: Double; get() {
    val af = abs(this).pow(0.42)
    return sign(this) * 400.0 * af / (af + 27.13)
}

private val Double.inverseChromaticAdaptation: Double; get() {
    val adaptedAbs = abs(this)
    val base = max(0.0, 27.13 * adaptedAbs / (400.0 - adaptedAbs))
    return sign(this) * base.pow(2.380952380952381)
}

private val Double.toLAB: Double; get() {
    return if (this > 0.008856451679035631) this.pow(0.3333333333333333) else (903.2962962962963 * this + 16.0) / 116.0
}

private val Double.fromLAB: Double; get() {
    return this.pow(3).let { if (it > 0.008856451679035631) it else (116 * this - 16) / 903.2962962962963 }
}

private val DoubleArray.hue: Double; get() {
    val rA = (this[0] * 0.001200833568784504   + this[1] * 0.002389694492170889  + this[2] * 0.0002795742885861124).chromaticAdaptation
    val gA = (this[0] * 0.0005891086651375999  + this[1] * 0.0029785502573438758 + this[2] * 0.0003270666104008398).chromaticAdaptation
    val bA = (this[0] * 0.00010146692491640572 + this[1] * 0.0005364214359186694 + this[2] * 0.0032979401770712076).chromaticAdaptation
    return atan2((rA + gA - 2.0 * bA) / 9.0, (11.0 * rA - 12.0 * gA + bA) / 11.0)
}

private val Double.sanitizeRadians: Double; get() = (this + PI * 8) % (PI * 2)

private val CRITICAL_PLANES = doubleArrayOf(
    0.015176349177441876, 0.045529047532325624, 0.07588174588720938, 0.10623444424209313, 0.13658714259697685,
    0.16693984095186062 , 0.19729253930674434 , 0.2276452376616281 , 0.2579979360165119 , 0.28835063437139563,
    0.3188300904430532  , 0.350925934958123   , 0.3848314933096426 , 0.42057480301049466, 0.458183274052838  ,
    0.4976837250274023  , 0.5391024159806381  , 0.5824650784040898 , 0.6277969426914107 , 0.6751227633498623 ,
    0.7244668422128921  , 0.775853049866786   , 0.829304845476233  , 0.8848452951698498 , 0.942497089126609  ,
    1.0022825574869039  , 1.0642236851973577  , 1.1283421258858297 , 1.1946592148522128 , 1.2631959812511864 ,
    1.3339731595349034  , 1.407011200216447   , 1.4823302800086415 , 1.5599503113873272 , 1.6398909516233677 ,
    1.7221716113234105  , 1.8068114625156377  , 1.8938294463134073 , 1.9832442801866852 , 2.075074464868551  ,
    2.1693382909216234  , 2.2660538449872063  , 2.36523901573795   , 2.4669114995532007 , 2.5710888059345764 ,
    2.6777882626779785  , 2.7870270208169257  , 2.898822059350997  , 3.0131901897720907 , 3.1301480604002863 ,
    3.2497121605402226  , 3.3718988244681087  , 3.4967242352587946 , 3.624204428461639  , 3.754355295633311  ,
    3.887192587735158   , 4.022731918402185   , 4.160988767090289  , 4.301978482107941  , 4.445716283538092  ,
    4.592217266055746   , 4.741496401646282   , 4.893568542229298  , 5.048448422192488  , 5.20615066083972   ,
    5.3666897647573375  , 5.5300801301023865  , 5.696336044816294  , 5.865471690767354  , 6.037501145825082  ,
    6.212438385869475   , 6.390297286737924   , 6.571091626112461  , 6.7548350853498045 , 6.941541251256611  ,
    7.131223617812143   , 7.323895587840543   , 7.5195704746346665 , 7.7182615035334345 , 7.919981813454504  ,
    8.124744458384042   , 8.332562408825165   , 8.543448553206703  , 8.757415699253682  , 8.974476575321063  ,
    9.194643831691977   , 9.417930041841839   , 9.644347703669503  , 9.873909240696694  , 10.106627003236781 ,
    10.342513269534024  , 10.58158024687427   , 10.8238400726681   , 11.069304815507364 , 11.317986476196008 ,
    11.569896988756009  , 11.825048221409341  , 12.083451977536606 , 12.345119996613247 , 12.610063955123938 ,
    12.878295467455942  , 13.149826086772048  , 13.42466730586372  , 13.702830557985108 , 13.984327217668513 ,
    14.269168601521828  , 14.55736596900856   , 14.848930523210871 , 15.143873411576273 , 15.44220572664832  ,
    15.743938506781891  , 16.04908273684337   , 16.35764934889634  , 16.66964922287304  , 16.985093187232053 ,
    17.30399201960269   , 17.62635644741625   , 17.95219714852476  , 18.281524751807332 , 18.614349837764564 ,
    18.95068293910138   , 19.290534541298456  , 19.633915083172692 , 19.98083495742689  , 20.331304511189067 ,
    20.685334046541502  , 21.042933821039977  , 21.404114048223256 , 21.76888489811322  , 22.137256497705877 ,
    22.50923893145328   , 22.884842241736916  , 23.264076429332462 , 23.6469514538663   , 24.033477234264016 ,
    24.42366364919083   , 24.817520537484558  , 25.21505769858089  , 25.61628489293138  , 26.021211842414342 ,
    26.429848230738664  , 26.842203703840827  , 27.258287870275353 , 27.678110301598522 , 28.10168053274597  ,
    28.529008062403893  , 28.96010235337422   , 29.39497283293396  , 29.83362889318845  , 30.276079891419332 ,
    30.722335150426627  , 31.172403958865512  , 31.62629557157785  , 32.08401920991837  , 32.54558406207592  ,
    33.010999283389665  , 33.4802739966603    , 33.953417292456834 , 34.430438229418264 , 34.911345834551085 ,
    35.39614910352207   , 35.88485700094671   , 36.37747846067349  , 36.87402238606382  , 37.37449765026789  ,
    37.87891309649659   , 38.38727753828926   , 38.89959975977785  , 39.41588851594697  , 39.93615253289054  ,
    40.460400508064545  , 40.98864111053629   , 41.520882981230194 , 42.05713473317016  , 42.597404951718396 ,
    43.141702194811224  , 43.6900349931913    , 44.24241185063697  , 44.798841244188324 , 45.35933162437017  ,
    45.92389141541209   , 46.49252901546552   , 47.065252796817916 , 47.64207110610409  , 48.22299226451468  ,
    48.808024568002054  , 49.3971762874833    , 49.9904556690408   , 50.587870934119984 , 51.189430279724725 ,
    51.79514187861014   , 52.40501387947288   , 53.0190544071392   , 53.637271562750364 , 54.259673423945976 ,
    54.88626804504493   , 55.517063457223934  , 56.15206766869424  , 56.79128866487574  , 57.43473440856916  ,
    58.08241284012621   , 58.734331877617365  , 59.39049941699807  , 60.05092333227251  , 60.715611475655585 ,
    61.38457167773311   , 62.057811747619894  , 62.7353394731159   , 63.417162620860914 , 64.10328893648692  ,
    64.79372614476921   , 65.48848194977529   , 66.18756403501224  , 66.89098006357258  , 67.59873767827808  ,
    68.31084450182222   , 69.02730813691093   , 69.74813616640164  , 70.47333615344107  , 71.20291564160104  ,
    71.93688215501312   , 72.67524319850172   , 73.41800625771542  , 74.16517879925733  , 74.9167682708136   ,
    75.67278210128072   , 76.43322770089146   , 77.1981124613393   , 77.96744375590167  , 78.74122893956174  ,
    79.51947534912904   , 80.30219030335869   , 81.08938110306934  , 81.88105503125999  , 82.67721935322541  ,
    83.4778813166706    , 84.28304815182372   , 85.09272707154808  , 85.90692527145302  , 86.72564993000343  ,
    87.54890820862819   , 88.3767072518277    , 89.2090541872801   , 90.04595612594655  , 90.88742016217518  ,
    91.73345337380438   , 92.58406282226491   , 93.43925555268066  , 94.29903859396902  , 95.16341895893969  ,
    96.03240364439274   , 96.9059996312159    , 97.78421388448044  , 98.6670533535366   , 99.55452497210776  ,
)