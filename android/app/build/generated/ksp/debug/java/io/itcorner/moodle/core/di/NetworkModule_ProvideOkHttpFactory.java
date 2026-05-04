package io.itcorner.moodle.core.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.itcorner.moodle.core.network.MoodleErrorInterceptor;
import io.itcorner.moodle.core.network.TokenInterceptor;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import okhttp3.OkHttpClient;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class NetworkModule_ProvideOkHttpFactory implements Factory<OkHttpClient> {
  private final Provider<TokenInterceptor> tokenInterceptorProvider;

  private final Provider<MoodleErrorInterceptor> errorInterceptorProvider;

  public NetworkModule_ProvideOkHttpFactory(Provider<TokenInterceptor> tokenInterceptorProvider,
      Provider<MoodleErrorInterceptor> errorInterceptorProvider) {
    this.tokenInterceptorProvider = tokenInterceptorProvider;
    this.errorInterceptorProvider = errorInterceptorProvider;
  }

  @Override
  public OkHttpClient get() {
    return provideOkHttp(tokenInterceptorProvider.get(), errorInterceptorProvider.get());
  }

  public static NetworkModule_ProvideOkHttpFactory create(
      Provider<TokenInterceptor> tokenInterceptorProvider,
      Provider<MoodleErrorInterceptor> errorInterceptorProvider) {
    return new NetworkModule_ProvideOkHttpFactory(tokenInterceptorProvider, errorInterceptorProvider);
  }

  public static OkHttpClient provideOkHttp(TokenInterceptor tokenInterceptor,
      MoodleErrorInterceptor errorInterceptor) {
    return Preconditions.checkNotNullFromProvides(NetworkModule.INSTANCE.provideOkHttp(tokenInterceptor, errorInterceptor));
  }
}
