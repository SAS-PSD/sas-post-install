import java.io.File

fun main() {

    print("Please Insert Path of SASHome :")
    val SASHomePath = readLine()


    val folders = listOf<String>("SASData","SASData/xerf","SASData/WL_Download",
    "SASData/Temp","SASLogs","SASLogs/Batch","SASLogs/Screening")

    println("# Create Dirs...")
    for (folder in folders)
        if(File("$SASHomePath/$folder").mkdir()) println("$folder created") else println("$folder not created ")



    println("# Copy files...")
    val copyFiles = listOf<String>("SASData/DM","SASCode/Screening")
    for(folder in copyFiles)
        File("./$folder")
                .copyRecursively(File("$SASHomePath/$folder"),
                        true)
                { _, exception ->
                    println("Error Coping file...")
                    throw exception
                }.also { println("$folder copied successfully...") }


}