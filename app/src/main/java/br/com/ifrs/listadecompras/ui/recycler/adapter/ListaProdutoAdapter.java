package br.com.ifrs.listadecompras.ui.recycler.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import br.com.ifrs.listadecompras.R;
import br.com.ifrs.listadecompras.dao.AppDatabase;
import br.com.ifrs.listadecompras.dao.ProdutoDAO;
import br.com.ifrs.listadecompras.model.Produto;

public class ListaProdutoAdapter extends RecyclerView.Adapter<ListaProdutoAdapter.ProdutoViewHolder> {

    private final List<Produto> produtos;
    Context context;

    public ListaProdutoAdapter(List<Produto> produtos, Context context) {
        this.produtos = produtos;
        this.context = context;
    }

    public void adicionaProduto(Produto produto) {
        produtos.add(produto);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListaProdutoAdapter.ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewLinhasProdutos = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item_produto, parent, false);
        return new ProdutoViewHolder(viewLinhasProdutos);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaProdutoAdapter.ProdutoViewHolder produtoViewHolder, int position) {
        Produto produto = produtos.get(position);
        produtoViewHolder.nomeProduto.setText(produto.getNome());
        produtoViewHolder.precoProduto.setText(String.valueOf(produto.getValor())) ;
        produtoViewHolder.quantidadeProduto.setText(String.valueOf(produto.getQuantidade()));
        produtoViewHolder.marcaProduto.setText(produto.getMarca());

        ImageButton btnExcluir = produtoViewHolder.itemView.findViewById(R.id.imgBtnExcluir);
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                produtos.remove(produto);
                notifyDataSetChanged();
            }
        });

        ImageButton btnEditar = produtoViewHolder.itemView.findViewById(R.id.imgBtnEditar);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir um diálogo de edição aqui
                abrirDialogoEdicao(produto);
            }
        });


    }

    @Override
    public int getItemCount() {
        return produtos.size();
    }

    public class ProdutoViewHolder extends RecyclerView.ViewHolder {
        TextView nomeProduto;
        TextView precoProduto;
        TextView quantidadeProduto;
        TextView marcaProduto;

        public ProdutoViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeProduto = itemView.findViewById(R.id.textViewNomeProduto);
            precoProduto = itemView.findViewById(R.id.textViewValorPreco);
            quantidadeProduto = itemView.findViewById(R.id.textViewValorQuantidade);
            marcaProduto = itemView.findViewById(R.id.textViewValorMarca);
        }

    }

    private void abrirDialogoEdicao(Produto produto) {
        //cria o dialogo conforme o layout
        View dialogView = LayoutInflater.from(context).inflate(R.layout.activity_editar_produto_dialog, null);

        // atribui os campos de texto do layout a variáveis
        TextInputLayout campoNomeProduto = dialogView.findViewById(R.id.formulario_edit_produto_nome);
        TextInputLayout campoQuantidadeProduto = dialogView.findViewById(R.id.formulario_edit_produto_quantidade);
        TextInputLayout campoMarcaProduto = dialogView.findViewById(R.id.formulario_edit_produto_marca);
        TextInputLayout campoPrecoProduto = dialogView.findViewById(R.id.formulario_edit_produto_preco);

        campoNomeProduto.getEditText().setText(produto.getNome());
        campoQuantidadeProduto.getEditText().setText(String.valueOf(produto.getQuantidade()));
        campoMarcaProduto.getEditText().setText(produto.getMarca());
        campoPrecoProduto.getEditText().setText(String.valueOf(produto.getValor()));

        // Criar o AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Configurar o botão de salvar no diálogo
        Button btnEditar = dialogView.findViewById(R.id.btnEditar);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obter os novos valores dos campos de texto
                String novoNomeProduto = campoNomeProduto.getEditText().getText().toString();
                int novaQuantidadeProduto = Integer.parseInt(campoQuantidadeProduto.getEditText().getText().toString());
                String novaMarcaProduto = campoMarcaProduto.getEditText().getText().toString();
                double novoPrecoProduto = Double.parseDouble(campoPrecoProduto.getEditText().getText().toString());


                // Atualizar o objeto Produto correspondente na lista de produtos
                produto.setNome(novoNomeProduto);
                produto.setQuantidade(novaQuantidadeProduto);
                produto.setMarca(novaMarcaProduto);
                produto.setValor(novoPrecoProduto);

                ProdutoDAO dao = AppDatabase.getInstance(context).createProdutoDAO();
                dao.edita(produto);


                notifyDataSetChanged();

                // Fechar o diálogo
                dialog.dismiss();
            }
        });

        Button btnCancelar = dialogView.findViewById(R.id.btnEditCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}