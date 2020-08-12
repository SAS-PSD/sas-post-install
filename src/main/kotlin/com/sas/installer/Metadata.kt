package com.sas.installer

import java.io.File
import java.util.concurrent.TimeUnit


fun setupMetaData() {
    println("Enter SAS Home path :")
    val sasHomePath = readLine()
    val workingDir = File("$sasHomePath/SASPlatformObjectFramework/9.4")

    val scriptExtison = when(getOS()){
        OS.WINDOWS->".exe"
        OS.LINUX->".sh"
        else->""
    }
    println("Enter SAS Profile :")
    val sasProfile = readLine()
    val script = ".\\ImportPackage$scriptExtison -profile $sasProfile -package \"${getPath("ETL-AML-MSSQL.spk")}\" -subprop \"${getPath("ETL-AML-MSSQL")}\" -target \"/System/Servers\""

    println(script)

    script.runCommand(workingDir)
}

fun getOS(): OS {
    val os = System.getProperty("os.name").toLowerCase()
    return when {
        os.contains("win") -> OS.WINDOWS
        os.contains("nix") || os.contains("nux") || os.contains("aix") -> OS.LINUX
        else -> OS.UNDEFINED
    }
}

fun String.runCommand(workingDir: File) {
    ProcessBuilder(*split(" ").toTypedArray())
            .directory(workingDir)
            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
            .redirectError(ProcessBuilder.Redirect.INHERIT)
            .start()
            .waitFor(60, TimeUnit.MINUTES)
}

fun getPath(filename:String):String{
    return File("metadata/$filename").absolutePath
}

enum class OS { WINDOWS, LINUX ,UNDEFINED}