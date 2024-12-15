package test.vtd.appointment_app.retrofit

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import test.vtd.appointment_app.model.MakeAppointmentModel
import test.vtd.appointment_app.model.UsersModel

interface ApiAppointment {
    @POST("register.php")
    @FormUrlEncoded
    fun register(
        @Field("id") id: Int,
        @Field("userName") userName: String,
        @Field("email") email: String,
        @Field("dateOfBirth") dateOfBirth: String
    ):Observable<UsersModel>

    @POST("login.php")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String
    ):Observable<UsersModel>

    @POST("makeAppointment.php")
    @FormUrlEncoded
    fun makeAppointment(
        @Field("doctorId") doctorId: Int,
        @Field("userId") userId: Int,
        @Field("time") time: String,
        @Field("note") note: String
    ):Observable<MakeAppointmentModel>

    @GET("getAppointment.php")
    fun getAppointment():Observable<MakeAppointmentModel>

    @POST("changeStateAppointment.php")
    @FormUrlEncoded
    fun changeStateAppointment(
        @Field("id") id: Int,
        @Field("state") state: Int,
        @Field("note") note: String
    ):Observable<MakeAppointmentModel>

    @POST("updateUser.php")
    @FormUrlEncoded
    fun updateUser(
        @Field("id") id: Int,
        @Field("userName") userName: String,
        @Field("dateOfBirth") dateOfBirth: String
    ): Observable<UsersModel>
}