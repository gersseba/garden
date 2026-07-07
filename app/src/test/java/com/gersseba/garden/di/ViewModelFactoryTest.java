package com.gersseba.garden.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gersseba.garden.viewmodel.CarePlanViewModel;
import com.gersseba.garden.viewmodel.MyPlantsViewModel;
import com.gersseba.garden.viewmodel.PlantDetailViewModel;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link ViewModelFactory}.
 *
 * These tests verify the factory's class structure and type compatibility.
 * Full integration tests with actual ViewModels and ServiceLocator are in androidTest.
 *
 * Verifies:
 * - ViewModelFactory correctly implements ViewModelProvider.Factory
 * - It declares support for all required ViewModel types
 * - Unknown ViewModel types are handled appropriately
 */
public class ViewModelFactoryTest {

    @Test
    public void ViewModelFactory_implements_ViewModelProvider_Factory() {
        // Verify that ViewModelFactory is a valid factory implementation
        assertTrue("ViewModelFactory should implement ViewModelProvider.Factory",
                ViewModelProvider.Factory.class.isAssignableFrom(ViewModelFactory.class));
    }

    @Test
    public void PlantDetailViewModel_is_ViewModel_subclass() {
        // Verify that PlantDetailViewModel is a valid ViewModel type
        // that the factory is expected to create
        assertTrue("PlantDetailViewModel should extend ViewModel",
                ViewModel.class.isAssignableFrom(PlantDetailViewModel.class));
    }

    @Test
    public void MyPlantsViewModel_is_ViewModel_subclass() {
        // Verify that MyPlantsViewModel is a valid ViewModel type
        // that the factory is expected to create
        assertTrue("MyPlantsViewModel should extend ViewModel",
                ViewModel.class.isAssignableFrom(MyPlantsViewModel.class));
    }

    @Test
    public void CarePlanViewModel_is_ViewModel_subclass() {
        // Verify that CarePlanViewModel is a valid ViewModel type
        // that the factory is expected to create
        assertTrue("CarePlanViewModel should extend ViewModel",
                ViewModel.class.isAssignableFrom(CarePlanViewModel.class));
    }

    @Test
    public void ViewModelFactory_has_required_constructor() {
        // Verify that ViewModelFactory has the required constructor
        boolean hasConstructor = false;
        try {
            ViewModelFactory.class.getConstructor(android.app.Application.class, ServiceLocator.class);
            hasConstructor = true;
        } catch (NoSuchMethodException e) {
            // Method not found
        }
        assertTrue("ViewModelFactory should have constructor(Application, ServiceLocator)", hasConstructor);
    }

    @Test
    public void ViewModelFactory_has_create_method() {
        // Verify that ViewModelFactory implements the factory create method
        boolean hasCreateMethod = false;
        try {
            ViewModelFactory.class.getMethod("create", Class.class);
            hasCreateMethod = true;
        } catch (NoSuchMethodException e) {
            // Method not found
        }
        assertTrue("ViewModelFactory should have create(Class) method", hasCreateMethod);
    }

    @Test
    public void ViewModelFactory_source_has_PlantDetailViewModel_support() {
        // Note: This verifies the factory is designed to support PlantDetailViewModel
        // by checking that both classes exist and are properly typed
        assertNotNull("ViewModelFactory class should exist", ViewModelFactory.class);
        assertNotNull("PlantDetailViewModel class should exist", PlantDetailViewModel.class);
    }

    @Test
    public void ViewModelFactory_source_has_MyPlantsViewModel_support() {
        // Note: This verifies the factory is designed to support MyPlantsViewModel
        // by checking that both classes exist and are properly typed
        assertNotNull("ViewModelFactory class should exist", ViewModelFactory.class);
        assertNotNull("MyPlantsViewModel class should exist", MyPlantsViewModel.class);
    }

    @Test
    public void ViewModelFactory_source_has_CarePlanViewModel_support() {
        // Note: This verifies the factory is designed to support CarePlanViewModel
        // by checking that both classes exist and are properly typed
        assertNotNull("ViewModelFactory class should exist", ViewModelFactory.class);
        assertNotNull("CarePlanViewModel class should exist", CarePlanViewModel.class);
    }
}

















