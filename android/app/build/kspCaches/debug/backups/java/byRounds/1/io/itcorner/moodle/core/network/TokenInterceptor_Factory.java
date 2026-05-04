package io.itcorner.moodle.core.network;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import io.itcorner.moodle.core.auth.TokenStore;
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
public final class TokenInterceptor_Factory implements Factory<TokenInterceptor> {
  private final Provider<TokenStore> tokenStoreProvider;

  public TokenInterceptor_Factory(Provider<TokenStore> tokenStoreProvider) {
    this.tokenStoreProvider = tokenStoreProvider;
  }

  @Override
  public TokenInterceptor get() {
    return newInstance(tokenStoreProvider.get());
  }

  public static TokenInterceptor_Factory create(Provider<TokenStore> tokenStoreProvider) {
    return new TokenInterceptor_Factory(tokenStoreProvider);
  }

  public static TokenInterceptor newInstance(TokenStore tokenStore) {
    return new TokenInterceptor(tokenStore);
  }
}
