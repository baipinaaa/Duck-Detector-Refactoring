/*
 * Copyright 2026 Duck Apps Contributor
 * If you have any questions, suggestions, or other inquiries, please email Eltavine <me@eltavine.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.eltavine.duckdetector.startup.legal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import com.eltavine.duckdetector.core.ui.components.DuckIcon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.eltavine.duckdetector.R
import com.eltavine.duckdetector.core.designsystem.theme.DuckTheme
import com.eltavine.duckdetector.core.designsystem.theme.DuckTypography
import io.github.xiaotong6666.uihelper.adaptive.AdaptiveContent
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card as MiuixCard
import top.yukonga.miuix.kmp.basic.Text as MiuixText
import top.yukonga.miuix.kmp.theme.MiuixTheme

/** The risk reminder above the terms; its key sentence is set in bold rather than in a color. */
@Composable
internal fun AgreementRiskBanner() {
    val emphasis = stringResource(R.string.agreement_risk_body_emphasis)
    val template = stringResource(R.string.agreement_risk_body_template, emphasis)
    val emphasisRange = remember(template, emphasis) {
        val start = template.indexOf(emphasis)
        if (start >= 0) start until (start + emphasis.length) else null
    }
    val riskSummary = buildAnnotatedString {
        append(template)
        emphasisRange?.let { range ->
            addStyle(
                style = SpanStyle(fontWeight = FontWeight.Bold),
                start = range.first,
                end = range.last + 1,
            )
        }
    }

    AdaptiveContent(
        miuix = {
            MiuixCard(
                modifier = Modifier.fillMaxWidth(),
                insideMargin = androidx.compose.foundation.layout.PaddingValues(0.dp),
            ) {
                BasicComponent(
                    title = stringResource(R.string.agreement_risk_title),
                    startAction = {
                        DuckIcon(
                            imageVector = Icons.Outlined.Warning,
                            contentDescription = null,
                            modifier = Modifier.padding(end = 12.dp).size(22.dp),
                            tint = MiuixTheme.colorScheme.onSurface,
                        )
                    },
                )
                MiuixText(
                    text = riskSummary,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                    style = MiuixTheme.textStyles.paragraph,
                    color = MiuixTheme.colorScheme.onSurface,
                )
            }
        },
        material = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = DuckTheme.palette.groupedSurface,
                        shape = MaterialTheme.shapes.large,
                    )
                    .padding(horizontal = 18.dp, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    DuckIcon(
                        imageVector = Icons.Outlined.Warning,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(22.dp),
                    )
                    Text(
                        text = stringResource(R.string.agreement_risk_title),
                        style = DuckTypography.Headline,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
                Text(
                    text = riskSummary,
                    style = DuckTypography.Callout,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = DuckTypography.Callout.lineHeight * 1.25,
                )
            }
        },
    )
}
