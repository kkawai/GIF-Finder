package com.kk.android.fuzzy_waddle.model

import android.text.TextUtils
import kotlinx.serialization.Serializable

@Serializable
data class GiphyImage(val images: ImageTypes) {

    var tempKey : Int = 0

    fun setTempKey(newKey : Int) : Int {
        tempKey = newKey
        return tempKey
    }

    fun getGifAspectRatio(): Float {
        return images.downsized.width.toFloat() / images.downsized.height.toFloat()
    }

    fun gifUrl() : String? {
        val data = getGifData()
        return data?.url
    }
    fun gifMmsUrl() : String? {
        val data = getGifMmsData()
        return data?.url
    }
    fun stillUrl() : String? {
        val data = getStillData()
        return data?.url
    }

    private fun getStillData(): ImageData? {
        return getFirstNonEmpty(
            images.downsized_still,
            images.fixed_height_still,
            images.fixed_width_still
        )
    }

    private fun getGifMmsData(): ImageData? {
        return getFirstNonEmpty(
            images.fixed_height_downsampled,
            images.fixed_width_downsampled
        )
    }

    fun getGifData(): ImageData? {
        return getFirstNonEmpty(
            images.downsized,
            images.downsized_medium,
            images.fixed_height,
            images.fixed_width
        )
    }

    private fun getFirstNonEmpty(vararg data: ImageData): ImageData? {
        for (image in data) {
            if (!TextUtils.isEmpty(image.url)) {
                return image
            }
        }
        return null
    }
}

@Serializable
data class ImageTypes(
    val fixed_height: ImageData = ImageData(),
    val fixed_height_still: ImageData = ImageData(),
    val fixed_height_downsampled: ImageData = ImageData(),
    val fixed_width: ImageData = ImageData(),
    val fixed_width_still: ImageData = ImageData(),
    val fixed_width_downsampled: ImageData = ImageData(),
    val fixed_width_small: ImageData = ImageData(),
    val downsized_medium: ImageData = ImageData(),
    val downsized: ImageData = ImageData(),
    val downsized_still: ImageData = ImageData()
)

@Serializable
data class ImageData(
    val url: String = "",
    val width: Int = 0,
    val height: Int = 0,
    val size: Int = 0,
    val mp4: String = "",
    val webp: String = ""
)