package gadget.basic.ui.dsl

import android.content.Context
import android.view.ViewGroup
import androidx.camera.view.PreviewView
import gadget.basic.annotation.DSLScope

inline fun PreviewView(
    context: Context,
    params: (@DSLScope PreviewView).() -> ViewLayoutParams = { marginLayoutParams() },
    lambda: (@DSLScope PreviewView).() -> Unit = {},
): PreviewView = PreviewView(context).scope(params, lambda)

inline fun <P : ViewGroup> P.PreviewView(
    params: (@DSLScope PreviewView).() -> ViewLayoutParams = { marginLayoutParams() },
    lambda: (@DSLScope PreviewView).() -> Unit = {},
): PreviewView = scope(PreviewView(context), params, lambda)
