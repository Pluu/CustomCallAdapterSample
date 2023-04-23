# Retrofit Custom CallAdapter.Factory

> 참고 : [Arrow Retrofit](https://github.com/arrow-kt/arrow/tree/main/arrow-libs/core/arrow-core-retrofit)



### Define Retrofit Service

```kotlin
import com.pluu.retrofit.adapter.ApiResult
import retrofit2.http.GET

data class User(val name: String)

interface GitHubService {
  @GET("/users/Pluu")
  suspend fun getUser(): ApiResult<User>
}
```

### Use Service

```kotlin
// Type 1 : default
viewModelScope.launch {
  val apiResult = api.getUser()
  when {
    apiResult.isSuccess() -> {
      // TODO, success action (ex: apiResult.result())
    }
    else -> {
      // TODO, fail action (ex: apiResult.error())
    }
  }
}

// Type 2 : use onSuccess/onFailure
viewModelScope.launch {
  api.getUser()
    .onSuccess { result ->
      // TODO, success action
    }.onFailure { error ->
      // TODO, fail action
    }
}
```

