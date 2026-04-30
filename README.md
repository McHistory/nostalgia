# Nostalgia

Nostalgia is an attempt to map older minecraft versions as closely as possible to the original names used in the code as possible.
Currently, nostalgia is only available for all minecraft beta versions (Only the latest of every last version though), with 100% of b1.0-b1.7.3 being mapped and b1.8-b1.9 being around 94% mapped.
If you would like a specific version, make an issue, and I'll see if I can get to it.

Currently, this project only has one maintainer, so PR's are greatly appreciated.

## Build Setup
```properties
# https://mvn.devos.one/#/releases/me/alphamode/nostalgia/
nostalgia_mappings=...
```

```groovy
repositories {
    maven { url 'https://mvn.devos.one/releases' }
}

<...>

dependencies {
    mappings ploceus.layeredMappings {
        mappings("me.alphamode:nostalgia:${project.nostalgia_mappings}:v2", {
            containsUnpick()
        })
    }
}
```

For checking the latest version please refer to the [maven](https://mvn.devos.one/#/releases/me/alphamode/nostalgia) or [here](https://github.com/McHistory/nostalgia/blob/main/named-mappings-builds.json)

## Usage
Under construction, sorry!