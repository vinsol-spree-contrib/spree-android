package com.vinsol.spree.viewhelpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vinsol.spree.R;
import com.vinsol.spree.SpreeApplication;
import com.vinsol.spree.models.LineItem;
import com.vinsol.spree.utils.Log;


/**
 * Created by vaibhav on 12/29/15.
 */
public class LineItemViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    private CartLineItemActionsInterface actionsInterface;
    private ImageView img;
    private TextView name, price, quantity, outOfStock;
    private TextView editQuantity;
    private TextView remove;

    public LineItemViewHolder(Context context, CartLineItemActionsInterface actionsInterface, View itemView) {
        super(itemView);
        this.context = context;
        this.actionsInterface = actionsInterface;
        img         = (ImageView) itemView.findViewById(R.id.line_items_list_view_item_img);
        name        = (TextView)  itemView.findViewById(R.id.line_items_list_view_item_name);
        price       = (TextView)  itemView.findViewById(R.id.line_items_list_view_item_price);
        quantity    = (TextView)  itemView.findViewById(R.id.line_items_list_view_item_qty_txt);
        editQuantity= (TextView)  itemView.findViewById(R.id.line_items_list_view_edit_quantity);
        remove      = (TextView)  itemView.findViewById(R.id.line_items_list_view_remove);
        outOfStock  = (TextView)  itemView.findViewById(R.id.line_items_list_view_item_out_of_stock);
    }

    public void setup(final LineItem lineItem) {
        quantity.setText("Qty. " + lineItem.getQuantity());
        name.setText(lineItem.getVariant().getName());
        price.setText("$ " + lineItem.getTotal());
        if (lineItem.getVariant().getImages()!=null && !lineItem.getVariant().getImages().isEmpty())
            Picasso.with(SpreeApplication.getContext()).load(lineItem.getVariant().getImages().get(0).getProductUrl()).into(img);
        if (lineItem.isInsufficientStock()) outOfStock.setVisibility(View.VISIBLE);

        editQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lineItem.isInsufficientStock()) {
                    Toast.makeText(SpreeApplication.getContext(), "Item out of stock", Toast.LENGTH_SHORT).show();
                    return;
                }

                int maxQuantityForPurchase = lineItem.getVariant().getStockOnHand();
                if(lineItem.getVariant().getIsBackOrderable() && maxQuantityForPurchase <= 0) {
                    maxQuantityForPurchase = 10; // limiting the quantity to 10 if product is back-orderable and server is not telling the quantity
                }

                CharSequence[] quantityList = new CharSequence[maxQuantityForPurchase];

                for (int i = 0; i < maxQuantityForPurchase; i++) {
                    quantityList[i] = String.valueOf(i+1);
                    Log.d("qty list : " + quantityList[i]);
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.edit_quantity))
                        .setSingleChoiceItems(quantityList, lineItem.getQuantity() - 1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                lineItem.setQuantity(which + 1);
                                actionsInterface.editQuantity(lineItem);
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionsInterface.removeLineItem(lineItem);
            }
        });
    }
}

