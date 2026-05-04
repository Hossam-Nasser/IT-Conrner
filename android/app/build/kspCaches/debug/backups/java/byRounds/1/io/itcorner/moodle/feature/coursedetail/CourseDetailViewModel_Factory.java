package io.itcorner.moodle.feature.coursedetail;

import androidx.lifecycle.SavedStateHandle;
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
public final class CourseDetailViewModel_Factory implements Factory<CourseDetailViewModel> {
  private final Provider<CourseDetailRepository> repositoryProvider;

  private final Provider<SavedStateHandle> savedStateProvider;

  public CourseDetailViewModel_Factory(Provider<CourseDetailRepository> repositoryProvider,
      Provider<SavedStateHandle> savedStateProvider) {
    this.repositoryProvider = repositoryProvider;
    this.savedStateProvider = savedStateProvider;
  }

  @Override
  public CourseDetailViewModel get() {
    return newInstance(repositoryProvider.get(), savedStateProvider.get());
  }

  public static CourseDetailViewModel_Factory create(
      Provider<CourseDetailRepository> repositoryProvider,
      Provider<SavedStateHandle> savedStateProvider) {
    return new CourseDetailViewModel_Factory(repositoryProvider, savedStateProvider);
  }

  public static CourseDetailViewModel newInstance(CourseDetailRepository repository,
      SavedStateHandle savedState) {
    return new CourseDetailViewModel(repository, savedState);
  }
}
