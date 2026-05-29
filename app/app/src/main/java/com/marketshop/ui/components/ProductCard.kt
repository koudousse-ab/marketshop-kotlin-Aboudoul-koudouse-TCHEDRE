package com.marketshop.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.marketshop.data.models.Product
import com.marketshop.data.models.Rating
import com.marketshop.ui.theme.MarketShopTheme

@Composable
fun ProductCard(product: Product, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth().clickable { onClick() }, elevation = CardDefaults.cardElevation(2.dp)) {
        Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = product.image,
                contentDescription = product.title,
                modifier = Modifier.fillMaxWidth().height(150.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(Modifier.height(8.dp))
            Text(product.title, style = MaterialTheme.typography.bodyMedium, maxLines = 2)
            Text("${product.price} €", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Text(product.category, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProductCard() {
    MarketShopTheme {
        ProductCard(
            product = Product(1, "T-shirt", 19.99, "Super t-shirt", "vêtements", "https://fakestoreapi.com/img/...", Rating(4.5, 10)),
            onClick = {}
        )
    }
}
