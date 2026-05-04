package io.itcorner.moodle.feature.grades;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.itcorner.moodle.feature.courses.CoursesRepository;
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
public final class GradesViewModel_Factory implements Factory<GradesViewModel> {
  private final Provider<CoursesRepository> coursesRepositoryProvider;

  private final Provider<GradesRepository> gradesRepositoryProvider;

  public GradesViewModel_Factory(Provider<CoursesRepository> coursesRepositoryProvider,
      Provider<GradesRepository> gradesRepositoryProvider) {
    this.coursesRepositoryProvider = coursesRepositoryProvider;
    this.gradesRepositoryProvider = gradesRepositoryProvider;
  }

  @Override
  public GradesViewModel get() {
    return newInstance(coursesRepositoryProvider.get(), gradesRepositoryProvider.get());
  }

  public static GradesViewModel_Factory create(
      Provider<CoursesRepository> coursesRepositoryProvider,
      Provider<GradesRepository> gradesRepositoryProvider) {
    return new GradesViewModel_Factory(coursesRepositoryProvider, gradesRepositoryProvider);
  }

  public static GradesViewModel newInstance(CoursesRepository coursesRepository,
      GradesRepository gradesRepository) {
    return new GradesViewModel(coursesRepository, gradesRepository);
  }
}
