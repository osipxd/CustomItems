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

import org.spongepowered.api.data.key.Keys
import org.spongepowered.api.item.inventory.ItemStack
import org.spongepowered.api.text.Text
import ru.endlesscode.customitems.data.ItemConfig
import ru.endlesscode.customitems.extension.colorized
import ru.endlesscode.customitems.model.Item
import ru.endlesscode.customitems.model.Texture

data class ItemImpl(
        override val name: Text,
        override val texture: Texture,
        override val unbreakable: Boolean,
        override val lore: List<Text>
) : Item {

    constructor(config: ItemConfig) : this(
            name = colorized(config.name),
            texture = TextureImpl.create(config.texture, config.meta),
            unbreakable = config.unbreakable,
            lore = config.lore.map(::colorized)
    )

    override fun createItemStack(): ItemStack {
        val itemStack = ItemStack.of(texture.type, 1)
        return itemStack.apply {
            if (texture.hasMeta()) offer(Keys.ITEM_DURABILITY, texture.meta)
            offer(Keys.UNBREAKABLE, unbreakable)
            offer(Keys.HIDE_MISCELLANEOUS, true)
            offer(Keys.HIDE_ATTRIBUTES, true)
            offer(Keys.HIDE_ENCHANTMENTS, true)
            offer(Keys.HIDE_UNBREAKABLE, true)
            offer(Keys.HIDE_CAN_DESTROY, true)
            offer(Keys.HIDE_CAN_PLACE, true)
            offer(Keys.DISPLAY_NAME, name)
            offer(Keys.ITEM_LORE, lore)
        }
    }
}