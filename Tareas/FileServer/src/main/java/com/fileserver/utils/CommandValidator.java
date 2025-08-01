package com.fileserver.utils;

import com.fileserver.utils.RequestManager.CommandType;

public class CommandValidator {

    public static class ValidationResult {
        private final boolean valid;
        private final String errorMessage;

        private ValidationResult(boolean valid, String errorMessage, Object validatedValue) {
            this.valid = valid;
            this.errorMessage = errorMessage;
        }

        public static ValidationResult success(Object value) {
            return new ValidationResult(true, null, value);
        }

        public static ValidationResult failure(String message) {
            return new ValidationResult(false, message, null);
        }

        // getters
        public boolean isValid() {
            return valid;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

    }

    public static ValidationResult validateOption(int option, int min, int max) {
        if (option < min || option > max) {
            return ValidationResult.failure("Opción fuera de rango. Debe estar entre " + min + " y " + max);
        }
        return ValidationResult.success(option);
    }

    public static ValidationResult validateFileSelection(int choice, String[] files) {
        if (choice < 0 || choice >= files.length) {
            return ValidationResult.failure("Selección de archivo inválida");
        }
        return ValidationResult.success(choice);
    }

    public static ValidationResult validateCommand(String[] parts, CommandType expectedType) {
        if (parts.length == 0) {
            return ValidationResult.failure("Comando vacío");
        }

        try {
            CommandType received = CommandType.valueOf(parts[0]);
            if (received != expectedType) {
                return ValidationResult.failure("Esperaba " + expectedType + " pero recibió " + received);
            }
            return ValidationResult.success(received);
        } catch (IllegalArgumentException e) {
            return ValidationResult.failure("Comando no reconocido: " + parts[0]);
        }
    }

    public static ValidationResult validateFileSize(long fileSize) {
        if (fileSize <= 0) {
            return ValidationResult.failure("Tamaño de archivo inválido: " + fileSize);
        }
        if (fileSize > 100 * 1024 * 1024) { // 100MB
            return ValidationResult.failure("Archivo demasiado grande (máx 100MB)");
        }
        return ValidationResult.success(fileSize);
    }
}