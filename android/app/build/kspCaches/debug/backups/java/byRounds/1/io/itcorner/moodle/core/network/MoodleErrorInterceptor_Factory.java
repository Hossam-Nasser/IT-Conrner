package io.itcorner.moodle.core.network;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;
import kotlinx.serialization.json.Json;

@ScopeMetadata
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
public final class MoodleErrorInterceptor_Factory implements Factory<MoodleErrorInterceptor> {
  private final Provider<Json> jsonProvider;

  public MoodleErrorInterceptor_Factory(Provider<Json> jsonProvider) {
    this.jsonProvider = jsonProvider;
  }

  @Override
  public MoodleErrorInterceptor get() {
    return newInstance(jsonProvider.get());
  }

  public static MoodleErrorInterceptor_Factory create(Provider<Json> jsonProvider) {
    return new MoodleErrorInterceptor_Factory(jsonProvider);
  }

  public static MoodleErrorInterceptor newInstance(Json json) {
    return new MoodleErrorInterceptor(json);
  }
}
