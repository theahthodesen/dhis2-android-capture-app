package org.dhis2.usescases.datasets.datasetInitial;

import com.google.auto.value.AutoValue;

import org.hisp.dhis.android.core.category.Category;
import org.hisp.dhis.android.core.period.PeriodType;

import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@AutoValue
public abstract class DataSetInitialModel {

    @NonNull
    public abstract String displayName();

    @Nullable
    public abstract String description();

    @NonNull
    public abstract String categoryCombo();

    @NonNull
    public abstract String categoryComboName();

    @NonNull
    public abstract PeriodType periodType();

    @NonNull
    public abstract List<Category> categories();

    @NonNull
    public static DataSetInitialModel create(@NonNull String displayName,
                                             @Nullable String description,
                                             @NonNull String categoryCombo,
                                             @NonNull String categoryComboName,
                                             @NonNull PeriodType periodType,
                                             @NonNull List<Category> categories) {
        return new AutoValue_DataSetInitialModel(displayName, description, categoryCombo, categoryComboName, periodType, categories);
    }

    public final List<Category> getCategories() {
        return Collections.unmodifiableList(categories());
    }
}
