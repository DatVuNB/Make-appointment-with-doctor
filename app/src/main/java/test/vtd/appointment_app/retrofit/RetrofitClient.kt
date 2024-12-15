package test.vtd.appointment_app.retrofit

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//Dùng từ khóa object để đảm bảo RetrofitClient là singleton, chỉ khởi tạo một lần trong toàn bộ ứng dụng
object RetrofitClient {
    private var instance:Retrofit? = null
    fun getInstance(baseUrl:String):Retrofit{
        //Dùng synchronized để đảm bảo rằng chỉ có một luồng được phép khởi tạo instance tại một thời điểm.
        return instance ?: synchronized(this){
            instance ?: Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .also { instance=it } // Gán giá trị retrofit cho instance mà không thay đổi retrofit.
        }
    }
}