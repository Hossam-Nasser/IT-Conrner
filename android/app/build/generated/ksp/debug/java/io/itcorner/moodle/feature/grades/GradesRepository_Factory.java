package io.itcorner.moodle.feature.grades;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.itcorner.moodle.core.auth.TokenStore;
import io.itcorner.moodle.core.network.MoodleApi;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class GradesRepository_Factory implements Factory<GradesRepository> {
  private final Provider<MoodleApi> apiProvider;

  private final Provider<TokenStore> tokenStoreProvider;

  public GradesRepository_Factory(Provider<MoodleApi> apiProvider,
      Provider<TokenStore> tokenStoreProvider) {
    this.apiProvider = apiProvider;
    this.tokenStoreProvider = tokenStoreProvider;
  }

  @Override
  public GradesRepository get() {
    return newInstance(apiProvider.get(), tokenStoreProvider.get());
  }

  public static GradesRepository_Factory create(Provider<MoodleApi> apiProvider,
      Provider<TokenStore> tokenStoreProvider) {
    return new GradesRepository_Factory(apiProvider, tokenStoreProvider);
  }

  public static GradesRepository newInstance(MoodleApi api, TokenStore tokenStore) {
    return new GradesRepository(api, tokenStore);
  }
}
