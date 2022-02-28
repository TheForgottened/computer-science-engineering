package pt.isec.amov.ajp.reversi.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import org.json.JSONObject
import pt.isec.amov.ajp.reversi.game.TypePiece
import java.io.ByteArrayOutputStream
import java.io.Serializable
import java.net.Socket

class Player : Serializable {
    var name: String
    var avatar : Bitmap? = null

    var currentPiece: TypePiece = TypePiece.NORMAL
    var bombPiece : Boolean = true
    var swapPiece : Boolean = true

    var socket : Socket? = null

    var score : Int = 0

    constructor(socket: Socket) {
        this.socket = socket

        var jsonObject = JSONObject()

        socket!!.getInputStream()?.run {
            try {
                jsonObject = JSONObject(this.bufferedReader().readLine())
            } catch (_: Exception) {
            }
        }

        name = jsonObject.getString(JSONHelper.JSONFields.PLAYER_NAME_NAME)

        val decodedAvatarString = Base64.decode(
            jsonObject.getString(JSONHelper.JSONFields.AVATAR_NAME),
            Base64.URL_SAFE
        )

        avatar = BitmapFactory.decodeByteArray(
            decodedAvatarString,
            0,
            decodedAvatarString.size
        )
    }

    constructor(name: String) {
        this.name = name
    }

    fun getAsJsonString() = getAsJsonObject().toString()

    fun getAsJsonObject() : JSONObject {
        val jsonObject = JSONObject()

        jsonObject.put(JSONHelper.JSONFields.TYPE_NAME, JSONHelper.PLAYER_INFO)
        jsonObject.put(JSONHelper.JSONFields.PLAYER_NAME_NAME, name)
        jsonObject.put(JSONHelper.JSONFields.AVATAR_NAME, getBitmapAsEncodedString())
        jsonObject.put(JSONHelper.JSONFields.SCORE_NAME, score)

        return jsonObject
    }

    private fun getBitmapAsEncodedString() : String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        avatar?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)

        val byteArray = byteArrayOutputStream.toByteArray()

        return Base64.encodeToString(byteArray, Base64.URL_SAFE)
    }

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is Player) return false

        val that = other as Player

        return that.name == this.name
    }
}