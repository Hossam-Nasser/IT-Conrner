package io.itcorner.moodle.navigation;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.itcorner.moodle.core.auth.AuthRepository;
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
public final class StartGateViewModel_Factory implements Factory<StartGateViewModel> {
  private final Provider<AuthRepository> authProvider;

  public StartGateViewModel_Factory(Provider<AuthRepository> authProvider) {
    this.authProvider = authProvider;
  }

  @Override
  public StartGateViewModel get() {
    return newInstance(authProvider.get());
  }

  public static StartGateViewModel_Factory create(Provider<AuthRepository> authProvider) {
    return new StartGateViewModel_Factory(authProvider);
  }

  public static StartGateViewModel newInstance(AuthRepository auth) {
    return new StartGateViewModel(auth);
  }
}
