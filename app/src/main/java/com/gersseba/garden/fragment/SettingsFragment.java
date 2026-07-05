package com.gersseba.garden.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gersseba.garden.databinding.FragmentSettingsBinding;
import com.gersseba.garden.i18n.LocaleManager;
import com.gersseba.garden.i18n.SettingsDataStoreImpl;

import java.util.Locale;

/**
 * Settings screen with language selector.
 */
public class SettingsFragment extends Fragment {
    private FragmentSettingsBinding binding;
    private LocaleManager localeManager;
    private boolean updatingSelection = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // use DataStore-backed settings store
        SettingsDataStoreImpl store = SettingsDataStoreImpl.create(context);
        localeManager = new LocaleManager(store);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Observe persisted locale and update UI when it posts a value to avoid races.
        localeManager.currentLocale().observe(getViewLifecycleOwner(), locale -> {
            if (locale == null) return;
            updatingSelection = true; // prevent listener loop
            if (Locale.GERMAN.getLanguage().equals(locale.getLanguage())) {
                binding.languageRadioGroup.check(binding.radioDe.getId());
            } else {
                binding.languageRadioGroup.check(binding.radioEn.getId());
            }
            updatingSelection = false;
        });

        binding.languageRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (updatingSelection) return;
            RadioButton rb = group.findViewById(checkedId);
            if (rb == null) return;
            String tag = (String) rb.getTag();
            Locale newLocale = Locale.forLanguageTag(tag);
            // LocaleManager performs persistence off the main thread; call directly.
            localeManager.setLocale(newLocale);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}



