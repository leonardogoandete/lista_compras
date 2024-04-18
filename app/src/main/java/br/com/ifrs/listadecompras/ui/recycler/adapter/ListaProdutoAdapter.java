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

import java.util.ArrayList;
import java.util.List;

import br.com.ifrs.listadecompras.R;
import br.com.ifrs.listadecompras.model.Produto;

public class ListaProdutoAdapter extends RecyclerView.Adapter<ListaProdutoAdapter.ProdutoViewHolder> {
    private Context context;
    List<Produto> produtos = new ArrayList<>();

    public ListaProdutoAdapter(Context context, List<Produto> produtos) {
        this.produtos = produtos;
        this.context = context;
    }

    @NonNull
    @Override
    public ListaProdutoAdapter.ProdutoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View itemList = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_item_produto, viewGroup, false);
        return new ProdutoViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull ListaProdutoAdapter.ProdutoViewHolder produtoViewHolder, int position) {
        Produto produto = produtos.get(position);
        produtoViewHolder.nomeProduto.setText(produto.getNome());
        produtoViewHolder.precoProduto.setText(String.valueOf(produto.getValor()));
        produtoViewHolder.quantidadeProduto.setText(String.valueOf(produto.getQuantidade()));
        produtoViewHolder.marcaProduto.setText(produto.getMarca());

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

            ImageButton btnEditar = itemView.findViewById(R.id.imgBtnEditar);
            btnEditar.setOnClickListener(viewBtnEditar -> {
                Produto produto = produtos.get(getAdapterPosition());
                abrirDialogoEdicao(produto, viewBtnEditar);
            });


            ImageButton btnExcluir = itemView.findViewById(R.id.imgBtnExcluir);
            btnExcluir.setOnClickListener(viewBtnExcluir -> {
                try {
                    produtos.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    Snackbar.make(viewBtnExcluir, R.string.txtSnackSucessoDelProdutoMsg, Snackbar.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Snackbar.make(viewBtnExcluir, R.string.txtSnackErroDelProdutoMsg, Snackbar.LENGTH_SHORT).show();
                }
            });
        }

        private void abrirDialogoEdicao(Produto produto, View viewBtnEditar) {
            View dialogView = LayoutInflater.from(context).inflate(R.layout.activity_editar_produto_dialog, null);
            TextInputLayout campoNomeProduto = dialogView.findViewById(R.id.textInputEditarNomeProduto);
            TextInputLayout campoQuantidadeProduto = dialogView.findViewById(R.id.textInputEditarQtdeProduto);
            TextInputLayout campoMarcaProduto = dialogView.findViewById(R.id.textInputEditarMarcaProduto);
            TextInputLayout campoPrecoProduto = dialogView.findViewById(R.id.textInputEditarPrecoProduto);

            campoNomeProduto.getEditText().setText(produto.getNome());
            campoQuantidadeProduto.getEditText().setText(String.valueOf(produto.getQuantidade()));
            campoMarcaProduto.getEditText().setText(produto.getMarca());
            campoPrecoProduto.getEditText().setText(String.valueOf(produto.getValor()));

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(dialogView);
            AlertDialog dialog = builder.create();

            Button btnEditar = dialogView.findViewById(R.id.btnEditarProduto);
            btnEditar.setOnClickListener(viewBtnEditarConfirma -> {
                try{
                    String novoNome = campoNomeProduto.getEditText().getText().toString();
                    int novaQuantidade = Integer.parseInt(campoQuantidadeProduto.getEditText().getText().toString());
                    String novaMarca = campoMarcaProduto.getEditText().getText().toString();
                    double novoPreco = Double.parseDouble(campoPrecoProduto.getEditText().getText().toString());

                    if (novoNome.isEmpty() || novaMarca.isEmpty()) {
                        dialog.dismiss();
                        Snackbar.make(viewBtnEditar, R.string.txtSnackErroEditProdutoMsg, Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    Produto produtoEditado = new Produto(novoNome, novaQuantidade, novaMarca, novoPreco);
                    produtos.set(getAdapterPosition(), produtoEditado);
                    Snackbar.make(viewBtnEditar, R.string.txtSnackSucessoEditProdutoMsg, Snackbar.LENGTH_SHORT).show();
                    // notifica o elemento que foi alterado para os ouvintes
                    notifyItemChanged(getAdapterPosition());

                    dialog.dismiss();
                }catch (Exception e){
                    dialog.dismiss();
                    Snackbar.make(viewBtnEditar, R.string.txtSnackErroEditProdutoMsg, Snackbar.LENGTH_SHORT).show();
                }
            });

            Button btnCancelar = dialogView.findViewById(R.id.btnCancelarEdicaoProduto);
            btnCancelar.setOnClickListener(viewBtnEditarCancela -> dialog.dismiss());
            dialog.show();
        }
    }

    public void adicionaProduto(Produto produto) {
        produtos.add(produto);
        // notifica que foi inserido um produto para os ouvintes
        notifyItemInserted(produtos.size() - 1);
    }
}
