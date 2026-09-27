plugins {
    id("maven-publish")
    id("net.fabricmc.fabric-loom") version "1.17-SNAPSHOT" apply false
    id("net.fabricmc.fabric-loom-remap") version "1.17-SNAPSHOT" apply false

    // https://github.com/ReplayMod/preprocessor
    // https://github.com/Fallen-Breath/preprocessor
    // https://jitpack.io/#Fallen-Breath/preprocessor
    id("com.replaymod.preprocess") version "c5abb4fb12"
}

preprocess {
    strictExtraMappings.set(false)

    // 主干源码（versions/mainProject = 1.21.11）放在根目录 src/main，
    // 26.2 的源码由主干经两级映射转换得到，所以这两个节点必须留在图中。
    // 它们只参与源码转换，不要构建它们，只构建 :26.2
    val mc12111     = createNode("1.21.11", 1_21_11, "mojang")
    val mc260102    = createNode("26.1.2",  26_01_02,"mojang")
    val mc260200    = createNode("26.2",    26_02_00,"mojang")

    mc12111 .link(mc260102, file("versions/mapping-1.21.11-26.1.2.txt"))
    mc260102.link(mc260200, file("versions/mapping-26.1.2-26.2.txt"))

    // See https://github.com/Fallen-Breath/fabric-mod-template/blob/1d72d77a1c5ce0bf060c2501270298a12adab679/build.gradle#L55-L63
    for (node in getNodes()) {
        findProject(node.project)
            ?.ext
            ?.set("mcVersion", node.mcVersion)
    }
}