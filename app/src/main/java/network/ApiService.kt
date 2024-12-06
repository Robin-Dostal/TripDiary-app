package network

import com.example.traveldiary.models.Country
import com.example.traveldiary.models.Place
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("api/countries")
    fun getCountries(): Call<List<Country>>
    @POST("api/countries/save")
    fun saveCountry(@Body country: Country): Call<Void>

    @GET("api/places")
    fun getPlaces(): Call<List<Place>>
}
