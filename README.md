# ABOUT THIS CLIENT

A client was make from superblaubeere27 client base with some basic modules

## Credits
- EventAPI: Darkmagican8 (https://bitbucket.org/DarkMagician6/eventapi)
- Some parts of the injection classes + gradle files: LiquidBase by CCBlueX (https://git.liquidbounce.net/CCBlueX/LiquidBase/)
- Client Base : https://github.com/superblaubeere27/ClientBase


# Setup
JVM-Args: -Dfml.coreMods.load=net.superblaubeere27.clientbase.injection.MixinLoader Please replace net.superblaubeere27.clientbase.injection.MixinLoader with the full class name of MixinLoader (if you moved the class)

# IntelliJ IDEA
Gradle setup command: gradlew -Dorg.gradle.jvmargs=-Xmx5G setupDecompWorkspace idea genIntelliJRuns build

# Eclipse
Gradle setup command: gradlew -Dorg.gradle.jvmargs=-Xmx5G setupDecompWorkspace eclipse build

# Export
Gradle build command: gradlew clean build
