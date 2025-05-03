package com.example.hkbuequipments

import android.util.Log
import com.example.hkbuequipments.models.Equipment
import com.example.hkbuequipments.models.EquipmentsResponse
import com.example.hkbuequipments.models.LoginRequest
import com.example.hkbuequipments.models.LoginResponse
import com.example.hkbuequipments.models.RegisterRequest
import com.example.hkbuequipments.models.RentResponse
import com.example.hkbuequipments.models.User
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.accept
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable

@Serializable
data class HttpBinResponse(
    val args: Map<String, String>,
    val data: String,
    val files: Map<String, String>,
    val form: Map<String, String>,
    val headers: Map<String, String>,
    val json: String?,
    val origin: String,
    val url: String
)

/**
 *  KtorClient object for handling network requests to the equipments API.
 *  It uses the Ktor HTTP client to perform various operations such as fetching equipments,
 *  logging in, registering, reserving, and unreserving equipments.
 */
object KtorClient {
    private const val BASE_URL = "https://equipments-api.azurewebsites.net/api"
    private var token: String = ""
    private var logoutCallback: (() -> Unit)? = null

    /**
     * Sets a callback function to be invoked when the client detects an unauthorized access
     * and needs to log the user out.
     *
     * @param callback A function to be called when a logout is required.
     */
    fun setLogoutCallback(callback: () -> Unit) {
        logoutCallback = callback
    }

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json() // enable the client to perform JSON serialization
        }

        expectSuccess = true
        HttpResponseValidator {
            handleResponseExceptionWithRequest { exception, _ ->
                if (exception is ClientRequestException) {
                    when (exception.response.status.value) {
                        401 -> {
                            Log.d("KtorClient", "Unauthorized access, logging out")
                            logoutCallback?.invoke()
                        }

                        else -> {
                            Log.e("KtorClient", "HTTP error: ${exception.response.status.value}")
                        }
                    }
                }
            }
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
            accept(ContentType.Application.Json)
            header("Authorization", "Bearer $token")
        }
    }

    suspend fun getEquipments(
        keyword: String = "",
        location: String = "",
        highlighted: String = "",
        page: Int = 1,
        perPage: String = ""
    ): EquipmentsResponse {
        return try {
            val response: EquipmentsResponse =
                httpClient.get("${BASE_URL}/equipments") {
                    parameter("keyword", keyword)
                    parameter("location", location)
                    parameter("highlighted", highlighted)
                    parameter("page", page)
                    parameter("perPage", perPage)
                }.body()

            Log.d("KtorClient", "Response: ${response.page}\n${response.perPage}")
            response
        } catch (e: Exception) {
            Log.e("KtorClient", "Error fetching equipments: ${e.localizedMessage}")
            EquipmentsResponse(emptyList(), 0, 0, 0)
        }
    }


    suspend fun getEquipmentById(id: String): Equipment {
        return try {
            val response: Equipment =
                httpClient.get("${BASE_URL}/equipments/$id") {
                }.body()

            Log.d("KtorClient", "getEquipmentById Response: ${response.id}")
            response
        } catch (e: Exception) {
            Log.e("KtorClient", "Error fetching equipment by ID: ${e.localizedMessage}")
            Equipment()
        }
    }

    suspend fun login(email: String, password: String): String {
        return try {
            val loginRequest = LoginRequest(email, password)
            val response: LoginResponse = httpClient.post("${BASE_URL}/login") {
                setBody(loginRequest)
            }.body()

            token = response.token // Store the token
            Log.d("KtorClient", "Login successful: ${response.token}")
            token
        } catch (e: Exception) {
            Log.e("KtorClient", "Error during login: ${e.localizedMessage}")
            ""
        }
    }

    suspend fun register(request: RegisterRequest): String {
        return try {
            val response: String = httpClient.post("${BASE_URL}/users") {
                setBody(request)
            }.body()

            Log.d("KtorClient", "Registration successful: ${response}")
            response
        } catch (e: Exception) {
            Log.e("KtorClient", "Error during registration: ${e.localizedMessage}")
            ""
        }
    }

    suspend fun reserveEquipment(id: String): Boolean {
        return try {
            val response: RentResponse = httpClient.post("${BASE_URL}/equipments/${id}/rent") {
            }.body()

            Log.d("KtorClient", "Reservation successful: ${response}")
            when (response.message) {
                "Equipment rented successfully" -> {
                    true
                }

                "Time clash detected" -> {
                    throw IllegalArgumentException("Time clash detected")
                }

                else -> {
                    Log.e("KtorClient", "Unknown error: ${response}")
                    false
                }
            }
        } catch (e: Exception) {
            Log.e("KtorClient", "Error during reserving: ${e.localizedMessage}")
            false
        }
    }

    suspend fun unreserveEquipment(id: String): Boolean {
        return try {
            val response: RentResponse = httpClient.delete("${BASE_URL}/equipments/${id}/rent") {
            }.body()

            Log.d("KtorClient", "Equipment remove successful: ${response}")
            when (response.message) {
                "Equipment removed successfully" -> {
                    true
                }
                else -> {
                    Log.e("KtorClient", "Unknown error: ${response}")
                    false
                }
            }
        } catch (e: Exception) {
            Log.e("KtorClient", "Error during removing: ${e.localizedMessage}")
            false
        }
    }

    suspend fun getUserById(id: String = "any"): List<Equipment> {
        return try {
            val response: User = httpClient.get("${BASE_URL}/users/$id") {
            }.body()

            Log.d("KtorClient", "getUserById Response: ${response.equipments.size} equipment(s)")
            response.equipments
        } catch (e: Exception) {
            Log.e("KtorClient", "Error fetching user by ID: ${e.localizedMessage}")
            emptyList()
        }
    }
}

