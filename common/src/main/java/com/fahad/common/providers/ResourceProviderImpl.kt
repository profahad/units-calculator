package com.fahad.common.providers

import android.content.Context
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import com.fahad.common.extensions.getStringFromName
import com.fahad.common.extensions.getStringFromNameLocalEng
import com.fahad.common_domain.providers.ResourceProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ResourceProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ResourceProvider {
    override fun getString(@StringRes id: Int, vararg args: Any) = context.getString(id, *args)
    override fun getStringByKey(key: String) = context.getStringFromName(key)
    override fun getStringByKeyEng(key: String) = context.getStringFromNameLocalEng(key)
    override fun getColor(id: Int) = context.resources.getColor(id, context.theme)
    override fun getQuantityString(@PluralsRes id: Int, quantity: Int) = context.resources.getQuantityString(id, quantity)
}
