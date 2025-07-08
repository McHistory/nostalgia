# Nostalgia

![Build](https://img.shields.io/github/actions/workflow/status/McHistory/nostalgia/build.yml?label=Build&branch=b1.7.3)
![Build All](https://img.shields.io/github/actions/workflow/status/McHistory/nostalgia/build_all.yml?label=Build%20All&branch=gen2)
![Publish](https://img.shields.io/github/actions/workflow/status/McHistory/nostalgia/publish.yml?label=Publish&branch=b1.7.3)

Nostalgia is a set of Minecraft mappings aiming to be as accurate as the original code as possible.
Names gathered for these mappings have been retrieved from various sources. Such as MCPE, b1.2_02 deobfuscated jar, and the first version of [mojmap](https://piston-data.mojang.com/v1/objects/a981dbf4095dbb2ffb078db14d676fb8f9c2fa99/client.txt), among various other places.
There are gaps in these sources so we assume the closet possible name of what the original would be.

## Build Setup
```properties
# https://maven.alphasmoddingserver.com/#/releases/me/alphamode/nostalgia
nostalgia_mappings=...
```

```groovy
repositories {
    maven { url 'https://maven.alphasmoddingserver.com/' }
}

<...>

dependencies {
    mappings "me.alphamode:nostalgia:${project.nostalgia_mappings}:v2"
}
```

## Usage
To use nostalgia-deobfuscated Minecraft for Minecraft modding or as a dependency in a Java project, you can use [loom](https://github.com/FabricMC/fabric-loom) and [ploceus](https://github.com/OrnitheMC/ploceus) Gradle plugins. See [fabric wiki tutorial](https://fabricmc.net/wiki/tutorial:setup) for more information.

To obtain a deobfuscated Minecraft jar, [`py nostalgia.py mapMinecraftToNamed <minecraft version>`](#mapMinecraftToNamed) will generate a jar named like `<minecraft version>-nostalgia-gen2.jar`, which can be sent to a decompiler for deobfuscated code.
You can also directly generate a mapped jar and decompile the code using one of the following commands (no need to run the `mapMinecraftToNamed` task first):
- CFR: `py nostalgia.py decompileWithCfr <minecraft version>`
- Vineflower: `py nostalgia.py decompileWithVineflower <minecraft version>`

## Contributing

We recommend discussing your contribution with other members of the community - either directly in your pull request, or in our other community spaces. We're always happy to help if you need us

### Getting Started

1. Fork and clone the repo
2. Run `py nostalgia.py enigma <minecraft version>` to open [Enigma](https://github.com/OrnitheMC/Enigma), a user interface to easily edit the mappings
3. Save your changes in Enigma and store them by running one of the following save tasks (`py nostalgia.py <task> <minecraft version>`):
   - `saveMappings`: propagate your changes up and down the version graph and save them to every applicable Minecraft version (this is most likely the task you want to use)
   - `insertMappings`: save your changes only to the specified Minecraft version
   - `saveMappingsDown`: propagate your changes down the version graph (to versions further away from the root (b1.0)) and save them to every applicable Minecraft version
   - `saveMappingsUp`: propagate your changes up the version graph (to versions closer to the root (b1.0)) and save them to every applicable Minecraft version
4. If you wish to continue working in Enigma, make sure to reload the mappings.
5. When you're done, commit and push your work to your fork
6. Open a pull request with your changes

#### NOTE

The `enigma` task loads the mappings for the specified version out into temporary files in the `/run/` folder. Enigma will read and write to these files, and the save tasks will use these files to save the mappings back into the version graph.

- DO NOT MANUALLY EDIT THESE FILES! You may corrupt the mappings!
- Running the `enigma` task **will** overwrite these files. If you have unsaved changes, make sure to run one of the save tasks **before** running the `enigma` task to open Enigma again!
- You can safely open multiple Enigma instances for *different* Minecraft versions. You **cannot** safely open multiple Enigma instances for *one* Minecraft version.

## Gradle
Nostalgia uses Gradle to provide a number of utility tasks for working with the mappings.

### `enigma`
Download and launch the latest version of [Enigma](https://github.com/OrnitheMC/Enigma) automatically configured to use the merged jar and the mappings.

Compared to launching Enigma externally, the gradle task adds a name guesser plugin that automatically map enums and a few constant field names.

### `build`
Build a GZip'd archive containing a tiny mapping between official (obfuscated), [intermediary](https://github.com/OrnitheMC/calamus), and Nostalgia names ("named") and packages Tiny V1 mappings into a zip archive.

### `mapMinecraftToNamed`
Builds a deobfuscated jar with Nostalgia mappings and automapped fields (enums, etc.). Unmapped names will be filled with [intermediary](https://github.com/OrnitheMC/calamus) names.

### `decompileWithCfr`
Decompile the mapped source code with the CFR decompiler. **Note:** This is not designed to be recompiled.

### `decompileWithVineflower`
Decompile the mapped source code with the Vineflower decompiler. **Note:** This is not designed to be recompiled.

### `downloadMinecraftJars`
Downloads the client and server Minecraft jars for the current Minecraft version to `/ornithe-cache/game-jars/` in your user gradle cache.

### `mergeMinecraftJars`
Merges the client and server jars into one merged jar, located at `/ornithe-cache/game-jars/<minecraft_version>-merged.jar` in your user gradle cache.
