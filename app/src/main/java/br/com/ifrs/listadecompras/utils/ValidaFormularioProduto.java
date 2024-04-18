package br.com.ifrs.listadecompras.utils;

import com.google.android.material.textfield.TextInputLayout;

public class ValidaFormularioProduto{

    public boolean camposPreenchidos(TextInputLayout... campos) {
        for (TextInputLayout campo : campos) {
            String textoCampo = campo.getEditText().getText().toString().trim();
            if (textoCampo.isEmpty()) {
                campo.setError("Campo obrigatório");
                return false;
            } else {
                campo.setError(null);
            }
        }
        return true;
    }
}
