/*
 * This file is part of CustomItems, licensed under the MIT License (MIT).
 *
 * Copyright (c) Osip Fatkullin <osip.fatkullin@gmail.com>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ru.endlesscode.customitems.entity

import org.spongepowered.api.Sponge
import org.spongepowered.api.item.ItemType
import ru.endlesscode.customitems.extension.getType
import ru.endlesscode.customitems.model.Texture

class TextureImpl(
        override val type: ItemType,
        override val meta: Int
) : Texture {

    companion object {
        fun create(type: String, meta: Int): TextureImpl {
            val itemType = Sponge.getRegistry().getType<ItemType>(type)
            return TextureImpl(itemType.get(), meta)
        }
    }

    override fun hasMeta(): Boolean = meta != -1

    override fun toString(): String = "$type:$meta"
}