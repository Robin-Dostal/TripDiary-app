package network

import com.example.traveldiary.models.Country
import com.example.traveldiary.models.Place
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("api/countries")
    fun getCountries(): Call<List<Country>>

    @GET("api/places")
    fun getPlaces(): Call<List<Place>>
}
