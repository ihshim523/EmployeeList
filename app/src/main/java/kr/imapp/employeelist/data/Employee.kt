package kr.imapp.employeelist.data

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Employee(
    @SerializedName("uuid")
    val uuid: String?,
    @SerializedName("full_name")
    val fullName: String?,
    @SerializedName("phone_number")
    val phoneNumber: String?,
    @SerializedName("email_address")
    val email: String?,
    @SerializedName("biography")
    val biography: String?,
    @SerializedName("photo_url_small")
    val smallPhotoUrl: String?,
    @SerializedName("photo_url_large")
    val largePhotoUrl: String?,
    @SerializedName("team")
    val team: String?,
    @SerializedName("employee_type")
    val employeeType: String?,
) : Parcelable
