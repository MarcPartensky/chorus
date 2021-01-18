package org.chorusmc.chorus.connection

import org.apache.commons.net.ftp.FTPSClient

/**
 * Class that handles connections over FTP using Apache Commons Net
 * @author Giorgio Garofalo
 */
class FTPRemoteConnection(override val ip: String, override val username: String, override val port: Int, override val password: String) : RemoteConnection {

    override var isValid: Boolean = false
    override var home: String = "/"

    val client = cl

    private val cl: FTPSClient?
        get() {
            val client = FTPSClient()
            return try {
                client.connect(ip, port)
                client.enterLocalPassiveMode()
                isValid = client.login(username, password)
                client.changeWorkingDirectory("/")
                home = client.printWorkingDirectory()
                client
            } catch(e: Exception) {
                null
            }
        }

    override fun getFiles(loc: String): List<RemoteFile> {
        val list = (client?.listFiles(loc)?.map {RemoteFile(it.name, it.isDirectory)} ?: emptyList()).toMutableList()
        if(!list.map {it.filename}.contains("..")) {
            list += RemoteFile("..", true)
        }
        return list
    }

    companion object : Password {
        override var psw = CharArray(0)
    }
}