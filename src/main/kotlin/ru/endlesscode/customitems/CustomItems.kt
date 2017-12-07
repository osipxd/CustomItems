/*
 * This file is part of customitems, licensed under the MIT License (MIT).
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

package ru.endlesscode.customitems

import org.spongepowered.api.Sponge
import org.spongepowered.api.command.CommandResult
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.command.args.CommandContext
import org.spongepowered.api.command.args.GenericArguments
import org.spongepowered.api.command.spec.CommandSpec
import org.spongepowered.api.config.DefaultConfig
import org.spongepowered.api.entity.living.player.Player
import org.spongepowered.api.event.Listener
import org.spongepowered.api.event.game.state.GameInitializationEvent
import org.spongepowered.api.plugin.Plugin
import org.spongepowered.api.plugin.PluginContainer
import org.spongepowered.api.text.Text
import ru.endlesscode.customitems.data.ItemsRepository
import ru.endlesscode.customitems.model.Item
import java.nio.file.Path
import javax.inject.Inject

@Plugin(id = "customitems")
class CustomItems {

    companion object {
        private const val ARG_PLAYER = "player"
        private const val ARG_ITEM = "item"
    }

    @Inject
    lateinit var plugin: PluginContainer

    @Inject @DefaultConfig(sharedRoot = true)
    lateinit var config: Path

    private val repository by lazy { ItemsRepository(config, plugin) }

    @Listener
    fun onInit(event: GameInitializationEvent) {
        this.initPlugin()
    }

    private fun initPlugin() {
        repository.loadItems()
        this.registerCommands()
    }

    private fun registerCommands() {
        val reloadCommand = CommandSpec.builder()
                .description(Text.of("Load items from config file"))
                .executor(::reload)
                .build()

        val getCommand = CommandSpec.builder()
                .description(Text.of("Give item to player"))
                .executor(::giveItem)
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.playerOrSource(Text.of(ARG_PLAYER))),
                        GenericArguments.choices(Text.of(ARG_ITEM), repository.items)
                ).build()

        val pluginCommand = CommandSpec.builder()
                .permission("ru.endlesscode.customitems.admin")
                .description(Text.of("Manage Custom Items"))
                .child(reloadCommand, "reload", "r")
                .child(getCommand, "give")
                .build()

        Sponge.getGame().commandManager.register(plugin, pluginCommand, "customitems", "ci")
    }

    private fun reload(src: CommandSource, args: CommandContext): CommandResult {
        initPlugin()
        src.sendMessage(Text.of("Plugin successfully reloaded!"))
        return CommandResult.success()
    }

    private fun giveItem(src: CommandSource, args: CommandContext): CommandResult {
        val player = args.getOne<Player>(ARG_PLAYER).get()
        val itemStack = args.getOne<Item>(ARG_ITEM).get().createItemStack()

        player.inventory.offer(itemStack)
        src.sendMessage(Text.of("Item successfully given to ${player.name}"))
        return CommandResult.success()
    }
}
