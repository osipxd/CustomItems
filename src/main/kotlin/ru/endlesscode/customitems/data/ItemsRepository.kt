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

package ru.endlesscode.customitems.data

import ninja.leaping.configurate.commented.CommentedConfigurationNode
import ninja.leaping.configurate.hocon.HoconConfigurationLoader
import ninja.leaping.configurate.objectmapping.ObjectMapper
import org.spongepowered.api.plugin.PluginContainer
import ru.endlesscode.customitems.entity.ItemImpl
import ru.endlesscode.customitems.model.Item
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

class ItemsRepository(
        private val configPath: Path,
        private val plugin: PluginContainer
) {

    var items = mutableMapOf<String, Item>()
    
    private inline val log get() = plugin.logger

    private val configLoader by lazy { HoconConfigurationLoader.builder().setPath(configPath).build() }
    private val itemMapper by lazy { ObjectMapper.forClass(ItemConfig::class.java).bindToNew() }

    fun loadItems() {
        log.info("Loading items...")
        
        copyDefaultConfigIfNotExists()
        items.clear()

        val rootNode = loadConfig() ?: return
        val items = rootNode.childrenMap
                .mapKeys { (key, _) -> key.toString() }
                .mapValues { (_, node) -> itemFromNode(node) }

        items.forEach { id, item ->
            item?.let { this.items.put(id, it) }
        }

        log.info("${this.items.size} item(s) loaded.")
    }

    private fun copyDefaultConfigIfNotExists() {
        if (!Files.notExists(configPath)) return

        log.info("Copying default config...")
        plugin.getAsset("default.conf").get().copyToFile(configPath)
    }

    private fun loadConfig(): CommentedConfigurationNode? {
        return try {
            configLoader.load()
        } catch (e: IOException) {
            log.error("Can't load default configuration: ${e.message}")
            log.error("See logs for more information...")
            log.debug("Exception on loading default items configuration", e)
            null
        }
    }

    private fun itemFromNode(node: CommentedConfigurationNode): Item? {
        return try {
            ItemImpl(itemMapper.populate(node))
        } catch (e: Exception) {
            node.setComment("Not loaded. Reason: ${e.message}")
            log.warn("Item '${node.key}' not loaded: ${e.message}")
            log.debug("Exception on loading item '${node.key}'", e)
            null
        }
    }
}