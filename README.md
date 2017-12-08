# CustomItems
Simple Sponge plugin for creating  items with custom lore and name

### Details
- Written on Kotlin
- Used Kotlin Build Script
- Used Commands API and Configurations API from Sponge

### Usage
Plugin is very simple in usage. You just should configure and add items to 
config and reload plugin. Then you free to give these items to any players.

Give item to Notch: `/ci give Notch testItem` \
Give item to itself: `/ci give testItem`

#### Config format
```hocon
testItem { // This ID will be used for giving item
  // Required
  name: "&6&lEpic &m&8Sword&r &4&lSWORD" 
  
  // Required. You also can use items from mods in 
  // format "modid:itemid"
  texture: "diamond_sword"
  
  // Optional.  By default is `true`
  unbreakable: false 
  
  // Durability of item or meta for blocks like wool etc.
  // Optional. By default -1 (none)
  meta: 4
  
  // Optional. By default empty
  lore: [
    "&7Rarity: &3IMBA"
    "&c+2000 Damage"
    "&a+1000 Health"
    "&b+100% Dodge"
    "&2-1 Luck"
    ""
    "&e&o- I've got the POWER!"
  ]
}

```

#### Commands 
**Aliases:** `customitems`, `ci` \
_`()` - optional, `[]` - required_

`/ci reload|r` - Reload plugin \
`/ci give (player) [item_id]` - Give customized item to user

_NOTE: You can use 'Tab' to quickly select needed item id or username_

#### Permissions
`ru.endlesscode.customitems.admin` - Access to all commands
