package com.bh.websitenavigator_compose

import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bh.websitenavigator_compose.ui.theme.WebSiteNavigatorComposeTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WebPageViewer(viewModel: WebViewModel) {
    val urls by viewModel.urls.collectAsState() // Collect the list of URLs from the ViewModel
    val pagerState =
        rememberPagerState(pageCount = { urls.size }) // Remember the pager state with the number of pages

    LaunchedEffect(Unit) {
        viewModel.fetchUrls() // Fetch the URLs when the composable is launched
    }

    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        state = pagerState,
    ) { page ->
        val url = urls[page] // Get the URL for the current page
        var isLoading by remember { mutableStateOf(true) } // State to hold the loading state
        var currentUrl by remember { mutableStateOf(url) } // State to hold the current URL
        val screenshots =
            remember { mutableMapOf<String, Bitmap>() } // Map to hold the screenshots of visited pages

        Column(modifier = Modifier.fillMaxSize()) {
            OutlinedTextField(
                value = currentUrl, // Display the current URL
                onValueChange = {}, // No-op for value change
                label = { androidx.compose.material3.Text("URL Address") }, // Label for the text field
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                readOnly = true // Make the text field read-only
            )

            Box(modifier = Modifier.fillMaxSize()) {
                if (screenshots.containsKey(url)) {
                    Image(
                        bitmap = screenshots[url]!!.asImageBitmap(), // Display the screenshot if available
                        contentDescription = null, modifier = Modifier.fillMaxSize()
                    )
                } else {
                    AndroidView(
                        factory = { context ->
                            WebView(context).apply {
                                webViewClient = object : WebViewClient() {
                                    override fun onPageStarted(
                                        view: WebView?, url: String?, favicon: Bitmap?
                                    ) {
                                        super.onPageStarted(view, url, favicon)
                                        currentUrl = url ?: "" // Update the current URL
                                        isLoading = true // Set loading state to true
                                    }

                                    override fun onPageFinished(view: WebView?, url: String?) {
                                        super.onPageFinished(view, url)
                                        isLoading = false // Set loading state to false
                                        view?.let {
                                            val scale = 0.25f // Scale down the screenshot to 25%
                                            val width = (it.width * scale).toInt()
                                            val height = (it.height * scale).toInt()
                                            if (width > 0 && height > 0) {
                                                val bitmap = Bitmap.createBitmap(
                                                    width, height, Bitmap.Config.ARGB_8888
                                                )
                                                val canvas = android.graphics.Canvas(bitmap)
                                                canvas.scale(scale, scale)
                                                it.draw(canvas) // Draw the WebView content on the canvas
                                                screenshots[url!!] = bitmap // Save the screenshot
                                            }
                                        }
                                    }
                                }

                                println("Loading index $page, URL: $url")
                                loadUrl(url) // Load the URL in the WebView
                            }
                        }, modifier = Modifier.fillMaxSize()
                    )
                }

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.Center) // Show a progress indicator while loading
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewWebPageViewer() {
    WebSiteNavigatorComposeTheme {
        WebPageViewer(WebViewModel()) // Preview the WebPageViewer composable
    }
}