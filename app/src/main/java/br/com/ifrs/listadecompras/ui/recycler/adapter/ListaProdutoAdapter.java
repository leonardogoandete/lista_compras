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

import com.google.android.material.snackbar.Snackbar;
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
                ProdutoDAO dao = AppDatabase.getInstance(context).createProdutoDAO();
                try {
                    produtos.remove(produto);
                    dao.remove(produto);
                    Snackbar snackbar = Snackbar.make(v, context.getResources().getText(R.string.txtSnackSucessoDelProdutoMsg), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }catch (Exception e){
                    Snackbar snackbar = Snackbar.make(v, context.getResources().getText(R.string.txtSnackErroDelProdutoMsg), Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                notifyDataSetChanged();
            }
        });

        ImageButton btnEditar = produtoViewHolder.itemView.findViewById(R.id.imgBtnEditar);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackbar = Snackbar.make(v, context.getResources().getText(R.string.txtSnackSucessoEditProdutoMsg), Snackbar.LENGTH_SHORT);
                String txtMsgErro = context.getResources().getString(R.string.txtSnackErroEditProdutoMsg);
                String txtMsgSucesso = context.getResources().getString(R.string.txtSnackSucessoEditProdutoMsg);
                abrirDialogoEdicao(produto, snackbar, txtMsgErro, txtMsgSucesso);
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

    private void abrirDialogoEdicao(Produto produto, Snackbar snackbar, String txtMsgErro, String txtMsgSucesso) {
        // Cria o dialogo conforme o layout
        View dialogView = LayoutInflater.from(context).inflate(R.layout.activity_editar_produto_dialog, null);

        // Atribui os campos de texto do layout a variáveis
        TextInputLayout campoNomeProduto = dialogView.findViewById(R.id.textInputEditarNomeProduto);
        TextInputLayout campoQuantidadeProduto = dialogView.findViewById(R.id.textInputEditarQtdeProduto);
        TextInputLayout campoMarcaProduto = dialogView.findViewById(R.id.textInputEditarMarcaProduto);
        TextInputLayout campoPrecoProduto = dialogView.findViewById(R.id.textInputEditarPrecoProduto);

        campoNomeProduto.getEditText().setText(produto.getNome());
        campoQuantidadeProduto.getEditText().setText(String.valueOf(produto.getQuantidade()));
        campoMarcaProduto.getEditText().setText(produto.getMarca());
        campoPrecoProduto.getEditText().setText(String.valueOf(produto.getValor()));

        // Cria o AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        // Configura o botão de salvar no diálogo
        Button btnEditar = dialogView.findViewById(R.id.btnEditarProduto);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtém os novos valores dos campos de texto
                String novoNomeProduto = campoNomeProduto.getEditText().getText().toString();
                int novaQuantidadeProduto = Integer.parseInt(campoQuantidadeProduto.getEditText().getText().toString());
                String novaMarcaProduto = campoMarcaProduto.getEditText().getText().toString();
                double novoPrecoProduto = Double.parseDouble(campoPrecoProduto.getEditText().getText().toString());

                // Atualiza o objeto Produto correspondente na lista de produtos
                produto.setNome(novoNomeProduto);
                produto.setQuantidade(novaQuantidadeProduto);
                produto.setMarca(novaMarcaProduto);
                produto.setValor(novoPrecoProduto);

                ProdutoDAO dao = AppDatabase.getInstance(context).createProdutoDAO();
                // Edita o produto no banco de dados
                try {
                    dao.edita(produto);
                    snackbar.setText(txtMsgSucesso);
                    snackbar.show();
                    notifyDataSetChanged();
                } catch (Exception e) {
                    snackbar.setText(txtMsgErro);
                    snackbar.show();
                }

                // Notifica o adapter que os dados foram alterados
                notifyDataSetChanged();

                // Fecha o diálogo de edição
                dialog.dismiss();
            }
        });

        // Configura o botão de cancelar no diálogo
        Button btnCancelar = dialogView.findViewById(R.id.btnCancelarEdicaoProduto);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // Mostra o diálogo de edição
        dialog.show();
    }
}
