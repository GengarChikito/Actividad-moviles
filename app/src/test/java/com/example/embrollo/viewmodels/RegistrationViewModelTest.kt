package com.example.embrollo.viewmodels

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.Assert.*
import org.junit.Before
import org.junit.After
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegistrationViewModelTest {

    private lateinit var viewModel: RegistrationViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher) // Setup coroutines xD
        viewModel = RegistrationViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Cleanup coroutines xD
    }

    @Test
    fun validarFormularioVacioDebeMostrarErrores() = runTest {
        viewModel.registerUser()
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertFalse(state.registrationSuccess)

        assertNotNull(state.termsError)
        assertEquals("Debes aceptar los términos y condiciones.", state.termsError)

        assertEquals("El nombre es obligatorio", state.nameError)
        assertNotNull(state.emailError)
        assertNotNull(state.phoneError)
        assertNotNull(state.birthDateError)

        assertEquals("Por favor, corrige los errores del formulario.", state.generalError)
    }

    @Test
    fun validarEmailInvalidoDebeMostrarError() = runTest {
        viewModel.updateName("Test User")
        viewModel.updateEmail("invalid-email")
        viewModel.updatePhone("123456789")
        viewModel.updateBirthDate("01/01/2000")
        viewModel.updatePassword("password")
        viewModel.updateConfirmPassword("password")
        viewModel.updateTermsAccepted(true)

        viewModel.registerUser()
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertNotNull(state.emailError)
        assertEquals("Formato de email incorrecto", state.emailError)
        assertNull(state.termsError)
    }

    @Test
    fun validarContrasenasNoCoincidentesDebeMostrarError() = runTest {
        viewModel.updateName("Test User")
        viewModel.updateEmail("test@example.com")
        viewModel.updatePhone("123456789")
        viewModel.updateBirthDate("01/01/2000")
        viewModel.updatePassword("password")
        viewModel.updateConfirmPassword("different_password")
        viewModel.updateTermsAccepted(true)

        viewModel.registerUser()
        advanceUntilIdle()

        val state = viewModel.uiState.value

        assertNotNull(state.confirmPasswordError)
        assertEquals("Las contraseñas no coinciden", state.confirmPasswordError)
        assertNull(state.termsError)
    }

    @Test
    fun validarRegistroCompletoDebeSerExitoso() = runTest {
        val name = "Test User"
        val email = "test@example.com"
        val phone = "123456789"
        val date = "01/01/2000"
        val password = "securepassword"

        viewModel.updateName(name)
        viewModel.updateEmail(email)
        viewModel.updatePhone(phone)
        viewModel.updateBirthDate(date)
        viewModel.updatePassword(password)
        viewModel.updateConfirmPassword(password)
        viewModel.updateTermsAccepted(true)

        viewModel.registerUser()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        val user = viewModel.registeredUser.value

        assertFalse(state.isLoading)
        assertTrue(state.registrationSuccess)
        assertNull(state.generalError)
        assertNotNull(user)
        assertNull(state.termsError)
        assertEquals(name, user?.name)
    }
}