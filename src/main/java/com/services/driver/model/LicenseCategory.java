package com.services.driver.model;

/**
 * Categorías de licencia de conducción en Colombia.
 *
 * Servicio PARTICULAR (vigencia por edad):
 *   - < 60 años  → 10 años
 *   - 60–79 años → 5 años
 *   - ≥ 80 años  → 1 año
 *
 * Servicio PÚBLICO (vigencia por edad):
 *   - < 60 años  → 3 años
 *   - ≥ 60 años  → 1 año
 */
public enum LicenseCategory {

    // Servicio PARTICULAR
    A1,
    A2,
    B1,
    B2,
    B3,
    // Servicio PÚBLICO
    C1,
    C2,
    C3;

    /** Determina si la categoría corresponde a servicio público. */
    public boolean isPublicService() {
        return this == C1 || this == C2 || this == C3;
    }

    /**
     * Calcula los años máximos de vigencia permitidos
     * según la edad del conductor en el momento de expedir la licencia.
     *
     * @param ageAtIssue edad del conductor en la fecha de expedición
     * @return número máximo de años de vigencia
     */
    public int maxValidityYears(int ageAtIssue) {
        if (isPublicService()) {
            return ageAtIssue < 60 ? 3 : 1;
        } else {
            if (ageAtIssue < 60) return 10;
            if (ageAtIssue < 80) return 5;
            return 1;
        }
    }
}