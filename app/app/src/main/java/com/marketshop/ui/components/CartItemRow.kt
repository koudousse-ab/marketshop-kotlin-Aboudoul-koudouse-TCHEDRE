package com.marketshop.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.marketshop.data.models.CartItem
import com.marketshop.ui.theme.MarketShopTheme

@Composable
fun CartItemRow(
    cartItem: CartItem,
    onQuantityChange: (Int) -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(1.dp)) {
        Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = cartItem.image,
                contentDescription = cartItem.title,
                modifier = Modifier.size(60.dp),
                contentScale = ContentScale.Fit
            )
            Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
                Text(cartItem.title, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
                Text("${cartItem.price} €", style = MaterialTheme.typography.bodySmall)
                Text(
                    "Sous-total: ${String.format("%.2f", cartItem.price * cartItem.quantity)} €",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onQuantityChange(cartItem.quantity - 1) }, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Remove, null)
                }
                Text(cartItem.quantity.toString(), style = MaterialTheme.typography.bodyMedium)
                IconButton(onClick = { onQuantityChange(cartItem.quantity + 1) }, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Add, null)
                }
                IconButton(onClick = onRemove, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, null)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCartItemRow() {
    MarketShopTheme {
        CartItemRow(
            cartItem = CartItem(productId = 1, title = "T-shirt", price = 19.99, image = "https://...", quantity = 2),
            onQuantityChange = {},
            onRemove = {}
        )
    }
}
