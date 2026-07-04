package com.gersseba.garden.viewmodel;

import com.gersseba.garden.model.PlantCareTask;
import com.gersseba.garden.model.PlantDetailInfo;
import com.gersseba.garden.model.PlantPhoto;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link PlantDetailViewModel} mocked data builders.
 *
 * Tests call the {@code buildMocked*()} methods directly to avoid LiveData/Looper
 * setup — the same strategy used by {@code CarePlanViewModelTest}.
 */
public class PlantDetailViewModelTest {

    private PlantDetailViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new PlantDetailViewModel();
    }

    // ── buildMockedPhotos ──────────────────────────────────────────────────

    @Test
    public void buildMockedPhotos_returnsNonEmptyList() {
        List<PlantPhoto> photos = viewModel.buildMockedPhotos();

        assertNotNull(photos);
        assertFalse("Expected at least one mocked photo", photos.isEmpty());
    }

    @Test
    public void buildMockedPhotos_returnsAtLeastThreePhotos() {
        List<PlantPhoto> photos = viewModel.buildMockedPhotos();

        assertEquals("Expected 3 mocked photos", 3, photos.size());
    }

    @Test
    public void buildMockedPhotos_allPhotosHaveNonZeroResourceIds() {
        List<PlantPhoto> photos = viewModel.buildMockedPhotos();

        for (PlantPhoto photo : photos) {
            assertFalse("drawableRes must be a valid resource ID", photo.drawableRes == 0);
            assertFalse("aiSummaryRes must be a valid resource ID", photo.aiSummaryRes == 0);
        }
    }

    @Test
    public void buildMockedPhotos_allDrawableResourceIdsAreDistinct() {
        List<PlantPhoto> photos = viewModel.buildMockedPhotos();

        assertEquals("Expected 3 photos", 3, photos.size());
        assertFalse("Photo 0 and 1 should use different drawables",
                photos.get(0).drawableRes == photos.get(1).drawableRes);
        assertFalse("Photo 1 and 2 should use different drawables",
                photos.get(1).drawableRes == photos.get(2).drawableRes);
    }

    // ── buildMockedGeneralInfo ─────────────────────────────────────────────

    @Test
    public void buildMockedGeneralInfo_returnsNonNull() {
        PlantDetailInfo info = viewModel.buildMockedGeneralInfo();

        assertNotNull(info);
    }

    @Test
    public void buildMockedGeneralInfo_allFieldsHaveNonZeroResourceIds() {
        PlantDetailInfo info = viewModel.buildMockedGeneralInfo();

        assertFalse("scientificNameRes must be a valid resource ID", info.scientificNameRes == 0);
        assertFalse("plantFamilyRes must be a valid resource ID", info.plantFamilyRes == 0);
        assertFalse("sunExposureRes must be a valid resource ID", info.sunExposureRes == 0);
        assertFalse("wateringFrequencyRes must be a valid resource ID", info.wateringFrequencyRes == 0);
        assertFalse("soilTypeRes must be a valid resource ID", info.soilTypeRes == 0);
    }

    // ── buildMockedCareTasks ───────────────────────────────────────────────

    @Test
    public void buildMockedCareTasks_returnsNonEmptyList() {
        List<PlantCareTask> tasks = viewModel.buildMockedCareTasks();

        assertNotNull(tasks);
        assertFalse("Expected at least one mocked care task", tasks.isEmpty());
    }

    @Test
    public void buildMockedCareTasks_returnsAtLeastThreeTasks() {
        List<PlantCareTask> tasks = viewModel.buildMockedCareTasks();

        assertEquals("Expected 4 mocked care tasks", 4, tasks.size());
    }

    @Test
    public void buildMockedCareTasks_allTasksHaveNonZeroResourceIds() {
        List<PlantCareTask> tasks = viewModel.buildMockedCareTasks();

        for (PlantCareTask task : tasks) {
            assertFalse("taskTypeRes must be a valid resource ID", task.taskTypeRes == 0);
            assertFalse("descriptionRes must be a valid resource ID", task.descriptionRes == 0);
        }
    }

    // ── model constructors ─────────────────────────────────────────────────

    @Test
    public void plantPhoto_storesAllFields() {
        PlantPhoto photo = new PlantPhoto(1, 2);

        assertEquals(1, photo.drawableRes);
        assertEquals(2, photo.aiSummaryRes);
    }

    @Test
    public void plantDetailInfo_storesAllFields() {
        PlantDetailInfo info = new PlantDetailInfo(1, 2, 3, 4, 5);

        assertEquals(1, info.scientificNameRes);
        assertEquals(2, info.plantFamilyRes);
        assertEquals(3, info.sunExposureRes);
        assertEquals(4, info.wateringFrequencyRes);
        assertEquals(5, info.soilTypeRes);
    }

    @Test
    public void plantCareTask_storesAllFields() {
        PlantCareTask task = new PlantCareTask(1, 2);

        assertEquals(1, task.taskTypeRes);
        assertEquals(2, task.descriptionRes);
    }
}

