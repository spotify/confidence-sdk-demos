package com.example.demo.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.demo.data.SubscriptionPlan

@Composable
fun SubscriptionCard(
    plan: SubscriptionPlan,
    modifier: Modifier = Modifier,
    onSelectPlan: () -> Unit = {},
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .height(350.dp),
        shape = RoundedCornerShape(16.dp),
        border =
            if (plan.isHighlighted) {
                BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            } else {
                BorderStroke(1.dp, Color.Gray.copy(alpha = 0.3f))
            },
        elevation =
            CardDefaults.cardElevation(
                defaultElevation = if (plan.isHighlighted) 8.dp else 4.dp,
            ),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Most Popular Badge
            if (plan.isHighlighted) {
                Box(
                    modifier =
                        Modifier
                            .align(Alignment.TopCenter)
                            .offset(y = (-12).dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(12.dp),
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = "Most Popular",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }

            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Plan Name
                Text(
                    text = plan.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = if (plan.isHighlighted) 8.dp else 0.dp),
                )

                // Price
                Text(
                    text = plan.price,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 8.dp),
                )

                // Features List
                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    plan.features.forEach { feature ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp),
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = feature,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }

                // Action Button
                Button(
                    onClick = onSelectPlan,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                    colors =
                        if (plan.isHighlighted) {
                            ButtonDefaults.buttonColors()
                        } else {
                            ButtonDefaults.outlinedButtonColors()
                        },
                ) {
                    Text(
                        text = if (plan.isHighlighted) "Get Started" else "Select Plan",
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
    }
}
