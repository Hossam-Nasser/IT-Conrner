package io.itcorner.moodle.feature.courses;

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
public final class CoursesRepository_Factory implements Factory<CoursesRepository> {
  private final Provider<MoodleApi> apiProvider;

  private final Provider<TokenStore> tokenStoreProvider;

  public CoursesRepository_Factory(Provider<MoodleApi> apiProvider,
      Provider<TokenStore> tokenStoreProvider) {
    this.apiProvider = apiProvider;
    this.tokenStoreProvider = tokenStoreProvider;
  }

  @Override
  public CoursesRepository get() {
    return newInstance(apiProvider.get(), tokenStoreProvider.get());
  }

  public static CoursesRepository_Factory create(Provider<MoodleApi> apiProvider,
      Provider<TokenStore> tokenStoreProvider) {
    return new CoursesRepository_Factory(apiProvider, tokenStoreProvider);
  }

  public static CoursesRepository newInstance(MoodleApi api, TokenStore tokenStore) {
    return new CoursesRepository(api, tokenStore);
  }
}
