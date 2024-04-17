package br.com.ifrs.listadecompras.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import br.com.ifrs.listadecompras.R;
import br.com.ifrs.listadecompras.model.Produto;
import br.com.ifrs.listadecompras.ui.recycler.adapter.ListaProdutoAdapter;

public class ListaProdutosActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lista_produtos);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView listaProdutosRecycleView = findViewById(R.id.listRecyclerViewListaCompras);
        
        ListaProdutoAdapter adapter = new ListaProdutoAdapter(this);
        listaProdutosRecycleView.setAdapter(adapter);
        listaProdutosRecycleView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listaProdutosRecycleView.setLayoutManager(layoutManager);

        // botao flutuante
        FloatingActionButton fabAdicionaProduto = findViewById(R.id.fabAddProduto);
        fabAdicionaProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflar o layout do diálogo
                View dialogView = getLayoutInflater().inflate(R.layout.activity_adiciona_produto_dialog, null);

                // Criar o AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(ListaProdutosActivity.this);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                // Configurar o botão de adicionar
                Button btnAdicionar = dialogView.findViewById(R.id.btnAdicionar);
                btnAdicionar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Obter referências para os campos de entrada de texto
                        TextInputLayout campoNomeProduto = dialogView.findViewById(R.id.textInputAddNomeProduto);
                        TextInputLayout campoQuantidadeProduto = dialogView.findViewById(R.id.textInputAddQtdeProduto);
                        TextInputLayout campoMarcaProduto = dialogView.findViewById(R.id.textInputAddMarcaProduto);
                        TextInputLayout campoPrecoProduto = dialogView.findViewById(R.id.textInputAddPrecoProduto);

                        // Verificar se os campos estão preenchidos
                        if (camposPreenchidos(campoNomeProduto, campoPrecoProduto, campoQuantidadeProduto, campoMarcaProduto)) {
                            // Extrair os valores dos campos
                            String nomeProduto = campoNomeProduto.getEditText().getText().toString();
                            int quantidadeProduto = Integer.parseInt(campoQuantidadeProduto.getEditText().getText().toString());
                            String marcaProduto = campoMarcaProduto.getEditText().getText().toString();
                            double precoProduto = Double.parseDouble(campoPrecoProduto.getEditText().getText().toString());

                            Produto novoProduto = new Produto(nomeProduto, quantidadeProduto, marcaProduto, precoProduto);

                            adapter.adicionaProduto(novoProduto);
                            dialog.dismiss();
                        }
                    }
                });

                // Configurar o botão de cancelar
                Button btnCancelar = dialogView.findViewById(R.id.btnCancelarAdicaoProduto);
                btnCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    // Método auxiliar para verificar se os campos de entrada de texto estão preenchidos
    private boolean camposPreenchidos(TextInputLayout... campos) {
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
