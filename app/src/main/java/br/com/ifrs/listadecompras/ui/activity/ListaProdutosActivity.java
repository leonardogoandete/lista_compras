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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import br.com.ifrs.listadecompras.R;
import br.com.ifrs.listadecompras.model.Produto;
import br.com.ifrs.listadecompras.ui.recycler.adapter.ListaProdutoAdapter;
import br.com.ifrs.listadecompras.utils.ValidaFormularioProduto;

public class ListaProdutosActivity extends AppCompatActivity {

    RecyclerView listaProdutosRecycleView;
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

        listaProdutosRecycleView = findViewById(R.id.listRecyclerViewListaCompras);
        
        ListaProdutoAdapter adapter = new ListaProdutoAdapter(this,Produto.inicializaListaProdutos());
        listaProdutosRecycleView.setAdapter(adapter);
        listaProdutosRecycleView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        listaProdutosRecycleView.setLayoutManager(layoutManager);

        // botao flutuante
        FloatingActionButton fabAdicionaProduto = findViewById(R.id.fabAddProduto);
        fabAdicionaProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = getLayoutInflater().inflate(R.layout.activity_adiciona_produto_dialog, null);
                Snackbar snackbar = Snackbar.make(v, R.string.txtSnackSucessoAddProdutoMsg, Snackbar.LENGTH_SHORT);

                // Criar o AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(ListaProdutosActivity.this);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                // Configurar o botão de adicionar
                Button btnAdicionar = dialogView.findViewById(R.id.btnAdicionar);
                configuraAcaoClickBtnAdicionar(btnAdicionar, dialogView, snackbar, dialog);

                // Configurar o botão de cancelar
                Button btnCancelar = dialogView.findViewById(R.id.btnCancelarAdicaoProduto);
                btnCancelar.setOnClickListener(viewBtnCancelar -> dialog.dismiss());
                dialog.show();
            }

            private void configuraAcaoClickBtnAdicionar(Button btnAdicionar, View dialogView, Snackbar snackbar, AlertDialog dialog) {
                btnAdicionar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ValidaFormularioProduto validador = new ValidaFormularioProduto();

                        // Obter referências para os campos de entrada de texto
                        TextInputLayout campoNomeProduto = dialogView.findViewById(R.id.textInputAddNomeProduto);
                        TextInputLayout campoQuantidadeProduto = dialogView.findViewById(R.id.textInputAddQtdeProduto);
                        TextInputLayout campoMarcaProduto = dialogView.findViewById(R.id.textInputAddMarcaProduto);
                        TextInputLayout campoPrecoProduto = dialogView.findViewById(R.id.textInputAddPrecoProduto);
                        // Verificar se os campos estão preenchidos

                        if (validador.camposPreenchidos(campoNomeProduto, campoPrecoProduto, campoQuantidadeProduto, campoMarcaProduto)) {
                            // Extrair os valores dos campos
                            String nomeProduto = campoNomeProduto.getEditText().getText().toString();
                            int quantidadeProduto = Integer.parseInt(campoQuantidadeProduto.getEditText().getText().toString());
                            String marcaProduto = campoMarcaProduto.getEditText().getText().toString();
                            double precoProduto = Double.parseDouble(campoPrecoProduto.getEditText().getText().toString());

                            adapter.adicionaProduto(new Produto(nomeProduto, quantidadeProduto, marcaProduto, precoProduto));
                            snackbar.show();
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
    }
}
