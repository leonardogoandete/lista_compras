package br.com.ifrs.listadecompras.ui.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

import br.com.ifrs.listadecompras.R;
import br.com.ifrs.listadecompras.dao.AppDatabase;
import br.com.ifrs.listadecompras.dao.ProdutoDAO;
import br.com.ifrs.listadecompras.model.Produto;

public class AdicionaProdutoDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adiciona_produto_dialog);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtendo referências para os campos de entrada de texto
        TextInputLayout campoNomeProduto = findViewById(R.id.formulario_add_produto_nome);
        TextInputLayout campoQuantidadeProduto = findViewById(R.id.formulario_add_produto_quantidade);
        TextInputLayout campoMarcaProduto = findViewById(R.id.formulario_add_produto_marca);
        TextInputLayout campoPrecoProduto = findViewById(R.id.formulario_add_produto_preco);

        // Obtendo referência para o botão de adicionar
        Button btnAdicionar = findViewById(R.id.btnAdicionar);
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificando se os campos de entrada de texto estão vazios
                if (camposPreenchidos(campoNomeProduto, campoQuantidadeProduto, campoMarcaProduto, campoPrecoProduto)) {
                    // Convertendo os valores dos campos de entrada de texto para seus tipos correspondentes
                    String nomeProduto = campoNomeProduto.getEditText().getText().toString();
                    int quantidadeProduto = Integer.parseInt(campoQuantidadeProduto.getEditText().getText().toString());
                    String marcaProduto = campoMarcaProduto.getEditText().getText().toString();
                    double precoProduto = Double.parseDouble(campoPrecoProduto.getEditText().getText().toString());

                    // Criando um objeto Produto com os valores obtidos
                    Produto produto = new Produto(nomeProduto, quantidadeProduto, marcaProduto, precoProduto);

                    // Salvando o produto no banco de dados
                    ProdutoDAO dao = AppDatabase.getInstance(getApplicationContext()).createProdutoDAO();
                    dao.salva(produto);

                    // Fechando a atividade (diálogo)
                    finish();
                }
            }
        });
    }

    // Método para verificar se os campos de entrada de texto estão preenchidos
    private boolean camposPreenchidos(TextInputLayout... campos) {
        for (TextInputLayout campo : campos) {
            if (campo.getEditText().getText().toString().trim().isEmpty()) {
                campo.setError("Campo obrigatório");
                return false;
            } else {
                campo.setError(null);
            }
        }
        return true;
    }
}
