package io.itcorner.moodle.feature.coursedetail;

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
public final class CourseDetailRepository_Factory implements Factory<CourseDetailRepository> {
  private final Provider<MoodleApi> apiProvider;

  private final Provider<TokenStore> tokenStoreProvider;

  public CourseDetailRepository_Factory(Provider<MoodleApi> apiProvider,
      Provider<TokenStore> tokenStoreProvider) {
    this.apiProvider = apiProvider;
    this.tokenStoreProvider = tokenStoreProvider;
  }

  @Override
  public CourseDetailRepository get() {
    return newInstance(apiProvider.get(), tokenStoreProvider.get());
  }

  public static CourseDetailRepository_Factory create(Provider<MoodleApi> apiProvider,
      Provider<TokenStore> tokenStoreProvider) {
    return new CourseDetailRepository_Factory(apiProvider, tokenStoreProvider);
  }

  public static CourseDetailRepository newInstance(MoodleApi api, TokenStore tokenStore) {
    return new CourseDetailRepository(api, tokenStore);
  }
}
