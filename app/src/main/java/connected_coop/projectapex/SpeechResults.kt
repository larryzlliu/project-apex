package connected_coop.projectapex

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

@SuppressLint("ParcelCreator")
data class SpeechResults (
        val pathToFile : String,
        val wordsPerMin : Float,
        val totalSpeechTime : Float,
        val fillers : Float
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readFloat(),
            parcel.readFloat(),
            parcel.readFloat())

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(pathToFile)
        dest?.writeFloat(wordsPerMin)
        dest?.writeFloat(totalSpeechTime)
        dest?.writeFloat(fillers)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SpeechResults> {
        override fun createFromParcel(parcel: Parcel): SpeechResults {
            return SpeechResults(parcel)
        }

        override fun newArray(size: Int): Array<SpeechResults?> {
            return arrayOfNulls(size)
        }
    }

}