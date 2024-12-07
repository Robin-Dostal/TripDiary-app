package network

import com.example.traveldiary.models.Country
import com.example.traveldiary.models.Place
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT


interface ApiService {
    @GET("api/countries")
    fun getCountries(): Call<List<Country>>
    @POST("api/countries/save")
    fun saveCountry(@Body country: Country): Call<Void>
    @PUT("api/countries/update")
    fun updateCountry(@Body country: Country): Call<Void>
    /*
    @PUT("countries/{id}")
    fun updateCountry(
        @Path("id") id: String,
        @Body country: Country
    ): Call<Country>
     */



    @GET("api/places")
    fun getPlaces(): Call<List<Place>>
}
