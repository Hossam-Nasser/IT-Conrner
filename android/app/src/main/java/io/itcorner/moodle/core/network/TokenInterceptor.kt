package io.itcorner.moodle.core.network

import io.itcorner.moodle.core.auth.TokenStore
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val tokenStore: TokenStore,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val builder = original.url.newBuilder()
            .setQueryParameter("moodlewsrestformat", "json")
        tokenStore.token()?.let { builder.setQueryParameter("wstoken", it) }
        val req = original.newBuilder().url(builder.build()).build()
        return chain.proceed(req)
    }
}
