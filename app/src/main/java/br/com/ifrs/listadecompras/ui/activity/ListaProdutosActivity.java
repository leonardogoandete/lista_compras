package br.com.ifrs.listadecompras.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import br.com.ifrs.listadecompras.R;
import br.com.ifrs.listadecompras.dao.AppDatabase;
import br.com.ifrs.listadecompras.dao.ProdutoDAO;
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

        ProdutoDAO dao = AppDatabase.getInstance(getApplicationContext()).createProdutoDAO();
        List<Produto> produtos = dao.todos();
        RecyclerView listaProdutosRecycleView = findViewById(R.id.listRecyclerViewListaCompras);

        ListaProdutoAdapter adapter = new ListaProdutoAdapter(produtos, this);
        listaProdutosRecycleView.setAdapter(adapter);
        listaProdutosRecycleView.setHasFixedSize(true);
        listaProdutosRecycleView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
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
                        if (camposPreenchidos(campoNomeProduto, campoQuantidadeProduto, campoMarcaProduto, campoPrecoProduto)) {
                            // Extrair os valores dos campos
                            String nomeProduto = campoNomeProduto.getEditText().getText().toString();
                            int quantidadeProduto = Integer.parseInt(campoQuantidadeProduto.getEditText().getText().toString());
                            String marcaProduto = campoMarcaProduto.getEditText().getText().toString();
                            double precoProduto = Double.parseDouble(campoPrecoProduto.getEditText().getText().toString());

                            // Criar um novo objeto Produto
                            Produto novoProduto = new Produto(nomeProduto, quantidadeProduto, marcaProduto, precoProduto);

                            // Adicionar o novo produto à lista
                            dao.salva(novoProduto);

                            // Fechar o diálogo
                            dialog.dismiss();

                            // Atualizar a lista de produtos na RecyclerView (se necessário)
                            adapter.adicionaProduto(novoProduto);


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
