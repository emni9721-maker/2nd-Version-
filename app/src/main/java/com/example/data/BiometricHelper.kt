package com.example.data

object BiometricHelper {
  const val DEFAULT_PIN = "1234"
  const val DECOY_PIN = "9999"

  enum class AuthResult {
    SUCCESS,
    DECOY_SUCCESS,
    FAILED,
    LOCKED_OUT
  }

  fun verifyPin(pin: String): AuthResult {
    return when (pin) {
      DEFAULT_PIN -> AuthResult.SUCCESS
      DECOY_PIN -> AuthResult.DECOY_SUCCESS
      else -> AuthResult.FAILED
    }
  }

  fun getBiometricCapabilities(): List<String> {
    return listOf(
      "Class 3 Strong Biometrics (Ultrasonic Fingerprint)",
      "3D Structured Light Facial Recognition",
      "Secure Enclave Fallback PIN (4-8 digits)",
      "Decoy Vault Protection (Self-defending)"
    )
  }
}
