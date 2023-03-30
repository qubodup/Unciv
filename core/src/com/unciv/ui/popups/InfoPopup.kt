package com.unciv.ui.popups

import com.badlogic.gdx.scenes.scene2d.Stage
import com.unciv.logic.UncivShowableException
import com.unciv.utils.concurrency.Concurrency

/** Variant of [Popup] with one label and a cancel button
 * @param stageToShowOn Parent [Stage], see [Popup.stageToShowOn]
 * @param texts The texts for the popup, as separated good-sized labels
 * @param action A lambda to execute when the button is pressed, after closing the popup
 */
open class InfoPopup(
    stageToShowOn: Stage,
    vararg texts: String,
    action: (() -> Unit)? = null
) : Popup(stageToShowOn) {

    init {
        for (element in texts) {
            addGoodSizedLabel(element).row()
        }
        addCloseButton(action = action).row()
        open()
    }

    companion object {

        /**
         * Wrap the execution of a coroutine to display an [InfoPopup] when a [UncivShowableException] occurs
         */
        suspend fun <T> wrap(stage: Stage, vararg texts: String, function: suspend () -> T): T? {
            try {
                return function()
            } catch (e: UncivShowableException) {
                Concurrency.runOnGLThread {
                    InfoPopup(stage, *texts, e.localizedMessage)
                }
            }
            return null
        }

    }

}
