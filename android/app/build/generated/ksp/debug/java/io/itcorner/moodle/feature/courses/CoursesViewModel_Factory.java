package io.itcorner.moodle.feature.courses;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class CoursesViewModel_Factory implements Factory<CoursesViewModel> {
  private final Provider<CoursesRepository> repositoryProvider;

  public CoursesViewModel_Factory(Provider<CoursesRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public CoursesViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static CoursesViewModel_Factory create(Provider<CoursesRepository> repositoryProvider) {
    return new CoursesViewModel_Factory(repositoryProvider);
  }

  public static CoursesViewModel newInstance(CoursesRepository repository) {
    return new CoursesViewModel(repository);
  }
}
