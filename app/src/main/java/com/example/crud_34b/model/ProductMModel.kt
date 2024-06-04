package com.example.crud_34b.model

import android.os.Parcel
import android.os.Parcelable

class ProductMModel(
    var id : String = "",
    var name: String = "",
    var price: Int = 0,
    var description: String = ""
) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeInt(price)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductMModel> {
        override fun createFromParcel(parcel: Parcel): ProductMModel {
            return ProductMModel(parcel)
        }

        override fun newArray(size: Int): Array<ProductMModel?> {
            return arrayOfNulls(size)
        }
    }


}