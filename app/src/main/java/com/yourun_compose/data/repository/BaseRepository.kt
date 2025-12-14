package com.yourun_compose.data.repository

import android.util.Log
import com.yourun_compose.data.model.common.BaseResponse
import retrofit2.Response

abstract class BaseRepository {

    protected suspend fun <T> safeApiCall(
        apiCall: suspend () -> Response<BaseResponse<T>>
    ): Result<T> {
        return try {
            val response = apiCall()
            val body = response.body()

            if (response.isSuccessful && body != null) {
                if (body.data != null) {
                    Result.success(body.data)
                } else {
                    // 데이터가 null이지만 통신은 성공한 경우 (예: 성공 메시지만 옴)
                    // T가 Boolean일 경우 true로 처리하거나, 상황에 따라 예외 처리
                    // 여기서는 안전하게 실패로 처리하되 메시지를 넘김
                    Result.failure(Exception(body.message ?: "데이터가 없습니다."))
                }
            } else {
                val errorCode = response.code()
                val errorMsg = response.errorBody()?.string() ?: "알 수 없는 에러"
                Log.e("API_ERROR", "Code: $errorCode, Msg: $errorMsg")
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Log.e("API_EXCEPTION", e.message ?: "Unknown Exception")
            Result.failure(e)
        }
    }
}