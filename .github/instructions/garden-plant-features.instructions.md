---
description: 'Plant garden app domain patterns and feature specifications for consistent implementation across plant identification, photo management, care scheduling, and AI integration.'
applyTo: '**/*.java, **/*.xml, **/*Fragment.java, **/*ViewModel.java'
---

# Garden App — Plant Feature Instructions

## Domain Overview

The `garden` app has three core domains:

1. **Plant Identification & Registration** — Capture photos → Gemini AI → save plant to database
2. **Plant Management** — View plant profiles with photos, general care instructions, and current requirements
3. **Care Planning** — Two distinct tiers:
   - **General Care:** Yearly/seasonal plan based on plant species (fixed reference data)
   - **Current Care:** Immediate tasks based on photo analysis and time elapsed (dynamic, user-driven)

## Common Patterns

### Plant Entity & Lifecycle

```java
// Room entity structure
@Entity(tableName = "plants")
public class PlantEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    
    public String commonName;        // "Tomato" (user-confirmed)
    public String scientificName;    // "Solanum lycopersicum" (AI-identified)
    public String plantFamily;       // "Solanaceae"
    public LocalDate dateAdded;
    public String soilType;          // "loamy", "sandy", "clay"
    public String sunExposure;       // "full sun", "partial shade", "shade"
    public String wateringFrequency; // "every 3 days", "weekly", etc.
    public boolean isIndoor;
    public String notes;
}

// ViewModel exposes LiveData
public LiveData<List<PlantWithPhotos>> getAllPlants() {
    return plantRepository.getAllPlantsWithPhotos();
}

```

### Photo Workflow

1. **Capture or Select**
   - CameraFragment: capture via `CameraX` or `Intent.ACTION_IMAGE_CAPTURE`
   - GalleryFragment: select from device storage
   - Save to app cache: `getContext().getCacheDir()`

2. **Process Before Upload**
   ```java
   // Strip EXIF (privacy), compress to <2MB
   Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
   bitmap = compressBitmap(bitmap, maxWidth=800, maxHeight=800);
   // Remove EXIF metadata via ExifInterface
   ```

3. **Send to Gemini AI**
   ```java
   // GeminiAIService.identifyPlant(bitmap) → AnalysisResult
   public AnalysisResult identifyPlant(Bitmap bitmap) {
       // Encode bitmap to base64
       // Call Gemini API with prompt:
       // "Identify this plant. Return JSON: {commonName, scientificName, family, careNotes}"
       // Parse response, handle errors (rate limit, auth, timeout)
   }
   ```

4. **Store Photo + Metadata**
   ```java
   PhotoEntity photo = new PhotoEntity();
   photo.plantId = plant.id;
   photo.photoPath = "/storage/...";
   photo.capturedAt = LocalDateTime.now();
   photo.healthAnalysis = "Healthy, well-watered";
   photoDao.insert(photo);
   ```

### AI Integration (Gemini)

**Setup:**
- Gemini API key in `local.properties`: `GEMINI_API_KEY=...`
- Never hardcode or commit keys; read from `BuildConfig` at runtime
- Use Google's Generative AI SDK: `com.google.ai:generativeai`

**Prompts:**

```java
// Plant Identification
String prompt = "Identify this plant in the photo. Return a JSON object with: " +
    "commonName (string), scientificName (string), family (string), " +
    "careNotes (string, 2-3 sentences), wateringFrequency (string), " +
    "sunRequirements (string), toxicity (boolean).";

// Health Analysis (after care period)
String prompt = "Analyze the health of this plant in the photo. " +
    "Return JSON: {overallHealth (healthy/caution/poor), " +
    "issues (array of strings), suggestions (array of strings), " +
    "nextActionSuggested (string)}.";
```

**Error Handling:**
```java
try {
    response = geminiService.call(prompt, bitmap);
} catch (RateLimitException e) {
    // Retry with exponential backoff
    showUserMessage("Request limit reached. Retry in 30 seconds.");
} catch (InvalidApiKeyException e) {
    // Check local.properties
    showUserMessage("Configuration error. Contact support.");
} catch (TimeoutException e) {
    // Network issue
    showUserMessage("Connection timeout. Please try again.");
}
```

### Care Scheduling: General vs. Current

```java
// GENERAL CARE PLAN - Yearly/Seasonal Reference
@Entity(tableName = "care_plans")
public class CarePlanEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    
    @ForeignKey(entity = PlantEntity.class, parentColumns = "id", childColumns = "plantId")
    public long plantId;
    
    public String seasonOrMonth;      // "spring", "summer", "fall", "winter" OR "january", "february"...
    public String taskType;           // "prune", "fertilize", "repot", "propagate"
    public String description;        // "Cut back stems for bushy growth"
    public String frequency;          // "once per season" or "weekly in summer"
    // Fixed reference data - same for all tomato plants in spring
}

// CURRENT CARE TASKS - Dynamic, Immediate Actions
@Entity(tableName = "care_tasks")
public class CareTaskEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    
    @ForeignKey(entity = PlantEntity.class, parentColumns = "id", childColumns = "plantId")
    public long plantId;
    
    public String taskType;           // "water", "fertilize", "prune", "check-pests"
    public String reason;             // "soil is dry" or "detected yellowing leaves" or "fertilizer schedule"
    public LocalDateTime suggestedDate; // NOW or specific future date
    public boolean isCompleted;
    public LocalDateTime completedDate;
    public String notes;              // User notes after completion
}

// Generate General Care Plan when plant is created
public List<CarePlanEntity> generateGeneralCarePlan(PlantEntity plant) {
    // Look up plant species database
    // Return yearly schedule for this species (e.g., tomato has specific pruning, fertilizing, harvesting times)
    // This is static reference data per plant type
}

// Generate Current Care Tasks on-demand (when user views plant or uploads photo)
public List<CareTaskEntity> generateCurrentTasks(PlantEntity plant, List<PhotoEntity> recentPhotos) {
    // 1. Get last 7 days of photos
    // 2. Send latest photo to Gemini AI: "Analyze health. Suggest immediate actions."
    // 3. Parse response: check for wilting, yellowing, pests, overgrowth
    // 4. Generate current tasks based on:
    //    - AI photo analysis (detected issues)
    //    - Time since last care (e.g., "water if >3 days since last watering")
    //    - General care plan (e.g., "fertilize in June if summer plant")
    // 5. Return list of immediate tasks to display
}

// User marks task complete
public void completeCurrentTask(CareTaskEntity task) {
    task.isCompleted = true;
    task.completedDate = LocalDateTime.now();
    taskDao.update(task);
    // Current tasks do NOT recur - they are completed or discarded
}
```

**Key Distinction:**
- **General Care:** Reference data, never changes for a given plant species, shown as educational/planning tool
- **Current Care:** Generated on-demand, AI-powered, user-driven, tied to plant health state

### ViewModel Lifecycle & State

```java
public class PlantViewModel extends AndroidViewModel {
    private final PlantRepository repository;
    public MutableLiveData<Plant> selectedPlant = new MutableLiveData<>();
    public LiveData<List<PlantPhoto>> plantPhotos;
    
    public PlantViewModel(Application app) {
        super(app);
        repository = new PlantRepository(app);
        // DO NOT restore from savedInstanceState; use repository
    }
    
    public void selectPlant(long plantId) {
        selectedPlant.setValue(repository.getPlant(plantId));
    }
    
    public void addPhoto(Bitmap bitmap) {
        // Coroutine to avoid main thread
        viewModelScope.launch(Dispatchers.Default, () -> {
            String path = savePhotoToStorage(bitmap);
            PhotoEntity photo = new PhotoEntity(/* ... */);
            repository.addPhoto(photo);
        });
    }
}
```

### Navigation & Fragment Transitions

```xml
<!-- nav_graph.xml structure -->
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/nav_graph"
    app:startDestination="@id/gardenFragment">
    
    <fragment android:id="@+id/gardenFragment"
        android:name="com.gersseba.garden.fragment.GardenFragment">
        <action android:id="@+id/action_garden_to_plant_detail"
            app:destination="@id/plantDetailFragment" />
    </fragment>
    
    <fragment android:id="@+id/plantDetailFragment"
        android:name="com.gersseba.garden.fragment.PlantDetailFragment">
        <argument android:name="plantId" app:argType="long" />
    </fragment>
    
</navigation>
```

Pass data via safe args:
```java
Bundle args = new Bundle();
args.putLong("plantId", plant.id);
navController.navigate(R.id.plantDetailFragment, args);
```

### Fragment UI Pattern

```java
public class PlantDetailFragment extends Fragment {
    private PlantDetailBinding binding;
    private PlantViewModel viewModel;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = PlantDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        viewModel = new ViewModelProvider(this).get(PlantViewModel.class);
        
        // Get arguments (plant ID)
        long plantId = getArguments().getLong("plantId");
        
        // Observe ViewModel
        viewModel.getPlant(plantId).observe(getViewLifecycleOwner(), plant -> {
            binding.plantName.setText(plant.commonName);
            binding.plantFamily.setText(plant.family);
            // ... update UI
        });
        
        // Handle user actions
        binding.addPhotoButton.setOnClickListener(v -> {
            navController.navigate(R.id.cameraFragment);
        });
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
```

## Testing Patterns

### Unit Test — ViewModel

```java
@Test
public void addNewPlant_savesToRepository() {
    PlantViewModel viewModel = new PlantViewModel(app);
    Plant plant = new Plant("Tomato", "Solanum lycopersicum", ...);
    
    viewModel.addPlant(plant);
    
    assertEquals(1, repository.getAllPlants().size());
}
```

### Instrumentation Test — Fragment

```java
@RunWith(AndroidJUnit4.class)
public class PlantDetailFragmentTest {
    
    @Rule
    public ActivityScenarioRule<MainActivity> rule = 
        new ActivityScenarioRule<>(MainActivity.class);
    
    @Test
    public void photoButtonClick_navigatesToCamera() {
        // Setup
        onView(withId(R.id.add_photo_button)).perform(click());
        
        // Verify navigation
        onView(withId(R.id.camera_fragment)).check(matches(isDisplayed()));
    }
}
```

### Mock AI Calls

```java
@Test
public void identifyPlant_returnsSpecies() {
    // Mock Gemini API response
    AnalysisResult mockResult = new AnalysisResult(
        "Tomato", "Solanum lycopersicum", "Solanaceae", "..."
    );
    when(geminiService.identifyPlant(any())).thenReturn(mockResult);
    
    AnalysisResult result = geminiService.identifyPlant(testBitmap);
    
    assertEquals("Tomato", result.commonName);
}
```

## Accessibility Checklist

- [ ] All images have `android:contentDescription` in XML
- [ ] All interactive elements are keyboard-accessible
- [ ] Text contrast ratio >= 4.5:1 (WCAG AA)
- [ ] Button minimum size 48x48 dp
- [ ] RecyclerView items are keyboard-focusable
- [ ] TalkBack labels are concise and descriptive
- [ ] Spinners/pickers have proper `android:hint`

## Security Checklist

- [ ] Gemini API key not in source code (use `local.properties`)
- [ ] Photos stripped of EXIF before sending to AI
- [ ] API calls made over HTTPS
- [ ] User consent for network access is explicit
- [ ] Database queries properly parametrized (Room handles this)
- [ ] No sensitive data in logs



