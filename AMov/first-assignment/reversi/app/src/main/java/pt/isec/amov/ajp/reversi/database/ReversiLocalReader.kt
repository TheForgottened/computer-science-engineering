package pt.isec.amov.ajp.reversi.database

import org.json.JSONObject
import java.io.File

object ReversiLocalReader {
    lateinit var filesDir: File

    fun createIfUnavailable() {
        val jsonFile = File(filesDir, Contract.JSON_NAME)
        if (jsonFile.exists() && jsonFile.readText().isNotBlank()) return

        jsonFile.createNewFile()

        val jsonString =
            "{" +
            "   \"" + Contract.JsonFields.NAME_NAME + "\": \"Player\"" +
            "}"

        jsonFile.writeText(jsonString)
    }

    fun getName(): String {
        val jsonFile = File(filesDir, Contract.JSON_NAME)
        val jsonString = jsonFile.readText()

        return JSONObject(jsonString).getString(Contract.JsonFields.NAME_NAME)
    }

    fun setName(name: String) {
        val jsonFile = File(filesDir, Contract.JSON_NAME)
        val jsonString =
            "{" +
            "   \"" + Contract.JsonFields.NAME_NAME + "\": \"" + name + "\"" +
            "}"

        jsonFile.writeText(jsonString)
    }

    fun hasAvatar(): Boolean = File(filesDir, Contract.PICTURE_NAME).exists()
    fun getAvatarPath(): String = File(filesDir, Contract.PICTURE_NAME).absolutePath

    object Contract {
        const val JSON_NAME = "reversi.json"
        const val PICTURE_NAME = "avatar.jpeg"

        object JsonFields {
            const val NAME_NAME = "name"
        }
    }
}