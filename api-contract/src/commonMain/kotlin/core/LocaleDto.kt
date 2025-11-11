package io.github.alelk.pws.api.contract.core

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
class LocaleDto(val value: String)