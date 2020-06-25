import java.io.File

fun main() {
    var choose: Int?
    do {
        println("Welcome to Installer ...\n1- setup folders\n2- setup AML Stag\n3- setup BU1\n4- setup BU2\n5- Exit")
        choose = readLine()?.toInt()
        when (choose) {
            1 -> setupFolders()
            2 -> setupAML()
            3 -> setupBU1()
            4 -> setupBU2()
        }
    } while (choose != 5)
}

fun setupBU1() {
    val changelogFiles = listOf("liquibase/fcfcore.changelog.xml", "liquibase/fcfkc.changelog.xml")
    changelogFiles.forEach {
        setupDatabase(openConnection(it), it)
    }
}

fun setupBU2() {
    val changelogFiles = listOf("liquibase/fcfcore2.changelog.xml", "liquibase/fcfkc2.changelog.xml")
    changelogFiles.forEach {
        setupDatabase(openConnection(it), it)
    }
}


fun setupAML() {
    val changelogFile = "liquibase/stg.changelog.xml"
    val connection: Connection = openConnection(changelogFile)
    setupDatabase(connection, changelogFile)
}

fun setupDatabase(connection: Connection, changelogFile: String) {
    val database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(JdbcConnection(connection))
    val liquibase = Liquibase(changelogFile, ClassLoaderResourceAccessor(), database)
    liquibase.update(Contexts(), LabelExpression())
}

fun openConnection(name: String): Connection {
    println("-- setup connection to $name --")
    print("Insert DB host : ")
    val host = readLine()
    print("Insert DB port : ")
    val port = readLine()
    print("Insert DB name or service name (Oracle) : ")
    val service = readLine()
    print("Insert DB username : ")
    val username = readLine()
    print("Insert DB password : ")
    val password = readLine()
    print("DB server : \n1- oracle\n2- sql server ")
    val server = readLine()?.toInt()
    val connection = if (server == 1) "jdbc:oracle:thin:@//$host:$port/$service" else "jdbc:sqlserver://$host:$port;database=$service"
    println("Connection String : $connection")
    return DriverManager.getConnection(connection, username, password)
}

private fun setupFolders() {
    print("Please Insert Home Path :")
    val homePath = readLine()


    val folders = listOf<String>("SASData","SASData/xerf","SASData/WL_Download",
    "SASData/Temp","SASLogs","SASLogs/Batch","SASLogs/Screening")

    println("# Create Dirs...")
    for (folder in folders)
        if(File("$SASHomePath/$folder").mkdir()) println("$folder created") else println("$folder not created ")


    println("# Copy files...")
    val copyFiles = listOf<String>("SASData/DM","SASCode/Screening")
    for(folder in copyFiles)
        File("./$folder")
                .copyRecursively(File("$homePath/$folder"),
                        true)
                { _, exception ->
                    println("Error Coping file...")
                    throw exception
                }.also { println("$folder copied successfully...") }


}