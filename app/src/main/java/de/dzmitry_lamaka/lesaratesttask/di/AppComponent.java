package de.dzmitry_lamaka.lesaratesttask.di;

import javax.inject.Singleton;

import dagger.Component;
import de.dzmitry_lamaka.lesaratesttask.ui.TrendProductsViewModel;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(TrendProductsViewModel viewModel);
}
